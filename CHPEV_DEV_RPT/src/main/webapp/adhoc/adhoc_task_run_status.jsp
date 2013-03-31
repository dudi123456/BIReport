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
<title>指标解释</title>
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
	/*
	form1.action = "http://172.19.31." + machine + ":9090" +rootPath + "/adhocDownLoad/" + id;
	
	form1.tartget="_blank";
	            form1.encoding = "multipart/form-data";
		   // fileform.encoding = "application/x-www-form-urlencoded";
    form1.action = rootPath + "/filemgrdownload.rptdo?opType=adhoc&fileName=" + id;
         
    form1.submit();
*/
	//  form1.encoding = "multipart/form-data";
      form1.action = rootPath + "/adhocDownLoadFile.rptdo?adhocType=1&id=" + id;
	//  form1.target="_blank";
      form1.submit();

}


function doQrySearchTask(){
	form1.action = "AdhocInfoExport.rptdo?oper_type=qryTaskSts";
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

<body>
<form name="form1" action="AdhocInfoExport.rptdo?oper_type=qryTaskSts" method="post">
<input type="hidden" name="sessionid" value="">
<%=WebPageTool.pageScript("form1","AdhocUserXlsTaskStatus.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 30 );

String init = (String)request.getAttribute("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 

String grp_id = DAOFactory.getCommonFac().getLoginUser(session).group_id;
//System.out.println("grp_id:" + grp_id);
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<table style="width: 99%;" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td class="toolbg"><%if(grp_id.equals("1")){%>用户ID:<INPUT type="text" name="in_oper_no" id="in_oper_no" value="<%=qryStruct.dim3%>"><%}%>起始日期:
            <input type="text" size="10" name="startDate" id="startDate" value="<%=qryStruct.dim1%>" readonly onClick="scwShow(this,this);" class="input-text" onFocus="switchClass(this)" size="15" 
									onBlur="switchClass(this)">结束日期:
            <input type="text" size="10" name="endDate" id="endDate" value="<%=qryStruct.dim2%>" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)"  size="15">
			<input type="button" id="button_search" value="查询" onclick="doQrySearchTask()"><input type="reset" id="button_reset" value="重置" ></td>
		<td class="toolbg" align="right">
		</td>
	</tr>
</table>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">任务运行情况</font></td>
				<td align="right" class="titlebg2_line"><table>
<TD><%=WebPageTool.pagePolit(pageInfo,rootPath)%></TD>
</table>
</td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata" width="15%">任务名称</td>
							<td  width="18%">运行建议</td>
							<td  width="13%">添加时间</td>		
							<td  width="13%">执行时间</td>
							<td  width="13%">结束时间</td>
							<td width="10%">状态</td>
							<td>操作</td>
						</tr>
  <%if(list==null||list.length==0){ %>
  <tr class="celdata" align='center'>
    <td colspan="7"  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
		int flag = Integer.parseInt(value[4]);
		String strCnt = StringB.NulltoBlank(value[12]);
		String curRunCnt = StringB.NulltoBlank(value[13]);
		

		if (strCnt.length()==0){
			strCnt="0";
		}
		if (curRunCnt.length()==0){
			curRunCnt="0";
		}

		double dblCnt = Double.parseDouble(strCnt);
		double dblCurRunCnt = Double.parseDouble(curRunCnt);
		

		String strAdvice = "";
		
		String stats = "未运行";
		switch(flag){
			case 0:
				stats = "未运行";
				break;
			case 1:
				String strRate = "";
				if (dblCnt>0){
					strRate = "<font color=red>[进度:" + NullProcFactory.transNullToFixedRate(Arith.divPer(dblCurRunCnt,dblCnt, 2), "100%") + "]</font>";
				}
				stats = "运行中" + strRate;
				break;
			case 2:
				stats = "运行完毕";
				if (dblCnt<10000){
					strAdvice = "直接导出即可,不必生成任务";
				}
				break;
			case 3:
				stats = "<font color=red>运行失败</font>";
				break;
			case 4:
				stats = "用户取消";
				break;
		}
		
  %>  

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=value[1]%></td>
							<td class="leftdata" title="数据小于1万条[数据量:<%=strCnt%>],导出时,不必选全部"><%=strAdvice%></td>
							<td class="leftdata"><%=value[3]%></td>
							<td class="leftdata"><%=value[10]%></td>
							<td class="leftdata"><%=value[11]%></td>
							<td class="leftdata"><%=stats%></td>
							<td class="leftdata"><span id="infospan<%=value[0]%>" name="infospan<%=value[0]%>"><%if (flag==2){%><a href="javascript:;" onclick="doDownLoadFile('<%=value[0]%>','<%=rootPath%>','<%=value[9]%>')">下载</a><%}else if(flag==3){%><%=value[7]%><%}%><%if(grp_id.equals("1")&&(flag==0 || flag==2)){%>&nbsp;&nbsp;<a href="javascript:;" onclick="doAgain('<%=value[0]%>','<%=value[9]%>','<%=value[12]%>')">重新生成</a><%}%>&nbsp;&nbsp;<%if (flag==2){%><a href="javascript:;" onclick="deleteJob('<%=value[0]%>')">删除</a>&nbsp;&nbsp;<%}if (flag==0){%><a href="javascript:;" onclick="qryTaskQueue('<%=value[0]%>','<%=value[9]%>');">查看任务队列</a><%}%></span></td>
							
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