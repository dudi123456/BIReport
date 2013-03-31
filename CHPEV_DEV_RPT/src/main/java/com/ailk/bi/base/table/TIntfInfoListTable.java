package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class TIntfInfoListTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4690148438121943991L;

	// HERE IS FROM DATABASE
	public String intf_name = ""; // VARCHAR->String

	public String cycle_type = ""; // VARCHAR->String

	public String stage_grp = ""; // VARCHAR->String

	public String ods_grp = ""; // VARCHAR->String

	public String intf_lvl = ""; // NUMBER->String

	public String intf_src = ""; // VARCHAR->String

	public String src_tab_name = ""; // VARCHAR->String

	public String valid_tag = ""; // NUMBER->String

	public String memo = ""; // VARCHAR->String

	public String op_name = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
