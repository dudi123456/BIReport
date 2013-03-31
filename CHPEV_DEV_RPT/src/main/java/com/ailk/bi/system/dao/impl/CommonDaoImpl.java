package com.ailk.bi.system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ailk.bi.base.struct.InfoResStruct;
import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.dao.ICommonDao;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonDaoImpl implements ICommonDao {

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public List getInfoResStruct() {
		List list = new ArrayList();

		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q0028");
			// System.out.println("Q0028=================" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				for (int i = 0; i < result.length; i++) {
					InfoResStruct infores = new InfoResStruct();
					infores = new InfoResStruct();
					infores.res_id = result[i][0];
					infores.parent_id = result[i][1];
					infores.is_res = result[i][2];
					infores.res_name = result[i][3];
					infores.res_url = result[i][4];
					//
					int count = 0;
					for (int m = 0; m < result.length; m++) {
						if (result[m][1].equals(infores.res_id)) {
							count++;
						}
					}
					//
					// System.out.println(count);
					//
					if (count > 0) {
						infores.submenu = new InfoResStruct[count];
						int num = 0;
						for (int m = 0; m < result.length; m++) {
							if (result[m][1].equals(infores.res_id)) {
								infores.submenu[num] = new InfoResStruct();
								infores.submenu[num].res_id = result[i][0];
								infores.submenu[num].parent_id = result[i][1];
								infores.submenu[num].is_res = result[i][2];
								infores.submenu[num].res_name = result[i][3];
								infores.submenu[num].res_url = result[i][4];
								num++;
							}
						}
					}
					list.add(infores);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List getInfoMenuStruct(String system_id) {
		List list = new ArrayList();

		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q0027_001", system_id);
			// System.out.println("Q0027=================" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				for (int i = 0; i < result.length; i++) {
					InfoResStruct infores = new InfoResStruct();
					infores = new InfoResStruct();
					infores.res_id = result[i][0];
					infores.parent_id = result[i][1];
					infores.is_res = result[i][2];
					infores.res_name = result[i][3];
					infores.res_url = result[i][4];
					//
					int count = 0;
					for (int m = 0; m < result.length; m++) {
						if (result[m][1].equals(infores.res_id)) {
							count++;
						}
					}
					//
					// System.out.println(count);
					//
					if (count > 0) {
						infores.submenu = new InfoResStruct[count];
						int num = 0;
						for (int m = 0; m < result.length; m++) {
							if (result[m][1].equals(infores.res_id)) {
								infores.submenu[num] = new InfoResStruct();
								infores.submenu[num].res_id = result[i][0];
								infores.submenu[num].parent_id = result[i][1];
								infores.submenu[num].is_res = result[i][2];
								infores.submenu[num].res_name = result[i][3];
								infores.submenu[num].res_url = result[i][4];
								num++;
							}
						}
					}
					list.add(infores);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}

		return list;
	}

	public List getInfoMenuStruct() {
		List list = new ArrayList();

		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q0027");
			// System.out.println("Q0027=================" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				for (int i = 0; i < result.length; i++) {
					InfoResStruct infores = new InfoResStruct();
					infores = new InfoResStruct();
					infores.res_id = result[i][0];
					infores.parent_id = result[i][1];
					infores.is_res = result[i][2];
					infores.res_name = result[i][3];
					infores.res_url = result[i][4];
					//
					int count = 0;
					for (int m = 0; m < result.length; m++) {
						if (result[m][1].equals(infores.res_id)) {
							count++;
						}
					}
					//
					// System.out.println(count);
					//
					if (count > 0) {
						infores.submenu = new InfoResStruct[count];
						int num = 0;
						for (int m = 0; m < result.length; m++) {
							if (result[m][1].equals(infores.res_id)) {
								infores.submenu[num] = new InfoResStruct();
								infores.submenu[num].res_id = result[i][0];
								infores.submenu[num].parent_id = result[i][1];
								infores.submenu[num].is_res = result[i][2];
								infores.submenu[num].res_name = result[i][3];
								infores.submenu[num].res_url = result[i][4];
								num++;
							}
						}
					}
					list.add(infores);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 取得登录所有系统资源
	 * 
	 * @author jcm
	 * @return
	 */
	public HashMap getResMap(String type) {
		HashMap map = new HashMap();
		String sql = "";
		try {
			// SELECT RES_ID,PARENT_ID,ISRESOURCE,NAME ,URL
			// FROM UI_PUB_INFO_RESOURCE
			if ("res".equalsIgnoreCase(type)) {
				sql = SQLGenator.genSQL("Q0029");
			} else {
				sql = SQLGenator.genSQL("Q0030");
			}

			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				for (int i = 0; i < result.length; i++) {
					map.put(result[i][0].trim(), result[i][3].trim());
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 提取对象路径结构
	 * 
	 * @param obj_id
	 * @return KeyValueStruct 顺序数组
	 * @author jcm
	 */
	public KeyValueStruct[] getObjPathStruct(String type, String obj_id) {
		KeyValueStruct struct[] = null;
		if (obj_id == null || "".equals(obj_id)) {
			return struct;
		}
		String sql = "";
		try {
			if ("res".equalsIgnoreCase(type)) {
				sql = SQLGenator.genSQL("Q0009", obj_id);
			} else {
				sql = SQLGenator.genSQL("Q0010", obj_id);
			}

			String arr[][] = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				struct = new KeyValueStruct[arr.length];
				for (int i = 0; i < arr.length; i++) {
					struct[i] = new KeyValueStruct();
					struct[i].level = arr[i][0];
					struct[i].key = arr[i][1];
					struct[i].value = arr[i][2];
					struct[i].parent_key = arr[i][3];
				}
			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return struct;

	}

	// 获得该菜单的所有下级
	public String[][] getChildMenu(String menu_id) {
		String sql = "";
		String[][] arr = null;
		try {
			sql = SQLGenator.genSQL("menu001", menu_id, menu_id);
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return arr;
	}
}
