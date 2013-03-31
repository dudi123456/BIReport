package com.ailk.bi.workplatform.entity;
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
@Table(name = "MK_CH_ORDER_TRANS_HIS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransPassInfo {
	private int transPassId;//TRANS_HIS_ID	NUMBER	Y			审批流水
	private TransInfo transInfo;//TRANS_ID	NUMBER(10)	N			转派流水号
	private String pid;//P_ID	VARCHAR2(20)	N			工作流ID
	private Date pDate;//P_TIME	DATE	N			审核时间
	private int decision;//P_DECISION	NUMBER	N			1:审核通过 0:等待 2:退回
	private String stepFlag;//P_STEP_FLAG	VARCHAR2(10)	N			1开始 E结束 对应流程定义表
	private String desc;//P_STEP_FLAG_DESC	VARCHAR2(100)	Y			流程步骤标志描述
	private String stepNext;////P_STEP_NEXT	VARCHAR2(10)	N			1开始 E结束 对应流程定义表
	private UserInfo userInfo;//P_USER_ID	VARCHAR2(50)	N			审批用户ID
	private String note;//P_HIS_NOTE	VARCHAR2(400)	Y			审批备注
	@Column(name = "P_ID", nullable = true)
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	@Column(name = "P_STEP_NEXT", nullable = true)
	public String getStepNext() {
		return stepNext;
	}
	public void setStepNext(String stepNext) {
		this.stepNext = stepNext;
	}
	@Id
	@Column(name = "TRANS_HIS_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_TRANS_HIS_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRANS_HIS_ID")
	public int getTransPassId() {
		return transPassId;
	}
	public void setTransPassId(int transPassId) {
		this.transPassId = transPassId;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TRANS_ID")
	public TransInfo getTransInfo() {
		return transInfo;
	}
	public void setTransInfo(TransInfo transInfo) {
		this.transInfo = transInfo;
	}
	@Column(name = "P_TIME", nullable = true)
	public Date getpDate() {
		return pDate;
	}
	public void setpDate(Date pDate) {
		this.pDate = pDate;
	}
	@Column(name = "P_DECISION", nullable = true)
	public int getDecision() {
		return decision;
	}
	public void setDecision(int decision) {
		this.decision = decision;
	}
	@Column(name = "P_STEP_FLAG", nullable = true)
	public String getStepFlag() {
		return stepFlag;
	}
	public void setStepFlag(String stepFlag) {
		this.stepFlag = stepFlag;
	}
	@Column(name = "P_STEP_FLAG_DESC", nullable = true)
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "P_USER_ID")
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	@Column(name = "P_HIS_NOTE", nullable = true)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

}
