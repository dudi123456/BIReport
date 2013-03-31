<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="com.ailk.bi.leader.struct.LeaderKpiInfoStruct" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>即时搜索</title>
    <link href="<%=request.getContextPath()%>/css/other/newmain.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/css/other/bimain.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/css/other/css.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/js/chart/Common.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/globe.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/DataGrid.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script type="text/javascript">
        var hashMap = {
            set: function(key, value) {
                this[key.toString()] = value;
            },
            get: function(key) {
                return this[key.toString()];
            }
        }
        <%
        String defaultMsuId = "";
        LeaderKpiInfoStruct[] kpi = (LeaderKpiInfoStruct[]) request.getAttribute("list");
        for (int i = 0; kpi != null && i < kpi.length; i++) {
            if (i == 0) defaultMsuId = kpi[i].getMsu_id();
            out.println("hashMap.set('"+kpi[i].getMsu_id()+"','"+kpi[i].getMsu_name()+","+kpi[i].getMsu_desc()+"');");
        }
        %>

        function doPost(id) {
            if (hashMap.get(id)) {
                var values = hashMap.get(id).split(",");
                $("#explain").html("<strong>" + values[0] + "： </strong><br>" + values[1]);
                $("#key_msu_desc").html(values[0]+" 指标详情:");
                $("iframe").each(function(index) {
                    switch (index) {
                        case 0:
                            this.src = "SubjectCommChart.screen?chart_id=QryChar001&msu_id=" + id;
                            break;
                        case 1:
                            this.src = "../search/column.jsp?msu_id=" + id;
                            break;
                        case 2:
                            this.src = "SubjectCommChart.screen?chart_id=QryChar002&msu_id=" + id;
                            break;
                        case 3:
                            this.src = "../search/chart.jsp?msu_id=" + id;
                            break;
                        default:break;
                    }
                });
            }
        }

        function menuChangerShort(groupID, ID, tabNum) {
            for (var i = 1; i <= tabNum; i++) {
                document.getElementById("menu_" + groupID + "_" + i).className = "tab_short";
            }
            document.getElementById("menu_" + groupID + "_" + ID).className = "tab_act_short";
        }

        function hiddenData(contentID, tabNum) {
            for (var i = 1; i <= tabNum; i++) {
                document.getElementById("content_" + i).style.display = "none";
            }
            document.getElementById("content_" + contentID).style.display = "block";
        }
    </script>
</head>
<body onload="doPost('<%=defaultMsuId%>');" >
<table style="width: 100%;" cellspacing="0" cellpadding="0">
    <tr>
        <td width="3">&nbsp;</td>
        <td>
            <a id="menu_1_1" class="tab_act_short" href="javascript:void(0)"
               onclick="menuChangerShort(1,1,2);hiddenData('1',2);">指标类</a><a id="menu_1_2" class="tab_short"
                                                                              href="javascript:void(0)"
                                                                              onclick="menuChangerShort(1,2,2);hiddenData('2',2);">报表类</a>
        </td>
    </tr>
    <tr>
        <td colspan="2" style="background-color:#caddeb;height:2px;"></td>
    </tr>
