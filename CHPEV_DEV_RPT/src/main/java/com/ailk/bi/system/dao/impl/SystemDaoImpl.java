package com.ailk.bi.system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.dao.ISystemDao;
import com.ailk.bi.system.entity.SystemInfo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SystemDaoImpl implements ISystemDao {

	public SystemInfo getSystem(String systemId) {
		List list = null;
		SystemInfo info = new SystemInfo();
		String sql = "select * from UI_INFO_SUB_SYSTEM where system_id='"
				+ systemId + "'";
		try {
			list = WebDBUtil.execQryArrayMap(sql, null);
			if (list != null && list.size() > 0) {
				HashMap map = (HashMap) list.get(0);
				info.setSystem_id((String) map.get("SYSTEM_ID"));
				info.setSystem_name((String) map.get("SYSTEM_NAME"));
				info.setSystem_desc((String) map.get("SYSTEM_DESC"));
				info.setSystem_url((String) map.get("SYSTEM_URL"));
				info.setStatus((String) map.get("STATUS"));
				info.setSequence((String) map.get("SEQUENCE"));
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return info;
	}

	private String getInsertSql(HttpServletRequest request) {
		String sql = "";
		String system_id = StringB.NulltoBlank(request
				.getParameter("system_id"));
		String system_name = StringB.NulltoBlank(request
				.getParameter("system_name"));
		String system_desc = StringB.NulltoBlank(request
				.getParameter("system_desc"));
		String system_url = StringB.NulltoBlank(request
				.getParameter("system_url"));
		String status = StringB.NulltoBlank(request.getParameter("status"));
		String sequence = StringB.NulltoBlank(request.getParameter("sequence"));
		List svc = new ArrayList();
		svc.add(system_id);
		svc.add(system_name);
		svc.add(system_desc);
		svc.add(system_url);
		svc.add(status);
		svc.add(sequence);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}
		try {
			sql = SQLGenator.genSQL("system001", param);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return sql;
	}

	public boolean isExistSystemId(String systemId) {
		boolean flag = false;
		try {
			String sql = "select * from UI_INFO_SUB_SYSTEM where system_id='"
					+ systemId + "'";
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
		String str = "";
		String[] param = new String[] { "system_name", "system_desc",
				"system_url", "status", "sequence" };

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
		strSql[0] = "update UI_INFO_SUB_SYSTEM set "
				+ str.substring(0, str.length() - 1) + " where system_id='"
				+ request.getParameter("system_id") + "'";

		try {
			WebDBUtil.execTransUpdate(strSql);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void del(String systemId) {
		try {
			String sql = "delete from UI_INFO_SUB_SYSTEM where system_id='"
					+ systemId + "'";
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}
}
