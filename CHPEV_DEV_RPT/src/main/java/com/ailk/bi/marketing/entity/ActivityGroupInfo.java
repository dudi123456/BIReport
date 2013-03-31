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
 * 【实体类】活动  客户名单 类   对应  表【MK_PL_ACTIVITY_GRUOP】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Entity
@Table(name = "MK_PL_ACTIVITY_GRUOP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityGroupInfo {
	private int id;//ID	NUMBER	N
	private int activityId;//ACTIVITY_ID	NUMBER	Y
	private String groupId;//GROUP_ID	NUMBER	Y
	private int groupTypeId;//	NUMBER	Y
	private int numCount;
	@Column(name = "NUM_COUNT", nullable = true)
	public int getNumCount() {
		return numCount;
	}
	public void setNumCount(int numCount) {
		this.numCount = numCount;
	}
	@Column(name = "GROUP_ID", nullable = true)
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@Id
	@Column(name = "ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_ID_001", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ID_001")
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
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	@Column(name = "GROUP_TYPE_ID", nullable = true)
	public int getGroupTypeId() {
		return groupTypeId;
	}
	public void setGroupTypeId(int groupTypeId) {
		this.groupTypeId = groupTypeId;
	}

}
