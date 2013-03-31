<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ page import="com.ailk.bi.filemgr.common.ListFile"%>

<%
String[][] rs = ListFile.getFileDirInfo();
if (rs==null){
	rs = new String[0][0];
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title></title>


<link rel="StyleSheet"  href="common/dtree.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script type="text/javascript" src="common/dtree.js"></script>
<script>
function clickNode(nodeid,objNodeid){
}

function openManager() {
			var h = "500";
        var w = "600";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
	var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,resizable=0";
	window.open('../system/dirMain.jsp',"新建文件夹",optstr);

}
</script>
</head>
<body bgcolor="#02449B" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<center>

<table width="99%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="23" background="../images/common/home/submenu_bg_2.gif">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="30" align="center"><img src="../images/common/home/scj.png" width="16" height="16"></td>
          <td>文件目录</td>
        </tr>
      </table>
    </td>
  </tr>
<tr>
	<td height="100%" valign="top" class="leftbg-tree"><div id="Layer1" style="position:absolute; width:100%; height:100%; z-index:1; overflow: auto;">
	<script type="text/javascript">

		d = new dTree('d');

d.add(0,-1,'目录','','test','dirMainFrame');
<%
  for(int i=0;rs != null && i<rs.length;i++) {
	  out.print("d.add("+rs[i][0]+","+rs[i][1]+",'"+rs[i][2]+"','CreateFileDirXML.rptdo?opType=listFile&id=" + rs[i][0] + "&pid=" + rs[i][1] + "','test','dirMainFrame');\n");

}%>
	document.write(d);
//	d.openTo(0, true);

</script>
</div></td>
</tr>

</table>

</center>
</body>
</html>
