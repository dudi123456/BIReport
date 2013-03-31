package com.ailk.bi.base.util;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.table.PubInfoChartDefTable;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.util.ReportConsts;

@SuppressWarnings({ "rawtypes" })
public class CommConditionUtil {

	private static String char_data = TableConsts.DATA_TYPE_STRING;

	private static String num_data = TableConsts.DATA_TYPE_NUMER;

	private static String condition_pub = TableConsts.CONDITION_PUB;

	private static String condition_chart = TableConsts.CONDITION_CHART;

	private static String condition_table = TableConsts.CONDITION_TABLE;

	/**
	 * 根据获取值和定义生成条件
	 *
	 * @param value
	 * @param data_type
	 * @param con_code
	 * @param con_tag
	 * @return
	 */
	public static String genWherePart(String value, String data_type,
			String con_code, String con_tag) {
		String where = "";
		con_tag = con_tag.toLowerCase();
		if (value != null && !"".equals(value.trim()) && !ReportConsts.RPT_EXPAND_VALUE.equals(value)) {
			if (con_code == null || "".equals(con_code.trim())) {
				where = " ";
			} else {
				where += " AND ";
			}
			if ("in".equals(con_tag)) {
				where += con_code;
				where += " " + con_tag + "(";
			} else if ("like_l".equals(con_tag)) {
				where += con_code;
				where += " like";
			} else if ("like_r".equals(con_tag)) {
				where += con_code;
				where += " like";
			} else {
				where += con_code;
				where += " " + con_tag;
			}

			if (char_data.equals(data_type)) {
				if ("in".equals(con_tag)) {
					value = StringB.replace(value, ":add:", ",");
					value = StringTool.changArrToStr(value.split(","), "'");
					where += value;
				} else if ("like".equals(con_tag)) {
					where += " '%" + value + "%'";
				} else if ("like_l".equals(con_tag)) {
					where += " '%" + value + "'";
				} else if ("like_r".equals(con_tag)) {
					where += " '" + value + "%'";
				} else {
					where += "'" + value + "'";
				}
			}
			if (num_data.equals(data_type)) {
				if ("in".equals(con_tag)) {
					value = StringB.replace(value, ":add:", ",");
					value = StringTool.changArrToStr(value.split(","), "");
					where += value;
				} else if ("like".equals(con_tag)) {
					where += " '%" + value + "%'";
				} else if ("like_l".equals(con_tag)) {
					where += " '%" + value + "'";
				} else if ("like_r".equals(con_tag)) {
					where += " '" + value + "%'";
				} else {
					where += value;
				}
			}

			if ("in".equals(con_tag)) {
				where += ")";
			}

			if ("AND".equals(where.trim())) {
				where = "";
			}
		}
		return where;
	}

	/**
	 * 获取统一资源生成的条件专用(condition_pub)
	 *
	 * @param res_id
	 * @param request
	 * @param qryStruct
	 * @return
	 * @throws AppException
	 */
	public static String getPubWhere(String res_id, HttpServletRequest request,
			Object qryStruct) {
		// 获取公共链接条件信息
		PubInfoConditionTable[] conTable = null;
		try {
			conTable = genCondition(res_id, condition_pub);
		} catch (AppException e) {
			return null;
		}

		String result = getPubWhere(conTable, request, qryStruct);
		return result;
	}

	/**
	 * 获取资源生成的条件(request专用，不包含session传入条件)
	 *
	 * @param conTable
	 * @param request
	 * @return
	 * @throws AppException
	 */
	public static String getPubWhere(PubInfoConditionTable[] conTable,
			HttpServletRequest request) {
		return getPubWhere(conTable, request, null, "1");
	}

	/**
	 * 获取资源生成的条件(session专用，不包括request传入条件)
	 *
	 * @param conTable
	 * @param qryStruct
	 * @return
	 * @throws AppException
	 */
	public static String getPubWhere(PubInfoConditionTable[] conTable,
			Object qryStruct) {

		String where = getPubWhere(conTable, null, qryStruct, "0");

		where += getPubWhere(conTable, null, qryStruct, "2");

		where += getPubWhere(conTable, null, qryStruct, "3");

		return where;

	}

	/**
	 * 获取资源生成的条件(全部条件)
	 *
	 * @param conTable
	 * @param request
	 * @param qryStruct
	 * @return
	 * @throws AppException
	 */
	public static String getPubWhere(PubInfoConditionTable[] conTable,
			HttpServletRequest request, Object qryStruct) {
		String where = getPubWhere(conTable, request, qryStruct, "0");
		where += getPubWhere(conTable, request, qryStruct, "1");
		where += getPubWhere(conTable, request, qryStruct, "2");
		where += getPubWhere(conTable, request, qryStruct, "3");
		return where;
	}

