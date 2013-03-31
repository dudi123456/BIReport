package com.ailk.bi.report.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportFilterDao;
import com.ailk.bi.report.domain.RptFilterTable;
import com.ailk.bi.report.util.ReportConsts;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LReportFilterDao implements ILReportFilterDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportFilterDao#getReportFilter(java.lang.String)
	 */
	public List getReportFilter(String rpt_id) throws AppException {
		List reportFilters = null;
		if (rpt_id == null || "".equals(rpt_id))
			throw new AppException();
		String strSql = SQLGenator.genSQL("Q3155", rpt_id);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			reportFilters = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptFilterTable reportFilter = RptFilterTable
						.genReportFilterFromArray(svces[i]);
				reportFilters.add(reportFilter);
			}
		}
		return reportFilters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportFilterDao#getReportFilterDefine(java.lang
	 * .String)
	 */
	public List getReportFilterDefine(String rpt_id) throws AppException {
		List reportFilters = null;
		if (rpt_id == null || "".equals(rpt_id))
			throw new AppException();
		String strSql = SQLGenator.genSQL("Q3465", rpt_id);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			reportFilters = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptFilterTable reportFilter = RptFilterTable
						.genReportFilterDefineFromArray(svces[i]);
				reportFilters.add(reportFilter);
			}
		}
		return reportFilters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportFilterDao#insertReportFilter(java.util.
	 * List)
	 */
	public void insertReportFilter(List rptFilterTable) throws AppException {
		if (rptFilterTable == null || rptFilterTable.size() == 0) {
			return;
		}

		List tmpSQL = new ArrayList();
		Iterator iter = rptFilterTable.iterator();
		while (iter.hasNext()) {
			RptFilterTable filterTable = (RptFilterTable) iter.next();
			List svc = new ArrayList();
			svc.add(filterTable.rpt_id);
			svc.add(filterTable.col_sequence);
			svc.add(filterTable.display_sequence);
			svc.add(filterTable.field_title);
			svc.add(filterTable.filter_type);
			svc.add(StringB.replace(filterTable.filter_script, "'",
					"'||chr(39)||'"));
			svc.add(filterTable.filter_datasource);
			svc.add(StringB.replace(filterTable.filter_sql, "'",
					"'||chr(39)||'"));
			svc.add(filterTable.filter_all);
			svc.add(filterTable.filter_default);
			svc.add(filterTable.status);

			String[] param = (String[]) svc.toArray(new String[svc.size()]);
			String strSql = SQLGenator.genSQL("I3466", param);
			System.out.println("strSql=" + strSql);
			tmpSQL.add(strSql);

			svc = new ArrayList();
			svc.add(filterTable.rpt_id);
			svc.add(ReportConsts.CONDITION_PUB);
			svc.add(ReportConsts.CONDITION_PUB);
			if (StringTool.checkEmptyString(filterTable.filter_script)) {
				svc.add("dim" + filterTable.display_sequence);
			} else {
				String[] tmpArr = filterTable.filter_script.split("'");
				String tmp_qry_code = "dim" + filterTable.display_sequence;
				for (int i = 0; tmpArr != null && i < tmpArr.length; i++) {
					if (tmpArr[i] != null && tmpArr[i].indexOf("qry__") >= 0) {
						tmp_qry_code = tmpArr[i];
						break;
					}
				}
				svc.add(StringB.replace(tmp_qry_code, "qry__", ""));
			}
			svc.add(filterTable.field_dim_code);
			svc.add(filterTable.data_type);
			svc.add(filterTable.con_tag);
			svc.add(filterTable.display_sequence);
			svc.add(filterTable.status);

			param = (String[]) svc.toArray(new String[svc.size()]);
			strSql = SQLGenator.genSQL("I3467", param);
			System.out.println("strSql=" + strSql);
			tmpSQL.add(strSql);
		}

		String[] strSql = (String[]) tmpSQL.toArray(new String[tmpSQL.size()]);
		WebDBUtil.execTransUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.ILReportFilterDao#delReportFilter(java.lang.String)
	 */
	public void delReportFilter(String rpt_id) throws AppException {
		List tmpSQL = new ArrayList();
		tmpSQL.add(SQLGenator.genSQL("D3468", rpt_id));
		System.out.println("strSql=" + SQLGenator.genSQL("D3468", rpt_id));
		tmpSQL.add(SQLGenator.genSQL("D3469", rpt_id));
		System.out.println("strSql=" + SQLGenator.genSQL("D3469", rpt_id));
		String[] strSql = (String[]) tmpSQL.toArray(new String[tmpSQL.size()]);
		WebDBUtil.execTransUpdate(strSql);
	}
}
