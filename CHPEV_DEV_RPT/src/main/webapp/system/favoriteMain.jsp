<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<HTML>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<HEAD>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>

<TITLE>收藏夹管理</TITLE>
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/syscss.css">
<SCRIPT src="<%=request.getContextPath()%>/js/common.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/tabpane.js"></SCRIPT>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/js.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language=javascript>
function _newFavor(){
	favorDefPage.pageName = "favoriteView.jsp";
	favorDefPage.favor_id = 0;
	window.work_frame.location = "favoriteView.jsp";
}

function _treeRedirect(treeType){
	window.favorList_frame.favorTree.location = "favoriteTree.jsp?treeType=" + treeType
	_newFavor();
}

//用户定义页面切换类,由用户列表和页签决定用户定义页面
function FavorDefPage(){
	this.pageName = "favoriteView.jsp";	//当前页面
	this.favor_id = 0;		//当前定义用户id
	this.favor_name = ""		//当前定义用户名称

	this.pageRedirect_favor = pageRedirect_favor;		//由用户列表发起的切换
	this.pageRedirect_res = pageRedirect_res; //由区域发起的切换
}

function pageRedirect_favor(favor_id, favor_name){
	this.favor_id = favor_id;
	this.favor_name = favor_name;
	this.pageName = "favoriteView.jsp";
	window.work_frame.location=this.pageName + "?favor_id=" + favor_id + "&favor_name=" + favor_name+"&submitType=1";
}

function pageRedirect_res(res_id, res_name){
	this.favor_id = res_id;
	this.favor_name = res_name;
	this.pageName = "favoriteView.jsp";
	window.work_frame.location=this.pageName + "?res_id=" + res_id + "&res_name=" + res_name+"&submitType=1";
}


var favorDefPage = new FavorDefPage();

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

<link id="luna-tab-style-sheet" type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/other/tab.css" />
<head>
<!-- <body  bgcolor="#02449B" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"> -->
<body>
<table cellspacing="0" cellpadding="0" width="100%" height="100%"
	border="0">
	<tr ><td height="10"  colspan="2">
<table cellspacing="0" cellpadding="0" width="100%" height="10"
			border="0" >
	  <tr> 
	    <td height="">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
	        <tr> 
	          <!-- <td width="5" background="../biimages/home/nav_bg.gif"><img src="../biimages/home/nav_corner.gif"></td>
	          <td width="5" ></td>
	          <td background="../biimages/home/nav_bg.gif"><img src="../biimages/home/nav_icon.gif" width="14" height="11" hspace="10">你所在位置：　<a href="javascript:;">系统管理</a> 
	            &gt;&gt; <span class="font-nav">收藏夹管理</span></td>  -->
	        	<td> 
	        		 <div class="toptag">
            				您所在位置：系统管理 >>  <em class="red">收藏夹管理</em>
       				 </div>  
				</td>
	        </tr>
	      </table>
	    </td>
	  </tr>

      </table>
    </td>
	</tr>
	<tr>
		<td width="180" height="99%" valign="top"  bgcolor="#ffffff"><iframe id="favorList_frame"
			name="favorList_frame" width="99.5%" height="100%"
			frameborder="0" marginwidth="0" marginheight="0" scrolling="no"
			src="favoriteTreeFrame.jsp"></iframe></td>

    <td align="center" valign="top"> 

      <table cellspacing="0" cellpadding="0" width="100%" height="100%"
			border="0" >

			<tr>
				<td>
				<TABLE width=100% height=100% border=0 cellPadding=0 cellspacing="0">
					<TBODY>
						<TR>
							
							<TD width="100%" height=100%><iframe id="work_frame" frameborder="0"
								name="work_frame" width="100%" height="100%" scrolling="no"
								src="favoriteView.jsp"> </iframe></TD>
							
						</TR>
						<!-- 首页搜索结束 -->

					</TBODY>
				</TABLE>

				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</body>