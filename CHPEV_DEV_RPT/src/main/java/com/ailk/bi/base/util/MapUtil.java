package com.ailk.bi.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

public class MapUtil {

	public static String genMapData(HttpServletRequest request, String msuId, String cityId,
			String gatherDay) {

		String sql = "";
		String arr[][] = null;
		try {
			//if (null != cityId && cityId.trim().length() > 0) {
			//	sql = SQLGenator.genSQL("chinaMap001", gatherDay, msuId, cityId);
			//} else {
				sql = SQLGenator.genSQL("chinaMap002", gatherDay, msuId);
			//}
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex1) {
			ex1.printStackTrace();
		}

		String mapStr = genMapXmlData(request, arr);

		return mapStr;
	}

	/*
	 * 合成xml数据
	 */
	public static String genMapXmlData(HttpServletRequest request, String[][] arr) {
		String records = null;
		String mapStr = null;

		records = getMapXml(request, arr);
		if (null == records || "".equals(records)) {
			// mapStr = getNoDataMapXml(request);
			mapStr = request.getContextPath() + "/js/FusionMaps/firstpage/map_china.xml";
		} else {
			// OutputFormat format = OutputFormat.createPrettyPrint();
			// format.setEncoding("UTF-8");// 设置XML文件的编码格式

			String filePath = "map_china2.xml";
			// System.out.println(filePath + "\n");
			mapStr = filePath;
		}
		return mapStr;
	}

