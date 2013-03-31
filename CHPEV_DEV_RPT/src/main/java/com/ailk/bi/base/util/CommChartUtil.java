package com.ailk.bi.base.util;

import java.awt.Color;
import java.awt.Font;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.PubInfoChartDefTable;
import com.ailk.bi.base.table.PubInfoChartFunTable;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.taglib.flashchart.ChartConsts;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.chart.CreateChartObj;
import com.ailk.bi.common.chart.WebChart;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.subject.util.SubjectConst;

@SuppressWarnings({ "rawtypes" })
public class CommChartUtil {

	private static String condition_part = TableConsts.CONDITION_PART;

	/**
	 * 获取图形
	 *
	 * @param chart_id
	 * @param chart_name
	 * @param qryStruct
	 * @param request
	 * @param pw
	 * @throws AppException
	 */
	public static String genererChart(String chart_id, String chart_name, Object qryStruct,
			HttpServletRequest request, PrintWriter pw) throws AppException {
		PubInfoChartDefTable chartDef = getChartDef(chart_id);
		if (chartDef == null) {
			return "<center><font color=red>未定义</font></center>";
		}
		String[][] data = genChartData(chartDef, request, qryStruct);
		return genererChart(chartDef, chart_name, data, request, pw);
	}

	/**
	 * 获取图形
	 *
	 * @param chart_id
	 * @param chart_name
	 * @param data
	 * @param request
	 * @param pw
	 * @throws AppException
	 */
	public static String genererChart(String chart_id, String chart_name, String[][] data,
			HttpServletRequest request, PrintWriter pw) throws AppException {
		PubInfoChartDefTable chartDef = getChartDef(chart_id);
		if (chartDef == null) {
			return "<center><font color=red>未定义</font></center>";
		}
		return genererChart(chartDef, chart_name, data, request, pw);
	}

	/**
	 * 获取图形
	 *
	 * @param chartDef
	 * @param chart_name
	 * @param data
	 * @param request
	 * @param pw
	 * @return
	 */
	public static String genererChart(PubInfoChartDefTable chartDef, String chart_name,
			String[][] data, HttpServletRequest request, PrintWriter pw) {
		if (chartDef == null) {
			return "<center><font color=red>未定义</font></center>";
		}
		// 生成图形对象
		WebChart chart = genChartObj(chartDef, data);
		// 设置图形属性
		setChartAttribute(chart, chartDef.chart_attribute);
		// 设置图形标题
		chart.setTitle(chart_name + " " + chart.getTitle());

		return genChartHTML(chartDef, chart, request, pw);
	}

