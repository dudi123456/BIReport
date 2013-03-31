<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.report.struct.*"%>
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
<title>清单详情</title>


<%
String rootPath = request.getContextPath();

ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}

String[][] list = (String[][])session.getAttribute("VIEW_TREE_LIST");
	//查询条件
	if(list == null){
	list = new String[0][0];
}

%>

<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1"/>
<body onLoad="selfDisp()">
<script>
document.body.onmousemove=quickalt;
document.body.onmouseover=getalt;
document.body.onmouseout=restorealt;
var tempalt='';

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
  window.open("<%=rootPath%>/subject/add/add_comm_shtmsg_exp.jsp","exportExcel",optstr);

}

</script>
</head>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>

<FORM name="TableQryForm" action="addSubjectExp.rptdo?opType=qry_AgainMsgComm"  method="post">
<%=WebPageTool.pageScript("TableQryForm","Show_AddFlowComm.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length,50 );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<div style="display:none;position:absolute;" id=altlayer></div>

<table width="100%" height="3" border="0" cellpadding="0" cellspacing="0" class="squareB" >

      <tr> 
        <td background="<%=request.getContextPath()%>/biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">
        
        <table width="100%" border="0">
          <tr>
		  	
			  <td align="left">&nbsp;&nbsp账期:<input style='input-text' value='<%=qryStruct.gather_month%>' type='text' name='qry__gather_month' id="qry__gather_month" readonly onClick="scwShowM(this,this);">
			  </td>
			   <td>资费名称:<BIBM:TagSelectList listName="qry__dim3"
									focusID="<%=qryStruct.dim3%>" listID="0"
									selfSQL="select x.price_plan_id,x.price_plan_name from new_bi_ui.d_adv_focus_pp x where x.price_plan_cat_id=2 order by price_plan_id" /></td>
			  <td>ARPU分档:<BIBM:TagSelectList listName="qry__dim10"
									focusID="<%=qryStruct.dim10%>" listID="0"
									selfSQL="select lvl_id,lvl_name from d_adv_param_lvl where param_id=103 order by lvl_id" />									
									</td>
									</tr>
				<tr>
  <td  align="left">在网时长分档:<BIBM:TagSelectList listName="qry__dim4"
									focusID="<%=qryStruct.dim4%>" listID="0"
									selfSQL="select lvl_id,lvl_name from d_adv_param_lvl where param_id=101 order by lvl_id" />							
									</td>
	 <td>
	手机品牌<BIBM:TagSelectList listName="qry__dim15"
									focusID="<%=qryStruct.dim15%>" listID="0" allFlag="" script="onChange='changelocation(document.TableQryForm.qry__dim15.options[document.TableQryForm.qry__dim15.selectedIndex].value)'" 
									selfSQL="select distinct device_brand_id x1,device_brand_id x2 from D_DEVICE_BRAND_MODEL order by x1" />
									 </td>
			  <td>机型<SELECT name='qry__dim16' id="qry__dim16">
								　　　　　<OPTION selected value="">全部</OPTION>

								　　　　</SELECT>
								  </td>
			</tr>
			<tr>
          		
            <td colspan=3 width="20%" align="center">
          
            
               <input id="button_submit" type="submit" value="确认"/>&nbsp;&nbsp
              
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
                  	<td align="center"  bgcolor="E3E3E3" width="9%">用户ID</td>   
                    <td align="center"  bgcolor="E3E3E3" width="9%">客户名称</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">电话号码</td>                    
                  
                  </tr>   
                    <%if(list==null||list.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="3" nowrap  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{  
	  for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>  

             
                	   <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                	  <td align="center"><%=value[0]%></td>      
					    <td align="center"><%=value[2]%></td>        
						   <td align="center"><%=value[5]%></td>      

                  
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