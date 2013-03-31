<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="java.util.*"%>

<%
String res_id = request.getParameter("res_id");
String res_name = request.getParameter("res_name");
String res_url = request.getParameter("res_url");
String gotoUrl = request.getParameter("gotoUrl");

%>
<html>
<head>
   <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jQuery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jQuery.blockUI.js"></script>

	<script language=javascript>
	    domHover(".btn3", "btn3_hover");

        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}
	</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<title>新建文件夹</title>
<style type="text/css">
body,td {
	font-family: 宋体;
	font-size: 9pt;
}
</style>
<script src="<%=request.getContextPath()%>/js/common.js" language="javascript"></script>
<script type="text/javascript" src="common/dtree.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/picker/dprgPicker.js"></SCRIPT>
<script src="<%=request.getContextPath()%>/js/XmlRPC.js" language="javascript"></script>
<script>

function doSavefolder() {

	if(trim(favorForm.favorite_name.value)=="") {
		alert("请输入文件夹名！");
		return false;
	}
	favorForm.favorite_name.value=trim(favorForm.favorite_name.value);
	favorForm.submit();
	window.close();
	return true;

}


</script>
</head>
<body class="favorites-bg_" style="background-color:White">
<div id="maincontent" >
<center>
<form name="favorForm" action="favorite.rptdo?submitType=5" method="post" target="addFavor" onsubmit="return false;">
<input type="hidden" name="res_id" value="<%=res_id %>">
<input type="hidden" name="res_name" value="<%=res_name %>">
<input type="hidden" name="res_url" value="<%=res_url %>">
<input type="hidden" name="gotoUrl" value="<%=gotoUrl %>">
<br><br>
<table width="100%"  border=0 cellpadding="2" cellspacing="0" >
  <tr>
    <td><img src="../images/system/folder_save.png" width="90" height="60"></td>
    <td><table border="0" cellspacing="0" cellpadding="0" class="tableSty">
        <tr>
          <td align="right" width="20%">文件夹名:</td>
          <td><div class="topsearch" width="10%"><input class="txtinput" type="text" name="favorite_name"></div></td>
        </tr>
        <tr>
          <td><br> </td>
          <td><br>
          <div class="fd_btn">
            <!-- <input class="btn3" name="savefolder" type="image" src="../images/system/save.gif" width="75" height="24" border="0" onclick="doSavefolder()" /> -->
             <input class="btn3" name="savefolder" type="button"  onclick="doSavefolder()" value="保 存"></input>
            </div>
            </td>

        </tr>
      </table></td>
  </tr>

</table>
</form>
</center>
</div>
</body>
</html>
