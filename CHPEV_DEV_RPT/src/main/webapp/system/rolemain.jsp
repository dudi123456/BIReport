<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
		return;
%>
<HTML xmlns:cool>
<HEAD>
<TITLE>角色定义</TITLE>
<link id="luna-tab-style-sheet" type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/other/tab.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
<SCRIPT src="<%=request.getContextPath()%>/js/common.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/tabpane.js"></SCRIPT>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>

<script language=javascript>


  //新增角色
  function _newRole(){
	roleDefPage.newLabel = label1;
	label1.select();
	roleDefPage.pageName = "roleview.jsp";
	roleDefPage.role_id = 0;
	roleDefPage.newLabel.select();
	window.work_frame.location="roleview.jsp";
  }

  //角色定义页面切换类,由角色列表和页签决定角色定义页面
  function RoleDefPage(){
	this.pageName = '';	                 //当前页面
	this.role_id = 0;	                         //当前定义角色id
	this.role_name = '';                         //当前定义角色名称
	this.role_type = '';
	this.newLabel = null;	                         //新增时，打开的标签
	this.pageRedirect_id = pageRedirect_id;		 //由角色列表发起的切换
	this.pageRedirect_name = pageRedirect_name;	 //由角色定义页签发起的切换
	this.sty='1';
  }
  var roleDefPage = new RoleDefPage();

  //由角色列表发起的切换
  function pageRedirect_id(role_id, role_name,role_type,local_net,comments){
	roleDefPage.newLabel = label1;
	this.role_id = role_id;
	this.role_name = role_name;
	this.role_type = role_type;
	if(this.pageName == "" || this.pageName == "roleview.jsp"){
		this.pageName = "roleview.jsp";
	}
	else{
		if(role_type=='1') {
			this.newLabel =label2;
			label2.select();
			this.pageName = "roleright.jsp";
		}else if(role_type=='2') {
			this.newLabel =label6;
			label6.select();
			this.pageName = "roleregion.jsp";
		}else if(role_type=='3') {
			this.newLabel =label7;
			label7.select();
			this.pageName = "ruleAdhocFrame.jsp";
		}else {
			this.newLabel =label8;
			label8.select();
			this.pageName = "roleview.rptdo";
			sty = 'roleGroup';
		}
	}
	//roleDefPage.newLabel.select();
	//alert(this.pageName + "?submitType="+this.sty+"&role_id=" + role_id + "&role_name=" + encodeURI(encodeURI(role_name))+"&role_type="+role_type+"&local_net="+local_net+"&comments="+comments)
	window.work_frame.location=this.pageName + "?submitType="+this.sty+"&role_id=" + role_id + "&role_name=" + encodeURI(encodeURI(role_name))+"&role_type="+role_type+"&local_net="+local_net+"&comments="+comments;
  }

  //由角色定义页签发起的切换
  function pageRedirect_name(pageName){
	this.pageName = pageName;
	//alert(pageName + "?submitType="+this.sty+"&role_id=" + this.role_id + "&role_name=" + this.role_name);
	window.work_frame.location=pageName + "?submitType="+this.sty+"&role_id=" + this.role_id + "&role_name=" + encodeURI(encodeURI(this.role_name));
  }

  //
