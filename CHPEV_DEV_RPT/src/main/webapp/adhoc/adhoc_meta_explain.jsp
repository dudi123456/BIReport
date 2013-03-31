<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page
	import="com.ailk.bi.pages.PagesInfoStruct,com.ailk.bi.pages.WebPageTool"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>指标解释</title>
<script>
	function doDownLoadFile() {

		form1.action = "adhocDownLoadFile.rptdo?adhocType=2";
		form1.submit();
	}

	function qryDtl() {
		form1.action = "adhocMetaExplain.rptdo";
		form1.submit();
	}
</script>

<%
	String rootPath = request.getContextPath();
	//列表数据
	String[][] list = (String[][]) session
			.getAttribute("VIEW_TREE_LIST");
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

	boolean blnShowDld = false;
	if (qryStruct.dim2.indexOf("ADH") == 0) {
		blnShowDld = true;
	}
%>

<link rel="stylesheet" href="<%=rootPath%>/css/icontent.css"
	type="text/css">

<script src="<%=rootPath%>/js/jquery.min.js"></script>

<script src="<%=rootPath%>/js/jquery.bi.js"></script>
<body>
	<form name="form1" action="adhocMetaExplain.rptdo" method="post">
		<input type="hidden" name="sessionid" value="">
		<%=WebPageTool.pageScript("form1", "qryMetaExplain.screen")%>
		<%
			PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request,
					list.length, 300);

			String init = (String) request.getAttribute("init");
			if ("true".equals(init)) {
				pageInfo.iCurPage = 0;
			}
		%>

		<!--//设置翻页隐藏域-->
		<%=WebPageTool.pageHidden(pageInfo)%>
		<div class="topsearch">
			<span>指标名称: <input type="text" size="50" name="metaName"
				id="metaName" class="txtinput" value="<%=qryStruct.dim1%>">
				<input type="hidden" size="10" name="adhoc_id" id="adhoc_id"
				value="<%=qryStruct.dim2%>"> </span> <span class="btnwrap">
				<input type="button" id="button_search" value="查询" class="btn3"
				onclick="qryDtl()"> <input type="reset" id="button_reset"
				value="重置" class="btn3">
			<%
 				if (blnShowDld) {
 			%>
 			 <input type="button" id="btndown" value="下载word文档"
				onclick="doDownLoadFile()" class="btn4">
			<%
 				}
 			%>
 			<input type="button" id="btnclose" value="关闭"
				onclick="window.close();" class="btn3"> </span>
		</div>

		<!--日指标分析开始-->
		<div class="listbox">
			<div class="listtitle">
				<span class="icon title">指标解释 </span>
				<span class="date"><%=WebPageTool.pagePolit(pageInfo, rootPath)%></span>
			</div>
			<div class="list_content hasbg">
				<table>
					<tr class="tl">
						<td width="35%">指标名称</td>
						<td>指标解释</td>
					</tr>
					<%
						if (list == null || list.length == 0) {
					%>
					<tr class="bgwl" align='center'>
						<td colspan="2">该条件下没有符合要求的数据</td>
					</tr>
					<%
						} else {
					%>

					<%
						String trClass = "bgwl";
							for (int i = 0; i < pageInfo.iLinesPerPage
									&& (i + pageInfo.absRowNoCurPage()) < pageInfo.iLines; i++) {
								String[] value = list[i + pageInfo.absRowNoCurPage()];
								if (i % 2 == 0) {
									trClass = "bgwl";
								} else {
									trClass = "jg";
								}
					%>

					<tr class="<%=trClass%>">
						<td><%=value[1]%></td>
						<td><%=value[2]%></td>


					</tr>
					<%
						}
					%>
					<%
						}
					%>



				</table>
			</div>
	</form>
    <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>

</body>
</html>