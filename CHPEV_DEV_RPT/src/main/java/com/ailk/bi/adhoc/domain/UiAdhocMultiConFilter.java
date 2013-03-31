package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocMultiConFilter extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String adhoc_id = "";

	private String con_id = "";

	private String filter_type = "";

	private String filter_con_id = "";

	private String filter_con_str = "";

	private String security_type = "";

	private String filter_qry_name = "";

	private String column_type = "";

	public String getAdhoc_id() {
		return adhoc_id;
	}

	public void setAdhoc_id(String adhoc_id) {
		this.adhoc_id = adhoc_id;
	}

	public String getCon_id() {
		return con_id;
	}

	public void setCon_id(String con_id) {
		this.con_id = con_id;
	}

	public String getFilter_con_id() {
		return filter_con_id;
	}

	public void setFilter_con_id(String filter_con_id) {
		this.filter_con_id = filter_con_id;
	}

	public String getFilter_con_str() {
		return filter_con_str;
	}

	public void setFilter_con_str(String filter_con_str) {
		this.filter_con_str = filter_con_str;
	}

	public String getFilter_type() {
		return filter_type;
	}

	public void setFilter_type(String filter_type) {
		this.filter_type = filter_type;
	}

	public String getSecurity_type() {
		return security_type;
	}

	public void setSecurity_type(String security_type) {
		this.security_type = security_type;
	}

	public String getFilter_qry_name() {
		return filter_qry_name;
	}

	public void setFilter_qry_name(String filter_qry_name) {
		this.filter_qry_name = filter_qry_name;
	}

	public String getColumn_type() {
		return column_type;
	}

	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}

}
