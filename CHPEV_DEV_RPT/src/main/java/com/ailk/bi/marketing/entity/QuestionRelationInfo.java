/**
 * 【实体类】问题/问卷 关系 实体类   对应  表【MK_PL_QUESTION_RELATION】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
package com.ailk.bi.marketing.entity;

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
@Table(name = "MK_PL_QUESTION_RELATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QuestionRelationInfo {
	private int id;//ID	NUMBER	N
	private int surveyId;//SURVEY_ID	NUMBER	N
	private QuestionInfo questionInfo;//QUESTION_ID	NUMBER	N
	private int order;//CODER	NUMBER	N
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "QUESTION_ID")
	public QuestionInfo getQuestionInfo() {
		return questionInfo;
	}
	public void setQuestionInfo(QuestionInfo questionInfo) {
		this.questionInfo = questionInfo;
	}
	@Id
	@Column(name = "ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "SURVEY_ID", nullable = true)
	public int getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	@Column(name = "QUESTION_ORDER", nullable = true)
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}

}
