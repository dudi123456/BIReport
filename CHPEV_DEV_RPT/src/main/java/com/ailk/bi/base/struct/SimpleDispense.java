package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

public class SimpleDispense extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3172263973696428570L;

	public String res_id = ""; // 资源ID

	public String res_name = ""; // 资源名称

	public String parent_id = ""; // 类型ID

	public String parent_name = ""; // 类型名称

	public String cycle = ""; // 周期

	public String cycle_desc = ""; // 周期描述

	public String role_code = ""; // 角色

	public String role_name = ""; // 角色名称

	public String p_id = ""; // 流程ID

	public String p_flag_name = ""; // 流程名称

}
