/**
 * 【实体类】短信类型实体类   对应  表【MK_PL_MESSAGE_TYPE】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
package com.ailk.bi.marketing.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="MK_PL_MESSAGE_TYPE")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageTypeInfo {

	private int msgTypeId;
	private String msgTypeName;
	private List<MessageInfo>msgList;

   @OneToMany
   @JoinColumn(name="MSG_TYPE_ID")
    public List<MessageInfo> getMsgList() {
		return msgList;
	}
	public void setMsgList(List<MessageInfo> msgList) {
		this.msgList = msgList;
	}
	@Id
	@Column(name="MSG_TYPE_ID",nullable=false)
	public int getMsgTypeId() {
		return msgTypeId;
	}
	public void setMsgTypeId(int msgTypeId) {
		this.msgTypeId = msgTypeId;
	}
	@Column(name="MSG_TYPE_NAME",length=50,nullable=false)
	public String getMsgTypeName() {
		return msgTypeName;
	}
	public void setMsgTypeName(String msgTypeName) {
		this.msgTypeName = msgTypeName;
	}

}
