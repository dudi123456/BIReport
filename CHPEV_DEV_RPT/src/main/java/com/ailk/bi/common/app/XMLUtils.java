package com.ailk.bi.common.app;

import java.io.FileInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Title: 北京朗新信息系统公司 CNC语音项目
 * </p>
 * <p>
 * Description: 解析配置文件用到的XML操作函数
 * </p>
 * <p>
 * Copyright: Copyright (c) 2001
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author WISEKING
 * @version 1.0 2003/12/09
 */
@SuppressWarnings("deprecation")
public class XMLUtils {
	/**
	 * 获得xml文件中的指定的第index个tag的单元
	 *
	 * @param doc
	 * @param tagName
	 * @param index
	 * @return
	 */
	public static Element getElement(Document doc, String tagName, int index) {
		NodeList rows = doc.getDocumentElement().getElementsByTagName(tagName);
		return (Element) rows.item(index);
	}

	/**
	 * 获得xml文件中指定的tagName的个数
	 *
	 * @param doc
	 * @param tagName
	 * @return
	 */
	public static int getSize(Document doc, String tagName) {
		NodeList rows = doc.getDocumentElement().getElementsByTagName(tagName);
		return rows.getLength();
	}

	/**
	 * 将XML document按照指定的encode格式转换成串
	 *
	 * @param doc
	 * @param encoding
	 * @return
	 */
	public static String doc2String(Document doc, String encoding) {
		try {
			doc.normalize();
			OutputFormat format = new OutputFormat(doc); // Serialize DOM
			format.setEncoding(encoding);
			format.setVersion("1.0");
			format.setIndenting(true);
			StringWriter stringOut = new StringWriter(); // Writer will be a
			// String
			XMLSerializer serial = new XMLSerializer(stringOut, format);
			serial.asDOMSerializer(); // As a DOM Serializer
			serial.serialize(doc.getDocumentElement());

			return stringOut.toString(); // Spit out DOM as a String
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析一个xml文件
	 *
	 * @param strFile
	 * @return
	 */
	public static Document parseXMLFile(String strFile) {
		try {
			FileInputStream is = new FileInputStream(strFile);
			DocumentBuilderFactory dd = DocumentBuilderFactory.newInstance();
			Document doc = dd.newDocumentBuilder().parse(is);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
