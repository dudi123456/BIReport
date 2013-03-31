package com.ailk.bi.marketing.entity;
/**
 * 【实体类】营维方案实体类   对应  表【MK_PL_PROJECT_INFO】
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
@Table(name = "MK_PL_PROJECT_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectInfo {

	private int projectId;//	NUMBER	N			方案ID
	private String projectName	;//VARCHAR2(50)	Y			方案名称
	private int projectType;//	NUMBER	Y			方案类型
	private int projectLevel;//	NUMBER	Y			方案深度
	private String creator;//	VARCHAR2(50)	Y			创建人
	private TacticInfo TacticInfo;//	NUMBER	Y			策略ID
	private String projectContent;//	VARCHAR2(200)	Y			方案内容
	private ChannleInfo channleInfo;//	NUMBER	Y			渠道ID
	private int clientType	;//NUMBER	Y			客户类型
	private Date createDate	;//DATE	Y			创建时间
	private Date InvaildDate;//	DATE	Y			失效时间
	private Date effectDate;//	DATE	Y			生效时间
	private int priority	;//NUMBER	Y			优先级
	private int state	;//NUMBER	Y			状态
	private String areaId	;//VARCHAR2(20)	Y
	private String countyId	;//VARCHAR2(20)	Y
	private String warnName;//WARN_NAME
	@Column(name = "WARN_NAME", nullable = true)
	public String getWarnName() {
		return warnName;
	}
	public void setWarnName(String warnName) {
		this.warnName = warnName;
	}
	@Id
	@Column(name = "PROJECT_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_PROJECT_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PROJECT_ID")
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	@Column(name = "PROJECT_NAME", nullable = true)
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@Column(name = "PROJECT_TYPE", nullable = true)
	public int getProjectType() {
		return projectType;
	}
	public void setProjectType(int projectType) {
		this.projectType = projectType;
	}
	@Column(name = "PROJECT_LEVEL", nullable = true)
	public int getProjectLevel() {
		return projectLevel;
	}
	public void setProjectLevel(int projectLevel) {
		this.projectLevel = projectLevel;
	}
	@Column(name = "CREATOR", nullable = true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TACTIC_ID")
	public TacticInfo getTacticInfo() {
		return TacticInfo;
	}
	public void setTacticInfo(TacticInfo tacticInfo) {
		TacticInfo = tacticInfo;
	}
	@Column(name = "PROJECT_CONTENT", nullable = true)
	public String getProjectContent() {
		return projectContent;
	}
	public void setProjectContent(String projectContent) {
		this.projectContent = projectContent;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CHANNLE_ID")
	public ChannleInfo getChannleInfo() {
		return channleInfo;
	}
	public void setChannleInfo(ChannleInfo channleInfo) {
		this.channleInfo = channleInfo;
	}
	@Column(name = "CLIENT_TYPE", nullable = true)
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	@Column(name = "CREATE_DATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "INVAILD_DATE", nullable = true)
	public Date getInvaildDate() {
		return InvaildDate;
	}
	public void setInvaildDate(Date invaildDate) {
		InvaildDate = invaildDate;
	}
	@Column(name = "EFFECT_DATE", nullable = true)
	public Date getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}
	@Column(name = "PRIORITY", nullable = true)
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
