package com.ailk.bi.common.taglib;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.event.DataSet;
import com.ailk.bi.common.event.JBTable;
import com.ailk.bi.common.event.JBTableBase;

/**
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description: 用于实现下拉列表框的taglib的实现的帮助类，
 * 在WEB-INF/taglibmappings.xml中定义了taglib处理类的配置文件的相关系信息
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TaglibHelper extends HttpServlet implements ServletContextListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4527775403139491405L;

	private String TAGLIB_MAPFILE = "/WEB-INF/config/taglibmappings.xml";

	private static final String PARA_NAME = "tagselect_mappings";

	private static final String TAG_TAGLIBMAP = "smapping";

	private static final String ATTR_ID = "ID";

	private static final String ATTR_SQL = "SQL";

	private static final String ATTR_VALUES = "VALUES";

	private static final String CTX_ATTR_TAGLIBMAPS = "taglib.CTX_TAGLIBMAPS";

	// 存放从后台交易中获得的参数信息

	private static HashMap TAGLIBMAPS_BK = null;

	// 存放从后台交易中获得的参数其它对象信息

	private static HashMap PARAOTHER_BK = new HashMap();

	private ServletContext context = null;

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		context.removeAttribute(CTX_ATTR_TAGLIBMAPS);
	}

	public void contextInitialized(ServletContextEvent event) {
		// 注意只能通过event.getServletContext()取得context之后用context进行参数读取等操作，否则会异常
		context = event.getServletContext();
		String file = context.getInitParameter(PARA_NAME);

		if (file != null && !"".equals(file))
			TAGLIB_MAPFILE = file;
		initTaglibMappings(context);

		// 读取后台交易的参数信息
	}

	private void initTaglibMappings(ServletContext context) {
		java.net.URL taglibURL = null;

		try {
			taglibURL = context.getResource(TAGLIB_MAPFILE);
		} catch (java.net.MalformedURLException ex) {
			System.err.println("Cann't FIND taglib mappping definiation file: "
					+ ex);
		}

		if (taglibURL != null) {
			HashMap taglibmaps = loadTaglibMappings(taglibURL);
			context.setAttribute(CTX_ATTR_TAGLIBMAPS, taglibmaps);
			if (taglibmaps == null) {
				System.err
						.println("Template Servlet Error Loading taglib mappings: Confirm that file at URL "
								+ TAGLIB_MAPFILE
								+ " contains the screen definitions");
			}
		} else {
			System.err
					.println("Template Servlet Error Loading taglib mappings: URL "
							+ TAGLIB_MAPFILE + " not found");
		}
	}

	public static HashMap getTaglibMapps(ServletContext ctx) {
		HashMap hm = (HashMap) ctx.getAttribute(CTX_ATTR_TAGLIBMAPS);
		return hm;
	}

	/**
	 * 设置设置了后台参数信息
	 * 
	 * @return
	 */
	public static boolean isSetTaglibMapps_BK() {
		if (TAGLIBMAPS_BK == null)
			return false;
		return true;
	}

	public static HashMap getTaglibMapps_BK() {
		HashMap hm = (HashMap) TAGLIBMAPS_BK;
		return hm;
	}

	/**
	 * 设置从后台读取的参数信息
	 * 
	 * @param hm
	 * @param hm
	 */
	public static void setTaglibMapps_BK(HashMap hm) {
		TAGLIBMAPS_BK = hm;
	}

	/**
	 * 此方法，只有在系统初次启动时，设置从后台读取的参数其它对象信息； 其它程序中不应该
	 * 
	 * @param ctx
	 * @param hm
	 */
	public static void setParaObj_BK(int tagNum, JBTableBase[] tables) {
		PARAOTHER_BK.put(tagNum + "", tables);
	}

	/**
	 * 读取的参数其它对象信息,义对象数组的形式返回，如果没有发现则返回null
	 * 
	 * @param ctx
	 * @param hm
	 */
	public static JBTableBase[] getParaObj(int tagNum) {
		return (JBTableBase[]) PARAOTHER_BK.get(tagNum + "");
	}

	/**
	 * 从所有对象中过滤出满足语句条件的对象，并以二维Vector方式的形式显示 sql格式
	 * "SELECT=field1,field2,...;WHERE=fieldi=xx,fieldj==xx,..."
	 * 
	 * @param allObjs
	 * @param sql
	 * @return
	 */
	public static Vector trimObjsByCond(Vector allObjs, String sql)
			throws AppException {
		HashMap selectFields = new HashMap(); // key=field value=sequence
		HashMap whereFields = new HashMap(); // key=field value=fieldvalue

		if (sql == null)
			throw new AppException("条件语句为空");

		sql = sql.trim();
		if (!sql.substring(0, 7).equalsIgnoreCase("SELECT="))
			throw new AppException(
					"条件语句格式为SELECT=field1,field2,...;WHERE=field1=xx,...");
		int iS = sql.toUpperCase().indexOf(";WHERE=");
		if (iS < 0)
			throw new AppException(
					"条件语句格式为SELECT=field1,field2,...;WHERE=field1=xx,...");
		String sSelect = sql.substring(7, iS);
		String sWhere = sql.substring(iS + 7);
		StringTokenizer st = new StringTokenizer(sSelect, ",");
		int iC = 0;
		while (st.hasMoreElements()) {
			String tmp = (String) st.nextElement();
			selectFields.put(tmp.trim().toLowerCase(), (iC++) + "");
		}
		st = new StringTokenizer(sWhere, ",");
		iC = 0;
		StringBuffer key = new StringBuffer();

		while (st.hasMoreElements()) {
			String tmp = (String) st.nextElement();
			tmp = tmp.trim();
			int i = 0;
			for (; i < tmp.length(); i++) {
				if (tmp.charAt(i) != '=') { // 目前只支持=,以后考虑> < >= <=的情况
					key.append(tmp.charAt(i));
				} else
					break;
			}
			String value = tmp.substring(i + 1).trim();
			StringB.trim(value, "'");
			whereFields.put(key.toString().toLowerCase(), value);
		}

		Iterator it = whereFields.keySet().iterator();
		while (it.hasNext()) {
			String field = (String) it.next();
			allObjs = trimObjs(allObjs, field, (String) whereFields.get(field));
			if (allObjs == null)
				throw new AppException("条件语句中WHERE部分的字段有错误");
		}
		Vector vRet = trimFields(allObjs, selectFields);
		if (vRet == null)
			throw new AppException("条件语句中SELECT部分的字段有错误");

		return vRet;
	}

	/**
	 * 找到要选择的列
	 * 
	 * @param trimObjs
	 * @param fields
	 * @return
	 */
	static private Vector trimFields(Vector trimObjs, HashMap fields) {
		Vector vRet = new Vector();
		int[] iFields = new int[fields.keySet().size()];
		int iC = 0;
		Vector tV = (Vector) trimObjs.elementAt(0);
		Vector vTitle = new Vector();
		for (int i = 0; i < tV.size(); i++) {
			if (fields.containsKey((String) tV.elementAt(i))) {
				iFields[iC++] = i;
				vTitle.add((String) tV.elementAt(i));
			}
		}
		if (fields.keySet().size() != iC)
			return null;
		vRet.add(vTitle);
		for (int i = 1; i < trimObjs.size(); i++) {
			Vector v = (Vector) trimObjs.elementAt(i);
			Vector nV = new Vector();
			for (int j = 0; j < v.size(); j++) {
				for (int k = 0; k < iC; k++) {
					if (j == iFields[k]) {
						nV.add(v.elementAt(j));
						break;
					}
				}
			}
			vRet.add(nV);
		}
		return vRet;
	}

	/**
	 * 找到要选择的行
	 * 
	 * @param allObjs
	 * @param field
	 * @param value
	 * @return
	 */
	static private Vector trimObjs(Vector allObjs, String field, String value) {
		Vector vRet = new Vector();
		int iField = -1;
		Vector tV = (Vector) allObjs.elementAt(0);
		for (int i = 0; i < tV.size(); i++) {
			if (field.equals((String) tV.elementAt(i))) {
				iField = i;
				break;
			}
		}

		if (iField == -1) // 字段有问题
			return null;

		// 放入第一行，字段的定义
		vRet.add(tV);
		for (int i = 1; i < allObjs.size(); i++) {
			tV = (Vector) allObjs.elementAt(i);
			if (((String) (tV.elementAt(iField))).equals(value)) {
				vRet.add(tV);
			}
		}
		return vRet;
	}

	private HashMap loadTaglibMappings(URL url) {
		Document doc = null;
		Element root = null;
		try {
			InputSource xmlInp = new InputSource(url.openStream());

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
			doc = parser.parse(xmlInp);
			root = doc.getDocumentElement();
			root.normalize();

			HashMap retHM = new HashMap();
			NodeList rows = root.getElementsByTagName(TAG_TAGLIBMAP);
			String id = null;
			String sql = null;
			String values = null;
			for (int i = 0; i < rows.getLength(); i++) {
				Element map = (Element) rows.item(i);
				id = map.getAttribute(ATTR_ID);
				sql = map.getAttribute(ATTR_SQL);
				values = map.getAttribute(ATTR_VALUES);
				// com.asiabi.common.app.Debug.println("sqlasfsafasdff="+sql);
				if (sql == null || "".equals(sql))
					sql = "$!$" + values;
				retHM.put(id, sql);
			}
			return retHM;
		} catch (SAXParseException err) {
			System.err.println("TaglibMappings ** Parsing error" + ", line "
					+ err.getLineNumber() + ", uri " + err.getSystemId());
			System.err.println("TaglibMappings error: " + err.getMessage());
		} catch (SAXException e) {
			System.err.println("TaglibMappings error: " + e);
		} catch (java.net.MalformedURLException mfx) {
			System.err.println("TaglibMappings error: " + mfx);
		} catch (java.io.IOException e) {
			System.err.println("TaglibMappings error: " + e);
		} catch (Exception pce) {
			System.err.println("TaglibMappings error: " + pce);
		}

		return null;
	}

	/**
	 * 通过指定table数组的显示列和提取条件构造下拉列表显示数据
	 * 
	 * @param tables
	 * @param optionID
	 * @param optionDisp
	 * @return
	 * @throws AppException
	 */
	public static String getListDatas(JBTable[] tables, String optionID,
			String optionDisp) throws AppException {
		return getListDatas(tables, optionID, optionDisp, null);
	}

	/**
	 * 通过指定table数组的显示列和提取条件构造下拉列表显示数据
	 * 
	 * @param tables
	 * @param optionID
	 * @param optionDisp
	 * @param cond
	 * @return
	 * @throws AppException
	 */
	public static String getListDatas(JBTable[] tables, String optionID,
			String optionDisp, String cond) throws AppException {
		DataSet ds = DataSet.tabs2DS(tables);
		return getListDatas(ds, optionID, optionDisp, cond);
	}

	/**
	 * * 通过指定table数组的显示列和提取条件构造下拉列表显示数据
	 * 
	 * @param ds
	 * @param optionID
	 * @param optionDisp
	 * @return
	 * @throws AppException
	 */
	public static String getListDatas(DataSet ds, String optionID,
			String optionDisp) throws AppException {
		return getListDatas(ds, optionID, optionDisp, null);
	}

	/**
	 * 通过指定table数组的显示列和提取条件构造下拉列表显示数据
	 * 
	 * @param tables
	 *            供提取的数组,也可以是DataSet
	 * @param optionID
	 *            option的ID
	 * @param optionDisp
	 *            option的Disp
	 * @param cond
	 *            可选 提取数据的条件，目前只支持 "xxxfield=xxxx"的格式
	 * @return 如果解析成功，则生成 "id1,disp1;id2,disp2;...."格式的串返回，否则返回""
	 */
	public static String getListDatas(DataSet ds, String optionID,
			String optionDisp, String cond) throws AppException {
		if (ds == null)
			throw new AppException(
					"TaglibHelper.getListDatas() 第一个参数为空或者不是JBTable的子类");
		if (ds.getICols() == 0 || ds.getLRows() == 0)
			throw new AppException("TaglibHelper.getListDatas() 第一个参数内容不正确");

		int idP = ds.getColNoByName(optionID);
		if (idP == -1)
			throw new AppException(
					"TaglibHelper.getListDatas() 第二个参数 定义的字段不在数组结构内，请检查！");

		int dispP = ds.getColNoByName(optionDisp);
		if (dispP == -1)
			throw new AppException(
					"TaglibHelper.getListDatas() 第三个参数 定义的字段不在数组结构内，请检查！");

		StringBuffer retSB = new StringBuffer();
		if (cond != null) {
			String condV = null;
			int condP = -2;
			int iPos = cond.indexOf("=");
			if (iPos == -1)
				throw new AppException(
						"TaglibHelper.getListDatas() 条件参数格式错误，请检查！");
			String condF = cond.substring(0, iPos);
			condV = cond.substring(iPos + 1);
			condP = ds.getColNoByName(condF);
			if (condP == -1)
				throw new AppException(
						"TaglibHelper.getListDatas() 条件参数内的字段名错误，请检查！");
			String fenHao = ""; // 小分号在此定义，呵呵
			for (int i = 0; i < ds.getLRows(); i++) {
				if (condV == null || !condV.equals(ds.elementAt(i, condP)))
					continue;
				retSB.append(fenHao + ds.elementAt(i, idP) + ","
						+ ds.elementAt(i, dispP));
				fenHao = ";";
			}
		} else {
			String fenHao = ""; // 小分号在此定义，呵呵
			for (int i = 0; i < ds.getLRows(); i++) {
				retSB.append(fenHao + ds.elementAt(i, idP) + ","
						+ ds.elementAt(i, dispP));
				fenHao = ";";
			}
		}
		return retSB.toString();
	}

	public static void main(String[] args) {
		/*
		 * Vector vAll=new Vector(); Vector tV=new Vector(); tV.add("code_id");
		 * tV.add("code_name"); vAll.add(tV); tV=new Vector(); tV.add("10");
		 * tV.add("12345"); vAll.add(tV); tV = new Vector(); tV.add("20");
		 * tV.add("ABCDE"); vAll.add(tV); tV = new Vector(); tV.add("10");
		 * tV.add("ZXYYY"); vAll.add(tV); try { vAll=trimObjsByCond(vAll,
		 * "SELECT=code_id,code_name;WHERE=code_id=10");
		 * System.out.println("vAll="+vAll); } catch (AppException ex) {
		 * System.out.println("ex="+ex.toString()); } test.Account a=new
		 * test.Account(); a.acctID="123"; a.acctName = "wiseking"; a.custID
		 * ="001";
		 * 
		 * test.Account b=new test.Account(); b.acctID="124"; b.acctName =
		 * "jacky"; b.custID="001";
		 * 
		 * test.Account c=new test.Account(); c.acctID="125"; c.acctName =
		 * "wilson"; c.custID="002";
		 * 
		 * test.Account[] as=new test.Account[3]; as[0]=a; as[1]=b; as[2]=c;
		 * 
		 * try { String ret=TaglibHelper.getListDatas(as, "acctID", "acctName");
		 * System.out.println("ret="+ret); ret=TaglibHelper.getListDatas(as,
		 * "acctID", "acctName", "custID=001"); System.out.println("ret="+ret);
		 * } catch (AppException ex) { ex.printStackTrace(); }
		 */
	}
}