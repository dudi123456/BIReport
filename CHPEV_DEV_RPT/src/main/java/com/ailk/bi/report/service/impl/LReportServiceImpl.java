package com.ailk.bi.report.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ailk.bi.base.exception.ReportHeadException;
import com.ailk.bi.base.table.DimensionTable;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.ReflectUtil;
import com.ailk.bi.report.dao.ILReportBaseDateDao;
import com.ailk.bi.report.dao.ILReportColDao;
import com.ailk.bi.report.dao.ILReportDao;
import com.ailk.bi.report.dao.ILReportDataDao;
import com.ailk.bi.report.dao.ILReportFilterDao;
import com.ailk.bi.report.dao.ILReportHeadDao;
import com.ailk.bi.report.dao.ILReportPrintDao;
import com.ailk.bi.report.dao.ILReportProcessDao;
import com.ailk.bi.report.dao.impl.LReportBaseDateDao;
import com.ailk.bi.report.dao.impl.LReportColDao;
import com.ailk.bi.report.dao.impl.LReportDao;
import com.ailk.bi.report.dao.impl.LReportDataDao;
import com.ailk.bi.report.dao.impl.LReportFilterDao;
import com.ailk.bi.report.dao.impl.LReportHeadDao;
import com.ailk.bi.report.dao.impl.LReportPrintDao;
import com.ailk.bi.report.dao.impl.LReportProcessDao;
import com.ailk.bi.report.service.ILReportService;

@SuppressWarnings({ "rawtypes" })
public class LReportServiceImpl implements ILReportService {

	private ILReportDao reportDao = new LReportDao();

	private ILReportColDao reportColDao = new LReportColDao();

	private ILReportFilterDao reportFilterDao = new LReportFilterDao();

	private ILReportHeadDao headDao = new LReportHeadDao();

	private ILReportDataDao dataDao = new LReportDataDao();

	private ILReportProcessDao processDao = new LReportProcessDao();

	private ILReportPrintDao printDao = new LReportPrintDao();

	private ILReportBaseDateDao baseDao = new LReportBaseDateDao();

