package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocDimMetaTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String group_id = "";

	private String dim_id = "";

	private String dim_name = "";

	private String dim_field_name = "";

	private String dim_field_desc = "";

	private String dim_desc = "";

	private String dim_map_code = "";

	private String dim_relation_field = "";

	private String sequence = "";

	private String status = "";

	private String dim_expand_flag = "";

	private String dim_expand_group = "";

	private boolean isDefault = false;

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getDim_desc() {
		return dim_desc;
	}

	public void setDim_desc(String dim_desc) {
		this.dim_desc = dim_desc;
	}

	public String getDim_field_desc() {
		return dim_field_desc;
	}

	public void setDim_field_desc(String dim_field_desc) {
		this.dim_field_desc = dim_field_desc;
	}

	public String getDim_field_name() {
		return dim_field_name;
	}

	public void setDim_field_name(String dim_field_name) {
		this.dim_field_name = dim_field_name;
	}

	public String getDim_id() {
		return dim_id;
	}

	public void setDim_id(String dim_id) {
		this.dim_id = dim_id;
	}

	public String getDim_name() {
		return dim_name;
	}

	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
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

	public String getDim_map_code() {
		return dim_map_code;
	}

	public void setDim_map_code(String dim_map_code) {
		this.dim_map_code = dim_map_code;
	}

	public String getDim_relation_field() {
		return dim_relation_field;
	}

	public void setDim_relation_field(String dim_relation_field) {
		this.dim_relation_field = dim_relation_field;
	}

	public String getDim_expand_flag() {
		return dim_expand_flag;
	}

	public void setDim_expand_flag(String dim_expand_flag) {
		this.dim_expand_flag = dim_expand_flag;
	}

	public String getDim_expand_group() {
		return dim_expand_group;
	}

	public void setDim_expand_group(String dim_expand_group) {
		this.dim_expand_group = dim_expand_group;
	}

}
