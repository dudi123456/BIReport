package com.ailk.bi.system.common;

import java.util.*;
import javax.servlet.http.*;

import com.ailk.bi.base.table.*;
import com.ailk.bi.base.util.*;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LSInfoDept {

	public static InfoDeptTable getDeptInfo(String dept_id) {

		String sql = "";
		InfoDeptTable infoDept = new InfoDeptTable();
		try {
			sql = "SELECT DEPT_id ,PARENT_id,DEPT_TYPE,DEPT_NAME,LOCAL_NET,COMMENTS,FLAG,PATH_CODE,PHONE,FAX,ADDRESS,REGION_ID"
					+ " FROM ui_INFO_DEPT a WHERE DEPT_id ='" + dept_id + "'";
			String rs[][] = WebDBUtil.execQryArray(sql, "");
			if (rs != null && rs.length > 0) {
				infoDept.dept_id = rs[0][0];
				infoDept.parent_id = rs[0][1];
				infoDept.parent_dept_id = rs[0][1];
				infoDept.parent_dept_name = getDeptName(infoDept.parent_id);
				infoDept.dept_type = rs[0][2];
				infoDept.dept_name = rs[0][3];
				infoDept.local_net = rs[0][4];
				infoDept.comments = rs[0][5];
				infoDept.flag = rs[0][6];
				infoDept.path_code = getDeptPathCode(infoDept.dept_id);
				infoDept.phone = rs[0][8];
				infoDept.fax = rs[0][9];
				infoDept.address = rs[0][10];
				infoDept.region_id = rs[0][11];
				infoDept.region_name = LSInfoRegion
						.getRegionName(infoDept.region_id);
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return infoDept;

	}

	/**
	 * 增加操作员信息
	 *
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void addNewDept(HttpServletRequest request,
			InfoDeptTable infoDept) throws HTMLActionException {

		HttpSession session = request.getSession();

		String dept_id = infoDept.dept_id;
		Vector sqlV = new Vector();
		//
		if (CheckExistDept(dept_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "部门编码已经存在！");

		}

		//
		InfoDeptTable dept = getDeptInfo(dept_id);
		//
		if (!infoDept.dept_name.equals(dept.dept_name)) {
			if (isDupDeptName(dept_id, infoDept.dept_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "部门名称重复！");
			}

		}
		String sqlA = "";
		try {
			sqlA = SQLGenator.genSQL("I2301", dept_id, infoDept.dept_type,
					infoDept.dept_name, "010");
		} catch (AppException e) {

			e.printStackTrace();
		}
		sqlV.add(sqlA);
		// }
		String upStr = getUpdateFiledStr(infoDept, dept);
		if (upStr.length() > 0) {
			String sqlC = "";
			try {
				sqlC = SQLGenator.genSQL("C2302", upStr, dept_id);
			} catch (AppException e) {

				e.printStackTrace();
			}
			sqlV.add(sqlC);
		}
		//
		String log_sql = getLogSql(request, "I2312", "1", dept_id);
		sqlV.add(log_sql);
		// 转化
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();

		}

		// 执行(移到相应的action中)

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			String url = "DeptView.screen?submitType=1&dept_id=" + dept_id
					+ "&region_id=" + infoDept.area_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "部门信息新增成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "部门信息新增失败！");
		}

	}

	/**
	 * 变更操作员信息
	 *
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void updateDeptInfo(HttpServletRequest request,
			InfoDeptTable infoDept) throws HTMLActionException {
		HttpSession session = request.getSession();
		String dept_id = infoDept.dept_id;
		String region_id = infoDept.area_id;
		Vector sqlV = new Vector();

		InfoDeptTable dept = getDeptInfo(dept_id);
		//
		if (!infoDept.dept_name.equals(dept.dept_name)) {
			if (isDupDeptName(dept_id, infoDept.dept_name)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "部门名称重复！");
			}

		}
		//
		String upStr = getUpdateFiledStr(infoDept, dept);
		if (upStr.length() > 0) {
			String sqlC = "";
			try {
				sqlC = SQLGenator.genSQL("C2302", upStr, dept_id);
			} catch (AppException e) {

				e.printStackTrace();
			}
			sqlV.add(sqlC);
		}
		//
		String log_sql = getLogSql(request, "I2312", "2", dept_id);
		sqlV.add(log_sql);

		// 转化
		String sqlArr[] = new String[sqlV.size()];
		for (int i = 0; i < sqlV.size(); i++) {
			sqlArr[i] = sqlV.get(i).toString();
			// System.out.println("sql==================" + sqlArr[i]);
		}

		// 执行(移到相应的action中)

		int i = 1;
		try {
			WebDBUtil.execTransUpdate(sqlArr);
		} catch (Exception e) {
			i = -1;
		}

		if (i > 0) {
			String url = "DeptView.screen?submitType=1&dept_id=" + dept_id
					+ "&region_id=" + region_id;
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "部门信息变更成功！", url);
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "部门信息变更失败！");
		}

	}

	/**
	 * 删除部门信息
	 *
	 * @param session
	 * @param infoOper
	 * @throws HTMLActionException
	 */
	public static void deleteDeptInfo(HttpServletRequest request,
			InfoDeptTable infoDept) throws HTMLActionException {
		HttpSession session = request.getSession();

		String dept_id = infoDept.dept_id;
		Vector sqlv = new Vector();
		// 注意
		if (deptReferenced(dept_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该部门下还有操作员，不可以删除！");

		}
		// 注意
		if (deptReferencedSelf(dept_id)) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "该部门下还有下级部门，不可以删除！");

		}
		//
		String log_sql = getLogSql(request, "I2312", "3", dept_id);
		sqlv.add(log_sql);

		// 删除部门信息
		String sqla = "";
		try {
			sqla = SQLGenator.genSQL("D2307", dept_id);
		} catch (AppException e) {

			e.printStackTrace();
		}
		sqlv.add(sqla);

		// 转化
		String sqlArr[] = new String[sqlv.size()];
		for (int i = 0; i < sqlv.size(); i++) {
			sqlArr[i] = sqlv.get(i).toString();
			System.out.println("sqlArr:" + i + ":" + sqlArr[i]);
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
					HTMLActionException.SUCCESS_PAGE, "部门信息删除成功！",
					"DeptView.screen?submitType=2");
		} else {
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "部信息删除失败！");
		}

	}

	/**
	 * 判断部门名称是否相同
	 *
	 * @param oper_no
	 * @param login_name
	 * @return
	 */
	public static boolean isDupDeptName(String dept_id, String dept_name) {
		String sql = "";
		boolean flag = false;
		try {
			sql = SQLGenator.genSQL("Q2300", dept_id, dept_name);
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
	 * 取得部门变更字段
	 *
	 * @param deptInfo
	 * @param olddept
	 * @return
	 */
	private static String getUpdateFiledStr(InfoDeptTable deptInfo,
			InfoDeptTable olddept) {
		String upStr = "";
		if (!deptInfo.dept_name.equals(olddept.dept_name)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " dept_name = '" + deptInfo.dept_name + "'";

		}
		if (!deptInfo.parent_id.equals(olddept.parent_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " parent_id = '" + deptInfo.parent_id + "'";

		}

		if (!deptInfo.parent_dept_id.equals(olddept.parent_dept_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " parent_id = '" + deptInfo.parent_dept_id + "'";

		}

		if (!deptInfo.dept_type.equals(olddept.dept_type)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " dept_type = '" + deptInfo.dept_type + "'";

		}
		if (!deptInfo.region_id.equals(olddept.region_id)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " region_id = '" + deptInfo.region_id + "'";

		}
		if (!deptInfo.comments.equals(olddept.comments)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " comments = '" + deptInfo.comments + "'";

		}
		if (!deptInfo.flag.equals(olddept.flag)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " flag = '" + deptInfo.flag + "'";

		}
		if (!deptInfo.fax.equals(olddept.fax)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " fax = '" + deptInfo.fax + "'";

		}
		if (!deptInfo.phone.equals(olddept.phone)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " phone = '" + deptInfo.phone + "'";

		}
		if (!deptInfo.address.equals(olddept.address)) {
			if (upStr.length() > 0) {
				upStr += " , ";
			}
			upStr += " address = '" + deptInfo.address + "'";

		}

		return upStr;
	}

	/**
	 * 判断当前部门是否存在用户
	 *
	 * @param role_code
	 * @return flag
	 * @author jcm
	 */
	public static boolean deptReferenced(String dept_id) {
		String sql = "";
		boolean flag = false;
		try {
			sql = SQLGenator.genSQL("Q2304", dept_id);

			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v.size() > 0) {
				flag = true;
			}

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断当前部门是否存在下级部门
	 *
	 * @param role_code
	 * @return flag
	 * @author jcm
	 */
	public static boolean deptReferencedSelf(String dept_id) {
		String sql = "";
		boolean flag = false;
		try {
			sql = SQLGenator.genSQL("Q2313", dept_id);

			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v.size() > 0) {
				flag = true;
			}

		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return flag;
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
		String ip = CommTool.getClientIP(request);
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
		String ip = "";// CommTool.getClientIP(request);
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
		String ip = "";// CommTool.getClientIP(request);
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
	 * 取得某区域的path_code
	 *
	 * @param region_id
	 * @return
	 */
	public static String getDeptPathCode(String dept_id) {
		String path_code = "";
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2311", dept_id);
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
	 * 判断部门标识是否存在
	 *
	 * @param dept_id
	 * @return
	 */
	public static boolean CheckExistDept(String dept_id) {
		boolean flag = false;
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q2318",dept_id);
			String[][] result = WebDBUtil.execQryArray(sql, "");

			if (result != null && result.length > 0) {
				flag = true;
			}
		} catch (AppException ex) {
			System.out.println("isDeptExists sql error ====== " + sql);
			ex.printStackTrace();
		}
		return flag;
	}

	public static String getUserDeptString(String dept_id, String oper_no) {
		String sql = "";
		String ss = "";
		try {
			sql = SQLGenator.genSQL("Q2669", dept_id, oper_no);
			String[][] re = WebDBUtil.execQryArray(sql, "");
			if (re.length > 0) {
				ss = re[0][0] + ";" + re[0][1] + ";" + re[0][2] + ";"
						+ re[0][3];
			}
		} catch (AppException ex) {
			ex.printStackTrace();
		}
		return ss;
	}

	/**
	 * 取得部门名称
	 *
	 * @param dept_id
	 * @return
	 */
	public static String getDeptName(String dept_id) {
		String name = "";
		String sql = "select dept_name from ui_info_dept where dept_id ='"
				+ dept_id + "'";
		if (dept_id == null || "".equals(dept_id)) {
			name = "没有上级部门";
			return name;
		}

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
