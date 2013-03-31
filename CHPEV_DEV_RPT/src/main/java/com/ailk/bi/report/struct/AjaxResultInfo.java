package com.ailk.bi.report.struct;

import net.sf.json.JSONObject;

public class AjaxResultInfo {
	private String title;

	private String content;

	private boolean success = false;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public AjaxResultInfo(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public AjaxResultInfo(String title, String content, boolean success) {
		this.title = title;
		this.content = content;
		this.success = success;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {

		JSONObject jsonObj = JSONObject.fromObject(this);
		return jsonObj.toString();
	}
}
