package com.ailk.bi.subject.domain;

import com.ailk.bi.common.event.JBTableBase;

/**
 * @author xdou 表格查询语句结构体
 */
public class TableSelectQuery extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 查询的选择部分
	 */
	public StringBuffer select = new StringBuffer();

	/**
	 * 查询的表部分
	 */
	public StringBuffer from = new StringBuffer();

	/**
	 * 查询的条件部分
	 */
	public StringBuffer where = new StringBuffer();

	/**
	 * 查询的分组部分
	 */
	public StringBuffer groupby = new StringBuffer();

	/**
	 * 查询的排序部分
	 */
	public StringBuffer orderby = new StringBuffer();

	/**
	 * 查询的过滤部分
	 */
	public StringBuffer having = new StringBuffer();
}
