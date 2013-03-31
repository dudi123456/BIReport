package com.ailk.bi.system.service;

import java.util.HashMap;
import java.util.List;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.table.InfoMenuTable;

@SuppressWarnings({ "rawtypes" })
public interface ICommonService {

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public List getInfoResStruct();

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public HashMap getResMap(String type);

	/**
	 * 提取对象路径结构
	 * 
	 * @param obj_id
	 * @return KeyValueStruct 顺序数组
	 * @author jcm
	 */
	public KeyValueStruct[] getObjPathStruct(String type, String obj_id);

	/**
	 * 获取用户名
	 * 
	 * @param user_id
	 * @return String
	 * @author xh
	 */
	public String getUserName(String user_id);

	// 获得该菜单的所有下级
	public InfoMenuTable getChildMenu(String menu_id);
}
