package com.ailk.bi.base.util;

import java.util.Vector;
import java.awt.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.*;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.chart.CreateChartObj;
import com.ailk.bi.common.chart.WebChart;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "rawtypes" })
public class ChartUtil {

	/**
	 * 获取指定chart_id指定查询结构的图形
	 * 
	 * @param chart_id
	 * @param qry
	 * @param request
	 * @param session
	 * @param pw
	 * @throws AppException
	 */
	public static String genererChart(String chart_id, String ext_name,
			LsbiQryStruct qry, HttpServletRequest request, HttpSession session,
			PrintWriter pw) throws AppException {
		UiCommonChartDefTable ctable = getChartTable(chart_id);
		if (ctable == null)
			return "<center><font color=red>未定义</font></center>";
		String[][] arrObj = genererDataSet(chart_id, qry, ctable, session);
		return genererChart(ctable, ext_name, arrObj, request, session, pw);
	}

	/**
	 * 获取指定chart_id指定查询结构的图形
	 * 
	 * @param chart_id
	 * @param qry
	 * @param wheresql
	 * @param request
	 * @param session
	 * @param pw
	 * @throws AppException
	 */
	public static String genererChart(String chart_id, String ext_name,
			LsbiQryStruct qry, String wheresql, HttpServletRequest request,
			HttpSession session, PrintWriter pw) throws AppException {
		UiCommonChartDefTable ctable = getChartTable(chart_id);
		if (ctable == null)
			return "<center><font color=red>未定义</font></center>";

		ctable.sql_where = ctable.sql_where + " " + wheresql;
		String[][] arrObj = genererDataSet(chart_id, qry, ctable, session);
		return genererChart(ctable, ext_name, arrObj, request, session, pw);
	}

	/**
	 * 获取指定chart_id指定查询结构的图形
	 * 
	 * @param chart_id
	 * @param wheresql
	 * @param ordersql
	 * @param request
	 * @param session
	 * @param pw
	 * @return
	 * @throws AppException
	 */
	public static String genererChart(String chart_id, String ext_name,
			LsbiQryStruct qry, String wheresql, String ordersql,
			HttpServletRequest request, HttpSession session, PrintWriter pw)
			throws AppException {
		UiCommonChartDefTable ctable = getChartTable(chart_id);
		if (ctable == null)
			return "<center><font color=red>未定义</font></center>";

		ctable.sql_where = ctable.sql_where + " " + wheresql;
		if (ordersql == null || "".equals(ordersql.trim()))
			ctable.sql_order = ordersql;
		String[][] arrObj = genererDataSet(chart_id, qry, ctable, session);
		return genererChart(ctable, ext_name, arrObj, request, session, pw);
	}

	/**
	 * 获取指定chart_id给定数据源的图形
	 * 
	 * @param chart_id
	 * @param arrObj
	 * @param request
	 * @param session
	 * @param pw
	 * @throws AppException
	 */
	public static String genererChart(String chart_id, String ext_name,
			String[][] arrObj, HttpServletRequest request, HttpSession session,
			PrintWriter pw) throws AppException {
		UiCommonChartDefTable ctable = getChartTable(chart_id);
		if (ctable == null)
			return "<center><font color=red>未定义</font></center>";

		return genererChart(ctable, ext_name, arrObj, request, session, pw);
	}

	/**
	 * *************************************************************************
	 * *******************
	 */

