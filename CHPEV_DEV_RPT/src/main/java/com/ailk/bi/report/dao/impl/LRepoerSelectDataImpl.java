package com.ailk.bi.report.dao.impl;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportSelectDataDao;

public class LRepoerSelectDataImpl implements ILReportSelectDataDao {

	public String getListItem(String strSql) throws AppException {
		String[][] arr = WebDBUtil.execQryArray(strSql, "");
		String result ="";
		if (null != arr) {
			int index = arr.length;
			if (index > 0) {
				for (int i = 0; i < arr.length; i++) {
					String st = "";
					for (int j = 0; j < arr[i].length; j++) {
						st += arr[i][j] + ",";
					}
					if (",".equals(String.valueOf(st.charAt(st.length() - 1)))) {
						st = st.substring(0, st.length() - 1);
					}
					result += st + ";";
				}
				if (";".equals(String.valueOf(result.charAt(result.length() - 1)))) {
					result = result.substring(0, result.length()-1);
				}
			}
		}
		return result;
	}

}
