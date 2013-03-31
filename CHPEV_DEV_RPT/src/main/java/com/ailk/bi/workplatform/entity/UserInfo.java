package com.ailk.bi.workplatform.entity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ailk.bi.marketing.entity.ChannleInfo;
@Entity
@Table(name = "UI_INFO_USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserInfo {
	private String userId;
	private String userName;
	private DeptInfo deptInfo;
	private String dutyId;
	private ChannleInfo channleInfo;
	@Column(name = "DUTY_ID", nullable = true)
	public String getDutyId() {
		return dutyId;
	}
	public void setDutyId(String dutyId) {
		this.dutyId = dutyId;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CHANNLE_ID")
	public ChannleInfo getChannleInfo() {
		return channleInfo;
	}
	public void setChannleInfo(ChannleInfo channleInfo) {
		this.channleInfo = channleInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "DEPT_ID")
	public DeptInfo getDeptInfo() {
		return deptInfo;
	}
	public void setDeptInfo(DeptInfo deptInfo) {
		this.deptInfo = deptInfo;
	}
	@Id
	@Column(name = "USER_ID", nullable = true)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "USER_NAME", nullable = true)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}


}
