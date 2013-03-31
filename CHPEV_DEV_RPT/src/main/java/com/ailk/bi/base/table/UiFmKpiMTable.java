package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiFmKpiMTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String month_id = ""; // NUMBER->String

	public String area_id = ""; // NUMBER->String

	public String svcknd_id = ""; // NUMBER->String

	public String kpi_id = ""; // NUMBER->String

	public String curval = ""; // NUMBER->String

	public String curvalplace = ""; // NUMBER->String

	public String yearcumval = ""; // NUMBER->String

	public String lastval = ""; // NUMBER->String

	public String lastvalplace = ""; // NUMBER->String

	public String recentavg = ""; // NUMBER->String

	public String lastyearval = ""; // NUMBER->String

	public String increaseval = ""; // NUMBER->String

	public String incrvalplace = ""; // NUMBER->String

	public String lastincrvalplace = ""; // NUMBER->String

	public String increaserate = ""; // NUMBER->String

	public String incrrateplace = ""; // NUMBER->String

	public String lastincrrateplace = ""; // NUMBER->String

	public String chgdegree = ""; // NUMBER->String

	public String kpichar_id = ""; // NUMBER->String

	public String lastyearcumval = ""; // NUMBER->String

	public String yearplanval = ""; // NUMBER->String

	public String curplanval = ""; // NUMBER->String

	public String predictyearval = ""; // NUMBER->String

	public String datestamp = ""; // NUMBER->String

	public String monthplanval = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String kpi_desc = ""; // NUMBER->String

	public String kpiknd_desc = ""; // NUMBER->String

	public String yearrise = ""; // NUMBER->String 比去年同期增长

	public String monthplan = ""; // NUMBER->String 完成月计划任务

	public String yearplan = ""; // NUMBER->String 完成年计划任务

	public String lastrise = ""; // NUMBER->String 比去年同期累计增长

	public String kpiunit_desc = ""; // NUMBER->String

	public String precision = ""; // NUMBER->String

	public String unit = ""; // NUMBER->String

}
