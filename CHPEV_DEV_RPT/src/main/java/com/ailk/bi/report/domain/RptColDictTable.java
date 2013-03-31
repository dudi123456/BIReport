package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class RptColDictTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String rpt_id = ""; // VARCHAR->String

	public String col_sequence = ""; // VARCHAR->String

	public String default_display = ""; // VARCHAR->String

	public String dim_code_display = ""; // VARCHAR->String

	public String is_expand_col = ""; // VARCHAR->String

	public String is_subsum = ""; // VARCHAR->String

	public String valuable_sum = ""; // VARCHAR->String

	public String field_dim_code = ""; // VARCHAR->String

	public String field_code = ""; // VARCHAR->String

	public String field_title = ""; // VARCHAR->String

	public String data_type = ""; // VARCHAR->String

	public String msu_length = ""; // VARCHAR->String

	public String msu_unit = ""; // VARCHAR->String

	public String comma_splitted = ""; // VARCHAR->String

	public String zero_proc = ""; // VARCHAR->String

	public String ratio_displayed = ""; // VARCHAR->String

	public String has_comratio = ""; // VARCHAR->String

	public String has_same = ""; // VARCHAR->String

	public String has_last = ""; // VARCHAR->String

	public String has_link = ""; // VARCHAR->String

	public String link_url = ""; // VARCHAR->String

	public String link_target = ""; // VARCHAR->String

	public String data_order = ""; // VARCHAR->String

	public String sms_flag = ""; // VARCHAR->String

	public String td_wrap = ""; // VARCHAR->String

	public String title_style = ""; // VARCHAR->String

	public String col_style = ""; // VARCHAR->String

	public String print_title_style = ""; // VARCHAR->String

	public String print_col_style = ""; // VARCHAR->String

	public String need_alert = ""; // VARCHAR->String

	public String compare_to = ""; // VARCHAR->String

	public String ratio_compare = ""; // VARCHAR->String

	public String high_value = ""; // VARCHAR->String

	public String low_value = ""; // VARCHAR->String

	public String alert_mode = ""; // VARCHAR->String

	public String rise_color = ""; // VARCHAR->String

	public String down_color = ""; // VARCHAR->String

	public String is_msu = ""; // VARCHAR->String

	public String is_user_msu = ""; // VARCHAR->String

	public String msu_id = ""; // VARCHAR->String

	public String datatable = ""; // VARCHAR->String

	public String status = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	/**
	 * 生成列定义对象
	 * 
	 * @param svces
	 * @return
	 */
	public static RptColDictTable genReportColFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptColDictTable dictTable = new RptColDictTable();
		int m = 0;
		dictTable.rpt_id = svces[m++];
		dictTable.col_sequence = svces[m++];
		dictTable.default_display = svces[m++].trim();
		dictTable.dim_code_display = svces[m++].trim();
		dictTable.is_expand_col = svces[m++].trim();
		dictTable.is_subsum = svces[m++].trim();
		dictTable.valuable_sum = svces[m++].trim();
		dictTable.field_dim_code = svces[m++];
		dictTable.field_code = svces[m++];
		dictTable.field_title = svces[m++];
		dictTable.data_type = svces[m++];
		dictTable.msu_length = svces[m++];
		dictTable.msu_unit = svces[m++];
		dictTable.comma_splitted = svces[m++].trim();
		dictTable.zero_proc = svces[m++];
		dictTable.ratio_displayed = svces[m++].trim();
		dictTable.has_comratio = svces[m++].trim();
		dictTable.has_same = svces[m++].trim();
		dictTable.has_last = svces[m++].trim();
		dictTable.has_link = svces[m++].trim();
		dictTable.link_url = svces[m++];
		dictTable.link_target = svces[m++];
		dictTable.data_order = svces[m++].trim();
		dictTable.sms_flag = svces[m++].trim();
		dictTable.td_wrap = svces[m++].trim();
		dictTable.title_style = svces[m++];
		dictTable.col_style = svces[m++];
		dictTable.print_title_style = svces[m++];
		dictTable.print_col_style = svces[m++];
		dictTable.need_alert = svces[m++].trim();
		dictTable.compare_to = svces[m++];
		dictTable.ratio_compare = svces[m++].trim();
		dictTable.high_value = svces[m++];
		dictTable.low_value = svces[m++];
		dictTable.alert_mode = svces[m++];
		dictTable.rise_color = svces[m++].trim();
		dictTable.down_color = svces[m++].trim();
		dictTable.is_msu = svces[m++].trim();
		dictTable.is_user_msu = svces[m++].trim();
		dictTable.msu_id = svces[m++];
		dictTable.datatable = svces[m++];
		dictTable.status = svces[m++].trim();
		return dictTable;
	}

}
