package com.ailk.bi.base.taglib;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.util.ConstantKeys;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.SortUtils;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * 自定义DIV列表标签
 * 
 * listFlag ="0" 缺省参数标志，表示从sqldef.xml文件提取SQL listFlag="1" 给定参数列表 注意分隔符 譬如：
 * param="1:jcm,2:chunming,3:金春明, listFlag="#" 自定义SQL语句 listFlag="!" 保留
 * 
 * script 属性是该标签文本框所带有的事件脚本
 * 
 * con 异构数据库连接
 * 
 * filter 多层次树的既定结尾列表
 * 
 * allFlag 全部值
 * 
 * nvlFlag 空值
 * 
 * @author chunming
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TagDiv extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5476061008886661241L;

	private String divId = null;// div标识

	private String listFlag = "0";// 参数标识

	private String textName = null;// 文本框名称

	private String hidName = null;// hidden名称

	private String textValue = null;// 文本框值

	private String hidValue = null;// hidden值

	private String param = null;// 参数类型

	private String script = null;// 参数类型

	private Connection con = null;// 数据库

	private String filter = null;// 过滤参数

	private String hidLvl = null;// 纬度层次

	private String hidLvlValue = null;// 层次值

	private String allFlag = null; // 全部

	private String nvlFlag = null; // 空值

	private String relateId = ""; // 关联纬度

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public String getAllFlag() {
		return allFlag;
	}

	public void setAllFlag(String allFlag) {
		this.allFlag = allFlag;
	}

	public String getNvlFlag() {
		return nvlFlag;
	}

	public void setNvlFlag(String nvlFlag) {
		this.nvlFlag = nvlFlag;
	}

	//
	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getHidLvl() {
		return hidLvl;
	}

	public void setHidLvl(String hidLvl) {
		this.hidLvl = hidLvl;
	}

	public String getHidLvlValue() {
		return hidLvlValue;
	}

	public void setHidLvlValue(String hidLvlValue) {
		this.hidLvlValue = hidLvlValue;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}

	public String getHidName() {
		return hidName;
	}

	public void setHidName(String hidName) {
		this.hidName = hidName;
	}

	public String getHidValue() {
		return hidValue;
	}

	public void setHidValue(String hidValue) {
		this.hidValue = hidValue;
	}

	public String getTextName() {
		return textName;
	}

	public void setTextName(String textName) {
		this.textName = textName;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public int doStartTag() throws JspException {
		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();

			String str = "";

			if (null != getFilter() && !"".equals(getFilter())) {// 存在层次树

				HashMap tail = new HashMap();
				String arr[] = getFilter().split(",");
				for (int i = 0; arr != null && i < arr.length; i++) {
					tail.put(arr[i], arr[i]);
				}
				KeyValueStruct[] struct = getObjInfoByParamLayer(getParam(),
						getCon(), tail, getListFlag());
				str = getDivLayerStr(struct, getDivId(), getTextName(),
						getTextValue(), getHidName(), getHidValue(),
						getScript(), getHidLvl(), getHidLvlValue(),
						getRelateId());
			} else {// 不存在层次树
				KeyValueStruct[] struct = this.getObjInfoByParam(getParam(),
						getCon(), getListFlag());
				str = getDivStr(struct, getDivId(), getTextName(),
						getTextValue(), getHidName(), getHidValue(),
						getScript(), getRelateId());
			}

			out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	/**
	 * 提取对象路径结构
	 * 
	 * @param obj_id
	 * @return KeyValueStruct 顺序数组
	 * @author jcm
	 */
	private KeyValueStruct[] getObjInfoByParam(String param, Connection con,
			String listflag) {
		KeyValueStruct struct[] = null;
		if (param == null || "".equals(param)) {
			return struct;
		}

		// System.out.println("listflag=================="+listflag);
		if ("1".equals(listflag)) {
			String arr[] = param.split(",");

			struct = new KeyValueStruct[arr.length];
			for (int i = 0; i < arr.length; i++) {
				// System.out.println("=================="+arr[i]);
				struct[i] = new KeyValueStruct();
				String temp[] = arr[i].split(":");
				struct[i].key = temp[0];
				struct[i].value = temp[1];
			}

		} else if ("0".equals(listflag)) {
			String sql = "";
			try {
				String arr[][] = null;

				sql = SQLGenator.genSQL(param);

				if (con == null) {
					arr = WebDBUtil.execQryArray(sql, "");
				} else {
					arr = WebDBUtil.execQryArray(con, sql, "");
				}

				if (arr != null && arr.length > 0) {
					struct = new KeyValueStruct[arr.length];
					for (int i = 0; i < arr.length; i++) {
						struct[i] = new KeyValueStruct();
						struct[i].key = arr[i][0];
						struct[i].value = arr[i][1];
					}
				}

			} catch (AppException e) {

				e.printStackTrace();
			}

		} else if ("#".equals(listflag)) {
			String sql = "";
			try {
				String arr[][] = null;

				sql = param;

				System.out.println("sql========" + sql);
				if (con == null) {
					arr = WebDBUtil.execQryArray(sql, "");
				} else {
					arr = WebDBUtil.execQryArray(con, sql, "");
				}

				if (arr != null && arr.length > 0) {
					struct = new KeyValueStruct[arr.length];
					for (int i = 0; i < arr.length; i++) {
						struct[i] = new KeyValueStruct();
						struct[i].key = arr[i][0];
						struct[i].value = arr[i][1];
					}
				}

			} catch (AppException e) {

				e.printStackTrace();
			}

		}

		return struct;

	}

	/**
	 * 层次结构查询
	 * 
	 * @param sqlcode
	 * @param param
	 * @return
	 */
	public static KeyValueStruct[] getObjInfoByParamLayer(String param,
			Connection con, HashMap map, String listflag) {

		KeyValueStruct struct[] = null;
		if (param == null || "".equals(param)) {
			return struct;
		}
		// System.out.println("listflag=================="+listflag);
		// 自定义
		if ("1".equals(listflag)) {
			String arr[] = param.split(";");
			struct = new KeyValueStruct[arr.length];
			for (int i = 0; i < arr.length; i++) {
				struct[i] = new KeyValueStruct();
				String temp[] = arr[i].split(",");
				struct[i].key = temp[0];
				struct[i].value = temp[1];
			}

		} else if ("0".equals(listflag)) {// 自定义SQL
			KeyValueStruct structTemp[] = null;
			String sql = "";
			try {
				String arr[][] = null;
				sql = SQLGenator.genSQL(param);

				if (con == null) {
					arr = WebDBUtil.execQryArray(sql, "");
				} else {
					arr = WebDBUtil.execQryArray(con, sql, "");
				}
				arr = WebDBUtil.execQryArray(sql, "");

				if (arr != null && arr.length > 0) {
					structTemp = new KeyValueStruct[arr.length];
					for (int i = 0; i < arr.length; i++) {
						structTemp[i] = new KeyValueStruct();
						structTemp[i].level = arr[i][0];
						structTemp[i].key = arr[i][1];
						structTemp[i].value = arr[i][2];
						structTemp[i].parent_key = arr[i][3];
						if (map.get(structTemp[i].key) != null) {
							structTemp[i].istail = true;
						}
					}

				}
				// 一层关系
				List list = new ArrayList();
				for (int i = 0; i < structTemp.length; i++) {
					if (structTemp[i].parent_key
							.equals(ConstantKeys.TAG_PARENT)) {
						list.add(structTemp[i]);
						KeyValueStruct structValue[] = getStruct(structTemp,
								structTemp[i].key);
						for (int j = 0; structValue != null
								&& j < structValue.length; j++) {
							list.add(structValue[j]);
						}
					}
				}
				//
				struct = (KeyValueStruct[]) list
						.toArray(new KeyValueStruct[list.size()]);

			} catch (AppException e) {

				e.printStackTrace();
			}
		} else if ("#".equals(listflag)) {
			String sql = "";
			try {
				String arr[][] = null;
				sql = param;

				if (con == null) {
					arr = WebDBUtil.execQryArray(sql, "");
				} else {
					arr = WebDBUtil.execQryArray(con, sql, "");
				}
				arr = WebDBUtil.execQryArray(sql, "");

				if (arr != null && arr.length > 0) {
					struct = new KeyValueStruct[arr.length];
					for (int i = 0; i < arr.length; i++) {
						struct[i] = new KeyValueStruct();
						struct[i].level = arr[i][0];
						struct[i].key = arr[i][1];
						struct[i].value = arr[i][2];
						struct[i].parent_key = arr[i][3];
						if (map.get(struct[i].key) != null) {
							struct[i].istail = true;
						}
					}
				}

			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		return struct;

	}

	/**
	 * 静态DIV
	 * 
	 * @param struct
	 * @param divId
	 * @param textname
	 * @param textvalue
	 * @param hidname
	 * @param hidvalue
	 * @param script
	 * @return
	 */
	private String getDivStr(KeyValueStruct struct[], String divId,
			String textname, String textvalue, String hidname, String hidvalue,
			String script, String relateId) {
		StringBuffer buff = new StringBuffer("");
		buff.append(" <input type=\"text\"  class=\"normalField2\" id =\""
				+ textname + "\"");
		buff.append(" name=\"" + textname + "\"");
		buff.append(" onclick=\"showDiv('" + divId + "',this,'" + relateId
				+ "')\" onMouseOut=\"hideDiv('" + divId + "')\"");

		buff.append(" value=\"" + textvalue + "\"> \n");
		buff.append(" <input type=\"hidden\" id =\"" + hidname + "\" name =\""
				+ hidname + "\" value=\"" + hidvalue + "\">\n");
		buff.append(" <div id=\""
				+ divId
				+ "\" class=\"DIVKING\" onMouseOver=\"showself(this)\" onMouseOut=\"hideself(this)\" >\n");
		buff.append(" <table width=\"100%\"  border=\"0\" align=\"center\" cellpadding=\"4\" cellspacing=\"0\" class=\"TableInDiv\"> \n");

		if (struct == null || struct.length <= 0) {
			buff.append("<tr> \n");
			buff.append("<td class=\"itemTd0\" nowrap>\n");
			buff.append("<a href=\"javascript:;\" URL='' id='none'  onclick=\"nodeClick('"
					+ textname + "','" + hidname + "',this,'" + divId
					+ "')\" >");
			buff.append("当前没有分析项！");
			buff.append("</a> \n");
			buff.append("</td>");
			buff.append("</tr>\n");
		} else {
			// 全部
			if (allFlag != null) {
				buff.append("<tr>");
				buff.append("<td class=\"itemTd0\" nowrap>");
				buff.append("<a href=\"javascript:;\" URL='" + allFlag + ":全部"
						+ "' id='allFlag'  onclick=\"nodeClick('" + textname
						+ "','" + hidname + "',this,'" + divId + "');");
				if (script != null && !"".equals(script)) {
					buff.append(script);
				}
				buff.append("\" >");
				buff.append("全部");
				buff.append("</a>");
				buff.append("</td>");
				buff.append("</tr>\n");

			}
			// 空值
			if (nvlFlag != null) {
				buff.append("<tr>");
				buff.append("<td class=\"itemTd0\" nowrap>");
				buff.append("<a href=\"javascript:;\" URL='" + nvlFlag + ":"
						+ "' id='nvlFlag'  onclick=\"nodeClick('" + textname
						+ "','" + hidname + "',this,'" + divId + "');");
				if (script != null && !"".equals(script)) {
					buff.append(script);
				}
				buff.append("\" >");
				buff.append("");
				buff.append("</a>");
				buff.append("</td>");
				buff.append("</tr>\n");
			}

			for (int i = 0; i < struct.length; i++) {
				buff.append("<tr>");
				buff.append("<td class=\"itemTd0\" nowrap>");
				buff.append("<a href=\"javascript:;\" URL='" + struct[i].key + ":"
						+ struct[i].value + "' id='" + struct[i].key
						+ "'  onclick=\"nodeClick('" + textname + "','"
						+ hidname + "',this,'" + divId + "');");
				if (script != null && !"".equals(script)) {
					buff.append(script);
				}
				buff.append("\" >");
				buff.append(struct[i].value);
				buff.append("</a>");
				buff.append("</td>");
				buff.append("</tr>\n");
			}
		}

		buff.append("</table>");
		buff.append("</div>");

		return buff.toString();

	}

	/**
	 * 层次DIV
	 * 
	 * @param struct
	 * @param divId
	 * @param textname
	 * @param textvalue
	 * @param hidname
	 * @param hidvalue
	 * @param script
	 * @return
	 */
	private String getDivLayerStr(KeyValueStruct struct[], String divId,
			String textname, String textvalue, String hidname, String hidvalue,
			String script, String hidlvl, String hidlvlvalue, String relateId) {
		StringBuffer buff = new StringBuffer("");
		// 名称
		buff.append(" <input type=\"text\"  class=\"normalField2\" id =\""
				+ textname + "\"");
		buff.append(" name=\"" + textname + "\"");
		buff.append(" onclick=\"showDiv('" + divId + "',this,'" + relateId
				+ "')\" onMouseOut=\"hideDiv('" + divId + "')\"");

		buff.append(" value=\"" + textvalue + "\"> \n");
		// 编码
		buff.append(" <input type=\"hidden\" id =\"" + hidname + "\" name =\""
				+ hidname + "\" value=\"" + hidvalue + "\">\n");
		// 层次
		buff.append(" <input type=\"hidden\" id =\"" + hidlvl + "\" name =\""
				+ hidlvl + "\" value=\"" + hidlvlvalue + "\">\n");
		// DIV
		buff.append(" <div id=\""
				+ divId
				+ "\" class=\"DIVKING\" onMouseOver=\"showself(this)\" onMouseOut=\"hideself(this)\" >\n");
		// table
		buff.append(" <table width=\"100%\"  border=\"0\" align=\"center\" cellpadding=\"4\" cellspacing=\"0\" class=\"TableInDiv\"> \n");

		if (struct == null || struct.length <= 0) {
			buff.append("<tr> \n");
			buff.append("<td class=\"itemTd0\" nowrap>\n");
			buff.append("<a href=\"javascript:;\" URL='' id='none'  onclick=\"nodeClick('"
					+ textname + "','" + hidname + "',this,'" + divId
					+ "')\" >");
			buff.append("当前没有分析项！");
			buff.append("</a> \n");
			buff.append("</td>");
			buff.append("</tr>\n");
		} else {

			// 全部
			if (allFlag != null) {
				buff.append("<tr>");
				buff.append("<td class=\"itemTd0\" nowrap>");
				buff.append("<a href=\"javascript:;\" URL='" + allFlag + ":全部"
						+ "' id='allFlag'  onclick=\"nodeClick('" + textname
						+ "','" + hidname + "',this,'" + divId + "');");
				if (script != null && !"".equals(script)) {
					buff.append(script);
				}
				buff.append("\" >");
				buff.append("全部");
				buff.append("</a>");
				buff.append("</td>");
				buff.append("</tr>\n");

			}
			// 空值
			if (nvlFlag != null) {
				buff.append("<tr>");
				buff.append("<td class=\"itemTd0\" nowrap>");
				buff.append("<a href=\"javascript:;\" URL='" + nvlFlag + ":"
						+ "' id='nvlFlag'  onclick=\"nodeClick('" + textname
						+ "','" + hidname + "',this,'" + divId + "');");
				if (script != null && !"".equals(script)) {
					buff.append(script);
				}
				buff.append("\" >");
				buff.append("");
				buff.append("</a>");
				buff.append("</td>");
				buff.append("</tr>\n");
			}
			for (int i = 0; i < struct.length; i++) {
				buff.append("<tr>");
				buff.append("<td class=\"itemTd0\" nowrap>");
				buff.append("<a href=\"javascript:;\" URL='" + struct[i].key + ":"
						+ struct[i].value + ":" + struct[i].level + "' id='"
						+ struct[i].key + "'  onclick=\"nodeLayerClick('"
						+ textname + "','" + hidname + "',this,'" + divId
						+ "','" + hidlvl + "');");
				if (script != null && !"".equals(script)) {
					buff.append(script);
				}
				buff.append("\" >");
				if (struct[i].parent_key.equals("0")) {
					buff.append(" <span class=\"style13\">- </span>");
				} else {
					if (false == struct[i].istail) {
						buff.append(" &nbsp;├");
					} else {
						buff.append(" &nbsp;└");
					}

				}
				buff.append(struct[i].value);
				buff.append("</a>");
				buff.append("</td>");
				buff.append("</tr>\n");
			}
		}

		buff.append("</table>");
		buff.append("</div>");

		return buff.toString();

	}

	public String getListFlag() {
		return listFlag;
	}

	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}

	/**
	 * 
	 * @param struct
	 * @param key
	 * @return
	 */
	public static KeyValueStruct[] getStruct(KeyValueStruct[] struct, String key) {
		KeyValueStruct[] restruct = null;

		if (struct == null || key == null) {
			return restruct;
		}
		int count = 0;
		for (int i = 0; i < struct.length; i++) {
			if (struct[i].parent_key.equals(key)) {
				count++;
			}
		}
		restruct = new KeyValueStruct[count];
		for (int i = 0, j = 0; i < struct.length; i++) {
			if (struct[i].parent_key.equals(key)) {
				restruct[j] = struct[i];
				j++;
			}
		}
		SortUtils.sortByStr(restruct, "key");
		return restruct;

	}

}
