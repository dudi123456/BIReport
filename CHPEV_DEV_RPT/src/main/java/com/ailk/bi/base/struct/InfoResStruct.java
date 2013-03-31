package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

public class InfoResStruct extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1998693376093932219L;

	public String res_id = ""; // 资源ID

	public String parent_id = "";// 上级资源ID

	public String res_name = "";// 资源名称

	public String res_url = "";// 资源URL

	public String is_res = "";// 是否资源

	public InfoResStruct[] submenu = null;

}
