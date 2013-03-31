package com.ailk.bi.common.app;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 有关字符串Byte的一些操作方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StringB {
	private final static String DOT_CONNECT = ".";

	private final static String ISOENCODING = "ISO8859-1";

	private final static String GBKENCODING = "GBK";

	// private final static boolean isGb_trans = false; //
	// GetSystemConfig.getConfig().getisGb_trans();

	/** 返回字符串的字节长度 */
	public static int length(String strExp) {
		return strExp.getBytes().length;
	}

	/**
	 * 计算一个串中包含的字串的个数
	 * 
	 * @param str
	 * @param subStr
	 * @return
	 */
	public static int countSubStr(String str, String subStr) {
		if (StringUtils.isEmpty(subStr) || StringUtils.isEmpty(str))
			return 0;
		int iCount = 0;
		int iPos = 0;
		while ((iPos = str.indexOf(subStr, iPos)) >= 0) {
			iCount++;
			iPos++;
		}
		return iCount;
	}

	/**
	 * 取一个字符串的左变的指定长度的字符或汉字
	 * 
	 * @param strExp
	 *            指定的字符串
	 * @param intLen
	 *            指定所取的长度
	 * @return 截取的串
	 */
	public static String left(String strExp, int intLen) {
		if (intLen <= 0)
			return "";

		if (length(strExp) <= intLen)
			return strExp;

		if (length(strExp) == strExp.length())
			return strExp.substring(0, intLen);

		int intCLoop = 0;
		int intBLoop = 0;
		byte abytTemp[] = strExp.getBytes();

		while (true) {
			if (abytTemp[intBLoop] > 127 || abytTemp[intBLoop] < 0) {
				if (intBLoop + 2 > intLen) {
					break;
				} else {
					intBLoop++;
					intBLoop++;
				}
			} else {
				if (intBLoop + 1 > intLen) {
					break;
				} else {
					intBLoop++;
				}
			}
			intCLoop++;
		}

		return strExp.substring(0, intCLoop);
	}

	/**
	 * 取一个字符串的右边的指定长度的字符或汉字
	 * 
	 * @param strExp
	 *            指定的字符串
	 * @param intLen
	 *            指定所取的长度
	 * @return 截取的串
	 */
	public static String right(String strExp, int intLen) {
		if (intLen <= 0)
			return "";

		if (length(strExp) <= intLen)
			return strExp;

		if (length(strExp) == strExp.length())
			return strExp.substring(strExp.length() - intLen);

		int intCLoop = 0;
		int intBLoop = 0;
		byte abytTemp[] = strExp.getBytes();

		while (intBLoop < length(strExp) - intLen) {
			if (abytTemp[intBLoop] > 127 || abytTemp[intBLoop] < 0) {
				intBLoop++;
				intBLoop++;
			} else {
				intBLoop++;
			}
			intCLoop++;

		}
		return strExp.substring(intCLoop);
	}

	/**
	 * 串截取
	 * 
	 * @param strExp
	 * @param intFrom
	 * @return
	 */
	public static String substring(String strExp, int intFrom) {
		if (intFrom <= 0)
			return strExp;

		if (length(strExp) <= intFrom)
			return "";

		int intCLoop = 0;
		int intBLoop = 0;
		byte abytTemp[] = strExp.getBytes();

		while (true) {
			if (abytTemp[intBLoop] > 127 || abytTemp[intBLoop] < 0) {
				if (intBLoop + 2 > intFrom) {
					break;
				} else {
					intBLoop++;
					intBLoop++;
				}
			} else {
				if (intBLoop + 1 > intFrom) {
					break;
				} else {
					intBLoop++;
				}
			}
			intCLoop++;
		}

		return strExp.substring(intCLoop);
	}

	/**
	 * 从某一位置后进行串截取
	 * 
	 * @param strExp
	 * @param intFrom
	 * @param intLen
	 * @return
	 */
	public static String substring(String strExp, int intFrom, int intLen) {
		if (intLen <= 0)
			return "";

		strExp = substring(strExp, intFrom);
		if (strExp.length() > 0) {
			strExp = left(strExp, intLen);
		}
		return strExp;
	}

	/**
	 * 替换一个串中的指定的第一个子串
	 * 
	 * @param strExp
	 * @param strFind
	 * @param strRep
	 * @return
	 */
	public static String replaceFirst(String strExp, String strFind,
			String strRep) {
		if (strFind == null || strFind.length() == 0)
			return strExp;
		int intFrom = strExp.indexOf(strFind, 0);
		if (intFrom >= 0) {
			int intTo = intFrom + strFind.length();
			strExp = strExp.substring(0, intFrom) + strRep
					+ strExp.substring(intTo);
		}
		return strExp;
	}

	/**
	 * 替换一个串中的指定的子串
	 * 
	 * @param strExp
	 * @param strFind
	 * @param strRep
	 * @return
	 */
	public static String replace(String strExp, String strFind, String strRep) {
		int intFrom, intTo;
		String strHead, strTail;

		if (strFind == null || strFind.length() == 0)
			return strExp;

		intFrom = strExp.indexOf(strFind, 0);
		while (intFrom >= 0) {
			intTo = intFrom + strFind.length();
			strHead = strExp.substring(0, intFrom);
			strTail = strExp.substring(intTo);
			strExp = strHead + strRep + strTail;
			intTo = intFrom + strRep.length();
			intFrom = strExp.indexOf(strFind, intTo);
		}

		return strExp;
	}

	/**
	 * 左裁减字符（串）
	 * 
	 * @param strExp
	 * @param strTrim
	 *            裁减的字符串
	 * @return
	 */
	public static String ltrim(String strExp, String strTrim) {
		int i;

		if (strExp == null || strExp.length() == 0)
			strExp = "";

		if (strTrim == null || strTrim.length() == 0)
			return strExp;

		char chrTrim = strTrim.charAt(0);

		for (i = 0; i < strExp.length(); i++) {
			if (strExp.charAt(i) != chrTrim)
				break;
		}

		return strExp.substring(i);
	}

	/**
	 * 右裁减
	 * 
	 * @param strExp
	 * @param strTrim
	 *            裁减的字符串
	 * @return
	 */
	public static String rtrim(String strExp, String strTrim) {
		int i;

		if (strExp == null || strExp.length() == 0)
			strExp = "";

		int intLen = strExp.length();

		if (strTrim == null || strTrim.length() == 0)
			return strExp;

		char chrTrim = strTrim.charAt(0);

		for (i = 0; i < strExp.length(); i++) {
			if (strExp.charAt(intLen - i - 1) != chrTrim)
				break;
		}

		return strExp.substring(0, intLen - i);
	}

	/**
	 * 左右裁减指定字符(串)
	 * 
	 * @param strExp
	 * @param strTrim
	 *            裁减的字符串
	 * @return
	 */
	public static String trim(String strExp, String strTrim) {
		return ltrim(rtrim(strExp, strTrim), strTrim);
	}

	/**
	 * 左补齐
	 * 
	 * @param strExp
	 * @param intLen
	 *            补齐后的长度
	 * @param strPad
	 *            要补齐的字符串
	 * @return
	 */
	public static String lpad(String strExp, int intLen, String strPad) {
		if (strExp == null)
			strExp = "";

		while (strExp.length() < intLen) {
			strExp = strPad + strExp;
		}
		return right(strExp, intLen);
	}

	/**
	 * 右补齐
	 * 
	 * @param strExp
	 * @param intLen
	 *            补齐后的长度
	 * @param strPad
	 *            要补齐的字符串
	 * @return
	 */
	public static String rpad(String strExp, int intLen, String strPad) {
		if (strExp == null)
			strExp = "";

		while (strExp.length() < intLen) {
			strExp = strExp + strPad;
		}
		return left(strExp, intLen);
	}

	/**
	 * 将字符串String转换为BigDecimal,如果为空则使用defaultNum值
	 * 
	 * @param value
	 * @param defaultNum
	 * @return
	 */
	public static BigDecimal stringToBigDecimal(String value, int defaultNum) {

		if (value != null && !value.equals("")) {
			BigDecimal num = new BigDecimal(defaultNum);
			try {
				double numDouble = Double.parseDouble(value);
				num = new BigDecimal(numDouble);
			} catch (Exception ignored) {
			}
			return num;
		} else {
			return new BigDecimal(defaultNum);
		}
	}

	/**
	 * 将一个串转换为BigDecimal类型
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal stringToBigDecimal(String value) {
		return stringToBigDecimal(value, 0);
	}

	/**
	 * 此函数判断字符串是否为NULL，如果为空返回"",否则返回原值
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String NulltoBlank(String sourceStr) {
		if (sourceStr == null) {
			return "";
		} else {
			return sourceStr.trim();
		}
	}

	public static String NulltoHtml(String sourceStr) {
		if (sourceStr == null || "".equals(sourceStr)) {
			return "&nbsp;";
		} else {
			return sourceStr;
		}
	}

	/**
	 * strChk字符串为空时的赋值""
	 * 
	 * @param strChk
	 * @return
	 */
	public static String nvl(String strChk) {
		return nvl(strChk, "");
	}

	/**
	 * strChk字符串为空时的赋值为strExp
	 * 
	 * @param strChk
	 * @param strExp
	 * @return
	 */
	public static String nvl(String strChk, String strExp) {
		if (isNull(strChk))
			return strExp;
		return strChk;
	}

	/**
	 * 返回变换后的整数，如果失败则返回默认值
	 * 
	 * @param strIn
	 * @param iV
	 * @return
	 */
	public static int toInt(String strIn, int iV) {
		if (strIn == null)
			return iV;
		try {
			return Integer.parseInt(strIn);
		} catch (Exception ex) {
			return iV;
		}
	}

	/**
	 * 将16进制的字符串转换成整数
	 * 
	 * @param strIn
	 *            如果为16进制，则必须为0X或者0x开头，否则认为是10进制数字
	 * @param iV
	 *            如果转换失败的返回值
	 * @return
	 */
	public static int hex2Int(String strIn, int iV) {
		int radix = 10; // 默认10进制

		if (strIn != null && (strIn.startsWith("0X") || strIn.startsWith("0x"))) {
			strIn = strIn.substring(2);
			radix = 16;
		}

		// 考虑支持16进制
		try {
			return Integer.parseInt(strIn, radix);
		} catch (Exception ex) {
			return iV;
		}

	}

	/**
	 * 返回变换后的双浮点数，如果失败则返回默认值
	 * 
	 * @param strIn
	 *            要转换的串
	 * @param iV
	 *            默认值
	 * @return
	 */
	public static float toFloat(String strIn, float iV) {
		if (strIn == null)
			return iV;
		try {
			return Double.valueOf(strIn).floatValue();
		} catch (Exception ex) {
			return iV;
		}
	}

	/**
	 * 返回变换后的双浮点数，如果失败则返回默认值
	 * 
	 * @param strIn
	 *            要转换的串
	 * @param iV
	 *            默认值
	 * @return
	 */
	public static double toDouble(String strIn, double iV) {
		if (strIn == null)
			return iV;
		try {
			return Double.valueOf(strIn).doubleValue();
		} catch (Exception ex) {
			return iV;
		}
	}

	/**
	 * 返回变换后的long，如果失败则返回默认值
	 * 
	 * @param strIn
	 *            要转换的串
	 * @param iV
	 *            默认值
	 * @return
	 */
	public static double toLong(String strIn, long iV) {
		if (strIn == null)
			return iV;
		try {
			return Long.parseLong(strIn);
		} catch (Exception ex) {
			return iV;
		}
	}

	/** 判断Vector是否为空或者size<1* */
	public static boolean isNull(Vector vExp) {
		if (vExp == null) {
			vExp = new Vector();
		}
		if (vExp.size() < 1) {
			return true;
		} else {
			return false;
		}
	}

	/** 判断字符串是否为空或长度是否为0 */
	public static boolean isNull(String strExp) {
		if (strExp == null || strExp.length() == 0)
			return true;
		return false;
	}

	/**
	 * 判断是否是年合法月日
	 * 
	 * @param intYear
	 * @param intMonth
	 * @param intDay
	 * @return
	 */
	public static boolean isDate(int intYear, int intMonth, int intDay) {
		try {
			Calendar cal = new GregorianCalendar();
			cal.setLenient(false);
			cal.set(intYear, intMonth, intDay);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是合法日期串（YYYY/MM/DD）
	 * 
	 * @param strExp
	 * @return
	 */
	public static boolean isDate(String strExp) {
		if (strExp.length() != 10)
			return false;

		try {
			Calendar cal = new GregorianCalendar();
			cal.setLenient(false);
			cal.set(Integer.parseInt(strExp.substring(0, 4)),
					Integer.parseInt(strExp.substring(5, 7)) - 1,
					Integer.parseInt(strExp.substring(8, 10)));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断某一指定格式的日期串是否合法
	 * 
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static boolean isDate(String dateStr, String formatStr) {
		String strDate = transDateString(dateStr, formatStr, "YYYY/MM/DD");
		if (strDate == null)
			return false;
		return isDate(strDate);
	}

	/**
	 * 判断字符串是否是一个在规定长度内的float
	 * 
	 * @param intWidth
	 *            整数部分的宽度
	 * @param intZero
	 *            小数部分的宽度
	 */
	public static boolean isFloat(String strExp, int intWidth, int intZero) {
		if (isFloat(strExp) == true) {
			int i = strExp.indexOf(DOT_CONNECT);
			if (i < 0) {
				if (strExp.length() > intWidth)
					return false;
				else
					return true;
			}

			if (i > intWidth)
				return false;

			if (strExp.length() - i - 1 > intZero)
				return false;
			return true;
		}
		return false;
	}

	/** 判断字符串是否是一个float */
	public static boolean isFloat(String strExp) {
		if (isNull(strExp))
			return false;

		try {
			Float.valueOf(strExp);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/** 判断字符串是否是一个在规定长度内的integer */
	public static boolean isInt(String strExp, int intWidth) {
		if (isNull(strExp) || intWidth <= 0 || strExp.length() > intWidth)
			return false;
		return isInt(strExp);
	}

	/** 判断字符串是否是一个integer */
	public static boolean isInt(String strExp) {
		if (isNull(strExp))
			return false;
		try {
			Integer.valueOf(strExp);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/** 判断字符串是否是一个数值 */
	public static boolean isNumeric(String strExp) {
		if (isFloat(strExp) || isInt(strExp))
			return true;
		return false;
	}

	/**
	 * 判断一个串是否在一个串数组中，如果在则返回在串数组中的index，否则返回-1
	 * 
	 * @param arr
	 *            串数组
	 * @param inStr
	 *            要查找的串
	 * @return
	 */
	public static int inStringArray(String[] arr, String inStr) {
		int iRet = -1;
		if (arr == null || inStr == null)
			return iRet;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(inStr))
				return i;
		}
		return iRet;
	}

	/** 判断字符串是否在规定长度内 */
	public static boolean isLegalLen(String strExp, int intWidth) {
		if (isNull(strExp))
			return true;

		if (strExp.length() > intWidth)
			return false;
		return true;
	}

	/** 判断字符串是否是一个电子信箱 */
	public static boolean isEmail(String strExp) {
		int intFind = strExp.indexOf("@");
		if (intFind <= 0 || intFind >= strExp.length() - 1)
			return false;
		return true;
	}

	/**
	 * 将字符串从ISO8859-1型转换成GB2312型 当strExp为空时取值strRep
	 * 
	 * @param strExp
	 *            要转化的串
	 * @param strRep
	 *            如果转换的串为空，则代替的串
	 * @return
	 */
	public static String toGB(String strExp, String strRep) {
		return toGB(nvl(strExp, strRep));
	}

	/**
	 * 将字符串从ISO8859-1型转换成GB2312型 当strExp为空时取值""
	 * 
	 * @param strExp
	 *            要转化的串
	 * @return
	 */
	public static String toGB(String strExp) {
		return strExp;

		// strExp = (strExp == null) ? "" : strExp;
		// if (isGb_trans) {
		// return strExp;
		// }
		//
		// try {
		// return new String(strExp.getBytes(ISOENCODING), GBKENCODING);
		// } catch (Exception e) {
		// return "";
		// }
	}

	/** SJIS UDC (NEC selection IBM kanji) support contributed */
	public static String toShiftjis(String strExp) {
		strExp = (strExp == null) ? "" : strExp;

		try {
			return new String(strExp.getBytes(ISOENCODING), "SJIS");
		} catch (Exception e) {
			return "";
		}
	}

	/** 将字符串从GB2312型转换成ISO8859-1型,如果为空则返回strRep */
	public static String toISO8859(String strExp, String strRep) {
		return toISO8859(nvl(strExp, strRep));
	}

	/** 将字符串从GB2312型转换成ISO8859-1型,如果为空则返回"" */
	public static String toISO8859(String strExp) {
		strExp = (strExp == null) ? "" : strExp;

		try {
			return new String(strExp.getBytes(GBKENCODING), ISOENCODING);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 解析固定分割的串的各个单元，并将其存放在Vector中返回
	 * 
	 * @param s
	 * @param delim
	 * @return
	 */
	public static Vector parseStringTokenizer(String s, String delim) {
		Vector vRet = new Vector();
		for (;;) {
			int iPos = s.indexOf(delim);
			if (iPos < 0) {
				if (s != null && !"".equals(s))
					vRet.addElement(s);
				break;
			}
			vRet.addElement(s.substring(0, iPos));
			s = s.substring(iPos + delim.length());
			if (s == null)
				break;
		}
		return vRet;
	}

	public static String getFieldValueByStr(java.lang.reflect.Field field,
			Object obj) throws IllegalAccessException, IllegalArgumentException {
		String v = "";
		if (field.getType().equals("double")) {
			v = field.getDouble(obj) + "";
		} else if (field.getType().equals("int")) {
			v = field.getInt(obj) + "";
		} else if (field.getType().equals("float")) {
			v = field.getFloat(obj) + "";
		} else if (field.getType().equals("boolean")) {
			v = field.getBoolean(obj) + "";
		} else if (field.getType().equals("byte")) {
			v = field.getByte(obj) + "";
		} else if (field.getType().equals("char")) {
			v = field.getChar(obj) + "";
		} else if (field.getType().equals("long")) {
			v = field.getLong(obj) + "";
		} else if (field.getType().equals("short")) {
			v = field.getShort(obj) + "";
		} else
			v = field.get(obj) + "";
		return v;
	}

	/**
	 * 将指定格式的日期串转换为另外一种格式
	 * 
	 * @param sDate
	 *            源日期串
	 * @param sFormat
	 *            ,源串的格式，如:"YYYY/MM/DD:HH24MISS"
	 * @param dFormat
	 *            ,目的格式，如:"HH24MISS:YYYY年MM月DD日"
	 * @return 转换后的串串，如果有误则返回null
	 */
	public static String transDateString(String sDate, String sFormat,
			String dFormat) {
		String retStr = "";
		try {
			if (sDate == null || "".equals(sDate))
				return sDate;
			if (sFormat == null || "".equals(sFormat))
				return sDate;
			if (dFormat == null || "".equals(dFormat))
				return sDate;
			sFormat = sFormat.toUpperCase();
			dFormat = dFormat.toUpperCase();
			sFormat = replace(sFormat, "HH24", "HH");
			dFormat = replace(dFormat, "HH24", "HH");
			retStr = dFormat;
			int iPos = sFormat.indexOf("YYYY");
			String year = "";
			if (iPos > -1)
				year = sDate.substring(iPos, iPos + 4);
			iPos = sFormat.indexOf("MM");
			String month = "";
			if (iPos > -1)
				month = sDate.substring(iPos, iPos + 2);
			iPos = sFormat.indexOf("DD");
			String day = "";
			if (iPos > -1)
				day = sDate.substring(iPos, iPos + 2);
			String hour = "";
			iPos = sFormat.indexOf("HH");
			if (iPos > -1)
				hour = sDate.substring(iPos, iPos + 2);
			String minute = "";
			iPos = sFormat.indexOf("MI");
			if (iPos > -1)
				minute = sDate.substring(iPos, iPos + 2);
			String second = "";
			iPos = sFormat.indexOf("SS");
			if (iPos > -1)
				second = sDate.substring(iPos, iPos + 2);
			year = rpad(year, 4, "0");
			month = rpad(month, 2, "0");
			day = rpad(day, 2, "0");
			hour = rpad(hour, 2, "0");
			minute = rpad(minute, 2, "0");
			second = rpad(second, 2, "0");
			retStr = replace(retStr, "YYYY", year);
			retStr = replace(retStr, "MM", month);
			retStr = replace(retStr, "DD", day);
			retStr = replace(retStr, "HH", hour);
			retStr = replace(retStr, "MI", minute);
			retStr = replace(retStr, "SS", second);
		} catch (Exception e) {
			System.err.println("StringB.transDateString() 方法错误，相关信息如下：");
			e.printStackTrace();
			return null;
		}
		return retStr;
	}

	public static void main(String[] args) {
		// System.out.println("date=" + replaceFirst("2002/12/12", "/", "w"));

	}
}