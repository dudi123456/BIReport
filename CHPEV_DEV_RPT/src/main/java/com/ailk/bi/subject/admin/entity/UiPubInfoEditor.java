package com.ailk.bi.subject.admin.entity;

/**
 * UiPubInfoEditor entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings({ "serial" })
public class UiPubInfoEditor implements java.io.Serializable {

	// Fields

	private String objId;
	private Long objDate;
	private String objInfo;

	// Constructors

	/** default constructor */
	public UiPubInfoEditor() {
	}

	/** full constructor */
	public UiPubInfoEditor(String objId, Long objDate, String objInfo) {
		this.objId = objId;
		this.objDate = objDate;
		this.objInfo = objInfo;
	}

	// Property accessors

	public String getObjId() {
		return this.objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public Long getObjDate() {
		return this.objDate;
	}

	public void setObjDate(Long objDate) {
		this.objDate = objDate;
	}

	public String getObjInfo() {
		return this.objInfo;
	}

	public void setObjInfo(String objInfo) {
		this.objInfo = objInfo;
	}

}