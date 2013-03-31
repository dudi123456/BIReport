package com.ailk.bi.base.table;

import java.io.Serializable;

/**
 * 用于保存在条件定制区中有数据的信息列表的内容
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class StunionParseTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID = ""; // 定义接口ID

	private String QUERYCN = ""; // 定义已经输入条件的中文名称

	private String QUERYNAME = ""; // 定义从界面获取的查询条件

	private String STRUCTNAME = ""; // 定义从结构体获取的英文名称

	private String QUERYVALUE = ""; // 定义从界面获取的列表数值

	private String FIELDTYPE = ""; // 1:数值；2：字符

	private String QUERYVALUE2 = ""; // 定义从界面获取的列表数值2

	private String CON_TYPE = ""; // 大类编号

	private String SHOW_TYPE = ""; // 显示类型

	private String ASSI_TYPE = ""; // 辅助按钮

	private String SHOW_TURN = ""; // 显示顺序

	private String IS_VALID = ""; // 是否有效

	private String CODE_SQL = ""; // 下拉框对应sql语句

	private String LTD_PARAMS = ""; // sql语句对应的where条件

	private String ADD_FLAG = ""; // 是否where条件有效

	/**
	 * 定义获取条件列表的序列结构体
	 * 
	 * @param ID
	 *            String
	 * @param QUERYCN
	 *            String
	 * @param QUERYNAME
	 *            String
	 * @param STRUCTNAME
	 *            String
	 * @param QUERYVALUE
	 *            String
	 */
	public StunionParseTable(String ID, String QUERYCN, String QUERYNAME,
			String STRUCTNAME, String QUERYVALUE, String FIELDTYPE) {
		this.ID = ID;
		this.QUERYCN = QUERYCN;
		this.QUERYNAME = QUERYNAME;
		this.STRUCTNAME = STRUCTNAME;
		this.QUERYVALUE = QUERYVALUE;
		this.FIELDTYPE = FIELDTYPE;
		/*
		 * this.CON_TYPE = CON_TYPE; this.SHOW_TYPE = SHOW_TYPE; this.ASSI_TYPE
		 * = ASSI_TYPE; this.SHOW_TURN = SHOW_TURN; this.IS_VALID = IS_VALID;
		 */
	}

	public StunionParseTable() {

	}

	/**
	 * 设置大类编号
	 * 
	 * @param CON_TYPE
	 *            String
	 */
	public void setCONTYPE(String CON_TYPE) {
		this.CON_TYPE = CON_TYPE;
	}

	/**
	 * 设置显示类型
	 * 
	 * @param CON_TYPE
	 *            String
	 */
	public void setSHOWTYPE(String SHOW_TYPE) {
		this.SHOW_TYPE = SHOW_TYPE;
	}

	/**
	 * 设置辅助按钮
	 * 
	 * @param CON_TYPE
	 *            String
	 */
	public void setASSITYPE(String ASSI_TYPE) {
		this.ASSI_TYPE = ASSI_TYPE;
	}

	/**
	 * 设置显示顺序
	 * 
	 * @param CON_TYPE
	 *            String
	 */
	public void setSHOWTURN(String SHOW_TURN) {
		this.SHOW_TURN = SHOW_TURN;
	}

	/**
	 * 设置是否有效
	 * 
	 * @param CON_TYPE
	 *            String
	 */
	public void setISVALID(String IS_VALID) {
		this.IS_VALID = IS_VALID;
	}

	/**
	 * 设置已经输入框的列表的ID
	 * 
	 * @param ID
	 *            String
	 */
	public void setID(String ID) {
		this.ID = ID;
	}

	/**
	 * 设置已经具有输入框的汉语名称
	 * 
	 * @param QUERYCN
	 *            String
	 */
	public void setQUERYCN(String QUERYCN) {
		this.QUERYCN = QUERYCN;
	}

	/**
	 * 设置已经输入内容的界面查询条件的英文名称
	 * 
	 * @param QUERYNAME
	 *            String
	 */
	public void setQUERYNAME(String QUERYNAME) {
		this.QUERYNAME = QUERYNAME;
	}

	/**
	 * 设置已经输入内容的界面的结构体的影射字段名称
	 * 
	 * @param STRUCTNAME
	 *            String
	 */
	public void setSTRUCTNAME(String STRUCTNAME) {
		this.STRUCTNAME = STRUCTNAME;
	}

	/**
	 * 设置已经输入内容的界面的结构体的输入的具体内容
	 * 
	 * @param QUERYVALUE
	 *            String
	 */
	public void setQUERYVALUE(String QUERYVALUE) {
		this.QUERYVALUE = QUERYVALUE;
	}

	/**
	 * 设置已经输入内容的界面的结构体的输入的具体内容2
	 * 
	 * @param QUERYVALUE2
	 *            String
	 */
	public void setQUERYVALUE2(String QUERYVALUE2) {
		this.QUERYVALUE2 = QUERYVALUE2;
	}

	/**
	 * 设置界面的类型属性
	 * 
	 * @param FIELDTYPE
	 *            String
	 */
	public void setFIELDTYPE(String FIELDTYPE) {
		this.FIELDTYPE = FIELDTYPE;
	}

	/**
	 * 设置界面的类型属性
	 * 
	 * @param FIELDTYPE
	 *            String
	 */
	public void setCODESQL(String CODE_SQL) {
		this.CODE_SQL = CODE_SQL;
	}

	/**
	 * 设置界面的类型属性
	 * 
	 * @param FIELDTYPE
	 *            String
	 */
	public void setLTDPARAMS(String LTD_PARAMS) {
		this.LTD_PARAMS = LTD_PARAMS;
	}

	/**
	 * 设置界面的类型属性
	 * 
	 * @param FIELDTYPE
	 *            String
	 */
	public void setADDFLAG(String ADD_FLAG) {
		this.ADD_FLAG = ADD_FLAG;
	}

	/**
	 * 取得已经输入框的列表的ID
	 * 
	 * @param ID
	 *            String
	 */
	public String getID() {
		return ID;
	}

	/**
	 * 取得已经具有输入框的汉语名称
	 * 
	 * @param QUERYCN
	 *            String
	 */
	public String getQUERYCN() {
		return QUERYCN;
	}

	/**
	 * 取得已经输入内容的界面查询条件的英文名称
	 * 
	 * @param QUERYNAME
	 *            String
	 */
	public String getQUERYNAME() {
		return QUERYNAME;
	}

	/**
	 * 取得已经输入内容的界面的结构体的影射字段名称
	 * 
	 * @param STRUCTNAME
	 *            String
	 */
	public String getSTRUCTNAME() {
		return STRUCTNAME;
	}

	/**
	 * 取得已经输入内容的界面的结构体的输入的具体内容
	 * 
	 * @param QUERYVALUE
	 *            String
	 */
	public String getQUERYVALUE() {
		return QUERYVALUE;
	}

	/**
	 * 取得已经输入内容的界面的结构体的输入的具体内容2
	 * 
	 * @param QUERYVALUE2
	 *            String
	 */
	public String getQUERYVALUE2() {
		return QUERYVALUE2;
	}

	/**
	 * 取得字段的类型属性
	 * 
	 * @return String
	 */
	public String getFIELDTYPE() {
		return FIELDTYPE;
	}

	public String getCONTYPE() {
		return CON_TYPE;
	}

	public String getSHOWTYPE() {
		return SHOW_TYPE;
	}

	public String getASSITYPE() {
		return ASSI_TYPE;
	}

	public String getSHOWTURN() {
		return SHOW_TURN;
	}

	public String getISVALID() {
		return IS_VALID;
	}

	public String getCODESQL() {
		return CODE_SQL;
	}

	/**
	 * 设置界面的类型属性
	 * 
	 * @param FIELDTYPE
	 *            String
	 */
	public String getLTDPARAMS() {
		return LTD_PARAMS;
	}

	/**
	 * 设置界面的类型属性
	 * 
	 * @param FIELDTYPE
	 *            String
	 */
	public String getADDFLAG() {
		return ADD_FLAG;
	}

}
