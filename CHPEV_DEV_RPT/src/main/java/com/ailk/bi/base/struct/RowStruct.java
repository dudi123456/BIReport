package com.ailk.bi.base.struct;

import java.util.List;

/**
 * 代表表格行的行对象
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class RowStruct {

	/**
	 * 是否时展开行
	 */
	public boolean expanded = false;

	/**
	 * 唯一行号，不随行的位置改变而改变 由各维度值、层次标识组合而成
	 */
	public String row_id = null;

	/**
	 * 该行生成时的展开列标识
	 */
	public int expand_index = Integer.MIN_VALUE;

	/**
	 * 该行生成时的层次水平
	 */
	public int level = Integer.MIN_VALUE;

	/**
	 * 存储各个列对象，
	 */
	public List colObjs = null;

	/**
	 * 存储各个列的值
	 */
	public String[] cols_value = null;

}
