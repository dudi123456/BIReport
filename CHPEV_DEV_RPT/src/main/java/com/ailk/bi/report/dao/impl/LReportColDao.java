package com.ailk.bi.report.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.ailk.bi.base.table.DimensionTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportColDao;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.util.ReportConsts;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class LReportColDao implements ILReportColDao {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportColDao#getReportCol(java.lang.String,
	 * java.lang.String)
	 */
	public List getReportCol(String rpt_id, String expandcol) throws AppException {
		if (rpt_id == null || "".equals(rpt_id))
			throw new AppException();
		String strSql = SQLGenator.genSQL("Q3150", rpt_id);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (svces == null || svces.length <= 0) {
			return null;
		}

		List reportCols = new ArrayList();
		List tmpCols = new ArrayList();
		for (int i = 0; i < svces.length; i++) {
			RptColDictTable reportCol = RptColDictTable.genReportColFromArray(svces[i]);
			tmpCols.add(reportCol);
		}

		if (!StringTool.checkEmptyString(expandcol) && expandcol.length() == 2) {
			String direction = expandcol.substring(0, 1);
			int expcol = Integer.parseInt(expandcol.substring(1));
			boolean changeYes = true;

			Iterator iter = tmpCols.iterator();
			while (iter.hasNext()) {
				RptColDictTable dict = (RptColDictTable) iter.next();
				int col_sequence = Integer.parseInt(dict.col_sequence);
				if (ReportConsts.DIRECTION_DOWN.equals(direction)) {
					if (ReportConsts.YES.equals(dict.is_expand_col)
							&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
						if (col_sequence > expcol) {
							if (changeYes) {
								dict.default_display = ReportConsts.YES;
								changeYes = false;
							} else {
								dict.default_display = ReportConsts.NO;
							}
						} else {
							dict.default_display = ReportConsts.YES;
						}
					}
				} else if (ReportConsts.DIRECTION_UP.equals(direction)) {
					if (ReportConsts.YES.equals(dict.is_expand_col)
							&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
						if (col_sequence < expcol) {
							dict.default_display = ReportConsts.YES;
						} else if (col_sequence >= expcol) {
							dict.default_display = ReportConsts.NO;
						}
					}
				}
				reportCols.add(dict);
			}
		} else {
			reportCols = tmpCols;
		}
		return reportCols;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.dao.ILReportColDao#getReportColDefine(java.lang.String)
	 */
	public List getReportColDefine(String rpt_id) throws AppException {
		List rptColTables = null;
		if (rpt_id == null || "".equals(rpt_id))
			throw new AppException();
		String strSql = SQLGenator.genSQL("Q3460", rpt_id);
		String[][] svces = WebDBUtil.execQryArray(strSql, "");
		if (null != svces) {
			rptColTables = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptColDictTable rptColTable = RptColDictTable.genReportColFromArray(svces[i]);
				rptColTables.add(rptColTable);
			}
		}
		return rptColTables;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.dao.ILReportColDao#getCustomRptDimTable(java.lang.String
	 * )
	 */
	public DimensionTable[] getCustomRptDimTable(String rpt_id) throws AppException {
		if (rpt_id == null || "".equals(rpt_id) || "0".equals(rpt_id))
			return null;

		String strSql = SQLGenator.genSQL("Q3475", rpt_id);
		String[][] tmp = WebDBUtil.execQryArray(strSql, "");
		int iCount = 0;
		String SRCTAB_ID = "";
		if (tmp != null && tmp.length > 0) {
			iCount = tmp.length;
		}

		DimensionTable[] customRptDims = null;
		if (iCount == 0) {
			return null;
		} else if (iCount == 1) {
			SRCTAB_ID = tmp[0][0];
			strSql = SQLGenator.genSQL("Q3476", SRCTAB_ID);
		} else {
			SRCTAB_ID = StringTool.changArrToStrComma(tmp, 0);
			strSql = SQLGenator.genSQL("Q3477", SRCTAB_ID, SRCTAB_ID);
		}
		System.out.println("Sql Q3475==>" + strSql);
		Vector temp = WebDBUtil.execQryVector(strSql, "");
		if (temp != null && temp.size() > 0) {
			customRptDims = new DimensionTable[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				Vector tempv = (Vector) temp.get(i);
				customRptDims[i] = new DimensionTable();
				int m = 0;
				customRptDims[i].dim_id = (String) tempv.get(m++);
				customRptDims[i].dim_name = (String) tempv.get(m++);
				customRptDims[i].dim_desc = (String) tempv.get(m++);
				customRptDims[i].dim_type = (String) tempv.get(m++);
				customRptDims[i].dim_table = (String) tempv.get(m++);
				customRptDims[i].code_idfld = (String) tempv.get(m++);
				customRptDims[i].idfld_type = (String) tempv.get(m++);
				customRptDims[i].code_descfld = (String) tempv.get(m++);
				customRptDims[i].dim_unit = (String) tempv.get(m++);
				customRptDims[i].is_deptdim = (String) tempv.get(m++);
				customRptDims[i].to_userlvl = (String) tempv.get(m++);
			}
		}
		return customRptDims;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportColDao#insertReportCol(java.util.List)
	 */
	public void insertReportCol(List rptColTable) throws AppException {
		if (rptColTable == null || rptColTable.size() == 0) {
			return;
		}
		String rpt_id = "";
		List tmpSQL = new ArrayList();
		Iterator iter = rptColTable.iterator();
		int mm = 1;
		while (iter.hasNext()) {
			RptColDictTable dictTable = (RptColDictTable) iter.next();
			List svc = new ArrayList();
			svc.add(dictTable.rpt_id);
			svc.add(dictTable.col_sequence);
			svc.add(dictTable.default_display);
			svc.add(dictTable.dim_code_display);
			svc.add(dictTable.is_expand_col);
			svc.add(dictTable.is_subsum);
			svc.add(dictTable.valuable_sum);
			svc.add(dictTable.field_dim_code);
			/*
			 * if("DATA_CHAR".equals(dictTable.field_code.substring(0,9))){
			 * svc.add("DATA_INT"+mm); mm++; }else { svc.add(""); }
			 */

			svc.add(dictTable.field_code);
			svc.add(StringB.replace(dictTable.field_title, "'", "'||chr(39)||'"));
			svc.add(dictTable.data_type);
			svc.add(dictTable.msu_length);
			svc.add(dictTable.msu_unit);
			svc.add(dictTable.comma_splitted);
			svc.add(dictTable.zero_proc);
			svc.add(dictTable.ratio_displayed);
			svc.add(dictTable.has_comratio);
			svc.add(dictTable.has_same);
			svc.add(dictTable.has_last);
			svc.add(dictTable.has_link);
			svc.add(dictTable.link_url);
			svc.add(dictTable.link_target);
			svc.add(dictTable.data_order);
			svc.add(dictTable.sms_flag);
			svc.add(dictTable.td_wrap);
			svc.add(dictTable.title_style);
			svc.add(dictTable.col_style);
			svc.add(dictTable.print_title_style);
			svc.add(dictTable.print_col_style);
			svc.add(dictTable.need_alert);
			svc.add(dictTable.compare_to);
			svc.add(dictTable.ratio_compare);
			svc.add(dictTable.high_value);
			svc.add(dictTable.low_value);
			svc.add(dictTable.alert_mode);
			svc.add(dictTable.rise_color);
			svc.add(dictTable.down_color);
			svc.add(dictTable.is_msu);
			svc.add(dictTable.is_user_msu);
			svc.add(dictTable.msu_id);
			svc.add(StringB.replace(dictTable.datatable, "'", "'||chr(39)||'"));
			svc.add(dictTable.status);

			String[] param = null;
			if (svc != null && svc.size() >= 0) {
				param = (String[]) svc.toArray(new String[svc.size()]);
			}
			String strSql = SQLGenator.genSQL("I3461", param);
			// System.out.println("strSql=" + strSql);
			tmpSQL.add(strSql);

			if (ReportConsts.YES.equals(dictTable.has_link)) {
				svc = new ArrayList();
				svc.add(dictTable.rpt_id);
				svc.add("0");
				svc.add("1");
				svc.add(dictTable.field_dim_code);
				svc.add(dictTable.field_dim_code);
				svc.add("2");
				svc.add("=");
				svc.add("9");
				svc.add("Y");
				param = null;
				if (svc != null && svc.size() >= 0) {
					param = (String[]) svc.toArray(new String[svc.size()]);
				}
				strSql = SQLGenator.genSQL("I3467", param);
				System.out.println("strSql=" + strSql);
				tmpSQL.add(strSql);
			}
			rpt_id = dictTable.rpt_id;

		}
		System.out.println("end");
		String delSql = SQLGenator.genSQL("D3469A", rpt_id);
		System.out.println("delSql=" + delSql);
		WebDBUtil.execUpdate(delSql);
		String[] strSql = (String[]) tmpSQL.toArray(new String[tmpSQL.size()]);
		WebDBUtil.execTransUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.dao.ILReportColDao#delReportCol(java.lang.String)
	 */
	public void delReportCol(String rpt_id) throws AppException {
		String strSql = SQLGenator.genSQL("D3463", rpt_id);
		WebDBUtil.execUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.dao.ILReportColDao#delReportNumberCol(java.lang.String)
	 */
	public void delReportNumberCol(String rpt_id) throws AppException {
		String strSql = SQLGenator.genSQL("D3463", rpt_id);
		strSql += " AND DATA_TYPE=" + ReportConsts.DATA_TYPE_NUMBER;
		WebDBUtil.execUpdate(strSql);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.dao.ILReportColDao#delReportCharCol(java.lang.String)
	 */
	public void delReportCharCol(String rpt_id) throws AppException {
		String strSql = SQLGenator.genSQL("D3463", rpt_id);
		strSql += " AND DATA_TYPE=" + ReportConsts.DATA_TYPE_STRING;
		WebDBUtil.execUpdate(strSql);
	}
}
