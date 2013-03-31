package com.ailk.bi.subject.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;

import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * 效能评估-社会渠道--渠道门店评价
 * @author lipan
 * @date 2013-1-16 上午11:22:18 
 * @version 1.0
 */
public class SocietyChnlHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		
		//判断是否为登录用户
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}
		String opType = StringB.NulltoBlank(request.getParameter("optype"));
		//月份
		String acct_month = StringB.NulltoBlank(request.getParameter("ACCT_MONTH"));
		//省编码
		String prov_id = StringB.NulltoBlank(request.getParameter("province_code"));
		//排序id
		String sort_id = StringB.NulltoBlank(request.getParameter("sort_id"));
		//地市编码
		String city_id = StringB.NulltoBlank(request.getParameter("city_code"));
		String city_code = StringB.NulltoBlank(request.getParameter("CITY_CODE"));

		//
		String linkType = StringB.NulltoBlank(request.getParameter("table_id"));
		HttpSession session = request.getSession();
		
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
		
		//用户
		UserCtlRegionStruct user=(UserCtlRegionStruct)session.getAttribute("ATTR_C_UserCtlStruct");
		//用户类型
		String ctl_lvl=StringB.NulltoBlank(user.getCtl_lvl());
		if(!"".equals(ctl_lvl))
		{
//			ctl_lvl = "1";
			qryStruct.ctl_lvl=ctl_lvl;
//			user.setCtl_metro_str("36");
//			user.setCtl_city_str("362");
			
			//区域总览   总部、省份
			if(opType.equals("sct_eval_by_area"))
				if(!ctl_lvl.equals("0") && !ctl_lvl.equals("1"))
					throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
			
			//总部
			if("0".equals(ctl_lvl))
			{

				//省
			}else if("1".equals(ctl_lvl))
			{
				List list=new ArrayList();
				String geo_flag="";
				String ctl_metro_str=StringB.NulltoBlank(user.getCtl_metro_str());
				if(!ctl_metro_str.equals("")){
					qryStruct.metro_id=ctl_metro_str;
					String sql_str="select distinct(a.geo_flag) from ST_DIM_AREA_PROVINCE a where a.province_code='"+ctl_metro_str+"' ";
					try {
						String[][] flags=WebDBUtil.execQuery(sql_str,list);
						if(flags!=null&&flags.length>0){
							geo_flag=flags[0][0];
							qryStruct.attach_region=geo_flag;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			//地市
			}else if("2".equals(ctl_lvl))
			{
				String ctl_metro_str=StringB.NulltoBlank(user.getCtl_metro_str());
				if(!ctl_metro_str.equals(""))
				{
					qryStruct.metro_id = ctl_metro_str;
				}
				String ctl_city_str=StringB.NulltoBlank(user.getCtl_city_str());
				if(!ctl_city_str.equals(""))
				{
					qryStruct.city_id = ctl_city_str;
				}
			//其他
			}else
			{
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
			}
		}
		
		
		if (acct_month != null && !"".equals(acct_month)) {
			qryStruct.gather_month = acct_month;
		}
		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {
			
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
		}
		if (qryStruct.dim4 == null || "".equals(qryStruct.dim4)) {
			
			qryStruct.dim4 = "D0501";
		}
		if (prov_id != null && !"".equals(prov_id)) {
			qryStruct.metro_id = prov_id;
		}
//		if (sort_id != null && !"".equals(sort_id)) {
//			if(qryStruct.metro_id == null || "".equals(qryStruct.metro_id)){
//				qryStruct.metro_id=ChnlCommonTools.sortIdToProvCode(sort_id);
//				System.out.println("sort_id:"+sort_id+";TO->prov_code:"+qryStruct.metro_id);
//			}
//		}
		if (city_id != null && !"".equals(city_id))
		{
			qryStruct.city_id = city_id;
		}
		if (city_code != null && !"".equals(city_code)) 
		{
			qryStruct.city_id = city_code;
		}
		

		if (opType.equals("city_detail")){
			String geo_flag = StringB.NulltoBlank(request.getParameter("GEO_FLAG"));
			String chnl_type = StringB.NulltoBlank(request.getParameter("CHANNEL_TYPE"));
			
			if(geo_flag != null && !"".equals(geo_flag)){
				qryStruct.attach_region = geo_flag;
			}
			if(chnl_type != null && !"".equals(chnl_type)) {
				qryStruct.tactic_type=chnl_type;
			}
		}
		if (opType.equals("sct_chnl_detail") || opType.equals("sct_chnl_store_lvl")) {
			if(linkType!=null && !linkType.equals("")){
				String geo_flag = StringB.NulltoBlank(request.getParameter("GEO_FLAG"));
				String prov_code = StringB.NulltoBlank(request.getParameter("PROVINCE_CODE"));
				String chnl_type = StringB.NulltoBlank(request.getParameter("CHANAL_TYPE"));
				String chnl_level = StringB.NulltoBlank(request.getParameter("chanal_level"));
				String msu_fld = StringB.NulltoBlank(request.getParameter("msu_fld"));
				String msu_name = StringB.NulltoBlank(request.getParameter("msu_name"));
			//查询
				qryStruct.client_type="0";
			//区域
				if(geo_flag != null && !"".equals(geo_flag)) {
					qryStruct.attach_region=geo_flag;
				}
				if (sort_id != null && !"".equals(sort_id)) {
					//sort_id 区域  
					if(sort_id.equals("-1")){//-1:南方
						qryStruct.attach_region="0";
					} else if(sort_id.equals("-2")){//-2:北方
						qryStruct.attach_region="1";
					} else if(sort_id.equals("-3")){//-3:全国
						qryStruct.attach_region="";
					} else {
						qryStruct.metro_id=ChnlCommonTools.sortIdToProvCode(sort_id);
						System.out.println("sort_id:"+sort_id+";TO->prov_code:"+qryStruct.metro_id);
					}
				}	
			//省份
				if(prov_code != null && !"".equals(prov_code)) {
					qryStruct.metro_id=prov_code;
				}
			//渠道类型
				if(chnl_type != null && !"".equals(chnl_type)) {
					qryStruct.tactic_type=chnl_type;
				}
			//渠道级别
				if(chnl_level != null && !"".equals(chnl_level)) {
					qryStruct.dim1=chnl_level;
				}
				if(("A".equals(msu_name)||"B".equals(msu_name)||"C".equals(msu_name)||"S".equals(msu_name)))
				{
					qryStruct.dim1=msu_name;
				}
				//得分等级
				if(msu_fld.indexOf("level_ex_num") != -1) {
					qryStruct.dim2="10";
					if(msu_fld.indexOf("level_bad_num") != -1)
						qryStruct.dim2="";
				} else if(msu_fld.indexOf("level_good_num") != -1) 
					qryStruct.dim2="11";
				else if(msu_fld.indexOf("level_mid_num") != -1) 
					qryStruct.dim2="12";
				else if(msu_fld.indexOf("level_bad_num") != -1)
					qryStruct.dim2="13";
				else
					qryStruct.dim2="";
			}
		}
		
		//
		if(qryStruct.attach_region ==null || "".equals(qryStruct.attach_region))
		{
			if(qryStruct.metro_id!=null && !"".equals(qryStruct.metro_id))
			{
				List list=new ArrayList();
				String geo_flag="";
				String sql_str="select distinct(a.geo_flag) from ST_DIM_AREA_PROVINCE a where a.province_code='"+qryStruct.metro_id+"' ";
				try {
					String[][] flags=WebDBUtil.execQuery(sql_str,list);
					if(flags!=null&&flags.length>0){
						geo_flag=flags[0][0];
						qryStruct.attach_region=geo_flag;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		qryStruct.optype = opType;
		System.out.println(opType);
		System.out.println(qryStruct.gather_month);
		System.out.println(qryStruct.metro_id);
		System.out.println(qryStruct.city_code);
		
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, opType + ".screen");
	}
}