function _treeRedirect(treeType){
	window.roleList_frame.roleTree.location = "roles.jsp?treeType=" + treeType
	_newRole();
}

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
          <td valign="bottom" class="pageTitle" nowrap>角色管理</td>
          <td width="2%"><span class="handin"></span></td>
          <td  border=0 height="22">
          <td width="125" height="22" valign="bottom" border=0 nowrap> <img src="../images/common/system/file.gif" title="点击新增一个角色" ><a href="javascript:_newRole();" target="_self">
            新增角色</a> </td>
         <td width="85%" valign="bottom"  border=0>
		<img src="../images/common/system/arrow7.gif" width="7" height="7">
		<span class="bulefont">你所在位置： </span>
		<a href="javascript:;">系统管理</a>
		&gt;&gt; <a href="javascript:;">权限管理</a>
        &gt;&gt; <a href="javascript:;">角色管理</a>
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
		<td width="240" height="100%" valign="top"><iframe id="roleList_frame"
			name="roleList_frame" width="100%" height="100%" border="1"
			frameborder="1" marginwidth="0" marginheight="0" scrolling="no"
			src="roleTreeFrame.jsp"> </iframe></td>


    <td align="center" valign="top">
     <table width="100%" border="0">
        <tr>
          <td height="3"></td>
        </tr>
      </table>

      <table cellspacing="0" cellpadding="0" width="100%" height="100%"
			border="0" >
			<tr>
				<td width="400" height="23" valign="bottom" nowrap id="roleedit">
				<div class="tab-pane" id="tabPane1">
					<script type="text/javascript">
					var tp1 = new WebFXTabPane( document.getElementById( "tabPane1" ) );
					</script>
				<div class="tab-page" id="roleView">
				<h2 class="tab">角色信息</h2>
					<script type="text/javascript">
						var label1 = tp1.addTabPage( document.getElementById( "roleView" ));
						label1.tab.onclick = function () {
						label1.select();
						roleDefPage.pageRedirect_name("roleview.jsp");
						return false;
						};
						roleDefPage.newLabel = label1;
						roleDefPage.pageName = "roleview.jsp";
						label1.select();
					</script>
				</div>

				<div class="tab-page" id="roleright" style="display:none">
				<h2 class="tab">角色菜单权限</h2>
					<script type="text/javascript">
						var label2 = tp1.addTabPage( document.getElementById( "roleright" ) );
						label2.tab.onclick = function (){
						if(roleDefPage.role_id == 0){
 							alert("请首先选择左侧角色！");
 							return;
 						}
						if(roleDefPage.role_type != 1){
 							alert("该角色没有菜单权限！");
 							return;
 						}
						label2.select();
						roleDefPage.newLabel = label2;
						roleDefPage.pageName = "roleright.jsp";
						roleDefPage.pageRedirect_name("roleright.jsp");

						return false;
						};
					</script>
				</div>

				<div class="tab-page" id="regioncheck" style="display:none">
				<h2 class="tab" >相关区域</h2>
				<script type="text/javascript">
					var label6 = tp1.addTabPage( document.getElementById( "regioncheck" ) );
					label6.tab.onclick = function (){
					if(roleDefPage.role_id == 0){
 						alert("请首先选择左侧角色！");
 						return;
 					}
					if(roleDefPage.role_type != 2){
						alert("该角色没有区域权限！");
						return;
					}
					label6.select();
					roleDefPage.newLabel = label6;
					roleDefPage.pageName = "roleregion.jsp";
					roleDefPage.pageRedirect_name("roleregion.jsp");

					return false;
					};
				</script>
				</div>
				<div class="tab-page" id="adhocRole" style="display:none">
				<h2 class="tab" >即席查询权限</h2>
				<script type="text/javascript">
					var label7 = tp1.addTabPage( document.getElementById( "adhocRole" ) );
					label7.tab.onclick = function (){
					if(roleDefPage.role_id == 0){
 						alert("请首先选择左侧角色！");
 						return;
 					}
					if(roleDefPage.role_type != 3){
						alert("该角色没有即席查询权限！");
						return;
					}
					label7.select();
					roleDefPage.newLabel = label7;
					roleDefPage.pageName = "ruleAdhocFrame.jsp";
					roleDefPage.pageRedirect_name("ruleAdhocFrame.jsp");

					return false;
					};
				</script>
				</div>
				<div class="tab-page" id="roleGroup" style="display:none">
				<h2 class="tab" >对应用户组</h2>
				<script type="text/javascript">
					var label8 = tp1.addTabPage( document.getElementById( "roleGroup" ) );
					label8.tab.onclick = function(){
					if(roleDefPage.role_id == 0){
 						alert("请首先选择左侧角色！");
 						return;
 					}
					label8.select();
					roleDefPage.newLabel = label8;
					roleDefPage.pageName = "roleview.rptdo";
					roleDefPage.sty='roleGroup';
					roleDefPage.pageRedirect_name("roleview.rptdo");

					return false;
					};
				</script>
				</div>
<!--  				<div class="tab-page" id="userlist" style="display:none">
				<h2 class="tab">相关用户</h2>
				<script type="text/javascript">
				var label7 = tp1.addTabPage( document.getElementById( "userlist" ) );
				label7.tab.onclick = function (){
				if(roleDefPage.role_id == 0){
 					alert("请首先选择左侧角色！");
 					return;
 				}
				label7.select();
				roleDefPage.pageRedirect_name("userlist.jsp");

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
								src="roleview.jsp"> </iframe></TD>
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