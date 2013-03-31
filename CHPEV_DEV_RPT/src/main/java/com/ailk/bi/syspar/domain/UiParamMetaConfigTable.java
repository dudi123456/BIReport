package com.ailk.bi.syspar.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiParamMetaConfigTable extends JBTableBase {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String param_id = ""; // VARCHAR->String

	private String column_cn_name = ""; // VARCHAR->String

	private String column_en_name = ""; // VARCHAR->String

	private String column_data_type = ""; // VARCHAR->String

	private String unique_index = ""; // CHAR->String

	private String column_show_type = ""; // NUMBER->String

	private String column_show_rule = ""; // VARCHAR->String

	private String sequence = ""; // NUMBER->String

	private String column_format = ""; // VARCHAR->String

	private String column_data_digit = ""; // VARCHAR->String

	private String column_length = "";  //字段定义长度

	public String getColumn_length() {
		return column_length;
	}

	public void setColumn_length(String column_length) {
		this.column_length = column_length;
	}

	public String getColumn_data_digit() {
		return column_data_digit;
	}

	public void setColumn_data_digit(String column_data_digit) {
		this.column_data_digit = column_data_digit;
	}

	public String getColumn_event() {
		return column_event;
	}

	public void setColumn_event(String column_event) {
		this.column_event = column_event;
	}

	private String status = ""; // CHAR->String

	private String column_event = ""; // VARCHAR->String

	public String getColumn_cn_name() {
		return column_cn_name;
	}

	public void setColumn_cn_name(String column_cn_name) {
		this.column_cn_name = column_cn_name;
	}

	public String getColumn_data_type() {
		return column_data_type;
	}

	public void setColumn_data_type(String column_data_type) {
		this.column_data_type = column_data_type;
	}

	public String getColumn_en_name() {
		return column_en_name;
	}

	public void setColumn_en_name(String column_en_name) {
		this.column_en_name = column_en_name;
	}

	public String getColumn_format() {
		return column_format;
	}

	public void setColumn_format(String column_format) {
		this.column_format = column_format;
	}

	public String getColumn_show_rule() {
		return column_show_rule;
	}

	public void setColumn_show_rule(String column_show_rule) {
		this.column_show_rule = column_show_rule;
	}

	public String getColumn_show_type() {
		return column_show_type;
	}

	public void setColumn_show_type(String column_show_type) {
		this.column_show_type = column_show_type;
	}

	public String getParam_id() {
		return param_id;
	}

	public void setParam_id(String param_id) {
		this.param_id = param_id;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUnique_index() {
		return unique_index;
	}

	public void setUnique_index(String unique_index) {
		this.unique_index = unique_index;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
