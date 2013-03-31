<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%
String context = request.getContextPath();
%>

<body onLoad="document.form1.submit();">
<form METHOD="POST" name="form1" action="<%=context%>/login/login.rptdo">
<INPUT TYPE="hidden" id="opType" name="opType" value="index"/>
</body>
</form>

</html>
