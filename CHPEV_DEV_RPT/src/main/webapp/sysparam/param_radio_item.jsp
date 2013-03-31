<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.util.WebKeys,com.ailk.bi.pages.PagesInfoStruct,com.ailk.bi.pages.WebPageTool"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<%
    String[][] struct = (String[][])session.getAttribute("listRadioStruct");
	//
	String pageRows = (String) session.getAttribute(WebKeys.ATTR_HiPageRows);
	if (pageRows == null || "".equals(pageRows)) {
		pageRows = "10";
	}

	int sltPage = -1;
	int sltRow = 0;
	// 閫変腑淇℃伅
	String qry_radio_id = request.getParameter("qry_radio_id");
%>
<script language="JavaScript">
channel="";
function returnNewChannel(){
   //alert(channel);
   window.returnValue=channel;
}

function returnChannel(theObj){
   //alert(theObj);
   channel=theObj;
   if (channel==null||channel==""){
      alert("浣犳病鏈夐�夋嫨");
      return;
    }else{
      //鍘婚閫楀彿
      if (channel.substring(0,1)==",")
        channel=channel.substring(1,channel.length);
        //鍘诲熬閫楀彿
        if (channel.substring(channel.length-1,channel.length)==",")
          channel=channel.substring(0,channel.length-1);
    }
	//alert(channel);
    window.returnValue=channel;
    window.close();
}

function returnChooseValue() {
	var code_Values = document.all['pageChkBox']; 
	//alert(code_Values.length);
	var rtnValue = "";
    if(code_Values.length){
		for(var i=0;i<code_Values.length;i++) 
		{ 
			if (code_Values[i].checked)
			{
				if (rtnValue.length==0)
				{
					rtnValue = code_Values[i].value;
				}else{
					rtnValue += "|" + code_Values[i].value;
				}
				 
			}
		} 
		alert(rtnValue);
	}
}
</script>
</head>
<%=WebPageTool.pageScript("myform")%>
<%
int iRowNum = 0;
if(struct!=null){
	iRowNum = struct.length;
}
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request,iRowNum, Integer.parseInt(pageRows) * 4);
%>
<body class="main-body">
<form name="myform" method="post" action="radioCheckDisp.screen" target="_self">
<input type="hidden" name="qry_radio_id" value="<%=qry_radio_id%>">
<center><%=WebPageTool.pageHidden(pageInfo)%> <%
 		if (sltPage > -1) {
 		out.println("  <script>");
 		out.println("  if (myform.page__iCurPage.value!='" + sltPage
 		+ "'){");
 		out.println("    goPage('" + (sltPage + 1) + "');");
 		out.println("  }");
 		out.println("  </script>");
 	}
 %>

<table width="100%" border="0" cellpadding="0" cellspacing="1">
	<tr id="polit">
		<td class="note-bg" colspan="4">
		<table width="100%">
			<tr>
				<TD align="center" vlaign="bottom"><%=WebPageTool.pagePolit(pageInfo)%>
				</TD>
			</tr>
		</table>
		</td>
	</tr>
	<%
	if (struct==null || struct.length <= 0) {
		out.println("<tr><td align=\"center\" class=\"table-item\" colspan=\"4\" height=\"20\"><font color=\"red\"><b>娌℃湁鍖归厤鐨勬暟鎹暟鎹紒</b></font></td></tr>");
	} else {
		for (int i = 0; i < pageInfo.iLinesPerPage && ((i + pageInfo.absRowNoCurPage()) < pageInfo.iLines);) {
			if (((i + 1) % 2) != 0) {
	%>
	<tr class="table-tr" onMouseOver="switchClass(this)"
		onMouseOut="switchClass(this)">
		<%
		} else {
		%>
	
	<tr class="table-trb" onMouseOver="switchClass(this)"
		onMouseOut="switchClass(this)">
		<%
					}
					int j = -1;
					while ((i + pageInfo.absRowNoCurPage()) < pageInfo.iLines
					&& j++ < 1) {
						String[] channel = struct[(i++)+ pageInfo.absRowNoCurPage()];
		%>
		<td align="left" class="table-td" width="50%">
		<%=WebPageTool.tdRadio(pageInfo,channel[0])%>&nbsp;<%=channel[1]%>(<%=channel[0]%>)
		</td>
		<%
		}
		%>
	</tr>
	<%
		}
		}
	%>
	<tr>
	  <td class="list-bg" align="center" colspan="2">
		<input type="button" name="okbutton" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="纭畾" onclick="returnChannel(myform.page__checkIDs.value);">
		<input type="button" name="okbutton" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="鍏抽棴" onclick="window.close();">
	  </td>
	</tr>
	<table>
</center>
</form>
</body>
</html>