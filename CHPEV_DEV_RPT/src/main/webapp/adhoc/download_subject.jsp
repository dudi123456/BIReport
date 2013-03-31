<%@ page contentType="text/html;charset=UTF-8" language="java" %>  
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@ page import="com.ailk.bi.common.app.Arith"%>
<%@ page import="com.ailk.bi.adhoc.util.AdhocUtil"%>
<%@ page import="javax.servlet.ServletOutputStream"%>

<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>


<%   
     response.reset();   
     response.setContentType("application/x-msdownload");   
	ReportQryStruct struct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);

	 String name=java.net.URLEncoder.encode("清单.csv","UTF-8");
	 name = struct.dim1 + "_" + struct.dim4 + "_" + struct.dim5 + name;
     response.setHeader("Content-Disposition",   "attachment;   filename=" + name);   
	 
	 if(struct == null){
		 System.out.println("null");
		struct = new ReportQryStruct();
	 }
	//ServletOutputStream out = response.getOutputStream();

	AdhocUtil.buildCSVFile(struct,response.getOutputStream());
	
	//out.clear();
	//out = pageContext.pushBody();

 %>  
