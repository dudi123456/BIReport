package com.ailk.bi.base.exception;

import java.io.Serializable;

public class ReportMsuException extends RuntimeException implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 644070733277952519L;

	private Throwable t;

	public String getThrowable() {
		return ("Received throwable with Message: " + t.getMessage());
	}

	public ReportMsuException(String s) {
		super(s);
	}

	public ReportMsuException(String s, Throwable t) {
		super(s);
		this.t = t;
	}
}
