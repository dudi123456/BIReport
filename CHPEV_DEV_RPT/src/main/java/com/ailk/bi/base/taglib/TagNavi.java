package com.ailk.bi.base.taglib;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.base.struct.InfoResStruct;
import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.system.facade.impl.CommonFacade;

/**
 * 页面导航条标签程序（即时搜索、我要反馈）
 *
 * @author jcm
 *
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class TagNavi extends BodyTagSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String getRes_or_menu_flag() {
		return res_or_menu_flag;
	}

	public void setRes_or_menu_flag(String res_or_menu_flag) {
		this.res_or_menu_flag = res_or_menu_flag;
	}

	private String res_or_menu_flag = "menu";

	private String objID = null;// 对象标识

	private boolean backTag = true;// 反馈标志

	private boolean searchTag = true;// 搜索标志

	private boolean favTag = true;// 收藏标志

	private String defaultValue = null;// 对象默认值（防止传值为空）

	private String prefix = null;// 前置对象

	private String target = null;

	private String postfix = null;// 后置对象

	protected String tmpValue = null;// 转换值

	private CommonFacade facade = new CommonFacade();

	private String rootPath = null;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getObjID() {
		return objID;
	}

	public boolean getBackTag() {
		return backTag;
	}

	public boolean getSearchTag() {
		return searchTag;
	}

	public boolean getFavTag() {
		return favTag;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setObjID(String obj_id) {
		this.objID = obj_id;
	}

	public void setBackTag(boolean back_tag) {
		this.backTag = back_tag;
	}

	public void setSearchTag(boolean search_tag) {
		this.searchTag = search_tag;
	}

	public void setFavTag(boolean fav_tag) {
		this.favTag = fav_tag;
	}

	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	public void setPrefix(String value) {
		this.prefix = value;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPostfix(String value) {
		this.postfix = value;
	}

	public String getPostfix() {
		return postfix;
	}

	public int doStartTag() throws JspException {

		try {
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			HttpSession session = request.getSession();
			if (request != null)
			{
				tmpValue = request.getParameter("SysMenuCode");
				if (tmpValue == null || "".equals(tmpValue))
				{
					if (objID != null)
					{
						tmpValue = objID;
					}
					else
					{
						tmpValue = session.getAttribute("SysMenuCode") + "";
						if (tmpValue == null || "".equals(tmpValue))
						{
							tmpValue = defaultValue;
						}
					}
				}
				else
				{
					session.removeAttribute("SysMenuCode");
					session.setAttribute("SysMenuCode", tmpValue);
				}

				objID = tmpValue;
				// System.out.println("objID==============" + objID);
				// 上下文
				if (rootPath == null) {
					rootPath = request.getContextPath();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			HttpSession session = pageContext.getSession();
			ServletContext context = session.getServletContext();
			if (session != null) {
				InfoResStruct[] infoRes = (InfoResStruct[]) session
						.getAttribute(WebConstKeys.ATTR_C_INFOMenuSTRUCT);
				HashMap rightsmap = (HashMap) session
						.getAttribute(WebConstKeys.ATTR_C_RES_RIGHTS_MAP);
				String NaviStr = getNavigationStr(session, objID, backTag,
						defaultValue, searchTag, favTag, infoRes, rightsmap,
						postfix);
				out.print(NaviStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	/**
	 * 生成导航条
	 *
	 * @param obj_id
	 *            对象ID
	 * @param flag
	 *            反馈标志
	 * @param defaultValue
	 *            默认对象值
	 * @param serchflag
	 *            即时搜索
	 * @param favflag
	 *            收藏标志
	 * @param infoRes
	 *            系统资源
	 * @return
	 * @author jcm
	 */
	private String getNavigationStr(HttpSession session, String obj_id,
			boolean flag, String defaultValue, boolean serchflag,
			boolean favflag, InfoResStruct[] infoRes, HashMap rightsmap,
			String postfix) {

		// String tag = "&gt;&gt;";
		// 根据资源类型来提取资源MAP
		HashMap resMap = facade.getResMap(getRes_or_menu_flag());
		StringBuffer buff = new StringBuffer("您所在位置：");
		KeyValueStruct[] path = facade.getObjPathStruct(getRes_or_menu_flag(),
				obj_id);
		for (int i = 0; path != null && i < path.length; i++) {
			if (i == path.length - 1) {
				buff.append("<em class=\"red\">" + path[i].value + "</em> ");
			} else {
				buff.append(path[i].value + " >> ");
			}
		}
		buff.append("<span>");
		// buff.append("<a href=\"" + rootPath +
		// "/system/favoriteAdd.jsp?res_id="
		// + obj_id + "\" target=\"addFavor\" class=\"icon cl\">收藏</a> ");

		buff.append("<a href=\"javascript:;\" onclick=\"window.open('"
				+ rootPath
				+ "/system/favoriteAdd.jsp?res_id="
				+ obj_id
				+ "','addFavor','width=700,height=290,top=250,left=250,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=auto,resizable=0');\"  class=\"icon cl\">收藏</a> ");

		buff.append("<a href=\"" + rootPath + "/bibbs/index?res_id=" + obj_id
				+ "\" target=\"_blank\" class=\"icon goforum\">进入论坛</a>");
		buff.append("</span>");

		// buff
		// .append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
		// buff.append("<tr>");
		// buff.append("<td width=\"5\" background=\"" + rootPath
		// + "/biimages/home/nav_bg.gif\">");
		// buff
		// .append("<img src=\""
		// + rootPath
		// + "/biimages/home/nav_corner.gif\">"
		// + "</td><td background=\""
		// + rootPath
		// + "/biimages/home/nav_bg.gif\" width=\"18\">"
		// + "<img src=\""
		// + rootPath
		// +
		// "/biimages/home/nav_icon.gif\" width=\"14\" height=\"11\" hspace=\"10\"></td><td background=\""
		// + rootPath
		// + "/biimages/home/nav_bg.gif\"  width=\"100%\">");
		//
		// buff.append("<link rel=\"stylesheet\" href=\"" + rootPath
		// + "/css/winclassic.css\" type=\"text/css\">\n");
		// buff.append("<script type=\"text/javascript\" src=\"" + rootPath
		// + "/js/poslib.js\"></script>\n");
		// buff.append("<script type=\"text/javascript\" src=\"" + rootPath
		// + "/js/scrollbutton.js\"></script>\n");
		// buff.append("<script type=\"text/javascript\" src=\"" + rootPath
		// + "/js/menu4.js\"></script>\n");
		// buff.append("<script type=\"text/javascript\" src=\"" + rootPath
		// + "/js/skintop.js\"></script> \n");
		// buff.append("<script type=\"text/javascript\">\n");
		//
		// buff.append("Menu.prototype.cssFile = cssFile;\n");
		// buff.append("Menu.prototype.mouseHoverDisabled = true;\n");
		// buff.append("var tmp;\n");
		// buff.append("var mb = new MenuBar;\n");
		// buff.append("mb.add( tmp = new MenuButton(\"您所在位置:\") ); \n");
		//
		// KeyValueStruct[] path =
		// facade.getObjPathStruct(getRes_or_menu_flag(),
		// obj_id);
		//
		// for (int i = 0; path != null && i < path.length; i++) {
		// //
		// buff.append(getNaviString(path[i].key, path[i].parent_key, infoRes,
		// resMap, String.valueOf(i), rightsmap));
		// if (i < path.length - 1) {
		// buff.append("mb.add( tmp = new MenuButton( \""
		// + resMap.get(path[i].key).toString() + ">>\", res_" + i
		// + "_" + path[i].key + " ,true) ); \n");
		// } else {
		// if (postfix == null || "".equals(postfix)) {
		// buff
		// .append("mb.add( tmp = new MenuButton( \"<font color='red'>"
		// + resMap.get(path[i].key).toString()
		// + "</font>\", null ,true) ); \n");
		// } else {
		// buff.append("mb.add( tmp = new MenuButton( \""
		// + resMap.get(path[i].key).toString()
		// + ">>\", null ,true) ); \n");
		// }
		//
		// }
		//
		// }
		//
		// if (postfix != null && !"".equals(postfix)) {
		// buff.append("mb.add( tmp = new MenuButton( \"<font color='red'>"
		// + postfix + "</font>\", null ,true) ); \n");
		// }
		// buff.append("mb.write();\n");
		// buff.append("</script>");
		// buff.append("<script type=\"text/javascript\" src=\"" + rootPath
		// + "/js/skintail.js\"></script>");
		//
		// buff.append("</td>");
		// buff.append("<td width=\"1\" bgcolor=\"BABEC1\"></td>");
		// buff.append("<td width=\"180\" nowrap background=\"" + rootPath
		// + "/biimages/home/nav_bg.gif\">");
		// buff
		// .append("<a href=\""
		// + rootPath
		// + "/system/favoriteAdd.jsp?res_id="
		// + obj_id
		// + "\" target=\"addFavor\"><img src=\""
		// + rootPath
		// +
		// "/biimages/home/nav_icon2.gif\" width=\"14\" height=\"14\" border=\"0\" hspace=\"10\">收藏</a>");
		// buff
		// .append("<a href=\""
		// + rootPath
		// + "/navToForum.jsp?res_id="
		// + obj_id
		// + "\" target=\"_blank\"><img src=\""
		// + rootPath
		// +
		// "/biimages/home/nav_icon3.gif\" width=\"13\" height=\"15\" border=\"0\" hspace=\"10\">进入论坛</a>");
		// buff.append("</td>");
		// buff.append("</tr>");
		// buff.append("</table>");
		return buff.toString();
	}

	/**
	 *
	 * @param value
	 * @param infoRes
	 * @return
	 */
	public String getNaviString(String key, String parent_key,
			InfoResStruct[] infoRes, HashMap map, String index,
			HashMap rightsmap) {
		StringBuffer sb = new StringBuffer();

		String resObj = "res_" + index + "_" + key;
		sb.append(" var " + resObj + " = new Menu(); \n");

		for (int j = 0; infoRes != null && j < infoRes.length; j++) {

			if (infoRes[j].parent_id.equals(key)) {

				if (null != infoRes[j].submenu && infoRes[j].submenu.length > 0) {// 再次遍历
					sb.append(getNaviString(infoRes[j].res_id, parent_key,
							infoRes, map, index, rightsmap).toString());
				} else {// 直接显示
					String tmp = null;
					if (null != target && !"".equals(target)) {
						tmp = "function getQueryUrl() { " + target
								+ ".document.location='" + rootPath
								+ infoRes[j].res_url + "'; }";
					} else {
						tmp = "function getQueryUrl() { document.location='"
								+ rootPath + infoRes[j].res_url + "'; }";
					}
					sb.append(resObj + ".add( tmp = new MenuItem( \""
							+ infoRes[j].res_name + "\" ," + tmp + ") ); \n");
					// if (rightsmap.get(infoRes[j].res_id) == null) {
					// sb.append("tmp.disabled = true;\n");
					// }

				}

			}

		}

		//
		String parent_id = "";
		for (int t = 0; t < infoRes.length; t++) {
			if (infoRes[t].res_id.equals(key)) {
				parent_id = infoRes[t].parent_id;
				break;
			}
		}
		String tmp = "";
		if (!parent_id.equals(parent_key)) {
			tmp = "res_" + index + "_" + parent_id;
		}
		if (!"".equals(tmp)) {
			sb.append(tmp + ".add( tmp = new MenuItem( \""
					+ map.get(key).toString() + "\", null, null, " + resObj
					+ " ) ); \n");
		}
		return sb.toString();

	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
