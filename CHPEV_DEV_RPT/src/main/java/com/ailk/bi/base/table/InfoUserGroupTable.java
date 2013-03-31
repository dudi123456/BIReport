package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class InfoUserGroupTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String group_id = ""; // VARCHAR->String

	public String group_name = ""; // NUMBER->String

	public String parent_id = ""; // VARCHAR->String

	public String status = ""; // VARCHAR->String

	public String region_id = ""; // VARCHAR->String

	public String dept_id = ""; // VARCHAR->String

	public String sequence = ""; // NUMBER->String

	public String role_id = "";

	public String group_type = "";

	// HERE IS USER DEFINE 这一行不要删除！

}
