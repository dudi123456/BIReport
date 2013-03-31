package com.ailk.bi.system.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.*;

public class TemplateListHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3583492342986992918L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		// 变量申明区
		HttpSession session = request.getSession();

		String oper_type = CommTool.getParameterGB(request, "oper_type");
		if (oper_type == null || "".equals(oper_type)) {
			oper_type = "query";
		}
		// 程序判断区
		String group_id = request.getParameter("group_id");
		if (group_id == null || "".equals(group_id)) {
			group_id = "1";
		}
		if ("query".equalsIgnoreCase(oper_type)) {
			// 提取数据
			String[][] value = getLightTemplateRight(group_id);
			session.setAttribute(WebKeys.ATTR_GROUP_TEMPLATE_LIST, value);

		} else {
			// 增加
			String[] template = request.getParameterValues("group_list");
			int t = ModifyLightTemplateRight(group_id, template);
			if (t >= 0) {
				throw new HTMLActionException(session,
						HTMLActionException.SUCCESS_PAGE, "保存信息成功！",
						"TemplateListView.rptdo?group_id=" + group_id);
			} else {
				throw new HTMLActionException(session,
						HTMLActionException.ERROR_PAGE, "保存信息失败！");
			}
		}
		session.setAttribute("group_id", group_id);
		// 会话返回
		this.setNextScreen(request, "GroupTemplateList.screen");

	}

	/*
	 * 
	 */
	public static String[][] getLightTemplateRight(String group_id) {
		String[][] value = null;
		try {
			String sql = SQLGenator.genSQL("SYS002", group_id);
			value = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return value;

	}

	/*
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int ModifyLightTemplateRight(String group_id,
			String[] template) {
		List sqllist = new ArrayList();
		int t = -1;
		try {
			// delete
			String deletsql = SQLGenator.genSQL("SYS003", group_id);
			System.out.println("SYS003=========" + deletsql);
			sqllist.add(deletsql);
			// add
			for (int i = 0; template != null && i < template.length; i++) {
				String tempsql = SQLGenator.genSQL("SYS004", group_id,
						template[i]);
				System.out.println("SYS004=========" + tempsql);
				sqllist.add(tempsql);
			}
			String[] sqlArr = (String[]) sqllist.toArray(new String[sqllist
					.size()]);
			t = WebDBUtil.execTransUpdate(sqlArr);

		} catch (AppException e) {

			e.printStackTrace();
		}

		return t;

	}

}
