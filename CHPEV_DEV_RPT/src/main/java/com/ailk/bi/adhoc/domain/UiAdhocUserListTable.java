package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocUserListTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String adhoc_id = ""; // VARCHAR->String

	private String oper_no = ""; // VARCHAR->String

	private String msu_field = ""; // VARCHAR->String

	private String msu_unit = ""; // VARCHAR->String

	private String msu_digit = ""; // NUMBER->String

	private String sequence = ""; // NUMBER->String

	private String status = ""; // CHAR->String

	private String msu_type = ""; // CHAR->String

	private String msu_name = ""; // CHAR->String

	private String map_code = ""; // CHAR->String

	private String default_view = "";

	private String group_Name = "";

	public String toString() {

		String rtn = "'" + adhoc_id + "','" + oper_no + "','" + msu_field
				+ "','" + msu_unit + "','" + msu_digit + "','" + sequence
				+ "','" + status + "','" + msu_type + "','" + msu_name + "','"
				+ map_code + "','" + default_view + "','" + group_Name + "'";
		return rtn;
	}

	public String getGroup_Name() {
		return group_Name;
	}

	public void setGroup_Name(String groupName) {
		group_Name = groupName;
	}

	public String getAdhoc_id() {
		return adhoc_id;
	}

	public void setAdhoc_id(String adhoc_id) {
		this.adhoc_id = adhoc_id;
	}

	public String getMsu_digit() {
		return msu_digit;
	}

	public void setMsu_digit(String msu_digit) {
		this.msu_digit = msu_digit;
	}

	public String getMsu_field() {
		return msu_field;
	}

	public void setMsu_field(String msu_field) {
		this.msu_field = msu_field;
	}

	public String getMsu_unit() {
		return msu_unit;
	}

	public void setMsu_unit(String msu_unit) {
		this.msu_unit = msu_unit;
	}

	public String getOper_no() {
		return oper_no;
	}

	public void setOper_no(String oper_no) {
		this.oper_no = oper_no;
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

	public String getMsu_type() {
		return msu_type;
	}

	public void setMsu_type(String msu_type) {
		this.msu_type = msu_type;
	}

	public String getMsu_name() {
		return msu_name;
	}

	public void setMsu_name(String msu_name) {
		this.msu_name = msu_name;
	}

	public String getMap_code() {
		return map_code;
	}

	public void setMap_code(String map_code) {
		this.map_code = map_code;
	}

	public String getDefault_view() {
		return default_view;
	}

	public void setDefault_view(String default_view) {
		this.default_view = default_view;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
