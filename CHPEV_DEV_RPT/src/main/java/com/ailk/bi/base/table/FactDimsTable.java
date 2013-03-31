package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FactDimsTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1649315435893368965L;

	// HERE IS FROM DATABASE

	// 数据表标识
	public String srctab_id = ""; // VARCHAR->String

	// 维度标识
	public String dim_id = ""; // VARCHAR->String

	// 维度数据粒度
	public String dim_level = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！
	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof FactDimsTable) {
			FactDimsTable tmpObj = (FactDimsTable) obj;
			equal = (this.srctab_id.equals(tmpObj.srctab_id) && this.dim_id
					.equals(tmpObj.dim_id));
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("fact_table=").append(this.srctab_id).append("\n");
		sb.append("dim_id=").append(this.dim_id).append("\n");
		sb.append("dim_level=").append(this.dim_level).append("\n");
		return sb.toString();
	}
}
