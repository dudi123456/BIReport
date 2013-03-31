package com.ailk.bi.syspar.util;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

public class ParamUtil {

	/**
	 * 查询数组（单一条件）
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sql) {
		String arr[][] = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组（单一条件）
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String whereStr) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, whereStr);
			System.out.println(sqlId + "================" + sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 查询数组(双条件)
	 * 
	 * @param sqlId
	 * @param whereStr
	 * @return
	 */
	public static String[][] queryArrayFacade(String sqlId, String paramA,
			String paramB) {
		String sql = "";
		String arr[][] = null;
		try {
			sql = SQLGenator.genSQL(sqlId, paramA, paramB);
			System.out.println(sql);
			arr = WebDBUtil.execQryArray(sql, "");

		} catch (AppException ex1) {
			//
			ex1.printStackTrace();
		}
		return arr;
	}

	/**
	 * 补齐列函数
	 * 
	 * @param length
	 * @return
	 */
	public static String getNbspTdInnerHtml(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append("<td>&nbsp;</td>\n");
			sb.append("<td>&nbsp;</td>\n");
		}
		return sb.toString();
	}

}
