package com.ailk.bi.subject.admin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ailk.bi.base.exception.ReportHeadException;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.subject.admin.dao.ISubjectTableDefDao;
import com.ailk.bi.subject.admin.entity.UiPubInfoChartDef;
import com.ailk.bi.subject.admin.entity.UiPubInfoCondition;
import com.ailk.bi.subject.admin.entity.UiSubjectCommDimhierarchy;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonColDef;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonRptHead;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonTableDef;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SubjectTableDefDaoImpl implements ISubjectTableDefDao {
	private static Log logger = LogFactory.getLog(SubjectTableDefDaoImpl.class);

	public List getSubjectCommonTblDefInfo(String tbl_id, String tbl_name,
			String rpt_cycle) {

		List list = new ArrayList();

		String sql = "select * from ui_subject_common_table_def where 1=1";
		if (tbl_id.length() > 0) {
			sql += " and table_id like '%" + tbl_id + "%'";
		}
		if (tbl_name.length() > 0) {
			sql += " and table_name like %'" + tbl_name + "%'";
		}
		if (rpt_cycle.length() > 0) {
			sql += " and time_level=" + rpt_cycle;
		}

		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					UiSubjectCommonTableDef tableDef = new UiSubjectCommonTableDef();
					tableDef.setTableId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setTableName(StringB.NulltoBlank(svces[i][1]));
					tableDef.setTableDesc(StringB.NulltoBlank(svces[i][2]));
					tableDef.setDataTable(StringB.NulltoBlank(svces[i][3]));
					tableDef.setDataWhere(StringB.NulltoBlank(svces[i][4]));
					tableDef.setHasExpand(StringB.NulltoBlank(svces[i][5]));
					tableDef.setHasPaging(StringB.NulltoBlank(svces[i][6]));
					tableDef.setTimeField(StringB.NulltoBlank(svces[i][7]));
					tableDef.setFieldType(StringB.NulltoBlank(svces[i][8]));
					tableDef.setTimeLevel(StringB.NulltoBlank(svces[i][9]));

					tableDef.setRowClickedChartChange(StringB
							.NulltoBlank(svces[i][10]));
					tableDef.setRltChartId(StringB.NulltoBlank(svces[i][11]));
					tableDef.setHasHead(StringB.NulltoBlank(svces[i][12]));
					tableDef.setSumDisplay(StringB.NulltoBlank(svces[i][13]));
					tableDef.setDimAscol(StringB.NulltoBlank(svces[i][14]));

					tableDef.setCustomMsu(StringB.NulltoBlank(svces[i][15]));
					tableDef.setThrowOld(StringB.NulltoBlank(svces[i][16]));
					tableDef.setHasExpandall(StringB.NulltoBlank(svces[i][17]));
					tableDef.setChartChangeJs(StringB.NulltoBlank(svces[i][18]));
					tableDef.setTableType(StringB.NulltoBlank(svces[i][19]));
					list.add(tableDef);
				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return list;
	}

	public UiSubjectCommonTableDef getSubjectCommonTblDefInfo(String tbl_id) {

		String sql = "select * from ui_subject_common_table_def where table_id='"
				+ tbl_id + "'";
		UiSubjectCommonTableDef tableDef = null;
		try {
			logger.info("sql-" + sql);

			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				tableDef = new UiSubjectCommonTableDef();
				tableDef.setTableId(StringB.NulltoBlank(svces[0][0]));
				tableDef.setTableName(StringB.NulltoBlank(svces[0][1]));
				tableDef.setTableDesc(StringB.NulltoBlank(svces[0][2]));
				tableDef.setDataTable(StringB.NulltoBlank(svces[0][3]));
				tableDef.setDataWhere(StringB.NulltoBlank(svces[0][4]));
				tableDef.setHasExpand(StringB.NulltoBlank(svces[0][5]));
				tableDef.setHasPaging(StringB.NulltoBlank(svces[0][6]));
				tableDef.setTimeField(StringB.NulltoBlank(svces[0][7]));
				tableDef.setFieldType(StringB.NulltoBlank(svces[0][8]));
				tableDef.setTimeLevel(StringB.NulltoBlank(svces[0][9]));

				tableDef.setRowClickedChartChange(StringB
						.NulltoBlank(svces[0][10]));
				tableDef.setRltChartId(StringB.NulltoBlank(svces[0][11]));
				tableDef.setHasHead(StringB.NulltoBlank(svces[0][12]));
				tableDef.setSumDisplay(StringB.NulltoBlank(svces[0][13]));
				tableDef.setDimAscol(StringB.NulltoBlank(svces[0][14]));

				tableDef.setCustomMsu(StringB.NulltoBlank(svces[0][15]));
				tableDef.setThrowOld(StringB.NulltoBlank(svces[0][16]));
				tableDef.setHasExpandall(StringB.NulltoBlank(svces[0][17]));
				tableDef.setChartChangeJs(StringB.NulltoBlank(svces[0][18]));
				tableDef.setTableType(StringB.NulltoBlank(svces[0][19]));

			}

		} catch (AppException e) {

			e.printStackTrace();
		}

		return tableDef;

	}

	public void insertTableDef(UiSubjectCommonTableDef obj) {

		List svc = new ArrayList();
		svc.add(obj.getTableId());
		svc.add(obj.getTableName());
		svc.add(obj.getTableDesc());
		svc.add(obj.getDataTable());
		svc.add(StringB.replace(obj.getDataWhere(), "'", "'||chr(39)||'"));
		svc.add(obj.getHasExpand());
		svc.add(obj.getHasPaging());
		svc.add(obj.getTimeField());
		svc.add(obj.getFieldType());
		svc.add(obj.getTimeLevel());
		svc.add(obj.getRowClickedChartChange());
		svc.add(obj.getRltChartId());
		svc.add(obj.getHasHead());
		svc.add(obj.getSumDisplay());
		svc.add(obj.getDimAscol());
		svc.add(obj.getCustomMsu());
		svc.add(obj.getThrowOld());
		svc.add(obj.getHasExpandall());
		svc.add(obj.getChartChangeJs());
		svc.add(obj.getTableType());
		String[] paramReport = null;

		if (svc != null && svc.size() >= 0) {
			paramReport = (String[]) svc.toArray(new String[svc.size()]);
		}

		try {
			String sqlDel = " delete from ui_subject_common_table_def where table_id='"
					+ obj.getTableId() + "'";
			logger.info(sqlDel);
			WebDBUtil.execUpdate(sqlDel);

			String sql = SQLGenator.genSQL("ANSRPT001_I", paramReport);
			logger.info("sql-" + sql);
			WebDBUtil.execUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}

	}

	public UiSubjectCommonRptHead getCommonRptHeadInfo(String tbl_id) {

		String sql = "select * from ui_subject_common_rtphead where table_id='"
				+ tbl_id + "'";
		UiSubjectCommonRptHead rtphead = new UiSubjectCommonRptHead();
		try {
			logger.info("sql-" + sql);
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				rtphead.setTableId(tbl_id);
				rtphead.setTableHeader(StringB.NulltoBlank(svces[0][1]));
				String rowSpan = StringB.NulltoBlank(svces[0][2]);
				if (rowSpan.length() > 0) {
					rtphead.setRowSpan(new Long(rowSpan));
				} else {
					rtphead.setRowSpan(null);
				}

			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return rtphead;
	}

	public void insertCommonRptHead(UiSubjectCommonRptHead obj) {

		String rpt_id = obj.getTableId();

		if (null != rpt_id && !"".equals(rpt_id)) {
			try {
				String update = null;
				String strSql = "SELECT table_ID,table_HEADER FROM ui_subject_common_rtphead WHERE table_ID='"
						+ rpt_id + "'";
				// 判断是否已经有表头了
				logger.info("strSql:" + strSql);

				String[][] ret = WebDBUtil.execQryArray(strSql, "");
				String head = obj.getTableHeader();
				head = head.replaceAll("'", "");
				head = head.replaceAll("\"", "");

				// rptHearClear
				if (null != ret && ret.length > 0) {
					// 更新记录
					update = SQLGenator
							.genSQL("ANSRPT002_U", obj.getTableHeader(),
									obj.getRowSpan() + "", rpt_id);
					// update = "UPDATE UI_RPT_INFO_HEAD SET RPT_HEADER=? WHERE
					// RPT_ID=?";
				} else {
					// 插入记录
					update = SQLGenator.genSQL("ANSRPT002_I", rpt_id,
							obj.getTableHeader(), obj.getRowSpan() + "");
					// update = "INSERT INTO UI_RPT_INFO_HEAD(RPT_HEADER,RPT_ID)
					// VALUES(?,?)";
				}
				logger.info("update:" + update);

				WebDBUtil.execUpdate(update);
				String strHasHead = "update ui_subject_common_table_def set has_head='Y' Where table_id='"
						+ rpt_id + "'";
				WebDBUtil.execUpdate(strHasHead);

			} catch (AppException ae) {
				System.out.println(ae);
				throw new ReportHeadException("保存报表表头失败", ae);
			}
		}

	}

	public void deleteCommonRptHead(String tbl_id) {

		if (null == tbl_id || "".equals(tbl_id))
			throw new IllegalArgumentException("报表ID标识为空");
		try {
			String sql = " delete from ui_subject_common_rtphead where table_id='"
					+ tbl_id + "'";
			WebDBUtil.execUpdate(sql);
			String strHasHead = "update ui_subject_common_table_def set has_head='N' Where table_id='"
					+ tbl_id + "'";
			WebDBUtil.execUpdate(strHasHead);

		} catch (AppException ae) {
			throw new ReportHeadException("删除报表表头出错", ae);
		}
	}

	public void deleteTableDef(String tbl_id) {

		if (null == tbl_id || "".equals(tbl_id))
			throw new IllegalArgumentException("报表ID标识为空");
		try {

			String sqlTrans[] = new String[6];
			sqlTrans[0] = "delete from ui_pub_info_editor where obj_id='"
					+ tbl_id + "'";
			sqlTrans[1] = "delete from ui_pub_info_condition where res_id='"
					+ tbl_id + "'";
			sqlTrans[2] = "delete from ui_subject_common_dimhierarchy where table_id='"
					+ tbl_id + "'";
			sqlTrans[3] = "delete from UI_SUBJECT_COMMON_RTPHEAD where table_id='"
					+ tbl_id + "'";
			sqlTrans[4] = "delete from UI_SUBJECT_COMMON_TABLE_COLDEF where table_id='"
					+ tbl_id + "'";
			sqlTrans[5] = "delete from ui_subject_common_table_def where table_id='"
					+ tbl_id + "'";
			WebDBUtil.execTransUpdate(sqlTrans);

		} catch (AppException ae) {
			throw new ReportHeadException("删除出错", ae);
		}

	}

	public List getSubjectCommonTblDefDrillInfo(String tbl_id) {

		List list = new ArrayList();

		String sql = "select * from ui_subject_common_dimhierarchy where table_id='"
				+ tbl_id + "'";

		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					UiSubjectCommDimhierarchy tableDef = new UiSubjectCommDimhierarchy();
					tableDef.setTableId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setColId(StringB.NulltoBlank(svces[i][1]));
					tableDef.setLevId(StringB.NulltoBlank(svces[i][2]));
					tableDef.setLevName(StringB.NulltoBlank(svces[i][3]));
					tableDef.setLevMemo(StringB.NulltoBlank(svces[i][4]));
					tableDef.setSrcIdfld(StringB.NulltoBlank(svces[i][5]));
					tableDef.setIdfldType(StringB.NulltoBlank(svces[i][6]));
					tableDef.setSrcNamefld(StringB.NulltoBlank(svces[i][7]));
					tableDef.setDescAstitle(StringB.NulltoBlank(svces[i][8]));
					tableDef.setHasLink(StringB.NulltoBlank(svces[i][9]));

					tableDef.setLinkUrl(StringB.NulltoBlank(svces[i][10]));
					tableDef.setLinkTarget(StringB.NulltoBlank(svces[i][11]));
					list.add(tableDef);
				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return list;
	}

	public void insertTableDefDrill(UiSubjectCommDimhierarchy obj) {

		List svc = new ArrayList();
		svc.add(obj.getTableId());
		svc.add(obj.getColId());
		svc.add(obj.getLevId());
		svc.add(obj.getLevName());

		svc.add(obj.getLevMemo());
		svc.add(obj.getSrcIdfld());
		svc.add(obj.getIdfldType());
		svc.add(obj.getSrcNamefld());

		svc.add(obj.getDescAstitle());
		svc.add(obj.getHasLink());
		svc.add(StringB.replace(obj.getLinkUrl(), "'", "'||chr(39)||'"));

		svc.add(obj.getLinkTarget());
		String[] paramReport = null;

		if (svc != null && svc.size() >= 0) {
			paramReport = (String[]) svc.toArray(new String[svc.size()]);
		}

		try {

			// 不确信是否一个table_id只有一个钻取设置，暂时定为一个设置，因此先删除所有钻取设置
			String sqlDel = " delete from ui_subject_common_dimhierarchy where table_id='"
					+ obj.getTableId() + "'";
			logger.info(sqlDel);
			WebDBUtil.execUpdate(sqlDel);

			String sql = SQLGenator.genSQL("ANSRPT003_I", paramReport);
			logger.info("ANSRPT003_I：" + sql);
			WebDBUtil.execUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public void deleteTableDefDrill(String tbl_id) {

		if (null == tbl_id || "".equals(tbl_id))
			throw new IllegalArgumentException("报表ID标识为空");
		try {
			String sqlTrans = "delete from ui_subject_common_dimhierarchy where table_id='"
					+ tbl_id + "'";
			WebDBUtil.execUpdate(sqlTrans);

		} catch (AppException ae) {
			throw new ReportHeadException("删除出错", ae);
		}
	}

	public List getSubjectCommonTblCoInfo(String tbl_id) {

		List list = new ArrayList();

		String sql = "select a.*,ROWIDTOCHAR(a.rowid) num1 from ui_subject_common_table_coldef a where a.table_id='"
				+ tbl_id + "' order by Col_Sequence";

		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					UiSubjectCommonColDef tableDef = new UiSubjectCommonColDef();
					tableDef.setTableId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setColId(StringB.NulltoBlank(svces[i][1]));
					tableDef.setColName(StringB.NulltoBlank(svces[i][2]));
					tableDef.setColDesc(StringB.NulltoBlank(svces[i][3]));
					tableDef.setColSequence(StringB.NulltoBlank(svces[i][4]));
					tableDef.setIsMeasure(StringB.NulltoBlank(svces[i][5]));
					tableDef.setDimAswhere(StringB.NulltoBlank(svces[i][6]));
					tableDef.setDefaultDisplay(StringB.NulltoBlank(svces[i][7]));
					tableDef.setIsExpandCol(StringB.NulltoBlank(svces[i][8]));
					tableDef.setDefaultDrilled(StringB.NulltoBlank(svces[i][9]));

					tableDef.setInitLevel(StringB.NulltoBlank(svces[i][10]));
					tableDef.setDimAscol(StringB.NulltoBlank(svces[i][11]));
					tableDef.setCodeField(StringB.NulltoBlank(svces[i][12]));
					tableDef.setDescField(StringB.NulltoBlank(svces[i][13]));
					tableDef.setIsRatio(StringB.NulltoBlank(svces[i][14]));
					tableDef.setDataType(StringB.NulltoBlank(svces[i][15]));
					tableDef.setDigitLength(StringB.NulltoBlank(svces[i][16]));
					tableDef.setHasComratio(StringB.NulltoBlank(svces[i][17]));
					tableDef.setHasLink(StringB.NulltoBlank(svces[i][18]));
					tableDef.setLinkUrl(StringB.NulltoBlank(svces[i][19]));

					tableDef.setLinkTarget(StringB.NulltoBlank(svces[i][20]));
					tableDef.setHasLast(StringB.NulltoBlank(svces[i][21]));
					tableDef.setLastDisplay(StringB.NulltoBlank(svces[i][22]));
					tableDef.setRiseArrowColor(StringB
							.NulltoBlank(svces[i][23]));
					tableDef.setHasLastLink(StringB.NulltoBlank(svces[i][24]));
					tableDef.setLastUrl(StringB.NulltoBlank(svces[i][25]));
					tableDef.setLastTarget(StringB.NulltoBlank(svces[i][26]));
					tableDef.setHasLoop(StringB.NulltoBlank(svces[i][27]));
					tableDef.setLoopDisplay(StringB.NulltoBlank(svces[i][28]));
					tableDef.setHasLoopLink(StringB.NulltoBlank(svces[i][29]));

					tableDef.setLoopUrl(StringB.NulltoBlank(svces[i][30]));
					tableDef.setLoopTarget(StringB.NulltoBlank(svces[i][31]));
					tableDef.setIsColClickChartChange(StringB
							.NulltoBlank(svces[i][32]));
					tableDef.setColRltChartId(StringB.NulltoBlank(svces[i][33]));
					tableDef.setIsCellClickChartChange(StringB
							.NulltoBlank(svces[i][34]));
					tableDef.setCellRltChartId(StringB
							.NulltoBlank(svces[i][35]));
					tableDef.setStatus(StringB.NulltoBlank(svces[i][36]));
					tableDef.setDescAstitle(StringB.NulltoBlank(svces[i][37]));
					tableDef.setLastVarDisplay(StringB
							.NulltoBlank(svces[i][38]));
					tableDef.setLoopVarDisplay(StringB
							.NulltoBlank(svces[i][39]));
					tableDef.setTotalDisplayed(StringB
							.NulltoBlank(svces[i][40]));
					tableDef.setRowId(StringB.NulltoBlank(svces[i][41]));

					list.add(tableDef);
				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return list;
	}

	public void insertCommonTblCoInfo(UiSubjectCommonColDef obj) {

		List svc = new ArrayList();
		svc.add(obj.getTableId());
		svc.add(obj.getColId());
		svc.add(obj.getColName());
		svc.add(obj.getColDesc());
		svc.add(obj.getColSequence());
		svc.add(obj.getIsMeasure());
		svc.add(obj.getDimAswhere());
		svc.add(obj.getDefaultDisplay());
		svc.add(obj.getIsExpandCol());
		svc.add(obj.getDefaultDrilled());

		svc.add(obj.getInitLevel());
		svc.add(obj.getDimAscol());
		svc.add(obj.getCodeField());
		svc.add(obj.getDescField());
		svc.add(obj.getIsRatio());
		svc.add(obj.getDataType());
		svc.add(obj.getDigitLength());
		svc.add(obj.getHasComratio());
		svc.add(obj.getHasLink());
		svc.add(obj.getLinkUrl());

		svc.add(obj.getLinkTarget());
		svc.add(obj.getHasLast());
		svc.add(obj.getLastDisplay());
		svc.add(obj.getRiseArrowColor());
		svc.add(obj.getHasLastLink());
		svc.add(obj.getLastUrl());
		svc.add(obj.getLastTarget());
		svc.add(obj.getHasLoop());
		svc.add(obj.getLoopDisplay());
		svc.add(obj.getHasLoopLink());

		svc.add(obj.getLoopUrl());
		svc.add(obj.getLoopTarget());
		svc.add(obj.getIsColClickChartChange());
		svc.add(obj.getColRltChartId());
		svc.add(obj.getIsCellClickChartChange());
		svc.add(obj.getCellRltChartId());
		svc.add(obj.getStatus());
		svc.add(obj.getDescAstitle());
		svc.add(obj.getLastVarDisplay());
		svc.add(obj.getLoopVarDisplay());

		svc.add(obj.getTotalDisplayed());

		String[] paramReport = null;

		if (svc != null && svc.size() >= 0) {
			paramReport = (String[]) svc.toArray(new String[svc.size()]);
		}
		logger.info("svc.size:" + svc.size());
		try {
			// 修改时，根据rowid删除原始的
			if (obj.getRowId().length() > 0) {
				deleteTableColDef(obj.getRowId());

			}

			String sql = SQLGenator.genSQL("ANSRPT004_I", paramReport);
			logger.info("ANSRPT004_I：" + sql);
			WebDBUtil.execUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public UiSubjectCommonColDef getSubjectCommonTblColInfo(String row_id) {

		UiSubjectCommonColDef tableDef = null;

		String sql = "select a.*,ROWIDTOCHAR(a.rowid) num1 from ui_subject_common_table_coldef a where a.rowid='"
				+ row_id + "' order by Col_Sequence";

		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					tableDef = new UiSubjectCommonColDef();

					tableDef.setTableId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setColId(StringB.NulltoBlank(svces[i][1]));
					tableDef.setColName(StringB.NulltoBlank(svces[i][2]));
					tableDef.setColDesc(StringB.NulltoBlank(svces[i][3]));
					tableDef.setColSequence(StringB.NulltoBlank(svces[i][4]));
					tableDef.setIsMeasure(StringB.NulltoBlank(svces[i][5]));
					tableDef.setDimAswhere(StringB.NulltoBlank(svces[i][6]));
					tableDef.setDefaultDisplay(StringB.NulltoBlank(svces[i][7]));
					tableDef.setIsExpandCol(StringB.NulltoBlank(svces[i][8]));
					tableDef.setDefaultDrilled(StringB.NulltoBlank(svces[i][9]));

					tableDef.setInitLevel(StringB.NulltoBlank(svces[i][10]));
					tableDef.setDimAscol(StringB.NulltoBlank(svces[i][11]));
					tableDef.setCodeField(StringB.NulltoBlank(svces[i][12]));
					tableDef.setDescField(StringB.NulltoBlank(svces[i][13]));
					tableDef.setIsRatio(StringB.NulltoBlank(svces[i][14]));
					tableDef.setDataType(StringB.NulltoBlank(svces[i][15]));
					tableDef.setDigitLength(StringB.NulltoBlank(svces[i][16]));
					tableDef.setHasComratio(StringB.NulltoBlank(svces[i][17]));
					tableDef.setHasLink(StringB.NulltoBlank(svces[i][18]));
					tableDef.setLinkUrl(StringB.NulltoBlank(svces[i][19]));

					tableDef.setLinkTarget(StringB.NulltoBlank(svces[i][20]));
					tableDef.setHasLast(StringB.NulltoBlank(svces[i][21]));
					tableDef.setLastDisplay(StringB.NulltoBlank(svces[i][22]));
					tableDef.setRiseArrowColor(StringB
							.NulltoBlank(svces[i][23]));
					tableDef.setHasLastLink(StringB.NulltoBlank(svces[i][24]));
					tableDef.setLastUrl(StringB.NulltoBlank(svces[i][25]));
					tableDef.setLastTarget(StringB.NulltoBlank(svces[i][26]));
					tableDef.setHasLoop(StringB.NulltoBlank(svces[i][27]));
					tableDef.setLoopDisplay(StringB.NulltoBlank(svces[i][28]));
					tableDef.setHasLoopLink(StringB.NulltoBlank(svces[i][29]));

					tableDef.setLoopUrl(StringB.NulltoBlank(svces[i][30]));
					tableDef.setLoopTarget(StringB.NulltoBlank(svces[i][31]));
					tableDef.setIsColClickChartChange(StringB
							.NulltoBlank(svces[i][32]));
					tableDef.setColRltChartId(StringB.NulltoBlank(svces[i][33]));
					tableDef.setIsCellClickChartChange(StringB
							.NulltoBlank(svces[i][34]));
					tableDef.setCellRltChartId(StringB
							.NulltoBlank(svces[i][35]));
					tableDef.setStatus(StringB.NulltoBlank(svces[i][36]));
					tableDef.setDescAstitle(StringB.NulltoBlank(svces[i][37]));
					tableDef.setLastVarDisplay(StringB
							.NulltoBlank(svces[i][38]));
					tableDef.setLoopVarDisplay(StringB
							.NulltoBlank(svces[i][39]));
					tableDef.setTotalDisplayed(StringB
							.NulltoBlank(svces[i][40]));
					tableDef.setRowId(StringB.NulltoBlank(svces[i][41]));

				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return tableDef;
	}

	public void deleteTableColDef(String row_id) {

		if (null == row_id || "".equals(row_id))
			throw new IllegalArgumentException("列rowID标识为空");
		try {
			String sqlTrans = "delete from ui_subject_common_table_coldef where rowid='"
					+ row_id + "'";
			WebDBUtil.execUpdate(sqlTrans);

		} catch (AppException ae) {
			throw new ReportHeadException("删除出错", ae);
		}
	}

	public List getSubjectCommonTblConditionInfo(String tbl_id) {

		List list = new ArrayList();

		String sql = "select a.*,ROWIDTOCHAR(a.rowid) num1  from UI_PUB_INFO_CONDITION a WHERE a.RES_ID='"
				+ tbl_id + "' order by sequence";
		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					UiPubInfoCondition tableDef = new UiPubInfoCondition();
					tableDef.setResId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setResType(StringB.NulltoBlank(svces[i][1]));
					tableDef.setQryType(StringB.NulltoBlank(svces[i][2]));
					tableDef.setQryCode(StringB.NulltoBlank(svces[i][3]));
					tableDef.setConCode(StringB.NulltoBlank(svces[i][4]));
					tableDef.setDataType(StringB.NulltoBlank(svces[i][5]));
					tableDef.setConTag(StringB.NulltoBlank(svces[i][6]));
					tableDef.setSequence(StringB.NulltoBlank(svces[i][7]));
					tableDef.setStatus(StringB.NulltoBlank(svces[i][8]));
					tableDef.setRowId(StringB.NulltoBlank(svces[i][9]));

					list.add(tableDef);
				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return list;
	}

	public void deleteTableCondition(String row_id) {

		if (null == row_id || "".equals(row_id))
			throw new IllegalArgumentException("列rowID标识为空");
		try {
			String sqlTrans = "delete from UI_PUB_INFO_CONDITION where rowid='"
					+ row_id + "'";
			WebDBUtil.execUpdate(sqlTrans);

		} catch (AppException ae) {
			throw new ReportHeadException("删除出错", ae);
		}
	}

	public UiPubInfoCondition getSubjectCommonTblCondition(String row_id) {

		UiPubInfoCondition tableDef = null;

		String sql = "select a.*,ROWIDTOCHAR(a.rowid) num1 from UI_PUB_INFO_CONDITION a where a.rowid='"
				+ row_id + "' order by Sequence";

		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					tableDef = new UiPubInfoCondition();
					tableDef.setResId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setResType(StringB.NulltoBlank(svces[i][1]));
					tableDef.setQryType(StringB.NulltoBlank(svces[i][2]));
					tableDef.setQryCode(StringB.NulltoBlank(svces[i][3]));
					tableDef.setConCode(StringB.NulltoBlank(svces[i][4]));
					tableDef.setDataType(StringB.NulltoBlank(svces[i][5]));
					tableDef.setConTag(StringB.NulltoBlank(svces[i][6]));
					tableDef.setSequence(StringB.NulltoBlank(svces[i][7]));
					tableDef.setStatus(StringB.NulltoBlank(svces[i][8]));
					tableDef.setRowId(StringB.NulltoBlank(svces[i][9]));

				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return tableDef;
	}

	public void insertCommonTblCondition(UiPubInfoCondition obj) {

		List svc = new ArrayList();
		svc.add(obj.getResId());
		svc.add(obj.getResType());
		svc.add(obj.getQryType());
		svc.add(obj.getQryCode());
		svc.add(obj.getConCode());
		svc.add(obj.getDataType());
		svc.add(obj.getConTag());
		svc.add(obj.getSequence());
		svc.add(obj.getStatus());

		String[] paramReport = null;

		if (svc != null && svc.size() >= 0) {
			paramReport = (String[]) svc.toArray(new String[svc.size()]);
		}
		logger.info("svc.size:" + svc.size());
		try {
			// 修改时，根据rowid删除原始的
			if (obj.getRowId().length() > 0) {
				deleteTableCondition(obj.getRowId());

			}

			String sql = SQLGenator.genSQL("ANSRPT005_I", paramReport);
			logger.info("ANSRPT005_I：" + sql);
			WebDBUtil.execUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public void deleteTableChartInfo(String table_id) {

		if (null == table_id || "".equals(table_id))
			throw new IllegalArgumentException("图型chart_id标识为空");
		try {
			String sqlTrans = "delete from UI_PUB_INFO_CHART_DEF where chart_ID='"
					+ table_id + "'";
			WebDBUtil.execUpdate(sqlTrans);

		} catch (AppException ae) {
			throw new ReportHeadException("删除出错", ae);
		}
	}

	public UiPubInfoChartDef getSubjectCommonChartInfo(String tbl_id) {

		UiPubInfoChartDef tableDef = null;
		String sql = "select * from UI_PUB_INFO_CHART_DEF a WHERE a.chart_ID='"
				+ tbl_id + "' order by chart_id";
		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					tableDef = new UiPubInfoChartDef();
					tableDef.setChartId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setChartBelong(StringB.NulltoBlank(svces[i][1]));
					tableDef.setChartType(StringB.NulltoBlank(svces[i][2]));
					tableDef.setChartIndex(StringB.NulltoBlank(svces[i][3]));
					tableDef.setChartAttribute(StringB.NulltoBlank(svces[i][4]));
					tableDef.setSqlMain(StringB.NulltoBlank(svces[i][5]));
					tableDef.setSqlWhere(StringB.NulltoBlank(svces[i][6]));
					tableDef.setSqlOrder(StringB.NulltoBlank(svces[i][7]));
					tableDef.setIsusecd(StringB.NulltoBlank(svces[i][8]));
					tableDef.setCategoryIndex(StringB.NulltoBlank(svces[i][9]));

					tableDef.setSeriesIndex(StringB.NulltoBlank(svces[i][10]));
					tableDef.setSeriesLength(StringB.NulltoBlank(svces[i][11]));
					tableDef.setSeriesCut(StringB.NulltoBlank(svces[i][12]));
					tableDef.setSeriesCutLen(StringB.NulltoBlank(svces[i][13]));
					tableDef.setValueIndex(StringB.NulltoBlank(svces[i][14]));
					tableDef.setCategoryDesc(StringB.NulltoBlank(svces[i][15]));
					tableDef.setCategoryDescIndex(StringB
							.NulltoBlank(svces[i][16]));
					tableDef.setChartDistype(StringB.NulltoBlank(svces[i][17]));

				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}
		return tableDef;
	}

	public void insertSubjectCommonChart(UiPubInfoChartDef obj) {

		List svc = new ArrayList();

		svc.add(obj.getChartId());
		svc.add(obj.getChartBelong());
		svc.add(obj.getChartType());
		svc.add(obj.getChartIndex());
		svc.add(obj.getChartAttribute());
		svc.add(obj.getSqlMain());
		svc.add(obj.getSqlWhere());
		svc.add(obj.getSqlOrder());
		svc.add(obj.getIsusecd());
		svc.add(obj.getCategoryIndex());

		svc.add(obj.getSeriesIndex());
		svc.add(obj.getSeriesLength());
		svc.add(obj.getSeriesCut());
		svc.add(obj.getSeriesCutLen());
		svc.add(obj.getValueIndex());
		svc.add(obj.getCategoryDesc());
		svc.add(obj.getCategoryDescIndex());
		svc.add(obj.getChartDistype());

		String[] paramReport = null;

		if (svc != null && svc.size() >= 0) {
			paramReport = (String[]) svc.toArray(new String[svc.size()]);
		}
		logger.info("svc.size:" + svc.size());
		try {
			// 修改时，根据CHART_id删除原始的
			deleteTableChartInfo(obj.getChartId());

			String sql = SQLGenator.genSQL("ANSRPT006_I", paramReport);
			logger.info("ANSRPT006_I：" + sql);
			WebDBUtil.execUpdate(sql);

		} catch (AppException e) {

			e.printStackTrace();
		}
	}

	public List getSubjectCommonChartInfoList(String tbl_id) {

		List list = new ArrayList();

		String sql = "select * from UI_PUB_INFO_CHART_DEF a WHERE a.chart_ID like '%"
				+ tbl_id + "%' order by chart_id";
		try {
			String[][] svces = WebDBUtil.execQryArray(sql, "");
			if (svces != null && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					UiPubInfoChartDef tableDef = new UiPubInfoChartDef();
					tableDef.setChartId(StringB.NulltoBlank(svces[i][0]));
					tableDef.setChartBelong(StringB.NulltoBlank(svces[i][1]));
					tableDef.setChartType(StringB.NulltoBlank(svces[i][2]));
					tableDef.setChartIndex(StringB.NulltoBlank(svces[i][3]));
					tableDef.setChartAttribute(StringB.NulltoBlank(svces[i][4]));
					tableDef.setSqlMain(StringB.NulltoBlank(svces[i][5]));
					tableDef.setSqlWhere(StringB.NulltoBlank(svces[i][6]));
					tableDef.setSqlOrder(StringB.NulltoBlank(svces[i][7]));
					tableDef.setIsusecd(StringB.NulltoBlank(svces[i][8]));
					tableDef.setCategoryIndex(StringB.NulltoBlank(svces[i][9]));

					tableDef.setSeriesIndex(StringB.NulltoBlank(svces[i][10]));
					tableDef.setSeriesLength(StringB.NulltoBlank(svces[i][11]));
					tableDef.setSeriesCut(StringB.NulltoBlank(svces[i][12]));
					tableDef.setSeriesCutLen(StringB.NulltoBlank(svces[i][13]));
					tableDef.setValueIndex(StringB.NulltoBlank(svces[i][14]));
					tableDef.setCategoryDesc(StringB.NulltoBlank(svces[i][15]));
					tableDef.setCategoryDescIndex(StringB
							.NulltoBlank(svces[i][16]));
					tableDef.setChartDistype(StringB.NulltoBlank(svces[i][17]));
					list.add(tableDef);

				}
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return list;
	}

}
