package com.ailk.bi.metamanage.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.metamanage.dao.ITableDefDao;
import com.ailk.bi.metamanage.model.TableDef;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableDefDaoImpl implements ITableDefDao {

	public TableDef getTableDef(String tableId) {
		List list = null;
		TableDef info = new TableDef();
		String sql = "select a.*,to_char(a.last_data_date,'yyyymmdd') as LAST_DATE,b.layer_name from UI_META_INFO_TABLE_DEF a,UI_META_INFO_LAYER b where a.layer_id=b.layer_id and table_id='"
				+ tableId + "'";
		try {
			list = WebDBUtil.execQryArrayMap(sql, null);
			if (list != null && list.size() > 0) {
				HashMap map = (HashMap) list.get(0);
				info.setTable_id((String) map.get("TABLE_ID"));
				info.setTable_type_id((String) map.get("TABLE_TYPE_ID"));
				info.setFlow_id((String) map.get("FLOW_ID"));
				info.setTable_name((String) map.get("TABLE_NAME"));
				info.setTable_desc((String) map.get("TABLE_DESC"));
				info.setData_cycle((String) map.get("DATA_CYCLE"));
				info.setLayer_id((String) map.get("LAYER_ID"));
				info.setLayer_name((String) map.get("LAYER_NAME"));
				info.setDomain_id((String) map.get("DOMAIN_ID"));
				info.setSys_id((String) map.get("SYS_ID"));
				info.setJob_id((String) map.get("JOB_ID"));
				info.setLast_data_date((String) map.get("LAST_DATE"));
				info.setTablespace_id((String) map.get("TABLESPACE_ID"));
				info.setIs_source_flag((String) map.get("IS_SOURCE_FLAG"));
				info.setApp_type((String) map.get("APP_TYPE"));
				info.setApp_script((String) map.get("APP_SCRIPT"));
				info.setApp_table_ids((String) map.get("APP_TABLE_IDS"));

			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return info;
	}

	public List getDomain(String layerId) {
		List list = new ArrayList();
		try {
			String sql = "select domain_id,domain_name from UI_META_INFO_DOMAIN where layer_id='"
					+ layerId + "' order by sequence";
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; rs != null && i < rs.length; i++) {
				list.add(rs[i]);
			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return list;
	}

	public List getEtlJob(String flowId) {
		List list = new ArrayList();
		try {
			String sql = "select job_id,job_name from UI_META_INFO_ETL_JOB where flow_id='"
					+ flowId + "' order by job_name";
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; rs != null && i < rs.length; i++) {
				list.add(rs[i]);
			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return list;
	}

	private String getInsertSql(HttpServletRequest request) {
		String sql = "";
		String table_id = StringB.NulltoBlank(request.getParameter("table_id"));
		String table_type_id = StringB.NulltoBlank(request
				.getParameter("table_type_id"));
		String flow_id = StringB.NulltoBlank(request.getParameter("flow_id"));
		String table_name = StringB.NulltoBlank(request
				.getParameter("table_name"));
		String table_desc = StringB.NulltoBlank(request
				.getParameter("table_desc"));
		String data_cycle = StringB.NulltoBlank(request
				.getParameter("data_cycle"));
		String layer_id = StringB.NulltoBlank(request.getParameter("layer_id"));
		String domain_id = StringB.NulltoBlank(request
				.getParameter("domain_id"));
		String sys_id = StringB.NulltoBlank(request.getParameter("sys_id"));
		String job_id = StringB.NulltoBlank(request.getParameter("job_id"));
		String last_data_date = StringB.NulltoBlank(request
				.getParameter("last_data_date"));
		String tablespace_id = StringB.NulltoBlank(request
				.getParameter("tablespace_id"));
		String is_source_flag = StringB.NulltoBlank(request
				.getParameter("is_source_flag"));
		String app_type = StringB.NulltoBlank(request.getParameter("app_type"));
		String app_script = StringB.NulltoBlank(request
				.getParameter("app_script"));
		String app_table_ids = StringB.NulltoBlank(request
				.getParameter("app_table_ids"));
		List svc = new ArrayList();
		svc.add(table_id);
		svc.add(table_type_id);
		svc.add(flow_id);
		svc.add(table_name);
		svc.add(table_desc);
		svc.add(data_cycle);
		svc.add(layer_id);
		svc.add(domain_id);
		svc.add(sys_id);
		svc.add(job_id);
		svc.add(last_data_date);
		svc.add(tablespace_id);
		svc.add(is_source_flag);
		svc.add(app_type);
		svc.add(app_script);
		svc.add(app_table_ids);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}
		try {
			sql = SQLGenator.genSQL("meta010", param);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return sql;
	}

	public boolean isExistTableId(String tableId) {
		boolean flag = false;
		try {
			String sql = "select * from UI_META_INFO_TABLE_DEF where table_id='"
					+ tableId + "'";
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
		// "delete from UI_META_INFO_TABLE_DEF where table_id='"+request.getParameter("table_id")+"'";
		// strSql[1] = getInsertSql(request);
		String str = "";
		String[] param = new String[] { "table_type_id", "flow_id",
				"table_name", "table_desc", "data_cycle", "layer_id",
				"domain_id", "sys_id", "job_id", "last_data_date##date",
				"tablespace_id", "is_source_flag", "app_type", "app_script",
				"app_table_ids" };

		for (int i = 0; i < param.length; i++) {
			String[] arr = param[i].split("##");
			String value = StringB.NulltoBlank(request.getParameter(arr[0]));
			if (arr.length == 1) {
				str += arr[0] + "='" + value + "',";
			} else {
				if (!"".equals(value))
					str += arr[0] + "=to_date('" + value + "','yyyymmdd'),";
				else
					str += arr[0] + "=null,";
			}
		}
		strSql[0] = "update UI_META_INFO_TABLE_DEF set "
				+ str.substring(0, str.length() - 1) + " where table_id='"
				+ request.getParameter("table_id") + "'";

		try {
			WebDBUtil.execTransUpdate(strSql);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void del(String tableId) {
		try {
			String sql = "delete from UI_META_INFO_TABLE_DEF where table_id='"
					+ tableId + "'";
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public List getTableField(String tableId) {
		List list = null;
		String sql = " select a.*,field_id||'$$'||field_name||'$$'||filed_desc||'$$'||filed_type||'$$'||filed_en_code||'$$'||data_type||'$$'||data_length||'$$'||data_precision||'$$'||field_rule||'$$'||field_rule_desc||'$$'||filed_column_id||'$$'||field_unit as param "
				+ " from UI_META_INFO_TABLE_FIELD a where a.table_id='"
				+ tableId + "'";
		try {
			list = WebDBUtil.execQryArrayMap(sql, null);

		} catch (AppException e) {
			e.printStackTrace();
		}
		return list;
	}

	private String getTableFieldInsertSql(HttpServletRequest request) {
		String sql = "";
		String table_id = StringB.NulltoBlank(request.getParameter("table_id"));
		String field_id = StringB.NulltoBlank(request.getParameter("field_id"));
		String field_name = StringB.NulltoBlank(request
				.getParameter("field_name"));
		String filed_desc = StringB.NulltoBlank(request
				.getParameter("filed_desc"));
		String filed_type = StringB.NulltoBlank(request
				.getParameter("filed_type"));
		String filed_en_code = StringB.NulltoBlank(request
				.getParameter("filed_en_code"));
		String data_type = StringB.NulltoBlank(request
				.getParameter("data_type"));
		String data_length = StringB.NulltoBlank(request
				.getParameter("data_length"));
		String data_precision = StringB.NulltoBlank(request
				.getParameter("data_precision"));
		String field_rule = StringB.NulltoBlank(request
				.getParameter("field_rule"));
		String field_rule_desc = StringB.NulltoBlank(request
				.getParameter("field_rule_desc"));
		String filed_column_id = StringB.NulltoBlank(request
				.getParameter("filed_column_id"));
		String field_unit = StringB.NulltoBlank(request
				.getParameter("field_unit"));
		List svc = new ArrayList();
		svc.add(table_id);
		svc.add(field_id);
		svc.add(field_name);
		svc.add(filed_desc);
		svc.add(filed_type);
		svc.add(filed_en_code);
		svc.add(data_type);
		svc.add("".equals(data_length) ? "null" : data_length);
		svc.add("".equals(data_precision) ? "null" : data_precision);
		svc.add(field_rule);
		svc.add(field_rule_desc);
		svc.add("".equals(filed_column_id) ? "null" : filed_column_id);
		svc.add(field_unit);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}
		try {
			sql = SQLGenator.genSQL("meta011", param);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return sql;
	}

	public void addTableField(HttpServletRequest request) {
		try {
			WebDBUtil.execUpdate(getTableFieldInsertSql(request));
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void saveTableField(HttpServletRequest request) {
		String[] strSql = new String[2];
		strSql[0] = "delete from UI_META_INFO_TABLE_FIELD where table_id='"
				+ request.getParameter("table_id") + "' and field_id='"
				+ request.getParameter("field_id") + "'";
		strSql[1] = getTableFieldInsertSql(request);
		try {
			WebDBUtil.execTransUpdate(strSql);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void delTableField(String tableId, String field_id) {
		try {
			String sql = "delete from UI_META_INFO_TABLE_FIELD where table_id='"
					+ tableId + "' and field_id='" + field_id + "'";
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}
}
