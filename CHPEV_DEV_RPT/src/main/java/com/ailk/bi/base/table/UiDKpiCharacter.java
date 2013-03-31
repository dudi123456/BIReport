package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiDKpiCharacter extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE

	// HERE IS USER DEFINE 这一行不要删除！

	public String kpichar_id = ""; // NUMBER->String

	public String kpichar_name = ""; // NUMBER->String

	public String charupperlimit = ""; // NUMBER->String 比去年同期增长

	public String charlowerlimit = ""; // NUMBER->String 完成月计划任务

	public String kpichar_desc = ""; // NUMBER->String 完成年计划任务

}
