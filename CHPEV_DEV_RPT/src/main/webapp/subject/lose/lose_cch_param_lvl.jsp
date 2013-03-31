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
<%@ page import="com.ailk.bi.common.app.StringB"%>  

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Content-Language" content="zh-cn">



<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/bimain.css">

<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
<title>主参数维护</title>


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

<body>
<script>
function selectValue(obj){

	var arr = obj.value.split("$$");
	document.TableQryForm.param_id.value =arr[0]; 
	document.TableQryForm.lvl_id.value =arr[1]; 
	document.TableQryForm.lvl_name.value =arr[2]; 
	document.TableQryForm.remk.value =arr[5]; 
	document.TableQryForm.start_val.value =arr[3]; 
	document.TableQryForm.end_val.value =arr[4]; 

	document.getElementById("oppparamspan").innerHTML = "&nbsp;";

	
}
function _newParam(){	
	//
	var param_id = document.TableQryForm.param_id.value;
	var param_name = document.TableQryForm.lvl_id.value;
	
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}

	if(param_name == null || param_name == ""){
		alert("分档ID不能为空！");
		return;
	}

    TableQryForm.action="loseSubjectParam.rptdo?optype=add_param_lvl";
	TableQryForm.submit();
}

function _editParam(){	
	//
	var param_id = document.TableQryForm.param_id.value;
	var param_name = document.TableQryForm.lvl_id.value;
	
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}

	if(param_name == null || param_name == ""){
		alert("分档ID不能为空！");
		return;
	}

    TableQryForm.action="loseSubjectParam.rptdo?optype=edt_param_lvl";
	TableQryForm.submit();
}

function _delParam(){	
	//
	var param_id = document.TableQryForm.param_id.value;
	var lvl_id = document.TableQryForm.lvl_id.value;
	
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}
	if(confirm("确认要删除吗？")){
		TableQryForm.action="loseSubjectParam.rptdo?optype=del_param_lvl";
		TableQryForm.submit();
	}
}

function _viewParamDtl(paramId){	
		TableQryForm.action="loseSubjectParam.rptdo?optype=viewDtl&param_id=" + paramId;
		alert(TableQryForm.action);
		//TableQryForm.submit();

}


</script>
</head>
<%
int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx)==null?"1280":(String)session.getAttribute(WebKeys.Screenx));
int chartWidthTmp = (screenx-250)/2;

%>

<FORM name="TableQryForm" action="loseSubjectParam.rptdo?optype=qry_d_cch_param_lvl"  method="post">
<Tag:Bar />

<%=WebPageTool.pageScript("TableQryForm","cch_param_config.screen")%>
<% 
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, list.length, list.length );
String init = request.getParameter("init");
if("true".equals(init)) {
	pageInfo.iCurPage=0;	
} 
String ctl_Info = StringB.NulltoBlank((String)request.getAttribute("CTL_INFO"));
%>

<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
   
<table width='100%' border='0' cellpadding='0' cellspacing='0' valign="top">
<tr>
<td align="center">
               <table style="width: 100%" border="0" bgcolor="#CFCFCF" cellpadding="1" cellspacing="1"
                                       style="margin:0;">          
                  <tr class="celtitle FixedTitleRow">
					<td align="center"  bgcolor="E3E3E3" width="5%">选择</td>   
					<td align="center"  bgcolor="E3E3E3" width="5%">参数ID</td>   
			
                  	<td align="center"  bgcolor="E3E3E3" width="9%">LVL_ID</td>   
                    <td align="center"  bgcolor="E3E3E3" width="9%">LVL_NAME</td>
                    <td align="center"  bgcolor="E3E3E3" width="9%">起始值</td>    
					<td align="center"  bgcolor="E3E3E3" width="9%">结束值</td>    

					<td align="center"  bgcolor="E3E3E3" width="9%">备注</td>    

                  </tr>   
                    <%if(list==null||list.length==0){ %>
  <tr class="table-white-bg">
    <td colspan="6" nowrap  class="leftdata">该条件下没有符合要求的数据</td>
  </tr>
  <%}else{  
	  for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
		String[] value = list[i+pageInfo.absRowNoCurPage()];
  %>  

             
                	   <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
					    <td align="center"><input type="radio"  name="param" value="<%=value[1]%>$$<%=value[2]%>$$<%=value[3]%>$$<%=value[4]%>$$<%=value[5]%>$$<%=value[6]%>" onclick="selectValue(this)"></td>   
						<td align="right"><%=value[1]%></td>   
                	  <td align="right"><%=value[2]%></td>      
					   <td align="right"><%=value[3]%></td>      
					    <td align="right"><%=value[4]%></td>     
						<td align="right"><%=value[5]%></td>     
						<td align="right"><%=value[6]%></td>     
						</tr>   
                	  <%                		  
                	  }}
                	  %>
                	 
                 
                	
                 
                
                </table>
			
</td>
</tr>
</table>
<table width='100%' border='0' cellpadding='0' cellspacing='0' valign="top">
 <tr>
                <td><img src="../biimages/feedback-ioc1.gif" width="22" height="14"></td>
                <td valign="bottom" nowrap><span class="title-bold">参数编辑区</span></td>
                <td><span class="bulebig"><img src="../biimages/kw/broken-line.gif"></span></td>
              </tr>
              <tr>
                <td colspan="4" nowrap align="center"><span id="oppparamspan" class="title-big-bold">&nbsp;<%=ctl_Info%></span></td>
              </tr>
                <tr>
                <td colspan="4" nowrap>
                <input name="h_param_id" type="hidden">
                <table width="100%"  border=1 cellpadding="0" cellspacing="0" borderColorLight=#C0C0C0 borderColorDark=#ffffff>
                  <tr>
                    <td align="center" class="td-bg">参数ID</td>
                    <td ><input name="param_id" type="text" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">分档ID</td>
                    <td ><input type="text" name="lvl_id" class="query-input-text-1" ></td>
                  </tr>
                  <tr>
                    <td align="center" class="td-bg">分档名称</td>
                    <td ><input type="text" name="lvl_name" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">备注</td>
                    <td ><input type="text" name="remk" class="query-input-text-1" ></td>
                  </tr>
                   <tr>
                    <td align="center" class="td-bg">起始值</td>
                    <td ><input type="text" name="start_val" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">结束值</td>
                    <td ><input type="text" name="end_val" class="query-input-text-1" ></td>
                  </tr>
                </table>
                  </td>
              </tr>
              
              <tr align="center">
                <td colspan="4" nowrap>
                  <input name="btn_add" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="增加" onClick="_newParam();">
                  <input name="btn_back" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="修改" onClick="_editParam();" >
                  <input name="btn_del" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"  value="删除" onClick="_delParam();">
                  <input type="reset" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"  value="重置">
                  <input type="button" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" onClick="history.back();" value="返回">
 
                 </td>
              </tr>
            </table>
</FORM>
</body>
</html>