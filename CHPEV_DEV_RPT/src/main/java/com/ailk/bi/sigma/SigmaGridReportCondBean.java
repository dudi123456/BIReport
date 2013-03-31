package com.ailk.bi.sigma;

public class SigmaGridReportCondBean {

	private int sigmaId;
	// 条件SQL
	private String sigmaConSql = "";

	// 存Session的条件值
	private String sigmaConValue = "";

	public String getSigmaConValue() {
		return sigmaConValue;
	}

	public void setSigmaConValue(String sigmaConValue) {
		this.sigmaConValue = sigmaConValue;
	}

	public int getSigmaId() {
		return sigmaId;
	}

	public void setSigmaId(int sigmaId) {
		this.sigmaId = sigmaId;
	}

	public String getSigmaConSql() {
		return sigmaConSql;
	}

	public void setSigmaConSql(String sigmaConSql) {
		this.sigmaConSql = sigmaConSql;
	}

}
