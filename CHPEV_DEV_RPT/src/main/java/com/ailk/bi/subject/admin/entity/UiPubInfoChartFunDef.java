package com.ailk.bi.subject.admin.entity;

/**
 * UiPubInfoChartFunDefId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings({ "serial" })
public class UiPubInfoChartFunDef implements java.io.Serializable {

	// Fields

	private String chartId;
	private String frameName;
	private String chartDesc;
	private String categoryDesc;
	private String categoryDescIndex;
	private Long valueIndex;
	private String wheresql;
	private String replaceContent;
	private String isChecked;
	private Long colSequence;

	// Constructors

	/** default constructor */
	public UiPubInfoChartFunDef() {
	}

	/** minimal constructor */
	public UiPubInfoChartFunDef(String chartId, Long colSequence) {
		this.chartId = chartId;
		this.colSequence = colSequence;
	}

	/** full constructor */
	public UiPubInfoChartFunDef(String chartId, String frameName,
			String chartDesc, String categoryDesc, String categoryDescIndex,
			Long valueIndex, String wheresql, String replaceContent,
			String isChecked, Long colSequence) {
		this.chartId = chartId;
		this.frameName = frameName;
		this.chartDesc = chartDesc;
		this.categoryDesc = categoryDesc;
		this.categoryDescIndex = categoryDescIndex;
		this.valueIndex = valueIndex;
		this.wheresql = wheresql;
		this.replaceContent = replaceContent;
		this.isChecked = isChecked;
		this.colSequence = colSequence;
	}

	// Property accessors

	public String getChartId() {
		return this.chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public String getFrameName() {
		return this.frameName;
	}

	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}

	public String getChartDesc() {
		return this.chartDesc;
	}

	public void setChartDesc(String chartDesc) {
		this.chartDesc = chartDesc;
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

	public Long getValueIndex() {
		return this.valueIndex;
	}

	public void setValueIndex(Long valueIndex) {
		this.valueIndex = valueIndex;
	}

	public String getWheresql() {
		return this.wheresql;
	}

	public void setWheresql(String wheresql) {
		this.wheresql = wheresql;
	}

	public String getReplaceContent() {
		return this.replaceContent;
	}

	public void setReplaceContent(String replaceContent) {
		this.replaceContent = replaceContent;
	}

	public String getIsChecked() {
		return this.isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public Long getColSequence() {
		return this.colSequence;
	}

	public void setColSequence(Long colSequence) {
		this.colSequence = colSequence;
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getChartId() == null ? 0 : this.getChartId().hashCode());
		result = 37 * result
				+ (getFrameName() == null ? 0 : this.getFrameName().hashCode());
		result = 37 * result
				+ (getChartDesc() == null ? 0 : this.getChartDesc().hashCode());
		result = 37
				* result
				+ (getCategoryDesc() == null ? 0 : this.getCategoryDesc()
						.hashCode());
		result = 37
				* result
				+ (getCategoryDescIndex() == null ? 0 : this
						.getCategoryDescIndex().hashCode());
		result = 37
				* result
				+ (getValueIndex() == null ? 0 : this.getValueIndex()
						.hashCode());
		result = 37 * result
				+ (getWheresql() == null ? 0 : this.getWheresql().hashCode());
		result = 37
				* result
				+ (getReplaceContent() == null ? 0 : this.getReplaceContent()
						.hashCode());
		result = 37 * result
				+ (getIsChecked() == null ? 0 : this.getIsChecked().hashCode());
		result = 37
				* result
				+ (getColSequence() == null ? 0 : this.getColSequence()
						.hashCode());
		return result;
	}

}