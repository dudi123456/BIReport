<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.domain.RptChartTable"%>
<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>

<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptFilterTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.report.util.ReportObjUtil"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%
request.setCharacterEncoding("UTF-8");
//查询条件
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
if(qryStruct==null){
	qryStruct = new ReportQryStruct();
}
qryStruct.gather_date=qryStruct.date_s;
session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT,qryStruct);
//qryStruct.visible_data = "Y";

//报表基本信息
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE);
//确定需要展示的条件
RptFilterTable[] rptFilterTables = (RptFilterTable[])session.getAttribute(WebKeys.ATTR_REPORT_FILTERS);
//纬度下拉列表全值
String[] filtersValue = (String[])session.getAttribute(WebKeys.ATTR_REPORT_FILTERS_VALUEALL);

//是否有script脚本
String outCdn = "";
boolean isScriptCity = false;
boolean isScriptBrand = false;
boolean isScriptChannel = false;

for (int i = 0; rptFilterTables!=null&&i < rptFilterTables.length; i++) {
	if(ReportConsts.FILTER_TYPE_SCRIPT.equals(rptFilterTables[i].filter_type) && !"".equals(rptFilterTables[i].filter_script)){
		outCdn = ReportConsts.FILTER_TYPE_SCRIPT;
		if(rptFilterTables[i].filter_script.indexOf("qry__city_id")>0) {
			isScriptCity = true;
		}
		if(rptFilterTables[i].filter_script.indexOf("qry__brand_knd")>0) {
			isScriptBrand = true;
		}
		if(rptFilterTables[i].filter_script.indexOf("qry__channel_type")>0) {
			isScriptChannel = true;
		}
	}
}
//获取外部条件
String requestwhere = (String)request.getAttribute("requestwhere");
if(requestwhere==null){
	requestwhere = "";
}

//---------------------------报表显示信息 start----------------------------
//报表标题内容
String strTitle = (String)session.getAttribute(WebKeys.ATTR_REPORT_TITLE_HTML);


//报表表体内容
String[] arrayBody = (String[])session.getAttribute(WebKeys.ATTR_REPORT_BODY_HTML);


//报表的审核内容
String processBody = (String)session.getAttribute(WebKeys.ATTR_REPORT_PROCESS_HTML);

//获取报表图形展示部分
RptChartTable[] rptCharts = (RptChartTable[])session.getAttribute(WebKeys.ATTR_REPORT_CHARTS);

//多序图形列表
String fusionchart_multisql = "select CHART_TYPE_CODE, CHART_TYPE_DESC from UI_PUB_INFO_CHART_KIND_DEF where CHART_DISTYPE=2 and chart_type_id<>24 order by dis_sequence";
//单序图形列表
String fusionchart_singlesql = "select CHART_TYPE_CODE, CHART_TYPE_DESC from UI_PUB_INFO_CHART_KIND_DEF where CHART_DISTYPE=3 order by dis_sequence";
//---------------------------报表显示信息 end  ----------------------------
%>
<!DOCTYPE html>
<HTML>
<HEAD>
<TITLE>本地化报表显示</TITLE>
<%@ include file="/base/commonHtml.jsp" %>
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>

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
   //alert(eval(args[i]+".location='"+args[i+1]+"'"));
   document.frmEdit.action=eval(args[i]+".location='"+args[i+1]+"'");
   document.frmEdit.submit();
   //eval(args[i]+".location='"+args[i+1]+"'");
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
 //页面登陆
function pageOnLoad(){
  <%if(isScriptCity){%>
    createCity();//地市
    if(frmEdit.qry__city_id){
      filterCounty('<%=qryStruct.city_id%>');
    }

    if(frmEdit.qry__city_id){
      frmEdit.qry__city_id.value = "<%=qryStruct.city_id%>";
    }
    if(frmEdit.qry__county_id){
      frmEdit.qry__county_id.value = "<%=qryStruct.county_id%>";
    }

  <%}%>
  <%if(isScriptBrand){%>
  createBrandCode(); //品牌
  if(frmEdit.qry__brand_knd){
	  filterSubBrandKnd('<%=qryStruct.brand_knd%>');
  }

  if(frmEdit.qry__brand_knd){
    frmEdit.qry__brand_knd.value = "<%=qryStruct.brand_knd%>";
  }
  if(frmEdit.qry__sub_brand_knd){
    frmEdit.qry__sub_brand_knd.value = "<%=qryStruct.sub_brand_knd%>";
  }

  <%}%>
  <%if(isScriptChannel){%>
	createChannelType(); //渠道
	if(frmEdit.qry__channel_type){
		filterSubChannelType('<%=qryStruct.channel_type%>');
	}

	if(frmEdit.qry__channel_type){
	  frmEdit.qry__channel_type.value = "<%=qryStruct.channel_type%>";
	}
	if(frmEdit.qry__channel_code){
	  frmEdit.qry__channel_code.value = "<%=qryStruct.channel_code%>";
	}

  <%}%>
}

