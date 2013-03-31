<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%@ page import="com.ailk.bi.common.sysconfig.*"%>
<% 
String AppRoot = GetSystemConfig.getBIBMConfig().getExtParam("web_root");
String filename = (String) session.getAttribute(WebKeys.ATTR_REPORT_BATCH_EXPORT);

%>

<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/subject.css" type="text/css">
<style type="text/css">
<!--
.style1 {
	color: #47597A;
	font-weight: bold;
	font-size: 14px;
}
-->
</style>

<script language="javascript">

//
function LoadXSL(){
	document.execCommand("SaveAs","<%=AppRoot+filename%>");
}
		
</script>



</head>

<body onload="LoadXSL()" >

<table width="100%" >
  <tr>
    <td width="30%" height="240" align="right"><img src="../biimages/zt/icon-educe.gif" width="136" height="126"></td>
    <td width="70%"><span class="style1">如果导出Excel时有提示是否保存，<br>
      <br>
    请先选择在浏览器中打开，<br>
    <br>
    然后另存为xls文件。</span></td>
  </tr>
</table>
</body>
</html>