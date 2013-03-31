<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page
	import="waf.controller.web.util.WebKeys,waf.controller.web.action.HTMLActionException"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
</head>

<body>
	<div id="maincontent">
		<div id="errorbox" class="errorbox">
			<%
				System.out.println(session.getAttribute(WebKeys.EXPTICON));
				String infoclass = "noticetipnew";
				if (session.getAttribute(WebKeys.EXPTICON) != null
						&& session.getAttribute(WebKeys.EXPTICON).equals(
								HTMLActionException.WARN_PAGE + "")) {
					out.println("<img src=\"" + request.getContextPath() + "/images/common/logo2.png\" />");
					infoclass = "noticetipnew";
				} else if (session.getAttribute(WebKeys.EXPTICON) != null
						&& session.getAttribute(WebKeys.EXPTICON).equals(
								HTMLActionException.ERROR_PAGE + "")) {
					out.println("<img src=\"" + request.getContextPath() + "/images/common/logo2.png\" />");
					infoclass = "errortipnew";
				} else if (session.getAttribute(WebKeys.EXPTICON) != null
						&& (session.getAttribute(WebKeys.EXPTICON).equals(
								HTMLActionException.SUCCESS_PAGE + "")
								|| session.getAttribute(WebKeys.EXPTICON).equals(
										HTMLActionException.PRINT_PAGE + "") || session
								.getAttribute(WebKeys.EXPTICON).equals(
										HTMLActionException.ACCT_PRINT_PAGE + ""))) {
					out.println("<img src=\"" + request.getContextPath() + "/images/common/logo2.png\" />");
					infoclass = "righttipnew";
				} else {
					out.println("<img src=\"" + request.getContextPath() + "/images/common/logo2.png\" />");
					infoclass = "errortipnew";
				}
				if (session.getAttribute(WebKeys.EXPTINFO) == null) {
					out.println("<div class=\"errorwarp\"><span class=\""+infoclass+"\"><span>后台操作报错,请检查程序配置逻辑!</span></span></div>");
				} else {
					out.println("<div class=\"errorwarp\"><span class=\""+infoclass+"\"><span>"+session.getAttribute(WebKeys.EXPTINFO)+"</span></span></div>");
				}
			%>
			<%
				if (session.getAttribute(WebKeys.EXPTINFO) != null) {
					if (session.getAttribute(WebKeys.EXPURL) != null
						&& !"".equals("" + session.getAttribute(WebKeys.EXPURL))) {
						if (!"#".equals(session.getAttribute(WebKeys.EXPURL))) {
			%>
						<input type="reset" name="Submit2" class="btn errorback"
						<%if (session.getAttribute(com.ailk.bi.base.util.WebKeys.ATTR_ANYFLAG) != null
							&& "2".equals("" + session.getAttribute(com.ailk.bi.base.util.WebKeys.ATTR_ANYFLAG))) {%>
							value="下 载" onClick="javascript:window.location='<%="" + session.getAttribute(WebKeys.EXPURL)%>'"
						<%} else {%>
							value="返 回" onClick="javascript:window.location='<%="" + session.getAttribute(WebKeys.EXPURL)%>'"
						<%}%>>
			<%
						}
					} else if (session.getAttribute(com.ailk.bi.base.util.WebKeys.ATTR_ANYFLAG) != null
						&& "1".equals("" + session.getAttribute(com.ailk.bi.base.util.WebKeys.ATTR_ANYFLAG))) {
			%>
						<input type="reset" name="Submit22" class="btn errorback" value="返 回" onClick="javascript:window.close();">
			<%		} else {%>
						<input type="reset" name="Submit22" class="btn errorback" value="返 回" onClick="javascript:history.back();">
			<%
					}
				} else {
			%>
					<input type="reset" name="Submit22" class="btn errorback" value="返 回" onClick="javascript:history.back();">
			<%	}%>

		</div>

		<%
			//清空session内的告警信息
			session.removeAttribute(WebKeys.EXPURL);
			session.removeAttribute(WebKeys.EXPTINFO);
			session.removeAttribute(WebKeys.EXPTICON);
			session.removeAttribute(com.ailk.bi.base.util.WebKeys.ATTR_ANYFLAG);
		%>
	</div>
    <script type="text/javascript">
        var clientWidth = $(window).width();
        var clientHeight = $(window).height();
        $("#msgbox").css("left", clientWidth / 2 - 270 + "px");
        $("#msgbox").css("top", clientHeight / 2 - 151 + "px");
	</script>
</body>
<script type="text/javascript">
<!--
	function keySpace() {
		if (event.keyCode == 32)
			document.img_btn.onclick();
	}
	document.onkeypress = keySpace
//-->
</script>
</html>

