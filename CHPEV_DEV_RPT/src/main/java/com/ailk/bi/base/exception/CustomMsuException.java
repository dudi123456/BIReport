package com.ailk.bi.base.exception;

import java.io.Serializable;

public class CustomMsuException extends RuntimeException implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7481066163061182640L;

	private Throwable t;

	public String getThrowable() {
		return ("Received throwable with Message: " + t.getMessage());
	}

	public CustomMsuException(String s) {
		super(s);
	}

	public CustomMsuException(String s, Throwable t) {
		super(s);
		this.t = t;
	}
}
