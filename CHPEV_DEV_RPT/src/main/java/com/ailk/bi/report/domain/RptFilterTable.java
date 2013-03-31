package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class RptFilterTable extends JBTableBase {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String rpt_id = ""; // VARCHAR->String

	public String col_sequence = ""; // VARCHAR->String

	public String display_sequence = ""; // VARCHAR->String

	public String field_title = ""; // VARCHAR->String

	public String filter_type = ""; // VARCHAR->String

	public String filter_script = ""; // VARCHAR->String

	public String filter_datasource = ""; // VARCHAR->String

	public String filter_sql = ""; // VARCHAR->String

	public String filter_all = ""; // VARCHAR->String

	public String filter_default = ""; // VARCHAR->String

	public String status = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String dim_conditon = ""; // VARCHAR->String

	public String field_dim_code = ""; // VARCHAR->String

	public String field_code = ""; // VARCHAR->String

	public String data_type = ""; // VARCHAR->String

	public String con_tag = ""; // VARCHAR->String

	/**
	 * 生成查询定义对象
	 *
	 * @param svces
	 * @return
	 */
	public static RptFilterTable genReportFilterFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptFilterTable filterTable = new RptFilterTable();
		int m = 0;
		filterTable.rpt_id = svces[m++];
		filterTable.col_sequence = svces[m++];
		filterTable.display_sequence = svces[m++];
		filterTable.field_dim_code = svces[m++];
		filterTable.field_code = svces[m++];
		filterTable.field_title = svces[m++];
		filterTable.filter_type = svces[m++].trim();
		filterTable.filter_script = svces[m++];
		filterTable.filter_datasource = svces[m++].trim();
		filterTable.filter_sql = svces[m++];
		filterTable.filter_all = svces[m++].trim();
		filterTable.filter_default = svces[m++].trim();
		filterTable.status = svces[m++].trim();
		filterTable.data_type = svces[m++].trim();
		return filterTable;
	}

	/**
	 * 生成查询定义对象
	 *
	 * @param svces
	 * @return
	 */
	public static RptFilterTable genReportFilterDefineFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptFilterTable filterTable = new RptFilterTable();
		int m = 0;
		filterTable.rpt_id = svces[m++];
		filterTable.col_sequence = svces[m++];
		filterTable.display_sequence = svces[m++];
		filterTable.dim_conditon = svces[m++].trim();
		filterTable.field_dim_code = svces[m++];
		filterTable.field_code = svces[m++];
		filterTable.field_title = svces[m++];
		filterTable.filter_type = svces[m++].trim();
		filterTable.filter_script = svces[m++];
		filterTable.filter_datasource = svces[m++].trim();
		filterTable.filter_sql = svces[m++];
		filterTable.filter_all = svces[m++].trim();
		filterTable.filter_default = svces[m++].trim();
		filterTable.status = svces[m++].trim();
		filterTable.data_type = svces[m++];
		filterTable.con_tag = svces[m++];
		return filterTable;
	}
}
