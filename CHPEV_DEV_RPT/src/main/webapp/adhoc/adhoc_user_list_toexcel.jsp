<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.adhoc.domain.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"
%>


<%

//列表数据
String[][] list =  (String[][])session.getAttribute(AdhocConstant.ADHOC_USER_LIST_VALUE);
if(list == null){
	list = new String[0][0];
}

//配置信息
UiAdhocUserListTable[] defineInfo  = (UiAdhocUserListTable[])session.getAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO);
if(defineInfo == null){
	defineInfo = new UiAdhocUserListTable[0];
}


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>即席查询-用户清单</title>
</head>
<body>
<form name="ListForm"  method="post" action="" >
 <!--显示script部分-->
<%=WebPageTool.pageScript("ListForm","AdhocUserList.screen")%>
<%
///获取翻页相关信息
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, list.length );
%>
<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>   

<table width="100%"  border="1" cellspacing="0" cellpadding="0">

<tr>
<td width="100" valign="bottom" nowrap align="center"><b>清单列表</b></td>
</tr>
<tr id="list">
        <td class="side-left">
 		<table width="100%" border="1" cellpadding="0" cellspacing="0" >
          <tr >
           <% 
           for(int i=0;defineInfo!=null&&i<defineInfo.length;i++){
						
							if("".equals(defineInfo[i].getMsu_unit())){
								out.println("<td align=\"center\" >"+defineInfo[i].getMsu_name()+"</td>");
							}else{
								out.println("<td align=\"center\"  >"+defineInfo[i].getMsu_name()+"("+defineInfo[i].getMsu_unit()+")</td>");
							}
							
						
		 	}       		
           
          %>
          </tr>
          <%
			for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
				String[] value = list[i+pageInfo.absRowNoCurPage()];     			
		  %>
        	<tr>
					 <%
					 		for(int j=0;j<value.length;j++){
					 			if("".equalsIgnoreCase(defineInfo[j].getMsu_type())|| "D".equalsIgnoreCase(defineInfo[j].getMsu_type()) || "M".equalsIgnoreCase(defineInfo[j].getMsu_type()) || "SS".equalsIgnoreCase(defineInfo[j].getMsu_type())){
					 				out.println("<td align=\"center\" class=\"table-td\" >"+value[j]+"</td>");									
					 			}else{
					 				String tmpV = "";
					 				if(value[j] == null){
					 					tmpV = "0";
					 				}else if("".equals(value[j])){
					 					tmpV = "0";
					 				}else{
					 					tmpV = value[j];
					 				}
					 				out.println("<td align=\"center\" >"+FormatUtil.formatStr(value[j],Integer.parseInt(defineInfo[j].getMsu_digit()),false)+"</td>");									
					 			}
					 			
					 		}
									
							
					 %>
			
		</tr>
		  <%
          	}
          %>
 
        </table>
        </td>
</tr>

</table>
</form>     
</body>
</html>