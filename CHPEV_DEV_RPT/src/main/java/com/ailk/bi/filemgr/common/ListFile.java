package com.ailk.bi.filemgr.common;

import com.ailk.bi.adhoc.util.FileOperator;
import com.ailk.bi.base.table.InfoFavoriteTable;
//import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.system.common.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2010-1-19 Time: 10:04:21
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class ListFile {

	public static String[][] getNormalFiles(String[] file_ids) throws Exception {
		StringBuffer sql = new StringBuffer(
				"SELECT T1.FILE_ID,\n"
						+ "       TO_CHAR(T1.FILE_CREATEDATE, 'yyyy-mm-dd hh24:mi:ss') AS CREATE_DATE,\n"
						+ "       T1.FILE_ORGNAME,\n"
						+ "       T1.FILE_SIZE,\n"
						+ "       T2.FILE_HANDLE_USER AS CREATEOR,\n"
						+ "       T1.FILE_NAME,\n"
						+ "       COUNT(T3.ID) AS DOWNLOADNUM\n"
						+ "  FROM UI_SYS_INFO_FILE_STORE T1\n"
						+ "  LEFT JOIN UI_SYS_LOG_FILE_STORE T2 ON T1.FILE_ID = T2.FILE_ID\n"
						+ "                               AND T2.FILE_HANDLE_ACTION = 'upload'\n"
						+ "  LEFT JOIN UI_SYS_LOG_FILE_STORE T3 ON T1.FILE_ID = T3.FILE_ID\n"
						+ "                               AND T3.FILE_HANDLE_ACTION = 'download'\n"
						+ " WHERE t1.FILE_STATUS = 'U'");
		for (int i = 0; file_ids != null && i < file_ids.length; i++) {
			if (i == 0) {
				sql.append(" AND T1.FILE_ID in (");
			}
			sql.append(file_ids[i]).append(", ");
			if (i == file_ids.length - 1) {
				sql.replace(sql.length() - 2, sql.length(), ")");
			}
		}
		sql.append(" GROUP BY T1.FILE_ID,\n" + "          T1.FILE_SIZE,\n"
				+ "          T1.FILE_NAME,\n"
				+ "          T2.FILE_HANDLE_USER,\n"
				+ "          T1.FILE_CREATEDATE,\n"
				+ "          T1.FILE_ORGNAME");
		return WebDBUtil.execQryArray(sql.toString(), "");
	}

	/**
	 * 
	 * @param id
	 * @param struct
	 * @return
	 * @throws Exception
	 */
	public static String[][] getNormalFiles(String id, ReportQryStruct struct) {
		StringBuffer sql = new StringBuffer(
				"SELECT T1.FILE_ID,\n"
						+ "       TO_CHAR(T1.FILE_CREATEDATE, 'yyyy-mm-dd hh24:mi:ss') AS CREATE_DATE,\n"
						+ "       T1.FILE_ORGNAME,\n"
						+ "       T1.FILE_SIZE,\n"
						+ "       T2.FILE_HANDLE_USER AS CREATEOR,\n"
						+ "       T1.FILE_NAME,\n"
						+ "       COUNT(T3.ID) AS DOWNLOADNUM\n"
						+

						"  FROM UI_SYS_INFO_FILE_STORE T1\n"
						+ "  LEFT JOIN UI_SYS_LOG_FILE_STORE T2 ON T1.FILE_ID = T2.FILE_ID\n"
						+ "                               AND T2.FILE_HANDLE_ACTION = 'upload'\n"
						+ "  LEFT JOIN UI_SYS_LOG_FILE_STORE T3 ON T1.FILE_ID = T3.FILE_ID\n"
						+ "                               AND T3.FILE_HANDLE_ACTION = 'download'\n"
						+ " WHERE t1.FILE_STATUS = 'U'");

		sql.append(" and file_dir in(SELECT id FROM UI_SYS_INFO_FILE_DIR START WITH id="
				+ id + " CONNECT BY PRIOR id=parent_id)");
		sql.append(" GROUP BY T1.FILE_ID,\n" + "          T1.FILE_SIZE,\n"
				+ "          T2.FILE_HANDLE_USER,\n"
				+ "          T1.FILE_CREATEDATE,\n" + "       T1.FILE_NAME,\n"
				+ "          T1.FILE_ORGNAME");

		// System.out.println(sql.toString());
		try {
			return WebDBUtil.execQryArray(sql.toString(), "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static String[][] getFileDirInfo() {
		String sql = "SELECT id,parent_id,dir_name,dir_desc FROM UI_SYS_INFO_FILE_DIR START WITH parent_id=0 "
				+ "CONNECT BY PRIOR ID=parent_id ORDER BY SORT_NUM,id";
		try {
			return WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return null;
	}

	// 新建收藏夹
	public static void addDir(HttpServletRequest request)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		String favor_name = StringB.NulltoBlank(request
				.getParameter("favor_name"));
		String parent_id = StringB.NulltoBlank(request
				.getParameter("parent_id"));
		if (parent_id.length() == 0) {
			parent_id = "0";
		}
		String user_id = request.getParameter("user_id");
		String seq = "";
		if (parent_id == null || "".equals(parent_id)) {
			parent_id = "0";
		}
		List sqlList = new ArrayList();
		int j = -1;

		try {
			String sql = "";
			seq = CommonUtil.getNextVal("INFO_FILE_DIR_SEQ") + "";
			sql = "insert into UI_SYS_INFO_FILE_DIR(id,parent_id,dir_name,oper_no,add_date) values("
					+ seq
					+ ","
					+ parent_id
					+ ",'"
					+ favor_name
					+ "','"
					+ user_id + "',sysdate)";

			WebDBUtil.execUpdate(sql);
			j = 1;
		} catch (AppException e) {
			j = -1;

			e.printStackTrace();
		}
		if (j > 0) {
			String url = "dirView.screen?submitType=3&id=" + seq;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "目录新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "目录新增失败！");
		}

	}

	public static InfoFavoriteTable getDirInfo(String favor_id) {
		String[][] rs = null;
		InfoFavoriteTable favor = new InfoFavoriteTable();
		// HttpSession session = request.getSession();
		// InfoOperTable user =
		// (InfoOperTable)session.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);

		try {
			String sql = "select id,dir_name,parent_id,dir_desc,'',sort_num from UI_SYS_INFO_FILE_DIR where id="
					+ favor_id;

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
	public static String getDirName(String favor_id) {
		String favorName = "";
		try {
			// String sql = SQLGenator.genSQL("fav003", user_id,favor_id);
			String sql = "select id,dir_name,parent_id,dir_desc,'',sort_num from UI_SYS_INFO_FILE_DIR where id="
					+ favor_id;

			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				favorName = rs[0][1];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return favorName;
	}

	// 删除目录
	public static void delDir(HttpServletRequest request)
			throws HTMLActionException {
		String favor_id = request.getParameter("favor_id");
		HttpSession session = request.getSession();
		String sql = "select parent_id from UI_SYS_INFO_FILE_DIR where id="
				+ favor_id;
		try {
			String[][] res = WebDBUtil.execQryArray(sql, "");
			String pid = "";
			if (res != null && res.length > 0) {
				pid = res[0][0];
			}

			String sqlDel = "delete from UI_SYS_INFO_FILE_STORE where  file_dir in(SELECT id FROM UI_SYS_INFO_FILE_DIR START WITH parent_id="
					+ pid + " CONNECT BY PRIOR id=parent_id)";
			WebDBUtil.execUpdate(sqlDel);
			sqlDel = "delete from UI_SYS_INFO_FILE_DIR where id in(SELECT id FROM UI_SYS_INFO_FILE_DIR START WITH id="
					+ favor_id + " CONNECT BY PRIOR ID=parent_id)";

			WebDBUtil.execUpdate(sqlDel);
			String url = "dirView.screen?submitType=3&favor_id=&res_id=";
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "目录删除成功！", url);

		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	public static void delUpfile(HttpServletRequest request)
			throws HTMLActionException {
		String id = request.getParameter("id");
		HttpSession session = request.getSession();

		String sql = "select FILE_FULPATH from UI_SYS_INFO_FILE_STORE where file_id="
				+ id;
		try {
			String[][] res = WebDBUtil.execQryArray(sql, "");
			if (res != null & res.length > 0) {
				String filePathName = res[0][0];
				if (FileOperator.fileExist(filePathName)) {
					FileOperator.delFile(filePathName);
				}
				sql = "delete from UI_SYS_INFO_FILE_STORE where file_id=" + id;
				WebDBUtil.execUpdate(sql);
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		String dirId = request.getParameter("dirId");
		String url = "CreateFileDirXML.rptdo?opType=listFile&id=" + dirId;
		throw new HTMLActionException(session,
				HTMLActionException.SUCCESS_PAGE, "文件删除成功！", url);

	}

	// 更新收藏夹
	public static void updateDir(HttpServletRequest request)
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
		try {
			String sql = "update UI_SYS_INFO_FILE_DIR set dir_name='"
					+ favor_name + "',parent_id=" + parent_id + " where id="
					+ favor_id;
			WebDBUtil.execUpdate(sql);
			String url = "dirView.screen?submitType=3&favor_id=" + favor_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "目录变更成功！", url);

		} catch (AppException e) {

			e.printStackTrace();
		}

	}

}
