<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import = "com.ailk.bi.base.util.WebKeys" %>
<%
String content = (String)session.getAttribute(WebKeys.ATTR_ST_AREA_SEGMENT_REFLECT);
if(content == null)
{
	content = "";
}
%>

<html>
  <head>
  <title>评分指标及权重</title>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/unicomcontent.css">
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
  </head>
<style>
body{padding:0px;margin:0px;overflow:auto;}
html{overflow:auto;}
</style>
  <body>
    <div class="result_title">
		<span >评分指标及权重</span>
	</div>
  	<table width="100%" cellpadding="0" cellspacing="0" id='weight'>
  	 <%=content%>
  	</table>
  </body>
</html>
