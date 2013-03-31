<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.sigma.*"%>

<%
String path = request.getContextPath();

	    int sigmaId = Integer.parseInt(request.getParameter("sigmaId"));

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<jsp:include  page="/jsp/head.jsp" />

	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
<body>

    <div id="bigbox" style="display:!none;">
<!--       <div id="gridbox_<%=sigmaId%>" style="border:0px solid #cccccc;background-color:#f3f3f3;padding:5px;height:600px;width:100%;" ></div> -->
      <div id="gridbox_<%=sigmaId%>"  ></div>
    </div>


</html>
<%
	    SigmaGridUtil util = new SigmaGridUtil(sigmaId,path);
		util.initBeanInfo();		
		out.print(util.showSigmaScriptLanguage().toString());
		out.print(util.showParamsRander(request).toString());


%>

