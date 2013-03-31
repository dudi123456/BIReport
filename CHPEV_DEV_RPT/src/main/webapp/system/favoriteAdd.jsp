<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.table.InfoMenuTable" %>
<%@ page import="com.ailk.bi.system.facade.impl.ServiceFacade" %>
 
<%
String res_id = request.getParameter("res_id");

String msg = (String)request.getAttribute("msg");
ServiceFacade service = new ServiceFacade();

InfoMenuTable info = service.getMenuInfo(res_id);
String res_name = info.menu_name;
String res_url = info.menu_url;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/common.js" language="javascript"></script>
<title>添加到收藏夹</title>
<style type="text/css">
body,td {
	font-family: 宋体;
	font-size: 9pt;
}
</style>

<script type="text/javascript" src="common/dtree.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/picker/dprgPicker.js"></SCRIPT>
<script src="<%=request.getContextPath()%>/js/XmlRPC.js" language="javascript"></script>
<script>
var msg = '<%=msg%>';
var res_id = '<%=res_id%>';
var res_name = '<%=res_name%>';
var res_url = '<%=res_url%>';
if(msg!="null") {
	
//	if(msg =="添加收藏成功！") {
		
//	}
	alert(msg);
	window.close();
}

function setFavor(id,name) {
	favorForm.favorite_id.value=id;
}
function doSave() {
	if(trim(favorForm.res_name.value)=="") {
		alert("请输入名称！");
		return false;
	}
	if(favorForm.favorite_id.value=="") {
		alert("请选择文件夹！");
		return false;
	}
	favorForm.res_name.value=trim(favorForm.res_name.value);
	favorForm.submit();
	return true;
}
function openAdd() {

	window.open('folderAdd.jsp?res_id='+res_id+'&res_name='+res_name+'&res_url='+res_url+'&gotoUrl=favoriteAdd.screen',"newFloder","width=350,height=200,top=200, left=400,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=no,resizable=0");
	return false;
}

</script>

 <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
	<script language=javascript>
	    domHover(".btn", "btn3_hover");
        
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
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">



</head>

 
<body class="favorites-bg-" style="background-color:White">
<div id="maincontent">
<div class="alert_box_container" id="alert_box_container" style="top:0%;height:100%;width=100%">
<div class="alert_box flowbox3">
<div class="layerbox">


<form name="favorForm" action="favorite.rptdo?submitType=4" method="post" onsubmit="return false;">
<input type="hidden" name="res_id" value="<%=res_id %>">
<input type="hidden" name="res_url" value="<%=res_url %>">
<input type="hidden" name="favorite_id">


<div class="fv_box">
 <span class="fvinput">
 <input type="text" name="res_name" value="<%=res_name %>" class="txtinput" />
 </span>

<div class="fvtree">
<div class="fvtreebox">
<iframe id="favor_frame" name="favor_frame" width="100%" height="100%" 
			frameborder="0" marginwidth="0" marginheight="0" scrolling="auto"
			src="favorTreeAdd.jsp"></iframe>
</div>
</div>

</div>

<div class="co_btn" id="co_btn" style="top:240px;left:0px;width:711px;">
<input name="save" type="button" class="btn" value="保 存" onclick="doSave()">
<input name="new" type="button" class="btn" value="新 建" onclick="openAdd()">
</div> 

<!-- 
<table width="100%"  border=0 cellpadding="0" cellspacing="0" >
  <tr> 
    <td><img src="../biimages/system/folder_new.png" width="80" height="82"></td>
    <td><br><table border="0" cellspacing="0" cellpadding="0" class="tableSty">
        <tr> 
          <td align="right" >名称：</td>
          <td><input type="text" name="res_name" value="<%=res_name %>" size="50"></td>
        </tr>
        <tr valign="top" > 
          <td align="right" valign="top" width="50">创建到：</td>
          <td>
           <iframe id="favor_frame"
			name="favor_frame" width="80%" height="100%" 
			frameborder="1" marginwidth="0" marginheight="0" scrolling="auto"
			src="favorTreeAdd.jsp"> </iframe>         
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr align="center"> 
    <td colspan="2"><br>
      <br>
      <br>
	  <input name="save" type="image" src="../biimages/system/save.gif" width="75" height="24" border="0" onclick="doSave()">
      <input name="new" type="image" src="../biimages/system/new_folder.gif" border="0" onclick="openAdd()"> 
	</td>
</tr>
</table>
 -->
</form>

</div>
</div>
</div>
</div>
</body>
</html>
