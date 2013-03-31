<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.common.app.DateUtil"%>
<%@page import="com.ailk.bi.common.dbtools.*"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page import="com.ailk.bi.common.dbtools.WebDBUtil"%>
<%@ page import="com.ailk.bi.common.app.AppException"%>
<%@page import="java.sql.*"%>
<html>
<head>
	<title>查看报表口径文档</title>
</head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<body>
<%
String sql = "";
sql = sql+ "select ";
sql = sql+ "a.msu_id,b.msu_name,b.msu_desc,c.dim_name,c.dim_desc ";
sql = sql+ "from  ";
sql = sql+ "UI_RPT_INFO_DICT a  ";
sql = sql+ "left join UI_PUB_META_MEASURE b on a.msu_id=b.msu_id ";
sql = sql+ "left join UI_PUB_META_DIMENSION c on a.msu_id=c.dim_id ";
sql = sql+ "where a.rpt_id='"+request.getParameter("rpt_id")+"' ";

System.out.println(sql);
int n = 0;
try {

String[][] svces = WebDBUtil.execQryArray(sql, "");

%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
    <!--报表显示 start-->
    <table id="AutoNumber1" width="100%" border="0" cellpadding="0" cellspacing="0">
	  <tr>
		<td colspan="2" class="tab-side2">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#999999">
	    <tr>
	    	<td class="tab-title">名称</td><td class="tab-title">说明</td>
	    </tr>
	    <%
	    for(int i=0;i<svces.length;i++) {
		System.out.println("rs.next is exist");
	    %>
	    <tr class="table-white-bg">
	    	<td><%if (svces[i][0].equals("Y")){out.write(svces[i][1]);}else{out.write(svces[i][3]);} %></td>
	    	<td><%if (svces[i][0].equals("Y")){out.write(svces[i][2]);}else{out.write(svces[i][4]);} %></td>
	    </tr>
	    <%
		}

		} catch (AppException e) {
			e.printStackTrace();
		}
				%>
        </table>
        </td>
      </tr>

      <tr>
        <td id="id2" height="2"></td>
      </tr>
      <tr>
        <td height="30" colspan="2"> </td>
      </tr>

    </table>
    <!--报表显示 end-->
    </td>
  </tr>
  <tr>
    <td>

    </td>
  </tr>
</table>

</body>

</html>