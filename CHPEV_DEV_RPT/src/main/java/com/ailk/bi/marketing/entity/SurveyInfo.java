/**
 * 【实体类】问卷实体类   对应  表【MK_PL_SURVEY】
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
@Table(name = "MK_PL_SURVEY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SurveyInfo {
	private int surveyId;//SURVEY_ID	NUMBER	N
	private int surveyType;//SURVEY_TYPE	NUMBER	Y
	private String surveyName;//SURVEY_NAME	VARCHAR2(200)	Y
	private String creator;//CREATOR	VARCHAR2(50)	Y
	private Date createDate;//CREATE_DATE	DATE	Y
	private int state;//STATE	NUMBER	Y
	private String areaId;//AREA_ID	VARCHAR2(50)	Y
	private String countyId;//COUNTY_ID	VARCHAR2(50)	Y
	@Id
	@Column(name = "SURVEY_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_SURVEY_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SURVEY_ID")
	public int getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	@Column(name = "SURVEY_TYPE", nullable = true)
	public int getSurveyType() {
		return surveyType;
	}
	public void setSurveyType(int surveyType) {
		this.surveyType = surveyType;
	}
	@Column(name = "SURVEY_NAME", nullable = true)
	public String getSurveyName() {
		return surveyName;
	}
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
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
