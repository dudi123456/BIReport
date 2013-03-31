package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class InfoPageItemTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String item_code = ""; // VARCHAR->String

	public String menu_code = ""; // VARCHAR->String

	public String item_title = ""; // VARCHAR->String

	public String item_type = ""; // VARCHAR->String

	public String form_name = ""; // VARCHAR->String

	public String item_name = ""; // VARCHAR->String

	public String item_value = ""; // VARCHAR->String

	public String command = ""; // VARCHAR->String

	public String isactive = ""; // NUMBER->String

	public String seq_no = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！
	public String menu_title = "";

	public String role_code = "";

	public String active_desc = "";

}
