package com.ailk.bi.common.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 动态可显示DIV区域定义
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
 * @author
 * @version 1.0 目前的使用格式为：
 * 
 *          <BIBM:TagDispDiv divID = "1" [必选][DIV的ID] disp = "true/false"
 *          [可选][设置一个区域是否可显示,如果为false则不显示] clear = "0/1"
 *          [可选][当满足隐藏条件时，是否输出DIV内容] script = ""
 *          [可选][执行的JavaScript语句如：onChange="verify(this)"] />
 */

public class TagDispDiv extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6605472418589432563L;

	/**
	 * 定义选择框属性
	 */
	private String divID = null;

	private String disp = "";

	private String clear = "";

	private String script = "";

	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			// 取得设置
			BodyContent bc = getBodyContent();
			String divContent = "";
			// 从tag的体中取得DIV内的内容
			if (bc != null && bc.getString() != null) {
				divContent = bc.getString();
				bc.clearBody();
			}
			String dispL = "";

			if ("false".equals(disp)) {
				dispL = " style=\"display:none\" ";
				if ("0".equals(clear)) {
					out.print("<DIV ID='" + divID + "' " + " " + dispL + " "
							+ script + " >\n");
					out.print(divContent);
					out.print("\n</DIV>\n");
				} else
					out.print("<!-- DIV ID='" + divID + "' WAS CLEARED-->");
			} else {
				out.print("<DIV ID='" + divID + "' " + " " + dispL + " "
						+ script + " >\n");
				out.print(divContent);
				out.print("\n</DIV>\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (SKIP_BODY);
	}

	public void release() {
	}

	public String getDivID() {
		return divID;
	}

	public String getClear() {
		return clear;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public void setDivID(String focusID) {
		this.divID = focusID;
	}

	public void setClear(String clear) {
		this.clear = clear;
	}

	public void setDisp(String disp) {
		this.disp = disp;
	}

	public String getDisp() {
		return disp;
	}

}