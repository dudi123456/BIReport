package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class InfoRegionTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String region_id = ""; // NUMBER->String

	public String parent_id = ""; // NUMBER->String

	public String parent_name = ""; // NUMBER->String

	public String region_code = ""; // VARCHAR->String

	public String region_name = ""; // VARCHAR->String

	public String path_code = ""; // VARCHAR->String

	public String region_type = ""; // NUMBER->String

	public String isactive = ""; // NUMBER->String

	public String comments = ""; // NUMBER->String

	public String status = "";

	// HERE IS USER DEFINE 这一行不要删除！

}
