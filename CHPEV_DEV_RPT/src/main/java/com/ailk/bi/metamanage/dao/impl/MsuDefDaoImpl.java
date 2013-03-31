package com.ailk.bi.metamanage.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.metamanage.dao.IMsuDefDao;
import com.ailk.bi.metamanage.model.Measure;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MsuDefDaoImpl implements IMsuDefDao {

	public Measure getMsu(String msuId) {
		List list = null;
		Measure info = new Measure();
		String sql = "select a.*,b.msu_type_name from UI_META_INFO_MEASURE a,UI_META_INFO_MEASURE_TYPE b where a.msu_type_id=b.msu_type_id and a.msu_id='"
				+ msuId + "'";
		try {
			list = WebDBUtil.execQryArrayMap(sql, null);
			if (list != null && list.size() > 0) {
				HashMap map = (HashMap) list.get(0);
				info.setMsu_id((String) map.get("MSU_ID"));
				info.setMsu_name((String) map.get("MSU_NAME"));
				info.setMsu_desc((String) map.get("MSU_DESC"));
				info.setMsu_type_id((String) map.get("MSU_TYPE_ID"));
				info.setMsu_type_name((String) map.get("MSU_TYPE_NAME"));
				info.setMsu_unit((String) map.get("MSU_UNIT"));
				info.setMsu_digit((String) map.get("MSU_DIGIT"));
				info.setMsu_rule_desc((String) map.get("MSU_RULE_DESC"));
				info.setMsu_app_rule((String) map.get("MSU_APP_RULE"));
				info.setMsu_rule((String) map.get("MSU_RULE"));
				info.setMsu_app_rule_desc((String) map.get("MSU_APP_RULE_DESC"));
				info.setIs_calmsu((String) map.get("IS_CALMSU"));
				info.setIs_timemsu((String) map.get("IS_TIMEMSU"));
				info.setIs_deri((String) map.get("IS_DERI"));
				info.setMsu_code((String) map.get("MSU_CODE"));
				info.setParent_id((String) map.get("PARENT_ID"));
				info.setMsu_field((String) map.get("MSU_FIELD"));
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return info;
	}

	private String getInsertSql(HttpServletRequest request) {
		String sql = "";
		String msu_id = StringB.NulltoBlank(request.getParameter("msu_id"));
		String msu_name = StringB.NulltoBlank(request.getParameter("msu_name"));
		String msu_desc = StringB.NulltoBlank(request.getParameter("msu_desc"));
		String msu_type_id = StringB.NulltoBlank(request
				.getParameter("msu_type_id"));
		String msu_unit = StringB.NulltoBlank(request.getParameter("msu_unit"));
		String msu_code = StringB.NulltoBlank(request.getParameter("msu_code"));
		String parent_id = StringB.NulltoBlank(request
				.getParameter("parent_id"));
		String msu_field = StringB.NulltoBlank(request
				.getParameter("msu_field"));
		String msu_rule = StringB.NulltoBlank(request.getParameter("msu_rule"));
		String msu_rule_desc = StringB.NulltoBlank(request
				.getParameter("msu_rule_desc"));
		String msu_digit = StringB.NulltoBlank(request
				.getParameter("msu_digit"));
		String msu_app_rule = StringB.NulltoBlank(request
				.getParameter("msu_app_rule"));
		String msu_app_rule_desc = StringB.NulltoBlank(request
				.getParameter("msu_app_rule_desc"));
		String is_calmsu = StringB.NulltoBlank(request
				.getParameter("is_calmsu"));
		String is_timemsu = StringB.NulltoBlank(request
				.getParameter("is_timemsu"));
		String is_deri = StringB.NulltoBlank(request.getParameter("is_deri"));
		List svc = new ArrayList();
		svc.add(msu_id);
		svc.add(msu_type_id);
		svc.add(msu_code);
		svc.add(parent_id);
		svc.add(msu_name);
		svc.add(msu_desc);
		svc.add(msu_field);
		svc.add(msu_unit);
		svc.add(msu_digit);
		svc.add(msu_rule);
		svc.add(msu_rule_desc);
		svc.add(msu_app_rule);
		svc.add(msu_app_rule_desc);
		svc.add(is_calmsu);
		svc.add(is_timemsu);
		svc.add(is_deri);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}
		try {
			sql = SQLGenator.genSQL("meta040", param);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return sql;
	}

	public boolean isExistMsuId(String msuId) {
		boolean flag = false;
		try {
			String sql = "select * from UI_META_INFO_MEASURE where msu_id='"
					+ msuId + "'";
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				flag = true;
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return flag;
	}

	public void add(HttpServletRequest request) {
		try {
			WebDBUtil.execUpdate(getInsertSql(request));
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void save(HttpServletRequest request) {
		String[] strSql = new String[1];
		// strSql[0] =
		// "delete from UI_META_INFO_MEASURE where msu_id='"+request.getParameter("msu_id")+"'";
		// strSql[1] = getInsertSql(request);
		String str = "";
		String[] param = new String[] { "msu_type_id", "msu_code", "parent_id",
				"msu_name", "msu_desc", "msu_field", "msu_unit", "msu_digit",
				"msu_rule", "msu_rule_desc", "msu_app_rule",
				"msu_app_rule_desc", "is_calmsu", "is_timemsu", "is_deri" };

		for (int i = 0; i < param.length; i++) {
			String[] arr = param[i].split("##");
			String value = StringB.NulltoBlank(request.getParameter(arr[0]));
			if (arr.length == 1) {
				str += arr[0] + "='" + value + "',";
			} else {
				if (!"".equals(value))
					str += arr[0] + "=" + value + ",";
				else
					str += arr[0] + "=null,";
			}
		}
		strSql[0] = "update UI_META_INFO_MEASURE set "
				+ str.substring(0, str.length() - 1) + " where msu_id='"
				+ request.getParameter("msu_id") + "'";

		try {
			WebDBUtil.execTransUpdate(strSql);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void del(String msuId) {
		try {
			String sql = "delete from UI_META_INFO_MEASURE where msu_id='"
					+ msuId + "'";
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public static String[][] getRuleMsuTable(String msuId) {
		String[][] rs = null;
		try {
			String sql = SQLGenator.genSQL("meta041", msuId, msuId);
			rs = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static Hashtable getMsuTable(String msu_id) {
		Hashtable hash = new Hashtable();
		String sql = null;
		try {
			sql = "select table_id from UI_META_RULE_TABLE_MEASURE where msu_id ='"
					+ msu_id + "'";

			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				for (int i = 0; i < rs.length; i++) {
					hash.put(rs[i][0], "1");
				}
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return hash;
	}

	public static int setMsuTable(HttpServletRequest request, String msu_id,
			Vector rgChkArr) {
		int flag = -1; // 返回值
		Hashtable sourceHashArr = getMsuTable(msu_id);

		// SQL
		Vector sqlArr = new Vector();

		// 删除串
		String delStr = "";
		for (int i = 0; i < rgChkArr.size(); i++) {
			// 重构
			KeyValueStruct keyValueStruct = (KeyValueStruct) rgChkArr.get(i);
			// 判断
			if (keyValueStruct.getValue().equals("1")) { // 被选中的区域
				// 不再界面选中的区域列表中插入
				if (sourceHashArr.get(keyValueStruct.getKey()) == null) {
					String sql = "INSERT INTO UI_META_RULE_TABLE_MEASURE (msu_id,table_id) VALUES ('"
							+ msu_id + "','" + keyValueStruct.getKey() + "')";
					sqlArr.add(sql);
				}

			} else {
				// 没有选中的区域
				if (sourceHashArr.get(keyValueStruct.getKey()) != null) { // 删除
					delStr += ",'" + keyValueStruct.getKey() + "'";
				}
			}
		}
		if (delStr.length() > 0) {
			String del_sql = "DELETE FROM UI_META_RULE_TABLE_MEASURE WHERE msu_id= '"
					+ msu_id
					+ "' and table_id in ("
					+ delStr.substring(1)
					+ ")";
			sqlArr.add(del_sql);
		}

		// 转化
		String sqlv[] = new String[sqlArr.size()];
		for (int i = 0; i < sqlArr.size(); i++) {
			sqlv[i] = sqlArr.get(i).toString();
			System.out.println("sqlv[i]==================" + sqlv[i]);
		}
		// 执行
		try {
			flag = 1;
			if (sqlv.length > 0) {
				WebDBUtil.execTransUpdate(sqlv);
			}
		} catch (Exception e) {
			System.out
					.println("setRoleRegions exec sqls err:" + e.getMessage());
			for (int i = 0; i < sqlv.length; i++) {
				System.out.println("err sqlv[" + i + "]=================="
						+ sqlv[i]);
			}
			// e.printStackTrace();
			flag = -1;
		}
		return flag;
	}

}
