<%@ page contentType="text/html; charset=UTF-8"%>

<html>
<head>
<title>报表管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenu.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=JavaScript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<%
String showTitle = "";
String type = request.getParameter("type");
if("edit".equals(type)){
	showTitle = "报表定制";
}else if("manager".equals(type)){
	showTitle = "报表管理";
}else if("self_task".equals(type)){
	showTitle = "我的任务";
}
%>
<script>
<%if("edit".equals(type)){%>
  var t;
  t=outlookbar.addtitle('报表定制');
  //outlookbar.additem('新增报表',t,'../report/addLocalRpt.screen');
  outlookbar.additem('新增报表',t,'../report/editLocalReport.rptdo?opType=step1&opSubmit=addLocal');
 /* outlookbar.additem('批量增加',t,'../report/batchAddLocalRpt.screen');
  outlookbar.additem('私有报表维护',t,'../report/ReportListView.rptdo?opType=listSelfRpt');
  t=outlookbar.addtitle('报表发布');
  outlookbar.additem('报表发布',t,'../report/customRptDispense.screen');
  t=outlookbar.addtitle('指标管理');
  outlookbar.additem('用户指标维护',t,'../report/rptCustomMsuMaintainFrame.screen');
  t=outlookbar.addtitle('MSTR条件定制');
  outlookbar.additem('条件定制',t,'../mstr/mstrMain.screen');
  outlookbar.additem('提示管理',t,'../mstr/mstrPrmptAdmin.rptdo?opType=listMstrPrmpt');
  outlookbar.additem('测试报表1',t,'../report/SubjectCommTable.rptdo?table_id=test_table_004&first=Y&table_height=320');
  outlookbar.additem('测试报表2',t,'../report/LockReport.screen');
  outlookbar.additem('测试报表3',t,'../report/SubjectCommTable.rptdo?table_id=test_fzing_001&first=Y&table_height=320gather_mon=200904');
  outlookbar.additem('测试报表3',t,'../report/ReportView.rptdo?rpt_id=RPTU0118');
  outlookbar.additem('测试报表1007',t,'../report/LockHeadReport.rptdo?rpt_id=table_1007');
  outlookbar.additem('测试报表1007',t,'../report/LockHeadReport.rptdo?rpt_id=table_1008');
  outlookbar.additem('测试报表1007',t,'../report/LockHeadReport.rptdo?rpt_id=table_1009');*/

<%}else if("manager".equals(type)){%>
  var t;
  t=outlookbar.addtitle('报表管理');
  outlookbar.additem('报表维护',t,'../report/ReportListView.rptdo?opType=listLocalRpt');
/*  outlookbar.additem('表样认证任务',t,'../report/ReportListView.rptdo?opType=listAttstationRpt');
  t=outlookbar.addtitle('流程管理');
  outlookbar.additem('审核流程维护',t,'../report/rptProcess.rptdo?opType=list');
  outlookbar.additem('审核流程发布',t,'../report/rptProcessDispense.screen');
  t=outlookbar.addtitle('打印管理');
  outlookbar.additem('报表打印信息',t,'../report/rptPrint.rptdo?opType=view'); */
<%}else if("self_task".equals(type)){%>
  var t;
  t=outlookbar.addtitle('我的任务');
  outlookbar.additem('表样认证任务',t,'../report/ReportListView.rptdo?opType=listAttstationRpt');
  outlookbar.additem('数据审核任务',t,'../report/rptProcess.rptdo?opType=self_task');   
<%}%>
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="96%"  height="100%" border="0" align="center" cellpadding="0" cellspacing="0" id=mnuList>
  <tr> 
    <td align=middle" bgcolor="DFEFFF" height="1"> </td>
  </tr>
  <tr> 
    <td align=middle id=outLookBarShow style="HEIGHT: 100%" vAlign=top name="outLookBarShow"> 
    <SCRIPT>
	locatefold('<%=showTitle%>')
	outlookbar.show()	
	</SCRIPT>  
 	</td>
  </tr>
</table>
</body>
</html>
