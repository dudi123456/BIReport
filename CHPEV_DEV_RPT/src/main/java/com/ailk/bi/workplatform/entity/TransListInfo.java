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
@Table(name = "MK_CH_ORDER_TRANS_LIST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransListInfo {
	private int listId;//TRANS_LIST_ID	NUMBER	N			流水号
	private int transId;//TRANS_ID	NUMBER(10)	N			转派流水号
	private int custType;//CUST_TYPE	NUMBER(10)	Y			客户类型
	private OrderInfo orderInfo;//ORDER_NO	NUMBER	Y			工单号
	//SUB_ORDER_NO	NUMBER	Y			子工单号，对政企工单
	private UserInfo updateUser;//TRANS_UPDATE_PERSONAL	VARCHAR2(50)	Y			更新人
	private Date updateDate;//;TRANS_UPDATE_TIME	DATE	Y			更新时间
	private int status;//STATUS	NUMBER	Y			1有效 0无效
	@Id
	@Column(name = "TRANS_LIST_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_TRANS_LIST_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRANS_LIST_ID")
	public int getListId() {
		return listId;
	}
	public void setListId(int listId) {
		this.listId = listId;
	}
	@Column(name = "TRANS_ID", nullable = true)
	public int getTransId() {
		return transId;
	}
	public void setTransId(int transId) {
		this.transId = transId;
	}
	@Column(name = "CUST_TYPE", nullable = true)
	public int getCustType() {
		return custType;
	}
	public void setCustType(int custType) {
		this.custType = custType;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDER_NO")
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TRANS_UPDATE_PERSONAL")
	public UserInfo getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(UserInfo updateUser) {
		this.updateUser = updateUser;
	}
	@Column(name = "TRANS_UPDATE_TIME", nullable = true)
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Column(name = "STATUS", nullable = true)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}








}
