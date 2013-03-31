package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class DimTypeTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2972742449998207095L;

	// HERE IS FROM DATABASE

	// 类型标识
	public String type_id = ""; // NUMBER->String

	// 类型名称
	public String type_desc = ""; // VARCHAR->String

	// 父类型编号
	public String parent_id = ""; // VARCHAR->String

	// 是否有效
	public String is_valid = "";

	// HERE IS USER DEFINE 这一行不要删除！
	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof DimTypeTable) {
			DimTypeTable tmpObj = (DimTypeTable) obj;
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
		sb.append("parent_id=").append(this.parent_id).append("\n");
		return sb.toString();
	}

}
