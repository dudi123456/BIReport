<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.common.app.DateUtil"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<html>
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/css.css">
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<%
String context = request.getContextPath();
String strDate = DateUtil.dateToString(DateUtil.getNowDate(),"yyyyMMddHmmss");
%>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//用户信息
String oper_no = CommonFacade.getLoginId(session);
//查询条件
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
if(qryStruct==null){
	qryStruct = new ReportQryStruct();
}
//报表基本信息
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE);
//验证需要打印报表ID
String verify_rpt_id = request.getParameter("rpt_id");
if(rptTable==null||!rptTable.rpt_id.equals(verify_rpt_id)){
	out.print("<center>");
	out.print("<br><br>此打印报表信息有误，可能你的操作信息丢失，请重新查询确定你需要打印的报表信息！<br>");
	out.print("<input type=\"reset\" name=\"close_win\" class=\"button\" onMouseOver=\"switchClass(this)\" onMouseOut=\"switchClass(this)\" value=\"返回\" onClick=\"javascript:window.close();\"> ");
	out.print("</center>");
	return;
}
//---------------------------报表显示信息 start----------------------------
//报表标题内容
String strTitle = (String)session.getAttribute(WebKeys.ATTR_REPORT_TITLE_HTML); 

//报表表体内容
String[] arrayBody = (String[])session.getAttribute(WebKeys.ATTR_REPORT_BODY_HTML);

//报表的审核内容
String processBody = (String)session.getAttribute(WebKeys.ATTR_REPORT_PROCESS_HTML);
//---------------------------报表显示信息 end  ----------------------------
%>

<head>
<title>报表打印</title>
<!-- special style sheet for printing -->
<style media="print">
.noprint     { display: none }
</style>
<script defer>
var code = "<%=strDate%>";
function viewinit() {
  window.resizeTo(window.screen.width,window.screen.height-30);window.moveTo(0,0);
  if (!factory.object) {
    return
  } else {
    factory.printing.header = ""
    factory.printing.footer = "&b- &p -&b"
    factory.printing.portrait = false
    factory.printing.leftMargin = 10.75
    factory.printing.topMargin = 15.75
    factory.printing.rightMargin = 10.75
    factory.printing.bottomMargin = 5.75
    // enable control buttons
    var templateSupported = factory.printing.IsTemplateSupported();
    var controls = idControls.all.tags("input");
    for ( i = 0; i < controls.length; i++ ) {
      controls[i].disabled = false;
      if ( templateSupported && controls[i].className == "ie55" )
        controls[i].style.display = "inline";
      }
    }
}
function printPage(){
  rnd = Math.ceil(Math.random() * 1000000); //得到随机数
  code += ""+"<%=oper_no%>"+rnd;
  
  var rpt_id = "<%=rptTable.rpt_id%>";
  var rpcUrl = "rptPrint.rptdo?rpt_id="+rpt_id+"&code="+code+"&opType=print";
  var params=[];
  var pos=rpcUrl.indexOf("?");
  if(pos>=0){
    var param=rpcUrl.substring(pos+1);
    rpcUrl=rpcUrl.substring(0,pos);
    params=param.split("&");
  }
  var ajaxHelper=new net.ContentLoader(rpcUrl,params,updateChartContent,ajaxError);
  ajaxHelper.sendRequest();
}

  //加载返回的维度值
function updateChartContent(){
  var jsonTxt=this.req.responseText;
  if(jsonTxt){
    jsonTxt=eval(jsonTxt);
    if(jsonTxt){
      factory.printing.footer = "&b- &p -&b=="+code+"==";
      factory.printing.Print(false);
      print_btn.disabled = true;
      alert("报表提交打印！");
    }else{
      alert("打印失败！");
    }
  }
}
  //向服务器发起请求失败
function ajaxError(){
  alert("打印失败！");
}
</script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
</head>

<body scroll="auto" onload="viewinit()">
<!-- MeadCo ScriptX Control -->
<object id="factory" style="display:none" viewastext
classid="clsid:1663ed61-23eb-11d2-b92f-008048fdd814"
codebase="<%=context%>/js/smsx.cab#Version=6,3,434,12">
</object>
<br>
<div id=idControls class="noprint" align="center">
 <input id="print_btn" disabled type="button" value="打印报表"
 onclick="printPage()" class="button2" 
 onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" />
 <input disabled type="button" value="打印设置"
 onclick="factory.printing.PageSetup()" class="button2"
 onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" />
 <input disabled type="button" value="打印预览"
 onclick="factory.printing.Preview()" class="button2"
 onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" />
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
    <!--报表显示 start-->
    <table id="AutoNumber1" width="100%" border="0" cellpadding="0" cellspacing="0">
      <%=strTitle%>
    
	  <tr>
		<td colspan="2" class="tab-side2">
		<table width="100%" border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#999999">
	    <%
	      for (int i = 0; arrayBody != null && i < arrayBody.length; i++) {
              out.print(arrayBody[i]);
          }
        %>
        </table>
        </td>
      </tr>
    
      <%if(ReportConsts.YES.equals(rptTable.processflag)){ %>
      <tr>
        <td id="id2" height="2"></td>
      </tr>
      <tr>
        <td height="30" colspan="2"><%=processBody%></td>
      </tr>
      <%} %>
    </table>
    <!--报表显示 end-->
    </td>
  </tr>
  <%if(!"".equals(rptTable.rp_bottom)) {%>
  <tr>
  	<td>
  		<TABLE width="100%">
  			<%=rptTable.rp_bottom %>
  		</TABLE>
  	</td>
  </tr>
  <%} %>
  <tr>
    <td>
    <%=CommTool.getEditorHtmlPrint(rptTable.rpt_id,"0")%>
    </td>
  </tr>
</table>
</body>
</html>
