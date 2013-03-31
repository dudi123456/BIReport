package com.ailk.bi.filemgr.common;

import java.util.TimerTask;

//import com.ailk.bi.common.dbtools.WebDBUtil;

public class UpdateBlobField extends TimerTask {

	private String keyId;
	private String blobFld;
	private String tablename;
	private String keyFld;
	private String fileName;

	public UpdateBlobField(String keyFld, String blobFld, String tablename,
			String keyId, String fileName) {
		this.keyFld = keyFld;
		this.keyId = keyId;
		this.blobFld = blobFld;
		this.tablename = tablename;
		this.fileName = fileName;

	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getBlobFld() {
		return blobFld;
	}

	public void setBlobFld(String blobFld) {
		this.blobFld = blobFld;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getKeyFld() {
		return keyFld;
	}

	public void setKeyFld(String keyFld) {
		this.keyFld = keyFld;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void run() {

		// WebDBUtil.updateBlogFld(keyFld,blobFld,tablename,keyId,fileName);
	}

}
