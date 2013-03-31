package com.ailk.bi.subject.util;

import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebKeys;

/**
 * @author XDOU 清除表格模版的缓存对象
 */
public class SubjectCacheUtil {
	/**
	 * 清除SESSION变量
	 * 
	 * @param session
	 *            session 对象
	 * @param tableId
	 *            表格标识
	 */
	public static void clearWebCache(HttpSession session, String tableId) {
		session.removeAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ + "_"
				+ tableId);
		session.removeAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ + "_"
				+ tableId);
		session.removeAttribute(WebKeys.ATTR_SUBJECT_COMMON_HTML + "_"
				+ tableId);
		session.removeAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_LIST + "_"
				+ tableId);

		session.removeAttribute(WebKeys.ATTR_SUBJECT_COMMON_EXPORT + "_"
				+ tableId);

		session.removeAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES + "_"
				+ tableId);

	}
}
