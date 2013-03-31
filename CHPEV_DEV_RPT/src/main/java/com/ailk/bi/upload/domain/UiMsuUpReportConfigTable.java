package com.ailk.bi.upload.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiMsuUpReportConfigTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String report_id = ""; // NUMBER->String

	private String report_code = ""; // VARCHAR->String

	private String report_name = ""; // VARCHAR->String

	private String report_sequence = ""; // VARCHAR->String

	private String report_group = ""; // NUMBER->String

	private String report_type = ""; // CHAR->String

	private String ftp_config_id = ""; // NUMBER->String

	private String is_valid = ""; // CHAR->String

	private String filename_type = ""; // VARCHAR->String

	public String getFilename_type() {
		return filename_type;
	}

	public void setFilename_type(String filename_type) {
		this.filename_type = filename_type;
	}

	public String getFtp_config_id() {
		return ftp_config_id;
	}

	public void setFtp_config_id(String ftp_config_id) {
		this.ftp_config_id = ftp_config_id;
	}

	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

	public String getReport_code() {
		return report_code;
	}

	public void setReport_code(String report_code) {
		this.report_code = report_code;
	}

	public String getReport_group() {
		return report_group;
	}

	public void setReport_group(String report_group) {
		this.report_group = report_group;
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

	public String getReport_sequence() {
		return report_sequence;
	}

	public void setReport_sequence(String report_sequence) {
		this.report_sequence = report_sequence;
	}

	public String getReport_type() {
		return report_type;
	}

	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
