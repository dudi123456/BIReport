package com.ailk.bi.olap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapMsuTable;
import com.ailk.bi.base.table.RptOlapUserDimTable;
import com.ailk.bi.base.table.RptOlapUserMsuTable;
import com.ailk.bi.olap.domain.RptOlapFuncStruct;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class RptOlapTabDomainUtil {
	/**
	 * 根据用户定制设置列定义
	 *
	 * @param tableCols
	 * @param userCustomDims
	 * @param userCustomMsus
	 * @return
	 */
	public static void adjustTableCols(List tableCols, List userCustomDims,
			List userCustomMsus) {
		// 怎么调整，LIST直接找不到，直接循环,好在少可以循环可以接受
		Iterator iter = tableCols.iterator();
		RptOlapTableColumnStruct tCol = null;
		while (iter.hasNext()) {
			tCol = (RptOlapTableColumnStruct) iter.next();
			tCol.setDisplay(false);
		}
		Iterator dimIter = userCustomDims.iterator();
		int orderIndex = 0;
		RptOlapUserDimTable userDim = null;
		while (dimIter.hasNext()) {
			userDim = (RptOlapUserDimTable) dimIter.next();
			// start iter tablecols
			adjustTableCols(tableCols, userDim, orderIndex++);
		}
		iter = tableCols.iterator();
		tCol = null;
		while (iter.hasNext()) {
			tCol = (RptOlapTableColumnStruct) iter.next();
			if (tCol.isDim() && !tCol.isDisplay()) {
				tCol.setDisplayOrder(orderIndex++);
			}
		}
		// 开始处理指标
		Iterator msuIter = userCustomMsus.iterator();
		RptOlapUserMsuTable userMsu = null;
		while (msuIter.hasNext()) {
			userMsu = (RptOlapUserMsuTable) msuIter.next();
			adjustTableCols(tableCols, userMsu, orderIndex++);
		}
		iter = tableCols.iterator();
		tCol = null;
		while (iter.hasNext()) {
			tCol = (RptOlapTableColumnStruct) iter.next();
			if (!tCol.isDim() && !tCol.isDisplay()) {
				tCol.setDisplayOrder(orderIndex++);
			}
		}
	}

	private static void adjustTableCols(List tableCols,
			RptOlapUserMsuTable userMsu, int order) {
		Iterator iter = tableCols.iterator();
		RptOlapTableColumnStruct tCol = null;
		while (iter.hasNext()) {
			tCol = (RptOlapTableColumnStruct) iter.next();
			if (!tCol.isDim()) {
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
				if (rptMsu.msu_id.equals(userMsu.msu_id)) {
					tCol.setDisplay(true);
					tCol.setDisplayOrder(order);
					break;
				}
			}
		}
	}

	private static void adjustTableCols(List tableCols,
			RptOlapUserDimTable userDim, int order) {
		Iterator iter = tableCols.iterator();
		RptOlapTableColumnStruct tCol = null;
		while (iter.hasNext()) {
			tCol = (RptOlapTableColumnStruct) iter.next();
			if (tCol.isDim()) {
				RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
				if (rptDim.dim_id.equals(userDim.dim_id)) {
					tCol.setDisplay(true);
					tCol.setDisplayOrder(order);
					break;
				}
			}
		}
	}

	public static List copyDomains(List tabCols) throws ReportOlapException,
			CloneNotSupportedException {
		if (null == tabCols)
			throw new ReportOlapException("复制列域对象参数为空");
		List cloneTabCols = new ArrayList();
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			RptOlapTableColumnStruct cloneTabCol = (RptOlapTableColumnStruct) tCol
					.clone();
			cloneTabCols.add(cloneTabCol);
		}
		return cloneTabCols;
	}

	/**
	 * 获取当前层次下维度的字段
	 *
	 * @param rptDim
	 *            报表维度对象
	 * @return 字段代码
	 */
	private static String getDimDataLevelFld(RptOlapDimTable rptDim)
			throws ReportOlapException {
		String dimFld = null;
		if (null == rptDim)
			throw new ReportOlapException("获取维度的当前层次下的字段时输入的维度对象为空");
		if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.is_timedim)) {
			dimFld = rptDim.dimInfo.code_idfld;
		} else {
			if (RptOlapConsts.NO.equalsIgnoreCase(rptDim.need_dig)) {
				dimFld = rptDim.dimInfo.code_idfld;
			} else {
				if (null == rptDim.dimInfo.dim_levels
						|| rptDim.dimInfo.dim_levels.size() <= 0) {
					dimFld = rptDim.dimInfo.code_idfld;
				} else {
					if (RptOlapConsts.ZERO_STR
							.equalsIgnoreCase(rptDim.max_level)) {
						dimFld = rptDim.dimInfo.code_idfld;
					} else {
						dimFld = getDimDataLevelFld(rptDim.max_level,
								rptDim.dimInfo.dim_levels);
					}
				}
			}
		}
		return dimFld;
	}

	/**
	 * 获取
	 *
	 * @param maxLevel
	 * @param levels
	 * @return
	 */
	private static String getDimDataLevelFld(String maxLevel, List levels)
			throws ReportOlapException {
		String dimFld = null;
		if (null == maxLevel || null == levels)
			throw new ReportOlapException("获取维度的当前层次下的字段时输入的参数为空");
		Iterator iter = levels.iterator();
		while (iter.hasNext()) {
			DimLevelTable level = (DimLevelTable) iter.next();
			if (level.lvl_id.equals(maxLevel)) {
				dimFld = level.code_idfld;
				break;
			}
		}
		return dimFld;
	}

	/**
	 * 将报表的维度对象转换成列对象列表
	 *
	 * @param from
	 * @param rptDims
	 * @return
	 * @throws ReportOlapException
	 */
	public static List convertRptDims(int from, List rptDims)
			throws ReportOlapException {
		List tableCols = null;
		if (null == rptDims || rptDims.size() <= 0)
			throw new IllegalArgumentException("组装表格列对象时，输入的参数为空");
		int index = from;
		tableCols = new ArrayList();
		Iterator iter = rptDims.iterator();
		while (iter.hasNext()) {
			RptOlapDimTable rptDim = (RptOlapDimTable) iter.next();
			RptOlapTableColumnStruct col = new RptOlapTableColumnStruct();
			col.setIndex(index);
			col.setStruct(rptDim);
			col.setDim(true);
			col.setTimeDim(RptOlapConsts.YES
					.equalsIgnoreCase(rptDim.is_timedim) ? true : false);

			col.setDisplay(RptOlapConsts.YES
					.equalsIgnoreCase(rptDim.default_display) ? true : false);
			col.setDisplayOrder(index);
			// 仔细设置是否可以继续钻取
			col.setNeedDig(RptOlapConsts.YES.equalsIgnoreCase(rptDim.need_dig) ? true
					: false);
			List levels = rptDim.dimInfo.dim_levels;

			// 设置初始层次，放置小于－1
			int init_level = Integer.parseInt(RptOlapConsts.NO_DIGGED_LEVEL);
			try {
				init_level = Integer.parseInt(rptDim.init_level);
			} catch (NumberFormatException nfe) {

			}
			if (init_level < Integer.parseInt(RptOlapConsts.NO_DIGGED_LEVEL))
				init_level = Integer.parseInt(RptOlapConsts.NO_DIGGED_LEVEL);
			col.setDigLevel(init_level + "");
			if ((RptOlapConsts.ZERO_STR.equals(col.getDigLevel()) && (null == levels || levels
					.size() <= 0))
					|| col.getDigLevel().equals(rptDim.max_level)) {
				col.setNeedDig(false);
			}
			col.setColFld(getDimDataLevelFld(rptDim));
			if (col.isTimeDim()) {
				col.setColName(RptOlapDimUtil.getTimeLevelName(
						col.getDigLevel(), col));
			} else {
				col.setColName(rptDim.dimInfo.dim_name);
			}
			if (!col.isNeedDig()) {
				col.setDigLevel(RptOlapConsts.ZERO_STR);
			}
			tableCols.add(col);
			index++;
		}
		return tableCols;
	}

	/**
	 * 将报表指标对象转为列域对象
	 *
	 * @param from
	 *            维度的索引
	 * @param rptMsus
	 *            指标列表
	 * @return 列域对象
	 * @throws ReportOlapException
	 */
	public static List convertRptMsus(int from, List rptMsus, String level)
			throws ReportOlapException {
		List tableCols = null;
		if (null == rptMsus || rptMsus.size() <= 0)
			throw new IllegalArgumentException("组装表格列对象时，输入的参数为空");
		int index = from;
		tableCols = new ArrayList();
		Iterator iter = rptMsus.iterator();
		while (iter.hasNext()) {
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) iter.next();
			RptOlapTableColumnStruct col = new RptOlapTableColumnStruct();
			col.setIndex(index);
			col.setStruct(rptMsu);
			col.setColName((null == rptMsu.col_name
					|| "".equals(rptMsu.col_name) ? rptMsu.msuInfo.msu_name
					: rptMsu.col_name));
			col.setDim(false);
			col.setTimeDim(false);
			col.setDisplay(RptOlapConsts.YES
					.equalsIgnoreCase(rptMsu.default_display) ? true : false);
			col.setDisplayOrder(index);
			col.setNeedDig(RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_dig) ? true
					: false);
			col.setDigLevel(level);
			col.setColFld(rptMsu.msuInfo.real_fld);
			tableCols.add(col);
			index++;
		}
		return tableCols;
	}

	/**
	 * 分析当前的请求并更新相应的状态到表格列对象
	 *
	 * @param request
	 *            请求对象
	 * @param tableCols
	 *            列域对象列表
	 * @return
	 */
	public static List pareseRequestToTableColStruct(
			HttpServletRequest request, List tableCols, List rptMsus)
			throws ReportOlapException {
		if (null == request || null == tableCols || 0 >= tableCols.size()
				|| null == rptMsus || 0 >= rptMsus.size())
			throw new ReportOlapException("分析请求设置列域对象列表时输入参数为空");
		List cols = new ArrayList();
		String reportId = request.getParameter(RptOlapConsts.REQ_REPORT_ID);
		String clickDim = request.getParameter(RptOlapConsts.REQ_CLICK_DIM);
		String clickMsu = request.getParameter(RptOlapConsts.REQ_CLICK_MSU);
		String clickMsuId = request
				.getParameter(RptOlapConsts.REQ_CLICK_MSU_ID);
		String collapseDimInit = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_DIM_INIT);
		String collapseDim = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_DIM);
		String collapseMsuInit = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_MSU_INIT);
		String collapseMsu = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_MSU);
		String collapseMsuId = request
				.getParameter(RptOlapConsts.REQ_COLLAPSE_MSU_ID);
		String sort = request.getParameter(RptOlapConsts.REQ_SORT);
		String cur_olap_fun = request.getParameter(RptOlapConsts.REQ_OLAP_FUNC);
		Iterator iter = tableCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDim()) {
				String level = request
						.getParameter(RptOlapConsts.REQ_DIM_LEVEL_PREFIX
								+ tCol.getIndex());
				if (null == level || "".equals(level)) {
					// 设置这个列不显示
					tCol.setDisplay(false);
					tCol.setDigLevel(RptOlapConsts.NO_DIGGED_LEVEL);
				} else {
					tCol.setDigLevel(level);
					if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level))
						tCol.setDimValue(null);
				}
				// 获取值
				String value = request
						.getParameter(RptOlapConsts.REQ_DIM_VALUE_PREFIX
								+ tCol.getIndex());
				if (null != value && !"".equals(value)) {
					tCol.setDimValue(value);
					Map values = tCol.getDimValues();
					values.put(tCol.getDigLevel(), value);
					tCol.setDimValues(values);
				}
				if (null != clickDim && clickDim.equals(tCol.getIndex() + "")) {
					// 说明是从表格中单击，取得其值
					tCol.setClickDim(true);
					// 设置为下一层次
					if (tCol.isTimeDim()) {
						String nextLevel = RptOlapDimUtil
								.getDimTimeNextLevel(tCol);
						tCol.setDigLevel(nextLevel);
						// 获取一下名字
						String levelName = RptOlapDimUtil.getTimeLevelName(
								nextLevel, tCol);
						tCol.setColName(levelName);
					} else {
						String nextLevel = RptOlapDimUtil.getNonTimeDimLevel(
								tCol, RptOlapConsts.LEVEL_NEXT);
						tCol.setDigLevel(nextLevel);
						String levelName = RptOlapDimUtil
								.getNonTimeDimLevelName(tCol, nextLevel);
						if (null != levelName && !"".equals(levelName))
							tCol.setColName(levelName);
					}
					// 这里还有看看后面还能不能钻取
					if (tCol.isTimeDim()) {
						String nextLevel = RptOlapDimUtil
								.getDimTimeNextLevel(tCol);
						if (RptOlapConsts.NO_NEXT_LEVEL.equals(nextLevel))
							tCol.setNeedDig(false);
						else
							tCol.setNeedDig(true);
					} else {
						String nextLevel = RptOlapDimUtil.getNonTimeDimLevel(
								tCol, RptOlapConsts.LEVEL_NEXT);
						if (null == nextLevel
								|| RptOlapConsts.NO_NEXT_LEVEL
										.equals(nextLevel))
							tCol.setNeedDig(false);
						else
							tCol.setNeedDig(true);
					}
				} else if (null == clickDim) {
					if (tCol.isClickDim()
							&& null != tCol.getDimValue()
							&& ((null != collapseMsuInit
									&& !"".equals(collapseMsuInit) && RptOlapConsts.YES
										.equalsIgnoreCase(collapseMsuInit)) || (null != collapseMsu
									&& null != collapseMsuId
									&& !"".equals(collapseMsu) && !""
										.equals(collapseMsuId)))) {

					} else if (tCol.isClickDim() && null != clickMsu
							&& !"".equals(clickMsu)) {
						// 是上次单击维度，且传进值了,将当前的层次向上设计一层
					} else if (tCol.isClickDim()
							&& null != tCol.getDimValue()
							&& ((null != collapseDimInit && !""
									.equals(collapseDimInit)) || (null != collapseDim && !""
									.equals(collapseDim)))) {
					} else if (tCol.isClickDim()
							&& ((null != sort && !"".equals(sort)) || (null != cur_olap_fun && (RptOlapConsts.OLAP_FUN_PERCENT
									.equals(cur_olap_fun)
									|| RptOlapConsts.OLAP_FUN_SAME
											.equals(cur_olap_fun)
									|| RptOlapConsts.OLAP_FUN_LAST
											.equals(cur_olap_fun) || RptOlapConsts.OLAP_FUN_DATA
										.equals(cur_olap_fun))))) {

					} else {
						tCol.setClickDim(false);
					}
				} else {
					tCol.setClickDim(false);
				}
				String can_dig = request
						.getParameter(RptOlapConsts.REQ_DIM_CAN_DIG_PREFIX
								+ tCol.getIndex());
				if (null != can_dig) {
					if (RptOlapConsts.FALSE.equalsIgnoreCase(can_dig)) {
						tCol.setNeedDig(false);
						tCol.setDigLevel(RptOlapConsts.NO_NEXT_LEVEL);
					}
					if (RptOlapConsts.TRUE.equalsIgnoreCase(can_dig)) {
						// 这里需要判断重新设置了以后，该维度的下一层次还有没有
						if (tCol.isTimeDim()) {
							String nextLevel = RptOlapDimUtil
									.getDimTimeNextLevel(tCol);
							if (RptOlapConsts.NO_NEXT_LEVEL.equals(nextLevel))
								tCol.setNeedDig(false);
							else
								tCol.setNeedDig(true);
						} else {
							String nextLevel = RptOlapDimUtil
									.getNonTimeDimLevel(tCol,
											RptOlapConsts.LEVEL_NEXT);
							if (null == nextLevel
									|| RptOlapConsts.NO_NEXT_LEVEL
											.equals(nextLevel))
								tCol.setNeedDig(false);
							else
								tCol.setNeedDig(true);
						}
					}
				}
				// 看看是否全部收缩
				if (null != collapseDimInit && !"".equals(collapseDimInit)
						&& collapseDimInit.equals(tCol.getIndex() + "")) {
					// 该维度收缩
					tCol.setDigLevel(RptOlapConsts.NO_DIGGED_LEVEL);
					RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
					tCol.setNeedDig(RptOlapConsts.YES
							.equalsIgnoreCase(rptDim.need_dig) ? true : false);
					// 既然全部收缩了，应该没有值了
					tCol.setDimValue(null);
					// 设置名称，这里需要区分时间维
					if (tCol.isTimeDim())
						tCol.setColName(RptOlapDimUtil.getTimeLevelName(
								tCol.getDigLevel(), tCol));
					else
						tCol.setColName(rptDim.dimInfo.dim_name);
				}
				// 看看是否收缩一级
				if (null != collapseDim && !"".equals(collapseDim)
						&& collapseDim.equals(tCol.getIndex() + "")) {
					level = RptOlapConsts.NO_DIGGED_LEVEL;
					String levelName = null;
					// 该维度收缩
					if (tCol.isTimeDim()) {
						level = RptOlapDimUtil.getDimTimePreLevel(tCol);
						levelName = RptOlapDimUtil
								.getTimeLevelName(level, tCol);
					} else {
						level = RptOlapDimUtil.getNonTimeDimLevel(tCol,
								RptOlapConsts.LEVEL_PRE);
						levelName = RptOlapDimUtil.getNonTimeDimLevelName(tCol,
								level);
					}
					tCol.setDigLevel(level);
					tCol.setColName(levelName);
					tCol.setNeedDig(true);// 肯定可以钻取
					tCol.setClickDim(true);
					if (RptOlapConsts.NO_DIGGED_LEVEL.equals(level))
						tCol.setDimValue(null);
					else {
						// 找上一层对应的本层值
						String preLevel = RptOlapDimUtil.getNonTimeDimLevel(
								tCol, RptOlapConsts.LEVEL_PRE);
						Map values = tCol.getDimValues();
						Object tmpObj = values.get(preLevel);
						if (null != tmpObj) {
							value = (String) tmpObj;
							tCol.setDimValue(value);
						} else {
							tCol.setDimValue(null);
						}
					}
				}
			} else {
				String level = request
						.getParameter(RptOlapConsts.REQ_MSU_LEVEL_PREFIX
								+ tCol.getIndex());
				if (null == level || "".equals(level)) {
					// 设置这个列不显示
					tCol.setDisplay(false);
					tCol.setDigLevel(RptOlapConsts.NO_DIGGED_LEVEL);
				} else {
					tCol.setDigLevel(level);
				}
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
				if (null != clickMsuId && !"".equals(clickMsuId)
						&& clickMsuId.equals(rptMsu.msu_id)) {
					if (null != level) {
						int nextLevel = Integer.parseInt(level) + 1;
						tCol.setDigLevel(nextLevel + "");
					} else {
						level = RptOlapConsts.ZERO_STR;
						int nextLevel = Integer.parseInt(level) + 1;
						tCol.setDigLevel(nextLevel + "");
					}
				}
			}
			cols.add(tCol);
		}
		// 这里需要重新设置所有的指标列域对象
		if (null != clickMsuId && !"".equals(clickMsuId)) {
			List list = resetTableColums(tableCols, clickMsuId);
			cols.clear();
			cols.addAll(list);
		}
		// 如果指标直接收缩到第0级的话
		if (null != collapseMsuInit && !"".equals(collapseMsuInit)
				&& RptOlapConsts.YES.equalsIgnoreCase(collapseMsuInit)) {
			// 直接收缩
			List list = collapseMsuToInit(reportId, tableCols);
			cols.clear();
			cols.addAll(list);
		}
		// 或者指标收缩到上一级
		if (null != collapseMsu && null != collapseMsuId
				&& !"".equals(collapseMsu) && !"".equals(collapseMsuId)) {
			List list = collapseMsuUp(reportId, tableCols, collapseMsuId,
					rptMsus, collapseMsu);
			cols.clear();
			cols.addAll(list);
		}
		// 看用户是否进行了定制
		String resetMode = request.getParameter(RptOlapConsts.REQ_RESET_MODE);
		if (null != resetMode && !"".equals(resetMode)) {
			// 用户还是层层钻取
			List list = pareseDigMode(request, resetMode, tableCols, true);
			cols.clear();
			cols.addAll(list);
		}
		return cols;
	}

	private static List pareseDigMode(HttpServletRequest request,
			String resetMode, List tabCols, boolean zeroLevel)
			throws ReportOlapException {
		if (null == request || null == resetMode || "".equals(resetMode)
				|| null == tabCols || 0 >= tabCols.size())
			throw new ReportOlapException("分析请求生成用户定制的信息时输入的参数为空");
		List tCols = new ArrayList();
		if (RptOlapConsts.RESET_MODE_DIG.equals(resetMode)) {
			tCols.clear();
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				tCol = resetTableColumDomain(tCol);
				if (tCol.isDim()) {
					RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
					String dimOrder = request.getParameter("dim_"
							+ rptDim.dim_id + "_order");
					if (null != dimOrder && !"".equals(dimOrder))
						tCol.setDisplayOrder(Integer.parseInt(dimOrder));
					String dimDisplay = request.getParameter("dim_"
							+ rptDim.dim_id + "_display");
					if (null != dimDisplay
							&& RptOlapConsts.TRUE.equals(dimDisplay))
						tCol.setDisplay(true);
					else
						tCol.setDisplay(false);
				} else {
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
					String msuOrder = request.getParameter("msu_"
							+ rptMsu.msu_id + "_order");
					if (null != msuOrder && !"".equals(msuOrder))
						tCol.setDisplayOrder(Integer.parseInt(msuOrder));
					String msuDisplay = request.getParameter("msu_"
							+ rptMsu.msu_id + "_display");
					if (null != msuDisplay
							&& RptOlapConsts.TRUE.equals(msuDisplay)) {
						tCol.setDisplay(true);
					} else
						tCol.setDisplay(false);
				}
				tCols.add(tCol);
			}
		} else {
			tCols.clear();
			String singleOrMulti = request
					.getParameter(RptOlapConsts.REQ_EXPAND_MODE);
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				tCol = resetTableColumDomain(tCol);
				if (zeroLevel)
					tCol.setDigLevel(RptOlapConsts.ZERO_STR);
				if (tCol.isDim()) {
					RptOlapDimTable rptDim = (RptOlapDimTable) tCol.getStruct();
					String dimOrder = request.getParameter("dim_"
							+ rptDim.dim_id + "_order");
					if (null != dimOrder && !"".equals(dimOrder))
						tCol.setDisplayOrder(Integer.parseInt(dimOrder));
					String dimDisplay = request.getParameter("dim_"
							+ rptDim.dim_id + "_display");
					if (null != dimDisplay
							&& RptOlapConsts.TRUE.equals(dimDisplay)) {
						tCol.setDisplay(true);
						// 要判断是单维度时
						if (null != singleOrMulti && !"".equals(singleOrMulti)) {
							if (tCol.isTimeDim()) {
								tCol.setDigLevel(RptOlapConsts.NO_DIGGED_LEVEL);
								tCol.setDigLevel(RptOlapDimUtil
										.getDimTimeNextLevel(tCol));
								tCol.setColName(RptOlapDimUtil
										.getTimeLevelName(tCol.getDigLevel(),
												tCol));
							} else
								tCol.setDigLevel(RptOlapConsts.ZERO_STR);
						}
					} else
						tCol.setDisplay(false);
				} else {
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
					String msuOrder = request.getParameter("msu_"
							+ rptMsu.msu_id + "_order");
					tCol.setDisplayOrder(Integer.parseInt(msuOrder));
					String msuDisplay = request.getParameter("msu_"
							+ rptMsu.msu_id + "_display");
					if (null != msuDisplay
							&& RptOlapConsts.TRUE.equals(msuDisplay)) {
						tCol.setDisplay(true);
					} else
						tCol.setDisplay(false);
				}
				tCols.add(tCol);
			}
		}
		return tCols;
	}

	private static RptOlapTableColumnStruct resetTableColumDomain(
			RptOlapTableColumnStruct tCol) throws ReportOlapException {
		if (null == tCol)
			throw new ReportOlapException("重置列域对象的某些属性时输入的参数为空");
		RptOlapTableColumnStruct col = tCol;
		if (col.isDim()) {
			RptOlapDimTable rptDim = (RptOlapDimTable) col.getStruct();
			col.setNeedDig(RptOlapConsts.YES.equalsIgnoreCase(rptDim.need_dig) ? true
					: false);
			List levels = rptDim.dimInfo.dim_levels;

			// 设置初始层次，放置小于－1
			int init_level = Integer.parseInt(RptOlapConsts.NO_DIGGED_LEVEL);
			try {
				init_level = Integer.parseInt(rptDim.init_level);
			} catch (NumberFormatException nfe) {

			}
			if (init_level < Integer.parseInt(RptOlapConsts.NO_DIGGED_LEVEL))
				init_level = Integer.parseInt(RptOlapConsts.NO_DIGGED_LEVEL);
			col.setDigLevel(init_level + "");
			if ((RptOlapConsts.ZERO_STR.equals(col.getDigLevel()) && (null == levels || levels
					.size() <= 0))
					|| col.getDigLevel().equals(rptDim.max_level)) {
				col.setNeedDig(false);
			}
			// 设置值为空
			col.setDimValue(null);
			col.setDisplay(RptOlapConsts.YES
					.equalsIgnoreCase(rptDim.default_display) ? true : false);
		} else {
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) col.getStruct();
			col.setNeedDig(RptOlapConsts.YES.equalsIgnoreCase(rptMsu.is_dig) ? true
					: false);
			col.setDigLevel(RptOlapConsts.ZERO_STR);
			col.setDisplay(RptOlapConsts.YES
					.equalsIgnoreCase(rptMsu.default_display) ? true : false);
		}
		return col;

	}

	private static List collapseMsuUp(String reportId, List tabCols,
			String collapseMsuId, List rptMsus, String collapseMsu)
			throws ReportOlapException {
		if (null == reportId || "".equals(reportId) || null == tabCols
				|| 0 >= tabCols.size() || null == collapseMsuId
				|| "".equals(collapseMsuId) || null == rptMsus
				|| 0 >= rptMsus.size())
			throw new ReportOlapException("收缩指标生成新的列对象列表时输入的参数为空");
		List tCols = new ArrayList();
		boolean toInitStatus = false;
		// 先看一下是否在当前
		toInitStatus = RptOlapMsuUtil.isCollapseToInitLevel(collapseMsuId,
				rptMsus);
		if (toInitStatus) {
			List list = collapseMsuToInit(reportId, tabCols);
			tCols.clear();
			tCols.addAll(list);
		} else {
			// 此时的collapseMsuId是本层的钻下去之前的标识
			// tabCols是钻完之后的列对象
			// 现在要找到collapseMsuId的父亲，
			// 由父亲找到所有孩子，再加上父亲,
			// 先找到合计的RptMsu,需要这个信息
			int level = 0;
			RptOlapMsuTable rptMsu = null;
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				if (!tCol.isDim()) {
					// 是指标
					RptOlapMsuTable tmpRptMsu = (RptOlapMsuTable) tCol
							.getStruct();
					if (tmpRptMsu.msu_id.equalsIgnoreCase(collapseMsuId)) {
						rptMsu = tmpRptMsu;
						level = Integer.parseInt(tCol.getDigLevel());
						break;
					}
				}
			}
			level--;
			// 找到父亲
			RptOlapMsuTable parentMsu = RptOlapMsuUtil.getCollapseParentMsu(
					rptMsu, collapseMsuId);
			// 找到孩子节点
			List children = RptOlapMsuUtil.genRptMsus(rptMsu,
					RptOlapMsuUtil.getMsuChildren(parentMsu.msuInfo.msu_id));
			// 开始组装新的
			List newTableCols = new ArrayList();
			int from = 0;
			iter = tabCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				if (tCol.isDim()) {
					newTableCols.add(tCol);
					from++;
				}
			}
			// 加上指标
			List list = convertRptMsus(from, children, level + "");
			from += list.size();
			newTableCols.addAll(list);
			list.clear();
			parentMsu.col_name = "合计";
			list.add(parentMsu);
			list = convertRptMsus(from, list, level + "");
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) list
					.get(0);
			tCol.setStruct(parentMsu);
			tCol.setColName("合计");
			tCol.setDisplayOrder(from);
			tCol.setIndex(from);
			tCol.setSumMsu(true);
			newTableCols.add(tCol);

			tCols.clear();
			tCols.addAll(newTableCols);
		}
		return tCols;
	}

	private static List collapseMsuToInit(String reportId, List tabCols)
			throws ReportOlapException {
		if (null == reportId || "".equals(reportId) || null == tabCols
				|| 0 >= tabCols.size())
			throw new ReportOlapException("重新生成报表列对象时输入的参数为空");
		List tCols = new ArrayList();
		List rptMsus = RptOlapMsuUtil.getReportMsu(reportId);
		int from = -1;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			if (tCol.isDim()) {
				tCols.add(tCol);
				from++;
			}
		}
		// 转换指标为列域对象
		List msuCols = convertRptMsus(from, rptMsus, RptOlapConsts.ZERO_STR);
		tCols.addAll(msuCols);
		return tCols;
	}

	/**
	 * @param tabCols
	 * @param clickMsu
	 * @return
	 * @throws ReportOlapException
	 */
	private static List resetTableColums(List tabCols, String clickMsuId)
			throws ReportOlapException {
		if (null == tabCols || 0 >= tabCols.size() || null == clickMsuId
				|| "".equals(clickMsuId))
			throw new ReportOlapException("钻取指标重新生成列域对象列表时输入的参数为空");
		List newTabCols = new ArrayList();
		int from = -1;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
					.next();
			// 不在区分是否
			if (tCol.isDim()) {
				// 维度
				from++;
				newTabCols.add(tCol);
			} else {
				RptOlapMsuTable rptMsu = (RptOlapMsuTable) tCol.getStruct();
				if (clickMsuId.equals(rptMsu.msu_id)) {
					List children = RptOlapMsuUtil.genRptMsus(rptMsu,
							RptOlapMsuUtil
									.getMsuChildren(rptMsu.msuInfo.msu_id));
					String level = tCol.getDigLevel();
					List list = convertRptMsus(from, children, level);
					from += list.size();
					rptMsu.col_name = "合计";
					tCol.setStruct(rptMsu);
					tCol.setColName("合计");
					tCol.setDisplayOrder(from);
					tCol.setIndex(from);
					tCol.setSumMsu(true);
					tCol.setDigLevel(level);
					tCol.setDisplay(true);
					list.add(tCol);
					newTabCols.addAll(list);
					break;
				}
			}
		}
		return newTabCols;
	}

	/**
	 * 将表格列对象转换成URL串
	 *
	 * @param tableCols
	 *            列域对象列表
	 * @return
	 */
	public static String convertTableColStructToUrl(List tableCols,
			RptOlapFuncStruct olapFunc) {
		String link = "";
		if (null != tableCols) {
			StringBuffer sb = new StringBuffer("");
			Iterator iter = tableCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				if (tCol.isDisplay()) {
					if (tCol.isDim()) {
						sb.append("&")
								.append(RptOlapConsts.REQ_DIM_LEVEL_PREFIX)
								.append(tCol.getIndex()).append("=")
								.append(tCol.getDigLevel());
						sb.append("&")
								.append(RptOlapConsts.REQ_DIM_CAN_DIG_PREFIX)
								.append(tCol.getIndex()).append("=")
								.append(tCol.isNeedDig());
						if (null != tCol.getDimValue()) {
							sb.append("&")
									.append(RptOlapConsts.REQ_DIM_VALUE_PREFIX)
									.append(tCol.getIndex()).append("=")
									.append(tCol.getDimValue());
						}
					} else {
						sb.append("&")
								.append(RptOlapConsts.REQ_MSU_LEVEL_PREFIX)
								.append(tCol.getIndex()).append("=")
								.append(tCol.getDigLevel());
						sb.append("&")
								.append(RptOlapConsts.REQ_MSU_CAN_DIG_PREFIX)
								.append(tCol.getIndex()).append("=")
								.append(tCol.isNeedDig());
					}
				}
			}
			String func = olapFunc.getCurFunc();
			if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
					|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
					|| RptOlapConsts.OLAP_FUN_LAST.equals(func))
				sb.append("&cur_func=" + func);
			link = sb.toString();
		}
		return link;
	}

	/**
	 * 将表格列对象转换成URL串
	 *
	 * @param tableCols
	 *            列域对象列表
	 * @return
	 */
	public static String convertTableColStructToSimpleUrl(List tableCols,
			RptOlapFuncStruct olapFunc) {
		String link = "";
		if (null != tableCols) {
			StringBuffer sb = new StringBuffer("");
			Iterator iter = tableCols.iterator();
			while (iter.hasNext()) {
				RptOlapTableColumnStruct tCol = (RptOlapTableColumnStruct) iter
						.next();
				if (tCol.isDisplay()) {
					if (tCol.isDim()) {
						sb.append("&")
								.append(RptOlapConsts.REQ_DIM_LEVEL_PREFIX)
								.append(tCol.getIndex()).append("=")
								.append(tCol.getDigLevel());
					} else {
						sb.append("&")
								.append(RptOlapConsts.REQ_MSU_LEVEL_PREFIX)
								.append(tCol.getIndex()).append("=")
								.append(tCol.getDigLevel());
					}
				}
			}
			String func = olapFunc.getCurFunc();
			if (RptOlapConsts.OLAP_FUN_PERCENT.equals(func)
					|| RptOlapConsts.OLAP_FUN_SAME.equals(func)
					|| RptOlapConsts.OLAP_FUN_LAST.equals(func))
				sb.append("&cur_func=" + func);

			// 将所有的当前level值加进去
			Map map = olapFunc.getExpandedValues();
			if (null != map && map.size() > 0) {
				iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String level = (String) entry.getKey();
					String value = (String) entry.getValue();
					sb.append(
							"&" + RptOlapConsts.REQ_EXPAND_VALUE + level + "=")
							.append(value);
				}
			}

			link = sb.toString();
		}
		return link;
	}
}
