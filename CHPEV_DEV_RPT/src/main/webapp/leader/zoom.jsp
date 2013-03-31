<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-1-14
  Time: 9:24:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<html>
<head><title>Simple jsp page</title>
    <style type="text/css">
        body {
            font-family: sans-serif;
        }

        .dojoDialog {
            background: #eee;
            border: 1px solid #999;
            -moz-border-radius: 5px;
            padding: 4px;
        }

        form {
            margin-bottom: 0;
        }

        /* group multiple buttons in a row */
        .box {
            display: block;
            text-align: center;
        }

        .box .dojoButton {
            float: left;
            margin-right: 10px;
        }

        .dojoButton .dojoButtonContents {
            font-size: medium;
        }
    </style>
</head>
<body>
<table width="900" align="center">
    <tr>
        <td height="15" valign="bottom" align="right">
            <a href="javascript:;" class="chart-zoom" onclick="dlgChart.hide();">还原</a>
        </td>
    </tr>
    <tr>
        <td>
            <div align='left' id='content_table'
                 style=' width: 100%;  overflow: auto; height:100%; cursor: default; '>
                <table style="width: 100%" class="datalist">
                    <tr class="celtitle FixedTitleRow">
                        <td width="200" align="left">指标名称</td>
                        <td>当日</td>
                        <td>前日</td>
                        <td>环比增长</td>
                        <td>上周当日</td>
                        <td>同期增长</td>
                        <td>排名</td>
                        <td>占有率</td>
                        <td>地区均值</td>
                        <td>上月当日</td>
                        <td>上月同期比</td>
                        <td>日累计值</td>
                        <td>上月日累计</td>
                    </tr>
                    <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                        <td colspan="13" class="leftdata">
                            <a href="javascript:void(0)" onclick="hiddenData('dataTitle1','dataContent1');">
                                <img id="dataTitle1" alt="" src="../biimages/data_show.jpg"
                                     style="margin-right: 5px">用户发展新增类</a></td>
                    </tr>
                    <tbody id="dataContent1" style="display:block">
                    <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                        <td class="leftdata"><a href="javascript:;" class="dataurl">本网新增用户_总数[导航]</a></td>
                        <td>8,026</td>
                        <td>5,015</td>
                        <td>60.04%</td>
                        <td>2,731</td>
                        <td>193.89%</td>
                        <td>1</td>
                        <td>100.00%</td>
                        <td>161</td>
                        <td>2,535</td>
                        <td>216.61%</td>
                        <td>25,869</td>
                        <td>25,212</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </td>
    </tr>
</table>
</body>
</html>