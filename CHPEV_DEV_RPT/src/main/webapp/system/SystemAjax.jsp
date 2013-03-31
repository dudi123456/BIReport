<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.system.common.*"%>

<%
String user_id = CommTool.getParameterGB(request,"user_id");
String pwd = CommTool.getParameterGB(request,"pwd");
String encode = ","+CommTool.getEncrypt(pwd).trim()+",";
String strB="";


String pwdStr = LSInfoUser.last3UserPwdStr(user_id);
if(pwdStr.indexOf(encode) >-1){
	strB = "新密码不能和原密码一样，或者包含原密码！";
}

out.print(strB.toString());


%>