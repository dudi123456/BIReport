package com.ailk.bi.system.facade;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.table.InfoMenuTable;

@SuppressWarnings({ "rawtypes" })
public interface ICommonFacade {

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

	/**
	 * 
	 * @param session
	 * @return
	 * @desc:获取用户第一次菜单
	 * 
	 */

	public String getFirstUserHeadMenu(HttpSession session);

	/**
	 * 
	 * @param session
	 * @param menuId
	 * @desc:根据一级菜单获取明细菜单
	 * 
	 * @param out
	 * @param rootPath
	 * @throws IOException
	 */
	public void getUserChildMenuDetail(HttpSession session, String menuId,
			JspWriter out, String rootPath) throws IOException;

	// 获得该菜单的所有下级
	public InfoMenuTable getChildMenu(String menu_id);
}
