package com.ailk.bi.olap.domain;

import java.io.Serializable;

public class RptOlapDimStruct implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 419905027345219009L;

	/**
	 * 维度值
	 */
	private String dim_id = null;

	/**
	 * 维度描述
	 */
	private String dim_desc = null;

	/**
	 * 该维度是否被选中
	 */
	private boolean checked = false;

	public String getDim_desc() {
		return dim_desc;
	}

	public void setDim_desc(String dim_desc) {
		this.dim_desc = dim_desc;
	}

	public String getDim_id() {
		return dim_id;
	}

	public void setDim_id(String dim_id) {
		this.dim_id = dim_id;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
