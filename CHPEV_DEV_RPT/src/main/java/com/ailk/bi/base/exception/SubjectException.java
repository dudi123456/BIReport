package com.ailk.bi.base.exception;

import java.io.Serializable;

public class SubjectException extends RuntimeException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8880546648958682563L;

	private Throwable t;

	public String getThrowable() {
		return ("Received throwable with Message: " + t.getMessage());
	}

	public SubjectException(String s) {
		super(s);
	}

	public SubjectException(String s, Throwable t) {
		super(s);
		this.t = t;
	}

	public void printStackTrace() {
		if (null != this.t)
			this.t.printStackTrace();
		super.printStackTrace();
	}

}
