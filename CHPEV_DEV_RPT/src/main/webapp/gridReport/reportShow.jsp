<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%@ page import="com.ailk.bi.sigma.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>

<%
	String path = request.getContextPath();
%>
<style>
<!--
.toptag {
	background: url("../images/unicom/bg_17.png") repeat-x scroll 0 0
		transparent;
	height: 29px;
	line-height: 29px;
	padding: 0 20px;
	position: relative;
}

.toptag span {
	position: absolute;
	right: 20px;
	_margin-top: 6px;
	_margin-right: 10px
}

.toptag span a.cl {
	background-position: 0 -804px;
	padding: 2px 0 0 20px;
	line-height: 14px;
}

.toptag span a.goforum {
	background-position: 0 -99px;
	padding: 2px 0 0 20px;
	line-height: 14px;
}

.toptag span a.dhelp {
	background-position: 0 -960px;
	padding: 2px 0 0 20px;
	line-height: 14px;
	color: Blue; *
	margin-top: 6px;
	_margin-top: 0;
}

.toptag span.hbt {
	right: 220px;
}

.toptag span.hbt a.dhelp { *
	margin-top: 0px;
}

.toptag span.hbt1 { *
	top: -2px;
	_top: -5px
}

.icon {
	background: url("../images/unicom/icon_1.gif") no-repeat scroll 0 0
		transparent;
}

.icon1 {
	background: url("../images/unicom/icon_2.gif") no-repeat scroll 0 0
		transparent;
}

.icon2 {
	background: url("../images/unicom/icon_4.gif") no-repeat scroll 0 0
		transparent;
}

.btn {
	background: url("../images/unicom/btn_1.gif") no-repeat scroll 0 0
		transparent;
	width: 81px;
	height: 39px;
	line-height: 39px;
	border: 0 none;
	font-weight: bold;
}

.btn3 {
	background: url("../images/unicom/btn_4.gif") no-repeat scroll 0 0
		transparent;
	width: 65px;
	height: 24px;
	line-height: 24px;
	border: 0 none;
}

.btn4 {
	background: url("../images/unicom/btn_5.gif") no-repeat scroll 0 0
		transparent;
	width: 85px;
	height: 24px;
	line-height: 24px;
	border: 0 none;
}

.btn_hover {
	background: url("../images/unicom/btn_6.gif") no-repeat scroll 0 0
		transparent;
	width: 81px;
	height: 39px;
	line-height: 39px;
	border: 0 none;
	color: White
}

.btn3_hover {
	background: url("../images/unicom/btn_8.gif") no-repeat scroll 0 0
		transparent;
	width: 65px;
	height: 24px;
	line-height: 24px;
	border: 0 none;
	color: White
}

.btn4_hover {
	background: url("../images/unicom/btn_7.gif") no-repeat scroll 0 0
		transparent;
	width: 85px;
	height: 24px;
	line-height: 24px;
	border: 0 none;
	color: White
}

.listbox {
	padding: 0 20px;
}

.listbox .listtitle {
	height: 34px;
	line-height: 34px;
	position: relative;
	overflow: hidden
}

.listbox .listtitle .title {
	background-position: 0 -236px;
	padding-left: 20px;
	float: left;
	overflow: hidden;
	font-size: 14px;
	font-weight: bold;
	color: #545454
}

.listbox .listtitle .bigtitle {
	padding-left: 28%;
	float: left;
	overflow: hidden;
	font-size: 16px;
	font-weight: bold;
	color: #000;
	font-family: "微软雅黑";
}

.listbox .listtitle .date {
	float: right;
	overflow: hidden;
	color: Gray
}

.listbox .listtitle .time {
	float: left;
	overflow: hidden;
	color: Gray
}

.topsearch {
	background: url("../images/unicom/bg_15.png") repeat-x scroll 0 0
		transparent;
	height: 33px;
	line-height: 33px;
	padding: 0 20px;
	position: relative;
	_padding-top: 4px
}

.topsearch a {
	display: inline-block;
	padding: 8px;
	background-position: 0 -768px;
	font-size: 0;
	position: relative;
	top: 4px;
	left: 4px
}

.topsearch .btnwrap {
	position: absolute;
	right: 20px
}

.topsearch input.sw {
	width: 50px;
}

.tipbox {
	margin-top: 10px;
}

.tipbox .title {
	font-weight: bold;
	font-size: 14px
}

.list_content {
	margin: 10px 5px;
	background-color: White
}

.hasbg {
	background: url("../images/unicom/bg_23.png") repeat-x scroll 0 0
		transparent;
	margin: 0
}

.list_content table {
	border: 1px solid #c7c7c7;
	border-top: 0 none;
	width: 100%
}

.list_content th {
	background: url("../images/unicom/bg_20.png") repeat-x scroll 0 0
		transparent;
	height: 25px;
	line-height: 25px;
	color: Gray;
	font-weight: normal;
	border-right: 1px solid #e6e6e6
}

.hasbg th {
	background-image: none
}

