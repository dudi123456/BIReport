package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocRuleUserDimTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String adhoc_id = ""; // VARCHAR->String

	private String oper_no = ""; // VARCHAR->String

	private String oper_name = ""; // VARCHAR->String

	private String dim_relation_field = ""; // VARCHAR->String

	private String low_value = ""; // NUMBER->String

	private String hign_value = ""; // NUMBER->String

	private String sequence = ""; // NUMBER->String

	private String status = ""; // CHAR->String

	private String self_dim_id = "";

	private String self_dim_name = "";

	public String getAdhoc_id() {
		return adhoc_id;
	}

	public void setAdhoc_id(String adhoc_id) {
		this.adhoc_id = adhoc_id;
	}

	public String getDim_relation_field() {
		return dim_relation_field;
	}

	public void setDim_relation_field(String dim_relation_field) {
		this.dim_relation_field = dim_relation_field;
	}

	public String getHign_value() {
		return hign_value;
	}

	public void setHign_value(String hign_value) {
		this.hign_value = hign_value;
	}

	public String getLow_value() {
		return low_value;
	}

	public void setLow_value(String low_value) {
		this.low_value = low_value;
	}

	public String getOper_name() {
		return oper_name;
	}

	public void setOper_name(String oper_name) {
		this.oper_name = oper_name;
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

	public String getSelf_dim_id() {
		return self_dim_id;
	}

	public void setSelf_dim_id(String self_dim_id) {
		this.self_dim_id = self_dim_id;
	}

	public String getSelf_dim_name() {
		return self_dim_name;
	}

	public void setSelf_dim_name(String self_dim_name) {
		this.self_dim_name = self_dim_name;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
