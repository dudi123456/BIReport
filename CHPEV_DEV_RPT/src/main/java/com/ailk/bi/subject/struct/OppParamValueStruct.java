package com.ailk.bi.subject.struct;

import com.ailk.bi.common.event.JBTableBase;

public class OppParamValueStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String param_type;
	private String param_id;
	private String param_name;

	public String getParam_type() {
		return param_type;
	}

	public void setParam_type(String param_type) {
		this.param_type = param_type;
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

	public String getLvl_id() {
		return lvl_id;
	}

	public void setLvl_id(String lvl_id) {
		this.lvl_id = lvl_id;
	}

	public String getLvl_name() {
		return lvl_name;
	}

	public void setLvl_name(String lvl_name) {
		this.lvl_name = lvl_name;
	}

	public String getStart_val() {
		return start_val;
	}

	public void setStart_val(String start_val) {
		this.start_val = start_val;
	}

	public String getEnd_val() {
		return end_val;
	}

	public void setEnd_val(String end_val) {
		this.end_val = end_val;
	}

	private String lvl_id;

	private String lvl_name;

	private String start_val;

	private String end_val;

}
