<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.adhoc.util.AdhocConstant" %>
<%@ page import="com.ailk.bi.adhoc.util.*" %>
<%@ page import="com.ailk.bi.adhoc.service.impl.*" %>
<%@ page import="com.ailk.bi.adhoc.dao.impl.*" %>
<%@ page import="com.ailk.bi.common.app.StringB" %>
<%@ page import="com.ailk.bi.system.facade.impl.CommonFacade" %>


<%
//
String adhoc = request.getParameter(AdhocConstant.ADHOC_ROOT);
if(adhoc == null || "".equals(adhoc)){
	adhoc = AdhocConstant.ADHOC_ROOT_DEFAULT_VALUE;
}
//
String fav_flag = StringB.NulltoBlank((String)session.getAttribute(AdhocConstant.ADHOC_FAV_FLAG));

AdhocTreeFacade facade = new AdhocTreeFacade(new AdhocTreeDao());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
<SCRIPT language=JavaScript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
</head>
<body class="lefttree-body">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <%
  String roleId = CommonFacade.getUserAdhocRole(session);
  //条件
  out.println(facade.getConditionHtmlTree(adhoc,roleId));
  //结果定制
  out.println(facade.getDimHtmlTree(adhoc,roleId));
  System.out.println(facade.getDimHtmlTree(adhoc,roleId));
  //指标定制
  out.println(facade.getMsuHtmlTree(adhoc,roleId));
  System.out.println(facade.getMsuHtmlTree(adhoc,roleId));
  //定制结果
  //out.println(AdhocHelper.getResultTreeHtml(adhoc));
  //收藏夹
  	if (fav_flag.length()==0){
		out.println(AdhocHelper.getFavoriteTreeHtml(adhoc,session));
	}

 // out.println(AdhocHelper.getFavoriteTreeHtml(adhoc,session));
  %>
 
</table>
</body>
</html>
<script language=JavaScript>
	var subMenuArr = new Array(submenu1,submenu2,submenu3);
	function onlydisplay(theSubMenu,theSubMenu1){
		for(i=0;i<subMenuArr.length;i++)
		{
			if(subMenuArr[i]!=theSubMenu){
				subMenuArr[i].style.display="none"	;
			}
			
			if(subMenuArr[i]!=theSubMenu1){
				subMenuArr[i].style.display="none"	;
			}
		}
				
		if(theSubMenu.style.display=="none"){
			theSubMenu.style.display="";
			if(theSubMenu1){
				theSubMenu1.style.display="";
			}
		}else{
			theSubMenu.style.display="none";
			if(theSubMenu1){
				theSubMenu1.style.display="none";
			}
		}
}
</script>