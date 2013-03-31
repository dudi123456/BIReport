package com.ailk.bi.subject.admin.entity;

/**
 * UiPubInfoChartDef entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@SuppressWarnings({ "serial" })
public class UiPubInfoChartDef implements java.io.Serializable {

	// Fields

	private String chartId;
	private String chartBelong;

	public String getChartBelong() {
		return chartBelong;
	}

	public void setChartBelong(String chartBelong) {
		this.chartBelong = chartBelong;
	}

	private String chartType;
	private String chartIndex;
	private String chartAttribute;
	private String sqlMain;
	private String sqlWhere;
	private String sqlOrder;
	private String isusecd;
	private String categoryIndex;
	private String seriesIndex;
	private String seriesLength;
	private String seriesCut;
	private String seriesCutLen;
	private String valueIndex;
	private String categoryDesc;
	private String categoryDescIndex;
	private String chartDistype;

	// Constructors

	/** default constructor */
	public UiPubInfoChartDef() {
	}

	/**
	 * minimal constructor
	 * 
	 * @param chartBelong
	 */
	public UiPubInfoChartDef(String chartId, String chartBeString,
			String chartType, String chartIndex, String sqlMain,
			String sqlWhere, String isusecd, String categoryIndex,
			String seriesIndex, String seriesLength, String seriesCut,
			String seriesCutLen, String valueIndex, String chartBelong) {
		this.chartId = chartId;
		this.chartBelong = chartBelong;
		this.chartType = chartType;
		this.chartIndex = chartIndex;
		this.sqlMain = sqlMain;
		this.sqlWhere = sqlWhere;
		this.isusecd = isusecd;
		this.categoryIndex = categoryIndex;
		this.seriesIndex = seriesIndex;
		this.seriesLength = seriesLength;
		this.seriesCut = seriesCut;
		this.seriesCutLen = seriesCutLen;
		this.valueIndex = valueIndex;
	}

	/**
	 * full constructor
	 * 
	 * @param chartBelong
	 */
	public UiPubInfoChartDef(String chartId, String chartBeString,
			String chartType, String chartIndex, String chartAttribute,
			String sqlMain, String sqlWhere, String sqlOrder, String isusecd,
			String categoryIndex, String seriesIndex, String seriesLength,
			String seriesCut, String seriesCutLen, String valueIndex,
			String categoryDesc, String categoryDescIndex, String chartDistype,
			String chartBelong) {
		this.chartId = chartId;
		this.chartBelong = chartBelong;
		this.chartType = chartType;
		this.chartIndex = chartIndex;
		this.chartAttribute = chartAttribute;
		this.sqlMain = sqlMain;
		this.sqlWhere = sqlWhere;
		this.sqlOrder = sqlOrder;
		this.isusecd = isusecd;
		this.categoryIndex = categoryIndex;
		this.seriesIndex = seriesIndex;
		this.seriesLength = seriesLength;
		this.seriesCut = seriesCut;
		this.seriesCutLen = seriesCutLen;
		this.valueIndex = valueIndex;
		this.categoryDesc = categoryDesc;
		this.categoryDescIndex = categoryDescIndex;
		this.chartDistype = chartDistype;
	}

	// Property accessors

	public String getChartId() {
		return this.chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public String getChartType() {
		return this.chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getChartIndex() {
		return this.chartIndex;
	}

	public void setChartIndex(String chartIndex) {
		this.chartIndex = chartIndex;
	}

	public String getChartAttribute() {
		return this.chartAttribute;
	}

	public void setChartAttribute(String chartAttribute) {
		this.chartAttribute = chartAttribute;
	}

	public String getSqlMain() {
		return this.sqlMain;
	}

	public void setSqlMain(String sqlMain) {
		this.sqlMain = sqlMain;
	}

	public String getSqlWhere() {
		return this.sqlWhere;
	}

	public void setSqlWhere(String sqlWhere) {
		this.sqlWhere = sqlWhere;
	}

	public String getSqlOrder() {
		return this.sqlOrder;
	}

	public void setSqlOrder(String sqlOrder) {
		this.sqlOrder = sqlOrder;
	}

	public String getIsusecd() {
		return this.isusecd;
	}

	public void setIsusecd(String isusecd) {
		this.isusecd = isusecd;
	}

	public String getCategoryIndex() {
		return this.categoryIndex;
	}

	public void setCategoryIndex(String categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public String getSeriesIndex() {
		return this.seriesIndex;
	}

	public void setSeriesIndex(String seriesIndex) {
		this.seriesIndex = seriesIndex;
	}

	public String getSeriesLength() {
		return this.seriesLength;
	}

	public void setSeriesLength(String seriesLength) {
		this.seriesLength = seriesLength;
	}

	public String getSeriesCut() {
		return this.seriesCut;
	}

	public void setSeriesCut(String seriesCut) {
		this.seriesCut = seriesCut;
	}

	public String getSeriesCutLen() {
		return this.seriesCutLen;
	}

	public void setSeriesCutLen(String seriesCutLen) {
		this.seriesCutLen = seriesCutLen;
	}

	public String getValueIndex() {
		return this.valueIndex;
	}

	public void setValueIndex(String valueIndex) {
		this.valueIndex = valueIndex;
	}

	public String getCategoryDesc() {
		return this.categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getCategoryDescIndex() {
		return this.categoryDescIndex;
	}

	public void setCategoryDescIndex(String categoryDescIndex) {
		this.categoryDescIndex = categoryDescIndex;
	}

	public String getChartDistype() {
		return this.chartDistype;
	}

	public void setChartDistype(String chartDistype) {
		this.chartDistype = chartDistype;
	}

}