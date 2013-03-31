package com.ailk.bi.upload.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiMsuUpRptRecordTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1149794774474135845L;

	// HERE IS FROM DATABASE
	private String report_id = ""; // VARCHAR->String

	private String report_name = ""; // VARCHAR->String

	private String record_code = ""; // VARCHAR->String

	private String record_desc = ""; // VARCHAR->String

	private String status = ""; // NUMBER->String

	private String svc_flag = ""; // CHAR->String

	private String db_config_id = ""; // NUMBER->String

	private String is_sql_used = ""; // CHAR->String

	private String sql = ""; // VARCHAR->String

	public String getDb_config_id() {
		return db_config_id;
	}

	public void setDb_config_id(String db_config_id) {
		this.db_config_id = db_config_id;
	}

	public String getIs_sql_used() {
		return is_sql_used;
	}

	public void setIs_sql_used(String is_sql_used) {
		this.is_sql_used = is_sql_used;
	}

	public String getRecord_code() {
		return record_code;
	}

	public void setRecord_code(String record_code) {
		this.record_code = record_code;
	}

	public String getRecord_desc() {
		return record_desc;
	}

	public void setRecord_desc(String record_desc) {
		this.record_desc = record_desc;
	}

	public String getReport_id() {
		return report_id;
	}

	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	public String getReport_name() {
		return report_name;
	}

	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSvc_flag() {
		return svc_flag;
	}

	public void setSvc_flag(String svc_flag) {
		this.svc_flag = svc_flag;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
