/**
 *
 */
package com.ailk.bi.system.common;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoRoleTable;
import com.ailk.bi.base.table.InfoUserGroupTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

/**
 * @author jcm
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoRole {

	/**
	 * 查询取得角色信息
	 *
	 * @param role_id
	 * @return InfoRoleTable 对象
	 * @author jcm
	 */
	public static InfoRoleTable getRoleInfo(String role_id) {

		InfoRoleTable inforole = null;
		//
		String sql = "select ROLE_ID,ROLE_NAME,ROLE_TYPE,STATUS,REGION_ID,DEPT_ID,COMMENTS,SEQUENCE from UI_INFO_ROLE where role_id ='"
				+ role_id + "' ";

		// //System.out.println(sql);
		try {
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			inforole = toInfoRole(rs);
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (inforole == null) {
			inforole = new InfoRoleTable();
		}
		return inforole;
	}

	/**
	 * 角色结果集转换为InfoRoleTable
	 *
	 * @param String
	 *            [][]
	 * @return InfoRoleTable 对象
	 * @author jcm
	 */
	private static InfoRoleTable toInfoRole(String[][] rs) {
		if (rs == null) {
			return null;
		}
		InfoRoleTable role = new InfoRoleTable();
		if (rs.length > 0) {
			role.role_id = rs[0][0];
			role.role_name = rs[0][1];
			role.role_type = rs[0][2];
			role.status = rs[0][3];
			role.region_id = rs[0][4];
			role.dept_id = rs[0][5];
			role.comments = rs[0][6];
			role.sequence = rs[0][7];

		}
		return role;
	}

	/**
	 * 判断角色名称是否重复
	 *
	 * @param role_id
	 * @param role_name
	 * @return boolean
	 * @author jcm
	 */
	public static boolean isDupRoleName(String role_id, String role_name) {

		boolean flag = false;
		String sql = "SELECT 1 FROM UI_INFO_ROLE WHERE EXISTS (SELECT role_id FROM UI_INFO_ROLE WHERE role_id != '"
				+ role_id + "' AND role_name = trim('" + role_name + "'))";

		// //System.out.println(sql);
		Vector v = new Vector();
		try {
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (v.size() > 0) {
			flag = true;
		}
		return flag;

	}

	/**
	 * 新增角色信息
	 *
	 * @param session
	 * @param roleInfo
	 * @throws HTMLActionException
	 * @author jcm
	 */
	public static void addNewRole(HttpServletRequest request,
			InfoRoleTable roleInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		// 取得传递参数
		String role_id = roleInfo.role_id;
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);

		Vector sqlV = new Vector();
		// 取得当前角色信息
		InfoRoleTable role = getRoleInfo(role_id);

		// 判断角色代码是否已经存在
		if (role != null && !role.role_id.trim().equals("")) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "<B>角色代码已经存在！</B>");
		}

		// 判断角色名称是否已经存在
		if (!role.role_name.equals(roleInfo.role_name)) {
			if (isDupRoleName(role_id, roleInfo.role_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "<B>角色名称重复!</B>");

			}
		}
		// 此处统一处理新增角色ID
		// 有多种方式: 1 数据库序列
		// 2 自定标识表等
		// String tmpID = CommTool.dbGetMaxIDBySeq("INFO_ROLE");

		// 统一新增固定角色信息！
		/*
		 * if(role_id == null || "".equals(role_id) ) { role_id =
		 * CommonUtil.format(CommonUtil.getNextVal("seq_info_role")); }
		 */
		// 角色基本信息
		String sqla = "insert into UI_INFO_ROLE(ROLE_ID,ROLE_NAME,ROLE_TYPE,STATUS,REGION_ID,DEPT_ID,COMMENTS,SEQUENCE,create_group,system_id) "
				+ "values('"
				+ role_id
				+ "','"
				+ roleInfo.role_name
				+ "','"
				+ roleInfo.role_type
				+ "','"
				+ roleInfo.status
				+ "','"
				+ user.region_id
				+ "','"
				+ user.dept_id
				+ "','"
				+ roleInfo.comments
				+ "',SEQ_INFO_ROLE.nextval,'"
				+ user.group_id + "','" + user.system_id + "')";
		sqlV.add(sqla);

		// 比较取得变更字段字符串
		String upStr = getUpdateFiledStr(roleInfo, role);
		if (upStr.length() > 0) {
			String sqlc = "UPDATE UI_INFO_ROLE SET " + upStr
					+ "   WHERE role_id= '" + role_id + "'";
			sqlV.add(sqlc);
		}

		String sql = getLogSql(request, "I2115", "1", role_id);
		sqlV.add(sql);
		// String sql_b = getLogSql(request, "I2116", "1", role_id);
		// sqlV.add(sql_b);
		// 转换
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			// //System.out.println(sqlArr[i]);
		}
		//
		int i = -1;
		try {
			i = 1;
			WebDBUtil.execTransUpdate(sqlArr);

		} catch (AppException e) {

			e.printStackTrace();
		}
		if (i > 0) {
			String url = "RoleView.screen?submitType=3&role_id=" + role_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "角色信息新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "角色信息新增失败！");
		}

	}

	/**
	 * 变更角色信息
	 *
	 * @param session
	 * @param roleInfo
	 * @throws HTMLActionException
	 * @author jcm
	 */
	public static void updateRoleInfo(HttpServletRequest request,
			InfoRoleTable roleInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		// 取得传递参数
		// String role_id = roleInfo.role_id;
		String oldRoleId = request.getParameter("hidRoleId");
		Vector sqlV = new Vector();

		// 取得当前角色信息
		InfoRoleTable role = getRoleInfo(oldRoleId);
		// String oldRoleId = request.getParameter("hidRoleId");

		// 判断角色代码是否改变
		/*
		 * if (!role.role_id.equals(roleInfo.role_id)) { throw new
		 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
		 * "角色代码不能修改！"); }
		 */
		// 判断角色名称是否相同
		if (!role.role_name.equals(roleInfo.role_name)) {
			if (isDupRoleName(oldRoleId, roleInfo.role_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "<B>角色名称重复!</B>");
			}
		}

		// 比较取得变更字段字符串
		String upStr = getUpdateFiledStr(roleInfo, role);
		String sqlc = "UPDATE UI_INFO_ROLE SET " + upStr
				+ "   WHERE role_id = '" + oldRoleId + "'";
		if (upStr.length() > 0) {
			sqlV.add(sqlc);
		}

		String sql = getLogSql(request, "I2115", "2", roleInfo.role_id);
		sqlV.add(sql);

		if (!roleInfo.role_id.equals(oldRoleId)) {
			sql = "update ui_rule_adhoc_role set role_id='" + roleInfo.role_id
					+ "' WHERE role_id = '" + oldRoleId + "'";
			sqlV.add(sql);

			sql = "update ui_rule_group_role set role_id='" + roleInfo.role_id
					+ "' WHERE role_id = '" + oldRoleId + "'";
			sqlV.add(sql);

			sql = "update ui_rule_res_role set role_id='" + roleInfo.role_id
					+ "' WHERE role_id = '" + oldRoleId + "'";
			sqlV.add(sql);

			sql = "update ui_rule_user_role set role_id='" + roleInfo.role_id
					+ "' WHERE role_id = '" + oldRoleId + "'";
			sqlV.add(sql);
		}

		// 转换
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			System.out.println("updateRoleInfo [" + i + "]" + sqlArr[i]);
		}
		//
		int i = -1;
		try {
			i = 1;
			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}
		} catch (AppException e) {
			i = -1;
			e.printStackTrace();
		}
		if (i > 0) {
			String url = "RoleView.screen?submitType=3&role_id="
					+ roleInfo.role_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "角色信息变更成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "角色信息变更失败！");
		}
	}

	/**
	 * 删除角色相关及本身信息
	 *
	 * @param session
	 * @param roleInfo
	 * @throws HTMLActionException
	 *             （1）删除角色权限 （2）删除角色区域 （3）删除角色信息 有相关用户的角色不能删除
	 */
	public static void deleteRoleInfo(HttpServletRequest request,
			InfoRoleTable roleInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		String role_id = roleInfo.role_id;
		Vector sqlv = new Vector();
		// 判断
		if (roleReferenced(role_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "已经为该角色分配了操作员，不可以删除！");

		}
		// 删除角色的相关功能权限
		String log_sql_a = getLogSql(request, "I2117", "3", role_id);
		sqlv.add(log_sql_a);
		String sqla = "delete from UI_RULE_RES_ROLE where role_id = '"
				+ role_id + "'";
		sqlv.add(sqla);

		sqla = "delete ui_rule_adhoc_role WHERE role_id = '" + role_id + "'";
		sqlv.add(sqla);

		sqla = "delete ui_rule_group_role WHERE role_id = '" + role_id + "'";
		sqlv.add(sqla);

		sqla = "delete ui_rule_res_role WHERE role_id = '" + role_id + "'";
		sqlv.add(sqla);

		sqla = "delete ui_rule_user_role WHERE role_id = '" + role_id + "'";
		sqlv.add(sqla);

		// 删除角色本身
		String sqle = "";
		try {
			// sqle = SQLGenator.genSQL("C2114", WebConstKeys.INACTIVE,
			// role_id);//标记删除
			sqle = "delete from UI_INFO_ROLE where role_id='" + role_id + "'";// 实体删除，为符合总部规范1.0.5版本要求
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		sqlv.add(sqle);
		String log_sql_c = getLogSql(request, "I2115", "3", role_id);
		sqlv.add(log_sql_c);

		String sqlArr[] = new String[sqlv.size()];
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i] = sqlv.get(i).toString();
			// //System.out.println("sqlArr[i]====="+sqlArr[i]);
		}
		// 执行
		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}
		if (i > 0) {
			String url = "RoleView.screen?submitType=3&role_id=" + role_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "角色信息删除成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "角色信息删除失败！");
		}

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

	/**
	 * 判断当前角色是否有相关联的用户
	 *
	 * @param role_id
	 * @return flag
	 * @author jcm
	 */
	public static boolean roleReferenced(String role_id) {
		String sql = "select role_id,user_id from UI_RULE_USER_ROLE where role_id ='"
				+ role_id + "'";
		boolean flag = false;
		try {

			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v.size() > 0) {
				flag = true;
			}

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * 更新角色实体变更信息（名称，本地网，描述，等级）
	 *
	 * @param newrole
	 *            新的角色实体
	 * @param oldrole
	 *            老的角色实体
	 * @return 变更字符串
	 * @author jcm
	 */
	private static String getUpdateFiledStr(InfoRoleTable newrole,
			InfoRoleTable oldrole) {
		String upStr = "";

		if (!newrole.role_id.equals(oldrole.role_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " role_id = '" + newrole.role_id + "'";

		}

		// 角色名称
		if (!newrole.role_name.equals(oldrole.role_name)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " role_name = '" + newrole.role_name + "'";

		}
		// 角色描述
		if (!newrole.comments.equals(oldrole.comments)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " comments = '" + newrole.comments + "'";
		}

		// 角色类型
		if (!newrole.role_type.equals(oldrole.role_type)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " role_type = '" + newrole.role_type + "'";
		}

		// 角色状态
		if (!newrole.status.equals(oldrole.status)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " status = '" + newrole.status + "'";
		}

		if (!newrole.dept_id.equals(oldrole.dept_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " dept_id = '" + newrole.dept_id + "'";
		}

		return upStr;
	}

	/**
	 * 保存角色资源权限信息
	 *
	 * @param request
	 * @param list
	 * @param role_id
	 * @return
	 */
	public static int saveMenuInfo(HttpServletRequest request, String list,
			String role_id) {
		//
		int tag = -1;
		Vector sqlv = new Vector();
		String[] arr = list.split(",");

		Vector s_arr = new Vector(); // 增加子系统
		Vector d_arr = new Vector(); // 增加模块
		Vector m_arr = new Vector(); // 增加菜单
		Vector s_arr_d = new Vector(); // 删除子系统
		Vector d_arr_d = new Vector(); // 删除模块
		Vector m_arr_d = new Vector(); // 删除菜单

		// 取得现有权限
		HashMap rights = getRoleRights(role_id);
		// 现有权限为空,直接增加
		if (rights == null || rights.isEmpty()) { // 增加
			//
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].startsWith("S_")) {
					s_arr.add(arr[i].substring(2));
				} else if (arr[i].startsWith("D_")) {
					d_arr.add(arr[i].substring(2));
				} else if (arr[i].startsWith("M_")) {
					m_arr.add(arr[i].substring(2));
				}
			}

			// 增加子系统
			String sql_s_arr[] = new String[s_arr.size()];
			String log_sql_s[] = new String[s_arr.size()];

			for (int i = 0; i < s_arr.size(); i++) {

				sql_s_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id,flag) values('"
						+ role_id
						+ "','1','"
						+ s_arr.get(i).toString()
						+ "','S')";

				// log_sql_s[i] = getLogSql(request, "I2503", "1", role_id,"S",
				// s_arr.get(i).toString());

			}
			// 增加模块
			String sql_d_arr[] = new String[d_arr.size()];
			String log_sql_d[] = new String[d_arr.size()];
			for (int i = 0; i < d_arr.size(); i++) {

				sql_d_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id,flag) values('"
						+ role_id
						+ "','1','"
						+ d_arr.get(i).toString()
						+ "','D')";

				// log_sql_d[i] = getLogSql(request, "I2503", "1", role_id,"D",
				// d_arr.get(i).toString());

			}
			// 增加菜单
			String sql_m_arr[] = new String[m_arr.size()];
			String log_sql_m[] = new String[m_arr.size()];
			for (int i = 0; i < m_arr.size(); i++) {

				sql_m_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id,flag) values('"
						+ role_id
						+ "','1','"
						+ m_arr.get(i).toString()
						+ "','M')";

				log_sql_m[i] = getLogSql(request, "I2503", "1", role_id, "1",
						m_arr.get(i).toString());

			}
			// add
			if (sql_s_arr.length > 0) {
				for (int i = 0; i < sql_s_arr.length; i++) {
					sqlv.add(sql_s_arr[i]);
				}
			}
			if (sql_d_arr.length > 0) {
				for (int i = 0; i < sql_d_arr.length; i++) {
					sqlv.add(sql_d_arr[i]);
				}
			}
			if (sql_m_arr.length > 0) {
				for (int i = 0; i < sql_m_arr.length; i++) {
					sqlv.add(sql_m_arr[i]);
				}
			}
			if (log_sql_s.length > 0) {
				for (int i = 0; i < log_sql_s.length; i++) {
					sqlv.add(log_sql_s[i]);
				}
			}
			if (log_sql_d.length > 0) {
				for (int i = 0; i < log_sql_d.length; i++) {
					sqlv.add(log_sql_d[i]);
				}
			}
			if (log_sql_m.length > 0) {
				for (int i = 0; i < log_sql_m.length; i++) {
					sqlv.add(log_sql_m[i]);
				}
			}

		} else {
			// 判断增加，删除
			// 已经存在权限处理
			// 判断增加权限
			Vector add_v = new Vector();
			for (int i = 0; i < arr.length; i++) {
				if (!rights.containsKey(arr[i])) {
					add_v.add(arr[i]);
					// //System.out.println("arr[i]======"+arr[i]);
				}
			}
			// 判断删除权限
			Vector del_v = new Vector();
			HashMap new_map = new HashMap();
			for (int i = 0; i < arr.length; i++) {
				new_map.put(arr[i], arr[i]);
			}
			//
			Object[] tmpArr = rights.values().toArray();
			for (int i = 0; i < tmpArr.length; i++) {
				if (!new_map.containsKey(tmpArr[i].toString())) {
					del_v.add(tmpArr[i].toString());
					// //System.out.println("tmpArr[i]======"+tmpArr[i]);
				}
			}
			// 增加处理
			for (int i = 0; i < add_v.size(); i++) {
				if (add_v.get(i).toString().startsWith("S_")) {
					s_arr.add(add_v.get(i).toString().substring(2));
				} else if (add_v.get(i).toString().startsWith("D_")) {
					d_arr.add(add_v.get(i).toString().substring(2));
				} else if (add_v.get(i).toString().startsWith("M_")) {
					m_arr.add(add_v.get(i).toString().substring(2));
				}

			}

			// 增加子系统
			String sql_s_arr[] = new String[s_arr.size()];
			String log_sql_s[] = new String[s_arr.size()];

			for (int i = 0; i < s_arr.size(); i++) {

				sql_s_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id,flag) values('"
						+ role_id
						+ "','1','"
						+ s_arr.get(i).toString()
						+ "','S')";

				// log_sql_s[i] = getLogSql(request, "I2503", "1", role_id,"S",
				// s_arr.get(i).toString());

			}
			// 增加模块
			String sql_d_arr[] = new String[d_arr.size()];
			String log_sql_d[] = new String[d_arr.size()];
			for (int i = 0; i < d_arr.size(); i++) {

				sql_d_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id,flag) values('"
						+ role_id
						+ "','1','"
						+ d_arr.get(i).toString()
						+ "','D')";

				// log_sql_d[i] = getLogSql(request, "I2503", "1", role_id,"D",
				// d_arr.get(i).toString());

			}
			// 增加菜单
			String sql_m_arr[] = new String[m_arr.size()];
			String log_sql_m[] = new String[m_arr.size()];
			for (int i = 0; i < m_arr.size(); i++) {

				sql_m_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id,flag) values('"
						+ role_id
						+ "','1','"
						+ m_arr.get(i).toString()
						+ "','M')";

				log_sql_m[i] = getLogSql(request, "I2503", "1", role_id, "1",
						m_arr.get(i).toString());

			}
			// add
			if (sql_s_arr.length > 0) {
				for (int i = 0; i < sql_s_arr.length; i++) {
					sqlv.add(sql_s_arr[i]);
				}
			}
			if (sql_d_arr.length > 0) {
				for (int i = 0; i < sql_d_arr.length; i++) {
					sqlv.add(sql_d_arr[i]);
				}
			}
			if (sql_m_arr.length > 0) {
				for (int i = 0; i < sql_m_arr.length; i++) {
					sqlv.add(sql_m_arr[i]);
				}
			}
			if (log_sql_s.length > 0) {
				for (int i = 0; i < log_sql_s.length; i++) {
					sqlv.add(log_sql_s[i]);
				}
			}
			if (log_sql_d.length > 0) {
				for (int i = 0; i < log_sql_d.length; i++) {
					sqlv.add(log_sql_d[i]);
				}
			}
			if (log_sql_m.length > 0) {
				for (int i = 0; i < log_sql_m.length; i++) {
					sqlv.add(log_sql_m[i]);
				}
			}

			// 处理删除
			for (int i = 0; i < del_v.size(); i++) {
				if (del_v.get(i).toString().startsWith("S_")) {
					s_arr_d.add(del_v.get(i).toString().substring(2));
				} else if (del_v.get(i).toString().startsWith("D_")) {
					d_arr_d.add(del_v.get(i).toString().substring(2));
				} else if (del_v.get(i).toString().startsWith("M_")) {
					m_arr_d.add(del_v.get(i).toString().substring(2));
				}

			}

			//
			String sql_s_arr_D[] = new String[s_arr_d.size()];
			String log_sql_SD[] = new String[s_arr_d.size()];
			for (int i = 0; i < s_arr_d.size(); i++) {

				// log_sql_SD[i] = getLogSql(request, "I2503", "3", role_id,"S",
				// s_arr_d.get(i).toString());
				sql_s_arr_D[i] = "delete from UI_RULE_RES_ROLE where role_id ='"
						+ role_id
						+ "' and res_type ='1' and res_id='"
						+ s_arr_d.get(i).toString() + "'";

			}

			String sql_d_arr_D[] = new String[d_arr_d.size()];
			String log_sql_DD[] = new String[d_arr_d.size()];
			for (int i = 0; i < d_arr_d.size(); i++) {

				// log_sql_DD[i] = getLogSql(request, "I2503", "3", role_id,"D",
				// d_arr_d.get(i).toString());
				sql_d_arr_D[i] = "delete from UI_RULE_RES_ROLE where role_id ='"
						+ role_id
						+ "' and res_type ='1' and res_id='"
						+ d_arr_d.get(i).toString() + "'";

			}

			String sql_m_arr_D[] = new String[m_arr_d.size()];
			String log_sql_MD[] = new String[m_arr_d.size()];
			for (int i = 0; i < m_arr_d.size(); i++) {

				log_sql_MD[i] = getLogSql(request, "I2503", "3", role_id, "1",
						m_arr_d.get(i).toString());
				sql_m_arr_D[i] = "delete from UI_RULE_RES_ROLE where role_id ='"
						+ role_id
						+ "' and res_type ='1' and res_id='"
						+ m_arr_d.get(i).toString() + "'";

			}
			// add
			if (log_sql_SD.length > 0) {
				for (int i = 0; i < log_sql_SD.length; i++) {
					sqlv.add(log_sql_SD[i]);
				}
			}
			if (log_sql_DD.length > 0) {
				for (int i = 0; i < log_sql_DD.length; i++) {
					sqlv.add(log_sql_DD[i]);
				}
			}
			if (log_sql_MD.length > 0) {
				for (int i = 0; i < log_sql_MD.length; i++) {
					sqlv.add(log_sql_MD[i]);
				}
			}

			if (sql_s_arr_D.length > 0) {
				for (int i = 0; i < sql_s_arr_D.length; i++) {
					sqlv.add(sql_s_arr_D[i]);
				}
			}
			if (sql_d_arr_D.length > 0) {
				for (int i = 0; i < sql_d_arr_D.length; i++) {
					sqlv.add(sql_d_arr_D[i]);
				}
			}
			if (sql_m_arr_D.length > 0) {
				for (int i = 0; i < sql_m_arr_D.length; i++) {
					sqlv.add(sql_m_arr_D[i]);
				}
			}

		}

		// 统一执行
		//

		int count = 0;
		for (int j = 0; j < sqlv.size(); j++) {
			if (sqlv.get(j) != null) {
				++count;
			}
		}
		String sqlArr[] = new String[count];
		int k = 0;
		for (int j = 0; j < sqlv.size(); j++) {
			if (sqlv.get(j) != null) {
				sqlArr[k++] = sqlv.get(j).toString();
			}
			// //System.out.println(sqlArr[j]);
		}
		// 执行
		try {
			tag = 1;
			if (sqlv.size() > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tag = -1;
		}
		return tag;

	}

	public static int saveMenuInfo(HttpServletRequest request, String selStr,
			String delStr, String role_id) {
		Vector sqlv = new Vector();
		Vector s_arr = new Vector(); // 增加子系统
		Vector d_arr = new Vector(); // 增加模块
		Vector m_arr = new Vector(); // 增加菜单
		Vector s_arr_d = new Vector(); // 删除子系统
		Vector d_arr_d = new Vector(); // 删除模块
		Vector m_arr_d = new Vector(); // 删除菜单
		String[] delarr = delStr.split(",");
		String[] selarr = selStr.split(",");
		for (int i = 0; i < selarr.length; i++) {
			if (selarr[i].startsWith("S_")) {
				s_arr.add(selarr[i].substring(2));
			} else if (selarr[i].startsWith("D_")) {
				d_arr.add(selarr[i].substring(2));
			} else if (selarr[i].startsWith("M_")) {
				m_arr.add(selarr[i].substring(2));
			}
		}
		for (int i = 0; i < delarr.length; i++) {
			if (delarr[i].startsWith("S_")) {
				s_arr_d.add(delarr[i].substring(2));
			} else if (delarr[i].startsWith("D_")) {
				d_arr_d.add(delarr[i].substring(2));
			} else if (delarr[i].startsWith("M_")) {
				m_arr_d.add(delarr[i].substring(2));
			}
		}
		// 增加子系统
		String sql_s_arr[] = new String[s_arr.size()];
		String log_sql_s[] = new String[s_arr.size()];

		for (int i = 0; i < s_arr.size(); i++) {

			sql_s_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id) values('"
					+ role_id + "','1','" + s_arr.get(i).toString() + "')";

			// log_sql_s[i] = getLogSql(request, "I2503", "1", role_id, "S",
			// s_arr.get(i).toString());

		}
		// 增加模块
		String sql_d_arr[] = new String[d_arr.size()];
		String log_sql_d[] = new String[d_arr.size()];
		for (int i = 0; i < d_arr.size(); i++) {

			sql_d_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id) values('"
					+ role_id + "','1','" + d_arr.get(i).toString() + "')";

			// log_sql_d[i] = getLogSql(request, "I2503", "1", role_id,
			// "D",d_arr.get(i).toString());

		}
		// 增加菜单
		String sql_m_arr[] = new String[m_arr.size()];
		String log_sql_m[] = new String[m_arr.size()];
		for (int i = 0; i < m_arr.size(); i++) {

			sql_m_arr[i] = "insert into UI_RULE_RES_ROLE(role_id ,res_type,res_id) values('"
					+ role_id + "','1','" + m_arr.get(i).toString() + "')";

			// log_sql_m[i] = getLogSql(request, "I2503", "1", role_id,
			// "M",m_arr.get(i).toString());

		}
		// add
		if (sql_s_arr.length > 0) {
			for (int i = 0; i < sql_s_arr.length; i++) {
				sqlv.add(sql_s_arr[i]);
			}
		}
		if (sql_d_arr.length > 0) {
			for (int i = 0; i < sql_d_arr.length; i++) {
				sqlv.add(sql_d_arr[i]);
			}
		}
		if (sql_m_arr.length > 0) {
			for (int i = 0; i < sql_m_arr.length; i++) {
				sqlv.add(sql_m_arr[i]);
			}
		}
		if (log_sql_s.length > 0) {
			for (int i = 0; i < log_sql_s.length; i++) {
				sqlv.add(log_sql_s[i]);
			}
		}
		if (log_sql_d.length > 0) {
			for (int i = 0; i < log_sql_d.length; i++) {
				sqlv.add(log_sql_d[i]);
			}
		}
		if (log_sql_m.length > 0) {
			for (int i = 0; i < log_sql_m.length; i++) {
				sqlv.add(log_sql_m[i]);
			}
		}
		String sql_s_arr_D[] = new String[s_arr_d.size()];
		String log_sql_SD[] = new String[s_arr_d.size()];
		for (int i = 0; i < s_arr_d.size(); i++) {

			// log_sql_SD[i] = getLogSql(request, "I2503", "3", role_id,"S",
			// s_arr_d.get(i).toString());
			sql_s_arr_D[i] = "delete from UI_RULE_RES_ROLE where role_id ='"
					+ role_id + "' and res_type ='1' and res_id='"
					+ s_arr_d.get(i).toString() + "'";

		}

		String sql_d_arr_D[] = new String[d_arr_d.size()];
		String log_sql_DD[] = new String[d_arr_d.size()];
		for (int i = 0; i < d_arr_d.size(); i++) {

			// log_sql_DD[i] = getLogSql(request, "I2503", "3", role_id,"D",
			// d_arr_d.get(i).toString());
			sql_d_arr_D[i] = "delete from UI_RULE_RES_ROLE where role_id ='"
					+ role_id + "' and res_type ='1' and res_id='"
					+ d_arr_d.get(i).toString() + "'";
		}

		String sql_m_arr_D[] = new String[m_arr_d.size()];
		String log_sql_MD[] = new String[m_arr_d.size()];
		for (int i = 0; i < m_arr_d.size(); i++) {

			// log_sql_MD[i] = getLogSql(request, "I2503", "3", role_id,"M",
			// m_arr_d.get(i).toString());
			sql_m_arr_D[i] = "delete from UI_RULE_RES_ROLE where role_id ='"
					+ role_id + "' and res_type ='1' and res_id='"
					+ m_arr_d.get(i).toString() + "'";

		}
		// add
		if (log_sql_SD.length > 0) {
			for (int i = 0; i < log_sql_SD.length; i++) {
				sqlv.add(log_sql_SD[i]);
			}
		}
		if (log_sql_DD.length > 0) {
			for (int i = 0; i < log_sql_DD.length; i++) {
				sqlv.add(log_sql_DD[i]);
			}
		}
		if (log_sql_MD.length > 0) {
			for (int i = 0; i < log_sql_MD.length; i++) {
				sqlv.add(log_sql_MD[i]);
			}
		}

		if (sql_s_arr_D.length > 0) {
			for (int i = 0; i < sql_s_arr_D.length; i++) {
				sqlv.add(sql_s_arr_D[i]);
			}
		}
		if (sql_d_arr_D.length > 0) {
			for (int i = 0; i < sql_d_arr_D.length; i++) {
				sqlv.add(sql_d_arr_D[i]);
			}
		}
		if (sql_m_arr_D.length > 0) {
			for (int i = 0; i < sql_m_arr_D.length; i++) {
				sqlv.add(sql_m_arr_D[i]);
			}
		}
		int tag = 0;
		// 统一执行
		String sqlArr[] = new String[sqlv.size()];
		for (int j = 0; j < sqlv.size(); j++) {
			sqlArr[j] = sqlv.get(j).toString();
			// //System.out.println(sqlArr[j]);
		}
		// 执行
		try {
			tag = 1;
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			e.printStackTrace();
			tag = -1;
		}
		return tag;
	}

	/**
	 * 取得当前角色的权限
	 *
	 * @param role_id
	 * @return
	 */
	public static HashMap getRoleRights(String role_id) {
		HashMap map = new HashMap();
		String sql = null;
		try {
			sql = "select role_id ,res_type,res_id,flag from ui_rule_res_role where role_id ='"
					+ role_id + "'";
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					String tmp = arr[i][3] + "_" + arr[i][2];
					map.put(tmp, tmp);
				}
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return map;

	}

	/**
	 * 取得菜单资源
	 *
	 * @return
	 */
	public static String getMenuTree(String user_id, String role_id,
			String system_id) {
		StringBuffer sb = new StringBuffer();
		// 子系统和模块
		// InfoMenuTable sysList[] = getSysModuleTree(user_id, role_id);
		// 菜单
		InfoMenuTable menus[] = getAllMenuTree(user_id, role_id, system_id);
		//
		// String sql = null;
		// String info = "您可以选择叶子节点进行界面元素的权限设置！";
		// 根节点
		sb.append("var tree = new WebFXTree('系统菜单资源');\n");
		sb.append("tree.setBehavior('classic');\n");
		/*
		 * // 子系统模块 for (int i = 0; i < sysList.length; i++) { if
		 * ("1".equals(sysList[i].catid)) { // sys
		 *
		 * String SYS_ID = "S_" + sysList[i].sys_code; String tag =
		 * (sysList[i].role_id == null || "".equals(sysList[i].role_id)) ?
		 * "false" : "true"; sb.append("var " + SYS_ID +
		 * " = new WebFXCheckBoxTreeItem('"+ sysList[i].sys_name + "','','" +
		 * SYS_ID + "'," + tag + ");\n"); sb.append("tree.add(" + SYS_ID +
		 * ");\n");
		 *
		 * } else if ("2".equals(sysList[i].catid)) { // module
		 *
		 * String SYS_ID = "S_" + sysList[i].sys_code; String MODULE_ID = "D_" +
		 * sysList[i].module_code; String tag = (sysList[i].role_id == null ||
		 * "".equals(sysList[i].role_id)) ? "false" : "true"; sb.append("var " +
		 * MODULE_ID + " = new WebFXCheckBoxTreeItem('" + sysList[i].module_name
		 * + "','','" + MODULE_ID + "'," + tag + ");\n"); sb.append(SYS_ID +
		 * ".add(" + MODULE_ID + ");\n");
		 *
		 * } }
		 */
		// 菜单
		for (int i = 0; i < menus.length; i++) {

			if ("3".equals(menus[i].catid)) { // menu
				if (null == menus[i].parent_id
						|| "0".equals(menus[i].parent_id)) { //
					// 一级菜单

					String MENU_ID = "M_" + menus[i].menu_id;
					String tag = (menus[i].role_id == null || ""
							.equals(menus[i].role_id)) ? "false" : "true";
					boolean flag = LSInfoMenu.hasSonMenu(menus[i].menu_id);
					if (!flag) {
						sb.append("var " + MENU_ID
								+ " = new WebFXCheckBoxTreeItem('"
								+ menus[i].menu_name + "','','" + MENU_ID
								+ "'," + tag + ");\n");
					} else {
						sb.append("var " + MENU_ID
								+ " = new WebFXCheckBoxTreeItem('"
								+ menus[i].menu_name + "','','" + MENU_ID
								+ "'," + tag + ");\n");
					}
					sb.append("tree.add(" + MENU_ID + ");\n");
					// 二级菜单
					for (int j = 0; j < menus.length; j++) {
						if (menus[j].parent_id.equals(menus[i].menu_id)) {
							String PARENT_ID_two = "M_" + menus[j].parent_id;
							String MENU_ID_two = "M_" + menus[j].menu_id;
							String tag_two = (menus[j].role_id == null || ""
									.equals(menus[j].role_id)) ? "false"
									: "true";
							boolean flag_two = LSInfoMenu
									.hasSonMenu(menus[j].menu_id);
							if (!flag_two) {
								sb.append("var " + MENU_ID_two
										+ " = new WebFXCheckBoxTreeItem('"
										+ menus[j].menu_name + "','','"
										+ MENU_ID_two + "'," + tag_two + ");\n");
							} else {
								sb.append("var " + MENU_ID_two
										+ " = new WebFXCheckBoxTreeItem('"
										+ menus[j].menu_name + "','','"
										+ MENU_ID_two + "'," + tag_two + ");\n");
							}
							sb.append(PARENT_ID_two + ".add(" + MENU_ID_two
									+ ");\n");
							// 三级菜单
							for (int m = 0; m < menus.length; m++) {
								if (menus[m].parent_id.equals(menus[j].menu_id)) {
									String PARENT_ID_three = "M_"
											+ menus[m].parent_id;
									String MENU_ID_three = "M_"
											+ menus[m].menu_id;
									String tag_three = (menus[m].role_id == null || ""
											.equals(menus[m].role_id)) ? "false"
											: "true";
									boolean flag_three = LSInfoMenu
											.hasSonMenu(menus[m].menu_id);
									if (!flag_three) {
										sb.append("var "
												+ MENU_ID_three
												+ " = new WebFXCheckBoxTreeItem('"
												+ menus[m].menu_name + "','','"
												+ MENU_ID_three + "',"
												+ tag_three + ");\n");
									} else {
										sb.append("var "
												+ MENU_ID_three
												+ " = new WebFXCheckBoxTreeItem('"
												+ menus[m].menu_name + "','','"
												+ MENU_ID_three + "',"
												+ tag_three + ");\n");
									}
									sb.append(PARENT_ID_three + ".add("
											+ MENU_ID_three + ");\n");
									// 四级菜单
									for (int n = 0; n < menus.length; n++) {
										if (menus[n].parent_id
												.equals(menus[m].menu_id)) {
											String PARENT_ID_four = "M_"
													+ menus[n].parent_id;
											String MENU_ID_four = "M_"
													+ menus[n].menu_id;
											String tag_four = (menus[n].role_id == null || ""
													.equals(menus[n].role_id)) ? "false"
													: "true";
											boolean flag_four = LSInfoMenu
													.hasSonMenu(menus[n].menu_id);
											if (!flag_four) {
												sb.append("var "
														+ MENU_ID_four
														+ " = new WebFXCheckBoxTreeItem('"
														+ menus[n].menu_name
														+ "','','"
														+ MENU_ID_four + "',"
														+ tag_four + ");\n");
											} else {
												sb.append("var "
														+ MENU_ID_four
														+ " = new WebFXCheckBoxTreeItem('"
														+ menus[n].menu_name
														+ "','','"
														+ MENU_ID_four + "',"
														+ tag_four + ");\n");
											}
											sb.append(PARENT_ID_four + ".add("
													+ MENU_ID_four + ");\n");
											// 五级级菜单
											for (int k = 0; k < menus.length; k++) {
												if (menus[k].parent_id
														.equals(menus[n].menu_id)) {
													String PARENT_ID_five = "M_"
															+ menus[k].parent_id;
													String MENU_ID_five = "M_"
															+ menus[k].menu_id;
													String tag_five = (menus[k].role_id == null || ""
															.equals(menus[k].role_id)) ? "false"
															: "true";
													boolean flag_five = LSInfoMenu
															.hasSonMenu(menus[k].menu_id);
													if (!flag_five) {
														sb.append("var "
																+ MENU_ID_five
																+ " = new WebFXCheckBoxTreeItem('"
																+ menus[k].menu_name
																+ "','','"
																+ MENU_ID_five
																+ "',"
																+ tag_five
																+ ");\n");
													} else {
														sb.append("var "
																+ MENU_ID_five
																+ " = new WebFXCheckBoxTreeItem('"
																+ menus[k].menu_name
																+ "','','"
																+ MENU_ID_five
																+ "',"
																+ tag_five
																+ ");\n");
													}
													sb.append(PARENT_ID_five
															+ ".add("
															+ MENU_ID_five
															+ ");\n");
													// 六级级菜单
													for (int t = 0; t < menus.length; t++) {
														if (menus[t].parent_id
																.equals(menus[k].menu_id)) {
															String PARENT_ID_six = "M_"
																	+ menus[t].parent_id;
															String MENU_ID_six = "M_"
																	+ menus[t].menu_id;
															String tag_six = (menus[t].role_id == null || ""
																	.equals(menus[t].role_id)) ? "false"
																	: "true";
															boolean flag_six = LSInfoMenu
																	.hasSonMenu(menus[t].menu_id);
															if (!flag_six) {
																sb.append("var "
																		+ MENU_ID_six
																		+ " = new WebFXCheckBoxTreeItem('"
																		+ menus[t].menu_name
																		+ "','','"
																		+ MENU_ID_six
																		+ "',"
																		+ tag_six
																		+ ");\n");
															} else {
																sb.append("var "
																		+ MENU_ID_six
																		+ " = new WebFXCheckBoxTreeItem('"
																		+ menus[t].menu_name
																		+ "','','"
																		+ MENU_ID_six
																		+ "',"
																		+ tag_six
																		+ ");\n");
															}
															sb.append(PARENT_ID_six
																	+ ".add("
																	+ MENU_ID_six
																	+ ");\n");

														}

													} // end of six

												}

											} // end of five

										}

									} // end of four

								}

							} // end of three

						}

					} // end of two
				}

			}

		} // end all
		sb.append("document.write(tree);\n");
		return sb.toString();
	}

	/**
	 * 取得菜单资源
	 *
	 * @return
	 */
	public static InfoMenuTable[] getAllMenuTree(String user_id,
			String role_id, String system_id) {
		InfoMenuTable menus[] = null;
		String sql = null;
		try {
			sql = SQLGenator.genSQL("Q2041", system_id, user_id, user_id,
					role_id);
			System.out.println("Q2041==================" + sql);
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				menus = new InfoMenuTable[rs.length];
				for (int i = 0; i < rs.length; i++) {
					menus[i] = new InfoMenuTable();
					menus[i].catid = rs[i][0];
					menus[i].menu_id = rs[i][1];
					menus[i].parent_id = rs[i][2];
					menus[i].menu_name = rs[i][3];
					menus[i].role_id = rs[i][4];
				}
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return menus;
	}

	/**
	 * 查询取得角色控制区域
	 *
	 * @param role_id
	 * @return Hashtable 打上标志的区域哈希表对象
	 * @author jcm
	 */
	public static Hashtable getRoleRegions(String role_id) {
		Hashtable hash = new Hashtable();
		String sql = null;
		try {
			sql = "select res_id from ui_rule_res_role where res_type='2' and role_id ='"
					+ role_id + "'";

			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				for (int i = 0; i < rs.length; i++) {
					hash.put(rs[i][0], "1");
				}
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return hash;
	}

	/**
	 * 查询取得角色相关用户数组
	 *
	 * @param role_id
	 * @return
	 * @author jcm
	 */
	public static InfoOperTable[] getRoleUsers(String role_id) {
		InfoOperTable opers[] = null;
		String sql = null;
		try {
			sql = "SELECT st.user_id, st.region_id FROM info_oper st, rule_oper_role rt  WHERE rt.role_id = '?'  AND st.user_id = rt.user_id";
			SQLGenator.genSQL("Q2110", role_id);

			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				opers = new InfoOperTable[rs.length];
				for (int i = 0; i < rs.length; i++) {
					opers[i] = new InfoOperTable();
					opers[i].user_id = rs[i][0];
					opers[i].region_id = rs[i][1];
				}
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return opers;
	}

	/**
	 * 设置角色控制区域
	 *
	 * @param role_id
	 * @param rgChkArr
	 * @return
	 */
	public static int setRoleRegions(HttpServletRequest request,
			String role_id, Vector rgChkArr) {
		// HttpSession session = request.getSession();
		int flag = -1; // 返回值
		// 角色控制区域
		Hashtable sourceHashArr = getRoleRegions(role_id);

		// SQL
		Vector sqlArr = new Vector();

		// 删除串
		String delStr = "";
		for (int i = 0; i < rgChkArr.size(); i++) {
			// 重构
			KeyValueStruct keyValueStruct = (KeyValueStruct) rgChkArr.get(i);
			// 判断
			if (keyValueStruct.getValue().equals("1")) { // 被选中的区域
				// 不再界面选中的区域列表中插入
				if (sourceHashArr.get(keyValueStruct.getKey()) == null) {

					String sql = "INSERT INTO UI_RULE_RES_ROLE (role_id, res_id,res_type) VALUES ('"
							+ role_id
							+ "','"
							+ keyValueStruct.getKey()
							+ "','2')";
					sqlArr.add(sql);
					// 记录资源表日志
					String log_sql1 = getLogSql(request, "I2118", "1", role_id,
							keyValueStruct.getKey());
					sqlArr.add(log_sql1);

				}

			} else {
				// 没有选中的区域
				if (sourceHashArr.get(keyValueStruct.getKey()) != null) { // 删除
					delStr += ",'" + keyValueStruct.getKey() + "'";
				}

			}
		}
		if (delStr.length() > 0) {
			// 记录资源表日志
			String log_sql2 = getLogSql(request, "I2119", "3", role_id,
					delStr.substring(1));
			sqlArr.add(log_sql2);
			String del_sql = "DELETE FROM UI_RULE_RES_ROLE WHERE role_id= '"
					+ role_id + "' and res_id in (" + delStr.substring(1)
					+ ") AND res_type='2'";
			sqlArr.add(del_sql);
		}

		// 转化
		String sqlv[] = new String[sqlArr.size()];
		for (int i = 0; i < sqlArr.size(); i++) {
			sqlv[i] = sqlArr.get(i).toString();
			System.out.println("sqlv[i]==================" + sqlv[i]);
		}
		// 执行
		try {
			flag = 1;
			if (sqlv.length > 0) {
				WebDBUtil.execTransUpdate(sqlv);
			}
		} catch (Exception e) {
			System.out
					.println("setRoleRegions exec sqls err:" + e.getMessage());
			for (int i = 0; i < sqlv.length; i++) {
				System.out.println("err sqlv[" + i + "]=================="
						+ sqlv[i]);
			}
			// e.printStackTrace();
			flag = -1;
		}
		return flag;

	}

	public static Vector getRoleRegionVct(String rootRegion, String role_id) {
		String sql = "select c.lv,c.region_id,c.parent_id,c.region_name,c.isactive,rr.role_id"
				+ " from UI_RULE_RES_ROLE rr"
				+ " right outer join("
				+ "       select region_id,parent_id,region_name,isactive,level as lv"
				+ "       from ui_info_region connect by prior region_id=parent_id "
				+ "       start with region_id in ("
				+ rootRegion
				+ ")"
				+ "       ) c"
				+ " on rr.role_id='"
				+ role_id
				+ "'"
				+ " and rr.res_id=c.region_id order by lv";
		Vector v = null;
		try {
			System.out.println("getRoleRegionVct sql =======" + sql);
			v = WebDBUtil.execQryVector(sql, "");
		} catch (AppException ex) {
			v = new Vector();
			System.out.println("getRoleRegionVct err sql =======[" + sql + "] "
					+ ex.getMessage());
		}
		return v;
	}

	public static String[][] getRoleGroupList(String log_group_id,
			String system_id, HttpServletRequest request) {

		String sql = "";
		String[][] result = null;
		String role_id = request.getParameter("role_id");
		String where = getQuery(request);
		InfoUserGroupTable groupInfo = LSInfoUserGroup
				.getUserGroupInfo(log_group_id);
		String groupId = groupInfo.parent_id;
		if ("".equals(groupId)) {
			groupId = log_group_id;
		}
		try {
			sql = SQLGenator.genSQL("roleGroup001", groupId, system_id, where,
					role_id);
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String[][] getRoleGroup(String role_id) {

		String sql = "";
		String[][] result = null;
		try {

			sql = "select a.group_id,b.group_name from ui_rule_group_role a,ui_info_user_group b where a.group_id=b.group_id and a.role_id='"
					+ role_id + "'";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getQuery(HttpServletRequest request) {
		String where = "";
		String group_id = request.getParameter("group_id");
		String group_name = request.getParameter("group_name");
		String group_type = request.getParameter("group_type");

		if (group_id != null && !"".equals(group_id)) {
			where = " and group_id like '%" + group_id + "%' ";
		}
		if (group_name != null && !"".equals(group_name)) {
			where += " and group_name like '%" + group_name + "%' ";
		}
		if (group_type != null && !"".equals(group_type)) {
			where += " and group_type='" + group_type + "' ";
		}
		return where;
	}

	public static void saveRoleGroup(HttpServletRequest request,
			InfoRoleTable roleInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		String where = getQuery(request);
		if (!"".equals(where)) {
			where = " and group_id in (select a.group_id from ui_info_user_group a where 1=1 "
					+ where + ") ";
		}
		String delSql = "delete from ui_rule_group_role where role_id='"
				+ roleInfo.role_id + "' " + where;
		Vector sqlv = new Vector();
		// 删除用户组与角色关系的日志
		String log_sql_a = getLogSql(request, "roleGroup003", "3",
				roleInfo.role_id, where);
		sqlv.add(log_sql_a);
		String[] groupArr = roleInfo.group_id.split(",");
		for (int i = 0; i < groupArr.length; i++) {
			String sql = "insert into ui_rule_group_role values ('"
					+ groupArr[i] + "','" + roleInfo.role_id + "')";
			sqlv.add(sql);
		}

		String log_sql_b = getLogSql(request, "roleGroup003", "1",
				roleInfo.role_id, where);
		sqlv.add(log_sql_b);
		String sqlArr[] = new String[sqlv.size() + 1];
		sqlArr[0] = delSql;
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i + 1] = sqlv.get(i).toString();
			// //System.out.println("sqlArr[i]====="+sqlArr[i]);
		}

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}
		if (i > 0) {
			String url = "roleview.rptdo?submitType=roleGroup&role_id="
					+ roleInfo.role_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "角色用户组保存成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "角色用户组保存失败！");
		}
	}

	/**
	 * 判断角色编号是否已经存在
	 *
	 * @param role_id
	 * @return
	 */
	public static boolean isRoleExists(String role_id, InfoOperTable loginUser) {
		String sql = "";
		boolean re = false;
		if (loginUser == null) {
			return re;
		}

		try {
			sql = "select * from UI_INFO_ROLE where role_id='" + role_id + "'";
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				re = true;
			}
		} catch (AppException ex) {
			System.out.println("isStaffExists sql error ====== " + sql);
			ex.printStackTrace();
		}
		return re;

	}
}
