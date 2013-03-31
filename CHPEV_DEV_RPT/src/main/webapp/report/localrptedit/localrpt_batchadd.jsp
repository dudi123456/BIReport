<%@page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>批量新增报表</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
</head>

<body background="../biimages/xtgl-pic3.jpg" class="welcome-xtgl-bg">
<FORM name="frmEdit" action="rptBatchLoad.rptdo" METHOD="POST" enctype="multipart/form-data" target="_self">
<table width="100%" border="0">
  <tr>
    <td colspan="2" class="welcome-title">批量新增表报</td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="welcome-title-underline">
        <tr>
          <td><img src="../biimages/size.gif" width="1" height="1"></td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td width="50%" align="center">EXCEL文件：<input type="file" name="batch_file_data" id="batch_file_data" /></td>
    <td height="89"><INPUT TYPE="BUTTON" class="button"
										onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
										NAME="BUTTON" style="cursor:hand;" VALUE="导入"
										onclick="_fnSubmit()" />	</td>
  </tr>
  <tr>
    <td colspan="2">批量导入请参考模版：<a href="../report/images/batch.xls" class="bule">>>>下载模版</a></td>
  </tr>
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