package com.ailk.bi.system.common;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.base.table.InfoOperTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

public class CommonUtil {
	private static Log logger = LogFactory.getLog(CommonUtil.class);

	public static long getNextVal(String seq) {
		// AdhocTreeFacade facade = new AdhocTreeFacade(new AdhocTreeDao());

		String sql = "";
		String[][] result = null;
		try {

			sql = "select " + seq + ".nextval from dual";
			result = WebDBUtil.execQryArray(sql, "");

		} catch (AppException e) {
			e.printStackTrace();
		}

		return Long.parseLong(result[0][0]);
	}

	public static String format(long intval) {
		String formatted = Long.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	public static String[][] getBulletin(HttpSession session) {
		String[][] rs = null;

		InfoOperTable loginUser = CommonFacade.getLoginUser(session);
		String systemId = StringB.NulltoBlank((String) session
				.getAttribute("system_id"));
		if (systemId.length() == 0) {
			systemId = "1";
		}

		String sqlBul = "select distinct bulletin_id from UI_RULE_BULLETIN_USERGRP t where  "
				+ "(t.link_type='1' and link_id='"
				+ loginUser.user_id
				+ "') or ( t.link_type='2' and link_id='"
				+ loginUser.group_id
				+ "')";

		String sql = "select id,title  from ui_info_bulletin t where (id in("
				+ sqlBul
				+ ") OR FIELD_BAK_01='0') and "
				+ "to_char(valid_begin_date, 'yyyymmdd')<= to_char(sysdate, 'yyyymmdd') and to_char(valid_end_date, 'yyyymmdd') >= to_char(sysdate, 'yyyymmdd')";

		sql += " and system_id in(0," + systemId + ") order by t.id desc";

		try {
			logger.debug(sql);
			rs = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}
		return rs;
	}
}
