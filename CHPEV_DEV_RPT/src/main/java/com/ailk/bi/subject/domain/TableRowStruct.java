package com.ailk.bi.subject.domain;

import com.ailk.bi.common.event.JBTableBase;

/**
 * @author xdou 表格的行结构
 */
public class TableRowStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一行号，不随行的位置改变而改变 由各维度值、层次标识组合而成
	 */
	public String row_id = null;

	/**
	 * 是否是合计行
	 */
	public boolean isSumRow = false;
	/**
	 * 跨行数
	 */
	public int row_span = 0;

	/**
	 * 表格的左侧部分
	 */
	public StringBuffer leftHTML = new StringBuffer();

	/**
	 * 为对齐表头而生成的左侧展开部分
	 */
	public StringBuffer leftExpandHTML = new StringBuffer();

	/**
	 * 收缩部分
	 */
	public StringBuffer leftCollapseHTML = new StringBuffer();

	/**
	 * 为对齐表头而生成的右侧部分
	 */
	public StringBuffer rightHTML = new StringBuffer();

	/**
	 * 行结构的导出HTML
	 */
	public StringBuffer exportHTML = new StringBuffer();

	/**
	 * 该行生成时的展开列标识
	 */
	public int expand_index = Integer.MIN_VALUE;

	/**
	 * 该行生成时的层次水平
	 */
	public int level = Integer.MIN_VALUE;

	/**
	 * 本行的数据，用于排序
	 */
	public String[] svces = null;

}
