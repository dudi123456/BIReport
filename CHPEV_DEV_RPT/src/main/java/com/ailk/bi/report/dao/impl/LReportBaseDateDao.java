package com.ailk.bi.report.dao.impl;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportBaseDateDao;

public class LReportBaseDateDao implements ILReportBaseDateDao {

	public String getBaseDate(String optTypeId) throws AppException {
		String returnStr = "";
		String[][] optIdArray;
		if (optTypeId == null || "".equals(optTypeId))
			throw new AppException();
		String strSql = "select t.opt_id from  UI_RPT_OPT_CODE t where t.opt_type_id="
				+ optTypeId;
		optIdArray = WebDBUtil.execQryArray(strSql, "");
		for (int i = 0; i < optIdArray.length; i++) {
			returnStr += optIdArray[i][0];
			if (i != optIdArray.length - 1) {
				returnStr += ",";
			}
		}
		return returnStr;
	}
}