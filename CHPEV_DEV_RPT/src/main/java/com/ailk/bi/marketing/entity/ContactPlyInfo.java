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
 * 【实体类】接触规则实体类   对应  表【MK_PL_CONTACT_PLY】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Entity
@Table(name = "MK_PL_CONTACT_PLY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContactPlyInfo {
	private int contactId;//	CONTACT_ID	NUMBER	N
	private int tacticId;//	TACTIC_ID	NUMBER	Y
	private int step;//	CONTACT_STEP	NUMBER	Y
	private	int mode;//CONTACT_MODE	NUMBER	Y
	private int contactTime;//	CONTACT_TIME	DATE	Y
	private int scriptId;//	SCRIPT_ID	NUMBER	Y
	private int contactWay;//	CONTACT_WAY	NUMBER	Y
	private String content;//	CONTACT_CONTENT	VARCHAR2(100)	Y
	private String 	areaId;//AREA_ID	VARCHAR2(20)	Y
	private String 	countyId;//COUNTY_ID	VARCHAR2(20)	Y
	private int contactDay;// 	CONTACT_DAY	NUMBER	Y
	@Id
	@Column(name = "CONTACT_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_CONTACT_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTACT_ID")
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	@Column(name = "TACTIC_ID", nullable = true)
	public int getTacticId() {
		return tacticId;
	}
	public void setTacticId(int tacticId) {
		this.tacticId = tacticId;
	}
	@Column(name = "CONTACT_STEP", nullable = true)
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	@Column(name = "CONTACT_MODE", nullable = true)
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	@Column(name = "CONTACT_TIME", nullable = true)
	public int getContactTime() {
		return contactTime;
	}
	public void setContactTime(int contactTime) {
		this.contactTime = contactTime;
	}
	@Column(name = "SCRIPT_ID", nullable = true)
	public int getScriptId() {
		return scriptId;
	}
	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}
	@Column(name = "CONTACT_WAY", nullable = true)
	public int getContactWay() {
		return contactWay;
	}
	public void setContactWay(int contactWay) {
		this.contactWay = contactWay;
	}
	@Column(name = "CONTACT_CONTENT", nullable = true)
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
	@Column(name = "CONTACT_DAY", nullable = true)
	public int getContactDay() {
		return contactDay;
	}
	public void setContactDay(int contactDay) {
		this.contactDay = contactDay;
	}
}