	/**
	 * 获取资源生成的条件(获取指定类型的条件)
	 *
	 * @param conTable
	 * @param request
	 * @param qryStruct
	 * @param qryType
	 * @return
	 */
	public static String getPubWhere(PubInfoConditionTable[] conTable,
			HttpServletRequest request, Object qryStruct, String qryType) {
		String where = "";
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			String tmpCode = conTable[i].qry_code.toLowerCase();
			String tmpValue = "";
			String data_type = conTable[i].data_type;
			String con_code = conTable[i].con_code;
			String con_tag = conTable[i].con_tag;
			if ("0".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 0:结构体条件(session传入)
				// System.out.println("tmpCode=" + tmpCode);
				// System.out.println("data_type=" + data_type);
				// System.out.println("con_code=" + con_code);
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				// System.out.println("tmpValue0=" + tmpValue);
				where += genWherePart(tmpValue, data_type, con_code, con_tag);
			}
			if ("1".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 1:外部条件(request传入)
				tmpValue = request.getParameter(conTable[i].qry_code);
				// System.out.println("tmpValue1=" + tmpValue);
				where += genWherePart(tmpValue, data_type, con_code, con_tag);
			}
			if ("2".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 2:替换条件(session传入)，需要其他方法扩展实现，如：getChartWhere
			}
			if ("3".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 3:附加条件(session传入，可用于权限或者其他)
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				// System.out.println("tmpValue3=" + tmpValue);
				if (tmpValue != null && !"".equals(tmpValue)) {
					if (tmpValue.toUpperCase().indexOf(" AND ") >= 0)
						where += " " + tmpValue;
					else
						where += " AND " + tmpValue;
				}
			}
			if ("4".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 1:外部条件(request传入)
				tmpValue = request.getParameter(conTable[i].qry_code);
				// System.out.println("tmpValue1=" + tmpValue);
				where += genWherePart(tmpValue, data_type, con_code, con_tag);
			}
		}
		return where;
	}

	/**
	 * 获取资源生成的条件(获取指定类型的条件)
	 *
	 * @param conTable
	 * @param request
	 * @param qryStruct
	 * @param qryType
	 * @return
	 */
	public static String getPubHaving(PubInfoConditionTable[] conTable,
			HttpServletRequest request, Object qryStruct, String qryType) {
		String where = "";
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			String tmpCode = conTable[i].qry_code.toLowerCase();
			String tmpValue = "";
			String data_type = conTable[i].data_type;
			String con_code = conTable[i].con_code;
			String con_tag = conTable[i].con_tag;
			if ("0".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 0:结构体条件(session传入)
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				// System.out.println("tmpValue0=" + tmpValue);
				where += genWherePart(tmpValue, data_type, con_code, con_tag);
			}
			if ("1".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 1:外部条件(request传入)
				tmpValue = request.getParameter(conTable[i].qry_code);
				// System.out.println("tmpValue1=" + tmpValue);
				where += genWherePart(tmpValue, data_type, con_code, con_tag);
			}
			if ("2".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				// System.out.println("tmpValue3=" + tmpValue);
				if (tmpValue != null && !"".equals(tmpValue)) {
					if (tmpValue.toUpperCase().indexOf(" AND ") >= 0)
						where += " " + tmpValue;
					else
						where += " AND " + tmpValue;
				}
				// 2:替换条件(session传入)，需要其他方法扩展实现，如：getChartWhere
			}
			if ("3".equals(conTable[i].qry_type)
					&& qryType.equals(conTable[i].qry_type)) {
				// 3:附加条件(session传入，可用于权限或者其他)
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				// System.out.println("tmpValue3=" + tmpValue);
				if (tmpValue != null && !"".equals(tmpValue)) {
					if (tmpValue.toUpperCase().indexOf(" AND ") >= 0)
						where += " " + tmpValue;
					else
						where += " AND " + tmpValue;
				}
			}
		}
		return where;
	}

	/**
	 * 获取图形条件
	 *
	 * @param ctable
	 * @param request
	 * @param qryStruct
	 * @return
	 */
	public static String getChartWhere(PubInfoChartDefTable ctable,
			HttpServletRequest request, Object qryStruct) {
		// 获取图形链接条件信息
		PubInfoConditionTable[] conTable = null;
		try {
			conTable = genCondition(ctable.chart_id, condition_chart);
		} catch (AppException e) {
			return null;
		}
		return getChartWhere(ctable, conTable, request, qryStruct);
	}

	/**
	 * 获取图形条件
	 *
	 * @param ctable
	 * @param request
	 * @param qryStruct
	 * @return
	 */
	public static String getChartWhere(PubInfoChartDefTable ctable,
			PubInfoConditionTable[] conTable, HttpServletRequest request,
			Object qryStruct) {
		String where = " " + ctable.sql_where;
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			String tmpCode = conTable[i].qry_code.toLowerCase();
			String tmpValue = "";
			String data_type = conTable[i].data_type;
			String con_code = conTable[i].con_code;
			String con_tag = conTable[i].con_tag;
			if ("0".equals(conTable[i].qry_type)) {
				// 0:结构体条件(session传入)
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				where += genWherePart(tmpValue, data_type, con_code, con_tag);
			}
			if ("1".equals(conTable[i].qry_type)) {
				// 1:外部条件(request传入)
				tmpValue = request.getParameter(conTable[i].qry_code);
				where += genWherePart(tmpValue, data_type, con_code, con_tag);
			}
			if ("2".equals(conTable[i].qry_type)) {
				// 2:替换条件(session传入)
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				String tmpStr = genWherePart(tmpValue, data_type, con_code,
						con_tag);
				ctable.sql_main = StringB.replaceFirst(ctable.sql_main, "?",
						tmpStr);
			}
			if ("3".equals(conTable[i].qry_type)) {
				// 3:附加条件(session传入，可用于权限或者其他)
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				if (tmpValue != null && !"".equals(tmpValue)) {
					if (tmpValue.toUpperCase().indexOf(" AND ") >= 0)
						where += " " + tmpValue;
					else
						where += " AND " + tmpValue;
				}
			}

			// 趋势取值
			if (StringB.toInt(ctable.series_length, 0) > 0) {
				String end_value = ""; // 结束值，向前或者向后取值
				int iPre = 0 - StringB.toInt(ctable.series_length, 0);
				int iAfter = StringB.toInt(ctable.series_length, 0);
				if ("<=".equals(conTable[i].con_tag)) {
					if (tmpValue != null && tmpValue.length() == 6) {
						end_value = DateUtil.getDiffMonth(iPre, tmpValue);
					}
					if (tmpValue != null && tmpValue.length() == 8) {
						end_value = DateUtil.getDiffDay(iPre, tmpValue);
					}
				}
				if (">=".equals(conTable[i].con_tag)) {
					if (tmpValue != null && tmpValue.length() == 6) {
						end_value = DateUtil.getDiffMonth(iAfter, tmpValue);
					}
					if (tmpValue != null && tmpValue.length() == 8) {
						end_value = DateUtil.getDiffDay(iAfter, tmpValue);
					}
				}

				if (end_value != null && !"".equals(end_value)) {
					if ("<=".equals(conTable[i].con_tag)) {
						con_tag = ">";
						where += genWherePart(end_value, data_type, con_code,
								con_tag);
					}
					if (">=".equals(conTable[i].con_tag)) {
						con_tag = "<=";
						where += genWherePart(end_value, data_type, con_code,
								con_tag);
					}
				}
			}

			// 当月取值
			if (StringB.toInt(ctable.series_length, 0) == -1) {
				if (tmpValue != null && tmpValue.length() == 8) {
					String end_value = tmpValue.subSequence(0, 6) + "01";
					if ("<=".equals(conTable[i].con_tag)) {
						con_tag = ">=";
						where += genWherePart(end_value, data_type, con_code,
								con_tag);
					}
				}
				if (tmpValue != null && tmpValue.length() == 6) {
					String end_value = tmpValue.subSequence(0, 4) + "01";
					if ("<=".equals(conTable[i].con_tag)) {
						con_tag = ">=";
						where += genWherePart(end_value, data_type, con_code,
								con_tag);
					}
				}
			}
		}
		return where;
	}

	/**
	 * 获取表格条件
	 *
	 * @param table_id
	 * @param request
	 * @param qryStruct
	 * @return
	 */
	public static String getTableWhere(String table_id,
			HttpServletRequest request, Object qryStruct) {
		// 获取图形链接条件信息
		PubInfoConditionTable[] conTable = null;
		try {
			conTable = genCondition(table_id, condition_table);
		} catch (AppException e) {
			return null;
		}
		return getPubWhere(conTable, request, qryStruct);
	}

	public static String getTableSpeWhere(String table_id,
			HttpServletRequest request, Object qryStruct) {
		// 获取图形链接条件信息
		PubInfoConditionTable[] conTable = null;
		try {
			conTable = genCondition(table_id, condition_table);
		} catch (AppException e) {
			return null;
		}
		return getPubWhere(conTable, request, qryStruct, "4");
	}

	/**
	 * 获得报表外部条件
	 *
	 * @param conTable
	 * @param request
	 * @return
	 */
	public static String getRptWhere(PubInfoConditionTable[] conTable,
			HttpServletRequest request) {
		return getPubWhere(conTable, request);
	}

	/**
	 * 获得报表外部条件
	 *
	 * @param conTable
	 * @param qryStruct
	 * @return
	 */
	public static String getRptWhere(PubInfoConditionTable[] conTable,
			Object qryStruct) {
		return getPubWhere(conTable, qryStruct);
	}

	/**
	 * 设置报表回写条件
	 *
	 * @param conTable
	 * @param request
	 * @return
	 */
	public static String setRptWhere(PubInfoConditionTable[] conTable,
			HttpServletRequest request) {
		String where = "";
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			if ("1".equals(conTable[i].qry_type)) {
				// 1:外部条件(request传入)
				String tmpvalue = request.getParameter(conTable[i].qry_code);
				if (tmpvalue != null && !"".equals(tmpvalue)) {
					where += "&" + conTable[i].qry_code + "=" + tmpvalue;
				}
			}
		}
		return where;
	}

	/**
	 * 直接解析request.getQueryString()中的条件
	 *
	 * @param res_id
	 * @param res_type
	 * @param strRequest
	 * @return
	 * @throws AppException
	 */
	public static String getStringWhere(String res_id, String res_type,
			String strRequest) throws AppException {
		String where = "";
		// 如果传入的串空则不做解析
		if (strRequest == null || "".equals(strRequest.trim())) {
			return "";
		}
		PubInfoConditionTable[] conTable = genCondition(res_id, res_type);
		String[] cons = strRequest.split("&");
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			if ("1".equals(conTable[i].qry_type)) {
				for (int j = 0; cons != null && j < cons.length; j++) {
					if (cons[j].indexOf(conTable[i].qry_code) >= 0) {
						String value = StringB.replace(cons[j],
								conTable[i].qry_code + "=", "");
						where += genWherePart(value, conTable[i].data_type,
								conTable[i].con_code, conTable[i].con_tag);
					}
				}
			}
		}
		return where;
	}

	/**
	 * 直接解析request.getQueryString()中的条件
	 *
	 * @param conTable
	 * @param strRequest
	 * @return
	 * @throws AppException
	 */
	public static String getStringWhere(PubInfoConditionTable[] conTable,
			String strRequest) throws AppException {
		String where = "";
		// 如果传入的串空则不做解析
		if (strRequest == null || "".equals(strRequest.trim())) {
			return "";
		}
		String[] cons = strRequest.split("&");
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			if ("1".equals(conTable[i].qry_type)) {
				for (int j = 0; cons != null && j < cons.length; j++) {
					if (cons[j].indexOf(conTable[i].qry_code) >= 0) {
						String value = StringB.replace(cons[j],
								conTable[i].qry_code + "=", "");
						where += genWherePart(value, conTable[i].data_type,
								conTable[i].con_code, conTable[i].con_tag);
					}
				}
			}
		}
		return where;
	}

	/**
	 * 获取公共链接条件信息
	 *
	 * @return
	 * @throws AppException
	 */
	public static PubInfoConditionTable[] genCondition(String res_id,
			String res_type) throws AppException {
		PubInfoConditionTable[] conditions = null;
		String strSql = SQLGenator.genSQL("TQ3006", res_type, res_id);
		// System.out.println("Sql TQ3006==>" + strSql);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result != null && result.size() > 0) {
			conditions = new PubInfoConditionTable[result.size()];
			int m = 0;
			for (int i = 0; i < conditions.length; i++) {
				Vector tempv = (Vector) result.get(i);
				conditions[i] = new PubInfoConditionTable();
				m = 0;
				conditions[i].res_id = (String) tempv.get(m++);
				conditions[i].res_type = (String) tempv.get(m++);
				conditions[i].qry_type = (String) tempv.get(m++);
				conditions[i].qry_code = (String) tempv.get(m++);
				conditions[i].con_code = (String) tempv.get(m++);
				conditions[i].data_type = (String) tempv.get(m++);
				conditions[i].con_tag = (String) tempv.get(m++);
			}
		}
		return conditions;
	}
}
