package com.ailk.bi.domain.olap;

import java.io.Serializable;

public class UiRptMetaUserOlapDim implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8450854089757068301L;
	private Integer customRptId;
	private String dimId;
	private Integer displayOrder;

	public Integer getCustomRptId() {
		return customRptId;
	}

	public void setCustomRptId(Integer customRptId) {
		this.customRptId = customRptId;
	}

	public String getDimId() {
		return dimId;
	}

	public void setDimId(String dimId) {
		this.dimId = dimId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

}
