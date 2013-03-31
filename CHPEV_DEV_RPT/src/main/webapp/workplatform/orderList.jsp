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


		if(null==qryStruct.custName)
		{
			qryStruct.custName="";
		}
		if(null==qryStruct.servNumber)
		{
			qryStruct.servNumber="";
		}

		if(null==qryStruct.orderCusSvcMgr)
		{
			qryStruct.orderCusSvcMgr="";
		}
		if(null==qryStruct.orderCusSvcMgr)
		{
			qryStruct.orderCusSvcMgr="";
		}
		if(null==qryStruct.activityName){
			qryStruct.activityName="";
		}
		if(null==qryStruct.activityId){
			qryStruct.activityId="";
		}

		//获得工单状态
		String	orderStateSql =  "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ORDER_STATE'";
		//获得客户等级
		String orderLevelSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_ORDER_LEVEL'";
		//是与否
		String trueSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TRUE' order by t.code_id desc";

		List<OrderInfo> list =(List<OrderInfo>) request.getAttribute("orderList");
	%>
<script language="javascript">
	function myopWindow(id,custId){
		var url = "orderInfoAction.rptdo?optype=orderInfo&doType=searchOne&orderId="+id+"&custId="+custId;
		window.open(url,'newwindow','height=600,width=900,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
	}
	function mySubmit() {
		form1.action="orderInfoAction.rptdo?optype=orderList&doType=search";
		form1.submit();
	}
	function myDoFunction(){

			if(form1.document.getElementById("txt_userName").value==""){
				alert("请选择渠道执行人！");
			}else{
				form1.action="orderInfoAction.rptdo?optype=orderList&doType=setPerformer";
				form1.submit();
			}
	}
	 function myLoad()
	 {
		 javascript:$("#d3").dialog('close');
		 javascript:$("#d4").dialog('close');
	     javascript:$("#d5").dialog('close');

	 	 if(<%=qryStruct.contact2%>==0){
	 		 document.getElementById("txt_date03").value="";
	 		 document.getElementById("txt_date03").disabled="disabled";
	 		document.getElementById("txt_date04").value="";
	 		 document.getElementById("txt_date04").disabled="disabled";
	 	 }
	 	 if(<%=qryStruct.date1==null%>){
	 		 document.getElementById("txt_date01").value="";
	 	 }
	 	 if(<%=qryStruct.date2==null%>){
	 		 document.getElementById("txt_date02").value="";
	 	 }
	 	if(<%=qryStruct.date3==null%>){
	 		 document.getElementById("txt_date03").value="";
	 	 }
	 	if(<%=qryStruct.date4==null%>){
	 		 document.getElementById("txt_date04").value="";
	 	 }
	 }
	 function opWindow(oo)
	 {
		 if(oo=='3'){
				pp.form1.action="activityAction.rptdo?optype=searchActivitydialog&doType=search";
				pp.form1.submit();
		     	javascript:$('#d3').dialog('open');
		 }else if(oo=='4'){
			 var arr  = new Array();
				arr = document.getElementsByName("OrderCheckbox");
				var checlCount = 0;
					for(var i =0;i< arr.length;i++ ){
						if(arr[i].checked){
							checlCount=checlCount+1;
						}
					}
					if(checlCount==0){
						alert("你没有选择任何的信息！");
					}else{
			 javascript:$('#d4').dialog('open');
					}
		 }else if(oo=="5"){
			 var arr  = new Array();
				arr = document.getElementsByName("OrderCheckbox");
				var checlCount = 0;
					for(var i =0;i< arr.length;i++ ){
						if(arr[i].checked){
							checlCount=checlCount+1;
						}
					}
					if(checlCount==0){
						alert("你没有选择任何的信息！");
					}else{
			 javascript:$('#d5').dialog('open');
					}
		 }

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
	 var userIds="";
	 var userNames="";
	 function  myCheckD4()
	 {  myClear();
		var arr  = new Array();
		arr = uu.DeptView.document.getElementsByName("checkbox");
		var checlCount = 0;
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					checlCount=checlCount+1;
				}
			}
			if(checlCount==0){
				alert("你没有选择任何的信息！");
			}else{
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						var myselectItem = arr[i].value.split("@@@");
						userIds += myselectItem[0]+",";
						userNames += myselectItem[1]+",";
					}
				}
		    if(userNames.charAt(userNames.length-1)==','){
		    	userNames=userNames.substring(0, userNames.length-1);
		    }
		    if(userIds.charAt(userIds.length-1)==','){
		    	userIds=userIds.substring(0, userIds.length-1);
		    }
			form1.document.getElementById("txt_userName").value=userNames;
			form1.document.getElementById("myUserId").value=userIds;
			javascript:$('#d4').dialog('close');
			}
	 }
	 function myClear(){
		 userNames="";
		 userIds="";
		 form1.document.getElementById("txt_userName").value="";
		 form1.document.getElementById("myUserId").value="";
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
	 function myChange(oo,state){
		 if(1!=state){
			 alert("对不起，只能选择‘待办’状态的工单！");
			 oo.checked=false;
		 }
	 }

	 function  myCheckD5(){
		 var transName = cc.form1.txt_transName.value;
		 var custType = cc.form1.txt_custType.value;
		 var channleId = cc.form1.txt_channleId.value;
		 var content = cc.form1.txt_content.value;
		 if(transName==""){
			 alert("请输入转派名称！");
			 return;
		 }else if(content==""){
			 alert("请输入申请理由！");
			 return;
		 }else{
			 var parement ="transName="+transName+"&custType="+custType+"&channleId="+channleId+"&content="+content;
		    javascript:$('#d5').dialog('close');
		    form1.action="orderInfoAction.rptdo?optype=orderList&doType=saveTrans&"+parement;
			form1.submit();
		 }
	 }

	 function addToTrans(){
		 var arr  = new Array();
			arr = document.getElementsByName("OrderCheckbox");
			var checlCount = 0;
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						checlCount=checlCount+1;
					}
				}
				if(checlCount==0){
					alert("你没有选择任何的信息！");
				}else{
					 form1.action="transAction.rptdo?optype=transAdd&doType=addOrder";
					 form1.submit();
				}

	 }
	 function returnToTrans(){
		 var url = "<%=request.getContextPath()%>/workplatform/transAdd.jsp";
		 window.open(url,'newwindow','height=600,width=1200,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
		}
</script>

</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp();myLoad()">

<jsp:include page="../marketing/processbar.jsp"></jsp:include>
<form ID="form1" name="form1" method="post" action="">
<input type="hidden" id="myUserId" name="myUserId" />
<input type="hidden" id="outChannle" name="outChannle" value="<%=qryStruct.outChannle %>"/>
<input type="hidden" id="setType" name="setType" value="<%=qryStruct.setType %>"/>
<%=WebPageTool.pageScript("form1","orderInfoAction.rptdo?optype=orderList&doType=search")%>
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
          <td align="right" width="15%">客户名称： </td>
          <td width="18%"><input id=qry__order_custName name=qry__order_custName value="<%=qryStruct.custName %>" class="txtinput"  />
          </td>
          <td align="right" width="15%"> 服务号码： </td>
          <td width="18%">
          <input id=qry__order_servNumber name=qry__order_servNumber value="<%=qryStruct.servNumber %>" class="txtinput"  />
          </td>
          <td align="right" width="15%">工单状态： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.orderState%>" script="class='easyui-combobox'"	listName="qry__order_state" listID="0" allFlag="" selfSQL="<%=orderStateSql %>" />
          </td>
        </tr>

        <tr>
 <td align="right" width="15%"> 活动名称： </td>
          <td width="20%">
          <input type="hidden" value="<%= qryStruct.activityId %>" id="qry__activityID" name="qry__activityID">
           <input id=txt_activityName name=txt_activityName value="<%=qryStruct.activityName %>" class="txtinput"   readonly="readonly">
           <input  type="button" class="public-btn2" value="选择" onclick="opWindow('3')" />
           <input  type="button" class="public-btn2" value="清空" onclick="moveActivity()" />
          </td>
          <td align="right" width="15%"> 服务经理： </td>
          <td width="18%">
          <input id=qry__order_cusSvcMgr name=qry__order_cusSvcMgr value="<%=qryStruct.orderCusSvcMgr %>" class="txtinput"  />
          </td>
          <td align="right" width="15%">客户等级： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.orderlevel%>" script="class='easyui-combobox'"	listName="qry__order_level" listID="0" allFlag="" selfSQL="<%=orderLevelSql %>" />
          </td>

        </tr>

        <tr>
        <td align="right" width="15%">工单编号：</td>
          <td width="18%"><input id=qry__order_orderID name=qry__order_orderID value="" class="txtinput"  /> </td>
            <td align="right" width="15%"> 一次接触开始时间： </td>
          <td width="18%">
          <input value="<%=qryStruct.date1 %>" id="txt_date01" name="txt_date01" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
          </td>
          <td align="right" width="15%">一次接触结束时间：</td>
          <td>
           <input value="<%=qryStruct.date2 %>" id="txt_date02" name="txt_date02" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">

       	  </td>

        </tr>

         <tr>
             <td align="right" width="15%"> 是否再次接触： </td>
          <td width="18%" >
           <BIBM:TagSelectList focusID="<%=qryStruct.contact2%>"  script="style='width:150px'onchange=isContcat(this.value)"
            listName="txt_contact2" listID="0"  selfSQL="<%=trueSql%>" />
          </td>
          </td>
            <td align="right" width="15%"> 再次接触开始时间： </td>
          <td width="18%">
          <input value="<%=qryStruct.date3 %>" !disabled id="txt_date03" name="txt_date03" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
          </td>
          <td align="right" width="15%">再次接触结束时间：</td>
          <td>
           <input value="<%=qryStruct.date4 %>" !disabled id="txt_date04" name="txt_date04" type="text" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">

       	  </td>
        </tr>

      </table>
    </div>
    <div class="topsearch_btn"> <span>

    <button class="btn4" type="button" onClick="mySubmit()"> 查 询 </button>
    <%if("4".equals(qryStruct.setType)) {
    	%>
    	 <button class="btn4" type="button" onClick="opWindow('5')"> 跨渠道转派 </button>
    	<%
    }%>
   <%if("5".equals(qryStruct.setType)) {
    	%>
    	 <button class="btn4" type="button" onClick="addToTrans()"> 添加到转派 </button>
    	 <button class="btn4" type="button" onClick="returnToTrans()"> 返回到转派修改页面 </button>
    	<%
    }%>
    </span> </div>
    <div class="list_content">
      <table >
        <tr width="100%">
          <th width="5%" align="center"> 选 择</th>
          <th width="5%" align="center"> 工单编号</th>
          <th width="8%" align="center"> 工单状态 </th>
          <th width="8%" align="center"> 活动名称 </th>
          <th width="8%" align="center"> 客户名称 </th>
          <th width="10%" align="center"> 服务号码 </th>
          <th width="8%" align="center"> 执行人 </th>
          <th width="8%" align="center"> 执行渠道 </th>
          <th width="8%" align="center"> 原执行渠道 </th>
          <th width="8%" align="center"> 客户等级</th>
          <th width="8%" align="center"> 生成时间</th>
  		  <th width="8%" align="center"> 第一次接触时间</th>
          <th width="8%" align="center"> 第二次接触时间</th>
        </tr>
         <%   if(perPageCount>0){
      	    for(int i=0;i<pageInfo.iLinesPerPage && (1+i+pageInfo.absRowNoCurPage())<=pageInfo.iLines;i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
        <td align="center"><label>
            <input type="checkbox"  onclick="myChange(this,'<%=list.get(i+pageInfo.absRowNoCurPage()).getOrder_state()%>')" id="checkbox_<%=list.get(i+pageInfo.absRowNoCurPage()).getOrder_no()%>" name="OrderCheckbox" value="<%=list.get(i+pageInfo.absRowNoCurPage()).getOrder_no() %>">
          </label></td>
          <td  align="center">
          <a href="javascript:myopWindow('<%=list.get(i+pageInfo.absRowNoCurPage()).getOrder_no()%>','<%=list.get(i+pageInfo.absRowNoCurPage()).getCust_id()%>')" ><font color="blue">
          <%=list.get(i+pageInfo.absRowNoCurPage()).getOrder_no() %></font></a></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ORDER_STATE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getOrder_state()))  %> </td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getActivityInfo().getActivityName()  %> </td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getCust_name()  %> </td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getServ_number()  %> </td>
          <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getPerformer_id()!=null){
        	  %>
        	  <%=list.get(i+pageInfo.absRowNoCurPage()).getPerformer_id().getUserName()%>
        	  <%
          } %> </td>
          <td align="center">
          <% if(list.get(i+pageInfo.absRowNoCurPage()).getChannelInfo()!=null){
        	  %>
        	  <%=list.get(i+pageInfo.absRowNoCurPage()).getChannelInfo().getChannleName()%>
        	  <%
          } %>
           </td>
            <td align="center">
          <% if(list.get(i+pageInfo.absRowNoCurPage()).getOldChannelInfo()!=null){
        	  %>
        	  <%=list.get(i+pageInfo.absRowNoCurPage()).getOldChannelInfo().getChannleName()%>
        	  <%
          } %>
           </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ORDER_LEVEL",list.get(i+pageInfo.absRowNoCurPage()).getCust_level())  %> </td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getCreatedate()) %></td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getContact_start_date()) %></td>
          <td align="center" class="last"><% if(list.get(i+pageInfo.absRowNoCurPage()).getNext_contact_date()!=null){
        	  %>
        	  <%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getNext_contact_date()) %>
        	  <%
          }%>
        	  </td>

        </tr>	<%
        		}
        	}%>
      </table>
    </div>
      <%=WebPageTool.pagePolit(pageInfo)%>
  </div>
  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>

