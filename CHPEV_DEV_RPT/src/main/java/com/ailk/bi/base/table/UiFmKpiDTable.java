package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiFmKpiDTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String day_id = ""; // NUMBER->String

	public String area_id = ""; // NUMBER->String

	public String svcknd_id = ""; // NUMBER->String

	public String kpi_id = ""; // NUMBER->String

	public String curval = ""; // NUMBER->String

	public String moncumval = ""; // NUMBER->String

	public String lastweekval = ""; // NUMBER->String

	public String recentweekavg = ""; // NUMBER->String

	public String lastmoncumval = ""; // NUMBER->String

	public String lastmonval = ""; // NUMBER->String

	public String increaseval = ""; // NUMBER->String

	public String increaserate = ""; // NUMBER->String

	public String chgdegree = ""; // NUMBER->String

	public String kpichar_id = ""; // NUMBER->String

	public String predictmonval = ""; // NUMBER->String

	public String brand_knd = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String kpi_desc = ""; // NUMBER->String

	public String kpiknd_desc = ""; // NUMBER->String

	public String kpiunit_desc = ""; // NUMBER->String

	public String precision = ""; // NUMBER->String

	// Add by renhui 首页使用
	public String measure_id = ""; // VARCHAR->String

	public String measure_desc = ""; // VARCHAR->String

	public String measure_sname = ""; // VARCHAR->String

	public String unit = ""; // VARCHAR->String

	public String per1 = ""; // String->String

	public String per2 = ""; // String->String

	public String per3 = ""; // String->String

	public String per4 = ""; // String->String

}
