package com.ailk.bi.base.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import waf.controller.web.action.HTMLActionException;

//import com.ailk.bi.base.table.DBrandCodeTable;
//import com.ailk.bi.base.table.DSubBrandKndTable;
import com.ailk.bi.base.table.InfoMenuTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "unused", "rawtypes" })
public class JSTool {
	/**
	 * 渠道类型过滤资渠道类型
	 * 
	 * @param session
	 * @param
	 * @param
	 * @return
	 */
	public synchronized static String BrandCodeVsSubBrandKnd(
			HttpSession session, String codeObj, String subObj)
			throws HTMLActionException {
		String[][] code = getBrandCode();
		StringBuffer js = new StringBuffer();
		if (code == null) {
			js.append("<Script>alert('取维表(d_brand)信息失败！');\nfunction createBrandCode(){}\nfunction filterSubBrandKnd(code){}\n</Script>");
			return js.toString();
		}
		int j;
		js.append("<Script language=\"Javascript\">\n");
		// 生成OperType数据的JS函数
		js.append("function createBrandCode() {\n");
		js.append("  var theObj=" + codeObj + ";\n");
		js.append("  var formObj=" + codeObj + ";\n");
		js.append("  //获得func_oper_type对象所属的form对象\n");
		js.append("  while (formObj.tagName!=\"FORM\"){\n");
		js.append("    formObj=formObj.parentElement;\n");
		js.append("  }\n");
		js.append("  //获得记录func_oper_type对象所选项的隐含对象\n");
		js.append("  for (i=0;i<formObj.elements.length;i++){\n");
		js.append("  if (formObj.elements[i].name==theObj.name){\n");
		js.append("    hiObj=formObj.elements[i-1];\n");
		js.append("    break;\n");
		js.append("    }\n");
		js.append("  }\n");
		js.append("  //获得下拉框的所选的id\n");
		js.append("  oldIdx=hiObj.value;\n");
		js.append("  if (oldIdx==null || oldIdx==-1 || oldIdx=='') oldIdx=0;\n");
		js.append("  //重新设置下拉框的值\n");
		js.append("  if(theObj.tagName==\"SELECT\"){\n");
		js.append("    cleanSelect(theObj);\n");

		js.append("  theObj.options[theObj.length]=new Option('全部','');\n");
		for (j = 0; j < code.length; j++) {
			js.append("    theObj.options[theObj.length]=new Option('"
					+ code[j][1].trim() + "','" + code[j][0].trim() + "');\n");
		}

		js.append("  }\n");
		js.append("  //重新设置下拉框为已选的id\n");
		js.append("  theObj.options[oldIdx].selected=true;\n");
		js.append("}\n");
		js.append("</Script>\n");
		// 产生地市过滤区县的脚本
		if (!"".equals(subObj))
			js.append(setSubBrandKnd(session, subObj, code));
		return js.toString();
	}

	/**
	 * 过滤子渠道类型
	 * 
	 * @param session
	 * @param operTypeObj
	 * @param teleTypeObj
	 * @return
	 */
	public synchronized static String setSubBrandKnd(HttpSession session,
			String subObj, String[][] code) {
		String[][] subBrand = getSubBrandKnd();
		StringBuffer js = new StringBuffer();
		if (subBrand == null) {
			js.append("<Script>alert('取维表(d_sub_brand)信息失败！');\nfunction filterSubBrandKnd(code){}\n</Script>");
			return js.toString();
		}
		int i, j;

		js.append("<Script language=\"Javascript\">\n");
		// 生成按operType过滤teleType的JS函数
		js.append("function filterSubBrandKnd(code) {\n");
		js.append("try{\n");
		js.append("  var theObj=" + subObj + ";\n");
		js.append("  var formObj=" + subObj + ";\n");
		js.append("  while (formObj.tagName!=\"FORM\"){\n");
		js.append("    formObj=formObj.parentElement;\n");
		js.append("  }\n");
		js.append("  for (i=0;i<formObj.elements.length;i++){\n");
		js.append("  if (formObj.elements[i].name==theObj.name){\n");
		js.append("    hiObj=formObj.elements[i-1];\n");
		js.append("    break;\n");
		js.append("    }\n");
		js.append("  }\n");
		js.append("  oldIdx=hiObj.value;\n");
		js.append("  if (oldIdx==null || oldIdx==-1 || oldIdx=='') oldIdx=0;\n");
		js.append("  cleanSelect(theObj);\n");
		js.append("  theObj.options[theObj.length]=new Option('全部','');\n");

		for (i = 0; i < code.length; i++) {
			js.append("  if (code=='" + code[i][0] + "'){\n");
			for (j = 0; j < subBrand.length; j++) {
				if (subBrand[j][2].equals(code[i][0])) {
					js.append("    theObj.options[theObj.length]=new Option('"
							+ subBrand[j][1].trim() + "','"
							+ subBrand[j][0].trim() + "');\n");

				}
			}
			js.append("  }\n");

		}

		js.append("  if (code==''){\n");
		for (j = 0; j < subBrand.length; j++) {
			js.append("    theObj.options[theObj.length]=new Option('"
					+ subBrand[j][1].trim() + "','" + subBrand[j][0].trim()
					+ "');\n");
		}
		js.append("  }\n");

		js.append("  theObj.options[oldIdx].selected=true;\n");
		js.append("}catch(e){;}\n");
		js.append("}\n");
		js.append("</Script>\n");
		return js.toString();
	}

