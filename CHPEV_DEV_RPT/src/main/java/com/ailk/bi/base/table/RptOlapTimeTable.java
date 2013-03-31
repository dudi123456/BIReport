package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class RptOlapTimeTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8375013892337360675L;

	// HERE IS FROM DATABASE
	// 分析型报表标识
	public String report_id = ""; // NUMBER->String

	// 按年钻取
	public String by_year = ""; // CHAR->String

	// 按季度钻取
	public String by_quarter = ""; // CHAR->String

	// 按月钻取
	public String by_month = ""; // CHAR->String

	// 按旬钻取
	public String by_tendays = ""; // CHAR->String

	// 按周钻取
	public String by_week = ""; // CHAR->String

	// 按天钻取
	public String by_day = ""; // CHAR->String

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptOlapTimeTable) {
			RptOlapTimeTable tmpObj = (RptOlapTimeTable) obj;
			equal = this.report_id.equals(tmpObj.report_id);
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

		// 是否按年钻取
		sb.append(" dim_id = ").append(this.by_year).append("\n");

		// 是否按季度钻取
		sb.append(" by_quarter = ").append(this.by_quarter).append("\n");

		// 是否按月钻取
		sb.append(" by_month = ").append(this.by_month).append("\n");

		// 是否按旬钻取
		sb.append(" by_tendays = ").append(this.by_tendays).append("\n");

		// 是否按周钻取
		sb.append(" by_week = ").append(this.by_week).append("\n");

		// 是否按天钻取
		sb.append(" by_day = ").append(this.by_day).append("\n");

		return sb.toString();
	}

}
