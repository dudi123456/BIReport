package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiKpiInfoOptTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1432685824516227686L;

	// HERE IS FROM DATABASE
	public String id = ""; // NUMBER->String

	public String rpt_id = ""; // NUMBER->String

	public String parent_id = ""; // NUMBER->String

	public String kpi_id = ""; // VARCHAR->String

	public String sequence = ""; // NUMBER->String

	public String opt_kpi_desc = ""; // VARCHAR->String

	public String status = ""; // NUMBER->String

	public String res_char = ""; // VARCHAR->String

	public String res_int = ""; // NUMBER->String

	public String colum_name = ""; // VARCHAR->String

	public String colum_desc = ""; // VARCHAR->String

	public String chart_type = ""; // NUMBER->String

	public String unit = ""; // VARCHAR->String

	public String measure_type = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

}
