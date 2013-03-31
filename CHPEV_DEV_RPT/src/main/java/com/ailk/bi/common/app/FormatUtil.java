package com.ailk.bi.common.app;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class FormatUtil {
	/**
	 * 将日期格式的字符串格式化为中文：如2005年06月31日
	 * 
	 * @param dateStr
	 * @return 返回日期的中文名称
	 */
	public static String formatdateCn(String dateStr) {
		if (dateStr == null || "".equals(dateStr))
			return "";

		switch (dateStr.length()) {
		case 4:
			return dateStr.substring(0, 4) + "年";
		case 6:
			return dateStr.substring(0, 4) + "年" + dateStr.substring(4, 6) + "月";
		default:
			return dateStr.substring(0, 4) + "年" + dateStr.substring(4, 6) + "月" + dateStr.substring(6, 8) + "日";
		}
	}

	/**
	 * 对于给定的数组，进行截取，截取分为左截或右截
	 * 
	 * @param inArray
	 *            输入的数组，如果为NULL,返回NULL
	 * @param charCount
	 *            指定的列，从0开始
	 * @param isLeftTrim
	 *            是否左截取
	 * @return String[]数组
	 */
	public static String[] trimStringArray(String[] inArray, int charCount, boolean isLeftTrim) {
		String[] value = null;
		if (inArray != null && inArray.length > 0)
			value = new String[inArray.length];
		for (int i = 0; inArray != null && i < inArray.length; i++) {
			//
			if (inArray[i] != null && charCount > 0 && charCount < inArray[i].length()) {
				//
				if (isLeftTrim) {
					//
					value[i] = inArray[i].substring(0, charCount);
				} else {
					value[i] = inArray[i].substring(charCount, inArray[i].length());
					//
				}
			} else {
				//
				value[i] = inArray[i];
			}
		}
		return value;
	}

	/**
	 * 对于给定的多维数组，对指定的列，按指定的字符数截取该列， 然后返回截取后的该列，截取分为左截或右截
	 * 
	 * @param inArray
	 *            输入的多维数组，如果为NULL,返回NULL
	 * @param colIndex
	 *            指定的列，从0开始
	 * @param charCount
	 *            截取的指定的字符数
	 * @param isLeftTrim
	 *            是否左截取
	 * @return 返回截取后的该列
	 */
	public static String[] trimStringArray(String[][] inArray, int colIndex, int charCount, boolean isLeftTrim) {
		String[] ret = null;
		if (null != inArray && colIndex >= 0 && charCount >= 0) {
			int minCols = inArray[0].length;
			for (int i = 0; i < inArray.length; i++) {
				if (inArray[i].length < minCols)
					minCols = inArray[i].length;
			}
			if (colIndex <= minCols) {
				ret = new String[inArray.length];
				for (int i = 0; i < ret.length; i++) {
					String tmpStr = inArray[i][colIndex];
					// 截取
					if (isLeftTrim) {
						if (tmpStr.length() >= charCount)
							tmpStr = tmpStr.substring(0, charCount);
					} else {
						if (tmpStr.length() >= charCount)
							tmpStr = tmpStr.substring(tmpStr.length() - charCount);
					}
					ret[i] = tmpStr;
				}
			}
		}
		return ret;
	}

	/**
	 * 对于给定的多维数组，对指定的列，按指定的字符数截取该列， 然后返回截取后的多维数组，截取分为左截或右截
	 * 
	 * @param inArray
	 *            输入的多维数组，如果为NULL,返回NULL
	 * @param colIndex
	 *            指定的列，从0开始
	 * @param charCount
	 *            截取的指定的字符数
	 * @param isLeftTrim
	 *            是否左截取
	 * @return 返回截取后的多维数组
	 */
	public static String[][] trimStringArrays(String[][] inArray, int colIndex, int charCount, boolean isLeftTrim) {
		String[][] ret = null;
		if (null != inArray && colIndex >= 0 && charCount >= 0) {
			ret = new String[inArray.length][];
			int minCols = inArray[0].length;
			for (int i = 0; i < inArray.length; i++) {
				ret[i] = new String[inArray[i].length];
				System.arraycopy(inArray[i], 0, ret[i], 0, inArray[i].length);
				if (inArray[i].length < minCols)
					minCols = inArray[i].length;
			}
			if (colIndex <= minCols) {

				for (int i = 0; i < ret.length; i++) {
					String tmpStr = inArray[i][colIndex];
					// 截取
					if (isLeftTrim) {
						if (tmpStr.length() >= charCount)
							tmpStr = tmpStr.substring(0, charCount);
					} else {
						if (tmpStr.length() >= charCount)
							tmpStr = tmpStr.substring(tmpStr.length() - charCount);
					}
					ret[i][colIndex] = tmpStr;
				}
			}
		}
		return ret;
	}

	/**
	 * 格式化输出百分比
	 * 
	 * @param value
	 *            输入的百分比小数
	 * @param fractionNum
	 *            保留小数位数,如果为为负数，则按默认位数进行格式化。 如果为0，对于98.0的形式输出98，默认为保留两位小数
	 * @param withSyb
	 *            是否带有％
	 * @return 返回格式化后的百分比
	 */
	public static String formatPercent(double value, int fractionNum, boolean withSyb) {
		double tmpValue = value;
		tmpValue = tmpValue * 100;
		if (null != Arith.round(tmpValue, fractionNum + 2))
			tmpValue = Arith.round(tmpValue, fractionNum + 2).doubleValue();
		if (withSyb) {
			tmpValue = tmpValue * 100;
			return FormatUtil.formatDouble(tmpValue, fractionNum, false, "%");
		} else {
			return FormatUtil.formatDouble(tmpValue, fractionNum, false);
		}
	}

	/**
	 * 格式化输出百分比
	 * 
	 * @param str
	 *            输入的百分比小数
	 * @param fractionNum
	 *            保留小数位数,如果为为负数，则按默认位数进行格式化。 如果为0，对于98.0的形式输出98，默认为保留两位小数
	 * @param withSyb
	 *            是否带有％
	 * @return 返回格式化后的百分比
	 */
	public static String formatPercent(String str, int fractionNum, boolean withSyb) {
		if (null == str || "".equalsIgnoreCase(str))
			return null;
		double tmpValue = Double.parseDouble(str);
		tmpValue = tmpValue * 100;
		if (null != Arith.round(tmpValue, fractionNum + 2))
			tmpValue = Arith.round(tmpValue, fractionNum + 2).doubleValue();
		if (withSyb) {
			return FormatUtil.formatDouble(tmpValue, fractionNum, false, "%");
		} else {
			return FormatUtil.formatDouble(tmpValue, fractionNum, false);
		}
	}

	/**
	 * 对于浮点型数据进行格式化数据输出
	 * 
	 * @param num
	 *            要被格式化的浮点型数据
	 * @param fractionNum
	 *            如果为为负数，则按默认位数进行格式化。 如果为0，对于98.0的形式输出98，默认为保留两位小数
	 * @param isSplit
	 *            是否用分隔符号分隔，默认为逗号
	 * @param unit
	 *            字符串单位
	 * @return 格式化后的字符串
	 */

	public static String formatDouble(double num, int fractionNum, boolean isSplit, String unit) {
		return formatDouble(num, fractionNum, isSplit) + unit;
	}

	/**
	 * 对输入的字符串进行格式化，然后输出格式化的带单位字符串
	 * 
	 * @param str
	 *            要被格式化的字符串
	 * @param fractionNum
	 *            如果为为负数，则按默认位数进行格式化。 如果为0，对于98.0的形式输出98，默认为保留两位小数
	 * @param isSplit
	 *            是否用分隔符号分隔，默认为逗号
	 * @param unit
	 *            字符串单位
	 * @return 格式化后的字符串
	 */
	public static String formatStr(String str, int fractionNum, boolean isSplit, String unit) {
		return formatStr(str, fractionNum, isSplit) + unit;
	}

	/**
	 * 对于浮点型数据进行格式化数据输出
	 * 
	 * @param num
	 *            要被格式化的浮点型数据
	 * @param fractionNum
	 *            要保留的小数位数，如果为为负数，则按默认位数进行格式化。 如果为0，对于98.0的形式输出98，默认为保留两位小数
	 * @param isSplit
	 *            是否用分隔符号分隔，默认为逗号
	 * @return 格式化后的字符串
	 */

	public static String formatDouble(double num, int fractionNum, boolean isSplit) {
		String retStr = "" + num;
		String fmt = null;
		double tmpValue = num;
		int tmpNum = fractionNum;
		if (fractionNum == 0)
			tmpNum = AppConst.FRACTIONNUM;
		if (null != Arith.round(tmpValue, tmpNum))
			tmpValue = Arith.round(tmpValue, tmpNum).doubleValue();

		if (isSplit)
			fmt = "###" + AppConst.FMTSPLITCHAR + "##0";
		else
			fmt = "#####0";
		fmt = fmt + ".";
		if (fractionNum > 0) {

			for (int i = 0; i < fractionNum; i++) {
				fmt = fmt + "0";
			}
		} else {
			for (int i = 0; i < AppConst.FRACTIONNUM; i++) {
				fmt = fmt + "#";
			}
		}
		DecimalFormat df = new DecimalFormat(fmt);

		retStr = df.format(tmpValue);
		return retStr;
	}

	public static String formatValueWithoutRound(String value, int fractionNum) {
		String ret = value;
		if (!StringUtils.isBlank(value)) {
			int pos = ret.indexOf(".");
			if (pos >= 0) {
				if (ret.length() >= (pos + fractionNum + 1))
					ret = ret.substring(0, pos + fractionNum + 1);
				else {
					int length = ret.length();
					for (int i = 0; i < (pos + fractionNum + 1 - length); i++) {
						ret = ret + "0";
					}
				}
			} else {
				ret = ret + ".";
				for (int i = 0; i < fractionNum; i++) {
					ret = ret + "0";
				}
			}
			if (0 == fractionNum) {
				pos = ret.indexOf(".");
				if (pos >= 0) {
					ret = ret.substring(0, pos);
				}
			}
		}
		return ret;
	}

	/**
	 * 对输入的字符串进行格式化，然后输出格式化的字符串
	 * 
	 * @param str
	 *            要被格式化的字符串
	 * @param fractionNum
	 *            要保留的小数位数，如果为为负数，则按默认位数进行格式化 如果为0，对于98.0的形式输出98，默认为保留两位小数
	 * @param isSplit
	 *            是否用分隔符号分隔，默认为逗号
	 * @return 格式化后的字符串
	 */
	public static String formatStr(String str, int fractionNum, boolean isSplit) {
		String retStr = null;
		if (null != str) {
			try {
				double tmpValue = Double.parseDouble(str);
				int tmpNum = Math.abs(fractionNum);
				if (fractionNum == 0)
					tmpNum = AppConst.FRACTIONZERONUM;
				if (null != Arith.round(tmpValue, tmpNum))
					tmpValue = Arith.round(tmpValue, tmpNum).doubleValue();
				String fmt = null;
				if (isSplit)
					fmt = "###" + AppConst.FMTSPLITCHAR + "##0";
				else
					fmt = "#####0";

				fmt = fmt + ".";
				if (fractionNum > 0) {
					for (int i = 0; i < fractionNum; i++) {
						fmt = fmt + "0";
					}
				} else if (fractionNum < 0) {
					for (int i = 0; i < tmpNum; i++) {
						fmt = fmt + "#";
					}
				} else {
					for (int i = 0; i < AppConst.FRACTIONNUM; i++) {
						fmt = fmt + "#";
					}
				}

				DecimalFormat df = new DecimalFormat(fmt);

				retStr = df.format(tmpValue);
			} catch (NumberFormatException nfe) {
				retStr = null;
			}
		}
		return retStr;
	}

	/**
	 * 将字符串一维数组转化为DOUBLE类型的一维数组
	 * 
	 * @param str
	 *            需要进行转换的字符串一维数组
	 * @return 返回转换后的DOUBLE类型一维数组
	 */
	public static double[] fmtStrToDouble(String[] str) {
		double[] dblList = null;
		if (str == null)
			return null;
		int row = str.length;
		dblList = new double[row];
		for (int i = 0; i < row; i++) {
			String tmp = str[i];
			if (tmp.equals(""))
				tmp = "0.0";
			double db_tmp = new Double(tmp).doubleValue();
			dblList[i] = db_tmp;
		}
		return dblList;
	}

	/**
	 * 定义将字符串二维数组转化为DOUBLE数组的形式输出
	 * 
	 * @param str
	 *            需要进行转换的字符串二维数组
	 * @return 返回转换后的DOUBLE类型的二维数组
	 */
	public static double[][] fmtStrToDouble(String[][] str) {
		double[][] doubleArray = null;
		if (str == null)
			return null;
		int row = str.length;
		int col = str[0].length;
		doubleArray = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				String temp = str[i][j];
				if (temp.equals(""))
					temp = "0.0";
				double dbl_temp = new Double(temp).doubleValue();
				doubleArray[i][j] = dbl_temp;
			}
		}
		return doubleArray;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// System.out.println("formatDouble==="
		// + FormatUtil.formatDouble(1232131123.5645, 1, false, "万元"));
		// System.out.println(FormatUtil.formatStr("1232131123.2345", 2, true,
		// "万元"));
		// System.out.println(FormatUtil.formatStr("321312123.645", 0, false));
		// System.out.println(FormatUtil.formatStr("1321323.2345", 0, true));
		// System.out.println(FormatUtil.formatStr("3213123.7345", -1, false));
		// System.out.println(FormatUtil.formatStr("3213123.00", 2, true));
		// System.out.println(FormatUtil.formatStr("23131123.00", -1, true));
		// System.out.println(FormatUtil.formatPercent("0.127234324", -5,
		// true));
		// System.out.println(FormatUtil.formatPercent(0.123234324, 0, false));
		// System.out.println(FormatUtil.formatPercent(0.123234324, 0, true));
		System.out.println(FormatUtil.formatPercent("0.1234566", 2, true));
		// String[] tmpStr = {"123456", "123456", "123456", "123456" };
		// { "123456", "123456", "123456", "123456" },
		// { "123456", "123456", "123456", "123456" },
		// { "123456", "123456", "123456", "123456" } };
		// String[] ret = FormatUtil.trimStringArray(tmpStr,2,false);
		// for (int i = 0; i < ret.length; i++)
		// System.out.print(ret[i] + " ");

		String txt = "亲爱的[UserName]用户你好: \n" + "这是来自[SiteName]的信息,请您注意查收!谢谢. \n" + "[SiteDate] ";
		Matcher m = Pattern.compile("\\[(\\w*)\\]").matcher(txt);
		while (m.find()) {
			System.out.print(m.group(1) + " ");
		}

		String str = "{loginname=wubaoquan,loginpassword=1987521,usersex=man,userage=23}";
		String regex = "=([a-z0-9]+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher m1 = pattern.matcher(str);
		while (m1.find()) {
			System.out.println(m1.group(1));
		}
		System.out.println("formatWithOutRound===" + FormatUtil.formatValueWithoutRound("1232131123.5645", 1));
		System.out.println("formatWithOutRound===" + FormatUtil.formatValueWithoutRound("1232131123.5645", 2));
		System.out.println("formatWithOutRound===" + FormatUtil.formatValueWithoutRound("1232131123.5645", 5));
		System.out.println("formatWithOutRound===" + FormatUtil.formatValueWithoutRound("1232131123.5645", 4));
		System.out.println("formatWithOutRound===" + FormatUtil.formatValueWithoutRound("1232131123", 4));
	}

}
