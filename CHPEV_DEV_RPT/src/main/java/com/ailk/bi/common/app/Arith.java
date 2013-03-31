/**
 *
 */
package com.ailk.bi.common.app;

import com.ailk.bi.common.app.AppConst;

import java.math.*;

/**
 * <p>
 * Title: asiabi BI System
 * </p>
 * <p>
 * Description:由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @jcm008
 * @version 1.0
 */
public class Arith {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 提供精确的加法运算。
	 *
	 * 参数为NULL 或是 空时,返回为null
	 *
	 * 参数含有不可运算元素时,返回为null
	 *
	 * 同时后台输出 "字符串参数内容抛出异常!"
	 *
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和String类型数据
	 *
	 *
	 */
	public static String add(String v1, String v2) {
		if ((v1 == null) || ("".equals(v1)))
			return null;
		if ((v2 == null) || ("".equals(v2)))
			return null;
		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.add(b2).toString();
		} catch (NumberFormatException e) {
			System.out.println("add(String " + v1 + ", String " + v2
					+ ")字符串参数内容抛出异常!");
			return null;
		}

	}

	/**
	 * 提供精确的加法运算。
	 *
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和Double类型数据 add by jcm008
	 */
	public static Double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return new Double(b1.add(b2).doubleValue());
	}

	/**
	 * 提供精确的减法运算。
	 *
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差Double类型
	 */
	public static Double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return new Double(b1.subtract(b2).doubleValue());
	}

	/**
	 * 提供精确的减法运算。 参数为NULL 或是 空时,返回为null
	 *
	 * 参数含有不可运算元素时,返回为null
	 *
	 * 同时后台输出 "字符串参数内容抛出异常!"
	 *
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static String sub(String v1, String v2) {
		if ((v1 == null) || ("".equals(v1)))
			return null;
		if ((v2 == null) || ("".equals(v2)))
			return null;
		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return String.valueOf(b1.subtract(b2));
		} catch (NumberFormatException e) {
			System.out.println("sub(String " + v1 + ", String " + v2
					+ ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积Double类型
	 */
	public static Double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return new Double(b1.multiply(b2).doubleValue());
	}

	/**
	 * 提供精确的乘法运算。
	 *
	 * 参数为NULL 或是 空时,返回为null
	 *
	 * 参数含有不可运算元素时,返回为null
	 *
	 * 同时后台输出 "字符串参数内容抛出异常!"
	 *
	 * 当保留小数位数小于零时会直接值为2 并输出提示信息保留位数必须是一个大于等于零的数字!
	 *
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static String mul(String v1, String v2, int scale) {
		if (("".equals(v1)) || (v1 == null))
			return null;
		if (("".equals(v2)) || (v2 == null))
			return null;
		if (scale < 0) {
			System.out.println("保留位数必须是一个大于等于零的数字!");
			scale = 2;
		}

		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal b3 = new BigDecimal("1");
			return b1.multiply(b2).divide(b3, scale, BigDecimal.ROUND_HALF_UP)
					.toString();
		} catch (NumberFormatException e) {
			System.out.println("mul(String " + v1 + ", String " + v2 + ", int "
					+ scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static Double div(double v1, double v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if (v2 == 0)
			return null;

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return new Double(b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue());
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * if (scale <0) scale =2
	 *
	 * 只要任意一个参数中存在null 或为空串时 返回null
	 *
	 * 传入的参数中含有非法字符时,返回null
	 *
	 * 分母为0则返回null
	 *
	 * 分子为零则返回 0
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static Double div(String v1, String v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if (v1 == null || "".equals(v1))
			return null;
		if (v2 == null || "".equals(v2))
			return null;

		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal zero = new BigDecimal("0");
			if (b1.compareTo(zero) == 0) {
				return new Double("0");
			}
			if (b2.compareTo(zero) == 0) {
				return null;
			}
			String temp = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
					.toString();
			return new Double(temp);
		} catch (NumberFormatException e) {
			System.out.println("div(String " + v1 + ", String " + v2 + ", int "
					+ scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static Double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * 分母为0 返回null
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商(字符串)
	 */
	public static String divs(double v1, double v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal zero = new BigDecimal("0");
		if (b1.compareTo(zero) == 0) {
			return "0";
		}
		if (b2.compareTo(zero) == 0) {
			return null;
		}
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 *
	 * 只要参数中有null或空串则返回值为null
	 *
	 * scale <0 则scale = 2
	 *
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商（字符串）
	 */
	public static String divs(String v1, String v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if ((v1 == null) || ("".equals(v1)))
			return null;
		if ((v2 == null) || ("".equals(v2)))
			return null;
		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal zero = new BigDecimal("0");
			if (b1.compareTo(zero) == 0) {
				return "0";
			}
			if (b2.compareTo(zero) == 0) {
				return null;
			}
			return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
		} catch (NumberFormatException e) {
			System.out.println("divs(String " + v1 + ", String " + v2
					+ ", int " + scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * scale <0 程序置为2
	 *
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果（double）
	 */
	public static Double round(double v, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return new Double(b.divide(one, scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue());
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 *
	 * 参数为空则返回null
	 *
	 * scale <0 程序设置为2
	 *
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果（字符串）
	 */
	public static String round(String v, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if (v == null || "".equals(v)) {
			System.out.println("处理数字为NULL!");
			return null;
		}
		try {
			BigDecimal b = new BigDecimal(v);
			BigDecimal one = new BigDecimal("1");
			return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
		} catch (NumberFormatException e) {
			System.out.println("round(String " + v + ", int " + scale
					+ ")字符串参数内容抛出异常!");
			return null;
		}

	}

	/**
	 * 求两个浮点数的百分比,分母为0返回null
	 *
	 * scale<0 则程序置为2
	 *
	 * 未必合理
	 *
	 * @param v1
	 * @param v2
	 * @return 百分比带百分号
	 */
	public static String divPer(double v1, double v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal zero = new BigDecimal("0");
		BigDecimal temp = new BigDecimal("100");
		if (b1.compareTo(zero) == 0) {
			return AppConst.RatePer;
		}
		if (b2.compareTo(zero) == 0) {
			return null;
		}
		BigDecimal c = b1.multiply(temp).divide(b2, scale,
				BigDecimal.ROUND_HALF_UP);
		return c.toString() + "%";
	}

	/**
	 * 求两个浮点数的百分比，分母为0返回null
	 *
	 * 参数中只要存在null或是空串时返回null
	 *
	 * scale<0 则后scale<0 则程序置为2
	 *
	 * 字符串中存在字符导致格式化错误会返回null值
	 *
	 * @param v1
	 * @param v2
	 * @return 百分比带百分号
	 */
	public static String divPer(String v1, String v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if ((v1 == null) || ("".equals(v1)))
			return null;
		if ((v2 == null) || ("".equals(v2)))
			return null;
		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal zero = new BigDecimal("0");
			BigDecimal temp = new BigDecimal("100");
			if (b1.compareTo(zero) == 0) {
				return AppConst.RatePer;
			}
			if (b2.compareTo(zero) == 0) {
				return null;
			}
			BigDecimal c = b1.multiply(temp).divide(b2, scale,
					BigDecimal.ROUND_HALF_UP);
			String strValue = c.toString();
			// 对百分比特殊的处理
			if (strValue.length() > 1 && ".".equals(strValue.substring(0, 1))){
				strValue = "0" + strValue;
			}
			if (strValue.length() > 2 && "-.".equals(strValue.substring(0, 2))){
				strValue = "-0" + strValue.substring(1, strValue.length());
			}
			return strValue + "%";
		} catch (NumberFormatException e) {
			System.out.println("divPer(String " + v1 + ", String " + v2
					+ ", int " + scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 求两个浮点数的百分比绝对值，不带百分号
	 *
	 * 分母为0返回null
	 *
	 * 参数中只要存在null或是空串时返回null
	 *
	 * scale<0 则程序置为2
	 *
	 * 字符串中存在字符导致格式化错误会返回null值
	 *
	 * @param v1
	 * @param v2
	 * @return 百分比绝对值不带百分号
	 *
	 */
	public static String divPerNot(String a1, String a2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if ((a1 == null) || ("".equals(a1)))
			return null;
		if ((a2 == null) || ("".equals(a2)))
			return null;
		try {
			BigDecimal b1 = new BigDecimal(a1);
			BigDecimal b2 = new BigDecimal(a2);
			b2 = b2.abs();
			BigDecimal zero = new BigDecimal("0");
			BigDecimal temp = new BigDecimal("100");
			if (b1.compareTo(zero) == 0) {
				return AppConst.RatePerNot;
			}
			if (b2.compareTo(zero) == 0) {
				return null;
			}
			BigDecimal c = b1.multiply(temp).divide(b2, scale,
					BigDecimal.ROUND_HALF_UP);
			return c.toString();
		} catch (NumberFormatException e) {
			System.out.println("divPerNot(String " + a1 + ", String " + a2
					+ ", int " + scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 求两个浮点数的百分比绝对值不需要百分号，分母为0返回NULL
	 *
	 * 参数中只要存在null或是空串时返回NULL
	 *
	 * scale<0 则后台置为2
	 *
	 * 字符串中存在字符导致格式化错误会返回null值
	 *
	 * @param v1
	 * @param v2
	 * @return 百分比绝对值不带百分号
	 *
	 */
	public static String divPerNotAbs(String v1, String v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if (v1 == null || "".equals(v1)) {
			return null;
		}
		if (v2 == null || "".equals(v2)) {
			return null;
		}
		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal zero = new BigDecimal("0");
			BigDecimal temp = new BigDecimal("100");
			if (b1.compareTo(zero) == 0) {
				return AppConst.RatePerNot;
			}
			if (b2.compareTo(zero) == 0) {
				return null;
			}
			BigDecimal c = b1.multiply(temp).divide(b2, scale,
					BigDecimal.ROUND_HALF_UP);
			return c.abs().toString();
		} catch (NumberFormatException e) {
			System.out.println("divPerNotAbs(String " + v1 + ", String " + v2
					+ ", int " + scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 求两个浮点数的百分比的绝对值输出，分母为0返回null
	 *
	 * 参数中只要存在null或是空串时返回null
	 *
	 * scale<0 则程序自动设置为2
	 *
	 * 字符串中存在字符导致格式化错误会返回null值
	 *
	 * @param v1
	 * @param v2
	 * @return 百分比绝对值带百分号输出
	 */
	public static String divPerAbs(String v1, String v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if (v1 == null || "".equals(v1)) {
			return null;
		}
		if (v2 == null || "".equals(v2)) {
			return null;
		}
		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			BigDecimal zero = new BigDecimal("0");
			BigDecimal temp = new BigDecimal("100");
			if (b1.compareTo(zero) == 0) {
				return AppConst.RatePer;
			}
			if (b2.compareTo(zero) == 0) {
				return null;
			}
			BigDecimal c = b1.multiply(temp).divide(b2, scale,
					BigDecimal.ROUND_HALF_UP);
			return c.abs().toString() + "%";
		} catch (NumberFormatException e) {
			System.out.println("divPerAbs(String " + v1 + ", String " + v2
					+ ", int " + scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 求两个浮点数的百分比，分母为0返回null
	 *
	 * 参数中只要存在null或是空串时返回null
	 *
	 * scale<0 则程序直接置为保留两位小数
	 *
	 * 字符串中存在字符导致格式化错误会返回null值
	 *
	 * @param v1
	 * @param v2
	 * @return 带百分号的百分比
	 */
	public static String divPerZero(String v1, String v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		if (("".equals(v1)) || (v1 == null))
			return null;
		if (("".equals(v2)) || (v2 == null))
			return null;
		try {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2).abs();
			BigDecimal zero = new BigDecimal("0");
			BigDecimal temp = new BigDecimal("100");
			if (b1.compareTo(zero) == 0) {
				return AppConst.RatePer;
			}
			if (b2.compareTo(zero) == 0) {
				return null;
			}
			BigDecimal c = b1.multiply(temp).divide(b2, scale,
					BigDecimal.ROUND_HALF_UP);
			return c.toString() + "%";
		} catch (NumberFormatException e) {
			System.out.println("divPerZero(String " + v1 + ", String " + v2
					+ ", int " + scale + ")字符串参数内容抛出异常!");
			return null;
		}
	}

	/**
	 * 求两个浮点数的百分比，分母为0返回null
	 *
	 * scale<0 则程序默认置scale = 2
	 *
	 * @param double v1
	 * @param double v2
	 * @return 两个浮点数百分比带百分号的String
	 */
	public static String divPerZero(double v1, double v2, int scale) {
		if (scale < 0) {
			scale = 2;
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal zero = new BigDecimal("0");
		BigDecimal temp = new BigDecimal("100");
		if (b1.compareTo(zero) == 0) {
			return AppConst.RatePer;
		}
		if (b2.compareTo(zero) == 0) {
			return null;
		}
		BigDecimal c = b1.multiply(temp).divide(b2, scale,
				BigDecimal.ROUND_HALF_UP);
		return c.toString() + "%";
	}

	public static void main(String[] args) {
		System.out.println(Arith.add("0", "0"));
	}

}
