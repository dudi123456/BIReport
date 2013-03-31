package com.ailk.bi.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoUserGroupTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.dao.IUserDao;
import com.ailk.bi.system.dao.impl.UserDaoImpl;
import com.ailk.bi.system.service.IUserService;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class UserServiceImpl implements IUserService {

	private IUserDao userDao = new UserDaoImpl();
	private UserCtlRegionStruct regionStruct = new UserCtlRegionStruct();

	public InfoOperTable getUserInfo(String user_id) {
		return userDao.getUserInfo(user_id);
	}

	// 用户组信息
	public InfoUserGroupTable getUserGroupInfo(String user_id) {
		return userDao.getUserGroupInfo(user_id);
	}

	// 用户角色信息
	public List getUserRoleInfo(String user_id) {
		return userDao.getUserRoleInfo(user_id);
	}

	// 用户菜单资源
	public List getUserMenuInfo(String user_id) {
		List menuList = new ArrayList();
		String[][] rs = userDao.getUserMenuInfo(user_id);

		int i = 0;
		int j = 0;
		while (rs != null && i < rs.length) {

			if ("0".equals(StringB.NulltoBlank(rs[i][2]))) {
				InfoMenuTable menuInfo = new InfoMenuTable();
				menuInfo.menu_id = rs[i][0];
				menuInfo.menu_name = rs[i][1];
				menuInfo.parent_id = rs[i][2];
				menuInfo.menu_url = rs[i][3];
				List newList = new ArrayList();
				if (i < rs.length - 1 && rs[i][0].equals(rs[i + 1][2])) {
					for (j = i + 1; j < rs.length; j++) {
						if (!"0".equals(rs[j][2])) {
							newList.add(rs[j]);
						} else {
							i = j;
							break;
						}
					}
					menuInfo.childMenu = newList;
				} else {
					++i;
				}
				menuList.add(getChildMenu(menuInfo));
				if (j == rs.length) {
					break;
				}
			} else
				++i;

		}
		return menuList;
	}

	// 用户菜单资源
	public List getUserMenuInfo(String user_id, String system_id) {
		List menuList = new ArrayList();
		String[][] rs = userDao.getUserMenuInfo(user_id, system_id);

		int i = 0;
		int j = 0;
		while (rs != null && i < rs.length) {

			if ("0".equals(StringB.NulltoBlank(rs[i][2]))) {
				InfoMenuTable menuInfo = new InfoMenuTable();
				menuInfo.menu_id = rs[i][0];
				menuInfo.menu_name = rs[i][1];
				menuInfo.parent_id = rs[i][2];
				menuInfo.menu_url = rs[i][3];
				menuInfo.openType = StringB.NulltoBlank(rs[i][9]);

				List newList = new ArrayList();
				if (i < rs.length - 1 && rs[i][0].equals(rs[i + 1][2])) {
					for (j = i + 1; j < rs.length; j++) {
						if (!"0".equals(rs[j][2])) {
							newList.add(rs[j]);
						} else {
							i = j;
							break;
						}
					}
					menuInfo.childMenu = newList;
				} else {
					++i;
				}
				menuList.add(getChildMenu(menuInfo));
				if (j == rs.length) {
					break;
				}
			} else
				++i;

		}
		return menuList;
	}

	public InfoMenuTable getChildMenu(InfoMenuTable info) {
		List menu = new ArrayList();
		String parent_id = info.menu_id;
		List list = info.childMenu;
		int i = 0;
		int j = 0;

		while (list != null && list.size() > 0 && i < list.size()) {
			String[] arr = (String[]) list.get(i);

			if (arr[2].equals(parent_id)) {
				InfoMenuTable menuInfo = new InfoMenuTable();
				menuInfo.menu_id = arr[0];
				menuInfo.menu_name = arr[1];
				menuInfo.parent_id = arr[2];
				menuInfo.menu_url = arr[3];
				menuInfo.openType = StringB.NulltoBlank(arr[9]);

				List newList = null;
				if (i < list.size() - 1
						&& arr[0].equals(((String[]) list.get(i + 1))[2])) {
					newList = new ArrayList();

					for (j = i + 1; j < list.size(); j++) {
						String[] obj = (String[]) list.get(j);
						if (!obj[2].equals(parent_id)) {
							newList.add(obj);
						} else {
							i = j;
							break;
						}
					}
					menuInfo.childMenu = newList;
					menuInfo = getChildMenu(menuInfo);
				} else {
					++i;
				}
				menu.add(menuInfo);
				if (j == list.size()) {
					break;
				}
			} else
				++i;
		}
		info.childMenu = menu;
		return info;
	}

	// 用户地域资源
	public UserCtlRegionStruct getUserRegionInfo(String user_id) {
		UserCtlRegionStruct region = new UserCtlRegionStruct();
		String[][] result = userDao.getUserRegionInfo(user_id);
		String select = "";
		String where = "";
		InfoUserGroupTable group = getUserGroupInfo(user_id);
		regionStruct.ctl_flag = group.group_type;
		try {
			for (int i = 0; result != null && i < result.length; i++) {

				regionStruct.ctl_lvl = result[i][3];

				if ("1".equals(regionStruct.ctl_lvl)) {
					region.ctl_metro_str = result[i][0];
					region.ctl_metro_str_add = "'" + result[i][0] + "'";

					select = " distinct dept_id,dept_name ";
					where = " 1=1 ";
					regionStruct.ctl_sql = getSqlStr(regionStruct.ctl_sql,
							select, where);
				} else if ("2".equals(regionStruct.ctl_lvl)) {
					region.ctl_city_str = result[i][0];
					region.ctl_city_str_add = "'" + result[i][0] + "'";
					// 拼sql
					select = " distinct dept_id,dept_name ";
					where = " dept_id2='" + result[i][0] + "'";
					regionStruct.ctl_sql = getSqlStr(regionStruct.ctl_sql,
							select, where);
				} else if ("3".equals(regionStruct.ctl_lvl)) {
					region.ctl_county_str = result[i][0];
					region.ctl_county_str_add = "'" + result[i][0] + "'";
					// 拼sql
					select = " distinct dept_id,dept_name ";
					where = " dept_id3='" + result[i][0] + "'";
					regionStruct.ctl_sql = getSqlStr(regionStruct.ctl_sql,
							select, where);
					// 获取上级信息
					select = " distinct dept_id2 ";
					where = " dept_id3='" + result[i][0] + "'";
					String sql = SQLGenator.genSQL("region001", select, where);
					String[][] temp = userDao.querySql(sql);
					if (temp != null && temp.length > 0) {
						region.ctl_city_str = temp[0][0];
						region.ctl_city_str_add = "'" + temp[0][0] + "'";
					}
				} else if ("4".equals(regionStruct.ctl_lvl)) {
					region.ctl_sec_str = result[i][0];
					region.ctl_sec_str_add = "'" + result[i][0] + "'";
					// 拼sql
					select = " distinct dept_id,dept_name ";
					where = " dept_id4='" + result[i][0] + "'";
					regionStruct.ctl_sql = getSqlStr(regionStruct.ctl_sql,
							select, where);
					// 获取上级信息
					select = " distinct dept_id2,dept_id3 ";
					where = " dept_id4='" + result[i][0] + "'";
					String sql = SQLGenator.genSQL("region001", select, where);
					String[][] temp = userDao.querySql(sql);
					if (temp != null && temp.length > 0) {
						region.ctl_city_str = temp[0][0];
						region.ctl_city_str_add = "'" + temp[0][0] + "'";

						region.ctl_county_str = temp[0][1];
						region.ctl_county_str_add = "'" + temp[0][1] + "'";
					}
				} else if ("5".equals(regionStruct.ctl_lvl)) {
					region.ctl_area_str = result[i][0];
					region.ctl_area_str_add = "'" + result[i][0] + "'";
					// 拼sql
					select = " distinct dept_id,dept_name ";
					where = " dept_id='" + result[i][0] + "'";
					regionStruct.ctl_sql = getSqlStr(regionStruct.ctl_sql,
							select, where);
					// 获取上级信息
					select = " distinct dept_id2,dept_id3,dept_id4 ";
					where = " dept_id='" + result[i][0] + "'";
					String sql = SQLGenator.genSQL("region001", select, where);
					String[][] temp = userDao.querySql(sql);
					if (temp != null && temp.length > 0) {
						region.ctl_city_str = temp[0][0];
						region.ctl_city_str_add = "'" + temp[0][0] + "'";

						region.ctl_county_str = temp[0][1];
						region.ctl_county_str_add = "'" + temp[0][1] + "'";

						region.ctl_sec_str = result[0][2];
						region.ctl_sec_str_add = "'" + result[0][2] + "'";
					}
				}

				setRegionStruct(region);
			}
			if (regionStruct.ctl_city_str != null
					&& !"".equals(regionStruct.ctl_city_str)) {
				if (regionStruct.ctl_city_str.indexOf(",") > 0) {
					regionStruct.ctl_in_or_equals = "in";
				} else {
					regionStruct.ctl_in_or_equals = "=";
				}
			}

		} catch (AppException ex) {
			ex.printStackTrace();
		}

		if (regionStruct.ctl_city_str_add.length() > 0) {
			String sqlBuilding = "select distinct BUILDING_BURA_ID from new_dim.d_building_bura where BUILDING_BURA_ID in("
					+ regionStruct.ctl_city_str_add + ")";

			try {
				String[][] resBuild = WebDBUtil.execQryArray(sqlBuilding, "");
				String retn = "";
				if (resBuild != null && resBuild.length > 0) {

					for (int i = 0; i < resBuild.length; i++) {
						if (retn.length() == 0) {
							retn = "'" + StringB.NulltoBlank(resBuild[i][0])
									+ "'";
						} else {
							retn += ",'" + StringB.NulltoBlank(resBuild[i][0])
									+ "'";
						}
					}
					regionStruct.CTL_BUILDING_BURA_ID = retn;
				} else {
					regionStruct.CTL_BUILDING_BURA_ID = "";
				}
			} catch (AppException e) {

				e.printStackTrace();
			}

		}

		return regionStruct;
	}

	public void setRegionStruct(UserCtlRegionStruct region) {
		regionStruct.ctl_metro_str = getString(regionStruct.ctl_metro_str,
				region.ctl_metro_str);
		regionStruct.ctl_metro_str_add = getString(
				regionStruct.ctl_metro_str_add, region.ctl_metro_str_add);
		regionStruct.ctl_city_str = getString(regionStruct.ctl_city_str,
				region.ctl_city_str);
		regionStruct.ctl_city_str_add = getString(
				regionStruct.ctl_city_str_add, region.ctl_city_str_add);
		regionStruct.ctl_county_str = getString(regionStruct.ctl_county_str,
				region.ctl_county_str);
		regionStruct.ctl_county_str_add = getString(
				regionStruct.ctl_county_str_add, region.ctl_county_str_add);
		regionStruct.ctl_sec_str = getString(regionStruct.ctl_sec_str,
				region.ctl_sec_str);
		regionStruct.ctl_sec_str_add = getString(regionStruct.ctl_sec_str_add,
				region.ctl_sec_str_add);
		regionStruct.ctl_area_str = getString(regionStruct.ctl_area_str,
				region.ctl_area_str);
		regionStruct.ctl_area_str_add = getString(
				regionStruct.ctl_area_str_add, region.ctl_area_str_add);

	}

	public String getString(String struct, String str) {
		boolean flag = false;
		if (struct.length() > 0) {
			String[] arr = struct.split(",");
			for (int i = 0; i < arr.length; i++) {
				if (str.equals(arr[i])) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				struct += "," + str;
			}
		} else {
			struct = str;
		}
		return struct;
	}

	public String getSqlStr(String str, String select, String where) {

		if (str.length() > 0) {
			str += " or " + where;
		} else {
			try {
				str = SQLGenator.genSQL("region001", select, where);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		return str.trim();
	}

	// 检查是否有该用户
	public boolean hasUser(String user_id) {
		return userDao.hasUser(user_id);
	}

	// 检查该用户密码是否正确
	public boolean checkUser(String user_id, String pwd) {
		return userDao.checkUser(user_id, pwd);
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
			System.out.println("login:" + sql);
			for (int i = 0; i < where.length; i++) {
				System.out.println("where[" + i + "]:" + where[i]);
			}
			return WebDBUtil.execQryArray(sql, where, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;
	}

	public String getDeptName(String dept_id) {
		return userDao.getDeptName(dept_id);
	}

	public String getWinUser(String user_id) {
		return userDao.getWinUser(user_id);
	}
}
