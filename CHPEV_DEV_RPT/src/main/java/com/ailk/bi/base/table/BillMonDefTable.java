package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class BillMonDefTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2546602329368525598L;

	// HERE IS FROM DATABASE
	public String bill_id = ""; // NUMBER->String

	public String bill_cycle = ""; // VARCHAR->String

	public String eff_date = ""; // DATE->String

	public String exp_date = ""; // DATE->String

	public String bill_name = ""; // VARCHAR->String

	public String sta_time = ""; // DATE->String

	public String end_time = ""; // DATE->String

	// HERE IS USER DEFINE 这一行不要删除！

}
