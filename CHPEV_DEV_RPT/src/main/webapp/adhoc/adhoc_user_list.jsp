<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ailk.bi.base.struct.UserCtlRegionStruct"/>
<jsp:directive.page import="com.ailk.bi.base.util.*"/>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.adhoc.domain.*"%>
<%@ page import="com.ailk.bi.adhoc.struct.AdhocViewQryStruct"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"
%>


<%
//列表数据
String[][] list =  (String[][])session.getAttribute(AdhocConstant.ADHOC_USER_LIST_VALUE);
String strExportBtn = "";

if(list == null){
	list = new String[0][0];
	strExportBtn = " disabled";
}
//配置信息
UiAdhocUserListTable[] defineInfo  = (UiAdhocUserListTable[])session.getAttribute(AdhocConstant.ADHOC_USER_LIST_DEFINE_INFO);
if(defineInfo == null){
	defineInfo = new UiAdhocUserListTable[0];
}

//字段类型
String sort_type=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
if (sort_type==null){
	//sort_type = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
	if(sort_type==null){
		sort_type="";
	}
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE,sort_type);

//列索引
String sortidx=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
if (sortidx==null){
	//sortidx = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
	if(sortidx==null){
		sortidx="0";
	}
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX,sortidx);

//1,asc;2,desc
String orderflag=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
if (orderflag==null){
	//orderflag = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
	if(orderflag == null){
		orderflag = "";
	}
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG,orderflag);

     //
     if (null!=sortidx && null != orderflag && null != list) {
		// 按数值排序
		if ("1".equalsIgnoreCase(sort_type)) {
			Arrays.sort(list, new NumberComparator(Integer.parseInt(sortidx), orderflag));

		} else {//2
		// 按字符排序2
			Arrays.sort(list, new CharacterComparator(Integer.parseInt(sortidx), orderflag));

		}
	 }

//增加发展人部门控制
UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
String tmpKey = "";
if(ctlStruct!=null&&ctlStruct.ctl_county_str!=null&&!"".equals(ctlStruct.ctl_county_str)){
	tmpKey=ctlStruct.ctl_county_str;
}
String tmpMap_code = "USER_DEVELOP_DEPART_ID4";
String tmpValue = "";
ServletContext servletContext = request.getSession().getServletContext();
HashMap codeMap = (HashMap)servletContext.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if(codeMap!=null){
			HashMap map = (HashMap)codeMap.get(tmpMap_code.trim().toUpperCase());
			if(map!=null){
				if(map.get(tmpKey) == null){
					tmpValue = "";
				}else{
					tmpValue = (String)map.get(tmpKey);
				}

			}
		}
if(!"".equals(tmpValue)){
	tmpValue = "(受限发展渠道查询："+tmpValue+")";
}
%>

<html>
<head>
<title>即席查询-清单</title>
<%@ include file="/base/commonHtml.jsp"%>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<script language=javascript>
<!--
//排序
function sort(order,colidx,datatype){
	location.href="../adhoc/adhoc_user_list.jsp?order_flag="+order+"&sort_idx="+colidx+"&sort_type="+datatype;
}
//导出
function exportExcel(){
  var h = "300";
        var w = "400";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "AdhocInfoExport.rptdo?oper_type=exportcsv";
	//	alert(strUrl)
        newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }

//	window.open("AdhocUserListToExcel.screen");
}



-->


</script>
</head>
<body>

<form name="ListForm"  method="post" action="" >
 <!--显示script部分-->
<%=WebPageTool.pageScript("ListForm","AdhocUserList.screen")%>
<%
///获取翻页相关信息
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, 20 );
%>
<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>

<table width="100%"  border="0" cellspacing="0" cellpadding="0">
<tr>
	<td height="5" colspan="4"></td>
</tr>
<tr>
						<td width="22"><img src="../images/common/system/icon_ztfx3.gif" width="16"
							height="16"></td>
						<td width="100" valign="bottom" nowrap><span
							class="title-bold">清单列表 <%=tmpValue %></span></td>
						<td ><span class="bulebig"><img
							src="../images/common/system/broken-line.gif"></span></td>
						<td align="center" width="200">

							<input type="button" name="btn_exl" value="导出" class="btn3" onclick="exportExcel()" <%=strExportBtn%>>
							<input type="button" name="btn_close" value="关闭" class="btn3" onclick="window.close();">
						</td>
