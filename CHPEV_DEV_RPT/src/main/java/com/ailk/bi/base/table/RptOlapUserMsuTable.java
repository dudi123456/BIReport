package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class RptOlapUserMsuTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4441829835295368638L;

	// HERE IS FROM DATABASE
	// 自定义分析型报表标识
	public String custom_rptid = ""; // NUMBER->String

	// 指标标识
	public String msu_id = ""; // VARCHAR->String

	// 显示顺序
	public String display_order = ""; // VARCHAR->String

	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof RptOlapUserMsuTable) {
			RptOlapUserMsuTable tmpObj = (RptOlapUserMsuTable) obj;
			equal = this.custom_rptid.equals(tmpObj.custom_rptid)
					&& this.msu_id.equals(tmpObj.msu_id);
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 自定义分析型报表标识
		sb.append(" custom_rptid = ").append(this.custom_rptid).append("\n");

		// 指标标识
		sb.append(" msu_id = ").append(this.msu_id).append("\n");

		// 显示顺序
		sb.append(" display_order = ").append(this.display_order).append("\n");

		return sb.toString();
	}

}
