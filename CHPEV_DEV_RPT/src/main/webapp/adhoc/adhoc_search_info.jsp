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

  <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
	<script language=javascript>
	    domHover(".btn3", "btn3_hover");

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
<base target="_self"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>搜索指标定义</title>
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
	//form1.action = "adhocSearchInfo.rptdo?opType=qryinfo";
  //  form1.submit();

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
		      window.close();
		}else{
			alert('没有选中项');
		}

	 }

    }

	    function resetValue()
	    {
	    	document.getElementById("txtQryName").value = "";
	    	document.getElementById("searchRange").selectedIndex = 0;
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
<form name="form1" action="adhocSearchInfo.rptdo?opType=qryinfo" method="post"  onSubmit="return doQrySearchTask()">
<table style="width: 99%;" align="center" cellspacing="0" cellpadding="0">
	<tr><td class="toolbg">名称:<INPUT type="text" name="txtQryName" class="txtinput" id="txtQryName" value="<%=qryStruct.dim2%>">&nbsp;范围:<SELECT ID="searchRange" name="searchRange"  class="txtinput" ><OPTION value='0' <%if (qryStruct.dim1.equals("0")) out.print(" selected");%>   >全部</OPTION>
<OPTION value='1' <%if (qryStruct.dim1.equals("1")) out.print(" selected");%>  >条件</OPTION>
<OPTION value='2' <%if (qryStruct.dim1.equals("2")) out.print(" selected");%>  >维度</OPTION>
<OPTION value='3' <%if (qryStruct.dim1.equals("3")) out.print(" selected");%>  >指标</OPTION>
</SELECT>&nbsp;&nbsp;<INPUT TYPE="hidden" NAME="adhocId" value="<%=qryStruct.dim3%>">
			<input type="submit"  id="button_search" value="查询"><input type="button" id="button_reset" value="重置" onclick="resetValue()"></td>
	</tr>
</table>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">查询结果</font></td>
				<td align="right" class="titlebg2_line"><table>
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
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			value="确定" <%=strEnable%> onclick="returnChooseValue();">&nbsp;<input
			type="button" name="okbutton3" class="button"
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			value="关闭" onclick="  window.close();"></td>
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
							<td class="leftdata"><input type="checkbox" id="pageChkBox" name="pageChkBox" value="<%=value[0]%>|<%=value[3]%>">
<font color=red>【<%=value[2]%>】</font><%=value[1].trim()%></td>
						</tr>
						 <%} %>
  <%} %>
 <tr class="celtitle">
							<td  width="100%"><input id="all" type="button" <%=strEnable%> class="button" value="全 选" onclick="checkAll()"/><input id="not" type="button"  class="button" <%=strEnable%> value="全不选" onclick="uncheckAll()">&nbsp;<input
			type="button" name="okbutton2" class="button" <%=strEnable%>
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			value="确定" onclick="returnChooseValue();">&nbsp;<input
			type="button" name="okbutton3" class="button"
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			value="关闭" onclick="  window.close();"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

			</td>
			</tr>
			</table>
		</form>

</body>
</html>

