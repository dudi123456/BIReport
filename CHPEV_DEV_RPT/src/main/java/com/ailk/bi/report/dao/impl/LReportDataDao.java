package com.ailk.bi.report.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.dao.ILReportDataDao;
import com.ailk.bi.report.dao.IMReportDataDao;
import com.ailk.bi.report.domain.DateTable;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import com.ailk.bi.report.util.ReportObjUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LReportDataDao implements ILReportDataDao {

	private static Map showMap = null;
	private static HashMap colMap = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.dao.ILReportDataDao#getReportData(com.asiabi.report
	 * .domain.RptResourceTable, java.util.List, java.util.List,
	 * com.asiabi.report.struct.ReportQryStruct)
	 */
	public String[][] getReportData(Object rptTable, List rptColTable, Object qryStruct,
			PubInfoConditionTable[] cdnTables) throws AppException {
		if (rptTable == null || rptColTable == null || rptColTable.size() <= 0)
			throw new AppException();

		RptResourceTable report = (RptResourceTable) rptTable;
		ReportQryStruct query = (ReportQryStruct) qryStruct;
		String[][] svces = null;
		if (ReportConsts.YES.equals(query.visible_data) && ReportConsts.NO.equals(query.first_view)) {
			String strSql = genDataSql(report, rptColTable, query, cdnTables);
			System.out.println("report data=" + strSql);

			int pagecount = StringB.toInt(report.pagecount, 0);
			if (pagecount > 0) {
				int pagenum = StringB.toInt(query.nowpage, 1);
				WebDBUtil db = new WebDBUtil();
				svces = db.execQryArray(strSql, "", pagenum, pagecount);
			} else {
				svces = WebDBUtil.execQryArray(strSql, "");
			}
			if ("3".equals(report.rpt_type.substring(0, 1)) && svces != null) {
				svces = getNewData(svces, report, rptColTable);
			}
		}
		return svces;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ailk.bi.report.dao.ILReportDataDao#getReportData(java.lang.Object,
	 * java.util.List, java.lang.Object,
	 * com.ailk.bi.common.dbtools.WebDBUtil)
	 */
	public String[][] getReportData(Object rptTable, List rptColTable, Object qryStruct,
			WebDBUtil db, PubInfoConditionTable[] cdnTables) throws AppException {
		if (rptTable == null || rptColTable == null || rptColTable.size() <= 0)
			throw new AppException();

		RptResourceTable report = (RptResourceTable) rptTable;
		ReportQryStruct query = (ReportQryStruct) qryStruct;
		String[][] svces = null;
		if (ReportConsts.YES.equals(query.visible_data) && ReportConsts.NO.equals(query.first_view)) {
			String strSql = genDataSql(report, rptColTable, query, cdnTables);
			System.out.println("report data=" + strSql);

			int pagecount = StringB.toInt(report.pagecount, 0);
			if (pagecount > 0) {
				int pagenum = StringB.toInt(query.nowpage, 0) + 1;
				svces = db.execQryArray(strSql, "", pagenum, pagecount);
			} else {
				svces = WebDBUtil.execQryArray(strSql, "");
			}
			if ("3".equals(report.rpt_type.substring(0, 1)) && svces != null) {
				svces = getNewData(svces, report, rptColTable);
			}
		}
		return svces;
	}

	// 得到指标维度的数组
	public String[][] getNewData(String[][] data, RptResourceTable report, List rptColTable) {
		// int len = Integer.parseInt(report.rpt_export_rule); //显示几个指标维度
		String[][] newData = null;
		List newList = new ArrayList();
		int cols = data[0].length;
		int index = 0; // 定位指标维度在数组中的序号

		for (int i = 0; data != null && i < cols; i++) {
			if (!data[0][i].equals(data[1][i])) {
				index = i;
				break;
			}
		}
		// 得到指标维度的显示规则
		IMReportDataDao imDataDao = new MReportDataDao();
		String[][] rule = imDataDao.getMeasureHeadRule(report.rpt_id);
		String[] ruleArray = rule[0][0].split(",");
		int len = ruleArray.length;
		String[][] newRule = new String[len][2];
		int ruleLen = ruleArray.length;
		getShowMap(); // 初始化
		Iterator it = showMap.entrySet().iterator();
		for (int i = 0; i < ruleLen; i++) {
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if (ruleArray[i].equals(entry.getKey())) {
					newRule[i][0] = ruleArray[i];
					newRule[i][1] = entry.getValue().toString();
					showMap.remove(entry.getKey());
					it = showMap.entrySet().iterator();
					break;
				}
			}
		}

		List list = getArrayToList(data);
		String currValue = ""; // 当前值
		String lastValue = ""; // 上一个值
		String[][] sumData = new String[3][cols]; // 合计值
		String firstStr = ""; // 第一个DATA_ID值
		int j = 0;
		while (j <= list.size() - 3) {
			String[] curr = (String[]) list.get(j);
			String[][] newAry = null;
			currValue = "";
			if (j == 0)
				firstStr = curr[0];
			if (curr != null)
				currValue = curr[index - 2];
			if (!lastValue.equals(currValue)) {
				String[] last = (String[]) list.get(j + 1);
				String[] same = (String[]) list.get(j + 2);

				if (j < (list.size() - 3)) {
					sumData[0] = getSum(curr, sumData[0], index); // 合计本期
					sumData[1] = getSum(last, sumData[1], index); // 合计上期
					sumData[2] = getSum(same, sumData[2], index); // 合计同期

					newAry = getIndexArray(curr, last, same, index, newRule);
				} else {
					newAry = getIndexArray(sumData[0], sumData[1], sumData[2], index, newRule);
				}
				// 最后的合计行
				if (j == (list.size() - 3)) {
					int k = 0;
					for (k = 0; k < newAry.length; k++) {
						newAry[k][0] = firstStr;
					}
					for (k = 0; k < newAry.length; k++) {
						newAry[k][2] = "合计";
					}
				}
				for (int i = 0; i < newAry.length; i++) {
					newList.add(newAry[i]);
				}
				lastValue = currValue;
				j += 3;
			}
		}
		newData = getListToArray(newList, cols);

		return newData;
	}

	/**
	 * 生成查询数据SQL
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @param qryStruct
	 * @return
	 */
	public static String genDataSql(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct, PubInfoConditionTable[] cdnTables) {
		// 报表数据的主体构成SQL,A表示本期值，B表示同比日期值，C表示环比日期值
		String tableSql = "SELECT " + genBaseCode(rptTable, rptColTable, qryStruct) + " FROM ";
		tableSql += "(";
		tableSql += genNowDataSql(rptTable, rptColTable, qryStruct, cdnTables);
		tableSql += ") A ";
		// 是否有同比数据
		if (ReportObjUtil.hasSameData(rptTable, rptColTable, qryStruct)) {
			tableSql += " LEFT JOIN (";
			tableSql += genSameDataSql(rptTable, rptColTable, qryStruct, cdnTables);
			tableSql += ") B ";
			tableSql += genJoinCode("A", "B", rptTable, rptColTable);
		}
		// 是否有环比数据
		if (ReportObjUtil.hasLastData(rptTable, rptColTable, qryStruct)) {
			tableSql += " LEFT JOIN (";
			tableSql += genLastDataSql(rptTable, rptColTable, qryStruct, cdnTables);
			tableSql += ") C ";
			tableSql += genJoinCode("A", "C", rptTable, rptColTable);
		}
		tableSql += genRollupCode(rptTable, rptColTable);
		if (ReportObjUtil.hasSumRow(rptTable.rpt_type, rptColTable)) {
			tableSql += " OR (" + genHavingSumCode(rptTable, rptColTable) + ")";
		}
		// 获取生成小计的代码
		String[] subsumGrouping = genHavingSubSumCode(rptTable, rptColTable);
		for (int i = 0; subsumGrouping != null && i < subsumGrouping.length; i++) {
			tableSql += " OR (" + subsumGrouping[i] + ")";
		}
		tableSql += genOrderCode(rptTable, rptColTable);
		return tableSql;
	}

	/**
	 * 获取报表字典和纬度表定义的字段部分,包括同比环比默认定义,Sql语句中"基本值"部分的外层
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @param qryStruct
	 * @return
	 */
	private static String genBaseCode(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct) {
		// 是否为第一个纬度字段
		boolean isFirst = true;
		// 获取生成小计的代码
		String[] subsumGrouping = genHavingSubSumCode(rptTable, rptColTable);
		int subsumNum = 0;

		// 计算字段
		String[][] tmpArr = null;
		if (rptColTable != null) {
			tmpArr = new String[rptColTable.size()][2];
		}
		// 重新封装rptColTable，过滤不显示的列
		List rptCol = new ArrayList();
		for (int j = 0; rptColTable != null && j < rptColTable.size(); j++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(j);
			if (ReportConsts.YES.equals(dict.default_display)) {
				rptCol.add(dict);
			}
		}
		rptColTable = rptCol;
		colMap = StringTool.colDefMap(rptColTable);
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			String tmpStr = dict.field_code.trim();
			while (tmpStr.indexOf("+") >= 0 || tmpStr.indexOf("-") >= 0
					|| tmpStr.trim().indexOf("*") >= 0 || tmpStr.trim().indexOf("/") >= 0) {
				if (tmpStr.trim().indexOf("+") >= 0) {
					tmpStr = tmpStr.substring(0, tmpStr.indexOf("+"));
				}
				if (tmpStr.trim().indexOf("-") >= 0) {
					tmpStr = tmpStr.substring(0, tmpStr.indexOf("-"));
				}
				if (tmpStr.trim().indexOf("*") >= 0) {
					tmpStr = tmpStr.substring(0, tmpStr.indexOf("*"));
				}
				if (tmpStr.trim().indexOf("/") >= 0) {
					tmpStr = tmpStr.substring(0, tmpStr.indexOf("/"));
				}
			}
			tmpArr[i][0] = StringB.replace(tmpStr, "DATA_NUM", "");
			tmpArr[i][1] = dict.col_sequence.trim();
		}

		// 定义字符串
		String ret = "";
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			ret += "CASE WHEN (" + genHavingSumCode(rptTable, rptColTable) + ") THEN "
					+ ReportConsts.RPT_SUMROW_ID + "";
			for (int i = 0; subsumGrouping != null && i < subsumGrouping.length; i++) {
				ret += " WHEN (" + subsumGrouping[i] + ") THEN " + ReportConsts.RPT_SUBSUMROW_ID
						+ "";
			}
			ret += " ELSE A." + rptTable.data_sequence_code;
			ret += " END AS " + rptTable.data_sequence_code;
		}
		// 日期字段
		if (rptTable.displaydate) {
			if (ret.length() > 0) {
				ret += ",";
			}
			ret += "A." + rptTable.data_date;
		}
		for (int i = 0; rptColTable != null && i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			if (!ReportConsts.YES.equals(dict.default_display)) {
				continue;
			}
			RptColDictTable tmpdict = null;
			if (i > 0) {
				tmpdict = (RptColDictTable) rptColTable.get(i - 1);
			}

			String colNum = dict.col_sequence;// 计算数值型字段序号
			if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				if (ret.length() > 0) {
					ret += ",";
				}
				if (!StringTool.checkEmptyString(dict.field_dim_code)) {
					ret += "A." + dict.field_dim_code + ",";
				}
				if (isFirst) {
					ret += "CASE WHEN (" + genHavingSumCode(rptTable, rptColTable) + ") THEN "
							+ ReportConsts.RPT_SUMROW_DESC + " ELSE ";
					ret += "A." + dict.field_code + " END";
					ret += " AS " + dict.field_code;
					isFirst = false;
				} else if (tmpdict != null && ReportConsts.YES.equals(tmpdict.is_subsum)
						&& subsumGrouping != null && subsumNum < subsumGrouping.length) {
					ret += "CASE WHEN (" + subsumGrouping[subsumNum] + ") THEN "
							+ ReportConsts.RPT_SUBSUMROW_DESC + " ELSE ";
					ret += "A." + dict.field_code + " END";
					ret += " AS " + dict.field_code;
					subsumNum++;
				} else {
					ret += "A." + dict.field_code;
				}
			}
			if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
				if (ret.length() > 0) {
					ret += ",";
				}
				if (!ReportConsts.RPT_DEFAUTL_DATATABLE.equals(rptTable.data_table)) {// 非主体数据表
					ret += "SUM(A.MSU" + colNum + ") AS MSU" + colNum;
				} else {
					// ret += "SUM(A.MSU" + colNum + ") AS MSU" + colNum;
					String tmp = dict.field_code.toUpperCase().trim();

					if (ReportConsts.hasSpcCalculator(tmp)) {
						// 优先级高于gourp
						ret += tmp;
						if (tmp.indexOf(" AS ") < 0) {
							ret += " AS MSU" + colNum;
						}
					} else {
						tmp = StringB.replace(tmp, "DATA_NUM", "MSU");
						//
						/*
						 * if(tmp.trim().indexOf("+")>=0 ||
						 * tmp.trim().indexOf("-")>=0
						 * ||tmp.trim().indexOf("*")>=0
						 * ||tmp.trim().indexOf("/")>=0){
						 *
						 * if(tmp.trim().indexOf("+")>=0){ tmp =
						 * tmp.substring(0, tmp.indexOf("+")); }
						 * if(tmp.trim().indexOf("-")>=0){ tmp =
						 * tmp.substring(0, tmp.indexOf("-")); }
						 * if(tmp.trim().indexOf("*")>=0){ tmp =
						 * tmp.substring(0, tmp.indexOf("*")); }
						 * if(tmp.trim().indexOf("/")>=0){ tmp =
						 * tmp.substring(0, tmp.indexOf("/")); }
						 *
						 * }
						 */
						//
						String[] mathArr = tmp.split("#");
						for (int m = 0; mathArr != null && m < mathArr.length; m++) {
							if (!mathArr[m].trim().equalsIgnoreCase("+")
									&& !mathArr[m].trim().equalsIgnoreCase("-")
									&& !mathArr[m].trim().equalsIgnoreCase("*")
									&& !mathArr[m].trim().equalsIgnoreCase("/")) {
								ret += "COALESCE(";
							}

							ret += StringTool.parseCmdNew(mathArr[m], "MSU", 3, tmpArr, colMap);
							if (!mathArr[m].trim().equalsIgnoreCase("+")
									&& !mathArr[m].trim().equalsIgnoreCase("-")
									&& !mathArr[m].trim().equalsIgnoreCase("*")
									&& !mathArr[m].trim().equalsIgnoreCase("/")) {
								ret += ",0)";
							}
						}
						ret += " AS MSU" + colNum;
					}
				}
				if (ReportObjUtil.hasSameData(rptTable, rptColTable, qryStruct)) {
					// 如果有同比数据
					ret += ",SUM(B.MSU" + colNum + ") AS SAME_MSU" + colNum;
					ret += ",(COALESCE(SUM(A.MSU" + colNum + "),0)-COALESCE(SUM(B.MSU" + colNum
							+ "),0))/(CASE WHEN SUM(B.MSU" + colNum
							+ ")=0 THEN NULL ELSE SUM(B.MSU" + colNum + ") END) AS SAME_PER"
							+ colNum;
				}
				if (ReportObjUtil.hasLastData(rptTable, rptColTable, qryStruct)) {
					// 如果有环比数据
					ret += ",SUM(C.MSU" + colNum + ") AS LAST_MSU" + colNum;
					ret += ",(COALESCE(SUM(A.MSU" + colNum + "),0)-COALESCE(SUM(C.MSU" + colNum
							+ "),0))/(CASE WHEN SUM(C.MSU" + colNum
							+ ")=0 THEN NULL ELSE SUM(C.MSU" + colNum + ") END) AS LAST_PER"
							+ colNum;
				}
			}
		}
		return ret;
	}

	/**
	 * 获取报表字典和纬度表定义的基本字段，通用于本期，同比，环比日期查询语句
	 *
	 * @param rptTable
	 * @param dicts
	 * @param date_code
	 * @return
	 */
	private static String genQryCode(RptResourceTable rptTable, List rptColTable, String date_code,
			ReportQryStruct query, PubInfoConditionTable[] cdnTables) {
		String ret = "";
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			ret += rptTable.data_sequence_code;
		}
		// 是否有日期字段
		if (ReportObjUtil.hasQryDateCode(rptTable, rptColTable)) {
			if (ret.length() > 0) {
				ret += ",";
			}
			ret += date_code;
		}
		Iterator iter = rptColTable.iterator();
		boolean sameFlag = ReportObjUtil.hasSameData(rptTable, rptColTable, null);
		boolean lastFlag = ReportObjUtil.hasLastData(rptTable, rptColTable, null);
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			// if (ReportConsts.YES.equals(dict.default_display)) {
			String colNum = dict.col_sequence;// 计算数值型字段序号
			if (ReportConsts.YES.equals(dict.default_display)
					&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				boolean isExpand = false;
				if (ret.length() > 0) {
					ret += ",";
				}
				if (!StringTool.checkEmptyString(dict.field_dim_code)) {
					isExpand = isExpandRow(query, cdnTables, dict.field_dim_code);
					if (isExpand) {
						ret += "'1' as " + dict.field_dim_code + ",";
					} else {
						ret += dict.field_dim_code + ",";
					}
				}
				if (!isExpand) {
					isExpand = isExpandRow(query, cdnTables, dict.field_code);
				}
				if (isExpand) {
					ret += "'--' as " + dict.field_code;
				} else {
					ret += dict.field_code;
				}
			}

			if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {

				String tmp = dict.field_code.toUpperCase().trim();
				if (!ReportConsts.RPT_DEFAUTL_DATATABLE.equals(rptTable.data_table)
						|| ((sameFlag || lastFlag) && (tmp.indexOf("+") >= 0
								|| tmp.indexOf("-") >= 0 || tmp.indexOf("*") >= 0 || tmp
								.indexOf("/") >= 0)) || ReportConsts.hasSpcCalculator(tmp)) {// 非主体数据表或者有同比环比和表达式的时候

					if (ret.length() > 0) {
						ret += ",";
					}

					// System.out.println("tmp=========="+tmp);
					if (ReportConsts.hasSpcCalculator(tmp)) {
						// 优先级高于gourp
						ret += tmp;
						if (tmp.indexOf(" AS ") < 0) {
							ret += " AS MSU" + colNum;
						}
					} else {
						String[] mathArr = tmp.split("#");
						for (int m = 0; mathArr != null && m < mathArr.length; m++) {
							if (!mathArr[m].trim().equalsIgnoreCase("+")
									&& !mathArr[m].trim().equalsIgnoreCase("-")
									&& !mathArr[m].trim().equalsIgnoreCase("*")
									&& !mathArr[m].trim().equalsIgnoreCase("/")) {
								ret += "COALESCE(";
							}
							ret += StringTool.parseCmd(mathArr[m], "DATA_NUM", 8,
									Integer.parseInt(colNum));
							if (!mathArr[m].trim().equalsIgnoreCase("+")
									&& !mathArr[m].trim().equalsIgnoreCase("-")
									&& !mathArr[m].trim().equalsIgnoreCase("*")
									&& !mathArr[m].trim().equalsIgnoreCase("/")) {
								ret += ",0)";
							}
						}
						// ret += StringTool.parseCmd(tmp, "DATA_NUM", 8,
						// Integer
						// .parseInt(colNum));
						ret += " AS MSU" + colNum;
					}
				}
			}
		}
		if (ReportConsts.RPT_DEFAUTL_DATATABLE.equals(rptTable.data_table)) {// 主体数据表
			colMap = StringTool.colDefMap(rptColTable);
			Iterator it = colMap.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();

				ret += ",COALESCE(SUM(DATA_NUM" + entry.getKey() + "),0) as MSU" + entry.getValue();
			}
		}
		return ret;
	}

	/**
	 * 获取本期值
	 *
	 * @param rptTable
	 * @param dicts
	 * @param qryStruct
	 * @return
	 */
	private static String genNowDataSql(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct, PubInfoConditionTable[] cdnTables) {
		// 数据表名
		String data_table = rptTable.data_table;
		// 日期字段名
		String date_code = rptTable.data_date;

		// 开始拼写sql
		String sql = "SELECT "
				+ genQryCode(rptTable, rptColTable, rptTable.data_date, qryStruct, cdnTables)
				+ " FROM " + data_table;
		// 添加基本条件
		sql += " " + rptTable.data_where_sql;
		// 日期条件
		if (ReportConsts.THIRD.equals(qryStruct.divcity_flag)) {
			// 月数据按年报设定条件显示
			if (SysConsts.STAT_PERIOD_PART.equals(rptTable.start_date)) {
				sql += " AND " + data_table + "." + date_code;
				sql += ">=" + qryStruct.date_s + "01";
				sql += " AND " + data_table + "." + date_code;
				sql += "<=" + qryStruct.date_e + "12";
			} else {
				sql += " AND " + data_table + "." + date_code;
				sql += " LIKE '" + qryStruct.date_s + "%'";
			}
		} else if (ReportConsts.SECOND.equals(qryStruct.divcity_flag)) {
			// 日数据按月报设定条件显示
			if (SysConsts.STAT_PERIOD_PART.equals(rptTable.start_date)) {
				sql += " AND " + data_table + "." + date_code;
				sql += ">=" + qryStruct.date_s + "01";
				sql += " AND " + data_table + "." + date_code;
				sql += "<=" + qryStruct.date_e + "31";
			} else {
				sql += " AND " + data_table + "." + date_code;
				sql += " LIKE '" + qryStruct.date_s + "%'";
			}
		} else {
			if (SysConsts.STAT_PERIOD_PART.equals(rptTable.start_date)) {
				sql += " AND " + data_table + "." + date_code;
				sql += ">=" + qryStruct.date_s;
				sql += " AND " + data_table + "." + date_code;
				sql += "<=" + qryStruct.date_e;
			} else {
				sql += " AND " + data_table + "." + date_code;
				sql += "=" + qryStruct.date_s;
			}
		}
		// 分组条件
		sql += genGroupCode(rptTable, rptColTable, data_table, date_code);
		return sql;
	}

	/**
	 * 获取同比日期值
	 *
	 * @param rptTable
	 * @param dicts
	 * @param qryStruct
	 * @return
	 */
	private static String genSameDataSql(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct, PubInfoConditionTable[] cdnTables) {
		// 日期维表
		DateTable dateStuct = getDateStruct(rptTable.cycle);

		// 开始拼写sql
		String sql = "SELECT "
				+ genQryCode(rptTable, rptColTable, dateStuct.gather_date, qryStruct, cdnTables)
				+ " FROM " + rptTable.data_table + "," + dateStuct.d_table;
		// 添加基本条件
		sql += " " + rptTable.data_where_sql;
		// 日期条件
		sql += " AND " + rptTable.data_table + "." + rptTable.data_date;
		sql += "=" + dateStuct.d_table + "." + dateStuct.same_gather_date;
		if (ReportConsts.SECOND.equals(qryStruct.divcity_flag)) {
			// 日数据按月报设定条件显示
			sql += " AND " + dateStuct.d_table + "." + dateStuct.gather_date;
			sql += " LIKE '" + qryStruct.date_s + "%'";
		} else {
			sql += " AND " + dateStuct.d_table + "." + dateStuct.gather_date;
			sql += "=" + qryStruct.date_s;
		}
		// 分组条件
		sql += genGroupCode(rptTable, rptColTable, dateStuct.d_table, dateStuct.gather_date);
		return sql;
	}

	/**
	 * 获取环比日期值
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @param qryStruct
	 * @return
	 */
	private static String genLastDataSql(RptResourceTable rptTable, List rptColTable,
			ReportQryStruct qryStruct, PubInfoConditionTable[] cdnTables) {
		// 日期维表
		DateTable dateStuct = getDateStruct(rptTable.cycle);

		// 开始拼写sql
		String sql = "SELECT "
				+ genQryCode(rptTable, rptColTable, dateStuct.gather_date, qryStruct, cdnTables)
				+ " FROM " + rptTable.data_table + "," + dateStuct.d_table;
		// 添加基本条件
		sql += " " + rptTable.data_where_sql;
		// 日期条件
		sql += " AND " + rptTable.data_table + "." + rptTable.data_date;
		sql += "=" + dateStuct.d_table + "." + dateStuct.last_gather_date;
		if (ReportConsts.SECOND.equals(qryStruct.divcity_flag)) {
			// 日数据按月报设定条件显示
			sql += " AND " + dateStuct.d_table + "." + dateStuct.gather_date;
			sql += " LIKE '" + qryStruct.date_s + "%'";
		} else {
			sql += " AND " + dateStuct.d_table + "." + dateStuct.gather_date;
			sql += "=" + qryStruct.date_s;
		}
		// 分组条件
		sql += genGroupCode(rptTable, rptColTable, dateStuct.d_table, dateStuct.gather_date);
		return sql;
	}

	/**
	 * 获取分组的基本sql语句
	 *
	 * @param rptTable
	 * @param dicts
	 * @param date_table
	 * @param date
	 * @return
	 */
	private static String genGroupCode(RptResourceTable rptTable, List rptColTable,
			String date_table, String date) {
		String ret = "";
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			ret += rptTable.data_sequence_code;
		}
		if (ReportObjUtil.hasQryDateCode(rptTable, rptColTable)) {
			if (ret.length() > 0) {
				ret += ",";
			}
			ret += date_table + "." + date;
		}
		Iterator iter = rptColTable.iterator();
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			if (ReportConsts.YES.equals(dict.default_display)
					&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				if (ret.length() > 0) {
					ret += ",";
				}
				if (!StringTool.checkEmptyString(dict.field_dim_code)) {
					ret += dict.field_dim_code + ",";
				}
				ret += dict.field_code;
			}
		}
		if (!"".equals(ret) && ret.length() > 0) {
			ret = " GROUP BY " + ret;
		}
		return ret;
	}

	/**
	 * 获取外联结条件
	 *
	 * @param table0
	 * @param table1
	 * @param rptTable
	 * @param dicts
	 * @return
	 */
	private static String genJoinCode(String table0, String table1, RptResourceTable rptTable,
			List rptColTable) {
		String date0 = rptTable.data_date;
		// 日期维表
		DateTable dateStuct = getDateStruct(rptTable.cycle);
		// 日期维表字段定义
		String date1 = dateStuct.gather_date;
		String ret = " ON " + table0 + "." + date0 + "=" + table1 + "." + date1;

		Iterator iter = rptColTable.iterator();
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			if (ReportConsts.YES.equals(dict.default_display)
					&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				if (!StringTool.checkEmptyString(dict.field_dim_code)) {
					ret += " AND " + table0 + "." + dict.field_dim_code;
					ret += "=" + table1 + "." + dict.field_dim_code;
				}
				ret += " AND " + table0 + "." + dict.field_code;
				ret += "=" + table1 + "." + dict.field_code;
			}
		}
		return ret;
	}

	/**
	 * 获取rollup代码
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @return
	 */
	private static String genRollupCode(RptResourceTable rptTable, List rptColTable) {
		String ret = "";
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			ret += "A." + rptTable.data_sequence_code;
		}
		String other = "";
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			other += "GROUPING(A." + rptTable.data_sequence_code + ")=0";
		}
		// 日期字段
		if (rptTable.displaydate) {
			if (ret.length() > 0) {
				ret += ",";
			}
			ret += rptTable.data_date;
			if (other.length() > 0) {
				other += " AND";
			}
			other += " GROUPING(A." + rptTable.data_date + ")=0";
		}

		Iterator iter = rptColTable.iterator();
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			if (ReportConsts.YES.equals(dict.default_display)
					&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				if (ret.length() > 0) {
					ret += ",";
				}
				if (other.length() > 0) {
					other += " AND ";
				}
				if (!StringTool.checkEmptyString(dict.field_dim_code)) {
					ret += "A." + dict.field_dim_code + ",";
					other += "GROUPING(A." + dict.field_dim_code + ")=0 AND ";
				}
				ret += "A." + dict.field_code;
				other += "GROUPING(A." + dict.field_code + ")=0";
			}
		}
		if (!"".equals(ret)) {
			ret = " GROUP BY ROLLUP(" + ret + ")";
		}
		if (!"".equals(other)) {
			ret += " HAVING (" + other + ")";
		}
		return ret;
	}

	/**
	 * 获取对应rollup的having代码
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @return
	 */
	private static String genHavingSumCode(RptResourceTable rptTable, List rptColTable) {
		String ret = "";
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			ret += "GROUPING(A." + rptTable.data_sequence_code + ")=1";
		}
		// 日期字段
		if (rptTable.displaydate) {
			if (ret.length() > 0) {
				ret += " AND ";
			}
			ret += " GROUPING(A." + rptTable.data_date + ")=1";
		}

		Iterator iter = rptColTable.iterator();
		while (iter.hasNext()) {
			RptColDictTable dict = (RptColDictTable) iter.next();
			if (ReportConsts.YES.equals(dict.default_display)
					&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
				if (ret.length() > 0) {
					ret += " AND ";
				}
				if (!StringTool.checkEmptyString(dict.field_dim_code)) {
					ret += " GROUPING(A." + dict.field_dim_code + ")=1 AND ";
				}
				ret += " GROUPING(A." + dict.field_code + ")=1";
			}
		}
		return ret;
	}

	/**
	 * 获取对应rollup的having代码
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @return
	 */
	private static String[] genHavingSubSumCode(RptResourceTable rptTable, List rptColTable) {
		List sumcol = new ArrayList();
		for (int i = 0; i < rptColTable.size(); i++) {
			RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
			if (i + 1 == rptColTable.size()) {
				break;
			}
			if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
				break;
			}
			RptColDictTable tmpdict = (RptColDictTable) rptColTable.get(i + 1);
			if (ReportConsts.DATA_TYPE_NUMBER.equals(tmpdict.data_type)) {
				break;
			}

			if (ReportConsts.YES.equals(dict.is_subsum)) {
				sumcol.add(dict.col_sequence);
			}
		}
		if (sumcol == null || sumcol.size() == 0) {
			return null;
		}

		String ret = "";
		if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
			ret += "GROUPING(A." + rptTable.data_sequence_code + ")=0";
		}
		// 日期字段
		if (rptTable.displaydate) {
			if (ret.length() > 0) {
				ret += " AND";
			}
			ret += " GROUPING(A." + rptTable.data_date + ")=0";
		}
		List code = new ArrayList();
		Iterator itercode = sumcol.iterator();
		while (itercode.hasNext()) {
			String scol = (String) itercode.next();
			int col = Integer.parseInt(scol);
			String tmpret = "";
			for (int i = 0; i < rptColTable.size(); i++) {
				RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
				if (ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
					break;
				}
				int col_sequence = Integer.parseInt(dict.col_sequence);
				if (ReportConsts.YES.equals(dict.default_display)
						&& ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
					if ("".equals(tmpret)) {
						tmpret = ret;
					}
					if (col_sequence <= col) {
						if (ret.length() > 0) {
							tmpret += " AND ";
						}
						if (!StringTool.checkEmptyString(dict.field_dim_code)) {
							tmpret += " GROUPING(A." + dict.field_dim_code + ")=0 AND ";
						}
						tmpret += " GROUPING(A." + dict.field_code + ")=0";
					} else {
						if (ret.length() > 0) {
							tmpret += " AND ";
						}
						if (!StringTool.checkEmptyString(dict.field_dim_code)) {
							tmpret += " GROUPING(A." + dict.field_dim_code + ")=1 AND ";
						}
						tmpret += " GROUPING(A." + dict.field_code + ")=1";
					}
				}
			}
			code.add(tmpret);
		}

		String[] result = null;
		if (code != null && code.size() > 0) {
			result = (String[]) code.toArray(new String[code.size()]);
		}
		return result;
	}

	/**
	 * 排序语句
	 *
	 * @param rptTable
	 * @param rptColTable
	 * @return
	 */
	private static String genOrderCode(RptResourceTable rptTable, List rptColTable) {
		// 基本排序语句
		String order = rptTable.data_order.toUpperCase();
		if (order == null) {
			order = "";
		}
		// 有小计或者需要左侧合并的报表
		if (ReportObjUtil.hasSubSumRow(rptColTable) || ReportConsts.YES.equals(rptTable.isleft)) {
			// 所有需要排序的字段名
			String allCode = "";
			// 描述字段名
			String charCode = "";

			for (int i = 0; i < rptColTable.size(); i++) {
				RptColDictTable dict = (RptColDictTable) rptColTable.get(i);
				if (ReportConsts.NO.equals(dict.default_display)) {
					continue;
				}
				if (i + 1 == rptColTable.size()
						|| ReportConsts.DATA_TYPE_NUMBER.equals(dict.data_type)) {
					break;
				}
				RptColDictTable nextdict = (RptColDictTable) rptColTable.get(i + 1);
				if (ReportConsts.DATA_TYPE_NUMBER.equals(nextdict.data_type)) {
					break;
				}

				if (ReportConsts.DATA_TYPE_STRING.equals(dict.data_type)) {
					if (charCode.length() > 0) {
						charCode += ",";
					}
					if (!StringTool.checkEmptyString(dict.field_dim_code)) {
						charCode += dict.field_dim_code + ",";
					}
					charCode += dict.field_code;

					if (ReportConsts.YES.equals(dict.is_subsum)) {
						if (allCode.length() > 0) {
							allCode += ",";
						}
						if (!StringTool.checkEmptyString(dict.field_dim_code)) {
							allCode += dict.field_dim_code + ",";
						}
						allCode += dict.field_code;
					}
				}
			}
			// 如果为空即按描述字段排序
			if ("".equals(allCode)) {
				allCode = charCode;
			}
			if (rptTable.displaydate) {
				if (allCode.length() > 0) {
					allCode += ",";
				}
				allCode += rptTable.data_date;
				if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
					allCode += "," + rptTable.data_sequence_code;
				}
			} else {
				if (!StringTool.checkEmptyString(rptTable.data_sequence_code)) {
					if (allCode.length() > 0) {
						allCode += ",";
					}
					allCode += rptTable.data_sequence_code;
				}
			}

			if (order.indexOf("ORDER BY") >= 0) {
				order = allCode + StringB.replace(order, "ORDER BY", ",");
			} else {
				order = allCode;
			}
			if (order.length() > 0) {
				order = " ORDER BY " + order;
			}
		} else if (!"".equals(order)) {
			if (order.indexOf("ORDER BY ") >= 0) {
				order = " " + order;
			} else {
				order = " ORDER BY " + order;
			}
		}
		return " " + order;
	}

	/**
	 * 获取日期维表字段定义
	 *
	 * @param cycle
	 * @return 0-表名；1-日期字段名；2-同比日期字段名；3-环比日期字段名
	 */
	private static DateTable getDateStruct(String cycle) {
		DateTable date = new DateTable();
		if (SysConsts.STAT_PERIOD_MONTH.equals(cycle)) {
			date.d_table = "D_MONTH";
			date.gather_date = "GATHER_MON";
			date.same_gather_date = "SAME_GATHER_MON";
			date.last_gather_date = "LAST_GATHER_MON";
		}
		if (SysConsts.STAT_PERIOD_DAY.equals(cycle)) {
			date.d_table = "D_DATE";
			date.gather_date = "GATHER_DAY";
			date.same_gather_date = "SAME_GATHER_DAY";
			date.last_gather_date = "LAST_GATHER_DAY";
		}
		return date;
	}

	/**
	 * 是否展开合并列
	 *
	 * @param qryStruct
	 * @param conTable
	 * @param code
	 * @return
	 */
	private static boolean isExpandRow(ReportQryStruct qryStruct, PubInfoConditionTable[] conTable,
			String code) {
		boolean isTrue = false;
		for (int i = 0; conTable != null && i < conTable.length; i++) {
			String tmpCode = conTable[i].qry_code.toLowerCase();
			String con_code = conTable[i].con_code.toUpperCase();
			String tmpValue = "";
			// 字段不符进入下一条
			if (!con_code.equals(code.toUpperCase())) {
				continue;
			}
			if ("0".equals(conTable[i].qry_type)) {
				tmpValue = ReflectUtil.getStringFromObj(qryStruct, tmpCode);
				if (ReportConsts.RPT_EXPAND_VALUE.equals(tmpValue)) {
					isTrue = true;
				}
			}
		}
		return isTrue;
	}

	// 数组转换为List
	public List getArrayToList(String[][] data) {
		List list = new ArrayList();
		for (int i = 0; i < data.length; i++) {
			list.add(data[i]);
		}
		String[] Str = new String[data[0].length];
		list.add(Str);
		list.add(Str);
		list.add(Str);

		return list;
	}

	// 求合值
	public String[] getSum(String[] value, String[] sumValue, int idx) {

		for (int i = (value.length - 1); i > (idx + 1); i--) {
			if (sumValue[i] == null) {
				sumValue[i] = Arith.add(value[i], "0");
			} else {
				sumValue[i] = Arith.add(value[i], sumValue[i]);
			}

		}
		return sumValue;
	}

	// 求合计的环比或同比
	public String[] getRtio(String[] currSum, String[] compareSum, String[] rtioSum, int idx) {

		for (int i = (currSum.length - 1); i > (idx + 1); i--) {
			if (Double.parseDouble(compareSum[i]) != 0) {
				rtioSum[i] = Arith.divPer(Arith.sub(currSum[i], compareSum[i]), compareSum[i], 2);
			} else {
				rtioSum[i] = "0";
			}
		}
		return rtioSum;
	}

	// List转换为数组
	public String[][] getListToArray(List list, int cols) {
		String[][] array = new String[list.size()][cols];
		for (int i = 0; i < list.size(); i++) {
			array[i] = (String[]) list.get(i);
			for (int j = 0; j < cols; j++) {
				if (array[i][j] == null) {
					array[i][j] = "";
				}
			}
		}
		return array;
	}

	public String[][] getIndexArray(String[] currData, String[] lastData, String[] sameData,
			int idx, String[][] rules) {
		String[][] addArray = new String[rules.length][currData.length];
		int intValue = 0;
		for (int i = 0; i < rules.length; i++) {
			for (int c = 0; c < currData.length; c++) {
				if (c < idx) {
					addArray[i][c] = currData[c];
				} else if (c == idx) {
					addArray[i][c] = String.valueOf(intValue);
				} else if (c == (idx + 1)) {
					addArray[i][c] = rules[i][1];
				} else {
					if ("1".equals(rules[i][0])) {
						addArray[i][c] = currData[c];
					} else if ("2".equals(rules[i][0])) {
						addArray[i][c] = lastData[c];
					} else if ("3".equals(rules[i][0])) {
						addArray[i][c] = Arith.sub(currData[c], lastData[c]);
					} else if ("4".equals(rules[i][0])) {
						addArray[i][c] = Arith.divPer(Arith.sub(currData[c], lastData[c]),
								lastData[c], 2);
					} else if ("5".equals(rules[i][0])) {
						addArray[i][c] = sameData[c];
					} else if ("6".equals(rules[i][0])) {
						addArray[i][c] = Arith.divPer(Arith.sub(currData[c], sameData[c]),
								sameData[c], 2);
					}
				}
			}
		}
		return addArray;
	}

	// 制定指标维度
	public static void getShowMap() {
		showMap = new HashMap();
		showMap.put("1", "本期");
		showMap.put("2", "上期");
		showMap.put("3", "增量");
		showMap.put("4", "环比");
		showMap.put("5", "同期");
		showMap.put("6", "同比");
	}
}
