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
 * 【实体类】营销政策实体类   对应  表【MK_PL_POLICY_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Entity
@Table(name = "MK_PL_POLICY_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PolicyInfo {
	private int policyId;// POLICY_ID	NUMBER	N
	private String policyName;//POLICY_NAME	VARCHAR2(50)	N
	private int policyType;//	POLICY_TYPE	NUMBER	N
	private Date effectDate;//EFFECT_DATE	DATE	N
	private Date invaildDate;//INVAILD_DATE	DATE	N
	private Date createDate;//CREATE_DATE	DATE	N
	private String creator;//CREATOR	VARCHAR2(50)	Y
	private int state;//STATE	NUMBER	N
	private String content;//POLICY_CONTENT	VARCHAR2(100)	Y
	private String areaId;//AREA_ID	VARCHAR2(20)	Y
	private String countyId;//COUNTY_ID	VARCHAR2(20)	Y
	@Id
	@Column(name = "POLICY_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_POLICY_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POLICY_ID")
	public int getPolicyId() {
		return policyId;
	}
	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}
	@Column(name = "POLICY_NAME", nullable = true)
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyNanme) {
		this.policyName = policyNanme;
	}
	@Column(name = "POLICY_TYPE", nullable = true)
	public int getPolicyType() {
		return policyType;
	}
	public void setPolicyType(int policyType) {
		this.policyType = policyType;
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
	@Column(name = "CREATE_DATE", nullable = true)
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
	@Column(name = "POLICY_CONTENT", nullable = true)
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
