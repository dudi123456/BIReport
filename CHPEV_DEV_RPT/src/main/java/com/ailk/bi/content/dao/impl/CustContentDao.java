package com.ailk.bi.content.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.content.dao.ICustContentDao;
import com.ailk.bi.content.domain.UiCustcontentCalmeasure;
import com.ailk.bi.content.domain.UiCustcontentTemplateTable;
import com.ailk.bi.content.domain.UiKpiData;

@SuppressWarnings({ "rawtypes" })
public class CustContentDao implements ICustContentDao {

	/**
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public List getContent(String whereStr) {
		String strSql = "";
		try {
			strSql = SQLGenator.genSQL("CON_Q5500", whereStr);
		} catch (AppException e1) {
			e1.printStackTrace();
		}
		System.out.println("getContent list=" + strSql);
		List pram = new ArrayList();
		List svces = null;
		try {
			svces = WebDBUtil.find(UiCustcontentTemplateTable.class, strSql,
					pram);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return svces;
	}

	/**
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public List getContentCalMsu(String whereStr) {
		String strSql = "";
		try {
			strSql = SQLGenator.genSQL("CON_Q5520", whereStr);
		} catch (AppException e1) {
			e1.printStackTrace();
		}
		System.out.println("getContentCalMsu list=" + strSql);
		List pram = new ArrayList();
		List svces = null;
		try {
			svces = WebDBUtil.find(UiCustcontentCalmeasure.class, strSql, pram);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return svces;
	}

	/**
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public List getContentData(String strSql) {
		if(StringTool.checkEmptyString(strSql)){
			return null;
		}

		List pram = new ArrayList();
		List svces = null;
		try {
			svces = WebDBUtil.find(UiKpiData.class, strSql, pram);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return svces;
	}

	/**
	 * @param whereStr
	 * @return
	 * @throws AppException
	 */
	public String[][] getData(String strSql) {
		if(StringTool.checkEmptyString(strSql)){
			return null;
		}

		String[][] svces = null;
		try {
			svces = WebDBUtil.execQryArray(strSql, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return svces;
	}

}
