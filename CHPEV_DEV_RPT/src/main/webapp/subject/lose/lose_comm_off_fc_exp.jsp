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
                  	<td align="center"  bgcolor="E3E3E3" width="9%">用户ID</td>   
                    <td align="center"  bgcolor="E3E3E3" width="9%">电话号码</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">客户名称</td>                    
                    <td align="center"  bgcolor="E3E3E3" width="9%">在网时长</td> 
                  	<td align="center"  bgcolor="E3E3E3" width="9%">主产品</td>   
                    <td align="center"  bgcolor="E3E3E3" width="9%">销售品</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">资费</td>                    
                    <td align="center"  bgcolor="E3E3E3" width="9%">入网时间</td>                   
                  
                  </tr>   
                    <%if(list==null||list.length==0){ %>
  <tr>
    <td colspan="8" nowrap >该条件下没有符合要求的数据</td>
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