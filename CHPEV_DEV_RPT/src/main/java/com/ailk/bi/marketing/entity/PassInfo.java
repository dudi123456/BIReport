/**
 * 【实体类】审批信息实体类   对应  表【MK_PL_PASS_INFO】
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
@Table(name = "MK_PL_PASS_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PassInfo {

	private int passId;//PASS_ID	NUMBER	N
	private String warnName;//WARN_NAME	VARCHAR2(50)	Y
	private String creator;//CREATOR	VARCHAR2(50)	Y
	private String advice;//ADVICE	VARCHAR2(500)	Y
	private int typeId;//TYPE_ID	NUMBER	Y
	private Date createDate;//	DATE	Y
	private int step;//STEP	NUMBER	Y
	@Id
	@Column(name = "PASS_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_PASS_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PASS_ID")
	public int getPassId() {
		return passId;
	}
	public void setPassId(int passId) {
		this.passId = passId;
	}
	@Column(name = "WARN_NAME", nullable = true)
	public String getWarnName() {
		return warnName;
	}
	public void setWarnName(String warnName) {
		this.warnName = warnName;
	}
	@Column(name = "CREATOR", nullable = true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name = "ADVICE", nullable = true)
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	@Column(name = "TYPE_ID", nullable = true)
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	@Column(name = "CREATE_DATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "STEP", nullable = true)
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}

}
