<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.util.List"%>
    <%@page import="com.ailk.bi.marketing.entity.FileInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<script LANGUAGE="JScript">

</script>
</head>
<body style="font-size: 12px" >
 <form action="fileUploadAction.rptdo?optype=modeFileUpload&doType=add&modeType=mode" method="post" enctype="multipart/form-data" name="form1">
  <input type="file" name="file">
  <input type="submit" name="Submit"  value="上传" class="btn4" >
  <br>
  <%
  	List<FileInfo> list = (List<FileInfo>) session.getAttribute("modeFiles");
  %>
	 <div class="list_content">
   <table >
     <tr width="100%">
       <th width="8%" align="center"> 营销案例选 择 </th>
       <th width="12%" align="center">文件编码</th>
       <th width="12%" align="center">文件名称</th>
       <th width="10%" align="center">文件大小 </th>
       <th width="10%" align="center">操作</th>
     </tr>
<%
 	if(null!=list){
       		for(int i = 0 ;i<list.size();i++){
       			if(i%2!=0){
       				%>
       				<tr class="jg">
       				<%
       			}else{
       			%>
       			 <tr><%} %>
          <td align="center">附件<%=i+1 %></td>
          <td align="center"><%=list.get(i).getFileCode()%></td>
      	  <td align="center"><%=list.get(i).getFileName()%></td>
          <td align="center"><%=list.get(i).getFileSize()%></td>
          <td align="center"><a style= "cursor:hand" href="fileUploadAction.rptdo?modeType=mode&optype=modeFileUpload&doType=delete&fileCode=<%=list.get(i).getFileCode()%>">删除</a></td>
       </tr>	<%
       		}
       	}%>
     </table>
   </div>
</form>
</body>
</html>