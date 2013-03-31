package com.ailk.bi.marketing.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * 【实体类】营销策略实体类   对应  表【MK_PL_TACTIC_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Entity
@Table(name = "MK_PL_TACTIC_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TacticInfo {

	private int tacticId;	//NUMBER	N			策略id
	private String tacticName;//	VARCHAR2(50)	Y			策略名称
	private int tacticType;//	NUMBER	Y			策略类型
	private int saledetain_method;//	NUMBER	Y			营销方式
	private String advise	;//VARCHAR2(100)	Y			营销建议
	private String content;//	VARCHAR2(200)	Y			描述
	private Date startDate;//	DATE	Y			开始时间
	private Date endDate	;//DATE	Y			结束时间
	private String  creator	;//VARCHAR2(100)	Y			创建人
	private Date createDate	;//DATE	Y			创建时间
	private int  client_type;//	NUMBER	Y			客户类型
	private String  decider	;//VARCHAR2(50)	Y			审批人
	private int 	state;//NUMBER	Y			状态
	private int channleId;//	NUMBER	Y			渠道ID
	private int leashCyc;//	NUMBER	Y			建议接触周期
	private int leashNum	;//NUMBER	Y			建议接触次数
	private String areaId	;//VARCHAR2(20)	Y
	private String countyId;//	VARCHAR2(20)	Y
	private List<MeansInfo> meansList = new ArrayList<MeansInfo>() ;

	@OneToMany(mappedBy="meansId")
	public List<MeansInfo> getMeansList() {
		return meansList;
	}
	public void setMeansList(List<MeansInfo> meansList) {
		this.meansList = meansList;
	}
	@Id
	@Column(name = "TACTIC_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_TACTIC_INFO", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TACTIC_INFO")
	public int getTacticId() {
		return tacticId;
	}
	public void setTacticId(int tacticId) {
		this.tacticId = tacticId;
	}
	@Column(name = "TACTIC_NAME",  nullable = true)
	public String getTacticName() {
		return tacticName;
	}
	public void setTacticName(String tacticName) {
		this.tacticName = tacticName;
	}
	@Column(name = "TACTIC_TYPE",  nullable = true)
	public int getTacticType() {
		return tacticType;
	}
	public void setTacticType(int tacticType) {
		this.tacticType = tacticType;
	}
	@Column(name = "SALEDETAIN_METHOD",  nullable = true)
	public int getSaledetain_method() {
		return saledetain_method;
	}
	public void setSaledetain_method(int saledetain_method) {
		this.saledetain_method = saledetain_method;
	}
	@Column(name = "ADVISE",  nullable = true)
	public String getAdvise() {
		return advise;
	}
	public void setAdvise(String advise) {
		this.advise = advise;
	}
	@Column(name = "CONTENT",  nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	@Column(name = "CLIENT_TYPE",  nullable = true)
	public int getClient_type() {
		return client_type;
	}
	public void setClient_type(int client_type) {
		this.client_type = client_type;
	}
	@Column(name = "DECIDER",  nullable = true)
	public String getDecider() {
		return decider;
	}
	public void setDecider(String decider) {
		this.decider = decider;
	}
	@Column(name = "STATE",  nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "CHANNLE_ID",  nullable = true)
	public int getChannleId() {
		return channleId;
	}
	public void setChannleId(int channleId) {
		this.channleId = channleId;
	}
	@Column(name = "LEASH_CYC",  nullable = true)
	public int getLeashCyc() {
		return leashCyc;
	}
	public void setLeashCyc(int leashCyc) {
		this.leashCyc = leashCyc;
	}
	@Column(name = "LEASH_NUM",  nullable = true)
	public int getLeashNum() {
		return leashNum;
	}
	public void setLeashNum(int leashNum) {
		this.leashNum = leashNum;
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
}
