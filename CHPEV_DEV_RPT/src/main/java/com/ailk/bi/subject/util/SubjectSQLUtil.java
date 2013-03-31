package com.ailk.bi.subject.util;

import org.apache.commons.lang.StringUtils;

public class SubjectSQLUtil {
	private SubjectSQLUtil() {
		// 禁止实例化
	}

	/**
	 * 由普通SQL产生计算总数SQL
	 * 
	 * @param querySQL
	 * @return
	 */
	public static String genTotalSQL(String querySQL, boolean hasGroup, boolean hasOrderBy) {
		String ret = querySQL;
		if (!StringUtils.isBlank(ret)) {
			int pos = -1;
			// 这里需要去掉order by
			if (hasOrderBy) {
				pos = ret.lastIndexOf("ORDER BY");
				if (pos >= 0) {
					// 有ORDER BY，可能是子查询中，也可能是后面的不管子查询了
					ret = ret.substring(0, pos);
				}
			}
			if (hasGroup) {
				ret = "SELECT COUNT(1) FROM (" + ret + ")";
			} else {
				pos = ret.indexOf("FROM");
				if (pos >= 0) {
					ret = "SELECT COUNT(1) " + ret.substring(pos);
				}
			}
		}
		return ret;
	}

	public static String genPageingSQL(String querySQL, int pageNo, int pageSize) {
		int start = (pageNo) * pageSize + 1;
		int end = (pageNo + 1) * pageSize;
		StringBuilder sb = new StringBuilder();
		// 这里 querySQL可能存在重复的字段，需要建立别名
		sb.append("SELECT * FROM (SELECT Z.* ,ROWNUM RN FROM (").append(querySQL).append(") Z WHERE ROWNUM<=")
				.append(end).append(") WHERE RN>=").append(start);
		return sb.toString();
	}

	public static String aliasFields(String querySQL) {
		StringBuilder sb = new StringBuilder();
		int selectPos = querySQL.indexOf("SELECT ") + "SELECT ".length();
		int fromPos = querySQL.indexOf(" FROM");
		String st = querySQL.substring(selectPos, fromPos);
		// 分割，这里直接给出所有别名
		sb.append("SELECT ");
		String[] splits = st.split(",");
		for (int i = 0; i < splits.length; i++) {
			sb.append(splits[i]).append(" AS F").append(i).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(querySQL.substring(fromPos));
		return sb.toString();
	}
}
