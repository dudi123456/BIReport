package com.ailk.bi.marketing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 【实体类】活动实体类   对应  表【MK_PL_NAME_LIST】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Entity
@Table(name = "MK_PL_NAME_LIST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NameListInfo {

private int 	nameListId	;//NUMBER	N
private String 	nameListName;//	VARCHAR2(100)	Y
private String countyId;//	VARCHAR2(50)	Y
private String areaId;//	VARCHAR2(50)	Y
private int state;//	NUMBER	Y
private int nameListType;//	NUMBER	Y
private String remark	;//VARCHAR2(300)	Y
private int clientType	;//NUMBER	Y
@Id
@Column(name = "NAMELIST_ID", length = 100, nullable = false)
@SequenceGenerator(name = "SEQ_NAMELIST_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NAMELIST_ID")
public int getNameListId() {
	return nameListId;
}
public void setNameListId(int nameListId) {
	this.nameListId = nameListId;
}
@Column(name = "NAMELIST_NAME", nullable = true)
public String getNameListName() {
	return nameListName;
}
public void setNameListName(String nameListName) {
	this.nameListName = nameListName;
}
@Column(name = "COUNTY_ID", nullable = true)
public String getCountyId() {
	return countyId;
}
public void setCountyId(String countyId) {
	this.countyId = countyId;
}
@Column(name = "AREA_ID", nullable = true)
public String getAreaId() {
	return areaId;
}
public void setAreaId(String areaId) {
	this.areaId = areaId;
}
@Column(name = "STATE", nullable = true)
public int getState() {
	return state;
}
public void setState(int state) {
	this.state = state;
}
@Column(name = "NAMELIST_TYPE", nullable = true)
public int getNameListType() {
	return nameListType;
}
public void setNameListType(int nameListNameType) {
	this.nameListType = nameListNameType;
}
@Column(name = "REMARK", nullable = true)
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
@Column(name = "CLIENT_TYPE", nullable = true)
public int getClientType() {
	return clientType;
}
public void setClientType(int clientType) {
	this.clientType = clientType;
}
}
