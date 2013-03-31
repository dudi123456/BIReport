package com.ailk.bi.metamanage.dao.impl;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.metamanage.dao.ISourceAnalyseDao;

public class SourceAnalyseDaoImpl implements ISourceAnalyseDao {

	public String[][] getMsuTable(String msu_id) {
		String[][] rs = null;
		try {
			String sql = "select a.msu_name,a.msu_field,b.table_id from UI_META_INFO_MEASURE a,UI_META_RULE_TABLE_MEASURE b where a.msu_id ='"
					+ msu_id + "' and a.msu_id=b.msu_id";

			rs = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return rs;
	}

	public String[][] getTableInfo(String table_id) {

		String strSql = "select a.job_id,a.job_name,b.table_id, case when app_type='SQL' then app_table_ids else a.in_table_id end as in_table_id,b.layer_id,b.table_name from UI_META_INFO_ETL_JOB a,UI_META_INFO_TABLE_DEF b where b.table_id='"
				+ table_id
				+ "' and a.out_table_id(+)=b.table_id order by b.table_id ";
		String[][] rs = null;
		if (table_id != null && !"".equals(table_id)) {
			try {
				rs = WebDBUtil.execQryArray(strSql);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		return rs;
	}

	public String[][] getSourceInfo(String str) {

		String[][] rs = null;
		if (!"".equals(str)) {
			String sql = "select a.job_name,b.table_id, case when app_type='SQL' then app_table_ids else a.in_table_id end as in_table_id ,b.layer_id,b.table_name "
					+ "from UI_META_INFO_ETL_JOB a,UI_META_INFO_TABLE_DEF b where b.table_id in("
					+ getSqlIn(str)
					+ ") and a.out_table_id(+)=b.table_id "
					+ getSqlOrder(str);
			try {
				rs = WebDBUtil.execQryArray(sql);
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		return rs;
	}

	private String getSqlIn(String str) {
		String temp = "";
		if (str != null && !"".equals(str)) {
			String[] arr = str.split(",");

			for (int i = 0; i < arr.length; i++) {

				temp += "'" + arr[i] + "',";
			}
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp;
	}

	private String getSqlOrder(String str) {
		String[] arr = str.split(",");
		String temp = " order by (case b.table_id ";
		for (int i = 0; i < arr.length; i++) {
			temp += " when '" + arr[i] + "' then " + (i + 1);
		}
		return temp + " end)";
	}

	public String[][] getLayerColor() {
		String strSql = "select layer_id,layer_name,layer_color from UI_META_INFO_LAYER order by sequence ";
		String[][] rs = null;
		try {
			rs = WebDBUtil.execQryArray(strSql);
		} catch (AppException e) {

			e.printStackTrace();
		}
		return rs;
	}

	public static String[][] getTableField(String table_id) {
		String strSql = "select b.table_id,b.table_name,filed_en_code,field_name from ui_meta_info_table_field a,UI_META_INFO_TABLE_DEF b where b.table_id='"
				+ table_id
				+ "' and a.table_id(+)=b.table_id order by a.filed_column_id";
		String[][] rs = null;
		try {
			rs = WebDBUtil.execQryArray(strSql);
		} catch (AppException e) {

			e.printStackTrace();
		}
		return rs;
	}

	public boolean isExistTable(String table_id) {
		String strSql = "select table_id from UI_META_INFO_TABLE_DEF where table_id="
				+ table_id;
		boolean flag = false;
		try {
			String[][] rs = WebDBUtil.execQryArray(strSql);
			if (rs != null && rs.length > 0) {
				flag = true;
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return flag;
	}
}
