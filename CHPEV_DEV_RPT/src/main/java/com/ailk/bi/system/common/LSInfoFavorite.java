package com.ailk.bi.system.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

import com.ailk.bi.base.table.InfoFavoriteTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoFavorite {

	public static InfoFavoriteTable getFavorInfo(String user_id,
			String favor_id, String res_id) {
		String[][] rs = null;
		InfoFavoriteTable favor = new InfoFavoriteTable();
		// HttpSession session = request.getSession();
		// InfoOperTable user =
		// (InfoOperTable)session.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);

		try {
			String sql = "";
			if (favor_id != null && !"".equals(favor_id)) {
				sql = SQLGenator.genSQL("fav003", user_id, favor_id);
			} else if (res_id != null && !"".equals(res_id)) {
				sql = SQLGenator.genSQL("fav004", user_id, res_id);
			}
			if (!"".equals(sql)) {
				rs = WebDBUtil.execQryArray(sql, "");
				if (rs != null && rs.length > 0) {
					favor.favorite_id = rs[0][0];
					favor.favorite_name = rs[0][1];
					favor.parent_id = rs[0][2];
					favor.favorite_desc = rs[0][3];
					favor.res_url = rs[0][4];
					favor.sequence = rs[0][5];
				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return favor;
	}

	// 获取收藏夹名
	public static String getFavorName(String user_id, String favor_id) {
		String favorName = "";
		try {
			String sql = SQLGenator.genSQL("fav003", user_id, favor_id);
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				favorName = rs[0][1];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return favorName;
	}

	// 更新收藏夹
	public static void updateFavor(HttpServletRequest request)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		String favor_id = request.getParameter("favor_id");

		String res_id = request.getParameter("res_id");
		String favor_name = request.getParameter("favor_name");
		String parent_id = request.getParameter("parent_id");
		String user_id = request.getParameter("user_id");
		String par = "";
		if (parent_id == null || "".equals(parent_id)) {
			parent_id = "0";
		}
		List sqlList = new ArrayList();
		int j = -1;
		try {
			String sql = "";
			if (favor_id != null && !"".equals(favor_id)) {
				par = "favor_id=" + favor_id;
				sql = SQLGenator.genSQL("fav005", favor_name, parent_id,
						user_id, favor_id);
				sqlList.add(sql);
				String sql1 = getLogSql(request, "logFav001", "2", user_id,
						favor_id);
				sqlList.add(sql1);
			} else if (res_id != null && !"".equals(res_id)) {
				par = "res_id=" + res_id;
				sql = SQLGenator.genSQL("fav006", favor_name, parent_id,
						user_id, res_id);
				sqlList.add(sql);
				String sql1 = getLogSql(request, "logFav002", "2", user_id,
						res_id);
				sqlList.add(sql1);
			}
			String[] sqlArr = new String[sqlList.size()];
			for (int i = 0; i < sqlList.size(); i++) {
				sqlArr[i] = (String) sqlList.get(i);
				System.out.println("updateFavor [" + i + "]===" + sqlArr[i]);
			}
			j = 1;
			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}
		} catch (AppException e) {
			j = -1;

			e.printStackTrace();
		}
		if (j > 0) {
			String url = "favoriteView.screen?submitType=3&" + par;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "收藏夹信息变更成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "收藏夹信息变更失败！");
		}

	}

	// 新建收藏夹
	public static void addFavor(HttpServletRequest request)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		String favor_id = request.getParameter("favor_id");
System.out.println("新增收藏夹的id为："+favor_id+"前面的是。");
		String favor_name = request.getParameter("favor_name");
		String parent_id = request.getParameter("parent_id");
		String user_id = request.getParameter("user_id");
		String seq = "";
		if (parent_id == null || "".equals(parent_id)) {
			parent_id = "0";
		}
		List sqlList = new ArrayList();
		int j = -1;

		try {
			String sql = "";
			//生成收藏夹id。seq=favorite_id
			seq = CommonUtil.getNextVal("SEQ_INFO_FAVORITE") + "";
System.out.println("再看，新增收藏夹的id为："+seq+"前面的是。");
			sql = SQLGenator.genSQL("fav007", user_id, seq, parent_id,
					favor_name, "1");
			sqlList.add(sql);
			String sql1 = getLogSql(request, "logFav001", "1", user_id, seq);
			sqlList.add(sql1);

			String[] sqlArr = new String[sqlList.size()];
			for (int i = 0; i < sqlList.size(); i++) {
				sqlArr[i] = (String) sqlList.get(i);
				System.out.println("addFavor [" + i + "]===" + sqlArr[i]);
			}
			j = 1;
			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}
		} catch (AppException e) {
			j = -1;

			e.printStackTrace();
		}
		if (j > 0) {
			String url = "favoriteView.screen?submitType=3&favor_id=" + seq;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "收藏夹信息新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "收藏夹信息新增失败！");
		}

	}

	// 删除收藏夹
	public static void delFavor(HttpServletRequest request)
			throws HTMLActionException {
		String user_id = request.getParameter("user_id");
		String favor_id = request.getParameter("favor_id");
		String res_id = request.getParameter("res_id");
		HttpSession session = request.getSession();
		List sqlList = new ArrayList();
		int j = -1;
		try {
			if (favor_id != null && !"".equals(favor_id)) {
				String favSql = "select favorite_id from UI_SYS_RULE_USER_FAVORITE start with favorite_id='"
						+ favor_id
						+ "' connect by prior  favorite_id =parent_id";
				String[][] rs = WebDBUtil.execQryArray(favSql, "");
				String favStr = "";
				for (int i = 0; i < rs.length; i++) {
					if (i != 0) {
						favStr += ",";
					}
					favStr += "'" + rs[i][0] + "'";
				}

				String sqlAdhoc = "select ADHOC_FAV_ID from UI_SYS_RULE_FAVORITE_RESOURCE where res_type='ADHOC' and favorite_id in ("
						+ favStr + ") ";

				String[][] resAdhoc = WebDBUtil.execQryArray(sqlAdhoc, "");
				String adhocFavId = "";
				String strAdhocFavId = "";

				if (resAdhoc != null && resAdhoc.length > 0) {
					for (int k = 0; k < resAdhoc.length; k++) {
						String tmp = StringB.NulltoBlank(resAdhoc[k][0]);
						if (tmp.length() > 0) {
							if (adhocFavId.length() > 0) {
								adhocFavId += "," + tmp;
								strAdhocFavId += ",'" + tmp + "'";

							} else {

								adhocFavId = tmp;
								strAdhocFavId = "'" + tmp + "'";

							}
						}

					}

				}

				if (adhocFavId.length() > 0) {
					String sqlTmp = " DELETE FROM UI_ADHOC_RULE_OPER_FAVORITE WHERE OPER_NO = '"
							+ user_id
							+ "' AND FAVORITE_ID in("
							+ adhocFavId
							+ ")";
					sqlList.add(sqlTmp);

					sqlTmp = "  DELETE FROM ui_adhoc_rule_favorite_meta WHERE FAVORITE_ID in("
							+ adhocFavId + ")";
					sqlList.add(sqlTmp);

					sqlTmp = "  DELETE FROM UI_ADHOC_FAVORITE_DEF WHERE FAVORITE_ID in("
							+ adhocFavId + ")";
					sqlList.add(sqlTmp);

					sqlTmp = "delete from UI_ADHOC_USER_LIST b where  b.fav_id in("
							+ strAdhocFavId
							+ ") AND b.OPER_NO = '"
							+ user_id
							+ "'";

					sqlList.add(sqlTmp);

				}

				String sql1 = getLogSql(request, "logFav003", "3", user_id,
						favStr);
				sqlList.add(sql1);
				String sql2 = getLogSql(request, "logFav004", "3", user_id,
						favStr);
				sqlList.add(sql2);
				String sql3 = SQLGenator.genSQL("fav009", user_id, favStr);
				sqlList.add(sql3);
				String sql4 = SQLGenator.genSQL("fav010", user_id, favStr);
				sqlList.add(sql4);

			} else if (res_id != null && !"".equals(res_id)) {
				String sql1 = getLogSql(request, "logFav002", "3", user_id,
						res_id);
				sqlList.add(sql1);
				String sql2 = SQLGenator.genSQL("fav011", user_id, res_id);
				sqlList.add(sql2);

				String sqlAdhoc = "select ADHOC_FAV_ID from UI_SYS_RULE_FAVORITE_RESOURCE where res_type='ADHOC' and USER_ID='"
						+ user_id + "' and RES_ID='" + res_id + "'";

				String[][] resAdhoc = WebDBUtil.execQryArray(sqlAdhoc, "");
				String adhocFavId = "";
				String strAdhocFavId = "";
				if (resAdhoc != null && resAdhoc.length > 0) {
					for (int k = 0; k < resAdhoc.length; k++) {
						String tmp = StringB.NulltoBlank(resAdhoc[k][0]);
						if (tmp.length() > 0) {
							if (adhocFavId.length() > 0) {
								adhocFavId += "," + tmp;
								strAdhocFavId += ",'" + tmp + "'";

							} else {

								adhocFavId = tmp;
								strAdhocFavId = "'" + tmp + "'";
							}
						}

					}

				}

				if (adhocFavId.length() > 0) {
					String sqlTmp = " DELETE FROM UI_ADHOC_RULE_OPER_FAVORITE WHERE OPER_NO = '"
							+ user_id
							+ "' AND FAVORITE_ID in("
							+ adhocFavId
							+ ")";
					sqlList.add(sqlTmp);

					sqlTmp = "  DELETE FROM ui_adhoc_rule_favorite_meta WHERE FAVORITE_ID in("
							+ adhocFavId + ")";
					sqlList.add(sqlTmp);

					sqlTmp = "  DELETE FROM UI_ADHOC_FAVORITE_DEF WHERE FAVORITE_ID in("
							+ adhocFavId + ")";
					sqlList.add(sqlTmp);

					sqlTmp = "delete from UI_ADHOC_USER_LIST b where  b.fav_id in("
							+ strAdhocFavId
							+ ") AND b.OPER_NO = '"
							+ user_id
							+ "'";

					sqlList.add(sqlTmp);
				}

			}
			String[] sqlArr = new String[sqlList.size()];
			for (int i = 0; i < sqlList.size(); i++) {
				sqlArr[i] = (String) sqlList.get(i);
				// System.out.println("delFavor ["+i+"]==="+sqlArr[i]);
			}
			j = 1;
			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}

		} catch (AppException e) {

			j = -1;
			e.printStackTrace();
		}

		if (j > 0) {
			String url = "favoriteView.screen?submitType=3&favor_id=&res_id=";
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "收藏夹信息删除成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "收藏夹信息删除失败！");
		}
	}

	// 新建收藏夹资源
	public static String addFavorRes(HttpServletRequest request)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		String res_name = CommTool.getParameterGB(request, "res_name");
		String res_id = CommTool.getParameterGB(request, "res_id");
		String res_url = request.getParameter("res_url");
		String favor_id = request.getParameter("favorite_id");
		String user_id = user.user_id;
		String flag = "添加收藏成功！";

		if (IsExistRes(user_id, res_id)) {
			flag = "该资源已经添加到收藏夹了！";
			return flag;
		}
		List sqlList = new ArrayList();

		try {
			String sql = "";

			sql = SQLGenator.genSQL("fav008", user_id, res_id, res_url,
					favor_id, res_name, "1");
			sqlList.add(sql);
			String sql1 = getLogSql(request, "logFav002", "1", user_id, res_id);
			sqlList.add(sql1);

			String[] sqlArr = new String[sqlList.size()];
			for (int i = 0; i < sqlList.size(); i++) {
				sqlArr[i] = (String) sqlList.get(i);
				System.out.println("addFavorRes [" + i + "]===" + sqlArr[i]);
			}

			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}
		} catch (AppException e) {
			flag = "添加收藏失败！";

			e.printStackTrace();
		}
		return flag;

	}

	// 新建文件夹
	public static void addFolder(HttpServletRequest request)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		String favorite_name = CommTool
				.getParameterGB(request, "favorite_name");
		String user_id = user.user_id;
		String seq = "";

		List sqlList = new ArrayList();

		try {
			String sql = "";
			seq = CommonUtil.getNextVal("SEQ_INFO_FAVORITE") + "";
			sql = SQLGenator.genSQL("fav007", user_id, seq, "0", favorite_name,
					"1");
			sqlList.add(sql);
			String sql1 = getLogSql(request, "logFav001", "1", user_id, seq);
			sqlList.add(sql1);

			String[] sqlArr = new String[sqlList.size()];
			for (int i = 0; i < sqlList.size(); i++) {
				sqlArr[i] = (String) sqlList.get(i);
				System.out.println("addFolder [" + i + "]===" + sqlArr[i]);
			}

			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param user_id
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_user_id = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_user_id, ip, type,
					para_a, para_b);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	/**
	 * 取得日志信息SQL
	 *
	 * @param request
	 * @param user_id
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b, String para_c) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_user_id = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_user_id, ip, type,
					para_a, para_b, para_c);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	// 获取收藏夹树结构
	public static String[][] getFavorTree(HttpServletRequest request) {
		String[][] rs = null;
		HttpSession session = request.getSession();
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		try {
			String sql = SQLGenator
					.genSQL("fav014", user.user_id, user.user_id);
			rs = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return rs;
	}

	// 收藏夹资源是否存在
	public static boolean IsExistRes(String user_id, String res_id) {
		boolean flag = false;
		try {
			String sql = SQLGenator.genSQL("fav004", user_id, res_id);
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				flag = true;
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return flag;
	}

	// 新建即席查询的收藏
	public static String addAdhocfavor(HttpServletRequest request,
			String favID, String favName, String favorite_id, String root,
			String group_tag, String group_msu_tag) {
		HttpSession session = request.getSession();
		InfoOperTable user = (InfoOperTable) session
				.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
		String res_url = "/adhoc/AdhocFav.rptdo?fav_flag=1&fav_id=" + favID
				+ "&fav_name=" + favName + "&adhoc_root=" + root
				+ "&group_tag=" + group_tag + "&group_msu_tag=" + group_msu_tag;

		String user_id = user.user_id;

		List sqlList = new ArrayList();

		try {
			String sql = "";
			String seq = CommonUtil.getNextVal("SEQ_INFO_FAVORITE") + "";
			sql = SQLGenator.genSQL("fav008", user_id, seq, res_url,
					favorite_id, favName, seq);
			sqlList.add(sql);
			String sql1 = getLogSql(request, "logFav002", "1", user_id, seq);
			sqlList.add(sql1);

			String[] sqlArr = new String[sqlList.size()];
			for (int i = 0; i < sqlList.size(); i++) {
				sqlArr[i] = (String) sqlList.get(i);
				System.out.println("addAdhocFavorRes [" + i + "]==="
						+ sqlArr[i]);
			}

			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}
			return seq;
		} catch (AppException e) {

			e.printStackTrace();
		}
		return "";
	}

	// 删除即席查询收藏
	public static void delAdhocfavor(HttpServletRequest request,
			String user_id, String favID, String root) {

		try {
			String sql = SQLGenator.genSQL("fav015", user_id, favID, root);
			String[][] rs = WebDBUtil.execQryArray(sql, "");

			if (rs != null && rs.length > 0) {
				List sqlList = new ArrayList();
				String res_id = rs[0][0];
				String sql1 = getLogSql(request, "logFav002", "3", user_id,
						res_id);
				sqlList.add(sql1);
				String sql2 = SQLGenator.genSQL("fav011", user_id, res_id);
				sqlList.add(sql2);

				String sql3 = SQLGenator.genSQL("fav016_del", favID, root,
						user_id);
				sqlList.add(sql3);

				String[] sqlArr = new String[sqlList.size()];
				for (int i = 0; i < sqlList.size(); i++) {
					sqlArr[i] = (String) sqlList.get(i);
					System.out.println("delAdhocFavorRes [" + i + "]==="
							+ sqlArr[i]);
				}

				if (sqlArr.length > 0) {
					WebDBUtil.execTransUpdate(sqlArr);
				}
			}
		} catch (AppException e1) {

			e1.printStackTrace();
		}

	}

	// 删除即席查询收藏
	public static void delAdhocfavor(String user_id, String favID, String root) {

		try {

			List sqlList = new ArrayList();
			// String sql1 =
			// "delete from UI_SYS_RULE_FAVORITE_RESOURCE where user_id='" +
			// user_id
			// + "' and ADHOC_ID='" + root + "' and ADHOC_FAV_ID=" + favID;

			String sql1 = "delete from UI_SYS_RULE_FAVORITE_RESOURCE where user_id='"
					+ user_id + "' and ADHOC_FAV_ID=" + favID;
			sqlList.add(sql1);

			// String sql2 =
			// "delete from UI_ADHOC_USER_LIST b where  b.fav_id='" + favID +
			// "' and b.ADHOC_ID = '"
			// + root + "'   AND b.OPER_NO = '" + user_id + "'";

			String sql2 = "delete from UI_ADHOC_USER_LIST b where  b.fav_id='"
					+ favID + "' AND b.OPER_NO = '" + user_id + "'";

			sqlList.add(sql2);

			String[] sqlArr = new String[sqlList.size()];
			for (int i = 0; i < sqlList.size(); i++) {
				sqlArr[i] = (String) sqlList.get(i);
				// System.out.println("delAdhocFavorRes ["+i+"]==="+sqlArr[i]);
			}

			if (sqlArr.length > 0) {
				WebDBUtil.execTransUpdate(sqlArr);
			}

		} catch (AppException e1) {

			e1.printStackTrace();
		}

	}
}
