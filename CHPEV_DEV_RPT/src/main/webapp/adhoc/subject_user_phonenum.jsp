<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@ page import="com.ailk.bi.common.app.Arith"%>

<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>
<%@ page import="com.ailk.bi.common.dbtools.DAOFactory"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户号码明细</title>
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

function doDownLoad(id,oper,fileName){
	form1.action = "AdhocInfoExport.rptdo?oper_type=doDownload&id=" + id + "&auth=" + oper + "&fileName=" + fileName;
	form1.tartget="_blank";
    form1.submit();

}

function doAgain(id,machine,rcnt){
	 if (rcnt>0)
	 {
		  if(confirm("您确定要重新生成吗？")){

			var bar = baseXmlSubmit.callAction("../adhoc/adhoc_doJobAgain.jsp?id="+id + "&machine=" + machine);
			bar=bar.replace(/^\s+|\n+$/g,'');
			//
			//alert(bar);
		   document.getElementById("infospan" + id).innerHTML = "<font color='red'>" + bar + "</font>";

		 }
	 }else{
		 alert("系统正在计算你的记录集，不能生成，请等待!");
	 }
	
}

function deleteJob(id){

	 if(confirm("您确定要删除吗？")){
		
		form1.action = "AdhocInfoExport.rptdo?oper_type=deleteXls&id=" + id;
		form1.submit();

	 }
}

function qryTaskQueue(id,machine){
	    var returnValue;
	var acptsite;
   	var time = new Date();
	  var h = "650";
        var w = "600";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;

   window.showModalDialog("AdhocInfoExport.rptdo?oper_type=qryJobQueue&id="+id+"&machine=" + machine + "&time=" +time,"任务队列","dialogWidth:" + w + "px; dialogHeight:" + h + "px; dialogLeft:" + left + "px; dialogTop:" + top + "px; status:no; directories:yes;scrollbars:yes;Resizable=no;help:no");

}

function doDownLoadFile(id,rootPath,machine){
	form1.action = "http://172.19.31." + machine + ":9090" +rootPath + "/adhocDownLoad/" + id;
	
	form1.tartget="_blank";
    form1.submit();

}

function doQrySearchTask(){
	form1.action = "AdhocInfoExport.rptdo?oper_type=qrySubjectTaskSts";
    form1.submit();

}


</script>
<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}

%>

<link href="<%=rootPath%>/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />

<SCRIPT language=javascript src="<%=rootPath%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=rootPath%>/js/date/scw.js"></script>
<script language=javascript src="<%=rootPath%>/js/date/scwM.js"></script>
<SCRIPT language=javascript src="<%=rootPath%>/js/dojo.js"></SCRIPT>
<script type="text/JavaScript" src="<%=rootPath%>/js/wait.js"></script>

<SCRIPT language=javascript src="<%=rootPath%>/js/kw.js"></SCRIPT>
<script type="text/javascript" src="<%=rootPath%>/js/XmlRPC.js"></script>

<script type="text/javascript">
function export_table_content(){

	window.open("../adhoc/download_subject.jsp","");
}

</script>
<body>
<form name="form1" action="subjectDtl.rptdo" method="post">
<input type="hidden" name="sessionid" value="">
<input type="hidden" name="op_time" value="<%=qryStruct.dim1%>">
<input type="hidden" name="msu_fld" value="<%=qryStruct.dim2%>">
<input type="hidden" name="table_id" value="<%=qryStruct.dim3%>">

<%=WebPageTool.pageScript("form1","SubjectUserNumDtl.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 30 );

String init = (String)request.getAttribute("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 


%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">明细号码</font></td>
				<td align="right" class="titlebg2_line"><table><td><input type="button" id="button_outputExcel" value="导出EXCEL" onclick="javascript:export_table_content()"></td>
<TD><%=WebPageTool.pagePolit(pageInfo,rootPath)%></TD>
</table>
</td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata"  width="30%">电话号码</td>
							<td class="leftdata">次数</td>
						</tr>
  <%if(list==null||list.length==0){ %>
  <tr class="celdata" align='center'>
    <td  class="leftdata" colspan=2>该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
	
  %>  

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=value[0]%></td>
							<td class="leftdata"><%=value[1]%></td>
						</tr>
						 <%} %>
  <%} %>
  
						
						
					</table>
				</td>
			</tr>
		</table>

			</td>
			</tr>
			</table>
		</form>		

</body>
</html>