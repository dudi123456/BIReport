<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>批量新增报表</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
</head>
<%
String[] result = (String[])session.getAttribute(WebKeys.ATTR_REPORT_BATCH_IMPORT);
int iSuccess = 0;
int iFail = 0;
for(int i=0;result!=null&&i<result.length;i++){
	if(StringTool.checkEmptyString(result[i])){
		iSuccess++;
	}else{
		iFail++;
	}
}
%>
<body background="../biimages/xtgl-pic3.jpg" class="welcome-xtgl-bg">
<FORM name="frmEdit" action="rptBatchLoad.rptdo" METHOD="POST" enctype="multipart/form-data" target="_self">
<table width="100%" border="0">
  <tr>
    <td colspan="2" class="welcome-title">批量新增表报结果</td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="welcome-title-underline">
        <tr>
          <td><img src="../biimages/size.gif" width="1" height="1"></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td width="20%" align="right">本次批量导入数量为：</td>
    <td><%if(result!=null){out.print(result.length);} %> 张；其中成功导入 <%=iSuccess%> 张；失败 <%=iFail%> 张。</td>
  </tr>
  <%if(iFail>0){ %>
  <tr>
    <td align="right">本次批量导入失败的结果如下：</td>
    <td></td>
  </tr>
  <%for(int i=0;result!=null&&i<result.length;i++){ %>
  <tr>
  	<td></td>
    <td>
    <%
    	if(!StringTool.checkEmptyString(result[i])){
    		out.print(result[i]);
    	}
    %>
    </td>
  </tr>
  <%}%>
  <%} %>
</table>
</FORM>
</body>
<script language=javascript>
function _fnSubmit(){
  if(document.all.batch_file_data.value==""){
    alert("请选择需要导入的Excel文件！");
    return;
  }
  document.frmEdit.submit();
}
</script>
</html>