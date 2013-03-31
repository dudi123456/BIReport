<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<HTML>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<HEAD>
<TITLE>用户定义</TITLE>
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/syscss.css">
<SCRIPT src="<%=request.getContextPath()%>/js/common.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/tabpane.js"></SCRIPT>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language=javascript>
function _newUser(){
	userDefPage.pageName = "userview.jsp";
	userDefPage.user_id = 0;
	userDefPage.newLabel.select();
	window.work_frame.location = "userview.jsp";
}

function _treeRedirect(treeType){
	window.userList_frame.userTree.location = "usertree.jsp?treeType=" + treeType
	_newUser();
}

//用户定义页面切换类,由用户列表和页签决定用户定义页面
function UserDefPage(){
	this.pageName = "userview.jsp";	//当前页面
	this.user_id = 0;		//当前定义用户id
	this.user_name = ""		//当前定义用户名称
	this.region_id = 0;		//当前区域id
	this.region_name = "";		//当前区域名称
	this.newLabel = null;		//新增时，打开的标签
	this.pageRedirect_id = pageRedirect_id;		//由用户列表发起的切换
	this.pageRedirect_region = pageRedirect_region; //由区域发起的切换
	this.pageRedirect_dept = pageRedirect_dept;	//由部门发起的切换
	this.pageRedirect_pagename = pageRedirect_pagename;	//由页签发起的切换
}

function pageRedirect_id(region_id, region_name, user_id, user_name){
	this.user_id = user_id;
	this.user_name = user_name;
	this.region_id = region_id;
	this.region_name = region_name;
	this.pageName = "userview.jsp";
	this.newLabel =label1;
	userDefPage.newLabel.select();
	window.work_frame.location=this.pageName + "?region_id=" + region_id + "&user_id=" + user_id + "&user_name=" + user_name+"&submitType=1";
}

function pageRedirect_pagename(pageName){
	this.pageName = pageName;
	window.work_frame.location=pageName + "&region_id=" + this.region_id + "&user_id=" + this.user_id + "&user_name=" + this.user_name;
}

function pageRedirect_region(region_id, region_name){
	this.user_id = 0;
	this.region_id = region_id;
	this.region_name = region_name;
	this.pageName = "userview.jsp";
	this.newLabel =label1;
	userDefPage.newLabel.select();
	window.work_frame.location="userview.jsp?region_id=" + region_id + "&region_name=" + region_name;
}

function pageRedirect_dept(region_id, region_name, dept_id){
	this.user_id = 0;
	this.region_id = region_id;
	this.region_name = region_name;
	this.pageName = "userview.jsp";
	this.newLabel =label1;
	userDefPage.newLabel.select();
	window.work_frame.location="userview.jsp?region_id=" + region_id + "&region_name=" + region_name+"&dept_id="+dept_id;
}
var userDefPage = new UserDefPage();

WebFXTabPage.prototype.dispose = function () {
	this.aElement.onclick = null;
	this.aElement = null;
	this.element.tabPage = null;
	this.tab.onclick = null;
	this.tab.onmouseover = null;
	this.tab.onmouseout = null;
	this.tab = null;
	this.tabPane = null;
	this.element = null;
};


</script>
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
<body>
<table cellspacing="0" cellpadding="0" width="100%" height="100%"
	border="0">
	<tr ><td height="10"  colspan="2">
