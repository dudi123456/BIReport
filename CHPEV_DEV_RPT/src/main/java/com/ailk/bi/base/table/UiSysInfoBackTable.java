package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiSysInfoBackTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4537398458107996139L;

	// HERE IS FROM DATABASE
	public String back_id = ""; // NUMBER->String

	public String obj_id = ""; // VARCHAR->String

	public String obj_name = ""; // VARCHAR->String

	public String back_oper_no = ""; // VARCHAR->String

	public String back_oper_name = ""; // VARCHAR->String

	public String back_date = ""; // DATE->String

	public String back_info = ""; // VARCHAR->String

	public String back_status = ""; // CHAR->String

	public String back_icon = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String back_date_other = ""; // DATE->String

}
