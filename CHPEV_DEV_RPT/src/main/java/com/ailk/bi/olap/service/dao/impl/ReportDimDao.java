package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.base.table.DimLevelTable;
import com.ailk.bi.base.table.DimensionTable;
import com.ailk.bi.base.table.RptOlapDimTable;
import com.ailk.bi.base.table.RptOlapTimeTable;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.olap.service.dao.IReportDimDao;
import com.ailk.bi.olap.util.RptOlapConsts;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportDimDao implements IReportDimDao {

	/**
	 * 所有维度定义，key为维度标识,value为DimensionTable对象
	 */
	private static Map allDims = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IReportDimDao#getReportDim(java.lang.String)
	 */
	public List getReportDim(String reportId) throws ReportOlapException {
		List reportDims = null;
		if (null == reportId || "".equals(reportId))
			throw new IllegalArgumentException("报表标识为空");
		try {
			String sql = SQLGenator.genSQL("Q5610", reportId);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				reportDims = new ArrayList();
				for (int i = 0; i < svces.length; i++) {
					RptOlapDimTable reportDim = genReportDimStruct(svces[i]);
					reportDims.add(reportDim);
				}
			}
		} catch (AppException ae) {
			throw new ReportOlapException("获取报表维度出错", ae);
		}
		return reportDims;
	}

	/**
	 * 组装报表维度表对象
	 * 
	 * @param svces
	 * @return
	 * @throws ReportOlapException
	 */
	private RptOlapDimTable genReportDimStruct(String[] svces)
			throws ReportOlapException {
		RptOlapDimTable reportDim = null;
		if (null == svces || svces.length <= 0)
			throw new IllegalArgumentException("组装报表维度对象传入参数为空");
		Map dimInfos = getAllDims();
		reportDim = new RptOlapDimTable();
		reportDim.report_id = svces[0];
		reportDim.dim_id = svces[1];
		reportDim.default_display = svces[2];
		reportDim.is_timedim = svces[3];
		reportDim.display_order = svces[4];
		reportDim.need_dig = svces[5];
		reportDim.init_level = svces[6];
		reportDim.max_level = svces[7];
		reportDim.to_userlist = svces[8];
		reportDim.is_evaldept = svces[9];
		reportDim.is_devdept = svces[10];
		reportDim.is_svcknd = svces[11];
		reportDim.is_valid = svces[12];
		if (null != dimInfos && null != dimInfos.get(reportDim.dim_id)) {
			reportDim.dimInfo = (DimensionTable) dimInfos.get(reportDim.dim_id);
			// 这里要对有些属性重新设置
			List levels = reportDim.dimInfo.dim_levels;
			if (null == levels || levels.size() <= 0) {
				// 设置为就一层
				reportDim.max_level = RptOlapConsts.ZERO_STR;
			} else {
				int maxLevel = 0;
				try {
					maxLevel = Integer.parseInt(reportDim.max_level);
				} catch (NumberFormatException nfe) {
					maxLevel = 0;
					reportDim.max_level = RptOlapConsts.ZERO_STR;
				}
				if (maxLevel > levels.size()) {
					reportDim.max_level = levels.size() + "";
				}
			}
		}
		if (RptOlapConsts.YES.equalsIgnoreCase(reportDim.is_timedim)) {
			RptOlapTimeTable reportTime = new RptOlapTimeTable();
			reportTime.report_id = svces[13];
			reportTime.by_year = svces[14];
			reportTime.by_quarter = svces[15];
			reportTime.by_month = svces[16];
			reportTime.by_tendays = svces[17];
			reportTime.by_week = svces[19];
			reportTime.by_day = svces[19];
			reportDim.timeLevel = reportTime;
			int maxLevel = 0;
			if (RptOlapConsts.YES.equalsIgnoreCase(reportTime.by_year))
				maxLevel++;
			if (RptOlapConsts.YES.equalsIgnoreCase(reportTime.by_quarter))
				maxLevel++;
			if (RptOlapConsts.YES.equalsIgnoreCase(reportTime.by_month))
				maxLevel++;
			if (RptOlapConsts.YES.equalsIgnoreCase(reportTime.by_tendays))
				maxLevel++;
			if (RptOlapConsts.YES.equalsIgnoreCase(reportTime.by_week))
				maxLevel++;
			if (RptOlapConsts.YES.equalsIgnoreCase(reportTime.by_day))
				maxLevel++;
			reportDim.max_level = maxLevel + "";
		}
		return reportDim;
	}

	/**
	 * 获取所有指标体系中的维度对象
	 * 
	 * @return
	 */
	public static Map getAllDims() {
		if (null == allDims)
			allDims = genAllDims();
		return allDims;
	}

	/**
	 * 生成指标体系中维度对象列表
	 * 
	 * @return
	 */
	private static Map genAllDims() throws ReportOlapException {
		Map dims = null;
		try {
			String sql = SQLGenator.genSQL("Q5600");
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (null != svces) {
				dims = new HashMap();
				for (int i = 0; i < svces.length; i++) {
					DimensionTable dim = genDimStruct(svces[i], dims);
					dims.put(dim.dim_id, dim);
				}
			}
		} catch (AppException ae) {
			// 这里不处理异常了
			throw new ReportOlapException("获取所有维度对象时发生异常", ae);
		}
		return dims;
	}

	/**
	 * 组装成指标体系中维度表对象
	 * 
	 * @param svces
	 * @param dims
	 * @return
	 */
	private static DimensionTable genDimStruct(String[] svces, Map dims)
			throws ReportOlapException {
		DimensionTable dim = null;
		if (null != svces && null != dims) {
			String dimId = svces[0];
			if (null != dims.get(dimId)) {
				dim = (DimensionTable) dims.get(dimId);
				List levels = dim.dim_levels;
				if (null == levels) {
					levels = new ArrayList();
				}
				DimLevelTable level = genDimLevel(svces);
				if (null != level)
					levels.add(level);
				dim.dim_levels = levels;
			} else {
				dim = genDimStruct(svces);
				List levels = new ArrayList();
				DimLevelTable level = genDimLevel(svces);
				if (null != level)
					levels.add(level);
				dim.dim_levels = levels;
			}
		}
		return dim;
	}

	/**
	 * 组装成指标体系中维度表对象
	 * 
	 * @param svces
	 * @return
	 */
	private static DimensionTable genDimStruct(String[] svces)
			throws ReportOlapException {
		DimensionTable dim = null;
		if (null == svces || svces.length <= 0)
			throw new ReportOlapException("生成维度对象时输入的参数为空");
		dim = new DimensionTable();
		dim.dim_id = svces[0];
		dim.dim_name = svces[1];
		dim.dim_desc = svces[2];
		dim.dim_type = svces[3];
		dim.dim_table = svces[4];
		dim.code_idfld = svces[5];
		dim.idfld_type = svces[6];
		dim.code_descfld = svces[7];
		dim.dim_unit = svces[8];
		dim.is_deptdim = svces[9];
		dim.to_userlvl = svces[10];
		dim.is_valid = svces[11];
		dim.val_date = svces[12];
		dim.exp_date = svces[13];
		dim.rsv_fld1 = svces[14];
		dim.rsv_fld2 = svces[15];
		dim.rsv_fld3 = svces[16];
		return dim;
	}

	/**
	 * 生成维度对象层次列表
	 * 
	 * @param svces
	 * @return
	 */
	private static DimLevelTable genDimLevel(String[] svces)
			throws ReportOlapException {
		DimLevelTable level = null;
		if (null == svces || svces.length <= 0)
			throw new ReportOlapException("生成维度层次对象时输入的参数为空");
		if (null != svces[17] && !"".equals(svces[17])) {
			level = new DimLevelTable();
			level.dim_id = svces[17];
			level.lvl_id = svces[18];
			level.lvl_name = svces[19];
			level.lvl_desc = svces[20];
			level.lvl_table = svces[21];
			level.parent_idfld = svces[22];
			level.pidfld_type = svces[23];
			level.code_idfld = svces[24];
			level.id_fldtype = svces[25];
			level.code_descfld = svces[26];
			level.to_userlvl = svces[27];
			level.is_valid = svces[28];
			level.rsv_fld1 = svces[29];
			level.rsv_fld2 = svces[30];
			level.rsv_fld3 = svces[31];
		}
		return level;
	}
}
