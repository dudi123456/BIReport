<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.common.dbtools.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.common.app.AppException"%>  
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"%>
<%@ page import="com.ailk.bi.base.common.*"%>    

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Language" content="zh-cn">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/bimain.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/css.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
<title><%=CSysCode.SYS_TITLE%></title>


<%
String rootPath = request.getContextPath();
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}
String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
String[] title = (String[])session.getAttribute("lose_subject_title");
//查询条件
if(list == null){
list = new String[0][0];
}

%>
<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
<body onLoad="selfDisp()">
<script>

function export_table_content(tables,names){
	window.open("SubjectTableContentExport.screen?table_id="+tables+"&table_name="+names,"数据导出","");
}
function open_metaExplain(adhoc_id)
    {
        var h = "500";
        var w = "750";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "../adhoc/adhocMetaExplain.rptdo?adhoc_id=" + adhoc_id;
        var newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }
    }

function _exportSubmit(){
	  var optstr = "height=600,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no"
	  window.open("<%=rootPath%>/subject/lose/lose_comm_call_desc_exp.jsp","exportExcel",optstr);

}


</script>
</head>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>
<FORM name="TableQryForm" action="addSubjectExp.rptdo"  method="post">
<%=WebPageTool.pageScript("TableQryForm","Show_comm_expire.screen")%>
<% 

PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length,50 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<INPUT type=hidden name="opType" value="<%=qryStruct.optype%>">
<INPUT type=hidden name="data_type" value="<%=qryStruct.rpt_type%>">
<div style="display:none;position:absolute;" id=altlayer></div>
<table width="100%" height="3" border="0" cellpadding="0" cellspacing="0" class="squareB" >

      <tr> 
        <td background="<%=request.getContextPath()%>/biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">
        
        <table width="100%" border="0">
          <tr>
		  	
			  <td width="80%" align="left">&nbsp;&nbsp;账期:

			  <input style='input-text' value='<%=qryStruct.gather_month%>' size='12' type='text' name='qry__gather_month' id="qry__gather_month" onClick="scwShowM(this,this);">

			  &nbsp;&nbsp;协议名称:<BIBM:TagSelectList listName="qry__dim1" script=" class=query-input-text-long" 
									focusID="<%=qryStruct.dim1%>" listID="0" allFlag="" 
									selfSQL="select price_plan_cd,name from new_dim.d_price_plan" />
			  &nbsp;&nbsp;arpu分档:<BIBM:TagSelectList listName="qry__dim2"
									focusID="<%=qryStruct.dim2%>" listID="0" allFlag="" 
									selfSQL="select lvl_id,lvl_name from d_cch_param_lvl where param_id='102'" />
			  </td>
			
          		
            <td  width="20%" align="left">
          
            
               <input id="button_submit" type="submit" value="确认"/>
              <input type="button" name="dc" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="导出" onclick="javascript:_exportSubmit();"> 
      
            
            </td>
          </tr>
          
	
        </table>
        
        </td>
        <td background="<%=request.getContextPath()%>/biimages/square_line_3.gif"></td>
      </tr>
   
    </table>
  
<table width='100%' border='0' cellpadding='0' cellspacing='0' valign="top">
<tr>
<td align="center">

<div id="oppparamdiv" style="width: 100%; margin-top: 2px; overflow: auto; height:500px;">
               <table style="width: 100%" border="0" bgcolor="#CFCFCF" cellpadding="1" cellspacing="1"
                                       style="margin:0;">          
                  <tr class="celtitle FixedTitleRow">
                  <%for (int j=0;j<title.length;j++) { %>
                  	<td align="center"  bgcolor="E3E3E3" width="9%"><%=title[j] %></td>
                  <%} %>                 
                  </tr>   
                    <%if(list==null||list.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="14" nowrap  align="center" >该条件下没有符合要求的数据</td>
  </tr>
  <%}else{  
	  for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>  
  <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
             <%for(int k=0;k<value.length;k++) { %> 	   
                	  <td align="center"><%=value[k]%></td>   
              <%} %>       
  </tr>   
   <%                		  
             }}
    %>
</table>
</div>

</td>
</tr>
<tr>
<td>	<%=WebPageTool.pagePolit(pageInfo,rootPath)%></td>
</tr>
</table>

</FORM>
</body>
</html>