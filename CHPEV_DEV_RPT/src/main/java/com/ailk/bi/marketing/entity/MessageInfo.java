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
 * 【实体类】短信模板实体类   对应  表【MK_PL_MESSAGE】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Entity
@Table(name = "MK_PL_MESSAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageInfo {

	private int msgId;
	private int msgType;
	private Date createTime;
	private String creator;
	private int msgState;
	private String warnName;
	private String content;
	@Id
	@Column(name = "MSG_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_MSG_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MSG_ID")
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	@Column(name = "MSG_TYPE", nullable = true)
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	@Column(name = "CREATE_TIME", nullable = true)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "CREATOR", nullable = true)
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name = "MSG_STATE", nullable = true)
	public int getMsgState() {
		return msgState;
	}
	public void setMsgState(int msgState) {
		this.msgState = msgState;
	}
	@Column(name = "WARN_NAME", nullable = true)
	public String getWarnName() {
		return warnName;
	}
	public void setWarnName(String warnName) {
		this.warnName = warnName;
	}
	@Column(name = "MSG_CONTENT", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
