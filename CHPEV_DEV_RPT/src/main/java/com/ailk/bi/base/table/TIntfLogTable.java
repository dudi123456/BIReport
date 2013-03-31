package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class TIntfLogTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2287981384590677666L;

	// HERE IS FROM DATABASE
	public String gather_time = ""; // NUMBER->String

	public String intf_name = ""; // VARCHAR->String

	public String cycle_type = ""; // VARCHAR->String

	public String stage_grp = ""; // VARCHAR->String

	public String ods_grp = ""; // VARCHAR->String

	public String intf_lvl = ""; // NUMBER->String

	public String intf_src = ""; // VARCHAR->String

	public String intf_stat = ""; // NUMBER->String

	public String time_stamp = ""; // DATE->String

	public String op_name = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
