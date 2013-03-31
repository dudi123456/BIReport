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
 * 【实体类】用户信息   对应  表【MK_PL_SELL_USER】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Entity
@Table(name = "MK_PL_SELL_USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SellUserInfo {

	private  int id;//主键id
	private String  servNumber;// SERV_NUMBER	VARCHAR2(11)	N			手机号码
	private String groupId;////GROUP_ID	VARCHAR2(20)	N			客户群编号
	@Id
	@Column(name = "ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_USER_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "SERV_NUMBER", nullable = true)
	public String getServNumber() {
		return servNumber;
	}
	public void setServNumber(String servNumber) {
		this.servNumber = servNumber;
	}
	@Column(name = "GROUP_ID", nullable = true)
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
