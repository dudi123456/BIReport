package com.ailk.bi.adhoc.util;

import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;

public class AdhocBuildXlsBean {

	private String mainID;
	UiAdhocUserListTable[] mdefineInfo;

	private String oper_no;

	private String name;
	private String desc;

	private String adhocId;

	private String rcnt;
	private String cntSql;
	private String qrySql;

	public String getQrySql() {
		return qrySql;
	}

	public void setQrySql(String qrySql) {
		this.qrySql = qrySql;
	}

	private String sessId;
	private String ip;

	public UiAdhocUserListTable[] getMdefineInfo() {
		return mdefineInfo;
	}

	public void setMdefineInfo(UiAdhocUserListTable[] mdefineInfo) {
		this.mdefineInfo = mdefineInfo;
	}

	public String getMainID() {
		return mainID;
	}

	public void setMainID(String mainID) {
		this.mainID = mainID;
	}

	public String getOper_no() {
		return oper_no;
	}

	public void setOper_no(String operNo) {
		oper_no = operNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAdhocId() {
		return adhocId;
	}

	public void setAdhocId(String adhocId) {
		this.adhocId = adhocId;
	}

	public String getRcnt() {
		return rcnt;
	}

	public void setRcnt(String rcnt) {
		this.rcnt = rcnt;
	}

	public String getCntSql() {
		return cntSql;
	}

	public void setCntSql(String cntSql) {
		this.cntSql = cntSql;
	}

	public String getSessId() {
		return sessId;
	}

	public void setSessId(String sessId) {
		this.sessId = sessId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
