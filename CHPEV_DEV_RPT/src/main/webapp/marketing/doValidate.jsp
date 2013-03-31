<%@ page import="java.util.*" language="java"contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.report.dao.ILReportSelectDataDao"%>
<%@page import="com.ailk.bi.report.dao.impl.LRepoerSelectDataImpl"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptFilterTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.report.util.ReportObjUtil"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.service.ITacticService"%>
<%@page import="com.ailk.bi.marketing.service.impl.TacticServiceImpl"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>



<%
/**
 * 查询功能
 * @author  方慧
 * @version  [版本号, 2012-04-01]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
response.setContentType("text/xml");
response.setHeader("Cache-Control","no-store");
response.setHeader("Pragma","no-cache");
response.setDateHeader("Expires",0);
String tacticName = request.getParameter("tacticName");//获得条件值
String tacticType = request.getParameter("tacticType");//获得条件值
WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext((request).getSession().getServletContext());
ITacticService tacticService = (TacticServiceImpl)ctx.getBean("tacticService");
TacticInfo entity = new TacticInfo();
if (!StringTool.checkEmptyString(tacticName)) {
	entity.setTacticName(tacticName);
}
if (!StringTool.checkEmptyString(tacticType)) {
	entity.setTacticType(Integer.parseInt(tacticType));
}else{
	entity.setTacticType(-999);//不做查询条件
}
entity.setState(1);//设置条件 已审批的
List<TacticInfo> list = tacticService.getAll(entity, 0);
response.getWriter().write(list.get(0).getTacticName());
%>