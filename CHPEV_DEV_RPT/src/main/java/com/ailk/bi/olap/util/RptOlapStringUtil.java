package com.ailk.bi.olap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RptOlapStringUtil {
	public static StringBuffer getPeriodMsuSumFld(String msuFld,
			String virTableName, String periodType, String periodValue,
			String dateFld, String begDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("SUM((CASE WHEN ").append(virTableName).append(".")
				.append(dateFld).append(">=").append(begDate);
		sb.append(" AND ").append(virTableName).append(".").append(periodType)
				.append("=").append(periodValue).append(" ");
		sb.append(" THEN ").append(msuFld).append(" ELSE 0 END))");
		return sb;
	}

	public static StringBuffer getPeriodMsuFld(String msuFld,
			String virTableName, String periodType, String periodValue) {
		StringBuffer sb = new StringBuffer();
		sb.append("SUM((CASE WHEN ").append(virTableName).append(".")
				.append(periodType).append("=").append(periodValue).append(" ");
		sb.append(" THEN ").append(msuFld).append(" ELSE 0 END))");
		return sb;
	}

	/**
	 * 去除重复的选出字段
	 *
	 * @param input
	 * @return
	 */
	public static String clearDupFld(String input) {
		String ret = null;
		if (null != input) {
			StringBuffer sb = new StringBuffer("");
			String tmp_str = input;
			int pos = tmp_str.indexOf("SELECT");
			if (pos >= 0) {
				tmp_str = tmp_str.substring(pos + "SELECT".length(),
						tmp_str.length());
				sb.append(" SELECT ");
			}
			tmp_str = tmp_str.trim();
			// 这里还存在一个问题,判断一下最后一个字符
			boolean hasLastComma = false;
			hasLastComma = tmp_str.endsWith(RptOlapConsts.MSU_SUB_SELECT_SPLIT);
			if (!hasLastComma)
				tmp_str += RptOlapConsts.MSU_SUB_SELECT_SPLIT;
			pos = tmp_str.indexOf(RptOlapConsts.MSU_SUB_SELECT_SPLIT);
			while (pos >= 0) {
				String tmp_fld = tmp_str.substring(0, pos);
				tmp_str = tmp_str.substring(pos + 1);
				if (sb.indexOf(tmp_fld + RptOlapConsts.MSU_SUB_SELECT_SPLIT) < 0) {
					sb.append(tmp_fld).append(
							RptOlapConsts.MSU_SUB_SELECT_SPLIT);
				}
				pos = tmp_str.indexOf(RptOlapConsts.MSU_SUB_SELECT_SPLIT);
			}
			if (tmp_str.length() > 0)
				sb.append(tmp_str).append(RptOlapConsts.MSU_SUB_SELECT_SPLIT);
			if (!hasLastComma
					&& sb.lastIndexOf(RptOlapConsts.MSU_SUB_SELECT_SPLIT) >= 0) {
				sb = new StringBuffer(sb.toString().substring(0,
						sb.lastIndexOf(RptOlapConsts.MSU_SUB_SELECT_SPLIT)));
			}
			ret = sb.toString();
			ret = ret.replaceAll(RptOlapConsts.MSU_SUB_SELECT_SPLIT, ",");
		}
		return ret;
	}

	public static String removeCalMsuSum(String codeFld) {
		String code_fld = codeFld;
		// 这里只是找SUM(对于其他的格式暂时不管
		int pos = -1;
		code_fld = code_fld.toUpperCase();
		pos = code_fld.indexOf("SUM(");
		if (pos >= 0) {
			StringBuffer sb = new StringBuffer();
			while (pos >= 0) {
				int end = code_fld.indexOf(")", pos + "SUM(".length());
				String msu_fld = "";
				if (end >= 0) {
					msu_fld = code_fld.substring(pos + "SUM(".length(), end);
				} else {
					msu_fld = code_fld.substring(pos + "SUM(".length());
				}
				sb.append(code_fld.substring(0, pos));
				sb.append(msu_fld);
				code_fld = code_fld.substring(end + 1);
				pos = code_fld.indexOf("SUM(");
			}
			sb.append(code_fld);
			code_fld = sb.toString();
		} else {
			// 如果没有SUM(
			// 什么也不作
		}
		return code_fld;
	}

	public static String processThisPeriodCalMsu(String codeFld,
			String virTabName, String periodType, String periodValue,
			String dateFld, String begDate) {
		String code_fld = codeFld;
		// 这里只是找SUM(对于其他的格式暂时不管
		int pos = -1;
		code_fld = code_fld.toUpperCase();
		StringBuffer sb = new StringBuffer();
		pos = code_fld.indexOf("SUM(");
		if (pos >= 0) {
			while (pos >= 0) {
				int end = code_fld.indexOf(")", pos + "SUM(".length());
				String msu_fld = "";
				if (end >= 0) {
					msu_fld = code_fld.substring(pos + "SUM(".length(), end);
				} else {
					msu_fld = code_fld.substring(pos + "SUM(".length());
				}
				sb.append(code_fld.substring(0, pos));
				String msuFld = msu_fld;
				msuFld = RptOlapStringUtil
						.replaceVirTabName(msuFld, virTabName);
				sb.append("SUM((CASE WHEN ").append(virTabName).append(".")
						.append(dateFld).append(">=").append(begDate);
				sb.append(" AND ").append(virTabName).append(".")
						.append(periodType).append("=").append(periodValue)
						.append(" ");
				sb.append(" THEN ").append(msuFld).append(" ELSE 0 END))");
				code_fld = code_fld.substring(end + 1);
				pos = code_fld.indexOf("SUM(");
			}
			sb.append(code_fld);
			code_fld = sb.toString();
		} else {
			// 如果没有SUM(
			// 什么也不作
		}

		return code_fld;
	}

	public static String processCalMsu(String codeFld, String virTabName,
			String periodType, String periodValue) {
		String code_fld = codeFld;
		// 这里只是找SUM(对于其他的格式暂时不管
		int pos = -1;
		code_fld = code_fld.toUpperCase();
		pos = code_fld.indexOf("SUM(");
		StringBuffer sb = new StringBuffer();
		if (pos >= 0) {
			while (pos >= 0) {
				int end = code_fld.indexOf(")", pos + "SUM(".length());
				String msu_fld = "";
				if (end >= 0) {
					msu_fld = code_fld.substring(pos + "SUM(".length(), end);
				} else {
					msu_fld = code_fld.substring(pos + "SUM(".length());
				}
				sb.append(code_fld.substring(0, pos));
				msu_fld = clearVirTabName(msu_fld);
				sb.append("SUM((CASE WHEN ").append(virTabName).append(".")
						.append(periodType).append("=").append(periodValue)
						.append(" ");
				sb.append(" THEN ").append(msu_fld).append(" ELSE 0 END))");
				code_fld = code_fld.substring(end + 1);
				pos = code_fld.indexOf("SUM(");
			}
			sb.append(code_fld);
			code_fld = sb.toString();
		} else {
			// 如果没有SUM(
			// 什么也不作
		}
		if (code_fld.indexOf("/") >= 0) {
			// 有除法,还得解析到分母，真麻烦，简化处理吧
			sb.delete(0, sb.length());
			sb.append("(CASE WHEN ").append(getDivisionFactor(code_fld))
					.append("=0 THEN NULL ELSE ").append(code_fld)
					.append(" END)");
			code_fld = sb.toString();
			code_fld = RptOlapConsts.NVL_PROC.replaceAll("::NULL::", code_fld);
		}
		return code_fld;
	}

	private static String getDivisionFactor(String codeFld){
		int pos=codeFld.indexOf("/");
		String factor=codeFld;
		if(pos>=0){
			//吧分子和前面的去掉
			factor=factor.substring(pos+1);
		}
		return factor;
	}
	/**
	 * 处理指标替换
	 *
	 * @param codeFld
	 *            指标字段
	 * @param replace
	 *            替换字符串
	 * @return 替换后的指标字段
	 */
	public static String getCalMsuFactors(String codeFld, String virTabName) {
		String code_fld = codeFld;
		// 这里只是找SUM(对于其他的格式暂时不管
		int pos = -1;
		code_fld = code_fld.toUpperCase();
		pos = code_fld.indexOf("SUM(");
		if (pos >= 0) {
			StringBuffer sb = new StringBuffer();
			while (pos >= 0) {
				int end = code_fld.indexOf(")", pos + "SUM(".length());
				String msu_fld = "";
				if (end >= 0) {
					msu_fld = code_fld.substring(pos + "SUM(".length(), end);
				} else {
					msu_fld = code_fld.substring(pos + "SUM(".length());
				}
				msu_fld = replaceVirTabName(msu_fld, virTabName);
				String msuFld = "SUM(" + msu_fld + ")";
				msuFld = RptOlapConsts.NVL_PROC.replaceAll("::NULL::", msuFld);
				sb.append(msuFld);
				msu_fld = clearVirTabName(msu_fld);
				sb.append(" AS ").append(msu_fld)
						.append(RptOlapConsts.MSU_SUB_SELECT_SPLIT);
				code_fld = code_fld.substring(end + 1);
				pos = code_fld.indexOf("SUM(");
			}
			code_fld = sb.toString();
		} else {
			// 如果没有SUM(
			// 什么也不作
		}
		return code_fld;
	}

	public static String addLineWrap(String input) {
		String content = input;
		if (null == content)
			return content;
		content = content.trim();
		int minBytes = RptOlapConsts.DIM_VALUE_ONE_LINE_BYTE;
		minBytes *= 2;
		int len = content.length();
		if (len > minBytes) {
			// 大于指定的长度
			StringBuffer sb = new StringBuffer();
			int loops = len / minBytes;
			for (int i = 0; i < loops; i++) {
				sb.append(content.substring(i * minBytes, (i + 1) * minBytes));
				sb.append("\n");
			}
			sb.append(content.substring(loops * minBytes, content.length()));
			content = sb.toString();
		}
		return content;
	}

	public static String addHtmlBr(String input) {
		String content = input;
		if (null == content)
			return content;
		content = content.trim();
		int minBytes = RptOlapConsts.DIM_VALUE_ONE_LINE_BYTE;
		int len = content.length();
		if (len > minBytes) {
			// 大于指定的长度
			content = content.substring(0, minBytes) + "...";
		}
		return content;
	}

	public static StringBuffer genHtmlSpaces(int count) {
		StringBuffer spaces = new StringBuffer();
		for (int i = 0; i < count; i++)
			spaces.append("&nbsp;");
		return spaces;
	}

	/**
	 * 生成固定表头的左边部分
	 *
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            列样式
	 * @return
	 */
	public static String genFixedHeadLeftPart(String trStyle, String tdStyle) {
		String left = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("<tr>\n");
		sb.append("		<td align=center valign='top'>\n");
		sb.append("<table id=\"iTable_LeftHeadTable1\" " + "border=\"0\" "
				+ "cellpadding=\"0\" cellspacing=\"0\" "
				+ "class=\"table-bg\">\n");
		sb.append("::leftBody::");
		sb.append("</table>");
		sb.append("</td>\n");
		left = sb.toString();
		return left;
	}

	/**
	 * 生成固定表头的右边部分
	 *
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            列样式
	 * @return
	 */
	public static String genFixedHeadRightPart(String trStyle, String tdStyle) {
		String right = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("<td width=\"100%\" align=\"left\" valign=\"top\">\n");
		sb.append("	<div id=\"fixHeadRightPart\" "
				+ "style=\"position:absolute; width:100%; z-index:1; "
				+ "overflow: hidden;\">\n");
		sb.append("	<table border=\"0\" "
				+ "cellpadding=\"0\" cellspacing=\"0\" "
				+ "class=\"table-bg\" id=\"iTable_HeadTable1\">\n");
		sb.append("::rightBody::");
		sb.append("</table></div></td>");
		sb.append("</tr>\n");
		right = sb.toString();
		return right;
	}

	public static String genFixedBodyLeftPart1(String trStyle, String tdStyle) {
		String left = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("<tr valign=\"top\">\n");
		sb.append("<td height=\"100%\">\n");
		sb.append("<div id=\"LayerLeft1\"")
				.append("style=\"position:absolute; ")
				.append("z-index:1; overflow: hidden; height: 100%;\">\n");
		sb.append("<table border=\"0\" ")
				.append("cellpadding=\"0\" cellspacing=\"0\"")
				.append("class=\"iTable\" id=\"iTable_LeftTable1\">\n");
		left = sb.toString();
		return left;
	}

	public static String genFixedBodyLeftPart2() {
		String left = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("</table>\n");
		sb.append("</div>\n");
		sb.append("</td>\n");
		left = sb.toString();
		return left;
	}

	public static String genFixedBodyRightPart1(String trStyle, String tdStyle) {
		String right = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("<td align=\"left\">");
		sb.append("<div id=\"LayerRight1\" ")
				.append("style=\"position:absolute; ")
				.append("z-index:1; overflow: auto; \" ")
				.append("onscroll=\"syncScroll()\">");
		sb.append("<table width=\"100%\" border=\"0\" ")
				.append("cellpadding=\"0\" cellspacing=\"0\" ")
				.append("class=\"iTable\" id=\"iTable_ContentTable1\">");
		right = sb.toString();
		return right;
	}

	public static String genFixedBodyRightPart2() {
		String right = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("</table>\n");
		sb.append("</div>\n");
		sb.append("</td>\n");
		sb.append("</tr>\n");
		right = sb.toString();
		return right;
	}

	/**
	 * 将字符串的子串去除
	 *
	 * @param str
	 *            要去除字符串的字符串
	 * @param subStr
	 *            去除字符串
	 * @return
	 */
	public static String removeLastSubStr(String str, String subStr) {
		String retStr = str;
		if (null != subStr && null != retStr) {
			int pos = retStr.lastIndexOf(subStr);
			if (pos >= 0) {
				retStr = retStr.substring(0, pos);
			}
		}
		return retStr;
	}

	/**
	 * 生成带有空格的字符串
	 *
	 * @param supStr
	 *            比较字符串
	 * @param value
	 *            要补充空格的字符串
	 * @return
	 */
	public static String genStringWithSpace(String supStr, String value) {
		String retStr = "";
		int name_len = 0;
		int val_len = 0;
		// 需要判断里面是否有图形或者其他HTML代码
		name_len = getHTMLStrLen(supStr) + 5;
		val_len = getHTMLStrLen(value);
		if (val_len <= name_len) {
			int total = (name_len - val_len) + 1;
			for (int i = 0; i < total; i++) {
				retStr += "&nbsp;";
			}
		}
		return retStr;
	}

	/**
	 * 返回可能带有HTML的字符串的实际长度
	 *
	 * @param html
	 *            带有HTML代码的字符串长度
	 * @return
	 */
	public static int getHTMLStrLen(String html) {
		int len = 0;
		if (null != html) {
			// 这里为了速度仅考虑href 和 img
			// 把<a href 替换掉
			String tmpHtml = html;
			String reg = "<a([^>]*)>(.*?)</a>";
			String replacement = "$2";
			tmpHtml = regReplace(tmpHtml, reg, replacement, true);
			// 累计img的宽度,按10px一个字计算，应该够了，
			String imgHtml = tmpHtml;
			int imgLen = 0;
			int pos = -1;
			pos = imgHtml.indexOf("<img");
			while (pos >= 0) {
				int end = imgHtml.indexOf(">", pos);
				if (end >= 0) {
					String tmp = imgHtml.substring(pos, end + 1);
					imgHtml = imgHtml.substring(end + 1);
					reg = "<img([^>]*) width=\"?(\\w*)\"?([^>]*)>";
					replacement = "$2";
					tmp = tmp.toLowerCase();
					String sub = tmp.replaceAll(reg, replacement);
					if (sub.length() < tmp.length()) {
						try {
							imgLen += Integer.parseInt(sub);
						} catch (NumberFormatException nfe) {

						}
					}
				} else {
					imgHtml = imgHtml.substring(pos + 4);
				}
				pos = imgHtml.indexOf("<img");
			}
			// 替换掉所有的
			reg = "<img([^>]*) width=\"?(\\w*)\"?([^>]*)>(\\w*)";
			replacement = "$4";
			tmpHtml = tmpHtml.toLowerCase();
			tmpHtml = tmpHtml.replaceAll(reg, replacement);
			try {
				len = tmpHtml.getBytes("GBK").length;
			} catch (Exception e) {

			}
			len += 2 * ((imgLen + RptOlapConsts.MIN_FONT_PX) / RptOlapConsts.MIN_FONT_PX);
		}
		return len;
	}

	/**
	 * 替换字符串的伪表名
	 *
	 * @param str
	 *            要替换的字符串
	 * @param virTabName
	 *            别名
	 * @return
	 */
	public static String replaceVirTabName(String str, String virTabName) {
		String ret = str;
		// replaceVirTabName(" test=1 and ddd<1", "M")
		int pos = ret.indexOf(".");
		if (pos >= 0) {
			String reg = "(\\s*)(\\w*\\.)(\\w*)";
			String replacement = "$1" + virTabName + ".$3";
			ret = regReplace(ret, reg, replacement, true);
		} else {
			ret = ret.trim();
			ret = virTabName + "." + ret;
		}
		return ret;
	}

	/**
	 * 清除字符串中的伪表名
	 *
	 * @param str
	 *            要清除的字符串
	 * @return
	 */
	public static String clearVirTabName(String str) {
		String ret = str;
		String reg = "(\\s*)(\\w*\\.)(\\w*)";
		String replacement = "$1$3";
		ret = regReplace(ret, reg, replacement, true);
		return ret;
	}

	/**
	 * 获取时间字符串的最开始日期
	 *
	 * @param start
	 *            开始日期
	 * @param period
	 *            统计周期
	 * @return
	 */
	public static String getBeginDate(String start, String period) {
		String ret = start;
		if (RptOlapConsts.RPT_STATIC_MONTH_PERIOD.equals(period)) {
			if (null != start && start.length() >= 4)
				ret = start.substring(0, 4) + "01";
		}
		if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(period)) {
			if (null != start && start.length() >= 6)
				ret = start.substring(0, 6) + "01";
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println(replaceVirTabName(" test=1 and ddd<1", "M"));
		// String test1="asdasdas::所代撒撒";
		// String test2="adsa<a href=\"dsad\" target=>阿斗撒阿斗撒sda</a>adsas";
		// String test3="<img id=\"sda\" width=\"20\"/>sdas sadsa所代洒的";
		// String test4="sadsa<img id=\"sda\" width=\"20\" sdadsad/>sdas
		// sadsa所代洒的";
		String test5 = "本期<a href=\"javascript:loadNewContent"
				+ "('digTableOlap.rptdo?report_id=::report_id::&level_d_1=-1&can_dig_d_1=true&level_d_2=0"
				+ "&can_dig_d_2=false&level_d_3=-1&can_dig_d_3=true&level_d_4=-1"
				+ "&can_dig_d_4=true&level_m_5=0&can_dig_m_5=false&level_m_6=0&can_dig_m_6=false"
				+ "&level_m_7=0&can_dig_m_7=false&level_m_8=0&can_dig_m_8=false"
				+ "&level_m_9=0&can_dig_m_9=false&level_m_10=0&can_dig_m_10=false&level_m_11=0"
				+ "&can_dig_m_11=false&level_m_13=0&can_dig_m_13=false&cur_func=2"
				+ "&sort=11&sort_order=ASC&sort_this=0')\"><img src=\"../images/menu_init.gif\""
				+ "  width=\"9\" border=\"0\"></a>";
		// "&level_m_4=0&can_dig_m_4=false&level_m_5=0&can_dig_m_5" +
		// "=false&level_m_6=0&can_dig_m_6=false&collapse=1')\">" +
		// "<img src=\"../images/shrink_up.gif\" " +
		// "width=\"7\" height=\"11\" border=\"0\"></a>业务类型维";
		String test6 = "<img src=\"../images/no_result.gif\"  width=\"20\" border=\"0\">";
		// sadsa所代洒的";
		// System.out.println(getHTMLStrLen(test1));
		// System.out.println(getHTMLStrLen(test2));
		// System.out.println(getHTMLStrLen(test3));
		// System.out.println(getHTMLStrLen(test4));
		System.out.println(getHTMLStrLen(test5));
		System.out.println(getHTMLStrLen(test6));
		// System.out.println(getHTMLStrLen(test1));
		// System.out.println(getHTMLStrLen(test1));
		// String test7 = "开帐核减用户<a
		// href=\"javascript:loadNewContent('digTableOlap.rptdo?report_id=9902094&level_d_1=0&can_dig_d_1=true&level_d_2=-1&can_dig_d_2=true&level_d_3=-1&can_dig_d_3=true&level_d_4=-1&can_dig_d_4=true&level_d_5=-1&can_dig_d_5=true&level_m_6=0&can_dig_m_6=false&level_m_7=0&can_dig_m_7=false&level_m_8=0&can_dig_m_8=false&level_m_9=0&can_dig_m_9=false&level_m_10=0&can_dig_m_10=false&level_m_11=0&can_dig_m_11=false&level_m_12=0&can_dig_m_12=false&level_m_13=0&can_dig_m_13=false&level_m_14=0&can_dig_m_14=false&level_m_15=0&can_dig_m_15=false&level_m_16=0&can_dig_m_16=false&level_m_17=0&can_dig_m_17=false&level_m_18=0&can_dig_m_18=false&level_m_19=0&can_dig_m_19=false&sort=7&sort_order=ASC&sort_this=0')\"><img
		// src=\"../images/menu_init.gif\" width=\"9\" border=\"0\"></a>";
		// String test8 = "797,561";
		// System.out.println(getHTMLStrLen(test7));
		// System.out.println(getHTMLStrLen(test8));
		// System.out.println("===" + genStringWithSpace(test7, test8) + "==");
		// String input =
		// "javascript:loadNewContent('digTableOlap.rptdo?report_id=9900422&level_d_0=2&can_dig_d_0=true&dim_0=2006&level_d_1=99&can_dig_d_1=false&dim_1=11&level_d_2=-1&can_dig_d_2=true&level_m_6=0&can_dig_m_6=false&level_m_7=0&can_dig_m_7=false&level_m_8=0&can_dig_m_8=false&level_m_9=0&can_dig_m_9=true&dim_0=20064&dim_1=11&click_dim=0')";
		// String regular = "(.*?)(&dim_0=\\w*)(.*?)";
		// String replace = "$1$3";
		// input = regReplace(input, regular, replace, true);
		// System.out.println(input);
		// System.out
		// .println(removeCalMsuSum("SUM(A.FAV_FEE)/(CASE WHEN SUM(A.CHARGE_UNUM)=0 THEN NULL ELSE SUM(A.CHARGE_UNUM) END)"));
		String test = "COALESCE(SUM(A0.CPUC_VISIT_CNT),0) AS CPUC_VISIT_CNT~COALESCE(SUM(A0.VISIT_CNT),0) AS VISIT_CNT~COALESCE(SUM(A0.NET_FLUX),0) AS NET_FLUX~COALESCE(SUM(A0.VISIT_CNT),0) AS VISIT_CNT~COALESCE(SUM(A0.CPUC_VISIT_CNT),0) AS CPUC_VISIT_CNT~COALESCE(SUM(A0.NET_FLUX),0) AS NET_FLUX~COALESCE(SUM(A0.CPUC_VISIT_CNT),0) AS CPUC_VISIT_CNT~";
		System.out.println(test);
		System.out.println(clearDupFld(test));
	}
}
