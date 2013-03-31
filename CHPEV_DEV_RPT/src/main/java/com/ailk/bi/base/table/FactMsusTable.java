package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FactMsusTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5559866108456478277L;

	// HERE IS FROM DATABASE

	// 数据表名称
	public String srctab_id = ""; // VARCHAR->String

	// 指标标识
	public String msu_id = ""; // VARCHAR->String

	// 数据表用途
	public String msu_target = ""; // NUMBER->String

	// 统计周期 日 月
	public String stat_period = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！
	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof FactMsusTable) {
			FactMsusTable tmpObj = (FactMsusTable) obj;
			equal = (this.msu_id.equals(tmpObj.msu_id)
					&& this.srctab_id.equals(tmpObj.srctab_id)
					&& this.msu_target.equals(tmpObj.msu_target) && this.stat_period
					.equals(tmpObj.stat_period));
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("msu_id=").append(this.msu_id).append("\n");
		sb.append("fact_table=").append(this.srctab_id).append("\n");
		sb.append("msu_target=").append(this.msu_target).append("\n");
		sb.append("stat_period=").append(this.stat_period).append("\n");
		return super.toString();
	}
}
