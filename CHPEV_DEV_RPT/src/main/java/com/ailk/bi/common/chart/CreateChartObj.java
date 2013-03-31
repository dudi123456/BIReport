package com.ailk.bi.common.chart;

import java.io.PrintWriter;
import javax.servlet.http.HttpSession;

import com.ailk.bi.common.app.ArrayUtil;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.chart.ChartDataUtil;
import com.ailk.bi.common.chart.WebChart;

/**
 * 获取WebChart图形对象
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author renhui
 * @version 1.0
 */
public class CreateChartObj {
	private static String[] category = null; // 分组的列表

	private static String[] series = null; // 描述的列表

	private static double[][] value = null; // 数据

	// private static double[][] xValue = null; //X轴数据
	// private static double[][] yValue = null; //Y轴数据
	// private static double[][] zValue = null; //Z轴数据

	/**
	 * 生成图形
	 *
	 * @param chartType
	 *            1-平面饼状图形,2-生成立体饼状图形,3-生成指环状饼图形
	 *            4-生成平面组合饼状图形,5-生成立体组合饼状图形,6-生成平面簇状柱形图
	 *            7-生成立体簇状柱形图,8-生成平面堆积柱形图,9-生成立体堆积柱形图
	 *            10-生成平面面积图,11-生成堆积面积图,12-生成平面折线图 13-生成立体折线图,
	 *            14-生成雷达图,15-生成极面图,16-生成瀑布图,17-生成气泡图,18-生成平面簇状折线图
	 *            19-生成立体簇状折线图,20-生成平面堆积折线图,21-生成立体堆积折线图 22-25柱状对比图
	 *            26-线线对比,27-线柱对比,28-线立体柱对比,29-线堆积对比,30-线立体堆积对比
	 *            31-35平面柱对比,36-40立体柱对比,41-45平面簇状对比,46-50立体簇状对比
	 * @param chart
	 *            WebChart对象
	 * @param p0
	 *            特殊索引标志。1-3生成饼图的数据索引；18-29的第n个分组开始变折线
	 * @param chartName
	 *            图形名称
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param session
	 * @param pw
	 * @return
	 */
	public static String getChartName(int chartType, WebChart chart, int p0, String chartName,
			int width, int height, HttpSession session, PrintWriter pw) {
		String filename = null;
		if (session == null)
			return null;
		if (pw == null)
			return null;

		if (p0 < 0)
			p0 = 0;
		if (width <= 0)
			width = 350;
		if (height <= 0)
			height = 200;
		chart.setTitle(chartName);// 设置图形名称
		chart.setWidth(width);// 设置图形宽度
		chart.setHeight(height);// 设置图片高度
		switch (chartType) {
		case 1:
			filename = chart.generatePieChart(p0, session, pw);
			break;
		case 2:
			filename = chart.generatePie3DChart(p0, session, pw);
			break;
		case 3:
			filename = chart.generatePieRingChart(p0, session, pw);
			break;
		case 4:
			filename = chart.generateMultiplePieChart(session, pw);
			break;
		case 5:
			filename = chart.generateMultiplePie3DChart(session, pw);
			break;
		case 6:
			filename = chart.generateBarChart(session, pw);
			break;
		case 7:
			filename = chart.generateBar3DChart(session, pw);
			break;
		case 8:
			filename = chart.generateBarChartStacked(session, pw);
			break;
		case 9:
			filename = chart.generateBar3DChartStacked(session, pw);
			break;
		case 10:
			filename = chart.generateAreaChart(session, pw);
			break;
		case 11:
			filename = chart.generateAreaChartStacked(session, pw);
			break;
		case 12:
			filename = chart.generateLineChart(session, pw);
			break;
		case 13:
			filename = chart.generateLine3DChart(session, pw);
			break;
		case 14:
			filename = chart.generateSpiderChart(session, pw);
			break;
		case 15:
			filename = chart.generatePolarChart(session, pw);
			break;
		case 16:
			filename = chart.generateWaterfallChart(session, pw);
			break;
		case 17:
			filename = chart.generateBubbleChart(session, pw);
			break;
		case 18:
			filename = chart.generateDualAxisChart(p0, 0, session, pw);
			break;
		case 19:
			filename = chart.generateDualAxisChart(p0, 1, session, pw);
			break;
		case 20:
			filename = chart.generateDualAxisChart(p0, 2, session, pw);
			break;
		case 21:
			filename = chart.generateDualAxisChart(p0, 3, session, pw);
			break;
		case 22:
			filename = chart.generateDualAxisChart(p0, 10, session, pw);
			break;
		case 23:
			filename = chart.generateDualAxisChart(p0, 11, session, pw);
			break;
		case 24:
			filename = chart.generateDualAxisChart(p0, 12, session, pw);
			break;
		case 25:
			filename = chart.generateDualAxisChart(p0, 13, session, pw);
			break;
		case 26:
			filename = chart.generateCombinedChart(p0, 1, 1, session, pw);
			break;
		case 27:
			filename = chart.generateCombinedChart(p0, 1, 2, session, pw);
			break;
		case 28:
			filename = chart.generateCombinedChart(p0, 1, 3, session, pw);
			break;
		case 29:
			filename = chart.generateCombinedChart(p0, 1, 4, session, pw);
			break;
		case 30:
			filename = chart.generateCombinedChart(p0, 1, 5, session, pw);
			break;
		case 31:
			filename = chart.generateCombinedChart(p0, 2, 1, session, pw);
			break;
		case 32:
			filename = chart.generateCombinedChart(p0, 2, 2, session, pw);
			break;
		case 33:
			filename = chart.generateCombinedChart(p0, 2, 3, session, pw);
			break;
		case 34:
			filename = chart.generateCombinedChart(p0, 2, 4, session, pw);
			break;
		case 35:
			filename = chart.generateCombinedChart(p0, 2, 5, session, pw);
			break;
		case 36:
			filename = chart.generateCombinedChart(p0, 3, 1, session, pw);
			break;
		case 37:
			filename = chart.generateCombinedChart(p0, 3, 2, session, pw);
			break;
		case 38:
			filename = chart.generateCombinedChart(p0, 3, 3, session, pw);
			break;
		case 39:
			filename = chart.generateCombinedChart(p0, 3, 4, session, pw);
			break;
		case 40:
			filename = chart.generateCombinedChart(p0, 3, 5, session, pw);
			break;
		case 41:
			filename = chart.generateCombinedChart(p0, 4, 1, session, pw);
			break;
		case 42:
			filename = chart.generateCombinedChart(p0, 4, 2, session, pw);
			break;
		case 43:
			filename = chart.generateCombinedChart(p0, 4, 3, session, pw);
			break;
		case 44:
			filename = chart.generateCombinedChart(p0, 4, 4, session, pw);
			break;
		case 45:
			filename = chart.generateCombinedChart(p0, 4, 5, session, pw);
			break;
		case 46:
			filename = chart.generateCombinedChart(p0, 5, 1, session, pw);
			break;
		case 47:
			filename = chart.generateCombinedChart(p0, 5, 2, session, pw);
			break;
		case 48:
			filename = chart.generateCombinedChart(p0, 5, 3, session, pw);
			break;
		case 49:
			filename = chart.generateCombinedChart(p0, 5, 4, session, pw);
			break;
		case 50:
			filename = chart.generateCombinedChart(p0, 5, 5, session, pw);
			break;
		}
		return filename;
	}

