package com.ailk.bi.report.dao;

import com.ailk.bi.base.exception.ReportHeadException;
import com.ailk.bi.common.app.AppException;

public interface ILReportHeadDao {

	/**
	 * 获取报表HTML表头代码(展示用)
	 * 
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract String getReportHead(String rpt_id);

	/**
	 * 获取报表HTML表头代码(报表维护用)
	 * 
	 * @param rpt_id
	 * @return
	 * @throws ReportHeadException
	 */
	public abstract String getReportHeadDefine(String rpt_id)
			throws ReportHeadException;

	/**
	 * 添加新的表头信息
	 * 
	 * @param rpt_id
	 * @param rptHead
	 * @throws ReportHeadException
	 */
	public abstract void insertRptHead(String rpt_id, String rptHead)
			throws ReportHeadException;

	/**
	 * 删除报表表头
	 * 
	 * @param rpt_id
	 * @throws ReportHeadException
	 */
	public abstract void deleteRptHead(String rpt_id)
			throws ReportHeadException;

	/**
	 * 总账报表描述
	 * 
	 * @param rpt_id
	 *            ,rpt_date
	 * @return
	 * @throws AppException
	 */
	public abstract String[][] getGLRptComment(String rpt_id);

	/**
	 * 总账数据生成时间
	 * 
	 * @param rpt_id
	 * @return
	 * @throws AppException
	 */
	public abstract String getGLRptCreateDate(String rpt_id, String rpt_date);
}
