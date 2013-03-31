<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.subject.util.AjaxActionUtil"%>
<%@page import="com.ailk.bi.base.util.*"%>
<%
String oper_type = request.getParameter("oper_type");
//���瀹���′欢
 String code = request.getParameter("pcode");
//������妗����绉�
 String txt_name = request.getParameter("txtname");
//������妗����绉�
 String hid_name = request.getParameter("hdname");
//DIV���绉�
 String div_name = request.getParameter("divname");
 
 String str = "";
 String whereStr =" AND 1=1 ";
 if(code == null || "".equals(code)){
	 if("brand".equals(oper_type)){
		 str = AjaxActionUtil.getDivStr("Q0903",code,div_name,txt_name,hid_name);
	 }else if("county".equals(oper_type)){
		 //whereStr += CommTool.getCountyDataRights(session);
		 str = AjaxActionUtil.getDivStr("Q0905",whereStr,div_name,txt_name,hid_name);
	 }	 
 }else{
	 if("brand".equals(oper_type)){
		 str = AjaxActionUtil.getDivStr("Q0903",code,div_name,txt_name,hid_name);
	 }else if("county".equals(oper_type)){
		 whereStr += " and city_id = '"+code+"'";
		 str = AjaxActionUtil.getDivStr("Q0905",whereStr,div_name,txt_name,hid_name);
	 }	 
 }
	  
 out.println(str);


%>