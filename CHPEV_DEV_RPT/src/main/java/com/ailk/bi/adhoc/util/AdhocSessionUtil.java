package com.ailk.bi.adhoc.util;

import javax.servlet.http.HttpSession;

public class AdhocSessionUtil {
	public static void clearUserInfo(HttpSession session) {
		if (null != session) {
			session.removeAttribute(AdhocConstant.ADHOC_USER_BASE_INFO);
			session.removeAttribute(AdhocConstant.ADHOC_USER_CONSUME_INFO);
			session.removeAttribute(AdhocConstant.ADHOC_USER_CALL_INFO);
			session.removeAttribute(AdhocConstant.ADHOC_USER_CONTRIBUTE_INFO);
			session.removeAttribute(AdhocConstant.ADHOC_USER_SCORE_INFO);

		}
	}
}
