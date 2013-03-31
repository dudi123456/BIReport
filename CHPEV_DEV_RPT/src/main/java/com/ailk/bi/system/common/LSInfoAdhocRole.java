package com.ailk.bi.system.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoAdhocRole {

	public static String[][] getAdhocInfo(String user_id) {

		String sql = "";
		String[][] rs = null;
		try {
			sql = SQLGenator.genSQL("ar001", user_id, user_id);
			rs = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return rs;

	}

	public static String getAdhocRole(String adhoc_id, String role_id) {

		String sql = "";
		StringBuffer str = new StringBuffer();
		try {
			// 条件
			sql = SQLGenator.genSQL("ar002", adhoc_id, role_id, adhoc_id);
			String[][] con = WebDBUtil.execQryArray(sql, "");
			// 维度
			sql = SQLGenator.genSQL("ar003", adhoc_id, role_id, adhoc_id);
			String[][] dim = WebDBUtil.execQryArray(sql, "");
			// 指标
			sql = SQLGenator.genSQL("ar004", adhoc_id, role_id, adhoc_id);
			String[][] msu = WebDBUtil.execQryArray(sql, "");
			// 用户清单
			sql = SQLGenator.genSQL("ar009", adhoc_id, "-1", role_id, adhoc_id);
			String[][] userList = WebDBUtil.execQryArray(sql, "");
			str.append(getHtml(con, "1")).append(getHtml(dim, "2"))
					.append(getHtml(msu, "3")).append(getHtml(userList, "4"));

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return str.toString();

	}

	public static String getHtml(String[][] rs, String type) {
		int msu = 0;
		String group_id = "";
		StringBuffer str = new StringBuffer();
		String strGroup = "";
		if (rs != null && rs.length > 0)
		{
			if ("1".equals(type))
			{
				String[] temp = rs[0][0].split("-");
				str.append(
						"<table style='width: 100%' ><tr><td colspan='6'><img src=\"../images/common/system/icon_well.gif\" border='0'>&nbsp;<b>当前-"
								+ temp[0] + "</b></td></tr><TR>\n")
						.append("<td width=\"10%\"><input type=\"checkbox\" name=\"all\" onclick=\"seleAll()\">全选</td>\n")
						.append("<td align=\"center\"><input type=\"button\" name=\"save\" id='save' class=\"button\"  value=\"保存\" onclick=\"javascript:formPost(this);\"> </td>\n")
						.append("</tr></table>\n").append("<table>\n");
			}
			for (int i = 0; i < rs.length; i++)
			{
				if (!group_id.equals(rs[i][1]))
				{
					if (!"".equals(group_id)) {
						strGroup += group_id + ",";
					}
					msu = 0;
					if (i != 0) {
						str.append("</tr></tbody>\n");
					}
				}
				if (msu == 0)
				{
					str.append(
							"<tr class=\"celdata\"><td colspan=\"13\"  class=\"leftdata\">\n")
							.append("<a href=\"javascript:void(0)\"  onclick=\"hiddenData('dataTitle"
									+ rs[i][1]
									+ "','dataContent"
									+ rs[i][1]
									+ "');\">\n");

					if ("1".endsWith(type) && i == 0)
					{
						str.append(
								"<img id=\"dataTitle"
										+ rs[i][1]
										+ "\"  src=\"../images/common/system/sign_show.gif\" border='0' style=\"margin-right: 5px\"><span class=\"tab-font\">"
										+ rs[i][0]
										+ "</span></a>&nbsp;&nbsp;&nbsp;&nbsp; <input type=\"checkbox\" name=\"ckall"
										+ rs[i][1]
										+ "\" onclick=\"checkEvent('"
										+ rs[i][1] + "','ckall" + rs[i][1]
										+ "')\" >全选</td></tr>\n")
								.append("<tbody id=\"dataContent" + rs[i][1]
										+ "\" >\n");
					} else {
						str.append(
								"<img id=\"dataTitle"
										+ rs[i][1]
										+ "\"  src=\"../images/common/system/sign_collect.gif\" border='0' style=\"margin-right: 5px\"><span class=\"tab-font\">"
										+ rs[i][0]
										+ "</span></a>&nbsp;&nbsp;&nbsp;&nbsp; <input type=\"checkbox\" name=\"ckall"
										+ rs[i][1]
										+ "\" onclick=\"checkEvent('"
										+ rs[i][1] + "','ckall" + rs[i][1]
										+ "')\" >全选</td></tr>\n").append(
								"<tbody id=\"dataContent" + rs[i][1]
										+ "\"  style=\"display:none\">\n");
					}
				}

				if (msu % 5 == 0)
				{
					str.append("<tr><td width='10'>&nbsp;</td>\n");
				}
//<td><input type="checkbox" name="TJ03" id="TJ03" value="TJ03;TJ0301;1" >项目1编码<td>

				str.append(
						"<td><input type=\"checkbox\""
								+" name=\"" + rs[i][1]
								+ "\" id=\"" + rs[i][1]
								+ "\" onclick=\"checkSingle('"
								+ rs[i][1] + "','ckall" + rs[i][1]
								+ "')\" value=\""+ rs[i][1] + ";" + rs[i][2] + ";" + type+ "\" " + rs[i][4]
								+ ">" + rs[i][3]).append("<td>\n");
				if ((msu % 5 == 4) || (msu == rs.length - 1))
				{
					if ((msu == rs.length - 1) && (msu % 5 != 4))
					{
						int count = 5 - ((rs.length) % 5);
						str.append(AdhocUtil.getNbspTdInnerHtml(count));
					}
				}

				group_id = rs[i][1];
				++msu;
			}
			if (rs != null && rs.length > 0)
			{
				strGroup += group_id;
				str.append("</tr></tbody>\n").append(
						"<input type='hidden' name='group_id' value='"
								+ strGroup + "'>\n");
			}
		}
		return str.toString();
	}

	/**
	 * 保存即席查询权限
	 *
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void saveAdhocRole(HttpServletRequest request)
			throws HTMLActionException {

		HttpSession session = request.getSession();

		String role_id = request.getParameter("role_id");
		String adhoc_id = request.getParameter("adhoc_id");
		String[] strGroup = request.getParameterValues("group_id");

		List list = new ArrayList();
		String delsql = "";
		String log_sql1 = getLogSql(request, "ar007", "3", role_id, adhoc_id);
		list.add(log_sql1);
		try {
			delsql = SQLGenator.genSQL("ar006", role_id, adhoc_id);

			WebDBUtil.execUpdate(delsql);

			// list.add(delsql);
		} catch (AppException e1) {

			e1.printStackTrace();
		}

		for (int k = 0; strGroup != null && k < strGroup.length; k++) {
			String[] group_id = strGroup[k].split(",");
			for (int i = 0; strGroup != null && i < group_id.length; i++) {
				String[] temp = request.getParameterValues(group_id[i]);

				for (int j = 0; temp != null && j < temp.length; j++) {
					String[] str = temp[j].split(";");
					String sql = "";
					try {
						sql = SQLGenator.genSQL("ar005", role_id, adhoc_id,
								str[0], str[1].replace("\'", "\'\'"), str[2]);
						list.add(sql);
					} catch (AppException e) {

						e.printStackTrace();
					}

				}
			}
		}

		String log_sql2 = getLogSql(request, "ar007", "1", role_id, adhoc_id);
		list.add(log_sql2);

		String sqlArr[] = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			sqlArr[i] = list.get(i).toString();
			// System.out.println("sql[i]================" + sqlArr[i]);

		}

		System.out.println("sql[i]================" + sqlArr[1]);
		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "赋权成功！",
					"adhocRole.screen?role_id=" + role_id + "&adhoc_id="
							+ adhoc_id);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "赋权失败！");
		}

	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param user_id
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_user_id = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_user_id, ip, type,
					para_a, para_b);
			System.out.println(sql);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}
}
