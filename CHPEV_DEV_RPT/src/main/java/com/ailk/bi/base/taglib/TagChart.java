package com.ailk.bi.base.taglib;

import java.awt.Color;
import java.awt.Font;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.base.struct.MonitorQryStruct;
import com.ailk.bi.base.table.UiItemChartConditionTable;
import com.ailk.bi.base.table.UiItemChartDefTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.chart.CreateChartObj;
import com.ailk.bi.common.chart.WebChart;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * 运营监控子页面图形标签 主要作用：屏蔽页面脚本
 * 
 * 实现要求： 需要回显的条件参数需要植入会话 不需要回显的参数则可以以URL方式传入，
 * 
 * 
 * 为了具有一定的扩充性，增加： chartId ：msuId 两个BEAN属性作为保留接口
 * 
 * 但是目前的实现以上两属性依赖于查询结构，URL存入失效
 * 
 * 
 * @author jcm
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class TagChart extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4045019501137210203L;

	private String tableId = null;// 表格对象标识

	private String chartId = null;// 图形对象标识

	private String msuId = null;// 指标对象标识

	private String chartCheck = null; // 指定图形个数

	private MonitorQryStruct qryStruct = null;// 查询结构对象

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String table_id) {
		this.tableId = table_id;
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chart_id) {
		this.chartId = chart_id;
	}

	public String getMsuId() {
		return msuId;
	}

	public void setMsuId(String msu_id) {
		this.msuId = msu_id;
	}

	public String getChartCheck() {
		return chartCheck;
	}

	public void setChartCheck(String check) {
		this.chartCheck = check;
	}

	public MonitorQryStruct getQryStruct() {
		return qryStruct;
	}

	public void setQryStruct(MonitorQryStruct qry_struct) {
		this.qryStruct = qry_struct;
	}

	// 界面传递参数
	protected String chart_name = null; // 图形名称

	protected String chart_name_ext = null; // 图形备用名称名称

	protected String chart_type = null; // 图形类型

	protected String chart_c_desc = null; // 图形分组名称

	protected String chart_cdesc_index = null; // 图形分组名称索引

	protected String chart_cvalue_index = null; // 图形分组名称索引

	public int doStartTag() throws JspException {

		// 提取界面传递参数
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			if (request != null) {
				// 表格应用
				tableId = request.getParameter("table_id");
				if (tableId == null || "".equals(tableId)) {
					tableId = getQryStruct().table_id;
				}
				// 指标应用
				msuId = request.getParameter("msu_id");
				if (msuId == null || "".equals(msuId)) {
					msuId = getQryStruct().msu_id;
				}
				// 图形名称
				chart_name = request.getParameter("chart_name");
				if (null == chart_name) {
					chart_name = "";
				}
				// 图形备用名称名称
				chart_name_ext = request.getParameter("chart_name_ext");
				if (null == chart_name_ext) {
					chart_name_ext = "";
				}
				// 图形类型
				chart_type = request.getParameter("chart_type");
				if (null == chart_type) {
					chart_type = "";
				}
				// 图形分组名称
				chart_c_desc = request.getParameter("c_desc");
				if (null == chart_c_desc) {
					chart_c_desc = "";
				}
				// 图形分组名称索引
				chart_cdesc_index = request.getParameter("cdesc_index");
				if (null == chart_cdesc_index) {
					chart_cdesc_index = "";
				}
				// 图形分组名称索引
				chart_cvalue_index = request.getParameter("cvalue_index");
				if (null == chart_cvalue_index) {
					chart_cvalue_index = "";
				}

			}
		} catch (Exception e) {
			System.out.println("初始化图形标签出现错误，有可能参数传递失败！");
		}

		return (SKIP_BODY);
	}

	public int doEndTag() throws JspException {
		String outStr = "";

		try {
			JspWriter out = pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			HttpSession session = pageContext.getSession();
			// 根据表格应用和指标应用查询取得图形应用
			chartId = getChartId(tableId, msuId, chartCheck);
			if (session != null) {
				UiItemChartDefTable chartable = getChartTable(chartId);
				if (chartable == null) {
					outStr = "<font color=red>未定义</font>";
				} else {
					if (chart_type != null && !"".equals(chart_type)) {
						chartable.chart_type = chart_type;
					}
					if (chart_c_desc != null && !"".equals(chart_c_desc)) {
						chartable.category_desc = chart_c_desc;
					}
					if (chart_cdesc_index != null
							&& !"".equals(chart_cdesc_index)) {
						chartable.category_desc_index = chart_cdesc_index;
					}
					if (chart_cvalue_index != null
							&& !"".equals(chart_cvalue_index)) {
						chartable.value_index = chart_cvalue_index;
					}
					String[][] arrObj = genererDataSet(chartId, qryStruct,
							chartable);
					outStr = genererChart(chartable, chart_name + " "
							+ chart_name_ext, arrObj, request, session,
							new PrintWriter(out));
				}

				out.print(outStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	/**
	 * 设置chart对象属性
	 * 
	 * @param chart_type
	 *            图形类型
	 * @param li_chart_index
	 * @param ext_name
	 *            图形扩展名称(标题)
	 * @param fchart
	 *            图形对象
	 * @param chart_attribute
	 *            图形属性
	 * @param request
	 * @param session
	 * @param pw
	 * @return
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
				fchart.setPieLinksLegend(tmps[1]);
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
	 * 生成chart对象
	 * 
	 * @param ctable
	 * @param ext_name
	 * @param data
	 * @param request
	 * @param session
	 * @param pw
	 * @return
	 */

	public static String genererChart(UiItemChartDefTable ctable,
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

		return setChartAttribute(ctype, li_chart_index, ext_name, chart,
				ctable.chart_attribute, request, session, pw);
	}

	/**
	 * 获取指定图形ID
	 * 
	 * @param chart_id
	 * @param con_type
	 * @return
	 * @throws AppException
	 */
	public static UiItemChartConditionTable[] getChartConditionTable(
			String chart_id) throws AppException {

		UiItemChartConditionTable[] itemtable = null;
		String sql = SQLGenator.genSQL("Q1801", chart_id);
		// System.out.println("Q1801====getChartConditionTable============" +
		// sql);
		Vector result = WebDBUtil.execQryVector(sql, "");
		itemtable = new UiItemChartConditionTable[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			itemtable[i] = new UiItemChartConditionTable();
			int m = 0;
			itemtable[i].chart_id = (String) tempv.get(m++);
			itemtable[i].table_field_code = (String) tempv.get(m++);
			itemtable[i].qry_field_code = (String) tempv.get(m++);
			itemtable[i].con_code_type = (String) tempv.get(m++);
			itemtable[i].con_tag = (String) tempv.get(m++);
			itemtable[i].sequence = (String) tempv.get(m++);
		}
		return itemtable;
	}

	/**
	 * 获取指定id的图形信息
	 * 
	 * @param chart_id
	 * @return
	 * @throws AppException
	 */
	public static UiItemChartDefTable getChartTable(String chart_id)
			throws AppException {
		UiItemChartDefTable ctable = null;
		String ls_sql = SQLGenator.genSQL("Q1800", chart_id);
		// System.out.println("Q1800======获取指定id的图形信息=================" +
		// ls_sql);
		Vector result = WebDBUtil.execQryVector(ls_sql, "");
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			ctable = new UiItemChartDefTable();
			int m = 0;
			ctable.chart_id = (String) tempv.get(m++);
			ctable.table_id = (String) tempv.get(m++);
			ctable.msu_id = (String) tempv.get(m++);
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
			ctable.dolast = (String) tempv.get(m++);
			ctable.doloop = (String) tempv.get(m++);
			ctable.chart_check = (String) tempv.get(m++);
			ctable.chart_parttern = (String) tempv.get(m++);

		}
		return ctable;
	}

	/**
	 * 取得图形ID
	 * 
	 * @param table_id
	 * @param msu_id
	 * @return
	 */
	public static String getChartId(String table_id, String msu_id, String index) {

		String chartid = "";
		try {
			String sql = SQLGenator.genSQL("Q1802", table_id, msu_id, index);
			// System.out.println("Q1802======取得图形ID=================" + sql);
			String[][] result = WebDBUtil.execQryArray(sql, "");
			if (result != null && result.length > 0) {
				chartid = result[0][0];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return chartid;
	}

	/**
	 * 获取数据sql
	 * 
	 * @param chart_id
	 * @param qry
	 * @return
	 * @throws AppException
	 */
	public static String[][] genererDataSet(String chart_id,
			MonitorQryStruct qry, UiItemChartDefTable ctable)
			throws AppException {

		String strsql = ctable.chart_sql;
		// 所有条件传递都必须通过查询结构
		// 之后以查询结构为统一配置参数取值
		// 参考的取值对象为配置对应结构
		// 无需关联数据表结构
		String whereStr = " 1=1 ";
		String whereStr_last = " 1=1";
		String whereStr_loop = " 1=1 ";

		UiItemChartConditionTable[] contables = getChartConditionTable(chart_id);
		// 本期
		for (int i = 0; contables != null && i < contables.length; i++) {

			String tmpCode = contables[i].qry_field_code.toLowerCase();
			String tmpValue = ReflectUtil.getStringFromObj(qry, tmpCode);
			if (null != tmpValue && !"".equals(tmpValue)) {
				// con_code_type=4数据权限
				if ("4".equals(contables[i].con_code_type)) {
					whereStr += tmpValue;
				} else {
					whereStr += " and " + contables[i].table_field_code + " "
							+ contables[i].con_tag;
					if ("in".equals(contables[i].con_tag)) {
						whereStr += "(";
					}

					if ("0".equals(contables[i].con_code_type)) {// 字符型
						if ("in".equals(contables[i].con_tag)) {
							if (tmpValue.indexOf("'") >= 0) {
								whereStr += tmpValue;
							} else {
								String[] arrV = tmpValue.split(",");
								whereStr += StringTool.changArrToStr(arrV, "'");
							}
						} else {
							if (tmpValue.indexOf("'") >= 0)
								whereStr += tmpValue;
							else
								whereStr += "'" + tmpValue + "'";
						}
					} else {
						whereStr += tmpValue;
					}

					if ("in".equals(contables[i].con_tag))
						whereStr += ")";
				}

			}

		}
		// 环比
		if ("Y".equals(ctable.dolast.toUpperCase())) {

			for (int i = 0; contables != null && i < contables.length; i++) {

				String tmpCode = contables[i].qry_field_code.toLowerCase();
				String tmpValue = ReflectUtil.getStringFromObj(qry, tmpCode);
				if (null != tmpValue && !"".equals(tmpValue)) {
					if ("4".equals(contables[i].con_code_type)) {
						whereStr_last += tmpValue;
					} else {
						// 本身可判断con_code_type=2
						if ("GATHER_DAY".equals(contables[i].table_field_code
								.toUpperCase())
								&& "2".equals(contables[i].con_code_type)) {
							tmpValue = DateUtil.getDiffDay(-1, tmpValue);
						}
						// 本身可判断con_code_type=3
						if ("GATHER_MON".equals(contables[i].table_field_code
								.toUpperCase())
								&& "3".equals(contables[i].con_code_type)) {
							tmpValue = DateUtil.getDiffMonth(-1, tmpValue);
						}

						whereStr_last += " and "
								+ contables[i].table_field_code + " "
								+ contables[i].con_tag;
						if ("in".equals(contables[i].con_tag)) {
							whereStr_last += "(";
						}

						if ("0".equals(contables[i].con_code_type)) {// 字符型
							if ("in".equals(contables[i].con_tag)) {
								if (tmpValue.indexOf("'") >= 0) {
									whereStr_last += tmpValue;
								} else {
									String[] arrV = tmpValue.split(",");
									whereStr_last += StringTool.changArrToStr(
											arrV, "'");
								}
							} else {
								if (tmpValue.indexOf("'") >= 0)
									whereStr_last += tmpValue;
								else
									whereStr_last += "'" + tmpValue + "'";
							}
						} else {
							whereStr_last += tmpValue;
						}

						if ("in".equals(contables[i].con_tag))
							whereStr_last += ")";
					}

				}
			}

		}

		// 同比
		if ("Y".equals(ctable.doloop.toUpperCase())) {
			for (int i = 0; contables != null && i < contables.length; i++) {

				String tmpCode = contables[i].qry_field_code.toLowerCase();
				String tmpValue = ReflectUtil.getStringFromObj(qry, tmpCode);
				if (null != tmpValue && !"".equals(tmpValue)) {

					if ("4".equals(contables[i].con_code_type)) {
						whereStr_loop += tmpValue;
					} else {
						if ("GATHER_DAY".equals(contables[i].table_field_code
								.toUpperCase())
								&& "2".equals(contables[i].con_code_type)) {
							tmpValue = getLoopDay(tmpValue);
						}

						if ("GATHER_MON".equals(contables[i].table_field_code
								.toUpperCase())
								&& "3".equals(contables[i].con_code_type)) {
							tmpValue = getLoopMonth(tmpValue);
						}

						whereStr_loop += " and "
								+ contables[i].table_field_code + " "
								+ contables[i].con_tag;
						if ("in".equals(contables[i].con_tag)) {
							whereStr_loop += "(";
						}

						if ("0".equals(contables[i].con_code_type)) {// 字符型
							if ("in".equals(contables[i].con_tag)) {
								if (tmpValue.indexOf("'") >= 0) {
									whereStr_loop += tmpValue;
								} else {
									String[] arrV = tmpValue.split(",");
									whereStr_loop += StringTool.changArrToStr(
											arrV, "'");
								}
							} else {
								if (tmpValue.indexOf("'") >= 0)
									whereStr_loop += tmpValue;
								else
									whereStr_loop += "'" + tmpValue + "'";
							}
						} else {
							whereStr_loop += tmpValue;
						}

						if ("in".equals(contables[i].con_tag))
							whereStr_loop += ")";
					}

				}
			}

		}

		// 条件
		strsql = StringB.replace(strsql, "#?#", whereStr);
		if ("Y".equals(ctable.dolast.toUpperCase())) {
			strsql = StringB.replace(strsql, "#last#", whereStr_last);
		}
		if ("Y".equals(ctable.doloop.toUpperCase())) {
			strsql = StringB.replace(strsql, "#loop#", whereStr_loop);
		}
		// 数据源表
		strsql = StringB.replace(strsql, "#DATA_TABLE#", qry.datasource_table);
		// 指标
		if (!"".equals(ctable.chart_parttern) && ctable.chart_parttern != null) {
			strsql = StringB.replace(strsql, "?", ctable.chart_parttern);
		} else {
			strsql = StringB.replace(strsql, "?", "nvl(sum(" + qry.msu_id
					+ "),0)");
		}

		// 替换纬度
		if (strsql.indexOf("#DIM#") >= 0) {
			if ("REGION".equals(qry.dim_col.toUpperCase())) {
				if ("1".equals(qry.region_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "CITY_ID");
					strsql = StringB.replace(strsql, "#DESC#", "CITY_DESC");
				} else if ("2".equals(qry.region_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "CITY_ID");
					strsql = StringB.replace(strsql, "#DESC#", "CITY_DESC");
				} else if ("3".equals(qry.region_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "COUNTY_ID");
					strsql = StringB.replace(strsql, "#DESC#", "COUNTY_DESC");
				} else if ("4".equals(qry.region_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "SEC_AREA_ID");
					strsql = StringB.replace(strsql, "#DESC#", "SECAREA_DESC");
				} else if ("5".equals(qry.region_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "AREA_ID");
					strsql = StringB.replace(strsql, "#DESC#", "AREA_DESC");
				}
			} else if ("BRAND".equals(qry.dim_col.toUpperCase())) {
				strsql = StringB.replace(strsql, "#DIM#", "BRAND_KND");
				strsql = StringB.replace(strsql, "#DESC#", "BRANDKND_DESC");
			} else if ("PRODUCT".equals(qry.dim_col.toUpperCase())) {
				if ("0".equals(qry.product_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "BRAND_CODE");
					strsql = StringB
							.replace(strsql, "#DESC#", "BRANDCODE_DESC");
				} else if ("1".equals(qry.product_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "BRAND_CODE");
					strsql = StringB
							.replace(strsql, "#DESC#", "BRANDCODE_DESC");
				} else if ("2".equals(qry.product_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "SUB_BRAND_KND");
					strsql = StringB.replace(strsql, "#DESC#",
							"SUBBRANDKND_DESC");
				} else if ("3".equals(qry.product_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "PRODUCT");
					strsql = StringB.replace(strsql, "#DESC#", "COMMENTS");
				}
			} else if ("CHANNEL".equals(qry.dim_col.toUpperCase())) {
				if ("0".equals(qry.channel_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "CHANNEL_TYPE");
					strsql = StringB.replace(strsql, "#DESC#",
							"CHANNELTYPE_DESC");
				} else if ("1".equals(qry.channel_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "CHANNEL_TYPE");
					strsql = StringB.replace(strsql, "#DESC#",
							"CHANNELTYPE_DESC");
				} else if ("2".equals(qry.channel_level)) {
					strsql = StringB.replace(strsql, "#DIM#",
							"SUB_CHANNEL_TYPE");
					strsql = StringB.replace(strsql, "#DESC#",
							"SUBCHANNELTYPE_DESC");
				} else if ("3".equals(qry.channel_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "CHANNEL_ID");
					strsql = StringB.replace(strsql, "#DESC#", "CHANNEL_DESC");
				}

			} else if ("SALE_TYPE".equals(qry.dim_col.toUpperCase())) {
				strsql = StringB.replace(strsql, "#DIM#", "SALE_TYPE");
				strsql = StringB.replace(strsql, "#DESC#", "SALETYPE_DESC");
			} else if ("MAC_TYPE".equals(qry.dim_col.toUpperCase())) {
				strsql = StringB.replace(strsql, "#DIM#", "MAC_TYPE");
				strsql = StringB.replace(strsql, "#DESC#", "MACTYPE_DESC");
			} else if ("SVC_KND".equals(qry.dim_col.toUpperCase())) {
				if ("0".equals(qry.svc_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "SVC_KND_LVL");
					strsql = StringB
							.replace(strsql, "#DESC#", "SVCKNDLVL_DESC");
				} else if ("1".equals(qry.svc_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "SVC_KND_LVL");
					strsql = StringB
							.replace(strsql, "#DESC#", "SVCKNDLVL_DESC");
				} else if ("2".equals(qry.svc_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "VPREPAY_KND");
					strsql = StringB.replace(strsql, "#DESC#",
							"VPREPAYKND_DESC");
				} else if ("3".equals(qry.svc_level)) {
					strsql = StringB.replace(strsql, "#DIM#", "SVC_KND");
					strsql = StringB.replace(strsql, "#DESC#", "SVCKND_DESC");
				}

			} else if ("VIP".equals(qry.dim_col.toUpperCase())) {
				strsql = StringB.replace(strsql, "#DIM#", "VIP_LEVEL");
				strsql = StringB.replace(strsql, "#DESC#", "VIPLEVEL_DESC");
			} else if ("DURA".equals(qry.dim_col.toUpperCase())) {
				strsql = StringB.replace(strsql, "#DIM#", "ONL_DURA");
				strsql = StringB.replace(strsql, "#DESC#", "ONLDURA_DESC");
			}
		}
		// 替换日期
		if (strsql.indexOf("#DAY#") >= 0) {
			strsql = StringB.replace(strsql, "#DAY#", "GATHER_DAY");
		}
		if (strsql.indexOf("#MON#") >= 0) {
			strsql = StringB.replace(strsql, "#MON#", "GATHER_MON");
		}
		return genererData(strsql);
	}

	/**
	 * 获取数据
	 * 
	 * @param strsql
	 * @return
	 */
	public static String[][] genererData(String strsql) {
		String[][] result = null;
		System.out.println("strsql======================" + strsql);
		try {
			result = WebDBUtil.execQryArray(strsql, "");
		} catch (Exception ex) {
			result = null;
		}
		return result;
	}

	/**
	 * 取得日期天的同期值
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getLoopDay(String dateStr) {
		return DateUtil.getDiffMonth(-1, dateStr.substring(0, 6))
				+ dateStr.substring(6, 8);
	}

	/**
	 * 取得日期月的同期值
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getLoopMonth(String dateStr) {

		return DateUtil.getDiffMonth(-12, dateStr.substring(0, 6));

	}

}
