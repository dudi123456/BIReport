package com.ailk.bi.adhoc.struct;

import com.ailk.bi.common.event.JBTableBase;

public class AdhocCheckStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String key = "";

	private String desc = "";

	private String parent_key = "";

	private String parent_desc = "";

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getParent_desc() {
		return parent_desc;
	}

	public void setParent_desc(String parent_desc) {
		this.parent_desc = parent_desc;
	}

	public String getParent_key() {
		return parent_key;
	}

	public void setParent_key(String parent_key) {
		this.parent_key = parent_key;
	}

}
