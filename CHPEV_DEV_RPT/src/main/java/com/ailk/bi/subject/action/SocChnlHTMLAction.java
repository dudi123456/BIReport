package com.ailk.bi.subject.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class SocChnlHTMLAction  extends HTMLActionSupport{
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doTrans(HttpServletRequest request, HttpServletResponse response) throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
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
		UserCtlRegionStruct user=(UserCtlRegionStruct)session.getAttribute("ATTR_C_UserCtlStruct");
		String ctl_lvl = user.getCtl_lvl();		
		if(ctl_lvl==null || ctl_lvl.equals("") || (!ctl_lvl.equals("1") && !ctl_lvl.equals("2") && !ctl_lvl.equals("0"))){
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
		}
		qryStruct.ctl_lvl=ctl_lvl;
		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {				
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
		}
		String acct_month = StringB.NulltoBlank(request.getParameter("ACCT_MONTH"));
		if (!"".equals(acct_month))
			qryStruct.gather_month = acct_month;
		
		//排名波动分析optype=county波动分析 detail点社会渠道名称 county2评价指标分析 detail2点社会渠道名称
		if(optype.equals("county")){
			if(ctl_lvl.equals("0")){
				qryStruct.target_name = "SOC_CHANNL_COUNTRY_T";
			}else if(ctl_lvl.equals("1")){
				qryStruct.target_name = "SOC_CHANNL_PRO_T";
				qryStruct.province_code = StringB.NulltoBlank(user.getCtl_metro_str());
			}else if(ctl_lvl.equals("2")){
				qryStruct.target_name = "SOC_CHANNL_CITY_T";
				qryStruct.province_code = StringB.NulltoBlank(user.getCtl_metro_str());
				qryStruct.city_code = StringB.NulltoBlank(user.getCtl_city_str());
			}
		}else if(optype.equals("detail")){
			String chnl_id = StringB.NulltoBlank(request.getParameter("chnl_id"));	
			if(ctl_lvl.equals("0")){
				String pro_code = StringB.NulltoBlank(request.getParameter("province_code"));
				if(pro_code.equals("09")){
					qryStruct.channel_id =chnl_id; 
				}else{
					qryStruct.channel_code=chnl_id;
				}
				qryStruct.target_name = "SOC_CHANNL_PRO_T2";
			}else if(ctl_lvl.equals("1")){
				qryStruct.province_code = StringB.NulltoBlank(user.getCtl_metro_str());	
				String gui_code = StringB.NulltoBlank(request.getParameter("gui_code"));
				if(gui_code.equals(qryStruct.province_code)){
					qryStruct.channel_id =chnl_id; 
				}else{
					qryStruct.channel_code=chnl_id;
				}
				qryStruct.target_name = "SOC_CHANNL_CITY_T2";
			}
		}else if(optype.equals("county2")){
			if(ctl_lvl.equals("0")){
				if(qryStruct.dim1.isEmpty() || qryStruct.dim1.equals("") || qryStruct.dim1.equals("D0601"))
				   qryStruct.target_name = "SOC_CHANNL_COUNTRY_P";
				else if(qryStruct.dim1.equals("D0602"))
				   qryStruct.target_name = "SOC_CHANNL_CTRY_01";
			}else if(ctl_lvl.equals("1")){
				qryStruct.province_code = StringB.NulltoBlank(user.getCtl_metro_str());
				if(qryStruct.dim1.isEmpty() || qryStruct.dim1.equals("") || qryStruct.dim1.equals("D0601"))
				   qryStruct.target_name = "SOC_CHANNL_PRO_P";
				else if(qryStruct.dim1.equals("D0602"))
				   qryStruct.target_name = "SOC_CHANNL_PRO_01";
			}else if(ctl_lvl.equals("2")){
				qryStruct.province_code = StringB.NulltoBlank(user.getCtl_metro_str());
				qryStruct.city_code = StringB.NulltoBlank(user.getCtl_city_str());
				if(qryStruct.dim1.isEmpty() || qryStruct.dim1.equals("") || qryStruct.dim1.equals("D0601"))
				   qryStruct.target_name = "SOC_CHANNL_CITY_P";
			    else if(qryStruct.dim1.equals("D0602"))
				   qryStruct.target_name = "SOC_CHANNL_CITY_01";
				else
				   qryStruct.target_name = "SOC_CHANNL_CITY_02";
			}
		}else if(optype.equals("detail2")){
			String chnl_id = StringB.NulltoBlank(request.getParameter("chnl_id"));
			String dim1 = (String)request.getParameter("dim1");
			if(ctl_lvl.equals("0")){
				String pro_code = StringB.NulltoBlank(request.getParameter("province_code"));
				if(pro_code.equals("09")){
					qryStruct.channel_id =chnl_id; 
				}else{
					qryStruct.channel_code=chnl_id;
				}
				if(dim1.equals("D0601"))
				  qryStruct.target_name = "SOC_CHANNL_PRO_P2";
			    else if(dim1.equals("D0602"))
				   qryStruct.target_name = "SOC_CHANNL_PRO_03";
			}else if(ctl_lvl.equals("1")){
				qryStruct.province_code = StringB.NulltoBlank(user.getCtl_metro_str());
				String gui_code = StringB.NulltoBlank(request.getParameter("gui_code"));
				if(gui_code.equals(qryStruct.province_code)){
					qryStruct.channel_id =chnl_id; 
				}else{
					qryStruct.channel_code=chnl_id;
				}
				if(dim1.equals("D0601"))
				  qryStruct.target_name = "SOC_CHANNL_CITY_P";
				else if(dim1.equals("D0602"))
				  qryStruct.target_name = "SOC_CHANNL_CITY_01";
			}
		}else if(optype.equals("right")){
			String chanl_weight="";
			StringBuffer trs = new StringBuffer();
			String sql = "select IOID_ID0,eva_name,chanl_weight,supper_id from ST_EVA_ALL_PARA where IOID_ID0 like 'D06%' and sysdate between eff_date and exp_date";
			List mapList = new ArrayList();			
		    try {
				mapList = WebDBUtil.execQryArrayMap(sql, null);
			} catch (AppException e) {
				e.printStackTrace();
			}
			Map<String,List<Map>> groupList = new HashMap();
			String gen_jie = "";
			for (int i=0;i<mapList.size();i++){
				Map result = (Map) mapList.get(i);
				String supper_id = (String)result.get("SUPPER_ID");
				if(supper_id==null || supper_id.equals("")){
					trs.append("<td class='tdrow3' colspan='2'>"+(String)result.get("EVA_NAME")+"</td>");
					trs.append("<td class='tdrow3'>权重</td></tr>");
					gen_jie = (String)result.get("IOID_ID0");
				}else{
					if(groupList.containsKey(supper_id))
					{
						groupList.get(supper_id).add(result);
					}else
					{
						List<Map> temp = new ArrayList();						
						temp.add(result);						
						groupList.put(supper_id, temp);
					}
				}
			}
			List chanl_type = (List)groupList.get(gen_jie);
			for(int i=0;i<chanl_type.size();i++){
				Map group = (Map) chanl_type.get(i);
				String supper_id = (String) group.get("IOID_ID0");
				List zhibiao = (List)groupList.get(supper_id);
				trs.append("<tr><td class='tdrow2' rowspan="+zhibiao.size()+">"+(String)group.get("EVA_NAME")+"</td>");
				for(int j=0;j<zhibiao.size();j++){
					Map juzhi = (Map)zhibiao.get(j);
					if(j==0){
						trs.append("<td class='tdrow2'>"+(String)juzhi.get("EVA_NAME")+"</td>");
					}else{
					 trs.append("<tr><td class='tdrow2'>"+(String)juzhi.get("EVA_NAME")+"</td>");
					}
					trs.append("<td class='tdrow2'>"+(String)juzhi.get("CHANL_WEIGHT")+"</td></tr>");
				}
			}
			chanl_weight = trs.toString();
			session.setAttribute(WebKeys.ATTR_ST_AREA_SEGMENT_REFLECT, chanl_weight);
		}
		qryStruct.optype = optype;
		// 把结果集存入会话
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		if(optype.equals("county") || optype.equals("detail")){
		   setNextScreen(request, "county_soc_apprial.screen");
		}else if(optype.equals("county2") || optype.equals("detail2")){
		   setNextScreen(request, "county_soc_detail.screen");
		}else if(optype.equals("right")){
		   setNextScreen(request, "soc_right.screen");
		}
	}
}
