<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<HEAD>
<TITLE>部门维护</TITLE>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/common.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/tabpane.js"></SCRIPT>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<STYLE>
@media all
{
	cool\:bar{
	   	behavior: url("../htc/coolbar_js.htc");
	        padding:1px
        }

	cool\:button{
		behavior: url("../htc/coolbutton_js.htc");
	        text-align: left
	}
}
</STYLE>
<link id="luna-tab-style-sheet" type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/other/tab.css" />
<head>
<body  style="margin:0">
<table cellspacing="0" cellpadding="0" width="100%" height="100%" border="0">

	<tr height="22">
    <td height="20"  colspan="2">
	<table cellspacing="0" cellpadding="0" width="100%" height="20"
			border="0" >
        <tr>
          <td valign="bottom" class="pageTitle" nowrap>部门维护</td>
          <td width="9"><span class="handin"></span></td>
          <td width="1%" height="22"  border=0>
          <td width="90" height="22" valign="bottom" border=0 nowrap> <img src="../images/common/system/file.gif" title="点击新增一个部门"><a href="deptview.jsp?oper_type=1" target="DeptView">新增部门</a>
          </td>
	  <td width="85%" valign="bottom"  border=0>
		<img src="../images/common/system/arrow7.gif" width="7" height="7">
		<span class="bulefont">你所在位置： </span>
		<a >系统管理</a>
		&gt;&gt; <a >权限管理</a>
        &gt;&gt; <a >部门维护</a>

          </td>
        </tr>
        <tr>
          <td height="1" colspan="4"></td>
          <td colspan="2" background="../images/common/system/black-dot.gif"></td>
        </tr>
      </table>
    </td>
	</tr>

	<tr>
		<td width="180" height="100%" valign="top">
		<iframe id="DeptList" name="DeptList" width="100%" height="100%" border="1"
			frameborder="1" marginwidth="0" marginheight="0" scrolling="auto"
			src="depttree.jsp"></iframe>
	   </td>
       <td  width="85%" height="100%">
       <iframe id="DeptView" name="DeptView" width="100%" height="100%" frameborder="0"
			marginwidth="0" marginheight="0" scrolling="auto"
			src="deptview.jsp?oper_type=1"> </iframe>
	 </td>
	</tr>

 </table>

</body>
</html>