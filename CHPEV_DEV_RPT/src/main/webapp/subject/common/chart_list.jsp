<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@page import="com.ailk.bi.base.util.WebKeys" %>
<%@page import="com.ailk.bi.base.table.PubInfoChartDefTable" %>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct" %>
<%@page import="com.ailk.bi.pages.PagesInfoStruct" %>
<%@page import="com.ailk.bi.pages.WebPageTool" %>
<%
    if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
        return;
    //报表列表
    PubInfoChartDefTable[] chartDef = (PubInfoChartDefTable[]) session.getAttribute(WebKeys.ATTR_REPORT_TABLES);
    //查询条件
    ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
    if (qryStruct == null) {
        qryStruct = new ReportQryStruct();
    }
%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/base/commonHtml.jsp"%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
    <SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
    <title>图形定义列表</title>
    <BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
    <%=WebPageTool.pageScript("frmEdit", "ChartList.rptdo")%>
</head>
<body class="main-body" onLoad="selfDisp();">
<form NAME="frmEdit" ID="frmEdit" ACTION="ChartList.rptdo" method="post">

    <%
    	int iLen = 0;
    	if(chartDef!=null && chartDef.length>0){
    		iLen = chartDef.length;
    	}
        PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, iLen, 15);
        if (pageInfo != null) {
            out.print(WebPageTool.pageHidden(pageInfo));
        }
    %>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">

	<tr>
		<td>
        <div class="toptag">
            <Tag:Bar />
        </div>
		</td>
	</tr>
        <!--条件区展示 start-->
        <tr>
            <td>
                <table width="100%" height="100%" border="0" cellpadding="0"
                       cellspacing="0" class="squareB">
                    <tr>
                        <td><img src="../biimages/square_corner_1.gif" width="5"
                                 height="5"></td>
                        <td background="../biimages/square_line_1.gif"></td>
                        <td><img src="../biimages/square_corner_2.gif" width="5"
                                 height="5"></td>
                    </tr>
                    <tr>
                        <td background="../biimages/square_line_2.gif"></td>
                        <td width="100%" height="100%" valign="top">
                            <table width="100%" border="0">
                                <tr>
                                    <td width="10%">图形编码：</td>
                                    <td><input type="text" size="10" name="qry__rpt_id"></td>
                                    <td width="10%">图形名称：</td>
                                    <td><input type="text" size="15" name="qry__rpt_name"></td>
                                    <td width="30%">
                                        <input type="button" value="查询" class="button" onclick="javascript:fnSubmit('list');">
                                        <input type="button" value="添加新的" class="button2" onclick="javascript:fnSubmit('new');">
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td background="../biimages/square_line_3.gif"></td>
                    </tr>
                    <tr>
                        <td height="6"><img src="../biimages/square_corner_3.gif"
                                            width="5" height="5"></td>
                        <td background="../biimages/square_line_4.gif"></td>
                        <td><img src="../biimages/square_corner_4.gif" width="5"
                                 height="5"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="right"><%=WebPageTool.pagePolit(pageInfo)%>
            </td>
        </tr>
        <tr>
            <td class="tab-side2">
                <TABLE width="100%" border="0" cellpadding="0" cellspacing="1"
                       class="table-bg">
                    <tr align="center">
                        <td class="tab-title" width="10%">图形编码</td>
                        <td class="tab-title" width="10%">图形归属</td>
                        <td class="tab-title" width="10%">图形类型</td>
                        <!-- td class="tab-title" >图形属性</td -->
                        <td class="tab-title" width="10%">操作</td>
                    </tr>
                    <%
                        if (chartDef == null || chartDef.length == 0) {
                    %>
                    <tr class="table-white-bg">
                        <td colspan="5" align="center">该条件下没有符合要求的数据</td>
                    </tr>
                    <%
                    } else {
                        //for(int i=0;i<chartDef.length;i++){
                        for (int i = 0; i < pageInfo.iLinesPerPage && (1 + i + pageInfo.absRowNoCurPage()) <= pageInfo.iLines; i++) {
                            if (i % 2 == 1) {
                    %>
                    <tr class="table-tr">
                        <td width="10%"><%=chartDef[i + pageInfo.absRowNoCurPage()].chart_id%>
                        </td>
                        <td width="10%"><%=chartDef[i + pageInfo.absRowNoCurPage()].chart_belong%>
                        </td>
                        <td width="10%"><%=chartDef[i + pageInfo.absRowNoCurPage()].chart_type_desc%>
                        </td>
                        <!--td title="<%//=chartDef[i + pageInfo.absRowNoCurPage()].chart_attribute%>" ><%//=chartDef[i + pageInfo.absRowNoCurPage()].chart_attribute%></td-->
                        <td align="center" width="10%"><a
                                href="ChartList.rptdo?optype=edit&chart_id=<%=chartDef[i + pageInfo.absRowNoCurPage()].chart_id%>">修改</a>
                        </td>
                    </tr>
                    <%
                    } else {
                    %>
                    <tr class="table-trb">
                        <td width="10%"><%=chartDef[i + pageInfo.absRowNoCurPage()].chart_id%>
                        </td>
                        <td width="10%"><%=chartDef[i + pageInfo.absRowNoCurPage()].chart_belong%>
                        </td>
                        <td width="10%"><%=chartDef[i + pageInfo.absRowNoCurPage()].chart_type_desc%>
                        </td>
                        <!--td title="<%//=chartDef[i + pageInfo.absRowNoCurPage()].chart_attribute%>"><%//=chartDef[i + pageInfo.absRowNoCurPage()].chart_attribute%></td-->
                        <td align="center" width="10%"><a
                                href="ChartList.rptdo?optype=edit&chart_id=<%=chartDef[i + pageInfo.absRowNoCurPage()].chart_id%>">修改</a>
                        </td>
                    </tr>
                    <%
                                }
                            }
                        }
                    %>
                </TABLE>
            </td>
        </tr>
    </table>
    <INPUT TYPE="hidden" id="optype" name="optype" value=""/></form>
</body>
<script language=javascript>
    function fnSubmit(type) {
        if (type == 'new') {
            if (confirm("您确定要创建一个新图形吗？")) {
                document.frmEdit.optype.value = type;
                document.frmEdit.submit();
            }
        } else {
            document.frmEdit.submit();
        }
    }
    function addNew() {
        document.frmEdit.action = "editLocalReport.rptdo?opType=step1&opSubmit=addnew";
        document.frmEdit.submit();
    }
</script>
</html>