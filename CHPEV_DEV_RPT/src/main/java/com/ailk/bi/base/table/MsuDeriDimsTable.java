package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class MsuDeriDimsTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1437114569923409704L;

	// HERE IS FROM DATABASE

	// 指标标识
	public String msu_id = ""; // VARCHAR->String

	// 数据源标识
	public String srctab_id = "";// VARCHAR->String

	// 指标衍生时横向的层次标识，从1开始，按1递增
	public String hlvl_id = ""; // NUMBER->String

	// 相关的维度标识
	public String dim_id = ""; // NUMBER->String

	// 数据表维度数据最细粒度的层次标识
	public String vlvl_id = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！
	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof MsuDeriDimsTable) {
			MsuDeriDimsTable tmpObj = (MsuDeriDimsTable) obj;
			equal = (this.msu_id.equals(tmpObj.msu_id)
					&& this.hlvl_id.equals(tmpObj.hlvl_id)
					&& this.srctab_id.equals(tmpObj.srctab_id) && this.vlvl_id
					.equals(tmpObj.vlvl_id));
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 指标标识
		sb.append(" msu_id = ").append(this.msu_id).append("\n");
		sb.append(" srctab_id = ").append(this.srctab_id).append("\n");

		// 指标横向层次标识
		sb.append(" hlvl_id = ").append(this.hlvl_id).append("\n");

		// 指标维度
		sb.append(" dim_id = ").append(this.dim_id).append("\n");

		// 维度层次标识
		sb.append(" vlvl_id = ").append(this.vlvl_id).append("\n");

		return sb.toString();
	}

}
