/**
 * 
 */
package com.ailk.bi.system.facade.impl;

import java.util.List;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoRegionTable;
import com.ailk.bi.base.table.InfoRoleTable;
import com.ailk.bi.base.table.InfoUserGroupTable;
import com.ailk.bi.system.common.LSInfoMenu;
import com.ailk.bi.system.common.LSInfoRegion;
import com.ailk.bi.system.common.LSInfoRole;
import com.ailk.bi.system.common.LSInfoUserGroup;
import com.ailk.bi.system.service.IUserService;
import com.ailk.bi.system.service.impl.UserServiceImpl;

/**
 * @author Administrator
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class ServiceFacade implements IUserService {

	private UserServiceImpl userService = new UserServiceImpl();

	// 用户信息
	public InfoOperTable getUserInfo(String user_id) {
		return userService.getUserInfo(user_id);
	}

	// 用户组信息
	public InfoUserGroupTable getUserGroupInfo(String user_id) {
		return userService.getUserGroupInfo(user_id);
	}

	// 用户角色信息
	public List getUserRoleInfo(String user_id) {
		return userService.getUserRoleInfo(user_id);
	}

	// 用户菜单资源
	public List getUserMenuInfo(String user_id) {
		return userService.getUserMenuInfo(user_id);
	}

	public List getUserMenuInfo(String user_id, String system_id) {
		return userService.getUserMenuInfo(user_id, system_id);
	}

	// 用户地域资源
	public UserCtlRegionStruct getUserRegionInfo(String user_id) {
		return userService.getUserRegionInfo(user_id);
	}

	// 获取角色信息
	public InfoRoleTable getRoleInfo(String role_id) {
		return LSInfoRole.getRoleInfo(role_id);
	}

	// 获取用户组信息
	public InfoUserGroupTable getGroupInfo(String group_id) {
		return LSInfoUserGroup.getUserGroupInfo(group_id);
	}

	// 获取菜单信息
	public InfoMenuTable getMenuInfo(String menu_id) {
		return LSInfoMenu.getMenuInfo(menu_id);
	}

	// 获取地域信息
	public InfoRegionTable getRegionInfo(String region_id) {
		return LSInfoRegion.getRegionInfo(region_id);
	}

	// 检查是否有该用户
	public boolean hasUser(String user_id) {
		return userService.hasUser(user_id);
	}

	// 检查该用户密码是否正确
	public boolean checkUser(String user_id, String pwd) {
		return userService.checkUser(user_id, pwd);
	}

	public String[][] UserLogin(String sql, String[] where) {

		return userService.UserLogin(sql, where);
	}

	public InfoOperTable qryBOUserInfo(InfoOperTable user) {

		return userService.qryBOUserInfo(user);
	}

	public String getDeptName(String user_id) {
		InfoOperTable info = userService.getUserInfo(user_id);
		return userService.getDeptName(info.dept_id);
	}

	public String getWinUser(String user_id) {
		return userService.getWinUser(user_id);
	}
}
