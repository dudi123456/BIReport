package com.ailk.bi.adhoc.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocDimMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocMsuMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocSumInfoTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
import com.ailk.bi.adhoc.struct.AdhocViewStruct;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocHelper;
import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.adhoc.util.AdhocViewHandler;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
//import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

/**
 * @author r00211480 即席查询查询结果处理
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class AdhocQueryHTMLAction extends HTMLActionSupport {

	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		HttpSession session = request.getSession();
		// 获取用户信息
		String oper_no = CommonFacade.getLoginId(session);

		// 操作类型
		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "view";
		}
		session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);

		// 当前分析即席查询功能点ID
		String adhoc_root = CommTool.getParameterGB(request, AdhocConstant.ADHOC_ROOT);
		// 即席查询服务
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		// 根据ID获取即席查询基本信息
		UiAdhocInfoDefTable hocInfo = facade.getAdhocInfo(adhoc_root);

		// 即席查询视图结构
		AdhocViewQryStruct qryStruct = new AdhocViewQryStruct();
		// 取得查询结构数据
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			// throw new HTMLActionException(request.getSession(),
			// HTMLActionException.WARN_PAGE, "查询条件受理信息有误！");
			request.setAttribute("CONST_SUBJECT_DEAL_INFO", "查询条件受理信息有误！");
			this.setNextScreen(request, "listSubjectCtrlDealOk.screen");
		}
		// 保持即席查询ID
		qryStruct.adhoc_id = adhoc_root;
		// 保存查询结构
		session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
		session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, qryStruct);

		//
		UiAdhocDimMetaTable parseDim[] = null;
		UiAdhocDimMetaTable parseDimExpand[] = null;
		UiAdhocMsuMetaTable parseMsu[] = null;
		UiAdhocConditionMetaTable[] parseCondition = null;

		if ("listView".equals(operType)) {
			// 界面选中条件编码（不含默认条件）
			String[] conParam = request
					.getParameterValues(AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表
			//
			String[] hconParam = request.getParameterValues("H_"
					+ AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表

			// 提取全部选中条件对象
			parseCondition = AdhocViewHandler.getConditionSelectedArray(conParam, facade,
					adhoc_root, hconParam);
			session.setAttribute(AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY, parseCondition);

			// 提取全部选中纬度对象
			parseDim = (UiAdhocDimMetaTable[]) session
					.getAttribute(AdhocConstant.ADHOC_VIEW_DIM_SESSION_KEY);

			// 扩展维度信息
			parseDimExpand = (UiAdhocDimMetaTable[]) session
					.getAttribute(AdhocConstant.ADHOC_VIEW_DIM_EXPAND_SESSION_KEY);
			// 获取扩展的列信息
			String expand_dimid = request.getParameter("dimid");
			// 获取扩展的方向
			String expand_order = request.getParameter("expand_order");
			if (parseDimExpand != null && expand_dimid != null && !"".equals(expand_dimid)) {
				ArrayList listtmp = new ArrayList();
				for (int i = 0; parseDim != null && i < parseDim.length; i++) {
					if ("up".equals(expand_order) && expand_dimid.equals(parseDim[i].getDim_id())) {
						break;
					}
					listtmp.add(parseDim[i]);
				}
				if ("down".equals(expand_order)) {
					for (int i = 0; i < parseDimExpand.length; i++) {
						if (expand_dimid.equals(parseDimExpand[i].getDim_id())) {
							if (i + 1 <= parseDimExpand.length - 1) {
								listtmp.add(parseDimExpand[i + 1]);
							}
						}
					}
				}
				parseDim = (UiAdhocDimMetaTable[]) listtmp.toArray(new UiAdhocDimMetaTable[listtmp
						.size()]);
			}
			session.setAttribute(AdhocConstant.ADHOC_VIEW_DIM_SESSION_KEY, parseDim);

			// 提取全部选中度量对象
			parseMsu = (UiAdhocMsuMetaTable[]) session
					.getAttribute(AdhocConstant.ADHOC_VIEW_MSU_SESSION_KEY);
			if (parseMsu == null || parseMsu.length <= 0) {
				// throw new HTMLActionException(request.getSession(),
				// HTMLActionException.WARN_PAGE, "请选择统计度量！");
				request.setAttribute("CONST_SUBJECT_DEAL_INFO", "请选择统计度量！");
				this.setNextScreen(request, "listSubjectCtrlDealOk.screen");
			}

		} else if ("view".equals(operType)) {
			// 界面选中条件编码（不含默认条件）
			String[] conParam = request
					.getParameterValues(AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表
			// 界面选中纬度编码（不含默认纬度）
			String[] dimParam = request.getParameterValues(AdhocConstant.ADHOC_DIM_CHECK_NAME); // 取得纬度的选择列表
			// 界面选中度量编码（不含默认度量）
			String[] msuParam = request.getParameterValues(AdhocConstant.ADHOC_MSU_CHECK_NAME); // 取得度量的选择列表
			// 
			String[] hconParam = request.getParameterValues("H_"
					+ AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表
			// 界面选中纬度编码（不含默认纬度）
			String[] hdimParam = request.getParameterValues("H_"
					+ AdhocConstant.ADHOC_DIM_CHECK_NAME); // 取得纬度的选择列表
			// 界面选中度量编码（不含默认度量）
			String[] hmsuParam = request.getParameterValues("H_"
					+ AdhocConstant.ADHOC_MSU_CHECK_NAME); // 取得度量的选择列表

			// 提取全部选中条件对象
			parseCondition = AdhocViewHandler.getConditionSelectedArray(conParam, facade,
					adhoc_root, hconParam);
			session.setAttribute(AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY, parseCondition);

			// 提取全部选中纬度对象
			parseDim = AdhocViewHandler
					.getDimSelectedArray(dimParam, facade, adhoc_root, hdimParam);
			session.setAttribute(AdhocConstant.ADHOC_VIEW_DIM_SESSION_KEY, parseDim);

			// 提取最后一个选定维度
			UiAdhocDimMetaTable lastDimObj = null;
			String whereStr = "";
			if (parseDim != null && parseDim.length > 0) {
				lastDimObj = parseDim[parseDim.length - 1];
				if ("Y".equals(lastDimObj.getDim_expand_flag())) {
					if (lastDimObj.getDim_expand_group() != null
							&& !"".equals(lastDimObj.getDim_expand_group())) {
						whereStr = " and DIM_EXPAND_GROUP=" + lastDimObj.getDim_expand_group();
					}
					if (lastDimObj.getSequence() != null && !"".equals(lastDimObj.getSequence())) {
						whereStr += " and sequence>=" + lastDimObj.getSequence();
					}
				}
			}
			parseDimExpand = AdhocViewHandler.getDimSelectedArray(facade, whereStr);
			session.setAttribute(AdhocConstant.ADHOC_VIEW_DIM_EXPAND_SESSION_KEY, parseDimExpand);

			// 提取全部选中度量对象
			parseMsu = AdhocViewHandler
					.getMsuSelectedArray(msuParam, facade, adhoc_root, hmsuParam);
			if (parseMsu == null || parseMsu.length <= 0) {
				// throw new HTMLActionException(request.getSession(),
				// HTMLActionException.WARN_PAGE, "请选择统计指标！");
				request.setAttribute("CONST_SUBJECT_DEAL_INFO", "请选择统计指标！");
				this.setNextScreen(request, "listSubjectCtrlDealOk.screen");
			}
			session.setAttribute(AdhocConstant.ADHOC_VIEW_MSU_SESSION_KEY, parseMsu);
		}
		// 提取有值条件对象数组
		// UiAdhocConditionMetaTable[] valueCondition = AdhocViewHandler
		// .getConditionValueArray(session, adhoc_root, qryStruct);

		// 初始化辅助结构视图

		AdhocViewStruct adhocView = AdhocViewHandler.init(request, qryStruct, parseCondition,
				parseDim, parseMsu, hocInfo);
		session.removeAttribute(AdhocConstant.ADHOC_VIEW_STRUCT);
		session.setAttribute(AdhocConstant.ADHOC_VIEW_STRUCT, adhocView);

		// 查询汇总信息
		// UiAdhocSumInfoTable[] sumInfo =
		// AdhocHelper.getAdhocSumValueStruct(facade.getAdhocSumInfo(adhoc_root),adhocView.sqlCon,hocInfo.getData_table());
		// session.setAttribute(AdhocConstant.ADHOC_VIEW_SUM, sumInfo);

		// 查询汇总信息对象
		UiAdhocSumInfoTable[] sumInfo = facade.getAdhocSumInfo(adhoc_root);

		// 汇总查询SQL
		String sumSql = AdhocHelper.getAdhocSumValueQrySQL(sumInfo);

		// 查询列表
		String sql = "";
		if ("".equals(adhocView.sqlStd)) {
			// throw new HTMLActionException(request.getSession(),
			// HTMLActionException.WARN_PAGE, "请选择统计纬度和指标！");
			request.setAttribute("CONST_SUBJECT_DEAL_INFO", "请选择统计纬度和指标！");
			this.setNextScreen(request, "listSubjectCtrlDealOk.screen");
		}
		if (!"".equals(adhocView.sqlStd)) {
			sql += "SELECT " + adhocView.sqlStd + " ";
		}
		if (!"".equals(sumSql)) {
			sql += "," + sumSql + " ";
		}

		int ctlSplitOk = 0; // 是否从分表查询

		if (hocInfo.getIsSplit() == 1) {
			// 需要分表
			String prefixTbl = hocInfo.getPrefixTbl();
			int accnt = hocInfo.getAccountNum();
			String selValue = StringB.NulltoBlank(request.getParameter(hocInfo.getConId()));// 选择的值
			// String selValue = "201011";//选择的值
			int iSelValue = 0;

			if (selValue.length() > 0) {
				iSelValue = StringB.toInt(selValue, 0);

				// 判断查询类型，日或月
				int curOptime = 0; // 当前账期
				switch (hocInfo.getAdhocType()) {
				case 0:
					// 日查询
					curOptime = StringB.toInt(DateUtil.getDiffDay(-1, DateUtil.getNowDate()), 0);
					break;
				case 1:
					// 月查询
					curOptime = StringB.toInt(DateUtil.getDiffMonth(-1, DateUtil.getNowDate()), 0);
					break;
				}

				int interval = accnt - (curOptime - iSelValue);// 间隔数

				if (interval >= 0) {
					ctlSplitOk = 1;
					sql += " FROM " + prefixTbl + selValue + " ";
				} else {
					sql += " FROM " + hocInfo.getData_table() + " ";
				}

			} else {
				sql += " FROM " + hocInfo.getData_table() + " ";
			}

		} else {
			sql += " FROM " + hocInfo.getData_table() + " ";
		}

		// 处理外关联情况[暂时不用此方法，因为返回结果不对]
		// sql += getLeftOuterJoin(adhocView.leftOuterJoinTag);

		// 处理普通关联查询
		List listLinkTable = AdhocHelper.getInnerLinkQry(adhocView.leftOuterJoinTag);
		String linkConTmp = "";

		/*
		 * for (int k = 0; k < listLinkTable.size(); k++) { AdhocViewStruct
		 * viewTmp = (AdhocViewStruct) listLinkTable.get(k); sql += "," +
		 * viewTmp.sqlLinkTable + " "; linkConTmp += " and " +
		 * viewTmp.sqlLinkTableCon; }
		 */
		for (int k = 0; k < listLinkTable.size(); k++) {
			AdhocViewStruct viewTmp = (AdhocViewStruct) listLinkTable.get(k);
			sql += viewTmp.sqlLinkTable + " ";
			linkConTmp += " and " + viewTmp.sqlLinkTableCon;
		}

		// 增加发展人部门控制(数据权限)????
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);

		String[] rtnRight = AdhocUtil.getAdhocRightControl(qryStruct.adhoc_id, ctlStruct,
				ctlSplitOk, session);
		String strDeptTable = rtnRight[0];
		String strSqlDeptCon = rtnRight[1];

		/*
		 * String[] adhocDef = AdhocUtil.getAdhocRightCtrl(qryStruct.adhoc_id);
		 * int iFlagCtl = Integer.parseInt(adhocDef[0]); if (iFlagCtl==1){ int
		 * iLevel = 0; if (ctlStruct.ctl_lvl.length() > 0) { iLevel =
		 * Integer.parseInt(ctlStruct.ctl_lvl); } if (iLevel > 1) { if
		 * (ctlStruct.ctl_sql.trim().length() > 0) { String ctlFlag =
		 * ctlStruct.ctl_flag.trim(); int iCtlFlag = 0; if (ctlFlag.length() >
		 * 0) { iCtlFlag = Integer.parseInt(ctlFlag); } switch (iCtlFlag) { case
		 * 1: // 发展人部门 int hasSecond = StringB.toInt(adhocDef[3], 0); int
		 * isSplit = StringB.toInt(adhocDef[4], 0);
		 *
		 *
		 * if (hasSecond==1 && isSplit==1 && ctlSplitOk ==1){ int ctl_lvl =
		 * StringB.toInt(ctlStruct.ctl_lvl, 1); switch(ctl_lvl){ case 1:
		 *
		 * break; case 2: //sql += ",(" + ctlStruct.ctl_sql + ") tblCtrl ";
		 * strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID2 in(" +
		 * ctlStruct.ctl_city_str_add +")";
		 *
		 * break; case 3: strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID3 in(" +
		 * ctlStruct.ctl_county_str_add +")"; strSqlDeptCon +=
		 * " AND USER_DEVELOP_DEPART_ID2 in(" + ctlStruct.ctl_city_str_add +")";
		 *
		 * break; case 4:
		 *
		 * break; case 5:
		 *
		 * break; }
		 *
		 * }else{ sql += ",(" + ctlStruct.ctl_sql + ") tblCtrl "; strSqlDeptCon
		 * = " AND USER_DEVELOP_DEPART_ID=tblCtrl.dept_id "; }
		 *
		 * break; case 2: // 楼宇部门 // 发展人部门ctl_city_str String grp_id =
		 * DAOFactory.getCommonFac().getLoginUser(session).group_id; if
		 * (grp_id.equals("018")){ //集团客户部：jtkh_ly 用户分群(客户类型)=政企 and 政企国内国际标志
		 * <>国际 String fieldName = "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'"; String[][]
		 * tmp = AdhocUtil.getTableFieldType("NEW_DW",adhocDef[2] , fieldName);
		 *
		 * if (tmp!=null && tmp.length>0){ for (int m=0;m<tmp.length;m++){
		 * String dataType = tmp[m][0].substring(0,3); String fldName =
		 * tmp[m][1]; if (fldName.equalsIgnoreCase("ENT_GNGJ_FLAG")){ if
		 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
		 * " AND ENT_GNGJ_FLAG!='2'"; }else{ strSqlDeptCon +=
		 * " AND ENT_GNGJ_FLAG!=2"; } }else
		 * if(fldName.equalsIgnoreCase("BI_U_GROUP_ID")){ if
		 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
		 * " AND BI_U_GROUP_ID='1'"; }else{ strSqlDeptCon +=
		 * " AND BI_U_GROUP_ID=1"; } }
		 *
		 * }
		 *
		 * }
		 *
		 * }else if(grp_id.equals("019")){ //如果是国际业务部 权限：用户分群(客户类型)=政企 and
		 * 政企国内国际标志 =国际 String fieldName = "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'";
		 * String[][] tmp = AdhocUtil.getTableFieldType("NEW_DW",adhocDef[2] ,
		 * fieldName); if (tmp!=null && tmp.length>0){ for (int
		 * m=0;m<tmp.length;m++){ String dataType = tmp[m][0].substring(0,3);
		 * String fldName = tmp[m][1]; if
		 * (fldName.equalsIgnoreCase("ENT_GNGJ_FLAG")){ if
		 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
		 * " AND ENT_GNGJ_FLAG='2'"; }else{ strSqlDeptCon +=
		 * " AND ENT_GNGJ_FLAG=2"; } }else
		 * if(fldName.equalsIgnoreCase("BI_U_GROUP_ID")){ if
		 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
		 * " AND BI_U_GROUP_ID='1'"; }else{ strSqlDeptCon +=
		 * " AND BI_U_GROUP_ID=1"; } }
		 *
		 * }
		 *
		 * } }else{ String limitFld = StringB.NulltoBlank(adhocDef[1]); if
		 * (limitFld.length()>0){ if
		 * (limitFld.equalsIgnoreCase("BUILDING_BURA_ID")){ if
		 * (ctlStruct.CTL_BUILDING_BURA_ID.length()>0){ strSqlDeptCon =
		 * " AND BUILDING_BURA_ID in(" + ctlStruct.CTL_BUILDING_BURA_ID + ") ";
		 * }
		 *
		 * }else if (limitFld.equalsIgnoreCase("BUILDING_ID")){ if
		 * (ctlStruct.CTL_BUILDING_BURA_ID.length()>0){ sql +=
		 * ",(select distinct building_id BJ_CTLBUILDID from new_dim.d_building where BUILDING_BURA_ID in ("
		 * + ctlStruct.CTL_BUILDING_BURA_ID + ")) tblCtrl "; strSqlDeptCon =
		 * " AND BUILDING_ID=tblCtrl.BJ_CTLBUILDID "; }
		 *
		 * } }
		 *
		 *
		 * }
		 *
		 *
		 * break; }
		 *
		 * } }
		 *
		 * }
		 */

		sql += strDeptTable;
		logcommon.debug("adhocView.sqlCon==" + adhocView.sqlCon);

		if (!"".equals(adhocView.sqlCon)) {
			sql += adhocView.sqlCon + " ";
		}
		// USER_DEVELOP_DEPART_ID
		// BUILDING_MGR_DEPT_ID

		// 处理条件[普通关联查询]：
		sql += linkConTmp + " " + strSqlDeptCon;

		if (!"".equals(adhocView.sqlGrp)) {
			sql += " GROUP BY ROLLUP (" + adhocView.sqlGrp + ") ";
		}
		if (!"".equals(adhocView.sqlHaving) && !"() OR ()".equals(adhocView.sqlHaving)) {
			sql += " HAVING (" + adhocView.sqlHaving + ") ";
		}

		if (!"".equals(adhocView.sqlOdr)) {
			sql += " ORDER BY " + adhocView.sqlOdr + " ";
		}
		// 查询
		System.out.println("newSQL:" + sql);
		long s_a = System.currentTimeMillis();
		// 记录查询日志
		int iSqlLog = AdhocUtil.insertSqlLog(sql, adhoc_root, oper_no, -1, 1);
		if (iSqlLog == 0) {
			logcommon.debug("iSqlLog is false!");
		}
		String[][] resultViewList = AdhocUtil.queryArrayFacade(sql);
		// String[][] resultViewList = null;
		long s_b = System.currentTimeMillis();
		logcommon.debug("time cost======================" + (s_b - s_a));

		if (resultViewList == null || resultViewList.length <= 0) {
			// throw new HTMLActionException(request.getSession(),
			// HTMLActionException.WARN_PAGE, "当前条件下没有查询数据！");
			request.setAttribute("CONST_SUBJECT_DEAL_INFO", "当前条件下没有查询数据！");
			this.setNextScreen(request, "listSubjectCtrlDealOk.screen");
		}
		// 汇总信息过滤赋值
		/*
		 * int icol = resultViewList.length; if (resultViewList.length == 1) {
		 * icol = 1; } String[][] viewList = new
		 * String[icol][resultViewList[0].length]; for (int i = 0, a = 0;
		 * resultViewList != null && i < resultViewList.length; i++) { if ((i ==
		 * resultViewList.length - 1 && (resultViewList[i][0] == null || ""
		 * .equals(resultViewList[i][0]))) || resultViewList.length == 1) { for
		 * (int j = 0; j < sumInfo.length; j++) {
		 * sumInfo[j].setValue(FormatUtil.formatStr( resultViewList[i][2 *
		 * parseDim.length + parseMsu.length + j], Integer
		 * .parseInt(sumInfo[j].getDigit()), true)); } viewList[a++] =
		 * resultViewList[i]; } else { viewList[a++] = resultViewList[i]; } }
		 *
		 * session.setAttribute(AdhocConstant.ADHOC_VIEW_SUM, sumInfo);
		 *
		 * // 纬度值与描述匹配装载 AdhocViewHandler.fillDimDescByCodeParam(request,
		 * viewList, parseDim, adhoc_root); if (viewList != null &&
		 * viewList.length > 1 && (!"".equals(adhocView.sqlHaving) &&
		 * !"() OR ()" .equals(adhocView.sqlHaving))) { for (int j = 0; j <
		 * viewList[viewList.length - 1].length; j++) { if (j < parseDim.length)
		 * { if (j == 0) resultViewList[viewList.length - 1][2 * j + 1] = "合计";
		 * else resultViewList[viewList.length - 1][2 * j + 1] = "&nbsp;"; } } }
		 *
		 * session.setAttribute(AdhocConstant.ADHOC_VIEW_SUM, sumInfo);
		 *
		 * // 纬度值与描述匹配装载 AdhocViewHandler.fillDimDescByCodeParam(request,
		 * viewList, parseDim, adhoc_root);
		 *
		 *
		 *
		 * session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST, viewList);
		 *
		 * // 处理报表索引 int comArr[] =
		 * AdhocUtil.getCombineArrIndex(parseDim.length, "1"); // 计算合并行数组
		 * ArrayList tmpList = AdhocUtil.getCombinRowArr(viewList, comArr, 0,
		 * 1); // 合并处理 String[][] viewRpt =
		 * AdhocUtil.getCombineHelperList(viewList, comArr, tmpList);
		 */

		// zxj changed
		/*
		 * for (int i = 0, a = 0; resultViewList != null && i <
		 * resultViewList.length; i++) { if ((i == resultViewList.length - 1 &&
		 * (resultViewList[i][0] == null || "" .equals(resultViewList[i][0])))
		 * || resultViewList.length == 1) { for (int j = 0; j < sumInfo.length;
		 * j++) { sumInfo[j].setValue(FormatUtil.formatStr(
		 * resultViewList[i][parseDim.length + parseMsu.length + j], Integer
		 * .parseInt(sumInfo[j].getDigit()), true)); }
		 *
		 * } else {
		 *
		 * } }
		 */

		if (parseDim.length > 0) {
			// 有维度时，最后一行是合计

			if (resultViewList != null && resultViewList.length > 0) {
				resultViewList[resultViewList.length - 1][0] = "合计";
			}
		}

		request.setAttribute("FirstRun", Boolean.TRUE);

		session.setAttribute(AdhocConstant.ADHOC_VIEW_SUM, sumInfo);

		// 纬度值与描述匹配装载
		AdhocViewHandler.fillDimDescByCodeParam(request, resultViewList, parseDim, adhoc_root);

		session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST, resultViewList);

		// 处理报表索引
		int comArr[] = AdhocUtil.getCombineArrIndex(parseDim.length, "1");
		// 计算合并行数组
		ArrayList tmpList = AdhocUtil.getCombinRowArr(resultViewList, comArr, 0, 1);
		// 合并处理
		String[][] viewRpt = AdhocUtil.getCombineHelperList(resultViewList, comArr, tmpList);

		session.setAttribute(AdhocConstant.ADHOC_VIEW_REPORT, viewRpt);

		session.setAttribute(AdhocConstant.ADHOC_VIEW_REPORT_HELPER_LIST, tmpList);

		session.setAttribute(AdhocConstant.ADHOC_VIEW_REPORT_HELPER_ARRAY, comArr);
		//

		this.setNextScreen(request, "AdhocView.screen");

	}

	/**
	 *
	 * @param leftOuterTag
	 * @return
	 * @Desc:获取leftOuterJoin串
	 */
	private String getLeftOuterJoin(String leftOuterTag) {
		String tableAlias = "adhocOuterTbl";
		String leftTable = " left outer join ui_adhoc_condition_updetail " + tableAlias;

		// leftOuterTag格式要求：条件映射的字段1,条件值1|条件映射的字段2,条件值2,
		if (leftOuterTag.length() == 0) {
			return "";
		}
		String splitTable[] = leftOuterTag.split("\\|");
		String strRtn = "";
		for (int i = 0; i < splitTable.length; i++) {
			String tmp = splitTable[i];
			String tmpSplit[] = tmp.split("\\,");
			strRtn += leftTable + i + " on " + tmpSplit[0] + "=" + tableAlias + i
					+ ".OBJ_VALUE and " + tableAlias + i + ".id=" + tmpSplit[1] + " ";

		}
		return strRtn;
	}

}
