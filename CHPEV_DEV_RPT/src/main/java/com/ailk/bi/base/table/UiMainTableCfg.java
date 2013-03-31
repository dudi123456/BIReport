package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiMainTableCfg extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String table_id = ""; // VARCHAR->String

	public String item_id = ""; // NUMBER->String

	public String axis_type = ""; // NUMBER->String

	public String field_value = ""; // NUMBER->String

	public String field_code = ""; // VARCHAR->String

	public String field_desc = ""; // VARCHAR->String

	public String digit_len = ""; // NUMBER->String

	public String head_style = ""; // VARCHAR->String

	public String status = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String sequence = ""; // NUMBER->String

}
