package com.ailk.bi.subject.admin.entity;

/**
 * UiSubjectCommDimhierarchyId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings({ "serial" })
public class UiSubjectCommDimhierarchy implements java.io.Serializable {

	// Fields

	private String tableId;
	private String colId = "1";
	private String levId = "1";
	private String levName;
	private String levMemo;
	private String srcIdfld;
	private String idfldType = "0";
	private String srcNamefld;
	private String descAstitle;
	private String hasLink;
	private String linkUrl;
	private String linkTarget;
	private String tableName;

	// Constructors

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/** default constructor */
	public UiSubjectCommDimhierarchy() {
	}

	/** minimal constructor */
	public UiSubjectCommDimhierarchy(String tableId, String colId,
			String levId, String levName, String srcIdfld, String idfldType,
			String srcNamefld, String descAstitle) {
		this.tableId = tableId;
		this.colId = colId;
		this.levId = levId;
		this.levName = levName;
		this.srcIdfld = srcIdfld;
		this.idfldType = idfldType;
		this.srcNamefld = srcNamefld;
		this.descAstitle = descAstitle;
	}

	/** full constructor */
	public UiSubjectCommDimhierarchy(String tableId, String colId,
			String levId, String levName, String levMemo, String srcIdfld,
			String idfldType, String srcNamefld, String descAstitle,
			String hasLink, String linkUrl, String linkTarget) {
		this.tableId = tableId;
		this.colId = colId;
		this.levId = levId;
		this.levName = levName;
		this.levMemo = levMemo;
		this.srcIdfld = srcIdfld;
		this.idfldType = idfldType;
		this.srcNamefld = srcNamefld;
		this.descAstitle = descAstitle;
		this.hasLink = hasLink;
		this.linkUrl = linkUrl;
		this.linkTarget = linkTarget;
	}

	// Property accessors

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getColId() {
		return this.colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getLevId() {
		return this.levId;
	}

	public void setLevId(String levId) {
		this.levId = levId;
	}

	public String getLevName() {
		return this.levName;
	}

	public void setLevName(String levName) {
		this.levName = levName;
	}

	public String getLevMemo() {
		return this.levMemo;
	}

	public void setLevMemo(String levMemo) {
		this.levMemo = levMemo;
	}

	public String getSrcIdfld() {
		return this.srcIdfld;
	}

	public void setSrcIdfld(String srcIdfld) {
		this.srcIdfld = srcIdfld;
	}

	public String getIdfldType() {
		return this.idfldType;
	}

	public void setIdfldType(String idfldType) {
		this.idfldType = idfldType;
	}

	public String getSrcNamefld() {
		return this.srcNamefld;
	}

	public void setSrcNamefld(String srcNamefld) {
		this.srcNamefld = srcNamefld;
	}

	public String getDescAstitle() {
		return this.descAstitle;
	}

	public void setDescAstitle(String descAstitle) {
		this.descAstitle = descAstitle;
	}

	public String getHasLink() {
		return this.hasLink;
	}

	public void setHasLink(String hasLink) {
		this.hasLink = hasLink;
	}

	public String getLinkUrl() {
		return this.linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkTarget() {
		return this.linkTarget;
	}

	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UiSubjectCommDimhierarchy))
			return false;
		UiSubjectCommDimhierarchy castOther = (UiSubjectCommDimhierarchy) other;

		return ((this.getTableId() == castOther.getTableId()) || (this
				.getTableId() != null && castOther.getTableId() != null && this
				.getTableId().equals(castOther.getTableId())))
				&& ((this.getColId() == castOther.getColId()) || (this
						.getColId() != null && castOther.getColId() != null && this
						.getColId().equals(castOther.getColId())))
				&& ((this.getLevId() == castOther.getLevId()) || (this
						.getLevId() != null && castOther.getLevId() != null && this
						.getLevId().equals(castOther.getLevId())))
				&& ((this.getLevName() == castOther.getLevName()) || (this
						.getLevName() != null && castOther.getLevName() != null && this
						.getLevName().equals(castOther.getLevName())))
				&& ((this.getLevMemo() == castOther.getLevMemo()) || (this
						.getLevMemo() != null && castOther.getLevMemo() != null && this
						.getLevMemo().equals(castOther.getLevMemo())))
				&& ((this.getSrcIdfld() == castOther.getSrcIdfld()) || (this
						.getSrcIdfld() != null
						&& castOther.getSrcIdfld() != null && this
						.getSrcIdfld().equals(castOther.getSrcIdfld())))
				&& ((this.getIdfldType() == castOther.getIdfldType()) || (this
						.getIdfldType() != null
						&& castOther.getIdfldType() != null && this
						.getIdfldType().equals(castOther.getIdfldType())))
				&& ((this.getSrcNamefld() == castOther.getSrcNamefld()) || (this
						.getSrcNamefld() != null
						&& castOther.getSrcNamefld() != null && this
						.getSrcNamefld().equals(castOther.getSrcNamefld())))
				&& ((this.getDescAstitle() == castOther.getDescAstitle()) || (this
						.getDescAstitle() != null
						&& castOther.getDescAstitle() != null && this
						.getDescAstitle().equals(castOther.getDescAstitle())))
				&& ((this.getHasLink() == castOther.getHasLink()) || (this
						.getHasLink() != null && castOther.getHasLink() != null && this
						.getHasLink().equals(castOther.getHasLink())))
				&& ((this.getLinkUrl() == castOther.getLinkUrl()) || (this
						.getLinkUrl() != null && castOther.getLinkUrl() != null && this
						.getLinkUrl().equals(castOther.getLinkUrl())))
				&& ((this.getLinkTarget() == castOther.getLinkTarget()) || (this
						.getLinkTarget() != null
						&& castOther.getLinkTarget() != null && this
						.getLinkTarget().equals(castOther.getLinkTarget())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTableId() == null ? 0 : this.getTableId().hashCode());
		result = 37 * result
				+ (getColId() == null ? 0 : this.getColId().hashCode());
		result = 37 * result
				+ (getLevId() == null ? 0 : this.getLevId().hashCode());
		result = 37 * result
				+ (getLevName() == null ? 0 : this.getLevName().hashCode());
		result = 37 * result
				+ (getLevMemo() == null ? 0 : this.getLevMemo().hashCode());
		result = 37 * result
				+ (getSrcIdfld() == null ? 0 : this.getSrcIdfld().hashCode());
		result = 37 * result
				+ (getIdfldType() == null ? 0 : this.getIdfldType().hashCode());
		result = 37
				* result
				+ (getSrcNamefld() == null ? 0 : this.getSrcNamefld()
						.hashCode());
		result = 37
				* result
				+ (getDescAstitle() == null ? 0 : this.getDescAstitle()
						.hashCode());
		result = 37 * result
				+ (getHasLink() == null ? 0 : this.getHasLink().hashCode());
		result = 37 * result
				+ (getLinkUrl() == null ? 0 : this.getLinkUrl().hashCode());
		result = 37
				* result
				+ (getLinkTarget() == null ? 0 : this.getLinkTarget()
						.hashCode());
		return result;
	}

}