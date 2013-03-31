package com.ailk.bi.report.domain;

import com.ailk.bi.common.event.JBTableBase;

public class RptMeasureShowTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String report_id = ""; // VARCHAR->String

	public String code = ""; // VARCHAR->String

	public String name = ""; // VARCHAR->String

	public String sequence = ""; // VARCHAR->String

	/**
	 * 生成查询定义对象
	 * 
	 * @param svces
	 * @return
	 */
	public static RptMeasureShowTable genReportMeasureShowFromArray(
			String[] svces) {
		if (null == svces)
			throw new IllegalArgumentException("对象数组为空");
		RptMeasureShowTable showTable = new RptMeasureShowTable();
		int m = 0;
		showTable.report_id = svces[m++];
		showTable.code = svces[m++];
		showTable.name = svces[m++];
		return showTable;
	}

}
