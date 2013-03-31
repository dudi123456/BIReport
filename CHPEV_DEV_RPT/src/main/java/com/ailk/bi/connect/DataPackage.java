package com.ailk.bi.connect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.crimson.tree.XmlDocument;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: EJB和SERVLET前台数据传递
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author
 * @version 1.0
 */

@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public class DataPackage implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4392371698113394719L;

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2003
	 * </p>
	 * <p>
	 * Company:
	 * </p>
	 *
	 * @author 联通BU 陈俊华
	 * @version 1.0
	 */
	class StructPackageHeader implements java.io.Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = -5274157327969208451L;

		// 块标志
		int tag;

		// 字段HASH表
		HashMap colList;

		// 块起始记录
		int firstRow;

		// 块记录数
		int size;

		// 字段英文名称列表（col1,col2,col3)
		String colNameList;

		StructPackageHeader(int tag, int firstRow) {
			this.tag = tag;
			this.firstRow = firstRow;
			colList = new HashMap();
		}
	}

	private Vector header; // 包头信息

	private Vector body; // 包体信息

	private Vector row; // 行缓冲

	private int rowIdx; // 行序号，以１开始的自然数

	private final static int ERR_TAG = 0x05;

	private final static int DELI = 0x08;

	private final static int MODULE_TAG = 0x06;

	private final static int ROW_TAG = 0x07;

	private final static int END_TAG = 0x09;

	private final static int TAG_LENGTH = 4; // tag所占的位数(wiseking add)

	// private Logger logger = null;

	/**
	 * 数据包类构造函数
	 *
	 */
	public DataPackage() {
		rowIdx = 0;
		header = new Vector();
		body = new Vector();
		// 日志处理初始化
		// logger = Logger.getLogger(TuxConnector.class);
		// BasicConfigurator.configure();
		// logger.setLevel(Level.INFO);
	}

	/**
	 * 向包中添加新的块描述
	 *
	 * @param tag
	 *            块标志
	 * @param colNameList
	 *            块字段列表
	 *            <p>
	 *            块标志是识别块的唯一标志，各个块有不同的块标志。 如果添加的块标志已经在块中存在，则不进行任何动作。
	 */
	public void AddModule(int tag, String colNameList) {
		String colName;
		int pos, idx = 0;

		StructPackageHeader headEle = new StructPackageHeader(tag, body.size());

		headEle.colNameList = colNameList;

		while ((pos = colNameList.indexOf(",")) != -1) {
			colName = colNameList.substring(0, pos);
			colNameList = colNameList.substring(pos + 1);
			headEle.colList.put((Object) colName, Integer.toString(idx));
			idx++;
		}
		if (colNameList != "")
			headEle.colList.put((Object) colNameList, Integer.toString(idx));

		header.add(headEle);

	}

	/**
	 * 申请向块中添加一行。
	 */
	public void AddRow() {
		StructPackageHeader headEle = null;

		headEle = (StructPackageHeader) header.lastElement();
		row = new Vector();
		for (int i = 0; i < headEle.colList.size(); i++)
			row.add("");
	}

	/**
	 * 向包提交新增的一行数据
	 */
	public void SaveRow() {
		StructPackageHeader headEle;

		headEle = (StructPackageHeader) header.lastElement();
		body.add(row);
		headEle.size++;
	}

	/**
	 * 设定当前行要写入内容
	 *
	 * @param colName
	 *            字段名称
	 * @param colContent
	 *            字段内容
	 *            <p>
	 *            如果描述的列名不存在，则不向行中写入内容。
	 */
	public void SetColContent(String colName, String pContent) {
		StructPackageHeader headEle = null;
		int colIdx;

		headEle = (StructPackageHeader) header.lastElement();
		String strColIdx = (String) headEle.colList.get((Object) colName);

		if (strColIdx == null)
			return;

		colIdx = Integer.parseInt(strColIdx);

		row.setElementAt(pContent, colIdx);
	}

	/**
	 * 设定当前行要写入内容
	 *
	 * @param colIdx
	 *            字段索引
	 * @param colContent
	 *            字段内容 方冰增加 2003/03/13
	 */
	public void SetColContent(int colIdx, String pContent) {
		row.setElementAt(pContent, colIdx);
	}

	/**
	 * 顺序取出数据到行缓冲
	 *
	 * @return 取出行的块标志
	 *         <p>
	 *         返回值 <br>
	 *         END_TAG 取到整个数据块结束 <br>
	 *         ERR_TAG 内部错误 其它 对应行的块标志<br>
	 *         如果取到包结束，返回END_TAG，再继续调用本函数，则从包首从新开始取数据。
	 */
	public int FetchRow() {
		int tag = ERR_TAG;
		StructPackageHeader headEle = null;

		rowIdx++;
		if (rowIdx > body.size()) {
			rowIdx = 0;
			return END_TAG;
		}

		for (int i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			if (headEle.firstRow < rowIdx)
				tag = headEle.tag;
			else
				break;
		}

		return tag;
	}

	/**
	 * 取指定块指定行的数据
	 *
	 * @param tag
	 *            块标志指定块的标志
	 * @param rowNum
	 *            指定块内的序号从1开始的自然数
	 * @return 取出行的块标志<br>
	 *         <p>
	 *         specially condition： 1.对应块在包中不存在，return END_TAG
	 *         2.对应块的对应行在包中不存在，return END_TAG
	 */
	public int FetchRow(int tag, int rowNum) {
		StructPackageHeader headEle = null;

		if (rowNum < 1)
			return END_TAG;
		for (int i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			if (headEle.tag == tag)
				break;
		}
		if (headEle.tag != tag)
			return END_TAG;
		else if (headEle.size < rowNum)
			return END_TAG;
		else {
			this.rowIdx = headEle.firstRow + rowNum;
			return headEle.tag;
		}
	}

	/**
	 * 取出当前行的字段内容
	 *
	 * @param colName
	 *            字段名称
	 * @return 字段内容
	 *         <p>
	 *         返回处于行缓冲区对应字段的内容。如果字段不存在，则返回""
	 */
	public String GetColContent(String colName) {
		int colIdx = 0;
		StructPackageHeader headEle = null;
		HashMap colList = null;

		for (int i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			if (rowIdx > headEle.firstRow)
				colList = headEle.colList;
		}
		if (colList == null)
			return "";

		String strColIdx = (String) colList.get((Object) colName);
		if (strColIdx == null)
			return "";

		colIdx = Integer.parseInt(strColIdx);

		return (String) ((Vector) body.elementAt(rowIdx - 1)).elementAt(colIdx);
	}

	/**
	 * 取出指定块指定行指定字段的内容
	 *
	 * @param tag
	 *            块标志
	 * @param rowNum
	 *            行号从1开始的整型序号
	 * @param colName
	 *            字段名称
	 * @return 字段内容
	 *         <p>
	 *
	 *         Specially condition: 1.块在包中不存在，返回"" 2.行号块中不存在，返回"" 3.字段行中不存在，返回""
	 */
	public String GetColContent(int tag, int rowNum, String colName) {
		int firstRow = 0;
		int i;
		int colIdx;

		StructPackageHeader headEle = null;

		if (rowNum < 1)
			return "";

		for (i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			if (headEle.tag == tag) {
				firstRow = headEle.firstRow;
				break;
			}
		}
		if (i >= header.size() || rowNum > headEle.size)
			return "";

		String strColIdx = (String) headEle.colList.get((Object) colName);
		if (strColIdx == null)
			return "";
		else
			colIdx = Integer.parseInt(strColIdx);

		return (String) ((Vector) body.elementAt(firstRow + rowNum - 1))
				.elementAt(colIdx);
	}

	/**
	 * wiseking add at 2004 03 03 根据tag值获得所对应的内容(二维vector)
	 *
	 * @param tag
	 * @return
	 */
	public Vector getContent(int tag) {
		int firstRow = -1;
		int iTolRows = -1;
		for (int i = 0; i < header.size(); i++) {
			StructPackageHeader headEle = (StructPackageHeader) header
					.elementAt(i);
			if (headEle.tag == tag) {
				firstRow = headEle.firstRow;
				iTolRows = headEle.size;
				break;
			}
		}
		if (firstRow == -1)
			return null;

		Vector retV = new Vector();
		for (int i = 0; i < iTolRows; i++) {
			retV.add(body.elementAt(firstRow + i));
		}

		return retV;
	}

	/**
	 * 取得package中所包含的所有模块的tag值，构成int[] 数组
	 *
	 * @author add by King at 2004.03.25
	 * @return
	 */
	public int[] getModuleTags() {
		int[] iRets = new int[this.header.size()];
		StructPackageHeader headEle = null;
		for (int i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			iRets[i] = headEle.tag;
		}

		return iRets;
	}

	/**
	 * 取出整个包的大小
	 *
	 * @return 包的大小
	 */
	public int size() {
		int count = 0;

		for (int i = 0; i < header.size(); i++)
			count += ((StructPackageHeader) header.elementAt(i)).size;

		return count;
	}

	/**
	 * 取出指定块的到小
	 *
	 * @return 块的大小
	 */
	public int size(int tag) {
		int count = 0;
		StructPackageHeader headEle = null;

		for (int i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			if (headEle.tag == tag)
				count = headEle.size;
		}

		return count;
	}

	/**
	 * 卸出本对象实例到文件中
	 */
	public void dump() {
		int i;

		// System.out.println("DataPackage dumping begin :");
		// System.out.println("Header Vector dump : size (" + header.size() +
		// ")");
		for (i = 0; i < header.size(); i++) {
			StructPackageHeader headEle = (StructPackageHeader) header
					.elementAt(i);
			// System.out.print("tag = " + headEle.tag + " first_row = "
			// + headEle.firstRow + " size = " + headEle.size);

			@SuppressWarnings("unused")
			String colNameList = "";
			String colName = "";
			for (int size = 0; size < headEle.colList.size(); size++) {
				Iterator it = headEle.colList.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					if (Integer.parseInt((String) headEle.colList.get(key)) == size) {
						colName = key;
						break;
					}
				}
				colNameList += colName
						+ ((size == headEle.colList.size() - 1) ? "." : ",");
			}
			// System.out.println(" colName = " + colNameList);
		}
		// System.out.println("Header Vector dump end!");
		// System.out.println("Body Vector dump : size (" + body.size() + ")");
		for (i = 0; i < body.size(); i++) {
			Vector vtRecord = (Vector) body.elementAt(i);
			@SuppressWarnings("unused")
			String colContent = "";
			for (int size = 0; size < vtRecord.size(); size++)
				colContent += vtRecord.elementAt(size)
						+ ((size == vtRecord.size() - 1) ? "." : ",");
			// System.out.println("row (" + i + ") :" + colContent);
		}
		// System.out.println("Body Vector dump end !");
		// System.out.println("rowIdx varible value = " + this.rowIdx);
		// System.out.println("DataPackage dumping end !");
	}

	/**
	 * 将数据包转换成字符串
	 *
	 * @author 方冰 2003/03/07
	 */
	public String DumpIntoString() {
		int i, j;
		StringBuffer CommSnd = new StringBuffer(1000);
		for (i = 0; i < header.size(); i++) {
			StructPackageHeader headEle = (StructPackageHeader) header
					.elementAt(i);
			CommSnd.append(String.valueOf((char) MODULE_TAG));
			// 左补零 直到长度为 TAG_LENGTH
			String tagStr = "" + headEle.tag;
			while (tagStr.length() < TAG_LENGTH)
				tagStr = "0" + tagStr;
			// CommSnd += String.valueOf((char)headEle.tag);
			CommSnd.append(tagStr);
			// System.out.println("tag = " + headEle.tag + " first_row = "
			// + headEle.firstRow + " size = " + headEle.size);

			String colNameList = "";
			String colName = "";
			for (int size = 0; size < headEle.colList.size(); size++) {
				Iterator it = headEle.colList.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					if (Integer.parseInt((String) headEle.colList.get(key)) == size) {
						colName = key;
						break;
					}
				}
				colNameList += colName
						+ ((size == headEle.colList.size() - 1) ? "" : ",");
			}
			CommSnd.append(colNameList);
			// System.out.println(" colName = " + colNameList);
			CommSnd.append(String.valueOf((char) DELI));
			for (j = headEle.firstRow; j < headEle.firstRow + headEle.size; j++) {
				Vector vtRecord = (Vector) body.elementAt(j);
				@SuppressWarnings("unused")
				String colContent = "";
				CommSnd.append(String.valueOf((char) ROW_TAG));

				for (int size = 0; size < vtRecord.size(); size++) {
					CommSnd.append(PbEncode(vtRecord.elementAt(size).toString()));
					CommSnd.append(String.valueOf((char) DELI));
					colContent += vtRecord.elementAt(size)
							+ ((size == vtRecord.size() - 1) ? "." : ",");
				}
				// System.out.println("row (" + j + ") :" + colContent);
			}
		}

		CommSnd.append(String.valueOf((char) END_TAG));
		CommSnd.append(String.valueOf(0));
		// System.out.println("CommSnd.length="+CommSnd.toString().getBytes().length);
		return CommSnd.toString();
	}

	/**
	 * 将字符串转换成数据包
	 *
	 * @author 方冰 2003/03/13
	 */
	public void StringIntoPkg(String CommRcv) {

		String whole_str = null;
		String module_str = null;
		String value_str = null;
		StringTokenizer colNameList_st = null;
		String colNameList_str = null;
		int tag = 0, len;
		StringTokenizer whole_st = new StringTokenizer(CommRcv,
				String.valueOf((char) END_TAG));
		while (whole_st.hasMoreTokens()) {
			whole_str = whole_st.nextToken();
			// saveFile(whole_str.getBytes(),"e:/java_dev/dev_xml",
			// "whole_str"+(i++)+".txt");
		}

		StringTokenizer module_st = new StringTokenizer(whole_str,
				String.valueOf((char) MODULE_TAG));
		while (module_st.hasMoreTokens()) {
			module_str = module_st.nextToken();
			// wiseking changed it tag length is TAG_LENGTH
			// tag = module_str.charAt(0);
			if (module_str == null || module_str.length() < TAG_LENGTH) {
				System.err.println("module_tag error, pls care it!");
				// System.out.println("module_str="+module_str);
				continue;
			}
			String tagStr = module_str.substring(0, TAG_LENGTH);
			// System.out.println("tagStr="+tagStr);
			try {
				tag = Integer.parseInt(tagStr);
			} catch (Exception e) {
				System.err.println("parse tag to int error, pls care it!");
				// System.out.println("tagStr="+tagStr);
				continue;
			}
			colNameList_st = new StringTokenizer(module_str,
					String.valueOf((char) DELI));
			colNameList_str = colNameList_st.nextToken().substring(TAG_LENGTH);
			AddModule(tag, colNameList_str);
			len = colNameList_str.length() + TAG_LENGTH + 1;
			value_str = module_str.substring(len);
			/* 这个方法比较慢， 真的假的? */
			String[] arrRows = value_str.split(String.valueOf((char) ROW_TAG));
			for (int ir = 0; arrRows != null && ir < arrRows.length; ir++) {
				AddRow();
				String[] arrCols = arrRows[ir].split(String
						.valueOf((char) DELI));
				for (int ic = 0; arrCols != null && ic < arrCols.length; ic++) {
					SetColContent(ic, PbDecode(arrCols[ic]));
				}
				SaveRow();
			}
			/*
			 * row_st = new StringTokenizer(value_str, String.valueOf( (char)
			 * ROW_TAG)); while (row_st.hasMoreTokens()) { row_str =
			 * row_st.nextToken(); AddRow(); idx = 0; if
			 * (row_str.startsWith(String.valueOf( (char) DELI))) row_str =
			 * "&;wkkw;&" + row_str; //wiseking add here at 2004.02.10 strFind =
			 * String.valueOf( (char) DELI) + String.valueOf( (char) DELI);
			 * strRep = String.valueOf( (char) DELI) + "&;wkkw;&" +
			 * String.valueOf( (char) DELI); strHead = null; strTail = null; int
			 * intFrom = row_str.indexOf(strFind, 0); int intTo = 0; while
			 * (intFrom >= 0) { intTo = intFrom + strFind.length(); strHead =
			 * row_str.substring(0, intFrom); strTail =
			 * row_str.substring(intTo); row_str = strHead + strRep + strTail;
			 * intTo = intFrom + strRep.length() - 1; intFrom =
			 * row_str.indexOf(strFind, intTo); } content_st = new
			 * StringTokenizer(row_str, String.valueOf( (char) DELI)); while
			 * (content_st.hasMoreTokens()) { tmpS = content_st.nextToken(); if
			 * ("&;wkkw;&".equals(tmpS)) SetColContent(idx, ""); else
			 * SetColContent(idx, PbDecode(tmpS)); idx++; } SaveRow();
			 * content_st = null; }
			 */
			colNameList_st = null;
		}
		dump();

	}

	/**
	 * PbEncode() 为转换字符串，为WINDOW下中文字符与UNIX下的连接
	 *
	 * @param orgStr
	 * @return pRtnBuff
	 * @author 方冰
	 */
	public String PbEncode(String orgData) {

		String pRtnBuff = new String("");
		String cc = new String("");
		String orgStr = null;
		char p;
		int i = 0;

		try {
			orgStr = new String(orgData.getBytes(), "ISO-8859-1");
		} catch (Exception ex) {
		}

		for (i = 0; i < orgStr.length(); i++) {
			p = orgStr.charAt(i);
			cc = String.valueOf(p);
			if (p == '%') {
				cc = "%25";
			} else if (p >= 128) {
				cc = "%" + Integer.toHexString(p).toUpperCase();
			}
			pRtnBuff += cc;
		}

		return pRtnBuff;

	}

	/**
	 * PbEncode() 为转换字符串，为WINDOW下中文字符与UNIX下的连接
	 *
	 * @param orgStr
	 * @return pRtnBuff 方冰
	 */
	public static String PbDecode(String codStr) {
		char p;
		String cc = new String("");
		String codData = new String("");
		int i = 0;

		codData = "";
		for (i = 0; i < codStr.length(); i++) {
			p = codStr.charAt(i);
			cc = String.valueOf(p);
			if ('%' == p) {
				if (codStr.substring(i, i + 3).equals("%25")) {
					i = i + 2;
				} else {
					p = codStr.charAt(i + 1);
					if ((p >= '8' && p <= '9') || (p >= 'A' && p <= 'F')) {
						p = (char) Integer.parseInt(
								codStr.substring(i + 1, i + 3), 16);
						cc = String.valueOf(p);
						i = i + 2;
					}
				}
			}
			codData += cc;
		}
		try {
			String pRtnBuff = new String(codData.getBytes("ISO8859-1"),
					"GB2312");
			return pRtnBuff;
		} catch (Exception ex) {
		}
		return "";
	}

	/**
	 * 取出指定块对应的字段名称列表,孟梅2003/02/05
	 */
	public String GetColNameList(int tag) {
		StructPackageHeader headEle = null;
		String strColNameList = "";

		for (int i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			if (headEle.tag == tag)
				strColNameList = headEle.colNameList;
		}
		return strColNameList;
	}

	/**
	 * 向包提交新增的一块数据,孟梅2003/02/05
	 */
	public void SaveVector(Vector v) {
		StructPackageHeader headEle;

		headEle = (StructPackageHeader) header.lastElement();
		body = v;
		headEle.size += v.size();
	}

	/**
	 * 取出指定块的内容,孟梅2003/02/05
	 *
	 * @param tag
	 *            块标志
	 * @return 块内容
	 *         <p>
	 */
	public Vector GetVector(int tag) {
		int firstRow = 0;
		int size = 0;
		int i;

		StructPackageHeader headEle = null;

		for (i = 0; i < header.size(); i++) {
			headEle = (StructPackageHeader) header.elementAt(i);
			if (headEle.tag == tag) {
				firstRow = headEle.firstRow;
				size = headEle.size;
				break;
			}
		}
		if (i >= header.size())
			return null;

		Vector v = new Vector();
		for (int j = firstRow; j < firstRow + size; j++) {
			v.add((Vector) body.elementAt(j));
		}

		return v;
	}

	// add by xiebin
	public DataPackage clear() {
		rowIdx = 0;
		header = new Vector();
		body = new Vector();
		return this;
	}

	/*
	 *
	 */
	public DataPackage loadFromXml(String file) throws DataException {
		DocumentBuilderFactory buildFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = buildFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// logger.error("New DocumentBuilder", e);
			throw new DataException("loadFromXml");
		}

		Document xmlDoc = null;
		try {
			xmlDoc = builder.parse(file);
		} catch (SAXException e) {
			// logger.error("parse xml document error", e);
			throw new DataException("parse xml document");
		} catch (IOException e) {
			// logger.error("parse xml document", e);
			throw new DataException("parse xml document");
		}

		// 从XML文件中加载DataPackage
		this.clear();
		Element root = xmlDoc.getDocumentElement();
		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element)
				appendModule((Element) list.item(i));
		}
		return this;
	}

	/*
	 *
	 */
	public void saveToXml(String file) throws DataException {

		XmlDocument xmlDoc = new XmlDocument();
		Element root = xmlDoc.createElement("DataPackage");
		xmlDoc.appendChild(root);
		for (int i = 0; i < header.size(); i++) {
			StructPackageHeader ele = (StructPackageHeader) header.elementAt(i);
			root.appendChild(getModuleElement(xmlDoc, ele.tag));
		}

		// 把XML文档输出到指定的文件
		try {
			FileOutputStream outStream = new FileOutputStream(file);
			OutputStreamWriter outWriter = new OutputStreamWriter(outStream);
			xmlDoc.write(outWriter, "GB2312");
			outWriter.close();
			outStream.close();
		} catch (Exception e) {
			// logger.warn("Write DataPackage to XML error", e);
			throw new DataException("Write DataPackage Error");
		}
	}

	/*
	 *
	 */
	public DataPackage appendModule(Element module) {
		int tag = Integer.parseInt(module.getAttribute("Tag"));

		NodeList rowList = module.getElementsByTagName("Row");
		for (int i = 0; i < rowList.getLength(); i++) {
			NodeList columns = rowList.item(i).getChildNodes();

			// 得到colum list
			if (i == 0) {
				StringBuffer colList = new StringBuffer();
				for (int j = 0; j < columns.getLength(); j++) {
					if (columns.item(j) instanceof Element) {
						colList.append(columns.item(j).getNodeName()).append(
								",");
					}
				}
				colList.deleteCharAt(colList.length() - 1);
				AddModule(tag, colList.toString());
			}

			// 加入行
			AddRow();
			for (int j = 0; j < columns.getLength(); j++) {
				Node value = columns.item(j).getFirstChild();
				if (value == null)
					continue;
				try {
					SetColContent(columns.item(j).getNodeName(),
							value.getNodeValue());
				} catch (Exception e) {
					System.err.println("SetColContent Sytem ERROR!第[" + i
							+ "]行,第[" + j + "]列,列名["
							+ columns.item(j).getNodeName() + "]时出错,请检查你的数据！");
					e.printStackTrace();
				}
			}
			SaveRow();
		}

		return this;
	}

	/*
	 *
	 */
	public Node getModuleElement(Document doc, int tag) {
		try {
			Element module = doc.createElement("Module");
			module.setAttribute("Tag", String.valueOf(tag));

			StructPackageHeader headEle = null;

			for (int i = 0; i < header.size(); i++) {
				StructPackageHeader ele = (StructPackageHeader) header
						.elementAt(i);
				if (ele.tag == tag) {
					headEle = ele;
					break;
				}
			}
			if (headEle == null) {
				return null;
			}

			for (int i = 0; i < headEle.size; i++) {
				Element row = doc.createElement("Row");
				Iterator colIter = headEle.colList.keySet().iterator();
				while (colIter.hasNext()) {
					String name = (String) colIter.next();
					Element column = null;
					try {
						column = doc.createElement(name);
					} catch (Exception e) {
						System.err.println("#ErrorField = " + name);
						column = doc.createElement("ErrorField_"
								+ System.currentTimeMillis());
					}
					String value = GetColContent(tag, i + 1, name);
					column.appendChild(doc.createTextNode(value));
					row.appendChild(column);
				}
				module.appendChild(row);
			}

			return module;
		} catch (Exception e) {
			System.err.println("#Error DataPackage.getModuleElement() :");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回xml格式的内容 add By King at 2004.04.20
	 *
	 * @return
	 */
	public String toXml() {
		XmlDocument xmlDoc = new XmlDocument();
		Element root = xmlDoc.createElement("DataPackage");
		xmlDoc.appendChild(root);
		for (int i = 0; i < header.size(); i++) {
			StructPackageHeader ele = (StructPackageHeader) header.elementAt(i);
			root.appendChild(getModuleElement(xmlDoc, ele.tag));
		}

		try {
			xmlDoc.normalize();
			OutputFormat format = new OutputFormat(xmlDoc); // Serialize DOM
			format.setEncoding("GB2312");
			format.setVersion("1.0");
			format.setIndenting(true);
			StringWriter stringOut = new StringWriter(); // Writer will be a
			// String
			XMLSerializer serial = new XMLSerializer(stringOut, format);
			serial.asDOMSerializer(); // As a DOM Serializer
			serial.serialize(xmlDoc.getDocumentElement());

			return stringOut.toString(); // Spit out DOM as a String
		} catch (Exception e) {
			return "ERROR XML CONTENT!";
		}
	}

	public static boolean saveFile(byte[] content, String dir, String fileName) {
		try {
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				// Directory doesn't exist, create it
				fileDir.mkdirs();
			}
			File file = new File(dir, fileName);

			FileOutputStream fileoutputstream = new FileOutputStream(file);
			fileoutputstream.write(content);
			fileoutputstream.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static void main(String[] args) {
		String wiseking = "1;2;;4;5";
		String[] t = wiseking.split(";");
		for (int i = 0; i < t.length; i++) {
			System.out.println("t=" + t[i]);
		}
	}
}