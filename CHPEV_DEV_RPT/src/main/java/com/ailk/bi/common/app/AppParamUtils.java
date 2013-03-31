package com.ailk.bi.common.app;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 此类取自Jive 用于从前台页面Encoding取值并做对象转换
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class AppParamUtils {
	/**
	 * Gets a parameter as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @return The value of the parameter or null if the parameter was not found
	 *         or if the parameter is a zero-length string.
	 */
	public static String getParameter(HttpServletRequest request, String name) {
		return getParameter(request, name, false);
	}

	/**
	 * Gets a parameter as a GB string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @return The value of the parameter or "" if the parameter was not found
	 *         or if the parameter is a zero-length string.
	 */
	public static String getParameterGB(HttpServletRequest request, String name) {
		return StringB.toGB(getParameter(request, name, ""));
	}

	/**
	 * Gets a parameter as a GB string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @param strDefault
	 *            default string if not found
	 * @return The value of the parameter or strDefault if the parameter was not
	 *         found or if the parameter is a zero-length string
	 */
	public static String getParameterGB(HttpServletRequest request,
			String name, String strDefault) {
		return StringB.toGB(getParameter(request, name, strDefault));
	}

	/**
	 * Gets a parameter as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @param emptyStringsOK
	 *            Return the parameter values even if it is an empty string.
	 * @return The value of the parameter or null if the parameter was not
	 *         found.
	 */
	public static String getParameter(HttpServletRequest request, String name,
			boolean emptyStringsOK) {
		String temp = request.getParameter(name);
		if (temp != null) {
			if (temp.equals("") && !emptyStringsOK) {
				return null;
			} else {
				return temp;
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets a parameter as a GB string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @param emptyStringsOK
	 * @return The value of the parameter or strDefault if the parameter was not
	 *         found or if the parameter is a zero-length string
	 */
	public static String getParameter(HttpServletRequest request, String name,
			String emptyStringsOK) {
		String temp = request.getParameter(name);
		if (temp != null) {
			return temp;
		} else {
			return emptyStringsOK;
		}
	}

	/**
	 * Gets a parameter's muti values as string array.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @param emptyStringsOK
	 * @return The values of the parameter or strDefault if the parameter was
	 *         not found or if the parameter is a zero-length string for
	 *         instance. param is a checkgrp.
	 */
	public static String[] getParameters(HttpServletRequest request, String name) {
		String[] temp = request.getParameterValues(name);
		if (temp != null) {
			return temp;
		} else {
			return null;
		}
	}

	/**
	 * Gets a parameter's muti values as GB string array.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @param emptyStringsOK
	 * @return The values of the parameter or strDefault if the parameter was
	 *         not found or if the parameter is a zero-length string for
	 *         instance. param is a checkgrp.
	 */
	public static String[] getParameterGBs(HttpServletRequest request,
			String name) {
		String[] temp = request.getParameterValues(name);
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				temp[i] = StringB.toGB(temp[i]);
			}
			return temp;
		} else {
			return null;
		}
	}

	/**
	 * Gets a parameter as a boolean.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @return True if the value of the parameter was "true", false otherwise.
	 */
	public static boolean getBooleanParameter(HttpServletRequest request,
			String name) {
		return getBooleanParameter(request, name, false);
	}

	/**
	 * Gets a parameter as a boolean.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @return True if the value of the parameter was "true", false otherwise.
	 */
	public static boolean getBooleanParameter(HttpServletRequest request,
			String name, boolean defaultVal) {
		String temp = request.getParameter(name);
		if ("true".equals(temp) || "on".equals(temp)) {
			return true;
		} else if ("false".equals(temp) || "off".equals(temp)) {
			return false;
		} else {
			return defaultVal;
		}
	}

	/**
	 * Gets a parameter as an int.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @return The int value of the parameter specified or the default value if
	 *         the parameter is not found.
	 */
	public static int getIntParameter(HttpServletRequest request, String name,
			int defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		return getIntParameter(request, name, 0);
	}

	/**
	 * Gets a list of int parameters.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @param defaultNum
	 *            The default value of a parameter, if the parameter can't be
	 *            converted into an int.
	 */
	public static int[] getIntParameters(HttpServletRequest request,
			String name, int defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new int[0];
		}
		int[] values = new int[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Integer.parseInt(paramValues[i]);
			} catch (Exception e) {
				values[i] = defaultNum;
			}
		}
		return values;
	}

	/**
	 * Gets a parameter as a double.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @return The double value of the parameter specified or the default value
	 *         if the parameter is not found.
	 */
	public static double getDoubleParameter(HttpServletRequest request,
			String name, double defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			double num = defaultNum;
			try {
				num = Double.parseDouble(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	/**
	 * 用小数格式的方式获得页面的参数,如果取不到或者失败，则返回按默认值
	 */
	public static BigDecimal getBigDecimalParameter(HttpServletRequest request,
			String name, int defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			BigDecimal num = new BigDecimal(defaultNum);
			try {
				double numDouble = Double.parseDouble(temp);
				num = new BigDecimal(numDouble);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return new BigDecimal(defaultNum);
		}
	}

	public static BigDecimal getBigDecimalParameter(HttpServletRequest request,
			String name) {
		return getBigDecimalParameter(request, name, 0);
	}

	/**
	 * 用"yyyy/MM/dd"时间邮票格式的方式获得页面的参数,如果取不到或者失败，则返回按默认值
	 */
	public static Timestamp getTimestampParameter(HttpServletRequest request,
			String name, Timestamp defaultDate) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			Timestamp numTime = defaultDate;
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date d = formatter.parse(temp);
				numTime = new Timestamp(d.getTime());
			} catch (Exception ignored) {
			}
			return numTime;
		} else {
			return defaultDate;
		}
	}

	/**
	 * 用"yyyy/MM/dd"日期格式的方式获得页面的参数,如果取不到或者失败，则返回按默认值
	 */
	public static java.sql.Date getDateParameter(HttpServletRequest request,
			String name, java.sql.Date defaultDate) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			java.sql.Date numTime = defaultDate;
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date d = formatter.parse(temp);
				numTime = new java.sql.Date(d.getTime());
			} catch (Exception ignored) {
			}
			return numTime;
		} else {
			return defaultDate;
		}
	}

	/**
	 * 用日期的方式获得页面的参数
	 */
	public static java.sql.Date getDateParameter(HttpServletRequest request,
			String name) {
		return getDateParameter(request, name, null);
	}

	/**
	 * 用时间邮票的方式获得页面的参数
	 */
	public static Timestamp getTimestampParameter(HttpServletRequest request,
			String name) {
		return getTimestampParameter(request, name, null);
	}

	/**
	 * 获取系统的当前的时间邮票
	 */
	public static Timestamp getNowTimestamp() {
		java.util.Date d = new java.util.Date();
		Timestamp numTime = new Timestamp(d.getTime());
		return numTime;

	}

	/**
	 * Gets a parameter as a long.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @return The long value of the parameter specified or the default value if
	 *         the parameter is not found.
	 */
	public static long getLongParameter(HttpServletRequest request,
			String name, long defaultNum) {
		String temp = request.getParameter(name);
		if (temp != null && !temp.equals("")) {
			long num = defaultNum;
			try {
				num = Long.parseLong(temp);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return defaultNum;
		}
	}

	/**
	 * Gets a list of long parameters.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the parameter you want to get
	 * @param defaultNum
	 *            The default value of a parameter, if the parameter can't be
	 *            converted into a long.
	 */
	public static long[] getLongParameters(HttpServletRequest request,
			String name, long defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new long[0];
		}
		long[] values = new long[paramValues.length];
		for (int i = 0; i < paramValues.length; i++) {
			try {
				values[i] = Long.parseLong(paramValues[i]);
			} catch (Exception e) {
				values[i] = defaultNum;
			}
		}
		return values;
	}

	/**
	 * Gets an object as a string.
	 * 
	 * @param request
	 *            The HttpServletRequest object, known as "request" in a JSP
	 *            page.
	 * @param name
	 *            The name of the attribute you want to get
	 * @return The long value of the attribute or the default value if the
	 *         attribute is not found or is a zero length string.
	 */
	public static String getObjectString(Object data) {
		return getObjectString(data, AppConst.HTMLBLANK);
	}

	public static String getObjectString(Object data, String strDefault) {
		return (data == null) ? strDefault : data.toString();
	}

	public static String getObjectStringGB(Object data) {
		return getObjectStringGB(data, AppConst.HTMLBLANK);
	}

	public static String getObjectStringGB(Object data, String strDefault) {
		return (data == null) ? strDefault : data.toString();
	}

	public static String getEditObjectString(Object data) {
		return getEditObjectString(data, AppConst.BLANK);
	}

	public static String getEditObjectString(Object data, String strDefault) {
		String strTemp = (data == null) ? strDefault : data.toString();
		return "\"" + strTemp + "\"";
	}

	public static String getEditObjectDateToString(Object data) {
		return getEditObjectDateToString(data, "");
	}

	public static String getEditObjectDateToString(Object data,
			String strDefault) {
		return "\"" + fromDataToString(data, strDefault) + "\"";
	}

	public static String getBigDecimalToString(BigDecimal data) {
		return getBigDecimalToString(data, "");
	}

	public static String getBigDecimalToString(BigDecimal data,
			String strDefault) {
		if (data == null) {
			return strDefault;
		} else {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			return String.valueOf(nf.format(data.doubleValue()));
		}
	}

	public static String getDateToString(Object data) {
		return fromDataToString(data, "");
	}

	public static String fromDataToString(Object data, String strDefault) {
		String strTemp = strDefault;
		if (data != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			strTemp = formatter.format((Timestamp) data);
		}
		return strTemp;
	}

	public static String getEditString(Object data, String strDefault) {
		String strTemp = (data == null) ? strDefault : data.toString();
		return strTemp;
	}

	public static String getEditString(Object data) {
		return getEditString(data, "");
	}

	public static Timestamp getDealTimestamp() {
		Date timenow = new Date();
		Timestamp dealTime = new Timestamp(timenow.getTime());
		return dealTime;

	}

	/**
	 * 检查当前用户是否为登录用户，如果不是则返回到登录页面
	 */
	public static void ChkUser(PageContext ctx, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		if (session.getAttribute(AppConst.LOGINUSERINFO) == null) {
			// com.asiabi.common.app.Debug.println("AppParamUtils class's
			// checkUser is OK!!!") ;
			try {

				ctx.getServletContext()
						.getRequestDispatcher("/web/login/login.jsp")
						.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
