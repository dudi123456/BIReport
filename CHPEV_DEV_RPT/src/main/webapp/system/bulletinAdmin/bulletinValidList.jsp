<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>

<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page
	import="com.ailk.bi.pages.PagesInfoStruct,com.ailk.bi.pages.WebPageTool"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>公告列表</title>
<%
	String rootPath = request.getContextPath();
	//列表数据
	String[][] list = (String[][]) session
			.getAttribute("BULLETIN_BOARD_LIST");
	//查询条件
	if (list == null) {
		list = new String[0][0];
	}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct) session
			.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if (qryStruct == null) {
		qryStruct = new ReportQryStruct();
	}
%>
<link rel="stylesheet" href="<%=rootPath%>/css/ilayout.css"
	type="text/css">
<script src="<%=rootPath%>/js/jquery.min.js"></script>
<script src="<%=rootPath%>/js/jquery.bi.js"></script>

</script>
<script type="text/javascript"
	src="<%=rootPath%>/js/prototype.lite.js"></script>
<script type="text/javascript">
	function openBullInfo(id) {
		var url = "bulletinAdmin.rptdo?opt_type=openshow&id=" + id;
		window
				.open(url,'','height=500, width=600,top=100, left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=0,	location=0, status=0')
}
</script>
<body>
<form name="form1" action="bulletinAdmin.rptdo?opt_type=bshow"
						method="post">
<div class="alert_box_bg" id="alert_box_bg">
    	<iframe></iframe>
</div>
<div class="alert_box_container" id="alert_box_container">
       <div class="alert_box flowbox1">
	       <div class="layerbox">
		       <div class="img_box">
		       	<img src="<%=rootPath%>/images/common/com_7.png">
		       </div>
	       		<div class="co_box" style="height:100%;">
	       			<a class=close href="javascript:;" onclick="javascript:{top.closeBlock();}">
	       				<img src="<%=rootPath%>/images/common/com_8.png"></a>
	      			<ul>

						<%=WebPageTool.pageScript("form1", "bulletinListShow.screen")%>
						<%
							PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request,
									list.length, 6);
							String init = request.getParameter("init");
							if ("true".equals(init)) {
								pageInfo.iCurPage = 0;
							}
						%>
						<%=WebPageTool.pageHidden(pageInfo)%>
						<%
							if (list == null || list.length == 0) {
						%>
						该条件下没有符合要求的数据
						<%
							} else {
						%>

						<%
							for (int i = 0; i < pageInfo.iLinesPerPage
										&& (i + pageInfo.absRowNoCurPage()) < pageInfo.iLines; i++) {
									String[] value = list[i + pageInfo.absRowNoCurPage()];
						%>
			       <li><%=value[2]%> <a href="javascript:;" onclick="javascript:openBullInfo('<%=value[4]%>');" class="blue"><%=value[0]%></a></li>
			       	<%
							}
						%>
						<%
							}
						%>


			       </ul>
				<div style="position:absolute; bottom: 25px;"><span class="date"><%=WebPageTool.pagePolit(pageInfo)%></span></div>
				</div>

			</div>
		</div>
</div>

 </form>
</body>
</html>
