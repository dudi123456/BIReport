<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.base.util.*"%>

<%
String strB = "";
String type = CommTool.getParameterGB(request , "type");
if(type == null || "".equals(type)){
	type = "1";
}

if("1".equals(type)){
	//从套餐表中取出名称(内存中已经有了，直接拿)
	strB = "测试套餐";
}else if("dept".equals(type)){
	strB = "测试部门";
}else if("region".equals(type)){
	strB = "测试区域";
}
out.print(strB.toString());
%>