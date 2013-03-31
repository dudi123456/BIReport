package com.fins.gt.dataaccess.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fins.gt.util.CacheUtils;
import com.fins.gt.util.LogHandler;
import com.fins.gt.util.StringUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DataAccessUtils {

	private static Pattern pattern = Pattern
			.compile(DataAccessConstants.REX_PARAMETER);
	private static Map sqlSnippetsCache = Collections
			.synchronizedMap(new HashMap());

	public static String[] getColumnName(ResultSet resultSet)
			throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();
		String[] columnName = new String[cols];
		for (int i = 0; i < columnName.length; i++) {
			columnName[i] = metaData.getColumnName(i + 1).toLowerCase();
			// columnName[i] = metaData.getColumnName(i + 1);
		}
		return columnName;
	}

	public static int[] getColumnType(ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols = metaData.getColumnCount();
		int[] columnType = new int[cols];
		for (int i = 0; i < columnType.length; i++) {
			columnType[i] = metaData.getColumnType(i + 1);
		}
		return columnType;
	}

	public static void buildRecord(ResultSet resultSet, String[] columnName,
			Map map) throws SQLException {
		for (int i = 0; i < columnName.length; i++) {
			map.put(columnName[i], resultSet.getString(columnName[i]));
		}
	}

	public static String parseEasySql(String easySql) {
		String newSql = StringUtils.trim(easySql);
		newSql = newSql.replaceAll(DataAccessConstants.NOTEQUAL_S,
				DataAccessConstants.NOTEQUAL_SS);
		return newSql;
	}

	public static String escRex(String in) {
		return in.replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}");
	}

	public static List createSqlSnippets(String inSql) {

		if (inSql == null || inSql.trim().length() < 1) {
			return null;
		}
		List sqlSnippetList = (List) CacheUtils.getFromCache(sqlSnippetsCache,
				inSql);
		if (sqlSnippetList != null) {
			return sqlSnippetList;
		}
		sqlSnippetList = new ArrayList();
		CacheUtils.addToCache(sqlSnippetsCache, inSql, sqlSnippetList);

		Pattern pattern = Pattern.compile(DataAccessConstants.REX_SQL);
		Matcher matcher = pattern.matcher(inSql);
		while (matcher.find()) {
			SqlSnippet sqlSnippet = new SqlSnippet();
			sqlSnippet.start = matcher.start(0);
			sqlSnippet.end = inSql.indexOf(DataAccessConstants.END_TAG,
					sqlSnippet.start) + DataAccessConstants.END_TAG.length();
			sqlSnippet.condition = matcher.group(1).trim();
			sqlSnippet.snippet = inSql.substring(sqlSnippet.start,
					sqlSnippet.end).trim();
			sqlSnippet.content = sqlSnippet.snippet.substring(
					matcher.group(0).length(),
					sqlSnippet.snippet.length()
							- DataAccessConstants.END_TAG.length()).trim();
			sqlSnippet.init();
			sqlSnippetList.add(sqlSnippet);
		}
		return sqlSnippetList;
	}

	public static PreparedStatement createPreparedStatement(Connection conn,
			String inSql, Map paramMap) throws SQLException {
		return createPreparedStatement(conn, inSql, new Map[] { paramMap },
				false);
	}

	public static PreparedStatement createPreparedStatement(Connection conn,
			String inSql, Map[] paramMaps) throws SQLException {
		return createPreparedStatement(conn, inSql, paramMaps, true);
	}

	public static PreparedStatement createPreparedStatement(Connection conn,
			String inSql, Map[] paramMaps, boolean batch) throws SQLException {

		PreparedStatement pstmt = null;
		try {
			for (int i = 0; i < paramMaps.length; i++) {
				Object[] sqlInfo = getDBSqlInfo(inSql, paramMaps[i]);
				String nsql = (String) sqlInfo[0];
				List paraList = (List) sqlInfo[1];
				if (pstmt == null) {
					pstmt = conn.prepareStatement(nsql);
				}
				for (int j = 0; j < paraList.size(); j++) {
					SqlParameter sqlParameter = (SqlParameter) paraList.get(j);
					pstmt.setString(sqlParameter.getIndex(),
							sqlParameter.getValueAsString());
				}
				if (batch) {
					pstmt.addBatch();
				}
			}
		} catch (SQLException e) {
			LogHandler.error(DataAccessUtils.class, e);
			if (pstmt != null)
				pstmt.close();
			throw e;
		}
		return pstmt;
	}

	public static Object[] getDBSqlInfo(String inSql, Map paramMap) {

		List sqlSnippetList = createSqlSnippets(inSql);
		List paraList = new ArrayList();

		StringBuffer sql = new StringBuffer();
		int start = 0;
		int last = 0;
		if (paramMap == null)
			paramMap = new HashMap();
		int i = 0;
		for (i = 0; i < sqlSnippetList.size(); i++) {
			SqlSnippet sqlSnippet = (SqlSnippet) sqlSnippetList.get(i);
			start = last;
			last = sqlSnippet.start;
			String osql = inSql.substring(start, last);
			sql.append(osql);
			if (sqlSnippet.check(paramMap)) {
				sql.append(sqlSnippet.content);
			}
			last = sqlSnippet.end;
		}
		if (last > 0 && last < inSql.length()) {
			sql.append(inSql.substring(last));
		}
		if (i == 0) {
			sql.append(inSql);
		}
		String outSql = sql.toString();
		Matcher matcher = pattern.matcher(outSql);

		int index = 0;
		while (matcher.find()) {
			index++;
			String pPara = matcher.group(0);
			String pName = matcher.group(1);
			String pType = DataAccessConstants.IN_TYPE;
			if (pName.startsWith(DataAccessConstants.C_SQL_OUT_PARAMETER)) {
				pName = pName.substring(4);
				pType = DataAccessConstants.OUT_TYPE;
				// pPara="#{"+pPara.substring(5);
			}
			String pValue = String.valueOf(paramMap.get(pName));
			SqlParameter sqlPara = new SqlParameter(pName, pValue, pType);

			sqlPara.setIndex(index);
			paraList.add(sqlPara);
			outSql = StringUtils.replace(outSql, pPara, "?");
		}
		LogHandler.debug(outSql + "  \t  " + paraList);
		return new Object[] { outSql, paraList };
	}

	public static boolean isEquals(long a, long b) {
		return a == b;
	}

	public static boolean isNotEquals(long a, long b) {
		return a != b;
	}

	public static boolean isLessThen(long a, long b) {
		return a < b;
	}

	public static boolean isGreatThen(long a, long b) {
		return a > b;
	}

	public static boolean isLessThenE(long a, long b) {
		return a <= b;
	}

	public static boolean isGreatThenE(long a, long b) {
		return a >= b;
	}

	public static boolean isEmpty(String a) {
		return a == null || a.length() < 1;
	}

	public static boolean isNotEmpty(String a) {
		return !isEmpty(a);
	}

	public static boolean isEquals(String a, String b) {
		if (b == null) {
			return a == null;
		}
		return b.equals(a);
	}

	public static boolean isNotEquals(String a, String b) {
		if (b == null) {
			return a != null;
		}
		return !b.equals(a);
	}

	public static boolean isLessThen(String a, String b) {
		return a != null && a.compareTo(b) < 0;
	}

	public static boolean isGreatThen(String a, String b) {
		return a != null && a.compareTo(b) > 0;
	}

	public static boolean isLessThenE(String a, String b) {
		return a != null && (a.compareTo(b) < 0 || a.equals(b));
	}

	public static boolean isGreatThenE(String a, String b) {
		return a != null && (a.compareTo(b) > 0 || a.equals(b));
	}

}
