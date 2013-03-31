package com.ailk.bi.base.table;

import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "unused", "rawtypes" })
public class InfoMenuTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// HERE IS FROM DATABASE
	public String menu_id = ""; // VARCHAR->String

	public String menu_name = ""; // VARCHAR->String

	public String parent_id = ""; // VARCHAR->String

	public String sequence = ""; // VARCHAR->String

	public String menu_type = ""; // VARCHAR->String

	public String menu_url = ""; // NUMBER->String

	public String status = ""; // NUMBER->String

	public String isshow = ""; // NUMBER->String

	public String icon_url = ""; // VARCHAR->String

	public String menu_desc = ""; // VARCHAR->String

	public String res_init1 = ""; // NUMBER->String

	public String res_init2 = ""; // NUMBER->String

	public String res_init3 = ""; // NUMBER->String

	public String res_char1 = ""; // VARCHAR->String

	public String res_char2 = ""; // VARCHAR->String

	public String res_char3 = ""; // VARCHAR->String

	public String res_char_c = ""; // VARCHAR->String

	public String deploy_sys_code = ""; // NUMBER->String

	// HERE IS USER DEFINE 这一行不要删除！

	public String parent_name = "";

	public String sys_code = "";

	public String sys_name = "";

	public String catid = "";

	public String system_id = "";

	public String role_code = "";

	public String menu_auth_level = "";

	public String role_id = "";

	public List childMenu;

	private String forumCatId = "";
	private String defaultForumId = "";
	private String linkForumFlag = "0";

	private String forumThemeName = "";

	public String openType = "";

}
