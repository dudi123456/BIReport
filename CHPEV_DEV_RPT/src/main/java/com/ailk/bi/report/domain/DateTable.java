package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class DateTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String d_table = ""; // VARCHAR->String

	public String gather_date = ""; // VARCHAR->String

	public String same_gather_date = ""; // VARCHAR->String

	public String last_gather_date = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！
}
