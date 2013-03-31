package com.ailk.bi.userlog.entity;

public class UiInfoUserLog {

	private String logId;
	private String sessionId;
	private String userId;
	private String clientAddress;
	private String operDate;
	private String operation;
	private String operationName;

	private String resourceType;
	private String resourceId;
	private String msg;
	private String browserVer;
	private String leaveDate;
	private String fieldBak01;
	private String fieldBak02;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getOperDate() {
		return operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBrowserVer() {
		return browserVer;
	}

	public void setBrowserVer(String browserVer) {
		this.browserVer = browserVer;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getFieldBak01() {
		return fieldBak01;
	}

	public void setFieldBak01(String fieldBak01) {
		this.fieldBak01 = fieldBak01;
	}

	public String getFieldBak02() {
		return fieldBak02;
	}

	public void setFieldBak02(String fieldBak02) {
		this.fieldBak02 = fieldBak02;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
}
