package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiSysInfoRebackTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5956742503551001210L;

	// HERE IS FROM DATABASE
	public String reback_id = ""; // NUMBER->String

	public String back_id = ""; // NUMBER->String

	public String obj_id = ""; // VARCHAR->String

	public String obj_name = ""; // VARCHAR->String

	public String reback_oper_no = ""; // VARCHAR->String

	public String reback_oper_name = ""; // VARCHAR->String

	public String reback_info = ""; // VARCHAR->String

	public String reback_date = ""; // DATE->String

	public String reback_status = ""; // CHAR->String

	public String reback_icon = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String reback_date_other = ""; // DATE->String
}
