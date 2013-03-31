package com.ailk.bi.olap.service.dao.impl;

import java.util.Iterator;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.olap.domain.RptOlapTableColumnStruct;
import com.ailk.bi.olap.service.dao.IDimLevelDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapDimUtil;

@SuppressWarnings({ "rawtypes" })
public class DimLevelDao implements IDimLevelDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IDimLevelDao#getDimNextLevel(com.asiabi.olap
	 * .domain.RptOlapTableColumn)
	 */
	public RptOlapTableColumnStruct getDimNextLevel(RptOlapTableColumnStruct col)
			throws ReportOlapException {
		RptOlapTableColumnStruct nextCol = null;
		if (null == col)
			throw new IllegalArgumentException("设置表格列对象的下一层次时列对象为空");
		if (col.isDim()) {
			if (col.isTimeDim()) {
				nextCol = col;
				nextCol.setDigLevel(RptOlapDimUtil.getDimTimePreLevel(col));
			} else {
				nextCol = col;
				nextCol.setDigLevel(RptOlapDimUtil.getNonTimeDimLevel(col,
						RptOlapConsts.LEVEL_NEXT));
			}
		}
		return nextCol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IDimLevelDao#getDimPreLevel(com.asiabi.olap
	 * .domain.RptOlapTableColumn)
	 */
	public RptOlapTableColumnStruct getDimPreLevel(RptOlapTableColumnStruct col)
			throws ReportOlapException {
		RptOlapTableColumnStruct preCol = null;
		if (null == col)
			throw new IllegalArgumentException("设置表格列对象的上一层次时列对象为空");
		if (col.isDim()) {
			if (col.isTimeDim()) {
				preCol = col;
				preCol.setDigLevel(RptOlapDimUtil.getDimTimePreLevel(col));
			} else {
				preCol = col;
				preCol.setDigLevel(RptOlapDimUtil.getNonTimeDimLevel(col,
						RptOlapConsts.LEVEL_PRE));
			}
		}
		return preCol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IDimLevelDao#getLevelInfo(com.asiabi.olap
	 * .domain.RptOlapTableColumn)
	 */
	public DimLevelTable getLevelInfo(RptOlapTableColumnStruct col)
			throws ReportOlapException {
		DimLevelTable levelObj = null;
		if (null == col)
			throw new IllegalArgumentException("设置表格列对象的下一层次时列对象为空");
		String level = col.getDigLevel();
		Object tmpObj = col.getStruct();
		RptOlapDimTable rptDim = (RptOlapDimTable) tmpObj;
		if (!RptOlapConsts.NO_DIGGED_LEVEL.equals(level)
				&& !RptOlapConsts.ZERO_STR.equals(level)) {
			List levels = rptDim.dimInfo.dim_levels;
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				DimLevelTable tmp = (DimLevelTable) iter.next();
				if (level.equals(tmp.lvl_id)) {
					levelObj = tmp;
					break;
				}
			}
		}
		return levelObj;
	}
}
