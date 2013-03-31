package com.ailk.bi.marketing.entity;
/**
 * 【实体类】渠道实体类   对应  表【MK_PL_CHANNLE_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Table(name = "MK_PL_CHANNLE_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChannleInfo {

	private int channleId;//	NUMBER	N			渠道编号
	private ChannleTypeInfo channleType	;//	NUMBER	Y			渠道类型
	private String channleName;//		VARCHAR2(50)	Y			渠道名称
	private int state;//		NUMBER	Y			状态
	private Date createDate;//		DATE	Y			创建时间
	private Date effectDate;//		DATE	Y			生效时间
	private Date invaildDate;//		DATE	Y			失效时间
	private String channleCode;//		VARCHAR2(50)	Y			渠道编码
	private String areaId;//		VARCHAR2(20)	Y
	private String countyId;//		VARCHAR2(20)	Y
	private String channle_desc;//   VARCHAR2(200)描述

	@Id
	@Column(name = "CHANNLE_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_CHANNLE_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHANNLE_ID")
	public int getChannleId() {
		return channleId;
	}
	public void setChannleId(int channleId) {
		this.channleId = channleId;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CHANNLE_TYPE_ID")
	public ChannleTypeInfo getChannleType() {
		return channleType;
	}
	public void setChannleType(ChannleTypeInfo channleType) {
		this.channleType = channleType;
	}
	@Column(name = "CHANNLE_NAME", nullable = true)
	public String getChannleName() {
		return channleName;
	}
	public void setChannleName(String channleName) {
		this.channleName = channleName;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "CREATE_DATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "EFFECT_DATE", nullable = true)
	public Date getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
	@Column(name = "INVAILD_DATE", nullable = true)
	public Date getInvaildDate() {
		return invaildDate;
	}
	public void setInvaildDate(Date invaildDate) {
		this.invaildDate = invaildDate;
	}
	@Column(name = "CHANNLE_CODE", nullable = true)
	public String getChannleCode() {
		return channleCode;
	}
	public void setChannleCode(String channleCode) {
		this.channleCode = channleCode;
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
	@Column(name = "CHANNLE_DESC", nullable = true)
	public String getChannle_desc() {
		return channle_desc;
	}
	public void setChannle_desc(String channle_desc) {
		this.channle_desc = channle_desc;
	}


}
