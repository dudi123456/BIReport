package com.ailk.bi.marketing.entity;
/**
 * 【实体类】营销手段实体类   对应  表【MK_PL_MEANS_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
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
@Table(name = "MK_PL_MEANS_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MeansInfo {
	private int meansId;//	NUMBER	N			营销手段ID
	private String meansName;//	VARCHAR2(50)	Y			营销手段名称
	private String meansContent;//	VARCHAR2(200)	Y			营销手段描述
	private int resourceBigType	;//NUMBER	Y			资源大类（实体。口头）
	private int resourceType;//	NUMBER	Y			资源类型
	private String resourceId;//	VARCHAR2(50)	Y			资源值ID
	private String creator	;//VARCHAR2(50)	Y			创建人
	private Date	createDate;//	DATE	Y			创建时间
	private Date startDate;//	DATE	Y			开始时间
	private Date endDate;//	DATE	Y			结束时间
	private int state	;//NUMBER	Y			状态
	private String areaId	;//VARCHAR2(20)	Y
	private String countyId	;//VARCHAR2(20)	Y
	private int tacticId	;//NUMBER	Y			营销策略ID
	@Id
	@Column(name = "MEANS_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_MEANS_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MEANS_ID")
	public int getMeansId() {
		return meansId;
	}
	public void setMeansId(int meansId) {
		this.meansId = meansId;
	}
	@Column(name = "MEANS_NAME",  nullable = true)
	public String getMeansName() {
		return meansName;
	}
	public void setMeansName(String meansName) {
		this.meansName = meansName;
	}
	@Column(name = "MEANS_CONTENT",  nullable = true)
	public String getMeansContent() {
		return meansContent;
	}
	public void setMeansContent(String meansContent) {
		this.meansContent = meansContent;
	}
	@Column(name = "RESOURCE_BIG_TYPE",  nullable = true)
	public int getResourceBigType() {
		return resourceBigType;
	}
	public void setResourceBigType(int resourceBigType) {
		this.resourceBigType = resourceBigType;
	}
	@Column(name = "RESOURCE_TYPE",  nullable = true)
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	@Column(name = "RESOURCE_ID",  nullable = true)
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	@Column(name = "CREATOR",  nullable = true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name = "CREATE_DATE",  nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "START_DATE",  nullable = true)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name = "END_DATE",  nullable = true)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column(name = "STATE",  nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "AREA_ID",  nullable = true)
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	@Column(name = "COUNTY_ID",  nullable = true)
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	@Column(name = "TACTIC_ID",  nullable = true)
	public int getTacticId() {
		return tacticId;
	}
	public void setTacticId(int tacticId) {
		this.tacticId = tacticId;
	}
}
