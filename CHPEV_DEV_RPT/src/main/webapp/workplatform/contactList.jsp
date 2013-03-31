<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.workplatform.entity.OrderInfo"%>
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
		List<ContactInfo> contactList = (List<ContactInfo>)request.getAttribute("contactList");
		if(null==qryStruct.qry_custName)
		{
			qryStruct.qry_custName="";
		}
		if(null==qryStruct.qry_servNumber)
		{
			qryStruct.qry_servNumber="";
		}
		if(null==qryStruct.qry_activityName)
		{
			qryStruct.qry_activityName="";
		}
		if(null==qryStruct.qry_activityId)
		{
			qryStruct.qry_activityId="";
		}
		//工单状态
		String orderTypeSlq ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ORDER_TYPE'";
		//访问性质
		String natureSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_NATURE'";
		//访问类型
		String intervienTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_INTERVIEW_TYPE'";
		//是与否
		String trueSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TRUE' order by t.code_id ";
		//未访问到原因
		String noreplyReasonSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_NOREPLY_REASON'";
		//是否有流失倾向
		String awaySql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_AWAY_TREND' order By code_id desc";
		//流失原因
		String awayReasonSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_AWAY_REASON'";
		//是否满意
		String pleasedSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_PLEASED_STATE'";
		//是否参加活动
		String interestSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_INTEREST'";
		//接触方式
		String contactModeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTAC_MODE'";
		List<OrderInfo> list =(List<OrderInfo>) request.getAttribute("orderList");
	%>
