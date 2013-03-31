<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.common.app.*" %>
<%@ page import = "com.ailk.bi.common.dbtools.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.common.app.AppException"%>

<%
//
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}

String[][] value = (String[][])session.getAttribute(WebKeys.ATTR_OPP_SUBJECT_CF_lIST);
if(value == null){
	value = new String[0][0];
}
%>


<html>
<head>
<meta http-equiv="Content-Language" content="zh-cn">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<title>饱和度分析</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/wait.js"></script>
<script>
document.body.onmousemove=quickalt;
document.body.onmouseover=getalt;
document.body.onmouseout=restorealt;
var tempalt='';
</script>
<script>
function doQuery(){
	 ShowWait();
	 TableQryForm.target = "_self";
	 TableQryForm.action = "AddSubject.rptdo";
	 TableQryForm.submit();

	 
}

</script>
<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
</head>


<body onload="selfDisp();closeWaitWin();">
<FORM name="TableQryForm" method="post">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<div style="display:none;position:absolute;" id=altlayer></div>

<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr class="tools">
		<td><font class='tooltitle'>&nbsp;增值业务专题&nbsp;>>&nbsp;增值业务推荐&nbsp;>>&nbsp;饱和度分析&nbsp;</font></td>
		<td title="点击打开条件选择菜单" align="right" onclick=showcat('menucontent'); style="cursor:pointer">
		    <font class="tooltxt"  title="点击打开条件选择菜单" ><b>时间</b>：<span id=gather_month><%=qryStruct.gather_month%></span><INPUT id=__gind971_50 type=hidden value="<%=qryStruct.gather_month%>" name="qry__gather_month" monthOnly="true" max="<%=qryStruct.date_e%>" min="200801" size="10"></font>
		</td>
	</tr>
</table>

<div id=menucontent style="position:absolute; width:180; height:1px;right:50;top:15;padding-top:10;overflow:hidden;"> 
<table class="popwidow" cellpadding="0" cellspacing="5">
	<tr>
		<td class="poptitle" style="width:80px">时间</td>
		<td class="poptitle" style="width:40px">操作</td>
	</tr>
	<tr>		
		
		
		<td valign="top"><%=PageConditionUtil.getMonthDesc()%></td>	
		<td valign="top" align="center">
		<table style="width: 100%">
			<tr>
				<td align="center"  height="25"><input name="Button3" id="button_submit" type="button" value="确认" onclick="doQuery()"/></td>
			</tr>
			<tr>
				<td align="center"  height="25"><input name="Button2" id="button_reset" type="button" value="取消" onclick="hiddencat('menucontent')"/></td>
			</tr>
		</table>
	</td>
	</tr>
</table>
 <iframe src="JavaScript:false" style="position:absolute; visibility:inherit; top:0px; left:0px; width:100px; height:200px; z-index:-1; filter=progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0);"></iframe>  
</div>
<table style="width: 100%"  style="background-color:#F0F0F0;border-top:1px #D8D8E4 solid;">
	<tr>
		<td class="toolbg">
		资费名称：<BIBM:TagSelectList listID="0" listName="qry__price_plan_id"  focusID="<%=qryStruct.price_plan_id%>" selfSQL="select price_plan_id,price_plan_name from d_adv_focus_pp"/>
		&nbsp;分析视角：<BIBM:TagRadio radioName="qry__add_dim_id" radioID="ADD001" focusID="<%=qryStruct.add_dim_id%>" />
		<input id="button_submit" type="button" value="确认" onclick="doQuery()"/>
		</td>
	</tr>
	
	<tr>
		<td class="toolbg">
		套餐描述：<%=qryStruct.price_plan_desc%>
		</td>
	</tr>	
</table>

<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" width="60%" vlaign="top">
				<iframe id="chart_add_subject_bhd_01" width="100%" height="500"	src="SubjectCommChart.screen?chart_id=<%=qryStruct.add_dim_id%>&first=Y&gather_month=<%=qryStruct.gather_month%>"  frameborder="0" scrolling="no"></iframe>
				</td>
				<td>
					<table style="width: 100%; height: 100px;" style="background-color:#EFF7FC; border:1px #CAD7E7 dashed">
					<tr>
						<td style="line-height: 20px;">
						<%=qryStruct.add_dim_desc%> 
						</td>
					</tr>
					
					
					<tr>
    					<td><%=CommTool.getEditorHtml("BHDFX","0")%></td>
					</tr>
					
				</table>
				</td>
			</tr>
		</table>
 
</FORM>

</body>

</html>






