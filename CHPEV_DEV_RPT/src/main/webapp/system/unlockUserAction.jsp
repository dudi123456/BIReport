<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<jsp:directive.page import="com.ailk.bi.common.dbtools.WebDBUtil"/>
<%
	String out_str = "";
	String sql ="";
	String user_id = request.getParameter("user_id");
	System.out.print("user_id:"+user_id);
	InfoOperTable oper = CommonFacade.getLoginUser(session);
	if (oper != null) {
		sql = "select status from ui_info_user where user_id='"+user_id+"'";
	    String result = WebDBUtil.getSingleValue(sql);
		if("99".equals(result)){
			String sqlupdate = "update ui_info_user set status=0 where user_id='"+user_id+"'";
			WebDBUtil.execUpdate(sqlupdate);
			out_str = "1";
		}else{
			out_str = "0";
		}
	} else {
		out_str = "error";
	}
	out.print(out_str);
%>