	/**
	 * 有数据时,获得地图的XML格式字符串
	 *
	 * @since
	 * @param nowOrders
	 *            当前排序
	 * @param lastOrders
	 *            上次排序
	 * @param nowDate
	 *            当前日期
	 * @param lastDate
	 *            上次比较的日期
	 * @param kpiCycle
	 *            kip周期类型
	 * @return xml字符窜
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	public static String getMapXml(HttpServletRequest request, String arr[][]) {
		// 判断非空
		if (null == arr || arr.length == 0) {
			return null;
		}
		// 集合个数
		int size = arr.length;
		// 前3的个数
		int sizeTop = 3;
		// 中间的个数

		int sizeNormal = size - sizeTop;
		// xml路径
		File f = new File(request.getRealPath(".") + "/js/FusionMaps/firstpage/map_china.xml");
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			// 遍历xml第二根节点
			for (@SuppressWarnings("rawtypes")
			Iterator i = root.elementIterator(); i.hasNext();) {
				Element root2 = (Element) i.next();
				// 往data节点设置值
				if (root2.getName().equalsIgnoreCase("data")) {
					for (@SuppressWarnings("rawtypes")
					Iterator j = root2.elementIterator(); j.hasNext();) {
						Element dataChild = (Element) j.next();
						String dataId = dataChild.attributeValue("id");
						// 设置描述
						StringBuffer tempStr = new StringBuffer();
						String arr2[] = { "", "0", "0", "0.00%", "0.00%" };
						if (arr != null && arr.length > 0) {
							for (int ii = 0; ii < arr.length; ii++) {
								if (arr[ii][4].equals(dataId)) {
									arr2[0] = arr[ii][3];// 地域ID
									arr2[1] = arr[ii][5];// 地域名称
									arr2[2] = arr[ii][6];// 当前值
									arr2[3] = arr[ii][8];// 同比
									arr2[4] = arr[ii][10];// 环比
								}
							}
						}

						tempStr.append("地域:");
						tempStr.append(arr2[1]);
						tempStr.append("\n");
						tempStr.append("当前值:");
						tempStr.append(arr2[2]);
						tempStr.append("\n");
						tempStr.append("同比:");
						tempStr.append(arr2[3]);
						tempStr.append("\n");
						tempStr.append("环比:");
						tempStr.append(arr2[4]);

						dataChild.setAttributeValue("toolText", tempStr.toString());

						dataChild
								.setAttributeValue("link", "JavaScript:clickArea(" + arr2[0] + ")");
					}
				}
				// 设置markers值
				/*
				 * if (root2.getName().equalsIgnoreCase("markers")) { for
				 * (Iterator m = root2.elementIterator(); m.hasNext();) {
				 * Element markersChild = (Element)m.next(); if
				 * (markersChild.getName().equalsIgnoreCase("shapes")) { for
				 * (Iterator j = markersChild.elementIterator(); j.hasNext();) {
				 * Element shapesChild = (Element)j.next(); String dataId =
				 * shapesChild.attributeValue("id"); for (int k = 0; k < size;
				 * k++) { if (dataId.equalsIgnoreCase(nowOrders.get(k)
				 * .getAreaId() + "Map")) { //根据排名不同加入不同旗帜 if (k < sizeTop) {
				 * shapesChild.setAttributeValue("url", XMLWriter.MAP_TOP_PATH);
				 * } } } } } } }
				 */
			}

			// System.out.println(doc.asXML().replaceAll("\\n", ""));
			// System.out.println("替换br");
			// System.out.println(doc.asXML().replaceAll(":br:", "\\n"));

			String path = request.getRealPath(".");
			String filePath = path + "/leader/map_china2.xml";
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			// 默认为Utf-8 编码，可以根据需要改变编码格式

			// 将document中的内容写入文件中
			XMLWriter writer;
			try {
				writer = new XMLWriter(new FileOutputStream(new File(filePath)), format);
				writer.write(doc);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return doc.asXML().replaceAll("\n", "");
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 如果没有数据,获得地图默认的XML格式字符串
	 *
	 * @since
	 * @return XML字符串
	 */
	public static String getNoDataMapXml(HttpServletRequest request) {

		// xml路径
		String filePath = "";

		URL url = CommTool.class.getResource("CommTool.class");
		String className = url.getFile();

		filePath = className.substring(0, className.indexOf("WEB-INF"));
		File f = new File(filePath + "js/FusionMaps/firstpage/map_china.xml");
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(f);
			return doc.asXML().replaceAll("\\n", "");
		} catch (DocumentException e) {
			System.err.println("默认地图xml文件解析失败!");
		}
		return null;
	}

	/**
	 * 获取地图的xml格式数据
	 *
	 * @param msuId
	 * @param cityId
	 * @param gatherDay
	 * @return
	 */
	public static String getMapDataXML(String msuId, String cityId, String gatherDay)
			throws Exception {
		Document document = DocumentHelper.createDocument();
		Element chartElement = document.addElement("map");

		// 图形属性
		chartElement.addAttribute("baseFontSize", "11");
		chartElement.addAttribute("fillAlpha", "70");
		chartElement.addAttribute("hoverColor", "ff6600");
		chartElement.addAttribute("exposeHoverEvent", "1");
		chartElement.addAttribute("showBevel", "1");
		chartElement.addAttribute("mapLeftMargin", "5");
		chartElement.addAttribute("mapRightMargin", "5");
		chartElement.addAttribute("mapTopMargin", "5");
		chartElement.addAttribute("mapBottomMargin", "5");
		chartElement.addAttribute("showCanvasBorder", "1");
		chartElement.addAttribute("canvasBorderColor", "ffffff");
		chartElement.addAttribute("canvasBorderThickness", "2");
		chartElement.addAttribute("borderColor", "ffffff");

		// 图形数据
		String sql = "";
		String arr[][] = null;
		try {
			//if (null != cityId && cityId.trim().length() > 0) {
			//	sql = SQLGenator.genSQL("chinaMap001", gatherDay, msuId, cityId);
			//} else {
				sql = SQLGenator.genSQL("chinaMap002", gatherDay, msuId);
			//}
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex1) {
			ex1.printStackTrace();
		}
		Element datasetElement = chartElement.addElement("data");
		for (int i = 0; arr != null && i < arr.length; i++) {
			Element setElement = datasetElement.addElement("entity");
			setElement.addAttribute("id", arr[i][5]);
			setElement.addAttribute("color", arr[i][6]);
			setElement.addAttribute("link", "JavaScript:mapClick(" + arr[i][3] + ")");
			setElement.addAttribute("toolText", "地域:" + arr[i][4] + "%26amp;#xA;当期值:" + arr[i][7]
					+ " %26amp;#xA;同比:" + arr[i][9] + "％ %26amp;#xA;环比:" + arr[i][11] + "％");
		}

		Element stylesElement = chartElement.addElement("styles");
		Element definitionElement = stylesElement.addElement("definition");
		Element styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "TTipFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("isHTML", "1");
		styleElement.addAttribute("color", "000000");
		styleElement.addAttribute("bgColor", "fef5d6");
		styleElement.addAttribute("size", "12");
		styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "HTMLFont");
		styleElement.addAttribute("type", "font");
		styleElement.addAttribute("color", "333333");
		styleElement.addAttribute("borderColor", "CCCCCC");
		styleElement.addAttribute("bgColor", "FFFFFF");
		styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "myShadow");
		styleElement.addAttribute("type", "Shadow");
		styleElement.addAttribute("distance", "1");
		styleElement = definitionElement.addElement("style");
		styleElement.addAttribute("name", "markerAnim");
		styleElement.addAttribute("type", "animation");
		styleElement.addAttribute("param", "_y");
		styleElement.addAttribute("start", "0");
		styleElement.addAttribute("duration", "1.2");
		styleElement.addAttribute("easing", "bounce");
		Element applicationElement = stylesElement.addElement("application");
		Element applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "MARKERS");
		applyElement.addAttribute("styles", "myShadow");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "MARKERLABELS");
		applyElement.addAttribute("styles", "HTMLFont,myShadow");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "TOOLTIP");
		applyElement.addAttribute("styles", "TTipFont");
		applyElement = applicationElement.addElement("apply");
		applyElement.addAttribute("toObject", "MARKERS");
		applyElement.addAttribute("styles", "markerAnim");
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
			System.err.println("地图xml文件解析失败!");
			throw e;
		} finally {
			writer.close();
		}
		return stringWriter.getBuffer().toString().replaceAll("\"", "'");
	}
}
