package com.ailk.bi.system.common;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoResRole {

	public static String[][] getMenuRoleInfo(String user_id, String role_type,
			String menu_id, String group_id) {
		String[][] rs = null;
		StringBuffer sql = new StringBuffer();
		if ("1".equals(role_type)) { // 菜单资源
			if ("1".equals(group_id)) { // 超级用户组
				sql.append(
						" select x.role_id,x.role_name,x.comments,case when y.menu_id is not null then 'checked' else '' end as chk ")
						.append("from (select D.ROLE_ID, D.role_name, D.role_type, D.comments from UI_INFO_ROLE D where  D.create_group='1' and D.role_type = '"
								+ role_type + "')x,")
						.append("(select c.role_id, d.menu_id from ui_rule_res_role c, ui_info_menu d where d.menu_id = '"
								+ menu_id + "' and c.res_id = d.menu_id) y ")
						.append("where x.role_id=y.role_id(+) order by x.role_name");
			} else {
				sql.append(
						" select x.role_id,x.role_name,x.comments,case when y.menu_id is not null then 'checked' else '' end as chk ")
						.append("from (select C.ROLE_ID,D.role_name,D.role_type,D.comments from ui_rule_user_group B , ui_rule_group_role C,UI_INFO_ROLE D ")
						.append("where B.user_id='"
								+ user_id
								+ "' AND B.GROUP_ID = C.GROUP_ID and C.role_id=D.role_id UNION ")
						.append("select A.ROLE_ID,D.role_name,D.role_type,D.comments from ui_rule_user_ROLE A,UI_INFO_ROLE D ")
						.append("where A.user_id='" + user_id
								+ "' and A.role_id=D.role_id) x, ")
						.append("(select c.role_id, d.menu_id from ui_rule_res_role c, ui_info_menu d where d.menu_id = '"
								+ menu_id + "' and c.res_id = d.menu_id) y ")
						.append("where x.role_id=y.role_id(+) and x.role_type='"
								+ role_type + "' ")
						.append("order by x.role_name");
			}

		} else if ("2".equals(role_type)) { // 地域资源
			if ("1".equals(group_id)) {
				sql.append(
						" select x.role_id,x.role_name,x.comments,case when y.region_id is not null then 'checked' else '' end as chk ")
						.append("from (select D.ROLE_ID, D.role_name, D.role_type, D.comments from UI_INFO_ROLE D where  D.create_group='1' and D.role_type = '"
								+ role_type + "')x,")
						.append("(select c.role_id,d.region_id from ui_rule_res_role c,ui_info_region d ")
						.append("where d.region_id= '" + menu_id
								+ "' and c.res_id=d.region_id) y ")
						.append("where x.role_id=y.role_id(+) order by x.role_name");
			} else {
				sql.append(
						" select x.role_id,x.role_name,x.comments,case when y.region_id is not null then 'checked' else '' end as chk ")
						.append("from (select C.ROLE_ID,D.role_name,D.role_type,D.comments from ui_rule_user_group B , ui_rule_group_role C,UI_INFO_ROLE D ")
						.append("where B.user_id='"
								+ user_id
								+ "' AND B.GROUP_ID = C.GROUP_ID and C.role_id=D.role_id UNION ")
						.append("select A.ROLE_ID,D.role_name,D.role_type,D.comments from ui_rule_user_ROLE A,UI_INFO_ROLE D ")
						.append("where A.user_id='" + user_id
								+ "' and A.role_id=D.role_id) x, ")
						.append("(select c.role_id,d.region_id from ui_rule_res_role c,ui_info_region d ")
						.append("where d.region_id= '" + menu_id
								+ "' and c.res_id=d.region_id) y ")
						.append("where x.role_id=y.role_id(+) and x.role_type='"
								+ role_type + "' order by x.role_name");
			}
		}

		System.out.println(sql);
		try {
			rs = WebDBUtil.execQryArray(sql.toString(), "");
		} catch (AppException e) {
			e.printStackTrace();
		}

		return rs;
	}

	public static void saveRoleInfo(HttpServletRequest request, String res_id,
			String role_ids, String res_type) throws HTMLActionException {
		HttpSession session = request.getSession();
		Vector sqlv = new Vector();

		String log_sql1 = getLogSql(request, "I3000", "3", res_id);
		sqlv.add(log_sql1);

		String sqlA = " delete from ui_rule_res_role where res_id='" + res_id
				+ "' and role_id not in ('1','2')";
		sqlv.add(sqlA);
		String[] role_id = role_ids.split(",");
		String msg = "";
		String val = "";
		for (int i = 0; i < role_id.length; i++) {
			String sqlC = "";
			if ("1".equals(res_type)) {
				sqlC = "insert into ui_rule_res_role values ('" + role_id[i]
						+ "','" + res_type + "','" + res_id + "','M')";
				msg = "菜单";
				val = "menu_id";
			} else {
				sqlC = "insert into ui_rule_res_role values ('" + role_id[i]
						+ "','" + res_type + "','" + res_id + "','')";
				msg = "区域";
				val = "region_id";
			}
			sqlv.add(sqlC);
		}
		String log_sql2 = getLogSql(request, "I3000", "1", res_id);
		sqlv.add(log_sql2);
		String sqlArr[] = new String[sqlv.size()];
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i] = sqlv.get(i).toString();
			System.out.println("sql[i]================" + sqlArr[i]);
		}
		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, msg + "赋权成功！",
					"resRole.screen?" + val + "=" + res_id);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, msg + "赋权失败！");
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
			String type, String para_a) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_user_id = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_user_id, ip, type,
					para_a);
			System.out.println(sql);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}
}
