package com.ailk.bi.marketing.entity;

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

/**
 * 【实体类】策略指标对用实体类   对应  表【MK_PL_TARGET_TACTIC】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Entity
@Table(name = "MK_PL_TARGET_TACTIC")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TargetOpInfo {

private int id;//	ID	NUMBER	N			主键id
	private int  tacticId;//TACTIC_ID	NUMBER	Y			策略ID
	private  TargetInfo targetInfo ;///TARGET_ID	NUMBER	Y			指标ID
	private int opType;// OP_TYPE	NUMBER	Y			操作符
	private int  opValue;//OP_VALUE	NUMBER	Y			指标值
	private String remark;//REMARK	VARCHAR2(500)	Y			备注说明
	@Id
	@Column(name = "ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_OP_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_OP_ID")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "TACTIC_ID", nullable = true)
	public int getTacticId() {
		return tacticId;
	}
	public void setTacticId(int tacticId) {
		this.tacticId = tacticId;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TARGET_ID")
	public TargetInfo getTargetInfo() {
		return targetInfo;
	}
	public void setTargetInfo(TargetInfo targetInfo) {
		this.targetInfo = targetInfo;
	}
	@Column(name = "OP_TYPE", nullable = true)
	public int getOpType() {
		return opType;
	}
	public void setOpType(int opType) {
		this.opType = opType;
	}
	@Column(name = "OP_VALUE", nullable = true)
	public int getOpValue() {
		return opValue;
	}
	public void setOpValue(int opValue) {
		this.opValue = opValue;
	}
	@Column(name = "REMARK", nullable = true)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
