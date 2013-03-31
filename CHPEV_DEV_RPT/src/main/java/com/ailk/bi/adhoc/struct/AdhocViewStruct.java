package com.ailk.bi.adhoc.struct;

import com.ailk.bi.common.event.JBTableBase;

public class AdhocViewStruct extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String headStr = ""; // 定义表头汉语描述的HTML格式的内容

	public String headDesc = "";// 定义表头的描述

	public String sqlStd = ""; // 定义查询内容的标准查询，例如：SELECT AA FROM TAB；

	public String sqlCon = ""; // 定义查询条件的组合，例如： WHERE AA=2；

	public String sqlGrp = ""; // 定义查询条件的分组组合，例如：GROUP BY AA；

	public String sqlHaving_1 = ""; // 定义查询条件的排序内容，例如：ORDER BY AA；

	public String sqlHaving_2 = ""; // 定义查询条件的排序内容，例如：ORDER BY AA；

	public String sqlHaving = ""; // 定义查询条件的排序内容，例如：ORDER BY AA；

	public String sqlOdr = ""; // 定义查询条件的排序内容，例如：ORDER BY AA；

	public String[] sqlListAry = null;

	// 外关联标志,格式：条件映射的字段1,条件值1|条件映射的字段2,条件值2,
	public String leftOuterJoinTag = "";

	public String sqlLinkTable = "";
	public String sqlLinkTableCon = "";

}
