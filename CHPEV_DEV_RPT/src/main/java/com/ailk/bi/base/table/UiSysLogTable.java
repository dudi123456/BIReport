package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiSysLogTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4776629261420961909L;

	// HERE IS FROM DATABASE
	public String service_sn = ""; // NUMBER->String

	public String log_seq = ""; // VARCHAR->String

	public String log_type = ""; // CHAR->String

	public String log_oper_no = ""; // VARCHAR->String

	public String log_oper_time = ""; // DATE->String

	public String log_ip = ""; // VARCHAR->String

	public String log_oper_name = ""; // VARCHAR->String

	public String obj_id = ""; // VARCHAR->String

	public String obj_name = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String login_num = ""; // VARCHAR->String

	public String dept_no = ""; // VARCHAR->String

	public String dept_name = ""; // VARCHAR->String

	public String region_id = ""; // VARCHAR->String

	public String region_name = ""; // VARCHAR->String

	public String log_index = ""; // VARCHAR->String

	public String log_num = ""; // VARCHAR->String

}
