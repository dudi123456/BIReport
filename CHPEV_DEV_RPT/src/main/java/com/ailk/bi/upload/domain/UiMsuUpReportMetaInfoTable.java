package com.ailk.bi.upload.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiMsuUpReportMetaInfoTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String report_id = ""; // VARCHAR->String

	private String record_code = ""; // VARCHAR->String

	private String col_code = ""; // VARCHAR->String

	private String new_title = ""; // VARCHAR->String

	private String ref_map_type = ""; // VARCHAR->String

	private String ref_field = ""; // VARCHAR->String

	private String ref_con = ""; // VARCHAR->String

	private String col_sequence = ""; // NUMBER->String

	private String title_desc = ""; // VARCHAR->String

	private String unit = ""; // VARCHAR->String

	private String digit = ""; // NUMBER->String

	private String msu_id = ""; // VARCHAR->String

	private String flag = ""; // CHAR->String

	private String filed_type = ""; // CHAR->String

	public String getCol_code() {
		return col_code;
	}

	public void setCol_code(String col_code) {
		this.col_code = col_code;
	}

	public String getCol_sequence() {
		return col_sequence;
	}

	public void setCol_sequence(String col_sequence) {
		this.col_sequence = col_sequence;
	}

	public String getDigit() {
		return digit;
	}

	public void setDigit(String digit) {
		this.digit = digit;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMsu_id() {
		return msu_id;
	}

	public void setMsu_id(String msu_id) {
		this.msu_id = msu_id;
	}

	public String getNew_title() {
		return new_title;
	}

	public void setNew_title(String new_title) {
		this.new_title = new_title;
	}

	public String getRecord_code() {
		return record_code;
	}

	public void setRecord_code(String record_code) {
		this.record_code = record_code;
	}

	public String getRef_con() {
		return ref_con;
	}

	public void setRef_con(String ref_con) {
		this.ref_con = ref_con;
	}

	public String getRef_field() {
		return ref_field;
	}

	public void setRef_field(String ref_field) {
		this.ref_field = ref_field;
	}

	public String getRef_map_type() {
		return ref_map_type;
	}

	public void setRef_map_type(String ref_map_type) {
		this.ref_map_type = ref_map_type;
	}

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getTitle_desc() {
		return title_desc;
	}

	public void setTitle_desc(String title_desc) {
		this.title_desc = title_desc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFiled_type() {
		return filed_type;
	}

	public void setFiled_type(String filed_type) {
		this.filed_type = filed_type;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
