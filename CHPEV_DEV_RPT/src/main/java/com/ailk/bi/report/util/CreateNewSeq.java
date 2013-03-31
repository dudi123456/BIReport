package com.ailk.bi.report.util;

import com.ailk.bi.common.dbtools.WebDBUtil;

public class CreateNewSeq {
	public CreateNewSeq() {

	}

	public static String getLhbb_batch_seq(String DbType) {
		String result = "";

		try {
			String sql = "   select seq_rpt_id.NEXTVAL  from dual ";

			String[][] svces = WebDBUtil.execQryArray(sql, "");

			result = svces[0][0];
			for (int i = 5 - result.length(); i > 0; i--) {
				result = "0" + result;
			}

		} catch (Exception e) {
			result = "getSeq false";
		}

		return result;
	}

}
