<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>

<html>
<head><base target="_self"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>清单任务队列信息</title>
<%
String rootPath = request.getContextPath();
	//列表数据
String[][] list = (String[][])request.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
	}

%>

<link href="<%=rootPath%>/css/other/bimain.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/css/tablecss/main.css" rel="stylesheet" type="text/css" />
<SCRIPT language=javascript src="<%=rootPath%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=rootPath%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=rootPath%>/js/date/scw.js"></script>
<script language=javascript src="<%=rootPath%>/js/date/scwM.js"></script>
<SCRIPT language=javascript src="<%=rootPath%>/js/dojo.js"></SCRIPT>
<script type="text/JavaScript" src="<%=rootPath%>/js/wait.js"></script>

<SCRIPT language=javascript src="<%=rootPath%>/js/kw.js"></SCRIPT>
<script type="text/javascript" src="<%=rootPath%>/js/XmlRPC.js"></script>

<body>

<table style="width: 99%;" height="100%" align="center">
	<tr>
		<td valign="top" width="50%" style="padding-right:5px;">
		<!--日指标分析开始-->
		<table style="width: 100%">
			<tr>
				<td class="titlebg2" width="151"><font class="title1">清单任务队列</font></td>
				<td align="right" class="titlebg2_line"><table>
<TD align=left></TD>
</table>
</td>
			</tr></table>
			<table style="width: 100%">
			<tr>
				<td colspan="2">
					<table style="width: 100%"  class="datalist">
						<tr class="celtitle">
							<td  width="20%">任务名称</td>
							<td  width="20%">提交人</td>
							<td  width="20%">数据量</td>
							<td>状态</td>	
						</tr>
  <%if(list==null||list.length==0){ %>
  <tr class="celdata" align='center'>
    <td colspan="4"  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{ %>

  <%for(int i=0;i<list.length;i++){
		String[] value = list[i];				
  %>  

						<tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
							<td class="leftdata"><%=value[1]%></td>		
							<td class="leftdata"><%=value[2]%></td>		
							<td class="leftdata"><%=value[3]%></td>		
							<td class="leftdata"><%=value[4]%></td>		
						</tr>
						 <%} %>
  <%} %>
  <br>

 <tr class="celdata">
							<td  width="100%" colspan=4 class="centerdata">&nbsp;<input
			type="button" name="okbutton3" class="button"
			onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
			value="关闭" onclick="  window.close();"></td>							
						</tr>
					</table>
				</td>
			</tr>
		</table>

			</td>
			</tr>
			</table>

</body>
</html>

