package com.ailk.bi.marketing.entity;

import java.util.Date;

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
 * 【实体类】活动实体类   对应  表【MK_PL_ACTIVITY_TYPE】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Entity
@Table(name = "MK_PL_ACTIVITY_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityTypeInfo {
	private int activityTypeId;//NUMBER	N			渠道类型ID
	private String activityTypeName;//	VARCHAR2(50)	Y			渠道类型名称
	private int contactCount;//NUMBER	Y			最大接触数
	private int state;//	NUMBER	Y			状态
	private Date setTime;//	DATE	Y			设置时间
	private String areaId;//	VARCHAR2(20)	Y
	private String countyId;//	VARCHAR2(20)	Y
	private String typeDesc;// VARCHAR2(200)           描述说明

	@Id
	@Column(name = "ACTIVITY_TYPE_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_ACTIVITY_TYPE_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACTIVITY_TYPE_ID")
	public int getActivityTypeId() {
		return activityTypeId;
	}
	public void setActivityTypeId(int activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	@Column(name = "ACTIVITY_TYPE_NAME", nullable = true)
	public String getActivityTypeName() {
		return activityTypeName;
	}
	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	@Column(name = "CONTACT_COUNT", nullable = true)
	public int getContactCount() {
		return contactCount;
	}
	public void setContactCount(int contactCount) {
		this.contactCount = contactCount;
	}

	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "SET_TIME", nullable = true)
	public Date getSetTime() {
		return setTime;
	}
	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}

	@Column(name = "AREA_ID", nullable = true)
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	@Column(name = "COUNTY_ID", nullable = true)
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	@Column(name = "TYPE_DESC", nullable = true)
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

}
