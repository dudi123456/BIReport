package com.ailk.bi.report.struct;

public class DojoTreeNode {
	private String widgetId;

	private String title;

	private String objectId;

	private int index;

	private boolean isFolder;

	private String msu_rule;

	private String precision;

	private String rule_desc;

	private String msu_desc;

	private String srctab_id;

	private String srctab_name;

	public String getSrctab_id() {
		return srctab_id;
	}

	public void setSrctab_id(String srctab_id) {
		this.srctab_id = srctab_id;
	}

	public String getSrctab_name() {
		return srctab_name;
	}

	public void setSrctab_name(String srctab_name) {
		this.srctab_name = srctab_name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isIsFolder() {
		return isFolder;
	}

	public void setIsFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public String getMsu_desc() {
		return msu_desc;
	}

	public void setMsu_desc(String msu_desc) {
		this.msu_desc = msu_desc;
	}

	public String getMsu_rule() {
		return msu_rule;
	}

	public void setMsu_rule(String msu_rule) {
		this.msu_rule = msu_rule;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getRule_desc() {
		return rule_desc;
	}

	public void setRule_desc(String rule_desc) {
		this.rule_desc = rule_desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

}
