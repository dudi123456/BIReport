<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
		return;
%>
<HTML xmlns:cool>
<HEAD>
<TITLE>用户组定义</TITLE>
<link id="luna-tab-style-sheet" type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/other/tab.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/common.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/tabpane.js"></SCRIPT>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>

<script language=javascript>
  //新增用户组
  function _newGroup(){
	groupDefPage.pageName = "userGroupView.jsp";
	groupDefPage.group_id = 0;
	groupDefPage.newLabel.select();
	window.work_frame.location="userGroupView.jsp";
  }

  //用户组定义页面切换类,由用户组列表和页签决定用户组定义页面
  function groupDefPage(){
	this.pageName = "userGroupView.jsp";	                 //当前页面
	this.group_id = 0;	                         //当前定义用户组id
	this.group_name = '';	                         //当前定义用户组名称
	this.newLabel = null;	                         //新增时，打开的标签
	this.pageRedirect_id = pageRedirect_id;		 //由用户组列表发起的切换
	this.pageRedirect_name = pageRedirect_name;	 //由用户组定义页签发起的切换
  }

  //由用户组列表发起的切换
  function pageRedirect_id(group_id, group_name,parent_id){
	this.group_id = group_id;

	this.group_name = group_name;
	if(this.pageName=="userGroupView.jsp" || this.pageName=="userGroupView.jsp?submitType=1") {
		this.newLabel =label1;
		label1.select();
		groupDefPage.pageRedirect_name("userGroupView.jsp?submitType=1");
	}else {
		this.newLabel =label2;
		label2.select();
		groupDefPage.pageRedirect_name("userGroupView.rptdo?submitType=groupRole");
	}
//	groupDefPage.newLabel.select();
//	window.work_frame.location=this.pageName + "?group_id=" + group_id + "&group_name=" + group_name+"&parent_id="+parent_id;

  }

  //由用户组定义页签发起的切换
  function pageRedirect_name(pageName){
	this.pageName = pageName;
	window.work_frame.location=pageName + "&group_id=" + this.group_id + "&group_name=" + this.group_name;
  }

  //
function _treeRedirect(treeType){

	window.groupList_frame.groupTree.location = "groupTree.jsp?treeType=" + treeType
	_newGroup();
}
  var groupDefPage = new groupDefPage();
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
<body  style="margin:0">
<table cellspacing="0" cellpadding="0" width="100%" height="100%"
	border="0">
	<tr height="20">
		<td colspan="2">
		<table cellspacing="0" cellpadding="0" width="100%"
			border="0" >
        <tr>
          <td valign="bottom" class="pageTitle" nowrap>用户组管理</td>
          <td width="2%"><span class="handin"></span></td>
          <td  border=0 height="22">
          <td width="125" height="22" valign="bottom" border=0 nowrap> <img src="../images/common/system/file.gif" title="点击新增一个用户组" ><a href="javascript:_newGroup();" target="_self">
            新增用户组</a> </td>
         <td width="85%" valign="bottom"  border=0>
		<img src="../images/common/system/arrow7.gif" width="7" height="7">
		<span class="bulefont">你所在位置： </span>
		<a href="javascript:;">系统管理</a>
		&gt;&gt; <a href="javascript:;">权限管理</a>
        &gt;&gt; <a href="javascript:;">用户组管理</a>
          </td>
          <td width="10%" valign="bottom" nowrap>&nbsp;</td>
        </tr>
        <tr>
          <td height="1" colspan="4" >
          <td colspan="2"  background="../images/common/system/black-dot.gif"></td>
        </tr>
        </table>
		</td>
	</tr>
	<tr>
		<td width="240" height="100%" valign="top"><iframe id="groupList_frame"
			name="groupList_frame" width="100%" height="100%" border="1"
			frameborder="1" marginwidth="0" marginheight="0" scrolling="no"
			src="groupTreeFrame.jsp"> </iframe></td>


    <td align="center" valign="top">
     <table width="100%" border="0">
        <tr>
          <td height="3"></td>
        </tr>
      </table>

      <table cellspacing="0" cellpadding="0" width="100%" height="100%"
			border="0" >
			<tr>


				<td width="160" height="23" valign="bottom" nowrap id="groupedit">

				<div class="tab-pane" id="tabPane1">
					<script type="text/javascript">
						tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
					</script>
				<div class="tab-page" id="groupView">
				<h2 class="tab">用户组信息</h2>
					<script type="text/javascript">
						var label1 = tp1.addTabPage( document.getElementById( "groupView" ));
						label1.tab.onclick = function () {
						label1.select();
						groupDefPage.pageRedirect_name("userGroupView.jsp?submitType=1");
						return false;
						};
						groupDefPage.newLabel = label1
						label1.select();

					</script>
				</div>

				<div class="tab-page" id="groupRole" style="display:none">
				<h2 class="tab">相关角色</h2>
					<script type="text/javascript">
						var label2 = tp1.addTabPage( document.getElementById( "groupRole" ) );
						label2.tab.onclick = function (){
						if(groupDefPage.group_id == 0){
 							alert("请首先选择左侧用户组！");
 							return;
 						}
						label2.select();
						groupDefPage.pageRedirect_name("userGroupView.rptdo?submitType=groupRole");

						return false;
						};
					</script>
				</div>
<!--
				<div class="tab-page" id="userlist" style="display:none">
				<h2 class="tab">相关用户</h2>
				<script type="text/javascript">
				var label7 = tp1.addTabPage( document.getElementById( "userlist" ) );
				label7.tab.onclick = function (){
				if(groupDefPage.group_id == 0){
 					alert("请首先选择左侧用户组！");
 					return;
 				}
				label7.select();
				groupDefPage.pageRedirect_name("userGroupView.rptdo?submitType=groupUser");

				return false;
				};
				</script>
				</div>
 -->
				</div>

				</td>

				<td width="100%" rowspan="2" valign="bottom" nowrap>
				<TABLE width=100% height=4 border=0 cellPadding=0 cellspacing="0" bgcolor="#F5F5F4">
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
							<TD width=4 background="../images/common/system/biao_l_m.gif"></TD>
							<TD width="100%" height=100%><iframe id="work_frame"
								name="work_frame" width="100%" height="100%" frameborder="0"
								marginwidth="0" marginheight="0" scrolling="auto"
								src="userGroupView.jsp"> </iframe></TD>
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