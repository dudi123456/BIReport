<%@ page contentType="text/html;charset=UTF-8" %>
<HTML>
  <HEAD><TITLE>单选</TITLE>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
</HEAD>
<%
String qry_radio_id = request.getParameter("qry_radio_id"); 
String node_id = request.getParameter("node_id"); 
String en_name = request.getParameter("en_name"); 
%>
<FRAMESET  frameSpacing=0 frameborder="no" border=0 id="ChannelSelectForm" rows="10%,90%">
  <frame name="queryFrame" id="queryFrame" scrolling="NO" noresize src="radioCheckQuery.screen?node_id=<%=node_id%>&en_name=<%=en_name%>">
  <frame name="dispFrame" id="dispFrame" scrolling="yes" noresize src="radioCheckDisp.screen?qry_radio_id=<%=qry_radio_id%>">
</FRAMESET><noframes></noframes>