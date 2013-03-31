package com.ailk.bi.subject.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.report.struct.ReportQryStruct;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class LeaderViewHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();

		// 获取页面screen标示
		String optype = request.getParameter("optype");

		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}

		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = new ReportQryStruct();
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "提取界面查询信息失败，请注意是否登陆超时！");
		}
		qryStruct.optype = optype;

		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}

		if (qryStruct.gather_month == null || "".equals(qryStruct.gather_month)) {
			qryStruct.gather_month = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}
		if (qryStruct.date_s == null || "".equals(qryStruct.date_s)) {
			qryStruct.date_s = qryStruct.gather_month.substring(0, 4) + "01";
		}
		if (qryStruct.date_e == null || "".equals(qryStruct.date_e)) {
			qryStruct.date_e = DateUtil.getDiffMonth(0, DateUtil.getNowDate());
		}

		if (optype.equalsIgnoreCase("login_rank")
				|| optype.equalsIgnoreCase("rpt_rank")) {

			if (qryStruct.gather_day == null || "".equals(qryStruct.gather_day)) {
				qryStruct.gather_day = DateUtil.getDiffDay(-0,
						DateUtil.getNowDate());
			}

			if (qryStruct.gather_day_e == null
					|| "".equals(qryStruct.gather_day_e)) {
				qryStruct.gather_day_e = DateUtil.getDiffDay(-0,
						DateUtil.getNowDate());
			}
		} else {
			if (qryStruct.gather_day == null || "".equals(qryStruct.gather_day)) {
				qryStruct.gather_day = DateUtil.getDiffDay(-1,
						DateUtil.getNowDate());
			}

			if (qryStruct.gather_day_e == null
					|| "".equals(qryStruct.gather_day_e)) {
				qryStruct.gather_day_e = DateUtil.getDiffDay(-1,
						DateUtil.getNowDate());
			}
		}

		qryStruct.gather_day_s = qryStruct.gather_day.substring(0, 6) + "01";

		if (qryStruct.svc_id.length() == 0) {
			qryStruct.svc_name = "全部";
		}

		if (qryStruct.cust_group_id.length() == 0) {
			qryStruct.cust_group_desc = "全部";
		}

		if (qryStruct.city_id.length() == 0) {
			qryStruct.city_desc = "全部";
		}

		if (qryStruct.plan_id.length() == 0) {
			qryStruct.plan_name = "全部";
		}

		if (qryStruct.wireless_id.length() == 0) {
			qryStruct.wireless_desc = "全部";
		}

		if (qryStruct.c_pstn_id.length() == 0) {
			qryStruct.c_pstn_id = "1";
			qryStruct.c_pstn_desc = "C网";
		}
		if (qryStruct.phone_seg_type_id.length() == 0) {
			qryStruct.phone_seg_type_desc = "全部";
		}
		if (qryStruct.discount_type_id.length() == 0) {
			qryStruct.discount_type_desc = "全部";
		}

		if (qryStruct.busi_type_id.length() == 0) {
			qryStruct.busi_type_desc = "全部";
		}

		String tabFeeFlag = request.getParameter("tab_fee_flag");
		if (tabFeeFlag != null) {
			session.setAttribute("monitor_addval_tabFee", tabFeeFlag);
		}

		if (qryStruct.product_lv1_id.length() == 0) {
			qryStruct.product_lv1_name = "全部";
		}

		// 把结果集存入会话
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype + ".screen");

	}

}
