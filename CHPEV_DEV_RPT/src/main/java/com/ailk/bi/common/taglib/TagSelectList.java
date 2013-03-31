package com.ailk.bi.common.taglib;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.ailk.bi.common.app.AppException;

/**
 * 通用下拉列表框的tag定义
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 *
 * @author WISEKING
 * @version 1.0 目前的使用格式为：
 *
 *          <BIBM:SelectList focusID = "1" [可选][focus的下拉列表条目][默认第一个条目] editMode
 *          = "1" [可选][下拉列表是否可选择][默认可选择] allFlag = "?"
 *          [可选][是否增加"全部"条目，后面的值为你设定的全部所对应的值][默认没有该条目] nvlFlag = "?"
 *          [可选][是否增加"空"条目，后面的值为你设定对应的值][默认没有该条目] listName = "MySelect"
 *          [必选][最后生成下拉列表框的名字][默认MySelect] listID = "1"
 *          [必选][在配置文件tagconf.xml中的selectID，或者如果listID以”!”开始，则从内存中获得下拉信息；
 *          如果为"0",则执行selfSQL从数据库查询；
 *          如果为"#",则从selfSQL里定义的列表集合中读取，列表格式："ID1,显示值1;ID2,显示值2;..."] selfSQL =
 *          "SELECT ...." [可选][当listID为0时，记录的选择从该条SQL语句中选择,或者定义串中提取]
 *          [当需要从后台交易信息中进行二次条件提取时使用
 *          "SELECT=field1,field2,..;WHERE=fieldm=xxx,field=xxx"格式] script = ""
 *          [可选][执行的JavaScript语句如：onChange="verify(this)"] scope = "session"
 *          [可选][该taglib的作用范围，取值为session或者application][默认session] />
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TagSelectList extends BodyTagSupport {
	/**
	 *
	 */
	private static final long serialVersionUID = 4373153828417974452L;

	final static String SELECT_LIST_DATAS = "taglib.SELECT_LIST_DATAS";

	final static String SELECT_ALL = "SELECT_ALL"; // 全部选择时候的OptionID

	final static String SELECT_IDSQL_MAPPING = "taglib.SELECT_IDSQL_MAPPING";

	/**
	 * 定义选择框属性
	 */
	private String focusID = null;

	private String editMode = "1";

	private String allFlag = null;

	private String allFlag_2 = null;

	public String getAllFlag_2() {
		return allFlag_2;
	}

	public void setAllFlag_2(String allFlag_2) {
		this.allFlag_2 = allFlag_2;
	}

	private String nvlFlag = null;

	private String listName = "MySelect";

	private String listID = null;

	private String selfSQL = "";

	private String script = "";

	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			// 取得设置
			BodyContent bc = getBodyContent();

			// 从tag的体中取得自定义的SQL
			if (listID == null && bc != null && bc.getString() != null) {
				listID = "0";
				selfSQL = bc.getString().trim();
				bc.clearBody();
			}

			/*******************************************************************
			 * 取得下拉菜单的数据
			 ******************************************************************/
			Vector vList = getListData(listID, selfSQL);

			if (vList == null) {
				out.print("<!-- SELECT ID=" + listName + " NO DATA FOUND! -->");
				vList = new Vector();
			}

			// 编辑模式
			if (editMode.equals("1")) {
				if ((script == null) || (script.equals(""))) {
					out.print("<SELECT ID=" + listName + " name=" + listName
							+ " >");
				} else {
					out.print("<SELECT ID=" + listName + " name=" + listName
							+ " " + script + ">");
				}

				Vector subV = null;
				for (int i = 0; i < vList.size(); i++) {
					subV = (Vector) vList.get(i);
					String strOptionDisp = "";
					String strSelected = "";

					// 设置全部可选项
					if (i == 0 && allFlag != null) {
						out.print("<OPTION value='" + allFlag
								+ "' selected>全部</OPTION>\n");
					}

					if (i == 0 && allFlag_2 != null) {
						out.print("<OPTION value='" + allFlag_2
								+ "'>不绑定</OPTION>\n");
					}

					if (i == 0 && nvlFlag != null) {
						out.print("<OPTION value='" + nvlFlag + "'></OPTION>\n");
					}

					for (int j = 0; j < subV.size(); j++) {
						String str = (String) subV.get(j);
						if (j == 0) { // option value
							out.print("<OPTION value='" + str + "' ");
							if (str.equals(focusID)) {
								strSelected = " selected ";
							}
						}
						if (j == 1) { // option display
							strOptionDisp = str;
						}
						if (j > 1) { // muti values
							out.print(" value" + (j + 1) + " = '" + str + "' ");
						}
						if (j == subV.size() - 1) {
							out.print(" " + strSelected + " >" + strOptionDisp
									+ "</OPTION>\n");
						}
					}
				}
				if (vList.size() == 0 && allFlag != null) {
					out.print("<OPTION value='" + allFlag
							+ "' selected>全部</OPTION>\n");
				}
				out.print("</SELECT>");

			} else if (editMode.equals("0")) { // 只读模式
				/* xulei 修改 2004-12-16 代码不匹配返回代码不返回空 */
				int m = 0;
				for (int i = 0; i < vList.size(); i++) {
					String optionID = (String) (((Vector) vList.elementAt(i))
							.elementAt(0));
					String strListName = (String) (((Vector) vList.elementAt(i))
							.elementAt(1));
					if (optionID.equals(focusID)) {
						out.print(strListName);
						m++;
						break;
					}
				}
				if (m == 0)
					out.print(focusID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	private Vector getListData(String strListID, String strSelfSQL) {
		String sql = null;
		Vector v = null;

		// 从交易结果中获取数据
		if (strListID.startsWith("!")) {
			HashMap hm = TaglibHelper.getTaglibMapps_BK();
			if (hm == null) {
				v = new Vector();
				return v;
			}
			v = (Vector) hm.get(strListID.substring(1));
			if (v == null) {
				v = new Vector();
			}

			if (selfSQL != null && !selfSQL.equals("")) // 二次提取
				try {
					v = TaglibHelper.trimObjsByCond(v, selfSQL);
				} catch (AppException ex) {
					Vector t = new Vector();
					t.add("ERROR");
					t.add(ex.toString());
					v.clear();
					v.add(t);
				}

			if (v == null) {
				v = new Vector();
			}
			// 去掉第一行头信息
			Vector vv = new Vector();
			for (int i = 1; i < v.size(); i++) {
				vv.add(v.elementAt(i));
			}
			v = vv;

			return v;
		}

		// 是否使用自定义SQL
		if ("#".equals(strListID)) {
			sql = "$!$" + strSelfSQL;
		} else if ("0".equals(strListID)) {
			sql = strSelfSQL;
		} else {
			// 根据listID取得list所对应的SQL语句
			sql = getSQLByListID(strListID);
		}
		// com.asiabi.common.app.Debug.println("sql="+sql);

		try {
			HashMap hm = null;
			if (!"0".equals(strListID) && !"#".equals(strListID)) {
				hm = (HashMap) pageContext.getSession().getAttribute(
						SELECT_LIST_DATAS);
				if (hm == null)
					hm = new HashMap();
				else {
					if (hm.containsKey(strListID)) {
						v = (Vector) hm.get(strListID);
						return v;
					}
				}
			}

			v = getVectorFromSqlOrValues(sql);
			if (v != null && !"0".equals(strListID) && !"#".equals(strListID)) {
				hm.put(strListID, v);
				pageContext.getSession().setAttribute(SELECT_LIST_DATAS, hm);
			}

			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getSQLByListID(String listID) {
		String retSQL = null;
		HashMap maps = null;

		maps = TaglibHelper.getTaglibMapps(pageContext.getServletContext());
		// com.asiabi.common.app.Debug.println("maps="+maps);
		if (maps == null) {
			return null;
		} else
			retSQL = (String) maps.get(listID);

		return retSQL;
	}

	private Vector getVectorFromSqlOrValues(String sql) throws AppException {
		Vector retV = null;
		if (sql == null) {
			return null;
		}

		if (sql.startsWith("$!$")) {// 值处理
			String values = sql.substring(3);
			values = values.replaceAll("&nbsp;", "#SOS#");
			StringTokenizer stSimecolon = new StringTokenizer(values, ";");
			retV = new Vector();
			while (stSimecolon.hasMoreTokens()) {
				String ss = stSimecolon.nextToken();
				StringTokenizer stComma = new StringTokenizer(ss, ",");
				if (stComma.hasMoreTokens()) {
					Vector subV = new Vector();
					while (stComma.hasMoreTokens()) {
						String sss = stComma.nextToken();
						if (sss.indexOf("#SOS#") > -1) {
							sss = sss.replaceAll("#SOS#", "&nbsp;");
						}
						subV.add(sss);
					}
					retV.add(subV);
				}
			}
		} else
			retV = com.ailk.bi.common.dbtools.WebDBUtil.execQryVector(sql,
					"");
		// com.asiabi.common.app.Debug.println("retV="+retV);
		return retV;
	}

	public void release() {
		this.listID = null;
		this.focusID = null;
		// 生命周期 需要查查资料
		// pageContext.getSession().removeAttribute(SELECT_LIST_DATAS);
	}

	public String getAllFlag() {
		return allFlag;
	}

	public String getEditMode() {
		return editMode;
	}

	public String getFocusID() {
		return focusID;
	}

	public String getListID() {
		return listID;
	}

	public String getListName() {
		return listName;
	}

	public String getScript() {
		return script;
	}

	public String getSelfSQL() {
		return selfSQL;
	}

	public void setSelfSQL(String selfSQL) {
		this.selfSQL = selfSQL;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setAllFlag(String allFlag) {
		this.allFlag = allFlag;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}

	public void setFocusID(String focusID) {
		this.focusID = focusID;
	}

	public void setListID(String listID) {
		this.listID = listID;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getNvlFlag() {
		return nvlFlag;
	}

	public void setNvlFlag(String nvlFlag) {
		this.nvlFlag = nvlFlag;
	}

}