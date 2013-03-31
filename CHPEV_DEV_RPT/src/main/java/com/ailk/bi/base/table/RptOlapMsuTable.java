package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class RptOlapMsuTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4441829835295368638L;

	// HERE IS FROM DATABASE
	// 分析型报表标识
	public String report_id = ""; // NUMBER->String

	// 指标标识
	public String msu_id = ""; // VARCHAR->String

	// 列显示名称
	public String col_name = ""; // VARCHAR->String

	// 默认是否显示
	public String default_display = ""; // CHAR->String

	// 显示顺序
	public String order_id = ""; // NUMBER->String

	// 是否钻取
	public String is_dig = ""; // CHAR->String

	// 钻取的最大层次
	public String dig_level = ""; // NUMBER->String

	// 是否显示累计
	public String is_sumbydate = ""; // CHAR->String

	// 是否有链接
	public String has_link = ""; // CHAR->String

	// 链接URL
	public String link_url = ""; // VARCHAR->String

	// 链接目标
	public String link_target = ""; // VARCHAR->String

	// 是否需要预警
	public String need_alert = ""; // CHAR->String

	// 预警和谁比较
	public String compare_to = ""; // NUMBER->String

	// 是否比率预警
	public String ratio_compare = ""; // CHAR->String

	// 预警上限
	public String high_value = ""; // NUMBER->String

	// 预警下限
	public String low_value = ""; // NUMBER->String

	// 预警模式
	public String alert_mode = ""; // CHAR->String

	// 超过上限颜色
	public String rise_color = ""; // CHAR->String

	// 低于下限颜色
	public String down_color = ""; // CHAR->String

	// 是否有效
	public String is_valid = ""; // CHAR->String

	public MeasureTable msuInfo = null;

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptOlapMsuTable) {
			RptOlapMsuTable tmpObj = (RptOlapMsuTable) obj;
			equal = this.report_id.equals(tmpObj.report_id)
					&& this.msu_id.equals(tmpObj.msu_id);
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

		// 指标标识
		sb.append(" msu_id = ").append(this.msu_id).append("\n");

		// 列显示名称
		sb.append(" col_name = ").append(this.col_name).append("\n");

		// 默认是否显示
		sb.append(" default_display = ").append(this.default_display)
				.append("\n");

		// 显示顺序
		sb.append(" order_id = ").append(this.order_id).append("\n");

		// 是否钻取
		sb.append(" is_dig = ").append(this.is_dig).append("\n");

		// 钻取最大层次
		sb.append(" dig_level = ").append(this.dig_level).append("\n");

		// 是否有链接
		sb.append(" has_link = ").append(this.has_link).append("\n");

		// 链接地址
		sb.append(" link_url = ").append(this.link_url).append("\n");

		// 链接目标
		sb.append(" link_target = ").append(this.link_target).append("\n");

		// 是否需要预警
		sb.append(" need_alert = ").append(this.need_alert).append("\n");

		// 和谁比较
		sb.append(" compare_to = ").append(this.compare_to).append("\n");

		// 是否比率值
		sb.append(" ratio_compare = ").append(this.ratio_compare).append("\n");

		// 预警上限
		sb.append(" high_value = ").append(this.high_value).append("\n");

		// 预警下限
		sb.append(" low_value = ").append(this.low_value).append("\n");

		// 预警模式
		sb.append(" alert_mode = ").append(this.alert_mode).append("\n");

		// 超过上限颜色
		sb.append(" rise_color = ").append(this.rise_color).append("\n");

		// 低于下限颜色
		sb.append(" down_color = ").append(this.down_color).append("\n");

		// 是否有效
		sb.append(" is_valid = ").append(this.is_valid).append("\n");

		return sb.toString();
	}

}
