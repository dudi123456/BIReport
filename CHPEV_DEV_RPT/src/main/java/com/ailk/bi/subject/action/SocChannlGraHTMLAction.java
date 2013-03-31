package com.ailk.bi.subject.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.report.struct.ReportQryStruct;

public class SocChannlGraHTMLAction extends HTMLActionSupport {
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
		/*user.setCtl_lvl("2");
		user.setCtl_metro_str("34");
		user.setCtl_city_str("340");*/
		String ctl_lvl = StringB.NulltoBlank(user.getCtl_lvl());
		qryStruct.ctl_lvl = ctl_lvl;

		if (optype.equals("general_grade")) {// 社会渠道评级菜单
			if (ctl_lvl.equals("0") || ctl_lvl.equals("1")
					|| ctl_lvl.equals("2")) {
				if (ctl_lvl.equals("1") || ctl_lvl.equals("2")) {// 省分人员和地市人员
					String ctl_metro_str = StringB.NulltoBlank(user
							.getCtl_metro_str());
					if (ctl_metro_str != null && !ctl_metro_str.equals("")) {
						qryStruct.metro_id = ctl_metro_str;
					}
				}
				if (ctl_lvl.equals("2")) {// 地市人员
					String ctl_city_str = StringB.NulltoBlank(user
							.getCtl_city_str());
					if (ctl_city_str != null && !ctl_city_str.equals("")) {
						qryStruct.city_id = ctl_city_str;
					}
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
