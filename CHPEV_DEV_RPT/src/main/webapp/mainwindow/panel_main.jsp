<%@ include file="/base/controlHead.jsp" %>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <link rel="stylesheet" href="<%=_base %>/css/icontent.css" type="text/css">
<script src="<%=_base %>/js/jquery.min.js"></script>
<script type="text/javascript">
function goForum(rootpth,res_id){
	//901135,901225
	//var forumUrl = rootpth + "/navToForum.rptdo?res_id=" + res_id;
	var forumUrl = rootpth + "/navToForum.jsp?res_id=" + res_id;
	window.open (forumUrl);

}
</script>

</head>

<body>
    <div id="maincontent">
        <div class="logobox" id="logobox">
            <img src="<%=_base%>/images/common/logo1.png" />
        </div>
    </div>
    <script type="text/javascript">
        var clientHeight = document.documentElement.clientHeight;
        $("#logobox").css("top", clientHeight / 2 - 80 + "px");
</script>
</body>
</html>


