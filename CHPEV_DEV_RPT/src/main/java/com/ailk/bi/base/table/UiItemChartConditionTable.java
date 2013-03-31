package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiItemChartConditionTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4628396631169777237L;

	// HERE IS FROM DATABASE
	public String chart_id = ""; // NUMBER->String

	public String table_field_code = ""; // VARCHAR->String

	public String qry_field_code = ""; // VARCHAR->String

	public String con_code_type = ""; // NUMBER->String

	public String con_tag = ""; // VARCHAR->String

	public String sequence = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

}
