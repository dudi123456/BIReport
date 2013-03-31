package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class MainpageInfoChart extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String chart_id = ""; // NUMBER->String

	public String page_name = ""; // NUMBER->String

	public String chart_name = ""; // NUMBER->String

	public String chart_categroy = ""; // VARCHAR->String

	public String chart_categroy_index = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String user_id = ""; // NUMBER->String

	public String chart_type = ""; // NUMBER->String

	public String chart_index = ""; // NUMBER->String

	public String user_categroy = ""; // VARCHAR->String

	public String user_categroy_index = ""; // VARCHAR->String

	public String series_length = ""; // VARCHAR->String

	public String data_table = ""; // VARCHAR->String

	public String measures = ""; // VARCHAR->String

	public String date_code = ""; // VARCHAR->String

	public String measure_code = ""; // VARCHAR->String

}