function viewdoc(id)
{
  window.open('viewdoc.jsp?rpt_id='+id,"报表口径","width=800,height=400,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,resizable=0");
}
function dataview(id,op_time)
{
  var rp_engine='<%=rptTable.rp_engine%>';
  window.open("dataview.jsp?rpt_id="+id+"&op_time="+op_time+"&rp_engine="+rp_engine,"报表口径","width=200,height=200,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=yes,resizable=0");
}

</script>
<%--(1)创建地市下拉列表(2)由地市过滤区县的JS脚本--%>
<%if(isScriptCity) {%>
<%//=com.ailk.bi.monitor.common.JSTool.CityVsBusiOper(session,"frmEdit.qry__city_id","frmEdit.qry__county_id","","")%>
<%}if(isScriptBrand) {%>
<%//=com.ailk.bi.monitor.common.JSTool.BrandCodeVsSubBrandKnd(session,"frmEdit.qry__brand_knd","frmEdit.qry__sub_brand_knd")%>
<%}if(isScriptChannel) {%>
<%//=com.ailk.bi.monitor.common.JSTool.ChannelTypeVsSubChannelType(session,"frmEdit.qry__channel_type","frmEdit.qry__channel_code")%>
<%}%>

<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
<%=WebPageTool.pageScript("frmEdit","ReportView.rptdo?p_condition=Y&rpt_id="+rptTable.rpt_id+requestwhere)%>
</HEAD>

<body onLoad="selfDisp();pageOnLoad()">
<!-- oncontextmenu="return false" ondragstart="return false" onselectstart ="return false" onselect="document.selection.empty()" oncopy="document.selection.empty()" onbeforecopy="return false" onmouseup="document.selection.empty()" -->
<FORM NAME="frmEdit" ID="frmEdit" ACTION="ReportView.rptdo?rpt_id=<%=rptTable.rpt_id%><%=requestwhere %>" method="post">

<div id="maincontent">
<%
//报表翻页页数
int tmpPageCount = StringB.toInt(rptTable.pagecount,0);
int perPageCount = 0;
PagesInfoStruct pageInfo = null;
if(tmpPageCount>0&&ReportConsts.NO.equals(rptTable.isleft)){
	perPageCount = tmpPageCount;
	int recordCount = arrayBody.length-1;
	pageInfo = WebPageTool.getPageInfo(request, recordCount, perPageCount );
}
%>
<%if(pageInfo!=null){out.print(WebPageTool.pageHidden(pageInfo));}%>

	<!--报表显示 start-->
	<div class="listbox">
	<!-- 专题类报表 -->
	<%if(ReportConsts.RPT_SUBJECT_SHOW.equals(rptTable.rpt_type)){ %>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <%//=strTitle%>
    </table>
	<iframe name="<%=rptTable.rpt_id%>" id="<%=rptTable.rpt_id%>" width="100%" height="440"	src="SubjectCommTable.rptdo?table_id=<%=rptTable.rpt_id%>&first=Y&table_height=450"	frameborder="0" scrolling="no"></iframe>
	<%} else {%>
	<!-- 传统报表 -->
    <table id="AutoNumber1" width="100%"  border="0" cellpadding="0" cellspacing="0">
      <%//=strTitle%>
      <%if(perPageCount>0){%>
      <tr>
        <td align="right"><%=WebPageTool.pagePolit(pageInfo)%></td>
      </tr>
      <%}%>

      <tr>
        <td>
        <div class="list_content hasbg">
        <table width="100%">
        <%if(perPageCount>0){
    	    if(arrayBody!=null&&arrayBody.length>0){
  		      out.print(arrayBody[0]);
  	        }
    	    //long start =System.currentTimeMillis();
    	    for(int i=0;i<pageInfo.iLinesPerPage && (1+i+pageInfo.absRowNoCurPage())<=pageInfo.iLines;i++){
   	    		out.print(arrayBody[1+i+pageInfo.absRowNoCurPage()]);
    	    }
    	    //long end1 =System.currentTimeMillis();
    	    //System.out.println("循环用时:"+(end1-start));
          }else{
            for (int i = 0; arrayBody != null && i < arrayBody.length; i++) {
              out.print(arrayBody[i]);
            }
          }
        %>
        </table>
        </div>
        </td>
      </tr>
    </table>
	<%} %>
	</div>
	<!--报表显示 end-->

