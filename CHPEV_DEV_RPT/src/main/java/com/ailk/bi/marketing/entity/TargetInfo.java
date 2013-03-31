package com.ailk.bi.marketing.entity;
/**
 *【实体类】目标实体类   对应  表【MK_PL_TARGET_INFO】
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
@Table(name = "MK_PL_TARGET_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TargetInfo {
	private int  targetId ;
	private String  targetName ;
	private int  targetType;
	private String   content  ;
	private Date   createDate ;
	private String   creator  ;
	private String   decrider  ;
	private int   state;
	private String   areaId  ;
	private String  countyId   ;
	private int unit;
	@Column(name = "UNIT", nullable = true)
	public int getUnit() {
		return unit;
	}
	public void setUnit(int unit) {
		this.unit = unit;
	}
	@Id
	@Column(name = "TARGET_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_TARGET_INFO", sequenceName = "SEQ_RPT_USER_OLAP", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TARGET_INFO")
	public int getTargetId() {
		return targetId;
	}
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	@Column(name = "TARGET_NAME", nullable = true)
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	@Column(name = "TARGET_TYPE_ID", nullable = true)
	public int getTargetType() {
		return targetType;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	@Column(name = "CONTENT", nullable = true)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "CREATE_DATE",  nullable = true)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "CREATOR", nullable = true )
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Column(name = "DECRIDER", nullable = true)
	public String getDecrider() {
		return decrider;
	}
	public void setDecrider(String decrider) {
		this.decrider = decrider;
	}
	@Column(name = "STATE", nullable = true)
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Column(name = "AREA_ID",nullable = true)
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	@Column(name = "COUNTY_ID",  nullable = true)
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

}
