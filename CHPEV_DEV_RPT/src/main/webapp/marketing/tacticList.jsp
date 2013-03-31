<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.*"%>
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
	<%
		String decider = (String)request.getAttribute("decider");
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(null==qryStruct.tactic_creator){
			qryStruct.tactic_creator="";
		}
		if(null==qryStruct.tactic_name){
			qryStruct.tactic_name="";
		}
		//获得营销策略类型
		String tactictypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TACTIC_TYPE'";
		//获得审批意见类型
		String tactictStateSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_APPROVAL_TYPE'";
		List<TacticInfo> list =(List<TacticInfo>) request.getAttribute("tacticList");
	%>
<script language="javascript">

//: 判断网页是否加载完成
 document.onreadystatechange = function () {
   if(document.readyState=="complete") {
	   <%
	   String msg = (String)request.getAttribute("delectMsg");
	   if(null!=msg){
	   	%>
	   	alert('<%=msg%>');
	   	<%
	   }
	   %>
   }
}
	function mySubmit(op) {
		form1.action="tacticAction.rptdo?optype=tacticList&doType="+op+"&decider=<%=decider%>";
		form1.submit();
	}
	function myAdd(op) {
		form1.action="tacticAction.rptdo?step=cancel&optype=tacticAddStep1&doType="+op;
		form1.submit();
	}
	function myModify(op) {
		var id = 0;
		var state = 0;
		var arr = document.getElementsByName("checkbox");
		var arrState = document.getElementsByName("hiddenState");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				checlCount++;
			}
		}
		if(checlCount==0){
			alert("你没有选择任何的信息！");
		}else if(checlCount>1)	{
			alert("每次只能编辑一条信息！");
		}else{
			arr = document.getElementsByName("checkbox");
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					id=arr[i].value;
					state = arrState[i].value;
				}
			}


			if(state!=0){
				alert("该信息已经进入审批环节，不能再修改！！");
			}else{
				form1.action="tacticAction.rptdo?optype=tacticAddStep1&doType=modify&tactictId="+id;
				form1.submit();
			}
		}
	}
	function myDelect(op) {
		var arr = document.getElementsByName("checkbox");
		var checlCount = 0;
		for(var i =0;i< arr.length;i++ ){
			if(arr[i].checked){
				checlCount++;
			}
		}

		if(checlCount==0){
			alert("你没有选择任何的信息！");
		}else if(checlCount>1)	{
			alert("每次只能删除一条信息！");
		}else{
			if(confirm("是否确定删除选中的"+checlCount+"条信息？")){
				form1.action="tacticAction.rptdo?optype=tacticList&doType="+op;
				form1.submit();
			}
		}
	}
	</script>

</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp()">
<form name="form1" method="post" action="">
  <div id="maincontent">
    <div class="toptag" > 您所在位置：营销策划 >> 营销策略 >> <em class="red">营销策略管理</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span> </div>
    <div class="topsearch">
      <table width="100%">
        <tr>
          <td align="right" width="15%">策略名称： </td>
          <td width="18%"><input id=qry__tactic_name name=qry__tactic_name class="txtinput" value="<%=qryStruct.tactic_name %>" />
          </td>
          <td align="right" width="15%"> 策略类型： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry__tactic_type" listID="0" allFlag="" selfSQL="<%=tactictypeSql%>" />
          </td>
          <td align="right" width="15%">审批状态： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.tactic_state%>" script="class='easyui-combobox'"	listName="qry__tactic_state" listID="0" allFlag="" selfSQL="<%=tactictStateSql %>" />
          </td>
        </tr>
        <tr>
          <td align="right" width="15%">创建人：  </td>
          <td><input id="qry__tactic_creator" name="qry__tactic_creator" class="txtinput" value="<%=qryStruct.tactic_creator %>" /></td>
          <td align="right" width="15%">  </td>
          <td></td>
          <td align="right" width="15%"></td>
          <td>
                <button class="btn3" type="button" onClick="mySubmit('search')"> 查 询 </button>
       	  </td>
        </tr>
      </table>
    </div>
     <div class="topsearch_btn"> <span>
<%if(null==decider){
	%>
	  <button class="btn3" type="button" onClick="myAdd('add')"> 新 建 </button>
      <button class="btn3" type="button" onClick="myModify('modify')"> 修 改 </button>
      <button class="btn3" type="button" onClick="myDelect('delect')">删 除 </button>
	<%

} %>

	     
    </span> </div>
    <div class="list_content">
      <table >
        <tr width="100%">
          <th width="8%" align="center"> 选  择 </th>
          <th width="12%" align="center"> 策略名称 </th>
          <th width="10%" align="center"> 策略类型 </th>
          <th width="20%" align="center"> 策略描述 </th>
          <th width="10%" align="center"> 审批状态 </th>
          <th width="10%" align="center"> 创建人</th>
          <th width="10%" align="center"> 创建时间</th>
          <th width="10%" align="center"> 生效时间</th>
          <th width="10%" align="center"> 失效时间</th>
        </tr>
         <% if(null!=list)
        	{
        		for(int i = 0 ;i<list.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
          <td align="center"><label>
            <input type="checkbox" id="checkbox_<%=list.get(i).getTacticId() %>" name="checkbox" value="<%=list.get(i).getTacticId() %>">
          </label></td>

          <td align="center">
            <input type="hidden" name="hiddenState" value="<%=list.get(i).getState()%>">
           <a href=" tacticAction.rptdo?optype=tacticPass&doType=modify&tactictId=<%=list.get(i).getTacticId() %>" ><font color="blue">
          <%=list.get(i).getTacticName() %></font></a></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_TACTIC_TYPE",String.valueOf(list.get(i).getTacticType()))  %></td>
          <td align="center"><%=list.get(i).getContent() %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_APPROVAL_TYPE",String.valueOf(list.get(i).getState()))  %> </td>
          <td align="center"><%=list.get(i).getCreator() %></td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i).getCreateDate()) %> </td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i).getStartDate()) %></td>
          <td align="center" class="last"><%=CommonFormate.dateFormate(list.get(i).getEndDate()) %></td>
        </tr>	<%
        		}
        	}%>
      </table>
    </div>
  </div>
  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
</form>
</body>
</html>
