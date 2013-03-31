<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.adhoc.domain.*"%>
<%@ page import="com.ailk.bi.adhoc.struct.AdhocViewStruct"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"
%>


<%


//当前条件
UiAdhocConditionMetaTable[]  conMeta  = (UiAdhocConditionMetaTable[])session.getAttribute(AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY);
if(conMeta ==null){
	conMeta = new UiAdhocConditionMetaTable[0];
}
//当前纬度
UiAdhocDimMetaTable[] dimMeta = (UiAdhocDimMetaTable[])session.getAttribute(AdhocConstant.ADHOC_VIEW_DIM_SESSION_KEY);
if(dimMeta ==null){
	dimMeta = new UiAdhocDimMetaTable[0];
}
//当前指标
UiAdhocMsuMetaTable[] msuMeta = (UiAdhocMsuMetaTable[])session.getAttribute(AdhocConstant.ADHOC_VIEW_MSU_SESSION_KEY);
if(msuMeta == null){
	msuMeta = new UiAdhocMsuMetaTable[0];
}

//列表
String[][] viewList = (String[][])session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST);
if(viewList == null){
	viewList = new String[0][0];
}
//视图信息
AdhocViewStruct adhocView = (AdhocViewStruct)session.getAttribute(AdhocConstant.ADHOC_VIEW_STRUCT);
if(adhocView ==null){
	adhocView = new AdhocViewStruct();
}
//标题头
String[] headArr = adhocView.headStr.split(",");
//字段长度
int  columnLength =  dimMeta.length+msuMeta.length;



%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

</head>
<body>
<form name="ListForm"  method="post" action="" >
<table border="1">
<tr>
<%=AdhocHelper.getSumTableHTML(session) %>
</tr>
</table>

 <!--显示script部分-->
<%=WebPageTool.pageScript("ListForm","AdhocList.screen")%>
<%
///获取翻页相关信息
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, viewList.length, viewList.length );
%>
<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>   

<table width="100%"  border="1" cellspacing="0" cellpadding="0">

<tr id="list">
        <td>
 		<table width="100%" border="1" cellpadding="0" cellspacing="0">
          <tr>
          <% 
           for(int i=0;headArr!=null&&i<headArr.length;i++){        	        
					if(i<dimMeta.length){
						
							out.println("<td>"+headArr[i]+"</td>");
						
						
					}else{
						
							out.println("<td>"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")</td>");
						
					}
					
		 	}       		
           
          %>
          </tr>
          <%
			for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
				String[] value = viewList[i+pageInfo.absRowNoCurPage()];
		  %>
          <tr>
			 <%
					 		for(int j=0;j<columnLength;j++){
								if(j<dimMeta.length){
									out.println("<td align=\"center\" >"+value[j]+"</td>");									
								}else{
									out.println("<td align=\"center\"  >"+FormatUtil.formatStr(value[j],Integer.parseInt(msuMeta[j-dimMeta.length].getDigit()),false)+"</td>");									
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