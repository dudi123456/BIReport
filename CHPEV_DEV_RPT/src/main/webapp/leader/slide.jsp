<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-1-14
  Time: 10:25:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	response.setHeader("Cache-Control", "no-stored");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
 <head>
  <title>Slideshow Widget Demo</title>
  <script type="text/javascript"> var djConfig = {isDebug: true}; </script>
  <script type="text/ja/js/dojo.js="<%=request.getContextPath()%>/js/dojo.js" djConfig="parseOnLoad:true, isDebug:true"></script>
  <script language="JavaScript" type="text/javascript">
   dojo.require("dojo.widget.*");
   dojo.require("dojo.widget.SlideShow");
  </script>
  <style type="text/css">
  html, body {
   height: 100%; width: 100%;
   overflow: hidden;
  }
  </style>
 </head>
 <body>
  <img dojoType="SlideShow"
   imgUrls="../biimages/demo_pic1.jpg;../biimages/demo_pic2.jpg;../biimages/demo_pic3.jpg"
   transitionInterval="700"
   delay="4000"
   src="../biimages/demo_pic1.jpg"
   imgWidth="400" imgHeight="300" />
 </body>
</html>