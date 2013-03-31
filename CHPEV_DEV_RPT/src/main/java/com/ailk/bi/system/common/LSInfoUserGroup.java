package com.ailk.bi.system.common;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoUserGroupTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoUserGroup {

	/**
	 * 查询取得用户组信息
	 *
	 * @param group_id
	 * @return InfoUserGroupTable 对象
	 * @author xh
	 */
	public static InfoUserGroupTable getUserGroupInfo(String group_id) {

		InfoUserGroupTable infogroup = null;
		//
		String sql = "select GROUP_ID,GROUP_NAME,PARENT_ID,STATUS,REGION_ID,DEPT_ID,SEQUENCE,group_type from UI_INFO_USER_GROUP where group_id ='"
				+ group_id + "'";

		// //System.out.println(sql);
		try {
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			infogroup = toInfogroup(rs);
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (infogroup == null) {
			infogroup = new InfoUserGroupTable();
		}
		return infogroup;
	}

	/**
	 * 用户组结果集转换为InfoUserGroupTable
	 *
	 * @param String
	 *            [][]
	 * @return InfoUserGroupTable 对象
	 * @author xh
	 */
	private static InfoUserGroupTable toInfogroup(String[][] rs) {
		if (rs == null) {
			return null;
		}
		InfoUserGroupTable group = new InfoUserGroupTable();
		if (rs.length > 0) {
			group.group_id = rs[0][0];
			group.group_name = rs[0][1];
			group.parent_id = rs[0][2];
			group.status = rs[0][3];
			group.region_id = rs[0][4];
			group.dept_id = rs[0][5];
			group.sequence = rs[0][6];
			group.group_type = rs[0][7];
		}
		return group;
	}

	/**
	 * 判断用户组名称是否重复
	 *
	 * @param group_id
	 * @param group_name
	 * @return boolean
	 * @author xh
	 */
	public static boolean isDupGroupName(String group_id, String group_name) {

		boolean flag = false;
		String sql = "SELECT 1 FROM UI_INFO_USER_GROUP WHERE EXISTS (SELECT group_id FROM UI_INFO_USER_GROUP WHERE group_id != '"
				+ group_id + "' AND group_name = trim('" + group_name + "'))";

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
	 * 判断用户组代码是否重复
	 *
	 * @param group_id
	 * @param group_name
	 * @return boolean
	 * @author xh
	 */
	public static boolean isUserGroupExists(String group_id) {

		boolean flag = false;
		String sql = "";

		try {
			sql = "SELECT * FROM UI_INFO_USER_GROUP WHERE group_id ='" + group_id +"'";
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				flag = true;
			}
		} catch (AppException ex) {
			System.out.println("isUserGroupExists sql error ====== " + sql);
			ex.printStackTrace();
		}
		return flag;

	}

	/**
	 * 新增用户组信息
	 *
	 * @param session
	 * @param groupInfo
	 * @throws HTMLActionException
	 * @author xh
	 */
	public static void addNewUserGroup(HttpServletRequest request,
			InfoUserGroupTable groupInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		// 取得传递参数
		String group_id = groupInfo.group_id;
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);

		Vector sqlV = new Vector();
		// 取得当前用户组信息
		InfoUserGroupTable group = getUserGroupInfo(group_id);

		// 判断用户组代码是否已经存在
		if (group != null && !group.group_id.trim().equals("")) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "<B>用户组已经存在！</B>");
		}

		// 判断用户组名称是否已经存在
		if (!group.group_name.equals(groupInfo.group_name)) {
			if (isDupGroupName(group_id, groupInfo.group_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "<B>用户组名重复!</B>");

			}
		}

		// 用户组基本信息
		String sqla = "insert into UI_INFO_USER_GROUP(group_ID,group_NAME,parent_id,STATUS,REGION_ID,DEPT_ID,SEQUENCE,system_id) "
				+ "values('"
				+ group_id
				+ "','"
				+ groupInfo.group_name
				+ "','"
				+ user.group_id
				+ "','"
				+ groupInfo.status
				+ "','"
				+ user.region_id
				+ "','"
				+ user.dept_id
				+ "',SEQ_INFO_user_group.nextval,'" + user.system_id + "')";
		sqlV.add(sqla);

		// 比较取得变更字段字符串
		String upStr = getUpdateFiledStr(groupInfo, group);
		if (upStr.length() > 0) {
			String sqlc = "UPDATE UI_INFO_USER_GROUP SET " + upStr
					+ "   WHERE group_id= '" + group_id + "'";
			sqlV.add(sqlc);
		}
		try {
			String[][] a = WebDBUtil.execQryArray(
					"select  log_seq_sn.nextval from dual", "");
			System.out.println("a[0][0]===" + a[0][0]);
		} catch (AppException e1) {

			e1.printStackTrace();
		}
		String sql = getLogSql(request, "I2120", "1", group_id);
		sqlV.add(sql);
		/*
		 * String sql_b = getLogSql(request, "I2116", "1", group_id);
		 * sqlV.add(sql_b);
		 */
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
			String url = "UserGroupView.screen?submitType=3&group_id="
					+ group_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "用户组信息新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "用户组信息新增失败！");
		}

	}

	/**
	 * 变更用户组信息
	 *
	 * @param session
	 * @param groupInfo
	 * @throws HTMLActionException
	 * @author xh
	 */
	public static void updategroupInfo(HttpServletRequest request,
			InfoUserGroupTable groupInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		// 取得传递参数
		String oldGrpId = request.getParameter("hidOldGrpId");

		// String group_id = groupInfo.group_id;
		Vector sqlV = new Vector();

		// 取得当前用户组信息
		InfoUserGroupTable group = getUserGroupInfo(oldGrpId);

		// 判断用户组代码是否改变
		/*
		 * if (!group.group_id.equals(groupInfo.group_id)) { throw new
		 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
		 * "用户组代码不能修改！"); }
		 */
		// 判断用户组名称是否相同
		if (!group.group_name.equals(groupInfo.group_name)) {
			if (isDupGroupName(oldGrpId, groupInfo.group_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "<B>用户组名称重复!</B>");
			}
		}

		// 比较取得变更字段字符串
		String upStr = getUpdateFiledStr(groupInfo, group);
		String sqlc = "UPDATE UI_INFO_USER_GROUP SET " + upStr
				+ "   WHERE group_id = '" + oldGrpId + "'";
		if (upStr.length() > 0) {
			sqlV.add(sqlc);
		}

		String sql = getLogSql(request, "I2120", "2", oldGrpId);
		sqlV.add(sql);

		if (!oldGrpId.equals(groupInfo.group_id)) {
			sql = "update ui_rule_group_role set group_id='"
					+ groupInfo.group_id + "'  WHERE group_id = '" + oldGrpId
					+ "'";
			sqlV.add(sql);

			sql = "update ui_rule_user_group set group_id='"
					+ groupInfo.group_id + "'  WHERE group_id = '" + oldGrpId
					+ "'";
			sqlV.add(sql);

		}

		// 转换
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			System.out.println("updategroupInfo [" + i + "]" + sqlArr[i]);
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
			String url = "UserGroupView.screen?submitType=3&group_id="
					+ groupInfo.group_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "用户组信息变更成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "用户组信息变更失败！");
		}
	}

	/**
	 * 删除用户组相关及本身信息
	 *
	 * @param session
	 * @param groupInfo
	 * @throws HTMLActionException
	 *             （1）删除用户组权限 （2）删除用户组区域 （3）删除用户组信息 有相关用户的用户组不能删除
	 */
	public static void deletegroupInfo(HttpServletRequest request,
			InfoUserGroupTable groupInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		String group_id = groupInfo.group_id;
		Vector sqlv = new Vector();
		// 判断
		if (groupReferenced(group_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "已经为该用户组分配了操作员，不可以删除！");

		}
		// 删除用户组的相关角色权限
		String log_sql_a = getLogSql(request, "I2121", "3", group_id, "");
		sqlv.add(log_sql_a);
		String sqla = "delete from UI_RULE_GROUP_ROLE where group_id = '"
				+ group_id + "'";
		sqlv.add(sqla);

		sqla = "delete from ui_rule_user_group where group_id = '" + group_id
				+ "'";
		sqlv.add(sqla);

		String log_sql_c = getLogSql(request, "I2120", "3", group_id);
		sqlv.add(log_sql_c);

		// 删除用户组本身
		String sqle = "";
		try {
			// sqle = SQLGenator.genSQL("C2114", WebConstKeys.INACTIVE,
			// group_id);//标记删除
			sqle = "delete from UI_INFO_USER_GROUP where group_id='" + group_id
					+ "'";// 实体删除，为符合总部规范1.0.5版本要求
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		sqlv.add(sqle);

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
			String url = "UserGroupView.screen?submitType=3&group_id="
					+ group_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "用户组信息删除成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "用户组信息删除失败！");
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

	/**
	 * 判断当前用户组是否有相关联的用户
	 *
	 * @param group_id
	 * @return flag
	 * @author xh
	 */
	public static boolean groupReferenced(String group_id) {
		String sql = "select group_id,user_id from UI_RULE_USER_GROUP where group_id ='"
				+ group_id + "'";
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
	 * 更新用户组实体变更信息
	 *
	 * @param newrole
	 *            新的用户组实体
	 * @param oldrole
	 *            老的用户组实体
	 * @return 变更字符串
	 * @author xh
	 */
	private static String getUpdateFiledStr(InfoUserGroupTable newGroup,
			InfoUserGroupTable oldGroup) {
		String upStr = "";
		// 用户组名称
		if (!newGroup.group_id.equals(oldGroup.group_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " group_id = '" + newGroup.group_id + "'";

		}

		if (!newGroup.group_name.equals(oldGroup.group_name)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " group_name = '" + newGroup.group_name + "'";

		}

		// 用户组父类
		if (!"".equals(newGroup.parent_id)
				&& !newGroup.parent_id.equals(oldGroup.parent_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " parent_id = '" + newGroup.parent_id + "'";
		}

		// 用户组状态
		if (!newGroup.status.equals(oldGroup.status)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " status = '" + newGroup.status + "'";
		}
		// 用户组类型
		if (!newGroup.group_type.equals(oldGroup.group_type)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " group_type = '" + newGroup.group_type + "'";
		}
		return upStr;
	}

	public static String[][] getGroupRole(String group_id) {

		String sql = "";
		String[][] result = null;
		try {

			sql = "select a.role_id,b.role_name from ui_rule_group_role a,ui_info_role b where a.role_id=b.role_id and a.group_id='"
					+ group_id + "'";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String[][] getNoGroupRole(String group_id) {

		String sql = "";
		String[][] result = null;
		try {

			sql = "select b.role_id,b.role_name from (select * from ui_rule_group_role where group_id='"
					+ group_id
					+ "') a,ui_info_role b where a.role_id(+)=b.role_id and a.role_id is null ";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getQuery(HttpServletRequest request) {
		String where = "";
		String role_id = request.getParameter("role_id");
		String role_name = request.getParameter("role_name");
		String role_type = request.getParameter("role_type");

		if (role_id != null && !"".equals(role_id)) {
			where = " and a.role_id like '%" + role_id + "%' ";
		}
		if (role_name != null && !"".equals(role_name)) {
			where += " and a.role_name like '%" + role_name + "%' ";
		}
		if (role_type != null && !"".equals(role_type)) {
			where += " and a.role_type='" + role_type + "' ";
		}
		return where;
	}

	public static void saveGroupRole(HttpServletRequest request,
			InfoUserGroupTable groupInfo) throws HTMLActionException {
		HttpSession session = request.getSession();
		String where = getQuery(request);
		if (!"".equals(where)) {
			where = " and role_id in (select a.role_id from ui_info_role a where 1=1 "
					+ where + ") ";
		}
		String delSql = "delete from ui_rule_group_role where group_id='"
				+ groupInfo.group_id + "' " + where;
		Vector sqlv = new Vector();
		// 删除用户组与角色关系的日志
		String log_sql_a = getLogSql(request, "I2121", "3", groupInfo.group_id,
				where);
		sqlv.add(log_sql_a);
		String[] roleArr = groupInfo.role_id.split(",");
		for (int i = 0; i < roleArr.length; i++) {
			String sql = "insert into ui_rule_group_role values ('"
					+ groupInfo.group_id + "','" + roleArr[i] + "')";
			sqlv.add(sql);
		}

		String log_sql_b = getLogSql(request, "I2121", "1", groupInfo.group_id,
				where);
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
			String url = "userGroupView.rptdo?submitType=groupRole&group_id="
					+ groupInfo.group_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "用户组角色保存成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "用户组角色保存失败！");
		}
	}

	public static String[][] getGroupUser(String group_id) {

		String sql = "";
		String[][] result = null;
		try {

			sql = "select b.user_id,b.user_name from ui_rule_user_group a,ui_info_user b where a.user_id=b.user_id and a.group_id='"
					+ group_id + "'";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String[][] getGroupRoleList(String log_group_id,
			HttpServletRequest request) {

		String sql = "";
		String where = "";
		String[][] result = null;
		String group_id = request.getParameter("group_id");
		where = getQuery(request);
		try {
			if ("1".equals(log_group_id))
				sql = SQLGenator.genSQL("groupRole001", log_group_id, where,
						group_id);
			else
				sql = SQLGenator.genSQL("groupRole003", log_group_id, where,
						group_id);
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获得该组的下级
	public static String getGroup(String group_id) {

		String sql = "";
		String str = "'" + group_id + "'";
		String[][] result = null;
		try {
			sql = SQLGenator.genSQL("groupRole002", group_id);
			result = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; result != null && i < result.length; i++) {
				str += ",'" + result[i][0] + "'";
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return str;
	}

}
