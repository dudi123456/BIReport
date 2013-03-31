package com.ailk.bi.olap.domain;

import java.io.Serializable;

public class RptOlapTDStruct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7123404823098165472L;

	/**
	 * 合并行数
	 */
	private String rowspan = null;

	/**
	 * 合并列数
	 */
	private String colspan = null;

	/**
	 * 样式
	 */
	private String style = null;

	/**
	 * 对齐方式
	 */
	private String align = null;

	/**
	 * 其余部分
	 */
	private String rest = null;

	/**
	 * TD的内容
	 */
	private String content = null;

	/**
	 * 针对指标列的序号列表，如1,2,3, 或只有一个
	 */
	private String colIndex = null;

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getColIndex() {
		return colIndex;
	}

	public void setColIndex(String colIndex) {
		this.colIndex = colIndex;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
