package com.ailk.bi.system.facade.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.JSTool;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.system.facade.ICommonFacade;
import com.ailk.bi.system.service.impl.CommonServiceImpl;

@SuppressWarnings({ "rawtypes" })
public class CommonFacade implements ICommonFacade {

	private CommonServiceImpl commonService = new CommonServiceImpl();
	/**
	 * 取得登录所有系统资源
	 *
	 * @author jcm
	 * @return
	 */
	public List getInfoResStruct() {
		return commonService.getInfoResStruct();
	}

	public List getInfoMenuStruct() {
		return commonService.getInfoMenuStruct();
	}

	public List getInfoMenuStruct(String system_id) {
		return commonService.getInfoMenuStruct(system_id);
	}

	/**
	 * 取得登录所有系统资源
	 *
	 * @author jcm
	 * @return
	 */
	public HashMap getResMap(String type) {
		return commonService.getResMap(type);
	}

	/**
	 * 提取对象路径结构
	 *
	 * @param obj_id
	 * @return KeyValueStruct 顺序数组
	 * @author jcm
	 */
	public KeyValueStruct[] getObjPathStruct(String type, String obj_id) {
		return commonService.getObjPathStruct(type, obj_id);
	}

	/**
	 * 获取用户名
	 *
	 * @param user_id
	 * @return String
	 * @author xh
	 */
	public String getUserName(String user_id) {
		return commonService.getUserName(user_id);
	}

	/**
	 * 生成一级菜单
	 *
	 */
	public String getFirstUserHeadMenu(HttpSession session) {

		List menuList = (ArrayList) session
				.getAttribute(WebConstKeys.LOGIN_USER_MENU_LIST);
		StringBuffer js = new StringBuffer();
		js.append("var taberArray=\n");
		if (menuList != null && menuList.size() > 0) {
			js.append("new Array(''");
			int xx = 0;
			for (int i = 0; i < menuList.size(); i++) {
				xx = i + 1;
				InfoMenuTable menuInfo = (InfoMenuTable) menuList.get(i);
				//
				if (null == menuInfo.childMenu
						|| menuInfo.childMenu.size() <= 0) {

					String menuUrl = StringB.NulltoBlank(menuInfo.menu_url);

					if (menuInfo.openType.equals("1")) {
						// 弹开方式
						js.append(",new Array('taber" + xx + "','"
								+ menuInfo.menu_name
								+ "','javascript:window.open(\"" + menuUrl
								+ "\")','')\n");
					} else {
						js.append(",new Array('taber"
								+ xx
								+ "','"
								+ menuInfo.menu_name
								+ "','javascript:parent.mainFrame.location.href=\""
								+ StringTool.getSysMenuCodeFromUrl(menuUrl,
										menuInfo.menu_id) + "\"','')\n");

					}

				} else {
					String menuUrl = StringB.NulltoBlank(menuInfo.menu_url);

					if (menuInfo.openType.equals("1")) {
						// 弹开方式
						js.append(",new Array('taber" + xx + "','"
								+ menuInfo.menu_name
								+ "','javascript:window.open(\"" + menuUrl
								+ "\")','')\n");
					} else {
						js.append(",new Array('taber"
								+ xx
								+ "','"
								+ menuInfo.menu_name
								+ "','javascript:parent.mainFrame.location.href=\"panel_frameset.jsp?menuId="
								+ menuInfo.menu_id + "\"','')\n");

					}

				}

			}
			js.append(");");
		} else {
			js.append("new Array(''");
			js.append(",new Array('taber1','指标说明','javascript:window.open(\"../adhoc/adhocMetaExplain.rptdo?adhoc_id=ADH101\")','')\n");

			js.append(");");
		}

		return js.toString();
	}

	public void getUserChildMenuDetail(HttpSession session, String menuId,
			JspWriter out, String rootPath) throws IOException {
		out.println("var t,it;");
		// outlookbar= new outlook();
		String strFirstName = "";
		List menuList = (ArrayList) session
				.getAttribute(WebConstKeys.LOGIN_USER_MENU_LIST);
		if (menuList != null && menuList.size() > 0) {

			for (int i = 0; i < menuList.size(); i++) {
				InfoMenuTable menuInfo = (InfoMenuTable) menuList.get(i);

				if (menuInfo.menu_id.equals(menuId)) {
					List childList = menuInfo.childMenu;
					if (childList != null) {
						for (int m = 0; m < childList.size(); m++) {
							InfoMenuTable menuInfoTmp = (InfoMenuTable) childList
									.get(m);
							// 一级
							if (m == 0) {
								strFirstName = menuInfoTmp.menu_name;
							}

							out.println("t=outlookbar.addtitle('"
									+ menuInfoTmp.menu_name + "');");
							List childList_2 = menuInfoTmp.childMenu;
							if (childList_2 != null) {
								// 二级
								JSTool.getUserChildMenu_02(childList_2, out,
										rootPath);
							}
							// strOut += getUserChildMenu(menuInfoTmp,strOut);
						}

					}
				}
			}
			out.println("locatefold(\"" + strFirstName + "\");");
			out.println("outlookbar.show();");

		}

	}

	// 获得该菜单的所有下级
	public InfoMenuTable getChildMenu(String menu_id) {
		return commonService.getChildMenu(menu_id);
	}

	/**
	 * 获得登录用户对象
	 *
	 * @param session
	 * @return
	 * @author jcm
	 */
	public static InfoOperTable getLoginUser(HttpSession session) {
		if (session == null) {
			return null;
		}
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (loginUser == null) {
			return null;
		}
		return loginUser;
	}

	/**
	 * 获得登录用户对象的ID
	 *
	 * @param session
	 * @return
	 * @author jcm
	 */
	public static String getLoginId(HttpSession session) {
		if (session == null) {
			return null;
		}
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (loginUser == null) {
			return null;
		}
		return loginUser.user_id;
	}

	/**
	 *
	 * @param session
	 * @desc:获取登录用户的即席查询角色编码,role_type=3的
	 * @return
	 */
	public static String getUserAdhocRole(HttpSession session) {

		String user_id = getLoginId(session);
		return JSTool.getAdhocRole(user_id);

		// return "027";

	}

	/**
	 * 获得登录用户对象名称
	 *
	 * @param session
	 * @return
	 * @author jcm
	 */
	public static String getLoginName(HttpSession session) {
		if (session == null) {
			return null;
		}
		InfoOperTable loginUser = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		if (loginUser == null) {
			return null;
		}
		return loginUser.user_name;
	}

	/**
	 * 取得一个特定MAP特定KEY的值
	 *
	 * @param request
	 * @param map_code
	 * @param key
	 * @return
	 */
	public static String codeListParamFetcher(HttpServletRequest request,
			String map_code, String key) {
		String value = "";
		// System.out.println("================"+map_code);
		ServletContext context = request.getSession().getServletContext();
		HashMap codeMap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if (codeMap != null) {
			HashMap map = (HashMap) codeMap.get(map_code.trim().toUpperCase());
			if (map != null) {
				if (map.get(key) == null) {
					value = "";
				} else {
					value = (String) map.get(key);
				}

			}
		}
		return value;
	}

	/**
	 * 取得一个特定MAP
	 *
	 * @param request
	 * @param map_code
	 * @return
	 */
	public static HashMap codeListParamFetcher(HttpServletRequest request,
			String map_code) {
		HashMap map = null;
		ServletContext context = request.getSession().getServletContext();
		HashMap codeMap = (HashMap) context
				.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if (codeMap != null) {
			map = (HashMap) codeMap.get(map_code.toUpperCase());
		}
		return map;
	}
}
