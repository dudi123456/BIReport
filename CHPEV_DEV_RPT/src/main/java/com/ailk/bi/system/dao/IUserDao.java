package com.ailk.bi.system.dao;

import java.util.List;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoUserGroupTable;

@SuppressWarnings({ "rawtypes" })
public interface IUserDao {
	// 用户信息
	public InfoOperTable getUserInfo(String user_id);

	// 用户组信息
	public InfoUserGroupTable getUserGroupInfo(String user_id);

	// 用户角色信息
	public List getUserRoleInfo(String user_id);

	// 用户菜单资源
	public String[][] getUserMenuInfo(String user_id);

	// 用户菜单资源
	public String[][] getUserMenuInfo(String user_id, String system_id);

	// 用户地域资源
	public String[][] getUserRegionInfo(String user_id);

	// 检查是否有该用户
	public boolean hasUser(String user_id);

	// 检查该用户密码是否正确
	public boolean checkUser(String user_id, String pwd);

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

	public abstract String getDeptName(String dept_id);

	/**
	 * 
	 * @param sql
	 *            ;
	 * @return
	 * @desc:执行SQL语句
	 */
	public abstract String[][] querySql(String sql);

	public String getWinUser(String user_id);
}
