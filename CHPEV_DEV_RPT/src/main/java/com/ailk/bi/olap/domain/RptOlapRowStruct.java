package com.ailk.bi.olap.domain;

public class RptOlapRowStruct implements Cloneable {
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 *
	 * 唯一行号，不随行的位置改变而改变 level_(值)_dim_value组成
	 */
	private String row_id = null;

	/**
	 * 代表每一行的左边维度部分HTML代码 展开时要重新设置其图片和链接 即一行
	 * <tr>
	 * </tr>
	 */
	private String leftPart = null;

	/**
	 * 代表每一行的右边指标部分，
	 * <tr>
	 * </tr>
	 */
	private String rightPart = null;

	/**
	 * 指标的占比HTML代码部分
	 */
	private String rightPerPart = null;

	/**
	 * 指标的同比HTML代码部分
	 */
	private String rightSameRatioPart = null;

	/**
	 * 指标的环比HMTL代码部分
	 */
	private String rightLastRatioPart = null;

	public String getLeftPart() {
		return leftPart;
	}

	public void setLeftPart(String leftPart) {
		this.leftPart = leftPart;
	}

	public String getRightPart() {
		return rightPart;
	}

	public void setRightPart(String rightPart) {
		this.rightPart = rightPart;
	}

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	public String getRightLastRatioPart() {
		return rightLastRatioPart;
	}

	public void setRightLastRatioPart(String rightLastRatioPart) {
		this.rightLastRatioPart = rightLastRatioPart;
	}

	public String getRightPerPart() {
		return rightPerPart;
	}

	public void setRightPerPart(String rightPerPart) {
		this.rightPerPart = rightPerPart;
	}

	public String getRightSameRatioPart() {
		return rightSameRatioPart;
	}

	public void setRightSameRatioPart(String rightSameRatioPart) {
		this.rightSameRatioPart = rightSameRatioPart;
	}
}
