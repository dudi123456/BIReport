package com.ailk.bi.base.exception;

import java.io.Serializable;

public class ReportOlapException extends RuntimeException implements
		Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 9142728933313634480L;
	private Throwable t;

	public String getThrowable() {
		return ("Received throwable with Message: " + t.getMessage());
	}

	public ReportOlapException(String s) {
		super(s);
	}

	public ReportOlapException(Throwable t) {
		this.t = t;
	}

	public ReportOlapException(String s, Throwable t) {
		super(s + "," + t.getMessage());
		this.t = t;
	}
}
