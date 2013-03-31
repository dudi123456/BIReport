package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class GeneralReportDefTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3208662770784452203L;

	// HERE IS FROM DATABASE
	public String general_ledger_id = ""; // NUMBER->String

	public String general_ledger_name = ""; // VARCHAR->String

	public String general_ledger_desc = ""; // VARCHAR->String

	public String general_ledger_code = ""; // VARCHAR->String

	public String general_ledger_type = ""; // NUMBER->String

	public String general_ledger_status = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