	/*
	 * (non-Javadoc)
	 *
	 * @see com.asiabi.report.service.ILReportService#getSelfReportID()
	 */
	public String getSelfReportID() throws AppException {
		return reportDao.getSelfReportID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#existReport(java.lang.String)
	 */
	public boolean existReport(String rpt_id) {
		boolean isExist = false;
		Object rptTable = null;
		try {
			rptTable = reportDao.getReport(rpt_id);
		} catch (AppException ex) {
			rptTable = null;
		}
		if (rptTable != null) {
			isExist = true;
		}
		return isExist;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#processReport(java.lang.Object,
	 * java.lang.String)
	 */
	public boolean processReport(Object RptTable, String rpt_date) {
		boolean isProcess = false;
		// 检查报表拥有流程匹配信息
		try {
			String rpt_id = ReflectUtil.getStringFromObj(RptTable, "rpt_id");
			String p_id = getReportProcessID(rpt_id);
			if (!StringTool.checkEmptyString(p_id)) {
				return true;
			}
		} catch (AppException e) {
			return false;
		}
		// 检查该报表目前周期的审核信息
		try {
			List processHis = getReportProcessStep(RptTable, rpt_date);
			if (processHis != null && processHis.size() > 0) {
				return true;
			}
		} catch (AppException e) {
			return false;
		}
		return isProcess;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReport(java.lang.String)
	 */
	public Object getReport(String rpt_id) throws AppException {
		if (null == rpt_id || "".equals(rpt_id))
			throw new AppException();
		return reportDao.getReport(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportManager#getReports(java.lang.String)
	 */
	public List getReports(String whereStr) throws AppException {
		return reportDao.getReports(whereStr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getRoleReports(java.lang.String
	 * , java.lang.String)
	 */
	public List getRoleReports(String whereStr, String role_id)
			throws AppException {
		return reportDao.getRoleReports(whereStr, role_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReport(java.lang.Object)
	 */
	public void insertReport(Object RptTable) throws AppException {
		reportDao.insertReport(RptTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#updateReport(java.lang.Object)
	 */
	public void updateReport(Object RptTable) throws AppException {
		reportDao.updateReport(RptTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#delReport(java.lang.String)
	 */
	public void delReport(String rpt_id) throws AppException {
		reportDao.delReport(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportManager#getReports(java.lang.String)
	 */
	public List getConditionReports(String con_id, String whereStr)
			throws AppException {
		return reportDao.getConditionReports(con_id, whereStr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportCol(java.lang.String,
	 * java.lang.String)
	 */
	public List getReportCol(String rpt_id, String expandcol)
			throws AppException {
		if (null == rpt_id || "".equals(rpt_id))
			throw new AppException();
		return reportColDao.getReportCol(rpt_id, expandcol);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportCol(java.lang.String)
	 */
	public List getReportColDefine(String rpt_id) throws AppException {
		if (null == rpt_id || "".equals(rpt_id))
			throw new AppException();
		return reportColDao.getReportColDefine(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getCustomRptDimTable(java.lang
	 * .String)
	 */
	public DimensionTable[] getCustomRptDimTable(String rpt_id)
			throws AppException {
		return reportColDao.getCustomRptDimTable(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReportCol(java.util.List)
	 */
	public void insertReportCol(List rptColTable) throws AppException {
		reportColDao.insertReportCol(rptColTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#delReportCol(java.lang.String)
	 */
	public void delReportCol(String rpt_id) throws AppException {
		reportColDao.delReportCol(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#delReportNumberCol(java.lang
	 * .String)
	 */
	public void delReportNumberCol(String rpt_id) throws AppException {
		reportColDao.delReportNumberCol(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#delReportCharCol(java.lang.
	 * String)
	 */
	public void delReportCharCol(String rpt_id) throws AppException {
		reportColDao.delReportCharCol(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportManager#getReportFilter(java.lang.String
	 * )
	 */
	public List getReportFilter(String rpt_id) throws AppException {
		return reportFilterDao.getReportFilter(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportFilterDefine(java.
	 * lang.String)
	 */
	public List getReportFilterDefine(String rpt_id) throws AppException {
		return reportFilterDao.getReportFilterDefine(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReportFilter(java.util
	 * .List)
	 */
	public void insertReportFilter(List rptFilterTable) throws AppException {
		reportFilterDao.insertReportFilter(rptFilterTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#delReportFilter(java.lang.String
	 * )
	 */
	public void delReportFilter(String rpt_id) throws AppException {
		reportFilterDao.delReportFilter(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportHead(java.lang.String)
	 */
	public String getReportHead(String rpt_id) {
		return headDao.getReportHead(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportHeadDefine(java.lang
	 * .String)
	 */
	public String getReportHeadDefine(String rpt_id) throws ReportHeadException {
		return headDao.getReportHeadDefine(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertRptHead(java.lang.String,
	 * java.lang.String)
	 */
	public void insertRptHead(String rpt_id, String rptHead)
			throws ReportHeadException {
		headDao.insertRptHead(rpt_id, rptHead);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#deleteRptHead(java.lang.String)
	 */
	public void deleteRptHead(String rpt_id) throws ReportHeadException {
		headDao.deleteRptHead(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportData(com.asiabi.report
	 * .domain.RptResourceTable, java.util.List,
	 * com.asiabi.report.struct.ReportQryStruct)
	 */
	public String[][] getReportData(Object RptTable, List rptCols,
			Object qryStruct, PubInfoConditionTable[] cdnTables)
			throws AppException {
		return dataDao.getReportData(RptTable, rptCols, qryStruct, cdnTables);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#existProcessName(java.lang.
	 * String)
	 */
	public boolean existProcessName(String p_name) {
		boolean isExist = false;
		String whereStr = " AND P_FLAG_NAME='" + p_name + "'";
		List listProcess = null;
		try {
			listProcess = processDao.getProcesses(whereStr);
		} catch (AppException e) {
			e.printStackTrace();
		}
		if (listProcess != null) {
			isExist = true;
		}
		return isExist;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getProcess(java.lang.String)
	 */
	public Object getProcess(String p_id) throws AppException {
		return processDao.getProcess(p_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getProcessDefine(java.lang.
	 * String)
	 */
	public List getProcesses(String whereStr) throws AppException {
		return processDao.getProcesses(whereStr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertProcess(java.lang.Object)
	 */
	public void insertProcess(Object RptProcessTable) throws AppException {
		processDao.insertProcess(RptProcessTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#updateProcess(java.lang.Object)
	 */
	public void updateProcess(Object RptProcessTable) throws AppException {
		processDao.updateProcess(RptProcessTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#delProcess(java.lang.String)
	 */
	public void delProcess(String p_id) throws AppException {
		processDao.delProcess(p_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getProcessStep(java.lang.String
	 * )
	 */
	public List getProcessStep(String p_id) throws AppException {
		return processDao.getProcessStep(p_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertProcessSetp(java.lang
	 * .Object)
	 */
	public void insertProcessStep(List RptProcessStepTable) throws AppException {
		processDao.insertProcessStep(RptProcessStepTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#delProcessSetp(java.lang.String
	 * )
	 */
	public void delProcessStep(String p_id) throws AppException {
		processDao.delProcessStep(p_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportProcess(java.lang.
	 * String)
	 */
	public List getReportProcess(String whereStr) throws AppException {
		return processDao.getReportProcess(whereStr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReportProcess(java.lang
	 * .String, java.util.List, java.lang.String[])
	 */
	public void insertReportProcess(String p_id, List reportProcess,
			String[] reportCheck) throws AppException {
		processDao.insertReportProcess(p_id, reportProcess, reportCheck);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportProcessID(java.lang
	 * .String)
	 */
	public String getReportProcessID(String rpt_id) throws AppException {
		return processDao.getReportProcessID(rpt_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportProcessStep(java.lang
	 * .Object, java.lang.String)
	 */
	public List getReportProcessStep(Object RptTable, String rpt_date)
			throws AppException {
		return processDao.getReportProcessStep(RptTable, rpt_date);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportProcessHis(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public List getReportProcessHis(String rpt_id, String rpt_date,
			String region_id) throws AppException {
		return processDao.getReportProcessHis(rpt_id, rpt_date, region_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReportProcessHis(java
	 * .lang.Object)
	 */
	public void insertReportProcessHis(Object RptHisTable) throws AppException {
		processDao.insertReportProcessHis(RptHisTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReportDispense(java.lang
	 * .String, java.lang.Object, java.lang.String[])
	 */
	public void insertReportDispense(String role_id, Object RptTable,
			String[] reportCheck) throws AppException {
		processDao.insertReportDispense(role_id, RptTable, reportCheck);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#getReportPrint(java.lang.String
	 * )
	 */
	public List getReportPrint(String whereStr) throws AppException {
		return printDao.getReportPrint(whereStr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReportPrint(java.lang
	 * .Object)
	 */
	public void insertReportPrint(Object rptTable) throws AppException {
		printDao.insertReportPrint(rptTable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.asiabi.report.service.ILReportService#insertReportPrint(java.lang
	 * .Object)
	 */
	public String[][] getViewTreeList(String resId, String resType,
			String rptId, String rptName) throws AppException {
		return reportDao.getViewTreeList(resId, resType, rptId, rptName);
	}

	public String getBaseVo(String optTypeId) throws AppException {
		return baseDao.getBaseDate(optTypeId);
	}

	public String[][] getViewTreeList(HttpSession session, String resId,
			String resType, String rptId, String rptName, String tabType)
			throws AppException {

		return reportDao.getViewTreeList(session, resId, resType, rptId,
				rptName, tabType);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.ailk.bi.report.service.ILReportService#getReportCharts(java.lang
	 * .String)
	 */
	public List getReportCharts(String rpt_id) throws Exception {
		return reportDao.getReportCharts(rpt_id);
	}

}
