package com.ailk.bi.syspar.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiParamInfoConfigTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String param_id = ""; // VARCHAR->String

	private String parent_id = ""; // VARCHAR->String

	private String param_name = ""; // VARCHAR->String

	private String param_desc = ""; // VARCHAR->String

	private String table_name = ""; // VARCHAR->String

	private String log_table_name = ""; // VARCHAR->String

	public String getLog_table_name() {
		return log_table_name;
	}

	public void setLog_table_name(String log_table_name) {
		this.log_table_name = log_table_name;
	}

	private String status = ""; // NUMBER->String

	private String isleaf = ""; // CHAR->String

	private String isdimtable = ""; // CHAR->String

	private String default_tag = ""; // CHAR->String

	public String getDefault_tag() {
		return default_tag;
	}

	public void setDefault_tag(String default_tag) {
		this.default_tag = default_tag;
	}

	public String getIsdimtable() {
		return isdimtable;
	}

	public void setIsdimtable(String isdimtable) {
		this.isdimtable = isdimtable;
	}

	public String getParam_desc() {
		return param_desc;
	}

	public void setParam_desc(String param_desc) {
		this.param_desc = param_desc;
	}

	public String getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(String isleaf) {
		this.isleaf = isleaf;
	}

	public String getParam_id() {
		return param_id;
	}

	public void setParam_id(String param_id) {
		this.param_id = param_id;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
