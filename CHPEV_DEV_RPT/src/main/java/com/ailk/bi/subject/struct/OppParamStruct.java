package com.ailk.bi.subject.struct;

import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

/**
 * 
 * 竞争对手评估参数配置对象
 * 
 * @author jcm
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class OppParamStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String param_type;
	private String param_id;
	private String param_name;
	private String param_desc;
	private String param_rule;
	private String param_weight;

	private String param_status;

	private List valueObjs;

	public List getValueObjs() {
		return valueObjs;
	}

	public void setValueObjs(List valueObjs) {
		this.valueObjs = valueObjs;
	}

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

	public String getParam_desc() {
		return param_desc;
	}

	public void setParam_desc(String param_desc) {
		this.param_desc = param_desc;
	}

	public String getParam_rule() {
		return param_rule;
	}

	public void setParam_rule(String param_rule) {
		this.param_rule = param_rule;
	}

	public String getParam_weight() {
		return param_weight;
	}

	public void setParam_weight(String param_weight) {
		this.param_weight = param_weight;
	}

	public String getParam_status() {
		return param_status;
	}

	public void setParam_status(String param_status) {
		this.param_status = param_status;
	}

}
