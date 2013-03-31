package com.fins.gt.dataaccess;

import java.sql.Connection;
import java.sql.SQLException;

import com.fins.gt.util.LogHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DataAccessManager {

	private static IDataBaseManager currentManager;

	protected static int DEFAULT_MAX_CONN = 10000;

	public static boolean initDataSource(String url, String username,
			String password) {
		if (currentManager != null) {
			currentManager.setURL(url);
			currentManager.setUser(username);
			currentManager.setPassword(password);
		}
		return currentManager != null;
	}

	public static boolean initConnectionPool() {
		return initConnectionPool(DEFAULT_MAX_CONN);
	}

	public static boolean initConnectionPool(int maxConn) {
		if (currentManager != null) {
			currentManager.setMaxConnections(maxConn);
			return true;
		}
		return false;
	}

	public static boolean testDataSource() {
		Connection conn = null;
		try {
			conn = getConnection();
			return conn != null;
		} catch (Exception e) {
			return false;
		} finally {
			closeConnection(conn);
		}
	}

	private static final ThreadLocal txConnection = new ThreadLocal();

	public static Connection setTxConnection(Connection connection) {
		Connection prevConnection = (Connection) txConnection.get();
		txConnection.set(connection);
		return prevConnection;
	}

	public static boolean txStart() {
		Connection conn = getConnection();
		setTxConnection(conn);
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
				return true;
			} catch (SQLException e) {
				LogHandler.error(DataAccessManager.class, e);
			}
		}
		return false;
	}

	public static boolean txEnd() {
		return txEnd(true);
	}

	public static boolean txRollback() {
		LogHandler.info("Transaction Rollback");
		return txEnd(false);
	}

	public static boolean txEnd(boolean commit) {
		Connection conn = getConnection();
		setTxConnection(null);
		if (conn != null) {
			try {
				if (commit) {
					conn.commit();
				} else {
					conn.rollback();
				}
				conn.setAutoCommit(true);
				return true;
			} catch (SQLException e) {
				LogHandler.error(DataAccessManager.class, e);
			} finally {
				closeConnection(conn);
			}
		}
		return false;
	}

	public static Connection getConnection() {
		Connection conn = (Connection) txConnection.get();
		if (conn == null && currentManager != null) {
			conn = currentManager.getConnection();
			try {
				conn.setAutoCommit(true);
			} catch (Exception e) {
			}
		}
		return conn;
	}

	public static boolean isAutoCommit(Connection conn) {
		try {
			return conn.getAutoCommit();
		} catch (Exception e) {
		}
		return true;
	}

	public static void closeConnection(Connection conn) {
		if (conn != null && isAutoCommit(conn)) {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				LogHandler.error(DataAccessManager.class, e);
			}
		}
	}

	public static void destroy() {
		if (currentManager != null)
			currentManager.dispose();
		currentManager = null;
	}

	public static IDataBaseManager getCurrentManager() {
		return currentManager;
	}

	public static void setCurrentManager(IDataBaseManager currentManager) {
		DataAccessManager.currentManager = currentManager;
	}
}
