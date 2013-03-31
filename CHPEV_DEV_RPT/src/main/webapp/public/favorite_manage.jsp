<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%@ page import="com.ailk.bi.base.struct.LsbiQryStruct"%>
<%@ page import="com.ailk.bi.base.table.PubInfoFavoriteTable"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表列表
PubInfoFavoriteTable[] favs = (PubInfoFavoriteTable[])session.getAttribute(WebKeys.ATTR_FAV_TABLES);
//查询条件
LsbiQryStruct lsbiQry = (LsbiQryStruct)session.getAttribute(WebKeys.ATTR_LsbiQryStruct);
if(lsbiQry==null){
	lsbiQry = new LsbiQryStruct();
}
//是否重载树
String strReload = (String)request.getAttribute("reload");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Tag:Log logType="2" defaultValue="00001zz"/>
<html>
<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script language=javascript>
function reload(){
  <%if("true".equals(strReload)){%>
    parent.leftFrame.location="favorite_left.jsp";
  <%}%>
}
</script>
<title>配置收藏夹</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_LsbiQryStruct%>" warn="1"/>
</head>
<body class="main-body" onLoad="selfDisp();reload();">
<form NAME="frmEdit" ID="frmEdit" ACTION="favorite.rptdo?opType=view_fav" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
              <Tag:Bar defaultValue="00001zz" favTag="false"/>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1" background="../biimages/black-dot.gif"></td>
	</tr>
	<tr>
		<td height="2"></td>
	</tr>
	<!--条件区展示 start-->
  <tr>
    <td>
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="squareB" >
      <tr> 
        <td><img src="../biimages/square_corner_1.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_1.gif"></td>
        <td><img src="../biimages/square_corner_2.gif" width="5" height="5"></td>
      </tr>
      <tr> 
        <td background="../biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">
        <table width="100%" border="0">
          <tr>
            <td width="5%" nowrap>名称：</td>
            <td nowrap><input type="text" size="20" name="qry__obj_name" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
			
			<td width="5%" nowrap>收藏类型：</td>
			<td nowrap>
			<BIBM:TagSelectList listID="#" allFlag="" listName="qry__hot_type" selfSQL="0,普通;1,常用"/></td>
            
            <td width="5%">
             <input type="button" name="search" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onclick="javascript:fnSubmit();"> 
            </td>
          </tr>
        </table>
        </td>
        <td background="../biimages/square_line_3.gif"></td>
      </tr>
      <tr> 
        <td height="6"><img src="../biimages/square_corner_3.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_4.gif"></td>
        <td><img src="../biimages/square_corner_4.gif" width="5" height="5"></td>
      </tr>
    </table>
    </td>
  </tr>
  <!--条件区展示 end-->
  <tr>
    <td height="5"></td>
  </tr>
  <tr>
    <td height="5" align="right">
    <a href="javascript:favSubmit(1);"><%if("1".equals(lsbiQry.oper_freq)){out.print("<font color=red>");} %>【整理我的收藏夹】</a>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:favSubmit(2);"><%if("2".equals(lsbiQry.oper_freq)){out.print("<font color=red>");} %>【设置常用的收藏】</a>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="javascript:favSubmit(3);"><%if("3".equals(lsbiQry.oper_freq)){out.print("<font color=red>");} %>【设置默认访问页】</a>&nbsp;&nbsp;</td>
  </tr>
  <!--报表显示 start-->
  <tr>
    <td class="tab-side2">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
  <tr align="center">
    <td class="tab-title">类型</td>
    <td class="tab-title">名称</td>
    <td class="tab-title">收藏类型</td>
    <td class="tab-title">选中</td>
  </tr>
  <%if(favs==null||favs.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="4" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>
  <%for(int i=0;i<favs.length;i++){ %>
  <tr class="table-white-bg">
    <td nowrap><%=favs[i].hot_type_desc%></td>
    <td nowrap><%=favs[i].name%></td>
    <td nowrap><%=favs[i].item_type_desc%></td>
    <td nowrap>
    <%if("1".equals(lsbiQry.oper_freq)){%>
    <input type="checkbox" name="rptSel" value="<%=favs[i].res_id%>">
    <%}else if("2".equals(lsbiQry.oper_freq)){ %>
    <input type="checkbox" name="rptSel" value="<%=favs[i].res_id%>" <%if("1".equals(favs[i].item_type)) out.println("checked=true");%>>
    <%}else if("3".equals(lsbiQry.oper_freq)){ %>
    <input type="radio" name="rptSel" value="<%=favs[i].res_id%>" <%if("1".equals(favs[i].item_default)) out.println("checked=true");%>>
    <%} %>
    </td>
  </tr>
  <%} %>
  <%} %>
</TABLE>
    </td>
  </tr>
  <!--报表显示 end-->
  <tr>
    <td>
<TABLE width="100%" border="0" cellpadding="5" cellspacing="5">
  <tr align="center" class="table-white-bg">
    <td nowrap>
    <%if("1".equals(lsbiQry.oper_freq)){%>
    <input type="button" name="del" class="button3" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="从收藏夹中删除" onclick="javascript:operSubmit(1,'del');"> 
    <%}else if("2".equals(lsbiQry.oper_freq)){ %>
    <input type="button" name="hot" class="button3" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="设置为常用收藏" onclick="javascript:operSubmit(2,'hot');"> 
    <%}else if("3".equals(lsbiQry.oper_freq)){ %>
    <input type="button" name="default" class="button3" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="设置为默认访问" onclick="javascript:operSubmit(3,'default');"> 
    <%} %>
    </td>
  </tr>
</TABLE>
    </td>
  </tr>
</table>
<input type="hidden" name="qry__oper_freq" value="<%=lsbiQry.oper_freq%>">
</form>
</body>
<script language=javascript>
function fnSubmit(){
  document.frmEdit.action="favorite.rptdo?opType=view_fav";
  document.frmEdit.submit();
}
function favSubmit(obj_type){
  document.frmEdit.qry__oper_freq.value=obj_type;
  document.frmEdit.action="favorite.rptdo?opType=view_fav";
  document.frmEdit.submit();
}
function operSubmit(obj_type,opType){
  document.frmEdit.qry__oper_freq.value=obj_type;
  document.frmEdit.action="favorite.rptdo?opType="+opType;
  document.frmEdit.submit();
}
</script>
</html>