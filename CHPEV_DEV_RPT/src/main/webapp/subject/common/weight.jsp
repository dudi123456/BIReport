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
  <script type="text/javascript">
  	$(document).ready(function(){
  		$("#weight").html("<%=content%>");
  	});
  	
  	function export_table_content(){
  		var tableName="评分指标、权重及计算公式";
  		tableName=encodeURI(encodeURI(tableName));
		window.open("RegionalMsuExport.rptdo?tableName="+tableName,"数据导出","");
	}
  	
  </script>
  </head>
<style>
body{padding:0px;margin:0px;overflow:auto;}
html{overflow:auto;}
.iw{
	width:250px;
	position:absolute;
	right:15px;
	top:0px;
	text-align:right;
}

</style>
  <body>
    	<div class="result_title">
		<span >评分指标及权重</span>
		<div class="iw">
	       <input type="button" id="button_submit"  class="btn_exceldown"  value="导出EXCEL" onclick="javascript:export_table_content()">
		</div>
	</div>
  	<table width="100%" cellpadding="0" cellspacing="0" id='weight'>
  	</table>
  </body>
</html>
