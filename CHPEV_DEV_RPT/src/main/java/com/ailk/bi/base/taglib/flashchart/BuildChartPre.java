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
import com.ailk.bi.common.chart.WebChart;

@SuppressWarnings({ "unused" })
public class BuildChartPre {

	private transient static final Log log = LogFactory.getLog(BuildChart.class);

	/**
	 * 生成多维图
	 *
	 * @param caption
	 * @param subcaption
	 * @param categories
	 * @param seriesNames
	 * @param dataset
	 * @param chartId
	 * @param chartType
	 * @return
	 * @throws Exception
	 */
	public static String BuildChartMutiSeries(String caption, String subcaption,
			String[] categories, String[] seriesNames, String[][] dataset, String chartId,
			String chartType) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element chartElement = document.addElement("chart");
		if (chartId.equals("")) {
			chartId = chartType;
		}
		String[][] getparas = TagFlashChartUtil.getChartDefine(chartId,
				"chart_attribute,series_attribute");
		String[] chart_paras = getparas[0][0].split(";");
		String[] series_paras = getparas[0][1].split(";");
		for (int para_i = 0; para_i < chart_paras.length; para_i++) {
			String[] para_m = chart_paras[para_i].split("#");
			if (para_m[0].equals("numvdivlines")) {
				chartElement.addAttribute(para_m[0], "" + (categories.length - 2));
			} else {
				chartElement.addAttribute(para_m[0], para_m[1]);
			}
		}
		chartElement.addAttribute("caption", caption);
		chartElement.addAttribute("subcaption", subcaption);
		//
		// Y最小值
		double minValue = 999999999999.999;
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

		// 判断大小
		String tmpMinValue = minValue + "";
		// String tmpMinValue_a = tmpMinValue.substring(0,
		// tmpMinValue.indexOf("."));
		// minValue = minValue
		chartElement.addAttribute("PYAxisMinValue", CommTool.genModValue(tmpMinValue));

		// chartElement.addAttribute("yAxisMinValue", minValue+"");

		//
		Element categoriesElement = chartElement.addElement("categories");
		if (categories == null || categories.length == 0) {
			throw new Exception("categories参数为空！");
		}
		for (int i = 0; i < categories.length; i++) {
			Element categoryElement = categoriesElement.addElement("category");
			categoryElement.addAttribute("label", categories[i]);
		}
		if (seriesNames == null) {
			throw new Exception("seriesNames参数为空！");
		}
		if (dataset == null) {
			throw new Exception("dataset参数为空！");
		}
		if (seriesNames.length != dataset.length) {
			throw new Exception("seriesNames与dataset数组长度不相等！");
		}
		for (int i = 0; i < seriesNames.length; i++) {
			Element datasetElement = chartElement.addElement("dataset");
			datasetElement.addAttribute("seriesName", seriesNames[i]);
			// 查询图例参数, 主要设置每个图例的颜色等,如果该图图例比设置的要多,则默认不加参数
			if (i < series_paras.length) {
				String[] seriespara_m = series_paras[i].split("!"); // 参数
				for (int k = 0; k < seriespara_m.length; k++) {
					String[] seriespara_mm = seriespara_m[k].split("#");
					datasetElement.addAttribute(seriespara_mm[0], seriespara_mm[1]);
				}
			}
			for (int k = 0; k < dataset[i].length; k++) {
				if ("-9.99999999E8".equals(dataset[i][k])) {
					dataset[i][k] = "0";
				}
				Element setElement = datasetElement.addElement("set");
				if (!"".equals(dataset[i][k])) {
					setElement.addAttribute("value", dataset[i][k]);
				}
			}
		}
		Element stylesElement = chartElement.addElement("styles");
		Element definitionElement = stylesElement.addElement("definition");
		Element styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "CaptionFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("size", "12");
		Element applicationElement = stylesElement.addElement("application");
		Element applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "CAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "SUBCAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
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
	 * 生成单维图
	 *
	 * @param caption
	 * @param subcaption
	 * @param categories
	 * @param seriesNames
	 * @param dataset
	 * @param chart_id
	 * @param chartType
	 * @return
	 * @throws Exception
	 */
	public static String BuildChartSingleSeries(String caption, String subcaption,
			String[] categories, String[] seriesNames, String[][] dataset, String chart_id,
			String chartType) throws Exception {
		Document document = DocumentHelper.createDocument();
		Element chartElement = document.addElement("chart");
		if (chart_id.equals("")) {
			chart_id = chartType;
		}
		String[][] getparas = TagFlashChartUtil.getChartDefine(chart_id,
				"chart_attribute,series_attribute");
		String[] chart_paras = getparas[0][0].split(";");
		// String[] series_paras = getparas[0][1].split(";");
		for (int para_i = 0; para_i < chart_paras.length; para_i++) {
			String[] para_m = chart_paras[para_i].split("#");
			chartElement.addAttribute(para_m[0], para_m[1]);
		}
		chartElement.addAttribute("caption", caption);
		chartElement.addAttribute("subcaption", subcaption);
		if (dataset == null) {
			throw new Exception("dataset参数为空！");
		}
		if (dataset.length != seriesNames.length) {
			throw new Exception("dataset与seriesNames长度不等！");
		}
		for (int i = 0; i < dataset.length; i++) {
			Element datasetElement = chartElement.addElement("set");
			datasetElement.addAttribute("label", seriesNames[i]);
			datasetElement.addAttribute("value", dataset[i][0]);
		}
		Element stylesElement = chartElement.addElement("styles");
		Element definitionElement = stylesElement.addElement("definition");
		Element styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "CaptionFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("size", "12");
		Element applicationElement = stylesElement.addElement("application");
		Element applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "CAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "SUBCAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
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
	 * 生成3D饼图
	 *
	 * @param caption
	 * @param subcaption
	 * @param dataset
	 * @return
	 * @throws Exception
	 */
	public static String buildPie3D(String caption, String subcaption, String[] categories,
			String[] seriesNames, String[][] dataset, String chart_id, String chartType)
			throws Exception {
		Document document = DocumentHelper.createDocument();
		// TagFlashChartUtil tu = new TagFlashChartUtil();
		Element chartElement = document.addElement("chart");
		if (chart_id.equals("")) {
			chart_id = chartType;
		}
		String[][] getparas = TagFlashChartUtil.getChartDefine(chart_id,
				"chart_attribute,series_attribute");
		String[] chart_paras = getparas[0][0].split(";");
		// String[] series_paras = getparas[0][1].split(";");
		for (int para_i = 0; para_i < chart_paras.length; para_i++) {
			String[] para_m = chart_paras[para_i].split("#");
			chartElement.addAttribute(para_m[0], para_m[1]);
		}
		chartElement.addAttribute("caption", caption);
		chartElement.addAttribute("subcaption", subcaption);
		if (dataset == null) {
			throw new Exception("dataset参数为空！");
		}
		for (int i = 0; i < dataset.length; i++) {
			if (dataset[i].length < 2) {
				throw new Exception("数据集第一维数组长度不小于2！");
			}
			Element datasetElement = chartElement.addElement("set");
			datasetElement.addAttribute("label", dataset[i][0]);
			datasetElement.addAttribute("value", dataset[i][1]);
			datasetElement.addAttribute("toolText", dataset[i][0] + ":" + dataset[i][1]);
		}
		Element stylesElement = chartElement.addElement("styles");
		Element definitionElement = stylesElement.addElement("definition");
		Element styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "CaptionFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("size", "12");
		Element applicationElement = stylesElement.addElement("application");
		Element applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "CAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "SUBCAPTION");
		applyElement.addAttribute("styles", "CaptionFont");
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
