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
String[][] listUser = (String[][])request.getAttribute("USER_LOG_DTL");
	//查询条件
	if(listUser == null){
	listUser = new String[0][0];
}

String[][] listDept = (String[][])request.getAttribute("DEPT_LOG_DTL");
	if(listDept == null){
	listDept = new String[0][0];
}

	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}

String strChartXml_User = (String)request.getAttribute("USER_LOG_DTL_CHART");
String strChartXml_Dept = (String)request.getAttribute("DEPT_LOG_DTL_CHART");
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
<form name="form1" action="userLogStat.rptdo?opType=visitsort" method="post">
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
			<input type="submit" id="button_search" value="查询" onclick="checkInfo()"><input type="button" id="button_reset" value="重置" onclick="resetValue()"></td>
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
				<td class="titlebg2" width="151"><font class="title1">按用户统计</font></td>
				<td align="right" class="titlebg2_line"></td>
			</tr></table>

			<table style="width: 100%">
			<tr>
				<td width="50%" valign="top">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata" width="15%">排名</td>
							<td width="45%">用户名</td>
							<td width="40%">登录次数</td>
						</tr>
						  <%if(listUser==null||listUser.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="3" nowrap  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%
	  for(int i=0;i<listUser.length;i++){
		if (i==10){
			break;
		}

  %>

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=i+1%></td>
							<td><%=listUser[i][0]%></td>
							<td><%=listUser[i][1]%></td>
						</tr>
						 <%} %>
  <%} %>


					</table>
				</td>
<td width="1%"></td>
<td width="49%"><div id="chartdiv1" align="center">FusionCharts.</div>
					<script type="text/javascript">
			   			var chart = new FusionCharts("<%=rootPath%>/swf/Pie3D.swf", "ChartId", "500", "300", "0", "0");
			   			chart.setDataXML("<%=strChartXml_User%>");
			   			chart.render("chartdiv1");
							</script></td>
			</tr>

		</table>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">按部门统计</font></td>
				<td align="right" class="titlebg2_line"></td>
			</tr></table>

			<table style="width: 100%">
			<tr>
				<td width="50%" valign="top">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td class="leftdata" width="15%">排名</td>
							<td width="45%">部门名称</td>
							<td width="40%">登录次数</td>
						</tr>
						  <%if(listDept==null||listDept.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="3" nowrap  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%
	  for(int i=0;i<listDept.length;i++){
		if (i==10){
			break;
		}

  %>

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=i+1%></td>
							<td><%=listDept[i][0]%></td>
							<td><%=listDept[i][1]%></td>
						</tr>
						 <%} %>
  <%} %>


					</table>
				</td>
<td width="1%"></td>
<td width="49%"><div id="chartdiv2" align="center">FusionCharts.</div>
					<script type="text/javascript">
			   			var chart = new FusionCharts("<%=rootPath%>/swf/Pie3D.swf", "ChartId", "500", "300", "0", "0");
			   			chart.setDataXML("<%=strChartXml_Dept%>");
			   			chart.render("chartdiv2");
							</script></td>
			</tr>

		</table>




		</form>

</body>
</html>