package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class InfoSysModuleTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String module_code = ""; // VARCHAR->String

	public String sys_code = ""; // VARCHAR->String

	public String module_name = ""; // VARCHAR->String

	public String module_index = ""; // NUMBER->String

	public String isactive = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String sys_name = ""; // VARCHAR->String

}
