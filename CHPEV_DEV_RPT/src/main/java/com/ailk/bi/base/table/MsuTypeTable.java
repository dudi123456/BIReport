package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class MsuTypeTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5743094058175437190L;

	// HERE IS FROM DATABASE

	// 指标分类标识
	public String type_id = ""; // VARCHAR->String

	// 指标分类名称
	public String type_desc = ""; // VARCHAR->String

	// 是否有效
	public String is_valid = "";// VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！
	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof MsuTypeTable) {
			MsuTypeTable tmpObj = (MsuTypeTable) obj;
			equal = this.type_id.equals(tmpObj.type_id);
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("type_id=").append(this.type_id).append("\n");
		sb.append("type_desc=").append(this.type_desc).append("\n");
		sb.append("is_valid=").append(this.is_valid).append("\n");
		return super.toString();
	}
}
