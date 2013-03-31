<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page
	import="com.ailk.bi.adhoc.struct.*,com.ailk.bi.base.util.WebKeys,com.ailk.bi.pages.PagesInfoStruct,com.ailk.bi.pages.WebPageTool"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<html>
<head>
  <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
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
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<%
    AdhocCheckStruct[] struct = (AdhocCheckStruct[]) session.getAttribute(WebKeys.ATTR_AdhocCheckStruct);
	if (struct == null) {
		struct = new AdhocCheckStruct[0];
	}
	//
	String pageRows = (String) session.getAttribute(WebKeys.ATTR_HiPageRows);
	if (pageRows == null || "".equals(pageRows)) {
		pageRows = "10";
	}

	int sltPage = -1;
	int sltRow = 0;
	//查找到的固定行
	String strCodeSelectId = (String) session.getAttribute(WebKeys.ATTR_HiCodeSelectId);
	if (strCodeSelectId == null) {//没有找到，则默认处理当前选择行
		if ((String) session.getAttribute(WebKeys.ATTR_HiCurRow) != null) {
			sltRow = Integer.parseInt((String) session.getAttribute(WebKeys.ATTR_HiCurRow));
			session.removeAttribute(WebKeys.ATTR_HiCurRow);
		}
	} else {
		int intCodeSelectId = Integer.parseInt(strCodeSelectId);
		sltPage = intCodeSelectId / (Integer.parseInt(pageRows) * 4);
		sltRow = intCodeSelectId % (Integer.parseInt(pageRows) * 4);
		session.setAttribute(WebKeys.ATTR_HiCurRow, "" + sltRow);
		session.removeAttribute(WebKeys.ATTR_HiCodeSelectId);
	}
%>
<script language="JavaScript">
    channel="";
    function returnNewChannel()
    {
       //alert(channel);
       window.returnValue=channel;
    }
    function returnChannel(theObj)
    {
       channel=theObj;
	 
       if (channel==null)
       {
          alert("你没有选择");
          return;
       }
       else{
          //去首逗号
          if (channel.substring(0,1)==",")
            channel=channel.substring(1,channel.length);
          //去尾逗号
          if (channel.substring(channel.length-1,channel.length)==",")
            channel=channel.substring(0,channel.length-1);
       }
	   //alert(channel);
       window.returnValue=channel;

       window.close();
    }


	    function returnChooseValue()
    {
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
			PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request,struct.length, Integer.parseInt(pageRows) * 4);
%>
<body class="main-body">
<form name="myform" method="post" action="AdhocMultiCheckDisp.screen"
	target="_self">
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

<table width="98%" border="0" cellpadding="0" cellspacing="1"
	class="table2" align="center">
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
	<tr><td>
	<div class="list_content">
	<table>
	<tr class="table-th">
		<th align="left" class="table-item" width="100%" colspan=2>&nbsp;<input id="all" type="button"  class="btn3" value="全 选" onclick="checkAll(true)" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"/><input id="not" type="button"  class="btn3" value="全不选" onclick="checkAll(false)" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" />&nbsp;<input
			type="button" name="okbutton2" class="btn3"
			onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
			value="确 定" onclick="returnChannel(myform.page__checkIDs.value);">&nbsp;<input
			type="button" name="okbutton3" class="btn3"
			onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
			value="关 闭" onclick="  window.close();"></th>
	</tr>
	<%
			if (struct.length <= 0) {
			out
			.println("<tr><td align=\"center\" class=\"table-item\" colspan=\"4\" height=\"8\"><font color=\"red\"><b>没有初始化数据！</b></font></td></tr>");
		} else {
			for (int i = 0; i < pageInfo.iLinesPerPage
			&& ((i + pageInfo.absRowNoCurPage()) < pageInfo.iLines);) {

				if (((i + 1) % 2) != 0) {
	%>
	<tr class="" onMouseOver="switchClass(this)"
		onMouseOut="switchClass(this)">
		<%
		} else {
		%>
	
	<tr class="jg" onMouseOver="switchClass(this)"
		onMouseOut="switchClass(this)">
		<%
					}
					int j = -1;
					while ((i + pageInfo.absRowNoCurPage()) < pageInfo.iLines
					&& j++ < 1) {
						AdhocCheckStruct channel = struct[(i++)+ pageInfo.absRowNoCurPage()];
		%>
		<td align="left" class="table-td" width="50%"><%=WebPageTool.tdCheckBox(pageInfo,
										channel.getKey())%>&nbsp;<%=channel.getDesc()%>(<%=channel.getKey()%>)
		</td>
		<%
		}
		%>
	</tr>
	<%
		}
		}
	%>

	</table>
	</div>
	</td></tr>
	<tr>
		<td class="list-bg-" align="center" colspan="2">
		<input type="button" name="okbutton" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="确 定" onclick="returnChannel(myform.page__checkIDs.value);" />
		<input type="button" name="cancelbutton" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="关 闭" onclick="window.close();" />
		</td>
	</tr>

	</table>
	
		</center>
		</form>
</body>
</html>
