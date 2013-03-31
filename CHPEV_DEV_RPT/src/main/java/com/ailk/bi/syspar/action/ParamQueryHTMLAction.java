package com.ailk.bi.syspar.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.syspar.util.ParamConstant;
import com.ailk.bi.syspar.util.ParamHelper;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class ParamQueryHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}

		HttpSession session = request.getSession(true);
		// 清空会话标志
		String clearFlag = request.getParameter(ParamConstant.PARAM_CLEAR_FLAG);
		if (clearFlag == null || "".equals(clearFlag)) {
			clearFlag = "0";
		}
		// 清空会话
		if ("1".equals(clearFlag)) {
			ParamHelper.clearSession(session);
		}

		// 表名称
		String tableName = (String) request
				.getParameter(ParamConstant.PARAM_TABLE_NAME);
		if (tableName == null || "".equals(tableName)) {
			tableName = (String) session
					.getAttribute(ParamConstant.PARAM_TABLE_NAME);
		}
		session.setAttribute(ParamConstant.PARAM_TABLE_NAME, tableName);

		// 节点名称
		String parNode = (String) request
				.getParameter(ParamConstant.PARAM_CONDITION_NODE_ID);
		if (parNode == null || "".equals(parNode)) {
			parNode = (String) session
					.getAttribute(ParamConstant.PARAM_CONDITION_NODE_ID);
		}
		session.setAttribute(ParamConstant.PARAM_CONDITION_NODE_ID, parNode);

		// 条件值
		String conValue = request
				.getParameter(ParamConstant.PARAM_CONDITION_RESULT);
		if (conValue == null) {
			conValue = (String) session
					.getAttribute(ParamConstant.PARAM_CONDITION_RESULT);
			if (conValue == null) {
				conValue = "";
			}
		}
		session.setAttribute(ParamConstant.PARAM_CONDITION_RESULT, conValue);

		//
		String[][] dataObj = null;
		if (tableName != null && tableName.length() > 0) {
			dataObj = ParamHelper.getQueryData(parNode, tableName,
					conValue.trim());
		}
		session.removeAttribute(ParamConstant.PARAM_SQL_VALUE);
		session.setAttribute(ParamConstant.PARAM_SQL_VALUE, dataObj);

		this.setNextScreen(request, "ParamQuery.screen");

	}

}
