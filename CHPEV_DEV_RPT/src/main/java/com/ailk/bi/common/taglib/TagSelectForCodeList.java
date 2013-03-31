package com.ailk.bi.common.taglib;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.util.WebConstKeys;

public class TagSelectForCodeList extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 784533299231950860L;

	/**
	 * code_type的值
	 */
	private String codeType = null;

	/**
	 * 选中的值
	 */
	private String value = null;

	/**
	 * 标签ID
	 */
	private String htmlId = null;

	/**
	 * 标签名字
	 */
	private String htmlName = null;

	/**
	 * 标签属性
	 */
	private String htmlAttr = null;

	/**
	 * 是否禁用
	 */
	private boolean disabled = false;

	/**
	 * 是否有全部
	 */
	private boolean hasAll = false;

	/**
	 * 全部选项的值
	 */
	private String allOptionValue = null;

	/**
	 * 全部选项的描述
	 */
	private String allOptionDesc = null;

	public String getAllOptionDesc() {
		return allOptionDesc;
	}

	public void setAllOptionDesc(String allOptionDesc) {
		this.allOptionDesc = allOptionDesc;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		// 取得设置
		BodyContent bc = getBodyContent();
		if (bc != null && bc.getString() != null) {
			bc.clearBody();
		}
		// 进行判断，设置最基本属性
		validateAndSetDefault();
		// 由内存中获取数据
		Map values = getTypeCodeValues(pageContext);
		// 开始生成html
		StringBuilder sb = new StringBuilder();
		sb.append("<SELECT id=\"").append(htmlId).append("\" ");
		sb.append(" name=\"").append(htmlName).append("\" ");
		if (disabled) {
			sb.append(" disabled=\"disabled\" ");
		}
		sb.append(htmlAttr).append(" >\n");
		if (hasAll) {
			sb.append("<OPTION value=\"").append(allOptionValue).append("\" ");
			sb.append(
					allOptionValue.equalsIgnoreCase(value) ? " SELECTED "
							: "").append(">");
			sb.append(allOptionDesc).append("</OPTION>\n");
		}
		// 循环取值生成
		Iterator iter = values.entrySet().iterator();
		Map.Entry entry = null;
		while (iter.hasNext()) {
			entry = (Map.Entry) iter.next();
			sb.append("<OPTION value=\"").append(entry.getKey()).append("\" ");
			sb.append(
					entry.getKey().toString().equalsIgnoreCase(value) ? " SELECTED "
							: "").append(">");
			sb.append(entry.getValue()).append("</OPTION>\n");
		}
		sb.append("</SELECT>\n");
		try {
			out.println(sb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	private void validateAndSetDefault() {
		if (StringUtils.isBlank(htmlId)) {
			htmlId = "select_" + (new Date()).getTime();
		}
		if (StringUtils.isBlank(htmlName)) {
			htmlName = htmlId;
		}
		if (StringUtils.isBlank(htmlAttr)) {
			htmlAttr = "";
		}
		if (StringUtils.isBlank(allOptionValue)) {
			allOptionValue = "";
		}
		if (StringUtils.isBlank(allOptionDesc)) {
			allOptionDesc = "全部";
		}
	}

	@SuppressWarnings("rawtypes")
	private Map getTypeCodeValues(PageContext pageContext) {
		Map values = new LinkedHashMap();
		Object obj = pageContext.getServletContext().getAttribute(
				WebConstKeys.ATTR_C_CODE_LIST);
		if (null != obj) {
			HashMap allValues = (HashMap) obj;
			if (null != allValues.get(codeType)) {
				values = (Map) allValues.get(codeType);
			}
		}
		return values;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getHtmlId() {
		return htmlId;
	}

	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}

	public String getHtmlName() {
		return htmlName;
	}

	public void setHtmlName(String htmlName) {
		this.htmlName = htmlName;
	}

	public String getHtmlAttr() {
		return htmlAttr;
	}

	public void setHtmlAttr(String htmlAttr) {
		this.htmlAttr = htmlAttr;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isHasAll() {
		return hasAll;
	}

	public void setHasAll(boolean hasAll) {
		this.hasAll = hasAll;
	}

	public String getAllOptionValue() {
		return allOptionValue;
	}

	public void setAllOptionValue(String allOptionValue) {
		this.allOptionValue = allOptionValue;
	}
}