<script language="javascript">
	function myopWindow(id,custId){
		var url = "orderInfoAction.rptdo?optype=orderInfo&doType=searchOne&orderId="+id+"&custId="+custId;
		window.open(url,'newwindow','height=600,width=900,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
	}
	function mySubmit() {
		form1.action="contactAction.rptdo?optype=contactList&doType=searchAll";
		form1.submit();
	}
	 function myLoad()
	 {
		 form1.document.getElementById("qry__activityID").value="<%=qryStruct.qry_activityId%>";
		javascript:$("#d3").dialog('close');

	 }
	 function opWindow()
	 {
		pp.form1.action="activityAction.rptdo?optype=searchActivitydialog&doType=search";
		pp.form1.submit();
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
	 function isContcat(myValue){
			var tt = document.getElementById("txt_date03");
			var pp = document.getElementById("txt_date04");
			tt.disabled=!tt.disabled;
			pp.disabled=!pp.disabled;
			if(0==myValue){
				tt.value = "";
				pp.value = "";
			}else{
				var myDate = new Date();
				var yy = myDate.getFullYear();    //获取完整的年份(4位,1970-????)
				var mm =  myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
				var dd = myDate.getDate();
				if(mm<10){
					mm="0"+mm;
				}
				if(dd<10){
					dd="0"+dd;
				}
				myDate1=yy+"-"+mm+"-"+dd;
				tt.value = myDate1;
				pp.value = myDate1;
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
<input type="hidden" id="outChannle" name="outChannle" value="<%=qryStruct.outChannle %>"/>
<%=WebPageTool.pageScript("form1","contactAction.rptdo?optype=contactList&doType=searchAll")%>
<%
//报表翻页页数
int perPageCount =10;
int recordCount = contactList.size();
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
          <td align="right" width="15%">客户名称： </td>
          <td width="18%"><input id=qry_custName name=qry_custName value="<%=qryStruct.qry_custName %>" class="txtinput"  />
          </td>
          <td align="right" width="15%"> 服务号码： </td>
          <td width="18%">
          <input id=qry_servNumber name=qry_servNumber value="<%=qryStruct.qry_servNumber %>" class="txtinput"  />
          </td>
          <td align="right" width="15%">工单类型： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.qry_orderType%>" script="class='easyui-combobox'"	listName="qry_orderType" listID="0" allFlag="" selfSQL="<%=orderTypeSlq %>" />
          </td>
        </tr>

        <tr>
          <td align="right" width="15%">访问性质： </td>
          <td width="18%"><BIBM:TagSelectList  allFlag="" focusID="<%=qryStruct.qry_interviewNature%>" script="class='easyui-combobox'"	listName="qry_interviewNature" listID="0"  selfSQL="<%=natureSql%>" />
          </td>
          <td align="right" width="15%"> 访问类型： </td>
          <td width="18%">
         <BIBM:TagSelectList  focusID="<%=qryStruct.qry_interviewType%>" script="class='easyui-combobox'"	allFlag=""	listName="qry_interviewType" listID="0"  selfSQL="<%=intervienTypeSql%>" />
          </td>
          <td align="right" width="15%">接触方式： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.qry_contactMode%>" script="class='easyui-combobox'"	 script="class='easyui-combobox'"	listName="qry__order_level" listID="0" allFlag="" selfSQL="<%=contactModeSql %>" />
          </td>
        </tr>

        <tr>
             <td align="right" width="15%"> 活动名称： </td>
          <td width="20%">
           <input type="hidden" value="<%=qryStruct.qry_activityId%>" id="qry__activityID" name="qry__activityID">
           <input id=txt_activityName name=txt_activityName value="<%=qryStruct.qry_activityName %>" class="txtinput"   readonly="readonly">
           <input  type="button" class="public-btn2" value="选择" onclick="opWindow()" />
           <input  type="button" class="public-btn2" value="清空" onclick="moveActivity()" />
          </td>
          </td>
            <td align="right" width="15%"> 是否访问成功： </td>
          <td width="18%">
      <BIBM:TagSelectList  focusID="<%=qryStruct.qry_interviewState%>" script="class='easyui-combobox'"	listName="qry_interviewState" listID="0" allFlag=""  selfSQL="<%=trueSql%>" />
          </td>
          <td align="right" width="15%">是否满意：</td>
          <td>
          <BIBM:TagSelectList focusID="<%=qryStruct.qry_pleasedState%>" script="class='easyui-combobox'"		listName="qry_pleasedState" listID="0"  allFlag="" selfSQL="<%=pleasedSql%>" />
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
              <th width="7%" align="center"> 工单编号</th>
              <th width="7%" align="center"> 工单类型</th>
              <th width="7%" align="center"> 客户名称</th>
              <th width="7%" align="center"> 服务号码</th>
              <th width="7%" align="center"> 访问性质</th>
              <th width="7%" align="center"> 访问类型</th>
              <th width="7%" align="center"> 接触方式</th>
              <th width="10%" align="center"> 活动名称</th>
              <th width="7%" align="center"> 接触成功</th>
              <th width="11%" align="center"> 接触时间 </th>
              <th width="8%" align="center"> 接触效果 </th>
              <th width="20%" align="center"> 接触内容 </th>
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
           <td align="center"><%=contactList.get(i+pageInfo.absRowNoCurPage()).getOrder_no() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ORDER_TYPE",String.valueOf(contactList.get(i+pageInfo.absRowNoCurPage()).getOrder_type())) %></td>
           <td align="center"><%=contactList.get(i+pageInfo.absRowNoCurPage()).getCust_name() %></td>
           <td align="center"><%=contactList.get(i+pageInfo.absRowNoCurPage()).getServ_number() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTACT_NATURE",String.valueOf(contactList.get(i+pageInfo.absRowNoCurPage()).getInterview_nature())) %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTACT_INTERVIEW_TYPE",String.valueOf(contactList.get(i+pageInfo.absRowNoCurPage()).getInterview_type())) %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTAC_MODE",String.valueOf(contactList.get(i+pageInfo.absRowNoCurPage()).getContactMode())) %></td>
           <td align="center"><%=contactList.get(i+pageInfo.absRowNoCurPage()).getActivity_name() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TRUE",String.valueOf(contactList.get(i+pageInfo.absRowNoCurPage()).getInterview_state())) %></td>
           <td align="center"><%=contactList.get(i+pageInfo.absRowNoCurPage()).getContact_date() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTACT_PLEASED_STATE",String.valueOf(contactList.get(i+pageInfo.absRowNoCurPage()).getPleased_state())) %></td>
           <td align="center"><%=contactList.get(i+pageInfo.absRowNoCurPage()).getContact_content() %></td>
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
