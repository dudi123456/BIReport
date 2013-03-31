package com.ailk.bi.olap.domain;

public class RptOlapFunc {
	/**
	 * 当前功能，默认数据，占比、同比、环比等
	 */
	private String curFunc = null;

	/**
	 * 是否有累计值
	 */
	private boolean hasSum = false;

	/**
	 * 是否有预警
	 */
	private boolean hasAlert = false;

	public String getCurFunc() {
		return curFunc;
	}

	public void setCurFunc(String curFunc) {
		this.curFunc = curFunc;
	}

	public boolean isHasAlert() {
		return hasAlert;
	}

	public void setHasAlert(boolean hasAlert) {
		this.hasAlert = hasAlert;
	}

	public boolean isHasSum() {
		return hasSum;
	}

	public void setHasSum(boolean hasSum) {
		this.hasSum = hasSum;
	}

}
