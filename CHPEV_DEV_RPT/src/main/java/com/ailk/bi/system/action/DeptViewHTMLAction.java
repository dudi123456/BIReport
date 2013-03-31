package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.InfoDeptTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.system.common.LSInfoDept;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class DeptViewHTMLAction extends HTMLActionSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = -6165404731714067995L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		//
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0";
		}
		String oper_type = request.getParameter("oper_type");
		if (oper_type == null || "".equals(oper_type)) {
			oper_type = "0";
		}

		//
		InfoDeptTable infoDept = new InfoDeptTable();
		if ("1".equals(submitType)) {
			// 取页面用户信息
			try {
				AppWebUtil.getHtmlObject(request, "dept", infoDept);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "部门受理信息有误！");
			}
			// 调试

			if ("1".equals(oper_type)) {// 新增
				LSInfoDept.addNewDept(request, infoDept);
			} else {
				LSInfoDept.updateDeptInfo(request, infoDept);
			}

		}
		if ("2".equals(submitType)) {// 删除
			// 取页面部门信息
			try {
				AppWebUtil.getHtmlObject(request, "dept", infoDept);
			} catch (AppException ex) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "部门受理信息有误！");
			}
			// 调试

			LSInfoDept.deleteDeptInfo(request, infoDept);

		}
	}
}
