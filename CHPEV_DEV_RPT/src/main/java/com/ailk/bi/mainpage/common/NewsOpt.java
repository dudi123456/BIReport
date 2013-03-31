package com.ailk.bi.mainpage.common;

import java.util.Vector;

import com.ailk.bi.base.struct.LsbiQryStruct;
import com.ailk.bi.base.table.PubInfoNewsTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

/**
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description: 处理经营公告操作
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: asiabi
 * </p>
 * 
 * @author renhui
 * @version 1.0
 */

@SuppressWarnings({ "rawtypes" })
public class NewsOpt {

	/**
	 * 向数据库中插入一条经营公告内容
	 * 
	 * @param user_id
	 * @param user_name
	 * @param subject
	 * @param descr
	 * @param news_type
	 * @throws AppException
	 */
	public static void insertNews(String user_id, String user_name,
			String subject, String descr, String news_type) throws AppException {
		String strSql = SQLGenator.genSQL("I4101", user_id, user_name, subject,
				descr, news_type);
		System.out.println("I4001 = " + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	/**
	 * 依据给定的ID进行更新经营公告内容
	 * 
	 * @param id
	 * @param subject
	 * @param descr
	 * @param news_type
	 * @throws AppException
	 */
	public static void updateNews(String news_id, String subject, String descr,
			String news_type) throws AppException {
		String strSql = SQLGenator.genSQL("C4102", subject, descr, news_type,
				news_id);
		System.out.println("C4002 = " + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	/**
	 * 依据给定的ID删除经营公告内容
	 * 
	 * @param strId
	 * @throws AppException
	 */
	public static void deleteNews(String news_id) throws AppException {
		String strSql = SQLGenator.genSQL("D4103", news_id);
		System.out.println("D4003 = " + strSql);
		WebDBUtil.execUpdate(strSql);
	}

	/**
	 * 查询指定ID的经营公告内容
	 * 
	 * @param news_id
	 * @return
	 * @throws AppException
	 */
	public static PubInfoNewsTable getNewsInfo(String news_id)
			throws AppException {
		if (news_id == null || "".equals(news_id)) {
			return null;
		}
		PubInfoNewsTable[] news = null;
		LsbiQryStruct lsbiQry = new LsbiQryStruct();
		news = getNewsInfos(news_id, lsbiQry, 0);
		if (news != null && news.length > 0)
			return news[0];
		else
			return null;
	}

	/**
	 * 查询数据库中的前N条经营公告内容，如果输入0查询全部
	 * 
	 * @param pNum
	 * @return
	 * @throws AppException
	 */
	public static PubInfoNewsTable[] getNewsTopTen(int pNum)
			throws AppException {
		LsbiQryStruct lsbiQry = new LsbiQryStruct();
		return getNewsInfos("", lsbiQry, 0);
	}

	/**
	 * 查询数据库中的所有经营公告内容，将其存放在PubInfoNewsTable[]中，以便于进行页面内容显示
	 * <p>
	 * Description:查询经营公告内容，如果所有的参数都为空，按照默认方式进行查询结果 如果参数不为空，则是有条件查询
	 * </p>
	 * 
	 * @param news_id
	 * @param lsbiQry
	 * @param pNum
	 *            前N条经营公告内容，如果输入0查询全部
	 */
	public static PubInfoNewsTable[] getNewsInfos(String news_id,
			LsbiQryStruct lsbiQry, int pNum) throws AppException {
		String strSql = "";
		if (pNum > 0) {
			strSql += "select * from (";
		}
		strSql = SQLGenator.genSQL("Q4100");
		String username = lsbiQry.oper_name;
		String subject = lsbiQry.obj_name;
		String sDate = lsbiQry.date_s;
		String eDate = lsbiQry.date_e;
		String field = lsbiQry.order_code;
		String order = lsbiQry.order;
		if (!"".equals(username)) {
			strSql += " and user_name like '%" + StringB.toGB(username) + "%'";
		}
		// search strSql conform to Subject;
		if (!"".equals(subject)) {
			strSql += " and subject like '%" + StringB.toGB(subject) + "%'";
		}
		// search strSql conform to Date;
		if (!"".equals(sDate)) {
			strSql += " and to_char(create_date,'yyyymmdd')>='" + sDate + "'";
		}
		if (!"".equals(eDate)) {
			strSql += " and to_char(create_date,'yyyymmdd')<='" + eDate + "'";
		}
		// search strSql conform to News_ID;
		if (!"".equals(news_id)) {
			strSql += " and news_id=" + news_id;
		}
		// get sort field and asc & desc
		if (!"".equals(field)) {
			strSql += " ORDER BY " + field + " " + order;
		} else {
			strSql += " ORDER BY NEWS_TYPE,SEQUENCE,CREATE_DATE DESC";
		}
		if (pNum > 0) {
			strSql += ") where rownum<=" + pNum;
		}
		// System.out.println("NewsInfos=====>" + strSql);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result == null || result.size() == 0) {
			return null;
		}
		PubInfoNewsTable[] news = new PubInfoNewsTable[result.size()];
		for (int i = 0; i < result.size(); i++) {
			Vector tempv = (Vector) result.get(i);
			news[i] = new PubInfoNewsTable();
			int m = 0;
			news[i].news_id = (String) tempv.get(m++);
			news[i].create_date = (String) tempv.get(m++);
			news[i].user_id = (String) tempv.get(m++);
			news[i].user_name = (String) tempv.get(m++);
			news[i].subject = (String) tempv.get(m++);
			news[i].descr = (String) tempv.get(m++);
			news[i].new_type = (String) tempv.get(m++);
		}
		return news;
	}

}