package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class TEtlSessInfoListTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1987830380878735125L;

	// HERE IS FROM DATABASE
	public String sess_id = ""; // NUMBER->String

	public String sess_layer = ""; // VARCHAR->String

	public String sess_knd = ""; // NUMBER->String

	public String sess_name = ""; // VARCHAR->String

	public String sess_desc = ""; // VARCHAR->String

	public String src_tab_list = ""; // VARCHAR->String

	public String trg_tab_list = ""; // VARCHAR->String

	public String func_list = ""; // VARCHAR->String

	public String valid_flag = ""; // NUMBER->String

	public String sess_cycle_type = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