</tr>
<tr>
				<td height="4"  colspan="5" align="center"></td>
</tr>

<tr id="list">
        <td class="side-left" colspan="4">
 		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table-with-lineshadow">
          <tr class="table-th">
          <%
           for(int i=0;defineInfo!=null&&i<defineInfo.length;i++){
						if("".equals(orderflag)){
							if("".equals(defineInfo[i].getMsu_unit())){
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"<a href=\"#\" onclick=\"sort(1,"+i+",2)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>"+"</td>");
							}else{
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"("+defineInfo[i].getMsu_unit()+")<a href=\"#\" onclick=\"sort(1,"+i+",1)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>"+"</td>");
							}

						}else if("1".equals(orderflag)&&(Integer.parseInt(sortidx)==i)){
							if("".equals(defineInfo[i].getMsu_unit())){
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"<a href=\"#\" onclick=\"sort(2,"+i+",2)\"><img src=\"../images/menu_up.gif\" border=\"0\"></a>"+"</td>");
							}else{
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"("+defineInfo[i].getMsu_unit()+")<a href=\"#\" onclick=\"sort(2,"+i+",1)\"><img src=\"../images/menu_up.gif\" border=\"0\"></a>"+"</td>");
							}

						}else if("2".equals(orderflag)&&(Integer.parseInt(sortidx)==i)){
							if("".equals(defineInfo[i].getMsu_unit())){
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"<a href=\"#\" onclick=\"sort(1,"+i+",2)\"><img src=\"../images/menu_down.gif\" border=\"0\"></a>"+"</td>");
							}else{
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"("+defineInfo[i].getMsu_unit()+")<a href=\"#\" onclick=\"sort(1,"+i+",1)\"><img src=\"../images/menu_down.gif\" border=\"0\"></a>"+"</td>");
							}

						}else{
							if("".equals(defineInfo[i].getMsu_unit())){
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"<a href=\"#\" onclick=\"sort(1,"+i+",2)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>"+"</td>");
							}else{
								out.println("<td align=\"center\" class=\"table-item\" >"+defineInfo[i].getMsu_name()+"("+defineInfo[i].getMsu_unit()+")<a href=\"#\" onclick=\"sort(1,"+i+",1)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>"+"</td>");
							}

						}

		 	}
          //out.println("<td align=\"center\" class=\"table-item\" >用户信息</td>");
          %>
          </tr>
          <%
			for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
				String[] value = list[i+pageInfo.absRowNoCurPage()];
				String strClassCaption = "table-td";
				String trClass = "table-tr";
				if((i+1)%2==1){
					strClassCaption = "table-td";
				}else{
					strClassCaption = "table-tdb";
					trClass = "table-trb";
				}

		  %>
        	<tr class="<%=trClass%>">
					 <%
					 		for(int j=0;j<value.length;j++){
					 			if("".equalsIgnoreCase(defineInfo[j].getMsu_type())|| "D".equalsIgnoreCase(defineInfo[j].getMsu_type()) || "M".equalsIgnoreCase(defineInfo[j].getMsu_type()) || "SS".equalsIgnoreCase(defineInfo[j].getMsu_type())){
					 				out.println("<td align=\"left\" class=\"" + strClassCaption + "\" >"+value[j]+"&nbsp;</td>");
					 			}else{
					 				String tmpV = "";
					 				if(value[j] == null){
					 					tmpV = "0";
					 				}else if("".equals(value[j])){
					 					tmpV = "0";
					 				}else{
					 					tmpV = value[j];
					 				}
					 				out.println("<td align=\"right\" class=\"" + strClassCaption + "\" >"+FormatUtil.formatStr(tmpV,Integer.parseInt(defineInfo[j].getMsu_digit()),true)+"&nbsp;</td>");
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
<tr>
	<td colspan="4"><%=WebPageTool.pagePolit(pageInfo)%></td>
</tr>
</table>
</form>
</body>
	<script language=javascript>
	    domHover(".btn3", "btn3_hover");

        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}
	</script>
</html>