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

String[][] value = (String[][])session.getAttribute(WebKeys.ATTR_OPP_SUBJECT_VALUE_lIST);
if(value == null){
	value = new String[0][0];
}
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<title>竞争对手高价值分析</title>
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
//对象
function BaseXmlSubmit(){
}
//动作
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = null;
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
 
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}

//实例
var baseXmlSubmit =new BaseXmlSubmit();



 function doQuery(){
	 ShowWait();
	 TableQryForm.target = "_self";
	 TableQryForm.action = "OppSubject.rptdo";
	 TableQryForm.submit();

	 
 }
 //导出
 function export_table_content(param){
	 if("cur" == param){
		 TableQryForm.target = "_blank";
		 TableQryForm.action = "OppValueToExcel.screen";
		 TableQryForm.submit();
		 
	 }else if("all" == param){
		    var dimTableStr = baseXmlSubmit.callAction("../subject/SubejctAjax.rptdo?type_id=OPPSUBJECT&res_id=高价值分析&exportName=竞争对手高价值分析");
			dimTableStr=dimTableStr.replace(/^\s+|\n+$/g,'');
			document.getElementById("errordiv").innerHTML = "<span><font color=\"red\">&nbsp;&nbsp;&nbsp;&nbsp;"+dimTableStr+"文件下载请稍候单击 \"文件下载\" 按钮！</font></span>";
			
	 }
	
 }
 //
 function download_file(){
	 TableQryForm.target = "_blank";
	 TableQryForm.action = "AdhocInfoExport.rptdo";
	 TableQryForm.submit();
 }

 function open_metaExplain(adhoc_id)
 {
     var h = "500";
     var w = "750";
     var top = (screen.availHeight - h) / 2;
     var left = (screen.availWidth - w) / 2;
     var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
     var strUrl = "../adhoc/adhocMetaExplain.rptdo?adhoc_id=" + adhoc_id;
     var newsWin = window.open(strUrl, "editRptHead", optstr);
     if (newsWin != null) {
         newsWin.focus();
     }
 }
 
</script>



<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
</head>


<body onload="selfDisp();closeWaitWin();">
<FORM name="TableQryForm" method="post">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden name="oper_type" value="qrySubjectTaskSts">
<INPUT type=hidden name="typeId" value="OPPSUBJECT">
<INPUT type=hidden name="resId" value="高价值分析">

<div style="display:none;position:absolute;" id=altlayer></div>

<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr class="tools">
		<td><font class='tooltitle'>&nbsp;竞争对手专题&nbsp;>>&nbsp;交往圈分析&nbsp;>>&nbsp;竞争对手高价值分析&nbsp;</font></td>
		<td><a href='javascript:;' onclick="open_metaExplain('SUBJECT')"><img src="<%=request.getContextPath()%>/biimages/home/nav_icon3.gif" width="14" height="14" border="0" hspace="10">指标说明</a></td>
		<td title="点击打开条件选择菜单" align="right" onclick=showcat('menucontent'); style="cursor:pointer">
		   <font class="tooltxt"  title="点击打开条件选择菜单" ><b>运营商</b>：<span id=carrier_name><%=qryStruct.carrier_name%></span><INPUT type=hidden name=qry__carrier_name size="10"></font>
		   <font class="tooltxt"  title="点击打开条件选择菜单" ><b>时间</b>：<span id=gather_month><%=qryStruct.gather_month%></span><INPUT id=__gind971_50 type=hidden value="<%=qryStruct.gather_month%>" name="qry__gather_month" monthOnly="true" max="<%=qryStruct.date_e%>" min="200601" size="10"></font>
		</td>
		<td width="250">
		<input type="button" id="button_outputExcel" onclick="javascript:export_table_content('all')" value="全部导出">
		<input type="button" id="button_outputExcel" onclick="javascript:download_file()" value="文件下载">
		</td>
	</tr>
</table>

