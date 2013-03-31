package com.ailk.bi.system.common;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoRoleTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;
import com.ailk.bi.system.facade.impl.ServiceFacade;
import com.ailk.bi.system.service.IUserService;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoMenu {

	/**
	 * 查询菜单信息
	 *
	 * @param menu_id
	 * @return
	 */
	public static InfoMenuTable getMenuInfo(String menu_id) {

		String sql = "select A.MENU_ID,A.MENU_NAME,A.PARENT_ID,B.MENU_NAME,A.SEQUENCE,A.MENU_TYPE,A.MENU_URL,A.STATUS,A.ISSHOW,A.MENU_DESC,A.ICON_URL,A.SYSTEM_ID "
				+ "from UI_INFO_MENU A left outer join UI_INFO_MENU B on A.PARENT_id = B.MENU_id where A.MENU_id = '"
				+ menu_id + "'";
		InfoMenuTable infoMenu = new InfoMenuTable();
		try {
			String rs[][] = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				infoMenu.menu_id = rs[0][0];
				infoMenu.menu_name = rs[0][1];
				infoMenu.parent_id = rs[0][2];
				infoMenu.parent_name = rs[0][3];
				infoMenu.sequence = rs[0][4];

				infoMenu.menu_type = rs[0][5];
				infoMenu.menu_url = rs[0][6];
				infoMenu.status = rs[0][7];
				infoMenu.isshow = rs[0][8];
				infoMenu.menu_desc = rs[0][9];
				infoMenu.icon_url = rs[0][10];
				infoMenu.system_id = rs[0][11];
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return infoMenu;
	}

	/**
	 * 判断是否是最低级菜单
	 *
	 * @param menu_id
	 * @return
	 */
	public static boolean hasSonMenu(String menu_id) {
		boolean flag = false;
		String sql = "select menu_id,menu_name from UI_INFO_MENU where parent_id = '"
				+ menu_id + "' and status='1'";
		try {
			String rs[][] = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				flag = true;
			}
		} catch (AppException ex) {

			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * 新增菜单信息
	 *
	 * @param session
	 * @param infoMenu
	 * @throws HTMLActionException
	 */
	public static void addNewMenu(HttpServletRequest request,
			InfoMenuTable infoMenu) throws HTMLActionException {
		HttpSession session = request.getSession();
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		String menu_id = infoMenu.menu_id;
		String parent_id = infoMenu.parent_id;
		// System.out.println("parent_id:" + parent_id);
		Vector sqlV = new Vector();
		//
		InfoMenuTable menu = getMenuInfo(menu_id);
		//
		if (!infoMenu.menu_name.equals(menu.menu_name)) {
			if (isDupMenuName(menu_id, parent_id, infoMenu.menu_name,
					user.system_id)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "同一父菜单下子菜单名称重复！");
			}

		}
		// 新增模块
		if ("0".equals(menu_id)) {
			infoMenu.menu_id = CommonUtil.getNextVal("seq_info_menu") + "";
			menu_id = infoMenu.menu_id;
			String sqlA = "insert into UI_INFO_MENU(menu_id,menu_name,system_id) values('"
					+ infoMenu.menu_id
					+ "','"
					+ infoMenu.menu_name
					+ "','"
					+ user.system_id + "')";
			sqlV.add(sqlA);
			IUserService service = new ServiceFacade();

			List list = service.getUserRoleInfo(user.user_id);
			String role_id = "";
			if (list != null && list.size() > 0) {
				role_id = ((InfoRoleTable) list.get(0)).role_id;
			}
			String strSql = "insert into ui_rule_res_role(role_id,res_type,res_id) values('"
					+ role_id + "','1','" + infoMenu.menu_id + "')";
			sqlV.add(strSql);

		}
		if (infoMenu.parent_id == null || "".equals(infoMenu.parent_id)) {
			infoMenu.parent_id = "0";
		}
		// update
		String upStr = getUpdateFiledStr(infoMenu, menu);

		if (upStr.length() > 0) {
			String sqlC = "update UI_INFO_MENU set " + upStr
					+ " where menu_id ='" + infoMenu.menu_id + "'";
			sqlV.add(sqlC);
		}

		// 转化
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			System.out.println("sql==================" + sqlArr[i]);
		}

		int re = 1;

		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			re = -1;
		}

		if (re > 0) {
			String url = "MenuView.screen?submitType=1&menu_id=" + menu_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "菜单信息新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "菜单信息新增失败！");
		}

	}

	/**
	 * 变更菜单信息
	 *
	 * @param session
	 * @param infoMenu
	 * @throws HTMLActionException
	 */
	public static void updateMenuInfo(HttpServletRequest request,
			InfoMenuTable infoMenu) throws HTMLActionException {
		HttpSession session = request.getSession();
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		String menu_id = infoMenu.menu_id;
		String parent_id = infoMenu.parent_id;

		Vector sqlV = new Vector();
		//
		InfoMenuTable menu = getMenuInfo(menu_id);
		//

		if (isDupMenuName(menu_id, parent_id, infoMenu.menu_name,
				user.system_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "同一父菜单下子菜单名称重复！");
		}

		// update
		String upStr = getUpdateFiledStr(infoMenu, menu);
		if (upStr.length() > 0) {
			String sqlC = "update UI_INFO_MENU set " + upStr
					+ " where menu_id ='" + menu_id + "'";
			sqlV.add(sqlC);
			String log_sql_A = getLogSql(request, "I2504", "2", menu_id);
			sqlV.add(log_sql_A);

		}

		// 判断子系统状态标志
		String sqlmenu = "update UI_INFO_MENU set status ='" + infoMenu.status
				+ "' where menu_id in( " + " SELECT distinct m.menu_id "
				+ " FROM UI_INFO_MENU m " + " START WITH m.menu_id='" + menu_id
				+ "' " + " CONNECT BY PRIOR m.menu_id =  m.parent_id)";
		@SuppressWarnings("unused")
		String logmenu = "";
		if (!infoMenu.status.trim().equals(menu.status.trim())) {
			// 变更菜单状态
			try {
				sqlmenu = SQLGenator.genSQL("C2807", infoMenu.status, menu_id);
				// logmenu = getLogSql(request, "I2814", "2", menu_id);
			} catch (AppException e) {

				e.printStackTrace();
			}
			sqlV.add(sqlmenu);
			// sqlV.add(logmenu);
		}

		// 转化
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			System.out.println("sql==================" + sqlArr[i]);
		}

		int re = 1;

		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			e.printStackTrace();
			re = -1;
		}

		if (re > 0) {
			String url = "MenuView.screen?submitType=1&menu_id=" + menu_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "菜单信息变更成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "菜单信息变更失败！");
		}

	}

	/**
	 * 删除菜单信息
	 *
	 * @param session
	 * @param infoMenu
	 * @throws HTMLActionException
	 */
	public static void deleteMenuInfo(HttpServletRequest request,
			InfoMenuTable infoMenu) throws HTMLActionException {
		HttpSession session = request.getSession();
		String menu_id = infoMenu.menu_id;
		if (hasSonMenu(menu_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该菜单下存在下级菜单，请先删除下级菜单！");
		}
		Vector sqlv = new Vector();

		// 删除该菜单所有的角色权限
		/*
		 * String log_sql_c = getLogSql(request, "I2502", "3", "M", menu_id);
		 * sqlv.add(log_sql_c);
		 */
		//
		String sqlc = "delete from UI_RULE_RES_ROLE where res_id = '" + menu_id
				+ "'";
		sqlv.add(sqlc);

		// 删除该菜单所有的用户权限
		/*
		 * String log_sql_o=getLogSql(request, "I2938", "3", "M", menu_id);
		 * sqlv.add(log_sql_o);
		 */

		// 菜单日志
		/*
		 * String log_sql_d = getLogSql(request, "I2504", "3", menu_id);
		 * sqlv.add(log_sql_d);
		 */

		// 删除菜单信息表
		String sqlo = "delete from UI_INFO_MENU where menu_id = '" + menu_id
				+ "'";
		sqlv.add(sqlo);

		// 转化
		String sqlArr[] = new String[sqlv.size()];
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i] = sqlv.get(i).toString();
			System.out.println("sqlArr[i]=============" + sqlArr[i]);
		}
		// 执行
		int re = 1;

		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			e.printStackTrace();
			re = -1;
		}

		// 执行结束

		if (re > 0) {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "菜单信息删除成功！",
					"MenuView.screen?submitType=2");
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "菜单信息删除失败！");
		}

	}

	/**
	 * 判断同一模块是否有相同名称的菜单
	 *
	 * @param menu_id
	 * @param module_code
	 * @param menu_name
	 * @return
	 */
	public static boolean isDupMenuName(String menu_id, String parent_id,
			String menu_name, String system_id) {
		String sql = "";
		boolean flag = false;
		try {
			if ("".equals(parent_id) || parent_id == null) {
				sql = "SELECT 1 FROM UI_INFO_MENU  WHERE EXISTS (SELECT menu_id FROM UI_INFO_MENU WHERE menu_id !='"
						+ menu_id
						+ "'  "
						+ "and parent_id is null and system_id='"
						+ system_id
						+ "' AND menu_name= trim('" + menu_name + "'))";
			} else {
				sql = "SELECT 1 FROM UI_INFO_MENU  WHERE EXISTS (SELECT menu_id FROM UI_INFO_MENU WHERE menu_id !='"
						+ menu_id
						+ "' "
						+ "and parent_id ='"
						+ parent_id
						+ "' and system_id='"
						+ system_id
						+ "'  AND menu_name = trim('" + menu_name + "') )";
			}
			//
			// System.out.println(sql);
			//
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = true;
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * 取得变更语句
	 *
	 * @param newMenuInfo
	 * @param oldMenuInfo
	 * @return
	 */
	private static String getUpdateFiledStr(InfoMenuTable newMenuInfo,
			InfoMenuTable oldMenuInfo) {
		String upStr = "";

		//
		// System.out.println(newMenuInfo.parent_id + ":" +
		// oldMenuInfo.parent_id);
		if (!newMenuInfo.parent_id.equals(oldMenuInfo.parent_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}

			upStr += " parent_id = '" + newMenuInfo.parent_id + "'";

		}
		//
		if (!newMenuInfo.menu_name.equals(oldMenuInfo.menu_name)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " menu_name = '" + newMenuInfo.menu_name + "'";

		}

		if (!newMenuInfo.sequence.equals(oldMenuInfo.sequence)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " sequence =" + newMenuInfo.sequence;

		}

		//
		if (!newMenuInfo.menu_type.equals(oldMenuInfo.menu_type)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " menu_type = '" + newMenuInfo.menu_type + "'";

		}

		if (!newMenuInfo.menu_url.equals(oldMenuInfo.menu_url)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " menu_url = '" + newMenuInfo.menu_url + "'";

		}

		if (!newMenuInfo.status.equals(oldMenuInfo.status)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " status = " + newMenuInfo.status;

		}

		//
		if (!newMenuInfo.isshow.equals(oldMenuInfo.isshow)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " isshow = " + newMenuInfo.isshow;

		}

		//
		if (!newMenuInfo.menu_desc.equals(oldMenuInfo.menu_desc)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " menu_desc = '" + newMenuInfo.menu_desc + "'";

		}

		//
		if (!newMenuInfo.res_char1.equals(oldMenuInfo.res_char1)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " RES_CHAR1 = '" + newMenuInfo.res_char1 + "'";

		}

		//
		if (!newMenuInfo.res_char2.equals(oldMenuInfo.res_char2)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " RES_CHAR2 = '" + newMenuInfo.res_char2 + "'";

		}

		//
		if (!newMenuInfo.res_char3.equals(oldMenuInfo.res_char3)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " RES_CHAR3 = '" + newMenuInfo.res_char3 + "'";

		}

		//
		if (!newMenuInfo.res_init1.equals(oldMenuInfo.res_init1)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " res_init1 = " + newMenuInfo.res_init1;
		}

		//
		if (!newMenuInfo.res_init2.equals(oldMenuInfo.res_init2)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " res_init2 = " + newMenuInfo.res_init2;
		}

		//
		if (!newMenuInfo.res_init3.equals(oldMenuInfo.res_init3)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " res_init3 = " + newMenuInfo.res_init3;
		}

		if (!newMenuInfo.icon_url.equals(oldMenuInfo.icon_url)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " icon_url = '" + newMenuInfo.icon_url + "'";
		}

		/*
		 * if (!newMenuInfo.system_id.equals(oldMenuInfo.system_id)) { if
		 * (upStr.length() > 0) { upStr += " , "; } upStr += " system_id = " +
		 * newMenuInfo.system_id; }
		 */

		return upStr;
	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param oper_no
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_oper_no, ip, type,
					para_a);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param oper_no
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_oper_no, ip, type,
					para_a, para_b);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param oper_no
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b, String para_c) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_oper_no, ip, type,
					para_a, para_b, para_c);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

}
