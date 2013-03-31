<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.workplatform.entity.OrderAdjustInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.ailk.bi.workplatform.entity.ContactInfo"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>北京联通统一经营分析系统</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/patch.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<%
		SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		//工单状态
		String stateSlq ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ADJUST_STATE'";
		String userSlq ="SELECT t.user_id,t.user_name  from ui_info_user t";
		List<OrderAdjustInfo> list =(List<OrderAdjustInfo>) request.getAttribute("adjustList");
	%>
<script language="javascript">
	function myopWindow(id,custId){
		var url = "orderInfoAction.rptdo?optype=orderInfo&doType=searchOne&orderId="+id+"&custId="+custId;
		window.open(url,'newwindow','height=600,width=900,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
	}
	function mySubmit() {
		form1.action="adjustAction.rptdo?optype=adjustList&doType=search";
		form1.submit();
	}
	 function myLoad()
	 {
		javascript:$("#d3").dialog('close');

	 }
	 function opWindow()
	 {
     	javascript:$('#d3').dialog('open');
	 }
	 function  myCheck(oo)
	 {
		var arr  = new Array();
		arr = pp.document.getElementsByName("checkbox");
		var checlCount = 0;
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					checlCount=checlCount+1;
				}
			}
			if(checlCount==0){
				alert("你没有选择任何的信息！");
			}else if(checlCount>1)	{
				alert("每次只能选择一条信息！");
			}else{
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						tacticIdName=arr[i].value;
					}
				}
			var arr = tacticIdName.split("@@@");
			saveTacticId = arr[0];
			showTacticNam = arr[1];
			form1.document.getElementById("txt_activityName").value=showTacticNam;
			form1.document.getElementById("qry__activityID").value=saveTacticId;
			javascript:$('#d3').dialog('close');

			}
	 }


	 function moveActivity(){
		 document.getElementById("qry__activityID").value="";
		 document.getElementById("txt_activityName").value="";
	 }
</script>

</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp();myLoad()">

<jsp:include page="../marketing/processbar.jsp"></jsp:include>
<form ID="form1" name="form1" method="post" action="">
<%=WebPageTool.pageScript("form1","adjustAction.rptdo?optype=adjustList&doType=search")%>
<%
//报表翻页页数
int perPageCount =10;
int recordCount = list.size();
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, recordCount, perPageCount );
if (pageInfo != null) {
    out.print(WebPageTool.pageHidden(pageInfo));
}
%>
  <div id="maincontent">
    <div class="toptag" > 您所在位置：营销策划 >> 营销策略 >> <em class="red">营销活动管理</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span> </div>
    <div class="topsearch">
      <table width="100%">
        <tr>
             <td align="right" width="15%"> 执行人： </td>
          <td width="20%">
            <BIBM:TagSelectList  focusID="<%=qryStruct.adjustUser%>" script="class='easyui-combobox'"	listName="adjustUser" listID="0" allFlag=""  selfSQL="<%=userSlq%>" />
          </td>
            <td align="right" width="15%"> 创建人： </td>
          <td width="18%">
      <BIBM:TagSelectList  focusID="<%=qryStruct.adjustCreator%>" script="class='easyui-combobox'"	listName="adjustCreator" listID="0" allFlag=""  selfSQL="<%=userSlq%>" />
          </td>
          <td align="right" width="15%">状 态：</td>
          <td>
          <BIBM:TagSelectList focusID="<%=qryStruct.adjustState%>" script="class='easyui-combobox'" listName="adjustState" listID="0"  allFlag="" selfSQL="<%=stateSlq%>" />
       	  </td>
        </tr>

      </table>
    </div>
    <div class="topsearch_btn"> <span>

    <button class="btn3" type="button" onClick="mySubmit()"> 查 询 </button>

    </span> </div>
<div class="list_content">
 <table >
            <tr width="100%">
              <th width="10%" align="center"> 工单编号</th>
              <th width="12%" align="center"> 渠道名称</th>
              <th width="12%" align="center"> 原渠道名称</th>
              <th width="12%" align="center"> 执行人</th>
              <th width="12%" align="center"> 原执行人</th>
              <th width="12%" align="center"> 状态</th>
              <th width="12%" align="center"> 创建人</th>
              <th width="12%" align="center"> 更新时间</th>

              </tr>
 <%  if(perPageCount>0){
	    for(int i=0;i<pageInfo.iLinesPerPage && (1+i+pageInfo.absRowNoCurPage())<=pageInfo.iLines;i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
           <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getOrderId()%></td>
           <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getChannleInfo()!=null){
        	   %>
        	   <%=list.get(i+pageInfo.absRowNoCurPage()).getChannleInfo().getChannleName() %>
        	   <%
           } %></td>
           <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getChannleInfo()!=null){
        	   %>
        	   <%=list.get(i+pageInfo.absRowNoCurPage()).getChannleInfo().getChannleName() %>
        	   <%
           } %></td>
           <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getPerformer()!=null){
        	   %>
        	   <%=list.get(i+pageInfo.absRowNoCurPage()).getPerformer().getUserName() %>
        	   <%
           } %></td>
			 <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getOldperformer()!=null){
			        	   %>
			        	   <%=list.get(i+pageInfo.absRowNoCurPage()).getOldperformer().getUserName() %>
			        	   <%
			           } %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ADJUST_STATE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getStatus())) %></td>

            <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getUpdatePersonal()!=null){
			        	   %>
			        	   <%=list.get(i+pageInfo.absRowNoCurPage()).getUpdatePersonal().getUserName() %>
			        	   <%
			           } %></td>
           <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getUpdateTime() %></td>
        </tr>
        			<%
        		}
        	}%>
            <tr class="jg">
              <td align="center"><label></label></td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
          </table>
 </div>
      <%=WebPageTool.pagePolit(pageInfo)%>
  </div>
  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
</form>
<!-- 方案弹出模式窗口 -->
<div id="d3" class="easyui-dialog"  title="营销方案选择窗口" style="width:800px;height:400px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="pp" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="../workplatform/searchActivitydialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d3')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d3').dialog('close')"><span>关闭</span></a></div>
<!-- 方案模式是窗口结束 -->
</body>
</html>
