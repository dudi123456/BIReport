package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class SubjectCommTabHead extends JBTableBase {

	private static final long serialVersionUID = -165990643371124378L;

	/**
	 * 表格标识
	 */
	public String table_id = "";

	/**
	 * 表头右侧HTML字符串,带有
	 * <tr>
	 * </tr>
	 */
	public String table_head = "";

	/**
	 * 表头的跨行数
	 */
	public String row_span = "";

	/**
	 * 纵转横时左侧表头，这个没办法生成
	 * 
	 */
	public String row_head_swap_header = "";

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof SubjectCommTabHead))
			return false;
		SubjectCommTabHead tmpObj = (SubjectCommTabHead) obj;
		return this.table_id.equals(tmpObj.table_id)
				&& this.table_head.equals(tmpObj.table_head);
	}

	public int hashCode() {
		return this.table_id.hashCode() + this.table_head.hashCode()
				+ super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 报表标识
		sb.append(" table_id = ").append(this.table_id).append("\n");

		// 报表表头
		sb.append(" table_head = ").append(this.table_head).append("\n");

		return sb.toString();
	}

}
