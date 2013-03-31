package com.fins.gt.dataaccess;

import com.fins.gt.util.LogHandler;

public abstract class TransactionWrapper {
	private boolean success = false;

	public boolean execute() {
		success = true;
		beforeStart();
		DataAccessManager.txStart();
		try {
			transaction();
		} catch (Exception e) {
			success = false;
			LogHandler.error(this, e);
			DataAccessManager.txRollback();
			afterRollback(e);
		}
		if (success) {
			DataAccessManager.txEnd();
			afterEnd();
		}
		return success;
	}

	abstract public void transaction() throws Exception;

	public void beforeStart() {
	}

	public void afterRollback(Exception e) {
	}

	public void afterEnd() {
	}

	public boolean isSuccess() {
		return success;
	}
}
