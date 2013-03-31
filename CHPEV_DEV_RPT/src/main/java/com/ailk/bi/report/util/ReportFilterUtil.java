package com.ailk.bi.report.util;

import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.domain.RptFilterTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.struct.ReportQryStruct;

public class ReportFilterUtil {

	/**
	 * 获取报表条件定义范围值
	 *
	 * @param rptTable
	 * @param rptFilterTables
	 * @param qryStruct
	 * @return
	 * @throws AppException
	 */
	public static String[] getRptFilterValue(RptResourceTable rptTable,
			RptFilterTable[] rptFilterTables, ReportQryStruct qryStruct)
			throws AppException {
		if (rptFilterTables == null || rptFilterTables.length == 0)
			return null;

		String[] result = new String[rptFilterTables.length];
		for (int i = 0; i < rptFilterTables.length; i++) {
			if (ReportConsts.FILTER_TYPE_TEXT
					.equals(rptFilterTables[i].filter_type)
					|| ReportConsts.FILTER_TYPE_SCRIPT
							.equals(rptFilterTables[i].filter_type)) {
				result[i] = "";
				continue;
			}

			if (ReportConsts.FILTER_DATA_SQL
					.equals(rptFilterTables[i].filter_datasource)) {
				// 从SQL语句提取
				String sql = rptFilterTables[i].filter_sql;
				// 区域条件的控制，对于各地系统需要变更
				if (sql.indexOf("?city?") >= 0) {
					String cityright = "";
					if (!StringTool.checkEmptyString(qryStruct.attach_region)) {
						cityright = "WHERE CITY_ID LIKE '"
								+ qryStruct.attach_region + "%'";
					}
					sql = StringB.replace(sql, "?city?", cityright);
				}
				// System.out.println("FILTER_DATA_SQL[i]=" + sql);
				String[][] temprs = WebDBUtil.execQryArray(sql, "");
				result[i] = StringTool.changArrsToStrAll(temprs);
				//展开扩展显示列表
				if(ReportConsts.FILTER_TYPE_MLIST.equals(rptFilterTables[i].filter_type)){
					if(result[i].length()>0){
						result[i] = "-99999999,合并;" + result[i];
					}
				}
				// System.out.println("result[i]=" + result[i]);
			} else if (ReportConsts.FILTER_DATA_TABLE
					.equals(rptFilterTables[i].filter_datasource)) {
				// 直接从数据表取值
				String sql = "SELECT/*+ RULE*/ DISTINCT "
						+ rptFilterTables[i].field_dim_code + ","
						+ rptFilterTables[i].field_code + " FROM "
						+ rptTable.data_table + " " + rptTable.data_where;
				if (qryStruct.date_s != null && !"".equals(qryStruct.date_s)) {
					sql += " and " + rptTable.data_date + ">="
							+ qryStruct.date_s;
				}
				if (qryStruct.date_e != null && !"".equals(qryStruct.date_e)) {
					sql += " and " + rptTable.data_date + "<="
							+ qryStruct.date_e;
				}
				sql += " ORDER BY " + rptFilterTables[i].field_dim_code;

				// System.out.println("sql[i]=" + sql);
				String[][] temprs = WebDBUtil.execQryArray(sql, "");
				result[i] = StringTool.changArrsToStrAll(temprs);
				// System.out.println("result[i]=" + result[i]);
			} else if (ReportConsts.FILTER_DATA_MAP
					.equals(rptFilterTables[i].filter_datasource)) {
				result[i] = rptFilterTables[i].filter_sql;
			} else {
				result[i] = "";
			}
		}
		return result;
	}

	/**
	 * 设置报表条件默认值
	 *
	 * @param rptFilter
	 *            条件定义
	 * @param paramValueAll
	 *            条件的列表值
	 * @throws AppException
	 */
	public static String getRptFilterDefault(RptFilterTable rptFilter,
			String paramValueAll) {
		if (rptFilter == null)
			return null;

		String paramDftValue = "";
		if (rptFilter.filter_default != null
				&& !"".equals(rptFilter.filter_default.trim())) {
			paramDftValue = rptFilter.filter_default;
		} else if (ReportConsts.YES.equals(rptFilter.filter_all)
				&& (rptFilter.filter_default == null || ""
						.equals(rptFilter.filter_default.trim()))) {
			paramDftValue = "";
		} else if (ReportConsts.NO.equals(rptFilter.filter_all)
				&& paramValueAll != null && !"".equals(paramValueAll.trim())) {
			paramDftValue = paramValueAll.substring(0,
					paramValueAll.indexOf(","));
		} else {
			paramDftValue = "";
		}
		return paramDftValue;
	}
}
