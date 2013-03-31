<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>

<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.SQLGenator"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.report.util.ReportObjUtil"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.report.struct.DimRuleStruct"%>





<!DOCTYPE html>
<HTML>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>

<%
//查询条件
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
if(qryStruct==null){
	qryStruct = new ReportQryStruct();
}
//报表基本信息
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE);
//
DimRuleStruct dimInfo  = (DimRuleStruct)session.getAttribute(WebKeys.ATTR_REPORT_MEASURE_RULE_DIM);
if(dimInfo == null){
	dimInfo = new DimRuleStruct("","region_id","","","","");
}


//地域信息，根据部署系统不同修改
String regionSql = SQLGenator.genSQL("Q3102");
if(!StringTool.checkEmptyString(qryStruct.attach_region)&&!ReportConsts.ZERO.equals(qryStruct.attach_region)){
	regionSql += " WHERE CITY_ID = '"+qryStruct.attach_region+"%' ";
}
regionSql += "order by CITY_ID";

//---------------------------报表显示信息 start----------------------------
//报表标题内容
String strTitle = (String)session.getAttribute(WebKeys.ATTR_REPORT_TITLE_HTML); 

//报表表体内容
String[] arrayBody = (String[])session.getAttribute(WebKeys.ATTR_REPORT_BODY_HTML);

//报表的审核内容
String processBody = (String)session.getAttribute(WebKeys.ATTR_REPORT_PROCESS_HTML);
//---------------------------报表显示信息 end  ----------------------------


%>

<HEAD>


<!-- js-add-start -->
    <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
    <script type="text/javascript">
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
    
<!-- js-add-end -->    

<TITLE>指标类报表显示</TITLE>
<%@ include file="/base/commonHtml.jsp" %>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scw.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
<script language="JavaScript" type="text/JavaScript">
//document.oncontextmenu = function() { return false;} 
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  var objbtn = document.getElementById("tj"); 
  objbtn.disabled = true;
  for (i=0; i<(args.length-1); i+=2){
   document.frmEdit.action=eval(args[i]+".location='"+args[i+1]+"'");
   document.frmEdit.submit();
  }
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}
//-->
function list(){
  <%if("1".equals(qryStruct.divcity_flag) && "region_id".equalsIgnoreCase(dimInfo.getDim_field().toLowerCase())){%>
     document.all.qry__region_id_r.disabled = true;
  <%}else{%>
     document.all.qry__region_id_r.disabled = false;
  <%}%>
}
</script>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
</HEAD>

<body class="main-body" onLoad="selfDisp();list();">


<!-- oncontextmenu="return false" ondragstart="return false" onselectstart ="return false" onselect="document.selection.empty()" oncopy="document.selection.empty()" onbeforecopy="return false" onmouseup="document.selection.empty()" -->
<div id="maincontent">
<!-- 导航区 --> 
<div class="toptag">
<Tag:Bar/>
</div>

<FORM NAME="frmEdit" ID="frmEdit" ACTION="ReportView.rptdo?rpt_id=<%=rptTable.rpt_id%>" method="POST">
<!--条件区展示 start-->
<div class="topsearch">
<table width="100%" border="0">
   <tr>
     <%=ReportObjUtil.genDateLayout(session,qryStruct.date_s,qryStruct.date_e)%>
     <td width="5%" nowrap >地域：</td>
     <td><BIBM:TagSelectList listName="qry__region_id_r" listID="0" allFlag="0" selfSQL="<%=regionSql%>" /></td>
     <%//if(ReportConsts.FIRST.equals(qryStruct.divcity_flag)){ %>
     <!--td width="5%" nowrap>是否按地域查看：</td-->
     <!-- td><BIBM:TagRadio radioName="qry__divcity_flag" radioID="S3004" focusID="<%//=qryStruct.divcity_flag%>"  script="onclick='_regionSubmit(this)'"/></td-->
     <%//} %>
     <td colspan="2" width="10%" align="center" nowrap>
      <input type="button" name="search" class=btn3 onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="查 询" onclick="javascript:_fnSubmit('<%=rptTable.start_date%>');"> 
      <input type="button" name="dc" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="导 出" onclick="javascript:_exportSubmit();"> 
      <input type="button" name="print" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="打 印" onclick="javascript:_print();"> 
     </td>
   </tr>
</table>
</div>
<!--条件区展示 end-->

<!--报表显示 start-->
<div class="listbox">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
    <table id="AutoNumber1" width="100%" border="0" cellpadding="0" cellspacing="0">
	  <%=strTitle%>
	  <tr>
		<td colspan="2" class="tab-side2" >
		<div class="list_content hasbg">
		<table width="100%">
	    <%
	      for (int i = 0; arrayBody != null && i < arrayBody.length; i++) {
              out.print(arrayBody[i]);
          }
        %>
        </table>
        </div>
        </td>
      </tr>
    
      <%if(ReportConsts.YES.equals(rptTable.processflag)){ %>
      <tr>
        <td height="30" colspan="2"><%=processBody%></td>
      </tr>
      <%} %> 
      
      <tr>
      <td>
   		<span class="blue title">
    	&nbsp;&nbsp;<%=CommTool.getEditorHtml(rptTable.rpt_id,"0")%>
   		</span>
      </td>
  	  </tr>
    </table>
    </td>
  </tr>
</table>
</div>
<!--报表显示 end-->

<INPUT TYPE="hidden" id="qry__first_view" name="qry__first_view" value="" />
</FORM>
</div>
</body>
<script language=javascript>
function _fnSubmit(start_date){
  if( (start_date==99)&& (document.all.qry__date_s.value>document.all.qry__date_e.value)){
    alert("起始日期大于终止日期！");
    return;
  }
  document.all.qry__first_view.value = "N";
  document.frmEdit.submit();
}
function _regionSubmit(region){
  document.all.qry__divcity_flag.value = region.value;
  document.frmEdit.submit();
}
function showReturn(obj){
  if(obj.value=="N"){
    tr_p_return.style.display="block";
    tr_p_forward.style.display="none";
  }else if(obj.value=="F"){
    tr_p_return.style.display="none";
    tr_p_forward.style.display="block";
  }
}
function _exportSubmit(){
  var optstr = "height=10,width=10,left=50,top=50,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no"
  window.open("../report/report_excel.jsp?rpt_id=<%=rptTable.rpt_id%>","exportExcel",optstr);
}
function _print(){
  var tablewidth=AutoNumber1.offsetWidth;
  if(tablewidth>1200){
    window.open("../report/report_excel_print.jsp?rpt_id=<%=rptTable.rpt_id%>","Print");
  }else{
    var optstr = "height=600,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no"
    newsWin = window.open("../report/report_print.jsp?rpt_id=<%=rptTable.rpt_id%>","Print",optstr);
    if(newsWin!=null)
      newsWin.focus();
  }
}
</script>
</html>