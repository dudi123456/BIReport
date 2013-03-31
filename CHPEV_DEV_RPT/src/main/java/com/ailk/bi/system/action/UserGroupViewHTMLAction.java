package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoUserGroupTable;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.system.common.LSInfoUserGroup;

public class UserGroupViewHTMLAction extends HTMLActionSupport {
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

		InfoUserGroupTable sstGroup = new InfoUserGroupTable();
		// 取页面用户组信息
		try {
			AppWebUtil.getHtmlObject(request, "group", sstGroup);
		} catch (AppException ex) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "用户组受理信息有误！");
		}

		if ("0".equals(submitType)) { // 新增
			LSInfoUserGroup.addNewUserGroup(request, sstGroup);
		} else if ("1".equals(submitType) || "3".equals(submitType)) { // 修改
			LSInfoUserGroup.updategroupInfo(request, sstGroup);
		}
		if ("2".equals(submitType)) { // 删除
			LSInfoUserGroup.deletegroupInfo(request, sstGroup);
		}

		if ("groupRole".equals(submitType)) {
			String group_id = request.getParameter("group_id");
			InfoUserGroupTable info = LSInfoUserGroup
					.getUserGroupInfo(group_id);
			String[][] groupRoleList = LSInfoUserGroup.getGroupRoleList(
					user.group_id, request);
			String[][] groupRole = LSInfoUserGroup.getGroupRole(group_id);
			// String[][] noInGroupRole =
			// LSInfoUserGroup.getNoGroupRole(group_id);
			request.setAttribute("info", info);
			request.setAttribute("groupRole", groupRole);
			request.setAttribute("groupRoleList", groupRoleList);
			// request.setAttribute("noInGroupRole", noInGroupRole);
			this.setNextScreen(request, "GroupRole.screen");
		} else if ("saveGroupRole".equals(submitType)) {
			LSInfoUserGroup.saveGroupRole(request, sstGroup);
		}
		if ("groupUser".equals(submitType)) {
			String group_id = request.getParameter("group_id");
			InfoUserGroupTable info = LSInfoUserGroup
					.getUserGroupInfo(group_id);
			String[][] groupUser = LSInfoUserGroup.getGroupUser(group_id);
			request.setAttribute("info", info);
			request.setAttribute("groupUser", groupUser);
			this.setNextScreen(request, "GroupUser.screen");
		} else if ("saveGroupUser".equals(submitType)) {
			LSInfoUserGroup.saveGroupRole(request, sstGroup);
		}

	}

}