<INPUT TYPE="hidden" id="qry__order_code" name="qry__order_code" value="<%=qryStruct.order_code%>" />
<INPUT TYPE="hidden" id="qry__order" name="qry__order" value="<%=qryStruct.order%>" />
<INPUT TYPE="hidden" id="qry__expandcol" name="qry__expandcol" value="<%=qryStruct.expandcol%>" />
<INPUT TYPE="hidden" id="opType" name="opType" value="">
<INPUT TYPE="hidden" id="qry__first_view" name="qry__first_view" value="">
<INPUT TYPE="hidden" id="preview" name="preview" value="">

</div>
</FORM>
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

function _fnSubmit(start_date){
  if( (start_date==99)&& (document.all.qry__date_s.value>document.all.qry__date_e.value)){
    alert("起始日期大于终止日期！");
    return;
  }
  document.all.qry__first_view.value = "N";
  if(typeof(frmEdit.page__iCurPage) != "undefined") {
	  frmEdit.page__iCurPage.value="";
	  frmEdit.page__iLinesPerPage.value="";
	  frmEdit.page__iLines.value="";
	  frmEdit.page__iPages.value="";
	  frmEdit.page__checkIDs.value="";
  }
  document.frmEdit.submit();
}
function _fnSubmitPre(start_date){
	if( (start_date==99)&& (document.all.qry__date_s.value>document.all.qry__date_e.value)){
	    alert("起始日期大于终止日期！");
	    return;
	}
	document.all.preview.value = "Y";
	if(typeof(frmEdit.page__iCurPage) != "undefined") {
		frmEdit.page__iCurPage.value="";
		frmEdit.page__iLinesPerPage.value="";
		frmEdit.page__iLines.value="";
		frmEdit.page__iPages.value="";
		frmEdit.page__checkIDs.value="";
	}
	document.frmEdit.submit();
}
function _order(code,order){
  document.all.qry__order_code.value = code;
  document.all.qry__order.value = order;
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
function _editRptHead(){
  var optstr = "height=600,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no"
  newsWin = window.open("loadReportHead.rptdo?report_id=<%=rptTable.rpt_id%>","editRptHead",optstr);
  if(newsWin!=null)
    newsWin.focus();
}
function _exportSubmit(){
  var optstr = "height=600,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no"
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
function _SmsUser(){
  var optstr = "height=600,width=800,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=no,resizable=no"
  newsWin = window.open("../report/reportsmsuser.jsp?rpt_id=<%=rptTable.rpt_id%>","smsuser",optstr);
  if(newsWin!=null)
    newsWin.focus();
}
function _SmsSend(){
  var d_number = document.frmEdit.deviceNumber.value;
  var s_sms = document.frmEdit.message.value;
  if(d_number==""){
    alert("人员号码不能为空！");
    return;
  }
  var arrnumbers = d_number.split(";");
  for(i=0; i < arrnumbers.length; i++){
    var tmp_number = arrnumbers[i];
  	if(tmp_number.length!=11){
      alert("手机号码为11位！");
      return;
    }
    var d_svc = tmp_number.substring(0,3);
    if(d_svc!=130&&d_svc!=131&&d_svc!=132&&d_svc!=133){
      alert("请输入手机号码！");
      return;
    }
  }
  if(s_sms==""){
    alert("短信内容不能为空！");
    return;
  }
  document.frmEdit.opType.value = "rptsend";
  document.frmEdit.action = "sendSms.do";
  document.frmEdit.submit();
}
</script>
</html>