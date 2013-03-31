package com.ailk.bi.marketing.entity;

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

/**
 * 【实体类】活动实体类   对应  表【MK_PL_ACTIVITY_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Entity
@Table(name = "MK_PL_ACTIVITY_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityInfo {
	private int activityId;//	NUMBER	N
	private int clientType;//	NUMBER	Y
	private String activityName;//	VARCHAR2(50)	Y
	private String activityCode;//	VARCHAR2(50)	Y
	private int falck_id;//	NUMBER	Y
	private int activityLevel	;//NUMBER	Y
	private ProjectInfo projectInfo	;//NUMBER	Y
	private TacticInfo tacticInfo	;//NUMBER	Y
	private ChannleInfo channleInfo	;//NUMBER	Y
	private int state;//	NUMBER	Y
	private ActivityTypeInfo activityType	;//NUMBER	Y
	private String content	;//VARCHAR2(200)	Y
	private String campaingBound;//	VARCHAR2(100)	Y
	private String creator	;//VARCHAR2(50)	Y
	private Date createDate;//	DATE	Y
	private Date startData;//	DATE	Y
	private Date endDate;//	DATE	Y
	private int priority	;//NUMBER	Y
	private int activityCost;//	NUMBER	Y
	private int nameListType;//	NUMBER	Y
	private NameListInfo  nameListId	;//NUMBER	Y
	private String areaId;//	VARCHAR2(20)	Y
	private String countyId	;//VARCHAR2(20)	Y
	private int disCyc;
	private Date disDat;
	private int Wave;
	private String decider;//DECIDER

	public String cont_activityIds;
	public String cont_activityNames;

	public int showLevel;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ACTIVITY_TYPE")
	public ActivityTypeInfo getActivityType() {
		return activityType;
	}
	public void setActivityType(ActivityTypeInfo activityType) {
		this.activityType = activityType;
	}
	@Column(name = "DECIDER", nullable = true)
	public String getDecider() {
		return decider;
	}
	public void setDecider(String decider) {
		this.decider = decider;
	}
	@Column(name = "WAVE", nullable = true)
	public int getWave() {
		return Wave;
	}
	public void setWave(int wave) {
		Wave = wave;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "NAMELIST_ID")
	public NameListInfo getNameListId() {
		return nameListId;
	}
	public void setNameListId(NameListInfo nameListId) {
		this.nameListId = nameListId;
	}
	@Column(name = "DISPATCH_CYC", nullable = true)
	public int getDisCyc() {
		return disCyc;
	}
	public void setDisCyc(int disCyc) {
		this.disCyc = disCyc;
	}
	@Column(name = "DISPATCH_DATE", nullable = true)
	public Date getDisDat() {
		return disDat;
	}
	public void setDisDat(Date disDat) {
		this.disDat = disDat;
	}
	@Id
	@Column(name = "ACTIVITY_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_ACTIVITY_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACTIVITY_ID")
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	@Column(name = "CLIENT_TYPE", nullable = true)
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	@Column(name = "ACTIVITY_NAME", nullable = true)
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	@Column(name = "ACTIVITY_CODE", nullable = true)
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	@Column(name = "FLACK_ID", nullable = true)
	public int getFalck_id() {
		return falck_id;
	}
	public void setFalck_id(int falck_id) {
		this.falck_id = falck_id;
	}
	@Column(name = "ACTIVITY_LEVEL", nullable = true)
	public int getActivityLevel() {
		return activityLevel;
	}
	public void setActivityLevel(int activityLevel) {
		this.activityLevel = activityLevel;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PROJECT_ID")
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}
	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TACTIC_ID")
	public TacticInfo getTacticInfo() {
		return tacticInfo;
	}
	public void setTacticInfo(TacticInfo tacticInfo) {
		this.tacticInfo = tacticInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CHANNLE_ID")
	public ChannleInfo getChannleInfo() {
		return channleInfo;
	}
	public void setChannleInfo(ChannleInfo channleInfo) {
		this.channleInfo = channleInfo;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	@Column(name = "CONTENT", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "CAMPAIGN_BOUND", nullable = true)
	public String getCampaingBound() {
		return campaingBound;
	}
	public void setCampaingBound(String campaingBound) {
		this.campaingBound = campaingBound;
	}
	@Column(name = "CREATOR", nullable = true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name = "CREATE_DATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "SATRT_DATE", nullable = true)
	public Date getStartData() {
		return startData;
	}
	public void setStartData(Date startData) {
		this.startData = startData;
	}
	@Column(name = "END_DATE", nullable = true)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column(name = "PRIORITY", nullable = true)
	public int getPriority() {
		return priority;
	}
	public void setPriority(int prioriyt) {
		this.priority = prioriyt;
	}
	@Column(name = "ACTIVITY_COST", nullable = true)
	public int getActivityCost() {
		return activityCost;
	}
	public void setActivityCost(int activityCost) {
		this.activityCost = activityCost;
	}
	@Column(name = "NAMELIST_TYPE", nullable = true)
	public int getNameListType() {
		return nameListType;
	}
	public void setNameListType(int nameListType) {
		this.nameListType = nameListType;
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
