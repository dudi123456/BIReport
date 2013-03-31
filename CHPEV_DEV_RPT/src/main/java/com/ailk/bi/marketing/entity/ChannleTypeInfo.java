package com.ailk.bi.marketing.entity;
/**
 * 【实体类】渠道类型实体类   对应  表【MK_PL_CHANNLE_TYPE】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
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


@Entity
@Table(name = "MK_PL_CHANNLE_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChannleTypeInfo {

	private int channleTypeId;//NUMBER	N			渠道类型ID
	private String channleTypeName;//	VARCHAR2(50)	Y			渠道类型名称
	private int channleBigNumber;//NUMBER	Y			最大派单数
	private int state;//	NUMBER	Y			状态
	private Date setTime;//	DATE	Y			设置时间
	private String areaId;//	VARCHAR2(20)	Y
	private String countyId;//	VARCHAR2(20)	Y
	private String typeDesc;// VARCHAR2(200)           描述说明

	@Id
	@Column(name = "CHANNLE_TYPE_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_CHANNLE_TYPE_ID", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CHANNLE_TYPE_ID")
	public int getChannleTypeId() {
		return channleTypeId;
	}
	public void setChannleTypeId(int channleTypeId) {
		this.channleTypeId = channleTypeId;
	}
	@Column(name = "CHANNLE_TYPE_NAME", nullable = true)
	public String getChannleTypeName() {
		return channleTypeName;
	}
	public void setChannleTypeName(String channleTypeName) {
		this.channleTypeName = channleTypeName;
	}
	@Column(name = "CHANNLE_BIG_NUMBER", nullable = true)
	public int getChannleBigNumber() {
		return channleBigNumber;
	}
	public void setChannleBigNumber(int channleBigNumber) {
		this.channleBigNumber = channleBigNumber;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "SET_TIME", nullable = true)
	public Date getSetTime() {
		return setTime;
	}
	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}
	@Column(name = "AREA_ID", nullable = true)
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	@Column(name = "COUNTY_ID", nullable = true)
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	@Column(name = "TYPE_DESC", nullable = true)
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}


}
