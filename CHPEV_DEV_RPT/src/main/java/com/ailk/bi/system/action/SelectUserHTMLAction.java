package com.ailk.bi.system.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.DBTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

public class SelectUserHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5099055935052096556L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		// ??
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();

		session.removeAttribute(WebKeys.ATTR_HiCodeSelectId);
		session.removeAttribute(WebKeys.ATTR_HiPageRows);

		InfoOperTable users[] = null;

		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "1";
		}
		if ("1".equals(operType)) {
			setOperInSession(session);
			this.setNextScreen(request, "UserProc.screen");

		} else if ("2".equals(operType)) {

			String pageRows = request.getParameter("page_rows");
			if (pageRows == null) {
				pageRows = "10";
			}
			// 渠道ID
			String oper_no = CommTool.getParameterGB(request, "oper_no");
			if (oper_no == null) {
				oper_no = "";
			}
			// System.out.println("oper_no============="+oper_no);
			// 渠道描述
			String oper_name = CommTool.getParameterGB(request, "oper_name");
			if (oper_name == null) {
				oper_name = "";
			}
			// System.out.println("oper_name============="+oper_name);
			// 手机机型记录集
			users = (InfoOperTable[]) session
					.getAttribute(WebKeys.ATTR_InfoOperTable);
			if (users == null || users.length <= 0) {
				throw new HTMLActionException(request.getSession(),
						HTMLActionException.WARN_PAGE, "操作员信息表没有数据，请检查会话是否失效！");
			}

			// 查找为第一个优先，不过率查找！！！（可以补充过滤查找）
			int j = 0;
			if (!"".equals(oper_no) && "".equals(oper_name)) {
				for (int i = 0; users != null && i < users.length; i++) {
					if (users[i].oper_no.toLowerCase().equals(
							oper_no.toLowerCase())) {
						j = i;
						break;
					}
				}
			} else {
				if ("".equals(oper_no) && !"".equals(oper_name)) {
					for (int i = 0; users != null && i < users.length; i++) {
						if (users[i].oper_name.toLowerCase().indexOf(
								oper_name.toLowerCase()) >= 0) {
							j = i;
							break;
						}
					}
				}
			}
			// System.out.println("j================"+j);
			// 设置当前行
			session.setAttribute(WebKeys.ATTR_HiCodeSelectId, String.valueOf(j));
			session.setAttribute(WebKeys.ATTR_HiPageRows, pageRows);
			setNextScreen(request, "UserDisp.screen");
		}

	}

	/**
	 * 
	 * @param session
	 * @param channel_type
	 * @param sub_channel_type
	 */
	public static void setOperInSession(HttpSession session) {

		InfoOperTable users[] = null;

		String sql = "select a.oper_no,a.oper_name ,b.dept_no,b.dept_name "
				+ "from info_oper a ,info_dept b where a.dept_no = b.dept_no ";

		try {
			String svces[][] = WebDBUtil.execQryArray(DBTool.getWLSConn(), sql,
					"");
			System.out.println("sql===========" + sql);
			if (svces != null && svces.length > 0) {
				users = new InfoOperTable[svces.length];
				for (int i = 0; i < svces.length; i++) {
					users[i] = new InfoOperTable();
					users[i].oper_no = svces[i][0];
					users[i].oper_name = svces[i][1];
					users[i].dept_no = svces[i][2];
					users[i].dept_name = svces[i][3];
				}
			}

		} catch (AppException ex1) {
			ex1.printStackTrace();
		}
		// 判断后存入session
		session.removeAttribute(WebKeys.ATTR_InfoOperTable);
		session.setAttribute(WebKeys.ATTR_InfoOperTable, users);
	}

}
