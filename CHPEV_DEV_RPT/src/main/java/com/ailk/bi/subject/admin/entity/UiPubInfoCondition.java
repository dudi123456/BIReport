package com.ailk.bi.subject.admin.entity;

/**
 * UiPubInfoCondition entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@SuppressWarnings({ "serial" })
public class UiPubInfoCondition implements java.io.Serializable {

	// Fields
	private String resId;
	private String resType;
	private String qryType;
	private String qryCode;
	private String conCode;
	private String dataType;
	private String conTag;
	private String sequence;
	private String status;

	private String rowId;

	// Constructors

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	/** default constructor */
	public UiPubInfoCondition() {
	}

	/** full constructor */
	public UiPubInfoCondition(String resId, String resType, String qryType,
			String qryCode, String conCode, String dataType, String conTag,
			String sequence, String status) {
		this.resId = resId;
		this.resType = resType;
		this.qryType = qryType;
		this.qryCode = qryCode;
		this.conCode = conCode;
		this.dataType = dataType;
		this.conTag = conTag;
		this.sequence = sequence;
		this.status = status;
	}

	// Property accessors

	public String getResId() {
		return this.resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getResType() {
		return this.resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getQryType() {
		return this.qryType;
	}

	public void setQryType(String qryType) {
		this.qryType = qryType;
	}

	public String getQryCode() {
		return this.qryCode;
	}

	public void setQryCode(String qryCode) {
		this.qryCode = qryCode;
	}

	public String getConCode() {
		return this.conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getConTag() {
		return this.conTag;
	}

	public void setConTag(String conTag) {
		this.conTag = conTag;
	}

	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}