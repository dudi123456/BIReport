package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiCommonTableDefTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6194290745540951705L;

	// HERE IS FROM DATABASE
	public String table_id = ""; // NUMBER->String

	public String obj_type_id = ""; // NUMBER->String

	public String table_desc = ""; // VARCHAR->String

	public String data_table = ""; // VARCHAR->String

	public String sql_where = ""; // VARCHAR->String

	public String sql_order = ""; // VARCHAR->String

	public String isexpand = ""; // CHAR->String

	public String time_field = ""; // VARCHAR->String

	public String field_type = ""; // NUMBER->String

	public String time_level = ""; // NUMBER->String

	public String cal_ratio = ""; // CHAR->String

	public String is_row_click_chart_change = ""; // CHAR->String

	public String rlt_chart_id = ""; // VARCHAR->String

	public String has_head = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String is_rowclk = "";

	public String chart_ids = "";

	public UiCommonRtpheadTable tabHead = null;

	public String hasSubpage = "";

	public String subpage_id = "";

	public String subpage_url = "";
}
