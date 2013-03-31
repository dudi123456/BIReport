package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class UiCommonDimhierarchyTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -259278444940651560L;

	// HERE IS FROM DATABASE
	public String table_id = ""; // NUMBER->String

	public String col_id = ""; // NUMBER->String

	public String lev_id = ""; // NUMBER->String

	public String lev_name = ""; // VARCHAR->String

	public String lev_memo = ""; // VARCHAR->String

	public String src_idfld = ""; // VARCHAR->String

	public String idfld_type = ""; // NUMBER->String

	public String src_namefld = ""; // VARCHAR->String

	public String dimsrch_where = ""; // VARCHAR->String

	public String is_valid = ""; // CHAR->String

	public String rsv_fld1 = ""; // VARCHAR->String

	public String rsv_fld2 = ""; // VARCHAR->String

	public String rsv_fld3 = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
