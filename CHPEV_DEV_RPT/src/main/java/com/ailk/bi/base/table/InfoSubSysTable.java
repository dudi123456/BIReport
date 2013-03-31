package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class InfoSubSysTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String sys_code = ""; // VARCHAR->String

	public String sys_name = ""; // VARCHAR->String

	public String sys_index = ""; // NUMBER->String

	public String domain = ""; // VARCHAR->String

	public String desc_info = ""; // VARCHAR->String

	public String isactive = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

}
