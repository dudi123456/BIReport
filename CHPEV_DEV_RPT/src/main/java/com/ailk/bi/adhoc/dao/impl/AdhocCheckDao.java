package com.ailk.bi.adhoc.dao.impl;

import com.ailk.bi.adhoc.dao.IAdhocCheckDAO;
import com.ailk.bi.adhoc.util.AdhocUtil;

public class AdhocCheckDao implements IAdhocCheckDAO {

	/**
	 * 提取多选SQL
	 */
	public String[][] getCheckSql(String obj_type) {

		// String sql ="";
		String[][] arr = AdhocUtil.queryArrayFacade("QT040", obj_type);
		if (arr != null && arr.length > 0) {
			return arr;
		}
		return null;
	}

	public String[][] getCheckRule(String obj_type) {

		return AdhocUtil.queryArrayFacade("QT043", obj_type);
	}

}
