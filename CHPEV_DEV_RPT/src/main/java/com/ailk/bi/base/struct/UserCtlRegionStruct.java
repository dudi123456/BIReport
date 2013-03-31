package com.ailk.bi.base.struct;

import com.ailk.bi.common.event.JBTableBase;

/**
 * UserCtlRegionStruct是操作员校验数据区域的前端查询表
 * 
 * @author jcm
 */
public class UserCtlRegionStruct extends JBTableBase {

	private static final long serialVersionUID = -5175382322476597600L;

	/**
	 * 部门控制标志 0-全部; 1-发展人部门; 2-楼宇部门;
	 */

	public String ctl_flag = "";

	/**
	 * 当前控制区域的所在等级 (1:省[都市]; 2:地市分公司; 3:区县分公司; 4：片区; 5：行区)
	 */
	public String ctl_lvl = "";

	/**
	 * 数据权限控制符 in（多值） = （单值）
	 */
	public String ctl_in_or_equals = "";

	/**
	 * 用户当前配置最顶层控制区域
	 */
	public String ctl_str = "";
	public String ctl_str_add = "";

	/**
	 * 用户当前控制区域的省级控制区域
	 */
	public String ctl_metro_str = "";// 都市等级（省概念）

	public String ctl_metro_str_add = "";// 都市等级（省概念）

	/**
	 * 用户当前控制区域的地市级控制区域
	 */
	public String ctl_city_str = ""; // 地市分公司
	public String ctl_city_str_add = ""; // 地市分公司

	/**
	 * 用户当前控制区域的区县级控制区域
	 */
	public String ctl_county_str = ""; // 区县分公司
	public String ctl_county_str_add = ""; // 区县分公司

	public String CTL_BUILDING_BURA_ID = "";// 区局ID

	/**
	 * 用户当前控制区域的片区级控制区域
	 */

	public String ctl_sec_str = ""; // 片区
	public String ctl_sec_str_add = ""; // 片区

	/**
	 * 用户当前控制区域的行区级控制区域
	 */

	public String ctl_area_str = ""; // 行区
	public String ctl_area_str_add = ""; // 行区

	public String ctl_sql = "";

	public String getCtl_flag() {
		return ctl_flag;
	}

	public void setCtl_flag(String ctl_flag) {
		this.ctl_flag = ctl_flag;
	}

	public String getCtl_lvl() {
		return ctl_lvl;
	}

	public void setCtl_lvl(String ctl_lvl) {
		this.ctl_lvl = ctl_lvl;
	}

	public String getCtl_in_or_equals() {
		return ctl_in_or_equals;
	}

	public void setCtl_in_or_equals(String ctl_in_or_equals) {
		this.ctl_in_or_equals = ctl_in_or_equals;
	}

	public String getCtl_str() {
		return ctl_str;
	}

	public void setCtl_str(String ctl_str) {
		this.ctl_str = ctl_str;
	}

	public String getCtl_str_add() {
		return ctl_str_add;
	}

	public void setCtl_str_add(String ctl_str_add) {
		this.ctl_str_add = ctl_str_add;
	}

	public String getCtl_metro_str() {
		return ctl_metro_str;
	}

	public void setCtl_metro_str(String ctl_metro_str) {
		this.ctl_metro_str = ctl_metro_str;
	}

	public String getCtl_metro_str_add() {
		return ctl_metro_str_add;
	}

	public void setCtl_metro_str_add(String ctl_metro_str_add) {
		this.ctl_metro_str_add = ctl_metro_str_add;
	}

	public String getCtl_city_str() {
		return ctl_city_str;
	}

	public void setCtl_city_str(String ctl_city_str) {
		this.ctl_city_str = ctl_city_str;
	}

	public String getCtl_city_str_add() {
		return ctl_city_str_add;
	}

	public void setCtl_city_str_add(String ctl_city_str_add) {
		this.ctl_city_str_add = ctl_city_str_add;
	}

	public String getCtl_county_str() {
		return ctl_county_str;
	}

	public void setCtl_county_str(String ctl_county_str) {
		this.ctl_county_str = ctl_county_str;
	}

	public String getCtl_county_str_add() {
		return ctl_county_str_add;
	}

	public void setCtl_county_str_add(String ctl_county_str_add) {
		this.ctl_county_str_add = ctl_county_str_add;
	}

	public String getCtl_sec_str() {
		return ctl_sec_str;
	}

	public void setCtl_sec_str(String ctl_sec_str) {
		this.ctl_sec_str = ctl_sec_str;
	}

	public String getCtl_sec_str_add() {
		return ctl_sec_str_add;
	}

	public void setCtl_sec_str_add(String ctl_sec_str_add) {
		this.ctl_sec_str_add = ctl_sec_str_add;
	}

	public String getCtl_area_str() {
		return ctl_area_str;
	}

	public void setCtl_area_str(String ctl_area_str) {
		this.ctl_area_str = ctl_area_str;
	}

	public String getCtl_area_str_add() {
		return ctl_area_str_add;
	}

	public void setCtl_area_str_add(String ctl_area_str_add) {
		this.ctl_area_str_add = ctl_area_str_add;
	}

	public String getCtl_sql() {
		return ctl_sql;
	}

	public void setCtl_sql(String ctl_sql) {
		this.ctl_sql = ctl_sql;
	}
}
