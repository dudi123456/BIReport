package com.ailk.bi.report.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportHeadUtil {

	/**
	 * 对表格表头进行处理 注意：不管用户如何粘贴或自己绘制表头，表头只能有一个
	 * <table>
	 * 对
	 * 
	 * @param rptHead
	 *            报表表头
	 * @param trStyle
	 *            表头行样式
	 * @param tdStyle
	 *            表头单元格样式
	 * @return 返回处理后的表格头
	 */
	public static String processHead(String rptHead, String trStyle,
			String tdStyle) {
		String retHead = null;
		if (null != rptHead) {
			retHead = rptHead;
			// 清除不必要的代码
			retHead = clearRptHead(retHead);
			if (null != trStyle && !"".equals(trStyle)) {
				// 还有可能包含<tbody>,下面用模式找出<tr></tr>数组
				// sHTML = sHTML.replace( /<(\w[^>]*) width=([^>]*)>/gi, "<$1>"
				// );
				// 设置行的样式
				retHead = setTableTrStyle(retHead, trStyle);
			}
			if (null != tdStyle && !"".equals(tdStyle)) {
				// 设置单元格样式
				retHead = setTableTdStylr(retHead, tdStyle);
			}
		}
		return retHead;
	}

	/**
	 * 设置表头的行样式
	 * 
	 * @param input
	 *            表头字符串
	 * @param style
	 *            行的样式
	 * @return 返回增加了行样式的表头
	 */
	public static String setTableTrStyle(String input, String style) {
		String ret = null;
		if (null != input && !"".equals(input)) {
			ret = input;
			if (null != style && !"".equals(style)) {
				// 使用正则表达式来替换
				// \s表示空格，* 表示0或多次 匹配<tr ><tr >或<tr>
				// \w 表示非特殊字符[0-9][A-Z][a-z]的任意字符，* 表示0或多次
				// [^>] 不是>的任何字符 * 表示0或多次
				// 使用括号是将其分组
				String reg = "<tr(\\s*\\w*[^>]*)>";
				String replacement = "<tr class=\"" + style + "\"$1>";
				ret = regReplace(ret, reg, replacement, true);
			}
		}
		return ret;
	}

	/**
	 * 设置单元格的样式
	 * 
	 * @param input
	 *            表头字符串
	 * @param style
	 *            单元格样式
	 * @return 返回增加了单元格样式的表头
	 */
	public static String setTableTdStylr(String input, String style) {
		String ret = null;
		if (null != input && !"".equals(input)) {
			ret = input;
			if (null != style && !"".equals(style)) {
				// 类似于行样式的增加
				String reg = "<td(\\s*\\w*[^>]*)>";
				String replacement = "<td class=\"" + style + "\"$1>";
				ret = regReplace(ret, reg, replacement, true);

				// 如果有下钻，加#colspan#标志
				if (ret.indexOf("#direction_up#") > 0
						&& ret.indexOf("#direction_down#") > 0) {
					int pos = ret.indexOf("class=\"" + style + "\"");
					if (pos >= 0) {
						ret = ret.substring(0, pos) + " #colspan# "
								+ ret.substring(pos);
					}
				}
			}

		}
		return ret;
	}

	/**
	 * 按给定的模式替换字符串
	 * 
	 * @param input
	 *            要替换的字符串
	 * @param regular
	 *            匹配模式
	 * @param replace
	 *            要替换成的字符串
	 * @param ignoreCase
	 *            是否忽略大小写
	 * @return 返回替换后的字符串
	 */
	public static String regReplace(String input, String regular,
			String replace, boolean ignoreCase) {
		String ret = null;
		if (null != input) {
			ret = input;
			if (null != regular && !"".equals(regular) && null != replace
					&& !"".equals(replace)) {
				Pattern p = null;
				if (ignoreCase) {
					p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
				} else {
					p = Pattern.compile(regular);
				}
				Matcher matchs = p.matcher(ret);
				ret = matchs.replaceAll(replace);
			}
		}
		return ret;
	}

	public static String clearRptHead(String rptHead) {
		String ret = null;
		if (null != rptHead && !"".equals(rptHead)) {
			ret = rptHead;
			// 去除<table></table>以外的内容，
			// 编辑器已经默认是小写
			// 可能有多个表格，
			// 这里只去掉最外层
			// 去掉<table之前的内容
			int pos = ret.indexOf("<table");
			if (pos >= 0) {
				ret = ret.substring(pos);
			}
			// 去掉最后一个</table>及后面的内容
			pos = ret.lastIndexOf("</table>");
			if (pos >= 0) {
				ret = ret.substring(0, pos);
			}
			// 找到第一个<tr
			pos = ret.indexOf("<tr");
			if (pos >= 0) {
				ret = ret.substring(pos);
			}
			pos = ret.lastIndexOf("</tr>");
			if (pos >= 0) {
				ret = ret.substring(0, pos + "</tr>".length());
			}
			// 至此应该就剩下<tr></tr>对了，下面去除所有样式
			// class style width height
			// 去除 style
			String reg = "<(\\w[^>]*) x:(\\w[^>]*)([^>]*)>";
			String replace = "<$1$3>";
			ret = regReplace(ret, reg, replace, true);

			reg = "<(\\w[^>]*) x:str=(\"?.[^>]*\"?)([^>]*)>";
			replace = "<$1$3>";
			ret = regReplace(ret, reg, replace, true);

			reg = "<(\\w[^>]*) style=\"([^\\\"]*)\"([^>]*)>";
			replace = "<$1$3>";
			ret = regReplace(ret, reg, replace, true);

			// 去除 class
			reg = "<(\\w[^>]*) class=\"([^\\\"]*)\"([^>]*)>";
			replace = "<$1$3>";
			ret = regReplace(ret, reg, replace, true);

			// 去除 width
			reg = "<(\\w[^>]*) width=\"([^\\\"]*)\"([^>]*)>";
			replace = "<$1$3>";
			ret = regReplace(ret, reg, replace, true);

			// 去除 height
			reg = "<(\\w[^>]*) height=\"([^\\\"]*)\"([^>]*)>";
			replace = "<$1$3>";
			ret = regReplace(ret, reg, replace, true);
		}
		return ret;
	}

}
