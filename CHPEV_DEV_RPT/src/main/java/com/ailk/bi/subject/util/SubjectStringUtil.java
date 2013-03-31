package com.ailk.bi.subject.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.subject.domain.TableCurFunc;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SubjectStringUtil {

	public static String truncChinese(String input, String length) {
		String ret = input;
		if (!StringUtils.isBlank(input) && !StringUtils.isBlank(length)) {
			int len = input.length();
			len = Integer.parseInt(length);
			String end = "";
			if (len > input.length()) {
				len = input.length();
			} else {
				end = "...";
			}
			ret = ret.substring(0, len) + end;
		}
		return ret;
	}

	public static String getTableCell(String input, int rowIndex, int cellIndex) {
		StringBuffer sb = new StringBuffer();
		// 先找到所要的行
		if (!StringUtils.isBlank(input)) {
			String regular = "<tr\\s*\\w*[^>]*>(\\s*.*?)</tr>";
			Pattern p = null;
			p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
			Matcher matchs = p.matcher(input);
			int row = 0;
			String rowHtml = null;
			while (matchs.find()) {
				rowHtml = matchs.group(1);
				if (row == rowIndex) {
					break;
				}
				row++;
			}
			// 还得加上跨行的，转变成最简单的td
			regular = "\\{::(\\s*.*?)::\\}";
			String replace = "<td></td>";
			rowHtml = regReplace(rowHtml, regular, replace, true);
			// 分解成td cells
			cellIndex -= 2;
			if (!StringUtils.isBlank(rowHtml)) {
				// 这里已经是里面的内容了
				regular = "(<td\\s*\\w*[^>]*>)(\\s*.*?)(</td>)";
				p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
				matchs = p.matcher(rowHtml);
				int beforeColspan = 0;
				int cells = 0;
				while (matchs.find()) {
					if (cellIndex == beforeColspan) {
						if (!StringUtils.isBlank(matchs.group(2))) {
							sb.append(matchs.group(1)).append(matchs.group(2)).append(matchs.group(3));
						}
						break;
					}
					// 看看是否有colspan
					cells = getCellSpan(matchs.group(1));
					if (cells <= 0)
						beforeColspan += 1;
					else
						beforeColspan += cells;

					if (cellIndex < beforeColspan) {
						// 都在这个跨行内
						break;
					}
				}
			}
		}
		String ret = sb.toString();
		ret = ret.replaceAll("colspan", "rowspan");
		ret = ret.replaceAll("<td", "<td nowrap ");
		return ret;
	}

	private static int getCellSpan(String input) {
		int span = 0;
		String regular = "<\\w[^>]* colspan=\"([^\\\"]*)\"([^>]*)>";
		if (null != regular && !"".equals(regular)) {
			Pattern p = null;
			p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
			Matcher matchs = p.matcher(input);
			while (matchs.find()) {
				span = Integer.parseInt(matchs.group(1));
			}
		}
		return span;
	}

	public static int getHtmlTagCount(String input, String tag) {
		int cnt = 0;
		String regular = "<" + tag + "\\s*\\w*[^>]*>(\\s*.*?)</" + tag + ">";
		if (null != regular && !"".equals(regular)) {
			Pattern p = null;
			p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
			Matcher matchs = p.matcher(input);
			while (matchs.find()) {
				cnt++;
			}

		}
		return cnt;
	}

	/**
	 * 去除某个html标签
	 * 
	 * @param input
	 * @param tag
	 * @return
	 */
	public static String removeHtmlTag(String input, String tag) {
		String ret = input;
		String regular = "<" + tag + "\\s*\\w*[^>]*>(\\s*.*?)</" + tag + ">";
		String replace = "$1";
		ret = regReplace(ret, regular, replace, true);
		return ret;
	}

	/**
	 * 为导出去掉HTML特有的样式
	 * 
	 * @param export
	 * @return
	 */
	public static String clearHTMLStyle(StringBuffer export) {
		StringBuffer ret = new StringBuffer();
		if (null != export) {
			String str = export.toString();
			// 去掉图形功能
			String regular = "<img\\s*\\w*[^>]*>(\\s*.*?)";
			String replace = "$1";
			str = regReplace(str, regular, replace, true);
			regular = "<a\\s*\\w*[^>]*>(\\s*.*?)</a>";
			replace = "$1";
			str = regReplace(str, regular, replace, true);
			// 去除 class
			regular = "<(\\w[^>]*) class=\"([^\\\"]*)\"([^>]*)>";
			replace = "<$1$3>";
			str = regReplace(str, regular, replace, true);
			// 去除 class
			regular = "<(\\w[^>]*) title=\"([^\\\"]*)\"([^>]*)>";
			replace = "<$1$3>";
			str = regReplace(str, regular, replace, true);
			ret.append(str);
		}
		return ret.toString();
	}

	/**
	 * 处理数据表查询条件部分
	 * 
	 * @param curFunc
	 * @param virTableName
	 * @return
	 */
	public static String processDataTableWHERE(TableCurFunc curFunc, String virTableName) {
		String where = curFunc.dataWhere;
		if (null == where || "".equals(where))
			return "";
		where = SubjectStringUtil.replaceVirTabName(where, virTableName);
		return where;
	}

	/**
	 * 生成扩展列的缩进
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param isExpandCol
	 *            是否是扩展列
	 * @return
	 */
	public static String genIndentSpace(SubjectCommTabCol tabCol, boolean isExpandCol) {
		String space = "";
		if (null != tabCol) {
			try {
				int count = Integer.parseInt(tabCol.level);
				if (isExpandCol)
					count++;
				StringBuffer sb = new StringBuffer("");
				for (int i = 0; i < count; i++) {
					sb.append(SubjectConst.INDENT_SPACE);
				}
				space = sb.toString();
			} catch (NumberFormatException nfe) {

			}
		}
		return space;
	}

	/**
	 * 生成固定表体的左侧部分前边
	 * 
	 * @return
	 */
	public static String genFixedBodyLeftPart1() {
		String left = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("<tr valign=\"top\">\n");
		sb.append("<td height=\"100%\">\n");
		sb.append("<div id=\"LayerLeft1\"")
				.append("style=\"position:absolute; width:100%;scrollbar-face-color: #e4eff1;	scrollbar-highlight-color: #bed6e0;	scrollbar-3dlight-color: #f8f9fd;	scrollbar-darkshadow-color: #f8f9fd;	scrollbar-shadow-color: #bed6e0;	scrollbar-arrow-color: #bed6e0;	scrollbar-track-color: #f8f9fd; ")
				.append("z-index:1; overflow: hidden; height: 100%;\">\n");
		sb.append("<table width=\"100%\" border=\"0\" ").append("cellpadding=\"0\" cellspacing=\"0\"")
				.append("class=\"iTable\" id=\"iTable_LeftTable1\">\n");
		left = sb.toString();
		return left;
	}

	/**
	 * 生成固定表体的左侧部分后面
	 * 
	 * @return
	 */
	public static String genFixedBodyLeftPart2() {
		String left = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("</table>\n");
		sb.append("</div>\n");
		sb.append("</td>\n");
		left = sb.toString();
		return left;
	}

	/**
	 * 生成固定表体的右侧部分前边
	 * 
	 * @return
	 */
	public static String genFixedBodyRightPart1() {
		String right = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("<td align=\"left\">");
		sb.append("<div id=\"LayerRight1\" ")
				.append("style=\"position:absolute; width:100%; scrollbar-face-color: #e4eff1;	scrollbar-highlight-color: #bed6e0;	scrollbar-3dlight-color: #f8f9fd;	scrollbar-darkshadow-color: #f8f9fd;	scrollbar-shadow-color: #bed6e0;	scrollbar-arrow-color: #bed6e0;	scrollbar-track-color: #f8f9fd;")
				.append("z-index:1; overflow: auto; height: 100%;\" ").append("onscroll=\"syncScroll()\">");
		sb.append("<table width=\"100%\" border=\"0\" ").append("cellpadding=\"0\" cellspacing=\"0\" ")
				.append("class=\"iTable\" id=\"iTable_ContentTable1\">");
		right = sb.toString();
		return right;
	}

	/**
	 * 生成固定表体的右侧部分后面
	 * 
	 * @return
	 */
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
	 * 根据给定序号生成排序的HTML代码
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param index
	 *            排序索引，数组下标
	 * @return 排序链接
	 */
	public static StringBuffer genSortStr(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			int index) {
		StringBuffer sortLink = new StringBuffer();
		if (SubjectConst.YES.equalsIgnoreCase(subTable.has_sort)) {
			// 先生成所有的当前维度状态链接
			sortLink.append(SubjectConst.TABLE_ACTION_DOT_DO).append("?");
			sortLink.append(SubjectConst.REQ_TABLE_ID).append("=").append(subTable.table_id);
			// 加上当前功能,是某行扩展
			sortLink.append("&").append(SubjectConst.REQ_TABLE_FUNC).append("=").append(SubjectConst.TABLE_FUNC_SORT);
			List tabCols = subTable.tableCols;
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				SubjectCommTabCol colObj = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
						&& SubjectConst.NO.equalsIgnoreCase(colObj.is_measure)) {
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equalsIgnoreCase(colObj.is_expand_col)) {
						sortLink.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
								.append(colObj.level);
					} else {
						sortLink.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
								.append(colObj.level);
					}
				} else if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
						&& SubjectConst.YES.equalsIgnoreCase(colObj.is_measure)) {
					sortLink.append("&").append(SubjectConst.REQ_MSU_LEVEL_PREFIX).append(colObj.index).append("=")
							.append(colObj.level);
				}
			}
			// 加上本层次的值
			StringBuffer sb = new StringBuffer();
			if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand))
				sortLink.append("&").append(SubjectConst.REQ_AJAX_REQUEST).append("=Y");

			// 当前点击列就是本列
			String data_type = SubjectConst.DATA_TYPE_NUMBER;
			if (null != tabCol) {
				if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)
						&& SubjectConst.DATA_TYPE_STRING.equalsIgnoreCase(tabCol.data_type)) {
					data_type = SubjectConst.DATA_TYPE_STRING;
				}
			}
			sortLink.append("&").append(SubjectConst.REQ_SORT_INDEX).append("=").append(index);
			sortLink.append("&").append(SubjectConst.REQ_SORT_DATA_TYPE).append("=").append(data_type);

			if (curFunc.sortedColum == index) {
				// 将前面条件加上，然后加上排序字段
				String sortOrder = "";
				if (SubjectConst.SORT_ASC.equalsIgnoreCase(curFunc.sortOrder))
					sortOrder = SubjectConst.SORT_DESC;
				else
					sortOrder = SubjectConst.SORT_ASC;
				sortLink.append("&").append(SubjectConst.REQ_SORT_ORDER).append("=").append(sortOrder);
				String img = null;
				if (curFunc.sortOrder.equals(SubjectConst.SORT_ASC))
					img = curFunc.sortAscImgName;
				else
					img = curFunc.sortDescImgName;
				String tmpStr = sortLink.toString();
				tmpStr = SubjectConst.AJAX_REQUEST_JS_FUNCTION.replaceAll("::LINK::", tmpStr);
				sb.append("<a href=\"javascript:").append(tmpStr);
				sb.append("\">").append(SubjectStringUtil.genHtmlImg(curFunc.imagePath, img, 9)).append("</a>");
				sortLink.delete(0, sortLink.length());
				sortLink.append(sb);
			} else {
				sortLink.append("&").append(SubjectConst.REQ_SORT_ORDER).append("=").append(SubjectConst.SORT_ASC);
				String img = curFunc.sortGenImgName;
				String tmpStr = sortLink.toString();
				tmpStr = SubjectConst.AJAX_REQUEST_JS_FUNCTION.replaceAll("::LINK::", tmpStr);
				sb.append("<a href=\"javascript:").append(tmpStr);
				sb.append("\">").append(SubjectStringUtil.genHtmlImg(curFunc.imagePath, img, 9)).append("</a>");
				sortLink.delete(0, sortLink.length());
				sortLink.append(sb);
			}
		}
		return sortLink;
	}

	/**
	 * 生成图象链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param ratioValue
	 *            比率值
	 * @param ratioType
	 *            比率类型
	 * @param img
	 *            图片名字
	 * @param alt
	 *            提示信息
	 * @param width
	 *            宽度
	 * @return 图片链接
	 */
	private static String genImgHtml(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String ratioValue, String ratioType, String img, String alt, int width) {
		String ret = "";
		if (null != img) {
			ret = "<img src=\"" + curFunc.imagePath + img + "\" alt=\"" + alt + "\" border=\"0\" width=\"" + width
					+ "\">";
			String tmp = null;
			if (null != tabCol) {
				boolean hasRatio = false;
				String ratioUrl = null;
				String urlTarget = null;
				if (SubjectConst.RATIO_TYPE_LAST.equalsIgnoreCase(ratioType)) {
					hasRatio = (null != tabCol.has_last_link && !"".equalsIgnoreCase(tabCol.has_last_link) && SubjectConst.YES
							.equalsIgnoreCase(tabCol.has_last_link)) ? true : false;
					ratioUrl = tabCol.last_url;
					urlTarget = tabCol.last_target;
				}

				if (SubjectConst.RATIO_TYPE_LOOP.equalsIgnoreCase(ratioType)) {
					hasRatio = (null != tabCol.has_loop_link && !"".equalsIgnoreCase(tabCol.has_loop_link) && SubjectConst.YES
							.equalsIgnoreCase(tabCol.has_loop_link)) ? true : false;
					ratioUrl = tabCol.loop_url;
					urlTarget = tabCol.loop_target;
				}

				if (hasRatio && null != ratioUrl && !"".equals(ratioUrl)) {

					String temp_where = convertWhereToUrlMode(curFunc.dataWhere);
					if ("js".equalsIgnoreCase(urlTarget)) {
						tmp = "<a href=\"javascript:parent." + ratioUrl + "('" + temp_where
								+ SubjectConst.LINK_DIM_STATE + "&" + SubjectConst.REQ_TABLE_ID + "="
								+ subTable.table_id + "&" + SubjectConst.REQ_MSU_FLD + "=" + tabCol.init_code_field
								+ "&" + SubjectConst.REQ_MSU_NAME + "=" + tabCol.col_desc + "&digit_length="
								+ tabCol.digit_length + "" + "');\"";
						tmp += ">";
						if (null != tmp)
							ret = tmp + ret + "</a>";
					} else {
						tmp = "<a href=\"" + ratioUrl + temp_where + SubjectConst.LINK_DIM_STATE + "&"
								+ SubjectConst.REQ_TABLE_ID + "=" + subTable.table_id + "&" + SubjectConst.REQ_MSU_FLD
								+ "=" + tabCol.init_code_field + "&" + SubjectConst.REQ_MSU_NAME + "="
								+ tabCol.col_desc + "&digit_length=" + tabCol.digit_length + "\"";
						if (null != urlTarget && !"".equalsIgnoreCase(urlTarget)) {
							// 有目标
							tmp += " target=\"" + urlTarget + "\"";
						}
						tmp += ">";
						if (null != tmp)
							ret = tmp + ret + "</a>";
					}

				}
			}

		}
		return ret;
	}

	/**
	 * 生成比率图片的HTML
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param ratioValue
	 *            比率值
	 * @param ratioType
	 *            比率类型
	 * @return 图片HTML
	 */
	public static String genRatioImgHTML(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String ratioValue, String ratioType) {
		String imgHTML = "";
		String imgAlt = "";
		if (SubjectConst.RATIO_TYPE_LAST.equalsIgnoreCase(ratioType))
			imgAlt = "同比";
		if (SubjectConst.RATIO_TYPE_LOOP.equalsIgnoreCase(ratioType))
			imgAlt = "环比";
		try {
			double ratio = Double.parseDouble(ratioValue);
			if (ratio > 0 && SubjectConst.RATIO_RISE_GREEN.equalsIgnoreCase(tabCol.rise_arrow_color)) {
				imgHTML += genImgHtml(subTable, curFunc, tabCol, ratioValue, ratioType, curFunc.ratioRiseGreenGif,
						imgAlt + "上升变好", 9);
			}
			if (ratio > 0 && SubjectConst.RATIO_RISE_RED.equalsIgnoreCase(tabCol.rise_arrow_color)) {
				imgHTML += genImgHtml(subTable, curFunc, tabCol, ratioValue, ratioType, curFunc.ratioRiseRedGif, imgAlt
						+ "上升变差", 9);
			}
			if (ratio > 0 && "".equals(tabCol.rise_arrow_color)) {
				imgHTML += genImgHtml(subTable, curFunc, tabCol, ratioValue, ratioType, curFunc.ratioRiseGreenGif,
						imgAlt + "上升变好", 9);
			}
			if (ratio < 0 && SubjectConst.RATIO_RISE_GREEN.equalsIgnoreCase(tabCol.rise_arrow_color)) {
				imgHTML += genImgHtml(subTable, curFunc, tabCol, ratioValue, ratioType, curFunc.ratioDownRedGif, imgAlt
						+ "下降变差", 9);
			}
			if (ratio < 0 && SubjectConst.RATIO_RISE_RED.equalsIgnoreCase(tabCol.rise_arrow_color)) {
				imgHTML += genImgHtml(subTable, curFunc, tabCol, ratioValue, ratioType, curFunc.ratioDownGreenGif,
						imgAlt + "下降变好", 9);
			}
			if (ratio < 0 && "".equals(tabCol.rise_arrow_color)) {
				imgHTML += genImgHtml(subTable, curFunc, tabCol, ratioValue, ratioType, curFunc.ratioDownRedGif, imgAlt
						+ "下降变好", 9);
			}
			if (ratio == 0.0) {
				imgHTML += genImgHtml(subTable, curFunc, tabCol, ratioValue, ratioType, curFunc.ratioZeroGif, imgAlt
						+ "不变", 9);
			}
		} catch (NumberFormatException nfe) {

		}
		return imgHTML;
	}

	/**
	 * 生成指标值正负时的图片显示
	 * 
	 * @param subTable
	 * @param curFunc
	 * @param tabCol
	 * @param value
	 * @return
	 */
	public static String genPosNegImgHTML(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String value) {
		String imgHTML = "";
		try {
			double realValue = Double.parseDouble(value);
			if (realValue > 0) {
				if (SubjectConst.POS_PROCESS_RISE.equalsIgnoreCase(tabCol.pos_process)) {
					imgHTML += "<img src=\"" + curFunc.imagePath + curFunc.ratioRiseRedGif + "\" alt=\"\" "
							+ "border=\"0\" width=\"" + 9 + "\">";
				} else {
					imgHTML += "<img src=\"" + curFunc.imagePath + curFunc.ratioDownGreenGif + "\" alt=\"\" "
							+ "border=\"0\" width=\"" + 9 + "\">";
				}
			}
			if (realValue < 0) {
				if (SubjectConst.POS_PROCESS_RISE.equalsIgnoreCase(tabCol.pos_process)) {
					imgHTML += "<img src=\"" + curFunc.imagePath + curFunc.ratioDownGreenGif + "\" alt=\"\" "
							+ "border=\"0\" width=\"" + 9 + "\">";
				} else {
					imgHTML += "<img src=\"" + curFunc.imagePath + curFunc.ratioRiseRedGif + "\" alt=\"\" "
							+ "border=\"0\" width=\"" + 9 + "\">";
				}
			}
		} catch (NumberFormatException nfe) {

		}
		return imgHTML;
	}

	/**
	 * 转换查询 WHERE为URL模式
	 * 
	 * @param where
	 *            WHERE字符串
	 * @return URL字符串
	 */
	public static String convertWhereToUrlMode(String where) {
		String ret = "";
		if (null != where && !"".equals(where)) {
			ret = where;
			// 替换掉所有的A.
			ret = ret.replaceAll("a\\.", "");
			ret = ret.replaceAll("A\\.", "");
			ret = ret.replaceAll(" and ", "&");
			ret = ret.replaceAll(" AND ", "&");
			ret = ret.replaceAll(" ", "");
			ret = ret.replaceAll("'", "");
		}
		return ret;
	}

	/**
	 * 生成指标值链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param value
	 *            指标值
	 * @return 指标链接
	 */
	public static StringBuffer genHtmlLink(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String value, String where) {
		StringBuffer html = new StringBuffer(value);
		if (null != tabCol && null != value && null != tabCol.link_url && !"".equals(tabCol.link_url)) {
			if ("js".equalsIgnoreCase(tabCol.link_target)) {
				// 这里的指标字段是带SUM值的，可能需要处理
				html = new StringBuffer("<a href=\"javascript:parent.");
				html.append(tabCol.link_url + "('");

				// 条件
				StringBuffer condition = new StringBuffer("");
				condition.append(SubjectConst.REQ_TABLE_ID).append("=").append(subTable.table_id);
				condition.append(where).append(SubjectConst.LINK_DIM_STATE).append("&");
				condition.append(SubjectConst.REQ_MSU_FLD).append("=").append(tabCol.init_code_field).append("&")
						.append(SubjectConst.REQ_MSU_NAME).append("=").append(tabCol.col_desc).append("&")
						.append("digit_length=").append(tabCol.digit_length).append("");

				html.append(condition + "');\"").append(" class=\"").append(curFunc.hrefLinkClass).append("\">")
						.append(value).append("</a>");

			} else {

				// 这里的指标字段是带SUM值的，可能需要处理
				html = new StringBuffer("<a href=\"");
				if (tabCol.link_url.indexOf("?") >= 0)
					html.append(tabCol.link_url);
				else
					html.append(tabCol.link_url).append("?");
				html.append(SubjectConst.REQ_TABLE_ID).append("=").append(subTable.table_id);
				html.append(where).append(SubjectConst.LINK_DIM_STATE).append("&");
				String msu_name = tabCol.col_name;
				try {
					msu_name = URLEncoder.encode(URLEncoder.encode(msu_name, "UTF-8"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
				}
				html.append(SubjectConst.REQ_MSU_FLD).append("=").append(tabCol.init_code_field).append("&")
						.append(SubjectConst.REQ_MSU_NAME).append("=").append(msu_name).append("&")
						.append("digit_length=").append(tabCol.digit_length).append("\"");

				if (null != tabCol.link_target && !"".equals(tabCol.link_target)) {
					html.append(" target=\"").append(tabCol.link_target).append("\"");
				}
				html.append(" class=\"").append(curFunc.hrefLinkClass).append("\">").append(value).append("</a>");
			}

		}
		return html;
	}

	public static int getMaxSubStringLength(String input, String split) {
		int length = getHTMLStrLen(input);
		if (null != input) {
			String[] subStrs = input.split(split);
			if (null != subStrs) {
				length = 0;
				for (int i = 0; i < subStrs.length; i++) {
					int tmpLen = getHTMLStrLen(subStrs[i]);
					if (tmpLen > length)
						length = tmpLen;
				}
			}
		}
		return length;
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
		if (null != supStr && null != value) {
			int name_len = 0;
			int val_len = 0;
			// 需要判断里面是否有图形或者其他HTML代码
			// 这里为了适用硬性回车换行，需要判断一下<br>
			name_len = getMaxSubStringLength(supStr, "<br>");
			val_len = getHTMLStrLen(value);
			if (val_len <= name_len) {
				int total = (name_len - val_len) + 1;
				for (int i = 0; i < total; i++) {
					retStr += "&nbsp;";
				}
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
					String sub = regReplace(tmp, reg, replacement, true);
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
			tmpHtml = regReplace(tmpHtml, reg, replacement, true);
			try {
				len = tmpHtml.getBytes("GBK").length;
			} catch (Exception e) {

			}
			len += 2 * ((imgLen + SubjectConst.MIN_FONT_PX / 2) / SubjectConst.MIN_FONT_PX);
		}
		return len;
	}

	/**
	 * 格式化指标值
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param value
	 *            指标值
	 * @return 格式化后的指标值
	 */
	public static String formatColValue(SubjectCommTabCol tabCol, String value) {
		String retValue = null;
		if (null != tabCol && null != value) {
			if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_ratio)) {
				// 二者皆不为空
				int fraction_num = 2;// 设置默认小数位数为2
				try {
					// 获取列对象定义的小数位数
					fraction_num = Integer.parseInt(tabCol.digit_length);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
				// 按小数位数格式化数值
				if (SubjectConst.DIGIT_PPROCESS_TRUNC.equals(tabCol.digit_process)) {
					retValue = FormatUtil.formatValueWithoutRound(value, fraction_num);
				} else if (SubjectConst.DIGIT_PPROCESS_TRUNC.equals(tabCol.digit_process)) {
					double dbValue = Double.parseDouble(value);
					String tmp = "0.";
					for (int i = 0; i < fraction_num - 1; i++) {
						tmp += "0";
					}
					tmp += "5";
					double t = Double.parseDouble(tmp);
					dbValue = dbValue + t;
					retValue = FormatUtil.formatStr(dbValue + "", fraction_num, true);
				} else {
					retValue = FormatUtil.formatStr(value, fraction_num, true);
				}
			} else {
				retValue = FormatUtil.formatPercent(value, 2, true);
			}
		}
		return retValue;
	}

	/**
	 * 生成提示
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @return 提示字符串
	 */
	public static String genTip(SubjectCommTabCol tabCol) {
		String tip = "";
		if (null != tabCol) {
			tip = "维度:" + SubjectConst.TIP_DIM_NAME + "\n";
			tip += "指标:" + tabCol.col_desc;
		}
		return tip;
	}

	/**
	 * 按指定的图像名称生成HTML图像
	 * 
	 * @param imgName
	 *            图像的名称
	 * @return 生成后的图像HTML
	 */
	public static String genHtmlImg(String imagePath, String imgName, int width) {
		String img = null;
		if (null != imgName) {
			StringBuffer sb = new StringBuffer("<img src=\"");
			sb.append(imagePath).append(imgName).append("\" border=\"0\" width=\"").append(width).append("\">");
			img = sb.toString();
		}
		return img;
	}

	/**
	 * 清除指标合计符号
	 * 
	 * @param codeFld
	 *            指标字段
	 * @return 去掉SUM的指标字段
	 */
	public static String clearMsuSumOper(String codeFld) {
		String code_fld = codeFld;
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

	/**
	 * 处理指标替换
	 * 
	 * @param codeFld
	 *            指标字段
	 * @param replace
	 *            替换字符串
	 * @return 替换后的指标字段
	 */
	public static String processMsuReplace(String codeFld, String replace) {
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
				sb.append("SUM(");
				String real_fld = replace.replaceAll(SubjectConst.MSU_REPLACE, msu_fld);
				sb.append(real_fld);
				sb.append(")");
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
				tmp_str = tmp_str.substring(pos + "SELECT".length(), tmp_str.length());
				sb.append(" SELECT ");
			}
			tmp_str = tmp_str.trim();
			// 这里还存在一个问题,判断一下最后一个字符
			boolean hasLastComma = false;
			hasLastComma = tmp_str.endsWith(",");
			if (!hasLastComma)
				tmp_str += ",";
			pos = tmp_str.indexOf(",");
			while (pos >= 0) {
				String tmp_fld = tmp_str.substring(0, pos);
				tmp_str = tmp_str.substring(pos + 1);
				if (sb.indexOf(tmp_fld + ",") < 0) {
					sb.append(tmp_fld).append(",");
				}
				pos = tmp_str.indexOf(",");
			}
			if (tmp_str.length() > 0)
				sb.append(tmp_str).append(",");
			if (!hasLastComma && sb.lastIndexOf(",") >= 0) {
				sb = new StringBuffer(sb.toString().substring(0, sb.lastIndexOf(",")));
			}
			ret = sb.toString();
		}
		return ret;
	}

	/**
	 * 分析进来维度选择串，生成左连接时的条件
	 * 
	 * @param dimSelect
	 *            维度选择字符串
	 * @return on 条件
	 */
	public static String parseDimSelectAsOnWhere(String dimSelect, String joinVirTabName, String joinedVirTabName) {
		String onWhere = null;
		if (null != dimSelect) {
			// 即使
			StringBuffer sb = new StringBuffer("");
			if (dimSelect.indexOf(",") >= 0) {
				String[] temp = dimSelect.split(",");

				if (null != temp) {
					// 每次两个走
					for (int i = 0; i < temp.length; i = i + 2) {
						temp[i] = replaceVirTabName(temp[i], joinVirTabName);
						if (temp[i].toLowerCase().indexOf("op_time") > -1) {
							// modify by jcm
							// 20100713
						} else {
							sb.append(" AND ").append(temp[i]).append("=");
							String temp_str = temp[i];
							temp_str = replaceVirTabName(temp_str, joinedVirTabName);
							sb.append(temp_str);
						}

					}
				}
				onWhere = sb.toString();
			} else if (dimSelect.trim().length() > 0) {

				dimSelect = dimSelect.trim();
				dimSelect = replaceVirTabName(dimSelect, joinVirTabName);
				sb.append(" AND ").append(dimSelect).append("=");
				String temp_str = dimSelect;
				temp_str = replaceVirTabName(temp_str, joinedVirTabName);
				sb.append(temp_str);
			}
		}
		return onWhere;
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
		String reg = "(\\s*)([a-zA-Z]+\\d*\\.)(\\w*)";
		String replacement = "$1$3";
		ret = regReplace(ret, reg, replacement, true);
		return ret;
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
			String reg = "(\\s*)([a-zA-Z]+\\d*\\.)(\\w*)";
			String replacement = "$1" + virTabName + ".$3";
			ret = regReplace(ret, reg, replacement, true);
		} else {
			if (ret.indexOf("1=1") >= 0) {
				return ret;
			}
			ret = virTabName + "." + ret;
			// 什么也不作，原样返回，由于条件过于复杂
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
	public static String regReplace(String input, String regular, String replace, boolean ignoreCase) {
		String ret = null;
		if (null != input) {
			ret = input;
			if (null != regular && !"".equals(regular) && null != replace && !"".equals(replace)) {
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
	 * 找到指定模式的所有的子串
	 * 
	 * @param input
	 * @param regular
	 * @param ignoreCase
	 * @return
	 */
	public static List findSubStr(String input, String regular, boolean ignoreCase) {
		List ret = null;
		if (null != input) {
			if (null != regular && !"".equals(regular)) {
				ret = new ArrayList();
				Pattern p = null;
				if (ignoreCase) {
					p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
				} else {
					p = Pattern.compile(regular, Pattern.MULTILINE);
				}
				Matcher matchs = p.matcher(input);
				while (matchs.find()) {
					String tmpStr = matchs.group();
					tmpStr = tmpStr.replaceAll("\\(", "");
					tmpStr = tmpStr.replaceAll("\\)", "");
					tmpStr = clearVirTabName(tmpStr);
					if (!ret.contains(tmpStr))
						ret.add(tmpStr);
				}
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		String tmp = "<tr id=\"R_level_0_dim_1_ZZZZ_dim_2_21\" "
				+ "class=\"table-tr\"><td nowrap align=\"right\">ads</td></tr><tr>dddd</tr>";
		// String regular = "<tr[^>]*>(.*?)</tr>";
		// String replace = "$1";
		// System.out.println(tmp.replaceAll(regular, replace));
		// System.out
		// .println(clearDupFld("T.GATHER_MON,A2.BRAND_CODE,A2.BRANDCODE_DESC,"));
		// System.out.println(replaceVirTabName("fm_p_general_d", "A0"));
		// System.out.println(removeHtmlTag(tmp, "tr"));
		System.out.println(getHtmlTagCount(tmp, "tr"));
		String rowHtml = "<tr><td>ddd</td>{::1::}<td>ffff</td>{::2::}";
		String regular = "\\{::(\\s*.*?)::\\}";
		String replace = "<td></td>";
		rowHtml = regReplace(rowHtml, regular, replace, true);
		System.out.println(rowHtml);
		tmp = "<tr id=\"L_level_dim_1_11\" class=\"table-tr\" >" +
				"<td nowrap align=\"CENTER\" class=\"table-td-withbg\" " +
				"title=\"北京京信捷通电信技术有限公司(四海市场南门口)沃代理店\">北京京信捷通...</td><td class=\"ss\">adsadsa</td></tr>";
		regular= "<td([^>]*?) title=\"(.*?)\"([^>]*?)>(.*?)</td>";
		replace = "<td$1$3>$2</td>";
		System.out.println(SubjectStringUtil.regReplace(tmp, regular, replace, true));
		
		
		
		System.out.println(SubjectStringUtil.genStringWithSpace("得分", "4.150").length());
		System.out.println(SubjectStringUtil.genStringWithSpace("得分", "28.500").length());
		String test = "测试12a擦达达网完全额外去";
		System.out.println(SubjectStringUtil.truncChinese(test, "3"));
		System.out.println(test.substring(0, 3));
		System.out.println(SubjectStringUtil.truncChinese(test, "20"));
		System.out.println(test.substring(0, 7));
	}
}