<table cellspacing="0" cellpadding="0" width="100%" height="10"
			border="0" >
        <tr>
          <td valign="bottom" class="pageTitle" nowrap>用户管理</td>
          <td width="9"><span class="handin"></span></td>
          <td width="1%" height="22"  border=0>
          <td width="90" height="22" valign="bottom" border=0 nowrap> <img src="../images/common/system/file.gif" title="点击新增一个用户"><a href="javascript:_newUser();" target="_self">新增用户</a>
          </td>
           <td width="85%" valign="bottom"  border=0>
		<img src="../images/common/system/arrow7.gif" width="7" height="7">
		<span class="bulefont">你所在位置： </span>
		<a href="javascript:top.locateMenuItem('19',true);">系统管理</a>
		&gt;&gt; <a href="javascript:top.locateMenuItem('9900001',true);">权限管理</a>
                &gt;&gt; <a href="javascript:top.locateMenuItem('9900003',true);">用户管理</a>
          </td>
	  <td align="right" width="10%"></td>
        </tr>
        <tr>
          <td height="1" colspan="4"></td>
          <td colspan="2" background="../images/common/system/black-dot.gif"></td>
        </tr>
      </table>
    </td>
	</tr>
	<tr>
		<td width="180" height="100%" valign="top"><iframe id="userList_frame"
			name="userList_frame" width="100%" height="100%" border="1"
			frameborder="1" marginwidth="0" marginheight="0" scrolling="no"
			src="userListFrame.jsp"></iframe></td>

    <td align="center" valign="top">
    <table width="100%" border="0">
        <tr>
          <td height="2"></td>
        </tr>
      </table>
      <table cellspacing="0" cellpadding="0" width="100%" height="100%"
			border="0" >
			<tr>
				<td width="160" height="23" valign="bottom" nowrap id="roleedit">
				<div class="tab-pane" id="tabPane1">
				<script type="text/javascript">
					tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
				</script>

				<div class="tab-page" id="userview">
				<h2 class="tab">用户信息</h2>
				<script type="text/javascript">
				var label1 = tp1.addTabPage( document.getElementById( "userview" ));
				label1.tab.onclick = function () {
 						label1.select();
						userDefPage.pageRedirect_pagename("userview.jsp?sub=1");
 						return false;
							};
				userDefPage.newLabel = label1
				label1.select();
				</script>
				</div>

				<div class="tab-page" id="userroles" style="display:none">
				<h2 class="tab">用户角色</h2>
				<script type="text/javascript">
				var label2 = tp1.addTabPage( document.getElementById( "userroles" ) );
				label2.tab.onclick = function (){
							if(userDefPage.user_id == 0){
							  alert("请首先选择左侧用户！");
							  return;
							}
							label2.select();
							userDefPage.pageRedirect_pagename("userview.rptdo?submitType=userRole");
							return false;
						  };
				</script>
				</div>

				<!--
				<div class="tab-page" id="louser" style="display:none">
				<h2 class="tab">子系统用户</h2>
				<script type="text/javascript">
				var label3 = tp1.addTabPage( document.getElementById( "louser" ) );
				label3.tab.onclick = function (){
							if(userDefPage.user_id == 0){
							  alert("请首先选择左侧用户！");
							  return;
							}
							label3.select();
							userDefPage.pageRedirect_pagename("loUserList.jsp?1=1");
							return false;
						  };
				</script>
				</div>
				 -->

				</div>
				</td>
				<td width="100%" rowspan="2" valign="bottom" nowrap>
				<TABLE width=100% height=4 border=0 cellPadding=0 cellspacing="0"
					bgcolor="#F5F5F4">
					<TBODY>
						<TR>
							<TD width=400 height="4" background="../images/common/system/biao_t_m.gif"></TD>
							<TD width=4 height="4"><IMG height=4
								src="../images/common/system/biao_r_t.gif" width=4 border=0></TD>
						</TR>
						<!-- 首页搜索开始 -->
						<!-- 首页搜索结束 -->
					</TBODY>
				</TABLE>
				</td>
			<tr>
				<td height="3" bgcolor="#F5F5F4">
				<table width="1" height="3" border="0" cellpadding="0"
					cellspacing="0" bgcolor="#9E9E9E">
					<tr>
						<td></td>
					</tr>
				</table>
				</td>
			<tr>
				<td colspan="2">
				<TABLE width=100% height=100% border=0 cellPadding=0 cellspacing="0">
					<TBODY>
						<TR>
							<TD width=1 background="../images/common/system/biao_l_m.gif"></TD>
							<TD width="100%" height=100%><iframe id="work_frame"
								name="work_frame" width="100%" height="100%" frameborder="0"
								marginwidth="0" marginheight="0" scrolling="auto"
								src="userview.jsp"> </iframe></TD>
							<TD width=4 background="../images/common/system/biao_r_m.gif"></TD>
						</TR>
						<!-- 首页搜索结束 -->
						<TR>
							<TD width=4><IMG height=4 src="../images/common/system/biao_l_b.gif" width=4
								border=0></TD>
							<TD height="4" background="../images/common/system/biao_b_m.gif"></TD>
							<TD width=4><IMG height=4 src="../images/common/system/biao_r_b.gif" width=4
								border=0></TD>
						</TR>
					</TBODY>
				</TABLE>

				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</body>