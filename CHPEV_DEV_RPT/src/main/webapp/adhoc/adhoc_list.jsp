<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="java.util.*"%>
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

//字段类型
String sort_type=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
if (sort_type==null){
	sort_type = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
	if(sort_type==null){
		sort_type="";
	}  
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE,sort_type);

//列索引
String sortidx=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
if (sortidx==null){
	sortidx = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
	if(sortidx==null){
		sortidx="0";
	}   
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX,sortidx);

//1,asc;2,desc    
String orderflag=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
if (orderflag==null){
	orderflag = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
	if(orderflag == null){
		orderflag = "";
	}
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG,orderflag);

    
     //
     if (null!=sortidx && null != orderflag && null != viewList) {
		// 按数值排序
		if ("1".equalsIgnoreCase(sort_type)) {
			//Arrays.sort(viewList, new NumberComparator(Integer.parseInt(sortidx), orderflag));
				
		} else {//2
		// 按字符排序2
			//Arrays.sort(viewList, new CharacterComparator(Integer.parseInt(sortidx), orderflag));
				
		}
	 }

    

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<script language=javascript>

function sort(order,colidx,datatype){
	location.href="../adhoc/adhoc_list.jsp?order_flag="+order+"&sort_idx="+colidx+"&sort_type="+datatype;
}
</script>
</head>
<body>
<form name="ListForm"  method="post" action="" >
 <!--显示script部分-->
<%=WebPageTool.pageScript("ListForm","AdhocList.screen")%>
<%
///获取翻页相关信息
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, viewList.length, 20 );
%>
<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>   

<table width="100%"  border="0" cellspacing="0" cellpadding="0">
<tr>
	<td><%=WebPageTool.pagePolit(pageInfo)%></td>
</tr>
<tr id="list">
        <td class="side-left">
 		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table-with-lineshadow">
          <tr class="table-th">
          <% 
           for(int i=0;headArr!=null&&i<headArr.length;i++){        	        
					if(i<dimMeta.length){
						if("".equals(orderflag)){
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"<a href=\"#\" onclick=\"sort(1,"+2*i+",2)\"><img src=\"../biimages/menu_init.gif\" border=\"0\"></a>"+"</td>");
						}else if("1".equals(orderflag)&&(Integer.parseInt(sortidx)==2*i)){
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"<a href=\"#\" onclick=\"sort(2,"+2*i+",2)\"><img src=\"../biimages/menu_up.gif\" border=\"0\"></a>"+"</td>");
						}else if("2".equals(orderflag)&&(Integer.parseInt(sortidx)==2*i)){
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"<a href=\"#\" onclick=\"sort(1,"+2*i+",2)\"><img src=\"../biimages/menu_down.gif\" border=\"0\"></a>"+"</td>");
						}else{
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"<a href=\"#\" onclick=\"sort(1,"+2*i+",2)\"><img src=\"../biimages/menu_init.gif\" border=\"0\"></a>"+"</td>");
						}
						
					}else{
						if("".equals(orderflag)){
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(1,"+(dimMeta.length+i)+",1)\"><img src=\"../biimages/menu_init.gif\" border=\"0\"></a>"+"</td>");
						}else if("1".equals(orderflag)&&(Integer.parseInt(sortidx)==dimMeta.length+i)){
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(2,"+(dimMeta.length+i)+",1)\"><img src=\"../biimages/menu_up.gif\" border=\"0\"></a>"+"</td>");
						}else if("2".equals(orderflag)&&(Integer.parseInt(sortidx)==dimMeta.length+i)){
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(1,"+(dimMeta.length+i)+",1)\"><img src=\"../biimages/menu_down.gif\" border=\"0\"></a>"+"</td>");
						}else{
							out.println("<td align=\"center\" class=\"table-item\" >"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(1,"+(dimMeta.length+i)+",1)\"><img src=\"../biimages/menu_init.gif\" border=\"0\"></a>"+"</td>");
						}
					}
					
		 	}       		
           
          %>
          </tr>
          <%
			for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
				String[] value = viewList[i+pageInfo.absRowNoCurPage()];
      			if((i+1)%2==1){
		  %>
          <tr class="table-tr" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
					 <%
					 		for(int j=0;j<columnLength;j++){
								if(j<dimMeta.length){
									out.println("<td align=\"center\" class=\"table-td\" >"+value[2*j+1]+"</td>");									
								}else{
									out.println("<td align=\"center\" class=\"table-td\" >"+FormatUtil.formatStr(value[dimMeta.length+j],Integer.parseInt(msuMeta[j-dimMeta.length].getDigit()),true)+"</td>");									
								}
								
					 		}
					 %>
			
					<%
				}else{
					%>
					<tr class="table-trb" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">    
					  <%
					 		for(int j=0;j<columnLength;j++){
								if(j<dimMeta.length){
									out.println("<td align=\"center\" class=\"table-tdb\" >"+value[2*j+1]+"</td>");									
								}else{
									out.println("<td align=\"center\" class=\"table-tdb\" >"+FormatUtil.formatStr(value[dimMeta.length+j],Integer.parseInt(msuMeta[j-dimMeta.length].getDigit()),true)+"</td>");									
								}
								
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