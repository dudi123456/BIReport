package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocInfoDefTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String adhoc_id = "";

	private String adhoc_name = "";

	private String adhoc_desc = "";

	private String status = "";

	private String data_table = "";

	private int isGroup = 0;

	private int isSplit = 0;

	private String prefixTbl = "";

	private int accountNum = 0;

	private String conId = "";

	private int adhocType = 0;

	public int getAdhocType() {
		return adhocType;
	}

	public void setAdhocType(int adhocType) {
		this.adhocType = adhocType;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public int getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(int isSplit) {
		this.isSplit = isSplit;
	}

	public String getPrefixTbl() {
		return prefixTbl;
	}

	public void setPrefixTbl(String prefixTbl) {
		this.prefixTbl = prefixTbl;
	}

	public int getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}

	public int getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(int isGroup) {
		this.isGroup = isGroup;
	}

	public String getAdhoc_desc() {
		return adhoc_desc;
	}

	public void setAdhoc_desc(String adhoc_desc) {
		this.adhoc_desc = adhoc_desc;
	}

	public String getAdhoc_id() {
		return adhoc_id;
	}

	public void setAdhoc_id(String adhoc_id) {
		this.adhoc_id = adhoc_id;
	}

	public String getAdhoc_name() {
		return adhoc_name;
	}

	public void setAdhoc_name(String adhoc_name) {
		this.adhoc_name = adhoc_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getData_table() {
		return data_table;
	}

	public void setData_table(String data_table) {
		this.data_table = data_table;
	}

}
