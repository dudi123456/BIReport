package com.ailk.bi.metamanage.dao.impl;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.metamanage.dao.IImpactAnalyseDao;

public class ImpactAnalyseDaoImpl implements IImpactAnalyseDao {

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
		StringBuffer strSql = new StringBuffer();
		String[][] rs = null;
		if (table_id != null && !"".equals(table_id)) {
			strSql.append("select table_id,table_name,layer_id from UI_META_INFO_TABLE_DEF where table_id in ("
					+ getSqlIn(table_id) + ")");
			try {
				rs = WebDBUtil.execQryArray(strSql.toString());
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

	public String[][] getImpactInfo(String table_id) {
		StringBuffer strSql = new StringBuffer();
		String[][] rs = null;
		if (table_id != null && !"".equals(table_id)) {
			strSql.append(
					"select table_id,table_name,layer_id from UI_META_INFO_TABLE_DEF where app_table_ids like '%"
							+ table_id + "%' union ")
					.append("select b.table_id,b.table_name,layer_id from UI_META_INFO_ETL_JOB a,UI_META_INFO_TABLE_DEF b ")
					.append("where a.in_table_id like '%" + table_id
							+ "%' and a.out_table_id(+)=b.table_id ");
			try {
				rs = WebDBUtil.execQryArray(strSql.toString());
			} catch (AppException e) {

				e.printStackTrace();
			}
		}
		return rs;
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
}
