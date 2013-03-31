package com.ailk.bi.metamanage.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.metamanage.dao.IEtlJobDao;
import com.ailk.bi.metamanage.model.EtlJob;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EtlJobDaoImpl implements IEtlJobDao {

	public EtlJob getEtlJob(String jobId) {
		List list = null;
		EtlJob info = new EtlJob();
		String sql = "select * from UI_META_INFO_ETL_JOB where job_id='"
				+ jobId + "'";
		try {
			list = WebDBUtil.execQryArrayMap(sql, null);
			if (list != null && list.size() > 0) {
				HashMap map = (HashMap) list.get(0);
				info.setJob_id((String) map.get("JOB_ID"));
				info.setJob_name((String) map.get("JOB_NAME"));
				info.setJob_desc((String) map.get("JOB_DESC"));
				info.setJob_rule((String) map.get("JOB_RULE"));
				info.setFlow_id((String) map.get("FLOW_ID"));
				info.setStatus((String) map.get("STATUS"));
				info.setIn_table_id((String) map.get("IN_TABLE_ID"));
				info.setOut_table_id((String) map.get("OUT_TABLE_ID"));
				info.setJob_cron((String) map.get("JOB_CRON"));
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return info;
	}

	private String getInsertSql(HttpServletRequest request) {
		String sql = "";
		String job_id = StringB.NulltoBlank(request.getParameter("job_id"));
		String job_name = StringB.NulltoBlank(request.getParameter("job_name"));
		String job_desc = StringB.NulltoBlank(request.getParameter("job_desc"));
		String job_rule = StringB.NulltoBlank(request.getParameter("job_rule"));
		String flow_id = StringB.NulltoBlank(request.getParameter("flow_id"));
		String status = StringB.NulltoBlank(request.getParameter("status"));
		String in_table_id = StringB.NulltoBlank(request
				.getParameter("in_table_id"));
		String out_table_id = StringB.NulltoBlank(request
				.getParameter("out_table_id"));
		String job_cron = StringB.NulltoBlank(request.getParameter("job_cron"));
		List svc = new ArrayList();
		svc.add(job_id);
		svc.add(job_name);
		svc.add(job_desc);
		svc.add(job_rule);
		svc.add(flow_id);
		svc.add(status);
		svc.add(in_table_id);
		svc.add(out_table_id);
		svc.add(job_cron);
		String[] param = null;
		if (svc != null && svc.size() >= 0) {
			param = (String[]) svc.toArray(new String[svc.size()]);
		}
		try {
			sql = SQLGenator.genSQL("meta020", param);
		} catch (AppException e) {
			e.printStackTrace();
		}
		return sql;
	}

	public boolean isExistJobId(String jobId) {
		boolean flag = false;
		try {
			String sql = "select * from UI_META_INFO_ETL_JOB where job_id='"
					+ jobId + "'";
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
		String[] strSql = new String[2];
		// strSql[0] =
		// "delete from UI_META_INFO_ETL_JOB where job_id='"+request.getParameter("job_id")+"'";
		// strSql[1] = getInsertSql(request);
		String str = "";
		String[] param = new String[] { "job_name", "job_desc", "job_rule",
				"flow_id", "status", "in_table_id", "out_table_id", "job_cron" };

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
		strSql[0] = "update UI_META_INFO_ETL_JOB set "
				+ str.substring(0, str.length() - 1) + " where job_id='"
				+ request.getParameter("job_id") + "'";

		try {
			WebDBUtil.execTransUpdate(strSql);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public void del(String jobId) {
		try {
			String sql = "delete from UI_META_INFO_ETL_JOB where job_id='"
					+ jobId + "'";
			WebDBUtil.execUpdate(sql);
		} catch (AppException e) {

			e.printStackTrace();
		}
	}
}
