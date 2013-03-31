<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@ include file="/base/commonHtml.jsp"%>
</head>
<body>
<form method="post" action="msuDef.rptdo" >
<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2">
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
</table>
<table width="100%" height="550" >
	<tr>
		<td width="20%" height="100%" valign="top"><iframe id="msu_frame"
		name="msu_frame" width="100%" height="100%" 
		frameborder="0" marginwidth="0" marginheight="0" scrolling="no"
		src="msuTree.jsp"> </iframe></td>
		<td width="80%" height="100%" valign="top"><iframe id="msulist_frame"
		name="msulist_frame" width="100%" height="100%" 
		frameborder="0" marginwidth="0" marginheight="0" scrolling="no"
		src="msuDef.rptdo"> </iframe></td>
	</tr>
</table>
</form>
</body>
</html>