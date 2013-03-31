package com.ailk.bi.base.table;

import java.util.List;

import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class FmPUseAnaSaveTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8527040512590170430L;

	/**
	 * 登陆用户标识
	 */
	public String sys_user_id = "";

	/**
	 * 收藏夹类型
	 */
	public String pack_type = "";

	/**
	 * 收藏夹名称
	 */
	public String pack_desc = "";

	/**
	 * 收藏家标识
	 */
	public String pack_id = "";

	/**
	 * 定制日期
	 */
	public String create_date = "";

	/**
	 * 修改日期
	 */
	public String update_date = "";

	/**
	 * 是否默认
	 */
	public String default_flag = "";

	/**
	 * 是否有效
	 */
	public String valid_flag = "";

	/**
	 * 保存用户定义的产品列表
	 */
	public List products = null;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("sys_user_id = ").append(this.sys_user_id).append("\n");
		sb.append("pack_type = ").append(this.pack_type).append("\n");
		sb.append("pack_desc = ").append(this.pack_desc).append("\n");
		sb.append("pack_id = ").append(this.pack_id).append("\n");
		sb.append("create_date = ").append(this.create_date).append("\n");
		sb.append("update_date = ").append(this.update_date).append("\n");
		sb.append("default_flag = ").append(this.default_flag).append("\n");
		sb.append("valid_flag = ").append(this.valid_flag).append("\n");
		return sb.toString();
	}

	/**
	 * 对应表格显示的列, add by pengfei,在客户专题中使用
	 */
	public String display_col = "";

}
