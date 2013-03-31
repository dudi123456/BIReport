package com.ailk.bi.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.IMReportDataDao;
import com.ailk.bi.report.domain.RptMeasureShowTable;
import com.ailk.bi.report.domain.RptMeasureTable;
import com.ailk.bi.report.struct.DimRuleStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MReportDataDao implements IMReportDataDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.IMReportDataDao#getReportMeasure(java.lang.String,
	 * java.lang.String)
	 */
	public List getMeasure(String rpt_id, String roleStr) {
		List rptMeasure = null;
		String svces[][] = null;
		try {
			String sql = SQLGenator.genSQL("Q3100", rpt_id);
			// System.out.println("===== Q3100 sql =====" + sql);
			svces = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex) {
			System.out.print("获取指定报表具有的指标失败！");
		}
		if (null != svces) {
			rptMeasure = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptMeasureTable measureTable = RptMeasureTable
						.genReportMeasureFromArray(svces[i]);
				rptMeasure.add(measureTable);
			}
		}
		return rptMeasure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.IMReportDataDao#getMeasureData(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	/*
	 * public String[][] getMeasureData(String rpt_id, String region_id, String
	 * date_s, String date_e) { String[][] measuredata = null; try { String
	 * strSql = SQLGenator.genSQL("Q3105", rpt_id, region_id, date_s, date_e);
	 * System.out.println("measure report data=" + strSql); measuredata =
	 * WebDBUtil.execQryArray(strSql, ""); } catch (AppException ex) {
	 * ex.printStackTrace(); } return measuredata; }
	 */
	public List getMeasureData(String rpt_id, String region_id, String date_s,
			String date_e) {
		List rptMeasureData = null;
		String[][] svces = null;
		try {
			String strSql = SQLGenator.genSQL("Q3105", rpt_id, region_id,
					date_s, date_e);
			// System.out.println("measure report data=" + strSql);
			svces = WebDBUtil.execQryArray(strSql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		if (null != svces) {
			rptMeasureData = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptMeasureTable measureTable = RptMeasureTable
						.genReportMeasureValueFromArray(svces[i]);
				rptMeasureData.add(measureTable);
			}
		}
		return rptMeasureData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.report.dao.IMReportDataDao#getMeasureRegionData(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	public String[][] getMeasureDimData(String rptid, String measure,
			String dimValue, String rpt_date, String region_id,
			DimRuleStruct dimInfo) {
		String result[][] = null;
		try {
			String strSql = "";
			String whereStr = " and date_id = " + rpt_date;
			if (!"".equals(dimValue) && dimValue.length() > 0) {
				if (dimInfo == null) {
					// strSql = SQLGenator.genSQL("Q3103",rptid,"REGION_ID"
					// ,rpt_date);
					whereStr += " AND REGION_ID IN(" + dimValue + ") ";
				} else {
					// strSql =
					// SQLGenator.genSQL("Q3103",rptid,dimInfo.getDim_field().toUpperCase()
					// ,rpt_date);
					whereStr += " AND " + dimInfo.getDim_field() + " IN("
							+ dimValue + ") ";
					if (null != region_id
							&& !"".equals(region_id)
							&& region_id.length() > 0
							&& !dimInfo.getDim_field().toUpperCase()
									.equals("REGION_ID")) {
						whereStr += " AND REGION_ID  = " + region_id + "";
					}
				}

			}
			if (!"".equals(measure) && measure.length() > 0) {
				String tmpSql = SQLGenator.genSQL("Q3100X", rptid);
				whereStr += " AND MEASURE_ID IN(" + tmpSql + ")";
			}

			//
			if (!"".equals(dimValue) && dimValue.length() > 0) {
				if (dimInfo == null) {
					strSql = SQLGenator.genSQL("Q3103", rptid, "REGION_ID",
							whereStr);

				} else {
					strSql = SQLGenator.genSQL("Q3103", rptid, dimInfo
							.getDim_field().toUpperCase(), whereStr);

				}

			}

			// System.out.println("getMeasureDimData============" + strSql);
			result = WebDBUtil.execQryArray(strSql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public List getMeasureShow(String rpt_id) {
		List rptShow = null;
		String svces[][] = null;
		try {
			String sql = SQLGenator.genSQL("Q3101", rpt_id);
			// System.out.println("===== Q3101 sql =====" + sql);
			svces = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex) {
			System.out.print("获取指定报表具有的指标失败！");
		}
		if (null != svces) {
			rptShow = new ArrayList();
			for (int i = 0; i < svces.length; i++) {
				RptMeasureShowTable showTable = RptMeasureShowTable
						.genReportMeasureShowFromArray(svces[i]);
				rptShow.add(showTable);
			}
		}
		return rptShow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asiabi.report.dao.IMReportDataDao#getRegion(java.lang.String,
	 * java.lang.String)
	 */
	public String[][] getRegion(String region_id, String roleStr) {
		String svces[][] = null;
		try {
			String strSql = SQLGenator.genSQL("Q3102");
			svces = WebDBUtil.execQryArray(strSql, "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return svces;
	}

	public DimRuleStruct getReportDim(String report_id) {

		DimRuleStruct struct = null;

		try {
			String strSql = SQLGenator.genSQL("Q3110", report_id);
			// System.out.println("Q3110============"+strSql);
			//
			String svces[][] = WebDBUtil.execQryArray(strSql, "");
			if (svces != null && svces.length > 0) {
				// DIM_FIELD,DIM_SRC_SQL,DIM_NAME,DIM_RULE,TOP_HEAD

				struct = new DimRuleStruct();
				struct.setReport_id(report_id);
				struct.setDim_field(svces[0][0]);
				struct.setDim_src_sql(svces[0][1]);
				struct.setDim_name(svces[0][2]);
				struct.setTop_head(svces[0][4]);
				struct.setDim_rule(svces[0][3]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return struct;

	}

	public String[][] getReportDimValue(DimRuleStruct dimInfo) {

		// DIM_FIELD,DIM_SRC_SQL,DIM_RULE,TOP_HEAD_FLAG

		String svces[][] = null;
		try {
			//
			// System.out.println("getReportDimValue============="+dimInfo.getDim_src_sql());
			svces = WebDBUtil.execQryArray(dimInfo.getDim_src_sql(), "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return svces;
	}

	public String[][] getMeasureHeadRule(String rpt_id) {
		String svces[][] = null;
		try {
			String strSql = SQLGenator.genSQL("Q3333", rpt_id);
			svces = WebDBUtil.execQryArray(strSql, "");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return svces;
	}

	public String[][] getExpand(String rpt_id) {
		String svces[][] = null;
		try {
			String strSql = SQLGenator.genSQL("Q3102E", rpt_id);
			// System.out.println("===== Q3102E sql =====" + strSql);
			svces = WebDBUtil.execQryArray(strSql, "");

			if (svces != null && svces.length > 0) {
				strSql = svces[0][0];
				svces = WebDBUtil.execQryArray(strSql, "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return svces;
	}

}
