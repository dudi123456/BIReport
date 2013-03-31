package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiSubjectRankMeasureDefTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6689418505191115930L;

	// HERE IS FROM DATABASE
	public String measure_id = ""; // VARCHAR->String

	public String measure_name = ""; // VARCHAR->String

	public String measure_sequence = ""; // VARCHAR->String

	public String dim_field = ""; // VARCHAR->String

	public String dim_desc_field = ""; // VARCHAR->String

	public String value_field = ""; // VARCHAR->String

	public String sum_filed = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String struct_dim_filed = ""; // VARCHAR->String

	public String struct_name_filed = ""; // VARCHAR->String

	public String struct_value_filed = ""; // VARCHAR->String

}
