package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocConditionMetaTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String group_id = "";

	private String con_id = "";

	private String con_name = "";

	private String con_desc = "";

	private String con_type = "";

	private String con_rule = "";

	private String con_tag = "";

	private String qry_name = "";

	private String filed_name = "";

	private String sequence = "";

	private String status = "";

	private String validator = "";

	private String valuea = "";

	private String valueb = "";

	private boolean isDefault = false;

	public String getCon_id() {
		return con_id;
	}

	public void setCon_id(String con_id) {
		this.con_id = con_id;
	}

	public String getCon_name() {
		return con_name;
	}

	public void setCon_name(String con_name) {
		this.con_name = con_name;
	}

	public String getCon_rule() {
		return con_rule;
	}

	public void setCon_rule(String con_rule) {
		this.con_rule = con_rule;
	}

	public String getCon_tag() {
		return con_tag;
	}

	public void setCon_tag(String con_tag) {
		this.con_tag = con_tag;
	}

	public String getCon_type() {
		return con_type;
	}

	public void setCon_type(String con_type) {
		this.con_type = con_type;
	}

	public String getFiled_name() {
		return filed_name;
	}

	public void setFiled_name(String filed_name) {
		this.filed_name = filed_name;
	}

	public String getQry_name() {
		return qry_name;
	}

	public void setQry_name(String qry_name) {
		this.qry_name = qry_name;
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

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public String getValuea() {
		return valuea;
	}

	public void setValuea(String valuea) {
		this.valuea = valuea;
	}

	public String getValueb() {
		return valueb;
	}

	public void setValueb(String valueb) {
		this.valueb = valueb;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getCon_desc() {
		return con_desc;
	}

	public void setCon_desc(String con_desc) {
		this.con_desc = con_desc;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
