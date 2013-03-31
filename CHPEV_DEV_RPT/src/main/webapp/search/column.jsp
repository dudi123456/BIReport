<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-1-27
  Time: 14:55:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="com.ailk.bi.common.app.StringB" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.ailk.bi.common.dbtools.WebDBUtil" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    String msuId = StringB.NulltoBlank(request.getParameter("msu_id"));
    String sql = "SELECT DAY_ID, SUM(T.CUR_VALUE), SUM(T.LAST_VALUE)\n" +
            "  FROM UI_LEADER_KPI_INFO_DATA_D T\n" +
            " WHERE T.MSU_ID = '" + msuId + "'\n" +
            "   AND T.DAY_ID <= " + new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + "\n" +
            "   AND T.DAY_ID >= " + new SimpleDateFormat("yyyyMMdd").format(cal.getTime()) + "\n" +
            " GROUP BY DAY_ID";
    String[][] list = WebDBUtil.execQryArray(sql, "");
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>即时搜索</title>
    <link href="<%=request.getContextPath()%>/css/other/bimain.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/css/other/css.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/js/chart/Common.css" rel="stylesheet" type="text/css">
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" class="tableSty2">
    <tr>
        <th>日期</th>
        <th>当日</th>
        <th>本月累计</th>
    </tr>
    <%
        if (list == null || list.length == 0) {
            out.println("<tr><td class=\"leftdata\" colspan=\"3\">没有报表相关数据！</td>");
        } else {
            for (int i = 0; i < list.length; i++) {
                String[] list1 = list[i];
    %>
    <tr>
        <td class="leftdata"><%=list1[0]%>
        </td>
        <td><%=list1[1]%>
        </td>
        <td><%=list1[2]%>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>