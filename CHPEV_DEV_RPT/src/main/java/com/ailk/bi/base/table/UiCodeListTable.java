package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiCodeListTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String type_code = ""; // VARCHAR->String

	public String type_name = ""; // VARCHAR->String

	public String rule_type = ""; // NUMBER->String

	public String code_id = ""; // VARCHAR->String

	public String code_name = ""; // VARCHAR->String

	public String code_filed = ""; // VARCHAR->String

	public String code_datasource = ""; // VARCHAR->String

	public String code_condition = ""; // VARCHAR->String

	public String code_seq = ""; // NUMBER->String

	public String status = ""; // CHAR->String

	public String code_order = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
