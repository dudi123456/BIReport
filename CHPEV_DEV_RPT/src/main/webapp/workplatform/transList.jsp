<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.workplatform.entity.TransInfo"%>
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

ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		String showSlq ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TRANS_SHOW'";
		String stateSlq ="select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TRANS_STATE'";
		List<TransInfo> list =(List<TransInfo>) request.getAttribute("transList");
	%>
<script language="javascript">
	function mySubmit(){
		form1.action="transAction.rptdo?doType=search&optype=transList";
		form1.submit();
	}
	 function myLoad() {
		javascript:$("#d3").dialog('close');
	 	javascript:$('#d4').dialog('close');

	 }
	 var transId ="";
	 function opWindow(newTransId,oo) {
		 transId=newTransId;
		 if(oo=='0'){
				javascript:$("#d3").dialog('open');
			}else{
				alert("改申请您已经处理过，不能重复处理！");
			}
	 }
	 function  myCheck() {
		 var passResult = pp.form1.txt_passResult.value;
		 var passDesc = pp.form1.txt_passDesc.value;
		 if(passResult==""){
			 alert("请输入审批结果！");
			 return;
		 }else if(passDesc==""){
			 alert("请输入审批意见！");
			 return;
		 }else{
			var parement ="passResult="+passResult+"&passDesc="+passDesc+"&transId="+transId;
		    javascript:$('#d3').dialog('close');
		    form1.action="transAction.rptdo?optype=transList&doType=save&"+parement;
			form1.submit();
		 }

	 }
	 var userId="";
	 var userName="";
	 var myTransId="";
	 function myPassOpen(transId){
		 myTransId = transId;
		 javascript:$('#d4').dialog('open');
	}
	 function  myCheckD4()
	 {
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
			}
			else if(checlCount>1){
				alert("每次只能选择一位审批人！");
			}else{
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						var myselectItem = arr[i].value.split("@@@");
						userId = myselectItem[0];
						userName = myselectItem[1];
					}
				}
			if(confirm("是否确定提交给"+userName+"?")){
				javascript:$('#d4').dialog('close');
		    form1.action="transAction.rptdo?optype=transList&doType=pass1&myTransId="+myTransId+"&myPassUser="+userId;
		    form1.submit();
		     userId="";
			 userName="";
			 myTransId="";
			}
			}
	 }
 function myPassBack(myTransId){
	  form1.action="transAction.rptdo?optype=transList&doType=passBack&myTransId="+myTransId;
	  form1.submit();
 }
 function myPassModify(myTransId){
	 var url = "transAction.rptdo?optype=transAdd&doType=modify&myTransId="+myTransId;
	 window.open(url,'newwindow','height=600,width=1200,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
	}
</script>

</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp();myLoad()">

<jsp:include page="../marketing/processbar.jsp"></jsp:include>
<form ID="form1" name="form1" method="post" action="">
<%=WebPageTool.pageScript("form1","transAction.rptdo?doType=search&optype=transList")%>
<%
//报表翻页页数
int perPageCount =10;
int recordCount = list.size();
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
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
             <td align="right" width="15%"> 查看方式： </td>
          <td width="20%">
            <BIBM:TagSelectList focusID="<%=qryStruct.showType%>"  script="class='easyui-combobox'"	listName="showType" listID="0"   selfSQL="<%=showSlq %>" />
          </td>
            <td align="right" width="15%"> 状态： </td>
          <td width="18%">
      <BIBM:TagSelectList  focusID="<%=qryStruct.transState%>" script="class='easyui-combobox'"	listName="transState" listID="0" allFlag=""  selfSQL="<%=stateSlq %>" />
          </td>
          <td align="right" width="15%"></td>
          <td>
           <button class="btn3" type="button" onClick="mySubmit()"> 查 询 </button>
       	  </td>
        </tr>

      </table>
    </div>
      <div class="topsearch_btn"> <span>


    </span> </div>
<div class="list_content">
 <table >
            <tr width="100%">
              <th width="6%" align="center"> 派单流水号</th>
              <th width="10%" align="center"> 派单名称</th>
              <th width="15%" align="center"> 派单原因</th>
              <th width="7%" align="center"> 申请人</th>
              <th width="7%" align="center"> 审批人</th>
              <th width="12%" align="center"> 申请时间</th>
              <th width="7%" align="center"> 转派渠道</th>
              <th width="7%" align="center"> 客户类型</th>
              <th width="7%" align="center"> 派单状态</th>
              <th width="10%" align="center"> 操作</th>
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
           <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getTransId() %></td>
           <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getTransName() %></td>
           <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getTransUse() %></td>
            <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getCreator()!=null){
            	%>
            	<%=list.get(i+pageInfo.absRowNoCurPage()).getCreator().getUserName() %>
            	<%
            } %></td>
             <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getPassUser()!=null){
            	%>
            	<%=list.get(i+pageInfo.absRowNoCurPage()).getPassUser().getUserName() %>
            	<%
            } %></td>
             <td align="center"><%=sdf.format(list.get(i+pageInfo.absRowNoCurPage()).getTransDate())  %></td>

              <td align="center"><% if(list.get(i+pageInfo.absRowNoCurPage()).getNewChannle()!=null){
            	%>
            	<%=list.get(i+pageInfo.absRowNoCurPage()).getNewChannle().getChannleName() %>
            	<%
            } %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CLIENT_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getCustType())) %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TRANS_STATE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getState())) %></td>
       <td align="center">
       <% if("2".equals(qryStruct.showType)&&list.get(i+pageInfo.absRowNoCurPage()).getState()==0){
    	   %>
    	    <a href="javascript:opWindow('<%=list.get(i+pageInfo.absRowNoCurPage()).getTransId()%>','<%=list.get(i+pageInfo.absRowNoCurPage()).getState()%>')"><font color="blue">
       审批</font></a>
    	   <%
       }else if("1".equals(qryStruct.showType)&&list.get(i+pageInfo.absRowNoCurPage()).getState()==-1){
    	   %>
    	   <a href="javascript:myPassOpen('<%=list.get(i+pageInfo.absRowNoCurPage()).getTransId()%>')"><font color="blue">提交</font></a>
    	   <a href="javascript:myPassModify('<%=list.get(i+pageInfo.absRowNoCurPage()).getTransId()%>')"><font color="blue">修改</font></a>
    	   <%
       }else if("1".equals(qryStruct.showType)&&list.get(i+pageInfo.absRowNoCurPage()).getState()==0){
    	   %>
    	   <a href="javascript:myPassBack('<%=list.get(i+pageInfo.absRowNoCurPage()).getTransId()%>')"><font color="blue">撤回</font></a>
    	   <%
       }else if("1".equals(qryStruct.showType)&&list.get(i+pageInfo.absRowNoCurPage()).getState()==3){
    	   %>
    	   <a href="javascript:myPassOpen('<%=list.get(i+pageInfo.absRowNoCurPage()).getTransId()%>')"><font color="blue">重新提交</font></a>
    	   <a href="javascript:myPassModify('<%=list.get(i+pageInfo.absRowNoCurPage()).getTransId()%>')"><font color="blue">修改</font></a>
    	   <%
       }
       %>
 </td>
        </tr>
        			<%
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
<!-- 方案弹出模式窗口 -->
<div id="d3" class="easyui-dialog"  title="营销方案选择窗口" style="width:500px;height:300px;left:0px;top:0px;padding:4px;"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<iframe id="pp" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="<%=request.getContextPath()%>/workplatform/passdialog.jsp"></iframe><br>
</div>
	<div id="dlg-buttons">
		<a href="#" class="public-btn1" onClick="javascript:myCheck()"><span>选择</span></a>
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
</body>
</html>
