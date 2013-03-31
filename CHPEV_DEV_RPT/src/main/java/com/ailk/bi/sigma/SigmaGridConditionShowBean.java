package com.ailk.bi.sigma;

public class SigmaGridConditionShowBean {

	private int showHidden;
	private String reqConName;
	private String showConName;
	private String conTag;
	private int dataType;

	private String showHtmlString;

	private int conRange;

	private String fieldName;

	private int isNull;

	private int passType;
	private String passParam;

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

	public int getIsNull() {
		return isNull;
	}

	public void setIsNull(int isNull) {
		this.isNull = isNull;
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

	public int getConRange() {
		return conRange;
	}

	public void setConRange(int conRange) {
		this.conRange = conRange;
	}

	public int getShowHidden() {
		return showHidden;
	}

	public void setShowHidden(int showHidden) {
		this.showHidden = showHidden;
	}

	public String getReqConName() {
		return reqConName;
	}

	public void setReqConName(String reqConName) {
		this.reqConName = reqConName;
	}

	public String getShowConName() {
		return showConName;
	}

	public void setShowConName(String showConName) {
		this.showConName = showConName;
	}

	public String getShowHtmlString() {
		return showHtmlString;
	}

	public void setShowHtmlString(String showHtmlString) {
		this.showHtmlString = showHtmlString;
	}

}
