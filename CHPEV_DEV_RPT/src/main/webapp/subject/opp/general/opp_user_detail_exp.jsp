<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
response.reset();
//response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//response.setContentType("application/vnd.ms-excel;charset=utf-8");
response.setContentType("application/vnd.ms-excel;charset=UTF-8");
response.setHeader("Content-Disposition","attachment;filename=oppUserDetail.xls");   

String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

%>

<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="mimetype" content="application/octet-stream">
<body>
<table>          
                  <tr>
                  <td>电话号码</td>   
					<td>运营商</td>
                    <td>在网时长</td>
                    <td>通话次数</td>
                    <td>主叫通话次数</td>                    
                    <td>被叫通话次数</td>
                    <td>通话时长</td>                    
                    <td>主叫通话时长</td>                    
                    <td>被叫通话时长</td>           
                  
                  </tr>   
                    <%if(list==null||list.length==0){ %>
  <tr>
    <td colspan="9" nowrap >该条件下没有符合要求的数据</td>
  </tr>
  <%}else{  
	  for(int i=0;i<list.length;i++){
		String[] value = list[i];
		String strTmp = "";
		if (value[2].equals("10")){
			strTmp = "中国";
		}else if(value[2].equals("20")){
			strTmp = "中国移动";
		}else if(value[2].equals("30")){
			strTmp = "中国联通";
		}
  %>  

             
                	   <tr>
                	  <td><%=value[1]%></td>      
					  <td><%=strTmp%></td>      
					   <td><%=value[3]%></td>      
					    <td><%=value[4]%></td>      
						 <td><%=value[5]%></td>      
						  <td><%=value[6]%></td>      
						   <td><%=value[7]%></td>      

                  	  <td><%=value[8]%></td>      
						   <td><%=value[9]%></td>      

                  
                      </tr>   
                	  <%                		  
                	  }}
                	  %>
                	 
                 
                	
                 
                
                </table>
               


</body>
</html>