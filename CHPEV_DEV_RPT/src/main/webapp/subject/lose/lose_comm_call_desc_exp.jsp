<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%@ page import="com.ailk.bi.report.struct.*"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
response.reset();
//response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//response.setContentType("application/vnd.ms-excel;charset=utf-8");
response.setContentType("application/vnd.ms-excel;charset=UTF-8");
response.setHeader("Content-Disposition","attachment;filename=expUserDtl.xls");   

String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
String[] title = (String[])session.getAttribute("lose_subject_title");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	
	qryStruct = new ReportQryStruct();
}

%>

<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="mimetype" content="application/octet-stream">
<body>
<table>          
                  <tr>
                  <%for (int j=0;j<title.length;j++) { %>
                  	<td align="center"  bgcolor="E3E3E3" width="9%"><%=title[j] %></td>
                  <%} %>  
                  </tr>   
                    <%if(list==null||list.length==0){ %>
  <tr>
    <td colspan="14" nowrap >该条件下没有符合要求的数据</td>
  </tr>
  <%}else{  
	  for(int i=0;i<list.length;i++){
		String[] value = list[i];
  %>  
	<tr>
	<%for(int k=0;k<value.length;k++) { %>
                	  <td><%=value[k]%></td> 
    <%} %>

                      </tr>   
                	  <%                		  
                	  }}
                	  %>
                	 
                 
                	
                 
                
                </table>
               


</body>
</html>