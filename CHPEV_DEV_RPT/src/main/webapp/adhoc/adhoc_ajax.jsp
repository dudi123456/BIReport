<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.adhoc.service.impl.*"%>
<%@page import="com.ailk.bi.adhoc.dao.impl.*"%>
<%@page import="com.ailk.bi.adhoc.util.*"%>
<%@page import="com.ailk.bi.adhoc.domain.*"%>
<%
AdhocFacade facade = new AdhocFacade(new AdhocDao());
//
StringBuffer strB = new StringBuffer("");


//即席查询业务
String adhoc_id = request.getParameter("adhoc_id");
UiAdhocInfoDefTable  adhocInfo = facade.getAdhocInfo(adhoc_id);

//属性标记
String group_tag = request.getParameter("group_tag");
UiAdhocGroupMetaTable  groupInfo = facade.getAdhocGroupMetaInfo(group_tag);
//path
UiAdhocGroupMetaTable[] groupPath = facade.getAdhocGroupPathInfo(groupInfo.getGroup_id());
//
//String msu_tag = "tableDL";
//
//String dim_tag = "tableWD";

UiAdhocFavoriteDefTable  favoriteInfo = (UiAdhocFavoriteDefTable)session.getAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
%>

<%
strB.append("<div class=\"toptag\">您所在位置：即席查询&nbsp;&gt;&gt;&nbsp;"+adhocInfo.getAdhoc_name());

if(favoriteInfo!=null){
	strB.append("&nbsp;&gt;&gt;&nbsp;<font color=\"blue\">收藏夹{"+favoriteInfo.getFavorite_name()+"}</font>");	
}

if("C".equals(groupInfo.getGroup_belong().toUpperCase())){
	strB.append("&nbsp;&gt;&gt;&nbsp;条件定制");	
}else if("D".equals(groupInfo.getGroup_belong().toUpperCase())){
	strB.append("&nbsp;&gt;&gt;&nbsp;维度定制");		
}else if("M".equals(groupInfo.getGroup_belong().toUpperCase())){
	strB.append("&nbsp;&gt;&gt;&nbsp;度量定制");		
}

for(int i=0;groupPath!=null&&i<groupPath.length;i++){
	if(i == groupPath.length-1 ){
		strB.append("&nbsp;&gt;&gt; <font color=\"red\">"+groupPath[i].getGroup_name()+"</font>");
	}else{
		strB.append("&nbsp;&gt;&gt;&nbsp;"+groupPath[i].getGroup_name());
	}
		
}

strB.append("<span><a href=\"javascript:;\"   class=\"icon dhelp\"  onclick=\"open_metaExplain('" + adhoc_id + "')\" >指标说明</a></span>");
strB.append("</div>");

/*

strB.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'><tr>");
strB.append("<td width='5' background='../biimages/home/nav_bg.gif'><img src='../biimages/home/nav_corner.gif'></td>");
strB.append("<td background='../biimages/home/nav_bg.gif'>您所在位置：");
strB.append("&nbsp;即席查询");
strB.append("&gt;&gt;&nbsp;"+adhocInfo.getAdhoc_name());
if(favoriteInfo!=null){
	strB.append("&gt;&gt;&nbsp;<font color=\"blue\">收藏夹{"+favoriteInfo.getFavorite_name()+"}</font>");	
}

//
if("C".equals(groupInfo.getGroup_belong().toUpperCase())){
	strB.append("&gt;&gt;&nbsp;条件定制");	
}else if("D".equals(groupInfo.getGroup_belong().toUpperCase())){
	strB.append("&gt;&gt;&nbsp;维度定制");		
}else if("M".equals(groupInfo.getGroup_belong().toUpperCase())){
	strB.append("&gt;&gt;&nbsp;度量定制");		
}
//
for(int i=0;groupPath!=null&&i<groupPath.length;i++){
	if(i == groupPath.length-1 ){
		strB.append("&gt;&gt; <font color=\"red\">"+groupPath[i].getGroup_name()+"</font>");
	}else{
		strB.append("&gt;&gt;&nbsp;"+groupPath[i].getGroup_name());
	}
		
}

strB.append("<td width=\"180\" nowrap background=\"../biimages/home/nav_bg.gif\"><a href='javascript:;' onclick=\"open_metaExplain('" + adhoc_id + "')\"><img src=\"../biimages/home/nav_icon3.gif\" width=\"14\" height=\"14\" border=\"0\" hspace=\"10\">指标说明</a></td>");


strB.append("</tr></table>");

  */

out.print(strB.toString());


%>