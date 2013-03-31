package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class RptOlapChartTimeTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4441829835295368638L;

	// HERE IS FROM DATABASE
	// 分析型报表标识
	public String report_id = ""; // NUMBER->String

	// 图形类型
	public String chart_type = ""; // VARCHAR->String

	// 有数据先前天数
	public String time_due = ""; // NUMBER->String

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptOlapChartTimeTable) {
			RptOlapChartTimeTable tmpObj = (RptOlapChartTimeTable) obj;
			equal = this.report_id.equals(tmpObj.report_id)
					&& this.chart_type.equals(tmpObj.chart_type);
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 报表标识
		sb.append(" report_id = ").append(this.report_id).append("\n");
		// 图形类型
		sb.append(" chart_type = ").append(this.chart_type).append("\n");
		// 前推天数
		sb.append(" time_due = ").append(this.time_due).append("\n");

		return sb.toString();
	}

}
