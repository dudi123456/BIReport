package com.ailk.bi.subject.util;

import com.ailk.bi.base.struct.KeyValueStruct;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AjaxActionUtil {

	private static Log logger = LogFactory.getLog(AjaxActionUtil.class);

	/**
	 * 
	 * @param sqlcode
	 * @param param
	 * @return
	 */
	public static KeyValueStruct[] getObjInfoByParam(String sqlcode,
			String param) {

		KeyValueStruct struct[] = null;
		if (sqlcode == null || "".equals(sqlcode)) {
			return struct;
		}
		// 默认置为全部
		if (param == null) {
			param = "";
		}

		String sql = "";
		try {
			String arr[][] = null;
			if ("".equals(param)) {
				sql = SQLGenator.genSQL(sqlcode);
			} else {
				sql = SQLGenator.genSQL(sqlcode, param);
			}

			logger.debug(sql);

			arr = WebDBUtil.execQryArray(sql, "");

			if (arr != null && arr.length > 0) {
				struct = new KeyValueStruct[arr.length];
				for (int i = 0; i < arr.length; i++) {
					struct[i] = new KeyValueStruct();
					struct[i].key = arr[i][0];
					struct[i].value = arr[i][1];
				}
			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return struct;

	}

	/**
	 * 
	 * @param struct
	 * @param divId
	 * @param textname
	 * @param textvalue
	 * @param hidname
	 * @param hidvalue
	 * @param script
	 * @return
	 */
	public static String getDivStr(String sqlid, String param, String divId,
			String textname, String hidname) {

		KeyValueStruct struct[] = getObjInfoByParam(sqlid, param);

		StringBuffer buff = new StringBuffer("");

		buff.append(" <table width=\"100%\"  border=\"0\" align=\"center\" cellpadding=\"4\" cellspacing=\"0\" class=\"TableInDiv\"> \n");

		if (struct == null || struct.length <= 0) {
			buff.append("<tr>");
			buff.append("<td class=\"itemTd0\" nowrap>");
			buff.append("<a href=\"javascript:;\" URL='" + ":全部"
					+ "' id='allFlag'  onclick=\"nodeClick('" + textname
					+ "','" + hidname + "',this,'" + divId + "');");
			buff.append("\" >");
			buff.append("全部");
			buff.append("</a>");
			buff.append("</td>");
			buff.append("</tr>\n");

		} else {
			buff.append("<tr>");
			buff.append("<td class=\"itemTd0\" nowrap>");
			buff.append("<a href=\"javascript:;\" URL='" + ":全部"
					+ "' id='allFlag'  onclick=\"nodeClick('" + textname
					+ "','" + hidname + "',this,'" + divId + "');");
			buff.append("\" >");
			buff.append("全部");
			buff.append("</a>");
			buff.append("</td>");
			buff.append("</tr>\n");

			for (int i = 0; i < struct.length; i++) {
				buff.append("<tr>");
				buff.append("<td class=\"itemTd0\" nowrap>");
				buff.append("<a href=\"javascript:;\" URL='" + struct[i].key + ":"
						+ struct[i].value + "' id='" + struct[i].key
						+ "'  onclick=\"nodeClick('" + textname + "','"
						+ hidname + "',this,'" + divId + "')\" >");
				buff.append(struct[i].value);
				buff.append("</a>");
				buff.append("</td>");
				buff.append("</tr>\n");
			}
		}

		buff.append("</table>");
		buff.append("</div>");

		return buff.toString();

	}

}
