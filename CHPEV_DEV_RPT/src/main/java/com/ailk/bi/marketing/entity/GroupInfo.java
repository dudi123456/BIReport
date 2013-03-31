package com.ailk.bi.marketing.entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * 【实体类】客户群实体类   对应  表【MK_PL_SELL_GROUP_INFO】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Entity
@Table(name = "MK_PL_SELL_GROUP_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GroupInfo {
	private String groupId;//GROUP_ID	VARCHAR2(20)	N			客户群编号
	private String groupName;//GROUP_NAME	VARCHAR2(200)	N			客户群名称
	private int groupTypeId;//GROUP_TYPE_ID	NUMBER(10)	N			客户群类型0-普通客户群1-种子名单 2-红名单 3-竞争对手9-黑名单5-通用客户群6-个性化客户群
	private char createType;//GROUP_CREATE_TYPE	CHAR(1)	N			客户群来源类型0-文件导入1-智能查询2-客户分群3-自定义查询4-后台生成5-定制群8业务支撑生成
	private String userCharacter;//USER_CHARACTER	VARCHAR2(1000)	Y			客户群特征描述
	private String areaCode;//AREA_CODE	VARCHAR2(15)	N			归属地市
	private String selectRule;//SELECT_RULE	VARCHAR2(4000)	Y			筛选规则
	private String operCode;//OPER_CODE	VARCHAR2(20)	N			创建人
	private String operUnit;//OPER_UNIT	VARCHAR2(20)	N			创建人单位代码
	private String operTime;//OPER_TIME	DATE	N			创建人时间
	private String remark;//REMARK	VARCHAR2(1000)	Y			详细描述
	private char delFlag;//DEL_FLAG	CHAR(1)	N			删除标志
	private String status;//STATUS	VARCHAR2(1)	Y			提取数据状态0.未完成1.已完成-6失败2.已作废
	private int customerCount;//CUSTOMER_COUNT	NUMBER(10)	Y			客户群所包含的客户数量
	private char isCommonUseGroup;//IS_COMMON_USE_GROUP	CHAR(1)	Y			是否常用目标客户群 0.不是1.是
	private int brandOf;//BRAND_OF	NUMBER(10)	Y			所属品牌0.彩铃用户1.彩信用户2.飞信用户
	private String useStatus;//USE_STATUS	VARCHAR2(1)	Y			被营销活动使用状态0未使用1-已使用
	private int groupModleTypeIdB;//GROUP_MODLE_TYPE_ID_B	NUMBER(10)	Y			客户群模型类型大类别
	private int groupModleTypeIdS;//GROUP_MODLE_TYPE_ID_S	NUMBER(10)	Y			客户群模型类型小类别
	private String operMobile;//OPER_MOBILE	VARCHAR2(12)	Y			创建人联系电话
	private String isShare;//IS_SHARE	VARCHAR2(1)	Y			是否共享0-不共享1-共享
	private int appraiseValue;//APPRAISE_VALUE	NUMBER(10)	Y			客户群总体评价值
	private String supportUserId;//SUPPORT_USER_ID	VARCHAR2(20)	Y			支撑人员Id
	private String supportUserName;//SUPPORT_USER_NAME	VARCHAR2(20)	Y			支撑人员名称
	private String supportUserMobile;//SUPPORT_USER_MOBILE	VARCHAR2(20)	Y			支撑人员手机号
	private Date supplyDate;//SUPPLY_DATE	DATE	Y			限定完成时间
	private String delValid;//	DEL_VALID	VARCHAR2(2)	Y			是否可删除
	private String reuseValid;//REUSE_VALID	VARCHAR2(2)	Y			是否可重用
	private Date startupDate;//STARTUP_DATE	DATE	Y			生效开始日期
	private Date expireDate;//EXPIRE_DATE	DATE	Y			生效结束日期
	private String groupModleTypeIdT;//GROUP_MODLE_TYPE_ID_T	VARCHAR2(20)	Y			客户群模型类型中类别
	public int numCount=this.customerCount;//help字段

	@Id
	@Column(name = "GROUP_ID", insertable=false, updatable=false, length = 100, nullable = false)
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@Column(name = "GROUP_NAME", nullable = true, insertable=false, updatable=false)
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Column(name = "GROUP_TYPE_ID", nullable = true, insertable=false, updatable=false)
	public int getGroupTypeId() {
		return groupTypeId;
	}
	public void setGroupTypeId(int groupTypeId) {
		this.groupTypeId = groupTypeId;
	}
	@Column(name = "GROUP_CREATE_TYPE", nullable = true, insertable=false, updatable=false)
	public char getCreateType() {
		return createType;
	}
	public void setCreateType(char createTypeChar) {
		this.createType = createTypeChar;
	}
	@Column(name = "USER_CHARACTER", nullable = true, insertable=false, updatable=false)
	public String getUserCharacter() {
		return userCharacter;
	}
	public void setUserCharacter(String userCharacter) {
		this.userCharacter = userCharacter;
	}
	@Column(name = "AREA_CODE", nullable = true, insertable=false, updatable=false)
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	@Column(name = "SELECT_RULE", nullable = true, insertable=false, updatable=false)
	public String getSelectRule() {
		return selectRule;
	}
	public void setSelectRule(String selectRule) {
		this.selectRule = selectRule;
	}
	@Column(name = "OPER_CODE", nullable = true, insertable=false, updatable=false)
	public String getOperCode() {
		return operCode;
	}
	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}
	@Column(name = "OPER_UNIT", nullable = true, insertable=false, updatable=false)
	public String getOperUnit() {
		return operUnit;
	}
	public void setOperUnit(String operUnit) {
		this.operUnit = operUnit;
	}
	@Column(name = "OPER_TIME", nullable = true, insertable=false, updatable=false)
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	@Column(name = "REMARK", nullable = true, insertable=false, updatable=false)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "DEL_FLAG", nullable = true, insertable=false, updatable=false)
	public char getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name = "STATUS", nullable = true, insertable=false, updatable=false)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "CUSTOMER_COUNT", nullable = true)
	public int getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(int customerCount) {
		this.customerCount = customerCount;
	}
	@Column(name = "IS_COMMON_USE_GROUP", nullable = true, insertable=false, updatable=false)
	public char getIsCommonUseGroup() {
		return isCommonUseGroup;
	}
	public void setIsCommonUseGroup(char isCommonUseGroup) {
		this.isCommonUseGroup = isCommonUseGroup;
	}
	@Column(name = "BRAND_OF", nullable = true, insertable=false, updatable=false)
	public int getBrandOf() {
		return brandOf;
	}
	public void setBrandOf(int brandOf) {
		this.brandOf = brandOf;
	}
	@Column(name = "USE_STATUS", nullable = true, insertable=false, updatable=false)
	public String getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	@Column(name = "GROUP_MODLE_TYPE_ID_B", nullable = true, insertable=false, updatable=false)
	public int getGroupModleTypeIdB() {
		return groupModleTypeIdB;
	}
	public void setGroupModleTypeIdB(int groupModleTypeIdB) {
		this.groupModleTypeIdB = groupModleTypeIdB;
	}
	@Column(name = "GROUP_MODLE_TYPE_ID_S", nullable = true, insertable=false, updatable=false)
	public int getGroupModleTypeIdS() {
		return groupModleTypeIdS;
	}
	public void setGroupModleTypeIdS(int groupModleTypeId) {
		this.groupModleTypeIdS = groupModleTypeId;
	}
	@Column(name = "OPER_MOBILE", nullable = true, insertable=false, updatable=false)
	public String getOperMobile() {
		return operMobile;
	}
	public void setOperMobile(String operMobile) {
		this.operMobile = operMobile;
	}
	@Column(name = "IS_SHARE", nullable = true, insertable=false, updatable=false)
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	@Column(name = "APPRAISE_VALUE", nullable = true)
	public int getAppraiseValue() {
		return appraiseValue;
	}
	public void setAppraiseValue(int appraiseValue) {
		this.appraiseValue = appraiseValue;
	}
	@Column(name = "SUPPORT_USER_ID", nullable = true)
	public String getSupportUserId() {
		return supportUserId;
	}
	public void setSupportUserId(String supportUserId) {
		this.supportUserId = supportUserId;
	}
	@Column(name = "SUPPORT_USER_NAME", nullable = true)
	public String getSupportUserName() {
		return supportUserName;
	}
	public void setSupportUserName(String supportUserName) {
		this.supportUserName = supportUserName;
	}
	@Column(name = "SUPPORT_USER_MOBILE", nullable = true)
	public String getSupportUserMobile() {
		return supportUserMobile;
	}
	public void setSupportUserMobile(String supportUserMobile) {
		this.supportUserMobile = supportUserMobile;
	}
	@Column(name = "SUPPLY_DATE", nullable = true)
	public Date getSupplyDate() {
		return supplyDate;
	}
	public void setSupplyDate(Date supplyDate) {
		this.supplyDate = supplyDate;
	}
	@Column(name = "DEL_VALID", nullable = true)
	public String getDelValid() {
		return delValid;
	}
	public void setDelValid(String delValid) {
		this.delValid = delValid;
	}
	@Column(name = "REUSE_VALID", nullable = true)
	public String getReuseValid() {
		return reuseValid;
	}
	public void setReuseValid(String reuseValid) {
		this.reuseValid = reuseValid;
	}
	@Column(name = "STARTUP_DATE", nullable = true)
	public Date getStartupDate() {
		return startupDate;
	}
	public void setStartupDate(Date startupDate) {
		this.startupDate = startupDate;
	}
	@Column(name = "EXPIRE_DATE", nullable = true)
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	@Column(name = "GROUP_MODLE_TYPE_ID_T", nullable = true)
	public String getGroupModleTypeIdT() {
		return groupModleTypeIdT;
	}
	public void setGroupModleTypeIdT(String groupModleTypeIdT) {
		this.groupModleTypeIdT = groupModleTypeIdT;
	}

}
