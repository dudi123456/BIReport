<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@ include file="/base/commonHtml.jsp"%>
</head>
<body>
<form method="post" action="dimDef.rptdo" >
<table style="width: 100%;" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2">
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
</table>
<table width="100%" height="500" >
	<tr>
		<td width="20%" height="100%" valign="top"><iframe id="dim_frame"
		name="dim_frame" width="100%" height="100%" 
		frameborder="0" marginwidth="0" marginheight="0" scrolling="no"
		src="dimTree.jsp"> </iframe></td>
		<td width="80%" height="100%" valign="top"><iframe id="dimlist_frame"
		name="dimlist_frame" width="100%" height="100%" 
		frameborder="0" marginwidth="0" marginheight="0" scrolling="no"
		src="dimDef.rptdo"> </iframe></td>
	</tr>
</table>
</form>
</body>
</html>