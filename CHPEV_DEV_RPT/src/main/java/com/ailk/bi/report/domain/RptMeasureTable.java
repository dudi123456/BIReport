package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class RptMeasureTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id = ""; // VARCHAR->String

	public String parent_id = ""; // VARCHAR->String

	public String ismeasure = ""; // VARCHAR->String

	public String code = ""; // VARCHAR->String

	public String name = ""; // VARCHAR->String

	public String unit = ""; // VARCHAR->String

	public String cycle = ""; // VARCHAR->String

	public String digit_len = ""; // VARCHAR->String

	public String data_src = ""; // VARCHAR->String

	// public String base_flag = ""; // VARCHAR->String

	public String istoleader = ""; // VARCHAR->String

	public String descr = ""; // VARCHAR->String
	public String type = ""; // VARCHAR->String

	public String type_desc = ""; // VARCHAR->String

	public String data_sql = ""; // VARCHAR->String

	public String note1 = ""; // VARCHAR->String

	public String note2 = ""; // VARCHAR->String

	public String note3 = ""; // VARCHAR->String

	public String note4 = ""; // VARCHAR->String

	public String measure_value = ""; // VARCHAR->String

	public String explain = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	/**
	 * 生成查询定义对象
	 * 
	 * @param svces
	 * @return
	 */
	/*
	 * public static RptMeasureTable genReportMeasureFromArray(String[] svces) {
	 * if (null == svces) throw new IllegalArgumentException("对象数组为空");
	 * RptMeasureTable measureTable = new RptMeasureTable(); int m = 0;
	 * measureTable.id = svces[m++]; measureTable.parent_id = svces[m++];
	 * measureTable.ismeasure = svces[m++]; measureTable.code = svces[m++];
	 * measureTable.name = svces[m++]; measureTable.unit = svces[m++];
	 * measureTable.cycle = svces[m++]; measureTable.digit_len = svces[m++];
	 * measureTable.data_src = svces[m++]; measureTable.base_flag = svces[m++];
	 * measureTable.istoleader = svces[m++]; measureTable.descr = svces[m++];
	 * return measureTable; }
	 */
	public static RptMeasureTable genReportMeasureFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptMeasureTable measureTable = new RptMeasureTable();
		int m = 0;
		measureTable.id = svces[m++];
		measureTable.parent_id = svces[m++];
		measureTable.ismeasure = svces[m++];
		measureTable.code = svces[m++];
		measureTable.name = svces[m++];
		measureTable.explain = svces[m++];
		measureTable.type = svces[m++];
		if ("0".equals(measureTable.type)) {
			measureTable.type_desc = "时点数";
		} else if ("1".equals(measureTable.type)) {
			measureTable.type_desc = "时期数";
		} else if ("2".equals(measureTable.type)) {
			measureTable.type_desc = "时期时点数";
		}
		measureTable.unit = svces[m++];
		measureTable.digit_len = svces[m++];
		measureTable.data_src = svces[m++];
		measureTable.data_sql = svces[m++];
		measureTable.istoleader = svces[m++];
		measureTable.note1 = svces[m++];
		measureTable.note2 = svces[m++];
		measureTable.note3 = svces[m++];
		measureTable.note4 = svces[m++];
		measureTable.descr = svces[m++];
		return measureTable;
	}

	/**
	 * 生成查询定义对象
	 * 
	 * @param svces
	 * @return
	 */
	public static RptMeasureTable genReportMeasureValueFromArray(String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptMeasureTable measureTable = new RptMeasureTable();
		int m = 0;
		measureTable.id = svces[m++];
		measureTable.ismeasure = svces[m++];
		measureTable.code = svces[m++];
		measureTable.name = svces[m++];
		measureTable.explain = svces[m++];
		measureTable.type = svces[m++];
		if ("0".equals(measureTable.type)) {
			measureTable.type_desc = "时点数";
		} else if ("1".equals(measureTable.type)) {
			measureTable.type_desc = "时期数";
		} else if ("2".equals(measureTable.type)) {
			measureTable.type_desc = "时期时点数";
		}
		measureTable.unit = svces[m++];
		measureTable.digit_len = svces[m++];
		measureTable.data_src = svces[m++];
		// measureTable.data_sql = svces[m++];
		measureTable.istoleader = svces[m++];
		measureTable.note1 = svces[m++];
		measureTable.note2 = svces[m++];
		measureTable.note3 = svces[m++];
		measureTable.note4 = svces[m++];
		measureTable.descr = svces[m++];
		measureTable.measure_value = svces[m++];
		return measureTable;
	}

}
