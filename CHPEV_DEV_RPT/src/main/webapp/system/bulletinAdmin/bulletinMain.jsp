<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>深度运营支撑平台</title>
<%
String rootPath = request.getContextPath();
%>

</head>
<body>
<div id="nrkp">
      <div class="mi jj">
        <a href="bulletinAdmin.rptdo?opt_type=navadd" target="mainF">
        <div class="a" onClick="sho(1)" id="t1"><span>公告添加</span></div></a>
        <a href="bulletinAdmin.rptdo?opt_type=invalid" target="mainF">
        <div class="b" onClick="sho(2)" id="t2"><span>过期公告维护</span></div></a>
		<a href="bulletinAdmin.rptdo?opt_type=bAdmin" target="mainF">
        <div class="b" onClick="sho(3)" id="t3"><span>公告管理</span></div></a>
     </div>
  <div class="bia" style="overflow:auto;">
    <iframe id="headlogin" name="mainF" marginwidth="0"  marginheight="0" src="bulletinAdmin.rptdo?opt_type=navadd" frameborder="0" width="100%" scrolling="No" height="100%" onload="this.height=this.contentWindow.document.body.scrollHeight"></iframe>
  </div>
</div>
<script type="text/javascript">
function sho(str)
{
	var str1="t"+str;
	document.getElementById("t1").className="b";
	document.getElementById("t2").className="b";
	document.getElementById("t3").className="b";
    document.getElementById(str1).className="a";
}</script>
</body>
</head>