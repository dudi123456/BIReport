package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class SubjectCommDimHierarchy extends JBTableBase {
	private static final long serialVersionUID = 3659589522673150886L;

	/**
	 * 表格标识
	 */
	public String table_id = "";

	/**
	 * 表格列标识
	 */
	public String col_id = "";

	/**
	 * 表格列的层次标识
	 */
	public String lev_id = "";

	/**
	 * 层次名称
	 */
	public String lev_name = "";

	/**
	 * 层次说明
	 */
	public String lev_memo = "";

	/**
	 * 编码字段
	 */
	public String src_idfld = "";

	/**
	 * 字段类型
	 */
	public String idfld_type = "";

	/**
	 * 名称字段
	 */
	public String src_namefld = "";

	/**
	 * 由于维度描述过长，作为title显示
	 */
	public String desc_astitle = "";

	/**
	 * 是否链接
	 */
	public String has_link = "";

	/**
	 * 链接URL
	 */
	public String link_url = "";

	/**
	 * 链接目标
	 */
	public String link_target = "";
	/**
	 * 列的表头的HTML代码
	 */
	public String colHeadHTML1 = "";

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof SubjectCommDimHierarchy))
			return false;
		SubjectCommDimHierarchy tmpObj = (SubjectCommDimHierarchy) obj;
		return this.table_id.equals(tmpObj.table_id)
				&& this.col_id.equals(tmpObj.col_id)
				&& this.lev_id.equals(tmpObj.lev_id);
	}

	public int hashCode() {
		return this.table_id.hashCode() + this.col_id.hashCode()
				+ this.lev_id.hashCode() + super.hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		// 报表标识
		sb.append(" table_id = ").append(this.table_id).append("\n");

		// 报表列标识
		sb.append(" col_id = ").append(this.col_id).append("\n");

		// 报表列层次标识
		sb.append(" lev_id = ").append(this.lev_id).append("\n");
		// 报表列层次名称
		sb.append(" lev_name = ").append(this.lev_name).append("\n");
		// 报表列层次说明
		sb.append(" lev_memo = ").append(this.lev_memo).append("\n");
		// 报表列层次的编码字段
		sb.append(" src_idfld = ").append(this.src_idfld).append("\n");
		// 报表列层次编码字段类型
		sb.append(" idfld_type = ").append(this.idfld_type).append("\n");
		// 报表列层次名称字段
		sb.append(" src_namefld = ").append(this.src_namefld).append("\n");
		return sb.toString();
	}

}