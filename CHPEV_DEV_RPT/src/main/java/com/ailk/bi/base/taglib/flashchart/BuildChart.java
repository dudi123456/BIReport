package com.ailk.bi.base.taglib.flashchart;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ailk.bi.base.table.PubInfoChartDefTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.chart.WebChart;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class BuildChart {

	private transient static final Log log = LogFactory.getLog(BuildChart.class);

	/**
	 * 生成饼图
	 *
	 * @param chartDef
	 * @param chart_obj
	 * @param categories
	 * @param seriesNames
	 * @param dataset
	 * @return
	 * @throws Exception
	 */
	public static String buildPie3D(PubInfoChartDefTable chartDef, WebChart chart_obj,
			String[] categories, String[] seriesNames, String[][] dataset) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element chartElement = document.addElement("chart");
		String[] chart_paras = chartDef.chart_attribute.split(";");
		for (int para_i = 0; chart_paras != null && para_i < chart_paras.length; para_i++) {
			String[] para_m = chart_paras[para_i].split(":");
			if (para_m != null && para_m.length > 1) {
				chartElement.addAttribute(para_m[0], para_m[1]);
			}
		}
		// 图形标题
		if (!StringTool.checkEmptyString(chart_obj.getTitle())) {
			chartElement.addAttribute("caption", chart_obj.getTitle());
		}
		// 图形子标题
		if (!StringTool.checkEmptyString(chart_obj.getSubtitle())) {
			chartElement.addAttribute("subcaption", chart_obj.getSubtitle());
		}
		// 图形Y轴是否显示0值
		if (!chart_obj.getIncludesZero()) {
			chartElement.addAttribute("yAxisMinValue", TagFlashChartUtil.getMinValue(dataset));
		}
		if (dataset == null) {
			// throw new Exception("dataset参数为空！");
		}
		for (int i = 0; dataset != null && i < dataset.length; i++) {
			if (dataset[i].length < 2) {
				throw new Exception("数据集第一维数组长度不小于2！");
			}
			Element datasetElement = chartElement.addElement("set");
			datasetElement.addAttribute("label", categories[i]);
			datasetElement.addAttribute("value", dataset[i][1]);
			datasetElement.addAttribute("toolText", dataset[i][0] + ":" + dataset[i][1]);
			if (chart_obj.getLablink() != null && !"".equals(chart_obj.getLablink())) {
				String link_script = chart_obj.getLablink();
				datasetElement.addAttribute("link", "JavaScript:" + link_script + "("
						+ dataset[i][2] + ")");
			}
		}
		Element stylesElement = chartElement.addElement("styles");
		Element definitionElement = stylesElement.addElement("definition");
		Element styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "CaptionFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("font", "宋体");
		styleElement.addAttribute("size", "15");
		styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "SubCaptionFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("font", "宋体");
		styleElement.addAttribute("size", "12");
		styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "LegendFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("font", "宋体");
		styleElement.addAttribute("size", "12");
		Element applicationElement = stylesElement.addElement("application");
		Element applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "CAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "SUBCAPTION");
		applyElement.addAttribute("styles", "SubCaptionFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "LEGEND");
		applyElement.addAttribute("styles", "LegendFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "YAXISNAME");
		applyElement.addAttribute("styles", "LegendFont");
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(stringWriter);
			writer.write(document);
		} catch (Exception e) {
			log.error("生成xml出错！", e);
			throw e;
		} finally {
			writer.close();
		}
		return stringWriter.getBuffer().toString();
	}

	/**
	 * 生成多维图，针对标准化配置
	 *
	 * @param chartDef
	 * @param chart_obj
	 * @param categories
	 * @param seriesNames
	 * @param dataset
	 * @return
	 * @throws Exception
	 */
	public static String BuildChartMutiSeries(PubInfoChartDefTable chartDef, WebChart chart_obj,
			String[] categories, String[] seriesNames, String[][] dataset) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element chartElement = document.addElement("chart");

		// 图形属性参数
		String[] chart_paras = chartDef.chart_attribute.split(";");
		for (int para_i = 0; chart_paras != null && para_i < chart_paras.length; para_i++) {
			String[] para_m = chart_paras[para_i].split(":");
			if (para_m[0].equals("numvdivlines")) {
				chartElement.addAttribute(para_m[0], "" + (categories.length - 2));
			} else {
				if (para_m != null && para_m.length > 1) {
					chartElement.addAttribute(para_m[0], para_m[1]);
				}
			}
		}
		// 图形标题
		if (!StringTool.checkEmptyString(chart_obj.getTitle())) {
			chartElement.addAttribute("caption", chart_obj.getTitle());
		}
		// 图形子标题
		if (!StringTool.checkEmptyString(chart_obj.getSubtitle())) {
			chartElement.addAttribute("subcaption", chart_obj.getSubtitle());
		}
		// 图形Y轴是否显示0值
		if (!chart_obj.getIncludesZero()) {
			chartElement.addAttribute("yAxisMinValue", TagFlashChartUtil.getMinValue(dataset));
		}

		Element categoriesElement = chartElement.addElement("categories");
		if (categories == null || categories.length == 0) {
			throw new Exception("categories参数为空！");
		}
		for (int i = 0; i < categories.length; i++) {
			Element categoryElement = categoriesElement.addElement("category");
			String tmpcateName = categories[i];
			String[] para_cate = tmpcateName.split("&");
			if (para_cate != null && para_cate.length > 1) {
				tmpcateName = para_cate[1];
			}
			categoryElement.addAttribute("label", tmpcateName);
		}
		if (seriesNames == null) {
			throw new Exception("seriesNames参数为空！");
		}
		if (dataset == null) {
			// throw new Exception("dataset参数为空！");
		}
		if (dataset != null && seriesNames.length != dataset.length) {
			throw new Exception("seriesNames与dataset数组长度不相等！");
		}

		// 序列属性参数
		String[] series_paras = chartDef.series_attribute.split(";");
		for (int i = 0; seriesNames != null && i < seriesNames.length; i++) {
			Element datasetElement = chartElement.addElement("dataset");
			String tmpserName = seriesNames[i];
			String[] para_series = tmpserName.split("&");
			if (para_series != null && para_series.length > 1) {
				tmpserName = para_series[1];
			}
			datasetElement.addAttribute("seriesName", tmpserName);
			boolean isLink = false;
			String link_script = "";

			// 查询图例参数, 主要设置每个图例的颜色等,如果该图图例比设置的要多,则默认不加参数
			if (series_paras != null && i < series_paras.length) {
				String[] seriespara_m = series_paras[i].split("!"); // 参数
				for (int k = 0; seriespara_m != null && k < seriespara_m.length; k++) {
					String[] seriespara_mm = seriespara_m[k].split(":");
					if (seriespara_mm != null && seriespara_mm.length > 1) {
						if ("lablink".equals(seriespara_mm[0])) {
							isLink = true;
							link_script = seriespara_mm[1];
						} else {
							datasetElement.addAttribute(seriespara_mm[0], seriespara_mm[1]);
						}
					}
				}
			}
			for (int k = 0; dataset != null && k < dataset[i].length; k++) {
				if ("-9.99999999E8".equals(dataset[i][k])) {
					if (chart_obj.getSynchroAxis()) {
						dataset[i][k] = "0";
					} else {
						continue;
					}
				}
				Element setElement = datasetElement.addElement("set");
				if (!"".equals(dataset[i][k])) {
					setElement.addAttribute("value", dataset[i][k]);

					String tmpserID = seriesNames[i];
					if (para_series != null && para_series.length > 1) {
						tmpserID = para_series[0];
					}
					String tmpcateID = categories[k];
					String[] para_cate = tmpcateID.split("&");
					if (para_cate != null && para_cate.length > 1) {
						tmpcateID = para_cate[0];
					}
					if (isLink) {
						setElement.addAttribute("link", "javascript:" + link_script + "(\""
								+ tmpserID + "\"," + tmpcateID + ")");
					}
				}
			}
		}
		Element stylesElement = chartElement.addElement("styles");
		Element definitionElement = stylesElement.addElement("definition");
		Element styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "CaptionFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("font", "宋体");
		styleElement.addAttribute("size", "15");
		styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "SubCaptionFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("font", "宋体");
		styleElement.addAttribute("size", "12");
		styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "LegendFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("font", "宋体");
		styleElement.addAttribute("size", "12");
		Element applicationElement = stylesElement.addElement("application");
		Element applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "CAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "SUBCAPTION");
		applyElement.addAttribute("styles", "SubCaptionFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "LEGEND");
		applyElement.addAttribute("styles", "LegendFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "YAXISNAME");
		applyElement.addAttribute("styles", "LegendFont");
		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = null;
		OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
		xmlFormat.setEncoding("utf-8");
		try {
			// 添加 utf-8的bom头
			stringWriter.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
			writer = new XMLWriter(stringWriter, xmlFormat);
			writer.write(document);
		} catch (Exception e) {
			log.error("生成xml出错！", e);
			throw e;
		} finally {
			writer.close();
		}
		return stringWriter.getBuffer().toString();
	}

	/**
	 * 生成气泡图
	 *
	 * @param chartDef
	 * @param chart_obj
	 * @param categories
	 * @param seriesNames
	 * @param dataset
	 * @return
	 * @throws Exception
	 */
	public static String buildBubble(PubInfoChartDefTable chartDef, WebChart chart_obj,
			String[] categories, String[] seriesNames, String[][] dataset) throws Exception {
		if (dataset == null) {
			throw new Exception("数据集为空！");
		}

		String chart_id = chart_obj.getConfigId();
		//
		System.out.println("x=======" + chart_obj.getXAxis());
		System.out.println("y=======" + chart_obj.getYAxis());
		//
		String[] xAxis = chart_obj.getXAxis().split("[$]");
		String[] yAxis = chart_obj.getYAxis().split("[$]");

		// 数据特殊处理结束
		Document document = DocumentHelper.createDocument();
		// chart
		Element chartElement = document.addElement("chart");
		String[] chart_paras = chartDef.chart_attribute.split(";");
		for (int para_i = 0; para_i < chart_paras.length; para_i++) {
			String[] para_m = chart_paras[para_i].split("#");
			chartElement.addAttribute(para_m[0], para_m[1]);
		}
		//
		// String numDivLines = yAxis.length+"";
		String xAxisMaxValue = xAxis[xAxis.length - 1].split("[|]")[1];

		chartElement.addAttribute("caption", chart_obj.getTitle());
		chartElement.addAttribute("xAxisName", chart_obj.getXAxisName());
		chartElement.addAttribute("yAxisName", chart_obj.getYAxisName());
		chartElement.addAttribute("xAxisMaxValue", xAxisMaxValue);

		// categories
		Element categoriesElement = chartElement.addElement("categories");

		for (int i = 0; i < xAxis.length; i++) {
			Element categoryElement = categoriesElement.addElement("category");
			String[] tmp = xAxis[i].split("[|]");

			categoryElement.addAttribute("label", tmp[0]);
			categoryElement.addAttribute("x", tmp[1]);
			if (i > 0) {
				categoryElement.addAttribute("showVerticalLine", "1");
			}
		}

		Element datasetElement = chartElement.addElement("dataSet");
		datasetElement.addAttribute("showValues", "0");

		for (int index = 0; index < dataset.length; index++) {
			Element setElement = datasetElement.addElement("set");
			setElement.addAttribute("y", dataset[index][1]);
			setElement.addAttribute("x", dataset[index][2]);
			setElement.addAttribute("z", dataset[index][3]);
			setElement.addAttribute("name", "x=" + dataset[index][2] + "y=" + dataset[index][1]
					+ "z=" + dataset[index][3]);
		}

		Element vTrendlinesElement = chartElement.addElement("vTrendlines");
		Element hTrendlinesElement = chartElement.addElement("hTrendlines");

		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(stringWriter);
			writer.write(document);
		} catch (Exception e) {
			log.error("生成xml出错！", e);
			throw e;
		} finally {
			writer.close();
		}
		return stringWriter.getBuffer().toString();
	}

	/**
	 * 生成XY散点图
	 *
	 * @param chartDef
	 * @param chart_obj
	 * @param categories
	 * @param seriesNames
	 * @param dataset
	 * @return
	 * @throws Exception
	 */
	public static String buildScatter(PubInfoChartDefTable chartDef, WebChart chart_obj,
			String[] categories, String[] seriesNames, String[][] dataset) throws Exception {
		if (dataset == null) {
			throw new Exception("数据集为空！");
		}

		String chart_id = chart_obj.getConfigId();
		//
		System.out.println("x=======" + chart_obj.getXAxis());
		System.out.println("y=======" + chart_obj.getYAxis());
		//
		String[] xAxis = chart_obj.getXAxis().split("[$]");
		String[] yAxis = chart_obj.getYAxis().split("[$]");

		// 数据特殊处理结束
		Document document = DocumentHelper.createDocument();
		// chart
		Element chartElement = document.addElement("chart");

		String[] chart_paras = chartDef.chart_attribute.split(";");
		for (int para_i = 0; para_i < chart_paras.length; para_i++) {
			String[] para_m = chart_paras[para_i].split("#");
			chartElement.addAttribute(para_m[0], para_m[1]);
		}
		//
		String numDivLines = yAxis.length + "";
		String xAxisName = "饱和度";
		String yAxisName = "APRU分档";
		String xAxisMaxValue = xAxis[xAxis.length - 1].split("[|]")[1];
		String xAxisMinValue = xAxis[0].split("[|]")[1];
		String yAxisMaxValue = yAxis[yAxis.length - 1];

		chartElement.addAttribute("caption", chart_obj.getTitle());
		chartElement.addAttribute("numDivLines", numDivLines);
		chartElement.addAttribute("xAxisName", xAxisName);
		chartElement.addAttribute("yAxisName", yAxisName);
		chartElement.addAttribute("xAxisMaxValue", xAxisMaxValue);
		chartElement.addAttribute("xAxisMinValue", xAxisMinValue);
		chartElement.addAttribute("yAxisMaxValue", yAxisMaxValue);
		//
		// dataSet
		if (seriesNames == null) {
			HashSet set = new HashSet();
			for (int i = 0; i < dataset.length; i++) {
				set.add(dataset[i][0]);

			}
			//
			if (set.size() > 0) {
				seriesNames = new String[set.size()];
			}
			Iterator it = set.iterator();
			int j = 0;
			while (it.hasNext()) {
				seriesNames[j++] = it.next() + "";
			}

		}

		// categories
		Element categoriesElement = chartElement.addElement("categories");
		categoriesElement.addAttribute("verticalLineColor", "666666");
		categoriesElement.addAttribute("verticalLineThickness", "1");

		for (int i = 0; i < xAxis.length; i++) {
			Element categoryElement = categoriesElement.addElement("category");
			String[] tmp = xAxis[i].split("[|]");

			categoryElement.addAttribute("label", tmp[0]);
			categoryElement.addAttribute("x", tmp[1]);
			if (i == yAxis.length - 1) {
				categoryElement.addAttribute("showVerticalLine", "0");
			} else {
				categoryElement.addAttribute("showVerticalLine", "1");
			}

		}

		for (int i = 0; i < seriesNames.length; i++) {
			Element datasetElement = chartElement.addElement("dataSet");
			datasetElement.addAttribute("seriesName", seriesNames[i]);
			String[] series = chartDef.series_attribute.split(";");
			for (int j = 0; j < series.length; j++) {
				String[] param = series[i].split("!");
				for (int k = 0; k < param.length; k++) {
					String[] tmp = param[k].split("#");
					datasetElement.addAttribute(tmp[0], tmp[1]);
				}

			}

			for (int index = 0; index < dataset.length; index++) {
				if (dataset[index][0].trim().equals(seriesNames[i].trim())) {
					Element setElement = datasetElement.addElement("set");
					setElement.addAttribute("y", dataset[index][1]);
					setElement.addAttribute("x", dataset[index][2]);
				}

			}
		}

		Element vTrendlinesElement = chartElement.addElement("vTrendlines");
		Element hTrendlinesElement = chartElement.addElement("hTrendlines");

		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(stringWriter);
			writer.write(document);
		} catch (Exception e) {
			log.error("生成xml出错！", e);
			throw e;
		} finally {
			writer.close();
		}
		return stringWriter.getBuffer().toString();
	}
}
