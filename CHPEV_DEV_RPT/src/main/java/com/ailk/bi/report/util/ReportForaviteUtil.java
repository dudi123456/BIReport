package com.ailk.bi.report.util;

import java.util.Vector;
import javax.servlet.http.HttpSession;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

public class ReportForaviteUtil {

	/**
	 * 判断收藏夹中是否有记录
	 * 
	 * @param userid
	 * @param measureId
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isHavingForavite(String userid, String itemid,
			String itemtype) throws AppException {
		String strSql = SQLGenator.genSQL("Q3100", userid, itemid, itemtype);
		Vector result = WebDBUtil.execQryVector(strSql, "");
		if (result == null || result.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 删除收藏夹中信息
	 * 
	 * @param userId
	 * @param itemId
	 * @throws AppException
	 */
	public static void deleteForvativeItem(String userid, String itemid,
			String itemtype) throws AppException {
		try {
			String strSql = SQLGenator
					.genSQL("D3101", userid, itemid, itemtype);
			int i = WebDBUtil.execUpdate(strSql);
			if (i != 1) {
				throw new AppException("删除收藏夹信息失败!");
			}
		} catch (Exception e) {
			throw new AppException("删除收藏夹信息失败![" + e.toString() + "]");
		}
	}

	/**
	 * 将信息加入收藏夹中
	 * 
	 * @param userId
	 * @param itemId
	 * @param itemType
	 * @throws AppException
	 */
	public static void insertForvativeItem(String userId, String itemId,
			String itemType) throws AppException {
		try {
			String strSql = SQLGenator
					.genSQL("I3102", userId, itemId, itemType);
			int i = WebDBUtil.execUpdate(strSql);
			if (i != 1) {
				throw new AppException("添加收藏夹表信息失败!");
			}
		} catch (Exception e) {
			throw new AppException("添加收藏夹信息失败![" + e.toString() + "]");
		}
	}

	/**
	 * 获取用户收藏报表信息
	 * 
	 * @param session
	 * @param userID
	 * @return
	 * @throws AppException
	 */
	public static PubInfoResourceTable[] getMSDTReport(HttpSession session,
			String userID) throws AppException {
		String sql = SQLGenator.genSQL("Q3103", userID);
		// System.out.println("sql=" + sql);
		String res[][] = WebDBUtil.execQryArray(sql, "");
		PubInfoResourceTable views[] = null;
		if (res == null)
			views = new PubInfoResourceTable[0];
		else {
			views = new PubInfoResourceTable[res.length];
			int m = 0;
			for (int i = 0; i < views.length; i++) {
				m = 0;
				views[i] = new PubInfoResourceTable();
				views[i].rpt_id = res[i][m++];
				views[i].name = res[i][m++];
				views[i].type = res[i][m++];
				views[i].inputnote = res[i][m++];
				views[i].parent_id = res[i][m++];
			}
		}
		session.setAttribute(WebKeys.ATTR_REPORT_TABLES, views);
		return views;
	}

	/**
	 * 查询用户报表
	 * 
	 * @param session
	 * @param userRole
	 * @param rptName
	 * @return
	 * @throws AppException
	 */
	public static PubInfoResourceTable[] getUserSearchReport(
			HttpSession session, String userRole, String rptName)
			throws AppException {
		PubInfoResourceTable[] views = null;
		String sql = SQLGenator.genSQL("Q3105", userRole, rptName);
		// System.out.println("sql=" + sql);
		String[][] res = WebDBUtil.execQryArray(sql, "");
		if (res == null)
			views = new PubInfoResourceTable[0];
		else {
			views = new PubInfoResourceTable[res.length];
			int m = 0;
			for (int i = 0; i < views.length; i++) {
				m = 0;
				views[i] = new PubInfoResourceTable();
				views[i].rpt_id = res[i][m++];
				views[i].name = res[i][m++];
				views[i].type = res[i][m++];
				views[i].inputnote = res[i][m++];
				views[i].parent_id = res[i][m++];
			}
		}
		session.setAttribute(WebKeys.ATTR_REPORT_TABLES, views);
		return views;
	}

	/**
	 * 删除收藏夹报表(包括指标和非指标的)
	 * 
	 * @param userID
	 * @param items
	 * @return
	 * @throws AppException
	 */
	public static int delFavRpt(String userID, String[] items)
			throws AppException {
		if (items == null || items.length == 0)
			return 0;
		String sqls[] = new String[items.length];
		for (int i = 0; i < sqls.length; i++) {
			String[] tmpvalue = items[i].split(",");
			sqls[i] = SQLGenator.genSQL("D3104", userID, tmpvalue[0],
					tmpvalue[1]);
			// System.out.println("sqls[i]=" + sqls[i]);
		}
		WebDBUtil.execTransUpdate(sqls);
		return items.length;
	}

	/**
	 * 获取某类报表第一个ID
	 * 
	 * @param knd
	 * @param userrole
	 * @return
	 */
	public static String getFristReportID(String knd, String userrole) {
		String rid = "";
		String sql = "";
		String[][] res = null;
		try {
			sql = SQLGenator.genSQL("Q3106", knd, userrole);
		} catch (AppException e) {
			sql = "";
		}
		// System.out.println("sql=" + sql);
		try {
			res = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {
			res = null;
		}

		if (res != null && res.length > 0)
			rid = res[0][0];
		return rid;
	}
}