	/**
	 * 获取指定ID的图形信息
	 *
	 * @param chart_id
	 * @return
	 * @throws AppException
	 */
	public static PubInfoChartDefTable getChartDef(String chart_id) throws AppException {
		PubInfoChartDefTable chartDef = null;
		String sql = SQLGenator.genSQL("Q4700", chart_id);
		// System.out.println("Q4700=" + sql);
		Vector result = WebDBUtil.execQryVector(sql, "");
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			chartDef = new PubInfoChartDefTable();
			int m = 0;
			chartDef.chart_id = (String) tempv.get(m++);
			chartDef.chart_type = (String) tempv.get(m++);
			chartDef.chart_index = (String) tempv.get(m++);
			chartDef.chart_attribute = (String) tempv.get(m++);
			chartDef.sql_main = (String) tempv.get(m++);
			chartDef.sql_where = (String) tempv.get(m++);
			chartDef.sql_order = (String) tempv.get(m++);
			chartDef.isusecd = (String) tempv.get(m++);
			chartDef.category_index = (String) tempv.get(m++);
			chartDef.series_index = (String) tempv.get(m++);
			chartDef.series_length = (String) tempv.get(m++);
			chartDef.series_cut = (String) tempv.get(m++);
			chartDef.series_cut_len = (String) tempv.get(m++);
			chartDef.value_index = (String) tempv.get(m++);
			chartDef.category_desc = (String) tempv.get(m++);
			chartDef.category_desc_index = (String) tempv.get(m++);
			chartDef.chart_distype = (String) tempv.get(m++);
			chartDef.series_attribute = (String) tempv.get(m++);
		}
		return chartDef;
	}

	/**
	 * 获取指定ID的图形信息
	 *
	 * @param chart_id
	 * @return
	 * @throws AppException
	 */
	public static PubInfoChartDefTable getFusionChartDef(String chart_id, String type)
			throws AppException {
		PubInfoChartDefTable chartDef = null;
		String sql = SQLGenator.genSQL("Q4700X", chart_id, type, type);
		// System.out.println("Q4700X=" + sql);
		Vector result = WebDBUtil.execQryVector(sql, "");
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			chartDef = new PubInfoChartDefTable();
			int m = 0;
			chartDef.chart_id = (String) tempv.get(m++);
			chartDef.chart_type = (String) tempv.get(m++);
			chartDef.chart_index = (String) tempv.get(m++);
			chartDef.chart_attribute = (String) tempv.get(m++);
			chartDef.sql_main = (String) tempv.get(m++);
			chartDef.sql_where = (String) tempv.get(m++);
			chartDef.sql_order = (String) tempv.get(m++);
			chartDef.isusecd = (String) tempv.get(m++);
			chartDef.category_index = (String) tempv.get(m++);
			chartDef.series_index = (String) tempv.get(m++);
			chartDef.series_length = (String) tempv.get(m++);
			chartDef.series_cut = (String) tempv.get(m++);
			chartDef.series_cut_len = (String) tempv.get(m++);
			chartDef.value_index = (String) tempv.get(m++);
			chartDef.category_desc = (String) tempv.get(m++);
			chartDef.category_desc_index = (String) tempv.get(m++);
			chartDef.chart_distype = (String) tempv.get(m++);
			chartDef.chart_code = (String) tempv.get(m++);
			chartDef.series_attribute = (String) tempv.get(m++);
		}
		return chartDef;
	}

	/**
	 * 获取指定ID的图形信息
	 *
	 * @param chart_id
	 * @return
	 * @throws AppException
	 */
	public static PubInfoChartDefTable[] getChartDefList(String where) throws AppException {
		PubInfoChartDefTable[] chartDef = null;
		String sql = SQLGenator.genSQL("Q4701", where);
		System.out.println("Q4701=====" + sql);
		Vector result = WebDBUtil.execQryVector(sql, "");
		if (result != null && result.size() > 0) {
			chartDef = new PubInfoChartDefTable[result.size()];
		}
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			chartDef[i] = new PubInfoChartDefTable();
			int m = 0;
			chartDef[i].chart_id = (String) tempv.get(m++);
			chartDef[i].chart_belong = (String) tempv.get(m++);
			chartDef[i].chart_type = (String) tempv.get(m++);
			chartDef[i].chart_index = (String) tempv.get(m++);
			chartDef[i].chart_attribute = (String) tempv.get(m++);
			chartDef[i].sql_main = (String) tempv.get(m++);
			chartDef[i].sql_where = (String) tempv.get(m++);
			chartDef[i].sql_order = (String) tempv.get(m++);
			chartDef[i].isusecd = (String) tempv.get(m++);
			chartDef[i].category_index = (String) tempv.get(m++);
			chartDef[i].series_index = (String) tempv.get(m++);
			chartDef[i].series_length = (String) tempv.get(m++);
			chartDef[i].series_cut = (String) tempv.get(m++);
			chartDef[i].series_cut_len = (String) tempv.get(m++);
			chartDef[i].value_index = (String) tempv.get(m++);
			chartDef[i].category_desc = (String) tempv.get(m++);
			chartDef[i].category_desc_index = (String) tempv.get(m++);
			chartDef[i].chart_distype = (String) tempv.get(m++);
			chartDef[i].chart_code = (String) tempv.get(m++);
			chartDef[i].series_attribute = (String) tempv.get(m++);
			chartDef[i].chart_type_desc = (String) tempv.get(m++);
		}
		return chartDef;
	}

	/**
	 * 获取图形功能定义
	 *
	 * @param chart_id
	 * @return
	 * @throws AppException
	 */
	public static PubInfoChartFunTable[] getChartFunDef(String chart_id) throws AppException {
		PubInfoChartFunTable[] chartDef = null;
		String sql = SQLGenator.genSQL("Q4704", chart_id);
		System.out.println("Q4704=" + sql);
		Vector result = WebDBUtil.execQryVector(sql, "");
		if (result != null && result.size() > 0) {
			chartDef = new PubInfoChartFunTable[result.size()];
		}
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			chartDef[i] = new PubInfoChartFunTable();
			int m = 0;
			chartDef[i].chart_id = (String) tempv.get(m++);
			chartDef[i].frame_name = (String) tempv.get(m++);
			chartDef[i].chart_desc = (String) tempv.get(m++);
			chartDef[i].category_desc = (String) tempv.get(m++);
			chartDef[i].category_desc_index = (String) tempv.get(m++);
			chartDef[i].value_index = (String) tempv.get(m++);
			chartDef[i].wheresql = (String) tempv.get(m++);
			chartDef[i].replace_content = (String) tempv.get(m++);
			chartDef[i].is_checked = (String) tempv.get(m++);
			chartDef[i].col_sequence = (String) tempv.get(m++);
		}
		return chartDef;
	}

	/**
	 * 设置图形属性
	 *
	 * @param fchart
	 * @param chart_attribute
	 */
	public static void setChartAttribute(WebChart fchart, String chart_attribute) {
		if (fchart == null) {
			return;
		}
		String[] attributes = chart_attribute.split(";");
		for (int i = 0; attributes != null && i < attributes.length; i++) {
			String[] attribute = attributes[i].split(":");
			String name = attribute[0].toLowerCase();
			if (name == null || "".equals(name.trim())) {
				continue;
			}
			String value = "";
			if (attribute != null && attribute.length > 1) {
				value = attribute[1];
			}
			if ("title".equals(name)) {
				// 设置图形的标题
				fchart.setTitle(setString(value, ""));
			}
			if ("titlefont".equals(name)) {
				// 设置图形的标题字体
				fchart.setTitleFont(setFont(value));
			}
			if ("width".equals(name)) {
				// 设置图形的宽度
				fchart.setWidth(StringB.toInt(value, 350));
			}
			if ("height".equals(name)) {
				// 设置图形的高度
				fchart.setHeight(StringB.toInt(value, 200));
			}
			if ("backgroundcolor".equals(name)) {
				// 设置图形背景色
				fchart.setBackgroundColor(setColor(value));
			}
			if ("bordervisible".equals(name)) {
				// 设置图形边框是否可见
				fchart.setBorderVisible(setBoolean(value));
			}
			if ("bordercolor".equals(name)) {
				// 设置图形边框色
				fchart.setBorderColor(setColor(value));
			}
			if ("legend".equals(name)) {
				// 设置图形是否显示图例
				fchart.setLegendVisible(setBoolean(value));
			}
			if ("legendfont".equals(name)) {
				// 设置图形图例字体
				fchart.setLegendFont(setFont(value));
			}
			if ("legendborder".equals(name)) {
				// 设置图形图例边框宽度
				fchart.setLegendBorder(StringB.toDouble(value, 0));
			}
			if ("legendpos".equals(name)) {
				// 设置图形图例的位置,设置的值为top,bottom,left,right
				fchart.setLegendPos(setString(value, "bottom"));
			}
			if ("backgroundcolor_plot".equals(name)) {
				// 设置图表区域背景色
				fchart.setBackgroundColor_plot(setColor(value));
			}
			if ("alpha".equals(name)) {
				// 设置图例的透明度
				fchart.setAlpha(StringB.toFloat(value, 0.9F));
			}
			if ("plotorientation".equals(name)) {
				// 设置图表区域水平或垂直显示模式，设置的值为0水平显示，1垂直显示
				fchart.setChartPlotOrientation(StringB.toInt(value, 0));
			}
			if ("labelfont".equals(name)) {
				// 设置图表区域提示数值字体
				fchart.setLableFont(setFont(value));
			}
			if ("categorylabelpositions".equals(name)) {
				// 设置图表区域X轴刻度值显示角度
				fchart.setCateGoryLabelPositions(StringB.toDouble(value, 0));
			}
			if ("categoryaxisfont".equals(name)) {
				// 设置图表区域X轴刻度值字体
				fchart.setCategoryAxisFont(setFont(value));
			}
			if ("numberaxisfont".equals(name)) {
				// 设置图表区域Y轴刻度值字体
				fchart.setNumberAxisFont(setFont(value));
			}
			if ("gridlinecolor".equals(name)) {
				// 设置图表区域刻度线颜色
				fchart.setGridlineColor(setColor(value));
			}
			if ("gridlinesdomain".equals(name)) {
				// 设置是否显示横坐标刻度线
				fchart.setGridlinesDomainVisible(setBoolean(value));
			}
			if ("gridlinesrange".equals(name)) {
				// 设置是否显示纵坐标刻度线
				fchart.setGridlinesRangeVisible(setBoolean(value));
			}
			if ("xaxisinfo".equals(name)) {
				// 设置图表区域X轴描述
				fchart.setChartXInfo(setString(value, ""));
			}
			if ("yaxisinfo".equals(name)) {
				// 设置图表区域Y轴描述
				fchart.setChartYInfo(setString(value, ""));
			}
			if ("yaxisinfo_extend".equals(name)) {
				// 设置图表区域扩展Y轴描述
				fchart.setChartYInfoExtend(setString(value, ""));
			}
			if ("piebordercolor".equals(name)) {
				// 设置饼状图形外框颜色
				fchart.setPieBorderColor(setColor(value));
			}
			if ("piecircular".equals(name)) {
				// 设置饼状图形是否为圆形
				fchart.setPieCircular(setBoolean(value));
			}
			if ("pielegend".equals(name)) {
				// 设置饼状图形的图例显示，设置的值为{0}显示名称，{1}显示数值，{2}显示百分比
				fchart.setPieLegend(setString(value, "{0}"));
			}
			if ("pielinks".equals(name)) {
				// 设置是否显示饼状图形提示连线
				fchart.setPieLinksVisible(setBoolean(value));
			}
			if ("pielinkscolor".equals(name)) {
				// 设置饼状图形提示连线颜色
				fchart.setPieLinksColor(setColor(value));
			}
			if ("pielinkslegend".equals(name)) {
				// 设置饼状图形提示连线数值显示，设置的值为{0}显示名称，{1}显示数值，{2}显示百分比
				fchart.setPieLinksLegend(setString(value, "{1}"));
			}
			if ("barlegend".equals(name)) {
				// 设置柱图的图例显示，设置的值为{0}显示名称，{2}显示数值，{3}显示百分比
				fchart.setBarLegend(setString(value, ""));
			}
			if ("baritemmargin".equals(name)) {
				// 设置柱状图柱子间的间距
				fchart.setBarItemMargin(StringB.toDouble(value, 0.1D));
			}
			if ("barmaxwidth".equals(name)) {
				// 设置柱状图柱子的最大宽度
				fchart.setBarMaxWidth(StringB.toDouble(value, 0.075D));
			}
			if ("baroutline".equals(name)) {
				// 设置柱状图是否显示边框
				fchart.setBarOutlineVisible(setBoolean(value));
			}
			if ("bartranstype".equals(name)) {
				// 设置柱图渐变颜色方向，设置的值为1垂直，2水平，3垂直居中，4水平居中
				fchart.setBarTransType(StringB.toInt(value, 1));
			}
			if ("baroutline".equals(name)) {
				// 设置柱状图是否显示边框
				fchart.setBarOutlineVisible(setBoolean(value));
			}
			if ("linestroke0".equals(name)) {
				// 设置线性图第一序列是否用虚线显示
				fchart.setLineStroke0(setBoolean(value));
			}
			if ("linestroke1".equals(name)) {
				// 设置线性图第二序列是否用虚线显示
				fchart.setLineStroke1(setBoolean(value));
			}
			if ("linestroke2".equals(name)) {
				// 设置线性图第三序列是否用虚线显示
				fchart.setLineStroke2(setBoolean(value));
			}
			if ("linestroke3".equals(name)) {
				// 设置线性图第四序列是否用虚线显示
				fchart.setLineStroke3(setBoolean(value));
			}
			if ("linestroke4".equals(name)) {
				// 设置线性图第五序列是否用虚线显示
				fchart.setLineStroke4(setBoolean(value));
			}
			if ("linestroke5".equals(name)) {
				// 设置线性图第六序列是否用虚线显示
				fchart.setLineStroke5(setBoolean(value));
			}
			if ("linestroke6".equals(name)) {
				// 设置线性图第七序列是否用虚线显示
				fchart.setLineStroke6(setBoolean(value));
			}
			if ("linestroke7".equals(name)) {
				// 设置线性图第八序列是否用虚线显示
				fchart.setLineStroke7(setBoolean(value));
			}
			if ("linestroke8".equals(name)) {
				// 设置线性图第九序列是否用虚线显示
				fchart.setLineStroke8(setBoolean(value));
			}
			if ("linestroke9".equals(name)) {
				// 设置线性图第十序列是否用虚线显示
				fchart.setLineStroke9(setBoolean(value));
			}
			if ("includeszero".equals(name)) {
				// 设置线性图是否显示0值点
				fchart.setIncludesZero(setBoolean(value));
			}
			if ("synchroaxis".equals(name)) {
				// 设置双轴图是否同步Y轴最大值高度
				fchart.setSynchroAxis(setBoolean(value));
			}
			if ("upwight".equals(name)) {
				// 设置对比图上图形占比
				fchart.setUpWight(StringB.toInt(value, 1));
			}
			if ("downwight".equals(name)) {
				// 设置对比图下图形占比
				fchart.setDownWight(StringB.toInt(value, 1));
			}
			if ("rangename".equals(name)) {
				// 设置特殊设定目标名称
				fchart.setRangeName(setString(value, ""));
			}
			if ("toprange".equals(name)) {
				// 设置特殊设定目标上限值
				fchart.setTopRange(StringB.toDouble(value, 0));
			}
			if ("bottomrange".equals(name)) {
				// 设置特殊设定目标下限值
				fchart.setBottomRange(StringB.toInt(value, 0));
			}
			if ("usetranscolor".equals(name)) {
				// 设置是否渐变色
				fchart.setUseTransColor(setBoolean(value));
			}
			if ("color0".equals(name)) {
				// 设置第一序列色彩
				fchart.setChartColor0(setColor(value));
			}
			if ("transcolor0".equals(name)) {
				// 设置第一序列渐变色彩
				fchart.setChartTransColor0(setColor(value));
			}
			if ("color1".equals(name)) {
				// 设置第二序列色彩
				fchart.setChartColor1(setColor(value));
			}
			if ("transcolor1".equals(name)) {
				// 设置第二序列渐变色彩
				fchart.setChartTransColor1(setColor(value));
			}
			if ("color2".equals(name)) {
				// 设置第三序列色彩
				fchart.setChartColor2(setColor(value));
			}
			if ("transcolor2".equals(name)) {
				// 设置第三序列渐变色彩
				fchart.setChartTransColor2(setColor(value));
			}
			if ("color3".equals(name)) {
				// 设置第四序列色彩
				fchart.setChartColor3(setColor(value));
			}
			if ("transcolor3".equals(name)) {
				// 设置第四序列渐变色彩
				fchart.setChartTransColor3(setColor(value));
			}
			if ("color4".equals(name)) {
				// 设置第五序列色彩
				fchart.setChartColor4(setColor(value));
			}
			if ("transcolor4".equals(name)) {
				// 设置第五序列渐变色彩
				fchart.setChartTransColor4(setColor(value));
			}
			if ("color5".equals(name)) {
				// 设置第六序列色彩
				fchart.setChartColor5(setColor(value));
			}
			if ("transcolor5".equals(name)) {
				// 设置第六序列渐变色彩
				fchart.setChartTransColor5(setColor(value));
			}
			if ("color6".equals(name)) {
				// 设置第七序列色彩
				fchart.setChartColor6(setColor(value));
			}
			if ("transcolor6".equals(name)) {
				// 设置第七序列渐变色彩
				fchart.setChartTransColor6(setColor(value));
			}
			if ("color7".equals(name)) {
				// 设置第八序列色彩
				fchart.setChartColor7(setColor(value));
			}
			if ("transcolor7".equals(name)) {
				// 设置第八序列渐变色彩
				fchart.setChartTransColor7(setColor(value));
			}
			if ("color8".equals(name)) {
				// 设置第九序列色彩
				fchart.setChartColor8(setColor(value));
			}
			if ("transcolor8".equals(name)) {
				// 设置第九序列渐变色彩
				fchart.setChartTransColor8(setColor(value));
			}
			if ("color9".equals(name)) {
				// 设置第十序列色彩
				fchart.setChartColor9(setColor(value));
			}
			if ("transcolor9".equals(name)) {
				// 设置第十序列渐变色彩
				fchart.setChartTransColor9(setColor(value));
			}

			// fusionchart 添加
			if ("configid".equals(name)) {
				fchart.setConfigId(value);
			}

			// fusionchart 添加
			if ("xaxis".equals(name)) {
				fchart.setXAxis(value);
			}

			// xAxis 添加
			if ("yaxis".equals(name)) {
				fchart.setYAxis(value);
			}

			// fusionchart 添加
			if ("xaxisname".equals(name)) {
				fchart.setXAxisName(value);
			}

			// xAxis 添加
			if ("yaxisname".equals(name)) {
				fchart.setYAxisName(value);
			}

			// lab,javascript链接
			if ("lablink".equals(name)) {
				fchart.setLablink(value);
			}
		}
	}

	/**
	 * 获取图形数据
	 *
	 * @param chartDef
	 * @param request
	 * @param qry
	 * @return
	 * @throws AppException
	 */
	public static String[][] genChartData(PubInfoChartDefTable chartDef,
			HttpServletRequest request, Object qryStruct) throws AppException {
		// 获取图形条件信息
		String wheresql = CommConditionUtil.getChartWhere(chartDef, request, qryStruct);
		// 定义查询语句
		String strSql = chartDef.sql_main + wheresql + " " + chartDef.sql_order;
		System.out.println("chart data sql=" + strSql);
		String[][] result = null;
		try {
			result = WebDBUtil.execQryArray(strSql, "");
		} catch (Exception ex) {
			result = null;
		}
		return result;
	}

	/**
	 * 获取专题通用图形数据
	 *
	 * @param chartDef
	 * @param request
	 * @param qry
	 * @return
	 * @throws AppException
	 */
	public static String[][] genSubjectChartData(PubInfoChartDefTable chartDef,
			HttpServletRequest request, Object qryStruct, HttpSession session) throws AppException {
		// 获取图形条件信息
		String wheresql = CommConditionUtil.getChartWhere(chartDef, request, qryStruct);
		System.out.println("wheresql=" + wheresql);
		// 定义查询语句
		String strSql = "";
		// 日报显示上月同期曲线，月报显示去年同期曲线
		if (chartDef.sql_main.indexOf("&6&") >= 0 || chartDef.sql_main.indexOf("&4&") >= 0) {
			String dateLevel = SubjectConst.DATA_DAY_LEVEL;
			if (chartDef.sql_main.indexOf("&4&") >= 0) {
				dateLevel = SubjectConst.DATA_MONTH_LEVEL;
				chartDef.sql_main = StringB.replace(chartDef.sql_main, "&4&", "").toUpperCase();
			} else if (chartDef.sql_main.indexOf("&6&") >= 0) {
				dateLevel = SubjectConst.DATA_DAY_LEVEL;
				chartDef.sql_main = StringB.replace(chartDef.sql_main, "&6&", "").toUpperCase();
			}
			chartDef.sql_order = chartDef.sql_order.toUpperCase();

			// 处理group by表达式
			String[] temp = chartDef.sql_order.split("ORDER BY ");
			temp[1] = StringB.replace(temp[1], "A.", "C.");
			String strGroupBy = temp[0].replace("GROUP BY ", "");
			strGroupBy = strGroupBy.replace("A.", "").trim();
			String[] groupBy = strGroupBy.split(",");
			String GroupBy = "";
			int i = 0;
			for (i = 0; i < groupBy.length; i++) {
				if (GroupBy.length() > 0) {
					GroupBy += " and";
				}
				GroupBy += " A." + groupBy[i] + "=C." + groupBy[i];
			}
			// 日期处理
			String dateStr = "case when C." + groupBy[0] + " is null then A." + groupBy[0]
					+ " else C." + groupBy[0] + " end";

			String tempWhere = wheresql.toUpperCase().replace("WHERE ", "");
			tempWhere = tempWhere.replace("1=1 AND ", "");
			String[] where = tempWhere.split("AND ");
			String[] dateSql = new String[] { "", "" };
			String[] other = new String[] { "", "" };
			for (i = 0; i < where.length; i++) {
				String[] sign = new String[] { "<=", ">=", "=", "<>", "IN", "LIKE" };
				String[] tmp;

				for (int j = 0; j < sign.length; j++) {
					tmp = where[i].replace("A.", "").split(sign[j]);

					if (tmp.length > 1) {
						String date = tmp[1].trim();
						if (groupBy[0].equals(tmp[0].trim())) {
							dateSql[0] = " and " + groupBy[0] + ">="
									+ date.substring(0, date.length() - 2) + "01 and " + groupBy[0]
									+ "<=" + date;
							if (dateLevel.equals(SubjectConst.DATA_DAY_LEVEL)) {
								dateSql[1] = " and " + SubjectConst.DATA_TABLE_TMP_VIR_NAME + "."
										+ SubjectConst.TIME_DIM_DAY_FLD + ">="
										+ date.substring(0, date.length() - 2) + "01 and "
										+ SubjectConst.DATA_TABLE_TMP_VIR_NAME + "."
										+ SubjectConst.TIME_DIM_DAY_FLD + "<="
										+ date.substring(0, date.length() - 2) + "31 and A."
										+ groupBy[0] + "=" + SubjectConst.DATA_TABLE_TMP_VIR_NAME
										+ "." + SubjectConst.TIME_DIM_SAME_DAY_FLD;
							} else {
								dateSql[1] = " and " + SubjectConst.DATA_TABLE_TMP_VIR_NAME + "."
										+ SubjectConst.TIME_DIM_MONTH_FLD + ">="
										+ date.substring(0, date.length() - 2) + "01 and "
										+ SubjectConst.DATA_TABLE_TMP_VIR_NAME + "."
										+ SubjectConst.TIME_DIM_MONTH_FLD + "<=" + tmp[1]
										+ " and A." + groupBy[0] + "="
										+ SubjectConst.DATA_TABLE_TMP_VIR_NAME + "."
										+ SubjectConst.TIME_DIM_SAME_MONTH_FLD;
							}
						} else {
							other[0] += " and " + where[i];
							other[1] += " and " + where[i];
						}
						break;
					}
				}

			}
			String sql1 = chartDef.sql_main + " where 1=1 " + dateSql[0] + other[0] + " "
					+ chartDef.sql_order;
			String str = chartDef.sql_main.substring(chartDef.sql_main.indexOf(",") + 1,
					chartDef.sql_main.length());
			String sql2 = "";
			if (dateLevel.equals(SubjectConst.DATA_DAY_LEVEL)) {
				sql2 = "select "
						+ SubjectConst.TIME_DIM_DAY_FLD
						+ " as "
						+ groupBy[0]
						+ ","
						+ str
						+ ","
						+ SubjectConst.TIME_DIM_DAY_TABLE
						+ " "
						+ SubjectConst.DATA_TABLE_TMP_VIR_NAME
						+ " where 1=1 "
						+ dateSql[1]
						+ other[1]
						+ " "
						+ chartDef.sql_order.replace("A.", "").replace(
								groupBy[0],
								SubjectConst.DATA_TABLE_TMP_VIR_NAME + "."
										+ SubjectConst.TIME_DIM_DAY_FLD);
			} else {
				sql2 = "select "
						+ SubjectConst.TIME_DIM_MONTH_FLD
						+ " as "
						+ groupBy[0]
						+ ","
						+ str
						+ ","
						+ SubjectConst.TIME_DIM_MONTH_TABLE
						+ " "
						+ SubjectConst.DATA_TABLE_TMP_VIR_NAME
						+ " where 1=1 "
						+ dateSql[1]
						+ other[1]
						+ " "
						+ chartDef.sql_order.replace("A.", "").replace(
								groupBy[0],
								SubjectConst.DATA_TABLE_TMP_VIR_NAME + "."
										+ SubjectConst.TIME_DIM_MONTH_FLD);
			}
			String select = str.substring(0, str.indexOf(" FROM "));
			String[] tempSelect = select.split(",");
			String[] sele = new String[] { "", "" };
			for (int k = 0; k < tempSelect.length; k++) {
				String[] arr = tempSelect[k].split(" AS ");
				sele[0] += " ,A." + arr[1];
				sele[1] += " ,C." + arr[1];
			}
			strSql = "select " + dateStr + sele[0] + sele[1] + " from (" + sql1 + ") A FULL JOIN ("
					+ sql2 + ") C on " + GroupBy + " order by " + dateStr;

		} else {
			strSql = chartDef.sql_main + wheresql + " " + chartDef.sql_order;
		}
		// 获取图形链接条件信息
		PubInfoConditionTable[] conTable = null;
		if (strSql.indexOf("?") >= 0) {
			try {
				conTable = CommConditionUtil.genCondition(chartDef.chart_id, condition_part);
			} catch (AppException e) {
				return null;
			}
			// 获取整合条件信息
			String partwhere = CommConditionUtil.getChartWhere(chartDef, conTable, request,
					qryStruct);
			String tmpcondition = (String) session.getAttribute("tmpcondition");
			partwhere += CommConditionUtil.getStringWhere(conTable, tmpcondition);
			// System.out.println("partwhere=" + partwhere);
			while (strSql.indexOf("?") >= 0) {
				strSql = StringB.replaceFirst(strSql, "?", partwhere);
			}
		}
		if (strSql.indexOf("#4#") >= 0) {
			try {
				conTable = CommConditionUtil.genCondition(chartDef.chart_id,
						TableConsts.CONDITION_PART_FOUR);
			} catch (AppException e) {
				return null;
			}
			// 获取整合条件信息
			String partwhere = CommConditionUtil.getChartWhere(chartDef, conTable, request,
					qryStruct);
			String tmpcondition = (String) session.getAttribute("tmpcondition");
			partwhere += CommConditionUtil.getStringWhere(conTable, tmpcondition);
			while (strSql.indexOf("#4#") >= 0) {
				strSql = StringB.replaceFirst(strSql, "#4#", partwhere);
			}
		}
		if (strSql.indexOf("#5#") >= 0) {
			try {
				conTable = CommConditionUtil.genCondition(chartDef.chart_id,
						TableConsts.CONDITION_PART_FIVE);
			} catch (AppException e) {
				return null;
			}
			// 获取整合条件信息
			String partwhere = CommConditionUtil.getChartWhere(chartDef, conTable, request,
					qryStruct);
			String tmpcondition = (String) session.getAttribute("tmpcondition");
			partwhere += CommConditionUtil.getStringWhere(conTable, tmpcondition);
			while (strSql.indexOf("#5#") >= 0) {
				strSql = StringB.replaceFirst(strSql, "#5#", partwhere);
			}
		}
		strSql = StringB.replace(strSql, "?", "");
		System.out.println("chart data sql=" + strSql);
		String[][] result = null;
		try {
			result = WebDBUtil.execQryArray(strSql, "");
		} catch (Exception ex) {
			result = null;
		}
		return result;
	}

	/**
	 * 生成图形对象
	 *
	 * @param chartDef
	 * @param data
	 * @return
	 */
	public static WebChart genChartObj(PubInfoChartDefTable chartDef, String[][] data) {
		WebChart chart = null;
		int ctype = StringB.toInt(chartDef.chart_type, 0); // 图形类型
		int cdtag = StringB.toInt(chartDef.isusecd, 0); // 获取分组描述定义

		int li_category = StringB.toInt(chartDef.category_index, 0); // 分组描述索引值
		int li_series = StringB.toInt(chartDef.series_index, 0); // X轴描述索引值
		String series_cut = chartDef.series_cut; // 是否对描述进行截取
		int series_cut_len = StringB.toInt(chartDef.series_cut_len, 0); // 描述截取的长度
		int li_value = StringB.toInt(chartDef.value_index, 0); // 数据字段索引值

		String category_desc = chartDef.category_desc.trim(); // 分组描述
		String[] pCategory = null; // 分组描述数组
		if (category_desc != null && !"".equals(category_desc)) {
			pCategory = category_desc.split(",");
		}
		String category_desc_index = chartDef.category_desc_index.trim(); // 分组描述索引
		String[] pCategoryIndex = null; // 分组描述数组索引
		if (category_desc_index != null && !"".equals(category_desc_index)) {
			pCategoryIndex = category_desc_index.split(",");
		}

		if (cdtag == 0) {
			// 0;默认(启用CATEGORY_INDEX,SERIES_INDEX,VALUE_INDEX三个字段)
			if (ctype >= 1 && ctype <= 3) {
				// 饼图
				chart = CreateChartObj.getChartObjPie(data, li_category, li_value);
			}
			if ((ctype >= 4 && ctype <= 14) || (ctype >= 18 && ctype <= 50)) {
				/*
				 * 以下是基本图形: 4-生成平面组合饼状图形,5-生成立体组合饼状图形,
				 * 6-生成平面簇状柱形图,7-生成立体簇状柱形图,8-生成平面堆积柱形图,9-生成立体堆积柱形图
				 * 10-生成平面面积图,11-生成堆积面积图,12-生成平面折线图 13-生成立体折线图, 14-生成雷达图
				 */
				/*
				 * 以下是双轴图: 18-生成平面簇状折线图,19-生成立体簇状折线图,20-生成平面堆积折线图,21-生成立体堆积折线图
				 * 22-25柱状对比图(一般不使用，不完善)
				 */
				/*
				 * 以下是双图对比图: 26-线线对比,27-线柱对比,28-线立体柱对比,29-线堆积对比,30-线立体堆积对比
				 * 31-35平面柱对比,36-40立体柱对比,41-45平面簇状对比,46-50立体簇状对比
				 */
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getChartObjCategory(data, li_category, li_series,
							li_value, series_cut_len, true);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getChartObjCategory(data, li_category, li_series,
							li_value, series_cut_len, false);
				} else {
					// N:不截取
					chart = CreateChartObj.getChartObjCategory(data, li_category, li_series,
							li_value);
				}
			}
		}
		if (cdtag == 1) {
			// 1:启用分组描述(启用CATEGORY_INDEX,SERIES_INDEX,VALUE_INDEX,CATEGORY_DESC四个字段)
			if (ctype >= 1 && ctype <= 3) {
				// 饼图
				chart = CreateChartObj.getChartObjPie(data, li_category, li_value);
			}
			if ((ctype >= 4 && ctype <= 14) || (ctype >= 18 && ctype <= 50)) {
				/*
				 * 以下是基本图形: 4-生成平面组合饼状图形,5-生成立体组合饼状图形,
				 * 6-生成平面簇状柱形图,7-生成立体簇状柱形图,8-生成平面堆积柱形图,9-生成立体堆积柱形图
				 * 10-生成平面面积图,11-生成堆积面积图,12-生成平面折线图 13-生成立体折线图, 14-生成雷达图
				 */
				/*
				 * 以下是双轴图: 18-生成平面簇状折线图,19-生成立体簇状折线图,20-生成平面堆积折线图,21-生成立体堆积折线图
				 * 22-25柱状对比图(一般不使用，不完善)
				 */
				/*
				 * 以下是双图对比图: 26-线线对比,27-线柱对比,28-线立体柱对比,29-线堆积对比,30-线立体堆积对比
				 * 31-35平面柱对比,36-40立体柱对比,41-45平面簇状对比,46-50立体簇状对比
				 */
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getChartObjCategory(data, li_category, li_series,
							li_value, pCategory, null, series_cut_len, true);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getChartObjCategory(data, li_category, li_series,
							li_value, pCategory, null, series_cut_len, false);
				} else {
					// N:不截取
					chart = CreateChartObj.getChartObjCategory(data, li_category, li_series,
							li_value, pCategory, null);
				}
			}
		}
		if (cdtag == 2) {
			// 2:启用分组描述和索引(启用SERIES_INDEX,CATEGORY_DESC,CATEGORY_DESC_INDEX三个字段)
			if (ctype >= 1 && ctype <= 3) {
				// 饼图
				chart = CreateChartObj.getChartObjPie(data, pCategory, pCategoryIndex);
			}
			if ((ctype >= 4 && ctype <= 14) || (ctype >= 18 && ctype <= 50)) {
				/*
				 * 以下是基本图形: 4-生成平面组合饼状图形,5-生成立体组合饼状图形,
				 * 6-生成平面簇状柱形图,7-生成立体簇状柱形图,8-生成平面堆积柱形图,9-生成立体堆积柱形图
				 * 10-生成平面面积图,11-生成堆积面积图,12-生成平面折线图 13-生成立体折线图, 14-生成雷达图
				 */
				/*
				 * 以下是双轴图: 18-生成平面簇状折线图,19-生成立体簇状折线图,20-生成平面堆积折线图,21-生成立体堆积折线图
				 * 22-25柱状对比图(一般不使用，不完善)
				 */
				/*
				 * 以下是双图对比图: 26-线线对比,27-线柱对比,28-线立体柱对比,29-线堆积对比,30-线立体堆积对比
				 * 31-35平面柱对比,36-40立体柱对比,41-45平面簇状对比,46-50立体簇状对比
				 */
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getChartObjCategory(data, pCategory, pCategoryIndex,
							li_series, series_cut_len, true);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getChartObjCategory(data, pCategory, pCategoryIndex,
							li_series, series_cut_len, false);
				} else {
					// N:不截取
					chart = CreateChartObj.getChartObjCategory(data, pCategory, pCategoryIndex,
							li_series);
				}
			}
		}
		return chart;
	}

	/**
	 * 生成图形对象
	 *
	 * @param chartDef
	 * @param data
	 * @return
	 */
	public static String[] genFlashChartObj(PubInfoChartDefTable chartDef, String[][] data,
			String type) {
		String[] chart = null;
		int cdtag = StringB.toInt(chartDef.isusecd, 0); // 获取分组描述定义

		int index_category = StringB.toInt(chartDef.category_index, 0); // 分组描述索引值
		int index_series = StringB.toInt(chartDef.series_index, 0); // X轴描述索引值
		String series_cut = chartDef.series_cut; // 是否对描述进行截取
		int series_cut_len = StringB.toInt(chartDef.series_cut_len, 0); // 描述截取的长度
		int index_value = StringB.toInt(chartDef.value_index, 0); // 数据字段索引值

		String category_desc = chartDef.category_desc.trim(); // 分组描述
		String[] pCategory = null; // 分组描述数组
		if (category_desc != null && !"".equals(category_desc)) {
			pCategory = category_desc.split(",");
		}
		String category_desc_index = chartDef.category_desc_index.trim(); // 分组描述索引
		String[] pCategoryIndex = null; // 分组描述数组索引
		if (category_desc_index != null && !"".equals(category_desc_index)) {
			pCategoryIndex = category_desc_index.split(",");
		}

		if (cdtag == 0) {
			// 0;默认(启用CATEGORY_INDEX,SERIES_INDEX,VALUE_INDEX三个字段)
			if (ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)) {
				// 饼图
				chart = CreateChartObj.getFlashChartObjPie(data, index_category, index_value, type,
						series_cut_len, series_cut);
			} else {
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getFlashChartObjCategory(data, index_category,
							index_series, index_value, series_cut_len, true, type);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getFlashChartObjCategory(data, index_category,
							index_series, index_value, series_cut_len, false, type);
				} else {
					// N:不截取
					chart = CreateChartObj.getFlashChartObjCategory(data, index_category,
							index_series, index_value, type);
				}
			}
		}
		if (cdtag == 1) {
			// 1:启用分组描述(启用CATEGORY_INDEX,SERIES_INDEX,VALUE_INDEX,CATEGORY_DESC四个字段)
			if (ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)) {
				// 饼图
				chart = CreateChartObj.getFlashChartObjPie(data, index_category, index_value, type,
						series_cut_len, series_cut);
			} else {
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getFlashChartObjCategory(data, index_category,
							index_series, index_value, pCategory, null, series_cut_len, true, type);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj
							.getFlashChartObjCategory(data, index_category, index_series,
									index_value, pCategory, null, series_cut_len, false, type);
				} else {
					// N:不截取
					chart = CreateChartObj.getFlashChartObjCategory(data, index_category,
							index_series, index_value, pCategory, null, type);
				}
			}
		}
		if (cdtag == 2) {
			// 2:启用分组描述和索引(启用SERIES_INDEX,CATEGORY_DESC,CATEGORY_DESC_INDEX三个字段)
			if (ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)) {
				// 饼图
				chart = CreateChartObj.getFlashChartObjPie(data, pCategory, pCategoryIndex, type,
						series_cut_len, series_cut);
			} else {
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getFlashChartObjCategory(data, pCategory,
							pCategoryIndex, index_series, series_cut_len, true, type);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getFlashChartObjCategory(data, pCategory,
							pCategoryIndex, index_series, series_cut_len, false, type);
				} else {
					// N:不截取
					chart = CreateChartObj.getFlashChartObjCategory(data, pCategory,
							pCategoryIndex, index_series, type);
				}
			}
		}
		return chart;
	}

	/**
	 * 生成图形对象
	 *
	 * @param chartDef
	 * @param data
	 * @return
	 */
	public static String[][] genFlashChartObjValue(PubInfoChartDefTable chartDef, String[][] data) {
		String[][] chart = null;
		int cdtag = StringB.toInt(chartDef.isusecd, 0); // 获取分组描述定义

		int li_category = StringB.toInt(chartDef.category_index, 0); // 分组描述索引值
		int li_series = StringB.toInt(chartDef.series_index, 0); // X轴描述索引值
		String series_cut = chartDef.series_cut; // 是否对描述进行截取
		int series_cut_len = StringB.toInt(chartDef.series_cut_len, 0); // 描述截取的长度
		int li_value = StringB.toInt(chartDef.value_index, 0); // 数据字段索引值

		String category_desc = chartDef.category_desc.trim(); // 分组描述
		String[] pCategory = null; // 分组描述数组
		if (category_desc != null && !"".equals(category_desc)) {
			pCategory = category_desc.split(",");
		}
		String category_desc_index = chartDef.category_desc_index.trim(); // 分组描述索引
		String[] pCategoryIndex = null; // 分组描述数组索引
		if (category_desc_index != null && !"".equals(category_desc_index)) {
			pCategoryIndex = category_desc_index.split(",");
		}

		if (cdtag == 0) {
			// 0;默认(启用CATEGORY_INDEX,SERIES_INDEX,VALUE_INDEX三个字段)
			if (ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)) {
				// 饼图
				chart = CreateChartObj.getFlashChartObjPieValue(data, li_series, li_category,
						li_value);
			} else {
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getFlashChartObjValue(data, li_category, li_series,
							li_value, series_cut_len, true);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getFlashChartObjValue(data, li_category, li_series,
							li_value, series_cut_len, false);
				} else {
					// N:不截取
					chart = CreateChartObj.getFlashChartObjValue(data, li_category, li_series,
							li_value);
				}
			}
		}
		if (cdtag == 1) {
			// 1:启用分组描述(启用CATEGORY_INDEX,SERIES_INDEX,VALUE_INDEX,CATEGORY_DESC四个字段)
			if (ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)) {
				// 饼图
				chart = CreateChartObj.getFlashChartObjPieValue(data, li_series, li_category,
						li_value);
			} else {
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getFlashChartObjValue(data, li_category, li_series,
							li_value, pCategory, null, series_cut_len, true);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getFlashChartObjValue(data, li_category, li_series,
							li_value, pCategory, null, series_cut_len, false);
				} else {
					// N:不截取
					chart = CreateChartObj.getFlashChartObjValue(data, li_category, li_series,
							li_value, pCategory, null);
				}
			}
		}
		if (cdtag == 2) {
			// 2:启用分组描述和索引(启用SERIES_INDEX,CATEGORY_DESC,CATEGORY_DESC_INDEX三个字段)
			if (ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)) {
				// 饼图
				chart = CreateChartObj.getFlashChartObjPieValue(data, pCategory, pCategoryIndex);
			} else {
				if ("F".equals(series_cut)) {
					// F:前截取
					chart = CreateChartObj.getFlashChartObjValue(data, pCategory, pCategoryIndex,
							li_series, series_cut_len, true);
				} else if ("B".equals(series_cut)) {
					// B:后截取
					chart = CreateChartObj.getFlashChartObjValue(data, pCategory, pCategoryIndex,
							li_series, series_cut_len, true);
				} else {
					// N:不截取
					chart = CreateChartObj.getFlashChartObjValue(data, pCategory, pCategoryIndex,
							li_series);
				}
			}
		}
		return chart;
	}

	/**
	 * 生成图片html代码
	 *
	 * @param chartDef
	 * @param chart
	 * @param request
	 * @param pw
	 * @return
	 */
	public static String genChartHTML(PubInfoChartDefTable chartDef, WebChart chart,
			HttpServletRequest request, PrintWriter pw) {
		// 图形类型
		int chart_type = StringB.toInt(chartDef.chart_type, 12);
		// 特殊索引标志
		int chart_index = StringB.toInt(chartDef.chart_index, 0);

		// 图片名称
		HttpSession session = request.getSession();
		String filename = CreateChartObj.getChartName(chart_type, chart, chart_index, session, pw);
		// URL链接
		String url = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
		return "<img src=" + url + " border=0 usemap=#" + filename + ">";
	}

	/**
	 * 设置字体
	 *
	 * @param font
	 * @return
	 */
	private static Font setFont(String font) {
		String[] tmpf = font.split(",");
		String p0 = tmpf[0];
		if (p0 == null || "".equals(p0.trim())) {
			p0 = "宋体";
		}
		int p1 = StringB.toInt(tmpf[1], 0);
		int p2 = StringB.toInt(tmpf[2], 12);
		return new Font(p0, p1, p2);
	}

	/**
	 * 设置颜色
	 *
	 * @param color
	 * @return
	 */
	private static Color setColor(String color) {
		String[] tmpf = color.split(",");
		int p0 = StringB.toInt(tmpf[0], 255);
		int p1 = StringB.toInt(tmpf[1], 255);
		int p2 = StringB.toInt(tmpf[2], 255);
		return new Color(p0, p1, p2);
	}

	/**
	 * 设置字符串
	 *
	 * @param p0
	 * @return
	 */
	private static String setString(String p0, String p1) {
		if ("null".equals(p0)) {
			p0 = p1;
		}
		return p0;
	}

	/**
	 * 返回真假
	 *
	 * @param p0
	 * @return
	 */
	private static boolean setBoolean(String p0) {
		if ("false".equals(p0))
			return false;
		else
			return true;
	}

	/**
	 * 清除图形session
	 *
	 * @param session
	 */
	public static void clearAllChartSession(HttpSession session) {
		session.removeAttribute("session_flag");
		session.removeAttribute("chart_name");
		session.removeAttribute("chart_name_r");
		session.removeAttribute("category_index");
		session.removeAttribute("category_desc");
		session.removeAttribute("tmpcondition");
	}
}
