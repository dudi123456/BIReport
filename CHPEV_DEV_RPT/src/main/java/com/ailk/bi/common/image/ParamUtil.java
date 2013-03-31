package com.ailk.bi.common.image;

import javax.servlet.ServletRequest;

public class ParamUtil {

	public ParamUtil() {
	}

	public static String getParameter(ServletRequest request, String paramName) {
		String temp = request.getParameter(paramName);
		if (temp != null && !temp.equals(""))
			return temp;
		else
			return "";
	}

	public static int getIntParameter(ServletRequest request, String paramName,
			int defaultNum) {
		String temp = request.getParameter(paramName);
		if (temp != null && !temp.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception exception) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}
}
