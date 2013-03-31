package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class DimLevelTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3733109254883805268L;

	// HERE IS FROM DATABASE
	// 维度标识
	public String dim_id = ""; // NUMBER->String

	// 层次标识，由一开始，按1递增

	public String lvl_id = ""; // NUMBER->String

	// 层次名称
	public String lvl_name = ""; // VARCHAR->String

	// 层次描述
	public String lvl_desc = ""; // NUMBER->String

	// 层次所在的维表
	public String lvl_table = ""; // VARCHAR->String

	// 上层维度的编码字段
	public String parent_idfld = ""; // NUMBER->String

	// 上层编码的类型
	public String pidfld_type = ""; // NUMBER->String

	// 本层的编码字段
	public String code_idfld = ""; // VARCHAR->String

	// 编码字段类型
	public String id_fldtype = ""; // NUMBER->String

	// 本层描述字段
	public String code_descfld = ""; // VARCHAR->String

	// 是否到用户级
	public String to_userlvl = ""; // VARCHAR->String

	// 是否有效
	public String is_valid = ""; // VARCHAR->String

	// 保留字段
	public String rsv_fld1 = ""; // VARCHAR->String

	// 保留字段
	public String rsv_fld2 = "";// VARCHAR->String

	// 保留字段
	public String rsv_fld3 = "";// VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！
	public boolean equals(Object obj) {
		boolean equal = false;
		if (null != obj && obj instanceof DimLevelTable) {
			DimLevelTable tmpObj = (DimLevelTable) obj;
			equal = (this.dim_id.equals(tmpObj.dim_id) && this.lvl_id
					.equals(tmpObj.lvl_id));
		}
		return equal;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("dim_id=").append(this.dim_id).append("\n");
		sb.append("lvl_id=").append(this.lvl_id).append("\n");
		sb.append("lvl_desc=").append(this.lvl_desc).append("\n");
		sb.append("lvl_table=").append(this.lvl_table).append("\n");
		sb.append("parent_idfld=").append(this.parent_idfld).append("\n");
		sb.append("pidfld_type=").append(this.pidfld_type).append("\n");
		sb.append("code_idfld=").append(this.code_idfld).append("\n");
		sb.append("idfld_type=").append(this.id_fldtype).append("\n");
		sb.append("code_descfld=").append(this.code_descfld).append("\n");
		sb.append("to_userlvl=").append(this.to_userlvl).append("\n");
		sb.append("is_valid=").append(this.is_valid).append("\n");
		return sb.toString();
	}
}
