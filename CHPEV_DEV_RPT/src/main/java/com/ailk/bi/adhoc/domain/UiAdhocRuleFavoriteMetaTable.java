package com.ailk.bi.adhoc.domain;

import com.ailk.bi.common.event.JBTableBase;

public class UiAdhocRuleFavoriteMetaTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	private String favorite_id = ""; // VARCHAR->String

	private String meta_type = ""; // CHAR->String

	private String meta_code = ""; // VARCHAR->String

	private String sequence = ""; // NUMBER->String

	public String getFavorite_id() {
		return favorite_id;
	}

	public void setFavorite_id(String favorite_id) {
		this.favorite_id = favorite_id;
	}

	public String getMeta_code() {
		return meta_code;
	}

	public void setMeta_code(String meta_code) {
		this.meta_code = meta_code;
	}

	public String getMeta_type() {
		return meta_type;
	}

	public void setMeta_type(String meta_type) {
		this.meta_type = meta_type;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	// HERE IS USER DEFINE 这一行不要删除！

}
