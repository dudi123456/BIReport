package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class PubInfoFavoriteTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7734673046581433332L;

	// HERE IS FROM DATABASE
	public String user_id = ""; // NUMBER->String

	public String res_id = ""; // NUMBER->String

	public String item_type = ""; // VARCHAR->String

	public String item_type_desc = ""; // VARCHAR->String

	public String item_default = ""; // VARCHAR->String

	public String item_order = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String name = ""; // VARCHAR->String

	public String hot_type = ""; // VARCHAR->String

	public String hot_type_desc = ""; // VARCHAR->String
}