	/**
	 * 生成chart对象
	 * 
	 * @param ctable
	 * @param data
	 * @param request
	 * @param session
	 * @param pw
	 */
	public static String genererChart(UiCommonChartDefTable ctable,
			String ext_name, String[][] data, HttpServletRequest request,
			HttpSession session, PrintWriter pw) {
		WebChart chart = null;
		int ctype = StringB.toInt(ctable.chart_type, 0); // 图形类型
		int cdtag = StringB.toInt(ctable.isusecd, 0); // 获取分组描述定义

		int li_category = StringB.toInt(ctable.category_index, 0); // 分组描述索引值
		int li_series = StringB.toInt(ctable.series_index, 0); // X轴描述索引值
		int li_slength = StringB.toInt(ctable.series_length, 0); // X轴描述长度
		int li_value = StringB.toInt(ctable.value_index, 0); // 数据字段索引值
		int li_chart_index = StringB.toInt(ctable.chart_index, 0);// 获取分图索引

		String[] pCategory = null; // 分组描述数组
		if (ctable.category_desc != null
				&& !"".equals(ctable.category_desc.trim()))
			pCategory = ctable.category_desc.split(",");
		String[] pCategoryIndex = null; // 分组描述数组索引
		if (ctable.category_desc_index != null
				&& !"".equals(ctable.category_desc_index))
			pCategoryIndex = ctable.category_desc_index.split(",");

		if (cdtag == 2) {
			if (ctype >= 1 && ctype <= 3)
				chart = CreateChartObj.getChartObjPie(data, pCategory,
						pCategoryIndex);
			else if ((ctype >= 4 && ctype <= 14)
					|| (ctype >= 18 && ctype <= 50)) {
				if (li_slength > 12 || li_slength == -1) {
					chart = CreateChartObj.getChartObjCategory(data, pCategory,
							pCategoryIndex, li_series, 4, false);
				} else {
					chart = CreateChartObj.getChartObjCategory(data, pCategory,
							pCategoryIndex, li_series);
				}
			}
		} else if (cdtag == 1) {
			if (ctype >= 1 && ctype <= 3)
				chart = CreateChartObj.getChartObjPie(data, li_category,
						li_value);
			else if ((ctype >= 4 && ctype <= 14)
					|| (ctype >= 18 && ctype <= 50)) {
				if (li_slength > 12 || li_slength == -1) {
					chart = CreateChartObj.getChartObjCategory(data,
							li_category, li_series, li_value, pCategory, null,
							4, false);
				} else {
					chart = CreateChartObj.getChartObjCategory(data,
							li_category, li_series, li_value, pCategory, null);
				}
			}
		} else {
			if (ctype >= 1 && ctype <= 3)
				chart = CreateChartObj.getChartObjPie(data, li_category,
						li_value);
			else if ((ctype >= 4 && ctype <= 14)
					|| (ctype >= 18 && ctype <= 50)) {
				if (li_slength > 12 || li_slength == -1) {
					chart = CreateChartObj.getChartObjCategory(data,
							li_category, li_series, li_value, 4, false);
				} else {
					chart = CreateChartObj.getChartObjCategory(data,
							li_category, li_series, li_value);
				}
			}
		}
		if ("N".equals(ctable.chart_valid.toUpperCase())) {
			System.out.println("======================此图无效!");
			chart.setNoDataMessage("此图无效");
			// chart.setPieBorderColor(Color.black);
		}

		return setChartAttribute(ctype, li_chart_index, ext_name, chart,
				ctable.chart_attribute, request, session, pw);
	}

