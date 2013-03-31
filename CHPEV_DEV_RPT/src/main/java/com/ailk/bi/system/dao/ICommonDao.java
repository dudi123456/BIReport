package com.ailk.bi.system.dao;

import java.util.HashMap;
import java.util.List;

import com.ailk.bi.base.struct.KeyValueStruct;

@SuppressWarnings({ "rawtypes" })
public interface ICommonDao {

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public List getInfoResStruct();

	public List getInfoMenuStruct();

	public List getInfoMenuStruct(String system_id);

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

	// 获得该菜单的所有下级
	public String[][] getChildMenu(String menu_id);
}