</table>
<table style="width:100%;display:block;" id="content_1" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="30%" align="center" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="5"></td>
                </tr>
            </table>
            <table width="99%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="5"><img src="../biimages/taber/cor_tleft.gif" width="5" height="26"></td>
                    <td valign="bottom" background="../biimages/taber/cor_tbg.gif">
                        <table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td><img src="../biimages/system/iocn_indicators.gif" width="15" height="16"
                                         hspace="10">
                                </td>
                                <td valign="bottom"><strong>指标列表</strong></td>
                            </tr>
                        </table>
                    </td>
                    <td width="5"><img src="../biimages/taber/cor_tright.gif" width="5" height="26"></td>
                </tr>
                <tr>
                    <td background="../biimages/taber/cor_mleft.gif"></td>
                    <td align="center">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="5"></td>
                            </tr>
                    </table>
                    <div id="jysd" style="width: 100%; margin-top: 2px; overflow: auto; height:350px;">
                        <table class="tablelist">
                            <tr class="celtitle FixedTitleRow">
                                <th align="center">指标名称</th>
                                <th align="center">当月累计</th>
                                <th align="center">累计环比</th>
                                <th align="center">波动</th>
                            </tr>


                            <%
                                for (int i = 0; kpi != null && i < kpi.length; i++) {
                            %>
                            <tr onmouseover="this.className='highlight'" onmouseout="this.className=''"
                                style="cursor:pointer"
                                onclick="doPost('<%=kpi[i].getMsu_id()%>');">
                                <td class="leftlist"><%=kpi[i].getMsu_name()%>（<%=kpi[i].getUnit()%>）
                                </td>
                                <td><%=kpi[i].getAgg_value()%>
                                </td>
                                <td><%=kpi[i].getLast_agg_value()%>%
                                </td>
                                <td>
                                    <%
                                        if (Double.parseDouble(kpi[i].last_per.substring(0, kpi[i].last_per.length() - 1)) > 0) {
                                    %>
                                    <img src="../biimages/sign_qushi_up.gif" width="11" height="10"></td>
                                <%
                                    }
                                    if (Double.parseDouble(kpi[i].last_per.substring(0, kpi[i].last_per.length() - 1)) == 0) {
                                %>
                                <img src="../biimages/sign_qushi_ping.gif" width="11" height="10">
                                <%
                                    }
                                    if (Double.parseDouble(kpi[i].last_per.substring(0, kpi[i].last_per.length() - 1)) < 0) {
                                %>
                                <img src="../biimages/sign_qushi_down.gif" width="11" height="10">
                            </tr>
                            <%
                                    }
                                }
                            %>

                        </table>
                        </div>
                    </td>
                    <td background="../biimages/taber/cor_mright.gif"></td>
                </tr>
                <tr>
                    <td><img src="../biimages/taber/cor_bleft.gif" width="5" height="5"></td>
                    <td background="../biimages/taber/cor_bbg.gif"></td>
                    <td><img src="../biimages/taber/cor_bright.gif" width="5" height="5"></td>
                </tr>
            </table>

            <table width="99%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="5"><img src="../biimages/taber/cor_tleft.gif" width="5" height="26"></td>
                    <td valign="bottom" background="../biimages/taber/cor_tbg.gif">
                        <table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td><img src="../biimages/system/iocn_indicators.gif" width="15" height="16"
                                         hspace="10"></td>
                                <td valign="bottom"><strong>指标解释</strong></td>
                            </tr>
                        </table>
                    </td>
                    <td width="5"><img src="../biimages/taber/cor_tright.gif" width="5" height="26"></td>
                </tr>

                <tr>
                    <td background="../biimages/taber/cor_mleft.gif"></td>
                    <td align="center">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="5"></td>
                            </tr>
                        </table>
                        <table class="tablelist">
                            <tr>
                                <td id="explain" class="leftlist"></td>
                            </tr>
                        </table>
                    </td>
                    <td background="../biimages/taber/cor_mright.gif"></td>
                </tr>


                <tr>
                    <td height="5"><img src="../biimages/taber/cor_bleft.gif" width="5" height="5"></td>
                    <td background="../biimages/taber/cor_bbg.gif"></td>
                    <td><img src="../biimages/taber/cor_bright.gif" width="5" height="5"></td>
                </tr>


            </table>
        </td>
        <td width="75%" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <span id="key_msu_desc" class="title1">指标详情</span>
                    </td>
                </tr>
                <tr>
                    <td valign="top" class="kuang">
                        <table width="100%">
                            <tr>
                                <td width="90%" valign="top" id="column">
                                    <iframe width="100%" height="250" frameborder="0" scrolling="no" src=""></iframe>
                                </td>

                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                 <td valign="top">
                     <iframe width="100%" height="250" frameborder="0" scrolling="auto" src=""></iframe>
                  </td>
                 </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>

            </table>
        </td>
    </tr>
</table>
<table style="width:100%;display:none;" id="content_2" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td width="1%">&nbsp;</td>
        <td width="97%" align="center" valign="top">
            <table border="0" cellpadding="0" cellspacing="0" class="datalist" style="width: 100%">
                <tr class="celtitle FixedTitleRow">
                    <td>报表名称</td>
                    <td>统计周期</td>
                    <td>报表路径</td>
                    <td>查阅</td>

                </tr>
                <%
                    String[][] list1 = (String[][]) request.getAttribute("list1");
                    if (list1 == null || list1.length == 0) {
                        out.println("<tr><td class=\"leftdata\" colspan=\"3\">没有报表相关数据！</td>");
                    } else {
                        for (int i = 0; i < list1.length; i++) {
                            String[] list = list1[i];
                %>


                <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                    <td class="leftdata"><%=list[0]%>
                    </td>
                    <td class="leftdata"><%=list[2]%>
                    </td>
                    <td class="leftdata"><%=list[3]%>
                    </td>
                    <td><a href="<%=request.getContextPath() + list[1]%>" target="_blank"><img
                            src="../biimages/system/search.gif"> </a></td>
                </tr>


                <%
                        }
                    }
                %>
            </table>
        </td>
    </tr>
</table>

</body>
</html>
