<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.login.dao.IUserDao"%>
<%@page import="com.ailk.bi.login.dao.impl.UserDaoImpl"%>
<%@ page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%
	int userCount = 0;
	IUserDao userDao = new UserDaoImpl();
	InfoOperTable loginUser = CommonFacade.getLoginUser(session);
	if(null!=loginUser){
		String[][] systemUser = userDao.getCurrentOnlineUser(loginUser.system_id);

		if (systemUser != null){
			userCount = systemUser.length;
		}
	}
	StringBuffer strB = new StringBuffer("");
	strB.append("<a href='javascript:;' onclick='javascript:current_online_user();'>当前在线用户数：<font color=red>" + userCount + "</font></a>");
	out.print(strB.toString());
%>