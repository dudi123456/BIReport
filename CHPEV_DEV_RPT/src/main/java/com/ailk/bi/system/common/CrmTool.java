package com.ailk.bi.system.common;

import java.util.HashMap;

import com.ailk.bi.base.table.BillMonDefTable;
import com.ailk.bi.base.table.GeneralReportDefTable;
import com.ailk.bi.base.util.DBTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CrmTool {

	/**
	 * 放欺诈报表纬度列表
	 * 
	 * @param type
	 * @return
	 */
	public static HashMap getCrmMap(String type) {
		HashMap map = new HashMap();
		String sql = "";

		if ("user".equals(type)) {// 用户类型
			sql = "select code_id , code_name from router.code_list@DEV_CRM where type_code = 'fraud_user_type'";
		} else if ("level".equals(type)) {// 欺诈等级
			sql = "select code_id , code_name from router.code_list@DEV_CRM where type_code = 'fraud_emerg_level'";
		} else if ("tele".equals(type)) {
			sql = "select code_id , code_name from router.code_list@DEV_CRM where type_code = 'fraud_tele_type'";
		} else if ("code".equals(type)) {
			sql = "select code_id , code_name from router.code_list@DEV_CRM where type_code = 'fraud_item_code'";
		} else if ("record".equals(type)) {
			sql = "select code_id , code_name from router.code_list@DEV_CRM where type_code = 'record_type'";
		}

		String arr[][] = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				map.put(arr[i][0], arr[i][1]);
			}
		}
		return map;

	}

	/**
	 * 总账报表定义 type=0 初始查找 else 指定查找
	 * 
	 * @param type
	 * @return
	 */
	public static GeneralReportDefTable getFirstGeneral(String type) {
		String sql = "";

		if ("0".equals(type)) {
			sql = "select general_ledger_id ,general_ledger_name ,general_ledger_type "
					+ " from general_report_def"
					+ " where general_ledger_status = 'Y' order by general_ledger_id";
		} else {
			sql = "select general_ledger_id ,general_ledger_name ,general_ledger_type "
					+ " from general_report_def"
					+ " where general_ledger_status = 'Y'"
					+ " and general_ledger_id = " + type;
		}
		GeneralReportDefTable def = new GeneralReportDefTable();
		String arr[][] = null;
		try {
			arr = WebDBUtil
					.execQryArray(DBTool.getFixedWLSConn("AcctSource",
							"t3://localhost:7100"), sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			def.general_ledger_id = arr[0][0];
			def.general_ledger_name = arr[0][1];
			def.general_ledger_type = arr[0][2];
		}
		return def;

	}

	/**
	 * 帐务周期表信息
	 * 
	 * @param type
	 * @return
	 */
	public static BillMonDefTable getFixBillMonth(String type) {
		String sql = "";
		if ("temp".equals(type)) {
			sql = "select bill_id ,bill_name , bill_cycle , to_char(sta_time,'YYYY-MM-DD') , to_char(end_time ,'YYYY-MM-DD'), to_char(eff_date ,'YYYY-MM-DD'),to_char(exp_date,'YYYY-MM-DD') from bill_mon_def order by bill_id";
		} else {
			sql = "select bill_id ,bill_name , bill_cycle , to_char(sta_time,'YYYY-MM-DD') , to_char(end_time ,'YYYY-MM-DD'), to_char(eff_date ,'YYYY-MM-DD'),to_char(exp_date,'YYYY-MM-DD') from bill_mon_def where bill_id = '"
					+ type + "'";

		}

		BillMonDefTable def = new BillMonDefTable();
		String arr[][] = null;
		System.out.println(sql);
		try {
			arr = WebDBUtil
					.execQryArray(DBTool.getFixedWLSConn("AcctSource",
							"t3://localhost:7100"), sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			def.bill_id = arr[0][0];
			def.bill_name = arr[0][1];
			def.bill_cycle = arr[0][2];
			def.sta_time = arr[0][3];
			def.end_time = arr[0][4];
			def.eff_date = arr[0][5];
			def.exp_date = arr[0][6];
		}
		return def;

	}

}
