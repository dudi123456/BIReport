package com.ailk.bi.subject.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommDimHierarchy;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.SubjectDimStruct;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.service.dao.ITableHeadHTMLDAO;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectDimUtil;
import com.ailk.bi.subject.util.SubjectStringUtil;
import com.ailk.bi.subject.util.TableRowHeadSwapUtil;

/**
 * @author xdou 表格表头实现类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableHeadHTMLDAO implements ITableHeadHTMLDAO {
	/**
	 * 保存每次调用的数组下标
	 */
	private volatile int aryIndex = 0;

	public volatile StringBuffer exportHead = new StringBuffer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableHeadHTMLDAO#getTableHead(com.asiabi
	 * .base.table.SubjectCommTabDef, com.asiabi.subject.domain.TableCurFunc)
	 */
	public List getTableHead(SubjectCommTabDef subTable, TableCurFunc curFunc,
			String[][] svces) throws SubjectException {
		if (null == subTable)
			throw new SubjectException("生成表格表头时表格定义对象为空");
		if (null == subTable.tableCols) {
			throw new SubjectException("生成表格表头时列对象对象为空");
		}
		if (null == curFunc)
			throw new SubjectException("生成表格表头时功能定义对象为空");
		List head = new ArrayList();
		String customHead = null;
		if (subTable.has_head.equalsIgnoreCase(SubjectConst.YES)
				&& null != subTable.tabHead
				&& null != subTable.tabHead.table_head
				&& !"".equals(subTable.tabHead.table_head)) {
			customHead = subTable.tabHead.table_head;
		}
		exportHead = new StringBuffer();

		if (SubjectConst.YES.equalsIgnoreCase(subTable.row_head_swap)) {
			Map result = TableRowHeadSwapUtil.genSwapHeadLeftPart(subTable,
					curFunc);
			head.add(result.get(SubjectConst.HEAD_KEYS_LEFT));
			exportHead.append(result.get(SubjectConst.HEAD_KEYS_LEFT_EXPORT));
			result = TableRowHeadSwapUtil.genSwapHeadRightPart(subTable,
					curFunc, customHead, svces);
			head.add(result.get(SubjectConst.HEAD_KEYS_RIGHT));
			exportHead.append(result.get(SubjectConst.HEAD_KEYS_RIGHT_EXPORT));
		} else {
			head.add("<tr>\n");
			exportHead.append("<tr>\n");
			head.add(genTableHeadLeftPart(subTable, curFunc, customHead));
			head.add(genTableHeadRightPart(subTable, curFunc, customHead));
			head.add("</tr>\n");
			exportHead.append("</tr>\n");
			exportHead = new StringBuffer(
					SubjectStringUtil.clearHTMLStyle(exportHead));
		}

		return head;
	}

	/**
	 * 生成表格表头固定的左侧
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param customHead
	 *            自定义表头
	 * @return 固定表头的左侧部分
	 */
	private String genTableHeadLeftPart(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String customHead) {
		StringBuffer left = new StringBuffer();
		left.append("		<td align=center valign='top'>\n");
		left.append("<table id=\"iTable_LeftHeadTable1\" "
				+ "width=\"100%\" border=\"0\" "
				+ "cellpadding=\"0\" cellspacing=\"0\" " + "class=\"table\">\n");
		left.append("<tr class=\"").append(curFunc.tabHeadTRClass)
				.append("\">\n");
		// 跨行数
		int rowSpan = 1;
		if (null != customHead && !"".equals(customHead)) {
			if (null != subTable.tabHead.row_span
					&& !"".equals(subTable.tabHead.row_span))
				rowSpan = Integer.parseInt(subTable.tabHead.row_span);
			else
				rowSpan++;
		}

		if (curFunc.hasDimCol)
			rowSpan++;
		aryIndex = 0;
		List tabCols = subTable.preTableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			// 设置一下该取那列值
			if (tabCol.col_id.equalsIgnoreCase(subTable.right_col_id))
				subTable.rightValueIndex = aryIndex;
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)
					&& SubjectConst.NO.equals(tabCol.dim_ascol)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES
								.equalsIgnoreCase(tabCol.is_expand_col)) {
					if (SubjectConst.TABLE_FUNC_EXPAND_ALL
							.equals(curFunc.curUserFunc)) {
						// 全部展开完了,加上收缩链接

						// 先加上本级
						StringBuffer sb = SubjectDimUtil.genCollapseAllLink(
								subTable, curFunc, tabCol);
						StringBuffer tdHTML = genTableColHTML(subTable,
								curFunc, tabCol, rowSpan, sb, aryIndex);
						left.append(tdHTML);
						exportHead.append(tdHTML);
						aryIndex++;
						aryIndex++;
						List levels = tabCol.levels;
						if (null != levels) {
							Iterator levIter = levels.iterator();
							while (levIter.hasNext()) {
								SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) levIter
										.next();
								tdHTML = genTableColLevHTML(subTable, curFunc,
										tabCol, colLev, rowSpan, aryIndex);
								left.append(tdHTML);
								exportHead.append(tdHTML);
								aryIndex++;
								aryIndex++;
							}
						}
					} else if (null != curFunc.filterIndexs
							&& null != curFunc.filterLevel
							&& null != curFunc.filterValues) {
						// 矩阵过滤
						int level = 0;
						int maxLevel = Integer.parseInt(curFunc.filterLevel);
						StringBuffer tdHTML = genTableColHTML(subTable,
								curFunc, tabCol, rowSpan, null, aryIndex);
						left.append(tdHTML);
						exportHead.append(tdHTML);
						aryIndex++;
						aryIndex++;
						List levels = tabCol.levels;
						if (null != levels) {
							Iterator levIter = levels.iterator();
							while (levIter.hasNext() && level <= maxLevel) {
								level++;
								SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) levIter
										.next();
								tdHTML = genTableColLevHTML(subTable, curFunc,
										tabCol, colLev, rowSpan, aryIndex);
								left.append(tdHTML);
								exportHead.append(tdHTML);
								aryIndex++;
								aryIndex++;
							}
						}
					} else {
						// 展开列,加上全部展开链接
						StringBuffer sb = SubjectDimUtil.genExpandAllLink(
								subTable, curFunc, tabCol);
						StringBuffer tdHTML = genTableColHTML(subTable,
								curFunc, tabCol, rowSpan, sb, aryIndex);
						left.append(tdHTML);
						exportHead.append(tdHTML);
						aryIndex++;
						aryIndex++;
					}
				} else {
					// 非展开列
					StringBuffer tdHTML = genTableColHTML(subTable, curFunc,
							tabCol, rowSpan, null, aryIndex);
					left.append(tdHTML);
					exportHead.append(tdHTML);
					aryIndex++;
					aryIndex++;
				}
			}
		}
		left.append("</tr></table>\n");
		left.append("</td>");
		return left.toString();
	}

	/**
	 * 生成固定表头的右侧部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param customHead
	 *            自定义表头
	 * @return 表格表头的右侧部分
	 */
	private String genTableHeadRightPart(SubjectCommTabDef subTable,
			TableCurFunc curFunc, String customHead) {
		StringBuffer right = new StringBuffer();
		right.append("<td width=\"100%\" align=\"left\" valign=\"top\">\n");
		right.append("	<div id=\"Layer1\" "
				+ "style=\"position:absolute; width:100%; z-index:1; "
				+ "overflow: hidden;\">\n");
		right.append("	<table width=\"100%\" border=\"0\" "
				+ "cellpadding=\"0\" cellspacing=\"0\" "
				+ "class=\"th\" id=\"iTable_HeadTable1\">\n");
		if (null != customHead) {
			// 这里需要对所有的样式进行替换
			String head = customHead;
			String reg = "<(\\w[^>]*) style=\"([^\\\"]*)\"([^>]*)>";
			String replace = "<$1$3>";
			String ret = SubjectStringUtil.regReplace(head, reg, replace, true);

			// 去除 class
			reg = "<(\\w[^>]*) class=\"([^\\\"]*)\"([^>]*)>";
			replace = "<$1$3>";
			ret = SubjectStringUtil.regReplace(ret, reg, replace, true);
			// 设置样式
			reg = "<tr(\\s*\\w*[^>]*)>";
			replace = "<tr class=\"" + curFunc.tabHeadTRClass + "\"$1>";
			ret = SubjectStringUtil.regReplace(ret, reg, replace, true);
			// 设置td样式
			reg = "<td(\\s*\\w*[^>]*)>";
			replace = "<td class=\"" + curFunc.tabHeadTDClass + "\"$1>";
			ret = SubjectStringUtil.regReplace(ret, reg, replace, true);
			right.append(ret);
			// 对于导出，还比较麻烦,需要处理，这时有<tr>了需要找出第一<tr>找出来，然后去掉
			String tmpHead = customHead;
			int pos = tmpHead.indexOf("<tr");
			if (pos >= 0) {
				int end = tmpHead.indexOf(">", pos + 1);
				if (end >= 0) {
					tmpHead = tmpHead.substring(end + 1);
					exportHead.append(tmpHead);
					exportHead.append("<tr>\n");
				}
			} else {
				pos = tmpHead.indexOf("<TR");
				int end = tmpHead.indexOf(">", pos + 1);
				if (end >= 0) {
					tmpHead = tmpHead.substring(end + 1);
					exportHead.append(tmpHead);
					exportHead.append("<tr>\n");
				}
			}
		}
		right.append("<tr class=\"").append(curFunc.tabHeadTRClass)
				.append("\">\n");
		// 列合并数
		int colspan = 0;
		StringBuffer sb = new StringBuffer();
		StringBuffer singleCell = new StringBuffer();
		List tabCols = subTable.preTableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			singleCell.delete(0, singleCell.length());
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_measure)) {
				colspan++;
				if (tabCol.rowSpaned) {
					String origin = "{::" + tabCol.head_place + "::}";
					int pos = right.indexOf(origin);
					if (pos >= 0) {
						tabCol.rowSpaned = true;
					} else {
						tabCol.rowSpaned = false;
					}
				}
				// 要考虑占比、同比、环比显示
				singleCell.append("<td nowrap");
				if (tabCol.rowSpaned) {
					singleCell.append(" rowspan=\"").append(tabCol.row_span)
							.append("\" ");
				}
				singleCell.append(" align=\"center\" class=\"")
						.append(curFunc.tabHeadTDClass).append("\" ").append("title=\"").append(tabCol.col_desc)
						.append("\">\n");
				StringBuffer html = new StringBuffer(tabCol.col_name);
				// 没有跨行合并，由于那样没法排序
				if (!curFunc.hasDimNotAsWhere)
					html.append(SubjectStringUtil.genSortStr(subTable, curFunc,
							tabCol, aryIndex));
				singleCell.append(html);
				// 设置对齐的表头HTMＬ代码
				tabCol.colHeadHTML1 = html.toString();
				singleCell.append("</td>");
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_comratio)) {
					// 有占比
					colspan++;
					singleCell.append("<td nowrap");
					if (tabCol.rowSpaned) {
						singleCell.append(" rowspan=\"")
								.append(tabCol.row_span).append("\" ");
					}
					singleCell.append(" align=\"center\" class=\"")
							.append(curFunc.tabHeadTDClass).append("\" ")
							.append(">\n");
					html.delete(0, html.length());
					html.append("占比");
					if (!curFunc.hasDimNotAsWhere)
						html.append(SubjectStringUtil.genSortStr(subTable,
								curFunc, tabCol, aryIndex));
					singleCell.append(html);
					tabCol.colHeadHTML2 = html.toString();
					singleCell.append("</td>");
				}
				aryIndex++;
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
					// 有占比
					colspan++;
					singleCell.append("<td nowrap");
					if (tabCol.rowSpaned) {
						singleCell.append(" rowspan=\"")
								.append(tabCol.row_span).append("\" ");
					}
					singleCell.append(" align=\"center\" class=\"")
							.append(curFunc.tabHeadTDClass).append("\" ")
							.append(">\n");
					html.delete(0, html.length());
					html.append("排名");
					if (!curFunc.hasDimNotAsWhere)
						html.append(SubjectStringUtil.genSortStr(subTable,
								curFunc, tabCol, aryIndex));
					singleCell.append(html);
					tabCol.colHeadHTML2 = html.toString();
					singleCell.append("</td>");
					aryIndex++;
				}

				if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)
						&& SubjectConst.YES
								.equalsIgnoreCase(tabCol.rank_varity)) {
					// 有占比
					colspan++;
					singleCell.append("<td nowrap");
					if (tabCol.rowSpaned) {
						singleCell.append(" rowspan=\"")
								.append(tabCol.row_span).append("\" ");
					}
					singleCell.append(" align=\"center\" class=\"")
							.append(curFunc.tabHeadTDClass).append("\" ")
							.append(">\n");
					html.delete(0, html.length());
					html.append("排名波动");
					if (!curFunc.hasDimNotAsWhere)
						html.append(SubjectStringUtil.genSortStr(subTable,
								curFunc, tabCol, aryIndex));
					singleCell.append(html);
					tabCol.colHeadHTML2 = html.toString();
					singleCell.append("</td>");
					aryIndex++;
				}

				if (SubjectConst.YES.equalsIgnoreCase(tabCol.last_display)) {
					// 同比显示
					colspan++;
					singleCell.append("<td nowrap");
					if (tabCol.rowSpaned) {
						singleCell.append(" rowspan=\"")
								.append(tabCol.row_span).append("\" ");
					}
					singleCell.append(" align=\"center\" class=\"")
							.append(curFunc.tabHeadTDClass).append("\" ")
							.append(">\n");
					html.delete(0, html.length());
					html.append("同比");
					aryIndex++;
					if (!curFunc.hasDimNotAsWhere)
						html.append(SubjectStringUtil.genSortStr(subTable,
								curFunc, tabCol, aryIndex));
					aryIndex++;
					singleCell.append(html);
					tabCol.colHeadHTML3 = html.toString();
					singleCell.append("</td>");
				} else if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
					// 有同比
					aryIndex++;
					aryIndex++;
				}
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.loop_display)) {
					// 环比显示
					colspan++;
					singleCell.append("<td nowrap");
					if (tabCol.rowSpaned) {
						singleCell.append(" rowspan=\"")
								.append(tabCol.row_span).append("\" ");
					}
					singleCell.append(" align=\"center\" class=\"")
							.append(curFunc.tabHeadTDClass).append("\" ")
							.append(">\n");
					html.delete(0, html.length());
					html.append("环比");
					aryIndex++;
					if (!curFunc.hasDimNotAsWhere)
						html.append(SubjectStringUtil.genSortStr(subTable,
								curFunc, tabCol, aryIndex));
					aryIndex++;
					singleCell.append(html);
					tabCol.colHeadHTML4 = html.toString();
					singleCell.append("</td>");
				} else if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
					// 有环比
					aryIndex++;
					aryIndex++;
				}
				// 这里需要判断
				// 如果指标跨行，则需要替换否则则直接品节上
				if (tabCol.rowSpaned) {
					String origin = "{::" + tabCol.head_place + "::}";
					int pos = right.indexOf(origin);
					if (pos >= 0) {
						right.replace(pos, pos + origin.length(),
								singleCell.toString());
					}
					pos = exportHead.indexOf(origin);
					if (pos >= 0) {
						exportHead.replace(pos, pos + origin.length(),
								singleCell.toString());
					}
				} else {
					sb.append(singleCell);
				}
			}

		}
		// 有维度作为列情况
		if (curFunc.hasDimCol) {
			List dimStructs = curFunc.tabColDimStructs;
			StringBuffer firRow = new StringBuffer();
			StringBuffer secRow = new StringBuffer();
			if (null != dimStructs) {
				iter = dimStructs.iterator();
				while (iter.hasNext()) {
					SubjectDimStruct dimStruct = (SubjectDimStruct) iter.next();
					firRow.append("<td nowrap align=\"center\" class=\"")
							.append(curFunc.tabHeadTDClass)
							.append("\" colspan=\"");
					firRow.append(colspan).append("\">\n");
					firRow.append(dimStruct.dim_desc);
					firRow.append("</td>");
					secRow.append(sb);
				}
			} else {
				secRow.append(sb);
			}
			// 两行都准备好了
			right.append(firRow).append("</tr>");
			exportHead.append(firRow);
			right.append("<tr class=\"").append(curFunc.tabHeadTRClass)
					.append("\">\n");
			right.append(secRow);
			exportHead.append(secRow);
		} else {
			right.append(sb);
			exportHead.append(sb);
		}
		right.append("</tr></table></div></td>\n");
		return right.toString();
	}

	/**
	 * 生成维度表格列的HTML
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param rowSpan
	 *            跨行数
	 * @param cntPreString
	 *            文字前的字符串
	 * @param aryIndex
	 *            数组下标
	 * @return 生成的单元格字符串
	 */
	private StringBuffer genTableColHTML(SubjectCommTabDef subTable,
			TableCurFunc curFunc, SubjectCommTabCol tabCol, int rowSpan,
			StringBuffer cntPreString, int aryIndex) {
		StringBuffer left = new StringBuffer();
		left.append("<td nowrap align=\"center\" class=\"")
				.append(curFunc.tabHeadTDClass).append("\" ").append("title=\"").append(tabCol.col_desc).append("\" ");
		if (0 != rowSpan)
			left.append(" rowspan=\"").append(rowSpan).append("\"");
		left.append(">\n");
		StringBuffer html = new StringBuffer();
		if (null != cntPreString)
			html.append(cntPreString);
		html.append(tabCol.col_name);
		if (!curFunc.hasDimNotAsWhere
				&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_measure))
			html.append(SubjectStringUtil.genSortStr(subTable, curFunc, tabCol,
					aryIndex));
		left.append(html);
		tabCol.colHeadHTML1 = html.toString();
		left.append("</td>");
		return left;
	}

	/**
	 * 生成维度层次的水平单元格
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param colLev
	 *            层次对象
	 * @param rowSpan
	 *            跨行数
	 * @param aryIndex
	 *            数组下标
	 * @return 维度层次的单元格HTML
	 */
	private StringBuffer genTableColLevHTML(SubjectCommTabDef subTable,
			TableCurFunc curFunc, SubjectCommTabCol tabCol,
			SubjectCommDimHierarchy colLev, int rowSpan, int aryIndex) {
		StringBuffer left = new StringBuffer();
		left.append("<td nowrap align=\"center\" class=\"")
				.append(curFunc.tabHeadTDClass).append("\" ");
		if (0 != rowSpan)
			left.append(" rowspan=\"").append(rowSpan).append("\"");
		left.append(">\n");
		StringBuffer html = new StringBuffer();
		html.append(colLev.lev_name);
		// if (!curFunc.hasDimNotAsWhere)
		// html.append(SubjectStringUtil.genSortStr(subTable, curFunc, tabCol,
		// aryIndex));
		left.append(html);
		colLev.colHeadHTML1 = html.toString();
		left.append("</td>");
		return left;
	}

	public StringBuffer getTableExportHead() throws SubjectException {
		return this.exportHead;
	}
}
