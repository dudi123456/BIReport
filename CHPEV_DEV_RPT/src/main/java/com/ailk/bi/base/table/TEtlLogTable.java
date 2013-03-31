package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class TEtlLogTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8220337441504799090L;

	// HERE IS FROM DATABASE
	public String sess_id = ""; // NUMBER->String

	public String gather_time = ""; // NUMBER->String

	public String start_time = ""; // DATE->String

	public String end_time = ""; // DATE->String

	public String src_rows = ""; // NUMBER->String

	public String trg_succ_rows = ""; // NUMBER->String

	public String trg_fail_rows = ""; // NUMBER->String

	public String result = ""; // NUMBER->String

	public String result_info = ""; // VARCHAR->String

	public String redo_flag = ""; // NUMBER->String

	public String curr_flag = ""; // NUMBER->String

	public String chkpt_done_flag = ""; // NUMBER->String

	public String sess_version = ""; // VARCHAR->String

	public String terminal_name = ""; // VARCHAR->String

	public String ip_address = ""; // VARCHAR->String

	public String os_user = ""; // VARCHAR->String

	public String db_user = ""; // VARCHAR->String

	public String sess_layer = ""; // VARCHAR->String

	public String sess_knd = ""; // NUMBER->String

	public String sess_desc = ""; // VARCHAR->String

	public String src_tab_list = ""; // VARCHAR->String

	public String trg_tab_list = ""; // VARCHAR->String

	public String func_list = ""; // VARCHAR->String

	public String valid_flag = ""; // NUMBER->String

	public String sess_cycle_type = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String sess_name = ""; // 任务名称

	public String src_obj_id = ""; // 源对象id

	public String src_obj_name = ""; // 源对象名称

	public String trg_obj_id = ""; // 目标表id

	public String trg_obj_name = ""; // 目标表名称

}
