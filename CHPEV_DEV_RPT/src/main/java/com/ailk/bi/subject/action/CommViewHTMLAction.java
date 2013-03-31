package com.ailk.bi.subject.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;

import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;

/**
 * 
 * 
 * 
 * 
 * 
 */
public class CommViewHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		System.out.println("进入do函数---------------------:");
	//初始化qryStruct
		HttpSession session = request.getSession();
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (qryStruct == null)
			qryStruct = new ReportQryStruct();
		else {
			qryStruct = new ReportQryStruct();
			try{//得到页面中保存的qryStruct值
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			}catch (AppException ex){
				throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
			}
		}
	//校验optype
		String opType = StringB.NulltoBlank(request.getParameter("optype"));
		if (opType == null || "".equals(opType))
			throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
	
		
	//权限设置,得到权限级别	
	//	UserCtlRegionStruct user=(UserCtlRegionStruct)session.getAttribute("ATTR_C_UserCtlStruct");
		//user.setCtl_lvl("0");
		//user.setCtl_metro_str("19");
		//user.setCtl_city_str("199");
	//	String ctl_lvl=StringB.NulltoBlank(user.getCtl_lvl());
		//qryStruct.ctl_lvl=ctl_lvl;

		//区域总览   总部、省份
//		if(opType.equals("eval_by_area"))
//			if(!ctl_lvl.equals("0") && !ctl_lvl.equals("1"))
//				throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
//		//级别总览  \渠道明细 \ 渠道级别     总部、省份、地市
//		if(opType.equals("eval_by_level") || opType.equals("eval_by_level") || opType.equals("chnl_detail") || opType.equals("self_chnl_levl"))
//			if(!ctl_lvl.equals("0") && !ctl_lvl.equals("1") && !ctl_lvl.equals("2"))
//				throw new HTMLActionException(session,HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
		
		
	//得到员工归属，省\市.
//		if(ctl_lvl.equals("1") || ctl_lvl.equals("2")){
//			String ctl_metro_str=StringB.NulltoBlank(user.getCtl_metro_str());
//			if(!ctl_metro_str.equals(""))
//				qryStruct.metro_id = ctl_metro_str;
//			String ctl_city_str=StringB.NulltoBlank(user.getCtl_city_str());
//			if(!ctl_city_str.equals(""))
//				qryStruct.city_id = ctl_city_str;
//		}
	
		//得到跳转页面时附带的参数
		if (opType.equals("chnl_detail")) {
			
			String linkType = StringB.NulltoBlank(request.getParameter("table_id"));
			
			if(linkType!=null && !linkType.equals("")){
				String acct_month = StringB.NulltoBlank(request.getParameter("ACCT_MONTH"));
				String prov_code = StringB.NulltoBlank(request.getParameter("PROVINCE_CODE"));
				String prov_id = StringB.NulltoBlank(request.getParameter("province_code"));
				String sort_id = StringB.NulltoBlank(request.getParameter("sort_id"));
				String city_code = StringB.NulltoBlank(request.getParameter("CITY_CODE"));
				String city_id = StringB.NulltoBlank(request.getParameter("city_code"));
				String chnl_type = StringB.NulltoBlank(request.getParameter("CHANAL_TYPE"));
				String chnl_level = StringB.NulltoBlank(request.getParameter("chanal_level"));
				String msu_fld = StringB.NulltoBlank(request.getParameter("msu_fld"));
				String msu_name = StringB.NulltoBlank(request.getParameter("msu_name"));
			//账期	
				if (acct_month != null && !"".equals(acct_month))
					qryStruct.gather_month = acct_month;
			//省份
				if(prov_code != null && !"".equals(prov_code))
					qryStruct.metro_id=prov_code;
				if (prov_id != null && !"".equals(prov_id))
					qryStruct.metro_id = prov_id;
				if (sort_id != null && !"".equals(sort_id)) {
					//sort_id 区域  
					if(sort_id.equals("-1")){//-1:南方
						qryStruct.area_id="0";
					} else if(sort_id.equals("-2")){//-2:北方
						qryStruct.area_id="1";
					} else if(sort_id.equals("-3")){//-3:全国
						qryStruct.area_id="";
					} else {
						qryStruct.metro_id=ChnlCommonTools.sortIdToProvCode(sort_id);
						System.out.println("sort_id:"+sort_id+";TO->prov_code:"+qryStruct.metro_id);
					}
				}	
			//地市
				if(city_code != null && !"".equals(city_code)) 
					qryStruct.city_id=city_code;
				if (city_id != null && !"".equals(city_id))
					qryStruct.city_id = city_id;
			//渠道类型
				if(chnl_type != null && !"".equals(chnl_type))
					qryStruct.tactic_type=chnl_type;
			//门店级别
				if(chnl_level != null && !"".equals(chnl_level))
					qryStruct.dim1=chnl_level;
				try {
					msu_name = URLDecoder.decode(msu_name, "UTF-8").trim();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					msu_name="";
				}
				if((msu_fld.indexOf("CHANAL_LEVEL") != -1) && ("A".equals(msu_name)||"B".equals(msu_name)||"C".equals(msu_name)||"D".equals(msu_name)))
					qryStruct.dim1=msu_name;
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
	//默认账期.
		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) 
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
		
		if (qryStruct.rpt_date == null || "".equals(qryStruct.rpt_date))
			qryStruct.rpt_date = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
	//区域标识
		if ("".equals(qryStruct.area_id) && !"".equals(qryStruct.metro_id))
			qryStruct.area_id=ChnlCommonTools.provCodeToAreaId(qryStruct.metro_id);
				
		qryStruct.optype = opType;
		qryStruct.chnl_id = StringB.NulltoBlank(request.getParameter("chnl_id"));
		qryStruct.isMrg = StringB.NulltoBlank(request.getParameter("isMrg"));
		qryStruct.chnl_name="";
		System.out.println("传入的chnl_name:"+request.getParameter("chnl_name"));
		
		try {
		    System.out.println("------------1------------"+URLDecoder.decode(request.getParameter("chnl_name"),"utf-8"));
            qryStruct.chnl_name = StringB.NulltoBlank(new String(URLDecoder.decode(request.getParameter("chnl_name"),"utf-8")));
            System.out.println("------------2------------");
        } catch (UnsupportedEncodingException e) {
            System.out.println("----------UnsupportedEncodingExceptionerrer-------------");
        }
		
		System.out.println("OPTYOE :"+opType);
		System.out.println("ACCT_MONTH :"+qryStruct.gather_month);
		System.out.println("AREA_ID :"+qryStruct.area_id);
		System.out.println("PROVINCE_CODE :"+qryStruct.metro_id);
		System.out.println("chnl_id :"+qryStruct.chnl_id);
		System.out.println("CITY_CODE :"+qryStruct.city_id);
		System.out.println("chnl_name :"+qryStruct.chnl_name);
		System.out.println("isMrg :"+qryStruct.isMrg);
		
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, opType + ".screen");
	}

}
