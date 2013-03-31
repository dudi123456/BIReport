<%@ page contentType="text/html;charset=UTF-8" %>
<HTML>
  <HEAD><TITLE>Check多选择页</TITLE>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
</HEAD>
<FRAMESET  frameSpacing=0 frameborder="no" border=0 id="ChannelSelectForm" rows="20%,80%">
  <frame name="queryFrame" id="queryFrame" scrolling="no" noresize src="AdhocMultiCheckQuery.screen">
  <frame name="dispFrame" id="dispFrame" scrolling="auto" noresize src="AdhocMultiCheckDisp.screen">
</FRAMESET><noframes></noframes>