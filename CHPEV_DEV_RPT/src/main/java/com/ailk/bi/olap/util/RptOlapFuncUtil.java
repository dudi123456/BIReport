package com.ailk.bi.olap.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.base.table.RptUserOlapTable;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptOlapFuncUtil {
	public static RptOlapFuncStruct genOlapFuncDomain(
			HttpServletRequest request, RptOlapFuncStruct func)
			throws ReportOlapException {
		if (null == request)
			throw new ReportOlapException("组装合并功能域对象时输入的参数为空");
		RptOlapFuncStruct olapFunc = func;
		if (null == olapFunc) {
			// 如果第一次为空，则立刻生成
			olapFunc = new RptOlapFuncStruct();
			olapFunc.setCurFunc(RptOlapConsts.OLAP_FUN_LINE);
		}
		String cur_olap_fun = request.getParameter(RptOlapConsts.REQ_OLAP_FUNC);
		if (null == cur_olap_fun || "".equals(cur_olap_fun))
			cur_olap_fun = RptOlapConsts.OLAP_FUN_LINE;
		olapFunc.setCurFunc(cur_olap_fun);
		return olapFunc;
	}

	/**
	 * 设置用户的收藏的分析型报表的最初状态
	 *
	 * @param olapFunc
	 * @param cusReport
	 * @param customDims
	 * @return
	 * @throws ReportOlapException
	 */
	public static RptOlapFuncStruct setUserCustomInitFunc(
			RptOlapFuncStruct olapFunc, RptUserOlapTable cusReport,
			List tableCols) throws ReportOlapException {
		if (null == olapFunc || null == cusReport || null == tableCols
				|| 0 >= tableCols.size())
			throw new ReportOlapException("设置用户自定义分析型报表时输入的参数为空");
		RptOlapFuncStruct olapFun = olapFunc;
		if (RptOlapConsts.CUSTOM_DISPLAY_DIG.equals(cusReport.display_mode))
			olapFun.setDisplayMode(RptOlapConsts.RESET_MODE_DIG);
		else {
			olapFun.setDisplayMode(RptOlapConsts.RESET_MODE_EXPAND);

			// 折叠、展开模式，
			// 设置全部累计、同比、占比模式都有
			olapFunc.setHasAlert(true);
			olapFunc.setHasSameRatioAlert(true);
			olapFunc.setHasLastRatioAlert(true);
			// 设置第一次展示,
			olapFunc.setFirstExpand(true);
			olapFun.setHasNewExpand(true);
			// 这里还要获取另外是单维度还是多维度
			int displayMsus = 0;
			RptOlapDimTable displayDim = null;
			int maxLevel = -1;
			Iterator iter = tableCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				if (tCol.isDisplay() && tCol.isDim()) {
					displayMsus++;
					RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
					displayDim = rptDim;
					// 对于单维度折叠、展开肯定是一个维度显示，
					// 用户也可能显示
					List levels = rptDim.dimInfo.dim_levels;
					if ((!RptOlapConsts.ZERO_STR.equals(rptDim.max_level)
							&& null != levels && 0 < levels.size())
							|| (tCol.isTimeDim() && null != rptDim.timeLevel)) {
						// 这样防止后台设置错误
						// 取得最大层次
						maxLevel = Integer.parseInt(rptDim.max_level);
						if (maxLevel > levels.size())
							maxLevel = levels.size();
						// 设置该维度最初显示
						if (tCol.isTimeDim()) {
							tCol.setDigLevel(RptOlapConsts.NO_DIGGED_LEVEL);
							String level = RptOlapDimUtil
									.getDimTimeNextLevel(tCol);
							tCol.setDigLevel(level);
							maxLevel = Integer.parseInt(rptDim.max_level);
						} else {
							// 如果是
							if (RptOlapConsts.NO_DIGGED_LEVEL.equals(tCol
									.getDigLevel()))
								tCol.setDigLevel(RptOlapConsts.ZERO_STR);
						}
					} else {
						// 每个都设置为初始展开
						tCol.setDigLevel(RptOlapConsts.ZERO_STR);
						maxLevel++;
					}
				}
			}
			olapFun.setSingleDimExpand(displayMsus <= 1 ? true : false);
			olapFun.setCurExpandLevel(getExpandInitLevel(tableCols));
			// 设置最大值
			olapFun.setMaxExpandLevel(maxLevel);
			if (olapFun.isSingleDimExpand()) {
				olapFunc.setMaxExpandLevel(Integer
						.parseInt(displayDim.max_level));
			}
			olapFunc.setCanUseCustomHead(false);
			olapFunc.setToUserLevel(MsuCanDigToUserLevel(tableCols));
		}
		return olapFun;
	}

	private static RptOlapFuncStruct setDisplayMode(RptOlapFuncStruct func,
			String resetMode, List tableCols, String singleOrMulti) {
		RptOlapFuncStruct olapFunc = func;
		if (null != resetMode && !"".equals(resetMode)) {
			// 需要将所有的values值设置为空
			olapFunc.setExpandedValues(null);
			olapFunc.setCanUseCustomHead(false);
			if (RptOlapConsts.RESET_MODE_DIG.equals(resetMode)) {
				olapFunc.setDisplayMode(RptOlapConsts.RESET_MODE_DIG);
			}
			if (RptOlapConsts.RESET_MODE_EXPAND.equals(resetMode)) {
				olapFunc.setDisplayMode(RptOlapConsts.RESET_MODE_EXPAND);
				// 折叠、展开模式，
				// 设置全部累计、同比、占比模式都有
				olapFunc.setHasAlert(true);
				olapFunc.setHasSameRatioAlert(true);
				olapFunc.setHasLastRatioAlert(true);
				// 设置第一次展示,
				olapFunc.setFirstExpand(true);
				//
				// 这里还要获取另外是单维度还是多维度

				if (null != singleOrMulti && !"".equals(singleOrMulti)) {
					if (RptOlapConsts.EXPAND_MULTI_DIM.equals(singleOrMulti))
						olapFunc.setSingleDimExpand(false);
					else
						olapFunc.setSingleDimExpand(true);
				}
				// 需要对时间维进行特殊设置
				olapFunc.setCurExpandLevel(getExpandInitLevel(tableCols));
			} else {
				olapFunc.setFirstExpand(false);
			}
		} else {
			olapFunc.setFirstExpand(false);
		}
		return olapFunc;
	}

	/**
	 * 根据请求中的信息生成功能结构
	 *
	 * @param request
	 *            请求对象
	 * @return
	 */
	public static RptOlapFuncStruct genOlapFuncDomain(
			HttpServletRequest request, RptOlapFuncStruct func, List tableCols,
			String statPeriod, List rptMsus) throws ReportOlapException {
		if (null == request || null == tableCols || null == statPeriod
				|| "".equals(statPeriod) || 0 >= tableCols.size()
				|| null == rptMsus || 0 >= rptMsus.size())
			throw new ReportOlapException("组装合并功能域对象时输入的参数为空");
		RptOlapFuncStruct olapFunc = func;
		if (null == olapFunc) {
			// 如果第一次为空，则立刻生成
			olapFunc = new RptOlapFuncStruct();
			olapFunc.setCurFunc(RptOlapConsts.OLAP_FUN_DATA);
			// 需要判断
			olapFunc.setHasSum(MsuHasSum(olapFunc, tableCols));
			olapFunc = MsuHasAlert(olapFunc, tableCols);
		}

		// 先保存一下之前的值
		boolean firstExpand = olapFunc.isFirstExpand();
		// 这时要根据请求的状态
		String cur_olap_fun = request.getParameter(RptOlapConsts.REQ_OLAP_FUNC);
		if (null == cur_olap_fun || "".equals(cur_olap_fun))
			cur_olap_fun = RptOlapConsts.OLAP_FUN_DATA;
		olapFunc.setCurFunc(cur_olap_fun);
		// 需要判断
		boolean hasSum = olapFunc.isHasSum();
		olapFunc.setHasSum(MsuHasSum(olapFunc, tableCols));
		if (RptOlapConsts.OLAP_FUN_PERCENT.equals(olapFunc.getCurFunc()))
			olapFunc.setHasSum(hasSum);

		// 此处设置
		olapFunc = MsuHasAlert(olapFunc, tableCols);
		// 设置一下排序功能
		String sort = request.getParameter(RptOlapConsts.REQ_SORT);
		if (null != sort && !"".equals(sort)) {
			olapFunc.setSort(sort);
			String sortThis = request.getParameter(RptOlapConsts.REQ_SORT_THIS);
			if (null == sortThis || "".equals(sortThis))
				sortThis = RptOlapConsts.SORT_THIS_PERIOD;
			olapFunc.setSortThis(sortThis);
			String sortOrder = request
					.getParameter(RptOlapConsts.REQ_SORT_ORDER);
			if (null != sortOrder && !"".equals(sortOrder))
				olapFunc.setSortOrder(sortOrder);
		} else {
			olapFunc.setSort(null);
		}
		String clickMsu = request.getParameter(RptOlapConsts.REQ_CLICK_MSU);
		if (null != clickMsu && !"".equals(clickMsu)) {
			olapFunc.setCanUseCustomHead(false);
			olapFunc.setClickMsu(clickMsu);
			String clickMusId = request
					.getParameter(RptOlapConsts.REQ_CLICK_MSU_ID);
			if (null != clickMusId && !"".equals(clickMusId)) {
				olapFunc.setClickMusId(clickMusId);
				olapFunc.setFirstExpand(true);
			}
		} else {
			olapFunc.setClickMsu(null);
			olapFunc.setClickMusId(null);
		}
		// 判断用户是否进行收缩指标
		String collapseMsuInit = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_MSU_INIT);
		if (null != collapseMsuInit && !"".equals(collapseMsuInit)
				&& RptOlapConsts.YES.equalsIgnoreCase(collapseMsuInit)) {
			// 直接收缩
			olapFunc.setCanUseCustomHead(true);
		}
		// 是否收缩一级且到了最顶层
		String collapseMsu = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_MSU);
		String collapseMsuId = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_MSU_ID);
		if (null != collapseMsu && null != collapseMsuId
				&& !"".equals(collapseMsu) && !"".equals(collapseMsuId)) {
			// 判断是否收缩到了最顶级
			boolean toInit = collapseToInitLevel(tableCols, collapseMsuId,
					rptMsus);
			if (toInit)
				olapFunc.setCanUseCustomHead(true);
		}
		// 判断一下是否用户进行了定制
		String resetMode = request.getParameter(RptOlapConsts.REQ_RESET_MODE);
		String singleOrMulti = request
				.getParameter(RptOlapConsts.REQ_EXPAND_MODE);
		olapFunc = setDisplayMode(olapFunc, resetMode, tableCols, singleOrMulti);
		//
		if (null != statPeriod && !"".equals(statPeriod)) {
			Iterator iter = tableCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				if (tCol.isDisplay() && tCol.isDim() && tCol.isTimeDim()) {
					RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
					if (RptOlapConsts.NO_NEXT_LEVEL.equals(tCol.getDigLevel())
							|| tCol.getDigLevel().equals(rptDim.max_level)) {
						// 时间到最细粒度
						if (!RptOlapConsts.RESET_MODE_EXPAND.equals(olapFunc
								.getDisplayMode()))
							olapFunc.setHasSum(false);
						break;
					}
				}
			}
		}
		// 设置最大值
		RptOlapDimTable displayDim = null;
		int maxExpandLevel = -1;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
				String level = request
						.getParameter(RptOlapConsts.REQ_DIM_LEVEL_PREFIX
								+ tCol.getIndex());
				if (null != level && !"".equals(level))
					maxExpandLevel++;
				else {
					String display = request
							.getParameter(RptOlapConsts.REQ_DIM_VALUE_PREFIX
									+ rptDim.dim_id + "_display");
					if (RptOlapConsts.TRUE.equalsIgnoreCase(display))
						maxExpandLevel++;
				}
				if (tCol.isDisplay())
					displayDim = rptDim;
			}
		}
		olapFunc.setMaxExpandLevel(maxExpandLevel);
		if (olapFunc.isSingleDimExpand() && null != displayDim) {
			// 这时设置一下
			olapFunc.setMaxExpandLevel(Integer.parseInt(displayDim.max_level));
		}
		// 获取当前层次
		String expandLevel = request
				.getParameter(RptOlapConsts.REQ_EXPAND_LEVEL);
		if (null != expandLevel && !"".equals(expandLevel)) {
			int level = 0;
			try {
				level = Integer.parseInt(expandLevel);
			} catch (NumberFormatException nfe) {

			}
			olapFunc.setCurExpandLevel(level);
			// 如果是单维度，需要设置一下那个维度的层次
			if (olapFunc.isSingleDimExpand()) {
				iter = tableCols.iterator();
				while (iter.hasNext()) {
					RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
							.next();
					if (tCol.isDisplay() && tCol.isDim()) {
						tCol.setDigLevel(expandLevel);
					}
				}
			}
		}
		// 获取各个层次的值
		int maxLevel = olapFunc.getMaxExpandLevel();
		Map levValues = new HashMap();
		for (int i = 0; i < maxLevel; i++) {
			String levValue = request
					.getParameter(RptOlapConsts.REQ_EXPAND_VALUE + i);
			if (null != levValue && !"".equals(levValue)) {
				levValues.put(i + "", levValue);
			}
		}
		if (levValues.size() > 0) {
			// 这里要设置各个层次的值
			olapFunc.setExpandedValues(levValues);
		}
		String collapseRowId = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_EXPAND);
		if (null != collapseRowId && !"".equals(collapseRowId)) {
			// 这里需要重新设置了
			String[] values = collapseRowId.split("_");
			String level = values[1];
			int iLevel = 0;
			try {
				iLevel = Integer.parseInt(level);
			} catch (NumberFormatException nfe) {

			}
			olapFunc.setCurExpandLevel(iLevel);
			Map expandValues = olapFunc.getExpandedValues();
			String preLevel = null;
			String value = null;

			// 如果是单维度，需要设置一下那个维度的层次
			if (olapFunc.isSingleDimExpand()) {
				iter = tableCols.iterator();
				while (iter.hasNext()) {
					RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
							.next();
					if (tCol.isDisplay() && tCol.isDim()) {
						tCol.setDigLevel(level);
						if (tCol.isTimeDim()) {
							preLevel = RptOlapDimUtil.getDimTimePreLevel(tCol);
						} else
							preLevel = RptOlapDimUtil.getNonTimeDimLevel(tCol,
									RptOlapConsts.LEVEL_PRE);
						Object obj = expandValues.get(preLevel);
						if (null != obj)
							value = (String) obj;
						else
							value = null;
						tCol.setDimValue(value);
					}
				}
			} else {
				// 需要将当前维度的值去掉
				int count = 0;
				iter = tableCols.iterator();
				while (iter.hasNext()) {
					RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
							.next();
					if (tCol.isDisplay() && tCol.isDim()) {
						// 找到当前展开维度，去掉其值
						if (count == iLevel) {
							tCol.setDimValue(null);
						}
						count++;
					}
				}
			}
			// 还得去上层的值
			expandValues.remove(level);
			olapFunc.setExpandedValues(expandValues);
			olapFunc.setCollapseRowId(collapseRowId);
		} else {
			olapFunc.setCollapseRowId(null);
			// 对于单维度多层次展开，需要重新设置一下该维度的值
			if (olapFunc.isSingleDimExpand()) {
				// 开始设置
				iter = tableCols.iterator();
				while (iter.hasNext()) {
					RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
							.next();
					if (tCol.isDisplay() && tCol.isDim()) {
						// 这是层次水平已经设置了
						String preLevel = null;
						String level = tCol.getDigLevel();
						if (tCol.isTimeDim()) {
							preLevel = RptOlapDimUtil.getDimTimePreLevel(tCol);
						} else
							preLevel = RptOlapDimUtil.getNonTimeDimLevel(tCol,
									RptOlapConsts.LEVEL_PRE);
						Object obj = null;
						String value = null;
						Map values = olapFunc.getExpandedValues();
						if (null != values) {
							obj = values.get(preLevel);
							if (null != obj)
								value = (String) obj;
							else
								value = null;
						}
						tCol.setDimValue(value);
						tCol.setDigLevel(level);
					}
				}
			}
		}
		if ((null != expandLevel && !"".equals(expandLevel))
				|| (null != collapseRowId && !"".equals(collapseRowId))) {
			olapFunc.setHasNewExpand(true);
			olapFunc.setHasAlert(true);
			olapFunc.setHasSameRatioAlert(true);
			olapFunc.setHasLastRatioAlert(true);
		} else {
			olapFunc.setHasNewExpand(false);
		}
		olapFunc = setOlapOtherFunc(olapFunc, tableCols, collapseMsuInit,
				collapseMsuId, collapseMsu, firstExpand);
		return olapFunc;
	}

	private static RptOlapFuncStruct setOlapOtherFunc(RptOlapFuncStruct func,
			List tableCols, String collapseMsuInit, String collapseMsuId,
			String collapseMsu, boolean firstExpand) {
		RptOlapFuncStruct olapFunc = func;
		// 上面根据非第一次折叠、展开标志已经正确设置了，
		olapFunc.setToUserLevel(MsuCanDigToUserLevel(tableCols));
		if (olapFunc.isFirstExpand()) {
			// 如果第一次
			if (RptOlapConsts.OLAP_FUN_DATA.equals(olapFunc.getCurFunc()))
				olapFunc.setHasNewExpand(true);
			else
				olapFunc.setHasNewExpand(false);
		}

		if (RptOlapConsts.RESET_MODE_EXPAND.equals(olapFunc.getDisplayMode())
				&& ((null != olapFunc.getClickMusId())
						|| (null != collapseMsuInit
								&& !"".equals(collapseMsuInit) && RptOlapConsts.YES
									.equalsIgnoreCase(collapseMsuInit)) || (null != collapseMsu
						&& null != collapseMsuId && !"".equals(collapseMsu) && !""
							.equals(collapseMsuId)))) {
			olapFunc.setHasNewExpand(true);
			olapFunc.setFirstExpand(true);
			int initLevel = getExpandInitLevel(tableCols);
			olapFunc.setCurExpandLevel(initLevel);
			olapFunc.setExpandedValues(null);
			tableCols = initTableColums(tableCols, initLevel + "");
		}
		if (firstExpand) {
			if (null != olapFunc.getSort())
				olapFunc.setFirstExpand(firstExpand);
		}
		if (olapFunc.isHasNewExpand()) {
			// 有新的折叠、展开，从数据开始，因为对于时间和其他维太复杂了
			olapFunc.setCurFunc(RptOlapConsts.OLAP_FUN_DATA);
		}
		if (RptOlapConsts.RESET_MODE_EXPAND.equals(olapFunc.getDisplayMode())) {
			olapFunc.setHasAlert(true);
			olapFunc.setHasSameRatioAlert(true);
			olapFunc.setHasLastRatioAlert(true);
		}
		return olapFunc;
	}

	private static List initTableColums(List tableCols, String level)
			throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size())
			throw new ReportOlapException("获取折叠展开的最初状态时输入的参数为空");
		List tabCols = new ArrayList();
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDim() && tCol.isDisplay()) {
				tCol.setDimValue(null);
				tCol.setDigLevel(level);
			}
			tabCols.add(tCol);
		}
		return tabCols;
	}

	private static int getExpandInitLevel(List tableCols)
			throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size())
			throw new ReportOlapException("获取折叠展开的最初状态时输入的参数为空");
		int initLevel = Integer.parseInt(RptOlapConsts.ZERO_STR);
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDim() && tCol.isTimeDim() && tCol.isDisplay()) {
				String level = tCol.getDigLevel();
				tCol.setDigLevel(RptOlapConsts.NO_DIGGED_LEVEL);
				initLevel = Integer.parseInt(RptOlapDimUtil
						.getDimTimeNextLevel(tCol));
				tCol.setDigLevel(level);
				break;
			}
		}
		return initLevel;
	}

	/**
	 * 判断显示指标中是否有要求预警的
	 *
	 * @param tableCols
	 *            列域对象列表
	 * @return
	 */
	private static RptOlapFuncStruct MsuHasAlert(RptOlapFuncStruct olapFun,
			List tableCols) throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size())
			throw new ReportOlapException("判断当前条件下是否有指标预警时输入的参数为空");
		RptOlapFuncStruct func = olapFun;
		boolean hasAlert = false;
		boolean hasSameAlert = false;
		boolean hasLastAlert = false;
		if (null != tableCols) {
			Iterator iter = tableCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tabCol = (RptOlapTableColumnStruct) iter
						.next();
				if (tabCol.isDisplay() && !tabCol.isDim()) {
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) tabCol
							.getStruct();
					if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.need_alert)) {
						hasAlert = true;
					}
					if (RptOlapConsts.ALERT_COMPARE_TO_SAME
							.equals(rptMsu.compare_to)) {
						hasSameAlert = true;
					}
					if (RptOlapConsts.ALERT_COMPARE_TO_LAST
							.equals(rptMsu.compare_to)) {
						hasLastAlert = true;
					}
				}
			}
		}

		func.setHasAlert(hasAlert);
		func.setHasSameRatioAlert(hasSameAlert);
		func.setHasLastRatioAlert(hasLastAlert);
		return func;
	}

	private static boolean collapseToInitLevel(List tabCols,
			String collapseMsuId, List rptMsus) throws ReportOlapException {
		if (null == tabCols || null == collapseMsuId || 0 >= tabCols.size()
				|| "".equals(collapseMsuId))
			throw new ReportOlapException("确定指标是否收缩到最初时输入的参数维空");
		boolean toInit = false;
		String parentId = RptOlapMsuUtil.getCollapseMsuParentId(tabCols,
				collapseMsuId);
		if (null != parentId) {
			toInit = RptOlapMsuUtil.isCollapseToInitLevel(parentId, rptMsus);
		}
		return toInit;
	}

	/**
	 * 判断显示的指标是否有汇总要求
	 *
	 * @param olapFun
	 *            当前功能对象
	 * @param tableCols
	 *            列域对象列表
	 * @return
	 */
	private static boolean MsuHasSum(RptOlapFuncStruct olapFun, List tableCols)
			throws ReportOlapException {
		if (null == olapFun || null == tableCols || 0 >= tableCols.size())
			throw new ReportOlapException("判断当前条件下是否有汇总值时输入的参数为空");
		String func = olapFun.getCurFunc();
		// 同比、环比时肯定没有累计
		if (!RptOlapConsts.RESET_MODE_EXPAND.equals(olapFun.getDisplayMode())
				&& (RptOlapConsts.OLAP_FUN_SAME.equals(func) || RptOlapConsts.OLAP_FUN_LAST
						.equals(func)))
			return false;
		boolean hasSum = false;
		// 是数据钻取，还有看是否有列定义了
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tabCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tabCol.isDisplay() && !tabCol.isDim()) {
				// 即使到最细粒度，也可以有累加值
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) tabCol.getStruct();
				if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_sumbydate)) {
					hasSum = true;
					break;
				}
			}
		}
		return hasSum;
	}

	private static boolean MsuCanDigToUserLevel(List tableCols)
			throws ReportOlapException {
		if (null == tableCols || 0 >= tableCols.size())
			throw new ReportOlapException("判断当前条件下是否能够" + "钻取到用户级时输入的参数为空");
		boolean toUserLevel = true;
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDisplay() && tCol.isDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
				// 显示维度,取出当前的层次
				String level = tCol.getDigLevel();
				if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(level)) {
					if (RptOlapConsts.ZERO_STR.equals(level)
							&& RptOlapConsts.NO
									.equalsIgnoreCase(rptDim.dimInfo.to_userlvl)) {
						toUserLevel = false;
						break;
					}
					// 如果是其他层次，还得看其层次
					List levels = rptDim.dimInfo.dim_levels;
					if (null != levels && null != level && levels.size() > 0) {
						DimLevelTable lvlObj = RptOlapDimUtil.getDimCurLevel(
								level, levels);
						if (null != lvlObj
								&& RptOlapConsts.NO
										.equalsIgnoreCase(lvlObj.to_userlvl)) {
							toUserLevel = false;
							break;
						}
					}
				}
			}
		}
		return toUserLevel;
	}
}
