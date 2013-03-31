package com.ailk.bi.subject.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.exception.SubjectException;
import com.ailk.bi.base.table.SubjectCommDimHierarchy;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableRowStruct;
import com.ailk.bi.subject.service.dao.ITableRowStructDAO;
import com.ailk.bi.subject.util.SubjectConst;
import com.ailk.bi.subject.util.SubjectDataTableUtil;
import com.ailk.bi.subject.util.SubjectDimUtil;
import com.ailk.bi.subject.util.SubjectFilterUtil;
import com.ailk.bi.subject.util.SubjectStringUtil;
import com.ailk.bi.subject.util.TableRowStructUtil;
import com.ailk.bi.subject.util.UserRightUtil;

/**
 * @author XDOU 表格行结构实现类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableRowStructDAO implements ITableRowStructDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.subject.service.dao.ITableRowStructDAO#assemleTableRowStructs
	 * (com.asiabi.base.table.SubjectCommTabDef,
	 * com.asiabi.subject.domain.TableCurFunc, java.lang.String[][])
	 */
	public List assemleTableRowStructs(SubjectCommTabDef subTable, TableCurFunc curFunc, String[][] svces)
			throws SubjectException {
		if (null == subTable)
			throw new SubjectException("组装表格行域对象时表格对象为空");
		if (null == curFunc)
			throw new SubjectException("组装表格行域对象时表格状态对象为空");
		if (null == svces || 0 >= svces.length)
			return null;
		long startTime = System.currentTimeMillis();
		List rows = new ArrayList();
		// 这里进行判断，需要变成每一行，右侧好变，左侧困难
		if (SubjectConst.YES.equalsIgnoreCase(subTable.row_head_swap)) {
			// 纵转横部分,行数其实是二维数组的列数减1
			TableRowStruct row = null;
			for (int i = 2; i < svces[0].length; i++) {
				row = TableRowStructUtil.genTableRowStruct(subTable, curFunc, svces, i);
				rows.add(row);
			}
		} else {
			// 先生成均值行
			TableRowStruct avgRow = null;
			if (SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby)
					&& SubjectConst.YES.equalsIgnoreCase(subTable.has_avg)) {
				avgRow = genAvgRowStruct(subTable, curFunc, svces.length - 1, svces[svces.length - 1]);
				if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.avg_pos))
					rows.add(avgRow);
			}
			boolean isSumRow = false;
			for (int i = 0; i < svces.length; i++) {
				// 是否是合计行
				isSumRow = false;
				if (SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby) && i == svces.length - 1) {
					isSumRow = true;
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_avg)
							&& !SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.avg_pos)) {
						rows.add(avgRow);
					}
				} else
					isSumRow = false;

				// 这里判断一下是否替换跨行
				List tmpRows = replaceRowSpan(rows, curFunc);
				if (null != tmpRows) {
					rows.clear();
					rows.addAll(tmpRows);
				}
				if (!isSumRow || SubjectConst.TABLE_FUNC_INIT.equals(curFunc.curUserFunc)
						|| SubjectConst.TABLE_FUNC_SORT.equals(curFunc.curUserFunc)
						|| SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
					// 判断一下是否需要显示
					if (null != curFunc.filterIndexs && null != curFunc.filterLevel && null != curFunc.filterValues) {
						boolean displayed = SubjectFilterUtil.rowCanDisplayed(subTable, curFunc, svces[i],
								svces[svces.length - 1]);
						if (displayed && !isSumRow) {
							TableRowStruct row = genTableRowStruct(subTable, curFunc, svces[i],
									svces[svces.length - 1], isSumRow);
							row.isSumRow = isSumRow;
							rows.add(row);
						}
					} else {
						TableRowStruct row = genTableRowStruct(subTable, curFunc, svces[i], svces[svces.length - 1],
								isSumRow);
						row.isSumRow = isSumRow;
						rows.add(row);
					}
				}
				// 这里全部展开，合计怎么没有了
			}
			//
			curFunc.needRowSpan = false;
			curFunc.rowspan = curFunc.rowSpanNum;
			List tmpRows = replaceRowSpan(rows, curFunc);
			if (null != tmpRows) {
				rows.clear();
				rows.addAll(tmpRows);
			}
			// 合计行位置问题

			if (SubjectConst.AVG_ROW_POS_FIRST.equalsIgnoreCase(subTable.sum_pos)) {
				// 需要取出最后以行，然后插入到第一行去
				TableRowStruct sum = (TableRowStruct) rows.get(rows.size() - 1);
				rows.remove(rows.get(rows.size() - 1));
				tmpRows = new ArrayList();
				tmpRows.clear();
				tmpRows.add(sum);
				tmpRows.addAll(rows);
				rows.clear();
				rows.addAll(tmpRows);
			}
			System.out.println("专题通用表格表格行组装用时：" + (System.currentTimeMillis() - startTime) + "ms");
		}
		return rows;
	}

	/**
	 * 根据当前功能状态替换跨行
	 * 
	 * @param rows
	 *            行结构列表
	 * @param curFunc
	 *            当前功能对象
	 * @return 替换后的行结构列表
	 */
	private List replaceRowSpan(List rows, TableCurFunc curFunc) {
		List tmpRows = null;
		if (curFunc.hasDimNotAsWhere) {
			// 有跨行设置
			if (!curFunc.needRowSpan) {
				// 这是需要替换合并的行数了
				// 对于所有的行进行遍历
				tmpRows = new ArrayList();
				tmpRows.addAll(rows);
				Iterator iter = tmpRows.iterator();
				while (iter.hasNext()) {
					TableRowStruct tmpRow = (TableRowStruct) iter.next();
					String html = tmpRow.leftCollapseHTML.toString();
					html = StringB.replace(html, SubjectConst.ROW_SPAN, "" + curFunc.rowspan);
					tmpRow.leftCollapseHTML = new StringBuffer(html);
					html = tmpRow.leftExpandHTML.toString();
					if (html.indexOf(SubjectConst.ROW_SPAN) >= 0) {
						// 找到了正确的行
						tmpRow.row_span = curFunc.rowspan;
					}
					html = StringB.replace(html, SubjectConst.ROW_SPAN, "" + curFunc.rowspan);
					tmpRow.leftExpandHTML = new StringBuffer(html);
					html = tmpRow.leftHTML.toString();
					html = StringB.replace(html, SubjectConst.ROW_SPAN, "" + curFunc.rowspan);
					tmpRow.leftHTML = new StringBuffer(html);
				}

			}
		}
		return tmpRows;
	}

	/**
	 * 生成行结构对象
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @param sum
	 *            合计行数组
	 * @param isOddRow
	 *            是否是奇数行
	 * @param isSumRow
	 *            是否是合计行
	 * @return 行结构对象
	 */
	public TableRowStruct genTableRowStruct(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces,
			String[] sum, boolean isSumRow) throws SubjectException {
		TableRowStruct row = new TableRowStruct();
		StringBuffer export = new StringBuffer();
		export.append("<tr>");
		// 生成行id,以 level_0_dim_1_值_dim_2_值的形式
		row.row_id = TableRowStructUtil.genRowStructId(subTable, curFunc, svces);
		// 生成左侧扩展部分
		StringBuffer sb = genRowLeftExpandPart(subTable, curFunc, svces, isSumRow);
		Map dimState = SubjectDimUtil.genDimStateURL(subTable, curFunc, svces);
		String codeState = (String) dimState.get("code_state");
		String descState = (String) dimState.get("desc_state");
		String chartState = (String) dimState.get("chart_state");
		// 替换行ID
		String tmpStr = sb.toString();
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_LEFT_ROWID, "L_" + row.row_id);
		tmpStr = StringB.replace(tmpStr, "::" + SubjectConst.REQ_DIM_EXPAND_ROWID + "::", row.row_id);
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_ROWID, row.row_id);
		String dataWhere = curFunc.dataWhere;
		dataWhere = SubjectStringUtil.convertWhereToUrlMode(dataWhere);
		tmpStr = StringB.replace(tmpStr, SubjectConst.LINK_DIM_STATE, codeState + chartState);
		row.leftExpandHTML.append(tmpStr);
		// 默认是展开链接
		row.leftHTML.append(tmpStr);
		// 这里要去掉所有的 <tr></tr>
		String regular = "<tr[^>]*?>(.*?)</tr>";
		String replace = "$1";
		tmpStr = StringB.replace(tmpStr, "\n", "::line::");
		tmpStr = SubjectStringUtil.regReplace(tmpStr, regular, replace, true);
		tmpStr = StringB.replace(tmpStr, "::line::", "\n");
		tmpStr = StringB.replace(tmpStr, "<br>", "");
		// tmpStr = tmpStr.replaceAll("&nbsp;", "");
		// 这里需要进行一下title的替换
		regular = "<td([^>]*?) title=\"(.*?)\"([^>]*?)>(.*?)</td>";
		replace = "<td$1$3>$2</td>";
		tmpStr = SubjectStringUtil.regReplace(tmpStr, regular, replace, true);
		export.append(tmpStr);
		if (RptOlapConsts.YES.equalsIgnoreCase(subTable.has_expand)) {
			// 生成左侧收缩部分
			sb = genRowLeftCollapsePart(subTable, curFunc, svces, isSumRow);
			// 替换行ID
			tmpStr = sb.toString();
			tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_LEFT_ROWID, "L_" + row.row_id);
			tmpStr = StringB.replace(tmpStr, "::" + SubjectConst.REQ_DIM_COLLAPSE_ROWID + "::", row.row_id);
			tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_ROWID, row.row_id);
			tmpStr = StringB.replace(tmpStr, SubjectConst.LINK_DIM_STATE, codeState + chartState);
			row.leftCollapseHTML.append(tmpStr);
		}
		// 这里生成一下提示的内容
		String dimTip = genDimTip(subTable, curFunc, svces);
		// 生成右侧部分
		sb = genRowRightPart(subTable, curFunc, svces, sum, isSumRow);
		tmpStr = sb.toString();
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_RIGHT_ROWID, "R_" + row.row_id);
		if (isSumRow)
			dimTip = "合计";
		tmpStr = StringB.replace(tmpStr, SubjectConst.TIP_DIM_NAME, dimTip);
		// 替换所有的维度状态
		tmpStr = StringB.replace(tmpStr, SubjectConst.LINK_DIM_STATE, codeState + descState);
		row.rightHTML.append(StringB.replace(tmpStr, SubjectConst.POS_NEG_REPLACE, ""));
		tmpStr = StringB.replace(tmpStr, SubjectConst.POS_NEG_REPLACE, "-");
		// 这里要去掉所有的 <tr></tr>
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
		String[] tmpAry = new String[svces.length];
		System.arraycopy(svces, 0, tmpAry, 0, svces.length);
		// 保存数组，用于排序
		row.svces = tmpAry;
		return row;
	}

	/**
	 * 生成维度提示
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @return 生成维度提示
	 */
	private String genDimTip(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces) {
		StringBuffer dimTip = new StringBuffer();
		int aryIndex = 0;
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
					// 判断是否全部展开,如果是
					if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
						// 全部展开，应该所有列都有了
						// 先加上第0层
						aryIndex++;
						dimTip.append(svces[aryIndex]).append(",");
						aryIndex++;
						List levels = tabCol.levels;
						if (null != levels) {
							for (int i = 0; i < levels.size(); i++) {
								aryIndex++;
								dimTip.append(svces[aryIndex]).append(",");
								aryIndex++;
							}
						}
					} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
							&& null != curFunc.filterValues) {
						aryIndex++;
						dimTip.append(svces[aryIndex]).append(",");
						aryIndex++;
					} else {
						aryIndex++;
						dimTip.append(svces[aryIndex]).append(",");
						aryIndex++;
					}
				} else {
					aryIndex++;
					dimTip.append(svces[aryIndex]).append(",");
					aryIndex++;
				}
			}
		}
		if (dimTip.lastIndexOf(",") >= 0) {
			String tmpStr = dimTip.substring(0, dimTip.lastIndexOf(","));
			dimTip.delete(0, dimTip.length());
			dimTip.append(tmpStr);
		}
		return dimTip.toString();
	}

	/**
	 * 生成行结构的标识
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @return 行标识字符串
	 */

	/**
	 * 生成行收缩情况下左侧部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @param isOddRow
	 *            是否奇数行
	 * @param isSumRow
	 *            是否合计行
	 * @return 行结构的收缩部分
	 */
	private StringBuffer genRowLeftCollapsePart(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces,
			boolean isSumRow) {
		StringBuffer leftPart = new StringBuffer();
		// 加上行
		leftPart.append("<tr id=\"").append(SubjectConst.TABLE_ROW_LEFT_ROWID).append("\" class=\"");
		leftPart.append(SubjectConst.TR_CLASS_REPLACE).append("\" ");
		leftPart.append(curFunc.trRest).append(">");
		List tabCols = subTable.tableCols;
		if (isSumRow) {
			int colSpan = 0;
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
						&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
					//
					colSpan++;
				}
			}
			leftPart.append("<td nowrap align=\"center\" class=\"");
			leftPart.append(SubjectConst.TD_CLASS_REPLACE);
			leftPart.append("\" colspan=\"").append(colSpan).append("\">");
			String chartIds = subTable.rlt_chart_ids;
			String urlLink = SubjectConst.CHART_CHANGE_JS;
			urlLink = StringB.replace(urlLink, "::CHART_IDS::", chartIds);

			String tableLink = SubjectConst.TABLE_CHANGE_JS;
			String tableIds = subTable.rlt_table_ids;
			tableLink = StringB.replace(tableLink, "::CHART_IDS::", tableIds);

			if (SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_chartchange)
					|| SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_tablechange)) {
				leftPart.append("<a href=\"javascript:").append(urlLink + tableLink);
				leftPart.append(" \" class=\"").append(curFunc.hrefLinkClass).append("\" style=\"cursor:hand\"");
				leftPart.append(">");
			}
			leftPart.append("<strong>合计</strong>");
			if (SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_chartchange)
					|| SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_tablechange)) {
				leftPart.append("</a>");
			}
			leftPart.append("</td>");
		} else {
			// 数组的时间下标，每次指向维度的编码
			int aryIndex = 0;
			StringBuffer dimUrl = new StringBuffer();
			dimUrl.append(SubjectDimUtil.genPreExpandDimUrl(subTable));
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
						&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
					// 如果是维度，且显示
					// 区分扩展维度和非展扩维度
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
						// 判断是否全部展开,如果是
						if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
							// 全部展开，应该所有列都有了
							// 先加上第0层
							dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
							leftPart.append(genCollapseLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1, true,
									SubjectConst.ZERO));
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								Iterator levIter = levels.iterator();
								while (levIter.hasNext()) {
									SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();
									dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
									leftPart.append(genCollapseLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1,
											false, levObj.lev_id));
									aryIndex++;
									aryIndex++;
								}
							}
						} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
								&& null != curFunc.filterValues) {
							dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
							leftPart.append(genExpandLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1, false,
									false, SubjectConst.ZERO));
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								int maxLevel = 0;
								int filterLevel = Integer.parseInt(curFunc.filterLevel);
								Iterator levIter = levels.iterator();
								while (levIter.hasNext() && maxLevel <= filterLevel) {
									SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();
									dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
									leftPart.append(genCollapseLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1,
											false, levObj.lev_id));
									aryIndex++;
									aryIndex++;
									maxLevel++;
								}
							}
						} else {
							dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
							leftPart.append(genCollapseLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1, true,
									tabCol.level));
							aryIndex++;
							aryIndex++;
						}

					} else {
						// 非扩展列,应该只要生成简单的列就可以了
						boolean genLink = false;
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_link)
								&& UserRightUtil.canLinkDisplay(subTable, tabCol, svces))
							genLink = true;
						leftPart.append(genNonExpandLeftPart(subTable, tabCol, curFunc, svces, aryIndex + 1,
								SubjectConst.ZERO, genLink));
						String code_fld = tabCol.code_field;
						code_fld = SubjectStringUtil.clearVirTabName(code_fld);
						dimUrl.append("&").append(code_fld).append("=");
						String value = svces[aryIndex];
						if (SubjectConst.DATA_TYPE_STRING.equals(tabCol.data_type)) {
							value = "'" + value + "'";
						}
						dimUrl.append(value);
						aryIndex++;
						aryIndex++;
					}
				}
			}
		}
		leftPart.append("</tr>");
		return leftPart;
	}

	/**
	 * 生成行结构的左侧扩展部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @param isOddRow
	 *            奇数行
	 * @param isSumRow
	 *            合计行
	 * @return 行结构的左侧扩展部分
	 */
	private StringBuffer genRowLeftExpandPart(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces,
			boolean isSumRow) {
		StringBuffer leftPart = new StringBuffer();
		// 加上行
		leftPart.append("<tr id=\"").append(SubjectConst.TABLE_ROW_LEFT_ROWID).append("\" class=\"");
		leftPart.append(SubjectConst.TR_CLASS_REPLACE).append("\" ");
		leftPart.append(curFunc.trRest).append(">");
		List tabCols = subTable.tableCols;
		if (isSumRow) {
			int colSpan = 0;
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
						&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
						if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
							colSpan++;
							List levels = tabCol.levels;
							if (null != levels) {
								colSpan += levels.size();
							}
						} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
								&& null != curFunc.filterValues) {
							colSpan++;
							List levels = tabCol.levels;
							if (null != levels) {
								int maxLevel = 0;
								int filterLevel = Integer.parseInt(curFunc.filterLevel);
								Iterator levIter = levels.iterator();
								while (levIter.hasNext() && maxLevel < filterLevel) {
									colSpan++;
									maxLevel++;
								}
							}
						} else {
							colSpan++;
						}
					} else {
						colSpan++;
					}
				}
			}
			leftPart.append("<td nowrap align=\"center\" class=\"");
			leftPart.append(SubjectConst.TD_CLASS_REPLACE);
			leftPart.append("\" colspan=\"").append(colSpan).append("\">");
			String chartIds = subTable.rlt_chart_ids;
			String urlLink = SubjectConst.CHART_CHANGE_JS;
			urlLink = StringB.replace(urlLink, "::CHART_IDS::", chartIds);

			String tableLink = SubjectConst.TABLE_CHANGE_JS;
			String tableIds = subTable.rlt_table_ids;
			tableLink = StringB.replace(tableLink, "::CHART_IDS::", tableIds);
			if (SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_chartchange)
					|| SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_tablechange)) {
				leftPart.append("<a href=\"javascript:").append(urlLink);
				leftPart.append(" \" class=\"").append(curFunc.hrefLinkClass).append("\" style=\"cursor:hand\"");
				leftPart.append(">");
			}
			leftPart.append("<strong>合计</strong>");
			if (SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_chartchange)
					|| SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_tablechange)) {
				leftPart.append("</a>");
			}
			leftPart.append("</td>");
		} else {
			// 数组的时间下标，每次指向维度的编码
			int aryIndex = 0;
			StringBuffer dimUrl = new StringBuffer();
			dimUrl.append(SubjectDimUtil.genPreExpandDimUrl(subTable));
			// int dimCount = 0;
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
						&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
					// dimCount++;
					// 如果是维度，且显示
					// 区分扩展维度和非展扩维度
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
						// 判断是否全部展开,如果是
						if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
							// 全部展开，应该所有列都有了
							// 先加上第0层
							dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
							leftPart.append(genExpandLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1, false,
									false, SubjectConst.ZERO));
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								Iterator levIter = levels.iterator();
								while (levIter.hasNext()) {
									SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();
									dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
									leftPart.append(genExpandLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1,
											false, false, levObj.lev_id));
									aryIndex++;
									aryIndex++;
								}
							}
						} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
								&& null != curFunc.filterValues) {
							dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
							leftPart.append(genExpandLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1, false,
									false, SubjectConst.ZERO));
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								int maxLevel = 0;
								int filterLevel = Integer.parseInt(curFunc.filterLevel);
								Iterator levIter = levels.iterator();
								while (levIter.hasNext() && maxLevel < filterLevel) {
									SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();
									dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
									leftPart.append(genExpandLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1,
											false, false, levObj.lev_id));
									aryIndex++;
									aryIndex++;
									maxLevel++;
								}
							}
						} else {
							dimUrl.append(SubjectDimUtil.genExpandDimUrl(tabCol, svces, aryIndex + 1));
							leftPart.append(genExpandLeftpart(subTable, tabCol, curFunc, svces, aryIndex + 1, true,
									true, tabCol.level));
							aryIndex++;
							aryIndex++;
						}

					} else {
						// 非扩展列,应该只要生成简单的列就可以了
						boolean genLink = false;
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_link)
								&& UserRightUtil.canLinkDisplay(subTable, tabCol, svces))
							genLink = true;
						leftPart.append(genNonExpandLeftPart(subTable, tabCol, curFunc, svces, aryIndex + 1,
								SubjectConst.ZERO, genLink));
						String code_fld = tabCol.code_field;
						code_fld = SubjectStringUtil.clearVirTabName(code_fld);
						dimUrl.append("&").append(code_fld).append("=");
						String value = svces[aryIndex];
						if (SubjectConst.DATA_TYPE_STRING.equals(tabCol.data_type)) {
							value = "'" + value + "'";
						}
						dimUrl.append(value);
						aryIndex++;
						aryIndex++;
					}
				}
			}
		}
		leftPart.append("</tr>");
		return leftPart;
	}

	/**
	 * 生成收缩部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @param aryIndex
	 *            数组下标
	 * @param isOddRow
	 *            是否奇数行
	 * @param rowSpan
	 *            跨行数
	 * @return 收缩部分
	 */
	private StringBuffer genCollapseLeftpart(SubjectCommTabDef subTable, SubjectCommTabCol tabCol,
			TableCurFunc curFunc, String[] svces, int aryIndex, boolean rowSpan, String level) {
		StringBuffer leftPart = new StringBuffer();
		String value = svces[aryIndex];
		String code = svces[aryIndex - 1];
		boolean genTableCell = true;
		if (curFunc.hasDimNotAsWhere && curFunc.needRowSpan) {
			genTableCell = false;
		} else {
			genTableCell = true;
		}
		if (genTableCell || !rowSpan) {
			leftPart.append("<td nowrap align=\"left\" class=\"");
			leftPart.append(SubjectConst.TD_CLASS_REPLACE);
			leftPart.append("\"");
			if (curFunc.hasDimNotAsWhere) {
				leftPart.append(" rowspan=\"").append(SubjectConst.ROW_SPAN).append("\"");
			}
			if (SubjectConst.YES.equalsIgnoreCase((String) tabCol.descAsTitle.get(level))) {
				value = StringB.replace(value, "\"", "");
				leftPart.append(" title=\"").append(value).append("\"");
			}
			leftPart.append(">");
			// 添加内容
			if (SubjectConst.YES.equalsIgnoreCase((String) tabCol.descAsTitle.get(level))) {
				value = code;
			}
			if (!SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc))
				leftPart.append(SubjectStringUtil.genIndentSpace(tabCol, true));
			// 判断是否生成折叠、扩展链接
			leftPart.append(SubjectDimUtil.genCollapseLink(subTable, curFunc, tabCol, svces));
			// 生成链接
			if (!SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)
					|| (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc) && SubjectConst.ZERO
							.equals(level))) {
				leftPart.append(SubjectDataTableUtil.genChartChangeLink(subTable, tabCol, curFunc, level, svces, value));

			} else {
				leftPart.append(value);
			}
			leftPart.append("</td>");
		}
		return leftPart;
	}

	/**
	 * 生成展开部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param svces
	 *            一维数组
	 * @param aryIndex
	 *            数组下标
	 * @param isOddRow
	 *            奇数行
	 * @param hasExpandLink
	 *            是否有扩展链接
	 * @param rowSpan
	 *            是否跨行
	 * @return 展开部分单元格
	 */
	private StringBuffer genExpandLeftpart(SubjectCommTabDef subTable, SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String[] svces, int aryIndex, boolean hasExpandLink, boolean rowSpan, String level) {
		StringBuffer leftPart = new StringBuffer();
		String code = svces[aryIndex - 1];
		String value = svces[aryIndex];
		if (curFunc.hasDimNotAsWhere) {
			if (value.equals(curFunc.rowSpanDimValue)) {
				curFunc.rowSpanNum++;
				curFunc.needRowSpan = true;
			} else {
				curFunc.rowspan = curFunc.rowSpanNum;
				curFunc.rowSpanNum = 1;
				curFunc.needRowSpan = false;
				curFunc.rowSpanDimValue = value;
			}
		}
		boolean genTableCell = true;
		if (curFunc.hasDimNotAsWhere && curFunc.needRowSpan) {
			genTableCell = false;
		} else {
			genTableCell = true;
		}
		if (genTableCell || !rowSpan) {
			if (!StringUtils.isBlank(tabCol.title_align)) {
				leftPart.append("<td nowrap align=\"").append(tabCol.title_align).append("\" class=\"");
			} else {
				leftPart.append("<td nowrap align=\"left\" class=\"");
			}
			leftPart.append(SubjectConst.TD_CLASS_REPLACE);
			leftPart.append("\"");
			if (curFunc.hasDimNotAsWhere && rowSpan) {
				leftPart.append(" rowspan=\"").append(SubjectConst.ROW_SPAN).append("\"");
			}
			if (SubjectConst.YES.equalsIgnoreCase((String) tabCol.descAsTitle.get(level))) {
				value = StringB.replace(value, "\"", "");
				leftPart.append(" title=\"").append(value).append("\"");
			}
			leftPart.append(">");
			// 添加内容
			if (SubjectConst.YES.equalsIgnoreCase((String) tabCol.descAsTitle.get(level))) {
				value = code;
			}
			StringBuffer html = new StringBuffer();
			if (!SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc))
				html.append(SubjectStringUtil.genIndentSpace(tabCol, true));
			if (hasExpandLink)
				// 判断是否生成折叠、扩展链接
				html.append(SubjectDimUtil.genExpandLink(subTable, curFunc, tabCol, svces));
			// 生成链接,这里应该生成图形联动链接
			if (!SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)
					|| (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc) && SubjectConst.ZERO
							.equals(level))) {
				html.append(SubjectDataTableUtil.genChartChangeLink(subTable, tabCol, curFunc, level, svces, value));

			} else {

				html.append(SubjectDataTableUtil.processDimLink(subTable, tabCol, curFunc, level, svces, value));
			}
			html.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
			leftPart.append(html);
			leftPart.append("</td>");
		}
		return leftPart;
	}

	/**
	 * 生成非扩展列的单元格
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            列对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @param aryIndex
	 *            数组下标
	 * @param isOddRow
	 *            是否是奇数行
	 * @return 非扩展单元格
	 */
	private StringBuffer genNonExpandLeftPart(SubjectCommTabDef subTable, SubjectCommTabCol tabCol,
			TableCurFunc curFunc, String[] svces, int aryIndex, String level, boolean genLink) {
		StringBuffer leftPart = new StringBuffer();
		String value = svces[aryIndex];
		String code = svces[aryIndex - 1];
		if (!StringUtils.isBlank(tabCol.title_align)) {
			leftPart.append("<td nowrap align=\"").append(tabCol.title_align).append("\" class=\"");
		} else {
			leftPart.append("<td nowrap align=\"left\" class=\"");
		}
		leftPart.append(SubjectConst.TD_CLASS_REPLACE);
		leftPart.append("\"");
		if (SubjectConst.YES.equalsIgnoreCase((String) tabCol.descAsTitle.get(level))
				|| SubjectConst.YES.equalsIgnoreCase(tabCol.need_substr)) {
			value = StringB.replace(value, "\"", "");
			leftPart.append(" title=\"").append(value).append("\"");
		}
		leftPart.append(">");
		// 添加内容
		if (SubjectConst.YES.equalsIgnoreCase((String) tabCol.descAsTitle.get(level))) {
			value = code;
		}
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.need_substr)) {
			value = SubjectStringUtil.truncChinese(value, tabCol.substr_num);
		}
		// 生成链接
		if (genLink) {
			value = SubjectDataTableUtil.genChartChangeLink(subTable, tabCol, curFunc, level, svces, value).toString();

		}
		String spaces = SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, value);
		if ("center".equalsIgnoreCase(tabCol.title_align)) {
			if (null != spaces) {
				int half = spaces.length() / 2;
				half = spaces.indexOf("&", half);
				if (half >= 0) {
					leftPart.append(spaces.substring(0, half));
					leftPart.append(value);
					leftPart.append(spaces.substring(half, spaces.length()));
				} else {
					leftPart.append(spaces);
					leftPart.append(value);
				}
			}
		} else if ("right".equalsIgnoreCase(tabCol.title_align)) {
			leftPart.append(spaces);
			leftPart.append(value);
		} else {
			leftPart.append(value);
			leftPart.append(spaces);
		}
		leftPart.append("</td>");
		return leftPart;
	}

	/**
	 * 生成行结构右侧部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param svces
	 *            一维数组
	 * @param sum
	 *            合计行数组
	 * @param isOdd
	 *            是否奇数行
	 * @param isSumRow
	 *            是否合计行
	 * @return 行结构的右侧部分
	 */
	private StringBuffer genRowRightPart(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces,
			String[] sum, boolean isSumRow) {
		StringBuffer rightPart = new StringBuffer();
		// 这里需要判断是否有维度变为列
		String where = SubjectStringUtil.convertWhereToUrlMode(curFunc.dataWhere);
		rightPart.append("<tr id=\"").append(SubjectConst.TABLE_ROW_RIGHT_ROWID).append("\" class=\"");
		rightPart.append(SubjectConst.TR_CLASS_REPLACE).append("\"");
		rightPart.append(curFunc.trRest).append(">");
		if (curFunc.hasDimCol) {
			List dimStructs = curFunc.tabColDimStructs;
			if (null != dimStructs) {
				int aryIndex = 0;
				// 确定每行指标的起点,然后开始累计
				List tabCols = subTable.preTableCols;
				Iterator colsIter = tabCols.iterator();
				while (colsIter.hasNext()) {
					SubjectCommTabCol tabCol = (SubjectCommTabCol) colsIter.next();
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)) {
						if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
							if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
									&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
								if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
									aryIndex++;
									aryIndex++;
									List levels = tabCol.levels;
									if (null != levels) {
										aryIndex += 2 * levels.size();
									}
								} else {
									aryIndex++;
									aryIndex++;
								}
							} else if (SubjectConst.NO.equalsIgnoreCase(tabCol.dim_ascol)) {
								aryIndex++;
								aryIndex++;
							}
						}
					}
				}
				Iterator iter = dimStructs.iterator();
				while (iter.hasNext()) {
					iter.next();
					// 对于每个维度应该都有这些
					colsIter = tabCols.iterator();
					while (colsIter.hasNext()) {
						SubjectCommTabCol tabCol = (SubjectCommTabCol) colsIter.next();
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
								&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_measure)) {
							// 指标
							// 生成指标的值
							rightPart.append(genMsuPart(subTable, curFunc, tabCol, svces, aryIndex, where, isSumRow));
							// 生成占比单元格
							rightPart.append(genMsuComRatioPart(subTable, curFunc, tabCol, svces, sum, aryIndex));
							// 生成同比单元格
							rightPart.append(genMsuLastRatioPart(subTable, curFunc, tabCol, svces, aryIndex));
							// 生成环比单元格
							rightPart.append(genMsuLoopRatioPart(subTable, curFunc, tabCol, svces, aryIndex));
							// 移动数组指针到下一个指标
							aryIndex++;
							if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
								aryIndex++;
							}
							if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
								aryIndex++;
							}
						}
					}
				}
			}
		} else {
			rightPart.append(genMsuParts(subTable, curFunc, svces, sum, where, isSumRow));
		}
		rightPart.append("</tr>");
		return rightPart;
	}

	/**
	 * 生成指标的单元格
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            当前功能对象
	 * @param svces
	 *            一维数组
	 * @param sum
	 *            合计数组
	 * @param isOdd
	 *            是否奇数行
	 * @return 指标单元格
	 */
	private StringBuffer genMsuParts(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces, String[] sum,
			String where, boolean isSumRow) {
		int aryIndex = 0;
		StringBuffer rightPart = new StringBuffer();
		List tabCols = subTable.preTableCols;

		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)) {
				if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
						if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								aryIndex += 2 * levels.size();
							}
						} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
								&& null != curFunc.filterValues) {
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								int maxLevel = 0;
								int filterLevel = Integer.parseInt(curFunc.filterLevel);
								Iterator levIter = levels.iterator();
								while (levIter.hasNext() && maxLevel < filterLevel) {
									aryIndex++;
									aryIndex++;
									maxLevel++;
								}
							}
						} else {
							aryIndex++;
							aryIndex++;
						}
					} else {
						aryIndex++;
						aryIndex++;
					}
				} else {
					// 指标
					// 生成指标的值
					if (!SubjectConst.COMPOSTION.equals(tabCol.has_comratio)) {
						rightPart.append(genMsuPart(subTable, curFunc, tabCol, svces, aryIndex, where, isSumRow));
					}
					// 生成排名单元格
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
						rightPart.append(genMsuRankPart(subTable, curFunc, tabCol, svces, sum, aryIndex, isSumRow));
					}
					// 生成排名波动单元格
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
						rightPart.append(genMsuRankVarPart(subTable, curFunc, tabCol, svces, sum, aryIndex, isSumRow));
					}
					// 生成占比单元格
					rightPart.append(genMsuComRatioPart(subTable, curFunc, tabCol, svces, sum, aryIndex));
					// 生成同比单元格
					rightPart.append(genMsuLastRatioPart(subTable, curFunc, tabCol, svces, aryIndex));
					// 生成环比单元格
					rightPart.append(genMsuLoopRatioPart(subTable, curFunc, tabCol, svces, aryIndex));
					// 移动数组指针到下一个指标
					aryIndex++;
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
						aryIndex++;
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
							aryIndex++;
						}
					}
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
						aryIndex++;
						aryIndex++;
					}
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
						aryIndex++;
						aryIndex++;
					}
				}
			}
		}
		return rightPart;
	}

	/**
	 * 生成指标的环比单元格
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            列对象
	 * @param svces
	 *            一维数组
	 * @param aryIndex
	 *            数组下标
	 * @param isOdd
	 *            是否奇数行
	 * @return 环比单元格
	 */
	private StringBuffer genMsuLoopRatioPart(SubjectCommTabDef subTable, TableCurFunc curFunc,
			SubjectCommTabCol tabCol, String[] svces, int aryIndex) {
		StringBuffer rightPart = new StringBuffer();
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.loop_display)) {
			rightPart.append("<td nowrap align=\"right\" class=\"");
			rightPart.append(SubjectConst.TD_CLASS_REPLACE);
			rightPart.append("\" title=\"");
			rightPart.append(SubjectStringUtil.genTip(tabCol)).append("\">");
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
				aryIndex++;
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
					aryIndex++;
				}
			}
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
				aryIndex++;
				aryIndex++;
			}
			aryIndex++;
			aryIndex++;
			String ratioValue = svces[aryIndex];
			ratioValue = FormatUtil.formatPercent(ratioValue, 2, true);
			rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML4, ratioValue));

			if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop_link)) {
				/** ******************************** */
				String temp_where = SubjectStringUtil.convertWhereToUrlMode(curFunc.dataWhere);

				/** ******************************** */
				if ("js".equalsIgnoreCase(tabCol.loop_target)) {
					String tmp = "<a href=\"javascript:parent." + tabCol.loop_url + "('" + temp_where
							+ SubjectConst.LINK_DIM_STATE + "&" + SubjectConst.REQ_TABLE_ID + "=" + subTable.table_id
							+ "&" + SubjectConst.REQ_MSU_FLD + "=" + tabCol.init_code_field + "&"
							+ SubjectConst.REQ_MSU_NAME + "=" + tabCol.col_desc + "&digit_length="
							+ tabCol.digit_length + "" + "');\"";
					tmp += " class=\"" + curFunc.hrefLinkClass + "\">";
					tmp += ratioValue + "</a>";
					rightPart.append(tmp);

				} else {
					String tmp = "<a href=\"";
					if (tabCol.last_url.indexOf("?") >= 0)
						tmp += tabCol.loop_url;
					else
						tmp += tabCol.loop_url + "?";

					if (temp_where.startsWith("&")) {
						temp_where = temp_where.substring(1);
					}
					tmp += "" + temp_where + SubjectConst.LINK_DIM_STATE + "&" + SubjectConst.REQ_TABLE_ID + "="
							+ subTable.table_id + "&" + SubjectConst.REQ_MSU_FLD + "=" + tabCol.init_code_field + "&"
							+ SubjectConst.REQ_MSU_NAME + "=" + tabCol.col_desc + "&digit_length="
							+ tabCol.digit_length + "" + "\"";
					tmp += " class=\"" + curFunc.hrefLinkClass + "\">";
					tmp += ratioValue + "</a>";
					rightPart.append(tmp);
				}

			} else {
				rightPart.append(ratioValue);
			}
			//
			rightPart.append("</td>");
		}
		return rightPart;
	}

	/**
	 * 生成指标的同比部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            列对象
	 * @param svces
	 *            一维数组
	 * @param aryIndex
	 *            数组下标
	 * @param isOdd
	 *            是否奇数行
	 * @return 同比部分
	 */
	private StringBuffer genMsuLastRatioPart(SubjectCommTabDef subTable, TableCurFunc curFunc,
			SubjectCommTabCol tabCol, String[] svces, int aryIndex) {
		StringBuffer rightPart = new StringBuffer();
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.last_display)) {
			rightPart.append("<td nowrap align=\"right\" class=\"");
			rightPart.append(SubjectConst.TD_CLASS_REPLACE);
			rightPart.append("\" title=\"");
			rightPart.append(SubjectStringUtil.genTip(tabCol)).append("\">");
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
				aryIndex++;
				if (SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
					aryIndex++;
				}
			}
			// 同比本期值
			aryIndex++;
			// 同比值
			aryIndex++;
			String ratioValue = svces[aryIndex];
			// 格式化
			ratioValue = FormatUtil.formatPercent(ratioValue, 2, true);
			rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML3, ratioValue));

			if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last_link)) {
				/** ******************************** */
				String temp_where = SubjectStringUtil.convertWhereToUrlMode(curFunc.dataWhere);

				/** ******************************** */
				if ("js".equalsIgnoreCase(tabCol.last_target)) {
					String tmp = "<a href=\"javascript:parent." + tabCol.last_url + "('" + temp_where
							+ SubjectConst.LINK_DIM_STATE + "&" + SubjectConst.REQ_TABLE_ID + "=" + subTable.table_id
							+ "&" + SubjectConst.REQ_MSU_FLD + "=" + tabCol.init_code_field + "&"
							+ SubjectConst.REQ_MSU_NAME + "=" + tabCol.col_desc + "&digit_length="
							+ tabCol.digit_length + "" + "');\"";
					tmp += " class=\"" + curFunc.hrefLinkClass + "\">";
					tmp += ratioValue + "</a>";
					rightPart.append(tmp);

				} else {
					String tmp = "<a href=\"";
					if (tabCol.last_url.indexOf("?") >= 0)
						tmp += tabCol.last_url;
					else
						tmp += tabCol.last_url + "?";

					if (temp_where.startsWith("&")) {
						temp_where = temp_where.substring(1);
					}
					tmp += "" + temp_where + SubjectConst.LINK_DIM_STATE + "&" + SubjectConst.REQ_TABLE_ID + "="
							+ subTable.table_id + "&" + SubjectConst.REQ_MSU_FLD + "=" + tabCol.init_code_field + "&"
							+ SubjectConst.REQ_MSU_NAME + "=" + tabCol.col_desc + "&digit_length="
							+ tabCol.digit_length + "" + "\"";
					tmp += " class=\"" + curFunc.hrefLinkClass + "\">";

					tmp += ratioValue + "</a>";
					rightPart.append(tmp);
				}
			} else {
				rightPart.append(ratioValue);
			}
			//

			rightPart.append("</td>");
		}
		return rightPart;
	}

	/**
	 * 生成指标的占比部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            列对象
	 * @param svces
	 *            一维数组
	 * @param sum
	 *            合计行数组
	 * @param aryIndex
	 *            数组下标
	 * @param isOdd
	 *            是否奇数行
	 * @return 指标占比单元格
	 */
	private StringBuffer genMsuComRatioPart(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String[] svces, String[] sum, int aryIndex) {
		StringBuffer rightPart = new StringBuffer();
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_comratio)
				|| SubjectConst.COMPOSTION.equals(tabCol.has_comratio)) {
			// 有占比，需要合计行
			rightPart.append("<td nowrap align=\"right\" class=\"");
			rightPart.append(SubjectConst.TD_CLASS_REPLACE);
			rightPart.append("\" title=\"");
			rightPart.append(SubjectStringUtil.genTip(tabCol)).append("\">");
			String value = com.ailk.bi.common.app.Arith.divPer(svces[aryIndex], sum[aryIndex], 2);
			rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML2, value));
			rightPart.append(value);
			rightPart.append("</td>");
		}
		return rightPart;
	}

	private StringBuffer genMsuRankPart(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String[] svces, String[] sum, int aryIndex, boolean isSumRow) {
		StringBuffer rightPart = new StringBuffer();
		// 有占比，需要合计行
		aryIndex++;
		rightPart.append("<td nowrap align=\"right\" class=\"");
		rightPart.append(SubjectConst.TD_CLASS_REPLACE);
		rightPart.append("\" title=\"");
		rightPart.append(SubjectStringUtil.genTip(tabCol)).append("\">");
		String value = svces[aryIndex];
		if (!SubjectConst.NO.equals(tabCol.total_displayed) && isSumRow) {
			rightPart.append("--");
		} else {
			rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML2, value));
			rightPart.append(value);
		}
		rightPart.append("</td>");
		return rightPart;
	}

	private StringBuffer genMsuRankVarPart(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String[] svces, String[] sum, int aryIndex, boolean isSumRow) {
		StringBuffer rightPart = new StringBuffer();
		// 有占比，需要合计行
		aryIndex++;
		aryIndex++;
		rightPart.append("<td nowrap align=\"right\" class=\"");
		rightPart.append(SubjectConst.TD_CLASS_REPLACE);
		rightPart.append("\" title=\"");
		rightPart.append(SubjectStringUtil.genTip(tabCol)).append("\">");
		String value = svces[aryIndex];
		if (!SubjectConst.NO.equals(tabCol.total_displayed) && isSumRow) {
			rightPart.append("--");
		} else {
			rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML2, value));
			rightPart.append(value);
		}
		rightPart.append("</td>");
		return rightPart;
	}

	/**
	 * 生成指标单元格
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            列对象
	 * @param svces
	 *            一维数组
	 * @param aryIndex
	 *            数组下标
	 * @param isOdd
	 *            是否奇数行
	 * @return 指标的单元格
	 */
	private StringBuffer genMsuPart(SubjectCommTabDef subTable, TableCurFunc curFunc, SubjectCommTabCol tabCol,
			String[] svces, int aryIndex, String where, boolean isSumRow) {
		StringBuffer rightPart = new StringBuffer();
		StringBuffer msuTip = new StringBuffer();
		rightPart.append("<td nowrap align=\"right\" class=\"");
		rightPart.append(SubjectConst.TD_CLASS_REPLACE);
		rightPart.append("\" title=\"");
		msuTip.append(SubjectStringUtil.genTip(tabCol));
		rightPart.append(SubjectConst.MSU_TIP).append("\">");
		// 首先要有指标值，其次要判断是否有箭头或底色预警
		// 还有就是是否指标有链接
		StringBuffer html = new StringBuffer();
		String value = svces[aryIndex];
		// 格式化指标输出
		value = SubjectStringUtil.formatColValue(tabCol, value);
		// 这里要加上是否有正负值处理
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.pos_neg_process)) {
			html.append(SubjectStringUtil.genPosNegImgHTML(subTable, curFunc, tabCol, svces[aryIndex]));
			// 此处value需要变成绝对值

			try {
				value = "" + Math.abs(Double.parseDouble(svces[aryIndex]));
				value = SubjectStringUtil.formatColValue(tabCol, value);
				if (Double.parseDouble(svces[aryIndex]) < 0.0) {
					value = SubjectConst.POS_NEG_REPLACE + value;
				}
			} catch (NumberFormatException e) {
			}
			// 导出时怎么办，又变成了正值，怎么办

		}
		// 判断指标是否有链接
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_link)) {
			// 数字有链接
			html.append(SubjectStringUtil.genHtmlLink(subTable, curFunc, tabCol, value, where));
		} else {
			if (SubjectConst.NO.equals(tabCol.total_displayed) && isSumRow) {
				html.append("--");
			} else {
				html.append(value);
			}
		}
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
			aryIndex++;
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
				aryIndex++;
			}
		}
		// 下面考虑是否有同比或者环比
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
			// 有同比
			// 同比值
			aryIndex++;
			aryIndex++;
			// String lastValue = svces[aryIndex];
			// 同比
			String lastRatio = svces[aryIndex];
			msuTip.append("\n同比:").append(FormatUtil.formatPercent(lastRatio, 2, true));
			html.append(SubjectStringUtil.genRatioImgHTML(subTable, curFunc, tabCol, lastRatio,
					SubjectConst.RATIO_TYPE_LAST));
		}
		// 是否有环比
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
			// 有环比值
			aryIndex++;
			aryIndex++;
			// String loopValue = svces[aryIndex];
			// 环比
			String loopRatio = svces[aryIndex];
			msuTip.append("\n环比:").append(FormatUtil.formatPercent(loopRatio, 2, true));
			html.append(SubjectStringUtil.genRatioImgHTML(subTable, curFunc, tabCol, loopRatio,
					SubjectConst.RATIO_TYPE_LOOP));
		}
		rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
		rightPart.append(html);
		rightPart.append("</td>");
		String tmpStr = rightPart.toString();
		tmpStr = StringB.replace(tmpStr, SubjectConst.MSU_TIP, msuTip.toString());
		rightPart.delete(0, rightPart.length());
		rightPart.append(tmpStr);
		return rightPart;
	}

	/**
	 * 生成均值行
	 * 
	 * @param subTable
	 * @param curFunc
	 * @param totalRows
	 * @param sum
	 * @return
	 * @throws SubjectException
	 */
	public TableRowStruct genAvgRowStruct(SubjectCommTabDef subTable, TableCurFunc curFunc, int totalRows, String[] sum)
			throws SubjectException {
		TableRowStruct row = new TableRowStruct();
		StringBuffer export = new StringBuffer();
		export.append("<tr>");
		// 生成行id,以 level_0_dim_1_值_dim_2_值的形式
		row.row_id = TableRowStructUtil.genRowStructId(subTable, curFunc, sum) + "AVG";
		// 生成左侧扩展部分
		StringBuffer sb = genAvgRowLeftExpandPart(subTable, curFunc, sum);
		// 替换行ID
		String tmpStr = sb.toString();
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_LEFT_ROWID, "L_" + row.row_id);
		tmpStr = StringB.replace(tmpStr, "::" + SubjectConst.REQ_DIM_EXPAND_ROWID + "::", row.row_id);
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_ROWID, row.row_id);
		row.leftExpandHTML.append(tmpStr);
		// 默认是展开链接
		row.leftHTML.append(tmpStr);
		// 这里要去掉所有的 <tr></tr>
		String regular = "<tr[^>]*?>(.*?)</tr>";
		String replace = "$1";
		tmpStr = StringB.replace(tmpStr, "\n", "::line::");
		tmpStr = SubjectStringUtil.regReplace(tmpStr, regular, replace, true);
		tmpStr = StringB.replace(tmpStr, "::line::", "\n");
		tmpStr = StringB.replace(tmpStr, "<br>", "");
		// tmpStr = tmpStr.replaceAll("&nbsp;", "");
		export.append(tmpStr);
		// 这里生成一下提示的内容
		String dimTip = genDimTip(subTable, curFunc, sum);
		// 生成右侧部分
		sb = genAvgRowRightPart(subTable, curFunc, totalRows, sum);
		tmpStr = sb.toString();
		tmpStr = StringB.replace(tmpStr, SubjectConst.TABLE_ROW_RIGHT_ROWID, "R_" + row.row_id);
		dimTip = "均值";
		tmpStr = StringB.replace(tmpStr, SubjectConst.TIP_DIM_NAME, dimTip);

		row.rightHTML.append(StringB.replace(tmpStr, SubjectConst.POS_NEG_REPLACE, ""));
		tmpStr = StringB.replace(tmpStr, SubjectConst.POS_NEG_REPLACE, "-");
		// 这里要去掉所有的 <tr></tr>
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
		String[] tmpAry = new String[sum.length];
		System.arraycopy(sum, 0, tmpAry, 0, sum.length);
		// 保存数组，用于排序
		row.svces = tmpAry;
		return row;
	}

	private StringBuffer genAvgRowLeftExpandPart(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces) {
		StringBuffer leftPart = new StringBuffer();
		// 加上行
		leftPart.append("<tr id=\"").append(SubjectConst.TABLE_ROW_LEFT_ROWID).append("\" class=\"");
		leftPart.append(SubjectConst.TR_CLASS_REPLACE).append("\" ");
		leftPart.append(curFunc.trRest).append(">");
		List tabCols = subTable.tableCols;
		int colSpan = 0;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
					if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
						colSpan++;
						List levels = tabCol.levels;
						if (null != levels) {
							colSpan += levels.size();
						}
					} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
							&& null != curFunc.filterValues) {
						colSpan++;
						List levels = tabCol.levels;
						if (null != levels) {
							int maxLevel = 0;
							int filterLevel = Integer.parseInt(curFunc.filterLevel);
							Iterator levIter = levels.iterator();
							while (levIter.hasNext() && maxLevel < filterLevel) {
								colSpan++;
								maxLevel++;
							}
						}
					} else {
						colSpan++;
					}
				} else {
					colSpan++;
				}
			}
		}
		leftPart.append("<td nowrap align=\"center\" class=\"");
		leftPart.append(SubjectConst.TD_CLASS_REPLACE);
		leftPart.append("\" colspan=\"").append(colSpan).append("\">");

		leftPart.append("<strong>均值</strong>");
		leftPart.append("</td>");
		leftPart.append("</tr>");
		return leftPart;
	}

	private StringBuffer genAvgRowRightPart(SubjectCommTabDef subTable, TableCurFunc curFunc, int totalRows,
			String[] svces) {
		StringBuffer rightPart = new StringBuffer();
		// 这里需要判断是否有维度变为列
		rightPart.append("<tr id=\"").append(SubjectConst.TABLE_ROW_RIGHT_ROWID).append("\" class=\"");
		rightPart.append(SubjectConst.TR_CLASS_REPLACE).append("\"");
		rightPart.append(curFunc.trRest).append(">");
		rightPart.append(genAvgMsuParts(subTable, curFunc, totalRows, svces));
		rightPart.append("</tr>");
		return rightPart;
	}

	private StringBuffer genAvgMsuParts(SubjectCommTabDef subTable, TableCurFunc curFunc, int totalRows, String[] svces) {
		int aryIndex = 0;
		StringBuffer rightPart = new StringBuffer();
		List tabCols = subTable.preTableCols;

		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)) {
				if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
						if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								aryIndex += 2 * levels.size();
							}
						} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
								&& null != curFunc.filterValues) {
							aryIndex++;
							aryIndex++;
							List levels = tabCol.levels;
							if (null != levels) {
								int maxLevel = 0;
								int filterLevel = Integer.parseInt(curFunc.filterLevel);
								Iterator levIter = levels.iterator();
								while (levIter.hasNext() && maxLevel < filterLevel) {
									aryIndex++;
									aryIndex++;
									maxLevel++;
								}
							}
						} else {
							aryIndex++;
							aryIndex++;
						}
					} else {
						aryIndex++;
						aryIndex++;
					}
				} else {
					// 指标
					// 生成指标的值
					rightPart.append("<td nowrap align=\"right\" class=\"");
					rightPart.append(SubjectConst.TD_CLASS_REPLACE);
					rightPart.append("\">");
					// 首先要有指标值，其次要判断是否有箭头或底色预警
					// 还有就是是否指标有链接
					StringBuffer html = new StringBuffer();
					// 合计，需要处以数量
					String value = svces[aryIndex];
					value = "" + Double.parseDouble(value) / totalRows;
					// 格式化指标输出
					value = SubjectStringUtil.formatColValue(tabCol, value);
					// 判断指标是否有链接
					if (SubjectConst.NO.equals(tabCol.avg_display)) {
						html.append("");
					} else {
						html.append(value);
					}
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
						aryIndex++;
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
							aryIndex++;
						}
					}
					// 下面考虑是否有同比或者环比
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
						// 有同比
						// 同比值
						aryIndex++;
						aryIndex++;
						// String lastValue = svces[aryIndex];
						// 同比
					}
					// 是否有环比
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
						// 有环比值
						aryIndex++;
						aryIndex++;
						// String loopValue = svces[aryIndex];
						// 环比
					}
					rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
					rightPart.append(html);
					rightPart.append("</td>");

					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_comratio)) {
						rightPart.append("<td nowrap align=\"right\" class=\"");
						rightPart.append(SubjectConst.TD_CLASS_REPLACE);
						rightPart.append("\">");
						html.delete(0, html.length());
						html.append("--");
						rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
						rightPart.append(html);
						rightPart.append("</td>");
					}
					// 生成排名单元格
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
						rightPart.append("<td nowrap align=\"right\" class=\"");
						rightPart.append(SubjectConst.TD_CLASS_REPLACE);
						rightPart.append("\">");
						html.delete(0, html.length());
						html.append("--");
						rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
						rightPart.append(html);
						rightPart.append("</td>");
					}
					// 生成排名波动单元格
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)
							&& SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
						rightPart.append("<td nowrap align=\"right\" class=\"");
						rightPart.append(SubjectConst.TD_CLASS_REPLACE);
						rightPart.append("\">");
						html.delete(0, html.length());
						html.append("--");
						rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
						rightPart.append(html);
						rightPart.append("</td>");
					}
					// 生成占比单元格
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_comratio)) {
						rightPart.append("<td nowrap align=\"right\" class=\"");
						rightPart.append(SubjectConst.TD_CLASS_REPLACE);
						rightPart.append("\">");
						html.delete(0, html.length());
						html.append("--");
						rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
						rightPart.append(html);
						rightPart.append("</td>");
					}
					// 生成同比单元格
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.last_display)) {
						rightPart.append("<td nowrap align=\"right\" class=\"");
						rightPart.append(SubjectConst.TD_CLASS_REPLACE);
						rightPart.append("\">");
						html.delete(0, html.length());
						html.append("--");
						rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
						rightPart.append(html);
						rightPart.append("</td>");
					}
					// 生成环比单元格
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.loop_display)) {
						rightPart.append("<td nowrap align=\"right\" class=\"");
						rightPart.append(SubjectConst.TD_CLASS_REPLACE);
						rightPart.append("\">");
						html.delete(0, html.length());
						html.append("--");
						rightPart.append(SubjectStringUtil.genStringWithSpace(tabCol.colHeadHTML1, html.toString()));
						rightPart.append(html);
						rightPart.append("</td>");
					}
					// 移动数组指针到下一个指标
					aryIndex++;
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_rank)) {
						aryIndex++;
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.rank_varity)) {
							aryIndex++;
						}
					}
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_last)) {
						aryIndex++;
						aryIndex++;
					}
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop)) {
						aryIndex++;
						aryIndex++;
					}
				}
			}
		}
		return rightPart;
	}

}
