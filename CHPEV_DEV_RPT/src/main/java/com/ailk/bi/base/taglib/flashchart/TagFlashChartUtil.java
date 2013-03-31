package com.ailk.bi.base.taglib.flashchart;

import com.ailk.bi.base.table.PubInfoChartDefTable;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.chart.WebChart;
import com.ailk.bi.common.dbtools.WebDBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TagFlashChartUtil {

	private transient static final Log log = LogFactory.getLog(TagFlashChartUtil.class);

	public static String buildChart(String chartType, String caption, String subcaption,
			String[] categories, String[] seriesNames, String[][] dataset, String chart_id) {
		String outputString = "";
		// String chart_id = chart_obj.getConfigId();
		System.out.println("=========dao====buildChart===");
		try {
			if (chartType.indexOf("Pie") > -1 || chartType.indexOf("Doughnut") > -1) {
				outputString = BuildChartPre.buildPie3D(caption, subcaption, categories,
						seriesNames, dataset, chart_id, chartType);
			} else {
				outputString = BuildChartPre.BuildChartMutiSeries(caption, subcaption, categories,
						seriesNames, dataset, chart_id, chartType);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return outputString;
	}

	/**
	 * 基于fusionchart生成图形对象
	 *
	 * @param chartDef
	 * @param chart_obj
	 * @param categories
	 * @param seriesNames
	 * @param dataset
	 * @return
	 */
	public static String buildChartByObj(PubInfoChartDefTable chartDef, WebChart chart_obj,
			String[] categories, String[] seriesNames, String[][] dataset) {
		String outputString = "";

		try {
			if (ChartConsts.CHART_TYPE_FUSION_SINGLE.equals(chartDef.chart_distype)) {
				outputString = BuildChart.buildPie3D(chartDef, chart_obj, categories, seriesNames,
						dataset);
			} else if (ChartConsts.CHART_TYPE_FUSION_SCATTER.equals(chartDef.chart_distype)) {
				outputString = BuildChart.buildScatter(chartDef, chart_obj, categories,
						seriesNames, dataset);
			} else if (ChartConsts.CHART_TYPE_FUSION_BUBBLE.equals(chartDef.chart_distype)) {
				outputString = BuildChart.buildBubble(chartDef, chart_obj, categories, seriesNames,
						dataset);
			} else {
				outputString = BuildChart.BuildChartMutiSeries(chartDef, chart_obj, categories,
						seriesNames, dataset);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return outputString;
	}

	/*
	 * 查找图形定义信息 返回结果
	 */
	public static String[][] getChartDefine(String chartid, String column) {
		String[][] result = null;
		String strSql = "";
		if (column.equals("")) {
			strSql = "select chart_id,chart_belong,chart_type,chart_index,chart_attribute,sql_main,sql_where,sql_order,"
					+ "caption,subcaption,categories_idx,seriesname,seriesname_idx,dataset_idx from ui_chart_define "
					+ "where chart_id='" + chartid + "'";
		} else {
			strSql = "select " + column + " from ui_chart_define where chart_id='" + chartid + "'";
		}
		try {
			result = WebDBUtil.execQryArray(strSql, "");
		} catch (Exception ex) {
			log.debug("查询图形定义信息错误:" + ex);
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取数据最小值
	 *
	 * @param dataset
	 * @return
	 */
	public static String getMinValue(String[][] dataset) {
		// Y最小值
		double minValue = 999999999999.999;
		// 判断大小
		for (int k = 0; k < dataset.length; k++) {
			for (int t = 0; t < dataset[k].length; t++) {
				if (dataset[k][t] != null) {
					double tmpValue = Double.parseDouble(dataset[k][t]);
					if (tmpValue <= minValue) {
						minValue = tmpValue;
					}
				}
			}
		}
		minValue = Arith.div(minValue, 1.5, 0);
		String tmpMinValue = minValue + "";
		tmpMinValue = Arith.divs(tmpMinValue, "1", 0);
		String transValues = "";
		for (int i = 0; tmpMinValue.length() > 0 && i < tmpMinValue.length(); i++) {
			if (i == 0) {
				transValues = tmpMinValue.substring(0, 1);
			} else {
				transValues += "0";
			}
		}

		return transValues;
	}

}
