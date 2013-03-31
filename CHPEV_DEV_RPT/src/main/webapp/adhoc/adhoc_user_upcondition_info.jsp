<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>

<%

	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	//切分符
	String split_char = AdhocConstant.ADHOC_SPLIT_CHAR;
	//随机数
	java.util.Random rnd = new java.util.Random(System
			.currentTimeMillis());

	//选择的条件
	if (null == session
			.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_ID_SESSION)) {
		return;
	}
	String con_id = (String) session
			.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_ID_SESSION);

	//条件名称
	String con_name = (String) session
			.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_NAME_SESSION);
	if (null == con_name) {
		con_name = "";
	}
%>
<html>
	<head>
	
	<script language=javascript>
        
        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}
	</script>
	
	
	
		<%@ include file="/base/commonHtml.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content="no-cache">
		<title><%=con_name%>-已上传条件列表</title>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/other/css.css"
			type="text/css">
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
		<SCRIPT language=javascript
			src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
		<SCRIPT language=javascript
			src="<%=request.getContextPath()%>/js/wait.js"></SCRIPT>
		<base target="_self" />
	</head>
	<script language="JavaScript">
<!--
	
	function MM_reloadPage(init) {  //reloads the window if Nav4 resized
	  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
	    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
	  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
	}
	MM_reloadPage(true);

function cancelClose(){
		
		this.close();
	}
	
function CheckWorkFile()
     {
	   var objName=document.getElementById('icon_name');
       if(objName.value=='')
         {
            alert('请输入条件名称');
			mainfrm.icon_name.focus();
            return false;
         }

       var obj=document.getElementById('upfile');
       if(obj.value=='')
         {
            alert('请选择要上传的文本文件');
            return false;
         }
        var stuff=obj.value.match(/^(.*)(\.)(.{1,8})$/)[3]; //这个文件类型正则很有用：）

        if(stuff.toLowerCase()!='txt' && stuff.toLowerCase()!='xls' && stuff.toLowerCase()!='xlsx')
        {
           alert('文件类型不正确，请选择.txt或.xls文件或.xlsx文件');
           return false;
        }
        ShowWait();
		mainfrm.submit();
//        return true;
     }


//删除对应的上传条件
function deleteFixed(id){
	 if(confirm("您确定要删除吗？此操作不可恢复!")){

	tableQryForm.action="AdhocUserUpLoadCon.rptdo?oper_type=delupfile&id="+id;
	//tableQryForm.target="_self";
	tableQryForm.submit();
	}
}

//预览数据
function viewDetailData(id){
var h = "500";
var w = "650";
var top=(screen.availHeight-h)/2;
var left=(screen.availWidth-w)/2;
  var optstr = "height=" + h +",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
  var strUrl = "AdhocUserUpLoadCon.rptdo?oper_type=viewdata&id="+id;
   var openobj = window;

              if(typeof(window.dialogArguments) == 'object')

              {    

                     openobj = window.dialogArguments;                 

              }

              openobj.open(strUrl,"_blank",optstr);

//  newsWin = window.open(strUrl,"editRptHead",optstr);
//  if(newsWin!=null){
//    newsWin.focus();
//  }

	//tableQryForm.action=strUrl;
	//tableQryForm.target="_self";
	//tableQryForm.submit();

}


	//确定提交
	function selectSubmit(){
	  var temp=document.getElementsByName("rdoConId");

	  var flag = 0;
	  for (i=0;i<temp.length;i++){
	  //遍历Radio
		if(temp[i].checked)
		  {
			flag = 1;
			window.returnValue=temp[i].value;
			this.close();
		  }
	  }
	  if (flag==0)
	  {
		  alert('你没有选择条件，请选择');
		  return;
	  }

	}
</script>

	<body onload="closeWaitWin()">
		
			<%
				String[][] objList = (String[][]) request.getAttribute("OBJ_LIST");
				if (objList == null) {
					objList = new String[0][0];
				}

				String handleInfo = (String) request.getAttribute("HANDLE_INFO");

				if (handleInfo == null) {
					handleInfo = "";
				}
			%>

