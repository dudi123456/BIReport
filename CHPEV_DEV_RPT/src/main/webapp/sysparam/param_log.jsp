<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.syspar.domain.*"%>
<%@ page import="com.ailk.bi.syspar.dao.impl.*"%>
<%@ page import="com.ailk.bi.syspar.manage.*"%>
<%@ page import="com.ailk.bi.syspar.util.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"
%>
    
<%
//当前分析参数结点
String node_id = request.getParameter(ParamConstant.PARAM_CONDITION_NODE_ID);
if(node_id ==null||"".equals(node_id)){
	node_id=(String)session.getAttribute(ParamConstant.PARAM_CONDITION_NODE_ID);
	if(node_id == null ||"".equals(node_id)){
		node_id=ParamConstant.DEFAULT_APP_NODE_ID;//需要提取值,目前默认
	}
}


//接受服务
ParamFacade facade = new ParamFacade(new ParamDao());
//节点信息
UiParamInfoConfigTable nodeInfo = facade.getParamConfigTreeMetaByNodeID(node_id);
//配置参数表
UiParamMetaConfigTable[] tableInfo = null;
ArrayList conf_list = facade.getParamConfigMetaByNodeID(node_id);
if(conf_list!=null&&!conf_list.isEmpty()){
	tableInfo = (UiParamMetaConfigTable[]) conf_list.toArray(new UiParamMetaConfigTable[conf_list.size()]);
}
//
String[][] logArr = facade.getParamLogInfoByNodeID(nodeInfo,tableInfo,"");


%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据表日志分析</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/newmain.css" type="text/css">
</head>
<body>
<table style="width: 100%"  style="background-color:#F0F0F0;border-top:1px #D8D8E4 solid;">
	<tr>
		<td class="toolbg" ><font class="tooltitle"><%=nodeInfo.getParam_name()%>--数据配置全部日志信息</font></td>
		<td class="toolbg" align="right">
		<input type="button" ID="button_return" value="返回" onclick="window.close();">
		</td>

	</tr>
</table>
<table style="width: 100%" cellspacing="0" cellpadding="0" class="kuangContent2">
							<tr>
								<td valign="top" class="kuangContentpadding">
						
								<table style="width: 100%" align="center" height="400px">
									<tr>
										<td>
								<div style="width: 100%; overflow: auto; cursor: default; height: 100%">
									<table style="width: 100%" class="datalist">
										<tr class="celtitle FixedTitleRow">
										<% 										    
											for(int i=0;tableInfo!=null&&i<tableInfo.length;i++){												
												out.println("<td >"+tableInfo[i].getColumn_cn_name()+"</td>");	
											}
											out.println("<td >操作类型</td>");
											out.println("<td >操作员</td>");
											out.println("<td >操作时间</td>");
										%>
										</tr>
										<%
											for(int i=0;logArr!=null&&i<logArr.length;i++){
												out.println("<tr class=\"celdata\" onmouseover=\"this.className='mouseM'\" onmouseout=\"this.className='celdata'\">");
												for(int j=0;tableInfo!=null&&j<tableInfo.length;j++){
													out.println("<td >"+logArr[i][j+6]+"</td>");
												}
												
												if("1".equals(logArr[i][5])){
													out.println("<td >增加</td>");
												}else if ("2".equals(logArr[i][5])){
													out.println("<td >修改</td>");
												}else if ("3".equals(logArr[i][5])){
													out.println("<td >删除</td>");
												}
												
												
												//out.println("<td > "+new CommonFacade().getUserName(logArr[i][2])+"("+logArr[i][2]+")</td>");
												out.println("<td > ("+logArr[i][2]+")</td>");
												out.println("<td >"+logArr[i][4]+"</td>");
												
												out.println("</tr>");
												
												
											}
										%>
								
									</table>
								</div>
								</td>
									</tr>
								</table>

	
</td>
</tr>
</table>

<table style="width: 100%"  style="background-color:#F0F0F0;border-top:1px #D8D8E4 solid;">
	<tr>
		<td class="toolbg" ></td>
		<td class="toolbg" align="right">
		<input type="button" ID="button_return" value="返回" onclick="window.close();">
		</td>

	</tr>
</table>
</body>
</html>