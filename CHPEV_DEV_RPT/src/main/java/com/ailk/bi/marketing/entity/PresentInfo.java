/**
 * 【实体类】礼品实体类   对应  表【MK_PL_PRESENT_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
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
@Entity
@Table(name = "MK_PL_PRESENT_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PresentInfo {
	private int presentId;//	NUMBER	N
	private String presentName;//	VARCHAR2(50)	N
	private Date invaildDate	;//DATE	N
	private Date effectDate;//	DATE	N
	private Date createDate;//	DATE	N
	private String creator;//CREATOR	VARCHAR2(50)	Y
	private int state;//	NUMBER	N
	private int presentType;//	NUMBER	N
	private int presentNum;//	NUMBER	N
	private String content;//	VARCHAR2(100)	Y
	private String areaId;//	VARCHAR2(20)	Y
	private String countyId;//	VARCHAR2(20)	Y
	@Id
	@Column(name = "PRESENT_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_PRESENT_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRESENT_ID")
	public int getPresentId() {
		return presentId;
	}
	public void setPresentId(int presentId) {
		this.presentId = presentId;
	}
	@Column(name = "PRESENT_NAME", nullable = true)
	public String getPresentName() {
		return presentName;
	}
	public void setPresentName(String presentName) {
		this.presentName = presentName;
	}
	@Column(name = "INVAILD_DATE", nullable = true)
	public Date getInvaildDate() {
		return invaildDate;
	}
	public void setInvaildDate(Date invaildDate) {
		this.invaildDate = invaildDate;
	}
	@Column(name = "EFFECT_DATE", nullable = true)
	public Date getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
	@Column(name = "CREAT_DATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "CREATOR", nullable = true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "PRESENT_TYPE", nullable = true)
	public int getPresentType() {
		return presentType;
	}
	public void setPresentType(int presentType) {
		this.presentType = presentType;
	}
	@Column(name = "PRESENT_NUM", nullable = true)
	public int getPresentNum() {
		return presentNum;
	}
	public void setPresentNum(int presentNum) {
		this.presentNum = presentNum;
	}
	@Column(name = "PRESENT_CONTENT", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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

}