<div id="maincontent">

			<form name="tableQryForm" method="post" action="AdhocUserUpLoadCon.rptdo?oper_type=upfile">

				<div class="formdiv2">
					<table width="95%" border="0" cellpadding="0" cellspacing="0" align="center">
						<tr>
							<td colspan="2">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td width="32">
											<img src="../images/system/feedback-ioc1.gif" width="22"
												height="14">
										</td>
										<td width="100%" class="left-menu_old">
										<div class="fd_title">
											已上传的条件
										</div>
										</td>

									</tr>

								</table>
							</td>
						</tr>
						<tr>
							<td colspan="2" class="search-bg_old">
							<div class="fd_content" style=" font-size: 12px;">
							<div class="widget_zj">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="choice-table-bg_old" style=" font-size: 12px;">
									<tr class="table-gay-bg2_old" >
										<th align="center" class="subject-title_old_old" width="8%">
											选择
										</th>
										<th align="center" class="subject-title_old" width="23%">
											条件名称
										</th>
										<th align="center" class="subject-title_old" width="23%">
											条件说明
										</th>
										<th align="center" class="subject-title_old" width="23%">
											添加日期
										</th>
										<th align="center" class="subject-title_old" width="14%">
											预览数据
										</th>
										<th align="center" class="subject-title_old" width="14%">
											删除
										</th>
									</tr>
									<%
										if (objList == null || objList.length == 0) {
									%>
									<tr class="table-white-bg_old">
										<td colspan="6" nowrap align="center">
											<font color='red'>你还没有上传数据</font>
										</td>
									</tr>
									<%
										} else {
											for (int i = 0; i < objList.length; i++) {
									%>

									<tr class="table-white-bg_old">
										<td width="8%" align="center">
											<INPUT TYPE="radio" NAME="rdoConId"
												VALUE="<%=objList[i][0]%>|<%=objList[i][2]%>">
										</td>
										<td width="23%" align="center"><%=objList[i][2]%></td>


										<td width="23%" align="left">
											<%=objList[i][3]%></td>
										<td width="23%" align="center"><%=objList[i][4]%></td>
										<td width="14%" align="center">
											<a href="#"><img src="../images/system/receipt.gif" border="0"
													onclick="viewDetailData('<%=objList[i][0]%>')">
											</a>
										</td>
										<td width="14%" align="center">
											<input type="button" class="btn3" value="删 除" onclick="deleteFixed('<%=objList[i][0]%>')" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" />
										</td>

									</tr>

									<%
										}
										}
									%>
								</table>
								
								</div></div>
							</td>
						</tr>
						<tr align="center">
							<td colspan="3">
								&nbsp;
							</td>
						</tr>

						<tr align="center">
							<td colspan="3">
								<input type="button" name="Submit" value="确定" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
									onclick="javascript:selectSubmit();">
							</td>
						</tr>
					</table>
				</div>
			</form>

			<form name="mainfrm" method="post"
				action="AdhocUserUpLoadCon.rptdo?oper_type=upfile"
				enctype="multipart/form-data">
				<div class="formdiv2">
					<table width="95%" border="0" cellpadding="0" cellspacing="0" align="center">
						<tr>
							<td colspan="3">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td width="32">
											<img src="../images/system/feedback-ioc1.gif" width="22"
												height="14">
										</td>
										<td width="100%" class="left-menu">
											上传新数据
										</td>
									</tr>
									<tr>
										<td colspan="3" class="search-bg">
										<div class="fd_content">
										<div class="widget_zj">
											<table width="100%" border="0" cellpadding="0"
												cellspacing="1" class="choice-table-bg_old" style=" font-size: 12px;">

													<tr><th align="center" colspan=2>
													上传新数据
												</th></tr>

												<tr class="table-white-bg_old">
												
													<td align="right" class="subject-title_old" width="35%">
														条件名称
													</td>
													<td align="left" class="subject-title_old" width="65%">
														<INPUT TYPE="hidden" NAME="msu_id" id="msu_id"
															value='<%=con_id%>'>
														<INPUT TYPE="text" NAME="icon_name" id="icon_name"
															style="width: 80%">
													</td>
												</tr>
												<tr class="table-white-bg_old">
													<td align="right" class="subject-title_old" width="35%">
														条件说明
													</td>
													<td align="left" class="subject-title_old" width="65%">
														<INPUT TYPE="text" NAME="icon_desc" id="icon_desc"
															style="width: 80%">
													</td>
												</tr>
												<tr class="table-white-bg_old">
													<td align="right" class="subject-title_old" width="35%">
														选择文件
													</td>
													<td align="left" class="subject-title_old" width="65%">
														<INPUT TYPE="FILE" NAME="upfile" SIZE="16"
															style="width: 80%">
													</td>
												</tr>
												<tr class="table-gay-bg2">
												<td align="left" class="subject-title_old" width="100%"
														colspan=2 >
														<font color='red'>
															上传文件只支持文本,excel文件[后缀是txt,xls],文件内容是只能有一列,其中excel文件从第一列第二行开始,文件最大20M
														</font><B>&nbsp;&nbsp;<%=handleInfo%></B>
												</td>
												</tr>
											</table>
										</div>
										</div>
										
										</td>
									</tr>
							<tr align="center">
							<td colspan="3">
								&nbsp;
							</td>
							</tr>
									
									<tr align="center">
										<td colspan="2" rowspan="2">
											<input type="button" ID="btnUpload" name="btnUpload"
												onclick="return CheckWorkFile()" value="确定上传" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
												class="btn4" onclick="">
											&nbsp;&nbsp;
											<input type="button" name="Submit4" value="取 消"
											class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"	onClick="cancelClose();">
										</td>
									</tr>
								</table>
								</td>
								</tr>
								</table>
								</div>
							
								</form>
</div>
	</body>
</html>
