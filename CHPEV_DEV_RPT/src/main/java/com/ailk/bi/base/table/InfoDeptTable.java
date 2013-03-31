package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class InfoDeptTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String dept_no = ""; // VARCHAR->String

	public String parent_dept_no = ""; // VARCHAR->String

	// HERE IS FROM DATABASE
	public String dept_id = ""; // VARCHAR->String

	public String parent_id = ""; // VARCHAR->String

	public String path_code = ""; // VARCHAR->String

	public String region_id = ""; // NUMBER->String

	public String dept_type = ""; // VARCHAR->String

	public String dept_name = ""; // VARCHAR->String

	public String local_net = ""; // VARCHAR->String

	public String comments = ""; // VARCHAR->String

	public String phone = ""; // VARCHAR->String

	public String fax = ""; // VARCHAR->String

	public String address = ""; // VARCHAR->String

	public String flag = ""; // VARCHAR->String

	public String res_init_a = ""; // NUMBER->String

	public String res_init_b = ""; // NUMBER->String

	public String res_char_a = ""; // VARCHAR->String

	public String res_char_b = ""; // VARCHAR->String

	public String area_id = "";

	public String area_name = "";

	// HERE IS USER DEFINE 这一行不要删除！
	public String parent_dept_id = "";

	public String parent_dept_name = ""; // VARCHAR->String
	public String region_name = ""; // VARCHAR->String
}
