package com.ailk.bi.subject.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.table.SubjectCommDimHierarchy;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.subject.domain.SubjectDimStruct;
import com.ailk.bi.subject.domain.TableCurFunc;

/**
 * @author xdou 数据表工具类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SubjectDataTableUtil {
	public static StringBuffer processDimLink(SubjectCommTabDef subTable, SubjectCommTabCol tabCol,
			TableCurFunc curFunc, String level, String[] svces, String value) {
		StringBuffer link = new StringBuffer();
		StringBuffer urlLink = genDimLink(subTable, tabCol, curFunc, svces, level);
		if (null != urlLink && 0 < urlLink.length()) {
			// 还要当前条件
			link.append("<a href=\"javascript:").append(urlLink);
			link.append(" \" class=\"").append(curFunc.hrefLinkClass).append("\"");
			link.append(">");
			link.append(value);
			link.append("</a>");
		} else {
			link.append(value);
		}
		return link;
	}

	public static StringBuffer genChartChangeLink(SubjectCommTabDef subTable, SubjectCommTabCol tabCol,
			TableCurFunc curFunc, String level, String[] svces, String value) {
		StringBuffer link = new StringBuffer();
		String urlLink = "";

		if (SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_chartchange) && null != subTable.rlt_chart_ids
				&& !"".equals(subTable.rlt_chart_ids)) {
			if ((SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col) && SubjectConst.YES
					.equalsIgnoreCase(subTable.has_expand))
					|| (SubjectConst.NO.equalsIgnoreCase(subTable.has_expand) && SubjectConst.NO
							.equalsIgnoreCase(tabCol.is_expand_col))) {
				// 获取图形标识
				String chartIds = subTable.rlt_chart_ids;
				urlLink = SubjectConst.CHART_CHANGE_JS;
				urlLink = urlLink.replaceAll("::CHART_IDS::", chartIds);
				/*
				 * urlLink += SubjectConst.ROW_CLICK_JS; // 判断是否有维度链接 urlLink +=
				 * genDimLink(subTable, tabCol, curFunc, level); // 还要当前条件
				 * link.append("<a href=\"").append(urlLink);
				 */

			} else {
				String chartIds = subTable.rlt_chart_ids;
				urlLink = SubjectConst.CHART_CHANGE_JS;
				urlLink = urlLink.replaceAll("::CHART_IDS::", chartIds);
				/*
				 * urlLink += SubjectConst.ROW_CLICK_JS; urlLink +=
				 * genDimLink(subTable, tabCol, curFunc, level);
				 * link.append("<a href=\"javascript:").append(urlLink);
				 */
			}
		}

		String tableLink = "";
		if (SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_tablechange) && null != subTable.rlt_table_ids
				&& !"".equals(subTable.rlt_table_ids)) {
			if ((SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col) && SubjectConst.YES
					.equalsIgnoreCase(subTable.has_expand))
					|| (SubjectConst.NO.equalsIgnoreCase(subTable.has_expand) && SubjectConst.NO
							.equalsIgnoreCase(tabCol.is_expand_col))) {
				// 获取图形标识
				String tableIds = subTable.rlt_table_ids;
				tableLink = SubjectConst.TABLE_CHANGE_JS;
				tableLink = tableLink.replaceAll("::CHART_IDS::", tableIds);

			} else {
				String tableIds = subTable.rlt_table_ids;
				tableLink = SubjectConst.TABLE_CHANGE_JS;
				tableLink = tableLink.replaceAll("::CHART_IDS::", tableIds);

			}
		}

		urlLink += tableLink;
		urlLink += SubjectConst.ROW_CLICK_JS;
		urlLink += genDimLink(subTable, tabCol, curFunc, svces, level);
		link.append("<a href=\"javascript:").append(urlLink);

		link.append(" \" class=\"").append(curFunc.hrefLinkClass).append("\"");
		link.append(">");
		link.append(value);
		link.append("</a>");

		// System.out.println(link.toString() +
		// "===============1kaokao1========");
		return link;
	}

	/*
	 * public static StringBuffer genTableChangeLink(SubjectCommTabDef subTable,
	 * SubjectCommTabCol tabCol, TableCurFunc curFunc, String level, String
	 * value) { StringBuffer link = new StringBuffer(); if
	 * (SubjectConst.YES.equalsIgnoreCase(subTable.row_clicked_tablechange) &&
	 * null != subTable.rlt_table_ids && !"".equals(subTable.rlt_table_ids)) {
	 * if ((SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col) &&
	 * SubjectConst.YES .equalsIgnoreCase(subTable.has_expand)) ||
	 * (SubjectConst.NO.equalsIgnoreCase(subTable.has_expand) && SubjectConst.NO
	 * .equalsIgnoreCase(tabCol.is_expand_col))) { // 获取图形标识 String tableIds =
	 * subTable.rlt_table_ids; String urlLink = SubjectConst.TABLE_CHANGE_JS;
	 * urlLink = urlLink.replaceAll("::CHART_IDS::", tableIds); urlLink +=
	 * SubjectConst.ROW_CLICK_JS; // 判断是否有维度链接 urlLink += genDimLink(subTable,
	 * tabCol, curFunc, level); // 还要当前条件
	 * link.append("<a href=\"").append(urlLink);
	 * link.append(" \" class=\"").append(curFunc.hrefLinkClass) .append("\"");
	 * link.append(">"); link.append(value); link.append("</a>"); } else {
	 * String tableIds = subTable.rlt_table_ids; String urlLink =
	 * SubjectConst.TABLE_CHANGE_JS; urlLink =
	 * urlLink.replaceAll("::CHART_IDS::", tableIds); urlLink +=
	 * SubjectConst.ROW_CLICK_JS; urlLink += genDimLink(subTable, tabCol,
	 * curFunc, level); link.append("<a href=\"javascript:").append(urlLink);
	 * link.append(" \" class=\"").append(curFunc.hrefLinkClass) .append("\"");
	 * link.append(">"); link.append(value); link.append("</a>"); } } else {
	 * String urlLink = SubjectConst.ROW_CLICK_JS; urlLink +=
	 * genDimLink(subTable, tabCol, curFunc, level);
	 * link.append("<a href=\"javascript:").append(urlLink);
	 * link.append(" \" class=\"").append(curFunc.hrefLinkClass).append( "\"");
	 * link.append(">"); link.append(value); link.append("</a>"); } return link;
	 * }
	 */
	public static StringBuffer genDimLink(SubjectCommTabDef subTable, SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String[] svces, String level) {
		StringBuffer link = new StringBuffer();
		if (SubjectConst.ZERO.equals(level)) {
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_link)
					&& UserRightUtil.canLinkDisplay(subTable, tabCol, svces)) {
				if (null != tabCol.link_url && !"".equals(tabCol.link_url)) {
					link.append(SubjectConst.DIM_LINK_JS).append("('").append(tabCol.link_url);
					// 这里需要加上所选条件

					link.append("&1=1");
					link.append(SubjectStringUtil.convertWhereToUrlMode(curFunc.dataWhere));
					link.append(SubjectConst.LINK_DIM_STATE).append("','").append(tabCol.link_target).append("');");
				}
			}
		} else {
			// 找到层次
			if (null != tabCol.levels) {
				Iterator iter = tabCol.levels.iterator();
				while (iter.hasNext()) {
					SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) iter.next();
					if (level.equals(levObj.lev_id)) {
						if (SubjectConst.YES.equalsIgnoreCase(levObj.has_link)
								&& UserRightUtil.canLinkDisplay(subTable, tabCol, svces)) {
							if (null != levObj.link_url && !"".equals(levObj.link_url)) {
								link.append(SubjectConst.DIM_LINK_JS).append("('").append(levObj.link_url);
								link.append("&1=1");
								link.append(SubjectStringUtil.convertWhereToUrlMode(curFunc.dataWhere));
								link.append(SubjectConst.LINK_DIM_STATE).append("','").append(levObj.link_target)
										.append("');");
							}
						}
						break;
					}
				}
			}
		}
		return link;
	}

	/**
	 * 生成指标值链接
	 * 
	 * @param tabCol
	 *            列对象
	 * @param curFunc
	 *            功能对象
	 * @param value
	 *            指标值
	 * @return 带链接的值HMTL
	 */
	public static StringBuffer genValueLink(SubjectCommTabDef subTable, SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String[] svces, String value) {
		StringBuffer link = new StringBuffer();
		if (SubjectConst.YES.equalsIgnoreCase(tabCol.has_link) && UserRightUtil.canLinkDisplay(subTable, tabCol, svces)) {
			String urlLink = tabCol.link_url;
			if (urlLink.indexOf("?") < 0)
				urlLink += "?1=1";
			//
			urlLink += SubjectStringUtil.convertWhereToUrlMode(curFunc.dataWhere);
			urlLink += SubjectConst.LINK_DIM_URL;

			link.append("<a href=\"").append(urlLink);
			link.append(" class=\"").append(curFunc.hrefLinkClass).append("\"");
			if (null != tabCol.link_target && !"".equals(tabCol.link_target))
				link.append(" target=\"").append(tabCol.link_target).append("\"");
			link.append(">");
			link.append(value);
			link.append("</a>");
		} else {
			link.append(value);
		}
		return link;
	}

	/**
	 * 生成新的表格列对象，以支持维度横向作为列
	 * 
	 * @param subTable
	 *            表格对象
	 * @param dimStructs
	 *            维度结构列表
	 * @return 表格列对象列表
	 */
	public static List genNewTableCols(SubjectCommTabDef subTable, List dimStructs) {
		List tabCols = new ArrayList();
		List cols = subTable.tableCols;
		Iterator iter = cols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				if (SubjectConst.NO.equalsIgnoreCase(tabCol.dim_ascol)) {
					// 作为
					tabCols.add(tabCol);
				}
			} else {
				// 是指标
				// 扩展每个指标
				List expandMsus = genExpandMsus(tabCol, dimStructs);
				tabCols.addAll(expandMsus);
			}
		}
		return tabCols;
	}

	/**
	 * 生成扩展后的指标列表
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param dimStructs
	 *            维度列表
	 * @return 扩展后的指标
	 */
	private static List genExpandMsus(SubjectCommTabCol tabCol, List dimStructs) {
		List expandedMsus = new ArrayList();
		Iterator iter = dimStructs.iterator();
		while (iter.hasNext()) {
			SubjectDimStruct dimStruct = (SubjectDimStruct) iter.next();
			// 需要克隆现在的列定义
			// 然后设置新的code_field
			SubjectCommTabCol tmpCol = (SubjectCommTabCol) tabCol.clone();
			// 设置新的查询字段,对于计算字段怎么办,直接替换SUM（）中的内容
			String code_fld = dimStruct.code_fld;// 应该是SUM(A.sads)
			code_fld = SubjectStringUtil.replaceVirTabName(code_fld, SubjectConst.DATA_TABLE_VIR_NAME);
			String replace = "(CASE WHEN " + code_fld + "=";
			String value = dimStruct.dim_code;
			if (SubjectConst.DATA_TYPE_STRING.equals(dimStruct.data_type)) {
				value = "'" + value + "'";
			}
			replace += value + " THEN " + SubjectConst.MSU_REPLACE + " ELSE 0 END)";
			code_fld = tabCol.code_field;
			code_fld = SubjectStringUtil.processMsuReplace(code_fld, replace);
			tmpCol.code_field = code_fld;
			expandedMsus.add(tmpCol);
		}
		return expandedMsus;
	}

	/**
	 * 生成数据表查询FROM部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param virTableName
	 *            伪表名
	 * @return 查询语句的FROM部分
	 */
	public static String genDataTableFROM(SubjectCommTabDef subTable, String virTableName, String where) {
		String from = " FROM ";
		String data_table = subTable.data_table;
		data_table = data_table.toUpperCase();
		// 还得判断括号
		if (SubjectConst.YES.equalsIgnoreCase(subTable.condition_in) && data_table.indexOf("SELECT") >= 0) {
			if (data_table.indexOf("(") == 0) {
				data_table = data_table.substring(1, data_table.length() - 1);
			}
			// 还得判断有没有WHERE，没有得加上
			int pos = data_table.lastIndexOf("FROM");
			if (pos >= 0 && data_table.indexOf("WHERE", pos) < 0) {
				data_table += " WHERE 1=1 ";
			}

			from += " (" + data_table + SubjectStringUtil.clearVirTabName(where) + ") " + virTableName;
		} else
			from += " " + data_table + " " + virTableName;
		return from;
	}

	/**
	 * 生成数据表查询WHERE部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param virTableName
	 *            伪表名
	 * @return 查询语句的WHERE部分
	 */
	public static String genDataTableWHERE(SubjectCommTabDef subTable, String virTableName) {
		String where = subTable.data_where;
		if (null == where || "".equals(where))
			return "";
		where = where.toUpperCase();
		where = where.replaceAll("WHERE ", "");
		where = SubjectStringUtil.replaceVirTabName(where, virTableName);
		return where;
	}

	/**
	 * 生成比率子查询的FROM部分
	 * 
	 * @param subTable
	 *            表格对象
	 * @param where
	 *            查询条件
	 * @param dimSelect
	 *            维度选择
	 * @param virTableName
	 *            伪表名
	 * @return 比率子查询的FROM部分
	 */
	public static String genRatioSubFrom(SubjectCommTabDef subTable, String where, String dimSelect, String virTableName) {
		StringBuffer from = new StringBuffer();
		// 如果有列变行的纬度，那么需要在基础指标上分组加上该维度，基本指标不变
		// 默认认为没有同比
		// 需要对dimSelect进行处理，以便里面不包含统计月份或者统计统计日期
		dimSelect = dimSelect.toUpperCase();
		// 判断有没有时间字段,有去掉
		String timeFld = null;
		if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_DAY_LEVEL)) {
			timeFld = virTableName + "\\." + SubjectConst.TIME_DIM_DAY_FLD + ",";
		} else {
			timeFld = virTableName + "\\." + SubjectConst.TIME_DIM_MONTH_FLD + ",";
		}
		timeFld = timeFld.toUpperCase();
		dimSelect += ",";
		// 开始判断
		dimSelect = dimSelect.replaceAll(timeFld, "");
		if (dimSelect.lastIndexOf(",") >= 0) {
			dimSelect = dimSelect.substring(0, dimSelect.lastIndexOf(","));
		}
		boolean hasLastRatio = false;
		// 默认认为没有环比
		boolean hasLoopRatio = false;

		SubjectCommTabCol tabColDim = null;
		// 先看看有没有要求计算环比和同期比的
		List tabCols = subTable.preTableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.is_measure)) {
				if (null != tabCol.has_last && SubjectConst.YES.equalsIgnoreCase(tabCol.has_last))
					hasLastRatio = true;
				if (null != tabCol.has_loop && SubjectConst.YES.equalsIgnoreCase(tabCol.has_loop))
					hasLoopRatio = true;
			} else {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.dim_ascol)
						&& SubjectConst.YES.equalsIgnoreCase(tabCol.dim_ascol)) {
					tabColDim = tabCol;
				}
			}
		}

		// 这里组装同比、环比方式是分三部分关联，主数据是一部分，同比一部分，环比一部分
		// 主数据那部分不用和时间维表关联，而其他两部分需要关联
		// 然后这三步分再关联

		// 比率部分的查询部分
		StringBuffer ratioSelect = new StringBuffer();
		// 数据部分的查询
		StringBuffer dataSelect = new StringBuffer();
		// 分组部分
		StringBuffer groupby = new StringBuffer();
		StringBuffer on = new StringBuffer();
		on.append(SubjectStringUtil.parseDimSelectAsOnWhere(dimSelect, virTableName,
				SubjectConst.DATA_TABLE_LAST_VIR_NAME));
		// 由于在关联环比查询时，对于数据粒度到日的查询，
		if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_DAY_LEVEL)) {
			groupby.append(SubjectConst.DATA_TABLE_TMP_VIR_NAME).append(".").append(SubjectConst.TIME_DIM_DAY_FLD)
					.append(",");
		} else {
			groupby.append(SubjectConst.DATA_TABLE_TMP_VIR_NAME).append(".").append(SubjectConst.TIME_DIM_MONTH_FLD)
					.append(",");
		}
		// 有作为列的维度?此处需要观察！
		if (null != tabColDim) {
			String tmpFld = tabColDim.code_field;
			tmpFld = SubjectStringUtil.replaceVirTabName(tmpFld, SubjectConst.DATA_TABLE_TMP_VIR_NAME);
			groupby.append(tmpFld).append(",");
		}
		// 怎么获得基本的指标,需要保存以前的
		String subSelect = dimSelect;
		subSelect = SubjectStringUtil.replaceVirTabName(subSelect, SubjectConst.DATA_TABLE_MID_VIR_NAME);
		dataSelect.append(subSelect).append(",");
		ratioSelect.append(subSelect).append(",");
		// 将指标查询出来,对于计算型指标不用查询，所有指标需要SUM
		iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tableCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tableCol.is_measure)) {
				String msu_fld = tableCol.code_field;
				msu_fld = msu_fld.toUpperCase();
				// 这里要分析具体的指标了，有的是计算指标
				// 首先要区分计算和非计算指标,这里看字段里是否有多个SUM()
				String regular = "[SUM\\(]{1}?\\w*\\.?\\w*[\\)]{1}?";
				List subMsus = SubjectStringUtil.findSubStr(msu_fld, regular, true);
				// 匹配计算公式
				// System.out.println("subMsus========"+subMsus.size());

				if (null != subMsus) {
					Iterator subMsuIter = subMsus.iterator();
					while (subMsuIter.hasNext()) {
						String subMsuFld = (String) subMsuIter.next();
						dataSelect.append("SUM(").append(SubjectConst.DATA_TABLE_TMP_VIR_NAME).append(".")
								.append(subMsuFld).append(") AS ").append(subMsuFld).append(",");
						ratioSelect.append("SUM(").append(SubjectConst.DATA_TABLE_MID_VIR_NAME).append(".")
								.append(subMsuFld).append(") AS ").append(subMsuFld).append(",");
					}
				} else {
					// 说明没有匹配
					// ratioSelect.append(msu_fld).append(",");
				}
			}
		}
		String time_fld = null;
		StringBuffer timeSelect = new StringBuffer();
		if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_DAY_LEVEL)) {
			timeSelect.append(SubjectConst.DATA_TABLE_TMP_VIR_NAME).append(".").append(SubjectConst.TIME_DIM_DAY_FLD)
					.append(" AS ").append(subTable.time_field + "_1").append(",");
			time_fld = SubjectConst.TIME_DIM_DAY_FLD;
		} else {
			timeSelect.append(SubjectConst.DATA_TABLE_TMP_VIR_NAME).append(".").append(SubjectConst.TIME_DIM_MONTH_FLD)
					.append(" AS ").append(subTable.time_field + "_1").append(",");
			time_fld = SubjectConst.TIME_DIM_MONTH_FLD;
		}
		dataSelect.append(SubjectConst.DATA_TABLE_MID_VIR_NAME).append(".").append(subTable.time_field).append(",");
		// 由于多加了一个逗号，去掉
		if (null != dataSelect && dataSelect.lastIndexOf(",") >= 0) {
			dataSelect = new StringBuffer(dataSelect.substring(0, dataSelect.lastIndexOf(",")));
		}
		if (null != ratioSelect && ratioSelect.lastIndexOf(",") >= 0) {
			ratioSelect = new StringBuffer(ratioSelect.substring(0, ratioSelect.lastIndexOf(",")));
		}
		timeSelect.append(ratioSelect);

		// 这里要判断一下是否有重复的选择字段
		String tmpSelect = timeSelect.toString();
		tmpSelect = SubjectStringUtil.clearDupFld(tmpSelect);
		ratioSelect.delete(0, ratioSelect.length());
		ratioSelect.append("(SELECT ").append(tmpSelect);
		// ratioSelect.append(" FROM ").append(subTable.data_table).append(" ").append(SubjectConst.DATA_TABLE_MID_VIR_NAME).append(",");
		ratioSelect.append(" FROM ").append(SubjectConst.SUBJECT_TABLE_DATASOURCE).append(" ")
				.append(SubjectConst.DATA_TABLE_MID_VIR_NAME).append(",");
		// 这里也需要判断是否有重复的
		tmpSelect = dataSelect.toString();
		tmpSelect = SubjectStringUtil.clearDupFld(tmpSelect);
		dataSelect.delete(0, dataSelect.length());
		dataSelect.append("SELECT ").append(tmpSelect);
		// dataSelect.append(" FROM ").append(subTable.data_table).append(" ").append(SubjectConst.DATA_TABLE_BASE_VIR_NAME);
		dataSelect.append(" FROM ").append(SubjectConst.SUBJECT_TABLE_DATASOURCE).append(" ")
				.append(SubjectConst.DATA_TABLE_BASE_VIR_NAME);
		String tmpGroupby = groupby.toString();
		tmpGroupby = SubjectStringUtil.clearDupFld(tmpGroupby);
		groupby.delete(0, groupby.length());
		groupby.append(tmpGroupby);
		if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_DAY_LEVEL)) {
			ratioSelect.append(SubjectConst.TIME_DIM_DAY_TABLE);
		} else {
			ratioSelect.append(SubjectConst.TIME_DIM_MONTH_TABLE);
		}
		ratioSelect.append(" ").append(SubjectConst.DATA_TABLE_TMP_VIR_NAME).append(" ");
		// 加上条件
		String subWhere = where;
		subWhere = SubjectStringUtil.replaceVirTabName(subWhere, SubjectConst.DATA_TABLE_MID_VIR_NAME);
		dataSelect.append(subWhere);
		// 比率部分
		subWhere = subWhere.replaceAll(SubjectConst.DATA_TABLE_MID_VIR_NAME + "\\." + subTable.time_field,
				SubjectConst.DATA_TABLE_TMP_VIR_NAME + "." + time_fld);
		subWhere = subWhere.replaceAll(
				SubjectConst.DATA_TABLE_MID_VIR_NAME + "\\." + subTable.time_field.toLowerCase(),
				SubjectConst.DATA_TABLE_TMP_VIR_NAME + "." + time_fld);
		ratioSelect.append(subWhere);

		ratioSelect.append(" AND ").append(SubjectConst.DATA_TABLE_MID_VIR_NAME).append(".")
				.append(subTable.time_field).append("=").append(SubjectConst.DATA_TABLE_TMP_VIR_NAME).append(".")
				.append(SubjectConst.RATIO_TMP_FLD);

		if (groupby.length() > 0) {
			String tempGroup = dimSelect;
			tempGroup = SubjectStringUtil.replaceVirTabName(tempGroup, SubjectConst.DATA_TABLE_MID_VIR_NAME);
			dataSelect.append(" GROUP BY ").append(tempGroup);
			groupby.append(tempGroup);
		}
		ratioSelect.append(" GROUP BY ").append(groupby).append(")");

		dataSelect.append(",").append(SubjectConst.DATA_TABLE_MID_VIR_NAME).append(".").append(subTable.time_field);

		// 关联同比
		if (hasLastRatio) {

			if (from.length() <= 0) {
				tmpSelect = dataSelect.toString();
				tmpSelect = SubjectStringUtil.replaceVirTabName(tmpSelect, SubjectConst.DATA_TABLE_BASE_VIR_NAME);
				// 基本SQL已经不能支持自定义SQL 因为需要替换数据表明！！！
				tmpSelect = tmpSelect.replaceAll(SubjectConst.SUBJECT_TABLE_DATASOURCE, subTable.data_table);
				from = new StringBuffer(" FROM (").append(tmpSelect).append(") ").append(virTableName);

			}
			tmpSelect = ratioSelect.toString();

			if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_DAY_LEVEL)) {
				tmpSelect = tmpSelect.replaceAll(SubjectConst.RATIO_TMP_FLD, SubjectConst.TIME_DIM_SAME_DAY_FLD);
			}
			if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_MONTH_LEVEL)) {
				tmpSelect = tmpSelect.replaceAll(SubjectConst.RATIO_TMP_FLD, SubjectConst.TIME_DIM_SAME_MONTH_FLD);
			}
			tmpSelect = tmpSelect.replaceAll(SubjectConst.DATA_TABLE_MID_VIR_NAME + "\\.",
					SubjectConst.DATA_TABLE_BASE_LAST_VIR_NAME + ".");
			tmpSelect = tmpSelect.replaceAll(" " + SubjectConst.DATA_TABLE_MID_VIR_NAME + ",", " "
					+ SubjectConst.DATA_TABLE_BASE_LAST_VIR_NAME + ",");
			tmpSelect = tmpSelect.replaceAll(SubjectConst.SUBJECT_TABLE_DATASOURCE, subTable.data_table);
			from.append(" LEFT JOIN ").append(tmpSelect);

			// 这里的::otherSelectFlds要替换成主表不包括比率的除时间字段外的字段
			from.append(SubjectConst.DATA_TABLE_LAST_VIR_NAME);
			from.append(" ON");
			tmpSelect = virTableName + "." + subTable.time_field;
			from.append(" ").append(tmpSelect).append("=").append(SubjectConst.DATA_TABLE_LAST_VIR_NAME).append(".")
					.append(subTable.time_field + "_1");

			from.append(on);
		}

		// 关联环比
		if (hasLoopRatio) {

			if (from.length() <= 0) {
				tmpSelect = dataSelect.toString();
				tmpSelect = SubjectStringUtil.replaceVirTabName(tmpSelect, SubjectConst.DATA_TABLE_BASE_VIR_NAME);
				tmpSelect = tmpSelect.replaceAll(SubjectConst.SUBJECT_TABLE_DATASOURCE, subTable.data_table);
				from = new StringBuffer(" FROM (").append(tmpSelect).append(") ").append(virTableName);
			}

			tmpSelect = ratioSelect.toString();

			if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_DAY_LEVEL)) {
				tmpSelect = tmpSelect.replaceAll(SubjectConst.RATIO_TMP_FLD, SubjectConst.TIME_DIM_LAST_DAY_FLD);
			}
			if (subTable.time_level.equalsIgnoreCase(SubjectConst.DATA_MONTH_LEVEL)) {
				tmpSelect = tmpSelect.replaceAll(SubjectConst.RATIO_TMP_FLD, SubjectConst.TIME_DIM_LAST_MONTH_FLD);
			}
			tmpSelect = tmpSelect.replaceAll(SubjectConst.DATA_TABLE_MID_VIR_NAME + "\\.",
					SubjectConst.DATA_TABLE_BASE_LOOP_VIR_NAME + ".");
			tmpSelect = tmpSelect.replaceAll(" " + SubjectConst.DATA_TABLE_MID_VIR_NAME + ",", " "
					+ SubjectConst.DATA_TABLE_BASE_LOOP_VIR_NAME + ",");
			tmpSelect = tmpSelect.replaceAll(SubjectConst.SUBJECT_TABLE_DATASOURCE, subTable.data_table);
			from.append(" LEFT JOIN ").append(tmpSelect);

			// 这里的::otherSelectFlds要替换成主表不包括比率的除时间字段外的字段
			from.append(SubjectConst.DATA_TABLE_LOOP_VIR_NAME);
			from.append(" ON");
			tmpSelect = virTableName + "." + subTable.time_field;
			from.append(" ").append(tmpSelect).append("=").append(SubjectConst.DATA_TABLE_LOOP_VIR_NAME).append(".")
					.append(subTable.time_field + "_1");

			tmpSelect = on.toString();
			tmpSelect = tmpSelect.replaceAll(SubjectConst.DATA_TABLE_LAST_VIR_NAME + "\\.",
					SubjectConst.DATA_TABLE_LOOP_VIR_NAME + ".");
			from.append(tmpSelect);
		}

		return from.toString();
	}
}
