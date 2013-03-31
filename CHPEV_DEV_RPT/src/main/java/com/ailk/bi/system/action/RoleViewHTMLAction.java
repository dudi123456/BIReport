package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoRoleTable;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.system.common.LSInfoRole;

public class RoleViewHTMLAction extends HTMLActionSupport {
	/**
   *
   */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);

		// 取得提交参数
		String submitType = request.getParameter("submitType");
		if (submitType == null || "".equals(submitType)) {
			submitType = "0";
		}

		InfoRoleTable sstRole = new InfoRoleTable();
		// 取页面角色信息
		try {
			AppWebUtil.getHtmlObject(request, "role", sstRole);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "角色受理信息有误！");
		}

		if ("0".equals(submitType)) { // 新增
			LSInfoRole.addNewRole(request, sstRole);
		} else if ("1".equals(submitType) || "3".equals(submitType)) { // 修改
			LSInfoRole.updateRoleInfo(request, sstRole);
		} else if ("2".equals(submitType)) { // 删除
			LSInfoRole.deleteRoleInfo(request, sstRole);
		} else if ("roleGroup".equals(submitType)) {
			String role_id = request.getParameter("role_id");
			InfoRoleTable info = LSInfoRole.getRoleInfo(role_id);
			String[][] roleGroupList = LSInfoRole.getRoleGroupList(
					user.group_id, user.system_id, request);
			String[][] roleGroup = LSInfoRole.getRoleGroup(role_id);
			request.setAttribute("info", info);
			request.setAttribute("roleGroup", roleGroup);
			request.setAttribute("roleGroupList", roleGroupList);
			this.setNextScreen(request, "roleGroup.screen");
		} else if ("saveRoleGroup".equals(submitType)) {
			LSInfoRole.saveRoleGroup(request, sstRole);
		}

	}

}