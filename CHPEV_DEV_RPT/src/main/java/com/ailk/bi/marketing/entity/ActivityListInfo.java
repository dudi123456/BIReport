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
 * 【实体类】互斥活动实体类   对应  表【MK_PL_ACTIVITY_LIST】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Entity
@Table(name = "MK_PL_ACTIVITY_LIST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityListInfo {

	private int id;//	NUMBER	N
	private int activityId;//	NUMBER	Y
	private int  conActivityId;
	@Id
	@Column(name = "ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_ACTIVITY_LIST_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACTIVITY_LIST_ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "ACTIVITY_ID", nullable = true)
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityInfo) {
		this.activityId = activityInfo;
	}
	@Column(name = "CON_ACTIVITY_ID", nullable = true)
	public int getConActivityId() {
		return conActivityId;
	}
	public void setConActivityId(int conActivityId) {
		this.conActivityId = conActivityId;
	}
}
