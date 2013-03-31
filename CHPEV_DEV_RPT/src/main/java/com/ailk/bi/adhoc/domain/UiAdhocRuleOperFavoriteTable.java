package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocRuleOperFavoriteTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String oper_no = ""; // VARCHAR->String

	private String favorite_id = ""; // VARCHAR->String

	private String fav_type = ""; // CHAR->String

	public String getFav_type() {
		return fav_type;
	}

	public void setFav_type(String fav_type) {
		this.fav_type = fav_type;
	}

	public String getFavorite_id() {
		return favorite_id;
	}

	public void setFavorite_id(String favorite_id) {
		this.favorite_id = favorite_id;
	}

	public String getOper_no() {
		return oper_no;
	}

	public void setOper_no(String oper_no) {
		this.oper_no = oper_no;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
