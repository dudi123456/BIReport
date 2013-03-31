package com.ailk.bi.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.system.dao.ICommonDao;
import com.ailk.bi.system.dao.IUserDao;
import com.ailk.bi.system.dao.impl.CommonDaoImpl;
import com.ailk.bi.system.dao.impl.UserDaoImpl;
import com.ailk.bi.system.service.ICommonService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonServiceImpl implements ICommonService {

	private ICommonDao commonDao = new CommonDaoImpl();
	private IUserDao userDao = new UserDaoImpl();

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public List getInfoResStruct() {
		return commonDao.getInfoResStruct();
	}

	public List getInfoMenuStruct() {
		return commonDao.getInfoMenuStruct();
	}

	public List getInfoMenuStruct(String system_id) {
		return commonDao.getInfoMenuStruct(system_id);
	}

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public HashMap getResMap(String type) {
		return commonDao.getResMap(type);
	}

	/**
	 * 提取对象路径结构
	 * 
	 * @param obj_id
	 * @return KeyValueStruct 顺序数组
	 * @author jcm
	 */
	public KeyValueStruct[] getObjPathStruct(String type, String obj_id) {
		return commonDao.getObjPathStruct(type, obj_id);
	}

	/**
	 * 获取用户名
	 * 
	 * @param user_id
	 * @return String
	 * @author xh
	 */
	public String getUserName(String user_id) {
		InfoOperTable info = userDao.getUserInfo(user_id);
		String user_name = info.user_name;
		return user_name;
	}

	// 获得该菜单的所有下级
	public InfoMenuTable getChildMenu(String menu_id) {

		String[][] rs = commonDao.getChildMenu(menu_id);
		InfoMenuTable menuInfo = null;
		if (rs != null && rs.length > 0) {
			menuInfo = new InfoMenuTable();
			List newList = new ArrayList();
			for (int i = 0; rs != null && i < rs.length; i++) {
				if (i == 0) {
					menuInfo.menu_id = rs[i][0];
					menuInfo.menu_name = rs[i][1];
					menuInfo.parent_id = rs[i][2];
					menuInfo.menu_url = rs[i][3];
				} else {
					newList.add(rs[i]);
				}
			}
			menuInfo.childMenu = newList;
			UserServiceImpl userService = new UserServiceImpl();
			userService.getChildMenu(menuInfo);
		}

		return menuInfo;
	}

}
