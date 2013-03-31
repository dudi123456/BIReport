<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@ include file="/base/commonHtml.jsp" %>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/kw.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
</head>  

<body onLoad="buildUnicomTab('sub1')">
	<div class="toptag">
		<Tag:Bar />
	</div>
	<br>
	<div class="clearfix">
	  <div id="tab">
	  </div>
	</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="100%" width="100%" valign="top">
    	<iframe id="iframeListBoard" width="100%" height="100%" scrolling="no" frameborder="0" onload="javascript:{dyniframesize('iframeListBoard');}" ></iframe></td>
  </tr>
</table>
</body>
<!--taber标签切换区开始-->
<script language="JavaScript">
	  //定义Taber标签数组
	  var taberArr=new Array(''
	   ,new Array('sub1','查看日志','UploadMonitorNew.rptdo?optype=view_log','iframeListBoard')
	   ,new Array('sub2','日报上传','UploadMonitorNew.rptdo?optype=up_day','iframeListBoard')
	   //,new Array('sub3','周报上传','UploadMonitorNew.rptdo?optype=up_week','iframeListBoard')
	   ,new Array('sub4','月报上传','UploadMonitorNew.rptdo?optype=up_month','iframeListBoard')
      )
</script>  
<!--/taber标签切换区结束-->
</html>
