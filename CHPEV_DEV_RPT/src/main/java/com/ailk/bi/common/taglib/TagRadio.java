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
 * 通用Radio的tag定义
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
 *          <BIBM:TagRadio focusID = "1" [可选][focus的下拉列表条目][默认第一个条目] radioName =
 *          "MySelect" [必选][最后生成下拉列表框的名字][默认MySelect] radioID = "1"
 *          [必选][在配置文件tagconf.xml中的selectID，或者 如果listID以”!”开始，则从内存中获得下拉信息；
 *          如果为"0",则执行selfSQL从数据库查询；
 *          如果为"#",则从selfSQL里定义的列表集合中读取，列表格式："ID1,显示值1;ID2,显示值2;..."] selfSQL =
 *          "SELECT ...." [可选][当listID为0时，记录的选择从该条SQL语句中选择,或者定义串中提取]
 *          [当需要从后台交易信息中进行二次条件提取时使用
 *          "SELECT=field1,field2,..;WHERE=fieldm=xxx,field=xxx"格式] script = ""
 *          [可选][执行的JavaScript语句如：onChange="verify(this)"] scope = "session"
 *          [可选][该taglib的作用范围，取值为session或者application][默认session] />
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TagRadio extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4411973194103644024L;

	final static String SELECT_LIST_DATAS = "taglib.RADIO_DATAS";

	final static String SELECT_IDSQL_MAPPING = "taglib.RADIO_IDSQL_MAPPING";

	/**
	 * 定义选择框属性
	 */
	private String focusID = null;

	private String radioName = "MyRadio";

	private String radioID = null;

	private String selfSQL = "";

	private String script = "";

	private String allFlag = null;

	private String nvlFlag = null;

	private boolean flag = false;

	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			// 取得设置
			BodyContent bc = getBodyContent();

			// 从tag的体中取得自定义的SQL
			if (radioID == null && bc != null && bc.getString() != null) {
				radioID = "0";
				selfSQL = bc.getString().trim();
				bc.clearBody();
			}

			/*******************************************************************
			 * 取得下拉菜单的数据
			 ******************************************************************/
			Vector vList = getListData(radioID, selfSQL);

			if (vList == null) {
				out.print("<!-- Radio ID=" + radioName + " NO DATA FOUND! -->");
				vList = new Vector();
			}

			Vector subV = null;
			for (int i = 0; i < vList.size(); i++) {
				subV = (Vector) vList.elementAt(i);
				String strSelected = "";

				// 设置全部可选项
				if (i == 0 && allFlag != null) {

					out.print("<input type='radio' id='" + radioName
							+ "' name='" + radioName + "' value='" + allFlag
							+ "'");
					if (allFlag.equals(focusID)) {
						out.print(" checked ");
					}
					if (script != null && !"".equals(script)) {
						out.print(" " + script + " ");
					}
					out.print(">全部");
					if (flag) {
						out.print("<br>\n");
					} else {
						out.print("\n");
					}

				} else if (i == 0 && nvlFlag != null) {

					out.print("<input type='radio' id='" + radioName
							+ "' name='" + radioName + "' value='" + nvlFlag
							+ "'");
					if (nvlFlag.equals(focusID)) {
						out.print(" checked ");
					}
					if (script != null && !"".equals(script)) {
						out.print(script);
					}
					out.print(">");
					if (flag) {
						out.print("<br>\n");
					} else {
						out.print("\n");
					}

				}

				for (int j = 0; j < subV.size(); j++) {
					//
					String str = (String) subV.elementAt(j);
					if (j == 0) { // option value
						out.print("<input type='radio' id='" + radioName
								+ "' name='" + radioName + "' value='" + str
								+ "' ");
						if (str.equals(focusID)) {
							strSelected = " checked ";
						}
						if (script != null && !"".equals(script))
							strSelected += script;
					}
					if (j == 1) { // option display
						if (flag) {
							out.print(" " + strSelected + " >"
									+ (String) subV.elementAt(1) + "<br>\n");
						} else {
							out.print(" " + strSelected + " >"
									+ (String) subV.elementAt(1) + "\n");
						}

					}
				}
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
			StringTokenizer stSimecolon = new StringTokenizer(values, ";");
			retV = new Vector();
			while (stSimecolon.hasMoreTokens()) {
				String ss = stSimecolon.nextToken();
				StringTokenizer stComma = new StringTokenizer(ss, ",");
				if (stComma.hasMoreTokens()) {
					Vector subV = new Vector();
					while (stComma.hasMoreTokens()) {
						String sss = stComma.nextToken();
						subV.add(sss);
					}
					retV.add(subV);
				}
			}
		} else
			retV = com.ailk.bi.common.dbtools.WebDBUtil.execQryVector(sql,
					"");

		return retV;
	}

	public void release() {
		this.radioID = null;
		this.focusID = null;
		// 生命周期 需要查查资料
		// pageContext.getSession().removeAttribute(SELECT_LIST_DATAS);
	}

	public String getAllFlag() {
		return allFlag;
	}

	public String getFocusID() {
		return focusID;
	}

	public String getRadioID() {
		return radioID;
	}

	public String getRadioName() {
		return radioName;
	}

	public boolean getFlag() {
		return flag;
	}

	public String getScript() {
		return script;
	}

	public String getSelfSQL() {
		return selfSQL;
	}

	public String getNvlFlag() {
		return nvlFlag;
	}

	public void setNvlFlag(String nvlFlag) {
		this.nvlFlag = nvlFlag;
	}

	public void setAllFlag(String allFlag) {
		this.allFlag = allFlag;
	}

	public void setSelfSQL(String selfSQL) {
		this.selfSQL = selfSQL;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setFocusID(String focusID) {
		this.focusID = focusID;
	}

	public void setRadioID(String listID) {
		this.radioID = listID;
	}

	public void setRadioName(String listName) {
		this.radioName = listName;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}