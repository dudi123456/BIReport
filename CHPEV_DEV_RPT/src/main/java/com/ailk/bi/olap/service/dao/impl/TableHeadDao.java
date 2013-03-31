package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;
import com.ailk.bi.olap.service.dao.ITableHeadDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDimUtil;
import com.ailk.bi.olap.util.RptOlapMsuUtil;
import com.ailk.bi.olap.util.RptOlapStringUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableHeadDao implements ITableHeadDao {

	/**
	 * 导出的表头
	 */
	private List exportHead = new ArrayList();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.dao.ITableHeadDao#genHTMLTableHead(java.util.
	 * List, com.asiabi.olap.domain.RptOlapFunc, java.lang.String, boolean,
	 * java.lang.String, java.lang.String)
	 */
	public List genHTMLTableHead(PubInfoResourceTable report, List tableCols,
			RptOlapFuncStruct olapFun, String statPeriod, boolean fixedHead,
			String trStyle, String tdStyle) throws ReportOlapException {
		List head = new ArrayList();
		String trClass = trStyle;
		String tdClass = tdStyle;
		if (null == trClass)
			trClass = "";
		if (null == tdClass)
			tdClass = "";
		if (null == tableCols || tableCols.size() <= 0)
			throw new IllegalArgumentException("生成标准表格表头时输入参数为空");
		if (fixedHead) {
			// 固定表头
			StringBuffer left = new StringBuffer("");
			StringBuffer right = new StringBuffer("");
			String leftPart = RptOlapStringUtil.genFixedHeadLeftPart(trStyle,
					tdStyle);
			String rightPart = RptOlapStringUtil.genFixedHeadRightPart(trStyle,
					tdStyle);
			List list = genFixedHeadBody(tableCols, olapFun, statPeriod,
					trStyle, tdStyle);
			if (null != list && list.size() == 2) {
				String[] aryStr = (String[]) list.toArray(new String[list
						.size()]);
				leftPart = leftPart.replaceAll("::leftBody::", aryStr[0]);
				rightPart = rightPart.replaceAll("::rightBody::", aryStr[1]);
			}
			left.append(leftPart);
			right.append(rightPart);
			left.append(right);
			String tmpStr = left.toString();
			tmpStr = tmpStr.replaceAll("::report_id::", report.rpt_id);
			head.add(tmpStr);
		} else {
			// 不固定表头
			StringBuffer sb = new StringBuffer("");
			sb.append(genNonFixedHeadBody(tableCols, olapFun, statPeriod,
					trStyle, tdStyle));
			String tmpStr = sb.toString();
			tmpStr = tmpStr.replaceAll("::report_id::", report.rpt_id);
			head.add(tmpStr);
		}
		return head;
	}

	/**
	 * 生成固定表头的表体部分
	 *
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            列样式
	 * @return
	 */
	private List genFixedHeadBody(List tableCols, RptOlapFuncStruct olapFun,
			String statPeriod, String trStyle, String tdStyle)
			throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size() || null == olapFun
				|| null == statPeriod)
			throw new ReportOlapException("生成固定表头的表体部分时输入的参数为空");
		List head = new ArrayList();

		StringBuffer left = new StringBuffer("");
		StringBuffer rightRow1 = new StringBuffer("");
		StringBuffer rightRow2 = new StringBuffer("");

		StringBuffer exportRow1 = new StringBuffer("");
		StringBuffer exportRow2 = new StringBuffer("");

		left.append("<tr class=\"").append(trStyle).append("\">\n");
		rightRow1.append("<tr class=\"").append(trStyle).append("\">\n");
		exportRow1.append("<tr>\n");
		boolean hasRowSpan = false;
		String func = olapFun.getCurFunc();
		if ((RptOlapConsts.OLAP_FUN_DATA.equals(func) && olapFun.isHasSum())
				|| RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
				|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
				|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
			hasRowSpan = true;
			rightRow2.append("<tr class=\"").append(trStyle).append("\">\n");
			exportRow2.append("<tr>\n");
		}
		int dimRowSpan = 1;
		if (hasRowSpan)
			dimRowSpan++;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay()) {
				if (tCol.isDim()) {
					// 维度不需要排序了，但需要收缩
					List list = RptOlapDimUtil.genDimHead(tableCols, tCol,
							olapFun, dimRowSpan, tdStyle);
					StringBuffer dimHead = (StringBuffer) list.get(0);
					StringBuffer dimExpHead = (StringBuffer) list.get(1);
					left.append(dimHead);
					if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
							.getDigLevel())) {
						exportRow1.append(dimExpHead);
					}
				} else {
					// 指标可能钻取、排序
					List right = RptOlapMsuUtil.genMsuHead(tableCols, tCol,
							olapFun, statPeriod, hasRowSpan, tdStyle);
					StringBuffer right1 = (StringBuffer) right.get(0);
					StringBuffer right2 = (StringBuffer) right.get(1);
					StringBuffer exportRight1 = (StringBuffer) right.get(2);
					StringBuffer exportRight2 = (StringBuffer) right.get(3);
					// 还有导出呢
					rightRow1.append(right1);
					exportRow1.append(exportRight1);
					if (right2.length() > 0) {
						rightRow2.append(right2);
					}
					if (exportRight2.length() > 0) {
						exportRow2.append(exportRight2);
					}
				}
			}
		}
		left.append("</tr>\n");
		rightRow1.append("</tr>\n");
		exportRow1.append("</tr>\n");
		StringBuffer right = new StringBuffer("");
		right.append(rightRow1);
		if (rightRow2.length() > 0) {
			rightRow2.append("</tr>\n");
			right.append(rightRow2);
		}
		if (exportRow2.length() > 0) {
			exportRow2.append("</tr>\n");
			exportRow1.append(exportRow2);
		}
		head.add(left.toString());
		head.add(right.toString());
		exportHead.clear();
		exportHead.add(exportRow1.toString());
		return head;
	}

	/**
	 * 生成非固定表头的表体部分
	 *
	 * @param tableCols
	 *            列域对象
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param trStyle
	 *            行样式
	 * @param tdStyle
	 *            列样式
	 * @return
	 */
	private String genNonFixedHeadBody(List tableCols,
			RptOlapFuncStruct olapFun, String statPeriod, String trStyle,
			String tdStyle) throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size() || null == olapFun
				|| null == statPeriod)
			throw new ReportOlapException("生成非固定表头的表体部分时输入的参数为空");
		String head = "";
		StringBuffer firRow = new StringBuffer("");
		StringBuffer firExpRow = new StringBuffer("");

		StringBuffer secRow = new StringBuffer("");
		StringBuffer secExpRow = new StringBuffer("");

		boolean hasRowSpan = false;

		firRow.append("<tr class=\"").append(trStyle).append("\">\n");
		if ((RptOlapConsts.OLAP_FUN_DATA.equals(olapFun.getCurFunc()) && olapFun
				.isHasSum())
				|| RptOlapConsts.OLAP_FUN_SAME.equals(olapFun.getCurFunc())
				|| RptOlapConsts.OLAP_FUN_LAST.equals(olapFun.getCurFunc())) {
			hasRowSpan = true;
			secRow.append("<tr class=\"").append(trStyle).append("\">\n");
		}
		int dimRowSpan = 1;
		if (hasRowSpan)
			dimRowSpan++;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay()) {
				if (tCol.isDim()) {
					// 维度不需要排序了，但需要收缩
					List list = RptOlapDimUtil.genDimHead(tableCols, tCol,
							olapFun, dimRowSpan, tdStyle);
					StringBuffer dimHead = (StringBuffer) list.get(0);
					StringBuffer dimExpHead = (StringBuffer) list.get(1);
					firRow.append(dimHead);
					if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
							.getDigLevel())) {
						firExpRow.append(dimExpHead);
					}
				} else {
					// 指标可能钻取、排序
					List right = RptOlapMsuUtil.genMsuHead(tableCols, tCol,
							olapFun, statPeriod, hasRowSpan, tdStyle);
					StringBuffer right1 = (StringBuffer) right.get(0);
					StringBuffer right2 = (StringBuffer) right.get(1);
					StringBuffer exportRight1 = (StringBuffer) right.get(2);
					StringBuffer exportRight2 = (StringBuffer) right.get(3);
					firRow.append(right1);
					firExpRow.append(exportRight1);
					if (null != right2 && right2.length() > 0) {
						secRow.append(right2);
					}
					if (null != exportRight2 && exportRight2.length() > 0) {
						secExpRow.append(exportRight2);
					}
				}
			}
		}

		firRow.append("</tr>\n");
		if (secRow.length() > 0) {
			secRow.append("</tr>\n");
			firRow.append(secRow);
		}
		if (secExpRow.length() > 0) {
			secExpRow.append("</tr>\n");
			firExpRow.append(secExpRow);
		}
		exportHead.clear();
		exportHead.add(firExpRow.toString());
		head = firRow.toString();
		return head;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.dao.ITableHeadDao#genExpandHTMLTableHead(java
	 * .util.List, com.asiabi.olap.domain.RptOlapFunc, java.lang.String,
	 * boolean, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	public List genExpandHTMLTableHead(List tableCols,
			RptOlapFuncStruct olapFun, String statPeriod, boolean fixedHead,
			String trStyle, String tdStyle, String level, boolean isSingleDim)
			throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size() || null == olapFun
				|| null == statPeriod || null == level)
			throw new ReportOlapException("生成折叠扩展表头的表体时输入的参数为空");

		List head = new ArrayList();
		String trClass = trStyle;
		String tdClass = tdStyle;
		if (null == trClass)
			trClass = "";
		if (null == tdClass)
			tdClass = "";

		if (fixedHead) {
			StringBuffer left = new StringBuffer("");
			StringBuffer right = new StringBuffer("");
			String leftPart = RptOlapStringUtil.genFixedHeadLeftPart(trStyle,
					tdStyle);
			String rightPart = RptOlapStringUtil.genFixedHeadRightPart(trStyle,
					tdStyle);
			List list = getFixedExpandHead(tableCols, olapFun, statPeriod,
					trClass, tdClass, level, isSingleDim);
			if (null != list && list.size() == 2) {
				String[] aryStr = (String[]) list.toArray(new String[list
						.size()]);
				leftPart = leftPart.replaceAll("::leftBody::", aryStr[0]);
				rightPart = rightPart.replaceAll("::rightBody::", aryStr[1]);
			}
			left.append(leftPart);
			right.append(rightPart);
			left.append(right);
			head.add(left.toString());
		} else {
			head.add(getNonFixedExpandHead(tableCols, olapFun, statPeriod,
					trClass, tdClass, level, isSingleDim));
		}
		return head;
	}

	/**
	 * 生成非固定折叠、展开的表头
	 *
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param trClass
	 *            行样式
	 * @param tdClass
	 *            列样式
	 * @param level
	 *            当前层次
	 * @param singleDim
	 *            是否是单维度
	 * @return
	 */
	private String getNonFixedExpandHead(List tableCols,
			RptOlapFuncStruct olapFun, String statPeriod, String trClass,
			String tdClass, String level, boolean isSingleDim)
			throws ReportOlapException {
		if (null == tableCols || tableCols.size() <= 0 || null == olapFun
				|| null == statPeriod || "".equals(statPeriod) || null == level)
			throw new ReportOlapException("生成非固定折叠、展开表头时输入的参数为空");

		String head = "";
		StringBuffer firRow = new StringBuffer("");
		StringBuffer secRow = new StringBuffer("");

		StringBuffer firExpRow = new StringBuffer("");
		StringBuffer secExpRow = new StringBuffer("");

		firRow.append("<tr class=\"").append(trClass).append("\">\n");
		firExpRow.append("<tr>\n");
		boolean hasRowSpan = false;

		if ((RptOlapConsts.OLAP_FUN_DATA.equals(olapFun.getCurFunc()) && olapFun
				.isHasSum())
				|| RptOlapConsts.OLAP_FUN_SAME.equals(olapFun.getCurFunc())
				|| RptOlapConsts.OLAP_FUN_LAST.equals(olapFun.getCurFunc())) {
			hasRowSpan = true;
			secRow.append("<tr class=\"").append(trClass).append("\">\n");
			secExpRow.append("<tr>\n");
		}

		//int dimRowSpan = 1;
		//if (hasRowSpan)
		//	dimRowSpan++;
		// 计算器
		int count = Integer.parseInt(level);
		int dimIndex = 0;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay()) {
				if (tCol.isDim()) {
					RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
					// 维度
					String tdContent = "";
					if (isSingleDim) {
						if (tCol.isTimeDim()) {
							tdContent = RptOlapDimUtil.getTimeLevelName(level,
									tCol);
						} else {
							tdContent = RptOlapDimUtil.getDimCurLevel(level,
									rptDim.dimInfo.dim_levels).lvl_name;
						}
						firRow.append("<td nowrap class=\"").append(tdClass)
								.append("\">");
						firRow.append(tdContent);
						firRow.append("</td>");
						firExpRow.append("<td nowrap>");
						firExpRow.append(tdContent);
						firExpRow.append("</td>");
					} else {
						if (dimIndex == count) {
							tdContent = rptDim.dimInfo.dim_name;
							firRow.append("<td nowrap class=\"")
									.append(tdClass).append("\">");
							firRow.append(tdContent);
							firRow.append("</td>");
							firExpRow.append("<td nowrap>");
							firExpRow.append(tdContent);
							firExpRow.append("</td>");
						}
					}
					dimIndex++;
				} else {
					// 指标
					List right = RptOlapMsuUtil.genMsuHead(tableCols, tCol,
							olapFun, statPeriod, hasRowSpan, tdClass);
					StringBuffer right1 = (StringBuffer) right.get(0);
					StringBuffer right2 = (StringBuffer) right.get(1);
					StringBuffer rightExp1 = (StringBuffer) right.get(2);
					StringBuffer rightExp2 = (StringBuffer) right.get(3);
					firRow.append(right1);
					firExpRow.append(rightExp1);
					if (right2.length() > 0) {
						secRow.append(right2);
					}
					if (rightExp2.length() > 0) {
						secExpRow.append(rightExp2);
					}
				}
			}
		}
		firRow.append("</tr>");
		if (null != secRow && secRow.length() > 0) {
			secRow.append("</tr>");
			firRow.append(secRow);
		}
		if (null != secExpRow && secExpRow.length() > 0) {
			secExpRow.append("</tr>");
			firExpRow.append(secExpRow);
		}
		exportHead.clear();
		exportHead.add(firExpRow.toString());
		head = firRow.toString();
		return head;
	}

	/**
	 * 生成固定表头的折叠、展开表头
	 *
	 * @param tableCols
	 *            列域对象
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param trClass
	 *            行样式
	 * @param tdClass
	 *            列样式
	 * @param level
	 *            当前层次
	 * @param singleDim
	 *            是否为单维度
	 * @return
	 */
	private List getFixedExpandHead(List tableCols, RptOlapFuncStruct olapFun,
			String statPeriod, String trClass, String tdClass, String level,
			boolean isSingleDim) throws ReportOlapException {
		if (null == tableCols || tableCols.size() <= 0 || null == olapFun
				|| null == statPeriod || "".equals(statPeriod))
			throw new ReportOlapException("生成固定折叠、展开表头时输入参数为空");

		List head = new ArrayList();
		StringBuffer left = new StringBuffer("");
		StringBuffer rightFirRow = new StringBuffer("");
		StringBuffer rightSecRow = new StringBuffer("");

		StringBuffer firExpRow = new StringBuffer("");
		StringBuffer secExpRow = new StringBuffer("");

		left.append("<tr class=\"").append(trClass).append("\">\n");
		rightFirRow.append("<tr class=\"").append(trClass).append("\">\n");
		firExpRow.append("<tr>\n");

		String func = olapFun.getCurFunc();
		boolean hasRowSpan = false;
		if ((RptOlapConsts.OLAP_FUN_DATA.equals(func) && olapFun.isHasSum())
				|| RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
				|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
				|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
			hasRowSpan = true;
			rightSecRow.append("<tr class=\"").append(trClass).append("\">\n");
			secExpRow.append("<tr>\n");
		}
		int dimRowSpan = 1;
		if (hasRowSpan)
			dimRowSpan++;
		int count = Integer.parseInt(level);
		int dimIndex = 0;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay()) {
				if (tCol.isDim()) {
					RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
					if (isSingleDim) {
						String content = "";
						if (tCol.isTimeDim()) {
							content = RptOlapDimUtil.getTimeLevelName(level,
									tCol);
						} else {
							content = rptDim.dimInfo.dim_name;
						}
						// 维度不需要排序了，但需要收缩
						left.append("<td rowspan=\"").append(dimRowSpan)
								.append("\" nowrap align=\"left\" class=\"")
								.append(tdClass).append("\">\n");
						left.append(content);
						left.append("</td>");
						tCol.setColHeadHTML1(content);
						firExpRow.append("<td rowspan=\"").append(dimRowSpan)
								.append("\" nowrap align=\"left\">\n");
						firExpRow.append(content);
						firExpRow.append("</td>");
					} else {
						if (dimIndex == count) {
							String content = rptDim.dimInfo.dim_name;
							left.append("<td rowspan=\"")
									.append(dimRowSpan)
									.append("\" nowrap align=\"left\" class=\"")
									.append(tdClass).append("\">\n");
							left.append(content);
							left.append("</td>");
							tCol.setColHeadHTML1(content);
							firExpRow.append("<td rowspan=\"")
									.append(dimRowSpan)
									.append("\" nowrap align=\"left\">\n");
							firExpRow.append(content);
							firExpRow.append("</td>");
						}
					}
					dimIndex++;
				} else {
					// 指标可能钻取、排序
					List right = RptOlapMsuUtil.genMsuHead(tableCols, tCol,
							olapFun, statPeriod, hasRowSpan, tdClass);
					StringBuffer right1 = (StringBuffer) right.get(0);
					StringBuffer right2 = (StringBuffer) right.get(1);
					StringBuffer rightExp1 = (StringBuffer) right.get(2);
					StringBuffer rightExp2 = (StringBuffer) right.get(3);
					rightFirRow.append(right1);
					firExpRow.append(rightExp1);
					if (null != right2 && right2.length() > 0) {
						rightSecRow.append(right2);
					}
					if (null != rightExp2 && rightExp2.length() > 0) {
						secExpRow.append(rightExp2);
					}
				}
			}
		}
		left.append("</tr>\n");
		firExpRow.append("</tr>\n");
		rightFirRow.append("</tr>\n");
		if (rightSecRow.length() > 0) {
			rightSecRow.append("</tr>\n");
			rightFirRow.append(rightSecRow);
		}
		if (secExpRow.length() > 0) {
			secExpRow.append("</tr>\n");
			firExpRow.append(secExpRow);
		}
		head.add(left.toString());
		head.add(rightFirRow.toString());
		exportHead.clear();
		exportHead.add(firExpRow.toString());
		return head;
	}

	public List getExportHead() {
		return exportHead;
	}
}
