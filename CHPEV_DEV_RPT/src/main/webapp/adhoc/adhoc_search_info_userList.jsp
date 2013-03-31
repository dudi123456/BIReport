<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>

<html>
<head>
<%@ include file="/base/commonHtml.jsp" %>

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

<base target="_self"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>搜索清单定义</title>
<script>
function isEmpty(s){
  return ((s == null) || (s.length == 0))
}

function doQrySearchTask(){
	
	 if(isEmpty(document.form1.txtQryName.value)){
		 alert("请输入查询名称，不能为空！");
		 document.form1.txtQryName.focus();
		 return false;
	}

}

function checkAll() 
{ 
var code_Values = document.all['pageChkBox']; 
if(code_Values.length){ 
	for(var i=0;i<code_Values.length;i++) 
	{ 
	code_Values[i].checked = true; 
	} 
}else{ 
	code_Values.checked = true; 
} 
} 
function uncheckAll() 
{ 
var code_Values = document.all['pageChkBox']; 
if(code_Values.length){ 
for(var i=0;i<code_Values.length;i++) 
{ 
code_Values[i].checked = false; 
} 
}else{ 
code_Values.checked = false; 
} 
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
					rtnValue += "," + code_Values[i].value;
				}
				 
			}
		} 
		if (rtnValue.length>0)
		{
			  window.returnValue=rtnValue;
	   //alert(rtnValue);
		      window.close();
		}else{
			alert('没有选中项');
		}
	 

	//	alert(rtnValue);

	 }

    }


</script>
<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

String strEnable = "";
if(list==null||list.length==0){
	strEnable = " disabled";
}
	//查询条件
	ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if(qryStruct == null){
		qryStruct = new ReportQryStruct();
	}

%>

<link href="<%=rootPath%>/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />

<SCRIPT language=javascript src="<%=rootPath%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=rootPath%>/js/date/scw.js"></script>
<script language=javascript src="<%=rootPath%>/js/date/scwM.js"></script>
<SCRIPT language=javascript src="<%=rootPath%>/js/dojo.js"></SCRIPT>
<script type="text/JavaScript" src="<%=rootPath%>/js/wait.js"></script>

<SCRIPT language=javascript src="<%=rootPath%>/js/kw.js"></SCRIPT>
<script type="text/javascript" src="<%=rootPath%>/js/XmlRPC.js"></script>

<body>
<div id="maincontent">
<form name="form1" action="adhocSearchInfo.rptdo?opType=qryListInfo" method="post"  onSubmit="return doQrySearchTask()">
<table style="width: 99%;" align="center" cellspacing="0" cellpadding="0">
	<tr><td class="toolbg_old">
	名称:<INPUT type="text" class="txtinput" name="txtQryName" id="txtQryName" value="<%=qryStruct.dim2%>">&nbsp;&nbsp;
	<INPUT TYPE="hidden" NAME="adhocId" value="<%=qryStruct.dim3%>">
			<input type="submit" id="button_search" class="button" value="查 询" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" />
			<input type="reset" id="button_reset" class="button" value="重 置" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" /></td>
	</tr>
</table>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2_" width="151"><font class="title1">查询结果</font></td>
				<td align="right" class="titlebg2_line_"><table>
<TD></TD>
</table>
</td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td  width="100%"><input id="all" type="button" <%=strEnable%> class="button" value="全 选" onclick="checkAll()"/><input id="not" type="button"  class="button" <%=strEnable%> value="全不选" onclick="uncheckAll()">&nbsp;<input
			type="button" name="okbutton2" class="button"
			onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
			value="确 定" <%=strEnable%> onclick="returnChooseValue();">&nbsp;<input
			type="button" name="okbutton3" class="button"
			onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
			value="关 闭" onclick="window.close();"></td>							
						</tr>
  <%if(list==null||list.length==0){ %>
  <tr class="celdata" align='center'>
    <td colspan="2"  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<list.length;i++){
		String[] value = list[i];				
  %>  

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><input type="checkbox" id="pageChkBox" name="pageChkBox" value="<%=value[0]%>"><%=value[1].trim()%></td>												
						</tr>
						 <%} %>
  <%} %>
 <tr class="celtitle">
							<td  width="100%"><input id="all" type="button" <%=strEnable%> class="button" value="全 选" onclick="checkAll()"/><input id="not" type="button"  class="button" <%=strEnable%> value="全不选" onclick="uncheckAll()">&nbsp;<input
			type="button" name="okbutton2" class="button" <%=strEnable%> 
			onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
			value="确 定" onclick="returnChooseValue();">&nbsp;<input
			type="button" name="okbutton3" class="button"
			onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
			value="关 闭" onclick="  window.close();"></td>							
						</tr>
					</table>
				</td>
			</tr>
		</table>

			</td>
			</tr>
			</table>
		</form>		
</div>
</body>
</html>

