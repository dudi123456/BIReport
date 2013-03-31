package com.fins.gt.dataaccess;

import java.sql.SQLException;

public class RuntimeSQLException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RuntimeSQLException() {
		super();
	}

	public RuntimeSQLException(SQLException cause) {
		super(cause);
	}

	public RuntimeSQLException(String message) {
		super(message);
	}

	public RuntimeSQLException(String message, SQLException cause) {
		super(message, cause);
	}

}
