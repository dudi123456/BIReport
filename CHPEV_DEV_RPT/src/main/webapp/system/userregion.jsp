<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.ailk.bi.system.common.*" %>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.struct.KeyValueStruct"%>

<%
String pageRight = "11";
//操作员ID
String  user_id = CommTool.getParameterGB(request,"user_id");
//操作员名称
String oper_name =CommTool.getParameterGB(request,"oper_name");
//path_code
String o_Str = CommTool.getParameterGB(request,"o_Str");
%>

<HTML>
<HEAD>
<TITLE>操作员控制区域</TITLE>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/css.css">
<script src="<%=request.getContextPath()%>/js/js_ana.js" language="javascript"></script>
<SCRIPT LANGUAGE=javascript src="SelVal.js"></SCRIPT>


<SCRIPT LANGUAGE=javascript>
var o_Str = "";

function clickCheck(obj){
	pos = o_Str.indexOf("," + obj.value + ";");
	
	if(pos != -1){
		o_Str = o_Str.substr(0, pos) + o_Str.substr(pos + obj.value.length + 3);
	}

	if(obj.checked){
		o_Str += "," + obj.value + ";1";
	}
	else{
		o_Str += "," + obj.value + ";0"
	}
}

function _frmSubmit(){
	document.location="userregion.jsp?user_id=<%=user_id%>&oper_name=<%=oper_name%>&o_Str="+o_Str;
}

function _frmReset(){
	document.location="userregion.jsp?user_id=<%=user_id%>&oper_name=<%=oper_name%>";
}
</SCRIPT>
</HEAD>
<BODY class="side-7">
<table width="100%">
<tr>
		<td height="22"><img src="../images/arrow7.gif" width="7"
			height="7"> <b><%=oper_name%></b>&nbsp;相关区域</td>
		<%--<TD height="22" align="right"><img
			src="../images/SELTREE/region0.gif"><b>[<font
			color="#CC0099">没有下级区域</font>]</b></td>
		<td height="22" align="left"><img
			src="../images/SELTREE/region1.gif"><b>[<font
			color="#CC0099">有下级区域</font>]</b></TD>
	--%></tr>
	<tr>
		<td height="1" background="../images/black-dot.gif" colspan="3"></td>
	</tr>
</table>
<%
	//数据库处理
int  ErrNo = -1;

if((o_Str!=null) && !"".equals(o_Str)){
	o_Str = o_Str.substring(1);	//去掉第一个","
	String[] o_Arr = o_Str.split(",");
	Vector rgChkArr = new Vector();

	for(int i = 0; i< o_Arr.length; i++){
		String  str = o_Arr[i];
		int pos = str.indexOf(";");
		KeyValueStruct keyvalue = new KeyValueStruct(str.substring(0, pos),str.substring(pos + 1));
		rgChkArr.add(keyvalue);
	}

	ErrNo = 0;//LSInfoUser.setControlRegions(request,user_id, rgChkArr);

	if(ErrNo <= 0){
		out.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改失败！</font></td></tr></table>");
	}
	else{
		out.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改成功！</font></td></tr></table>");
	}
}
%>



<TABLE width="100%" height="85%" frameborder="0" marginwidth="0" marginheight="0"  align='center' valign='top'>
	<TR><TD>
	    <IFRAME name='RegionFrame' src="userRgChkListClient.jsp?user_id=<%=user_id%>&pageRight=<%=pageRight%>" width="100%" height="100%" SCROLLING="auto"></IFRAME>
	</TD></TR>
    <% if("11".equals(pageRight)){ %>
	<TR height=30>
	  <TD align=center valign=bottom>
	    <INPUT type=button value="保 存" class="button" onClick="_frmSubmit();">
	    <INPUT type=button value="取 消" class="button" onClick="_frmReset();">
	  </TD>
	</TR>
<%}%>

</TABLE>
</BODY>
</HTML>




