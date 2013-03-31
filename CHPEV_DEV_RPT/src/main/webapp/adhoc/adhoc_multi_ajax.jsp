<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.adhoc.service.impl.*"%>
<%@page import="com.ailk.bi.adhoc.dao.impl.*"%>
<%@page import="com.ailk.bi.adhoc.domain.*"%>
<%
AdhocFacade facade = new AdhocFacade(new AdhocDao());
//
StringBuffer strB = new StringBuffer("");

//��冲腑��ヨ��涓����
String adhoc_id = request.getParameter("adhoc_id");
//灞���ф��璁�
String con_id = request.getParameter("con_id");
//
UiAdhocMultiConFilter[]  multiList = facade.getAdhocMultiSelectCondition(adhoc_id,con_id);
if(multiList == null){
	multiList = new UiAdhocMultiConFilter[0];
}

for(int i=0;i<multiList.length;i++){
	if(strB.toString().length()>0){
		strB.append(":");
	}
	strB.append(multiList[i].getFilter_qry_name()+","+multiList[i].getFilter_con_str());
}
out.print(strB.toString());

%>