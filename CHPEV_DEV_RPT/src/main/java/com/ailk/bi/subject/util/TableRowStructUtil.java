package com.ailk.bi.subject.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableRowStruct;

public class TableRowStructUtil {
	private TableRowStructUtil() {

	}

	public static TableRowStruct genTableRowStruct(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String[][] svces, int index) {
		TableRowStruct row = new TableRowStruct();
		StringBuffer export = new StringBuffer();
		export.append("<tr>");
		row.row_id = "_" + index;
		// 生成左侧部分
		StringBuffer sb = genRowLeftPart(subTable, curFunc, index);
		String tmpStr = sb.toString();
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_LEFT_ROWID,
				"L_" + row.row_id);
		tmpStr = StringB.replace(tmpStr, "::"
				+ SubjectConst.REQ_DIM_EXPAND_ROWID + "::", row.row_id);
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_ROWID,
				row.row_id);
		row.leftHTML.append(tmpStr);
		String regular = "<tr[^>]*?>(.*?)</tr>";
		String replace = "$1";
		tmpStr = StringB.replace(tmpStr, "\n", "::line::");
		tmpStr = SubjectStringUtil.regReplace(tmpStr, regular, replace, true);
		tmpStr = StringB.replace(tmpStr, "::line::", "\n");
		tmpStr = StringB.replace(tmpStr, "<br>", "");
		// tmpStr = tmpStr.replaceAll("&nbsp;", "");
		export.append(tmpStr);
		// 生成右侧部分
		sb = genRowRightPart(subTable, curFunc, svces, index);
		tmpStr = sb.toString();
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_RIGHT_ROWID,
				"R_" + row.row_id);
		row.rightHTML.append(tmpStr);
		regular = "<tr[^>]*?>(.*?)</tr>";
		replace = "$1";
		tmpStr = StringB.replace(tmpStr, "\n", "::line::");
		tmpStr = SubjectStringUtil.regReplace(tmpStr, regular, replace, true);
		tmpStr = StringB.replace(tmpStr, "::line::", "\n");
		tmpStr = StringB.replace(tmpStr, "<br>", "");
		// tmpStr = tmpStr.replaceAll("&nbsp;", "");
		export.append(tmpStr);
		// 下面要去掉所有的图象链接，href 等
		export.append("</tr>");
		export = new StringBuffer(SubjectStringUtil.clearHTMLStyle(export));
		row.exportHTML.append(export);
		return row;
	}

	private static StringBuffer genRowLeftPart(SubjectCommTabDef subTable,
			TableCurFunc curFunc, int index) {
		StringBuffer leftPart = new StringBuffer();
		SubjectCommTabCol tabCol = getTableCol(subTable, index);
		leftPart.append(genMsuLeft(subTable, curFunc, tabCol, index));
		return leftPart;
	}

	private static StringBuffer genMsuLeft(SubjectCommTabDef subTable,
			TableCurFunc curFunc, SubjectCommTabCol tabCol, int index) {
		StringBuffer leftPart = new StringBuffer();
		// 先生成最底层部分，其他部分插入
		leftPart.append("<td nowrap align=\"left\" class=\"");
		leftPart.append(SubjectConst.TD_CLASS_REPLACE);
		leftPart.append("\" ");
		// 还要看跨行数
		String rowSpan = tabCol.row_span;
		int span = 0;
		if (!StringUtils.isBlank(rowSpan)) {
			span = Integer.parseInt(rowSpan);
			if (span > 1) {
				leftPart.append(" colspan=\"").append(rowSpan).append("\" ");
			}
		}
		leftPart.append(">");
		// 插入描述
		leftPart.append(tabCol.col_name);
		leftPart.append("</td>");
		// 考虑行的标签
		// 先取出表头
		int totalRows = 1;
		String tableHead = null;
		if (null != subTable.tabHead && null != subTable.tabHead.table_head) {
			tableHead = subTable.tabHead.table_head;
		}
		// 获取总行数
		totalRows += SubjectStringUtil.getHtmlTagCount(tableHead, "tr");
		if (1 == totalRows || span == totalRows) {
			// 如果还是1就简单了,直接加上tr标签即可
			StringBuffer sb = new StringBuffer();
			sb.append("<tr id=\"").append(SubjectConst.TABLE_ROW_LEFT_ROWID)
					.append("\" class=\"");
			if (!StringUtils.isBlank(tabCol.row_css_class)) {
				sb.append(tabCol.row_css_class);
			} else {
				sb.append(SubjectConst.TR_CLASS_REPLACE);
			}

			sb.append("\"");
			sb.append(curFunc.trRest).append(">");
			sb.append(leftPart);
			sb.append("</tr>");
			leftPart.delete(0, leftPart.length());
			leftPart.append(sb);
		} else {
			// 分多种情况，1、 列直接跨所有行，简单模式跟上面一样,放到上面去了
			// 下面为有可能不跨行，或者跨小于总行数
			StringBuffer sb = new StringBuffer();
			sb.append("<tr id=\"").append(SubjectConst.TABLE_ROW_LEFT_ROWID)
					.append("\" class=\"");
			if (!StringUtils.isBlank(tabCol.row_css_class)) {
				sb.append(tabCol.row_css_class);
			} else {
				sb.append(SubjectConst.TR_CLASS_REPLACE);
			}

			sb.append("\"");
			sb.append(curFunc.trRest).append(">");
			// 不跨行，根据index找到所有的td，然后加上
			for (int i = 0; i < (totalRows - 1); i++) {
				sb.append(SubjectStringUtil.getTableCell(tableHead, i, index));
			}
			sb.append(leftPart);
			sb.append("</tr>");
			leftPart.delete(0, leftPart.length());
			leftPart.append(sb);
		}
		return leftPart;
	}

	private static StringBuffer genRowRightPart(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String[][] svces, int index) {
		StringBuffer rightPart = new StringBuffer();
		SubjectCommTabCol tabCol = getTableCol(subTable, index);
		// 这里需要判断是否有维度变为列
		String where = SubjectStringUtil
				.convertWhereToUrlMode(curFunc.dataWhere);
		rightPart.append("<tr id=\"")
				.append(SubjectConst.TABLE_ROW_RIGHT_ROWID)
				.append("\" class=\"");
		if (!StringUtils.isBlank(tabCol.row_css_class)) {
			rightPart.append(tabCol.row_css_class);
		} else {
			rightPart.append(SubjectConst.TR_CLASS_REPLACE);
		}

		rightPart.append("\"");
		rightPart.append(curFunc.trRest).append(">");
		rightPart.append(genMsuParts(subTable, curFunc, tabCol, svces, index,
				where));
		rightPart.append("</tr>");
		return rightPart;
	}

	private static StringBuffer genMsuParts(SubjectCommTabDef subTable,
			TableCurFunc curFunc, SubjectCommTabCol tabCol, String[][] svces,
			int index, String where) {
		// 由于只支持一个维度，因此固定为两列
		StringBuffer rightPart = new StringBuffer();
		String ratioIndexs = subTable.head_swap_ratio_index;
		String ratioDigit = subTable.head_swap_ratio_digit;
		Map<String, String> colDigits = new HashMap<String, String>();
		String coldigit = subTable.head_swap_col_digit;
		if (!StringUtils.isBlank(coldigit)) {
			String[] splits = coldigit.split(";");
			String[] tmp = null;
			if (null != splits) {
				for (String split : splits) {
					tmp = null;
					tmp = split.split(",");
					if (null != tmp && tmp.length >= 2) {
						colDigits.put(tmp[0].replaceAll("\\[", ""),
								tmp[1].replaceAll("\\]", ""));
					}
				}
			}
		}
		int ratioLength = 2;

		try {
			ratioLength = Integer.parseInt(ratioDigit);
		} catch (NumberFormatException e) {
		}

		boolean isRatio = false;
		if (!StringUtils.isBlank(ratioIndexs)) {
			String msuIndex = (index - 1) + ",";
			if (ratioIndexs.indexOf(msuIndex) >= 0) {
				isRatio = true;
			}
		}
		String value = null;
		String msuIndex = null;
		for (int i = 0; i < svces.length - 1; i++) {
			rightPart.append("<td nowrap align=\"right\" class=\"");
			rightPart.append(SubjectConst.TD_CLASS_REPLACE);
			rightPart.append("\">");
			// 首先要有指标值，其次要判断是否有箭头或底色预警
			// 还有就是是否指标有链接
			isRatio = false;
			msuIndex = (i + 1) + ",";
			if (!StringUtils.isBlank(ratioIndexs)) {
				if (ratioIndexs.indexOf(msuIndex) >= 0) {
					isRatio = true;
				}
			}
			value = svces[i][index];
			// 格式化指标输出，这里得做个判断
			if (isRatio) {
				value = FormatUtil.formatPercent(value, ratioLength, true);
			} else if (null != colDigits.get((i + 1) + "")) {
				value = FormatUtil.formatStr(value,
						Integer.parseInt(colDigits.get((i + 1) + "")), true);
			} else {
				value = SubjectStringUtil.formatColValue(tabCol, value);
			}
			// 判断指标是否有链接
			rightPart.append(SubjectStringUtil.genStringWithSpace(svces[i][1],
					value));
			rightPart.append(value);
			rightPart.append("</td>");
		}
		return rightPart;
	}

	@SuppressWarnings("rawtypes")
	private static SubjectCommTabCol getTableCol(SubjectCommTabDef subTable,
			int index) {
		int aryIndex = 0;
		List tabCols = subTable.preTableCols;
		SubjectCommTabCol retCol = null;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)) {
				if (aryIndex == index) {
					retCol = tabCol;
					break;
				}
				if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
					aryIndex++;
				}
				aryIndex++;
			}
		}
		return retCol;
	}

	@SuppressWarnings("rawtypes")
	public static String genRowStructId(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String[] svces) {
		StringBuffer rowId = new StringBuffer();
		StringBuffer levelPart = new StringBuffer();
		StringBuffer valuePart = new StringBuffer();
		levelPart.append(SubjectConst.ROWID_EXP_LEVEL_PREFIX);
		int aryIndex = 0;
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES
								.equalsIgnoreCase(tabCol.is_expand_col)) {
					// 加上层次水平
					levelPart.append(tabCol.level);
					levelPart.append("_");
				}
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES
								.equalsIgnoreCase(tabCol.is_expand_col)) {
					// 判断是否全部展开,如果是
					if (SubjectConst.TABLE_FUNC_EXPAND_ALL
							.equals(curFunc.curUserFunc)) {
						// 全部展开，应该所有列都有了
						// 先加上第0层
						valuePart.append(SubjectConst.ROWID_DIM_PREFIX)
								.append(tabCol.index).append("_");
						valuePart.append(svces[aryIndex]);
						valuePart.append("_");
						aryIndex++;
						aryIndex++;
						List levels = tabCol.levels;
						if (null != levels) {
							for (int i = 0; i < levels.size(); i++) {
								valuePart.append(SubjectConst.ROWID_DIM_PREFIX)
										.append(tabCol.index).append("_");
								valuePart.append(svces[aryIndex]);
								valuePart.append("_");
								aryIndex++;
								aryIndex++;
							}
						}
					} else if (null != curFunc.filterIndexs
							&& null != curFunc.filterLevel
							&& null != curFunc.filterValues) {
						valuePart.append(SubjectConst.ROWID_DIM_PREFIX)
								.append(tabCol.index).append("_");
						valuePart.append(svces[aryIndex]);
						valuePart.append("_");
						aryIndex++;
						aryIndex++;
					} else {
						valuePart.append(SubjectConst.ROWID_DIM_PREFIX)
								.append(tabCol.index).append("_");
						valuePart.append(svces[aryIndex]);
						valuePart.append("_");
						aryIndex++;
						aryIndex++;
					}
				} else {
					valuePart.append(SubjectConst.ROWID_DIM_PREFIX)
							.append(tabCol.index).append("_");
					valuePart.append(svces[aryIndex]);
					valuePart.append("_");
					aryIndex++;
					aryIndex++;
				}
			}
		}
		rowId.append(levelPart).append(valuePart);
		if (rowId.lastIndexOf("_") >= 0) {
			String tmpStr = rowId.toString();
			tmpStr = tmpStr.substring(0, tmpStr.lastIndexOf("_"));
			rowId.delete(0, rowId.length());
			rowId.append(tmpStr);
		}
		return rowId.toString();
	}
}
