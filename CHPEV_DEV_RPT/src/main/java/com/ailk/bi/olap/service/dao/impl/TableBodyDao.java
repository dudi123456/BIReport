package com.ailk.bi.olap.service.dao.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.base.util.NullProcFactory;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapRowStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;
import com.ailk.bi.olap.service.dao.ITableBodyDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDimUtil;
import com.ailk.bi.olap.util.RptOlapStringUtil;
import com.ailk.bi.olap.util.RptOlapTabDomainUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TableBodyDao implements ITableBodyDao {
	/**
	 * 导出的表体
	 */
	private List exportBody = new ArrayList();

	/**
	 * 以前的导出的行结构列表
	 */
	private List preExpBody = new ArrayList();

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.dao.ITableBodyDao#getTableBody(java.lang.String
	 * [][], java.util.List, com.asiabi.olap.domain.RptOlapFunc,
	 * java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	public List getTableBody(String[][] svces, PubInfoResourceTable report,
			List tableCols, RptOlapFuncStruct olapFun, boolean fixedHead,
			String trStyle, String tdStyle) throws ReportOlapException {
		List body = null;
		// 考虑返回字符串数组加快速度，还有加、减号方式
		if (null == tableCols || null == report || null == olapFun
				|| null == svces || svces.length <= 1) {
			// 如果没有结果，返回NULL
			throw new ReportOlapException("生成表格表体时输入的参数为空");
		}
		long startTime = System.currentTimeMillis();
		body = new ArrayList();
		if (fixedHead) {
			List left = new ArrayList();
			List right = new ArrayList();
			left.add(RptOlapStringUtil.genFixedBodyLeftPart1(trStyle, tdStyle));
			right.add(RptOlapStringUtil
					.genFixedBodyRightPart1(trStyle, tdStyle));
			Map tmpBody = genFixedTableBody(svces, report.rpt_id, tableCols,
					olapFun, report.cycle, trStyle, tdStyle);
			left.addAll((List) tmpBody.get("left"));
			right.addAll((List) tmpBody.get("right"));
			left.add(RptOlapStringUtil.genFixedBodyLeftPart2());
			right.add(RptOlapStringUtil.genFixedBodyRightPart2());
			left.addAll(right);
			body.addAll(left);
		} else {
			body = genNonFixedTableBody(svces, report.rpt_id, tableCols,
					olapFun, report.cycle, trStyle, tdStyle);
		}
		System.out.println("组装表体用时:" + (System.currentTimeMillis() - startTime)
				+ "ms");
		return body;
	}

	/**
	 * 生成固定表头的表体
	 *
	 * @param svces
	 *            表体内容
	 * @param tableCols
	 *            列域对象
	 * @param olapFun
	 *            当前功能对象
	 * @param statPeriod
	 *            统计周期
	 * @param trClass
	 *            行对象
	 * @param tdClass
	 *            列对象
	 * @return 返回左右两部分的MAP集合
	 */
	private Map genFixedTableBody(String[][] svces, String reportId,
			List tableCols, RptOlapFuncStruct olapFun, String statPeriod,
			String trClass, String tdClass) throws ReportOlapException {
		if (null == svces || svces.length <= 0 || null == tableCols
				|| tableCols.size() <= 0 || null == olapFun
				|| null == statPeriod || null == reportId)
			throw new ReportOlapException("生成固定表体时输入的参数为空");
		Map body = new HashMap();
		List left = new ArrayList();
		List right = new ArrayList();

		boolean toUserLevel = olapFun.isToUserLevel();
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;

		if (olapFun.isHasAlert() && olapFun.isHasSameRatioAlert())
			hasSameAlert = true;
		if (olapFun.isHasAlert() && olapFun.isHasLastRatioAlert())
			hasLastAlert = true;

		StringBuffer leftPart = new StringBuffer("");
		StringBuffer rightPart = new StringBuffer("");
		StringBuffer export = new StringBuffer("");
		StringBuffer sumExport = new StringBuffer("");
		StringBuffer sumRightPart = new StringBuffer("");

		StringBuffer userWhere = new StringBuffer("");

		RptOlapTableColumnStruct[] cols = (RptOlapTableColumnStruct[]) tableCols
				.toArray(new RptOlapTableColumnStruct[tableCols.size()]);

		sumRightPart.append("<tr class=\"")
				.append(RptOlapConsts.TABLE_SUM_TR_CLASS).append("\">\n");

		int dimCount = 0;
		for (int i = 0; i < svces.length - 1; i++) {
			// 重新声明变量
			StringBuffer link = new StringBuffer("");
			// 清空链接条件
			userWhere.delete(0, userWhere.length());
			// 生成基本的链接条件
			link.append(RptOlapConsts.OLAP_DIG_ACTION);
			link.append("?report_id=::report_id::").append(
					RptOlapTabDomainUtil.convertTableColStructToUrl(tableCols,
							olapFun));

			leftPart.append("<tr nowrap class=\"").append(trClass)
					.append("\">");
			rightPart.append("<tr nowrap class=\"").append(trClass)
					.append("\">");
			export.append("<tr nowrap>");

			int dimIndex = 0;
			int sumColspan = 0;
			int msuIndex = 0;
			boolean firstMsu = true;
			for (int j = 0; j < cols.length; j++) {
				RptOlapTableColumnStruct tCol = cols[j];
				if (tCol.isDisplay()) {
					if (tCol.isDim()) {
						List result = genDimCellContent(dimIndex, toUserLevel,
								tCol, svces[i], false, false);
						String title = (String) result.get(1);
						leftPart.append("<td nowrap align=\"left\" class=\"")
								.append(tdClass).append("\"");
						leftPart.append(" title=\"").append(title).append("\"");
						leftPart.append(">");
						String html = tCol.getColHeadHTML1();

						String content = (String) result.get(0);
						String spaces = RptOlapStringUtil.genStringWithSpace(
								html, content);
						leftPart.append(content);
						leftPart.append(spaces);
						leftPart.append("</td>");
						if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
								.getDigLevel())) {
							export.append("<td nowrap>");
							export.append(genDimCellExportContent(dimIndex,
									toUserLevel, tCol, svces[i], false));
							export.append("</td>");
							sumColspan++;
						}
						if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
								.getDigLevel())) {
							int realIndex = 2 * dimIndex;
							// 这里还得去重
							String input = userWhere.toString();
							String regular = "(.*?)(&"
									+ tCol.getColFld().toLowerCase()
									+ "=\\w*)(.*?)";
							String replace = "$1$3";
							input = input.replaceAll(regular, replace);
							userWhere.delete(0, userWhere.length());
							userWhere.append(input);
							userWhere.append("&").append(tCol.getColFld())
									.append("=").append(svces[i][realIndex]);
							input = link.toString();
							regular = "(.*?)(&dim_" + tCol.getIndex()
									+ "=\\w*)(.*?)";
							replace = "$1$3";
							input = input.replaceAll(regular, replace);
							link.delete(0, link.length());
							link.append(input);
							link.append("&dim_").append(tCol.getIndex())
									.append("=").append(svces[i][realIndex]);
						}
						dimIndex++;
					} else {
						RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol
								.getStruct();
						List list = genMsuBody(tCol, olapFun, rptMsu,
								statPeriod, hasSameAlert, hasLastAlert, i,
								svces, firstMsu, dimCount, dimIndex, msuIndex,
								toUserLevel, tdClass);
						dimCount = ((Integer) list.get(0)).intValue();
						firstMsu = ((Boolean) list.get(1)).booleanValue();
						msuIndex = ((Integer) list.get(2)).intValue();
						rightPart.append((StringBuffer) list.get(3));
						sumRightPart.append((StringBuffer) list.get(4));
						export.append((StringBuffer) list.get(5));
						sumExport.append((StringBuffer) list.get(6));
					}
				}
			}
			leftPart.append("</tr>");
			rightPart.append("</tr>");
			export.append("</tr>");
			// 加上合计部分
			if (i == svces.length - 2) {
				leftPart.append("<tr class=\"")
						.append(RptOlapConsts.TABLE_SUM_TR_CLASS)
						.append("\">\n");
				leftPart.append("<td nowrap align=\"center\" colspan=\"");
				leftPart.append(dimIndex).append("\" class=\"").append(tdClass)
						.append("\">");
				leftPart.append("<strong>合计</strong>");
				leftPart.append("</td>");
				leftPart.append("</tr>\n");

				String tmp = sumExport.toString();
				sumExport.delete(0, sumExport.length());
				sumExport.append("<tr>\n");
				sumExport.append("<td nowrap align=\"center\" colspan=\"");
				sumExport.append(sumColspan).append("\">");
				sumExport.append("<strong>合计</strong>");
				sumExport.append("</td>");
				sumExport.append(tmp);
				sumExport.append("</tr>\n");
				// 右侧也应该加上合计部分
				sumRightPart.append("</tr>\n");
				rightPart.append(sumRightPart);
				export.append(sumExport);
			}
			// 替换相应的链接
			String sTmp = leftPart.toString();
			sTmp = sTmp.replaceAll("::LINK::", link.toString());
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			sTmp = sTmp.replaceAll("::report_id::", reportId);
			left.add(sTmp);
			sTmp = rightPart.toString();
			sTmp = sTmp.replaceAll("::LINK::", link.toString());
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			sTmp = sTmp.replaceAll("::report_id::", reportId);
			right.add(sTmp);
			leftPart.delete(0, leftPart.length());
			rightPart.delete(0, rightPart.length());
			exportBody.add(export.toString());
			export.delete(0, export.length());
		}
		StringBuffer sb = new StringBuffer("");
		// 为了固定表头，需要在左侧部分增加一行什么也没有的行
		sb.append("<tr>\n");
		for (int i = 0; i < dimCount; i++) {
			sb.append("<td>&nbsp;</td>");
		}
		sb.append("</tr>\n");

		left.add(sb.toString());
		body.put("left", left);
		body.put("right", right);
		return body;
	}

	/**
	 * 生成非固定表头的表体
	 *
	 * @param svces
	 *            表格内容
	 * @param tableCols
	 *            列域对象列表
	 * @param olapFun
	 *            当前功能对象
	 * @param trClass
	 *            行样式
	 * @param tdClass
	 *            列样式
	 * @return 表格的字符串列表
	 * @throws ReportOlapException
	 */
	private List genNonFixedTableBody(String[][] svces, String reportId,
			List tableCols, RptOlapFuncStruct olapFun, String statPeriod,
			String trClass, String tdClass) throws ReportOlapException {
		if (null == svces || svces.length <= 0 || null == tableCols
				|| tableCols.size() <= 0 || null == olapFun
				|| null == statPeriod || "".equals(statPeriod))
			throw new ReportOlapException("生成固定表体时输入的参数为空");
		List lBody = new ArrayList();

		StringBuffer body = new StringBuffer("");
		StringBuffer sumBody = new StringBuffer("");
		StringBuffer export = new StringBuffer("");
		StringBuffer sumExport = new StringBuffer("");

		boolean toUserLevel = olapFun.isToUserLevel();
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;

		if (olapFun.isHasAlert() && olapFun.isHasSameRatioAlert())
			hasSameAlert = true;
		if (olapFun.isHasAlert() && olapFun.isHasLastRatioAlert())
			hasLastAlert = true;
		String link = RptOlapTabDomainUtil.convertTableColStructToUrl(
				tableCols, olapFun);

		StringBuffer userWhere = new StringBuffer("");
		RptOlapTableColumnStruct[] cols = (RptOlapTableColumnStruct[]) tableCols
				.toArray(new RptOlapTableColumnStruct[tableCols.size()]);

		int dimCount = 0;
		for (int i = 0; i < svces.length - 1; i++) {
			// 初始化
			body.delete(0, body.length());
			export.delete(0, export.length());
			body.append("<tr nowrap class=\"").append(trClass).append("\">");
			export.append("<tr nowrap>");
			int sumColspan = 0;
			int dimIndex = 0;
			int msuIndex = 0;
			boolean firstMsu = true;
			for (int j = 0; j < cols.length; j++) {
				RptOlapTableColumnStruct tCol = cols[j];
				if (tCol.isDisplay()) {
					if (tCol.isDim()) {
						body.append("<td nowrap class=\"").append(tdClass)
								.append("\">");
						body.append(genDimCellContent(dimIndex, toUserLevel,
								tCol, svces[i], false, false));
						body.append("</td>");
						if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
								.getDigLevel())) {
							export.append("<td nowrap>");
							export.append(genDimCellExportContent(dimIndex,
									toUserLevel, tCol, svces[i], false));
							export.append("</td>");
							sumColspan++;
						}
						if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
								.getDigLevel())) {
							int realIndex = 2 * dimIndex;
							userWhere.append("&").append(tCol.getColFld())
									.append("=").append(svces[realIndex]);
							link += "&dim_" + tCol.getIndex() + "="
									+ svces[realIndex];
						}
						dimIndex++;
					} else {
						RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol
								.getStruct();
						List list = genMsuBody(tCol, olapFun, rptMsu,
								statPeriod, hasSameAlert, hasLastAlert, i,
								svces, firstMsu, dimCount, dimIndex, msuIndex,
								toUserLevel, tdClass);
						dimCount = ((Integer) list.get(0)).intValue();
						firstMsu = ((Boolean) list.get(1)).booleanValue();
						msuIndex = ((Integer) list.get(2)).intValue();
						body.append((StringBuffer) list.get(3));
						sumBody.append((StringBuffer) list.get(4));
						export.append((StringBuffer) list.get(5));
						sumExport.append((StringBuffer) list.get(6));
					}
				}
			}
			body.append("</tr>");
			// 加上合计部分
			if (i == svces.length - 2) {
				String tmp = sumBody.toString();
				sumBody.delete(0, sumBody.length());
				sumBody.append("<tr class=\"")
						.append(RptOlapConsts.TABLE_SUM_TR_CLASS)
						.append("\">\n");
				sumBody.append("<td nowrap align=\"center\" colspan=\"");
				sumBody.append(dimIndex).append("\" class=\"").append(tdClass)
						.append("\">");
				sumBody.append("<strong>合计</strong>");
				sumBody.append("</td>");
				sumBody.append(tmp);
				sumBody.append("</tr>\n");

				tmp = sumExport.toString();
				sumExport.delete(0, sumExport.length());
				sumExport.append("<tr>\n");
				sumExport.append("<td nowrap align=\"center\" colspan=\"");
				sumExport.append(sumColspan).append("\">");
				sumExport.append("<strong>合计</strong>");
				sumExport.append("</td>");
				sumExport.append(tmp);
				sumExport.append("</tr>\n");
				// 右侧也应该加上合计部分
				body.append(sumBody);
				export.append(sumExport);
			}
			// 替换相应的链接
			String sTmp = body.toString();
			sTmp = sTmp.replaceAll("::LINK::", link);
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			sTmp = sTmp.replaceAll("::report_id::", reportId);
			lBody.add(sTmp);
			exportBody.add(export.toString());
		}
		// 循环完毕
		// 合计部分
		return lBody;
	}

	/**
	 * 生成维度的单元格内容
	 *
	 * @param index
	 *            数组索引
	 * @param toUserLevel
	 *            是否到用户级
	 * @param tCol
	 *            列域对象
	 * @param svces
	 *            表格内容
	 * @param addBlank
	 *            是否添加空格
	 * @return 返回单元格HTML
	 */
	private List genDimCellContent(int index, boolean toUserLevel,
			RptOlapTableColumnStruct tCol, String[] svces, boolean addBlank,
			boolean expand) throws ReportOlapException {
		if (null == tCol || null == svces || svces.length <= 0)
			throw new ReportOlapException("生成单元格内容时输入的参数为空");
		List list = new ArrayList();
		StringBuffer title = new StringBuffer();
		StringBuffer content = new StringBuffer();

		if (tCol.isDim()) {
			RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
			// 考虑没有展开、第0层、以及没有再钻取层
			String level = tCol.getDigLevel();
			if (RptOlapConsts.NO_NEXT_LEVEL.equals(level)
					|| rptDim.max_level.equals(level) || !tCol.isNeedDig()) {
				// 最后一层了,不能钻取了
				int realIndex = 2 * index + 1;
				String dimValue = svces[realIndex];

				dimValue = RptOlapStringUtil.addHtmlBr(dimValue);
				content.append(dimValue);
				title.append(svces[realIndex]);
			} else if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level)) {
				// 还没有钻取
				String url = "::LINK::&" + RptOlapConsts.REQ_CLICK_DIM + "="
						+ tCol.getIndex();
				if (!expand)
					content.append("<a href=\"javascript:")
							.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION)
							.append("('").append(url).append("')\" class=\"")
							.append(RptOlapConsts.HREF_LINK_CLASS)
							.append("\">").append(tCol.getColName())
							.append("</a>");
				else
					content.append(tCol.getColName());
			} else {
				// 还能钻取
				int realIndex = 2 * index + 1;
				String url = "::LINK::&click_dim=" + tCol.getIndex();
				if (!expand) {
					content.append("<a href=\"javascript:")
							.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION)
							.append("('").append(url).append("')\" class=\"")
							.append(RptOlapConsts.HREF_LINK_CLASS);
					String dimValue = svces[realIndex];
					dimValue = RptOlapStringUtil.addHtmlBr(dimValue);
					content.append("\">").append(dimValue).append("</a>");
					title.append(svces[realIndex]);
				} else {
					String dimValue = svces[realIndex];
					dimValue = RptOlapStringUtil.addHtmlBr(dimValue);
					content.append(dimValue);
					title.append(svces[realIndex]);
				}
			}
		}
		list.add(content.toString());
		list.add(title.toString());
		return list;
	}

	private String genDimCellExportContent(int index, boolean toUserLevel,
			RptOlapTableColumnStruct tCol, String[] svces, boolean addBlank)
			throws ReportOlapException {
		if (null == tCol || null == svces || svces.length <= 0)
			throw new ReportOlapException("生成单元格内容时输入的参数为空");

		StringBuffer content = new StringBuffer("");

		if (tCol.isDim()) {
			RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
			// 考虑没有展开、第0层、以及没有再钻取层
			String level = tCol.getDigLevel();
			if (RptOlapConsts.NO_NEXT_LEVEL.equals(level)
					|| rptDim.max_level.equals(level)) {
				// 最后一层了,不能钻取了
				int realIndex = 2 * index + 1;
				content.append(svces[realIndex]);
			} else if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level)) {
				// 还没有钻取
				content.append(tCol.getColName());
			} else {
				// 还能钻取
				int realIndex = 2 * index + 1;
				content.append(svces[realIndex]);
			}
		}
		return content.toString();
	}

	/**
	 * @param index
	 * @param tCol
	 * @param svces
	 * @param olapFun
	 * @param curLevel
	 * @return
	 * @throws ReportOlapException
	 */
	private String genDimExpandLink(int index, RptOlapTableColumnStruct tCol,
			String[] svces, RptOlapFuncStruct olapFun, String curLevel,
			boolean addSpaces) throws ReportOlapException {
		if (null == tCol || null == olapFun || null == svces
				|| svces.length <= 0)
			throw new ReportOlapException("生成单元格内容链接时输入的参数为空");

		StringBuffer content = new StringBuffer("");
		if (addSpaces)
			content.append("&nbsp;&nbsp;");
		if (tCol.isDim()) {
			// 考虑没有展开、第0层、以及没有再钻取层
			// 还没有钻取
			int level = olapFun.getCurExpandLevel();
			// 这里好像是单维度多层次展开,对于多维度不合适
			if (level < olapFun.getMaxExpandLevel()) {
				for (int i = 1; i <= level; i++) {
					content.append("&nbsp;&nbsp;");
				}
				// 需要每次带全所有的值
				String url = "::LINK::&" + RptOlapConsts.REQ_EXPAND_VALUE
						+ level + "=" + svces[index];
				if (tCol.isTimeDim() && olapFun.isSingleDimExpand()) {
					level = Integer.parseInt(RptOlapDimUtil
							.getDimTimeNextLevel(tCol));
				} else
					level++;
				url += "&" + RptOlapConsts.REQ_EXPAND_LEVEL + "=" + level;
				url += "&" + RptOlapConsts.REQ_OLAP_FUNC + "=::"
						+ RptOlapConsts.REQ_OLAP_FUNC + "::";
				content.append("<a href=\"javascript:")
						.append(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION)
						.append("('").append(url).append("')\" class=\"")
						.append(RptOlapConsts.HREF_LINK_CLASS).append("\">")
						.append(RptOlapConsts.IMG_EXPAND).append("</a>");
			} else {
				for (int i = 1; i <= level; i++) {
					content.append("&nbsp;&nbsp;");
				}
			}
		}
		return content.toString();
	}

	/**
	 * 生成指标的单元格内容
	 *
	 * @param toUserLevel
	 *            是否到用户级
	 * @param tCol
	 *            列域对象
	 * @param svces
	 *            单元格内容
	 * @param addBlank
	 *            是否加空格
	 * @param isPercent
	 *            是否是百分比
	 * @return 单元格HTML
	 */
	private String genMsuCellContent(boolean toUserLevel,
			RptOlapTableColumnStruct tCol, String svces, boolean addBlank,
			boolean isPercent) throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("生成指标单元格内容时输入的参数为空");

		StringBuffer content = new StringBuffer("");

		if (!tCol.isDim()) {
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
			if (!toUserLevel
					|| RptOlapConsts.NO.equalsIgnoreCase(rptMsu.has_link)) {
				// 能够到用户级
				content.append(formartMsu(tCol, svces, isPercent));
			} else {
				String url = rptMsu.link_url + "::USERLINK::";
				content.append("<a href=\"").append(url).append("\"");
				if (null != rptMsu.link_target && "".equals(rptMsu.link_target))
					content.append(" target=\"").append(rptMsu.link_target)
							.append("\" class=\"")
							.append(RptOlapConsts.HREF_LINK_CLASS).append("\"");
				content.append(">");
				content.append(formartMsu(tCol, svces, isPercent)).append(
						"</a>");
			}
		}
		return content.toString();
	}

	/**
	 * 将指标进行格式化输出
	 *
	 * @param tCol
	 *            列域对象
	 * @param value
	 *            要格式化的值
	 * @param isPercent
	 *            是否是百分比
	 * @return 格式化后的字符串
	 */
	private String formartMsu(RptOlapTableColumnStruct tCol, String value,
			boolean isPercent) {
		String retStr = null;
		RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
		String unit = rptMsu.msuInfo.msu_unit;
		if (null == unit)
			unit = "";
		String precision = rptMsu.msuInfo.pricesion;
		if (null == precision || "".equals(precision))
			precision = RptOlapConsts.ZERO_STR;
		int fractionNum = 2;
		try {
			fractionNum = Integer.parseInt(precision);
		} catch (NumberFormatException nfe) {
			fractionNum = 2;
		}
		if (!unit.equals(RptOlapConsts.UNIT_PECENT) && !isPercent) {
			retStr = NullProcFactory.transNullToEmptyString(FormatUtil
					.formatStr(value, fractionNum, true));
		} else {
			retStr = NullProcFactory.transNullToEmptyString(FormatUtil
					.formatPercent(value, 2, true));
		}

		return retStr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.olap.service.dao.ITableBodyDao#getExpandTableBody(java.lang
	 * .String[][], java.util.List, com.asiabi.olap.domain.RptOlapFunc,
	 * java.lang.String, boolean, java.lang.String, java.lang.String,
	 * java.lang.String, boolean)
	 */
	public List getExpandTableBody(List preBody, String[][] svces,
			PubInfoResourceTable report, List tableCols,
			RptOlapFuncStruct olapFun, boolean fixedHead, String trStyle,
			String tdStyle, String level, boolean singleDim)
			throws ReportOlapException {
		List body = null;
		// 考虑返回字符串数组加快速度，还有加、减号方式
		if (null == tableCols || null == olapFun || null == report)
			throw new IllegalArgumentException("生成表格表体时输入的参数为空");
		if (null == svces || svces.length <= 1) {
			// 如果没有结果，返回NULL
			throw new ReportOlapException("生成表格HTML时数组内容为空");
		}
		body = new ArrayList();
		if (fixedHead) {
			StringBuffer left = new StringBuffer("");
			StringBuffer right = new StringBuffer("");
			left.append(RptOlapStringUtil.genFixedBodyLeftPart1(trStyle,
					tdStyle));
			right.append(RptOlapStringUtil.genFixedBodyRightPart1(trStyle,
					tdStyle));
			// 加上头部
			RptOlapRowStruct headRow = new RptOlapRowStruct();
			headRow.setRow_id(null);
			headRow.setLeftPart(left.toString());
			headRow.setRightPart(right.toString());
			if (olapFun.isFirstExpand()
					&& RptOlapConsts.OLAP_FUN_DATA.equals(olapFun.getCurFunc())) {
				// 展开指标，清除以前的表体
				preBody = null;
			}
			// 当前表体表头
			if (olapFun.isFirstExpand())
				body.add(headRow);
			if (olapFun.isHasNewExpand() || null != olapFun.getSort()) {
				// 排序时重新生成行结构列表
				List tmpBody = genFixedExpandTableBody(svces, report.rpt_id,
						tableCols, olapFun, report.cycle, trStyle, tdStyle,
						level);
				// body应该时最新的，经过排序的
				body.addAll(tmpBody);
			} else {
				return preBody;
			}
			// 加上尾部的
			left.delete(0, left.length());
			right.delete(0, right.length());
			left.append(RptOlapStringUtil.genFixedBodyLeftPart2());
			right.append(RptOlapStringUtil.genFixedBodyRightPart2());
			RptOlapRowStruct endRow = new RptOlapRowStruct();
			endRow.setRow_id(null);
			endRow.setLeftPart(left.toString());
			endRow.setRightPart(right.toString());
			if (olapFun.isFirstExpand())
				body.add(endRow);

			// 合并以前的部分
			if (null == preBody || 0 >= preBody.size()) {
				// 此处需要设置以前的表体和导出部分
				preExpBody.addAll(exportBody);
				assembleExport(olapFun);
				return body;
			} else {
				if (null == olapFun.getCollapseRowId()) {
					// 找到当前行的行号，插入，并改变当前行的展开为折叠
					String rowId = null;
					// 生成行号
					String curLevel = olapFun.getCurExpandLevel() + "";
					Map values = olapFun.getExpandedValues();
					String preLevel = RptOlapDimUtil.getExpandPreLevel(
							curLevel, tableCols);
					// 这里也得考虑时间维
					if (null != values && null != values.get(preLevel)) {
						rowId = "level_" + preLevel + "_dim";
						// + (String) values.get(preLevel);
						List lKeys = new ArrayList();
						Iterator iter = values.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							String key = (String) entry.getKey();
							lKeys.add(key);
						}
						String[] keys = (String[]) lKeys
								.toArray(new String[lKeys.size()]);
						Arrays.sort(keys);
						for (int i = 0; i < keys.length; i++) {
							String value = (String) values.get(keys[i]);
							if (null != value) {
								rowId += "_" + value;
							}
						}
						// 这里如果是排序呢，先合并掉以前，再添加新的
						if (null != olapFun.getSort()) {
							// 先收缩
							List list = collapseExpand(preBody, rowId);
							preBody.clear();
							preBody.addAll(list);
						}
						List tbody = combineTwoBody(preBody, body, rowId);
						body.clear();
						body.addAll(tbody);
						assembleExport(olapFun);
					} else {
						// 到这里应该是－1了
						if (null != olapFun.getSort()
								&& !olapFun.isFirstExpand()) {
							List list = new ArrayList();
							list.add(headRow);
							list.addAll(body);
							list.add(endRow);
							body.clear();
							body.addAll(list);
						}
						preExpBody.clear();
						preExpBody.addAll(exportBody);
						assembleExport(olapFun);
					}
				} else {
					// 这里也必须缩小数据
					List tbody = collapseExpand(preBody,
							olapFun.getCollapseRowId());
					body.clear();
					body.addAll(tbody);
				}
			}
		} else {
			body = genNonFixedTableBody(svces, report.rpt_id, tableCols,
					olapFun, report.cycle, trStyle, tdStyle);
		}
		return body;
	}

	private List genFixedExpandTableBody(String[][] svces, String reportId,
			List tableCols, RptOlapFuncStruct olapFun, String statPeriod,
			String trClass, String tdClass, String curLevel)
			throws ReportOlapException {
		if (null == svces || svces.length <= 0 || null == tableCols
				|| tableCols.size() <= 0 || null == olapFun
				|| null == statPeriod || null == reportId)
			throw new ReportOlapException("生成固定表体时输入的参数为空");

		List body = new ArrayList();

		boolean toUserLevel = olapFun.isToUserLevel();

		StringBuffer leftPart = new StringBuffer("");
		StringBuffer rightPart = new StringBuffer("");
		StringBuffer rightPerPart = new StringBuffer("");
		StringBuffer rightSameRatioPart = new StringBuffer("");
		StringBuffer rightLastRatioPart = new StringBuffer("");
		StringBuffer sumRightPart = new StringBuffer("");
		StringBuffer sumRightPerPart = new StringBuffer("");
		StringBuffer sumRightSameRatioPart = new StringBuffer("");
		StringBuffer sumRightLastRatioPart = new StringBuffer("");

		StringBuffer export = new StringBuffer("");
		StringBuffer exportPer = new StringBuffer("");
		StringBuffer exportSameRatio = new StringBuffer("");
		StringBuffer exportLastRatio = new StringBuffer("");
		StringBuffer sumExportPer = new StringBuffer("");
		StringBuffer sumExportSameRatio = new StringBuffer("");
		StringBuffer sumExportLastRatio = new StringBuffer("");
		StringBuffer sumExport = new StringBuffer("");

		StringBuffer userWhere = new StringBuffer("");

		String preValues = "";
		Map values = olapFun.getExpandedValues();
		if (null != values) {
			// 必须排序,按从小到大的顺序
			List list = new ArrayList();
			Iterator iter = values.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				list.add(entry.getKey());
			}
			// 排序
			String[] keys = (String[]) list.toArray(new String[list.size()]);
			Arrays.sort(keys, 0, keys.length);
			for (int i = 0; i < keys.length; i++) {
				String value = (String) values.get(keys[i]);
				if (null != value) {
					preValues += "_" + value;
				}
			}
		}
		String dimValues = "";
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay() && tCol.isDim()) {
				String value = tCol.getDimValue();
				if (null != value) {
					dimValues += "&dim_" + tCol.getIndex() + "=" + value;
				}
			}
		}
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;

		if (olapFun.isHasAlert() && olapFun.isHasSameRatioAlert())
			hasSameAlert = true;
		if (olapFun.isHasAlert() && olapFun.isHasLastRatioAlert())
			hasLastAlert = true;

		RptOlapTableColumnStruct[] cols = (RptOlapTableColumnStruct[]) tableCols
				.toArray(new RptOlapTableColumnStruct[tableCols.size()]);

		sumRightPart.append("<tr class=\"")
				.append(RptOlapConsts.TABLE_SUM_TR_CLASS).append("\">\n");
		// 当前展开层次
		int curLvl = Integer.parseInt(curLevel);
		int dimCount = 0;
		for (int i = 0; i < svces.length - 1; i++) {
			// 初始化
			userWhere.delete(0, userWhere.length());

			StringBuffer link = new StringBuffer("");
			link.append(RptOlapConsts.OLAP_DIG_ACTION);
			link.append("?report_id=::report_id::").append(
					RptOlapTabDomainUtil.convertTableColStructToSimpleUrl(
							tableCols, olapFun));
			//
			link.append(dimValues);
			leftPart.append("<tr nowrap class=\"").append(trClass)
					.append("\">");
			rightPart.append("<tr nowrap class=\"").append(trClass)
					.append("\">");
			rightPerPart.append("<tr nowrap class=\"").append(trClass)
					.append("\">");
			rightSameRatioPart.append("<tr nowrap class=\"").append(trClass)
					.append("\">");
			rightLastRatioPart.append("<tr nowrap class=\"").append(trClass)
					.append("\">");
			export.append("<tr nowrap>");
			exportPer.append("<tr nowrap>");
			exportSameRatio.append("<tr nowrap>");
			exportLastRatio.append("<tr nowrap>");

			// 维度计数器
			int index = 0;
			int dimIndex = 0;
			int msuIndex = 0;
			String rowId = "";
			boolean firstMsu = true;
			for (int j = 0; j < cols.length; j++) {
				RptOlapTableColumnStruct tCol = cols[j];
				if (tCol.isDisplay()) {
					if (tCol.isDim()) {
						if ((!olapFun.isSingleDimExpand() && curLvl == index)
								|| olapFun.isSingleDimExpand()) {
							List result = genDimCellContent(dimIndex,
									toUserLevel, tCol, svces[i], false, false);
							String title = (String) result.get(1);
							leftPart.append(
									"<td nowrap align=\"left\" class=\"")
									.append(tdClass).append("\"");
							leftPart.append(" title=\"" + title + "\"");
							leftPart.append(">");

							// 这里得把所有的维度值全组合进去
							rowId = "level_" + curLvl + "_dim" + preValues
									+ "_" + svces[i][2 * dimIndex];
							String html = tCol.getColHeadHTML1();
							String content = (String) result.get(0);
							String expContent = content;
							String spaces = RptOlapStringUtil
									.genStringWithSpace(html, content);
							content = genDimExpandLink(dimIndex, tCol,
									svces[i], olapFun, curLevel, true)
									+ "&nbsp;" + content;
							// 加上空格
							spaces = RptOlapStringUtil.genStringWithSpace(html,
									content);
							leftPart.append(content);
							leftPart.append(spaces);
							leftPart.append("</td>");

							StringBuffer sb = new StringBuffer();
							sb.append("<td nowrap>");
							sb.append(RptOlapStringUtil
									.genHtmlSpaces(2 * curLvl + 1));
							sb.append(expContent);
							sb.append("</td>");

							export.append(sb);
							exportPer.append(sb);
							exportSameRatio.append(sb);
							exportLastRatio.append(sb);
							if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
									.getDigLevel())) {
								int realIndex = 2 * dimIndex;
								userWhere.append("&").append(tCol.getColFld())
										.append("=")
										.append(svces[i][realIndex]);
								link.append("&dim_").append(tCol.getIndex())
										.append("=")
										.append(svces[i][realIndex]);
							}
							dimIndex++;
						}
						if (!olapFun.isSingleDimExpand())
							index++;
					} else {
						RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol
								.getStruct();
						String func = olapFun.getCurFunc();
						int tMsuIndex = msuIndex;
						int tDimCount = dimCount;
						boolean tFirstMsu = firstMsu;
						int tDimIndex = dimIndex;
						// 如果目前是同比或者环比呢,对于那三个整数无所谓，
						// 数据部分
						olapFun.setCurFunc(RptOlapConsts.OLAP_FUN_DATA);
						List list = genMsuBody(tCol, olapFun, rptMsu,
								statPeriod, hasSameAlert, hasLastAlert, i,
								svces, firstMsu, dimCount, dimIndex, msuIndex,
								toUserLevel, tdClass);
						dimCount = ((Integer) list.get(0)).intValue();
						firstMsu = ((Boolean) list.get(1)).booleanValue();
						msuIndex = ((Integer) list.get(2)).intValue();
						rightPart.append((StringBuffer) list.get(3));
						sumRightPart.append((StringBuffer) list.get(4));
						export.append((StringBuffer) list.get(5));
						sumExport.append((StringBuffer) list.get(6));
						// 占比部分
						olapFun.setCurFunc(RptOlapConsts.OLAP_FUN_PERCENT);
						list = genMsuBody(tCol, olapFun, rptMsu, statPeriod,
								hasSameAlert, hasLastAlert, i, svces,
								tFirstMsu, tDimCount, tDimIndex, tMsuIndex,
								toUserLevel, tdClass);
						rightPerPart.append((StringBuffer) list.get(3));
						sumRightPerPart.append((StringBuffer) list.get(4));
						exportPer.append((StringBuffer) list.get(5));
						sumExportPer.append((StringBuffer) list.get(6));
						// 同比部分
						olapFun.setCurFunc(RptOlapConsts.OLAP_FUN_SAME);
						list = genMsuBody(tCol, olapFun, rptMsu, statPeriod,
								hasSameAlert, hasLastAlert, i, svces,
								tFirstMsu, tDimCount, tDimIndex, tMsuIndex,
								toUserLevel, tdClass);
						rightSameRatioPart.append((StringBuffer) list.get(3));
						sumRightSameRatioPart
								.append((StringBuffer) list.get(4));
						exportSameRatio.append((StringBuffer) list.get(5));
						sumExportSameRatio.append((StringBuffer) list.get(6));
						// 环比部分
						olapFun.setCurFunc(RptOlapConsts.OLAP_FUN_LAST);
						list = genMsuBody(tCol, olapFun, rptMsu, statPeriod,
								hasSameAlert, hasLastAlert, i, svces,
								tFirstMsu, tDimCount, tDimIndex, tMsuIndex,
								toUserLevel, tdClass);
						rightLastRatioPart.append((StringBuffer) list.get(3));
						sumRightLastRatioPart
								.append((StringBuffer) list.get(4));
						exportLastRatio.append((StringBuffer) list.get(5));
						sumExportLastRatio.append((StringBuffer) list.get(6));
						// 将功能域对象设置回去
						olapFun.setCurFunc(func);
					}
				}
			}
			leftPart.append("</tr>");
			rightPart.append("</tr>");
			rightPerPart.append("</tr>");
			rightSameRatioPart.append("</tr>");
			rightLastRatioPart.append("</tr>");
			export.append("</tr>");
			exportPer.append("</tr>");
			exportSameRatio.append("</tr>");
			exportLastRatio.append("</tr>");

			// 声明一个新的行结构
			RptOlapRowStruct row = new RptOlapRowStruct();
			// 设置行ID
			row.setRow_id(rowId);
			// 替换相应的链接
			String sTmp = leftPart.toString();
			sTmp = sTmp.replaceAll("::LINK::", link.toString());
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			sTmp = sTmp.replaceAll("::report_id::", reportId);
			row.setLeftPart(sTmp);
			sTmp = rightPart.toString();
			sTmp = sTmp.replaceAll("::LINK::", link.toString());
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			sTmp = sTmp.replaceAll("::report_id::", reportId);
			row.setRightPart(sTmp);

			sTmp = rightPerPart.toString();
			sTmp = sTmp.replaceAll("::LINK::", link.toString());
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			sTmp = sTmp.replaceAll("::report_id::", reportId);
			row.setRightPerPart(sTmp);

			sTmp = rightSameRatioPart.toString();
			sTmp = sTmp.replaceAll("::LINK::", link.toString());
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			row.setRightSameRatioPart(sTmp);

			sTmp = rightLastRatioPart.toString();
			sTmp = sTmp.replaceAll("::LINK::", link.toString());
			sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
			sTmp = sTmp.replaceAll("::report_id::", reportId);
			row.setRightLastRatioPart(sTmp);
			// 每次清空了，相当于一次一行了
			leftPart.delete(0, leftPart.length());
			rightPart.delete(0, rightPart.length());
			rightPerPart.delete(0, rightPerPart.length());
			rightSameRatioPart.delete(0, rightSameRatioPart.length());
			rightLastRatioPart.delete(0, rightLastRatioPart.length());
			body.add(row);

			RptOlapRowStruct expRow = new RptOlapRowStruct();
			expRow.setRow_id(rowId);
			expRow.setLeftPart(export.toString());
			expRow.setRightPerPart(exportPer.toString());
			expRow.setRightSameRatioPart(exportSameRatio.toString());
			expRow.setRightLastRatioPart(exportLastRatio.toString());
			exportBody.add(expRow);
			// 加上合计部分
			if (i == svces.length - 2) {
				leftPart.append("<tr class=\"")
						.append(RptOlapConsts.TABLE_SUM_TR_CLASS)
						.append("\">\n");
				leftPart.append("<td nowrap align=\"center\" colspan=\"");
				leftPart.append(dimIndex).append("\" class=\"").append(tdClass)
						.append("\">");
				leftPart.append("<strong>合计</strong>");
				leftPart.append("</td>");
				leftPart.append("</tr>\n");

				// 右侧也应该加上合计部分
				sumRightPart.append("</tr>\n");

				row = new RptOlapRowStruct();
				// 合计行设置行ID为空
				row.setRow_id("sum");
				// 替换相应的链接
				sTmp = leftPart.toString();
				sTmp = sTmp.replaceAll("::LINK::", link.toString());
				sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
				sTmp = sTmp.replaceAll("::report_id::", reportId);
				row.setLeftPart(sTmp);

				sTmp = sumRightPart.toString();
				sTmp = sTmp.replaceAll("::LINK::", link.toString());
				sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
				sTmp = sTmp.replaceAll("::report_id::", reportId);
				row.setRightPart(sTmp);

				sTmp = sumRightPerPart.toString();
				sTmp = sTmp.replaceAll("::LINK::", link.toString());
				sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
				sTmp = sTmp.replaceAll("::report_id::", reportId);
				row.setRightPerPart(sTmp);

				sTmp = sumRightSameRatioPart.toString();
				sTmp = sTmp.replaceAll("::LINK::", link.toString());
				sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
				sTmp = sTmp.replaceAll("::report_id::", reportId);
				row.setRightSameRatioPart(sTmp);

				sTmp = sumRightLastRatioPart.toString();
				sTmp = sTmp.replaceAll("::LINK::", link.toString());
				sTmp = sTmp.replaceAll("::USERLINK::", userWhere.toString());
				sTmp = sTmp.replaceAll("::report_id::", reportId);
				row.setRightLastRatioPart(sTmp);

				sTmp = sumExport.toString();
				StringBuffer sb = new StringBuffer();
				sumExport.delete(0, sumExport.length());
				sb.append("<tr class=\"")
						.append(RptOlapConsts.TABLE_SUM_TR_CLASS)
						.append("\">\n");
				sb.append("<td nowrap align=\"center\" colspan=\"");
				sb.append(dimIndex).append("\" class=\"").append(tdClass)
						.append("\">");
				sb.append("<strong>合计</strong>");
				sb.append("</td>\n");
				sumExport.append(sb);
				sumExport.append(sTmp);
				sumExport.append("</tr>\n");

				sTmp = sumExportPer.toString();
				sumExportPer.delete(0, sumExportPer.length());
				sumExportPer.append(sb);
				sumExportPer.append(sTmp);
				sumExportPer.append("</tr>\n");

				sTmp = sumExportSameRatio.toString();
				sumExportSameRatio.delete(0, sumExportSameRatio.length());
				sumExportSameRatio.append(sb);
				sumExportSameRatio.append(sTmp);
				sumExportSameRatio.append("</tr>\n");

				sTmp = sumExportLastRatio.toString();
				sumExportLastRatio.delete(0, sumExportLastRatio.length());
				sumExportLastRatio.append(sb);
				sumExportLastRatio.append(sTmp);
				sumExportLastRatio.append("</tr>\n");

				RptOlapRowStruct sumRow = new RptOlapRowStruct();
				sumRow.setRow_id(null);
				sumRow.setLeftPart(sumExport.toString());
				sumRow.setRightPerPart(sumExportPer.toString());
				sumRow.setRightSameRatioPart(sumExportSameRatio.toString());
				sumRow.setRightLastRatioPart(sumExportLastRatio.toString());

				// 每次清空了，相当于一次一行了
				leftPart.delete(0, leftPart.length());
				rightPart.delete(0, rightPart.length());
				// 第一次
				if (olapFun.isFirstExpand()
						|| RptOlapConsts.ZERO_STR.equals(""
								+ olapFun.getCurExpandLevel())) {
					body.add(row);
					exportBody.add(sumRow);
				}
			}
			export.delete(0, export.length());
			exportPer.delete(0, exportPer.length());
			exportSameRatio.delete(0, exportSameRatio.length());
			exportLastRatio.delete(0, exportLastRatio.length());
		}
		// 为了调整宽度和高度
		StringBuffer sb = new StringBuffer("");
		sb.append("<tr>\n");
		for (int i = 0; i < dimCount; i++) {
			sb.append("<td>&nbsp;</td>");
		}
		sb.append("</tr>\n");

		RptOlapRowStruct row = new RptOlapRowStruct();
		row.setRow_id(null);
		row.setLeftPart(sb.toString());
		row.setRightPart("");
		// 第一次时加上
		if (olapFun.isFirstExpand()
				|| RptOlapConsts.ZERO_STR.equals(""
						+ olapFun.getCurExpandLevel()))
			body.add(row);
		return body;
	}

	private List combineTwoBody(List preBody, List body, String rowId)
			throws ReportOlapException {
		if (null == body || null == preBody || null == rowId
				|| "".equals(rowId) || 0 >= preBody.size() || 0 >= body.size())
			throw new ReportOlapException("组合折叠展开表体时输入的参数为空");

		List tBody = new ArrayList();
		Iterator iter = preBody.iterator();
		while (iter.hasNext()) {
			RptOlapRowStruct row = (RptOlapRowStruct) iter.next();
			String tmpRowId = row.getRow_id();
			if (null != tmpRowId && rowId.equals(tmpRowId)) {
				// 本行row的左部分需要变成折叠
				row = convertRowUrl(row, false);
				tBody.add(row);
				tBody.addAll(body);
			} else {
				tBody.add(row);
			}
		}
		List tmpBody = new ArrayList();
		iter = preExpBody.iterator();
		while (iter.hasNext()) {
			RptOlapRowStruct row = (RptOlapRowStruct) iter.next();
			String tmpRowId = row.getRow_id();
			if (null != tmpRowId && rowId.equals(tmpRowId)) {
				// 本行row的左部分需要变成折叠
				row = convertRowUrl(row, false);
				tmpBody.add(row);
				tmpBody.addAll(exportBody);
			} else {
				tmpBody.add(row);
			}
		}
		// 此时tmpBody为全局
		preExpBody.clear();
		preExpBody.addAll(tmpBody);
		return tBody;
	}

	private void assembleExport(RptOlapFuncStruct olapFun) {
		exportBody.clear();
		String func = olapFun.getCurFunc();
		Iterator iter = preExpBody.iterator();
		while (iter.hasNext()) {
			RptOlapRowStruct row = (RptOlapRowStruct) iter.next();
			if (RptOlapConsts.OLAP_FUN_DATA.equals(func))
				exportBody.add(row.getLeftPart());
			if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func))
				exportBody.add(row.getRightPerPart());
			if (RptOlapConsts.OLAP_FUN_SAME.equals(func))
				exportBody.add(row.getRightSameRatioPart());
			if (RptOlapConsts.OLAP_FUN_LAST.equals(func))
				exportBody.add(row.getRightLastRatioPart());
		}
	}

	private List collapseExpand(List preBody, String rowId)
			throws ReportOlapException {
		if (null == preBody || 0 >= preBody.size() || null == rowId
				|| "".equals(rowId))
			throw new ReportOlapException("收缩展开行的结构时输入的参数为空");
		String[] values = rowId.split("_");
		int curLevel = Integer.parseInt(values[1]);
		boolean found = false;
		List body = new ArrayList();
		Iterator iter = preBody.iterator();
		while (iter.hasNext()) {
			RptOlapRowStruct row = (RptOlapRowStruct) iter.next();
			String tRowId = row.getRow_id();
			if (null == tRowId || "".equals(tRowId)) {
				body.add(row);
			} else {
				// 先找到当前行
				if (tRowId.equals(rowId)) {
					row = convertRowUrl(row, true);
					body.add(row);
					found = true;
				} else {
					if (found) {
						// 下面的行
						values = tRowId.split("_");
						if (values.length >= 2) {
							int tmpLevel = Integer.parseInt(values[1]);
							if (tmpLevel <= curLevel) {
								body.add(row);
								found = false;
							}
						} else {
							body.add(row);
						}
					} else {
						body.add(row);
					}
				}
			}
		}
		// 设置以前的内容
		List list = new ArrayList();
		list.addAll(exportBody);
		exportBody.clear();
		iter = preExpBody.iterator();
		while (iter.hasNext()) {
			RptOlapRowStruct row = (RptOlapRowStruct) iter.next();
			String tRowId = row.getRow_id();
			if (null == tRowId || "".equals(tRowId)) {
				exportBody.add(row);
			} else {
				// 先找到当前行
				if (tRowId.equals(rowId)) {
					row = convertRowUrl(row, true);
					exportBody.add(row);
					found = true;
				} else {
					if (found) {
						// 下面的行
						values = tRowId.split("_");
						int tmpLevel = Integer.parseInt(values[1]);
						if (tmpLevel <= curLevel) {
							exportBody.add(row);
							found = false;
						}
					} else {
						exportBody.add(row);
					}
				}
			}
		}
		preExpBody.clear();
		preExpBody.addAll(exportBody);
		exportBody.clear();
		exportBody.addAll(list);
		return body;
	}

	private RptOlapRowStruct convertRowUrl(RptOlapRowStruct row,
			boolean toExpand) {
		RptOlapRowStruct tRow = row;
		String left = tRow.getLeftPart();
		if (null != left && !"".equals(left)) {
			// 图片变为减号
			if (toExpand)
				left = StringB.replace(left, RptOlapConsts.IMG_COLLAPSE,
						RptOlapConsts.IMG_EXPAND);
			else
				left = StringB.replace(left, RptOlapConsts.IMG_EXPAND,
						RptOlapConsts.IMG_COLLAPSE);
			// 看看是否有
			String url = "&" + RptOlapConsts.REQ_COLLAPSE_EXPAND + "=";
			int pos = left.indexOf(url);
			if (pos >= 0) {
				if (toExpand) {
					// 要展开
					url = "";
				} else {
					// 要收缩
					url += row.getRow_id();
				}
				// 替换
				pos = left
						.indexOf(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION + "('");
				if (pos >= 0) {
					int end = left.indexOf("')", pos + 1);
					if (end >= 0) {
						String subStr = left
								.substring(
										pos
												+ (RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION + "('")
														.length(), end);
						String tmp = "&" + RptOlapConsts.REQ_COLLAPSE_EXPAND
								+ "=";
						String replace = subStr;
						pos = replace.indexOf(tmp);
						if (pos >= 0) {
							end = replace.indexOf("&", pos + 1);
							if (end >= 0)
								replace = replace.substring(0, pos)
										+ replace.substring(end);
							else
								replace = replace.substring(0, pos);
							//
							replace += url;
							left = StringB.replace(left, subStr, replace);
						}
					}
				}
			} else {
				// 没有，看看是干什么
				if (toExpand) {
					// 要展开
					url = "";
				} else {
					// 要收缩
					url += row.getRow_id();
				}
				// 把URL加到最后,
				// 找到那个
				pos = left
						.indexOf(RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION + "('");
				if (pos >= 0) {
					int end = left.indexOf("')", pos + 1);
					if (end >= 0) {
						String subStr = left
								.substring(
										pos
												+ (RptOlapConsts.DIG_DIM_MSU_JS_FUNCTION + "('")
														.length(), end);
						String replace = subStr + url;
						left = StringB.replace(left, subStr, replace);
					}
				}
			}
		}
		tRow.setLeftPart(left);
		return tRow;
	}

	public List convertRowStructToHtml(List body, RptOlapFuncStruct olapFun)
			throws ReportOlapException {
		if (null == body || 0 >= body.size() || null == olapFun)
			throw new ReportOlapException("将行对象转换成表格字符串时输入的参数为空");
		List tBody = new ArrayList();
		List left = new ArrayList();
		List right = new ArrayList();
		String func = olapFun.getCurFunc();
		Iterator iter = body.iterator();
		while (iter.hasNext()) {
			RptOlapRowStruct row = (RptOlapRowStruct) iter.next();
			left.add(row.getLeftPart());
			if (null == row.getRow_id()) {
				right.add(row.getRightPart());
			} else {
				if (RptOlapConsts.OLAP_FUN_DATA.equals(func))
					right.add(row.getRightPart());
				if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func))
					right.add(row.getRightPerPart());
				if (RptOlapConsts.OLAP_FUN_SAME.equals(func))
					right.add(row.getRightSameRatioPart());
				if (RptOlapConsts.OLAP_FUN_LAST.equals(func))
					right.add(row.getRightLastRatioPart());
			}
		}
		tBody.addAll(left);
		tBody.addAll(right);
		assembleExport(olapFun);
		return tBody;
	}

	private String genTDBgContent(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun, int index, String[] svces,
			boolean hasSameAlert) throws ReportOlapException {
		if (null == tCol || null == olapFun || null == svces
				|| 0 >= svces.length)
			throw new ReportOlapException("生成预警内容时输入得参数为空");

		StringBuffer content = new StringBuffer();
		StringBuffer title = new StringBuffer();
		RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
		String highValue = rptMsu.high_value;

		String lowValue = rptMsu.low_value;

		String ratio = null;
		if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.need_alert)
				&& RptOlapConsts.ALERT_MODE_TDBGCOLOR.equals(rptMsu.alert_mode)) {
			// 底色预警
			if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.ratio_compare)) {
				// 比率预警
				if (RptOlapConsts.ALERT_COMPARE_TO_SAME
						.equals(rptMsu.compare_to)) {
					title.append("指标同比预警\n");
					title.append("指标预警上限:")
							.append(Double.parseDouble(rptMsu.high_value) * 100)
							.append("%\n");
					title.append("指标预警下限:")
							.append(Double.parseDouble(rptMsu.low_value) * 100)
							.append("%\n");
					// 同比比率
					int realIndex = index + 2;
					// 前移两个，如果有累计，还得移动一个
					if (olapFun.isHasSum()) {
						// 有累计，移动
						realIndex++;
					}
					// 同比值
					ratio = svces[realIndex];
					title.append("指标同比值:")
							.append(formartMsu(tCol, ratio, true)).append("\n");
				}
				if (RptOlapConsts.ALERT_COMPARE_TO_LAST
						.equals(rptMsu.compare_to)) {
					title.append("指标环比预警\n");
					title.append("指标预警上限:")
							.append(Double.parseDouble(rptMsu.high_value) * 100)
							.append("%\n");
					title.append("指标预警下限:")
							.append(Double.parseDouble(rptMsu.low_value) * 100)
							.append("%\n");
					// 定位
					int realIndex = index + 2;
					if (hasSameAlert) {
						realIndex++;
						realIndex++;
					}
					if (olapFun.isHasSum()) {
						// 有累计，移动
						realIndex++;
					}
					// 环比值
					ratio = svces[realIndex];
					title.append("指标环比值:")
							.append(formartMsu(tCol, ratio, true)).append("\n");
				}
				if (null != ratio && !"".equals(ratio) && null != highValue
						&& !"".equals(highValue) && null != lowValue
						&& !"".equals(lowValue)) {
					// 开始比较了
					double dRatio = Double.parseDouble(ratio);
					double dHighValue = Double.parseDouble(highValue);
					double dLowValue = Double.parseDouble(lowValue);
					if (dRatio >= dHighValue) {
						// 超出了高限
						if (RptOlapConsts.ALERT_GREEN_COLOR
								.equalsIgnoreCase(rptMsu.rise_color)) {
							// 绿色
							content = new StringBuffer(" class=\"").append(
									RptOlapConsts.ALERT_BG_GREEN_CLASS).append(
									"\"");
							title.append("超出上限变好\n");
						} else {
							content = new StringBuffer(" class=\"").append(
									RptOlapConsts.ALERT_BG_RED_CLASS).append(
									"\"");
							title.append("超出上限变坏\n");
						}
					}
					if (dRatio <= dLowValue) {
						// 超出了高限
						if (RptOlapConsts.ALERT_GREEN_COLOR
								.equalsIgnoreCase(rptMsu.down_color)) {
							// 绿色
							content = new StringBuffer(" class=\"").append(
									RptOlapConsts.ALERT_BG_GREEN_CLASS).append(
									"\"");
							title.append("低于下限变好\n");
						} else {
							content = new StringBuffer(" class=\"").append(
									RptOlapConsts.ALERT_BG_RED_CLASS).append(
									"\"");
							title.append("低于下限变坏\n");
						}
					}
				}

			}
		}
		content.append(" title=\"").append(title).append("\" ");
		return content.toString();
	}

	private String genArrowContent(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun, int index, String[] svces,
			boolean hasSameAlert) throws ReportOlapException {
		if (null == tCol || null == olapFun || null == svces
				|| 0 >= svces.length)
			throw new ReportOlapException("生成预警内容时输入得参数为空");
		StringBuffer content = new StringBuffer();
		StringBuffer alt = new StringBuffer();
		RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
		String highValue = rptMsu.high_value;
		String lowValue = rptMsu.low_value;
		String ratio = null;
		if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.need_alert)
				&& RptOlapConsts.ALERT_MODE_ARROW.equals(rptMsu.alert_mode)) {
			// 箭头预警
			if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.ratio_compare)) {
				// 比率预警
				if (RptOlapConsts.ALERT_COMPARE_TO_SAME
						.equals(rptMsu.compare_to)) {
					// 同比比率
					int realIndex = index + 2;
					if (olapFun.isHasSum()) {
						// 有累计，移动
						realIndex++;
					}
					// 同比值
					ratio = svces[realIndex];
					alt.append("指标同比预警\n");
					alt.append("指标预警上限:")
							.append(Double.parseDouble(rptMsu.high_value) * 100)
							.append("%\n");
					alt.append("指标预警下限:")
							.append(Double.parseDouble(rptMsu.low_value) * 100)
							.append("%\n");
					alt.append("指标同比值:").append(formartMsu(tCol, ratio, true))
							.append("\n");
				}
				if (RptOlapConsts.ALERT_COMPARE_TO_LAST
						.equals(rptMsu.compare_to)) {
					// 定位
					int realIndex = index + 2;
					if (hasSameAlert) {
						realIndex++;
						realIndex++;
					}
					if (olapFun.isHasSum()) {
						// 有累计，移动
						realIndex++;
					}
					// 环比值
					ratio = svces[realIndex];
					alt.append("指标环比预警\n");
					alt.append("指标预警上限:")
							.append(Double.parseDouble(rptMsu.high_value) * 100)
							.append("%\n");
					alt.append("指标预警下限:")
							.append(Double.parseDouble(rptMsu.low_value) * 100)
							.append("%\n");
					alt.append("指标环比值:").append(formartMsu(tCol, ratio, true))
							.append("\n");
				}
				if (null != ratio && !"".equals(ratio) && null != highValue
						&& !"".equals(highValue) && null != lowValue
						&& !"".equals(lowValue)) {
					// 开始比较了
					double dRatio = Double.parseDouble(ratio);
					double dHighValue = Double.parseDouble(highValue);
					double dLowValue = Double.parseDouble(lowValue);
					if (dRatio >= dHighValue) {
						// 超出了高限
						if (RptOlapConsts.ALERT_GREEN_COLOR
								.equalsIgnoreCase(rptMsu.rise_color)) {
							// 绿色
							content = new StringBuffer(
									RptOlapConsts.ALERT_ARROW_RISE_GREEN_IMG);
							alt.append("超出上限变好\n");
						} else {
							content = new StringBuffer(
									RptOlapConsts.ALERT_ARROW_RISE_RED_IMG);
							alt.append("超出上限变坏\n");
						}
					} else if (dRatio > dLowValue) {
						// 为了对齐，需要放置一个空图片占位置
						content = new StringBuffer(
								RptOlapConsts.ALERT_ARROW_BLANK_IMG);
					}
					if (dRatio <= dLowValue) {
						// 超出了高限
						if (RptOlapConsts.ALERT_GREEN_COLOR
								.equalsIgnoreCase(rptMsu.down_color)) {
							// 绿色
							content = new StringBuffer(
									RptOlapConsts.ALERT_ARROW_DOWN_GREEN_IMG);
							alt.append("低于下限变好\n");
						} else {
							content = new StringBuffer(
									RptOlapConsts.ALERT_ARROW_DOWN_RED_IMG);
							alt.append("低于下限变坏\n");
						}
					} else if (dRatio < dHighValue) {
						// 为了对齐，需要放置一个空图片占位置
						content = new StringBuffer(
								RptOlapConsts.ALERT_ARROW_BLANK_IMG);
						alt = new StringBuffer();
					}
				}

			}
		}
		String imgHTML = content.toString();
		imgHTML = imgHTML.replaceAll("::ALT::", alt.toString());
		return imgHTML;
	}

	private List genMsuBody(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun, RptOlapMsuTable rptMsu,
			String statPeriod, boolean hasSameAlert, boolean hasLastAlert,
			int i, String[][] svces, boolean firstMsu, int dimCount,
			int dimIndex, int msuIndex, boolean toUserLevel, String tdClass) {
		List list = new ArrayList();
		StringBuffer rightPart = new StringBuffer("");
		StringBuffer export = new StringBuffer("");
		StringBuffer sumRightPart = new StringBuffer("");
		StringBuffer sumExport = new StringBuffer("");
		String func = olapFun.getCurFunc();

		if (firstMsu) {
			// 第一次的时候进行定位
			dimCount = dimIndex;
			msuIndex = 2 * dimIndex;
			firstMsu = false;
		}
		String alertContent1 = "";
		String alertContent2 = "";
		alertContent1 = genTDBgContent(tCol, olapFun, msuIndex, svces[i],
				hasSameAlert);
		alertContent2 = genArrowContent(tCol, olapFun, msuIndex, svces[i],
				hasSameAlert);

		rightPart.append("<td nowrap align=\"right\"");
		rightPart.append(alertContent1);
		rightPart.append(" class=\"").append(tdClass).append("\"");
		rightPart.append(">");

		String html = tCol.getColHeadHTML1();
		if (!RptOlapConsts.OLAP_FUN_DATA.equals(olapFun.getCurFunc())) {
			if (RptOlapConsts.RESET_MODE_DIG.equals(olapFun.getDisplayMode()))
				html = tCol.getColHeadHTML2();
			else {
				html = tCol.getColName();
				try {
					byte[] bytes = html.getBytes("GBK");
					html = new String(bytes, 0, bytes.length / 2);
					html += "   ";
				} catch (UnsupportedEncodingException uee) {

				}
				String tmpHtml = "同比" + "<img src=\"../images/"
						+ RptOlapConsts.SORT_INIT_IMG
						+ "\" width=\"9\" border=\"0\">";
				if (RptOlapStringUtil.getHTMLStrLen(tmpHtml) > RptOlapStringUtil
						.getHTMLStrLen(html)) {
					html = tmpHtml;
				}
			}
		}
		// 本期值
		String content = genMsuCellContent(toUserLevel, tCol,
				svces[i][msuIndex], false, false);
		content += alertContent2;
		String spaces = RptOlapStringUtil.genStringWithSpace(html, content);
		rightPart.append(spaces);
		rightPart.append(content);
		rightPart.append("</td>");

		export.append("<td nowrap align=\"right\">");
		export.append(formartMsu(tCol, svces[i][msuIndex], false));
		export.append("</td>");

		if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func)) {
			rightPart.append("<td nowrap align=\"right\" class=\"")
					.append(tdClass).append("\">");
			// 占比值
			html = tCol.getColHeadHTML2();
			if (null == html || "".equals(html))
				html = "同比" + "<img src=\"../images/"
						+ RptOlapConsts.SORT_INIT_IMG
						+ "\" width=\"9\" border=\"0\">";
			String tmpStr = Arith.divs(svces[i][msuIndex],
					svces[svces.length - 1][msuIndex], 4);
			// 这里判断一下吧，目前只能如此了
			if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.msuInfo.is_calmsu)
					&& (rptMsu.msuInfo.real_fld.indexOf("/") >= 0 || rptMsu.msuInfo.real_fld
							.indexOf("*") >= 0)) {
				content = RptOlapConsts.IMG_CANNOT_CAL;
				spaces = RptOlapStringUtil.genStringWithSpace(html, content);
				rightPart.append(spaces);
				rightPart.append(content);
			} else {
				content = genMsuCellContent(false, tCol, tmpStr, false, true);
				spaces = RptOlapStringUtil.genStringWithSpace(html, content);
				rightPart.append(spaces);
				rightPart.append(content);
			}
			rightPart.append("</td>");

			export.append("<td nowrap align=\"right\">");
			if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.msuInfo.is_calmsu)
					&& (rptMsu.msuInfo.real_fld.indexOf("/") >= 0 || rptMsu.msuInfo.real_fld
							.indexOf("*") >= 0)) {
				export.append("");
			} else {
				export.append(formartMsu(tCol, tmpStr, true));
			}
			export.append("</td>");
		}
		// 合计值
		if (i == svces.length - 2) {

			String sumAlertContent1 = "";
			String sumAlertContent2 = "";
			sumAlertContent1 = genTDBgContent(tCol, olapFun, msuIndex,
					svces[svces.length - 1], hasSameAlert);
			sumAlertContent2 = genArrowContent(tCol, olapFun, msuIndex,
					svces[svces.length - 1], hasSameAlert);
			sumRightPart.append("<td nowrap align=\"right\" class=\"")
					.append(tdClass).append("\"");
			sumRightPart.append(sumAlertContent1);
			sumRightPart.append(">");
			sumRightPart.append(genMsuCellContent(false, tCol,
					svces[svces.length - 1][msuIndex], false, false));
			sumRightPart.append(sumAlertContent2);
			sumRightPart.append("</td>");

			sumExport.append("<td nowrap align=\"right\">");
			sumExport.append(formartMsu(tCol,
					svces[svces.length - 1][msuIndex], false));
			sumExport.append("</td>");
			// 合计占比
			if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func)) {
				sumRightPart.append("<td nowrap align=\"right\" class=\"")
						.append(tdClass).append("\">");
				sumRightPart.append("100%");
				sumRightPart.append("</td>");

				sumExport.append("<td nowrap align=\"right\">");
				sumExport.append("100%");
				sumExport.append("</td>");
			}
		}
		// 指标索引向前
		// 累计值、同比值、环比值显示不会同时出现,且出现任何其中之一所有的指标都带有这些值
		if ((RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate)
				&& RptOlapConsts.OLAP_FUN_DATA.equals(func) && olapFun
				.isHasSum())
				|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
				|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
			// 先向前移动一个下标,这里主要解决取到正确得值得问题
			// 如果有累计，加完是累计值，如果是同比或者环比，则是环比本期值了
			msuIndex++;

			// 累计值、同比或者环比
			rightPart.append("<td nowrap align=\"right\" class=\"")
					.append(tdClass).append("\">");

			// 生成链接
			html = tCol.getColHeadHTML3();
			if (null == html || "".equals(html))
				html = "同比" + "<img src=\"../images/"
						+ RptOlapConsts.SORT_INIT_IMG
						+ "\" width=\"9\" border=\"0\">";
			boolean isPercent = true;
			if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate))
				isPercent = false;
			if (RptOlapConsts.OLAP_FUN_SAME.equals(func)
					|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
				// 这里需要来判断了
				if (RptOlapConsts.RESET_MODE_EXPAND.equals(olapFun
						.getDisplayMode())) {
					// 如果是折叠、展开，还要向前一步
					if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate))
						msuIndex++;
				}
				// 没有预警
				// 此时指向环比或者同比了，
				msuIndex++;
				// 对于同比，不需要定位了
				// 如果环比分析，需要定位
				if (RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
					// 看有没有同比预警
					if (hasSameAlert) {
						// 有同比预警，
						msuIndex++;
						msuIndex++;
					}
				}
				isPercent = true;
			}
			content = genMsuCellContent(false, tCol, svces[i][msuIndex], false,
					isPercent);
			spaces = RptOlapStringUtil.genStringWithSpace(html, content);
			rightPart.append(spaces);
			rightPart.append(content);
			rightPart.append("</td>");

			export.append("<td nowrap align=\"right\">");
			export.append(formartMsu(tCol, svces[i][msuIndex], isPercent));
			export.append("</td>");
			// 先要累计的值
			if (i == svces.length - 2) {
				sumRightPart.append("<td nowrap align=\"right\" class=\"")
						.append(tdClass).append("\">");
				// 生成链接
				sumRightPart.append(genMsuCellContent(false, tCol,
						svces[svces.length - 1][msuIndex], false, isPercent));
				sumRightPart.append("</td>");

				sumExport.append("<td nowrap align=\"right\">");
				sumExport.append(formartMsu(tCol,
						svces[svces.length - 1][msuIndex], isPercent));
				sumExport.append("</td>");

			}
			// 如果周同比和月同比都有,那就要再加一个
			if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod)
					&& RptOlapConsts.OLAP_FUN_SAME.equals(func)
					&& RptOlapConsts.SAME_RATIO_BOTH_PERIOD.equals(olapFun
							.getSameRatioPeriod())) {
				//
				msuIndex++;
				msuIndex++;
				rightPart.append("<td nowrap align=\"right\" class=\"")
						.append(tdClass).append("\">");

				html = tCol.getColHeadHTML3();
				content = genMsuCellContent(false, tCol, svces[i][msuIndex],
						false, isPercent);
				spaces = RptOlapStringUtil.genStringWithSpace(html, content);
				rightPart.append(spaces);
				rightPart.append(content);
				rightPart.append("</td>");

				export.append("<td nowrap align=\"right\">");
				export.append(formartMsu(tCol, svces[i][msuIndex], isPercent));
				export.append("</td>");
				if (i == svces.length - 2) {
					sumRightPart.append("<td nowrap align=\"right\" class=\"")
							.append(tdClass).append("\">");
					// 生成链接
					sumRightPart
							.append(genMsuCellContent(false, tCol,
									svces[svces.length - 1][msuIndex], false,
									isPercent));
					sumRightPart.append("</td>");

					sumExport.append("<td nowrap align=\"right\">");
					sumExport.append(formartMsu(tCol,
							svces[svces.length - 1][msuIndex], isPercent));
					sumExport.append("</td>");
				}
			}
			// 上面
			// 如果是累计，有同比预警、和环比预警，这是到同比本期值了
			msuIndex++;
			if (RptOlapConsts.OLAP_FUN_DATA.equals(func) && olapFun.isHasSum()
					&& hasSameAlert) {
				msuIndex++;
				msuIndex++;
			}
			if (RptOlapConsts.OLAP_FUN_DATA.equals(func) && olapFun.isHasSum()
					&& hasLastAlert) {
				msuIndex++;
				msuIndex++;
			}
			if (RptOlapConsts.OLAP_FUN_SAME.equals(func)) {
				if (hasLastAlert) {
					msuIndex++;
					msuIndex++;
				}
			}
		} else {
			if (olapFun.isHasSum()) {
				msuIndex++;
			}
			if (hasSameAlert) {
				msuIndex++;
				msuIndex++;
			}
			if (hasLastAlert) {
				msuIndex++;
				msuIndex++;
			}
			msuIndex++;
		}
		list.add(new Integer(dimCount));
		list.add(new Boolean(firstMsu));
		list.add(new Integer(msuIndex));
		list.add(rightPart);
		list.add(sumRightPart);
		list.add(export);
		list.add(sumExport);
		return list;
	}

	public List getExportBody() {
		return exportBody;
	}

	public List getPreExpBody() {
		return preExpBody;
	}

	public void setPreExpBody(List preExpBody) {
		this.preExpBody = preExpBody;
	}
}
