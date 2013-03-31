package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiCommonChartDefTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8318137281892896223L;

	// HERE IS FROM DATABASE
	public String chart_id = ""; // NUMBER->String

	public String table_id = ""; // NUMBER->String

	public String chart_belong = ""; // NUMBER->String

	public String chart_type = ""; // NUMBER->String

	public String chart_attribute = ""; // VARCHAR->String

	public String chart_sql = ""; // VARCHAR->String

	public String sql_where = ""; // VARCHAR->String

	public String sql_order = ""; // VARCHAR->String

	public String category_index = ""; // NUMBER->String

	public String series_index = ""; // NUMBER->String

	public String series_length = ""; // NUMBER->String

	public String value_index = ""; // NUMBER->String

	public String isusecd = ""; // NUMBER->String

	public String category_desc = ""; // VARCHAR->String

	public String category_desc_index = ""; // VARCHAR->String

	public String chart_index = ""; // NUMBER->String

	public String chart_drill = ""; // NUMBER->String

	public String chart_drill_type = ""; // NUMBER->String

	public String chart_valid = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

}
