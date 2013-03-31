package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocMsuMetaTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String group_id = "";

	private String msu_id = "";

	private String msu_name = "";

	private String msu_desc = "";

	private String msu_field = "";

	private String unit = "";

	private String digit = "";

	private String status = "";

	private String sequence = "";

	private String isduraflag = "";

	private String cal_rule = "";

	private String cal_msu = "";

	private boolean isDefault = false;

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getDigit() {
		return digit;
	}

	public void setDigit(String digit) {
		this.digit = digit;
	}

	public String getIsduraflag() {
		return isduraflag;
	}

	public void setIsduraflag(String isduraflag) {
		this.isduraflag = isduraflag;
	}

	public String getMsu_field() {
		return msu_field;
	}

	public void setMsu_field(String msu_field) {
		this.msu_field = msu_field;
	}

	public String getMsu_id() {
		return msu_id;
	}

	public void setMsu_id(String msu_id) {
		this.msu_id = msu_id;
	}

	public String getMsu_name() {
		return msu_name;
	}

	public void setMsu_name(String msu_name) {
		this.msu_name = msu_name;
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

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getCal_rule() {
		return cal_rule;
	}

	public void setCal_rule(String cal_rule) {
		this.cal_rule = cal_rule;
	}

	public String getCal_msu() {
		return cal_msu;
	}

	public void setCal_msu(String cal_msu) {
		this.cal_msu = cal_msu;
	}

	public String getMsu_desc() {
		return msu_desc;
	}

	public void setMsu_desc(String msu_desc) {
		this.msu_desc = msu_desc;
	}

}