	/**
	 * 设置chart对象属性
	 * 
	 * @param chart_type
	 * @param fchart
	 * @param chart_attribute
	 * @param request
	 * @param session
	 * @param pw
	 */
	public static String setChartAttribute(int chart_type, int li_chart_index,
			String ext_name, WebChart fchart, String chart_attribute,
			HttpServletRequest request, HttpSession session, PrintWriter pw) {
		String[] atts = chart_attribute.split(";");
		String chart_name = "";
		int iwidth = 200;
		int iheight = 200;
		for (int i = 0; atts != null && i < atts.length; i++) {
			String[] tmps = atts[i].split(":");
			if ("outlinecolor".equals(tmps[0].toLowerCase()))
				fchart.setPieBorderColor(Color.white);
			if ("width".equals(tmps[0].toLowerCase()))
				iwidth = StringB.toInt(tmps[1], 200);
			if ("height".equals(tmps[0].toLowerCase()))
				iheight = StringB.toInt(tmps[1], 200);
			if ("title".equals(tmps[0].toLowerCase()))
				chart_name = tmps[1];
			if ("titlefont".equals(tmps[0].toLowerCase())) {
				String[] tmpf = tmps[1].split(",");
				fchart.setTitleFont(new Font(tmpf[0],
						StringB.toInt(tmpf[1], 0), StringB.toInt(tmpf[2], 12)));
			}
			if ("xaxisinfo".equals(tmps[0].toLowerCase()))
				fchart.setChartXInfo(tmps[1]);
			if ("yaxisinfo".equals(tmps[0].toLowerCase()))
				fchart.setChartYInfo(tmps[1]);
			if ("yaxisinfo_extend".equals(tmps[0].toLowerCase()))
				fchart.setChartYInfoExtend(tmps[1]);
			if ("legend".equals(tmps[0].toLowerCase())) {
				if ("false".equals(tmps[1]))
					fchart.setLegendVisible(false);
				else
					fchart.setLegendVisible(true);
			}
			if ("legendborder".equals(tmps[0].toLowerCase()))
				fchart.setLegendBorder(StringB.toDouble(tmps[1], 0));
			if ("legendpos".equals(tmps[0].toLowerCase())
					|| "labelpositions".equals(tmps[0].toLowerCase()))
				fchart.setLegendPos(tmps[1]);
			if ("pielegender".equals(tmps[0].toLowerCase())) {
				if ("null".equals(tmps[1]))
					tmps[1] = "";
				fchart.setPieLegend(tmps[1]);
			}
			if ("pieoutline".equals(tmps[0].toLowerCase())) {
				if ("false".equals(tmps[1]))
					fchart.setPieLinksVisible(false);
				else
					fchart.setPieLinksVisible(true);
			}
			if ("pielegend".equals(tmps[0].toLowerCase())) {
				if ("null".equals(tmps[1]))
					tmps[1] = "";
				fchart.setPieLegend(tmps[1]);

			}
			if ("barlegend".equals(tmps[0].toLowerCase()))
				fchart.setBarLegend(tmps[1]);
			if ("includeszero".equals(tmps[0].toLowerCase())) {
				if ("false".equals(tmps[1]))
					fchart.setIncludesZero(false);
				else
					fchart.setIncludesZero(true);
			}
			if ("labelpositions".equals(tmps[0].toLowerCase()))
				fchart.setCateGoryLabelPositions(StringB.toDouble(tmps[1], 0));
			if ("valuepositions".equals(tmps[0].toLowerCase()))
				fchart.setValuePositions(StringB.toDouble(tmps[1], 0));
			if ("plotorientation".equals(tmps[0].toLowerCase()))
				fchart.setChartPlotOrientation(StringB.toInt(tmps[1], 0));
			if ("alpha".equals(tmps[0].toLowerCase()))
				fchart.setAlpha(StringB.toFloat(tmps[1], 0.9F));
			if ("circular".equals(tmps[0].toLowerCase())) {
				if ("false".equals(tmps[1]))
					fchart.setPieCircular(false);
				else
					fchart.setPieCircular(true);
			}
			if ("issynchroaxis".equals(tmps[0].toLowerCase())) {
				if ("false".equals(tmps[1]))
					fchart.setSynchroAxis(false);
				else
					fchart.setSynchroAxis(true);
			}
			if ("upwight".equals(tmps[0].toLowerCase()))
				fchart.setUpWight(StringB.toInt(tmps[1], 1));
			if ("downwight".equals(tmps[0].toLowerCase()))
				fchart.setDownWight(StringB.toInt(tmps[1], 1));
			if ("toprange".equals(tmps[0].toLowerCase()))
				fchart.setTopRange(StringB.toDouble(tmps[1], 0));
			if ("bottomrange".equals(tmps[0].toLowerCase()))
				fchart.setBottomRange(StringB.toInt(tmps[1], 0));
			if ("rangename".equals(tmps[0].toLowerCase()))
				fchart.setRangeName(tmps[1]);
			if ("colorswoon".equals(tmps[0].toLowerCase())) {
				if ("false".equals(tmps[1]))
					fchart.setUseTransColor(false);
				else
					fchart.setUseTransColor(true);
			}

		}
		String filename = CreateChartObj.getChartName(chart_type, fchart,
				li_chart_index, ext_name + chart_name, iwidth, iheight,
				session, pw);
		String url = request.getContextPath()
				+ "/servlet/DisplayChart?filename=" + filename;
		return "<img src=" + url + " border=0 usemap=#" + filename + ">";
	}

	/**
	 * *************************************************************************
	 * *******************
	 */

