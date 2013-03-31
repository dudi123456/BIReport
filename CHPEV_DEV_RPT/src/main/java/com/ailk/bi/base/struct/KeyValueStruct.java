package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

public class KeyValueStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6291086746604616195L;

	public String key = ""; // 标识

	public String key_2 = ""; // 标识

	public String value = "";// 名称

	public String level = "";// 等级

	public String parent_key = "";// 等级

	public boolean istail = false;

	public KeyValueStruct(String mykey, String myvalue) {
		this.key = mykey;
		this.value = myvalue;
	}

	public KeyValueStruct(String mykey, String key2, String myvalue) {
		this.key = mykey;
		this.key_2 = key2;
		this.value = myvalue;
	}

	public KeyValueStruct() {
		key = "";
		value = "";
		level = "";
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
