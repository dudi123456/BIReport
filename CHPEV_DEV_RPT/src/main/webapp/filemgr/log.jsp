<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-1-22
  Time: 10:02:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.lang.StringUtils,
                 com.ailk.bi.common.dbtools.WebDBUtil,
                 com.ailk.bi.pages.PagesInfoStruct,
                 com.ailk.bi.pages.WebPageTool" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    String fileId = StringUtils.trimToEmpty(request.getParameter("fileid"));
    StringBuffer sql = new StringBuffer();
    sql.append("select t2.FILE_ORGNAME, t1.FILE_HANDLE_USER, to_char(t1.FILE_HANDLE_DATE, 'yyyy-mm-dd hh24:mi:ss') from UI_SYS_LOG_FILE_STORE t1, UI_SYS_INFO_FILE_STORE t2 where t1.file_id=t2.file_id and t1.FILE_HANDLE_ACTION='download' and t1.file_id=")
       .append(fileId);
    pageContext.setAttribute("downloadfiles", WebDBUtil.execQryArray(sql.toString(), ""));
%>
<html>
<head><title>日志查看</title>
    <style type="text/css">

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
<form action="" name="form1" method="post">
    <table width="900" align="center">
        <tr>
            <td height="15" valign="bottom" align="right">
                <a href="javascript:;" class="chart-zoom" onclick="dlgChart.hide();">关闭</a>
            </td>
        </tr>
        <tr>
            <td>
                <div valign='top' align='left' id='content_table'
                     style=' width: 100%;  overflow: auto; height:100%; cursor: default; '>
                    <table style="width: 100%" class="datalist">
                        <tr class="celtitle FixedTitleRow">
                            <td>文件名</td>
                            <td>下载人</td>
                            <td>下载日期</td>
                        </tr>
                        <%=WebPageTool.pageScript("form1", request.getContextPath() + "/filemgr/log.jsp")%>
                        <%
                            String[][] list = (String[][]) pageContext.getAttribute("downloadfiles");
                            PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list == null ? 0 : list.length, Integer.MAX_VALUE);
                            String init = request.getParameter("init");
                            if ("true".equals(init)) {
                                pageInfo.iCurPage = 0;
                            }
                        %>
                        <!--//设置翻页隐藏域-->
                        <%=WebPageTool.pageHidden(pageInfo)%>
                        <% if (list == null || list.length == 0) { %>
                        <tr>
                            <td colspan="3" nowrap align="center">该条件下没有符合要求的数据</td>
                        </tr>
                        <%
                        } else {
                            for (int i = 0; i < pageInfo.iLinesPerPage && (i + pageInfo.absRowNoCurPage()) < pageInfo.iLines; i++) {
                                String[] value = list[i + pageInfo.absRowNoCurPage()];
                        %>
                        <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                            <td><%=value[0]%></td>
                            <td><%=value[1]%></td>
                            <td><%=value[2]%></td>
                        </tr>
                        <%
                                }
                            }
                        %>
                        <tr>
                            <td colspan="3"><%=WebPageTool.pagePolit(pageInfo)%>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
