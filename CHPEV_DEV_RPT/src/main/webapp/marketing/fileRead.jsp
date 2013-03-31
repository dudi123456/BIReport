<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.marketing.entity.FileInfo"%>
<%@page import="com.ailk.bi.common.sysconfig.GetSystemConfig"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<script type="text/javascript">
function openFile(obj){
	 var oo = getPath(obj);
	 document.getElementById("fileUrl").value=oo;
}
function getPath(obj)
{
 if(obj)
    {

   if (window.navigator.userAgent.indexOf("MSIE")>=1)
     {
        obj.select();

      return document.selection.createRange().text;
     }

  else if(window.navigator.userAgent.indexOf("Firefox")>=1)
     {
      if(obj.files)
        {
        return obj.files.item(0).getAsDataURL();
        }
      return obj.value;
     }
   return obj.value;
   }
}
</script>
</head>
<body style="font-size: 12px" >
 <form action="fileReadAction.rptdo?optype=fileRead&doType=add" method="post" name="form1">
  <input type="hidden" name="fileUrl" id="fileUrl">
  <input id="fileName" type="file" name="fileName"  onchange="openFile(this)">
  <input type="submit" name="submit"  value="打开文件" class="btn4" >
  <br>
</form>
</body>
</html>