.list_content td {
	height: 30px;
	line-height: 30px;
	padding: 0 3px;
	border-bottom: 1px solid #c6c6c6;
	border-right: 1px solid #c6c6c6;
}

.list_content th.last,.list_content td.last {
	border-right: 0 none;
}

.list_content .jg td {
	background-color: #f5f5f5;
	color: #545454
}

.list_content .bgwl td {
	background-color: #fff;
	color: #545454
}

.list_content .tl td {
	height: 24px;
	line-height: 24px;
	padding: 0 3px;
}
-->
</style>
<!DOCTYPE html>
<html>
	<head>

		<!-- js-add-start -->
		<%
			String context = request.getContextPath();
		%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
		<script type="text/javascript">
        domHover(".btn3", "btn3_hover");

        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}

    </script>
		<!-- js-add-end -->

		<title>清单情况</title>

		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<SCRIPT language="javascript" src="<%=path%>/js/SmartMenuToc.js"></SCRIPT>
		<SCRIPT language="javascript" src="<%=path%>/js/js.js"></SCRIPT>
		<script type="text/javascript" src="<%=path%>/js/chart.js"></script>
		<script type="text/JavaScript" src="<%=path%>/js/date/scw.js"></script>
		<script type="text/JavaScript" src="<%=path%>/js/date/scwM.js"></script>
		<link rel="stylesheet" href="<%=path%>/css/other/css.css" type="text/css">
		<link rel="stylesheet" href="<%=path%>/css/other/tab_css.css"
			type="text/css">

	</head>
	<%
		//int sigmaId = 3;
		int sigmaId = Integer.parseInt((String) request
				.getAttribute("sigmaId"));
		SigmaGridUtil util = new SigmaGridUtil(sigmaId, path);
		util.initConditonInfo();
		//out.print(util.showSigmaScriptLanguage().toString());
		String rpt_id = Integer.toString(sigmaId);
		String rp_bottom = SigmaGridUtil.reportBottom(rpt_id);
	%>
	<body>
		<div id="maincontent">
			<form name="frmConditon" method="post" target="dataShowFrame">
				<div class="toptag">
					<Tag:Bar />
				</div>
				<div class="topsearch">
				<table width="100%" border="0">
					<tr>
						<%
							List<SigmaGridConditionShowBean> list = util
									.getSigmaGridDisplayCondition();
							//System.out.println(list.size());
							for (SigmaGridConditionShowBean bean : list) {
								//StringBuffer sb = list.get(i).getShowHtmlString();
						%>
						<td width="5%" nowrap><%=bean.getShowConName()%>：</td>
						<td nowrap><%=bean.getShowHtmlString()%></td>
						<%
							//out.println(bean.getShowConName()+bean.getShowHtmlString());

							}
						%>
						<td width="20%" colspan="3" nowrap>
							<INPUT TYPE="hidden" NAME="sigmaId" id="sigmaId"
								value="<%=sigmaId%>">
						</td>
						<td colspan="3" width="5%" nowrap>
							<input type="button" name="search" class="btn3"
								onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
								value="查询" onclick="javascript:doQrySearchTask();">
							<input type="button" name="wholeExport" class="btn3"
								onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
								value="CSV导出"
								onclick="javascript:wholeDimExport('<%=sigmaId%>');">
							<input type="button" name="dc" class="btn3"
								onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
								value="导出" onclick="javascript:doDownLoad('<%=sigmaId%>');">
						</td>
					</tr>
				</table>


				</div>
				<%
					list = util.getSigmaGridHiddenCondition();
					//System.out.println(list.size());
					for (SigmaGridConditionShowBean bean : list) {
						//StringBuffer sb = list.get(i).getShowHtmlString();
						out.println(bean.getShowHtmlString());

					}
				%>

			</form>
			<table id="AutoNumber1" width="100%" border="0" cellpadding="0"
				cellspacing="0">
				<TR>
					<TD colspan="2" class="tab-boldtitle"><%=util.getSigmaReportInfo(sigmaId).getReportName()%></TD>
				</TR>
			</TABLE>
			<iframe id="dataShowFrame" name="dataShowFrame" scrolling="no"
				width="100%" height="500" border="0" frameborder="0" marginwidth="0"
				marginheight="0" src=""></iframe>

			<TABLE width="100%">
				<%=rp_bottom%>
			</TABLE>
		</div>
	</body>
</html>

<script type="text/javascript">

function doQrySearchTask(){
	frmConditon.action = "<%=path%>/gridReport/showData.jsp";
	frmConditon.target="dataShowFrame";
    frmConditon.submit();

}

 function doDownLoad(sigmaId)
    {
        var h = "350";
        var w = "900";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "<%=path%>/gridReport/sigmaRptBuild.rptdo?sigmaId=" + sigmaId;
        var newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }
    }

function wholeDimExport(sigmaId){
	//document.all.wholeExport.disabled = true;
	document.frmConditon.target = "_blank";
	document.frmConditon.action = "../servlet/ExportData?oper_type_export=datareport&rpt_id="+sigmaId;
	document.frmConditon.submit();
}

doQrySearchTask();

</script>

