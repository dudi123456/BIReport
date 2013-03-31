/**
 * 
 */
package com.ailk.bi.system.facade;

import java.util.List;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoRegionTable;
import com.ailk.bi.base.table.InfoRoleTable;
import com.ailk.bi.base.table.InfoUserGroupTable;

/**
 * @author Administrator
 * 
 */
@SuppressWarnings({ "rawtypes" })
public interface IServiceFacade {

	// 用户信息
	public InfoOperTable getUserInfo(String user_id);

	// 用户组信息
	public InfoUserGroupTable getUserGroupInfo(String user_id);

	// 用户角色信息
	public List getUserRoleInfo(String user_id);

	// 用户菜单资源
	public List getUserMenuInfo(String user_id);

	// 用户地域资源
	public UserCtlRegionStruct getUserRegionInfo(String user_id);

	// 获取角色信息
	public InfoRoleTable getRoleInfo(String role_id);

	// 获取用户组信息
	public InfoUserGroupTable getGroupInfo(String group_id);

	// 获取菜单信息
	public InfoMenuTable getMenuInfo(String menu_id);

	// 获取地域信息
	public InfoRegionTable getRegionInfo(String region_id);

	// 检查是否有该用户
	public boolean hasUser(String user_id);

	// 检查该用户密码是否正确
	public boolean checkUser(String user_id, String pwd);

	// 获取部门名称
	public String getDeptName(String user_id);
}
