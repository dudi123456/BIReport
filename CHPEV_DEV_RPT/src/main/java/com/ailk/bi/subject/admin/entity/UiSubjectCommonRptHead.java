package com.ailk.bi.subject.admin.entity;

/**
 * UiSubjectCommonRptHeadId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

@SuppressWarnings({ "serial" })
public class UiSubjectCommonRptHead implements java.io.Serializable {

	// Fields

	private String tableId = "";
	private String tableHeader = "";
	private Long rowSpan = null;

	// Constructors

	/** default constructor */
	public UiSubjectCommonRptHead() {
	}

	/** minimal constructor */
	public UiSubjectCommonRptHead(String tableId) {
		this.tableId = tableId;
	}

	/** full constructor */
	public UiSubjectCommonRptHead(String tableId, String tableHeader,
			Long rowSpan) {
		this.tableId = tableId;
		this.tableHeader = tableHeader;
		this.rowSpan = rowSpan;
	}

	// Property accessors

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableHeader() {
		return this.tableHeader;
	}

	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}

	public Long getRowSpan() {
		return this.rowSpan;
	}

	public void setRowSpan(Long rowSpan) {
		this.rowSpan = rowSpan;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UiSubjectCommonRptHead))
			return false;
		UiSubjectCommonRptHead castOther = (UiSubjectCommonRptHead) other;

		return ((this.getTableId() == castOther.getTableId()) || (this
				.getTableId() != null && castOther.getTableId() != null && this
				.getTableId().equals(castOther.getTableId())))
				&& ((this.getTableHeader() == castOther.getTableHeader()) || (this
						.getTableHeader() != null
						&& castOther.getTableHeader() != null && this
						.getTableHeader().equals(castOther.getTableHeader())))
				&& ((this.getRowSpan() == castOther.getRowSpan()) || (this
						.getRowSpan() != null && castOther.getRowSpan() != null && this
						.getRowSpan().equals(castOther.getRowSpan())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTableId() == null ? 0 : this.getTableId().hashCode());
		result = 37
				* result
				+ (getTableHeader() == null ? 0 : this.getTableHeader()
						.hashCode());
		result = 37 * result
				+ (getRowSpan() == null ? 0 : this.getRowSpan().hashCode());
		return result;
	}

}