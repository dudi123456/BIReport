<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.adhoc.service.impl.*"%>
<%@page import="com.ailk.bi.adhoc.dao.impl.*"%>
<%@page import="com.ailk.bi.adhoc.domain.*"%>
<%@page import="com.ailk.bi.pages.CommKeys"%>
<%@page import="com.ailk.bi.adhoc.util.*"%>
<%@page import="com.ailk.bi.common.app.*"%>
<%@page import="com.ailk.bi.common.dbtools.WebDBUtil"%>

<%
String con_id = request.getParameter("con_id");
String putInId = StringB.NulltoBlank(request.getParameter("idStr")).trim();

//String con_id = "11412";
//String putInId = "16|17";

String sqlId = AdhocUtil.fillCharString(putInId);

AdhocCheckFacade facade = new AdhocCheckFacade(new AdhocCheckDao());
		//��烘��SQL
		String [][] multiCheck = facade.getCheckSQL(con_id);
		
		String sql = multiCheck[0][1];
		
		String orderStr = "";
		String sqlStr = "";
		//logcommon.debug("sql===========" + sql);
		
		//澶����order  by 
		if(sql.toUpperCase().indexOf("ORDER BY")>0){
			sqlStr  = sql.substring(0, sql.toUpperCase().indexOf("ORDER BY"));
			orderStr = sql.substring(sql.toUpperCase().indexOf("ORDER BY"));
			//logcommon.debug("sql=====order======" + sqlStr);
			//logcommon.debug("orderStr=order==========" + orderStr);
			
		}else{
			sqlStr  = sql;
		}
		
		String strField = AdhocUtil.getSQLFirstField(sqlStr);
		


		if(sqlStr.toUpperCase().indexOf(" WHERE ")<0){
			sqlStr += " where 1=1 ";
		}

		if (sqlId.length()>0){
			sqlStr += " and " + strField + " in (" + sqlId + ")";
		}
	//	StringBuffer strB = new StringBuffer("");
		System.out.println("sdfdsf:" + sqlStr);
		String strOutHtml = "";
		try{
			String svces[][] = WebDBUtil.execQryArray(sqlStr, "");
			if (svces != null && svces.length > 0) {
				
				for (int i = 0; i < svces.length; i++) {		
					if (strOutHtml.length()==0){
						strOutHtml = svces[i][1];
					}else{
						strOutHtml += "$" + svces[i][1];
					}
					
				}
			}
			
		}catch (AppException ex1) {
			ex1.printStackTrace();
		}
		out.print(strOutHtml);


%>