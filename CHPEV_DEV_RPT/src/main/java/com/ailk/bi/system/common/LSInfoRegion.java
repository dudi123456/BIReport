package com.ailk.bi.system.common;

import java.util.*;
import javax.servlet.http.*;

import com.ailk.bi.base.table.InfoRegionTable;
import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoRegion {
	public static InfoRegionTable getRegionInfo(String region_id) {
		InfoRegionTable infoRegion = new InfoRegionTable();
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2652", region_id);
			String rs[][] = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				infoRegion.region_id = rs[0][0];
				infoRegion.parent_id = rs[0][1];
				infoRegion.parent_name = getRegionName(infoRegion.parent_id);
				infoRegion.region_name = rs[0][2];
				infoRegion.region_code = rs[0][3];
				infoRegion.region_type = rs[0][4];
				infoRegion.status = rs[0][5];
				infoRegion.comments = rs[0][7];
				infoRegion.path_code = getRegionPathCode(region_id);

			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return infoRegion;

	}

	/**
	 * 取得某区域的path_code
	 * 
	 * @param region_id
	 * @return
	 */
	public static String getRegionPathCode(String region_id) {
		String path_code = "";
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2654", region_id);
			String rs[][] = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				for (int i = 0; i < rs.length; i++) {
					path_code = rs[i][0] + "," + path_code;
				}
			}

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return path_code;
	}

	/**
	 * 增加区域信息
	 * 
	 * @param request
	 * @param infoRegion
	 * @throws HTMLActionException
	 */
	public static void addNewRegion(HttpServletRequest request,
			InfoRegionTable infoRegion) throws HTMLActionException {
		HttpSession session = request.getSession();
		String region_id = infoRegion.region_id;
		Vector sqlV = new Vector();
		//
		if (CheckExistRegion(region_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "区域编码已经存在！");

		}
		//
		InfoRegionTable region = getRegionInfo(region_id);
		//
		if (!infoRegion.region_name.equals(region.region_name)) {
			if (isDupRegionName(region_id, infoRegion.region_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "区域名称重复！");
			}

		}
		// 新增区域
		String sqlA = "";
		try {
			sqlA = SQLGenator.genSQL("I2656", infoRegion.region_id,
					infoRegion.region_name);
		} catch (AppException e) {

			e.printStackTrace();
		}
		sqlV.add(sqlA);

		String upStr = getUpdateFiledStr(infoRegion, region);
		if (upStr.length() > 0) {
			String sqlC = "";
			try {
				sqlC = SQLGenator.genSQL("C2657", upStr, region_id);
			} catch (AppException e) {

				e.printStackTrace();
			}
			sqlV.add(sqlC);
		}
		//
		String log_sql = getLogSql(request, "I2658", "1", region_id);
		sqlV.add(log_sql);
		//
		// 转化
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			System.out.println("sql==================" + sqlArr[i]);
		}

		// 执行(移到相应的action中)

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			String url = "RegionView.screen?submitType=1&region_id="
					+ region_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "区域信息新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "区域信息新增失败！");
		}

	}

	/**
	 * 变更区域信息
	 * 
	 * @param request
	 * @param infoRegion
	 * @throws HTMLActionException
	 */
	public static void updateRegionInfo(HttpServletRequest request,
			InfoRegionTable infoRegion) throws HTMLActionException {
		HttpSession session = request.getSession();
		String region_id = infoRegion.region_id;
		Vector sqlV = new Vector();

		//
		InfoRegionTable region = getRegionInfo(region_id);
		//
		if (!infoRegion.region_name.equals(region.region_name)) {
			if (isDupRegionName(region_id, infoRegion.region_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "区域名称重复！");
			}

		}
		String upStr = getUpdateFiledStr(infoRegion, region);
		if (upStr.length() > 0) {
			String sqlC = "";
			try {
				sqlC = SQLGenator.genSQL("C2657", upStr, region_id);
			} catch (AppException e) {

				e.printStackTrace();
			}
			sqlV.add(sqlC);
		}
		//
		String log_sql = getLogSql(request, "I2658", "2", region_id);
		sqlV.add(log_sql);

		// 转化
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			System.out.println("sql==================" + sqlArr[i]);
		}

		// 执行(移到相应的action中)

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			String url = "RegionView.screen?submitType=1&region_id="
					+ region_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "区域信息变更成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "区域信息变更失败！");
		}

	}

	/**
	 * 删除部门信息(目前不使用) 应为区域不能删除 并且有效标志无效!
	 * 
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void deleteRegionInfo(HttpServletRequest request,
			InfoRegionTable infoRegion) throws HTMLActionException {
		HttpSession session = request.getSession();
		String region_id = infoRegion.region_id;
		Vector sqlv = new Vector();
		// 注意
		if (regionReferenced_dept(region_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该区域下还有部门，不可以删除！");

		}
		if (regionReferenced_role(region_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该区域下还有角色，不可以删除！");

		}
		if (regionReferenced_user(region_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该区域下还有操作员，不可以删除！");

		}
		if (regionReferenced_self(region_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该区域下还有下级区域，不可以删除！");

		}

		// 删除区域信息
		//
		String log_sql = getLogSql(request, "I2658", "3", region_id);
		sqlv.add(log_sql);
		String sqla = "";
		sqla = "delete from ui_info_region where region_id='" + region_id + "'";
		sqlv.add(sqla);

		// 转化
		String sqlArr[] = new String[sqlv.size()];
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i] = sqlv.get(i).toString();
			System.out.println(sqlArr[i]);
		}
		// 执行

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "区域信息删除成功！",
					"RegionView.screen?submitType=2");
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "区域信息删除失败！");
		}

	}

	/**
	 * 判断区域名称是否相同
	 * 
	 * @param oper_no
	 * @param login_name
	 * @return
	 */
	public static boolean isDupRegionName(String region_id, String region_name) {
		String sql = "";
		boolean flag = false;
		try {
			sql = SQLGenator.genSQL("Q2655", region_id, region_name);
			//
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = true;
			}
		} catch (AppException ex) {
		}
		return flag;
	}

	/**
	 * 取得区域变更字段
	 * 
	 * @param deptInfo
	 * @param olddept
	 * @return
	 */
	private static String getUpdateFiledStr(InfoRegionTable regionInfo,
			InfoRegionTable oldregion) {
		String upStr = "";

		if (!regionInfo.region_name.equals(oldregion.region_name)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " region_name = '" + regionInfo.region_name + "'";

		}

		if (!regionInfo.parent_id.equals(oldregion.parent_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " parent_id = '" + regionInfo.parent_id + "'";

		}

		if (!regionInfo.region_code.equals(oldregion.region_code)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " region_code = '" + regionInfo.region_code + "'";

		}

		if (!regionInfo.status.equals(oldregion.status)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " isactive = " + regionInfo.status;

		}

		if (!regionInfo.comments.equals(oldregion.comments)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " comments = '" + regionInfo.comments + "'";

		}

		return upStr;
	}

	/**
	 * 取得日志信息SQL
	 * 
	 * @param request
	 * @param oper_no
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = "";// CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_oper_no, ip, type,
					para_a);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	/**
	 * 取得日志信息SQL
	 * 
	 * @param request
	 * @param oper_no
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_oper_no, ip, type,
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
	 * @param oper_no
	 * @param type
	 * @return
	 */
	public static String getLogSql(HttpServletRequest request, String sql_no,
			String type, String para_a, String para_b, String para_c) {
		String sql = "";
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());
		try {
			sql = SQLGenator.genSQL(sql_no, sessionID, oper_oper_no, ip, type,
					para_a, para_b, para_c);
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return sql;
	}

	/**
	 * 判断区域下面是否还有相应的部门
	 * 
	 * @param region_id
	 * @return
	 */
	public static boolean regionReferenced_dept(String region_id) {

		boolean flag = false;
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2665", region_id, region_id);
			//
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = true;
			}
		} catch (AppException ex) {
		}
		return flag;

	}

	/**
	 * 判断区域下是否还有相应的角色
	 * 
	 * @param region_id
	 * @return
	 */
	public static boolean regionReferenced_role(String region_id) {
		boolean flag = false;
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2666", region_id, region_id);
			//
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = true;
			}
		} catch (AppException ex) {
		}
		return flag;

	}

	/**
	 * 判断区域下面是否还有操作员
	 * 
	 * @param region_id
	 * @return
	 */
	public static boolean regionReferenced_user(String region_id) {
		boolean flag = false;
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2664", region_id, region_id);
			//
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = true;
			}
		} catch (AppException ex) {
		}
		return flag;

	}

	/**
	 * 判断本区域下面是否有子区域
	 * 
	 * @param region_id
	 * @return
	 */
	public static boolean regionReferenced_self(String region_id) {
		boolean flag = false;
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2663", region_id);
			//
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = true;
			}
		} catch (AppException ex) {
		}
		return flag;
	}

	/**
	 * 
	 * 
	 * @param region_id
	 * @return
	 */
	public static boolean CheckExistRegion(String region_id) {
		boolean flag = false;
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2667", region_id);
			//
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v != null && v.size() > 0) {
				flag = true;
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * 查询区域名称
	 * 
	 * @param region_id
	 * @return
	 */
	public static String getRegionName(String region_id) {
		String name = "";
		String sql = "SELECT REGION_NAME FROM ui_INFO_REGION WHERE REGION_ID = '"
				+ region_id + "' ";

		try {
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				name = rs[0][0];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 查询区域名称
	 * 
	 * @param region_id
	 * @return
	 */
	public static String getGroupName(String group_id) {
		String name = "";
		String sql = "SELECT group_NAME FROM ui_info_user_group WHERE group_ID = '"
				+ group_id + "' ";

		try {
			String[][] rs = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				name = rs[0][0];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return name;
	}

}