package com.ailk.bi.base.util;

import java.util.*;
import java.io.*;

import org.jdom.*;
import org.jdom.output.*;
import org.jdom.transform.*;

import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.sysconfig.GetSystemConfig;

/**
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
 * @author not attributable
 * @version 1.0 把xsl文件放到与tomcat.bat相同的文件夹 －－ex. E:\BI_WorkDir\configs\下面
 *
 */
@SuppressWarnings({ "rawtypes" })
public class XMLTranser {
	public XMLTranser() {
	}

	/**
	 * 自定义解析字符串函数
	 *
	 * @param sql
	 *            String
	 * @return a String array
	 */
	public static String[] parseString(String sql) {
		String str1 = "";
		String str2 = "";
		String retArr[] = null;
		sql = sql.toUpperCase();
		sql = StringB.replaceFirst(sql, "DISTINCT ", "");
		if (sql.trim().startsWith("SELECT")) {
			str1 = sql.trim().substring(6);
			int temp = str1.indexOf("FROM");
			str2 = str1.substring(0, temp);
			// System.out.println(str2);
			String cond[] = str2.trim().split(",");
			retArr = new String[cond.length];
			for (int j = 0; j < cond.length; j++) {
				if (cond[j].indexOf(" AS ") != -1) {
					String bb[] = cond[j].split(" AS ");
					retArr[j] = bb[1].trim();
				} else {
					if (cond[j].indexOf(".") != -1) {
						int idx = cond[j].indexOf(".");
						retArr[j] = cond[j].substring(idx + 1).trim();
					} else {
						retArr[j] = cond[j].trim();
					}
				}
			}
		}
		return retArr;
	}

	/**
	 * 解析写入目标文件
	 *
	 * @param doc
	 *            xml文档
	 * @param stylesheet
	 *            样式单
	 * @param fileName
	 *            文件名
	 */
	public static void transWithXsl(Document doc, String stylesheet,
			String fileName) {
		try {
			String xslDir = CommTool.getWebInfPath()
					+ GetSystemConfig.getBIBMConfig().getExtParam("xsl_file");
			XSLTransformer transformer = new XSLTransformer(xslDir + stylesheet);
			Document x2 = transformer.transform(doc);

			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream output = new FileOutputStream(new File(fileName));
			outputter.output(x2, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析返回目标xml文档
	 *
	 * @param doc
	 *            源xml文档
	 * @param stylesheet
	 *            指定样式单
	 */
	public static Document transWithXsl(Document doc, String stylesheet) {
		Document retDoc = null;
		try {
			String xslDir = CommTool.getWebInfPath()
					+ GetSystemConfig.getBIBMConfig().getExtParam("xsl_file");
			XSLTransformer transformer = new XSLTransformer(xslDir + stylesheet);
			retDoc = transformer.transform(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retDoc;
	}

	/**
	 * 从查询结果生成源XML文档函数
	 *
	 * @param String
	 *            sql
	 * @param Vector
	 *            result
	 * @return Documnet
	 */

	public static Document getDocument(String sql, Vector result) {
		// 构建xml
		Element root = new Element("Tree");
		Document doc = new Document(root);
		// 填充value生成tree元素
		String volName[] = XMLTranser.parseString(sql);

		for (int i = 0; i < result.size(); i++) {
			Element element = new Element("tree");
			Vector tempV = (Vector) result.get(i);

			for (int j = 0; j < tempV.size(); j++) {
				Element subelement = new Element(volName[j]);
				subelement.setText((String) tempV.get(j));
				element.addContent(subelement);
			}
			root.addContent(element);
		}
		return doc;
	}

	/**
	 * 从查询结果生成XML文档函数
	 *
	 * @param String
	 *            sql
	 * @param String
	 *            [][]
	 * @return Document
	 */
	public static Document getDocument(String sql, String[][] result) {
		// 构建xml
		Element root = new Element("Tree");
		Document doc = new Document(root);
		// 填充value生成tree元素
		String volName[] = XMLTranser.parseString(sql);
		for (int i = 0; i < result.length; i++) {
			Element element = new Element("tree");
			String[] tempV = result[i];

			for (int j = 0; j < tempV.length; j++) {
				Element subelement = new Element(volName[j]);
				subelement.setText((String) tempV[j]);
				element.addContent(subelement);
			}
			root.addContent(element);
		}
		return doc;
	}

	/** **********************测试属性xsl************************************* */
	/**
	 * 从查询结果生成源XML文档函数
	 *
	 * @param String
	 *            sql
	 * @param Vector
	 *            result
	 * @return Documnet
	 */

	public static Document getAttDocument(String sql, Vector result) {
		// 构建xml
		Element root = new Element("Tree");
		Document doc = new Document(root);
		// 填充value生成tree元素
		String volName[] = XMLTranser.parseString(sql);
		for (int i = 0; i < result.size(); i++) {
			Element element = new Element("tree");
			Vector tempV = (Vector) result.get(i);

			for (int j = 0; j < tempV.size(); j++) {
				Element subelement = new Element(volName[j]);
				// subelement.setText((String)tempV.get(j));
				Attribute attri = new Attribute(volName[j],
						(String) tempV.get(j));
				subelement.setAttribute(attri);
				element.addContent(subelement);
			}
			root.addContent(element);
		}
		return doc;
		// 写文件
		/*
		 * XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		 * FileOutputStream output = null; try { output = new
		 * FileOutputStream(new File("E:\\jcm\\jcm.xml")); } catch
		 * (FileNotFoundException ex) { } try { outputter.output(doc, output); }
		 * catch (IOException ex1) { }
		 */

	}

}