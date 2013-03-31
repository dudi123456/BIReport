package com.ailk.bi.base.util;

import com.ailk.bi.base.common.CSysCode;
import com.ailk.bi.base.table.InfoDeptTable;
import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.base.table.InfoPageItemTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletContext;

/**
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SecurityTool {

	/**
	 * 
	 */

	/**
	 * 查询取得部门信息
	 * 
	 * @param dept_no
	 * @return
	 */
	public static InfoDeptTable getDeptInfoFromULP(String dept_no) {
		InfoDeptTable list = new InfoDeptTable();
		String sql = "";
		try {
			sql = SQLGenator.genSQL("Q0011", dept_no);
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				list.dept_no = arr[0][0];
				list.parent_dept_no = arr[0][1];
				list.path_code = arr[0][2];
				list.dept_type = arr[0][3];
				list.dept_name = arr[0][4];
				list.local_net = arr[0][5];
				list.comments = arr[0][6];
				list.phone = arr[0][7];
				list.fax = arr[0][8];
				list.address = arr[0][9];
				list.flag = arr[0][10];
				list.res_init_a = arr[0][11];
				list.res_init_b = arr[0][12];
				list.res_char_a = arr[0][13];
				list.res_char_b = arr[0][14];
				list.area_id = arr[0][15];
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return list;

	}

	/**
	 * 查询指定工号操作员信息
	 * 
	 * <Stmt ID="Q0001" Desc="提取操作员信息" Author="jcm"> SELECT
	 * OPER_NO,REGION_ID,DEPT_NO,ROLE_CODE,OPER_NAME,LOGIN_NAME,
	 * OPER_PWD,QUESTION,ANSWER,GENDER,POSITION,PHONE,EMAIL,MOBILE_NO,
	 * NEED_SMS,OPER_MESSAGE,EFF_DATE,EXP_HINT_DATE,EXP_DATE,INIT_FLAG,
	 * DEAL_TYPE,APP_TYPE,STATISTICAL_DEPT_NO,OPER_ALIAS,LOGIN_COUNT,
	 * RES_INIT1,RES_INIT2,RES_CHAR1,RES_CHAR2,PWD_ENCODE,OPER_STATUS,OPER_KIND
	 * FROM ULP.INFO_OPER WHERE OPER_NO = '?' </Stmt>
	 * 
	 * @param oper_no
	 *            操作员工号
	 * @return InfoOperTable 操作员信息对象
	 * @author jcm
	 */

	public static InfoOperTable getOperInfoFromULP(String oper_no) {

		InfoOperTable infoOper = new InfoOperTable();
		String sql = "";

		try {
			sql = SQLGenator.genSQL("Q0001", oper_no);
			Vector v = WebDBUtil.execQryVector(sql, "");
			if (v.size() > 0) {
				Vector tempv = (Vector) v.get(0);
				infoOper.oper_no = tempv.get(0).toString();
				infoOper.region_id = tempv.get(1).toString();
				infoOper.dept_no = tempv.get(2).toString();
				infoOper.dept_name = getDeptInfoFromULP(infoOper.dept_no).dept_name;
				infoOper.role_code = tempv.get(3).toString();
				infoOper.oper_name = tempv.get(4).toString();
				infoOper.login_name = tempv.get(5).toString();
				infoOper.oper_pwd = tempv.get(6).toString();
				// infoOper.oper_pwd = CommTool.getDecrypt(
				// tempv.get(29).toString()).trim();
				infoOper.question = tempv.get(7).toString();
				infoOper.answer = tempv.get(8).toString();
				infoOper.gender = tempv.get(9).toString();
				infoOper.position = tempv.get(10).toString();
				infoOper.phone = tempv.get(11).toString();
				infoOper.email = tempv.get(12).toString();
				infoOper.mobile_no = tempv.get(13).toString();
				infoOper.need_sms = tempv.get(14).toString();
				infoOper.oper_message = tempv.get(15).toString();
				infoOper.eff_date = tempv.get(16).toString();
				infoOper.exp_hint_date = tempv.get(17).toString();
				infoOper.exp_date = tempv.get(18).toString();
				infoOper.init_flag = tempv.get(19).toString();
				infoOper.deal_type = tempv.get(20).toString();
				infoOper.app_type = tempv.get(21).toString();
				infoOper.statistical_dept_no = tempv.get(22).toString();
				infoOper.oper_alias = tempv.get(23).toString();
				infoOper.login_count = tempv.get(24).toString();
				infoOper.res_init_a = tempv.get(25).toString();
				infoOper.res_init_b = tempv.get(26).toString();
				infoOper.res_char_a = tempv.get(27).toString();
				infoOper.res_char_b = tempv.get(28).toString();
				infoOper.pwd_encode = tempv.get(29).toString();
				infoOper.oper_status = tempv.get(30).toString();
				infoOper.oper_kind = tempv.get(31).toString();
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return infoOper;
	}

	/**
	 * 提取BI系统的所有界面元素
	 * 
	 * @return
	 * @author jcm
	 */
	public static InfoPageItemTable[] getBIPageItemFromULP() {

		InfoPageItemTable[] items = null;
		String sql = "";

		try {
			sql = SQLGenator.genSQL("Q0031", CSysCode.SYS_CODE);
			System.out.println("Q0031================" + sql);
			String[][] arr = WebDBUtil.execQryArray(DBTool.getWLSConn(), sql,
					"");
			if (arr != null && arr.length > 0) {
				items = new InfoPageItemTable[arr.length];
				for (int i = 0; i < items.length; i++) {
					items[i] = new InfoPageItemTable();
					items[i].item_code = arr[i][0];
					items[i].menu_code = arr[i][1];
					items[i].item_title = arr[i][2];
					items[i].item_type = arr[i][3];
					items[i].form_name = arr[i][4];
					items[i].item_name = arr[i][5];
					items[i].item_value = arr[i][6];
					items[i].command = arr[i][7];
					items[i].isactive = arr[i][8];
					items[i].seq_no = arr[i][9];
				}

			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return items;
	}

	/**
	 * 根据操作员工号取得角色列表
	 * 
	 * <Stmt ID="Q0003" Desc="提取操作员角色列表字符串" Author="jcm"> select role_code from
	 * ulp.rule_oper_role where oper_no = '?' </Stmt>
	 * 
	 * @param oper_no
	 *            操作员工号
	 * @return String
	 * @author jcm
	 */

	public static String getOperRoleStrFromULP(String oper_no) {
		String roleStr = "";
		String sql = "";

		try {
			sql = SQLGenator.genSQL("Q0003", oper_no);
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; arr != null && i < arr.length; i++) {
				if (roleStr.length() > 0) {
					roleStr += ",";
				}
				roleStr += "'" + arr[i][0] + "'";
			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return roleStr;
	}

	/**
	 * 装载资源名称和标识对照MAP参数（统一资源表）
	 * 
	 * @param session
	 * @author jcm
	 */
	public static void setAllResConstsKeyValue(ServletContext context) {

		HashMap map = new HashMap();
		// 装载所有资源
		String sql = "";
		try {
			sql = SQLGenator.genSQL("AIS1002");
			System.out.println("AIS1002=================" + sql);
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			for (int i = 0; arr != null && i < arr.length; i++) {
				map.put(arr[i][0], arr[i][1]);
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		context.setAttribute(WebConstKeys.ATTR_C_RES_ID_VS_NAME, map);
		//

	}

	/**
	 * 取得区域，部门等常量信息
	 * 
	 * @param type
	 * @return
	 */
	public static HashMap getConstMap(String type) {
		HashMap map = new HashMap();
		String sql = "";
		String[][] arr = null;
		if ("REGION".equals(type.toUpperCase())) {
			try {
				sql = SQLGenator.genSQL("AIS1001");
				arr = WebDBUtil.execQryArray(sql, "");
			} catch (AppException e) {

				e.printStackTrace();
			}

		} else if ("DEPT".equals(type.toUpperCase())) {
			try {
				System.out.println(sql);
				sql = SQLGenator.genSQL("AIS1003");
				System.out.println(sql);
				arr = WebDBUtil.execQryArray(sql, "");
			} catch (AppException e) {

				e.printStackTrace();
			}
		}

		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				map.put(arr[i][0], arr[i][1]);
			}

		}

		return map;

	}

}
