package com.ailk.bi.report.struct;

import com.ailk.bi.common.event.JBTableBase;

public class MultiColDataStruct extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String row_no;

	public String getRow_no() {
		return row_no;
	}

	public void setRow_no(String row_no) {
		this.row_no = row_no;
	}

	public String getItem_desc() {
		return item_desc;
	}

	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}

	public String getItem_value() {
		return item_value;
	}

	public void setItem_value(String item_value) {
		this.item_value = item_value;
	}

	public String getItem_2_desc() {
		return item_2_desc;
	}

	public void setItem_2_desc(String item_2_desc) {
		this.item_2_desc = item_2_desc;
	}

	public String getItem_2_value() {
		return item_2_value;
	}

	public void setItem_2_value(String item_2_value) {
		this.item_2_value = item_2_value;
	}

	private String item_desc;
	private String item_value;
	private String item_2_desc;
	private String item_2_value;

}
