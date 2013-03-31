<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表基本信息
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE);
//验证需要打印报表ID
String verify_rpt_id = request.getParameter("rpt_id");
if(rptTable==null||!rptTable.rpt_id.equals(verify_rpt_id)){
	out.print("<center>");
	out.print("<br><br>此导出报表信息有误，可能你的操作信息丢失，请重新查询确定你需要导出的报表信息！<br>");
	out.print("<input type=\"reset\" name=\"close_win\" class=\"button\" onMouseOver=\"switchClass(this)\" onMouseOut=\"switchClass(this)\" value=\"关闭\" onClick=\"javascript:window.close();\"> ");
	out.print("</center>");
	return;
}
//---------------------------报表显示信息 start----------------------------
//报表标题内容
String strTitle = (String)session.getAttribute(WebKeys.ATTR_REPORT_TITLE_HTML);

//报表表体内容
String[] arrayBody = (String[])session.getAttribute(WebKeys.ATTR_REPORT_BODY_HTML);
//---------------------------报表显示信息 end  ----------------------------
String rpt_id=rptTable.rpt_id;
String url1 = "<a href=\"../report/ReportView.rptdo?p_condition=Y&rpt_id="+rpt_id+"&startcol=1\"></a>";
String url2 = "<a href=\"../report/ReportView.rptdo?p_condition=Y&rpt_id="+rpt_id+"&startcol=2\"></a>";
String url3 = "<a href=\"../report/ReportView.rptdo?p_condition=Y&rpt_id="+rpt_id+"&startcol=3\"></a>";
%>
<%
response.reset();
//response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//response.setContentType("application/vnd.ms-excel;charset=utf-8");
response.setContentType("application/vnd.ms-excel;charset=UTF-8");
response.setHeader("Content-Disposition","attachment;filename="+rptTable.rpt_id+".xls");
%>

<HTML>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="mimetype" content="application/octet-stream">
<body>
<iframe src="" name=show style="width:0;height:0"></iframe>
<table id="AutoNumber1" width="100%" border="0" cellpadding="0" cellspacing="0" style="display:none">
<%=strTitle%>
  <tr>
	<td colspan="2" class="tab-side2">
	<table width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#999999">
<%for(int i=0;arrayBody!=null&&i<arrayBody.length;i++){%>
<%
String content=arrayBody[i];
content = StringB.replace(content,"<span style=\"font-family:webdings;\">6</span>","");
content = StringB.replace(content,"<span style=\"font-family:webdings;\">5</span>","");
content = StringB.replace(content,url1,"");
content = StringB.replace(content,url2,"");
content = StringB.replace(content,url3,"");
content = StringB.replace(content,"<img src=\"../biimages/down_red.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<img src=\"../biimages/down_green.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<img src=\"../biimages/rise_green.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<img src=\"../biimages/rise_red.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<img src=\"../biimages/blank.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<img src=\"../biimages/menu_init.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<img src=\"../biimages/menu_down.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<img src=\"../biimages/menu_up.gif\"","<input type=\"hidden\"");
content = StringB.replace(content,"<a href=","<input type=\"hidden\" ");
%>
<%=content %>
<%}%>
	</table>
	</td>
  </tr>
  <%if(!"".equals(rptTable.rp_bottom)) {%>
  <tr>
  	<td>
  		<TABLE width="100%">
  			<%=rptTable.rp_bottom %>
  		</TABLE>
  	</td>
  </tr>
  <%} %>
</table>
</body>
</HTML>