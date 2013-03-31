package com.ailk.bi.system.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoRoleTable;
import com.ailk.bi.base.table.InfoUserGroupTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.MD5;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.dao.IUserDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserDaoImpl implements IUserDao {

	// 用户信息
	public InfoOperTable getUserInfo(String user_id) {
		StringBuffer sql = new StringBuffer();
		InfoOperTable userInfo = new InfoOperTable();
		try {
			sql.append(
					"select a.USER_ID,a.REGION_ID,a.DEPT_ID,a.DUTY_ID,a.USER_NAME,a.PWD,a.STATUS,a.BEGIN_DATE,END_DATE,CREATE_TIME,BIRTHDAY,MOBILE_PHONE,HOME_PHONE,OFFICE_PHONE,")
					.append("OFFICE_FAX,EMAIL,SEX,AGE,POSTALCODE,ADDRESS,NATION,PASSPORTNO,DIRECTOR,QUESTION,ANSWER,RES_CHAR,b.group_id,c.group_name,c.parent_id,a.system_id,channle_id ")
					.append("from UI_INFO_USER a,UI_RULE_USER_GROUP b,ui_info_user_group c where a.user_id=b.user_id and a.user_id='")
					.append(user_id).append("' and b.group_id=c.group_id ");

			Vector v = WebDBUtil.execQryVector(sql.toString(), "");
			if (v.size() > 0) {
				Vector tempv = (Vector) v.get(0);
				userInfo.user_id = tempv.get(0).toString();
				userInfo.oper_no = tempv.get(0).toString();

				userInfo.region_id = tempv.get(1).toString();
				userInfo.dept_id = tempv.get(2).toString();
				userInfo.duty_id = tempv.get(3).toString();
				userInfo.user_name = tempv.get(4).toString();
				// userInfo.pwd = MD5.getInstance().getMD5ofStr(
				// CommTool.getDecrypt(tempv.get(25).toString()).trim());
				userInfo.pwd = tempv.get(5).toString();
				userInfo.pwd_encode = tempv.get(5).toString();

				userInfo.status = tempv.get(6).toString();
				userInfo.begin_date = tempv.get(7).toString();
				userInfo.end_date = tempv.get(8).toString();
				userInfo.create_time = tempv.get(9).toString();
				userInfo.birthday = tempv.get(10).toString();
				userInfo.mobile_phone = tempv.get(11).toString();
				userInfo.home_phone = tempv.get(12).toString();
				userInfo.office_phone = tempv.get(13).toString();
				userInfo.office_fax = tempv.get(14).toString();
				userInfo.email = tempv.get(15).toString();
				userInfo.sex = tempv.get(16).toString();
				userInfo.age = tempv.get(17).toString();
				userInfo.postalcode = tempv.get(18).toString();
				userInfo.address = tempv.get(19).toString();
				userInfo.nation = tempv.get(20).toString();
				userInfo.passportno = tempv.get(21).toString();
				userInfo.director = tempv.get(22).toString();
				userInfo.question = tempv.get(23).toString();
				userInfo.answer = tempv.get(24).toString();
				userInfo.res_char = CommTool.getDecrypt(
						tempv.get(25).toString()).trim();
				userInfo.group_id = tempv.get(26).toString();
				userInfo.group_name = tempv.get(27).toString();
				userInfo.parent_group_id = tempv.get(28).toString();
				userInfo.system_id = tempv.get(29).toString();
				userInfo.channleId = tempv.get(30).toString();
			}

		} catch (AppException ex) {
		}
		return userInfo;

	}

	// 用户组信息
	public InfoUserGroupTable getUserGroupInfo(String user_id) {

		InfoUserGroupTable groupInfo = new InfoUserGroupTable();

		String sql = "  select a.GROUP_ID,a.GROUP_NAME,a.PARENT_ID,a.STATUS,a.REGION_ID,a.DEPT_ID,a.SEQUENCE,a.group_type from UI_INFO_USER_GROUP a,ui_rule_user_group b "
				+ " where a.group_id=b.group_id and b.user_id= '"
				+ user_id
				+ "'";

		try {
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				groupInfo.group_id = rs[0][0];
				groupInfo.group_name = rs[0][1];
				groupInfo.parent_id = rs[0][2];
				groupInfo.status = rs[0][3];
				groupInfo.region_id = rs[0][4];
				groupInfo.dept_id = rs[0][5];
				groupInfo.sequence = rs[0][6];
				groupInfo.group_type = rs[0][7];
			}
		} catch (AppException e) {
			e.printStackTrace();
		}

		return groupInfo;
	}

	// 用户角色信息
	public List getUserRoleInfo(String user_id) {
		List role = new ArrayList();
		try {
			String sql = SQLGenator.genSQL("system003", user_id, user_id);
			String[][] rs = WebDBUtil.execQryArray(sql, "");

			for (int i = 0; rs != null && i < rs.length; i++) {
				InfoRoleTable roleInfo = new InfoRoleTable();
				roleInfo.role_id = rs[0][0];
				roleInfo.role_name = rs[0][1];
				roleInfo.role_type = rs[0][2];
				roleInfo.status = rs[0][3];
				/*
				 * roleInfo.region_id = rs[0][4]; roleInfo.dept_id = rs[0][5];
				 * roleInfo.comments = rs[0][6]; roleInfo.sequence = rs[0][7];
				 */
				role.add(roleInfo);
			}

		} catch (AppException e) {
			e.printStackTrace();
		}
		return role;
	}

	// 用户菜单资源
	public String[][] getUserMenuInfo(String user_id) {
		String[][] rs = null;

		try {
			String sql = SQLGenator.genSQL("Lg001", user_id, user_id);
			System.out.println("Lg001:" + sql);
			rs = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return rs;
	}

	// 用户菜单资源
	public String[][] getUserMenuInfo(String user_id, String system_id) {
		String[][] rs = null;

		try {
			String sql = SQLGenator.genSQL("Lg001_001", user_id, user_id,
					system_id);
			System.out.println("Lg001_001:" + sql);
			rs = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return rs;
	}

	// 用户地域资源
	public String[][] getUserRegionInfo(String user_id) {
		String[][] rs = null;

		try {
			String sql = SQLGenator.genSQL("Lg002", user_id, user_id);
			rs = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return rs;
	}

	// 检查是否有该用户
	public boolean hasUser(String user_id) {
		String[][] rs = null;
		boolean flag = false;

		try {
			String sql = "select * from ui_info_user where user_id='" + user_id
					+ "'";
			rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				flag = true;
			}

		} catch (AppException e) {
			e.printStackTrace();
		}
		return flag;
	}

	// 检查该用户密码是否正确
	public boolean checkUser(String user_id, String pwd) {
		String[][] rs = null;
		boolean flag = false;
		String formatPwd = MD5.getInstance().getMD5ofStr(
				CommTool.getDecrypt(pwd.toString()).trim());
		try {
			String sql = "select * from ui_info_user where user_id='" + user_id
					+ "' and pwd='" + formatPwd + "'";
			rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				flag = true;
			}

		} catch (AppException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void excuteSql(String sql) {

		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public String[][] querySql(String sql) {

		String[][] rs = null;
		try {
			rs = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return rs;
	}

	public InfoOperTable qryBOUserInfo(InfoOperTable user) {

		InfoOperTable rtnUser = new InfoOperTable();

		String strSql = "";
		try {
			strSql = SQLGenator.genSQL("Login001X", user.oper_no);
		} catch (AppException e1) {

			e1.printStackTrace();
		}
		try {
			String[][] svces = WebDBUtil.execQryArray(strSql, "");
			if (svces != null && svces.length > 0) {
				rtnUser.bo_user = svces[0][0];
				rtnUser.bo_pwd = svces[0][1];
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return rtnUser;
	}

	public String[][] UserLogin(String sql, String[] where) {

		try {

			return WebDBUtil.execQryArray(sql, where, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;
	}

	public String getDeptName(String dept_id) {

		String deptName = null;
		try {
			String sql = "select dept_name from ui_info_dept where dept_id='"
					+ dept_id + "'";
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0)
				deptName = rs[0][0];
		} catch (AppException e) {

			e.printStackTrace();
		}
		return deptName;
	}

	public String getWinUser(String user_id) {

		String WinUser = null;
		try {
			String sql = "select lo_user from ui_sso_user where bi_user='"
					+ user_id + "'";
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0)
				WinUser = rs[0][0];
		} catch (AppException e) {

			e.printStackTrace();
		}
		return WinUser;
	}
}
