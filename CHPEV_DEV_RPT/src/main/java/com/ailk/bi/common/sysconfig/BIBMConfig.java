package com.ailk.bi.common.sysconfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BIBMConfig extends DefaultHandler implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 404236223123605547L;

	BIBMConfig bibmConfig = null;

	/** 存放tag和Tag的值 */

	private HashMap tagMap = new HashMap();

	private String tagValue = null;

	// 配置文件中几个固定tag的定义
	// 0 app_conntype
	private final static String DB_CTYPE_TAG = "db_ctype";

	// 1 app_url
	private final static String APP_URL_TAG = "db_url";

	// 2 app_user
	private final static String APP_USER_TAG = "db_user";

	// 3 app_pwd
	private final static String APP_PWD_TAG = "db_pwd";

	// 4 app_datasource
	private final static String APP_DATASOURCE_TAG = "db_datasource";

	// 5 err_file
	private final static String ERR_FILE_TAG = "err_file";

	// 6 page_file
	private final static String PAGE_FILE_TAG = "page_file";

	// 7 classtag_file
	private final static String CLASS_TAG_TAG = "classtag_file";

	// 8 login的url 通常为login.do 这里是为了判断系统是否合法登陆
	private final static String LOGIN_TAG_TAG = "login_url";

	private final static String UPLOAD_FOLDER = "upload_fold";
	
	private final static String APP_DATASOURCE_SYS="db_datesoucesys";
	
	private final static String YUYAN_URL = "yuyan_url";

	public String getYuyanUrl() {
		return getExtParam(YUYAN_URL);
	}

	public String getAppDataSys() {
		return getExtParam(APP_DATASOURCE_SYS);
	}

	public String getClassTagFile() {
		return getExtParam(CLASS_TAG_TAG);
	}

	public String getLoginURL() {
		String loginURL = getExtParam(LOGIN_TAG_TAG);
		if (loginURL == null)
			return "login.do"; // 默认的登陆url
		else
			return loginURL;
	}

	public String getDBCType() {
		return getExtParam(DB_CTYPE_TAG);
	}

	public String getAppURL() {
		return getExtParam(APP_URL_TAG);
	}

	public String getAppUser() {
		return getExtParam(APP_USER_TAG);
	}

	public String getAppPWD() {
		return getExtParam(APP_PWD_TAG);
	}

	public String getAppDataSource() {
		return getExtParam(APP_DATASOURCE_TAG);
	}

	public String getErrFile() {
		return getExtParam(ERR_FILE_TAG);
	}

	public String getPageFile() {
		return getExtParam(PAGE_FILE_TAG);
	}

	public String getUploadFolder() {
		return getExtParam(UPLOAD_FOLDER);
	}

	/**
	 * 读取配置文件中自定义tag所对应的值
	 *
	 * @param tagName
	 * @return
	 */
	public String getExtParam(String tagName) {
		if (tagName == null)
			return null;
		tagName = tagName.toUpperCase();
		if (tagMap != null && tagMap.containsKey(tagName)) {
			return (String) tagMap.get(tagName);
		} else
			return null;
	}

	public boolean isGb_trans() {
		String v = getExtParam("gb_trans");
		if ("TRUE".equalsIgnoreCase(v))
			return true;
		else
			return false;
	}

	public void startDocument() throws SAXException {
		bibmConfig = this;
	}

	// 对每一个开始元属进行处理
	public void startElement(String namespaceURI, String localName,
			String rawName, Attributes atts) throws SAXException {
		tagValue = null;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		tagValue = new String(ch, start, length);
	}

	// 解析完成后的统计工作
	public void endElement(String namespaceURI, String tagName, String qName)
			throws SAXException {
		if (tagName == null || "".equals(tagName))
			tagName = qName;
		if (tagName != null && tagValue != null) {
			tagMap.put(tagName.toUpperCase(), tagValue.trim());
		}
	}

	public void endDocument() throws SAXException {
	}

	/**
	 * 处理表映射的操作
	 *
	 * @param fileName
	 *            映射的文件名
	 * @return 表映射类
	 */
	public BIBMConfig doConfig(String fileName) {
		tagMap.clear();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		XMLReader xmlReader = null;
		SAXParser saxParser = null;

		try {
			// 创建一个解析器SAXParser对象
			saxParser = spf.newSAXParser();

			// 得到SAXParser中封装的SAX
			xmlReader = saxParser.getXMLReader();
		} catch (Exception ex) {
			return null;
		}

		try {
			xmlReader.setContentHandler(this);
			xmlReader.parse(new InputSource(new FileInputStream(fileName)));
		} catch (SAXException se) {
			System.err.println(se.getMessage());
			return null;
		} catch (IOException ioe) {
			System.err.println(ioe);
			return null;
		}

		return bibmConfig;
	}

	/** 打印读取的配置文件信息 */

	public void printInfo() {
		String key = null;
		System.out
				.println("********************Cofing Info Begin***********************");
		if (tagMap == null) {
			System.out.println("no any tag defined!");
			return;
		}
		java.util.Iterator it = tagMap.keySet().iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			System.out.println(key + "=" + tagMap.get(key));
		}
		System.out
				.println("********************Cofing Info End***********************");
	}

	public static void main(String[] args) {
		BIBMConfig c1 = new BIBMConfig();
		if (args.length > 1)
			c1.doConfig(args[1]);
		else
			c1.doConfig("classes\\lsbi_config.xml");
		c1.printInfo();
	}
}