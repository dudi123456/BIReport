package com.ailk.bi.subject.action;

import java.math.BigDecimal;
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
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

public class RegionalHTMLAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		// 获取页面screen标示
		String optype = StringB.NulltoBlank(request.getParameter("optype"));

		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		
		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = null;
		qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();			
		} else {			
			qryStruct = new ReportQryStruct();
			try {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
			}
		}
		List list=new ArrayList();
		String geo_flag="";
		UserCtlRegionStruct user=(UserCtlRegionStruct)session.getAttribute("ATTR_C_UserCtlStruct");
		//user.setCtl_lvl("1");
		//user.setCtl_metro_str("19");
		//user.setCtl_city_str("191");
		String ctl_lvl=StringB.NulltoBlank(user.getCtl_lvl());
		qryStruct.ctl_lvl=ctl_lvl;
		
		//设置默认账期为当前月的前一个月
		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {				
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
		}
		if (qryStruct.rpt_date == null || "".equals(qryStruct.rpt_date)){
			qryStruct.rpt_date = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
		}
		
		if(optype.equals("prov") || optype.equals("city")){
			if(qryStruct.right_attach_region == null || "".equals(qryStruct.right_attach_region)){
				if(optype.equals("prov") && ctl_lvl.equals("1")){
					String ctl_metro_str=StringB.NulltoBlank(user.getCtl_metro_str());
					if(ctl_metro_str!=null && !ctl_metro_str.equals("")){
						qryStruct.right_metro_id=ctl_metro_str;
						String sql_str="select distinct(a.geo_flag) from ST_DIM_AREA_PROVINCE a where a.province_code='"+ctl_metro_str+"' ";
						try {
							String[][] flags=WebDBUtil.execQuery(sql_str,list);
							if(flags!=null&&flags.length>0){
								geo_flag=flags[0][0];
								qryStruct.right_attach_region=geo_flag;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}					
				}else{
					qryStruct.right_attach_region = "1";
				}
			}
			if(qryStruct.classifySelect == null || "".equals(qryStruct.classifySelect)){
				qryStruct.classifySelect = "";
			}
			
			if(optype.equals("prov")){					
				if(ctl_lvl.equals("0") || ctl_lvl.equals("1")){
					if(ctl_lvl.equals("1")){
						String ctl_metro_str=StringB.NulltoBlank(user.getCtl_metro_str());
						if(ctl_metro_str!=null && !ctl_metro_str.equals("")){
							qryStruct.right_metro_id=ctl_metro_str;
						}
					}
				}else{
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
				}
			}
			if(optype.equals("city")){							
				if(ctl_lvl.equals("1") || ctl_lvl.equals("2")){
					String ctl_metro_str=StringB.NulltoBlank(user.getCtl_metro_str());
					if(ctl_metro_str!=null && !ctl_metro_str.equals("")){
						qryStruct.province_code=ctl_metro_str;
					}
					String ctl_city_str=StringB.NulltoBlank(user.getCtl_city_str());
					if(ctl_city_str!=null && !ctl_city_str.equals("")){
						qryStruct.right_city_id=ctl_city_str;
					}
				}else{
					throw new HTMLActionException(session,
							HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
				}
			}
		}
		
		//String sql="select distinct(a.geo_flag) from ST_DIM_AREA_PROVINCE a,dim_pub_city b where a.province_code=b.province_code ";
		if(optype.equals("prov_his")){
			String province_code = StringB.NulltoBlank(request.getParameter("province_code"));	
			if(province_code!=null && !province_code.equals("")){
				//String province_code = ChnlCommonTools.sortIdToProvCode(sort_id);
				qryStruct.province_code=province_code;
				//sql+=" and a.PROVINCE_CODE='"+province_code+"' ";
			}									
		}else if(optype.equals("city_his")){
			String province_code = StringB.NulltoBlank(request.getParameter("PROVINCE_CODE"));	
			if(province_code!=null && !province_code.equals("")){
				qryStruct.province_code=province_code;
			}
			String city_code = StringB.NulltoBlank(request.getParameter("city_code"));
			if(city_code!=null && !city_code.equals("")){
				qryStruct.city_code=city_code;
				//sql+=" and b.CITY_CODE='"+city_code+"' ";
			}
		}
		
		if(optype.equals("prov_his") || optype.equals("city_his")){
			String acct_month = StringB.NulltoBlank(request.getParameter("ACCT_MONTH"));
			if(acct_month!=null && !acct_month.equals("")){
				qryStruct.date_e=acct_month;
			}
		}
		
		//lipan 添加评分指标及权重查询操作 --begin--
		if("mark_right".equals(optype))
		{
			//-----------1.获得南北方标识
			//获取页面南北方标识  1:北方；0:南方；2：總部
			String geo_flag_page = StringB.NulltoBlank(request.getParameter("geo_flag"));
			
			//參數中没有南北方标识，从session中判断
			if("".equals(geo_flag_page))
			{
				//控制区域的所在等级-总部
				if ("0".equals(ctl_lvl))
				{
					//总部
					geo_flag_page = "2";
					
				}else
				{

					//sqlString
					String query_geo_sql = "";
					
					//省
					if("1".equals(ctl_lvl))
					{
						//根据省份查份code询南北放标识
						String provinceCode = StringB.NulltoBlank(user.getCtl_metro_str());
						
						if(!"".equals(provinceCode))
						{
							query_geo_sql = "select distinct(geo_flag) from st_dim_area_province t where t.province_code='"+provinceCode+"'";
						}
						
					//地市
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
			
			//-----------2.查询区域渠道评价映射与公式描述定义表
			//sqlString
			String query_reflect_sql = "select kpi_name,kpi_up_id,kpi_up_name,kpi_weight,kpi_weight_south,kpi_sort,sort_id,cal_expression,cal_explation" +
					" from st_area_segment_reflect order by kpi_up_id asc,sort_id asc";
			
			//
			try
			{
				//查询结果数据
				List mapList = new ArrayList();
				
				//区域渠道评价映射与公式描述定义表  Map集合
				mapList = WebDBUtil.execQryArrayMapSys2(query_reflect_sql, null);
				
				//S
				StringBuffer trs = new StringBuffer(1024);
				
				if(mapList.size()>0)
				{
					
					//-----------3.对查询结果数据mapList进行分组
					LinkedHashMap<String,List<Map>> groupList = new LinkedHashMap();
					
					for(int i=0; i<mapList.size(); i++)
					{
						
						Map mapi = (Map)mapList.get(i);
						
						//-----过滤权重为0的数据
						if("1".equals(geo_flag_page))//北方
						{
							//将指标上级ID作为组标识KPI_WEIGHT  
							Double KPI_WEIGHT = Double.parseDouble((String)mapi.get("KPI_WEIGHT"));
							if(0 == KPI_WEIGHT)
							{
								continue;
							}
						}else if("0".equals(geo_flag_page))//南方
						{
							Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)mapi.get("KPI_WEIGHT_SOUTH"));
							if(0 == KPI_WEIGHT_SOUTH)
							{
								continue;
							}
						}else if("2".equals(geo_flag_page))//总部
						{
							Double KPI_WEIGHT = Double.parseDouble((String)mapi.get("KPI_WEIGHT"));
							Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)mapi.get("KPI_WEIGHT_SOUTH"));
							if(0 == KPI_WEIGHT && 0 == KPI_WEIGHT_SOUTH)
							{
								continue;
							}
						}
						
						
						//将指标上级ID作为组标识
						String kpi_up_id_i = (String)mapi.get("KPI_UP_ID");
						
						//该组已存在
						if(groupList.containsKey(kpi_up_id_i))
						{
							//添加进组中
							groupList.get(kpi_up_id_i).add(mapi);
						
						//该组不存在
						}else
						{
							//新建一个List<Map>
							List<Map> temp = new ArrayList();
							
							//添加当前map
							temp.add(mapi);
							
							//加入组中
							groupList.put(kpi_up_id_i, temp);
						}
					}
					
					//-----------4.分组后对拼接页面需要的内容
					
					//用一个map来标识是否已经在tr中加入了指标上级td
					Map<String,String> hasAddUp = new HashedMap();
					
					//指标上级id
					String kpi_up_id = "";
					
					//拼接表头tr
					trs.append("<tr><td class='tdrow3' colspan='2'><strong>区域评价指标</strong></td>");
					
					//南北方标识  2：總部
					if("2".equals(geo_flag_page))
					{
						trs.append("<td class='tdrow3'><strong>北方权重</strong></td>");
						trs.append("<td class='tdrow3'><strong>南方权重</strong></td>");
						
					//1:北方/0:南方；
					}else
					{
						trs.append("<td class='tdrow3'><strong>权重</strong></td>");
						
					}
					trs.append("<td class='tdrow3'><strong>指标计算公式</strong></td><td class='tdrow3'><strong>指标口径解释</strong></td>");
					trs.append("</tr>");
					
					//拼接数据tr
					for(Entry<String, List<Map>>  temp : groupList.entrySet())
					{
						//
						List<Map> tempList =  temp.getValue();
						
						//跨行数
						int rowspanNum = tempList.size();
						
						//指标上级id (组id)
						kpi_up_id = temp.getKey();
						
						//tr
						for(Map tempMap : tempList)
						{
							
							//-----过滤权重为0的数据
							if("1".equals(geo_flag_page))//北方
							{
								//将指标上级ID作为组标识KPI_WEIGHT  
								Double KPI_WEIGHT = Double.parseDouble((String)tempMap.get("KPI_WEIGHT"));
								if(0 == KPI_WEIGHT)
								{
									continue;
								}
							}else if("0".equals(geo_flag_page))//南方
							{
								Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH"));
								if(0 == KPI_WEIGHT_SOUTH)
								{
									continue;
								}
							}else if("2".equals(geo_flag_page))//总部
							{
								Double KPI_WEIGHT = Double.parseDouble((String)tempMap.get("KPI_WEIGHT"));
								Double KPI_WEIGHT_SOUTH = Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH"));
								if(0 == KPI_WEIGHT && 0 == KPI_WEIGHT_SOUTH)
								{
									continue;
								}
							}
							
							trs.append("<tr>");
							
							//---添加指标上级td
							if(!hasAddUp.containsKey(kpi_up_id))
							{
								//已添加指标上级td的标识
								hasAddUp.put(kpi_up_id, "has");
								
								//指标上级名称
								String kpi_up_name = String.valueOf(tempList.get(0).get("KPI_UP_NAME")).trim();
								
								//计算子权重的总和
								String sumD = "0";
								
								for(Map map : tempList)
								{
									BigDecimal b1 = new BigDecimal((String)map.get("KPI_WEIGHT"));
									BigDecimal b2 = new BigDecimal(sumD);
									sumD = String.valueOf(b2.add(b1).doubleValue());
								}
								
								//指标上级权重 - 子权重的总和
								String kpi_up_weight = getPerStr(Double.parseDouble(sumD),0);
								
								//指标上级td
								trs.append("<td class='tdrow2' rowspan='"+rowspanNum+"'>"+kpi_up_name+"（"+kpi_up_weight+"）</td>");
								
							}
							
							//---添加其他td
							//指标名称
							trs.append("<td class='tdrow'>"+(String)tempMap.get("KPI_NAME")+"</td>");
							
							//权重
							//南北方标识  2：總部
							if("2".equals(geo_flag_page))
							{
								trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT")),0)
										+"&nbsp;</td>");
								trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH")),0)
										+"&nbsp;</td>");
								
							//1:北方
							}else if("1".equals(geo_flag_page))
							{
								trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT")),0)
										+"&nbsp;</td>");
							
							//0:南方；
							}else if("0".equals(geo_flag_page))
							{
								trs.append("<td align='left' class='tdrow'>"+getPerStr(Double.parseDouble((String)tempMap.get("KPI_WEIGHT_SOUTH")),0)
										+"&nbsp;</td>");
							}
							
							//指标计算公式
							trs.append("<td align='left' class='tdrow'>"+(String)tempMap.get("CAL_EXPRESSION")+"</td>");
							
							//指标口径解释
							trs.append("<td align='left' class='tdrow'>"+(String)tempMap.get("CAL_EXPLATION")+"</td>");
							
							trs.append("</tr>");
						}
					}
				}
				//将评分指标及权重结果放入session中
				session.setAttribute(WebKeys.ATTR_ST_AREA_SEGMENT_REFLECT, trs.toString().replaceAll("\\r","").replaceAll("\\n","").replaceAll("\"", "&quot;"));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		//lipan 添加评分指标及权重查询操作  --end --
		
		
		qryStruct.optype = optype;
		// 把结果集存入会话
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+"_appraisal.screen");
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
	public static void main(String[] args)
	{
		System.out.println(Double.parseDouble("0.000")==0);
	}
}
