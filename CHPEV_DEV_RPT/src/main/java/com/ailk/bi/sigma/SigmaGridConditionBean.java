package com.ailk.bi.sigma;

public class SigmaGridConditionBean {

	private String sigmaConId;
	private String sigmaId;
	private String conId;
	private String conName;
	private String conDesc;
	private String sortNum;
	private String fieldName;
	private String conTag;
	private int dataType;
	private int conType;

	private String selectSql;
	private String whereSql;
	private String orderSql;
	private String groupSql;
	private String defaultValue;
	private int conRange;

	private int isNull;

	private String showAll;

	private String choseType;

	private String hasChild;

	private String passParam;

	private int passType;

	public int getPassType() {
		return passType;
	}

	public void setPassType(int passType) {
		this.passType = passType;
	}

	public String getPassParam() {
		return passParam;
	}

	public void setPassParam(String passParam) {
		this.passParam = passParam;
	}

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}

	public String getShowAll() {
		return showAll;
	}

	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}

	public String getChoseType() {
		return choseType;
	}

	public void setChoseType(String choseType) {
		this.choseType = choseType;
	}

	public int getIsNull() {
		return isNull;
	}

	public void setIsNull(int isNull) {
		this.isNull = isNull;
	}

	public int getConRange() {
		return conRange;
	}

	public void setConRange(int conRange) {
		this.conRange = conRange;
	}

	private int showHidden;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getShowHidden() {
		return showHidden;
	}

	public void setShowHidden(int showHidden) {
		this.showHidden = showHidden;
	}

	public String getSigmaConId() {
		return sigmaConId;
	}

	public void setSigmaConId(String sigmaConId) {
		this.sigmaConId = sigmaConId;
	}

	public String getSigmaId() {
		return sigmaId;
	}

	public void setSigmaId(String sigmaId) {
		this.sigmaId = sigmaId;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getConName() {
		return conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	public String getConDesc() {
		return conDesc;
	}

	public void setConDesc(String conDesc) {
		this.conDesc = conDesc;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getConTag() {
		return conTag;
	}

	public void setConTag(String conTag) {
		this.conTag = conTag;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getConType() {
		return conType;
	}

	public void setConType(int conType) {
		this.conType = conType;
	}

	public String getSelectSql() {
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}

	public String getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

	public String getOrderSql() {
		return orderSql;
	}

	public void setOrderSql(String orderSql) {
		this.orderSql = orderSql;
	}

	public String getGroupSql() {
		return groupSql;
	}

	public void setGroupSql(String groupSql) {
		this.groupSql = groupSql;
	}

	public String getParentConId() {
		return parentConId;
	}

	public void setParentConId(String parentConId) {
		this.parentConId = parentConId;
	}

	public String getParentFld() {
		return parentFld;
	}

	public void setParentFld(String parentFld) {
		this.parentFld = parentFld;
	}

	public String getSrcDataType() {
		return srcDataType;
	}

	public void setSrcDataType(String srcDataType) {
		this.srcDataType = srcDataType;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getSrcConName() {
		return srcConName;
	}

	public void setSrcConName(String srcConName) {
		this.srcConName = srcConName;
	}

	public String getSrcConDesc() {
		return srcConDesc;
	}

	public void setSrcConDesc(String srcConDesc) {
		this.srcConDesc = srcConDesc;
	}

	private String parentConId;
	private String parentFld;

	private String srcDataType;
	private String dataFormat;

	private String srcConName;
	private String srcConDesc;

}
