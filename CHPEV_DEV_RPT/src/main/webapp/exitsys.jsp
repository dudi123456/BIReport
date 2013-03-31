<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import = "java.util.*"%>
<%@ page import = "com.ailk.bi.base.table.InfoOperTable"%>
<%@ page import = "com.ailk.bi.base.util.WebConstKeys"%>
<%@ page import = "com.ailk.bi.userlog.entity.UiInfoUserLog"%>
<%@ page import = "com.ailk.bi.common.dbtools.DAOFactory"%>
<%@ page import="com.crystaldecisions.sdk.framework.CrystalEnterprise"%>
<%@ page import="com.crystaldecisions.sdk.framework.IEnterpriseSession"%>
<%@ page import="com.crystaldecisions.sdk.occa.infostore.IInfoStore"%>
<%@ page import="com.crystaldecisions.sdk.occa.infostore.IInfoObjects"%>
<%@ page import="com.crystaldecisions.sdk.occa.infostore.IInfoObject"%>
<%@ page import="com.crystaldecisions.sdk.occa.infostore.CeSecurityID"%>
<%@ page import="com.crystaldecisions.sdk.plugin.desktop.folder.IFolder"%>
<%@ page import="com.businessobjects.sdk.plugin.desktop.category.ICategory"%>
<%@ page import="com.crystaldecisions.sdk.plugin.CeKind"%>
<%@ page import="com.crystaldecisions.sdk.framework.ISessionMgr"%>
<%@ page import="com.crystaldecisions.sdk.occa.security.ILogonTokenMgr"%>

<%
//清楚BO会话
IEnterpriseSession enterpriseSession = (IEnterpriseSession)session.getAttribute("CE_ENTERPRISESESSION");
String logontoken = (String)session.getAttribute("CE_LOGONTOKEN");
if(logontoken!=null&&!"".equals(logontoken)){
	enterpriseSession.getLogonTokenMgr().releaseToken(logontoken);
}
if(enterpriseSession!=null){
	enterpriseSession.logoff();
}

InfoOperTable loginUser = (InfoOperTable)session.getAttribute(WebConstKeys.ATTR_C_SSTUSERTABLE);
if (loginUser!=null){
	UiInfoUserLog obj = new UiInfoUserLog();
	obj.setUserId(loginUser.oper_no);
	obj.setSessionId(session.getId());
	DAOFactory.getUserLogDao().update(obj);
}


request.getSession().invalidate();
String strType = request.getParameter("TYPE");

%>
<script language="JavaScript">

<%

String strHostIP = "";
//String port = "8080";
System.out.println("strType="+strType);
if(strType.equals("1"))
{
%>

	parent.location.href="./login/login.jsp";
<%
}
else if(strType.equals("0"))
{
%>
window.close();
<%
}
%>
</script>