	public static String[][] getBrandCode() {
		String sql = "select brand_id,brand_name from D_BRAND order by sequence";
		String[][] svces = null;
		try {
			svces = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return svces;

	}

	public static String[][] getSubBrandKnd() {
		String sql = "select sub_brand_id,sub_brand_name,brand_id,brand_name from D_SUB_BRAND order by sequence";
		String[][] svces = null;
		try {
			svces = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return svces;

	}

	public static String getFirstUserHeadMenu(HttpSession session) {
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

				if (i == 0) {
					js.append(",new Array('taber"
							+ xx
							+ "','"
							+ menuInfo.menu_name
							+ "','javascript:top.mainFrame.location.href=\"panel_frameset.jsp?menuId="
							+ menuInfo.menu_id + "\"','')\n");
				} else {
					js.append(",new Array('taber" + xx + "','"
							+ menuInfo.menu_name
							+ "','javascript:navLeftTree(\"" + menuInfo.menu_id
							+ "\",\"" + menuInfo.menu_url + "\");','')\n");
				}

			}

			js.append(");");
		}

		return js.toString();
	}

	public static String getUserRelateMenu(HttpSession session, String menuId) {
		List menuList = (ArrayList) session
				.getAttribute(WebConstKeys.LOGIN_USER_MENU_LIST);
		StringBuffer js = new StringBuffer();
		js.append("var t,it;\n");
		if (menuList != null && menuList.size() > 0) {

			int xx = 0;
			for (int i = 0; i < menuList.size(); i++) {
				xx = i + 1;
				InfoMenuTable menuInfo = (InfoMenuTable) menuList.get(i);
				String menuIdTmp = menuInfo.menu_id;
				if (menuIdTmp.equals(menuId)) {// 根据ID查子菜单
					// 一级
					List childMenuList = menuInfo.childMenu;
					if (childMenuList != null) {//
						for (int m = 0; m < childMenuList.size(); m++) {
							InfoMenuTable menuInfo_1 = (InfoMenuTable) childMenuList
									.get(m);
						}
					} else {

					}

				}
				if (i == 0) {
					js.append(",new Array('taber"
							+ xx
							+ "','"
							+ menuInfo.menu_name
							+ "','javascript:top.mainFrame.location.href=\"panel_left_tree2.jsp\"','')\n");
				} else {
					js.append(",new Array('taber" + xx + "','"
							+ menuInfo.menu_name
							+ "','javascript:navLeftTree(\"" + menuInfo.menu_id
							+ "\",\"" + menuInfo.menu_url + "\");','')\n");
				}

			}

			js.append(");");
		}

		return js.toString();
	}

