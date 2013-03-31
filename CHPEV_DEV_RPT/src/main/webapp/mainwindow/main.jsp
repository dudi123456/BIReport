<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.WebConstKeys"%>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
<%
InfoOperTable user = (InfoOperTable)session.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
String target = "bi_main1_"+user.system_id;
%>
<HTML>
<HEAD>
<title>中国联通经营分析系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>

<BODY leftmargin="0" topmargin="0">
正在连接到中国联通经营分析系统...<br>
<script language="JavaScript">

function openWindow()
{
   var height = window.screen.height - 55;
   var width = window.screen.width - 10;

   window.open("<%=request.getContextPath()%>/mainwindow/mainwindow_frameset.jsp", "<%=target%>",
			"height=" + height + ",width=" + width + ",left=0,top=0,"
			+ "location=no,menubar=no,resizable=no,toolbar=no,titlebar=yes,status=no");
 }
openWindow();
if (self.name != "<%=target%>") {
	window.opener=null;
	window.open('','_self');
	window.close();
}

</script>

已经连接到用中国联通经营分析系统...<br>
</BODY>
</HTML>
