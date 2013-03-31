package com.ailk.bi.report.struct;

import com.ailk.bi.common.event.JBTableBase;

public class HtmlPosStruct extends JBTableBase {

	/**
	 * 定位
	 */
	private static final long serialVersionUID = 1L;

	public int pos = 0;// 行定位

	public int colspan = 0;// 记录行colspan

	public int rowspan = 0;// 记录行rowspan
}
