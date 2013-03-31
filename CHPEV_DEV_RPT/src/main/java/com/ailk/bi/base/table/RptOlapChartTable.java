package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class RptOlapChartTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4441829835295368638L;

	// HERE IS FROM DATABASE
	// 分析型报表标识
	public String report_id = ""; // NUMBER->String

	// 图形类型
	public String chart_type = ""; // VARCHAR->String

	// 图形属性
	public String attr_id = ""; // VARCHAR->String

	// 维度或指标标识
	public String dim_msu_id = ""; // CHAR->String

	// 使用所有的维度值
	public String all_dim_values = ""; // CHAR->String

	public RptOlapChartTimeTable chartTime = null;

	/**
	 * 表示维度值的维度层次
	 */
	public String dim_level = "";// NUMBER->String

	// 维度值
	public String dim_value = ""; // CHAR->String

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptOlapChartTable) {
			RptOlapChartTable tmpObj = (RptOlapChartTable) obj;
			equal = this.report_id.equals(tmpObj.report_id)
					&& this.chart_type.equals(tmpObj.chart_type)
					&& this.attr_id.equals(tmpObj.attr_id)
					&& this.dim_msu_id.equals(tmpObj.dim_msu_id);
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

		// 图形要素，观察维度、条件维度、观察指标
		sb.append(" attr_id = ").append(this.attr_id).append("\n");

		// 维度或指标标识
		sb.append(" dim_msu_id = ").append(this.dim_msu_id).append("\n");

		// 是否使用所有维度值
		sb.append(" all_dim_values = ").append(this.all_dim_values)
				.append("\n");
		sb.append(" dim_level = ").append(this.dim_level).append("\n");
		// 维度值
		sb.append(" dim_value = ").append(this.dim_value).append("\n");

		return sb.toString();
	}

}
