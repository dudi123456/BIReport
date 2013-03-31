package com.ailk.bi.adhoc.action;

//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.adhoc.dao.impl.AdhocDao;
import com.ailk.bi.adhoc.domain.UiAdhocConditionMetaTable;
import com.ailk.bi.adhoc.domain.UiAdhocFavoriteDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocInfoDefTable;
import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.adhoc.service.impl.AdhocFacade;
import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
import com.ailk.bi.adhoc.struct.AdhocViewStruct;
import com.ailk.bi.adhoc.util.AdhocConstant;
import com.ailk.bi.adhoc.util.AdhocHelper;
import com.ailk.bi.adhoc.util.AdhocUtil;
import com.ailk.bi.adhoc.util.AdhocViewHandler;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.CommTool;
//import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
//import com.ailk.bi.common.dbtools.DAOFactory;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocUserListHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * public void doTrans(HttpServletRequest request, HttpServletResponse
	 * response) throws HTMLActionException {
	 * 
	 * HttpSession session = request.getSession(); // 操作类型 String operType =
	 * CommTool.getParameterGB(request, "oper_type"); if (operType == null ||
	 * "".equals(operType)) { operType = "view"; }
	 * 
	 * String roleId = CommonFacade.getUserAdhocRole(session);
	 * 
	 * // 记录条数 // StringB.NulltoBlank(sourceStr)
	 * 
	 * String row_count = CommTool.getParameterGB(request, "row"); if (row_count
	 * == null) { row_count = ""; } // 当前分析即席查询功能点 String adhoc_root =
	 * CommTool.getParameterGB(request,AdhocConstant.ADHOC_ROOT); AdhocFacade
	 * facade = new AdhocFacade(new AdhocDao()); UiAdhocInfoDefTable hocInfo =
	 * facade.getAdhocInfo(adhoc_root); // 操作员 String oper_no =
	 * CommonFacade.getLoginId(session);
	 * 
	 * // 即席查询视图结构 AdhocViewQryStruct qryStruct = new AdhocViewQryStruct(); //
	 * 取得查询结构数据 try { AppWebUtil.getHtmlObject(request, "qry", qryStruct); }
	 * catch (AppException ex) { throw new
	 * HTMLActionException(request.getSession(), HTMLActionException.WARN_PAGE,
	 * "查询条件受理信息有误！"); } // if (qryStruct.gather_mon == null ||
	 * "".equals(qryStruct.gather_mon)) { qryStruct.gather_mon =
	 * DateUtil.getDiffMonth(-1, DateUtil .getNowDate()); } //
	 * qryStruct.adhoc_id = adhoc_root; // 保存查询结构
	 * session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
	 * session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, qryStruct);
	 * 
	 * if ("init".equals(operType)) { // 清单配置信息 UiAdhocUserListTable[]
	 * defineInfo = null; String favId =
	 * StringB.NulltoBlank(request.getParameter("favorite_id"));
	 * 
	 * if (favId.length()>0){ logcommon.debug("favId:::-=========" + favId);
	 * 
	 * //查询该收藏夹是否保存了清单字段 defineInfo =
	 * facade.getAdhocUserListDefine(hocInfo.getAdhoc_id(), oper_no,favId);
	 * 
	 * }else{ defineInfo = facade.getAdhocUserListDefine(hocInfo.getAdhoc_id(),
	 * oper_no);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * if (defineInfo == null || defineInfo.length <= 0) {//
	 * 没有用户自定义清单配置则采用系统默认配置 defineInfo =
	 * facade.getAdhocUserListDefineDefault(hocInfo.getAdhoc_id(),roleId); if
	 * (defineInfo == null || defineInfo.length <= 0) { throw new
	 * HTMLActionException(request.getSession(), HTMLActionException.WARN_PAGE,
	 * "当前用户没有自定义清单以及当前即席查询也没有默认自定义清单，请通知相关配置人员！"); } }
	 * 
	 * String[] checkList = request.getParameterValues("msuSelected"); List
	 * arrList = new ArrayList(); for (int i = 0; checkList != null && i <
	 * checkList.length; i++) { for (int j = 0; j < defineInfo.length; j++) { if
	 * (checkList[i].equals(defineInfo[j].getMsu_field())) {
	 * arrList.add(defineInfo[j]); } } }
	 * 
	 * 
	 * if (arrList != null && arrList.size() > 0) { defineInfo =
	 * (UiAdhocUserListTable[]) arrList.toArray(new
	 * UiAdhocUserListTable[arrList.size()]); }
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO,defineInfo)
	 * ;
	 * 
	 * // sql String sql = ""; String strField = ""; String strConstField =
	 * "BJFLD"; for (int i = 0; i < defineInfo.length; i++) { if (sql.length() >
	 * 0) { sql += " , "; } if (strField.length() > 0) { strField += " , "; }
	 * strField += strConstField + i;
	 * 
	 * if ("".equals(defineInfo[i].getMsu_type())) { sql +=
	 * defineInfo[i].getMsu_field() + " " + strConstField + i; } else if
	 * ("C".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql += "COALESCE("
	 * + defineInfo[i].getMsu_field() + ",0)" + " " + strConstField + i; } else
	 * if ("D".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql +=
	 * "TO_CHAR(" + defineInfo[i].getMsu_field() + ",'YYYYMMDD')" + " " +
	 * strConstField + i; } else if
	 * ("M".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql += "TO_CHAR(" +
	 * defineInfo[i].getMsu_field() + ",'YYYYYMM')" + " " + strConstField + i; }
	 * else { sql += defineInfo[i].getMsu_field() + " " + strConstField + i; } }
	 * 
	 * //logcommon.debug("字段:" + strField);
	 * 
	 * 
	 * 
	 * String sqlFrom = (String)session.getAttribute("query_from");
	 * 
	 * 
	 * String sqlCon = (String) session.getAttribute("query_con");
	 * 
	 * // String sqlTmp = "SELECT " + sql + " FROM (SELECT " + sql +
	 * ",rownum rn "+ sqlFrom + sqlCon + " and rownum<=?) where rn>?"; //String
	 * sqlTmp = "SELECT * FROM (SELECT distinct(" + sql + "),rownum rn "+
	 * sqlFrom + sqlCon + " and rownum<=?) where rn>?"; String sqlTmp =
	 * "SELECT distinct " + strField + " FROM (SELECT " + sql + ",rownum rn "+
	 * sqlFrom + sqlCon + " and rownum<=?) where rn>?";
	 * 
	 * sql = "SELECT distinct " + sql + sqlFrom + sqlCon;
	 * 
	 * 
	 * 
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_QRY_SQL, sqlTmp);
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_RECORD_CNT, "SELECT COUNT(*) " +
	 * sqlFrom + sqlCon);
	 * session.setAttribute(AdhocConstant.ADHOC_EXPORT_ROWCNT, row_count);
	 * 
	 * 
	 * if (!"".equals(row_count)) { sql += " AND rownum <=" + row_count; } //
	 * //logcommon.debug(sql); // String[][] list = null; String[][] list =
	 * AdhocUtil.queryArrayFacade(sql); //写日志
	 * writeQryLog(request,"I_ADHOC_LOG_01",sql);
	 * 
	 * 
	 * AdhocViewHandler.fillDescByMapCode(request, list, defineInfo);
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_USER_LIST_VALUE, list);
	 * 
	 * this.setNextScreen(request, "AdhocUserList.screen"); }else {
	 * 
	 * String favId = StringB.NulltoBlank(request.getParameter("favorite_id"));
	 * // 提取有值条件对象数组
	 * 
	 * UiAdhocConditionMetaTable[] valueCondition = AdhocViewHandler
	 * .getConditionValueArray(session, adhoc_root, qryStruct);
	 * 
	 * 
	 * 
	 * // 界面选中条件编码（不含默认条件） String[] conParam = request
	 * .getParameterValues(AdhocConstant.ADHOC_CONDITION_CHECK_NAME); //
	 * 取得条件的选择列表
	 * 
	 * 
	 * //保存清单条件========处理过程
	 * 
	 * String conStr = CommTool.getParameterGB(request, "conStr001"); if(conStr
	 * == null){ conStr = ""; }
	 * 
	 * String dimStr = CommTool.getParameterGB(request, "dimStr001"); if(dimStr
	 * == null){ dimStr = ""; }
	 * 
	 * String msuStr = CommTool.getParameterGB(request, "msuStr001"); if(msuStr
	 * == null){ msuStr = ""; } UiAdhocFavoriteDefTable favInfo = new
	 * UiAdhocFavoriteDefTable(); favInfo.setConStr(conStr);
	 * favInfo.setDimStr(dimStr); favInfo.setMsuStr(msuStr);
	 * 
	 * 
	 * String[] conArr = null;
	 * 
	 * if(conStr!=null&&!"".equals(conStr)){ conArr = conStr.split(","); } List
	 * listSql = new ArrayList(); List listConStr = new ArrayList();
	 * 
	 * if (conArr!=null){ for(int i=0;i<conArr.length;i++){ String[] value =
	 * getConditionValue(request,conArr[i]);
	 * 
	 * listSql.add(value); listConStr.add(conArr[i]); } }
	 * 
	 * 
	 * favInfo.setListValue(listSql); favInfo.setListConStr(listConStr);
	 * 
	 * session.setAttribute("UiAdhocFavoriteDefTable", favInfo);
	 * 
	 * //结束处理过程
	 * 
	 * 
	 * 
	 * 
	 * // 界面选中条件编码（含默认条件） String[] hconParam = request.getParameterValues("H_" +
	 * AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表
	 * 
	 * // 提取全部选中条件对象 UiAdhocConditionMetaTable[] parseCondition =
	 * AdhocViewHandler .getConditionSelectedArray(conParam, facade, adhoc_root,
	 * hconParam); session.setAttribute(
	 * AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY, parseCondition); // 条件
	 * String sqlCon = " WHERE 1=1 "; String leftOuterJoin = "";
	 * 
	 * if (parseCondition != null) { for (int i = 0; parseCondition != null && i
	 * < parseCondition.length; i++) { // 分不同类型条件提取拼装SQL
	 * 
	 * if
	 * (parseCondition[i].getCon_type().equals(AdhocConstant.ADHOC_CONDITION_TYPE_17
	 * )){//上传文件类型
	 * 
	 * 
	 * if (valueCondition!=null){ for (int yy=0;yy<valueCondition.length;yy++){
	 * if
	 * (parseCondition[i].getCon_id().equals(valueCondition[yy].getCon_id())){
	 * 
	 * if (StringB.NulltoBlank(valueCondition[yy].getValuea()).length()>0){
	 * String strTmp1 = valueCondition[yy].getFiled_name() + "," +
	 * valueCondition[yy].getValuea(); if (leftOuterJoin.length()==0){
	 * leftOuterJoin = strTmp1; }else{ leftOuterJoin += "|" + strTmp1; } } } } }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * sqlCon += "  " + AdhocViewHandler.getSqlWhereString( valueCondition,
	 * parseCondition[i] .getCon_id());
	 * 
	 * } } // 增加发展人部门控制
	 * 
	 * UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct)
	 * session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct); //
	 * UserCtlRegionStruct rgStruct =
	 * (UserCtlRegionStruct)session.getAttribute(WebConstKeys
	 * .ATTR_C_UserCtlStruct); logcommon.debug("UserCtlRegionStruct:" +
	 * ctlStruct.ctl_lvl + ":" + ctlStruct.ctl_sql);
	 * 
	 * 
	 * 
	 * List listLinkTable = AdhocHelper.getInnerLinkQry(leftOuterJoin); String
	 * linkConTmp = ""; String strTblFrom = ""; for (int k = 0;k
	 * <listLinkTable.size();k++){ AdhocViewStruct viewTmp =
	 * (AdhocViewStruct)listLinkTable.get(k); strTblFrom += "," +
	 * viewTmp.sqlLinkTable + " "; linkConTmp += " and " +
	 * viewTmp.sqlLinkTableCon; }
	 * 
	 * 
	 * String strDeptTable = ""; String strSqlDeptCon = "";
	 * 
	 * int iLevel = 0; if (ctlStruct.ctl_lvl.length()>0){ iLevel =
	 * Integer.parseInt(ctlStruct.ctl_lvl); } if (iLevel>1){ if
	 * (ctlStruct.ctl_sql.trim().length()>0){ String ctlFlag =
	 * ctlStruct.ctl_flag.trim(); int iCtlFlag = 0; if (ctlFlag.length()>0){
	 * iCtlFlag = Integer.parseInt(ctlFlag); } switch(iCtlFlag){ case 1: //发展人部门
	 * strDeptTable = ",(" + ctlStruct.ctl_sql + ") tblCtrl "; strSqlDeptCon=
	 * " AND USER_DEVELOP_DEPART_ID=tblCtrl.dept_id "; break; case 2: //楼宇部门
	 * //发展人部门 strDeptTable = ",(" + ctlStruct.ctl_sql + ") tblCtrl ";
	 * strSqlDeptCon = " AND BUILDING_MGR_DEPT_ID=tblCtrl.dept_id "; break; }
	 * 
	 * } } if (!"".equals(row_count)) { sqlCon += " AND rownum <=" + row_count;
	 * }
	 * 
	 * sqlCon += linkConTmp; sqlCon += strSqlDeptCon; // 清单配置信息
	 * UiAdhocUserListTable[] defineInfo = null;
	 * logcommon.debug("favId::kkkkkkkkkkkkk:-=========" + favId);
	 * 
	 * if (favId.length()>0){ logcommon.debug("favId:::-=========" + favId);
	 * 
	 * //查询该收藏夹是否保存了清单字段 defineInfo = facade.getAdhocUserListDefine(
	 * hocInfo.getAdhoc_id(), oper_no,favId);
	 * 
	 * }else{ defineInfo = facade.getAdhocUserListDefine( hocInfo.getAdhoc_id(),
	 * oper_no);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * if (defineInfo == null || defineInfo.length <= 0) {//
	 * 没有用户自定义清单配置则采用系统默认配置
	 * 
	 * 
	 * defineInfo = facade.getAdhocUserListDefineDefault(hocInfo
	 * .getAdhoc_id(),roleId);
	 * 
	 * if (defineInfo == null || defineInfo.length <= 0) { throw new
	 * HTMLActionException(request.getSession(), HTMLActionException.WARN_PAGE,
	 * "当前用户没有自定义清单以及当前即席查询也没有默认自定义清单，请通知相关配置人员！"); } }
	 * 
	 * 
	 * String[] checkList = request.getParameterValues("msuSelected"); List
	 * arrList = new ArrayList(); for (int i = 0; checkList != null && i <
	 * checkList.length; i++) { for (int j = 0; j < defineInfo.length; j++) { if
	 * (checkList[i].equals(defineInfo[j].getMsu_field())) {
	 * arrList.add(defineInfo[j]); } } } if (arrList != null && arrList.size() >
	 * 0) { defineInfo = (UiAdhocUserListTable[]) arrList .toArray(new
	 * UiAdhocUserListTable[arrList.size()]); }
	 * 
	 * 
	 * String[][] res = getUserListGroupName(hocInfo .getAdhoc_id());
	 * 
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO,
	 * defineInfo); request.setAttribute("LIST_GROUP_NAME", res);
	 * 
	 * // sql String sql = ""; for (int i = 0; i < defineInfo.length; i++) { if
	 * (sql.length() > 0) { sql += " , "; } if
	 * ("".equals(defineInfo[i].getMsu_type())) { sql +=
	 * defineInfo[i].getMsu_field(); } else if
	 * ("C".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql += "COALESCE("
	 * + defineInfo[i].getMsu_field() + ",0)";
	 * 
	 * } else if ("D".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql +=
	 * "TO_CHAR(" + defineInfo[i].getMsu_field() + ",'YYYYMMDD')"; } else if
	 * ("M".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql += "TO_CHAR(" +
	 * defineInfo[i].getMsu_field() + ",'YYYYYMM')"; } else { sql +=
	 * defineInfo[i].getMsu_field(); } }
	 * 
	 * //sql = "SELECT " + sql + " FROM " + hocInfo.getData_table() + sqlCon;
	 * sql = "SELECT " + sql + " FROM " + hocInfo.getData_table() + " " +
	 * strTblFrom + strDeptTable + sqlCon; logcommon.debug("清单:" + sql); // //
	 * String[][] list = AdhocUtil.queryArrayFacade(sql); // //
	 * AdhocViewHandler.fillDescByMapCode(request, list, defineInfo);
	 * 
	 * // session.setAttribute(AdhocConstant.ADHOC_USER_LIST_VALUE, list);
	 * session.setAttribute("query_con", sqlCon);
	 * session.setAttribute("query_from", " FROM " + hocInfo.getData_table() +
	 * " " + strTblFrom + strDeptTable);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // this.setNextScreen(request, "AdhocUserListSelect.screen"); }
	 * 
	 * }
	 * 
	 * 
	 * // public void doTrans(HttpServletRequest request, HttpServletResponse
	 * response) throws HTMLActionException {
	 * 
	 * HttpSession session = request.getSession(); // 操作类型 String operType =
	 * CommTool.getParameterGB(request, "oper_type"); if (operType == null ||
	 * "".equals(operType)) { operType = "view"; }
	 * session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
	 * 
	 * String roleId = CommonFacade.getUserAdhocRole(session);
	 * 
	 * // 记录条数 // StringB.NulltoBlank(sourceStr)
	 * 
	 * String row_count = CommTool.getParameterGB(request, "row"); if (row_count
	 * == null) { row_count = ""; } // 当前分析即席查询功能点 String adhoc_root =
	 * CommTool.getParameterGB(request, AdhocConstant.ADHOC_ROOT); AdhocFacade
	 * facade = new AdhocFacade(new AdhocDao()); UiAdhocInfoDefTable hocInfo =
	 * facade.getAdhocInfo(adhoc_root); // 操作员 String oper_no =
	 * CommonFacade.getLoginId(session);
	 * 
	 * // 即席查询视图结构 AdhocViewQryStruct qryStruct = new AdhocViewQryStruct(); //
	 * 取得查询结构数据 try { AppWebUtil.getHtmlObject(request, "qry", qryStruct); }
	 * catch (AppException ex) { throw new
	 * HTMLActionException(request.getSession(), HTMLActionException.WARN_PAGE,
	 * "查询条件受理信息有误！"); } // if (qryStruct.gather_mon == null ||
	 * "".equals(qryStruct.gather_mon)) { qryStruct.gather_mon =
	 * DateUtil.getDiffMonth(-1, DateUtil .getNowDate()); } //
	 * qryStruct.adhoc_id = adhoc_root; // 保存查询结构
	 * session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
	 * session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, qryStruct);
	 * 
	 * if ("init".equals(operType)) { // 清单配置信息 UiAdhocUserListTable[]
	 * defineInfo = null; String favId = StringB.NulltoBlank(request
	 * .getParameter("favorite_id"));
	 * 
	 * if (favId.length() > 0) { logcommon.debug("favId:::-=========" + favId);
	 * 
	 * // 查询该收藏夹是否保存了清单字段 defineInfo = facade.getAdhocUserListDefine(hocInfo
	 * .getAdhoc_id(), oper_no, favId);
	 * 
	 * } else { defineInfo = facade.getAdhocUserListDefine(hocInfo
	 * .getAdhoc_id(), oper_no);
	 * 
	 * }
	 * 
	 * if (defineInfo == null || defineInfo.length <= 0) {//
	 * 没有用户自定义清单配置则采用系统默认配置 defineInfo =
	 * facade.getAdhocUserListDefineDefault(hocInfo .getAdhoc_id(), roleId); if
	 * (defineInfo == null || defineInfo.length <= 0) { throw new
	 * HTMLActionException(request.getSession(), HTMLActionException.WARN_PAGE,
	 * "当前用户没有自定义清单以及当前即席查询也没有默认自定义清单，请通知相关配置人员！"); } }
	 * 
	 * String[] checkList = request.getParameterValues("msuSelected"); List
	 * arrList = new ArrayList(); for (int i = 0; checkList != null && i <
	 * checkList.length; i++) { for (int j = 0; j < defineInfo.length; j++) { if
	 * (checkList[i].equals(defineInfo[j].getMsu_field())) {
	 * arrList.add(defineInfo[j]); } } }
	 * 
	 * if (arrList != null && arrList.size() > 0) { defineInfo =
	 * (UiAdhocUserListTable[]) arrList .toArray(new
	 * UiAdhocUserListTable[arrList.size()]); }
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO,
	 * defineInfo);
	 * 
	 * // sql String sql = ""; String strField = ""; String strConstField =
	 * "BJFLD";
	 * 
	 * int isGroup = hocInfo.getIsGroup(); String strGroupBy = "";
	 * 
	 * for (int i = 0; i < defineInfo.length; i++) { if (sql.length() > 0) { sql
	 * += " , "; } if (strField.length() > 0) { strField += " , "; }
	 * 
	 * 
	 * strField += strConstField + i;
	 * 
	 * if ("".equals(defineInfo[i].getMsu_type())) { sql +=
	 * defineInfo[i].getMsu_field() + " " + strConstField + i; if
	 * (strGroupBy.length() > 0) { strGroupBy += " , " +
	 * defineInfo[i].getMsu_field(); } else{ strGroupBy =
	 * defineInfo[i].getMsu_field(); }
	 * 
	 * 
	 * } else if ("C".equalsIgnoreCase(defineInfo[i].getMsu_type())) { if
	 * (isGroup==1){//需要sum sql += "sum(COALESCE(" +
	 * defineInfo[i].getMsu_field() + ",0))" + " " + strConstField + i; }else{
	 * sql += "COALESCE(" + defineInfo[i].getMsu_field() + ",0)" + " " +
	 * strConstField + i; }
	 * 
	 * } else if ("D".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql +=
	 * "TO_CHAR(" + defineInfo[i].getMsu_field() + ",'YYYYMMDD')" + " " +
	 * strConstField + i;
	 * 
	 * 
	 * if (strGroupBy.length() > 0) { strGroupBy += " , " + "TO_CHAR(" +
	 * defineInfo[i].getMsu_field()+ ",'YYYYMMDD')"; } else{ strGroupBy =
	 * "TO_CHAR(" + defineInfo[i].getMsu_field()+ ",'YYYYMMDD')"; }
	 * 
	 * 
	 * 
	 * } else if ("M".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql +=
	 * "TO_CHAR(" + defineInfo[i].getMsu_field() + ",'YYYYMM')" + " " +
	 * strConstField + i;
	 * 
	 * if (strGroupBy.length() > 0) { strGroupBy += " , " + "TO_CHAR(" +
	 * defineInfo[i].getMsu_field()+ ",'YYYYMM')"; } else{ strGroupBy =
	 * "TO_CHAR(" + defineInfo[i].getMsu_field()+ ",'YYYYMM')"; }
	 * 
	 * 
	 * } else if ("SS".equalsIgnoreCase(defineInfo[i].getMsu_type())) { sql +=
	 * "TO_CHAR(" + defineInfo[i].getMsu_field() + ",'YYYY-MM-DD hh24:mi:ss')" +
	 * " " + strConstField + i;
	 * 
	 * if (strGroupBy.length() > 0) { strGroupBy += " , " + "TO_CHAR(" +
	 * defineInfo[i].getMsu_field()+ ",'YYYY-MM-DD hh24:mi:ss')"; } else{
	 * strGroupBy = "TO_CHAR(" + defineInfo[i].getMsu_field()+
	 * ",'YYYY-MM-DD hh24:mi:ss')"; }
	 * 
	 * 
	 * } else { sql += defineInfo[i].getMsu_field() + " " + strConstField + i;
	 * 
	 * if (strGroupBy.length() > 0) { strGroupBy += " , " +
	 * defineInfo[i].getMsu_field(); } else{ strGroupBy =
	 * defineInfo[i].getMsu_field(); }
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * // logcommon.debug("字段:" + strField);
	 * 
	 * String sqlFrom = (String) session.getAttribute("query_from");
	 * 
	 * String sqlCon = (String) session.getAttribute("query_con");
	 * 
	 * 
	 * //去掉distinct
	 * 
	 * String sqlTmp = "SELECT " + strField + " FROM (SELECT " + sql +
	 * ",rownum rn " + sqlFrom + sqlCon + " and rownum<=?) where rn>?";
	 * 
	 * String sqlCnt = "SELECT COUNT(*) " + sqlFrom + sqlCon;
	 * 
	 * if (isGroup==1){ //group by sqlTmp = "select " + strField +
	 * " from (select JFTOT.*,ROWNUM RN from (select " + sql + sqlFrom + sqlCon
	 * + " group by " + strGroupBy + ") JFTOT Where rownum<=? ) where RN>?";
	 * 
	 * sql = "select " + sql + sqlFrom + sqlCon + " group by " + strGroupBy;
	 * sqlCnt = "SELECT COUNT(*) " + sqlFrom + sqlCon + " group by " +
	 * strGroupBy;
	 * 
	 * sqlCnt = "SELECT COUNT(*) from (select " + sql + sqlFrom + sqlCon +
	 * " group by " + strGroupBy + ") JFTOT";
	 * 
	 * }else{ sqlTmp = "select " + strField +
	 * " from (select JFTOT.*,ROWNUM RN from (select " + sql + sqlFrom + sqlCon
	 * + ") JFTOT Where rownum<=? ) where RN>?";
	 * 
	 * sql = "SELECT " + sql + sqlFrom + sqlCon;
	 * 
	 * sqlCnt = "SELECT COUNT(*) " + sqlFrom + sqlCon; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_QRY_SQL, sqlTmp);
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_RECORD_CNT, sqlCnt);
	 * session.setAttribute(AdhocConstant.ADHOC_EXPORT_ROWCNT, row_count);
	 * 
	 * if (!"".equals(row_count)) {
	 * 
	 * }else{ row_count = "20000"; }
	 * 
	 * String strWhere[] = new String[] { row_count + "|1", "0|1" };
	 * 
	 * //String arr[][] = WebDBUtil.execQryArray(sqlTmp, strWhere, "");
	 * //String[][] list = AdhocUtil.queryArrayFacade(sql); String[][] list =
	 * AdhocUtil.queryArrayFacade(sqlTmp,strWhere);
	 * 
	 * AdhocViewHandler.fillDescByMapCode(request, list, defineInfo);
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_USER_LIST_VALUE, list);
	 * 
	 * this.setNextScreen(request, "AdhocUserList.screen"); } else {
	 * 
	 * String favId = StringB.NulltoBlank(request .getParameter("favorite_id"));
	 * // 提取有值条件对象数组
	 * 
	 * // UiAdhocConditionMetaTable[] valueCondition = AdhocViewHandler //
	 * .getConditionValueArray(session, adhoc_root, qryStruct);
	 * 
	 * // 界面选中条件编码（不含默认条件） String[] conParam = request
	 * .getParameterValues(AdhocConstant.ADHOC_CONDITION_CHECK_NAME); //
	 * 取得条件的选择列表
	 * 
	 * // 保存清单条件========处理过程
	 * 
	 * String conStr = CommTool.getParameterGB(request, "conStr001"); if (conStr
	 * == null) { conStr = ""; }
	 * 
	 * String dimStr = CommTool.getParameterGB(request, "dimStr001"); if (dimStr
	 * == null) { dimStr = ""; }
	 * 
	 * String msuStr = CommTool.getParameterGB(request, "msuStr001"); if (msuStr
	 * == null) { msuStr = ""; } UiAdhocFavoriteDefTable favInfo = new
	 * UiAdhocFavoriteDefTable(); favInfo.setConStr(conStr);
	 * favInfo.setDimStr(dimStr); favInfo.setMsuStr(msuStr);
	 * 
	 * String[] conArr = null;
	 * 
	 * if (conStr != null && !"".equals(conStr)) { conArr = conStr.split(","); }
	 * List listSql = new ArrayList(); List listConStr = new ArrayList();
	 * 
	 * if (conArr != null) { for (int i = 0; i < conArr.length; i++) { String[]
	 * value = getConditionValue(request, conArr[i]);
	 * 
	 * listSql.add(value); listConStr.add(conArr[i]); } }
	 * 
	 * favInfo.setListValue(listSql); favInfo.setListConStr(listConStr);
	 * 
	 * session.setAttribute("UiAdhocFavoriteDefTable", favInfo);
	 * 
	 * // 结束处理过程
	 * 
	 * // 界面选中条件编码（含默认条件） String[] hconParam = request.getParameterValues("H_" +
	 * AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表
	 * 
	 * // 提取全部选中条件对象 UiAdhocConditionMetaTable[] parseCondition =
	 * AdhocViewHandler .getConditionSelectedArray(conParam, facade, adhoc_root,
	 * hconParam); session.setAttribute(
	 * AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY, parseCondition); // 条件
	 * String sqlCon = " WHERE 1=1 "; String leftOuterJoin = "";
	 * 
	 * if (parseCondition != null) { for (int i = 0; parseCondition != null && i
	 * < parseCondition.length; i++) { // 分不同类型条件提取拼装SQL
	 * 
	 * if (parseCondition[i].getCon_type().equals(
	 * AdhocConstant.ADHOC_CONDITION_TYPE_17)) {// 上传文件类型
	 * 
	 * String tmpValue =
	 * StringB.NulltoBlank(request.getParameter(parseCondition[
	 * i].getQry_name()));
	 * 
	 * 
	 * 
	 * if (tmpValue.length()>0){ String strTmp1 =
	 * parseCondition[i].getFiled_name() + "," + tmpValue; if
	 * (leftOuterJoin.length() == 0) { leftOuterJoin = strTmp1; } else {
	 * leftOuterJoin += "|" + strTmp1; } }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * sqlCon += "  " + AdhocViewHandler.getSqlWhereString(
	 * parseCondition[i],request );
	 * 
	 * } } // 增加发展人部门控制
	 * 
	 * UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
	 * .getAttribute(WebConstKeys.ATTR_C_UserCtlStruct); // UserCtlRegionStruct
	 * rgStruct = //
	 * (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct
	 * ); logcommon.debug("UserCtlRegionStruct:" + ctlStruct.ctl_lvl + ":" +
	 * ctlStruct.ctl_sql);
	 * 
	 * List listLinkTable = AdhocHelper.getInnerLinkQry(leftOuterJoin); String
	 * linkConTmp = ""; String strTblFrom = ""; for (int k = 0; k <
	 * listLinkTable.size(); k++) { AdhocViewStruct viewTmp = (AdhocViewStruct)
	 * listLinkTable .get(k); strTblFrom += "," + viewTmp.sqlLinkTable + " ";
	 * linkConTmp += " and " + viewTmp.sqlLinkTableCon; }
	 * 
	 * String strDeptTable = ""; String strSqlDeptCon = ""; String[] adhocDef =
	 * AdhocUtil.getAdhocRightCtrl(qryStruct.adhoc_id); int iFlagCtl =
	 * Integer.parseInt(adhocDef[0]);
	 * 
	 * if (iFlagCtl==1){ int iLevel = 0; if (ctlStruct.ctl_lvl.length() > 0) {
	 * iLevel = Integer.parseInt(ctlStruct.ctl_lvl); }
	 * 
	 * if (iLevel > 1) { if (ctlStruct.ctl_sql.trim().length() > 0) { String
	 * ctlFlag = ctlStruct.ctl_flag.trim(); int iCtlFlag = 0; if
	 * (ctlFlag.length() > 0) { iCtlFlag = Integer.parseInt(ctlFlag); } switch
	 * (iCtlFlag) { case 1: // 发展人部门 strDeptTable = ",(" + ctlStruct.ctl_sql +
	 * ") tblCtrl "; strSqlDeptCon =
	 * " AND USER_DEVELOP_DEPART_ID=tblCtrl.dept_id "; break; case 2: // 楼宇部门 //
	 * 发展人部门 //strDeptTable = ",(" + ctlStruct.ctl_sql + ") tblCtrl ";
	 * //strSqlDeptCon = " AND BUILDING_MGR_DEPT_ID=tblCtrl.dept_id ";
	 * 
	 * //ENT_GNGJ_FLAG 国际:2 VARCHAR2 //BI_U_GROUP_ID 政企:1 NUMBER String grp_id =
	 * DAOFactory.getCommonFac().getLoginUser(session).group_id; if
	 * (grp_id.equals("018")){ //集团客户部：jtkh_ly 用户分群(客户类型)=政企 and 政企国内国际标志 <>国际
	 * String fieldName = "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'"; String[][] tmp =
	 * AdhocUtil.getTableFieldType("NEW_DW",adhocDef[2] , fieldName); if
	 * (tmp!=null && tmp.length>0){ for (int m=0;m<tmp.length;m++){ String
	 * dataType = tmp[m][0].substring(0,3); String fldName = tmp[m][1]; if
	 * (fldName.equalsIgnoreCase("ENT_GNGJ_FLAG")){ if
	 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
	 * " AND ENT_GNGJ_FLAG!='2'"; }else{ strSqlDeptCon +=
	 * " AND ENT_GNGJ_FLAG!=2"; } }else
	 * if(fldName.equalsIgnoreCase("BI_U_GROUP_ID")){ if
	 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
	 * " AND BI_U_GROUP_ID='1'"; }else{ strSqlDeptCon += " AND BI_U_GROUP_ID=1";
	 * } }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }else if(grp_id.equals("019")){ //如果是国际业务部 权限：用户分群(客户类型)=政企 and 政企国内国际标志
	 * =国际 String fieldName = "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'"; String[][] tmp
	 * = AdhocUtil.getTableFieldType("NEW_DW",adhocDef[2] , fieldName); if
	 * (tmp!=null && tmp.length>0){ for (int m=0;m<tmp.length;m++){ String
	 * dataType = tmp[m][0].substring(0,3); String fldName = tmp[m][1]; if
	 * (fldName.equalsIgnoreCase("ENT_GNGJ_FLAG")){ if
	 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
	 * " AND ENT_GNGJ_FLAG='2'"; }else{ strSqlDeptCon += " AND ENT_GNGJ_FLAG=2";
	 * } }else if(fldName.equalsIgnoreCase("BI_U_GROUP_ID")){ if
	 * (dataType.equalsIgnoreCase("VAR")){ strSqlDeptCon +=
	 * " AND BI_U_GROUP_ID='1'"; }else{ strSqlDeptCon += " AND BI_U_GROUP_ID=1";
	 * } }
	 * 
	 * }
	 * 
	 * } }else{
	 * 
	 * String limitFld = StringB.NulltoBlank(adhocDef[1]); if
	 * (limitFld.length()>0){ if
	 * (limitFld.equalsIgnoreCase("BUILDING_BURA_ID")){ if
	 * (ctlStruct.CTL_BUILDING_BURA_ID.length()>0){ strSqlDeptCon =
	 * " AND BUILDING_BURA_ID in(" + ctlStruct.CTL_BUILDING_BURA_ID + ") "; }
	 * 
	 * }else if (limitFld.equalsIgnoreCase("BUILDING_ID")){ if
	 * (ctlStruct.CTL_BUILDING_BURA_ID.length()>0){ strDeptTable +=
	 * ",(select distinct building_id BJ_CTLBUILDID from new_dim.d_building where BUILDING_BURA_ID in ("
	 * + ctlStruct.CTL_BUILDING_BURA_ID + ")) tblCtrl "; strSqlDeptCon =
	 * " AND BUILDING_ID=tblCtrl.BJ_CTLBUILDID "; }
	 * 
	 * } } }
	 * 
	 * break; }
	 * 
	 * } } }
	 * 
	 * if (!"".equals(row_count)) { sqlCon += " AND rownum <=" + row_count; }
	 * 
	 * sqlCon += linkConTmp; sqlCon += strSqlDeptCon; // 清单配置信息
	 * UiAdhocUserListTable[] defineInfo = null;
	 * logcommon.debug("favId::kkkkkkkkkkkkk:-=========" + favId);
	 * 
	 * if (favId.length() > 0) { logcommon.debug("favId:::-=========" + favId);
	 * 
	 * // 查询该收藏夹是否保存了清单字段 defineInfo = facade.getAdhocUserListDefine(hocInfo
	 * .getAdhoc_id(), oper_no, favId);
	 * 
	 * } else { defineInfo = facade.getAdhocUserListDefine(hocInfo
	 * .getAdhoc_id(), oper_no);
	 * 
	 * }
	 * 
	 * if (defineInfo == null || defineInfo.length <= 0) {//
	 * 没有用户自定义清单配置则采用系统默认配置
	 * 
	 * defineInfo = facade.getAdhocUserListDefineDefault(hocInfo .getAdhoc_id(),
	 * roleId);
	 * 
	 * if (defineInfo == null || defineInfo.length <= 0) { throw new
	 * HTMLActionException(request.getSession(), HTMLActionException.WARN_PAGE,
	 * "当前用户没有自定义清单以及当前即席查询也没有默认自定义清单，请通知相关配置人员！"); } }
	 * 
	 * String[] checkList = request.getParameterValues("msuSelected"); List
	 * arrList = new ArrayList();
	 * 
	 * for (int i = 0; checkList != null && i < checkList.length; i++) { for
	 * (int j = 0; j < defineInfo.length; j++) { if
	 * (checkList[i].equals(defineInfo[j].getMsu_field())) {
	 * arrList.add(defineInfo[j]); } } }
	 * 
	 * if (arrList != null && arrList.size() > 0) { defineInfo =
	 * (UiAdhocUserListTable[]) arrList .toArray(new
	 * UiAdhocUserListTable[arrList.size()]); }
	 * 
	 * String[][] res = getUserListGroupName(hocInfo.getAdhoc_id());
	 * 
	 * session.setAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO,
	 * defineInfo); request.setAttribute("LIST_GROUP_NAME", res);
	 * 
	 * session.setAttribute("query_con", sqlCon); session
	 * .setAttribute("query_from", " FROM " + hocInfo.getData_table() + " " +
	 * strTblFrom + strDeptTable);
	 * 
	 * 
	 * this.setNextScreen(request, "AdhocUserListSelect.screen"); }
	 * 
	 * }
	 */

	// right outer join关联
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {

		HttpSession session = request.getSession();
		// 操作类型
		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null || "".equals(operType)) {
			operType = "view";
		}
		session.removeAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);

		String roleId = CommonFacade.getUserAdhocRole(session);

		// 记录条数
		// StringB.NulltoBlank(sourceStr)

		String row_count = CommTool.getParameterGB(request, "row");
		if (row_count == null) {
			row_count = "";
		}
		// 当前分析即席查询功能点
		String adhoc_root = CommTool.getParameterGB(request,
				AdhocConstant.ADHOC_ROOT);
		AdhocFacade facade = new AdhocFacade(new AdhocDao());
		UiAdhocInfoDefTable hocInfo = facade.getAdhocInfo(adhoc_root);

		// hocInfo.setIsSplit(1);

		// 操作员
		String oper_no = CommonFacade.getLoginId(session);

		// 即席查询视图结构
		AdhocViewQryStruct qryStruct = new AdhocViewQryStruct();
		// 取得查询结构数据
		try {
			AppWebUtil.getHtmlObject(request, "qry", qryStruct);
		} catch (AppException ex) {
			throw new HTMLActionException(request.getSession(),
					HTMLActionException.WARN_PAGE, "查询条件受理信息有误！");
		}
		//
		if (qryStruct.gather_mon == null || "".equals(qryStruct.gather_mon)) {
			qryStruct.gather_mon = DateUtil.getDiffMonth(-1,
					DateUtil.getNowDate());
		}
		//
		qryStruct.adhoc_id = adhoc_root;
		// 保存查询结构
		session.removeAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
		session.setAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT, qryStruct);

		if ("init".equals(operType)) {
			// 清单配置信息
			UiAdhocUserListTable[] defineInfo = null;
			String favId = StringB.NulltoBlank(request
					.getParameter("favorite_id"));

			if (favId.length() > 0) {
				logcommon.debug("favId:::-=========" + favId);

				// 查询该收藏夹是否保存了清单字段
				defineInfo = facade.getAdhocUserListDefine(
						hocInfo.getAdhoc_id(), oper_no, favId);

			} else {
				defineInfo = facade.getAdhocUserListDefine(
						hocInfo.getAdhoc_id(), oper_no);

			}

			if (defineInfo == null || defineInfo.length <= 0) {// 没有用户自定义清单配置则采用系统默认配置
				defineInfo = facade.getAdhocUserListDefineDefault(
						hocInfo.getAdhoc_id(), roleId);
				if (defineInfo == null || defineInfo.length <= 0) {
					throw new HTMLActionException(request.getSession(),
							HTMLActionException.WARN_PAGE,
							"当前用户没有自定义清单以及当前即席查询也没有默认自定义清单，请通知相关配置人员！");
				}
			}

			String[] checkList = request.getParameterValues("msuSelected");
			List arrList = new ArrayList();
			for (int i = 0; checkList != null && i < checkList.length; i++) {
				for (int j = 0; j < defineInfo.length; j++) {
					if (checkList[i].equals(defineInfo[j].getMsu_field())) {
						arrList.add(defineInfo[j]);
					}
				}
			}

			if (arrList != null && arrList.size() > 0) {
				defineInfo = (UiAdhocUserListTable[]) arrList
						.toArray(new UiAdhocUserListTable[arrList.size()]);
			}

			session.setAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO,
					defineInfo);

			// sql
			String sql = "";
			String strField = "";
			String strConstField = "BJFLD";

			int isGroup = hocInfo.getIsGroup();
			String strGroupBy = "";

			for (int i = 0; i < defineInfo.length; i++) {
				if (sql.length() > 0) {
					sql += " , ";
				}
				if (strField.length() > 0) {
					strField += " , ";
				}

				strField += strConstField + i;

				if ("".equals(defineInfo[i].getMsu_type())) {
					sql += defineInfo[i].getMsu_field() + " " + strConstField
							+ i;
					if (strGroupBy.length() > 0) {
						strGroupBy += " , " + defineInfo[i].getMsu_field();
					} else {
						strGroupBy = defineInfo[i].getMsu_field();
					}

				} else if ("C".equalsIgnoreCase(defineInfo[i].getMsu_type())) {
					if (isGroup == 1) {// 需要sum
						sql += "sum(COALESCE(" + defineInfo[i].getMsu_field()
								+ ",0))" + " " + strConstField + i;
					} else {
						sql += "COALESCE(" + defineInfo[i].getMsu_field()
								+ ",0)" + " " + strConstField + i;
					}

				} else if ("D".equalsIgnoreCase(defineInfo[i].getMsu_type())) {
					sql += "TO_CHAR(" + defineInfo[i].getMsu_field()
							+ ",'YYYYMMDD')" + " " + strConstField + i;

					if (strGroupBy.length() > 0) {
						strGroupBy += " , " + "TO_CHAR("
								+ defineInfo[i].getMsu_field() + ",'YYYYMMDD')";
					} else {
						strGroupBy = "TO_CHAR(" + defineInfo[i].getMsu_field()
								+ ",'YYYYMMDD')";
					}

				} else if ("M".equalsIgnoreCase(defineInfo[i].getMsu_type())) {
					sql += "TO_CHAR(" + defineInfo[i].getMsu_field()
							+ ",'YYYYMM')" + " " + strConstField + i;

					if (strGroupBy.length() > 0) {
						strGroupBy += " , " + "TO_CHAR("
								+ defineInfo[i].getMsu_field() + ",'YYYYMM')";
					} else {
						strGroupBy = "TO_CHAR(" + defineInfo[i].getMsu_field()
								+ ",'YYYYMM')";
					}

				} else if ("SS".equalsIgnoreCase(defineInfo[i].getMsu_type())) {
					sql += "TO_CHAR(" + defineInfo[i].getMsu_field()
							+ ",'YYYY-MM-DD hh24:mi:ss')" + " " + strConstField
							+ i;

					if (strGroupBy.length() > 0) {
						strGroupBy += " , " + "TO_CHAR("
								+ defineInfo[i].getMsu_field()
								+ ",'YYYY-MM-DD hh24:mi:ss')";
					} else {
						strGroupBy = "TO_CHAR(" + defineInfo[i].getMsu_field()
								+ ",'YYYY-MM-DD hh24:mi:ss')";
					}

				} else {
					sql += defineInfo[i].getMsu_field() + " " + strConstField
							+ i;

					if (strGroupBy.length() > 0) {
						strGroupBy += " , " + defineInfo[i].getMsu_field();
					} else {
						strGroupBy = defineInfo[i].getMsu_field();
					}

				}
			}

			// logcommon.debug("字段:" + strField);

			String sqlFrom = (String) session.getAttribute("query_from");

			String sqlCon = (String) session.getAttribute("query_con");

			// 去掉distinct

			String sqlTmp = "SELECT " + strField
					+ " FROM (SELECT /*+ ordered */ " + sql + ",rownum rn "
					+ sqlFrom + sqlCon + " and rownum<=?) where rn>?";

			String sqlCnt = "SELECT /*+ ordered */ COUNT(*) " + sqlFrom
					+ sqlCon;

			if (isGroup == 1) {
				// group by
				if (strGroupBy.length() > 0) {
					sqlTmp = "select "
							+ strField
							+ " from (select JFTOT.*,ROWNUM RN from (select /*+ ordered */ "
							+ sql + sqlFrom + sqlCon + " group by "
							+ strGroupBy
							+ ") JFTOT Where rownum<=? ) where RN>?";

					sql = "select /*+ ordered */ " + sql + sqlFrom + sqlCon
							+ " group by " + strGroupBy;
				} else {
					sqlTmp = "select "
							+ strField
							+ " from (select JFTOT.*,ROWNUM RN from (select /*+ ordered */ "
							+ sql + sqlFrom + sqlCon
							+ ") JFTOT Where rownum<=? ) where RN>?";

					sql = "select /*+ ordered */ " + sql + sqlFrom + sqlCon;

				}

				// sqlCnt = "SELECT COUNT(*) " + sqlFrom + sqlCon + " group by "
				// + strGroupBy;

				sqlCnt = "SELECT /*+ ordered */ COUNT(*) from (" + sql
						+ ") JFTOT";

			} else {
				sqlTmp = "select "
						+ strField
						+ " from (select JFTOT.*,ROWNUM RN from (select /*+ ordered */ "
						+ sql + sqlFrom + sqlCon
						+ ") JFTOT Where rownum<=? ) where RN>?";

				sql = "SELECT /*+ ordered */ " + sql + sqlFrom + sqlCon;

				sqlCnt = "SELECT /*+ ordered */ COUNT(*) " + sqlFrom + sqlCon;
				// sqlCnt = "SELECT COUNT(acc_nbr) " + sqlFrom + sqlCon;

			}

			// 记录查询日志
			int iSqlLog = AdhocUtil.insertSqlLog(sql, adhoc_root, oper_no,
					StringB.toInt(row_count, -1), 2);
			if (iSqlLog == 0) {
				logcommon.debug("iSqlLog is false!");
			}
			session.setAttribute(AdhocConstant.ADHOC_QRY_SQL, sqlTmp);

			session.setAttribute(AdhocConstant.ADHOC_RECORD_CNT, sqlCnt);
			session.setAttribute(AdhocConstant.ADHOC_EXPORT_ROWCNT, row_count);

			if (!"".equals(row_count)) {

			} else {
				row_count = "20000";
			}

			String strWhere[] = new String[] { row_count + "|1", "0|1" };

			// String arr[][] = WebDBUtil.execQryArray(sqlTmp, strWhere, "");
			// String[][] list = AdhocUtil.queryArrayFacade(sql);
			String[][] list = AdhocUtil.queryArrayFacade(sqlTmp, strWhere);

			AdhocViewHandler.fillDescByMapCode(request, list, defineInfo);

			session.setAttribute(AdhocConstant.ADHOC_USER_LIST_VALUE, list);

			this.setNextScreen(request, "AdhocUserList.screen");
		} else {

			String favId = StringB.NulltoBlank(request
					.getParameter("favorite_id"));
			// 提取有值条件对象数组

			// UiAdhocConditionMetaTable[] valueCondition = AdhocViewHandler
			// .getConditionValueArray(session, adhoc_root, qryStruct);

			// 界面选中条件编码（不含默认条件）
			String[] conParam = request
					.getParameterValues(AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表

			// 保存清单条件========处理过程

			String conStr = CommTool.getParameterGB(request, "conStr001");
			if (conStr == null) {
				conStr = "";
			}

			String dimStr = CommTool.getParameterGB(request, "dimStr001");
			if (dimStr == null) {
				dimStr = "";
			}

			String msuStr = CommTool.getParameterGB(request, "msuStr001");
			if (msuStr == null) {
				msuStr = "";
			}
			UiAdhocFavoriteDefTable favInfo = new UiAdhocFavoriteDefTable();
			favInfo.setConStr(conStr);
			favInfo.setDimStr(dimStr);
			favInfo.setMsuStr(msuStr);

			String[] conArr = null;

			if (conStr != null && !"".equals(conStr)) {
				conArr = conStr.split(",");
			}
			List listSql = new ArrayList();
			List listConStr = new ArrayList();

			if (conArr != null) {
				for (int i = 0; i < conArr.length; i++) {
					String[] value = getConditionValue(request, conArr[i]);

					listSql.add(value);
					listConStr.add(conArr[i]);
				}
			}

			favInfo.setListValue(listSql);
			favInfo.setListConStr(listConStr);

			session.setAttribute("UiAdhocFavoriteDefTable", favInfo);

			// 结束处理过程

			// 界面选中条件编码（含默认条件）
			String[] hconParam = request.getParameterValues("H_"
					+ AdhocConstant.ADHOC_CONDITION_CHECK_NAME); // 取得条件的选择列表

			// 提取全部选中条件对象
			UiAdhocConditionMetaTable[] parseCondition = AdhocViewHandler
					.getConditionSelectedArray(conParam, facade, adhoc_root,
							hconParam);
			session.setAttribute(
					AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY,
					parseCondition);
			// 条件
			String sqlCon = " WHERE 1=1 ";
			String leftOuterJoin = "";

			if (parseCondition != null) {
				for (int i = 0; parseCondition != null
						&& i < parseCondition.length; i++) {
					// 分不同类型条件提取拼装SQL

					if (parseCondition[i].getCon_type().equals(
							AdhocConstant.ADHOC_CONDITION_TYPE_17)) {// 上传文件类型

						String tmpValue = StringB.NulltoBlank(request
								.getParameter(parseCondition[i].getQry_name()));

						if (tmpValue.length() > 0) {
							String strTmp1 = parseCondition[i].getFiled_name()
									+ "," + tmpValue;
							if (leftOuterJoin.length() == 0) {
								leftOuterJoin = strTmp1;
							} else {
								leftOuterJoin += "|" + strTmp1;
							}
						}

					}

					sqlCon += "  "
							+ AdhocViewHandler.getSqlWhereString(
									parseCondition[i], request);

				}
			}
			// 增加发展人部门控制

			int ctlSplitOk = 0; // 是否从分表查询
			String strQryTable = hocInfo.getData_table() + " ";

			if (hocInfo.getIsSplit() == 1) {
				// 需要分表
				String prefixTbl = hocInfo.getPrefixTbl();
				int accnt = hocInfo.getAccountNum();
				String selValue = StringB.NulltoBlank(request
						.getParameter(hocInfo.getConId()));// 选择的值
				// String selValue = "201011";//选择的值
				int iSelValue = 0;

				if (selValue.length() > 0) {
					iSelValue = StringB.toInt(selValue, 0);

					// 判断查询类型，日或月
					int curOptime = 0; // 当前账期
					switch (hocInfo.getAdhocType()) {
					case 0:
						// 日查询
						curOptime = StringB.toInt(
								DateUtil.getDiffDay(-1, DateUtil.getNowDate()),
								0);
						break;
					case 1:
						// 月查询
						curOptime = StringB.toInt(DateUtil.getDiffMonth(-1,
								DateUtil.getNowDate()), 0);
						break;
					}

					int interval = accnt - (curOptime - iSelValue);// 间隔数

					if (interval >= 0) {
						ctlSplitOk = 1;
						strQryTable = prefixTbl + selValue + " ";
					} else {
						strQryTable = hocInfo.getData_table() + " ";
					}

				} else {
					strQryTable = hocInfo.getData_table() + " ";
				}

			} else {
				strQryTable = hocInfo.getData_table() + " ";
			}

			UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
					.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
			// UserCtlRegionStruct rgStruct =
			// (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
			logcommon.debug("UserCtlRegionStruct:" + ctlStruct.ctl_lvl + ":"
					+ ctlStruct.ctl_sql);

			List listLinkTable = AdhocHelper.getInnerLinkQry(leftOuterJoin);
			String linkConTmp = "";
			String strTblFrom = "";
			for (int k = 0; k < listLinkTable.size(); k++) {
				AdhocViewStruct viewTmp = (AdhocViewStruct) listLinkTable
						.get(k);
				strTblFrom += viewTmp.sqlLinkTable + " ";
				linkConTmp += " and " + viewTmp.sqlLinkTableCon;
			}

			String[] rtnRight = AdhocUtil.getAdhocRightControl(
					qryStruct.adhoc_id, ctlStruct, ctlSplitOk, session);
			String strDeptTable = rtnRight[0];
			String strSqlDeptCon = rtnRight[1];
			/*
			 * String[] adhocDef =
			 * AdhocUtil.getAdhocRightCtrl(qryStruct.adhoc_id); int iFlagCtl =
			 * Integer.parseInt(adhocDef[0]);
			 * 
			 * if (iFlagCtl == 1) { int iLevel = 0; if
			 * (ctlStruct.ctl_lvl.length() > 0) { iLevel =
			 * Integer.parseInt(ctlStruct.ctl_lvl); }
			 * 
			 * if (iLevel > 1) { if (ctlStruct.ctl_sql.trim().length() > 0) {
			 * String ctlFlag = ctlStruct.ctl_flag.trim(); int iCtlFlag = 0; if
			 * (ctlFlag.length() > 0) { iCtlFlag = Integer.parseInt(ctlFlag); }
			 * switch (iCtlFlag) { case 1: // 发展人部门 int hasSecond =
			 * StringB.toInt(adhocDef[3], 0); int isSplit =
			 * StringB.toInt(adhocDef[4], 0);
			 * 
			 * if (hasSecond==1 && isSplit==1 && ctlSplitOk ==1){ int ctl_lvl =
			 * StringB.toInt(ctlStruct.ctl_lvl, 1); switch(ctl_lvl){ case 1:
			 * 
			 * break; case 2: //sql += ",(" + ctlStruct.ctl_sql + ") tblCtrl ";
			 * strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID2 in(" +
			 * ctlStruct.ctl_city_str_add +")";
			 * 
			 * break; case 3: strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID3 in("
			 * + ctlStruct.ctl_county_str_add +")"; strSqlDeptCon +=
			 * " AND USER_DEVELOP_DEPART_ID2 in(" + ctlStruct.ctl_city_str_add
			 * +")";
			 * 
			 * break; case 4:
			 * 
			 * break; case 5:
			 * 
			 * break; }
			 * 
			 * }else{ strDeptTable = ",(" + ctlStruct.ctl_sql + ") tblCtrl ";
			 * strSqlDeptCon = " AND USER_DEVELOP_DEPART_ID=tblCtrl.dept_id "; }
			 * 
			 * 
			 * break; case 2: // 楼宇部门 // 发展人部门 // strDeptTable = ",(" +
			 * ctlStruct.ctl_sql + // ") tblCtrl "; // strSqlDeptCon = //
			 * " AND BUILDING_MGR_DEPT_ID=tblCtrl.dept_id ";
			 * 
			 * // ENT_GNGJ_FLAG 国际:2 VARCHAR2 // BI_U_GROUP_ID 政企:1 NUMBER
			 * String grp_id = DAOFactory.getCommonFac()
			 * .getLoginUser(session).group_id; if (grp_id.equals("018")) { //
			 * 集团客户部：jtkh_ly 用户分群(客户类型)=政企 and 政企国内国际标志 <>国际 String fieldName =
			 * "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'"; String[][] tmp =
			 * AdhocUtil.getTableFieldType( "NEW_DW", adhocDef[2], fieldName);
			 * if (tmp != null && tmp.length > 0) { for (int m = 0; m <
			 * tmp.length; m++) { String dataType = tmp[m][0].substring( 0, 3);
			 * String fldName = tmp[m][1]; if (fldName
			 * .equalsIgnoreCase("ENT_GNGJ_FLAG")) { if (dataType
			 * .equalsIgnoreCase("VAR")) { strSqlDeptCon +=
			 * " AND ENT_GNGJ_FLAG!='2'"; } else { strSqlDeptCon +=
			 * " AND ENT_GNGJ_FLAG!=2"; } } else if (fldName
			 * .equalsIgnoreCase("BI_U_GROUP_ID")) { if (dataType
			 * .equalsIgnoreCase("VAR")) { strSqlDeptCon +=
			 * " AND BI_U_GROUP_ID='1'"; } else { strSqlDeptCon +=
			 * " AND BI_U_GROUP_ID=1"; } }
			 * 
			 * }
			 * 
			 * }
			 * 
			 * } else if (grp_id.equals("019")) { // 如果是国际业务部 权限：用户分群(客户类型)=政企
			 * and 政企国内国际标志 =国际 String fieldName =
			 * "'ENT_GNGJ_FLAG','BI_U_GROUP_ID'"; String[][] tmp =
			 * AdhocUtil.getTableFieldType( "NEW_DW", adhocDef[2], fieldName);
			 * if (tmp != null && tmp.length > 0) { for (int m = 0; m <
			 * tmp.length; m++) { String dataType = tmp[m][0].substring( 0, 3);
			 * String fldName = tmp[m][1]; if (fldName
			 * .equalsIgnoreCase("ENT_GNGJ_FLAG")) { if (dataType
			 * .equalsIgnoreCase("VAR")) { strSqlDeptCon +=
			 * " AND ENT_GNGJ_FLAG='2'"; } else { strSqlDeptCon +=
			 * " AND ENT_GNGJ_FLAG=2"; } } else if (fldName
			 * .equalsIgnoreCase("BI_U_GROUP_ID")) { if (dataType
			 * .equalsIgnoreCase("VAR")) { strSqlDeptCon +=
			 * " AND BI_U_GROUP_ID='1'"; } else { strSqlDeptCon +=
			 * " AND BI_U_GROUP_ID=1"; } }
			 * 
			 * }
			 * 
			 * } } else {
			 * 
			 * String limitFld = StringB .NulltoBlank(adhocDef[1]); if
			 * (limitFld.length() > 0) { if (limitFld
			 * .equalsIgnoreCase("BUILDING_BURA_ID")) { if
			 * (ctlStruct.CTL_BUILDING_BURA_ID .length() > 0) { strSqlDeptCon =
			 * " AND BUILDING_BURA_ID in(" + ctlStruct.CTL_BUILDING_BURA_ID +
			 * ") "; }
			 * 
			 * } else if (limitFld .equalsIgnoreCase("BUILDING_ID")) { if
			 * (ctlStruct.CTL_BUILDING_BURA_ID .length() > 0) { strDeptTable +=
			 * ",(select distinct building_id BJ_CTLBUILDID from new_dim.d_building where BUILDING_BURA_ID in ("
			 * + ctlStruct.CTL_BUILDING_BURA_ID + ")) tblCtrl "; strSqlDeptCon =
			 * " AND BUILDING_ID=tblCtrl.BJ_CTLBUILDID "; }
			 * 
			 * } } }
			 * 
			 * break; }
			 * 
			 * } } }
			 */
			if (!"".equals(row_count)) {
				sqlCon += " AND rownum <=" + row_count;
			}

			sqlCon += linkConTmp;
			sqlCon += strSqlDeptCon;
			// 清单配置信息
			UiAdhocUserListTable[] defineInfo = null;
			logcommon.debug("favId::kkkkkkkkkkkkk:-=========" + favId);

			if (favId.length() > 0) {
				logcommon.debug("favId:::-=========" + favId);

				// 查询该收藏夹是否保存了清单字段
				defineInfo = facade.getAdhocUserListDefine(
						hocInfo.getAdhoc_id(), oper_no, favId);

			} else {
				defineInfo = facade.getAdhocUserListDefine(
						hocInfo.getAdhoc_id(), oper_no);

			}

			if (defineInfo == null || defineInfo.length <= 0) {// 没有用户自定义清单配置则采用系统默认配置

				defineInfo = facade.getAdhocUserListDefineDefault(
						hocInfo.getAdhoc_id(), roleId);

				if (defineInfo == null || defineInfo.length <= 0) {
					throw new HTMLActionException(request.getSession(),
							HTMLActionException.WARN_PAGE,
							"当前用户没有自定义清单以及当前即席查询也没有默认自定义清单，请通知相关配置人员！");
				}
			}

			String[] checkList = request.getParameterValues("msuSelected");
			List arrList = new ArrayList();

			for (int i = 0; checkList != null && i < checkList.length; i++) {
				for (int j = 0; j < defineInfo.length; j++) {
					if (checkList[i].equals(defineInfo[j].getMsu_field())) {
						arrList.add(defineInfo[j]);
					}
				}
			}

			if (arrList != null && arrList.size() > 0) {
				defineInfo = (UiAdhocUserListTable[]) arrList
						.toArray(new UiAdhocUserListTable[arrList.size()]);
			}

			String[][] res = getUserListGroupName(hocInfo.getAdhoc_id());

			session.setAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO,
					defineInfo);
			request.setAttribute("LIST_GROUP_NAME", res);

			session.setAttribute("query_con", sqlCon);

			session.setAttribute("query_from", " FROM " + strQryTable + " "
					+ strTblFrom + strDeptTable);

			/*
			 * session .setAttribute("query_from", " FROM " +
			 * hocInfo.getData_table() + " " + strTblFrom + strDeptTable);
			 */

			this.setNextScreen(request, "AdhocUserListSelect.screen");
		}

	}

	private String[] getConditionValue(HttpServletRequest request, String conArr) {
		String sql = "select a.qry_name,a.con_type from ui_adhoc_condition_meta a where a.con_id='"
				+ conArr + "'";
		String[] strRetn = { "", "", "", "" };

		try {
			String[][] arr = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				int con_type = Integer.parseInt(arr[0][1]);
				switch (con_type) {
				case 2:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_11"));
					strRetn[1] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_22"));
					break;
				case 9:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_11"));
					strRetn[1] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_22"));
					break;
				case 10:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_11"));
					strRetn[1] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_A_22"));
					break;
				default:
					strRetn[0] = StringB.NulltoBlank(request
							.getParameter(arr[0][0]));
					strRetn[1] = "";
					strRetn[2] = StringB.NulltoBlank(request
							.getParameter(arr[0][0] + "_desc"));
					break;
				}

			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return strRetn;
	}

	/**
	 * 
	 * @param adhoc_Id
	 * @return
	 * @desc:得到group_name信息
	 * 
	 */
	private String[][] getUserListGroupName(String adhoc_Id) {
		String res[][] = null;
		String sql = "select distinct group_name,group_id from ui_adhoc_user_list t where t.oper_no='-1' and t.adhoc_id='"
				+ adhoc_Id + "' order by t.group_id";
		try {
			res = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
		}

		return res;
	}

}
