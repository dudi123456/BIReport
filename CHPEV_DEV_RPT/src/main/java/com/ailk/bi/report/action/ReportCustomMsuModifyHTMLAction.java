package com.ailk.bi.report.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.exception.CustomMsuException;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.report.util.CustomMsuUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ReportCustomMsuModifyHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7668607437022589800L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		Log log = LogFactory.getLog(this.getClass());
		HttpSession session = request.getSession();
		if (session == null) {
			setNextScreen(request, "LoginIn.screen");
		}
		String action = CommTool.getParameterGB(request, "action");
		if (null == action || "".equals(action)) {
			action = "normal";
		}

		String customMsuName = null;
		if ("normal".equalsIgnoreCase(action)) {
			customMsuName = CommTool.getParameterGB(request, "customMsuName");
		} else {
			customMsuName = CommTool.getParameterUTF(request, "customMsuName");
		}

		if (null == customMsuName || "".equals(customMsuName)) {
			log.error("指标定制维护－修改指标时没有提供衍生指标名称");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		String msuId = CommTool.getParameterGB(request, "selectedMsu");
		if (null == msuId || "".equals(msuId)) {
			log.error("指标定制维护－修改指标时没有提供衍生指标标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
							+ "由此造成对您的工作不便，深表歉意！");
		}
		boolean used = CustomMsuUtil.customMsuUsedForReport(msuId);
		if (used) {
			log.error("指标定制维护－修改指标时发现指标已经被报表使用");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"对不起，该自定义指标已经被报表使用，不能进行修改或删除！");
		}
		boolean hasSameCustomMsu = CustomMsuUtil.hasSameNamedCustomMsu(msuId,
				customMsuName);
		if (hasSameCustomMsu) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "对不起，您已经定义过名称为 '"
							+ customMsuName + "' 的指标，请返回修改指标名称，或者使用已经定义的指标!");
		} else {
			try {
				CustomMsuUtil.modifyCustomMsu(msuId, customMsuName, request);
			} catch (CustomMsuException cme) {
				log.error("指标定制维护－更新自定义指标失败");
				log.error(cme);// 把异常记录下来
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE,
						"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
								+ "由此造成对您的工作不便，深表歉意！");
			}
		}
		setNextScreen(request, "rptCustomMsuMaintain.screen");
	}
}
