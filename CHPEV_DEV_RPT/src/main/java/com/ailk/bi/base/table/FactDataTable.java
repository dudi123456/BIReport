package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FactDataTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9093067387646316088L;

	// HERE IS FROM DATABASE

	// 数据表标识
	public String srctab_id = ""; // VARCHAR->String

	// 数据表描述的简短名称
	public String srctab_name = ""; // VARCHAR->String

	// 数据事实表名
	public String datatab_name = ""; // VARCHAR->String

	// 数据源说明
	public String srctab_desc = ""; // VARCHAR->String

	// 数据表用途
	public String used_for = ""; // NUMBER->String

	// 统计周期
	public String static_period = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！
	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof FactDataTable) {
			FactDataTable tmpObj = (FactDataTable) obj;
			equal = (this.srctab_id.equals(tmpObj.srctab_id));
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("srctab_id=").append(this.srctab_id).append("\n");
		sb.append("srctab_name=").append(this.srctab_name).append("\n");
		sb.append("datatab_name=").append(this.datatab_name).append("\n");
		sb.append("srctab_desc=").append(this.srctab_desc).append("\n");
		sb.append("used_for=").append(this.used_for).append("\n");
		sb.append("static_period=").append(this.static_period).append("\n");
		return sb.toString();
	}
}
