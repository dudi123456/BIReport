package com.ailk.bi.subject.admin.entity;

/**
 * UiPubInfoChartKindDefId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings({ "serial" })
public class UiPubInfoChartKindDef implements java.io.Serializable {

	// Fields

	private Long chartTypeId;
	private String chartTypeDesc;
	private String chartTypeCode;
	private Long chartDistype;

	// Constructors

	/** default constructor */
	public UiPubInfoChartKindDef() {
	}

	/** minimal constructor */
	public UiPubInfoChartKindDef(Long chartTypeId, Long chartDistype) {
		this.chartTypeId = chartTypeId;
		this.chartDistype = chartDistype;
	}

	/** full constructor */
	public UiPubInfoChartKindDef(Long chartTypeId, String chartTypeDesc,
			String chartTypeCode, Long chartDistype) {
		this.chartTypeId = chartTypeId;
		this.chartTypeDesc = chartTypeDesc;
		this.chartTypeCode = chartTypeCode;
		this.chartDistype = chartDistype;
	}

	// Property accessors

	public Long getChartTypeId() {
		return this.chartTypeId;
	}

	public void setChartTypeId(Long chartTypeId) {
		this.chartTypeId = chartTypeId;
	}

	public String getChartTypeDesc() {
		return this.chartTypeDesc;
	}

	public void setChartTypeDesc(String chartTypeDesc) {
		this.chartTypeDesc = chartTypeDesc;
	}

	public String getChartTypeCode() {
		return this.chartTypeCode;
	}

	public void setChartTypeCode(String chartTypeCode) {
		this.chartTypeCode = chartTypeCode;
	}

	public Long getChartDistype() {
		return this.chartDistype;
	}

	public void setChartDistype(Long chartDistype) {
		this.chartDistype = chartDistype;
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getChartTypeId() == null ? 0 : this.getChartTypeId()
						.hashCode());
		result = 37
				* result
				+ (getChartTypeDesc() == null ? 0 : this.getChartTypeDesc()
						.hashCode());
		result = 37
				* result
				+ (getChartTypeCode() == null ? 0 : this.getChartTypeCode()
						.hashCode());
		result = 37
				* result
				+ (getChartDistype() == null ? 0 : this.getChartDistype()
						.hashCode());
		return result;
	}

}