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
import com.ailk.bi.metamanage.dao.IDimDefDao;
import com.ailk.bi.metamanage.model.Dimension;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DimDefDaoImpl implements IDimDefDao {

	public Dimension getDim(String dimId) {
		List list = null;
		Dimension info = new Dimension();
		String sql = "select a.*,b.dim_type_name from UI_META_INFO_DIMENSIONS a,UI_META_INFO_DIM_TYPE b where a.dim_type_id=b.dim_type_id and a.dim_id='"
				+ dimId + "'";
		try {
			list = WebDBUtil.execQryArrayMap(sql, null);
			if (list != null && list.size() > 0) {
				HashMap map = (HashMap) list.get(0);
				info.setDim_id((String) map.get("DIM_ID"));
				info.setDim_name((String) map.get("DIM_NAME"));
				info.setDim_desc((String) map.get("DIM_DESC"));
				info.setDim_type_id((String) map.get("DIM_TYPE_ID"));
				info.setDim_type_name((String) map.get("DIM_TYPE_NAME"));
				info.setDim_unit((String) map.get("DIM_UNIT"));
				info.setCode_id_field((String) map.get("CODE_ID_FIELD"));
				info.setId_field_type((String) map.get("ID_FIELD_TYPE"));
				info.setCode_desc_field((String) map.get("CODE_DESC_FIELD"));
				info.setDim_rule((String) map.get("DIM_RULE"));
				info.setDim_map_code((String) map.get("DIM_MAP_CODE"));
				info.setDim_lvl((String) map.get("DIM_LVL"));
				info.setIs_same_dim((String) map.get("IS_SAME_DIM"));
				info.setSequence((String) map.get("SEQUENCE"));
				info.setStatus((String) map.get("STATUS"));
				info.setDim_chain_id((String) map.get("DIM_CHAIN_ID"));
				info.setDim_table_id((String) map.get("DIM_TABLE_ID"));
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return info;
	}

	private String getInsertSql(HttpServletRequest request) {
		String sql = "";
		String dim_id = StringB.NulltoBlank(request.getParameter("dim_id"));
		String dim_name = StringB.NulltoBlank(request.getParameter("dim_name"));
		String dim_desc = StringB.NulltoBlank(request.getParameter("dim_desc"));
		String dim_type_id = StringB.NulltoBlank(request
				.getParameter("dim_type_id"));
		String dim_unit = StringB.NulltoBlank(request.getParameter("dim_unit"));
		String code_id_field = StringB.NulltoBlank(request
				.getParameter("code_id_field"));
		String id_field_type = StringB.NulltoBlank(request
				.getParameter("id_field_type"));
		String code_desc_field = StringB.NulltoBlank(request
				.getParameter("code_desc_field"));
		String dim_rule = StringB.NulltoBlank(request.getParameter("dim_rule"));
		String dim_map_code = StringB.NulltoBlank(request
				.getParameter("dim_map_code"));
		String dim_lvl = StringB.NulltoBlank(request.getParameter("dim_lvl"));
		String is_same_dim = StringB.NulltoBlank(request
				.getParameter("is_same_dim"));
		String sequence = StringB.NulltoBlank(request.getParameter("sequence"));
		String status = StringB.NulltoBlank(request.getParameter("status"));
		String dim_chain_id = StringB.NulltoBlank(request
				.getParameter("dim_chain_id"));
		String dim_table_id = StringB.NulltoBlank(request
				.getParameter("dim_table_id"));
		List svc = new ArrayList();
		svc.add(dim_id);
		svc.add(dim_name);
		svc.add(dim_desc);
		svc.add(dim_type_id);
		svc.add(dim_unit);
		svc.add(code_id_field);
		svc.add(id_field_type);
		svc.add(code_desc_field);
		svc.add(dim_rule);
		svc.add(dim_map_code);
		svc.add("".equals(dim_lvl) ? "null" : dim_lvl);
		svc.add(is_same_dim);
		svc.add("".equals(sequence) ? "null" : sequence);
		svc.add(status);
		svc.add(dim_chain_id);
		svc.add(dim_table_id);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}
		try {
			sql = SQLGenator.genSQL("meta030", param);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return sql;
	}

	public boolean isExistDimId(String dimId) {
		boolean flag = false;
		try {
			String sql = "select * from UI_META_INFO_DIMENSIONS where dim_id='"
					+ dimId + "'";
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
		// "delete from UI_META_INFO_DIMENSIONS where dim_id='"+request.getParameter("dim_id")+"'";
		// strSql[1] = getInsertSql(request);
		String str = "";
		String[] param = new String[] { "dim_name", "dim_desc", "dim_type_id",
				"dim_unit", "code_id_field", "id_field_type",
				"code_desc_field", "dim_rule", "dim_map_code",
				"dim_lvl##number", "is_same_dim", "sequence##number", "status",
				"dim_chain_id", "dim_table_id" };

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
		strSql[0] = "update UI_META_INFO_DIMENSIONS set "
				+ str.substring(0, str.length() - 1) + " where dim_id='"
				+ request.getParameter("dim_id") + "'";
		try {
			WebDBUtil.execTransUpdate(strSql);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void del(String dimId) {
		try {
			String sql = "delete from UI_META_INFO_DIMENSIONS where dim_id='"
					+ dimId + "'";
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public static String[][] getRuleDimTable(String dimId) {
		String[][] rs = null;
		try {
			String sql = SQLGenator.genSQL("meta031", dimId, dimId);
			rs = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static Hashtable getDimTable(String dim_id) {
		Hashtable hash = new Hashtable();
		String sql = null;
		try {
			sql = "select table_id from UI_META_RULE_TABLE_DIMONSION where dim_id ='"
					+ dim_id + "'";

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

	public static int setDimTable(HttpServletRequest request, String dim_id,
			Vector rgChkArr) {
		int flag = -1; // 返回值
		Hashtable sourceHashArr = getDimTable(dim_id);

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
					String sql = "INSERT INTO UI_META_RULE_TABLE_DIMONSION (dim_id,table_id) VALUES ('"
							+ dim_id + "','" + keyValueStruct.getKey() + "')";
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
			String del_sql = "DELETE FROM UI_META_RULE_TABLE_DIMONSION WHERE dim_id= '"
					+ dim_id
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
