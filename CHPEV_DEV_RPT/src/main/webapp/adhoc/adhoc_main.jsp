<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.adhoc.struct.AdhocViewQryStruct"%>

<%
	//
    AdhocViewQryStruct qryStruct  = (AdhocViewQryStruct)session.getAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
	
	//即席查询
	String adhoc = request.getParameter(AdhocConstant.ADHOC_ROOT);
	if(adhoc == null || "".equals(adhoc)){
		if(qryStruct!=null){
			adhoc = qryStruct.adhoc_id;
		}else{
			adhoc = AdhocConstant.ADHOC_ROOT_DEFAULT_VALUE;
		}
		
	}
	
	System.out.println("adhoc===================="+adhoc);
%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
</head>

<frameset cols="165,*" frameborder="no" border="0" framespacing="0" name="bodyFrameset"> 
  <frame src="adhoc_left.jsp?adhoc_root=<%=adhoc%>" name="adhoc_leftFrame" scrolling="no">
  <frame src="adhoc_qry.jsp?adhoc_root=<%=adhoc%>" name="adhoc_mainFrame" scrolling="yes">
</frameset>

<noframes>
</noframes> 
</html>
