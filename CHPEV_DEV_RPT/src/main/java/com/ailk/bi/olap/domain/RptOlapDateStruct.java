package com.ailk.bi.olap.domain;

import java.io.Serializable;

/**
 * @author DXF
 * 
 */
public class RptOlapDateStruct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3839072468500510434L;

	/**
	 * 开始时间
	 */
	private String start = null;

	/**
	 * 结束时间
	 */
	private String end = null;

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
}