	/**
	 * 获取指定id的图形信息
	 * 
	 * @param chart_id
	 * @return
	 * @throws AppException
	 */
	public static UiCommonChartDefTable getChartTable(String chart_id)
			throws AppException {
		UiCommonChartDefTable ctable = null;
		String sql = SQLGenator.genSQL("Q2300", chart_id);
		System.out.println("Q2300==================" + sql);
		Vector result = WebDBUtil.execQryVector(sql, "");
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			ctable = new UiCommonChartDefTable();
			int m = 0;
			ctable.chart_id = (String) tempv.get(m++);
			ctable.table_id = (String) tempv.get(m++);
			ctable.chart_belong = (String) tempv.get(m++);
			ctable.chart_type = (String) tempv.get(m++);
			ctable.chart_attribute = (String) tempv.get(m++);
			ctable.chart_sql = (String) tempv.get(m++);
			ctable.sql_where = (String) tempv.get(m++);
			ctable.sql_order = (String) tempv.get(m++);
			ctable.category_index = (String) tempv.get(m++);
			ctable.series_index = (String) tempv.get(m++);
			ctable.series_length = (String) tempv.get(m++);
			ctable.value_index = (String) tempv.get(m++);
			ctable.isusecd = (String) tempv.get(m++);
			ctable.category_desc = (String) tempv.get(m++);
			ctable.category_desc_index = (String) tempv.get(m++);
			ctable.chart_index = (String) tempv.get(m++);
			ctable.chart_drill = (String) tempv.get(m++);
			ctable.chart_drill_type = (String) tempv.get(m++);
			ctable.chart_valid = (String) tempv.get(m++);
		}
		return ctable;
	}

	/**
	 * 获取指定id图形的条件
	 * 
	 * @param chart_id
	 * @return
	 * @throws AppException
	 */
	public static UiCommonChartConditionTable[] getChartConditionTable(
			String chart_id, String con_type) throws AppException {

		UiCommonChartConditionTable[] ctable = null;
		String sql = SQLGenator.genSQL("Q2301", chart_id, con_type);
		System.out.println("Q2301===========" + sql);
		Vector result = WebDBUtil.execQryVector(sql, "");
		ctable = new UiCommonChartConditionTable[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			ctable[i] = new UiCommonChartConditionTable();
			int m = 0;
			ctable[i].chart_id = (String) tempv.get(m++);
			ctable[i].con_code = (String) tempv.get(m++);
			ctable[i].qry_code = (String) tempv.get(m++);
			ctable[i].con_code_type = (String) tempv.get(m++);
			ctable[i].con_tag = (String) tempv.get(m++);
			ctable[i].con_type = (String) tempv.get(m++);
			ctable[i].table_con_tag = (String) tempv.get(m++);
		}
		return ctable;
	}

	/**
	 * 获取数据sql
	 * 
	 * @param chart_id
	 * @param qry
	 * @return
	 * @throws AppException
	 */
	public static String[][] genererDataSet(String chart_id, LsbiQryStruct qry,
			UiCommonChartDefTable ctable, HttpSession session)
			throws AppException {
		UiCommonChartConditionTable[] contables = getChartConditionTable(
				chart_id, "0,2,4");
		String strsql = ctable.chart_sql;
		String wheresql = " " + ctable.sql_where;
		String ordersql = " " + ctable.sql_order;
		for (int i = 0; contables != null && i < contables.length; i++) {
			String tmpCode = contables[i].qry_code.toLowerCase();
			String tmpValue = ReflectUtil.getStringFromObj(qry, tmpCode);

			if ("4".equals(contables[i].con_type)) {
				// add by jcm,add the data rights to the where string end
				wheresql += tmpValue;

			} else if ("3".equals(contables[i].con_type)) {// 没用
				if (tmpValue != null && !"".equals(tmpValue)) {
					tmpValue = " and " + contables[i].con_code + " "
							+ contables[i].con_tag + tmpValue;
					strsql = StringB.replaceFirst(strsql, "?", tmpValue);
				} else {
					strsql = StringB.replaceFirst(strsql, "?", "");
				}
			} else if (tmpValue != null && !"".equals(tmpValue)) {
				if ("2".equals(contables[i].con_type)) {// 没用
					strsql = StringB.replaceFirst(strsql, "?", tmpValue);
				} else {
					wheresql += " and " + contables[i].con_code + " "
							+ contables[i].con_tag;
					if ("in".equals(contables[i].con_tag))
						wheresql += "(";

					if ("0".equals(contables[i].con_code_type)) {// 字符型
						if ("in".equals(contables[i].con_tag)) {
							if (tmpValue.indexOf("'") >= 0) {
								wheresql += tmpValue;
							} else {
								String[] arrV = tmpValue.split(",");
								wheresql += StringTool.changArrToStr(arrV, "'");
							}
						} else {
							if (tmpValue.indexOf("'") >= 0)
								wheresql += tmpValue;
							else
								wheresql += "'" + tmpValue + "'";
						}
					} else {
						wheresql += tmpValue;
					}

					if ("in".equals(contables[i].con_tag))
						wheresql += ")";
				}
			}

			// 趋势取值
			if (StringB.toInt(ctable.series_length, 0) > 0) {
				String end_value = ""; // 结束值
				int iPre = 0 - StringB.toInt(ctable.series_length, 0);
				int iAfter = StringB.toInt(ctable.series_length, 0);
				if ("<=".equals(contables[i].con_tag)) {
					if (tmpValue != null && tmpValue.length() == 6) {
						end_value = DateUtil.getDiffMonth(iPre, tmpValue);
					}
					if (tmpValue != null && tmpValue.length() == 8) {
						end_value = DateUtil.getDiffDay(iPre, tmpValue);
					}
				}
				if (">=".equals(contables[i].con_tag)) {
					if (tmpValue != null && tmpValue.length() == 6) {
						end_value = DateUtil.getDiffMonth(iAfter, tmpValue);
					}
					if (tmpValue != null && tmpValue.length() == 8) {
						end_value = DateUtil.getDiffDay(iAfter, tmpValue);
					}
				}

				if (end_value != null && !"".equals(end_value)) {
					if ("<=".equals(contables[i].con_tag)) {
						wheresql += " and " + contables[i].con_code + ">=";
						if ("0".equals(contables[i].con_code_type))
							wheresql += "'" + end_value + "'";
						else
							wheresql += end_value;
					}
					if (">=".equals(contables[i].con_tag)) {
						wheresql += " and " + contables[i].con_code + "<=";
						if ("0".equals(contables[i].con_code_type))
							wheresql += "'" + end_value + "'";
						else
							wheresql += end_value;
					}
				}
			}

			// 当月取值
			if (StringB.toInt(ctable.series_length, 0) == -1) {
				if (tmpValue != null && tmpValue.length() == 8) {
					String end_value = tmpValue.subSequence(0, 6) + "01";
					if ("<=".equals(contables[i].con_tag)) {
						wheresql += " and " + contables[i].con_code + ">=";
						if ("0".equals(contables[i].con_code_type))
							wheresql += "'" + end_value + "'";
						else
							wheresql += end_value;
					}
				}
			}

		}

		String sql = "";

		if ("N".equals(ctable.chart_valid.toUpperCase())) {
			wheresql += " and  1<>1";
		}

		// 钻取分析start
		if (ctable.chart_drill.toUpperCase().equals("Y")) {
			String str = ChartDillSQL(session, strsql, ctable);
			String strorder = ChartDillOrder(session, ordersql, ctable);
			sql = str + wheresql + strorder;
		} else {
			sql = strsql + wheresql + ordersql;
		}

		// 钻取分析end

		if ("5".equals(ctable.chart_type) || "7".equals(ctable.chart_type)) {
			sql = "select * from (" + sql + ") where rownum <=10";
		}

		System.out.println("chartUtil>>genererDataSet======================="
				+ sql);
		return genererData(sql);
	}

	/**
	 * 获取结构体基本条件
	 * 
	 * @param chart_id
	 * @param qry
	 * @return
	 * @throws AppException
	 */
	public static String genererBaseWhere(String chart_id, LsbiQryStruct qry)
			throws AppException {
		UiCommonChartConditionTable[] contables = getChartConditionTable(
				chart_id, "0");
		String wheresql = " ";
		for (int i = 0; contables != null && i < contables.length; i++) {
			String tmpCode = contables[i].qry_code.toLowerCase();
			String tmpValue = ReflectUtil.getStringFromObj(qry, tmpCode);
			if (tmpValue != null && !"".equals(tmpValue)) {
				wheresql += " and " + contables[i].con_code + " "
						+ contables[i].con_tag;
				if ("in".equals(contables[i].con_tag))
					wheresql += "(";

				if ("0".equals(contables[i].con_code_type)) {// 字符型
					if ("in".equals(contables[i].con_tag)) {
						if (tmpValue.indexOf("'") >= 0) {
							wheresql += tmpValue;
						} else {
							String[] arrV = tmpValue.split(",");
							wheresql += StringTool.changArrToStr(arrV, "'");
						}
					} else {
						if (tmpValue.indexOf("'") >= 0)
							wheresql += tmpValue;
						else
							wheresql += "'" + tmpValue + "'";
					}
				} else {
					wheresql += tmpValue;
				}

				if ("in".equals(contables[i].con_tag))
					wheresql += ")";
			}
		}
		return wheresql;
	}

	/**
	 * 获取结构体条件(Table专用)
	 * 
	 * @param chart_id
	 * @param qry
	 * @return
	 * @throws AppException
	 */
	public static String genererDataWhere(String chart_id, LsbiQryStruct qry)
			throws AppException {

		UiCommonChartConditionTable[] contables = getChartConditionTable(
				chart_id, "0,2,4");
		String wheresql = " ";

		for (int i = 0; contables != null && i < contables.length; i++) {
			String tmpCode = contables[i].qry_code.toLowerCase();
			String tmpValue = ReflectUtil.getStringFromObj(qry, tmpCode);

			if (tmpValue != null && !"".equals(tmpValue)) {
				// add by jcm start
				if ("4".equals(contables[i].con_type)) {
					// add by jcm,add the data rights to the where string end
					wheresql += tmpValue;
					// add by jcm end
				} else {

					wheresql += " and A." + contables[i].con_code + " "
							+ contables[i].table_con_tag;
					if ("in".equals(contables[i].table_con_tag)) {
						wheresql += "(";
					}

					if ("0".equals(contables[i].con_code_type)) {// 字符型
						if ("in".equals(contables[i].con_tag)) {
							if (tmpValue.indexOf("'") >= 0) {
								wheresql += tmpValue;
							} else {
								String[] arrV = tmpValue.split(",");
								wheresql += StringTool.changArrToStr(arrV, "'");
							}
						} else {
							if (tmpValue.indexOf("'") >= 0)
								wheresql += tmpValue;
							else
								wheresql += "'" + tmpValue + "'";
						}
					} else {
						wheresql += tmpValue;
					}

					if ("in".equals(contables[i].table_con_tag)) {
						wheresql += ")";
					}
				}

			}
		}
		return wheresql;
	}

	/**
	 * 获取数据
	 * 
	 * @param strsql
	 * @return
	 */
	public static String[][] genererData(String strsql) {
		String[][] result = null;
		System.out.println("genererData======================" + strsql);
		try {
			result = WebDBUtil.execQryArray(strsql, "");
		} catch (Exception ex) {
			result = null;
		}
		return result;
	}

	/**
	 * 清除图形session
	 * 
	 * @param session
	 */
	public static void clearAllChartSession(HttpSession session) {
		session.removeAttribute("s_chart_flag");
		session.removeAttribute("s_chart_id");
		session.removeAttribute("s_chart_name_ext");
		session.removeAttribute("s_chart_type");
		session.removeAttribute("s_c_desc");
		session.removeAttribute("s_cdesc_index");
		session.removeAttribute("s_value_index");
	}

	/**
	 * 
	 * @param session
	 * @param qrysql
	 * @param ctable
	 * @return
	 */
	public static String ChartDillSQL(HttpSession session, String qrysql,
			UiCommonChartDefTable ctable) {
		String sql = "";
		if (ctable == null) {
			return sql;
		}

		if (qrysql == null) {
			return sql;
		}
		//
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		if (ctable.chart_drill.toUpperCase().equals("Y")
				&& "REGION".equals(ctable.chart_drill_type.toUpperCase())) {

			sql = "select ";
			if ("1".equals(ctlStruct.ctl_lvl)) {
				sql += " city_id ,city_desc, ";
			} else if ("2".equals(ctlStruct.ctl_lvl)) {
				sql += " county_id ,county_desc, ";
			} else if ("3".equals(ctlStruct.ctl_lvl)) {
				sql += " sec_area_id ,secarea_desc, ";
			} else if ("4".equals(ctlStruct.ctl_lvl)) {
				sql += " area_id ,area_desc, ";
			}
			sql = sql
					+ qrysql.substring(qrysql.toLowerCase().indexOf(
							"area_id,area_desc,") + 18);
		}
		return sql;
	}

	/**
	 * 
	 * @param session
	 * @param ordersql
	 * @param ctable
	 * @return
	 */
	public static String ChartDillOrder(HttpSession session, String ordersql,
			UiCommonChartDefTable ctable) {
		String sql = "";
		if (ctable == null) {
			return sql;
		}

		if (ordersql == null) {
			return sql;
		}
		//
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		if (ctable.chart_drill.toUpperCase().equals("Y")
				&& "REGION".equals(ctable.chart_drill_type.toUpperCase())) {

			sql = "group by ";
			if ("1".equals(ctlStruct.ctl_lvl)) {
				sql += " city_id ,city_desc ";
			} else if ("2".equals(ctlStruct.ctl_lvl)) {
				sql += " county_id ,county_desc ";
			} else if ("3".equals(ctlStruct.ctl_lvl)) {
				sql += " sec_area_id ,secarea_desc ";
			} else if ("4".equals(ctlStruct.ctl_lvl)) {
				sql += " area_id ,area_desc ";
			}
			sql = sql
					+ ordersql.substring(ordersql.toLowerCase().indexOf(
							"order by"));
		}
		return sql;
	}

}