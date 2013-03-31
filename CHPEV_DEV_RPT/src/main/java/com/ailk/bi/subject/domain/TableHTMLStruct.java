package com.ailk.bi.subject.domain;

import com.ailk.bi.common.event.JBTableBase;

/**
 * @author xdou 表格的HTML结构 包括显示和到处结构
 */
public class TableHTMLStruct extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 显示的字符串数组
	 */
	public String[] html = null;

	/**
	 * 导出的字符串数组
	 */
	public String[] export = null;
}
