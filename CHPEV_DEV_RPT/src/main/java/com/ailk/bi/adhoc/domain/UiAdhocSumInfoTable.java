package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocSumInfoTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String adhoc_id = ""; // VARCHAR->String

	private String msu_name = ""; // VARCHAR->String

	private String msu_filed = ""; // VARCHAR->String

	private String sequence = ""; // NUMBER->String

	private String unit = ""; // VARCHAR->String

	private String digit = ""; // NUMBER->String

	private String status = ""; // NUMBER->String

	private String cal_rule = ""; // NUMBER->String

	private String data_source = ""; // NUMBER->String

	private String value = ""; // NUMBER->String

	public String getAdhoc_id() {
		return adhoc_id;
	}

	public void setAdhoc_id(String adhoc_id) {
		this.adhoc_id = adhoc_id;
	}

	public String getDigit() {
		return digit;
	}

	public void setDigit(String digit) {
		this.digit = digit;
	}

	public String getMsu_filed() {
		return msu_filed;
	}

	public void setMsu_filed(String msu_filed) {
		this.msu_filed = msu_filed;
	}

	public String getMsu_name() {
		return msu_name;
	}

	public void setMsu_name(String msu_name) {
		this.msu_name = msu_name;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCal_rule() {
		return cal_rule;
	}

	public void setCal_rule(String cal_rule) {
		this.cal_rule = cal_rule;
	}

	public String getData_source() {
		return data_source;
	}

	public void setData_source(String data_source) {
		this.data_source = data_source;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
