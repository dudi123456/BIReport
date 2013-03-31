package com.ailk.bi.subject.admin.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.report.util.ReportHeadUtil;
import com.ailk.bi.subject.admin.SubjectCommonConst;
import com.ailk.bi.subject.admin.dao.ISubjectTableDefDao;
import com.ailk.bi.subject.admin.dao.impl.SubjectTableDefDaoImpl;
import com.ailk.bi.subject.admin.entity.UiPubInfoCondition;
import com.ailk.bi.subject.admin.entity.UiSubjectCommDimhierarchy;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonColDef;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonRptHead;
import com.ailk.bi.subject.admin.entity.UiSubjectCommonTableDef;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused" })
public class SubjectTableDefHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6982842713835449698L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		String table_id = StringB.NulltoBlank(request.getParameter("table_id"));
		String table_name = StringB.NulltoBlank(request
				.getParameter("table_name"));
		String rpt_cycle = StringB.NulltoBlank(request
				.getParameter("rpt_cycle"));
		String opt_type = StringB.NulltoBlank(request.getParameter("opt_type"));
		opt_type.replaceAll("\"", "'");
		ISubjectTableDefDao tableDefDao = new SubjectTableDefDaoImpl();
		String strReturn = "";

		if ("list".equals(opt_type)) {// 查询所有ui_subject_common_table_def中数据
			strReturn = "listSubjectCommonTblDef";
			request.setAttribute("table_id", table_id);
			request.setAttribute("table_name", table_name);
			request.setAttribute("rpt_cycle", rpt_cycle);

			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableDefDao.getSubjectCommonTblDefInfo(table_id,
							table_name, rpt_cycle));
		} else if ("edthead".equals(opt_type)) {// 复杂表头
			strReturn = "SubjectCommonHeadEditor";
			request.setAttribute("table_id", table_id);
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableDefDao.getCommonRptHeadInfo(table_id));
		} else if ("saveHead".equals(opt_type)) {// 保存复杂表头
			strReturn = "listSubjectCtrlDealOk";
			UiSubjectCommonRptHead obj = new UiSubjectCommonRptHead();
			String rowspan = StringB.NulltoBlank(request
					.getParameter("row_span"));
			if (rowspan.length() == 0) {
				obj.setRowSpan(null);
			} else {
				obj.setRowSpan(new Long(rowspan));
			}

			obj.setTableId(table_id);
			obj.setTableHeader(transferHeaderContent(StringB
					.NulltoBlank(request.getParameter("content"))));
			tableDefDao.insertCommonRptHead(obj);
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"保存信息成功");
			/*
			 * session.setAttribute(WebKeys.ATTR_ANYFLAG, "1"); throw new
			 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
			 * "<font size=2><b>保存信息成功！</b></font>");
			 */
		} else if ("deleHead".equals(opt_type)) {// 删除复杂表头
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute("table_id", table_id);
			tableDefDao.deleteCommonRptHead(table_id);
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"删除信息成功!");
			/*
			 * session.setAttribute(WebKeys.ATTR_ANYFLAG, "1"); throw new
			 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
			 * "<font size=2><b>删除信息成功！</b></font>");
			 */
		} else if ("addTblDef".equals(opt_type)) {// 导航到增加主表ui_subject_common_table_def页面
			strReturn = "SubjectCommonTblDefAdd";
		} else if ("saveCommTblDef".equals(opt_type)) {// 处理增加主表ui_subject_common_table_def
			saveCommTblDef(request, response);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"修改信息成功!");
		} else if ("edtTblDef".equals(opt_type)) {// 导航到编辑主表ui_subject_common_table_def页面
			UiSubjectCommonTableDef tableDef = tableDefDao
					.getSubjectCommonTblDefInfo(table_id);
			strReturn = "SubjectCommonTblDefEdt";
			request.setAttribute("table_id", table_id);
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableDef);
		} else if ("doEdtCommTblDef".equals(opt_type)) {// 处理修改主表ui_subject_common_table_def
			doEdtCommTblDef(request, response);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"修改信息成功!");
		} else if ("doDelCommTblDef".equals(opt_type)) {// 删除主表ui_subject_common_table_def
			request.setAttribute("table_id", table_id);
			tableDefDao.deleteTableDef(table_id);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"修改信息成功!");
			/*
			 * session.setAttribute(WebKeys.ATTR_ANYFLAG, "1"); throw new
			 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
			 * "<font size=2><b>删除信息成功！</b></font>");
			 */
		} else if ("listDrillset".equals(opt_type)) {// 导航到钻取设置页面ui_subject_common_dimhierarchy
			strReturn = "listSubjectCommonTblDefDrill";
			request.setAttribute("table_id", table_id);
			request.setAttribute("table_name", tableDefDao
					.getSubjectCommonTblDefInfo(table_id).getTableName());
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableDefDao.getSubjectCommonTblDefDrillInfo(table_id));

		} else if ("edtTblDefDrill".equals(opt_type)) {// 导航到编辑ui_subject_common_dimhierarchy页面
			UiSubjectCommDimhierarchy tableDefDrill = (UiSubjectCommDimhierarchy) tableDefDao
					.getSubjectCommonTblDefDrillInfo(table_id).get(0);
			strReturn = "SubjectCommonTblDefDrillEdt";
			request.setAttribute("table_id", table_id);
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableDefDrill);

		} else if ("doDelCommTblDefDrill".equals(opt_type)) {// 删除ui_subject_common_dimhierarchy
			request.setAttribute("table_id", table_id);
			tableDefDao.deleteTableDefDrill(table_id);
			String url = "SubjectCommonTblDef.rptdo?opt_type=listTblCondition&table_id="
					+ table_id;
			session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"<font size=2><b>删除信息成功！</b></font>", url);
		} else if ("doEdtCommTblDefDrill".equals(opt_type)) {// 处理修改ui_subject_common_dimhierarchy
			doEdtCommTblDefDrill(request, response);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"修改信息成功!");

		} else if ("addTblDefDrill".equals(opt_type)) {// 导航到增加ui_subject_common_dimhierarchy页面
			request.setAttribute("table_id", table_id);
			strReturn = "SubjectCommonTblDefDrillAdd";
		} else if ("doAddCommTblDefDrill".equals(opt_type)) {// 处理增加ui_subject_common_dimhierarchy页面
			doEdtCommTblDefDrill(request, response);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"增加信息成功!");

		} else if ("listTblColDef".equals(opt_type)) {// 导航到ui_subject_common_table_coldef列表页面
			strReturn = "listSubjectCommonTblColDefInfo";
			request.setAttribute("table_id", table_id);
			request.setAttribute("table_name", tableDefDao
					.getSubjectCommonTblDefInfo(table_id).getTableName());
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableDefDao.getSubjectCommonTblCoInfo(table_id));
		} else if ("addTblColDef".equals(opt_type)) {// 导航到增加ui_subject_common_table_coldef页面
			request.setAttribute("table_id", table_id);
			strReturn = "SubjectCommonTblColDefAdd";
		} else if ("doDelCommTblColDef".equals(opt_type)) {// 删除ui_subject_common_table_coldef
			request.setAttribute("table_id", table_id);
			String rowId = request.getParameter("rowid");
			tableDefDao.deleteTableColDef(rowId);
			// session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			String url = "SubjectCommonTblDef.rptdo?opt_type=listTblColDef&table_id="
					+ table_id;

			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"<font size=2><b>删除信息成功！</b></font>", url);

		} else if ("edtTblColDef".equals(opt_type)) {// 导航到编辑ui_subject_common_table_coldef页面
			request.setAttribute("table_id", table_id);
			String rowId = request.getParameter("rowid");
			UiSubjectCommonColDef tableColDef = tableDefDao
					.getSubjectCommonTblColInfo(rowId);
			strReturn = "SubjectCommonTblColDefEdt";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableColDef);
		} else if ("doAddCommTblColDef".equals(opt_type)) {// 处理增加ui_subject_common_table_coldef页面
			doAddCommTblColDef(request, response);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"增加信息成功!");

		} else if ("doEdtCommTblColDef".equals(opt_type)) {// 处理修改ui_subject_common_table_coldef页面
			doAddCommTblColDef(request, response);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"修改信息成功!");

		} else if ("listTblCondition".equals(opt_type)) {// 查询UI_PUB_INFO_CONDITION中数据
			strReturn = "listCommonTblCondition";
			request.setAttribute("table_id", table_id);
			request.setAttribute("table_name", tableDefDao
					.getSubjectCommonTblDefInfo(table_id).getTableName());
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableDefDao.getSubjectCommonTblConditionInfo(table_id));
		} else if ("addTblCondition".equals(opt_type)) {// 导航到增加UI_PUB_INFO_CONDITION页面
			request.setAttribute("table_id", table_id);
			strReturn = "SubjectCommonTblConditionAdd";
		} else if ("doDelCommTblCondition".equals(opt_type)) {// 删除UI_PUB_INFO_CONDITION
			request.setAttribute("table_id", table_id);
			String rowId = request.getParameter("rowid");
			tableDefDao.deleteTableCondition(rowId);
			// session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			String url = "SubjectCommonTblDef.rptdo?opt_type=listTblCondition&table_id="
					+ table_id;

			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"<font size=2><b>删除信息成功！</b></font>", url);

		} else if ("edtTblCondition".equals(opt_type)) {// 导航到编辑UI_PUB_INFO_CONDITION页面
			request.setAttribute("table_id", table_id);
			String rowId = request.getParameter("rowid");
			UiPubInfoCondition tableColDef = tableDefDao
					.getSubjectCommonTblCondition(rowId);
			strReturn = "SubjectCommonTblConditionEdt";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF,
					tableColDef);
		} else if ("doAddCommTblCondition".equals(opt_type)) {// 处理增加UI_PUB_INFO_CONDITION页面
			doAddCommTblCondition(request, response);
			strReturn = "listSubjectCtrlDealOk";
			request.setAttribute(SubjectCommonConst.CONST_SUBJECT_DEAL_INFO,
					"操作成功!");

		}

		this.setNextScreen(request, strReturn + ".screen");

	}

	private String doAddCommTblCondition(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		HttpSession session = request.getSession();

		String resId = StringB.NulltoBlank(request.getParameter("table_id"));
		String resType = StringB.NulltoBlank(request.getParameter("res_type"));
		String qryType = StringB.NulltoBlank(request.getParameter("qry_type"));
		String qryCode = StringB.NulltoBlank(request.getParameter("qry_code"));
		String conCode = StringB.NulltoBlank(request.getParameter("con_code"));
		String dataType = StringB
				.NulltoBlank(request.getParameter("data_type"));
		String conTag = StringB.NulltoBlank(request.getParameter("con_tag"));
		String sequence = StringB.NulltoBlank(request.getParameter("sequence"));
		String status = StringB.NulltoBlank(request.getParameter("status"));

		UiPubInfoCondition obj = new UiPubInfoCondition();
		obj.setResId(resId);
		obj.setResType(resType);
		obj.setQryType(qryType);
		obj.setQryCode(qryCode);
		obj.setConCode(conCode);
		obj.setDataType(dataType);
		obj.setConTag(conTag);
		obj.setSequence(sequence);
		if (sequence.length() == 0) {
			obj.setSequence("0");
		}
		obj.setStatus(status);
		String rowId = StringB.NulltoBlank(request.getParameter("rowid"));
		obj.setRowId(rowId);

		ISubjectTableDefDao tableDefDao = new SubjectTableDefDaoImpl();
		tableDefDao.insertCommonTblCondition(obj);

		session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
		// String url =
		// "SubjectCommonTblDef.rptdo?opt_type=listTblColDef&table_id=" +
		// tableId;
		return "";
		/*
		 * throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
		 * "<font size=2><b>保存信息成功！</b></font>");
		 */
	}

	private String doAddCommTblColDef(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		HttpSession session = request.getSession();
		String tableId = StringB.NulltoBlank(request.getParameter("table_id"));
		String colId = StringB.NulltoBlank(request.getParameter("col_id"));
		String colName = StringB.NulltoBlank(request.getParameter("col_name"));
		String colDesc = StringB.NulltoBlank(request.getParameter("col_desc"));
		String colSequence = StringB.NulltoBlank(request
				.getParameter("col_sequence"));
		String isMeasure = StringB.NulltoBlank(request
				.getParameter("is_measure"));
		String dimAswhere = StringB.NulltoBlank(request
				.getParameter("dim_aswhere"));
		String defaultDisplay = StringB.NulltoBlank(request
				.getParameter("default_display"));
		String isExpandCol = StringB.NulltoBlank(request
				.getParameter("is_expand_col"));
		String defaultDrilled = StringB.NulltoBlank(request
				.getParameter("default_drill"));
		String initLevel = StringB.NulltoBlank(request
				.getParameter("init_level"));

		String dimAscol = StringB
				.NulltoBlank(request.getParameter("dim_ascol"));
		String codeField = StringB.NulltoBlank(request
				.getParameter("code_field"));
		String descField = StringB.NulltoBlank(request
				.getParameter("desc_field"));
		String isRatio = StringB.NulltoBlank(request.getParameter("is_ratio"));
		String dataType = StringB
				.NulltoBlank(request.getParameter("data_type"));
		String digitLength = StringB.NulltoBlank(request
				.getParameter("digit_length"));
		String hasComratio = StringB.NulltoBlank(request
				.getParameter("has_comratio"));
		String hasLink = StringB.NulltoBlank(request.getParameter("has_link"));
		String linkUrl = StringB.NulltoBlank(request.getParameter("link_url"));
		String linkTarget = StringB.NulltoBlank(request
				.getParameter("link_target"));
		String hasLast = StringB.NulltoBlank(request.getParameter("has_last"));
		String lastDisplay = StringB.NulltoBlank(request
				.getParameter("last_display"));
		String riseArrowColor = StringB.NulltoBlank(request
				.getParameter("rise_color"));

		String hasLastLink = StringB.NulltoBlank(request
				.getParameter("has_last_link"));
		String lastUrl = StringB.NulltoBlank(request.getParameter("last_url"));
		String lastTarget = StringB.NulltoBlank(request
				.getParameter("last_target"));
		String hasLoop = StringB.NulltoBlank(request.getParameter("has_loop"));
		String loopDisplay = StringB.NulltoBlank(request
				.getParameter("loop_display"));
		String hasLoopLink = StringB.NulltoBlank(request
				.getParameter("has_loop_link"));
		String loopUrl = StringB.NulltoBlank(request.getParameter("loop_url"));
		String loopTarget = StringB.NulltoBlank(request
				.getParameter("loop_target"));
		String isColClickChartChange = StringB.NulltoBlank(request
				.getParameter("is_col_click_chart_chg"));

		String colRltChartId = StringB.NulltoBlank(request
				.getParameter("col_rlt_chart_id"));
		String isCellClickChartChange = StringB.NulltoBlank(request
				.getParameter("is_celll_click_chart_chg"));
		String cellRltChartId = StringB.NulltoBlank(request
				.getParameter("cell_rlt_chart_id"));
		String status = StringB.NulltoBlank(request.getParameter("status"));
		String descAstitle = StringB.NulltoBlank(request
				.getParameter("desc_astitle"));
		String lastVarDisplay = StringB.NulltoBlank(request
				.getParameter("last_var_dis"));
		String loopVarDisplay = StringB.NulltoBlank(request
				.getParameter("loop_var_dis"));
		String totalDisplayed = StringB.NulltoBlank(request
				.getParameter("total_dis"));
		String rowId = StringB.NulltoBlank(request.getParameter("rowid"));
		ISubjectTableDefDao tableDefDao = new SubjectTableDefDaoImpl();
		UiSubjectCommonColDef obj = new UiSubjectCommonColDef();

		obj.setTableId(tableId);
		obj.setColId(colId);
		obj.setColName(colName);
		obj.setColDesc(colDesc);
		obj.setColSequence(colSequence);
		obj.setIsMeasure(isMeasure);
		obj.setDimAswhere(dimAswhere);
		obj.setDefaultDisplay(defaultDisplay);
		obj.setIsExpandCol(isExpandCol);
		obj.setDefaultDrilled(defaultDrilled);

		obj.setInitLevel(initLevel);
		if (initLevel.length() == 0) {
			obj.setInitLevel("null");
		}
		obj.setDimAscol(dimAscol);
		obj.setCodeField(codeField);
		obj.setDescField(descField);
		obj.setIsRatio(isRatio);
		obj.setDataType(dataType);
		obj.setDigitLength(digitLength);
		obj.setHasComratio(hasComratio);
		obj.setHasLink(hasLink);
		obj.setLinkUrl(linkUrl);

		obj.setLinkTarget(linkTarget);
		obj.setHasLast(hasLast);
		obj.setLastDisplay(lastDisplay);
		obj.setRiseArrowColor(riseArrowColor);
		obj.setHasLastLink(hasLastLink);
		obj.setLastUrl(lastUrl);
		obj.setLastTarget(lastTarget);
		obj.setHasLoop(hasLoop);
		obj.setLoopDisplay(loopDisplay);
		obj.setHasLoopLink(hasLoopLink);

		obj.setLoopUrl(loopUrl);
		obj.setLoopTarget(loopTarget);
		obj.setIsColClickChartChange(isColClickChartChange);
		obj.setColRltChartId(colRltChartId);
		obj.setIsCellClickChartChange(isCellClickChartChange);
		obj.setCellRltChartId(cellRltChartId);
		obj.setStatus(status);
		obj.setDescAstitle(descAstitle);
		obj.setLastVarDisplay(lastVarDisplay);
		obj.setLoopVarDisplay(loopVarDisplay);
		obj.setTotalDisplayed(totalDisplayed);
		obj.setRowId(rowId);
		tableDefDao.insertCommonTblCoInfo(obj);
		return "";
		/*
		 * session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
		 * 
		 * throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
		 * "<font size=2><b>保存信息成功！</b></font>");
		 */
	}

	private String doEdtCommTblDefDrill(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		HttpSession session = request.getSession();
		String table_id = StringB.NulltoBlank(request.getParameter("table_id"));
		String colId = StringB.NulltoBlank(request.getParameter("col_id"));
		String levId = StringB.NulltoBlank(request.getParameter("lev_id"));
		String levName = StringB.NulltoBlank(request.getParameter("lev_name"));
		String levMemo = StringB.NulltoBlank(request.getParameter("lev_memo"));
		String srcIdfld = StringB
				.NulltoBlank(request.getParameter("src_idfld"));
		String idfldType = StringB.NulltoBlank(request
				.getParameter("id_fldtype"));
		String srcNamefld = StringB.NulltoBlank(request
				.getParameter("src_namefld"));
		String descAstitle = StringB.NulltoBlank(request
				.getParameter("desc_astitle"));
		String hasLink = StringB.NulltoBlank(request.getParameter("has_link"));
		String linkUrl = StringB.NulltoBlank(request.getParameter("link_url"));
		String linkTarget = StringB.NulltoBlank(request
				.getParameter("link_target"));

		ISubjectTableDefDao tableDefDao = new SubjectTableDefDaoImpl();
		UiSubjectCommDimhierarchy drillObj = new UiSubjectCommDimhierarchy();
		drillObj.setTableId(table_id);
		drillObj.setColId(colId);
		if (colId.length() == 0) {
			drillObj.setColId("1");
		}

		drillObj.setLevId(levId);
		if (levId.length() == 0) {
			drillObj.setLevId("1");
		}

		drillObj.setLevName(levName);
		drillObj.setLevMemo(levMemo);
		drillObj.setSrcIdfld(srcIdfld);

		drillObj.setIdfldType(idfldType);
		if (idfldType.length() == 0) {
			drillObj.setIdfldType("0");
		}
		drillObj.setSrcNamefld(srcNamefld);
		drillObj.setDescAstitle(descAstitle);
		drillObj.setHasLink(hasLink);
		drillObj.setLinkUrl(linkUrl);
		drillObj.setLinkTarget(linkTarget);
		tableDefDao.insertTableDefDrill(drillObj);
		return "";
		/*
		 * session.setAttribute(WebKeys.ATTR_ANYFLAG, "1"); throw new
		 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
		 * "<font size=2><b>保存信息成功！</b></font>");
		 */
	}

	private String doEdtCommTblDef(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		HttpSession session = request.getSession();
		String table_id = StringB.NulltoBlank(request.getParameter("table_id"));
		String table_name = StringB.NulltoBlank(request
				.getParameter("table_name"));
		String tableDesc = StringB.NulltoBlank(request
				.getParameter("table_desc"));
		String dataTable = StringB.NulltoBlank(request
				.getParameter("data_table"));
		String dataWhere = StringB.NulltoBlank(request
				.getParameter("data_where"));
		String hasExpand = StringB.NulltoBlank(request
				.getParameter("has_expand"));
		String hasPaging = StringB.NulltoBlank(request
				.getParameter("has_paging"));
		String timeField = StringB.NulltoBlank(request
				.getParameter("time_field"));
		String fieldType = StringB.NulltoBlank(request
				.getParameter("field_type"));
		String timeLevel = StringB.NulltoBlank(request
				.getParameter("time_level"));
		String rowClickedChartChange = StringB.NulltoBlank(request
				.getParameter("row_click_chart_chg"));
		String rltChartId = StringB.NulltoBlank(request
				.getParameter("rlt_chart_id"));
		String hasHead = StringB.NulltoBlank(request.getParameter("has_head"));
		String sumDisplay = StringB.NulltoBlank(request
				.getParameter("sum_desplay"));
		String dimAscol = StringB
				.NulltoBlank(request.getParameter("dim_ascol"));
		String customMsu = StringB.NulltoBlank(request
				.getParameter("custom_msu"));
		String throwOld = StringB
				.NulltoBlank(request.getParameter("throw_old"));
		String hasExpandall = StringB.NulltoBlank(request
				.getParameter("has_expandall"));
		String chartChangeJs = StringB.NulltoBlank(request
				.getParameter("chart_change_js"));
		String tableType = StringB.NulltoBlank(request
				.getParameter("table_type"));

		ISubjectTableDefDao tableDefDao = new SubjectTableDefDaoImpl();
		UiSubjectCommonTableDef tableDef = new UiSubjectCommonTableDef();
		tableDef.setTableId(table_id);
		tableDef.setTableName(table_name);
		tableDef.setTableDesc(tableDesc);
		tableDef.setDataTable(dataTable);
		tableDef.setDataWhere(dataWhere);
		tableDef.setHasExpand(hasExpand);
		tableDef.setHasPaging(hasPaging);
		tableDef.setTimeField(timeField);
		tableDef.setFieldType(fieldType);
		tableDef.setTimeLevel(timeLevel);

		tableDef.setRowClickedChartChange(rowClickedChartChange);
		tableDef.setRltChartId(rltChartId);
		tableDef.setHasHead(hasHead);
		tableDef.setSumDisplay(sumDisplay);
		tableDef.setDimAscol(dimAscol);

		tableDef.setCustomMsu(customMsu);
		tableDef.setThrowOld(throwOld);
		tableDef.setHasExpandall(hasExpandall);
		tableDef.setChartChangeJs(chartChangeJs);
		tableDef.setTableType(tableType);
		tableDefDao.insertTableDef(tableDef);
		return "";
		/*
		 * session.setAttribute(WebKeys.ATTR_ANYFLAG, "1"); throw new
		 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
		 * "<font size=2><b>保存信息成功！</b></font>");
		 */
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @desc 保存主报表
	 * @return
	 * @throws HTMLActionException
	 */
	private String saveCommTblDef(HttpServletRequest request,
			HttpServletResponse response) throws HTMLActionException {
		HttpSession session = request.getSession();
		String table_id = StringB.NulltoBlank(request.getParameter("table_id"));
		String table_name = StringB.NulltoBlank(request
				.getParameter("table_name"));
		String tableDesc = StringB.NulltoBlank(request
				.getParameter("table_desc"));
		String dataTable = StringB.NulltoBlank(request
				.getParameter("data_table"));
		String dataWhere = StringB.NulltoBlank(request
				.getParameter("data_where"));
		String hasExpand = StringB.NulltoBlank(request
				.getParameter("has_expand"));
		String hasPaging = StringB.NulltoBlank(request
				.getParameter("has_paging"));
		String timeField = StringB.NulltoBlank(request
				.getParameter("time_field"));
		String fieldType = StringB.NulltoBlank(request
				.getParameter("field_type"));
		String timeLevel = StringB.NulltoBlank(request
				.getParameter("time_level"));
		String rowClickedChartChange = StringB.NulltoBlank(request
				.getParameter("row_click_chart_chg"));
		String rltChartId = StringB.NulltoBlank(request
				.getParameter("rlt_chart_id"));
		String hasHead = StringB.NulltoBlank(request.getParameter("has_head"));
		String sumDisplay = StringB.NulltoBlank(request
				.getParameter("sum_desplay"));
		String dimAscol = StringB
				.NulltoBlank(request.getParameter("dim_ascol"));
		String customMsu = StringB.NulltoBlank(request
				.getParameter("custom_msu"));
		String throwOld = StringB
				.NulltoBlank(request.getParameter("throw_old"));
		String hasExpandall = StringB.NulltoBlank(request
				.getParameter("has_expandall"));
		String chartChangeJs = StringB.NulltoBlank(request
				.getParameter("chart_change_js"));
		String tableType = StringB.NulltoBlank(request
				.getParameter("table_type"));

		ISubjectTableDefDao tableDefDao = new SubjectTableDefDaoImpl();
		UiSubjectCommonTableDef tableDef = tableDefDao
				.getSubjectCommonTblDefInfo(table_id);
		if (tableDef != null) {
			session.setAttribute(WebKeys.ATTR_ANYFLAG, "1");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE,
					"<font size=2><b>该报表ID已经存在，不能添加！</b></font>");
		} else {
			tableDef = new UiSubjectCommonTableDef();
			tableDef.setTableId(table_id);
			tableDef.setTableName(table_name);
			tableDef.setTableDesc(tableDesc);
			tableDef.setDataTable(dataTable);
			tableDef.setDataWhere(dataWhere);
			tableDef.setHasExpand(hasExpand);
			tableDef.setHasPaging(hasPaging);
			tableDef.setTimeField(timeField);
			tableDef.setFieldType(fieldType);
			tableDef.setTimeLevel(timeLevel);

			tableDef.setRowClickedChartChange(rowClickedChartChange);
			tableDef.setRltChartId(rltChartId);
			tableDef.setHasHead(hasHead);
			tableDef.setSumDisplay(sumDisplay);
			tableDef.setDimAscol(dimAscol);

			tableDef.setCustomMsu(customMsu);
			tableDef.setThrowOld(throwOld);
			tableDef.setHasExpandall(hasExpandall);
			tableDef.setChartChangeJs(chartChangeJs);
			tableDef.setTableType(tableType);
			tableDefDao.insertTableDef(tableDef);
			return "";
			/*
			 * session.setAttribute(WebKeys.ATTR_ANYFLAG, "1"); throw new
			 * HTMLActionException(session, HTMLActionException.WARN_PAGE,
			 * "<font size=2><b>保存信息成功！</b></font>");
			 */
		}
	}

	private String transferHeaderContent(String report_head) {
		String trStyle = "";
		String tdStyle = "tab-title";
		// 处理表头并验证
		return ReportHeadUtil.processHead(report_head, trStyle, tdStyle);

	}
}
