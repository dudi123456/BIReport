package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class PubInfoNewsTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7734673046581433332L;

	// HERE IS FROM DATABASE
	public String news_id = ""; // NUMBER->String

	public String create_date = ""; // NUMBER->String

	public String user_id = ""; // VARCHAR->String

	public String user_name = ""; // VARCHAR->String

	public String subject = ""; // VARCHAR->String

	public String descr = ""; // VARCHAR->String

	public String new_type = ""; // VARCHAR->String

	public String sequence = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！

}
