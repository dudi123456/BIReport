package com.ailk.bi.olap.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.table.RptOlapChartTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;

@SuppressWarnings({ "rawtypes" })
public class RptOlapDateUtil {

	public static RptOlapDateStruct genOlapChartDateDomain(
			HttpServletRequest request, RptOlapDateStruct ds,
			List chartStructs, PubInfoResourceTable report,
			RptOlapFuncStruct olapFun, boolean firstAccess)
			throws ReportOlapException {
		if (null == request || null == chartStructs || chartStructs.size() <= 0
				|| null == report || null == olapFun)
			throw new ReportOlapException("根据请求生成日期对象时输入的参数为空");
		RptOlapDateStruct retDs = ds;
		// 根据标准自定义设置
		String func = olapFun.getCurFunc();
		String due = null;
		String time_fld = "";
		RptOlapChartAttrStruct chartStruct = null;
		Iterator iter = chartStructs.iterator();
		while (iter.hasNext()) {
			chartStruct = (RptOlapChartAttrStruct) iter.next();
			if (chartStruct.isDim() && chartStruct.isTime()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
						.getRptStruct();
				time_fld = rptDim.dimInfo.code_idfld;
			}
			if (!chartStruct.isDim() && chartStruct.isDisplay()
					&& !chartStruct.isUsedForGroup()) {
				// 找到这个唯一的，因此每周图形方式必须有,这个也不保险，可以没有分组了
				// 找指标吧
				Map settings = chartStruct.getSets();
				if (null != settings) {
					Object tmpObj = settings.get(func);
					if (null != tmpObj) {
						RptOlapChartTable set = (RptOlapChartTable) tmpObj;
						due = set.chartTime.time_due;
					}
				}
			}
		}
		if (null == due)
			due = RptOlapConsts.ZERO_STR;
		if (null == retDs) {
			// 初始化一个
			retDs = new RptOlapDateStruct();
			retDs.setStart(getBeginDate(report, time_fld));
			retDs.setEnd(retDs.getStart());
		}
		// 开始判断了
		String end = retDs.getEnd();
		String start = retDs.getStart();
		int diff = Integer.parseInt(due);
		diff = -diff;
		if (!RptOlapConsts.ZERO_STR.equals(due)) {
			// 前推时间不为0
			if (RptOlapConsts.RPT_STATIC_MONTH_PERIOD.equals(report.cycle))
				start = com.ailk.bi.common.app.DateUtil.getDiffMonth(diff,
						end);
			if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(report.cycle))
				start = com.ailk.bi.common.app.DateUtil
						.getDiffDay(diff, end);
		} else if (RptOlapConsts.OLAP_FUN_LINE.equals(func)) {
			// 前推时间为0，趋势要重新设置
			if (RptOlapConsts.RPT_STATIC_MONTH_PERIOD.equals(report.cycle))
				start = end.substring(0, 4) + "01";
			if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(report.cycle))
				start = end.substring(0, 6) + "01";
		}
		retDs.setStart(start);
		retDs.setEnd(end);
		// 根据用户请求分析
		// 看看用户选择了新的日期没有
		String startDate = request.getParameter(RptOlapConsts.REQ_START_DATE);
		if (null != startDate && !"".equals(startDate)) {
			retDs.setStart(startDate);
		}
		String endDate = request.getParameter(RptOlapConsts.REQ_END_DATE);
		if (null != endDate && !"".equals(endDate)) {
			retDs.setEnd(endDate);
		}
		if (firstAccess) {
			if (!StringUtils.isBlank(endDate)
					&& !StringUtils.isBlank(startDate)
					&& endDate.equals(startDate)) {
				if (!RptOlapConsts.OLAP_FUN_LINE.equals(func)) {
					retDs.setStart(retDs.getEnd());
				} else {
					retDs.setStart(start);
				}
			}

		}
		return retDs;
	}

	/**
	 * 根据请求中的参数生成日期对象
	 *
	 * @param request
	 *            请求对象
	 * @return
	 */
	public static RptOlapDateStruct genOlapDateDomain(
			HttpServletRequest request, RptOlapDateStruct ds, List tabCols,
			PubInfoResourceTable report) throws ReportOlapException {
		if (null == request || null == tabCols || tabCols.size() <= 0
				|| null == report)
			throw new ReportOlapException("根据请求生成日期对象时输入的参数为空");
		RptOlapDateStruct retDs = ds;
		String time_fld = "";
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tabCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tabCol.isDim() && tabCol.isTimeDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) tabCol.getStruct();
				time_fld = rptDim.dimInfo.code_idfld;
				break;
			}
		}
		if (null == retDs) {
			// 初始化一个
			retDs = new RptOlapDateStruct();
			retDs.setStart(getBeginDate(report, time_fld));
			retDs.setEnd(retDs.getStart());
		}
		// 看看用户选择了新的日期没有
		String startDate = request.getParameter(RptOlapConsts.REQ_START_DATE);
		if (null != startDate && !"".equals(startDate)) {
			retDs.setStart(startDate);
		}
		String endDate = request.getParameter(RptOlapConsts.REQ_END_DATE);
		if (null != endDate && !"".equals(endDate)) {
			retDs.setEnd(endDate);
		}
		return retDs;
	}

	/**
	 * 生成开始日期
	 *
	 * @param report
	 *            报表对象
	 * @param time_fld
	 *            时间字段
	 * @return
	 */
	private static String getBeginDate(PubInfoResourceTable report,
			String time_fld) throws ReportOlapException {
		if (null == report || null == time_fld)
			throw new ReportOlapException("生成日期对象开始日期时输入参数为空");
		String beginDate = null;
		String select = "";
		select = " SELECT MAX(A." + RptOlapConsts.DATA_TABLE_TIME_FLD
				+ ") FROM " + RptOlapConsts.DATA_TABLE_TIME_VIEW + " A WHERE "
				+ RptOlapConsts.DATA_TABLE_TIME_TABLE_NAME_FLD + "='"
				+ report.data_table + "'";

		if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equalsIgnoreCase(report.cycle)) {
			select += " AND " + RptOlapConsts.DATA_TABLE_TIME_PERIOD_FLD + "='"
					+ RptOlapConsts.DATA_TABLE_TIME_DAY + "'";
			beginDate = DateUtil.getDataDay(select, "yyyyMMdd");
			if (beginDate == null) {
				beginDate = DateUtil.getDiffDay(-1, DateUtil.getNowDate());
			}
		} else {
			select += " AND " + RptOlapConsts.DATA_TABLE_TIME_PERIOD_FLD + "='"
					+ RptOlapConsts.DATA_TABLE_TIME_MONTH + "'";
			// select="SELECT MAX(GATHER_MON) FROM FM_I_INTE_USER_M";
			beginDate = DateUtil.getDataMonth(select, "yyyyMM");
			if (beginDate == null) {
				beginDate = DateUtil.getDiffMonth(-1, DateUtil.getNowDate());
			}
		}
		return beginDate;
	}

	public static String getDataTableBeginDate(String statPeriod,
			RptOlapFuncStruct olapFun, String begDate)
			throws ReportOlapException {
		if (null == statPeriod || "".equals(statPeriod) || null == olapFun
				|| null == begDate || "".equals(begDate))
			throw new ReportOlapException("生成数据表查询用的最小开始日期时输入的参数为空");
		String dataBeginDate = begDate;
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;

		String func = olapFun.getCurFunc();
		if (olapFun.isHasAlert() && olapFun.isHasSameRatioAlert())
			hasSameAlert = true;
		if (olapFun.isHasAlert() && olapFun.isHasLastRatioAlert())
			hasLastAlert = true;
		if (RptOlapConsts.RPT_STATIC_MONTH_PERIOD.equals(statPeriod)) {
			// 月表，相对简单
			if (hasSameAlert || RptOlapConsts.OLAP_FUN_SAME.equals(func)) {
				// 有同比预警，
				// 前推12个月
				dataBeginDate = com.ailk.bi.common.app.DateUtil
						.getDiffMonth(-12, begDate);
				return dataBeginDate;
			}
			if (hasLastAlert || RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
				// 前推1个月
				dataBeginDate = com.ailk.bi.common.app.DateUtil
						.getDiffMonth(-1, begDate);
				return dataBeginDate;
			}
			// 如果是累计或其他情况直接返回初始值
		} else {
			// 日表
			if (hasSameAlert || RptOlapConsts.OLAP_FUN_SAME.equals(func)) {
				// 有同比预警，
				// 前推31天，一个月最多31天
				dataBeginDate = com.ailk.bi.common.app.DateUtil.getDiffDay(
						-31, begDate);
				return dataBeginDate;
			}
			if (hasLastAlert || RptOlapConsts.OLAP_FUN_LAST.equals(func)) {
				// 前推1天
				dataBeginDate = com.ailk.bi.common.app.DateUtil.getDiffDay(
						-1, begDate);
				return dataBeginDate;
			}
			// 如果是累计或其他情况直接返回初始值
		}
		return dataBeginDate;
	}

}
