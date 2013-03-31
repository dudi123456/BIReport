<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>
<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}

String strChartXml = (String)session.getAttribute(WebKeys.ATTR_KpiDateForChart);

%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@ include file="/base/commonHtml.jsp"%>
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<SCRIPT language=javascript src="<%=rootPath%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=rootPath%>/js/date/scw.js"></script>
<script language=javascript src="<%=rootPath%>/js/date/scwM.js"></script>

<script language="JavaScript"
	src="<%=rootPath%>/js/chart/FusionCharts.js"></script>
<script language="JavaScript"
	src="<%=rootPath%>/js/chart/globe.js"></script>
<script language="JavaScript"
	src="<%=rootPath%>/js/chart/DataGrid.js"></script>

<script lanaguage="javascript">
	function viewLogDetail(userid) {
		var h = "300";
		var w = "400";
		var top=(screen.availHeight-h)/2;
		var left=(screen.availWidth-w)/2;
		  var optstr = "height=" + h +",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
		  var strUrl = "userLogStat.rptdo?opType=rptclickdetail&res_id=" + userid;
		  newsWin = window.open(strUrl,"editRptHead",optstr);
		  if(newsWin!=null){
			newsWin.focus();
		  }

//		form1.action="userLogStat.rptdo?opType=rptclickdetail&res_id=" + userid;
//		document.form1.submit();
	}

	function checkInfo()
	{
		var startDate = document.getElementById("qryStartDate").value;
		var endDate = document.getElementById("qryEndDate").value;

		if(startDate!= null && endDate!= null)
			{
				if(startDate > endDate)
					{
						alert("起始日期不能超过结束日期，请重新选择！");
						return false;
					}
			}
	}
	function resetValue()
	{
		var date = new Date();
		var year = date.getFullYear();
		var mon = date.getMonth()+1;
		var day = date.getDate();
		mon = checkDate(mon);
		day = checkDate(day);

		var time = year+""+mon+""+day;
		document.getElementById("qryStartDate").value=time;
		document.getElementById("qryEndDate").value=time;
	}
	function checkDate(i)
	{
		if(i < 10)
			{
				i = "0" + i;
			}
		return i;
	}

 </script>

<body class="main-body">
<form name="form1" action="userLogStat.rptdo?opType=rptclickstatic" method="post">
<%=WebPageTool.pageScript("form1","rptclickstatic.screen")%>
<%
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length,30 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;
}
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<table style="width: 99%;" align="center" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="2">
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
	<tr>
		<td class="toolbg">起始日期:
            <input type="text" size="10" name="qryStartDate" id="qryStartDate" value="<%=qryStruct.dim1%>" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)">结束日期:
            <input type="text" size="10" name="qryEndDate" id="qryEndDate" value="<%=qryStruct.dim2%>" readonly onClick="scwShow(this,this);"
									class="input-text" onFocus="switchClass(this)"
									onBlur="switchClass(this)">
			<input type="submit" id="button_search" value="查询" onclick="checkInfo()" ><input type="button" id="button_reset" value="重置" onclick="resetValue()"></td>
		<td class="toolbg" align="right">
		</td>
	</tr>
</table>
<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">报表点击分析</font></td>
				<td align="right" class="titlebg2_line"><%=WebPageTool.pagePolit(pageInfo,rootPath)%></td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata" width="15%">访问排名</td>
							<td width="25%">热点类型</td>
							<td width="15%">访问次数</td>
							<td width="20%">访问明细</td>

						</tr>
						  <%if(list==null||list.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="6" nowrap  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%
	  for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		if (i==10){
			break;
		}
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=value[0]%></td>
							<td><%=value[2]%></td>
							<td><%=value[3]%></td>
							<td class="centerdata"><a href="javascript:;" onclick="javascript:viewLogDetail('<%=value[1]%>')">报表点击详情</a></td>
						</tr>
						 <%} %>
  <%} %>


					</table>
				</td>
			</tr>
		</table>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">TOP10占比</font></td>
				<td align="right" class="titlebg2_line"></td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2"><div id="chartdiv1" align="center">FusionCharts.</div>
					<script type="text/javascript">
			   			var chart = new FusionCharts("<%=rootPath%>/swf/Pie3D.swf", "ChartId", "500", "300", "0", "0");
			   			chart.setDataXML("<%=strChartXml%>");
			   			chart.render("chartdiv1");
							</script>
				</td>
			</tr>
		</table>
			</td>
			</tr>
			</table>
		</form>

</body>
</html>