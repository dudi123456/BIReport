<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>帮助文档列表</title>
<%
	String rootPath = request.getContextPath();
%>

<link rel="stylesheet" href="<%=rootPath%>/css/ilayout.css"
	type="text/css">

<body>
	<div id="maincontent">
	<div class="alert_box_bg" id="alert_box_bg">
    	<iframe></iframe>
	</div>
	<div class="alert_box_container" id="alert_box_container">
	       <div class="alert_box flowbox1">
		       <div class="layerbox">
			       <div class="img_box">
			       	<img src="<%=rootPath%>/images/common/com_9.gif">
			       </div>
		       		<div class="co_box">
		       			<a class=close href="javascript:;" onclick="javascript:{top.closeBlock();}">
		       				<img src="<%=rootPath%>/images/common/com_8.png"></a>
		      			<ul>

							<li><a href="jfqfHelp.doc"
								target="_blank">1、经分系统欠费数据查询文档</a></li>

				       </ul>
					</div>

				</div>
			</div>
	</div>
</div>
</body>
</html>