package com.ailk.bi.subject.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class SocChannlPDHTMLAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
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
		qryStruct = (ReportQryStruct) session
				.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
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
		
		//设置默认账期为当前月的前一个月
		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}
		if (qryStruct.rpt_date == null || "".equals(qryStruct.rpt_date)){
			qryStruct.rpt_date = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
		}
		
		UserCtlRegionStruct user = (UserCtlRegionStruct) session
				.getAttribute("ATTR_C_UserCtlStruct");
		/*user.setCtl_lvl("1");
		user.setCtl_metro_str("34");
		user.setCtl_city_str("340");*/
		String ctl_lvl = StringB.NulltoBlank(user.getCtl_lvl());
		qryStruct.ctl_lvl = ctl_lvl;

		if (optype.equals("general_pool")) {// 总部门店汇总菜单
			if (ctl_lvl.equals("0") || ctl_lvl.equals("1")
					|| ctl_lvl.equals("2")) {
				if (ctl_lvl.equals("1") || ctl_lvl.equals("2")) {// 省分人员和地市人员
					String ctl_metro_str = StringB.NulltoBlank(user
							.getCtl_metro_str());
					if (ctl_metro_str != null && !ctl_metro_str.equals("")) {
						qryStruct.province_code = ctl_metro_str;
					}
				}
				if (ctl_lvl.equals("2")) {// 地市人员
					String ctl_city_str = StringB.NulltoBlank(user
							.getCtl_city_str());
					if (ctl_city_str != null && !ctl_city_str.equals("")) {
						qryStruct.city_code = ctl_city_str;
					}
				}
			} else {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
			}
		}

		/*if (optype.equals("prov_pool")) {// 省分门店汇总菜单
			if (ctl_lvl.equals("0") || ctl_lvl.equals("1")) {
				if (ctl_lvl.equals("1")) {// 省分人员
					String ctl_metro_str = StringB.NulltoBlank(user
							.getCtl_metro_str());
					if (ctl_metro_str != null && !ctl_metro_str.equals("")) {
						qryStruct.province_code = ctl_metro_str;
					}
				}
			} else {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
			}
		}

		if (optype.equals("city_pool")) {// 地市门店汇总菜单
			if (ctl_lvl.equals("0") || ctl_lvl.equals("1")
					|| ctl_lvl.equals("2")) {
				if (ctl_lvl.equals("1") || ctl_lvl.equals("2")) {// 省分人员和地市人员
					String ctl_metro_str = StringB.NulltoBlank(user
							.getCtl_metro_str());
					if (ctl_metro_str != null && !ctl_metro_str.equals("")) {
						qryStruct.province_code = ctl_metro_str;
					}
				}
				if (ctl_lvl.equals("2")) {// 地市人员
					String ctl_city_str = StringB.NulltoBlank(user
							.getCtl_city_str());
					if (ctl_city_str != null && !ctl_city_str.equals("")) {
						qryStruct.city_code = ctl_city_str;
					}
				}
			} else {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
			}
		}*/

		if (optype.equals("general_detail")) {// 门店明细菜单
			if (ctl_lvl.equals("0") || ctl_lvl.equals("1")
					|| ctl_lvl.equals("2")) {
				if (ctl_lvl.equals("1") || ctl_lvl.equals("2")) {// 省分人员和地市人员
					String ctl_metro_str = StringB.NulltoBlank(user
							.getCtl_metro_str());
					if (ctl_metro_str != null && !ctl_metro_str.equals("")) {
						qryStruct.province_code = ctl_metro_str;
					}
				}
				if (ctl_lvl.equals("2")) {// 地市人员
					String ctl_city_str = StringB.NulltoBlank(user
							.getCtl_city_str());
					if (ctl_city_str != null && !ctl_city_str.equals("")) {
						qryStruct.city_code = ctl_city_str;
					}
				}


				String acct_month = StringB.NulltoBlank(request
						.getParameter("ACCT_MONTH"));
				String chain_level = StringB.NulltoBlank(request
						.getParameter("chain_level"));
				String chnl_id = StringB.NulltoBlank(request
						.getParameter("chnl_id"));
				String chnl_name = StringB.NulltoBlank(request
						.getParameter("chnl_name"));
				String msu_name = StringB.NulltoBlank(request
						.getParameter("msu_name"));
				String owe_code = StringB.NulltoBlank(request
						.getParameter("owe_code"));
				String bowe_code = StringB.NulltoBlank(request
						.getParameter("bowe_code"));//省分汇总时
				try {
					msu_name = URLDecoder.decode(msu_name, "UTF-8").trim();
					chnl_name = URLDecoder.decode(chnl_name, "UTF-8").trim();
				} catch (UnsupportedEncodingException e) {
				}
				String msu_fld = StringB.NulltoBlank(request
						.getParameter("msu_fld"));
				if(ctl_lvl.equals("0")){//总部人员
					if(owe_code.equals("09")){
						if (!chnl_id.equals("")) {
							qryStruct.dim2 = chnl_id;
						}
						if (!chnl_name.equals("")) {
							qryStruct.dim6 = chnl_name;
						}
					}else{
						if (!chnl_id.equals("")) {
							qryStruct.dim10 = chnl_id;
						}
						if (!chnl_name.equals("")) {
							qryStruct.dim11 = chnl_name;
						}
					}
				}else if(ctl_lvl.equals("1")){//省分人员
					if(!bowe_code.equals("") && bowe_code.length()==2){//归属省分
						if (!chnl_id.equals("")) {
							qryStruct.dim2 = chnl_id;
						}	
						if (!chnl_name.equals("")) {
							qryStruct.dim6 = chnl_name;
						}
					}else if(!bowe_code.equals("") && bowe_code.length()>=3){//归属地市
						if (!chnl_id.equals("")) {
							qryStruct.dim10 = chnl_id;
						}
						if (!chnl_name.equals("")) {
							qryStruct.dim11 = chnl_name;
						}
					}
				}else if(ctl_lvl.equals("2")){//地市人员
					if (!chnl_id.equals("")) {
						qryStruct.dim2 = chnl_id;
					}
					if (!chnl_name.equals("")) {
						qryStruct.dim6 = chnl_name;
					}
				}
				if (!acct_month.equals("")) {
					qryStruct.gather_month = acct_month;
				}
				if (!chain_level.equals("")) {
					qryStruct.chain_level = chain_level;
				}
				
				// 门店级别
				if (("A级".equals(msu_name) || "B级".equals(msu_name)
						|| "C级".equals(msu_name) || "S级".equals(msu_name))) {
					qryStruct.dim1 = msu_name.substring(0,1).trim();
				} else if (("A".equals(msu_name) || "B".equals(msu_name)
						|| "C".equals(msu_name) || "S".equals(msu_name))) {
					qryStruct.dim1 = msu_name;
				}
				// 得分等级
				if (msu_fld.indexOf("level_ex_num") != -1) {// 高
					qryStruct.dim9 = "10";
					if (msu_fld.indexOf("level_bad_num") != -1) {// 全部
						qryStruct.dim9 = "";
					}
				} else if (msu_fld.indexOf("level_good_num") != -1) {// 中
					qryStruct.dim9 = "11";
				} else if (msu_fld.indexOf("level_mid_num") != -1) {// 低
					qryStruct.dim9 = "12";
				} else if (msu_fld.indexOf("level_bad_num") != -1) {// 极低
					qryStruct.dim9 = "13";
				} else {
					qryStruct.dim9 = "";
				}
			} else {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "对不起，您无权访问此菜单！");
			}
		}

		qryStruct.optype = optype;
		// 把结果集存入会话
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, "soc_channl_" + optype + ".screen");
	}
}
