<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.adhoc.util.AdhocConstant" %>
<%@ page import="com.ailk.bi.adhoc.service.impl.*" %>
<%@ page import="com.ailk.bi.adhoc.dao.impl.*" %>
<%@ page import="com.ailk.bi.common.app.StringB" %>
<%@ page import="com.ailk.bi.system.facade.impl.CommonFacade" %>

<%
String adhoc = request.getParameter(AdhocConstant.ADHOC_ROOT);
if(adhoc == null || "".equals(adhoc)){
	adhoc = AdhocConstant.ADHOC_ROOT_DEFAULT_VALUE;
}
String fav_flag = StringB.NulltoBlank((String)session.getAttribute(AdhocConstant.ADHOC_FAV_FLAG));
AdhocTreeFacade facade = new AdhocTreeFacade(new AdhocTreeDao());
String roleId = CommonFacade.getUserAdhocRole(session);
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=JavaScript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
//-->
</script>
</head>
<body>
<div id="treebox" style="width:160px">
  <%
  //条件定制
  out.println(facade.getConditionHtmlTree(adhoc,roleId));
  //结果定制
  out.println(facade.getDimHtmlTree(adhoc,roleId));
  //指标定制
  out.println(facade.getMsuHtmlTree(adhoc,roleId));
  //收藏夹
  if (fav_flag.length()==0){
    out.println(AdhocHelper.getFavoriteTreeHtml(adhoc,session));
  }
  %>
</div>

<div id="treetoggle" style="right:0">
</div>

<script type="text/javascript">
        function layout1()
        {
            var clientHeight = document.documentElement.clientHeight;
            var leftHeight = clientHeight + 70
            $("#treebox,#treetoggle").css("height", leftHeight + "px");
            $("#treetoggle a").css("top", leftHeight / 2 - 50 + "px");
            var treenodnum = $("#treebox").children(".childtreenode").length;
            if (treenodnum > 1)
            {
                $("div.childnodebox").css("height", clientHeight - treenodnum * 30 + "px");
            }
        }
        layout1();
        $(".node1 a").click(
        function()
        {
            $(this).parent().next(".childBox").toggle();
            $(this).toggleClass("open");
        });
        $(".childtreenode").click(
        function()
        {
            $("div.childnodebox").hide();
            $(this).next("div.childnodebox").show();
        });
</script>
</body>
</html>
