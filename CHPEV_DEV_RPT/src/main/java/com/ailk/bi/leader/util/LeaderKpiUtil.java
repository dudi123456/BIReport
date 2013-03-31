package com.ailk.bi.leader.util;

import com.ailk.bi.SysConsts;
import com.ailk.bi.base.struct.LeaderQryStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.NullProcFactory;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.leader.struct.LeaderKpiInfoStruct;

public class LeaderKpiUtil {

	/**
	 * 查询客户分群数据源表
	 *
	 * @param group_type
	 * @param data_cycle
	 * @return
	 */
	public static String getCustGroupDataSource(String group_type, String data_cycle) {
		String datasource = "";
		try {
			String sql = SQLGenator.genSQL("AIQ1010", group_type, data_cycle);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				datasource = result[0][0];
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return datasource;
	}

	/**
	 * 查询领导首页可以查看的KPI数据信息
	 *
	 * @param qryStruct
	 * @return
	 */
	public static LeaderKpiInfoStruct[] getKpiInfoStruct(LeaderQryStruct qryStruct,
			UserCtlRegionStruct regionStruct) {
		LeaderKpiInfoStruct[] infoStruct = null;
		String whereStr = "";
		String whereStr1 = "";
		// 日期条件
		//if (SysConsts.STAT_PERIOD_DAY.equals(qryStruct.data_cycle)) {
			if (!StringTool.checkEmptyString(qryStruct.gather_day)) {
				whereStr += " AND A.DAY_ID = " + qryStruct.gather_day;
			}
		//} else if (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle)) {
			if (!StringTool.checkEmptyString(qryStruct.gather_mon)) {
				whereStr1 += " AND A.MONTH_ID = " + qryStruct.gather_mon;
			}
		//}
		// 客户群条件
		if (!StringTool.checkEmptyString(qryStruct.cust_group_id)) {
			whereStr += " AND A.CUST_GROUP_ID = '" + qryStruct.cust_group_id + "'";
			whereStr1 += " AND A.CUST_GROUP_ID = '" + qryStruct.cust_group_id + "'";
		}
		// 地域条件
		if (!StringTool.checkEmptyString(qryStruct.city_id)) {// 全省
			whereStr += " AND A.CITY_ID = '" + qryStruct.city_id + "'";
			whereStr1 += " AND A.CITY_ID = '" + qryStruct.city_id + "'";
		} else if (!SysConsts.RIGHT_LVL_METRO.equals(regionStruct.ctl_lvl)) {// 不是全省
			if ("".equals(qryStruct.city_id) && !"".equals(regionStruct.ctl_city_str)) {
				whereStr += " AND A.CITY_ID " + regionStruct.ctl_in_or_equals
						+ regionStruct.ctl_city_str_add;
				whereStr1 += " AND A.CITY_ID " + regionStruct.ctl_in_or_equals
						+ regionStruct.ctl_city_str_add;
			}
		}

		try {
			String sql = SQLGenator.genSQL("AIQ1001", qryStruct.group_type,
					whereStr, whereStr1);
			System.out.println("AIQ1001=====KPI分析============" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				infoStruct = new LeaderKpiInfoStruct[result.length];
				for (int i = 0; i < result.length; i++) {
					infoStruct[i] = new LeaderKpiInfoStruct();
					infoStruct[i].msu_id = result[i][0];
					infoStruct[i].msu_name = result[i][1];
					infoStruct[i].unit = result[i][2];
					infoStruct[i].digit = Integer.parseInt(result[i][3]);
					infoStruct[i].cur_value = FormatUtil.formatStr(result[i][4],
							infoStruct[i].digit, true);// 本期值
					infoStruct[i].last_value = FormatUtil.formatStr(result[i][5],
							infoStruct[i].digit, true);// 上期值
					infoStruct[i].same_value = FormatUtil.formatStr(result[i][6],
							infoStruct[i].digit, true);// 同期值
					infoStruct[i].agg_value = FormatUtil.formatStr(result[i][7],
							infoStruct[i].digit, true);// 累计值
					infoStruct[i].last_agg_value = FormatUtil.formatStr(result[i][8],
							infoStruct[i].digit, true);// 上期累计值
					infoStruct[i].same_agg_value = FormatUtil.formatStr(result[i][9],
							infoStruct[i].digit, true);// 同期累计值
					infoStruct[i].last_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][4], result[i][5]), result[i][5], 2),
							"100%");// 环比
					infoStruct[i].same_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][4], result[i][6]), result[i][6], 2),
							"100%");// 同比
					infoStruct[i].agg_last_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][7], result[i][8]), result[i][8], 2),
							"100%");// 累计环比
					infoStruct[i].agg_same_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][7], result[i][9]), result[i][9], 2),
							"100%");// 累计同比
					infoStruct[i].parent_id = result[i][10];
					infoStruct[i].msu_cycle = result[i][11];
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return infoStruct;
	}

	/**
	 * ajaxkpi表格输出
	 *
	 * @param qryStruct
	 * @param regionStruct
	 * @return
	 */
	public static String findKpiInfoStructByCityId(LeaderQryStruct qryStruct,
			UserCtlRegionStruct regionStruct, String reqPath) {
		StringBuffer strHtml = new StringBuffer();
		strHtml.append(findKpiInfoStructForJsp(getKpiInfoStruct(qryStruct, regionStruct),
				qryStruct, reqPath));
		return strHtml.toString();
	}

	/**
	 * 页面的kpi表格输出
	 *
	 * @return
	 */
	public static String findKpiInfoStructForJsp(LeaderKpiInfoStruct[] kpiStruct,
			LeaderQryStruct qryStruct, String reqPath) {
		StringBuffer strHtml = new StringBuffer("<div class=\"widget_tb_head\"><table><tr>\n");
		if (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle)) {
			strHtml.append("<td width=\"40%\" align=\"center\">指标</td>\n")
					.append("<td width=\"15%\" align=\"center\">当月</td>\n")
					.append("<td width=\"15%\" align=\"center\">上月</td>\n")
					.append("<td width=\"15%\" align=\"center\">本年累计</td>\n")
					.append("<td width=\"15%\" align=\"center\">环比</td>\n");
		} else if (SysConsts.STAT_PERIOD_DAY.equals(qryStruct.data_cycle)) {
			strHtml.append("<td width=\"40%\" align=\"center\">指标</td>\n")
					.append("<td width=\"15%\" align=\"center\">当日</td>\n")
					.append("<td width=\"15%\" align=\"center\">本月累计</td>\n")
					.append("<td width=\"15%\" align=\"center\">上月累计</td>\n")
					.append("<td width=\"15%\" align=\"center\">累计环比</td>\n");
		}
		strHtml.append("</tr></table></div>\n");

		if (kpiStruct != null) {
			strHtml.append("<div class=\"widget_tb_content\">\n");
			strHtml.append("<table id=\"tablebox1\">\n");
			for (int i = 0; i < kpiStruct.length; i++) {
				// 上级节点
				if (kpiStruct[i].parent_id == null || kpiStruct[i].parent_id.trim().length() == 0) {
					String tr_css = "";
					if (i % 2 == 1) {
						tr_css = "class=\"jg\"";
					}
					// 显示该节点
					strHtml.append("<tr " + tr_css + ">\n");
					// 指标名称
					strHtml.append("<td width=\"40%\"><a class=\"icon1 tdopen\" href=\"javascript:;\" name=\"haschild\"></a>");
					strHtml.append("<a title=\"" + kpiStruct[i].msu_desc
							+ "\" onclick=\"HighLightSelRow();\""
							+ "href=\"javascript:ShowMsuInfo('" + kpiStruct[i].msu_id + "','"
							+ kpiStruct[i].msu_name + "(" + kpiStruct[i].unit + ")" + "');\">");
					strHtml.append(kpiStruct[i].msu_name + "(" + kpiStruct[i].unit + ")");
					strHtml.append("</a></td>\n");
					// 当期值
					strHtml.append("<td width=\"15%\" align=\"right\">" + kpiStruct[i].cur_value
							+ "</td>\n");
					// 本月累计
					strHtml.append("<td width=\"15%\" align=\"right\">"
							+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[i].last_value
									: kpiStruct[i].agg_value) + "</td>\n");
					// 上月累计
					strHtml.append("<td width=\"15%\" align=\"right\">"
							+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[i].agg_value
									: kpiStruct[i].last_agg_value) + "</td>\n");
					// 累计环比&环比波动
					if (Double.parseDouble(kpiStruct[i].agg_last_per.substring(0,
							kpiStruct[i].agg_last_per.length() - 1)) > 0) {
						strHtml.append("<td width=\"15%\" align=\"right\">"
								+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[i].last_per
										: kpiStruct[i].agg_last_per) + "<img src=\"" + reqPath
								+ "/images/common/leader/sign_qushi_up.gif\">" + "</td>\n");
					} else {
						strHtml.append("<td width=\"15%\" align=\"right\">"
								+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[i].last_per
										: kpiStruct[i].agg_last_per) + "<img src=\"" + reqPath
								+ "/images/common/leader/sign_qushi_down.gif\">" + "</td>\n");
					}
					strHtml.append("</tr>\n");
					// 在找到它的子节点显示
					strHtml.append("<tbody style=\"display:none;\">\n");
					for (int j = 0; j < kpiStruct.length; j++) {
						if (kpiStruct[j].parent_id != null
								&& kpiStruct[i].msu_id.equalsIgnoreCase(kpiStruct[j].parent_id)) {
							strHtml.append("<tr class=\"widget_tb_child\">\n");
							// 指标名称
							strHtml.append("<td width=\"40%\"><a class=\"icon1 tdchild\"></a>");
							strHtml.append("<a title=\"" + kpiStruct[i].msu_desc
									+ "\" href=\"javascript:ShowMsuInfo('" + kpiStruct[j].msu_id
									+ "','" + kpiStruct[j].msu_name + "(" + kpiStruct[i].unit + ")"
									+ "');\" onclick=\"HighLightSelRow();\">");
							strHtml.append(kpiStruct[j].msu_name + "(" + kpiStruct[i].unit + ")");
							strHtml.append("</a></td>\n");
							// 当期值
							strHtml.append("<td width=\"15%\" align=\"right\">"
									+ kpiStruct[j].cur_value + "</td>\n");
							// 本月累计
							strHtml.append("<td width=\"15%\" align=\"right\">"
									+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[j].last_value
											: kpiStruct[j].agg_value) + "</td>\n");
							// 上月累计
							strHtml.append("<td width=\"15%\" align=\"right\">"
									+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[j].agg_value
											: kpiStruct[j].last_agg_value) + "</td>\n");
							// 累计环比&环比波动
							if (Double.parseDouble(kpiStruct[j].agg_last_per.substring(0,
									kpiStruct[j].agg_last_per.length() - 1)) > 0) {
								strHtml.append("<td width=\"15%\" align=\"right\">"
										+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[j].last_per
												: kpiStruct[j].agg_last_per) + "<img src=\""
										+ reqPath + "/images/common/leader/sign_qushi_up.gif\">"
										+ "</td>\n");
							} else {
								strHtml.append("<td width=\"15%\" align=\"right\">"
										+ (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle) ? kpiStruct[j].last_per
												: kpiStruct[j].agg_last_per) + "<img src=\""
										+ reqPath + "/images/common/leader/sign_qushi_down.gif\">"
										+ "</td>\n");
							}
							strHtml.append("</tr>\n");
						}
					}
				}
				strHtml.append("</tbody>");
			}
			strHtml.append("</table>");
			strHtml.append("</div>");
		}
		return strHtml.toString();
	}

	/**
	 * 查看领导看板报表
	 *
	 * @param rptBelong
	 * @return
	 */
	public static String[][] getRptInfoByRptBelong(String rptBelong) {
		String result[][] = null;
		try {
			String sql = "select rpt_id, name, LOCAL_RES_CODE from ui_rpt_info_report WHERE rpt_belong='"
					+ rptBelong + "' and status!='N' order by LOCAL_RES_CODE";
			result = WebDBUtil.execQryArray(sql, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询指标信息
	 *
	 * @param msu_id
	 * @return
	 */
	public static LeaderKpiInfoStruct getKpiInfoStruct(String msu_id) {
		LeaderKpiInfoStruct struct = new LeaderKpiInfoStruct();
		try {
			String sql = SQLGenator.genSQL("AIQ1005", msu_id);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				struct.msu_id = result[0][0];
				struct.msu_code = result[0][1];
				struct.parent_id = result[0][2];
				struct.msu_type = result[0][3];
				struct.msu_name = result[0][4];
				struct.msu_desc = result[0][5];
				struct.unit = result[0][6];
				struct.status = result[0][7];
				struct.digit = Integer.parseInt(result[0][8]);
				struct.sequence = result[0][9];
				struct.day_same_limit = result[0][10];
				struct.day_last_limit = result[0][11];
				struct.day_agg_same_limit = result[0][12];
				struct.day_agg_last_limit = result[0][13];
				struct.month_same_limit = result[0][14];
				struct.month_last_limit = result[0][15];
				struct.month_agg_same_limit = result[0][16];
				struct.month_agg_last_limit = result[0][17];
				struct.msu_belong = result[0][18];
				struct.msu_knd = result[0][19];
				struct.msu_cycle = result[0][20];
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return struct;
	}

	/**
	 * 查询指标信息
	 *
	 * @param qryStruct
	 * @return
	 */
	public static LeaderKpiInfoStruct[] findKpiInfoStruct(LeaderQryStruct qryStruct) {
		LeaderKpiInfoStruct[] infoStruct = null;
		String whereStr = "";
		if ("1".equals(qryStruct.data_cycle)) {
			if (qryStruct.gather_mon != null && !"".equals(qryStruct.gather_mon)) {
				whereStr += " AND A.MONTH_ID = " + qryStruct.gather_mon;
			}
		} else {
			if (qryStruct.gather_day != null && !"".equals(qryStruct.gather_day)) {
				whereStr += " AND A.DAY_ID = " + qryStruct.gather_day;
			}
		}
		if (qryStruct.cust_group_id != null && !"".equals(qryStruct.cust_group_id)) {
			whereStr += " AND A.CUST_GROUP_ID = '" + qryStruct.cust_group_id + "'";
		}
		if (qryStruct.city_id != null && !"".equals(qryStruct.city_id)) {
			whereStr += " AND A.CITY_ID = '" + qryStruct.city_id + "'";
		}

		try {
			String sql = "SELECT B.MSU_ID,\n" + "       B.MSU_NAME,\n" + "       B.UNIT,\n"
					+ "       B.DIGIT,\n" + "       COALESCE(A.VALUE1, 0),\n"
					+ "       COALESCE(A.VALUE2, 0),\n" + "       COALESCE(A.VALUE3, 0),\n"
					+ "       COALESCE(A.VALUE4, 0),\n" + "       COALESCE(A.VALUE5, 0),\n"
					+ "       COALESCE(A.VALUE6, 0),\n" + "       B.MSU_DESC\n"
					+ "  FROM UI_PUB_INFO_MSU B\n" + "  LEFT OUTER JOIN (SELECT A.MSU_ID,\n"
					+ "                          COALESCE(SUM(A.CUR_VALUE), 0) AS VALUE1,\n"
					+ "                          COALESCE(SUM(A.LAST_VALUE), 0) AS VALUE2,\n"
					+ "                          COALESCE(SUM(A.SAME_VALUE), 0) AS VALUE3,\n"
					+ "                          COALESCE(SUM(A.AGG_VALUE), 0) AS VALUE4,\n"
					+ "                          COALESCE(SUM(A.LAST_AGG_VALUE), 0) AS VALUE5,\n"
					+ "                          COALESCE(SUM(A.SAME_AGG_VALUE), 0) VALUE6\n"
					+ "                     FROM UI_LEADER_KPI_INFO_DATA_D A"
					+ "           WHERE 1=1 " + whereStr
					+ "GROUP BY A.MSU_ID) A ON B.MSU_ID = A.MSU_ID WHERE B.MSU_NAME like '%"
					+ qryStruct.msu_name + "%' ORDER BY B.SEQUENCE";
			System.out.println("kpisql==============================" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				infoStruct = new LeaderKpiInfoStruct[result.length];
				for (int i = 0; i < result.length; i++) {
					infoStruct[i] = new LeaderKpiInfoStruct();
					infoStruct[i].msu_id = result[i][0];
					infoStruct[i].msu_name = result[i][1];
					infoStruct[i].unit = result[i][2];
					infoStruct[i].msu_desc = result[i][10];
					infoStruct[i].digit = Integer.parseInt(result[i][3]);
					infoStruct[i].cur_value = FormatUtil.formatStr(result[i][4],
							infoStruct[i].digit, true);// 本期值
					infoStruct[i].last_value = FormatUtil.formatStr(result[i][5],
							infoStruct[i].digit, true);// 上期值
					infoStruct[i].same_value = FormatUtil.formatStr(result[i][6],
							infoStruct[i].digit, true);// 同期值
					infoStruct[i].agg_value = FormatUtil.formatStr(result[i][7],
							infoStruct[i].digit, true);// 累计值
					infoStruct[i].last_agg_value = FormatUtil.formatStr(result[i][8],
							infoStruct[i].digit, true);// 上期累计值
					infoStruct[i].same_agg_value = FormatUtil.formatStr(result[i][9],
							infoStruct[i].digit, true);// 同期累计值
					infoStruct[i].last_per = NullProcFactory.transNullToFullRate(Arith.divPer(
							Arith.sub(result[i][4], result[i][5]), result[i][5], 2));// 环比
					infoStruct[i].same_per = NullProcFactory.transNullToFullRate(Arith.divPer(
							Arith.sub(result[i][4], result[i][6]), result[i][6], 2));// 同比
					infoStruct[i].agg_last_per = NullProcFactory.transNullToFullRate(Arith.divPer(
							Arith.sub(result[i][7], result[i][8]), result[i][8], 2));// 累计环比
					infoStruct[i].agg_same_per = NullProcFactory.transNullToFullRate(Arith.divPer(
							Arith.sub(result[i][7], result[i][9]), result[i][9], 2));// 累计同比
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return infoStruct;
	}

	public static String[][] getViewByGuide(String MsuID) {
		String result[][] = null;
		try {
			String sql = SQLGenator.genSQL("AIVIEW01", MsuID);
			result = WebDBUtil.execQryArray(sql, "");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param infoStruct
	 * @param qryStruct
	 * @return
	 */
	public static String[][] getOppChartInfo(LeaderKpiInfoStruct infoStruct,
			LeaderQryStruct qryStruct) {
		// 条件
		String whereStr = "";
		if (qryStruct.gather_day != null && !"".equals(qryStruct.gather_day)) {
			whereStr += " AND A.GATHER_DAY = " + qryStruct.gather_day;
		}
		// 查询结果
		String[][] result = null;
		try {
			// SELECT A.OPP_ID , A.OPP_NAME ,
			// SUM(A.NEW_UNUM),SUM(A.NEW_UNUM)-SUM(A.LAST_NEW_UNUM),SUM(A.AGG_NEW_UNUM),SUM(A.ARRIVE_UNUM)
			// FROM FI_LEADER_OPP_UNUM_D A
			// WHERE A.GATHER_DAY_ID =20090701
			// GROUP BY ROLLUP(A.OPP_ID , A.OPP_NAME)
			// HAVING(A.OPP_ID IS NOT NULL AND A.OPP_NAME IS NOT NULL) OR
			// (A.OPP_ID IS NULL AND A.OPP_NAME IS NULL)
			// ORDER BY A.OPP_ID
			String sql = SQLGenator.genSQL("AIQ1009", whereStr);
			System.out.println("AIQ1009===getOppcHARTInfo===============" + sql);
			String[][] tmpresult = WebDBUtil.execQryArray(sql, "");
			if (tmpresult != null && tmpresult.length > 1) {
				result = new String[tmpresult.length - 1][5];
				for (int i = 0; i < tmpresult.length - 1; i++) {
					result[i][0] = tmpresult[i][0];
					result[i][1] = tmpresult[i][1];
					result[i][2] = Arith.divPerNot(tmpresult[i][2], tmpresult[i][11], 2);
					result[i][3] = Arith.divPerNot(tmpresult[i][3], tmpresult[i][12], 2);
					result[i][4] = Arith.divPerNot(tmpresult[i][4], tmpresult[i][13], 2);
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static double[][] setCategoryValue(String[] category, String[] series, String[][] value,
			int p1, int p2, int p3) {
		if (value == null)
			return null;
		if (series == null || series.length == 0)
			return null;
		if (category == null || category.length == 0) {
			category = new String[1];
			category[0] = "";
		}
		double[][] result = new double[category.length][series.length];
		for (int i = 0; i < category.length; i++) {
			for (int j = 0; j < series.length; j++) {
				String tmp1 = category[i];
				String tmp2 = series[j];
				for (int m = 0; m < value.length; m++) {
					String res1 = "";
					if (p1 >= 0)
						res1 = value[m][p1];
					String res2 = value[m][p2].trim();
					if (tmp1.equals(res1) && tmp2.equals(res2)) {
						if (value[m][p3] == null || "".equals(value[m][p3])) {
							result[i][j] = -999999999;
						} else {
							result[i][j] = StringB.toDouble(value[m][p3], -999999999);
						}
						// result[i][j] = StringB.toDouble(value[m][p3], 0);
						break;
					} else {
						result[i][j] = -999999999;
					}
				}
			}
		}
		return result;
	}

	/**
	 * 查询KPI数据信息
	 *
	 * @param qryStruct
	 * @return
	 */
	public static LeaderKpiInfoStruct[] getLVKpiInfoStruct(LeaderQryStruct qryStruct,
			UserCtlRegionStruct regionStruct) {
		LeaderKpiInfoStruct[] infoStruct = null;
		String whereStr = "";
		if ("1".equals(qryStruct.data_cycle)) {
			if (qryStruct.gather_mon != null && !"".equals(qryStruct.gather_mon)) {
				whereStr += " AND A.MONTH_ID = " + qryStruct.gather_mon;
			}
		} else {
			if (qryStruct.gather_day != null && !"".equals(qryStruct.gather_day)) {
				whereStr += " AND A.DAY_ID = " + qryStruct.gather_day;
			}
		}
		if (qryStruct.cust_group_id != null && !"".equals(qryStruct.cust_group_id)) {
			whereStr += " AND A.CUST_GROUP_ID = '" + qryStruct.cust_group_id + "'";
		}
		if ("999".equals(qryStruct.city_id)) {
			qryStruct.city_id = "";
		}

		if (qryStruct.city_id != null && !"".equals(qryStruct.city_id)) {// 全省
			whereStr += " AND A.CITY_ID = '" + qryStruct.city_id + "'";
		} else if (!"1".equals(regionStruct.ctl_lvl)) {// 不是全省
			if ("".equals(qryStruct.city_id) && !"".equals(regionStruct.ctl_city_str)) {
				whereStr += " AND A.CITY_ID " + regionStruct.ctl_in_or_equals
						+ regionStruct.ctl_city_str_add;
			}
			// ?
			if (qryStruct.city_id != null && !"".equals(qryStruct.city_id)
					&& regionStruct.ctl_city_str.indexOf(qryStruct.city_id) >= 0) {
				whereStr += " AND A.CITY_ID = '" + qryStruct.city_id + "'";
			} else {
				whereStr += " AND 1=1 ";// 不是一个闭环！
			}

		}

		try {
			String sql = SQLGenator.genSQL("LVQ1001", qryStruct.data_cycle,
					qryStruct.data_source.toUpperCase(), whereStr);
			System.out.println("LVQ1001=====KPI分析============" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				infoStruct = new LeaderKpiInfoStruct[result.length];
				for (int i = 0; i < result.length; i++) {
					infoStruct[i] = new LeaderKpiInfoStruct();
					infoStruct[i].msu_id = result[i][0];
					infoStruct[i].msu_name = result[i][1];
					infoStruct[i].unit = result[i][2];
					infoStruct[i].digit = Integer.parseInt(result[i][3]);
					infoStruct[i].cur_value = FormatUtil.formatStr(result[i][4],
							infoStruct[i].digit, true);// 本期值
					infoStruct[i].last_value = FormatUtil.formatStr(result[i][5],
							infoStruct[i].digit, true);// 上期值
					infoStruct[i].same_value = FormatUtil.formatStr(result[i][6],
							infoStruct[i].digit, true);// 同期值
					infoStruct[i].agg_value = FormatUtil.formatStr(result[i][7],
							infoStruct[i].digit, true);// 累计值
					infoStruct[i].last_agg_value = FormatUtil.formatStr(result[i][8],
							infoStruct[i].digit, true);// 上期累计值
					infoStruct[i].same_agg_value = FormatUtil.formatStr(result[i][9],
							infoStruct[i].digit, true);// 同期累计值
					infoStruct[i].last_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][4], result[i][5]), result[i][5], 2),
							"100%");// 环比
					infoStruct[i].same_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][4], result[i][6]), result[i][6], 2),
							"100%");// 同比
					infoStruct[i].agg_last_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][7], result[i][8]), result[i][8], 2),
							"100%");// 累计环比
					infoStruct[i].agg_same_per = NullProcFactory.transNullToFixedRate(
							Arith.divPer(Arith.sub(result[i][7], result[i][9]), result[i][9], 2),
							"100%");// 累计同比
					infoStruct[i].msu_knd = result[i][10];
				}
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
		return infoStruct;
	}

	/**
	 * 页面的kpi表格输出
	 *
	 * @return
	 */
	public static String findLVKpiInfoStructForJsp(LeaderKpiInfoStruct[] kpiStruct,
			LeaderQryStruct qryStruct) {
		StringBuffer strHtml = new StringBuffer("<tr class=\"celtitle FixedTitleRow\">");
		if ("1".equals(qryStruct.data_cycle)) {
			strHtml.append("<td bgcolor=\"E3E3E3\" align=\"center\">指标</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">当月</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">上月</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">本年累计</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">环比</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">环比波动</td>");
		} else {
			strHtml.append("<td bgcolor=\"E3E3E3\" align=\"center\">指标</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">当日</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">本月累计</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">上月累计</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">累计环比</td>")
					.append("<td bgcolor=\"E3E3E3\" align=\"center\">环比波动</td>");
		}
		strHtml.append("</tr>");
		if (kpiStruct != null) {
			for (int i = 0; i < kpiStruct.length; i++) {
				// 上级节点
				if (i == 0
						|| (kpiStruct[i].msu_knd != null && !kpiStruct[i].msu_knd
								.equals(kpiStruct[i - 1].msu_knd))) {
					// 显示该节点
					if (i != 0) {
						strHtml.append("</tbody>");
					}
					String msu_name = "";
					if ("1".equals(kpiStruct[i].msu_knd)) {
						msu_name = "移动";
					} else if ("2".equals(kpiStruct[i].msu_knd)) {
						msu_name = "固网";
					} else if ("3".equals(kpiStruct[i].msu_knd)) {
						msu_name = "宽带";
					}
					strHtml.append(
							"<tr id=\"a"
									+ i
									+ "_row\" class=\"celdata\" onmouseover=\"this.className='mouseM'\" onmouseout=\"this.className='celdata'\">\n"
									+ "<td class=\"leftdata\" align=\"left\"><img id='c1' src=\"../images/common/leader/sign_hidden.gif\" onclick=\"showDetail('a"
									+ i + "','a" + i + "_content','a" + i
									+ "_row');swtichPic(this);\"/> <a id=\"a" + i
									+ "\" class=\"txt_hidden\" title=\"" + kpiStruct[i].msu_desc
									+ "\" onclick=\"HighLightSelRow();\""
									+ "href=\"javascript:ShowMsuInfo('" + kpiStruct[i].msu_id
									+ "');\">")
							.append(msu_name)
							.append("</a>\n" + "</td>\n" + "<td>" + "</td>\n" + "<td>" + "</td>\n"
									+ "<td>" + "</td>\n");

					strHtml.append("<td></td>");
					strHtml.append("<td align=\"center\"></td>");

					strHtml.append("</tr>");
					// 在找到它的子节点显示
					if (i == 0) {
						strHtml.append("<tbody id=\"a" + i + "_content\" >\n");
					} else {
						strHtml.append("<tbody id=\"a" + i
								+ "_content\" style=\"display:none;\">\n");
					}
				}

				strHtml.append("<tr class=\"celdata_default\" onmouseover=\"this.className='mouseM';this.style.textAlign='left';\"\n"
						+ "onmouseout=\"this.className='celdata_default'\">\n"
						+ "<td><a title=\""
						+ kpiStruct[i].msu_desc
						+ "\" href=\"javascript:ShowMsuInfo('"
						+ kpiStruct[i].msu_id
						+ "');\" onclick=\"HighLightSelRow();\" class=\"dataurl\">"
						+ kpiStruct[i].msu_name
						+ "("
						+ kpiStruct[i].unit
						+ ")"
						+ "</a></td>\n"
						+

						"<td align=\"right\">"
						+ kpiStruct[i].cur_value
						+ "</td>\n"
						+ "<td align=\"right\">"
						+ ("1".equals(qryStruct.data_cycle) ? kpiStruct[i].last_value
								: kpiStruct[i].agg_value)
						+ "</td>\n"
						+ "<td align=\"right\">"
						+ ("1".equals(qryStruct.data_cycle) ? kpiStruct[i].agg_value
								: kpiStruct[i].last_agg_value) + "</td>\n");
				if (Double.parseDouble(kpiStruct[i].agg_last_per.substring(0,
						kpiStruct[i].agg_last_per.length() - 1)) > 0) {
					strHtml.append("<td align=\"right\"><font class=\"numred\">"
							+ ("1".equals(qryStruct.data_cycle) ? kpiStruct[i].last_per
									: kpiStruct[i].agg_last_per) + "</font></td>");
					strHtml.append("<td align=\"center\"><img src=\"../images/common/leader/sign_qushi_up.gif\" ></td>");
				} else {
					strHtml.append("<td align=\"right\"><font class=\"numgreen\">"
							+ ("1".equals(qryStruct.data_cycle) ? kpiStruct[i].last_per
									: kpiStruct[i].agg_last_per) + "</font></td>");
					strHtml.append("<td align=\"center\"><img src=\"../images/common/leader/sign_qushi_down.gif\" ></td>");
				}
				strHtml.append("</tr>");
			}
			strHtml.append("</tbody>")
					.append("<script>document.getElementById('a0').className = 'txt_show';")
					.append("document.getElementById('c1').src = '../images/common/leader/sign_show.gif';")
					.append("document.getElementById('a0_row').style.background='#fefece';")
					.append("</script>");
		}

		return strHtml.toString();
	}

	/**
	 * ajaxkpi表格输出
	 *
	 * @param qryStruct
	 * @param regionStruct
	 * @return
	 */
	public static String findLVKpiInfoStructByCityId(LeaderQryStruct qryStruct,
			UserCtlRegionStruct regionStruct) {
		StringBuffer strHtml = new StringBuffer();
		strHtml.append(
				"<table style=\"width: 100%\" border=\"0\" bgcolor=\"#CFCFCF\" cellpadding=\"1\" cellspacing=\"1\" style=\"margin:0;\">")
				.append(findLVKpiInfoStructForJsp(getLVKpiInfoStruct(qryStruct, regionStruct),
						qryStruct)).append("</table>");
		return strHtml.toString();
	}
}
