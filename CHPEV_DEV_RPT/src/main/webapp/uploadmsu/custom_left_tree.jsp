<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>

<%@ page import="com.ailk.bi.report.util.ReportObjUtil" %>
<html>
<head>
<title>BI</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenu.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<%
  String parent_id=request.getParameter("parent_id");
  String oper_no = CommonFacade.getLoginId(session);
  String user_role = CommTool.getRoleStringByOper(oper_no);
  String loadname = "";
  String[][] dataArr = ReportObjUtil.getCommonRptArr(parent_id);
  if(dataArr!=null && dataArr.length>0){
    loadname = dataArr[0][1];
  }
  out.print(ReportObjUtil.genRptTree(dataArr));
  //out.print(CommTool.getLeftCenterTree(dataArr,parent_id));
%>
<script>
function pageOnLoad(){
  <%if(dataArr!=null && dataArr.length>0){%>
  parent.parent.bodyFrame.location.href="<%=dataArr[0][4]%>";
  <%}%>
}
</script>
</head>

<body onLoad="pageOnLoad()" bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="96%"  height="100%" border="0" align="center" cellpadding="0" cellspacing="0" id=mnuList>
  <tr> 
    <td align=middle" bgcolor="DFEFFF" height="0"></td>
  </tr>
  <tr> 
    <td align=middle id=outLookBarShow style="HEIGHT: 100%" vAlign=top name="outLookBarShow">
    <%if(dataArr==null || dataArr.length==0){ %>
      <center>还没有可用的报表</center>
    <%}else{ %>
	  <SCRIPT>
	  locatefold('<%=loadname%>')
	  outlookbar.show(18)
	  </SCRIPT>
	<%} %>
 	</td>
  </tr>
</table>
</body>
</html>