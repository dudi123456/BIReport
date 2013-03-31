package com.ailk.bi.metamanage.model;

public class EtlJob {
	private String job_id = "";
	private String job_name = "";
	private String job_desc = "";
	private String job_rule = "";
	private String flow_id = "";
	private String status = "";
	private String in_table_id = "";
	private String out_table_id = "";
	private String job_cron = "";

	public String getJob_id() {
		return job_id;
	}

	public void setJob_id(String job_id) {
		this.job_id = job_id;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getJob_desc() {
		return job_desc;
	}

	public void setJob_desc(String job_desc) {
		this.job_desc = job_desc;
	}

	public String getJob_rule() {
		return job_rule;
	}

	public void setJob_rule(String job_rule) {
		this.job_rule = job_rule;
	}

	public String getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIn_table_id() {
		return in_table_id;
	}

	public void setIn_table_id(String in_table_id) {
		this.in_table_id = in_table_id;
	}

	public String getOut_table_id() {
		return out_table_id;
	}

	public void setOut_table_id(String out_table_id) {
		this.out_table_id = out_table_id;
	}

	public String getJob_cron() {
		return job_cron;
	}

	public void setJob_cron(String job_cron) {
		this.job_cron = job_cron;
	}

}
