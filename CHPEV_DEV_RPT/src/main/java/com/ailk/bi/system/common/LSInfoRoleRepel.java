package com.ailk.bi.system.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoRoleRepel {
	/**
	 * 获取互斥列表
	 * 
	 * @return
	 */
	public static String[][] getRoleRepelLs() {
		try {
			String sql = " select distinct relation_id,relation_name from rule_role_relation ";
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			return rs;
		} catch (AppException e) {
		}
		return null;
	}

	public static String saveRelation(String oStr, String relationId,
			String relationName) {
		try {
			String ret = "1";
			if (oStr == null || "".equals(oStr))
				return ret;
			String[] roles = oStr.split(",");
			if (roles == null || roles.length == 0)
				return ret;
			if (relationId == null || "".equals(relationId)) {
				String sql = " select seq_id.nextval from dual";
				String[][] rs = WebDBUtil.execQryArray(sql, "");
				relationId = rs[0][0];
				ret = relationId;
			}
			List ls = new ArrayList();
			for (int i = 1; i < roles.length; i++) {
				String[] role = roles[i].split(";");
				if (role[1].equals("1")) {
					String sql = "insert into rule_role_relation values('"
							+ relationId + "','" + role[0] + "','"
							+ relationName + "')";
					ls.add(sql);
				}
				if (role[1].equals("-1")) {
					String sql = "delete from rule_role_relation where RELATION_ID='"
							+ relationId + "' and ROLE_CODE='" + role[0] + "'";
					ls.add(sql);
				}
			}
			String[] sqls = new String[ls.size()];
			for (int i = 0; i < ls.size(); i++) {
				sqls[i] = (String) ls.get(i);
			}
			WebDBUtil.execTransUpdate(sqls);
			return relationId;
			// return rs;
		} catch (AppException e) {
			return "-1";
		}
	}

	public static int delRelation(String relationId) {
		String sql = " delete from rule_role_relation where relation_id='"
				+ relationId + "' ";
		try {

			int ret = WebDBUtil.execUpdate(sql);
			return ret;
		} catch (AppException e) {
		}
		return -1;
	}

	public static String getRoleTree(HttpServletRequest request,
			String relationId) {
		StringBuffer sb = new StringBuffer();
		HttpSession session = request.getSession();
		InfoOperTable logUser = CommonFacade.getLoginUser(session);
		try {
			String sql = SQLGenator.genSQL("Q0002", logUser.region_id,
					logUser.region_id, logUser.dept_no);
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs == null || rs.length == 0)
				return "";
			for (int i = 0; i < rs.length; i++) {
				sb.append("var type" + rs[i][1] + " = new WebFXTreeItem('"
						+ rs[i][2] + "');\n");
				sb.append("tree.add(type" + rs[i][1] + ");\n");
			}
			if (relationId != null && !"".equals(relationId)) {
				sql = SQLGenator.genSQL("Q2950", logUser.region_id,
						logUser.region_id, logUser.dept_no, relationId);
			} else {
				sql = SQLGenator.genSQL("Q2949", logUser.region_id,
						logUser.region_id, logUser.dept_no);
			}
			rs = WebDBUtil.execQryArray(sql, "");
			if (rs == null || rs.length == 0)
				return "";
			for (int i = 0; i < rs.length; i++) {
				if (relationId != null && !"".equals(relationId)
						&& relationId.equals(rs[i][3])) {
					sb.append("var role_" + rs[i][0]
							+ " = new WebFXCheckBoxTreeItem('" + rs[i][1]
							+ "',null,'" + rs[i][0] + "',true);\n");
					sb.append("type" + rs[i][2] + ".add(role_" + rs[i][0]
							+ ");\n");
				} else {
					sb.append("var role" + rs[i][0]
							+ " = new WebFXCheckBoxTreeItem('" + rs[i][1]
							+ "',null,'" + rs[i][0] + "',false);\n");
					sb.append("type" + rs[i][2] + ".add(role" + rs[i][0]
							+ ");\n");
				}
			}
		} catch (AppException e) {
		}
		return sb.toString();
	}

	public static String checkOperByRole(String operNo, String[] roleCodes) {
		if (roleCodes == null || roleCodes.length < 1)
			return "";
		String sql = " select distinct a.relation_name"
				+ " from rule_role_relation a,"
				+ " (select b.relation_id, count(*)"
				+ " from rule_role_relation b"
				+ " where b.role_code in (select o.role_code"
				+ " 		from rule_oper_role o"
				+ "              where o.oper_no = '" + operNo
				+ "') or b.role_Code in (";
		String sqlcond = "";
		for (int i = 0; i < roleCodes.length; i++) {
			sqlcond += "'" + roleCodes[i] + "',";
		}
		sqlcond = sqlcond.substring(0, sqlcond.length() - 1);
		sql += sqlcond + ")" + " group by b.relation_id"
				+ " having count(*) > 1) d"
				+ " where a.relation_id = d.relation_id and a.role_Code in("
				+ sqlcond + ")";
		try {

			String[][] ret = WebDBUtil.execQryArray(sql, "");
			if (ret == null || ret.length < 1)
				return "";
			String rets = "";
			for (int i = 0; i < ret.length; i++) {
				rets += ret[i][0] + ",";
			}
			return rets.substring(0, rets.length() - 1);
		} catch (AppException e) {
			return null;
		}
	}

	public static String checkRoleByOper(String operNo, String roleCode) {
		String sql = " select distinct a.relation_name"
				+ " from rule_role_relation a,"
				+ " (select b.relation_id, count(*)"
				+ " from rule_role_relation b"
				+ " where b.role_code in (select o.role_code"
				+ " 		from rule_oper_role o"
				+ "              where o.oper_no = '" + operNo
				+ "') or b.role_Code ='" + roleCode + "'"
				+ " group by b.relation_id" + " having count(*) > 1) d"
				+ " where a.relation_id = d.relation_id and a.role_code='"
				+ roleCode + "'";
		try {

			String[][] ret = WebDBUtil.execQryArray(sql, "");
			if (ret == null || ret.length < 1)
				return "";
			String rets = "";
			for (int i = 0; i < ret.length; i++) {
				rets += ret[i][0] + ",";
			}
			return rets.substring(0, rets.length() - 1);
		} catch (AppException e) {
			return null;
		}

	}

	public String getRoleRepel(List roleCodes) {
		if (roleCodes == null || roleCodes.size() < 1)
			return null;
		String sql = " select distinct a.relation_name"
				+ " from rule_role_relation a,"
				+ " (select b.relation_id, count(*)"
				+ " from rule_role_relation b" + " where b.role_code in (";
		String sqlcond = "";
		for (int i = 0; i < roleCodes.size(); i++) {
			sqlcond += "'" + (String) roleCodes.get(i) + "',";
		}
		sqlcond = sqlcond.substring(0, sqlcond.length() - 1);
		sql += sqlcond;
		sql += ")" + "group by b.relation_id" + "having count(*) > 1) d"
				+ "where a.relation_id = d.relation_id";
		try {

			String[][] ret = WebDBUtil.execQryArray(sql, "");
			if (ret == null || ret.length < 1)
				return "";
			String rets = "";
			for (int i = 0; i < ret.length; i++) {
				rets += ret[i] + ",";
			}
			return rets.substring(0, rets.length() - 1);
		} catch (AppException e) {
			return null;
		}
	}
}
