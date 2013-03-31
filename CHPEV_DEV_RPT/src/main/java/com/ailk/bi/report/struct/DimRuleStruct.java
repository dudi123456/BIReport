package com.ailk.bi.report.struct;

import com.ailk.bi.common.event.JBTableBase;

/**
 * UI_RPT_RULE_MEASURE_DIM
 * 
 * @author Administrator
 * 
 */
public class DimRuleStruct extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getDim_field() {
		return dim_field;
	}

	public void setDim_field(String dim_field) {
		this.dim_field = dim_field;
	}

	public String getDim_src_sql() {
		return dim_src_sql;
	}

	public void setDim_src_sql(String dim_src_sql) {
		this.dim_src_sql = dim_src_sql;
	}

	public String getDim_rule() {
		return dim_rule;
	}

	public void setDim_rule(String dim_rule) {
		this.dim_rule = dim_rule;
	}

	public String getDim_name() {
		return dim_name;
	}

	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
	}

	public String getTop_head() {
		return top_head;
	}

	public void setTop_head(String top_head) {
		this.top_head = top_head;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	private String report_id;
	private String dim_field;
	private String dim_src_sql;
	private String dim_rule;
	private String dim_name;
	private String top_head;

	public DimRuleStruct() {

	}

	public DimRuleStruct(String report_id, String dim_field,
			String dim_src_sql, String dim_rule, String dim_name,
			String top_head) {
		this.report_id = report_id;
		this.dim_field = dim_field;
		this.dim_src_sql = dim_src_sql;
		this.dim_rule = dim_rule;
		this.dim_name = dim_name;
		this.top_head = top_head;
	}

}
