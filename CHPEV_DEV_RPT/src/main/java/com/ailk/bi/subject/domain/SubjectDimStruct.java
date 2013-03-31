package com.ailk.bi.subject.domain;

import com.ailk.bi.common.event.JBTableBase;

/**
 * @author xdou 维度结构体
 */
public class SubjectDimStruct extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 字段编码
	 */
	public String code_fld = null;

	/**
	 * 数据类型
	 */
	public String data_type = null;

	/**
	 * 维度值
	 */
	public String dim_code = null;

	/**
	 * 维度描述
	 */
	public String dim_desc = null;
}
