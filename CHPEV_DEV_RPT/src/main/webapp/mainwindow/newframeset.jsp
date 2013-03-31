<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String context = request.getContextPath();
%>
<html>
<head>
<title>北京经分系统</title>
	<link rel="stylesheet" type="text/css" href="<%=context%>/jsp/easyui/themes/default/easyui.css">
	<script type="text/javascript" src="<%=context%>/jsp/easyui/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="<%=context%>/jsp/easyui/jquery.easyui.min.js"></script>
</head>
<body class="easyui-layout">
	<div region="north" border="false" style="height:95px;"><iframe name="topFrame" scrolling="no" frameborder="0" marginwidth="0" marginheight="0" src="<%=context%>/mainwindow/top.jsp" style="width:100%;height:100%;"></iframe></div>

	<div region="center"><iframe name="mainFrame" scrolling="auto" frameborder="0" marginwidth="0" marginheight="0" src="<%=context%>/mainwindow/panel_frameset.jsp" style="width:100%;height:100%;"></iframe></div>

	<div region="south" border="false" style="height:20px"><iframe name="bottomFrame" scrolling="no" frameborder="0" marginwidth="0" marginheight="0" src="<%=context%>/mainwindow/bottom.jsp" style="width:100%;height:100%;"></div>

</body>
</html>

