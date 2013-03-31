package com.ailk.bi.metamanage.model;

import java.util.List;

@SuppressWarnings({ "rawtypes" })
public class JobTableFlow {

	private String job_id;
	private String job_name;
	private String in_table_id;
	private String in_table_name;
	private String out_table_id;
	private String out_table_name;
	private String layer_id;
	private int x;
	private int y;
	private List childTable;

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

	public String getLayer_id() {
		return layer_id;
	}

	public void setLayer_id(String layer_id) {
		this.layer_id = layer_id;
	}

	public List getChildTable() {
		return childTable;
	}

	public void setChildTable(List childTable) {
		this.childTable = childTable;
	}

	public String getIn_table_name() {
		return in_table_name;
	}

	public void setIn_table_name(String in_table_name) {
		this.in_table_name = in_table_name;
	}

	public String getOut_table_name() {
		return out_table_name;
	}

	public void setOut_table_name(String out_table_name) {
		this.out_table_name = out_table_name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
