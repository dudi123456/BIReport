<%@page import="waf.controller.web.action.HTMLActionException"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.base.util.CommConditionUtil"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.base.table.PubInfoConditionTable"%>
<%@page import="com.ailk.bi.base.struct.UserCtlRegionStruct"%>
<%@page import="com.ailk.bi.base.util.WebConstKeys"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*,java.util.*"%>
<%@ page import="com.fins.gt.server.*"%>
<%@ page import="com.ailk.bi.common.dbtools.WebDBUtil"%>
<%@ page import="com.ailk.bi.common.app.*"%>
<%@ page import="com.ailk.bi.sigma.*"%>


<%!
	int getRecordCount(String sql){
		try {
			String res[][] = WebDBUtil.execQryArray(sql, "");
			if (res!=null && res.length>0){
				return Integer.parseInt( res[0][0]);
			}else{
				return 0;
			}
		} catch (AppException e) {
			return 0;
		}

	}
%>
<%
String sigmaId = request.getParameter("sigmaId");

//报表查询条件对象
ReportQryStruct qryStruct = new ReportQryStruct();
UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
if (ctlStruct == null) {
	ctlStruct = new UserCtlRegionStruct();
}
if (StringTool.checkEmptyString(qryStruct.city_id)) {// 地市，控制地市
	qryStruct.right_city_id = ctlStruct.ctl_city_str_add;
}

//
//报表条件解析
PubInfoConditionTable[] cdnTables = null;
//读取报表条件解析
try {
	cdnTables = CommConditionUtil.genCondition(sigmaId, ReportConsts.CONDITION_PUB);
} catch (AppException e) {
	throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "获取报表条件信息失败！");
}
String rightWhere = CommConditionUtil.getRptWhere(cdnTables, qryStruct);
//System.out.println("rightWhere======"+rightWhere);


		GridServerHandler gridServerHandler=new GridServerHandler(request,response);
		System.out.println("zxj:" + gridServerHandler.getParameter("_gt_json"));

		// 取得总记录数
		int totalRowNum=gridServerHandler.getTotalRowNum();
		//如果总数不存在, 那么调用"相应的方法" 来取得总数


		//System.out.println(gridServerHandler.getParameters().get("tableId") + ":>>>>>>>>>>>>>");
		//String sigmaId = request.getParameter("sigmaId");
		String sql = "select SQL_DEFINE,SQL_CONDITION,SQL_ORDERBY,REMOTE_PAGE,SQL_GROUP,SIGMA_DESC from ui_sigma_define_base where SIGMA_ID=" + sigmaId;
		//System.out.println(sql);
		SigmaGridUtil util = new SigmaGridUtil(Integer.parseInt(sigmaId));

		try {
			String res[][] = WebDBUtil.execQryArray(sql, "");
			if (res!=null && res.length>0){
				String sqlCon = util.getConditonSql(gridServerHandler.getParameters(),request);
				sqlCon += rightWhere;
				System.out.println(sqlCon + ":::sqlCon:" + gridServerHandler.getParameters().size());
				String strSelect = res[0][0] + " " + res[0][1] + " " + sqlCon + " " + res[0][4] + " " +  res[0][2];
				System.out.println("sigmaSql:" + strSelect);
				session.setAttribute("oper_type_export","datareport");
				session.setAttribute("datareport_sql_"+sigmaId, strSelect);
				session.setAttribute("datareport_condition_"+sigmaId, sqlCon);
				session.setAttribute("datareport_desc_"+sigmaId, res[0][5]);
				String sqlCnt = " Select count(*) from (" + strSelect + ")";
				if (totalRowNum<1){
					//System.out.println("qry count ");
					totalRowNum=getRecordCount(sqlCnt);
					session.setAttribute("datareport_totalnum_"+sigmaId, totalRowNum);
					//将取得的总数赋给gridServerHandler
					gridServerHandler.setTotalRowNum(totalRowNum);
				}
				List list = null;
				String remotePage = StringB.NulltoBlank( res[0][3]);

				if (remotePage.equalsIgnoreCase("true")){
					list = WebDBUtil.getBeans_withSortSupport_limit_general(gridServerHandler.getStartRowNum()-1,gridServerHandler.getPageSize(), strSelect);
				}else{
					list = WebDBUtil.getBeans_withSortSupport_limit_general(strSelect);
				}


				gridServerHandler.setData(list);
				//System.out.println(gridServerHandler.getLoadResponseText());
				//System.out.println("ok");

				out.print(gridServerHandler.getLoadResponseText());

			}

		} catch (AppException e) {
			e.printStackTrace();
		}
%>