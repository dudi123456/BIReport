<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
                 com.ailk.bi.pages.WebPageTool" %>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@ page import="com.ailk.bi.common.app.Arith"%>

<%@ page import="com.ailk.bi.report.struct.*"%>

<%
	request.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>文件上传</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css"
	type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
    <script type="text/javascript" src="<c:url value="/js/dojo.js"/>"></script>
    <script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
    <script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
	<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/wait.js"></SCRIPT>

    <script type="text/javascript">
        var dlgChart;
        var djConfig = { parseWidgets: false };
        dojo.require("dojo.widget.Dialog");
        dojo.addOnLoad(function (e) {
            dlgChart = dojo.widget.createWidget("Dialog", {bgColor:"white", bgOpacity:"0.5", toggle:"fade",
                style:"display:none", caption:"Create"});
            dojo.body().appendChild(dlgChart.domNode);
        });
        function ZoomOut(id) {
            dlgChart.checkSize();
            dlgChart.setUrl("<c:out value="${pageContext.request.contextPath}"/>/filemgr/log.jsp?fileid=" + id);
            dlgChart.show();
        }
        function upload() {
            if (document.all.filename.value == "") {
                alert("请输入文件名！");
                document.all.filename.focus();
                return;
            }
            fileform.encoding = "multipart/form-data";
            fileform.action = "<c:out value="${pageContext.request.contextPath}"/>/filemgrupload.rptdo";
            fileform.submit();
        }
        function download() {
            if (fileform.file_id.value == "") {
                alert("请选择要下载的文件！");
                return;
            }
            fileform.encoding = "multipart/form-data";
		   // fileform.encoding = "application/x-www-form-urlencoded";
            fileform.action = "<c:out value="${pageContext.request.contextPath}"/>/filemgrdownload.rptdo";
            fileform.submit();
        }
        function ShowSize(files) {
            var fso = new ActiveXObject("Scripting.FileSystemObject");
            var f = fso.GetFile(files);
            if (f.size > 150000000) {
                alert("上传文件应小于150M!");
                files.select();
                document.execCommand("Delete");
                document.all.filename.focus();
            }
        }
        function clr(obj) {
            obj.select();
            document.execCommand("Delete");
        }

function cancelClose(){

		this.close();
	}

function CheckWorkFile()
     {
	   var objName=document.getElementById('icon_name');
       if(objName.value=='')
         {
            alert('请输入文件名称');
			fileform.icon_name.focus();
            return false;
         }

       var obj=document.getElementById('upfile');
       if(obj.value=='')
         {
            alert('请选择要上传的文本文件');
            return false;
         }

        //ShowWait();
		fileform.encoding = "multipart/form-data";
        fileform.action = "<c:out value="${pageContext.request.contextPath}"/>/filemgrupload.rptdo";
        fileform.submit();
//        return true;
     }


    </script>
</head>
<%
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}

%>
<body>
<form name="fileform" enctype="multipart/form-data" action="<c:out value="${pageContext.request.contextPath}"/>/filemgrupload.rptdo" method="post" >
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td colspan="3"><table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
<td width="32"><img src="../images/common/home/feedback-ioc1.gif" width="22" height="14"></td>
<td width="100%" class="left-menu">上传文件【上传到:<%=qryStruct.dim3%>】</td>
<td width="5"><img src="../images/common/home/left-ico2.gif" width="5" height="23"></td>
					</tr>
					<tr>
						<td colspan="3" class="search-bg">
						<table width="100%" border="0" cellpadding="0" cellspacing="1"
							class="choice-table-bg">

							<tr class="table-white-bg">
								<td align="right" class="subject-title" width="35%">文件名称</td>
								<td align="left" class="subject-title" width="65%"><INPUT TYPE="text" NAME="icon_name" id="icon_name" style="width:80%"></td>
							</tr>
							<tr class="table-white-bg">
								<td align="right" class="subject-title" width="35%">文件说明</td>
								<td align="left" class="subject-title" width="65%"><INPUT TYPE="text" NAME="icon_desc" id="icon_desc"  style="width:80%"></td>
							</tr>
							<tr class="table-white-bg">
								<td align="right" class="subject-title" width="35%">文件目录</td>
								<td align="left" class="subject-title" width="65%"><INPUT TYPE="text" NAME="fileDir" readonly id="fileDir"  style="width:80%" value="<%=qryStruct.dim3%>"><INPUT TYPE="hidden" NAME="fileDirId" id="fileDirId"  style="width:80%" value="<%=qryStruct.dim1%>"></td>
							</tr>
							<tr class="table-white-bg">
								<td align="right" class="subject-title" width="35%">选择文件</td>
								<td align="left" class="subject-title" width="65%"><INPUT TYPE="FILE" NAME="upfile" SIZE="16" style="width:80%"></td>
							</tr>
						</table>
						</td></tr>
						<tr align="center">
						<td colspan="3">&nbsp;</td>
						</tr>

						<tr align="center">
						<td colspan="3">
						<input type="button" ID="btnUpload" name = "btnUpload" onclick="return CheckWorkFile()" value="确定上传" onclick="">&nbsp;&nbsp;
						<input type="button" name="Submit4" value="取消" onClick="cancelClose();"></td>
						</tr>
						</table>

</td>
</tr>
</table>
</form>

</body>
</html>
