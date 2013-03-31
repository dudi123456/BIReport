package com.ailk.bi.olap.service.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;
import com.ailk.bi.olap.service.dao.IReportContentDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDataRight;
import com.ailk.bi.olap.util.RptOlapDateUtil;
import com.ailk.bi.olap.util.RptOlapDimUtil;
import com.ailk.bi.olap.util.RptOlapMsuUtil;
import com.ailk.bi.olap.util.RptOlapStringUtil;

@SuppressWarnings({ "rawtypes" })
public class ReportContentDao implements IReportContentDao {

	/**
	 * 是否有分组
	 */
	private boolean hasGroupBy = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IReportContentDao#getContent(java.util.List,
	 * com.asiabi.base.table.PubInfoResourceTable,
	 * com.asiabi.olap.domain.RptOlapDateStruct,
	 * com.asiabi.olap.domain.RptOlapFunc)
	 */
	public String[][] getContent(List tableCols, PubInfoResourceTable report,
			RptOlapDateStruct ds, RptOlapFuncStruct olapFun,
			UserCtlRegionStruct userCtl, String svckndRight)
			throws ReportOlapException {
		String[][] svces = null;
		if (null == tableCols || tableCols.size() <= 0 || null == report
				|| null == ds || null == olapFun)
			throw new ReportOlapException("从数据库获取内容时输入参数为空");
		long startTime = System.currentTimeMillis();
		String select = genSelectSQL(tableCols, report, ds, olapFun, userCtl,
				svckndRight, false, "0");
		System.out.println("组装SQL用时:"
				+ (System.currentTimeMillis() - startTime) + "ms");
		try {
			System.out.println("olap select=" + select);
			startTime = System.currentTimeMillis();
			svces = WebDBUtil.execQryArray(select, "");
			if (!hasGroupBy) {
				// 需要添加一行合计，此时应该就一行
				String[][] tmpSvces = new String[2][svces[0].length];
				System.arraycopy(svces[0], 0, tmpSvces[0], 0, svces[0].length);
				System.arraycopy(svces[0], 0, tmpSvces[1], 0, svces[0].length);
				System.out.println("数据库查询用时:"
						+ (System.currentTimeMillis() - startTime) + "ms");
				return tmpSvces;
			}
			System.out.println("数据库查询用时:"
					+ (System.currentTimeMillis() - startTime) + "ms");
		} catch (AppException ae) {
			throw new ReportOlapException("获取分析型表格内容出错", ae);
		}
		return svces;
	}

	/**
	 * 生成查询语句
	 * 
	 * @param tableCols
	 *            列域对象列表
	 * @param report
	 *            报表对象
	 * @param ds
	 *            日期对象
	 * @param olapFun
	 *            当前功能对象
	 * @return 查询的语句
	 * @throws ReportOlapException
	 */
	private String genSelectSQL(List tableCols, PubInfoResourceTable report,
			RptOlapDateStruct ds, RptOlapFuncStruct olapFun,
			UserCtlRegionStruct userCtl, String svckndRight,
			boolean multiExpand, String count) throws ReportOlapException {
		if (null == tableCols || tableCols.size() <= 0 || null == report
				|| null == ds || null == olapFun)
			throw new IllegalArgumentException("从数据库获取内容时输入参数为空");
		// 由于union all 不支持 order by 子句，这里不用了，排序用程序进行
		StringBuffer selectSQL = null;

		StringBuffer select = new StringBuffer("SELECT ");
		StringBuffer subSelect = new StringBuffer();
		StringBuffer dimSelect = new StringBuffer("");
		StringBuffer msuSelect = new StringBuffer("");
		StringBuffer having = new StringBuffer("");
		StringBuffer joinSelect = new StringBuffer("");
		StringBuffer from = new StringBuffer(" FROM ");
		StringBuffer where = new StringBuffer(" WHERE 1=1 ");
		StringBuffer groupby = new StringBuffer("");
		// StringBuffer orderby = new StringBuffer(" ORDER BY ");

		String dataVirTabeName = RptOlapConsts.THIS_VIR_TMP_TAB_NAME;
		from.append(report.data_table).append(" " + dataVirTabeName + ",");
		String dataWhere = report.data_where;
		if (null != dataWhere && !"".equals(dataWhere)) {
			dataWhere = dataWhere.toUpperCase();
			dataWhere = dataWhere.replaceAll("WHERE 1=1", "");
			dataWhere = dataWhere.replaceAll("WHERE", "");
			dataWhere = RptOlapStringUtil.replaceVirTabName(dataWhere,
					dataVirTabeName);
			if (null != dataWhere && !"".equals(dataWhere))
				where.append(" AND ").append(dataWhere);
		}
		// 是否有累计值
		boolean hasSum = false;
		String func = olapFun.getCurFunc();
		if ((RptOlapConsts.OLAP_FUN_DATA.equals(func) || RptOlapConsts.RESET_MODE_EXPAND
				.equals(olapFun.getDisplayMode())) && olapFun.isHasSum()) {
			hasSum = true;
		}
		// 处理时间,有累计情况要从当前期
		String start = ds.getStart();
		String end = ds.getEnd();
		if (hasSum) {
			System.out.println("start_date=" + start);
			start = RptOlapStringUtil.getBeginDate(start, report.cycle);
		}
		// 获得数据的最小日期
		String dataStart = RptOlapDateUtil.getDataTableBeginDate(report.cycle,
				olapFun, start);

		RptOlapDimTable rptTime = null;
		RptOlapDimTable rptEvalDept = null;
		RptOlapDimTable rptSvcKnd = null;
		int dimCount = 0;
		int index = 0;
		if (null != count && !"".equals(count))
			dimCount = Integer.parseInt(count);
		// 由于时间维大,因此一定先将时间维关联,这里加上一个循环

		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			// 防止时间维不显示
			if (tCol.isTimeDim()) {
				// 提升时间维，因为有可能从没有展开到最细粒度，如同比、环比，
				// 而数据库查询不能使用 -1 作为别名
				rptTime = (RptOlapDimTable) tCol.getStruct();
				String upLevel = tCol.getDigLevel();
				if (RptOlapConsts.NO_DIGGED_LEVEL.equals(upLevel))
					upLevel = RptOlapConsts.ZERO_STR;

				// A0
				// 数据表条件加上时间范围
				where.append(" AND ")
						.append(dataVirTabeName + "."
								+ rptTime.dimInfo.code_idfld).append(">=")
						.append(dataStart);
				where.append(" AND ")
						.append(dataVirTabeName + "."
								+ rptTime.dimInfo.code_idfld).append("<=")
						.append(end);
				// 先将时间查询语句关联
				joinSelect.append(genTimeSQL(tCol, olapFun, report.cycle,
						start, end));

				// 当前为同比或环比分析
				if (RptOlapConsts.OLAP_FUN_SAME.equals(func)
						|| RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
					// 需要设置时间维的钻取层次为最后层次了
					tCol.setDigLevel(RptOlapConsts.NO_NEXT_LEVEL);
				}

				// 时间肯定要选出来，因为要进行关联
				dimSelect.append(dataVirTabeName + ".")
						.append(rptTime.dimInfo.code_idfld).append(",");
			}
		}
		iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDim()) {
				RptOlapDimTable tmpDim = (RptOlapDimTable) tCol.getStruct();
				if (RptOlapConsts.YES.equalsIgnoreCase(tmpDim.is_evaldept))
					rptEvalDept = tmpDim;
				if (RptOlapConsts.YES.equalsIgnoreCase(tmpDim.is_svcknd))
					rptSvcKnd = tmpDim;
			}
			if (tCol.isDisplay()) {
				if (tCol.isDim()) {
					// 时间维在同比或环比时需要特殊处理，不是时不需要
					Map dimParts = RptOlapDimUtil.genDimSelectParts(tCol,
							olapFun, report.cycle, ds);
					if (null != dimParts) {
						// 这里要判断
						if (multiExpand) {
							if (index == dimCount) {
								// 相等时，选出来
								select.append((String) dimParts.get("select"));
								groupby.append((String) dimParts.get("groupby"));
							}
						} else {
							select.append((String) dimParts.get("select"));
							groupby.append((String) dimParts.get("groupby"));
						}
						if (!tCol.isTimeDim())
							if (multiExpand) {
								if (index <= dimCount) {
									dimSelect.append((String) dimParts
											.get("subSelect"));
								}
							} else {
								dimSelect.append((String) dimParts
										.get("subSelect"));
							}
						if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
								.getDigLevel())) {
							if (multiExpand) {
								if (index <= dimCount) {
									// 这是应该有值
									joinSelect.append((String) dimParts
											.get("joinSQL"));
								}
							} else {
								joinSelect.append((String) dimParts
										.get("joinSQL"));
							}
						}
						// orderby.append((String)
						// dimParts.get("orderby"));
					}
					index++;
				} else {
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
					Map msuParts = RptOlapMsuUtil.genMsuSelectParts(
							rptMsu.msuInfo, hasSum, ds.getStart(),
							rptTime.dimInfo.code_idfld, olapFun, report.cycle);
					select.append((String) msuParts.get("select"));
					having.append((String) msuParts.get("having"));
					subSelect.append((String) msuParts.get("select"));
					msuSelect.append((String) msuParts.get("subSelect"));
					groupby.append((String) msuParts.get("groupby"));
					// orderby.append((String) msuParts.get("orderby"));
				}
			}
		}
		// msuSelect需要去除重复
		msuSelect = new StringBuffer(RptOlapStringUtil.clearDupFld(msuSelect
				.toString()));
		// 开始判断，是否累计、是否同比、是否环比等,还有合计(把最后一个 group by 去掉)
		selectSQL = new StringBuffer("");

		String rightJoinSQL = RptOlapDataRight.genEvalDeptJoinSQL(rptEvalDept,
				userCtl, RptOlapConsts.THIS_VIR_TAB_NAME);
		String rightSvcKndSQL = RptOlapDataRight.genSvcKndJoinSQL(rptSvcKnd,
				svckndRight, RptOlapConsts.THIS_VIR_TMP_TAB_NAME);

		// 数据表查询语句
		StringBuffer dataSelect = new StringBuffer();
		boolean addRightFld = ((null == rightJoinSQL || "".equals(rightJoinSQL)) ? false
				: true);
		dataSelect.append(genDataSelect(dimSelect, msuSelect, from, where,
				rptEvalDept, userCtl, addRightFld, rightSvcKndSQL));
		// 累计

		// 查询部分
		selectSQL.append(RptOlapStringUtil.removeLastSubStr(select.toString(),
				","));
		// sumSelectSQL.append(RptOlapStringUtil.removeLastSubStr(subSelect
		// .toString(), ","));
		selectSQL.append(" FROM ");
		// sumSelectSQL.append(" FROM ");
		// 数据部分
		selectSQL.append(dataSelect);
		// sumSelectSQL.append(dataSelect);
		// 获取数据权限
		joinSelect.append(rightJoinSQL);
		// 维表关联部分
		selectSQL.append(joinSelect);
		// sumSelectSQL.append(joinSelect);
		String dimHaving = null;
		// 分组部分,要判断没有分组情况
		if (groupby.indexOf(",") >= 0) {
			String tmpStr = groupby.toString();
			tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, ",");
			selectSQL.append(" GROUP BY ROLLUP(");
			selectSQL.append(tmpStr);
			selectSQL.append(")");
			// 这是需要生成过虑子合计
			dimHaving = genDimHaving(tmpStr);
			hasGroupBy = true;
		} else {
			// 根本没有分组，这时需要设置个变量
			hasGroupBy = false;
		}
		// 要不要剔除0值行
		if (RptOlapConsts.YES.equals(report.filter_zerorow)) {
			// 要剔除
			selectSQL.append(genHavingPart(having));
			if (null != dimHaving && dimHaving.length() > 0)
				selectSQL.append(" AND (").append(dimHaving).append(")");
		} else {
			if (null != dimHaving && dimHaving.length() > 0)
				selectSQL.append(" HAVING (").append(dimHaving).append(")");
		}
		// 至此语句组装完毕，下面合并
		// String tmpStr = selectSQL.toString();
		// selectSQL.delete(0, selectSQL.length());
		// selectSQL.append("(").append(tmpStr).append(") UNION ALL(").append(
		// sumSelectSQL).append(")");
		return selectSQL.toString();
	}

	private String genDimHaving(String groupby) {
		StringBuffer having = new StringBuffer();
		if (null != groupby) {
			String[] dims = groupby.split(",");
			if (dims.length > 0) {
				having.append("(");
				StringBuffer sbNotNull = new StringBuffer();
				StringBuffer sbNull = new StringBuffer();
				for (int i = 0; i < dims.length; i++) {
					sbNotNull.append(dims[i]).append(" IS NOT NULL AND ");
					sbNull.append(dims[i]).append(" IS NULL AND ");
				}
				if (sbNotNull.lastIndexOf(" AND ") > 0)
					having.append(RptOlapStringUtil.removeLastSubStr(
							sbNotNull.toString(), " AND "));
				having.append(")");
				if (sbNull.length() > 0) {
					having.append(" OR (");
					if (sbNull.lastIndexOf(" AND ") > 0)
						having.append(RptOlapStringUtil.removeLastSubStr(
								sbNull.toString(), " AND "));
					having.append(")");
				}
			}
		}
		return having.toString();
	}

	public RptOlapFuncStruct setOlapFunc(List tableCols,
			RptOlapFuncStruct olapFun) throws ReportOlapException {
		RptOlapFuncStruct fun = null;
		if (null == tableCols)
			throw new IllegalArgumentException("设置分析型报表当前功能" + "及状态时输入参数为空");
		if (null == olapFun) {
			fun = new RptOlapFuncStruct();
			fun.setCurFunc(RptOlapConsts.OLAP_FUN_DATA);
		} else {
			fun = olapFun;
		}
		boolean hasSum = false;
		boolean hasAlert = false;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (!tCol.isDim() && tCol.isDisplay()) {
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
				if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate)) {
					hasSum = true;
				}
				if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.need_alert)) {
					hasAlert = true;
				}
			}
		}
		fun.setHasAlert(hasAlert);
		fun.setHasSum(hasSum);
		return fun;
	}

	public String[][] getExpandContent(List tableCols,
			PubInfoResourceTable report, RptOlapDateStruct ds,
			RptOlapFuncStruct olapFun, UserCtlRegionStruct userCtl,
			String svckndRight, String level, boolean singleDim)
			throws ReportOlapException {
		String[][] svces = null;
		if (null == tableCols || null == report || null == ds
				|| null == olapFun || null == level)
			throw new IllegalArgumentException("生成折叠、展开表格内容时输入的参数为空");
		String select = "";
		long startTime = System.currentTimeMillis();
		if (singleDim) {
			// 单维度,不同层次、就靠tableCols来设置了
			select = genSelectSQL(tableCols, report, ds, olapFun, userCtl,
					svckndRight, false, "0");

			System.out.println(select);
		} else {
			// 多维度单层次
			select = genSelectSQL(tableCols, report, ds, olapFun, userCtl,
					svckndRight, true, level);
			System.out.println(select);
		}
		System.out.println("组装SQL用时:"
				+ (System.currentTimeMillis() - startTime) + "ms");
		try {
			startTime = System.currentTimeMillis();
			svces = WebDBUtil.execQryArray(select, "");
			if (!hasGroupBy) {
				// 需要添加一行合计，此时应该就一行
				String[][] tmpSvces = new String[2][svces[0].length];
				System.arraycopy(svces[0], 0, tmpSvces[0], 0, svces[0].length);
				System.arraycopy(svces[0], 0, tmpSvces[1], 0, svces[0].length);
				System.out.println("数据库查询用时:"
						+ (System.currentTimeMillis() - startTime) + "ms");
				return tmpSvces;
			}
			System.out.println("数据库用时:"
					+ (System.currentTimeMillis() - startTime) + "ms");
		} catch (AppException ae) {
			throw new ReportOlapException("生成折叠、展开表格内容发生数据库错误", ae);
		}
		return svces;
	}

	private String genTimeSQL(RptOlapTableColumnStruct tCol,
			RptOlapFuncStruct olapFun, String statPeriod, String begDate,
			String endDate) throws ReportOlapException {
		if (null == tCol || null == olapFun || null == statPeriod
				|| "".equals(statPeriod) || null == begDate || null == endDate
				|| "".equals(begDate) || "".equals(endDate))
			throw new ReportOlapException("生成时间查询部分时输入的参数为空");

		StringBuffer timeSQL = new StringBuffer("");
		RptOlapDimTable rptTime = (RptOlapDimTable) tCol.getStruct();
		String timeFld = rptTime.dimInfo.code_idfld;
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;
		String func = olapFun.getCurFunc();

		if (olapFun.isHasAlert() && olapFun.isHasSameRatioAlert())
			hasSameAlert = true;
		if (olapFun.isHasAlert() && olapFun.isHasLastRatioAlert())
			hasLastAlert = true;

		timeSQL.append(" JOIN ( SELECT ");
		timeSQL.append(timeFld).append(" AS ").append(timeFld).append(",");
		timeSQL.append(timeFld).append(" AS ")
				.append(RptOlapConsts.SQL_TIME_JOIN_FLD).append(",");
		timeSQL.append(RptOlapConsts.SQL_THIS_PERIOD).append(" AS ")
				.append(RptOlapConsts.SQL_PERIOD_TYPE);
		timeSQL.append(" FROM ").append(rptTime.dimInfo.dim_table);
		timeSQL.append(" WHERE ").append(timeFld).append(">=").append(begDate);
		timeSQL.append(" AND ").append(timeFld).append("<=").append(endDate);

		if (hasSameAlert || RptOlapConsts.OLAP_FUN_SAME.equals(func)) {
			String sameFun = olapFun.getSameRatioPeriod();
			if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod)
					&& RptOlapConsts.OLAP_FUN_SAME.equals(func)
					&& (RptOlapConsts.SAME_RATIO_BOTH_PERIOD.equals(sameFun) || RptOlapConsts.SAME_RATIO_WEEK_PERIOD
							.equals(sameFun))) {
				// 数据粒度为日的周同比
				timeSQL.append(" UNION ALL SELECT ");
				timeSQL.append(timeFld).append(" AS ").append(timeFld)
						.append(",");
				timeSQL.append(RptOlapConsts.TIME_DIM_SAME_WEEK_DAY_FLD)
						.append(" AS ").append(RptOlapConsts.SQL_TIME_JOIN_FLD)
						.append(",");
				timeSQL.append(RptOlapConsts.SQL_SAME_WEEK_PERIOD)
						.append(" AS ").append(RptOlapConsts.SQL_PERIOD_TYPE);
				timeSQL.append(" FROM ").append(rptTime.dimInfo.dim_table);
				timeSQL.append(" WHERE ").append(timeFld).append(">=")
						.append(begDate);
				timeSQL.append(" AND ").append(timeFld).append("<=")
						.append(endDate);
			}
			// 月同比
			if (!(RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod) && RptOlapConsts.SAME_RATIO_WEEK_PERIOD
					.equals(sameFun))) {
				timeSQL.append(" UNION ALL SELECT ");
				timeSQL.append(timeFld).append(" AS ").append(timeFld)
						.append(",");
				if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod))
					timeSQL.append(RptOlapConsts.TIME_DIM_SAME_DAY_FLD)
							.append(" AS ")
							.append(RptOlapConsts.SQL_TIME_JOIN_FLD)
							.append(",");
				else
					timeSQL.append(RptOlapConsts.TIME_DIM_SAME_MONTH_FLD)
							.append(" AS ")
							.append(RptOlapConsts.SQL_TIME_JOIN_FLD)
							.append(",");
				timeSQL.append(RptOlapConsts.SQL_SAME_PERIOD).append(" AS ")
						.append(RptOlapConsts.SQL_PERIOD_TYPE);
				timeSQL.append(" FROM ").append(rptTime.dimInfo.dim_table);
				timeSQL.append(" WHERE ").append(timeFld).append(">=")
						.append(begDate);
				timeSQL.append(" AND ").append(timeFld).append("<=")
						.append(endDate);
			}
		}
		if (hasLastAlert || RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
			// 环比
			timeSQL.append(" UNION ALL SELECT ");
			timeSQL.append(timeFld).append(" AS ").append(timeFld).append(",");
			if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(statPeriod))
				timeSQL.append(RptOlapConsts.TIME_DIM_LAST_DAY_FLD)
						.append(" AS ").append(RptOlapConsts.SQL_TIME_JOIN_FLD)
						.append(",");
			else
				timeSQL.append(RptOlapConsts.TIME_DIM_LAST_MONTH_FLD)
						.append(" AS ").append(RptOlapConsts.SQL_TIME_JOIN_FLD)
						.append(",");
			timeSQL.append(RptOlapConsts.SQL_LAST_PERIOD).append(" AS ")
					.append(RptOlapConsts.SQL_PERIOD_TYPE);
			timeSQL.append(" FROM ").append(rptTime.dimInfo.dim_table);
			timeSQL.append(" WHERE ").append(timeFld).append(">=")
					.append(begDate);
			timeSQL.append(" AND ").append(timeFld).append("<=")
					.append(endDate);
		}
		timeSQL.append(" ) ").append(RptOlapConsts.TIME_VIR_TAB_NAME)
				.append(" ON ");
		timeSQL.append(RptOlapConsts.THIS_VIR_TAB_NAME + "." + timeFld)
				.append("=")
				.append(RptOlapConsts.TIME_VIR_TAB_NAME + "."
						+ RptOlapConsts.SQL_TIME_JOIN_FLD);
		timeSQL.append(" ");
		return timeSQL.toString();
	}

	private StringBuffer genDataSelect(StringBuffer dimSelect,
			StringBuffer msuSelect, StringBuffer from, StringBuffer where,
			RptOlapDimTable rptEvalDept, UserCtlRegionStruct userCtl,
			boolean addRightFld, String svcKndRightSQL)
			throws ReportOlapException {
		if (null == dimSelect || null == msuSelect || null == from
				|| null == where)
			throw new ReportOlapException("组装数据表查询语句时输入的参数为空");
		StringBuffer select = new StringBuffer();
		select.append("( SELECT ").append(dimSelect);
		String coldFld = null;
		if (null != rptEvalDept && null != userCtl && addRightFld) {
			// 加上权限过虑
			coldFld = RptOlapDimUtil.getDimDataFld(rptEvalDept);
			coldFld = RptOlapStringUtil.replaceVirTabName(coldFld,
					RptOlapConsts.THIS_VIR_TMP_TAB_NAME);
			if (dimSelect.indexOf(coldFld) < 0)
				select.append(coldFld).append(",");
		}
		select.append(RptOlapStringUtil.removeLastSubStr(msuSelect.toString(),
				","));
		select.append(" ").append(
				RptOlapStringUtil.removeLastSubStr(from.toString(), ","));
		if (null != svcKndRightSQL && !"".equals(svcKndRightSQL))
			select.append(svcKndRightSQL);
		select.append(" ").append(where);
		select.append(" GROUP BY ").append(
				RptOlapStringUtil.removeLastSubStr(dimSelect.toString(), ","));
		if (null != rptEvalDept && null != userCtl && addRightFld) {
			if (dimSelect.indexOf(coldFld) < 0)
				select.append(",").append(coldFld);
		}
		select.append(" ) ").append(RptOlapConsts.THIS_VIR_TAB_NAME)
				.append(" ");
		return select;
	}

	private StringBuffer genHavingPart(StringBuffer having)
			throws ReportOlapException {
		if (null == having)
			throw new ReportOlapException("生成查询语句的HAVING部分时输入的参数为空");
		StringBuffer retHaving = new StringBuffer();
		retHaving.append(" HAVING ");
		retHaving.append(RptOlapStringUtil.removeLastSubStr(having.toString(),
				"+"));
		retHaving.append(">0");
		return retHaving;
	}
}
