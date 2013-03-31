package com.fins.gt.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fins.gt.dataaccess.helper.DataAccessUtils;
import com.fins.gt.util.LogHandler;
import com.fins.gt.util.StringUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseDAO {

	public List executeQuery(String sql) throws RuntimeSQLException {
		return executeQuery(sql, null);
	}

	public List executeQuery(String sql, Map paramMap)
			throws RuntimeSQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rest = null;
		List recordList = null;

		try {
			conn = getConnection();
			pstmt = DataAccessUtils
					.createPreparedStatement(conn, sql, paramMap);
			if (pstmt != null) {
				rest = pstmt.executeQuery();
				String[] columnName = DataAccessUtils.getColumnName(rest);
				recordList = new ArrayList();
				Map record = null;
				while (rest.next()) {
					record = new HashMap();
					DataAccessUtils.buildRecord(rest, columnName, record);
					recordList.add(record);
				}
			}
		} catch (SQLException e) {
			recordList = null;
			LogHandler.error(this, e);
			throw new RuntimeSQLException(e);
		} finally {
			close(rest, pstmt, conn);
		}

		return recordList;
	}

	public int executeUpdate(String sql) throws RuntimeSQLException {
		return executeUpdate(sql, null);
	}

	public int[] executeBatchUpdate(String sql, List paramMapList)
			throws RuntimeSQLException {
		Map[] paramMaps = new Map[paramMapList.size()];
		return executeBatchUpdate(sql, (Map[]) paramMapList.toArray(paramMaps));
	}

	public int[] executeBatchUpdate(String sql, Map[] paramMaps)
			throws RuntimeSQLException {

		int[] opresults = null;

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = DataAccessUtils.createPreparedStatement(conn, sql,
					paramMaps);
			if (pstmt != null)
				opresults = pstmt.executeBatch();
		} catch (SQLException e) {
			opresults = null;
			LogHandler.error(this, e);
			throw new RuntimeSQLException(e);
		} finally {
			close(pstmt, conn);
		}
		return opresults;
	}

	public int executeUpdate(String sql, Map paramMap)
			throws RuntimeSQLException {
		int opresult = -1;

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = getConnection();
			pstmt = DataAccessUtils
					.createPreparedStatement(conn, sql, paramMap);
			if (pstmt != null)
				opresult = pstmt.executeUpdate();
		} catch (SQLException e) {
			opresult = -1;
			LogHandler.error(this, e);
			throw new RuntimeSQLException(e);
		} finally {
			close(pstmt, conn);
		}

		return opresult;
	}

	public Integer countTable(String tableName) throws RuntimeSQLException {
		return countTable(tableName, null);
	}

	public Integer countTable(String tableName, String whereSql)
			throws RuntimeSQLException {
		whereSql = StringUtils.isNotEmpty(whereSql) ? " where " + whereSql : "";
		String sql = "select count(*) as totalrownum from " + tableName
				+ whereSql;
		return countQuery(sql, "totalrownum");
	}

	public Integer countQuery(String sql, String fieldName) {
		return countQuery(sql, fieldName, null);
	}

	public Integer countQuery(String sql, String fieldName, Map paramMap) {
		List list = executeQuery(sql);
		if (list != null && list.size() > 0) {
			String t = String.valueOf(((Map) list.get(0)).get(fieldName
					.toLowerCase()));
			try {
				return new Integer(t);
			} catch (Exception e) {
				LogHandler.error(this, e);
			}
		}
		return new Integer(0);
	}

	// ///////////////////////////////////////////

	public static Connection getConnection() {
		return DataAccessManager.getConnection();
	}

	public void close(Connection conn) {
		DataAccessManager.closeConnection(conn);
	}

	public void close(ResultSet rest, Statement pstmt, Connection conn) {
		try {
			close(rest);
		} catch (SQLException e) {
			LogHandler.error(this, e);
		}
		close(pstmt, conn);
	}

	public void close(Statement pstmt, Connection conn) {
		try {
			close(pstmt);
		} catch (SQLException e) {
			LogHandler.error(this, e);
		}
		close(conn);
	}

	public void close(ResultSet rest) throws SQLException {
		if (rest != null)
			rest.close();
	}

	public void close(Statement pstmt) throws SQLException {
		if (pstmt != null)
			pstmt.close();
	}

}
