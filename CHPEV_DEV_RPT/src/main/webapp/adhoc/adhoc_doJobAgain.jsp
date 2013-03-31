<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="java.io.*"%>
<%@page import="com.ailk.bi.adhoc.util.AdhocUtil"%>

<%
String id = request.getParameter("id");
String serverIp = request.getParameter("machine");
AdhocUtil.sendInfoToSocket(id,serverIp);
String strOutHtml = "宸插�����绔���崇�����璇锋��";
out.print(strOutHtml);

		//System.out.println(strB.toString());

%>