	/**
	 * 生成图形
	 *
	 * @param chartType
	 * @param chart
	 * @param p0
	 * @param session
	 * @param pw
	 * @return
	 */
	public static String getChartName(int chartType, WebChart chart, int p0, HttpSession session,
			PrintWriter pw) {
		String name = chart.getTitle();
		int width = chart.getWidth();
		int height = chart.getHeight();
		return getChartName(chartType, chart, p0, name, width, height, session, pw);
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段名(分组序列)，简单序列可以为null
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @param p3
	 *            数据数据的字段名
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String p1, String p2, String p3) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(obj, p1);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段名(分组序列)，简单序列可以为null
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @param p3
	 *            数据数据的字段名
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String p1, String p2, String p3,
			int iCut, boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(obj, p1);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21，根据传入的pCategory,pSeries画出图形，即可以不画出所有序列
	 *
	 * @param obj
	 *            数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段名(分组序列)
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @param p3
	 *            数据数据的字段名
	 * @param pCategory
	 *            String[]数据分组的字段名(分组序列)，如传入null，将取所有值
	 * @param pSeries
	 *            String[]数据描述的字段名(X轴描述)，如传入null，将取所有值
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String p1, String p2, String p3,
			String[] pCategory, String[] pSeries) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		if (pCategory != null && pCategory.length > 0)
			category = pCategory;
		else
			category = ArrayUtil.setStringArr(obj, p1);
		if (pSeries != null && pSeries.length > 0)
			series = pSeries;
		else
			series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21，根据传入的pCategory,pSeries画出图形，即可以不画出所有序列
	 *
	 * @param obj
	 *            数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段名(分组序列)
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @param p3
	 *            数据数据的字段名
	 * @param pCategory
	 *            String[]数据分组的字段名(分组序列)，如传入null，将取所有值
	 * @param pSeries
	 *            String[]数据描述的字段名(X轴描述)，如传入null，将取所有值
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String p1, String p2, String p3,
			String[] pCategory, String[] pSeries, int iCut, boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		if (pCategory != null && pCategory.length > 0)
			category = pCategory;
		else
			category = ArrayUtil.setStringArr(obj, p1);
		if (pSeries != null && pSeries.length > 0)
			series = pSeries;
		else
			series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            数据对象，指标分组以横表显示
	 * @param qry_desc
	 *            分组描述数组
	 * @param qry_code
	 *            分组描述数组对应对象名称
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String[] qry_desc, String[] qry_code,
			String p2) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = qry_desc;
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(qry_desc, qry_code, series, obj, p2);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            数据对象，指标分组以横表显示
	 * @param qry_desc
	 *            分组描述数组
	 * @param qry_code
	 *            分组描述数组对应对象名称
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String[] qry_desc, String[] qry_code,
			String p2, int iCut, boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = qry_desc;
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(qry_desc, qry_code, series, obj, p2);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            数据对象，指标分组以横表显示
	 * @param info
	 *            数据分组的字段所在名称和索引(分组序列和数据值)所在信息 String[][] info = new
	 *            String[2]长度2，0表示中文描述，1表示数据字段名称
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String[][] info, String p2) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(info, 0);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(info, series, obj, p2);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            数据对象，指标分组以横表显示
	 * @param info
	 *            数据分组的字段所在名称和索引(分组序列和数据值)所在信息 String[][] info = new
	 *            String[2]长度2，0表示中文描述，1表示数据字段名称
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(Object[] obj, String[][] info, String p2, int iCut,
			boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(info, 0);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(info, series, obj, p2);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            String[][]数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段所在索引(分组序列)，简单序列可以为-1
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述)
	 * @param p3
	 *            数据数据的字段所在索引
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, int p1, int p2, int p3) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(obj, p1);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjCategory(String[][] obj, int p1, int p2, int p3,
			String type) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type))
			arrValue = ArrayUtil.setStringArr(obj, p1);
		else
			arrValue = ArrayUtil.setStringArr(obj, p2);
		if (p2 < 0 && "series".equals(type)) {
			arrValue = new String[] { "" };
		}
		return arrValue;
	}

	public static String[][] getFlashChartObjValue(String[][] obj, int p1, int p2, int p3) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			arrValue = null;
			return arrValue;
		}

		category = ArrayUtil.setStringArr(obj, p1);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		arrValue = ChartDataUtil.changeArrDoubleToString(value);
		return arrValue;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            String[][]数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段所在索引(分组序列))，简单序列可以为-1
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述)
	 * @param p3
	 *            数据数据的字段所在索引
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, int p1, int p2, int p3, int iCut,
			boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(obj, p1);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjCategory(String[][] obj, int p1, int p2, int p3,
			int iCut, boolean b0, String type) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type)) {
			arrValue = ArrayUtil.setStringArr(obj, p1);
		} else {
			arrValue = ArrayUtil.setStringArr(obj, p2);
			arrValue = FormatUtil.trimStringArray(arrValue, iCut, b0);
		}
		if (p2 < 0 && "series".equals(type)) {
			arrValue = new String[] { "" };
		}
		return arrValue;
	}

	public static String[][] getFlashChartObjValue(String[][] obj, int p1, int p2, int p3,
			int iCut, boolean b0) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			arrValue = null;
			return arrValue;
		}

		category = ArrayUtil.setStringArr(obj, p1);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		arrValue = ChartDataUtil.changeArrDoubleToString(value);
		return arrValue;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21，根据传入的pCategory,pSeries画出图形，即可以不画出所有序列
	 *
	 * @param obj
	 *            String[][]数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段所在索引(分组序列)
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述)
	 * @param p3
	 *            数据数据的字段所在索引
	 * @param pCategory
	 *            String[]数据分组的字段所在索引(分组序列)，如传入null，将取所有值
	 * @param pSeries
	 *            String[]数据描述的字段所在索引(X轴描述)，如传入null，将取所有值
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, int p1, int p2, int p3,
			String[] pCategory, String[] pSeries) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		if (pCategory != null && pCategory.length > 0)
			category = pCategory;
		else
			category = ArrayUtil.setStringArr(obj, p1);
		if (pSeries != null && pSeries.length > 0)
			series = pSeries;
		else
			series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjCategory(String[][] obj, int p1, int p2, int p3,
			String[] pCategory, String[] pSeries, String type) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type)) {
			if (pCategory != null && pCategory.length > 0)
				arrValue = pCategory;
			else
				arrValue = ArrayUtil.setStringArr(obj, p1);
		} else {
			if (pSeries != null && pSeries.length > 0)
				arrValue = pSeries;
			else
				arrValue = ArrayUtil.setStringArr(obj, p2);
		}
		return arrValue;
	}

	public static String[][] getFlashChartObjValue(String[][] obj, int p1, int p2, int p3,
			String[] pCategory, String[] pSeries) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			arrValue = null;
			return arrValue;
		}

		if (pCategory != null && pCategory.length > 0)
			category = pCategory;
		else
			category = ArrayUtil.setStringArr(obj, p1);
		if (pSeries != null && pSeries.length > 0)
			series = pSeries;
		else
			series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		arrValue = ChartDataUtil.changeArrDoubleToString(value);
		return arrValue;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21，根据传入的pCategory,pSeries画出图形，即可以不画出所有序列
	 *
	 * @param obj
	 *            String[][]数据对象,其中p1,p2,p3必须存在
	 * @param p1
	 *            数据分组的字段所在索引(分组序列)
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述)
	 * @param p3
	 *            数据数据的字段所在索引
	 * @param pCategory
	 *            String[]数据分组的字段所在索引(分组序列)，如传入null，将取所有值
	 * @param pSeries
	 *            String[]数据描述的字段所在索引(X轴描述)，如传入null，将取所有值
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, int p1, int p2, int p3,
			String[] pCategory, String[] pSeries, int iCut, boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		if (pCategory != null && pCategory.length > 0)
			category = pCategory;
		else
			category = ArrayUtil.setStringArr(obj, p1);
		if (pSeries != null && pSeries.length > 0)
			series = pSeries;
		else
			series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjCategory(String[][] obj, int p1, int p2, int p3,
			String[] pCategory, String[] pSeries, int iCut, boolean b0, String type) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type)) {
			if (pCategory != null && pCategory.length > 0)
				arrValue = pCategory;
			else
				arrValue = ArrayUtil.setStringArr(obj, p1);
		} else {
			if (pSeries != null && pSeries.length > 0)
				arrValue = pSeries;
			else
				arrValue = ArrayUtil.setStringArr(obj, p2);

			arrValue = FormatUtil.trimStringArray(arrValue, iCut, b0);
		}
		return arrValue;
	}

	public static String[][] getFlashChartObjValue(String[][] obj, int p1, int p2, int p3,
			String[] pCategory, String[] pSeries, int iCut, boolean b0) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			arrValue = null;
			return arrValue;
		}

		if (pCategory != null && pCategory.length > 0)
			category = pCategory;
		else
			category = ArrayUtil.setStringArr(obj, p1);
		if (pSeries != null && pSeries.length > 0)
			series = pSeries;
		else
			series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(category, series, obj, p1, p2, p3);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		arrValue = ChartDataUtil.changeArrDoubleToString(value);
		return arrValue;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            String[][]数据对象，指标分组以横表显示
	 * @param qry_desc
	 *            分组描述数组
	 * @param qry_code
	 *            分组描述数组对应索引值
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述)
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, String[] qry_desc,
			String[] qry_code, int p2) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = qry_desc;
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(qry_desc, qry_code, series, obj, p2);
		if (p2 < 0) {
			series = new String[] { "" };
		}
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjCategory(String[][] obj, String[] qry_desc,
			String[] qry_code, int p2, String type) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type))
			arrValue = qry_desc;
		else
			arrValue = ArrayUtil.setStringArr(obj, p2);
		if (p2 < 0 && "series".equals(type)) {
			arrValue = new String[] { "" };
		}
		return arrValue;
	}

	public static String[][] getFlashChartObjValue(String[][] obj, String[] qry_desc,
			String[] qry_code, int p2) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			arrValue = null;
			return arrValue;
		}

		category = qry_desc;
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(qry_desc, qry_code, series, obj, p2);
		if (p2 < 0) {
			series = new String[] { "" };
		}
		arrValue = ChartDataUtil.changeArrDoubleToString(value);
		return arrValue;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            String[][]数据对象，指标分组以横表显示
	 * @param qry_desc
	 *            分组描述数组
	 * @param qry_code
	 *            分组描述数组对应索引值
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述)
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, String[] qry_desc,
			String[] qry_code, int p2, int iCut, boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = qry_desc;
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(qry_desc, qry_code, series, obj, p2);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		if (p2 < 0) {
			series = new String[] { "" };
		}
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjCategory(String[][] obj, String[] qry_desc,
			String[] qry_code, int p2, int iCut, boolean b0, String type) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type)) {
			arrValue = qry_desc;
		} else {
			arrValue = ArrayUtil.setStringArr(obj, p2);
			arrValue = FormatUtil.trimStringArray(arrValue, iCut, b0);
		}
		if (p2 < 0 && "series".equals(type)) {
			arrValue = new String[] { "" };
		}
		return arrValue;
	}

	public static String[][] getFlashChartObjValue(String[][] obj, String[] qry_desc,
			String[] qry_code, int p2, int iCut, boolean b0) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			return arrValue;
		}

		category = qry_desc;
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(qry_desc, qry_code, series, obj, p2);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		if (p2 < 0) {
			series = new String[] { "" };
		}
		arrValue = ChartDataUtil.changeArrDoubleToString(value);
		return arrValue;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            String[][]数据对象，指标分组以横表显示
	 * @param info
	 *            数据分组的字段所在名称和索引(分组序列和数据值)所在信息 String[][] info = new
	 *            String[2]长度2，0表示中文描述，1表示数据索引位置
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述),简单序列可以为-1
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, String[][] info, int p2) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(info, 0);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(info, series, obj, p2);
		if (p2 < 0) {
			series = new String[] { "" };
		}
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-14,16,18-21
	 *
	 * @param obj
	 *            String[][]数据对象，指标分组以横表显示
	 * @param info
	 *            数据分组的字段所在名称和索引(分组序列和数据值)所在信息 String[][] info = new
	 *            String[2]长度2，0表示中文描述，1表示数据索引位置
	 * @param p2
	 *            数据描述的字段所在索引(X轴描述)
	 * @param iCut
	 *            描述字段截取的起始位数，一般适用日期的截取
	 * @param b0
	 *            前截取还是后截取
	 * @return WebChart对象
	 */
	public static WebChart getChartObjCategory(String[][] obj, String[][] info, int p2, int iCut,
			boolean b0) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = ArrayUtil.setStringArr(info, 0);
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setCategoryValue(info, series, obj, p2);
		series = FormatUtil.trimStringArray(series, iCut, b0);
		if (p2 < 0) {
			series = new String[] { "" };
		}
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-3(单饼图专用)
	 *
	 * @param obj
	 *            数据对象
	 * @param p2
	 *            数据描述的字段名(X轴描述)
	 * @param p3
	 *            数据数据的字段名
	 * @return WebChart对象
	 */
	public static WebChart getChartObjPie(Object[] obj, String p2, String p3) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[1];
			series = new String[1];
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = new String[1];
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setPieValue(series, obj, p2, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-3(单饼图专用)
	 *
	 * @param obj
	 *            String[][]数据对象
	 * @param p2
	 *            描述数组
	 * @param p3
	 *            描述数组对应数据对象中的名称
	 * @return WebChart对象
	 */
	public static WebChart getChartObjPie(Object[] obj, String[] p2, String[] p3) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = new String[1];
		series = p2;
		value = ChartDataUtil.setPieValue(series, obj, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	/**
	 * 获取通用分组类型的图形,适用1-3(单饼图专用)
	 *
	 * @param obj
	 *            String[][]数据对象
	 * @param p2
	 *            数据描述的字段所在索引
	 * @param p3
	 *            数据数据的字段所在索引
	 * @return WebChart对象
	 */
	public static WebChart getChartObjPie(String[][] obj, int p2, int p3) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = new String[1];
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setPieValue(series, obj, p2, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjPie(String[][] obj, int p2, int p3, String type,
			int series_cut_len, String series_cut) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type)) {
			arrValue = new String[1];
		} else {
			arrValue = ArrayUtil.setStringArr(obj, p2);
			if ("F".equals(series_cut)) {
				arrValue = FormatUtil.trimStringArray(arrValue, series_cut_len, true);
			} else if ("B".equals(series_cut)) {
				arrValue = FormatUtil.trimStringArray(arrValue, series_cut_len, false);
			}
		}

		return arrValue;
	}

	public static String[][] getFlashChartObjPieValue(String[][] obj, int p1, int p2, int p3) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			arrValue = null;
			return arrValue;
		}

		category = new String[1];
		series = ArrayUtil.setStringArr(obj, p2);
		value = ChartDataUtil.setPieValue(series, obj, p2, p3);

		if (value != null && value.length > 0 && series != null && series.length > 0) {
			arrValue = new String[series.length][3];
		}
		for (int i = 0; arrValue != null && i < arrValue.length; i++) {
			arrValue[i][0] = series[i];
			arrValue[i][1] = obj[i][p3];
			if (obj[i].length > 2) {
				arrValue[i][2] = obj[i][2];
			} else {
				arrValue[i][2] = obj[i][p1];
			}
		}
		return arrValue;
	}

	/**
	 * 获取通用分组类型的图形,适用1-3(单饼图专用)
	 *
	 * @param obj
	 *            String[][]数据对象
	 * @param p2
	 *            描述数组
	 * @param p3
	 *            描述数组对应数据中的索引
	 * @return WebChart对象
	 */
	public static WebChart getChartObjPie(String[][] obj, String[] p2, String[] p3) {
		WebChart fchart = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			value = null;
			fchart = new WebChart(category, series, value);
			return fchart;
		}

		category = new String[1];
		series = p2;
		value = ChartDataUtil.setPieValue(series, obj, p3);
		fchart = new WebChart(category, series, value);
		return fchart;
	}

	public static String[] getFlashChartObjPie(String[][] obj, String[] p2, String[] p3,
			String type, int series_cut_len, String series_cut) {
		String[] arrValue = null;
		if (obj == null || obj.length == 0) {
			if ("category".equals(type))
				arrValue = new String[] { "" };
			else
				arrValue = new String[] { "" };
			return arrValue;
		}

		if ("category".equals(type)) {
			arrValue = new String[1];
		} else {
			arrValue = p2;
			if ("F".equals(series_cut)) {
				arrValue = FormatUtil.trimStringArray(arrValue, series_cut_len, true);
			} else if ("B".equals(series_cut)) {
				arrValue = FormatUtil.trimStringArray(arrValue, series_cut_len, false);
			}
		}

		return arrValue;
	}

	public static String[][] getFlashChartObjPieValue(String[][] obj, String[] p2, String[] p3) {
		String[][] arrValue = null;
		if (obj == null || obj.length == 0) {
			category = new String[] { "" };
			series = new String[] { "" };
			arrValue = null;
			return arrValue;
		}

		category = new String[1];
		series = p2;
		value = ChartDataUtil.setPieValue(series, obj, p3);

		if (value != null && value.length > 0 && series != null && series.length > 0) {
			arrValue = new String[series.length][2];
		}
		for (int i = 0; arrValue != null && i < value[0].length; i++) {
			arrValue[i][0] = series[i];
			arrValue[i][1] = Double.toString(value[0][i]);
		}
		return arrValue;
	}

}