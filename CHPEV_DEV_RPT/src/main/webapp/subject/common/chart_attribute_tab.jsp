<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<!DOCTYPE html>
<html>
<head>
<title>图形属性参考</title>
<%@ include file="/base/commonHtml.jsp" %>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/kw.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">

<%
String tab_name = request.getParameter("tab_name");
if(tab_name==null||"".equals(tab_name)){
	tab_name = "sub1";
}
%>
</head>

<body onLoad="buildUnicomTab('<%=tab_name%>')">
	<div class="toptag">
		您所在位置：图形配置 >> 图形属性参考
	</div>
	<br>
	<div class="clearfix">
	  <div id="tab">
	  </div>
	</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="500" width="100%" valign="top">
    	<iframe id="iframeListBoard" width="100%" height="500" scrolling="auto" frameborder="0" onload="javascript:{dyniframesize('iframeListBoard');}" ></iframe></td>
  </tr>
</table>
</body>
<!--taber标签切换区开始-->
<script language="JavaScript">
	  //定义Taber标签数组
	  var taberArr=new Array(''
	   ,new Array('sub1','fusionChart图形属性','chart_attribute_funsion.htm','iframeListBoard')
	   ,new Array('sub2','fusionChart序列属性','chart_attribute_funsion_series.htm','iframeListBoard')
	   ,new Array('sub3','jfreeChart图形属性','chart_attribute.htm','iframeListBoard')
      )
</script>
<!--/taber标签切换区结束-->
</html>
