package com.ailk.bi.base.util;

import java.util.*;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.app.XMLUtils;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.common.sysconfig.*;
import com.ailk.bi.report.util.CustomMsuUtil;

import org.w3c.dom.Element;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SQLGenator {

	private static Logger logger = Logger.getLogger(CustomMsuUtil.class);

	public static HashMap hmSQLStore = new HashMap();

	public static List schemaStore = new ArrayList();

	static {
		String fileName = GetSystemConfig.getBIBMConfig().getExtParam(
				"sqldef_file");
		try {
			loadSQLs(CommTool.getWebInfPath() + fileName);

		} catch (AppException ex) {
			ex.printStackTrace();
			System.out.println("ex=" + ex.toString());
		}

	}

	/**
	 * 从sql配置文件中
	 *
	 * @param sqlFile
	 * @throws AppException
	 */
	public static void loadSQLs(String sqlFile) throws AppException {
		Document doc = XMLUtils.parseXMLFile(sqlFile);
		if (doc == null)
			throw new AppException("读取sql配置文件[ " + sqlFile + " ]失败!请检查");
		int iStmt = XMLUtils.getSize(doc, "Stmt");
		hmSQLStore.clear();
		for (int i = 0; i < iStmt; i++) {
			Element e = XMLUtils.getElement(doc, "Stmt", i);
			String ID = e.getAttribute("ID");
			String sql = e.getLastChild().getNodeValue();
			sql = StringB.trim(sql, " ");
			sql = StringB.replace(sql, "\n", " ");
			hmSQLStore.put(ID, sql);
		}
	}

	public static String getOrignalSQL(String sqlID) throws AppException {
		String SQL = null;
		if (null != hmSQLStore.get(sqlID)) {
			SQL = (String) hmSQLStore.get(sqlID);
		}
		return SQL;

	}

	public static String genSQL(String sqlID) throws AppException {
		String cond[] = new String[0];
		return genSQL(sqlID, cond);
	}

	public static String genSQL(String sqlID, String strValue)
			throws AppException {
		String cond[] = new String[1];
		cond[0] = strValue;
		return genSQL(sqlID, cond);
	}

	public static String genSQL(String sqlID, String strV1, String strV2)
			throws AppException {
		String cond[] = new String[2];
		cond[0] = strV1;
		cond[1] = strV2;
		return genSQL(sqlID, cond);
	}

	public static String genSQL(String sqlID, String strV1, String strV2,
			String strV3) throws AppException {
		String cond[] = new String[3];
		cond[0] = strV1;
		cond[1] = strV2;
		cond[2] = strV3;
		return genSQL(sqlID, cond);
	}

	public static String genSQL(String sqlID, String strV1, String strV2,
			String strV3, String strV4) throws AppException {
		String cond[] = new String[4];
		cond[0] = strV1;
		cond[1] = strV2;
		cond[2] = strV3;
		cond[3] = strV4;
		return genSQL(sqlID, cond);
	}

	public static String genSQL(String sqlID, String strV1, String strV2,
			String strV3, String strV4, String strV5) throws AppException {
		String cond[] = new String[5];
		cond[0] = strV1;
		cond[1] = strV2;
		cond[2] = strV3;
		cond[3] = strV4;
		cond[4] = strV5;

		return genSQL(sqlID, cond);
	}

	public static String genSQL(String sqlID, String strV1, String strV2,
			String strV3, String strV4, String strV5, String strV6)
			throws AppException {
		String cond[] = new String[6];
		cond[0] = strV1;
		cond[1] = strV2;
		cond[2] = strV3;
		cond[3] = strV4;
		cond[4] = strV5;
		cond[5] = strV6;
		return genSQL(sqlID, cond);
	}

	public static String genSQL(String sqlID, String strV1, String strV2,
			String strV3, String strV4, String strV5, String strV6, String strV7)
			throws AppException {
		String cond[] = new String[7];
		cond[0] = strV1;
		cond[1] = strV2;
		cond[2] = strV3;
		cond[3] = strV4;
		cond[4] = strV5;
		cond[5] = strV6;
		cond[6] = strV7;
		return genSQL(sqlID, cond);
	}

	/**
	 * 从sql配置表中查找到指定的sql,并设置动态位置的条件值
	 *
	 * @param sqlID
	 * @param strIdxValue
	 * @return
	 */
	public static String genSQL(String sqlID, String[] strIdxValue)
			throws AppException {
		// 根据配置确定当前系统使用的sql语句配置文件
		String retSql = "";

		if (!hmSQLStore.containsKey(sqlID))
			throw new AppException("该sqlID[ " + sqlID + " ]没有在sql配置文件中定义");
		retSql = (String) hmSQLStore.get(sqlID);
		int iCount = StringB.countSubStr(retSql, "?");
		int iIdxC = 0;
		if (strIdxValue != null)
			iIdxC = strIdxValue.length;
		if (iIdxC != iCount) {
			throw new AppException("语句[ " + retSql + " ]的位置变量不配备");
		}
		String tmpStr = null;
		for (int i = 0; strIdxValue != null && i < strIdxValue.length; i++) {
			// 首先替换strIdxValue[i]中的问号
			tmpStr = strIdxValue[i];
			if (tmpStr == null)
				tmpStr = "";
			strIdxValue[i] = StringB.replace(tmpStr, "?", "&qbs;&");
			retSql = StringB.replaceFirst(retSql, "?", strIdxValue[i]);
			strIdxValue[i] = tmpStr;
		}

		// 将正常的问号还原
		retSql = StringB.replace(retSql, "&qbs;&", "?");

		// 替换数据库模式
		logger.debug("sqlID:" + sqlID + ":" + retSql);
		return retSql;
	}

	public static void main(String[] args) {

		try {
			String sql = SQLGenator.genSQL("L000X", "admin");
			System.out.println("sql=*" + sql + "*");
			WebDBUtil.execQryArray(sql, "");
		} catch (AppException ex) {
			ex.printStackTrace();
		}
	}

}