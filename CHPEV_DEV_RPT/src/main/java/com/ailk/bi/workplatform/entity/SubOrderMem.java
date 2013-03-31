package com.ailk.bi.workplatform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "MK_CH_SUBORDER_MEM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SubOrderMem {

	private int sub_order_no;// NUMBER N
	private int order_no;// NUMBER N
	private String subcust_id;// VARCHAR2(20) Y
	private String subcust_name;// VARCHAR2(50) Y
	private String user_id;// VARCHAR2(20) Y
	private String serv_number;// VARCHAR2(20) Y
	private String cust_svc_mgr_id;// VARCHAR2(50) Y
	private int state;// NUMBER Y
	private Date createdate;// DATE Y

	@Id
	@Column(name = "SUB_ORDER_NO", length = 10, nullable = false)
	public int getSub_order_no() {
		return sub_order_no;
	}
	public void setSub_order_no(int sub_order_no) {
		this.sub_order_no = sub_order_no;
	}
	@Column(name = "ORDER_NO", nullable = false)
	public int getOrder_no() {
		return order_no;
	}
	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}
	@Column(name = "SUBCUST_ID", nullable = true)
	public String getSubcust_id() {
		return subcust_id;
	}
	public void setSubcust_id(String subcust_id) {
		this.subcust_id = subcust_id;
	}
	@Column(name = "SUBCUST_NAME", nullable = true)
	public String getSubcust_name() {
		return subcust_name;
	}
	public void setSubcust_name(String subcust_name) {
		this.subcust_name = subcust_name;
	}
	@Column(name = "USER_ID", nullable = true)
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Column(name = "SERV_NUMBER", nullable = true)
	public String getServ_number() {
		return serv_number;
	}
	public void setServ_number(String serv_number) {
		this.serv_number = serv_number;
	}
	@Column(name = "CUST_SVC_MGR_ID", nullable = true)
	public String getCust_svc_mgr_id() {
		return cust_svc_mgr_id;
	}
	public void setCust_svc_mgr_id(String cust_svc_mgr_id) {
		this.cust_svc_mgr_id = cust_svc_mgr_id;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "CREATEDATE", nullable = true)
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
}