<div id=menucontent style="position:absolute; width:300; height:1px;right:180;top:15;padding-top:10;overflow:hidden;"> 
<table class="popwidow" cellpadding="0" cellspacing="5">
	<tr>
		<td class="poptitle" style="width:80px">运营商<INPUT type=hidden name=qry__carrier_id></td>
		<td class="poptitle" style="width:80px">时间</td>
		<td class="poptitle" style="width:40px">操作</td>
	</tr>
	<tr>		
		
		<td valign="top"><%=PageConditionUtil.getCarrierDesc(request,qryStruct.carrier_id,"carrier_id","carrier_name","'20','30'")%></td>
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
		<td class="toolbg">高价值阀值：<BIBM:TagSelectList listID="OPP003" listName="qry__opp_valued_value"  focusID="<%=qryStruct.opp_valued_value%>" />&nbsp;近3月通话次数权重：<input type="text" name="qry__avg_3_call_times"  style="width:50px;" value=""/> 近3月次均通话时长权重：<input type="text" name="qry__avg_3_avg_call_dura"  style="width:50px;" /> 近3月交往圈客户数权重：<input type="text" name="qry__avg_3_sphere_num"  style="width:50px;"/> &nbsp;高端客户数权重：<input type="text" name="qry__avg_3_ct_vip_unum"  style="width:50px;" />&nbsp;<input type="button"  value="确定" ID="button_submit" onclick="doQuery()"></td>
	</tr>
	<tr>
		<td class="toolbg">展现记录级：<BIBM:TagSelectList listID="OPP002" listName="qry__opp_rownum"  focusID="<%=qryStruct.opp_rownum%>" />&nbsp;近3月通话时长权重：<input type="text" name="qry__avg_3_call_dura"  style="width:50px;" /> &nbsp;&nbsp;近3月主被叫系数权重：<input type="text" name="qry__avg_3_zbjxs"  style="width:50px;" /> &nbsp;&nbsp;&nbsp;稳定性价值系数权重：<input type="text" name="qry__stable_value"  style="width:50px;"/>高价值客户数权重：<input type="text" name="qry__avg_3_ct_hign_value_unum"  style="width:50px;"/>&nbsp;<input type="button" id="button_outputExcelMin" onclick="javascript:export_table_content('cur')" value="导出"></td>
	</tr>
	
</table>
<div id="errordiv">

</div>
<table width='100%' border='0' cellpadding='0' cellspacing='0' valign="top">
<tr>
<td align="center">
<div id="oppparamdiv" style="width: 100%; margin-top: 2px; overflow: auto; height:400px;">
               <table style="width: 100%" border="0" bgcolor="#CFCFCF" cellpadding="1" cellspacing="1"
                                       style="margin:0;">          
                  <tr class="celtitle FixedTitleRow">
                  	<td align="center"  bgcolor="E3E3E3" width="9%">手机号码</td>   
                    <td align="center"  bgcolor="E3E3E3" width="9%">运营商</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">价值评估值 </td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">近3月通话次数估值</td>                    
                    <td align="center"  bgcolor="E3E3E3" width="9%">近3月通话时长估值</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">近3月次均时长估值</td>                    
                    <td align="center"  bgcolor="E3E3E3" width="9%">近3月主被叫估值</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">近3月交往圈估值</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">稳定性估值</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">高端客户数估值</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">高价值客户数估值</td>
                  </tr>   
                  
                	  <%
                	  if(value==null||value.length <=0){
                		  out.println("<tr class=\"celdata\" onmouseover=\"this.className='mouseM'\" onmouseout=\"this.className='celdata'\">");
                		  out.println("<td align=\"center\" colspan=\"11\">当前条件没有匹配的竞争对手高价值用户</td>");                		  
                		  out.println("</tr>");
                	  }else{
                	  for(int i=0;value!=null&&i<value.length;i++){
                	  %>
                	   <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                	  <td align="center"><%=value[i][0]%></td>          
                      <td align="center" ><%=qryStruct.carrier_name%></td>
                      <td align="center"><%=FormatUtil.formatStr(value[i][10],2,false)%></td> 
                      <td align="center" ><%=FormatUtil.formatStr(value[i][2],2,false)%></td>
                      <td align="center"><%=FormatUtil.formatStr(value[i][3],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][4],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][5],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][6],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][7],2,false)%></td>
                      <td align="center"><%=FormatUtil.formatStr(value[i][8],2,false)%></td>   
                      <td align="center"><%=FormatUtil.formatStr(value[i][9],2,false)%></td>
                      
                      </tr>   
                	  <%                		  
                	  }}
                	  %>
                	 
                 
                	
                 
                
                </table>
                </div>

</td>
</tr>
</table>
  <table cellspacing="0" cellpaddingx="0" style="width: 100%;">

		<tr>
		<td><%=CommTool.getEditorHtml("OppValue","0")%></td>
	</tr>
</table>

</FORM>

</body>

</html>






