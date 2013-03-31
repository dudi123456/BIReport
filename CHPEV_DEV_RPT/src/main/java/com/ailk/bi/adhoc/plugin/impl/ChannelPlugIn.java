package com.ailk.bi.adhoc.plugin.impl;

import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.plugin.IAdhocPlugIn;

/**
 * 重庆两级过滤业务包装SQL
 * 
 * @author chunming
 * 
 */
public class ChannelPlugIn implements IAdhocPlugIn {

	public String AdhocBusiness(String baseCode, HttpSession session) {
		String sql = "";
		// 当前用户级数据权限
		// 进行权限设置

		sql += " select name," + baseCode
				+ " dept_code,code from ui_sdt_dept  ";
		sql += " where parent_id=" + baseCode;

		return sql;
	}

}
