package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class PubInfoResourceTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String res_id = ""; // VARCHAR->String

	public String parent_id = ""; // VARCHAR->String

	public String parent_name = ""; // VARCHAR->String

	public String isresource = ""; // VARCHAR->String

	public String ishot = ""; // VARCHAR->String

	public String hot_type = ""; // VARCHAR->String

	public String local_res_code = ""; // VARCHAR->String

	public String belong = ""; // VARCHAR->String

	public String type = ""; // VARCHAR->String

	public String code = ""; // VARCHAR->String

	public String name = ""; // VARCHAR->String

	public String cycle = ""; // NUMBER->String

	public String cycle_desc = ""; // NUMBER->String

	public String url = ""; // NUMBER->String

	public String title_note = ""; // NUMBER->String

	public String filldept = ""; // VARCHAR->String

	public String fillperson = ""; // VARCHAR->String

	public String makedept = ""; // VARCHAR->String

	public String makeperson = ""; // VARCHAR->String

	public String needdept = ""; // VARCHAR->String

	public String needperson = ""; // VARCHAR->String

	public String needreason = ""; // VARCHAR->String

	public String inputnote = ""; // VARCHAR->String

	public String divcity_flag = ""; // NUMBER->String

	public String row_flag = ""; // NUMBER->String

	public String ishead = ""; // NUMBER->String

	public String isleft = ""; // NUMBER->String

	public String startcol = ""; // NUMBER->String

	public String processtype = ""; // NUMBER->String

	public String processflag = ""; // NUMBER->String

	public String dispenseflag = ""; // NUMBER->String

	public String d_user_id = ""; // NUMBER->String

	public String start_date = ""; // NUMBER->String

	public String data_table = ""; // VARCHAR->String

	public String data_where = ""; // VARCHAR->String

	public String rpt_id_where = ""; // VARCHAR->String

	public String filter_zerorow = ""; // VARCHAR->String

	public String data_order = ""; // VARCHAR->String

	public String sequence = ""; // NUMBER->String

	public String status = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！
	public String rpt_id = ""; // VARCHAR->String

	public String isreport = ""; // VARCHAR->String

	// 如果有表头则设置此对象
	public UiRptInfoHeadTable head = null;
}
