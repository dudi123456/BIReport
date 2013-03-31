<%
response.reset();
response.setContentType("application/x-download"); 
//response.setHeader("Content-disposition", "attachment; filename=\"sssss.xls\"");
//response.setContentType("application/vnd.ms-excel;charset=UTF-8");
%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.table.SubjectCommTabDef"%>
<%@page import="com.ailk.bi.subject.domain.TableCurFunc"%>
<%@page import="com.ailk.bi.subject.util.SubjectConst"%>
<%@page import="com.ailk.bi.subject.service.dao.ITableHeadHTMLDAO"%>
<%@page import="com.ailk.bi.subject.service.dao.impl.TableHeadHTMLDAO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ailk.bi.base.table.SubjectCommTabCol"%>
<%@page import="com.ailk.bi.subject.util.SubjectStringUtil"%>
<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
<%
	request.setCharacterEncoding("UTF-8");
	String content = (String)session.getAttribute(WebKeys.ATTR_ST_AREA_SEGMENT_REFLECT);
	if(null != content && !"".equals(content))
	{
		out.println("<table width='100%' border='1' cellpadding='0' cellspacing='0' "
							+ ">\n");
		out.println(content);
		out.println("</table>");
		out.println("<BR/>");
	}
%>
</body>
</HTML>