package com.ailk.bi.bulletin.entity;

@SuppressWarnings({ "unused" })
public class MartInfoBulletin {
	private String id;
	private String title;
	private String newsMsg;
	private String cityId;
	private String validBeginDate;
	private String validEndDate;
	private String creator;
	private String addDt;
	private int newsPriority;
	private int isFront;
	private String remark;
	private String typeId;
	private String typeName;
	private String fieldBak01;
	private String userId;
	private String userName;
	private String groupId;
	private String groupName;
	private String parentId;
	private int level;

	private String systemId;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getFieldBak01() {
		return fieldBak01;
	}

	public void setFieldBak01(String fieldBak01) {
		this.fieldBak01 = fieldBak01;
	}

	public String getTypeName() {
		BulletinType btype = new BulletinType();
		return (String) btype.getBulletinType().get(typeId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsMsg() {
		return newsMsg;
	}

	public void setNewsMsg(String newsMsg) {
		this.newsMsg = newsMsg;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getValidBeginDate() {
		return validBeginDate;
	}

	public void setValidBeginDate(String validBeginDate) {
		this.validBeginDate = validBeginDate;
	}

	public String getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(String validEndDate) {
		this.validEndDate = validEndDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getAddDt() {
		return addDt;
	}

	public void setAddDt(String addDt) {
		this.addDt = addDt;
	}

	public int getNewsPriority() {
		return newsPriority;
	}

	public void setNewsPriority(int newsPriority) {
		this.newsPriority = newsPriority;
	}

	public int getIsFront() {
		return isFront;
	}

	public void setIsFront(int isFront) {
		this.isFront = isFront;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		String strOut = this.title + ":" + this.cityId + ":"
				+ this.validBeginDate + ":" + this.validEndDate + ":"
				+ this.title + ":" + this.newsMsg;
		return strOut;
	}

}
