package com.ailk.bi.base.exception;

import java.io.Serializable;

public class ReportHeadException extends RuntimeException implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 169885670958228915L;

	private Throwable t;

	public String getThrowable() {
		return ("Received throwable with Message: " + t.getMessage());
	}

	public ReportHeadException(String s) {
		super(s);
	}

	public ReportHeadException(String s, Throwable t) {
		super(s);
		this.t = t;
	}
}
