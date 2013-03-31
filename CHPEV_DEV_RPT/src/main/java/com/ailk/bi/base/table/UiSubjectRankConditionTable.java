package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiSubjectRankConditionTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2754215936233532120L;

	// HERE IS FROM DATABASE
	public String id = ""; // NUMBER->String

	public String rank_code = ""; // VARCHAR->String

	public String qry_field = ""; // VARCHAR->String

	public String table_field = ""; // VARCHAR->String

	public String con_tag = ""; // VARCHAR->String

	public String con_type = ""; // VARCHAR->String

	public String con_sequence = ""; // NUMBER->String

	public String status = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
