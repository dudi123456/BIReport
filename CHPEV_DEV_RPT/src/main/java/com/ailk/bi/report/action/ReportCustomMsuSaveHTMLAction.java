package com.ailk.bi.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.exception.CustomMsuException;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.report.util.CustomMsuUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ReportCustomMsuSaveHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4174769377393282857L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		Log log = LogFactory.getLog(this.getClass());
		HttpSession session = request.getSession();
		if (session == null) {
			setNextScreen(request, "LoginIn.screen");
		}
		// 获取用户ID,暂时设定为0
		String userId = CommonFacade.getLoginId(session);

		String token = CommTool.getParameterGB(request, "token");
		String server_token = (String) session
				.getAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_TOKEN);
		if (!token.equals(server_token)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "请不要刷新或多次提交重复的内容");
		}

		String customMsuName = CommTool
				.getParameterGB(request, "customMsuName");
		if (null == customMsuName || "".equals(customMsuName)) {
			log.error("指标定制－没有提供衍生指标名称");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		String baseMsu = CommTool.getParameterGB(request, "selectedMsu");
		if (null == baseMsu || "".equals(baseMsu)) {
			log.error("指标定制－没有提供基本指标");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		String srcTabId = CommTool.getParameterGB(request, "srcTabId");
		if (null == srcTabId || "".equals(srcTabId)) {
			log.error("指标定制－没有提供数据源标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		String[] values = srcTabId.split("\\|");
		srcTabId = values[0];
		Object tmpObj = session
				.getAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_STAT_PERIOD);
		String stat_period = null;
		if (null != tmpObj) {
			stat_period = (String) tmpObj;
		}
		if (null == stat_period || "".equals(stat_period)) {
			log.error("指标定制－没有提供统计周期");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		// 判断是否有同名的自定义指标
		boolean hasSameCustomMsu = CustomMsuUtil.hasOtherSameNamedCustomMsu(
				userId, customMsuName, stat_period);
		if (hasSameCustomMsu) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "对不起，您已经定义过名称为 '"
							+ customMsuName + "' 的指标，请返回修改指标名称，或者使用已经定义的指标!");
		} else {
			try {
				CustomMsuUtil.saveCustomMsu(userId, customMsuName, baseMsu,
						srcTabId, stat_period, request);
			} catch (CustomMsuException cme) {
				log.error("指标定制－保存指标时发生错误");
				log.error(cme);
			}
		}
		// 设置下一个令牌
		session.setAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_TOKEN,
				CustomMsuUtil.getToken());
		setNextScreen(request, "rptCustomMsuAddSuccess.screen");
	}
}
