package com.ailk.bi.adhoc.domain;

import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class UiAdhocFavoriteDefTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String favorite_id = ""; // VARCHAR->String

	private String favorite_name = ""; // VARCHAR->String

	private String favorite_desc = ""; // VARCHAR->String

	private String status = ""; // CHAR->String

	private int favTypeFlag = 1;

	private String conStr = "";
	private String dimStr = "";

	private String msuStr = "";

	private List listValue = null;
	private List listConStr = null;

	public List getListConStr() {
		return listConStr;
	}

	public void setListConStr(List listConStr) {
		this.listConStr = listConStr;
	}

	public List getListValue() {
		return listValue;
	}

	public void setListValue(List listValue) {
		this.listValue = listValue;
	}

	public String getConStr() {
		return conStr;
	}

	public void setConStr(String conStr) {
		this.conStr = conStr;
	}

	public String getDimStr() {
		return dimStr;
	}

	public void setDimStr(String dimStr) {
		this.dimStr = dimStr;
	}

	public String getMsuStr() {
		return msuStr;
	}

	public void setMsuStr(String msuStr) {
		this.msuStr = msuStr;
	}

	public int getFavTypeFlag() {
		return favTypeFlag;
	}

	public void setFavTypeFlag(int favTypeFlag) {
		this.favTypeFlag = favTypeFlag;
	}

	public String getFavorite_desc() {
		return favorite_desc;
	}

	public void setFavorite_desc(String favorite_desc) {
		this.favorite_desc = favorite_desc;
	}

	public String getFavorite_id() {
		return favorite_id;
	}

	public void setFavorite_id(String favorite_id) {
		this.favorite_id = favorite_id;
	}

	public String getFavorite_name() {
		return favorite_name;
	}

	public void setFavorite_name(String favorite_name) {
		this.favorite_name = favorite_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
