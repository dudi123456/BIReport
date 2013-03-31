<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.bulletin.util.*"%>
<%@page import="com.ailk.bi.common.app.*"%>

<%
String putInId = StringB.NulltoBlank(request.getParameter("idStr")).trim();

out.print(BulletinRoleGrp.getSelectUserOrGroup(putInId).trim());

		//System.out.println(strB.toString());

%>