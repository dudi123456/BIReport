package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class RptOlapDimTable extends JBTableBase {

	/**
	 * 系列号
	 */
	private static final long serialVersionUID = -8375013892337360675L;

	// HERE IS FROM DATABASE
	/**
	 * 分析型报表标识
	 */
	public String report_id = ""; // NUMBER->String

	/**
	 * 维度标识
	 */
	public String dim_id = ""; // VARCHAR->String

	// 默认是否显示
	public String default_display = ""; // CHAR->String

	/**
	 * 是否是时间维
	 */
	public String is_timedim = ""; // CHAR->String

	/**
	 * 显示顺序
	 */
	public String display_order = ""; // NUMBER->String

	/**
	 * 是否需要钻取
	 */
	public String need_dig = ""; // CHAR->String

	/**
	 * 钻取的初始层次
	 */
	public String init_level = ""; // NUMBER->String

	/**
	 * 钻取的最大层次
	 */
	public String max_level = ""; // NUMBER->String

	/**
	 * 是否可以钻到用户列表
	 */
	public String to_userlist = ""; // CHAR->String

	/**
	 * 是否是考核部门
	 */
	public String is_evaldept = "";// CHAR->String

	/**
	 * 是否是发展部门
	 */
	public String is_devdept = "";// CHAR->String

	/**
	 * 是否是业务类型维
	 */
	public String is_svcknd = "";// CHAR->String

	// 是否有效
	public String is_valid = ""; // CHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	/**
	 * 仅对时间维有效
	 */
	public RptOlapTimeTable timeLevel = null;

	/**
	 * 指标体系维度定义表
	 */
	public DimensionTable dimInfo = null;

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptOlapDimTable) {
			RptOlapDimTable tmpObj = (RptOlapDimTable) obj;
			equal = this.report_id.equals(tmpObj.report_id)
					&& this.dim_id.equals(tmpObj.dim_id);
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

		// 维度标识
		sb.append(" dim_id = ").append(this.dim_id).append("\n");

		// 默认是否显示
		sb.append(" default_display = ").append(this.default_display)
				.append("\n");

		// 是否是时间维
		sb.append(" is_timedim = ").append(this.is_timedim).append("\n");

		// 显示顺序
		sb.append(" display_order = ").append(this.display_order).append("\n");

		// 是否需要钻取
		sb.append(" need_dig = ").append(this.need_dig).append("\n");

		// 钻取初始层次
		sb.append(" init_level = ").append(this.init_level).append("\n");

		// 最大钻取层次
		sb.append(" max_level = ").append(this.max_level).append("\n");

		// 是否可以钻取到用户列表
		sb.append(" to_userlist = ").append(this.to_userlist).append("\n");

		// 是否有效
		sb.append(" is_valid = ").append(this.is_valid).append("\n");

		return sb.toString();
	}

}
