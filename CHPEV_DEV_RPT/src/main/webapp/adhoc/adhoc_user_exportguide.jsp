<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ailk.bi.adhoc.util.AdhocConstant"/>

<%

String rootPath = request.getContextPath();
	String adhoc = request.getParameter(AdhocConstant.ADHOC_ROOT);
	if (adhoc == null || "".equals(adhoc)) {
		adhoc = (String) session.getAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
		if (adhoc == null || "".equals(adhoc)) {
			adhoc = AdhocConstant.ADHOC_ROOT_DEFAULT_VALUE;
		}
	}


String rowcnt = (String)session.getAttribute(AdhocConstant.ADHOC_EXPORT_ROWCNT);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=rootPath%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/css/other/tab_css.css" type="text/css">

<title>清单导出</title>
</head>
<body class="main-body">
<!--<form name="tableQryForm" method="post" action="/servlet/adhocFileDownload">-->
<form name="tableQryForm" method="post" action="AdhocInfoExport.rptdo?oper_type=doSaveTask"  onSubmit="return getValue()">


<table style="width: 100%" height="100%" class="kuangContent" cellspacing="0" cellpadding="0">
					<tr>
						<td valign="top" style="padding: 10px;">
						<table style="width: 100%">
							<tr>
								<td><font class="title1">导出Excel</font></td>
							</tr>
							 
<tr>
								<td>
								<hr noshade="noshade" style="height: 1px; color: #caddeb">
								</td>
							</tr>
							<tr>
								 <td class="toolbg">任务名称:
            <input type="text" size="30" name="taskName" id="taskName" value=""><input type="hidden" name="adhoc_id" id="adhoc_id" value="<%=adhoc%>"><input type="hidden" name="row_cnt" id="row_cnt" value="<%=rowcnt%>"></td>
							</tr>
							<tr>
								 <td class="toolbg">任务说明:
            <input type="text" size="30" name="taskDesc" id="taskDesc" value=""></td>
							</tr>
							
							 <tr>
                          <td class="toolbg" align="center"><input name="btnexport" type="submit" class="button" value="确定">&nbsp;<input type="button" name="btn_close" value="关闭" class="button-add" onclick="window.close();"></td>
                        </tr>
						</table>
						</td>
					</tr>
					<tr>
					<td><table>
<TD></TD>
</table>

					</td>
					</tr>
				</table>


</form>     
</body>
</html>
<script>
function isEmpty(s){
  return ((s == null) || (s.length == 0))
}
function isSpace(content2){
  for(i=0;i<content2.length;i++){
    var c=content2.charAt(i);
    if(c!=" ") return false;
  }
  return true;
}

function getValue(){

 if(isEmpty(document.tableQryForm.taskName.value)){
     alert("请输入任务名称！");
     document.tableQryForm.taskName.focus();
     return false;
  }
	//document.btnexport.disabled = true; 
}
</script>