	public static void getUserChildMenuDetail(HttpSession session,
			String menuId, JspWriter out, String rootPath) throws IOException {
		out.println("var t,it;");
		// outlookbar= new outlook();
		String strFirstName = "";
		List menuList = (ArrayList) session
				.getAttribute(WebConstKeys.LOGIN_USER_MENU_LIST);
		if (menuList != null && menuList.size() > 0) {

			for (int i = 0; i < menuList.size(); i++) {
				InfoMenuTable menuInfo = (InfoMenuTable) menuList.get(i);
				if (menuInfo.menu_id.equals("901096")) {
					System.out.println("menuId901096 size："
							+ menuInfo.childMenu.size());
				}
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
								getUserChildMenu_02(childList_2, out, rootPath);
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

	public static void getUserChildMenu_02(List childList, JspWriter out,
			String rootPath) throws IOException {
		for (int m = 0; m < childList.size(); m++) {
			InfoMenuTable menuInfoTmp = (InfoMenuTable) childList.get(m);

			out.println("it=outlookbar.additem('"
					+ menuInfoTmp.menu_name
					+ "',t,'"
					+ rootPath
					+ StringTool.getSysMenuCodeFromUrl(menuInfoTmp.menu_url,
							menuInfoTmp.menu_id) + "');");

			List childList_3 = menuInfoTmp.childMenu;
			if (childList_3 != null) {
				getUserChildMenu_03(childList_3, out, rootPath);
			}

		}

	}

	public static void getUserChildMenu_03(List childList, JspWriter out,
			String rootPath) throws IOException {

		for (int m = 0; m < childList.size(); m++) {
			InfoMenuTable menuInfoTmp = (InfoMenuTable) childList.get(m);
			out.println("outlookbar.addsubitem('"
					+ menuInfoTmp.menu_name
					+ "',t,it,'"
					+ rootPath
					+ StringTool.getSysMenuCodeFromUrl(menuInfoTmp.menu_url,
							menuInfoTmp.menu_id) + "');");

			List childList_4 = menuInfoTmp.childMenu;
			if (childList_4 != null) {
				System.out.println("four:");
				getUserChildMenu_04(childList_4, out, rootPath);
			}

		}

	}

	public static void getUserChildMenu_04(List childList, JspWriter out,
			String rootPath) throws IOException {
		for (int m = 0; m < childList.size(); m++) {
			InfoMenuTable menuInfoTmp = (InfoMenuTable) childList.get(m);
			out.println("outlookbar.addsubitem('"
					+ menuInfoTmp.menu_name
					+ "',t,it,'"
					+ rootPath
					+ StringTool.getSysMenuCodeFromUrl(menuInfoTmp.menu_url,
							menuInfoTmp.menu_id) + "');");

		}

	}

	public static String getUserChildMenu(InfoMenuTable menuInfo, String strOut) {

		strOut += "menuid:" + menuInfo.menu_id + "\n";
		List childList = menuInfo.childMenu;
		if (childList != null) {
			for (int m = 0; m < childList.size(); m++) {
				InfoMenuTable menuInfoTmp = (InfoMenuTable) childList.get(m);
				// strOut += getUserChildMenu(menuInfoTmp,strOut);
				System.out.println("menuidc:" + menuInfoTmp.menu_id + "\n");
				strOut += "menuid:" + menuInfoTmp.menu_id + "\n";
				// js.append(getUserChildMenu(menuInfoTmp,js));
			}

		}
		return strOut;
	}

	public static String getAdhocRole(String user_id) {
		String sql = "";
		String role_id = null;
		try {
			sql = SQLGenator.genSQL("ar008", user_id, user_id);
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				role_id = arr[0][0];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return role_id;
	}

	/**
	 * 
	 * @return
	 */
	public static String printDeviceModel() {

		StringBuffer js = new StringBuffer();
		js.append("var onecount;\n");
		js.append("onecount=0;\n");
		js.append("subcat = new Array();\n");

		String sql = "select * from D_DEVICE_BRAND_MODEL";
		try {
			String[][] rs = WebDBUtil.execQryArray(sql);
			if (rs != null && rs.length > 0) {

				for (int i = 0; i < rs.length; i++) {

					String tmp = "subcat[" + i + "] = new Array('" + rs[i][1]
							+ "','" + rs[i][1] + "','" + rs[i][0] + "');\n";
					js.append(tmp);

				}
				js.append("onecount=" + rs.length + ";\n");
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return js.toString();
	}

	public static String getProduct() {

		StringBuffer js = new StringBuffer();
		js.append("var onecount;\n");
		js.append("onecount=0;\n");
		js.append("subcat = new Array();\n");

		String sql = "select cch_prod_id,cch_prod_name,cch_prod_cat_id from d_cch_prod";
		try {
			String[][] rs = WebDBUtil.execQryArray(sql);
			if (rs != null && rs.length > 0) {

				for (int i = 0; i < rs.length; i++) {

					String tmp = "subcat[" + i + "] = new Array('" + rs[i][1]
							+ "','" + rs[i][0] + "','" + rs[i][2] + "');\n";
					js.append(tmp);

				}
				js.append("onecount=" + rs.length + ";\n");
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return js.toString();
	}
}
