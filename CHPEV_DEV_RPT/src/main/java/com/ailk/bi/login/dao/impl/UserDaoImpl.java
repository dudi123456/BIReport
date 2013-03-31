package com.ailk.bi.login.dao.impl;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.login.dao.IUserDao;

public class UserDaoImpl implements IUserDao {

	public String[][] UserLogin(String sql, String[] where) {

		try {

			return WebDBUtil.execQryArray(sql, where, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;
	}

	public void excuteSql(String sql) {

		try {
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
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

	public InfoOperTable getUserPassword(String sql, String[] where) {

		InfoOperTable userObj = null;
		try {
			// select
			// user_id,region_id,dept_id,duty_id,user_name,pwd,status,question,answer,res_char
			String[][] user = WebDBUtil.execQryArray(sql, where, "");
			if (user != null && user.length > 0) {
				userObj = new InfoOperTable();
				userObj.user_id = user[0][0];
				userObj.region_id = user[0][1];
				userObj.dept_id = user[0][2];
				userObj.duty_id = user[0][3];
				userObj.user_name = user[0][4];
				userObj.pwd = user[0][5];
				userObj.status = user[0][6];
				userObj.question = user[0][7];
				userObj.answer = user[0][8];
				userObj.res_char = user[0][9];
				userObj.oper_pwd = CommTool.getDecrypt(user[0][9]);
			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return userObj;
	}

	public String[] getUserOwnerSystem(String user_id) {
		String strSql = "";
		String[] retn = null;

		try {
			strSql = SQLGenator.genSQL("Lg001_System", user_id, user_id);
		} catch (AppException e1) {

			e1.printStackTrace();
		}
		try {
			String[][] svces = WebDBUtil.execQryArray(strSql, "");
			if (svces != null && svces.length > 0) {
				retn = new String[svces.length];
				for (int i = 0; i < svces.length; i++) {
					retn[i] = svces[i][0];
				}
				// return retn;
			} else {
				return null;
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return retn;
	}

	public String[][] getCurrentOnlineUser(String system_id) {
		String sql = "select distinct log_oper_no,log_oper_name from ui_sys_log,ui_info_user where log_type='1' and log_oper_no=user_id and system_id='"
				+ system_id
				+ "' "
				+ " and leave_time is null and to_char(log_oper_time,'yyyymmdd')='"
				+ DateUtil.getDiffDay(0, DateUtil.getNowDate()) + "'";

		try {
			String[][] res = WebDBUtil.execQryArray(sql);

			return res;
		} catch (AppException e) {

			e.printStackTrace();
		}

		return null;
	}

	public String getLoUser(String user_id) {
		String sql = " select a.lo_user from ui_sso_user a,ui_info_user b,ui_info_sub_system c "
				+ "where a.bi_user(+)=b.user_id and b.system_id=c.system_id and b.user_id='"
				+ user_id + "' and a.lo_user is not null";

		try {
			String[][] res = WebDBUtil.execQryArray(sql);
			String str = "";
			if (res != null && res.length > 0) {
				str = "";
				for (int i = 0; i < res.length; i++) {
					str += res[i][0] + ",";
				}
				str = str.substring(0, str.length() - 1);
			} else {
				str = null;
			}
			return str;
		} catch (AppException e) {

			e.printStackTrace();
		}

		return null;
	}

	public String getBiUser(String user_id) {
		String sql = " select a.bi_user from ui_sso_user a,ui_info_user b,ui_info_sub_system c "
				+ "where a.lo_user(+)=b.user_id and b.system_id=c.system_id and b.user_id='"
				+ user_id + "' and a.bi_user is not null";

		try {
			String[][] res = WebDBUtil.execQryArray(sql);
			String str = "";
			if (res != null && res.length > 0) {
				str = "";
				for (int i = 0; i < res.length; i++) {
					str += res[i][0] + ",";
				}
				str = str.substring(0, str.length() - 1);
			} else {
				str = null;
			}
			return str;
		} catch (AppException e) {

			e.printStackTrace();
		}

		return null;
	}

	public String[][] getSystemLogin(String bi_user, String lo_user) {

		try {
			String strSql = SQLGenator.genSQL("system002", bi_user, lo_user);
			String[][] res = WebDBUtil.execQryArray(strSql);

			return res;
		} catch (AppException e) {

			e.printStackTrace();
		}

		return null;
	}

	public String[][] getSystemInfo() {
		String sql = "select system_id,system_name from ui_info_sub_system ";

		try {
			String[][] res = WebDBUtil.execQryArray(sql);

			return res;
		} catch (AppException e) {

			e.printStackTrace();
		}

		return null;
	}

	public String getSqlIn(String str) {
		String temp = "";
		if (str != null && !"".equals(str)) {
			String[] arr = str.split(",");

			for (int i = 0; i < arr.length; i++) {

				temp += "'" + arr[i] + "',";
			}
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp;
	}
}
