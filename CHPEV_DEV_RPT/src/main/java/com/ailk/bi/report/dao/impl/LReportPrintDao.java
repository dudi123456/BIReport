package com.ailk.bi.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportPrintDao;
import com.ailk.bi.report.domain.RptResourceTable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LReportPrintDao implements ILReportPrintDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportPrintDao#getReportPrint(java.lang.String)
	 */
	public List getReportPrint(String whereStr) throws AppException {
		List reports = null;
		String strSql = SQLGenator.genSQL("Q3341");
		strSql += whereStr;
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			reports = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptResourceTable report = RptResourceTable
						.genReportPrintFromArray(svces[i]);
				reports.add(report);
			}
		}
		return reports;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportPrintDao#insertReportPrint(java.lang.Object
	 * )
	 */
	public void insertReportPrint(Object rptTable) throws AppException {
		RptResourceTable report = (RptResourceTable) rptTable;
		List svc = new ArrayList();
		svc.add(report.print_code);
		svc.add(report.print_time);
		svc.add(report.rpt_id);
		svc.add(report.print_userid);
		svc.add(report.print_username);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}
		String strSql = SQLGenator.genSQL("I3340", param);
		WebDBUtil.execUpdate(strSql);
	}

}
