package com.ailk.bi.subject.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
/**
 * 评分指标及权重查询
 * @ClassName: WeightAction 
 * @Description: 评分指标及权重查询
 * @author lipan lipan3@asiainfo-linkage.com
 * @date 2013-1-23 上午10:43:41 
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WeightAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		
		// 获取页面screen标示
		HttpSession session = request.getSession();
		
		// 业务类型
		String ioid_id0 = StringB.NulltoBlank(request.getParameter("ioid_id0"));

		if (ioid_id0 == null || "".equals(ioid_id0)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		
		//用户
		UserCtlRegionStruct user=(UserCtlRegionStruct)session.getAttribute("ATTR_C_UserCtlStruct");
		
//		user.setCtl_lvl("0");
		//user.setCtl_metro_str("19");
		//user.setCtl_city_str("191");
		
		//用户类型
		String ctl_lvl=StringB.NulltoBlank(user.getCtl_lvl());
		
		//-----------1.获得南北方标识
		
		//获取页面南北方标识  1:北方；0:南方；2：總部
		String geo_flag_page = "";
		
		//參數中没有南北方标识，从session中判断
		if("".equals(geo_flag_page))
		{
			//控制区域的所在等级-总部用户
			if ("0".equals(ctl_lvl))
			{
				//总部用户
				geo_flag_page = "2";
				
			}else
			{

				//sqlString
				String query_geo_sql = "";
				
				//省份用户
				if("1".equals(ctl_lvl))
				{
					//根据省份查份code询南北放标识
					String provinceCode = StringB.NulltoBlank(user.getCtl_metro_str());
					
					if(!"".equals(provinceCode))
					{
						query_geo_sql = "select distinct(geo_flag) from st_dim_area_province t where t.province_code='"+provinceCode+"'";
					}
					
				//地市用户
				}else if("2".equals(ctl_lvl))
				{
					//根据地市code查询南北放标识
					String cityCode = StringB.NulltoBlank(user.getCtl_city_str());
					
					if(!"".equals(cityCode))
					{
						query_geo_sql = "select distinct(a.geo_flag) from st_dim_area_province a,dim_pub_city b" +
								" where a.province_code=b.province_code and b.city_code='"+cityCode+"'";
					}
				}
				
				//查询南北方标识
				try 
				{
					List geo_list = new ArrayList();
					String[][] flags = WebDBUtil.execQuery(query_geo_sql , geo_list);
					geo_flag_page = flags[0][0];
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		//未得到出南北方标识
		if("".equals(geo_flag_page))
		{
			throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
		}
		
		//-----------2.查询渠道评价公式描述定义表
		
		//sqlString
		String query_reflect_sql = "select t.kpi_name1, t.kpi_name2, t.kpi_name, t.kpi_weight, t.kpi_weight_south, t.cal_expression, t.cal_explation, " +
				"t.remark from st_soc_segment_expression t where t.ioid_id0 = '"+ioid_id0+"'order by t.sort_id asc ";
		
		//
		try
		{
			//查询结果数据
			List mapList = new ArrayList();
			
			//区域渠道评价映射与公式描述定义表  Map集合
			mapList = WebDBUtil.execQryArrayMapSys2(query_reflect_sql,null);
			
			//S
			StringBuffer trs = new StringBuffer(1024);
			
			if(mapList.size()>0)
			{
				//-----------3.对查询结果数据mapList进行拼接
				
				//是否有二级目录
				boolean hasKpi_name2 = true;
				
				//
				Map mapi = (Map)mapList.get(0);
				//评价指标子分类
				String kpi_name2 = (String)mapi.get("KPI_NAME2");
				if(null==kpi_name2 || kpi_name2.trim() == "")
				{
					//无 评价指标子分类 列
					hasKpi_name2 = false;
				}
				
				//评价指标分类
				LinkedHashMap<String,List<Map>> groupMap1 = groupByName(mapList,"KPI_NAME1",geo_flag_page);
				
				//--拼接表头
				trs.append("<tr><td class='tdrow3' nowrap><strong>评价指标分类</strong></td>");
				
				//存在子分类列
				if(hasKpi_name2)
				{
					trs.append("<td class='tdrow3' nowrap><strong>评价指标子分类</strong></td>");
				}
				
				trs.append("<td class='tdrow3' nowrap><strong>评价指标</strong></td>");
				
				//南北方标识  2：總部
				if("2".equals(geo_flag_page))
				{
					trs.append("<td class='tdrow3' nowrap><strong>北方权重</strong></td>");
					trs.append("<td class='tdrow3' nowrap><strong>南方权重</strong></td>");
					
				//1:北方/0:南方；
				}else if("1".equals(geo_flag_page))
				{
					trs.append("<td class='tdrow3' nowrap><strong>北方权重</strong></td>");
				}else
				{
					trs.append("<td class='tdrow3' nowrap><strong>南方权重</strong></td>");
				}
				trs.append("<td class='tdrow3' nowrap><strong>指标计算公式</strong></td><td class='tdrow3' nowrap><strong>指标口径解释</strong></td><td class='tdrow3'><strong>备注</strong></td>");
				trs.append("</tr>");
				
				//--拼接数据tr
				for(Entry<String, List<Map>>  temp : groupMap1.entrySet())
				{
					//
					List<Map> tempList =  temp.getValue();
					
					//如果有指标子分类
					if(hasKpi_name2)
					{
						//评价指标子分类分组
						LinkedHashMap<String,List<Map>> groupMap2 =  groupByName(tempList,"KPI_NAME2",geo_flag_page);
						
						//
						trs.append(getTrs(temp,groupMap2,geo_flag_page));
						
					//如果没有指标子分类
					}else
					{
						//
						trs.append(getTrs(temp,null,geo_flag_page));
					}
				}
				
				//将评分指标及权重结果放入session中
				session.setAttribute(WebKeys.ATTR_ST_AREA_SEGMENT_REFLECT, trs.toString().replaceAll("\\r","").replaceAll("\\n","").replaceAll("\"", "&quot;"));
				
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		//lipan 添加评分指标及权重查询操作  --end --
		setNextScreen(request , "weight.screen");
	}
	
	/**
	 * 将double转换成%
	 * author lipan
	 * @param double
	 * @param 保留小数位数
	 * @return
	 */
	public String getPerStr(double d ,int n)
	{
		d = d*100;
		if(Math.round(d)-d==0)
		{
			return (int)d+"%";
		}else
		{
			//n保留小数位数
			return d+"%";
		}
	}
	
	/**
	 * 
	 * @Title: groupByName 
	 * @Description: 根据list中的某个值进行分组
	 * @param @param mapList
	 * @param @param groupString
	 * @param @return
	 * @return TreeMap<String,List<Map>>
	 * @throws
	 */
	public LinkedHashMap<String,List<Map>> groupByName(List mapList,String groupString,String geo_flag_page)
	{
		//
		LinkedHashMap<String,List<Map>> groupMap = new LinkedHashMap();
		
		for(int i=0; i<mapList.size(); i++)
		{
			Map mapi = (Map)mapList.get(i);
			
			//-----过滤权重为0的数据
			//南北方标识  2：總部
			if("2".equals(geo_flag_page))
			{
				Double KPI_WEIGHT = Double.parseDouble((String)mapi.get("KPI_WEIGHT"));
				Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)mapi.get("KPI_WEIGHT_SOUTH"));
				if(0 == KPI_WEIGHT && 0 == KPI_WEIGHT_SOUTH)
				{
					continue;
				}
				
			//1:北方
			}else if("1".equals(geo_flag_page))
			{
				Double KPI_WEIGHT = Double.parseDouble((String)mapi.get("KPI_WEIGHT"));
				if(0 == KPI_WEIGHT)
				{
					continue;
				}
				
			//0:南方；
			}else if("0".equals(geo_flag_page))
			{
				Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)mapi.get("KPI_WEIGHT_SOUTH"));
				if(0 == KPI_WEIGHT_SOUTH)
				{
					continue;
				}
			}
			
			//评价指标分类
			String kpi_name1 = (String)mapi.get(groupString);
			
			//该组已存在
			if(groupMap.containsKey(kpi_name1))
			{
				//添加进组中
				groupMap.get(kpi_name1).add(mapi);
			
			//该组不存在
			}else
			{
				//新建一个List<Map>
				List<Map> temp = new ArrayList();
				
				//添加当前map
				temp.add(mapi);
				
				//加入组中
				groupMap.put(kpi_name1, temp);
			}
		}
		return groupMap;
	}
	
	/**
	 * 
	 * @Title: getTrs 
	 * @Description: 拼接tr
	 * @param @param temp
	 * @param @param groupMap2
	 * @param @param geo_flag_page
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getTrs(Entry<String, List<Map>> temp,LinkedHashMap<String,List<Map>> groupMap2,String geo_flag_page)
	{
		//
		List<Map> tempList =  temp.getValue();
		
		//评价指标分类 垮行数
		int rowspanNum = tempList.size();
		
		//评价指标分类名称 (组id)
		String kpi_name1 = temp.getKey();
		
		StringBuffer trs = new StringBuffer(1024);
		
		//用一个map来标识是否已经在tr中加入了评价指标分类td
		Map<String,String> hasAddName1 = new HashedMap();
		
		//用一个map来标识是否已经在tr中加入了评价指标子分类td
		Map<String,String> hasAddName2 = new HashedMap();
		
		//tr
		for(Map tempMap : tempList)
		{
			
			//-----过滤权重为0的数据
			//南北方标识  2：總部
			if("2".equals(geo_flag_page))
			{
				Double KPI_WEIGHT = Double.parseDouble((String)tempMap.get("KPI_WEIGHT"));
				Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH"));
				if(0 == KPI_WEIGHT && 0 == KPI_WEIGHT_SOUTH)
				{
					continue;
				}
				
			//1:北方
			}else if("1".equals(geo_flag_page))
			{
				Double KPI_WEIGHT = Double.parseDouble((String)tempMap.get("KPI_WEIGHT"));
				if(0 == KPI_WEIGHT)
				{
					continue;
				}
				
			//0:南方；
			}else if("0".equals(geo_flag_page))
			{
				Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH"));
				if(0 == KPI_WEIGHT_SOUTH)
				{
					continue;
				}
			}
			
			trs.append("<tr>");
			
			//---添加评价指标分类td
			if(!hasAddName1.containsKey(kpi_name1))
			{
				//已添加评价指标分类td的 标识
				hasAddName1.put(kpi_name1, "has");
				
				//评价指标分类td
				trs.append("<td class='tdrow2' rowspan='"+rowspanNum+"'>"+kpi_name1+"</td>");
				
			}
			
			//---添加评价指标子分类td
			if(null!=groupMap2 && groupMap2.size()>0)
			{

				//评价指标子分类名称 (组id)
				String kpi_name2 = (String)tempMap.get("KPI_NAME2");
				
				//
				List<Map> tempList2 =  groupMap2.get(kpi_name2);
				
				//评价指标子分类 垮行数
				int rowspanNum2 = tempList2.size();
				
				//---添加评价指标分类td
				if(!hasAddName2.containsKey(kpi_name2))
				{
					//已添加评价指标分类td的 标识
					hasAddName2.put(kpi_name2, "has");
					
					//评价指标分类td
					trs.append("<td class='tdrow2' rowspan='"+rowspanNum2+"'>"+kpi_name2+"</td>");
				}
			}
			
			//---添加其他td
			//评价指标名称
			trs.append("<td class='tdrow'>"+(String)tempMap.get("KPI_NAME")+"</td>");
			
			//权重
			//南北方标识  2：總部
			if("2".equals(geo_flag_page))
			{
				trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT")),0)
						+"</td>");
				trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH")),0)
						+"</td>");
				
			//1:北方
			}else if("1".equals(geo_flag_page))
			{
				trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT")),0)
						+"</td>");
			
			//0:南方；
			}else if("0".equals(geo_flag_page))
			{
				trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH")),0)
						+"</td>");
			}
			
			//指标计算公式
			trs.append("<td align='left' class='tdrow'>"+(String)tempMap.get("CAL_EXPRESSION")+"</td>");
			
			//指标口径解释
			trs.append("<td align='left' class='tdrow'>"+(String)tempMap.get("CAL_EXPLATION")+"</td>");
			
			//备注
			trs.append("<td align='left' class='tdrow'>"+(String)tempMap.get("REMARK")+"</td>");
			
			trs.append("</tr>");
		}
		
		return trs.toString();
	}
}
