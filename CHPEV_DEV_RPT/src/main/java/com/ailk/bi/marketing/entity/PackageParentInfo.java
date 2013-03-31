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
@Table(name = "MK_PL_PACKAGE_PARENT_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PackageParentInfo {
	private int packageId;
	private String packageName;
	private Date createDate;//		DATE	Y			创建时间
	private Date effectDate;//		DATE	Y			生效时间
	private Date invaildDate;
	private int state;
	private int  parentPackageId;
	private String packageContent;
	private int packageType;
	private int productType;
	private int voiceType;
	private int custGroup;
	private int priceMode;
	private String freeProject;
	private Date packageDate;
	private int channleId;
	private String areaId;
	private int countryId;
	private String creator;

	@Id
	@Column(name = "PACKAGE_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_PACKAGE_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PACKAGE_ID")
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	@Column(name = "PACKAGE_NAME", nullable = true)
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
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

	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "PARENT_PACKAGE_ID", nullable = true)
	public int getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(int parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	@Column(name = "PACKAGE_CONTENT", nullable = true)
	public String getPackageContent() {
		return packageContent;
	}
	public void setPackageContent(String packageContent) {
		this.packageContent = packageContent;
	}

	@Column(name = "PACKAGE_TYPE", nullable = true)
	public int getPackageType() {
		return packageType;
	}
	public void setPackageType(int packageType) {
		this.packageType = packageType;
	}

	@Column(name = "PRODUCT_TYPE", nullable = true)
	public int getProductType() {
		return productType;
	}
	public void setProductType(int productType) {
		this.productType = productType;
	}

	@Column(name = "VOICE_TYPE", nullable = true)
	public int getVoiceType() {
		return voiceType;
	}
	public void setVoiceType(int voiceType) {
		this.voiceType = voiceType;
	}

	@Column(name = "CUST_GROUP", nullable = true)
	public int getCustGroup() {
		return custGroup;
	}
	public void setCustGroup(int custGroup) {
		this.custGroup = custGroup;
	}

	@Column(name = "PRICE_MODE", nullable = true)
	public int getPriceMode() {
		return priceMode;
	}
	public void setPriceMode(int priceMode) {
		this.priceMode = priceMode;
	}

	@Column(name = "FEE_PROJECT", nullable = true)
	public String getFreeProject() {
		return freeProject;
	}
	public void setFreeProject(String freeProject) {
		this.freeProject = freeProject;
	}

	@Column(name = "PACKAGE_DATE", nullable = true)
	public Date getPackageDate() {
		return packageDate;
	}
	public void setPackageDate(Date packageDate) {
		this.packageDate = packageDate;
	}

	@Column(name = "CHANNLE_ID", nullable = true)
	public int getChannleId() {
		return channleId;
	}
	public void setChannleId(int channleId) {
		this.channleId = channleId;
	}

	@Column(name = "AREA_ID", nullable = true)
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	@Column(name = "COUNTY_ID", nullable = true)
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	@Column(name = "CREATOR", nullable = true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}


}
