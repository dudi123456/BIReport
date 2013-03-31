package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class StunionFfunitTable extends JBTableBase {
	// HERE IS FROM DATABASE

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 标识
	public String ID = ""; // 定义度量ID

	public String LATCODE = ""; // 定义度量汉语名称

	public String LATNAME = ""; // 定义度量字段描述

	public String CONTYPE = ""; // 定义度量类别id

	public String SHOWTURN = ""; // 定义度量显示顺序

	public String ISVALID = ""; // 定义度量是否有效

	public String DIGIT = ""; // 小数点位数

	public String UNIT = ""; // 度量单位

	public String RES_CHAR1 = ""; // 系数

}
