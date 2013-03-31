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
/**
 * 【实体类】营销案例类   对应  表【MK_PL_ACTIVITY_MODE】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Entity
@Table(name = "MK_PL_ACTIVITY_MODE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityModeInfo {

	private int modeId;//ACTIVITY_MODE_ID	NUMBER	N			营销案例ID
	private int  activityId;//ACTIVITY_ID	VARCHAR2(50)	Y			活动ID
	private String content;//ACTIVITY_MODE_CONTENT	VARCHAR2(2000)	Y			备注信息
	private Date createDate;//CREATE_DATE	DATE	Y			创建时间
	@Column(name = "ACTIVITY_ID", nullable = true)
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	@Id
	@Column(name = "ACTIVITY_MODE_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_ACTIVITY_MODE_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ACTIVITY_MODE_ID")
	public int getModeId() {
		return modeId;
	}
	public void setModeId(int modeId) {
		this.modeId = modeId;
	}
	@Column(name = "ACTIVITY_MODE_CONTENT", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "CREATE_DATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