</form>
<br>
<%  if(!"1".equals(qryStruct.setType)&&!"4".equals(qryStruct.setType)&&!"5".equals(qryStruct.setType))
		{
	%>
	提示：可以选择多个渠道执行人，进行工单任务的合理调配.
<input type="text" id="txt_userName" name="txt_userName">   &nbsp;  &nbsp;
 <button class="btn4" type="button" onClick="opWindow('4')">选择执行人</button>  &nbsp;
  <button class="btn4" type="button" onClick="myClear()"> 清  除</button>  &nbsp;
   <button class="btn4" type="button" onClick="myDoFunction()"> 确  定 </button>  &nbsp;
	<%
		}
%>

<!-- 方案弹出模式窗口 -->
<div id="d3" class="easyui-dialog"  title="营销方案选择窗口" style="width:800px;height:400px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="pp" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/workplatform/searchActivitydialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck('d3')"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d3').dialog('close')"><span>关闭</span></a></div>
<!-- 方案模式是窗口结束 -->
<!-- 用户弹出模式窗口 -->
<div id="d4" class="easyui-dialog"  title="渠道执行人选择窗口" style="width:800px;height:400px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="uu" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/system/deptmain2.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheckD4()"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d4').dialog('close')"><span>关闭</span></a></div>
<!-- 用户模式是窗口结束 -->
<!-- 渠道分配弹出模式窗口 -->
<div id="d5" class="easyui-dialog"  title="渠道转派" style="width:600px;height:300px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="cc" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/workplatform/channledialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheckD5()"><span>选择</span></a>
		<a href="#" class="public-btn1" onClick="javascript:$('#d5').dialog('close')"><span>关闭</span></a></div>
<!-- 渠道分配模式是窗口结束 -->
</body>
</html>
