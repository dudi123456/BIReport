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
@Table(name = "MK_CH_ORDER_ADJUST_LIST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderAdjustInfo {
	private int adjustId;//ADJUST_LIST_ID	NUMBER	N			流水号
	private int orderId;//ORDER_NO	NUMBER(10)	Y			工单号
	private ChannleInfo channleInfo;//CHANNEL_ID	VARCHAR2(50)	Y			执行渠道
	private UserInfo performer;//PERFORMER_ID	VARCHAR2(50)	Y			执行人
	private int oldChannler;//OLD_CHANNEL_ID	VARCHAR2(50)	Y			原执行渠道
	private UserInfo oldperformer;//OLD_PERFORMER_ID	VARCHAR2(50)	Y			原执行人
	private UserInfo updatePersonal;//UPDATE_PERSONAL	VARCHAR2(50)	Y			更新人
	private Date updateTime;//UPDATE_TIME	DATE	Y			更新时间
	private int status;//STATUS	NUMBER	Y			1有效 0无效
	@Id
	@Column(name = "ADJUST_LIST_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_ADJUST_LIST_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADJUST_LIST_ID")
	public int getAdjustId() {
		return adjustId;
	}
	public void setAdjustId(int adjustId) {
		this.adjustId = adjustId;
	}
	@Column(name = "ORDER_NO", nullable = false)
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CHANNEL_ID")
	public ChannleInfo getChannleInfo() {
		return channleInfo;
	}
	public void setChannleInfo(ChannleInfo channleInfo) {
		this.channleInfo = channleInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PERFORMER_ID")
	public UserInfo getPerformer() {
		return performer;
	}
	public void setPerformer(UserInfo performer) {
		this.performer = performer;
	}
	@Column(name = "OLD_CHANNEL_ID", nullable = false)
	public int getOldChannler() {
		return oldChannler;
	}
	public void setOldChannler(int oldChannler) {
		this.oldChannler = oldChannler;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "OLD_PERFORMER_ID")
	public UserInfo getOldperformer() {
		return oldperformer;
	}
	public void setOldperformer(UserInfo oldperformer) {
		this.oldperformer = oldperformer;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "UPDATE_PERSONAL")
	public UserInfo getUpdatePersonal() {
		return updatePersonal;
	}
	public void setUpdatePersonal(UserInfo updatePersonal) {
		this.updatePersonal = updatePersonal;
	}
	@Column(name = "UPDATE_TIME", nullable = false)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "STATUS", nullable = false)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
