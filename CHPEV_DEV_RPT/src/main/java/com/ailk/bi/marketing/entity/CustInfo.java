package com.ailk.bi.marketing.entity;

import java.util.Date;

public class CustInfo {

	private int custId;//	NUMBER	N
	private String name;//	VARCHAR2(100)	Y
	private String svcNum;//	VARCHAR2(100)	Y
	private String contactName	;//VARCHAR2(100)	Y
	private Date creatorDate;//	DATE	Y
	private String managerID;//	VARCHAR2(50)	Y
	private int state;//	NUMBER	Y
	private String countyId	;//VARCHAR2(50)	Y
	private String areaId	;//VARCHAR2(50)	Y
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSvcNum() {
		return svcNum;
	}
	public void setSvcNum(String svcNum) {
		this.svcNum = svcNum;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Date getCreatorDate() {
		return creatorDate;
	}
	public void setCreatorDate(Date creatorDate) {
		this.creatorDate = creatorDate;
	}
	public String getManagerID() {
		return managerID;
	}
	public void setManagerID(String managerID) {
		this.managerID = managerID;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

}
