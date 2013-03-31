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
<%@ page import="com.ailk.bi.common.dbtools.DAOFactory"%>
<%@ page import="com.ailk.bi.system.facade.impl.CommonFacade"%>

<%
	request.setCharacterEncoding("UTF-8");

    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
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
String grp_id = DAOFactory.getCommonFac().getLoginUser(session).group_id;
String cur_user_id = CommonFacade.getLoginId(session);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>文件列表</title>
<link href="<%=rootPath%>/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/js/dojo.js" djConfig="parseOnLoad:true, isDebug:false"></script>
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

		//文件上传。
        function upload()
        {
			var h = "300";
	        var w = "400";
	        var top = (screen.availHeight - h) / 2;
	        var left = (screen.availWidth - w) / 2;
	        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
	        var strUrl = "./upload.jsp";
	        var newsWin = window.open(strUrl, "editRptHead", optstr);
	        if (newsWin != null)
	        {
	            newsWin.focus();
	        }

        }
        function download() {
			var aa = "";
			var obj = document.getElementsByName("file_id");
			if(obj == null || obj == "")
			{
				alert("请选择要下载的文件！");
				return ;
			}
			for(var i=0;i<obj.length;i++){

				if(obj[i].checked)
			   {
				   aa = obj[i].value;
			   }

			}
			/*
			if(aa == "" && fileform.file_id.value){
				aa = fileform.file_id.value;
			}
			*/
            if (aa == "") {
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


        function delUpFile(id,dirId) {
			if(confirm("确认要删除该文件吗？")){
			//	fileform.encoding = "application/x-www-form-urlencoded";
				window.location.href =  "CreateFileDirXML.rptdo?opType=delFile&id=" + id + "&dirId=" + dirId;
				//fileform.action = "CreateFileDirXML.rptdo?opType=delFile&id=" + id + "&dirId=" + dirId;
				//alert(fileform.action);
				//fileform.submit();
			//	return ;
			}

        }

    </script>
</head>
<body>
<form name="fileform" action="" method="post">
<input type="hidden" name="sessionid" value="">
<%=WebPageTool.pageScript("form1","AdhocUserXlsTaskStatus.screen")%>
<%
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 30 );

String init = (String)request.getAttribute("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;
}

String strEnable = "";
//System.out.println("grp_id:" + grp_id);
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="200"><font class="title1">文件信息</font></td>
				<td align="left" class="titlebg2_line">文件所在目录[<font color=red><%=qryStruct.dim3%>及其子目录</font>]</td>
				<td align="right" class="titlebg2_line"><table>
<TD><%=WebPageTool.pagePolit(pageInfo,rootPath)%></TD>
</table>
</td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="3">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
						  <td class="leftdata" width="5%">&nbsp;</td>
                        <TD width="18%">上传日期</td>
                        <TD >文件名</td>
                        <TD width="13%">文件大小（KB）</td>
                        <TD width="13%">上传人</td>
                        <TD width="13%">下载次数</td>
						 <TD width="8%">操作</td>
						</tr>
  <%if(list==null||list.length==0){
	  strEnable = " disabled";
	  %>
  <tr class="celdata" align='center'>
    <td colspan="7"  class="leftdata"><%=qryStruct.dim3%>下没有文件</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
						 <td><input type="radio" name="file_id" value='<%=value[0]%>'/></td>
                        <td><%=value[1]%>
                        </td>
                        <td class="leftdata" title="<%=value[2]%>"><%=value[5]%>
                        </td>
                        <td><%=com.ailk.bi.common.app.Arith.divs(value[3], "1024", 2)%>
                        </td>
                        <td><%=value[4]%>
                        </td>
                        <td><a href="javascript:void(0);" onclick="ZoomOut('<%=value[0]%>');"><%=value[6]%>
                        </a>
                        </td>
							<td><%if(grp_id.equals("1") || value[4].equals(cur_user_id)){%><a href="javascript:;" onclick="delUpFile('<%=value[0]%>','<%=qryStruct.dim1%>');">删除
                        </a><%}%>
                        </td>
						</tr>
						 <%} %>
  <%} %>



					</table>
				</td>
			</tr>

		<tr>
            <td>&nbsp;</td>
        </tr>

        <tr>
            <td align="center">
                <img src="<c:out value="${pageContext.request.contextPath}"/>/images/system/document_load.gif"
                     alt="上传" width="75" height="24" style="cursor:pointer" onClick="upload();">　　
                <img src="<c:out value="${pageContext.request.contextPath}"/>/images/system/download.gif"
                     alt="下载" width="75" height="24" style="cursor:pointer" onclick="download();"></td>
        </tr>

		</table>

			</td>
			</tr>

			</table>
		</form>

</body>
</html>