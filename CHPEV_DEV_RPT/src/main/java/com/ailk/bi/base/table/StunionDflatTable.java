package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class StunionDflatTable extends JBTableBase {
	// HERE IS FROM DATABASE

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 标识
	public String ID = ""; // 定义维度ID

	public String LATCODE = ""; // 定义维度汉语名称

	public String LATNAME = ""; // 定义维度编码字段名称

	public String DEFFIELD = ""; // 定义维度描述字段名称

	public String IS_VALID = ""; // 定义维度的类型和有效 0无效；大于0有效（1普通维；2：是分档维）

	public String UNIT = ""; // 定义分档维度的单位

	public String ACTFIELD = ""; // 定义分档维度的实际字段

	public String ASSFIELD = ""; // 定义分档维度的特殊定义

}
