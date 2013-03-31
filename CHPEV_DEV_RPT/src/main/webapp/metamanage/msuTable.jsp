<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.struct.*"%>
<%@ page import="com.ailk.bi.metamanage.dao.impl.MsuDefDaoImpl"%>

<%

	String msu_id = request.getParameter("msu_id");
	String msu_name = request.getParameter("msu_name");

	String o_Str = CommTool.getParameterGB(request, "o_Str");
	if (o_Str == null) {
		o_Str = "";
	}

%>

<HTML>
<HEAD>
<TITLE>指标与数据表关系</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT LANGUAGE=javascript src="SelVal.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
	<style>
		.tab-button-on {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_on.gif);
		    vertical-align: bottom;
		    font-size: 12px;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #9f4700;
		}
		
		.tab-button-off {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_off.gif);
		    vertical-align: bottom;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #666666;
		}
	</style>
<SCRIPT LANGUAGE=javascript>
  var o_Str = "";

  function clickCheck(obj){
  　var value = obj.value.split("$$");
  	if(value[1] != "") {
		pos = o_Str.indexOf("," + value[1] + ";");
		if(pos != -1){
			o_Str = o_Str.substr(0, pos) + o_Str.substr(pos + value[1].length + 3);
		}
		//alert(obj.checked);
		if(obj.checked){
			o_Str += "," + value[1] + ";1";
		}
		else{
			o_Str += "," + value[1] + ";0"
		}
//		alert(o_Str);
  	}
  }

  function _frmSubmit(){
	//alert(o_Str.length);
	document.hdform.o_Str.value=o_Str;
	document.hdform.submit();
  }
	function menuChanger(contentID,obj){
		
		if(contentID==1) {
			obj.href="<%=request.getContextPath()%>/metamanage/msuDef.rptdo?msu_id=<%=msu_id%>";
		}
	}
</SCRIPT>
</HEAD>

<BODY class="side-7">
<table width="99" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_line.gif" width="16" height="5"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
    <tr> 
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(1,this)" class="tab-button-off-link">指标管理</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
       
      </table></td>
    <td><img src="<%=request.getContextPath()%>/images/common/system/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-on"><a href="javascript:void(0)" onclick="menuChanger(2,this)" class="tab-button-off-link">指标与数据表关系</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_line.gif" width="280" height="5"></td>
  </tr>
</table>
<table width="100%">
	<tr>
		<td height="22"><img src="../biimages/arrow7.gif" width="7"
			height="7"> <b>指标：<%=msu_name%></b>&nbsp;</td>
		<TD height="22" align="right"><!--<img
			src="../images/SELTREE/region0.gif"><b>[<font
			color="#CC0099">没有下级区域</font>]</b>--></td>
		<td height="22" align="left"><!--<img
			src="../images/SELTREE/region1.gif"><b>[<font
			color="#CC0099">有下级区域</font>]</b>--></TD>
	</tr>
	<tr>
		<td height="1" background="../images/black-dot.gif" colspan="3"></td>
	</tr>

	<%
		//数据库处理
		int ErrNo = 0;
		if ((o_Str != null) && !"".equals(o_Str)) {
			o_Str = o_Str.substring(1); //去掉第一个","
			String[] o_Arr = o_Str.split(",");
			Vector rgChkArr = new Vector();
			for (int i = 0; i < o_Arr.length; i++) {
				String str = o_Arr[i];
				int pos = str.indexOf(";");
				KeyValueStruct keyvalue = new KeyValueStruct(str.substring(
				0, pos), str.substring(pos + 1));

				rgChkArr.add(keyvalue);
			}
			ErrNo = MsuDefDaoImpl.setMsuTable(request, msu_id, rgChkArr);

			if (ErrNo < 0) {
				out
				.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改失败！</font></td></tr></table>");
			} else {
				out
				.println("<table width=\"100%\"><tr><td width=\"100%\" align=\"center\"><font color=red>修改成功！</font></td></tr></table>");
			}
		}
	%>
</table>
<TABLE width="100%" height="85%" frameborder="0" marginwidth="0"
	marginheight="0" align='center' valign='top'>
	<TR>
<TD><IFRAME name='msuFrame'	src="checkboxMsu.jsp?msu_id=<%=msu_id%>"
			width="100%" height="100%" SCROLLING="auto"></IFRAME></TD>
	</TR>
	<%
	if (true) {
	%>
	<TR height=30>
		<TD align=center valign=bottom><INPUT type=button value="确 定"
			class="button" onClick="_frmSubmit();"></TD>
	</TR>
	<%
	}
	%>
<form action="msuTable.jsp" name="hdform" method="post">
<input name="msu_id" type="hidden" value="<%=msu_id%>"/>
<input name="msu_name" type="hidden" value="<%=msu_name%>"/>
<input name="o_Str" type="hidden" value=""/>
</form>
</TABLE>
</BODY>
</HTML>






