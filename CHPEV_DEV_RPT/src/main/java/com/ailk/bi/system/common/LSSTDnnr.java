package com.ailk.bi.system.common;

import com.ailk.bi.common.dbtools.WebDBUtil;

public class LSSTDnnr {

	/**
	 * 取得政策类型
	 * 
	 * @param old_user_dnnr
	 * @return
	 */

	public static String getUserDnnr(String old_user_dnnr) {
		String userDnnr = "";
		String sql = "select user_dnnr from bi_rw.d_old_user_dnnr where old_user_dnnr = '"
				+ old_user_dnnr + "'";
		String arr[][] = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			userDnnr = arr[0][0];
		}
		return userDnnr;
	}

	/**
	 * 取得政策类型
	 * 
	 * @param old_user_dnnr
	 * @return
	 */

	public static String getUserDnnrBySvcKnd(String svc_knd) {
		String userDnnr = "";
		String sql = "select user_dnnr,userdnnr_desc from bi_rw.d_user_dnnr where svc_knd ='"
				+ svc_knd + "'";
		String arr[][] = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				if (userDnnr.length() > 0) {
					userDnnr += "|";
				}
				userDnnr += arr[i][0] + ":" + arr[i][1];
			}

		}
		return userDnnr;
	}

}
