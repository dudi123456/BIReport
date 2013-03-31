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

import com.ailk.bi.marketing.entity.ChannleInfo;
@Entity
@Table(name = "MK_CH_ORDER_TRANS_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransInfo {
	private int transId;//TRANS_ID	NUMBER(10)	N			转派流水号
	private String transName;//TRANS_NAME	VARCHAR2(100)	Y			转派申请名称
	private String transUse;//TRANS_USE	VARCHAR2(100)	Y			转派申请用途
	private UserInfo passUser;//PASS_USER	VARCHAR2(50)	Y			审批人
	private UserInfo creator;//REQUEST_PERSONAL	VARCHAR2(50)	Y			录入人
	private Date transDate;//TRANS_DATE	DATE	Y			申请时间
	private ChannleInfo oldChannle;//OLD_CHANNEL	VARCHAR2(50)	Y			申请渠道
	private ChannleInfo newChannle;//NEW_CHANNEL	VARCHAR2(50)	Y			转派渠道
	private int custType;//CUST_TYPE	VARCHAR2(10)	Y			客户类型
	private String pid;//P_ID	VARCHAR2(50)	Y			工作流ID
	private int state;//STATE	NUMBER(10)	Y			流水单状态（0:等待审批 ;1:同意；2不同意）
	private int status;//STATUS	NUMBER	Y			1有效 0无效

	public int showType;//
	public String loginUser;//登陆用户信息
	@Id
	@Column(name = "TRANS_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_TRANS_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRANS_ID")
	public int getTransId() {
		return transId;
	}
	public void setTransId(int transId) {
		this.transId = transId;
	}
	@Column(name = "TRANS_NAME", nullable = true)
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
	}
	@Column(name = "TRANS_USE", nullable = true)
	public String getTransUse() {
		return transUse;
	}
	public void setTransUse(String transUse) {
		this.transUse = transUse;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PASS_USER")
	public UserInfo getPassUser() {
		return passUser;
	}
	public void setPassUser(UserInfo passUser) {
		this.passUser = passUser;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "REQUEST_PERSONAL")
	public UserInfo getCreator() {
		return creator;
	}
	public void setCreator(UserInfo creator) {
		this.creator = creator;
	}
	@Column(name = "TRANS_DATE", nullable = true)
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "OLD_CHANNEL")
	public ChannleInfo getOldChannle() {
		return oldChannle;
	}
	public void setOldChannle(ChannleInfo oldChannle) {
		this.oldChannle = oldChannle;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "NEW_CHANNEL")
	public ChannleInfo getNewChannle() {
		return newChannle;
	}
	public void setNewChannle(ChannleInfo newChannle) {
		this.newChannle = newChannle;
	}
	@Column(name = "CUST_TYPE", nullable = true)
	public int getCustType() {
		return custType;
	}
	public void setCustType(int custType) {
		this.custType = custType;
	}
	@Column(name = "P_ID", nullable = true)
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "STATUS", nullable = true)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}


}
