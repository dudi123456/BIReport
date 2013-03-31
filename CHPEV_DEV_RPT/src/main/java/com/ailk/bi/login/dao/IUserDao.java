package com.ailk.bi.login.dao;

import com.ailk.bi.base.table.InfoOperTable;

public interface IUserDao {

	/**
	 * 
	 * @param sql
	 * @param where
	 * @return
	 * @desc:用户登录
	 */
	public abstract String[][] UserLogin(String sql, String[] where);

	/**
	 * 
	 * @param sql
	 *            ;
	 * @return
	 * @desc:执行SQL语句
	 */
	public abstract void excuteSql(String sql);

	/**
	 * 
	 * @param user
	 * @return
	 * @desc:根据登录的用户ID从UI_BO_USER表中查询
	 */
	public abstract InfoOperTable qryBOUserInfo(InfoOperTable user);

	/**
	 * 
	 * @param sql
	 * @param where
	 * @return
	 * @desc:获取用户密码
	 */
	public abstract InfoOperTable getUserPassword(String sql, String[] where);

	public abstract String[] getUserOwnerSystem(String user_id);

	public abstract String[][] getCurrentOnlineUser(String system_id);

	public String getLoUser(String user_id);

	public String getBiUser(String user_id);

	public String[][] getSystemLogin(String bi_user, String lo_user);

	public String[][] getSystemInfo();

	public String getSqlIn(String str);
}
