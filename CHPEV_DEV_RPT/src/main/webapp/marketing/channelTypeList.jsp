<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.ChannleTypeInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
		    form1.action="channelManaTypeAction.rptdo?optype=channleTypeList&doType="+op;
		    form1.submit();
	    }
	    function myAdd(op) {
			form1.action="channelManaTypeAction.rptdo?optype=channleTypeAdd&doType="+op;
			form1.submit();
		}
	    function myModify(op) {
			var id = 0;
			var arr = document.getElementsByName("checkbox");
			var checlCount = 0;
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					checlCount=checlCount+1;
				}
			}
			if(checlCount==0){
				alert("你没有选择任任何的信息！");
			}else if(checlCount>1)	{
				alert("每次只能编辑一条信息！");
			}else{
				arr = document.getElementsByName("checkbox");
				for(var i =0;i< arr.length;i++ ){
					if(arr[i].checked){
						id=arr[i].value;
					}
				}
				form1.action="channelManaTypeAction.rptdo?optype=channleTypeAdd&doType="+op+"&channleTypeId="+id;
				form1.submit();
			}

		}
		function myDelect(op) {
			var arr = document.getElementsByName("checkbox");
			var checlCount = 0;
			for(var i =0;i< arr.length;i++ ){
				if(arr[i].checked){
					checlCount=checlCount+1;
				}
			}
			if(checlCount==0){
				alert("你没有选择任何的信息！");
			}else if(checlCount>1)	{
				alert("每次只能删除一条信息！");
			}else{
				if(confirm("是否确定删除选中的"+checlCount+"条信息？")){
					form1.action="channelManaTypeAction.rptdo?optype=channleTypeList&doType="+op;
					form1.submit();
				}
			}
		}
	</script>
    <%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
   		 if(null==qryStruct.channleType_name){
   			qryStruct.channleType_name="";
   		  }
   	    //获得渠道类型状态
 		String channelTypeStateSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CHANNEL_TYPE_STATE'";
		List<ChannleTypeInfo> list =(List<ChannleTypeInfo>) request.getAttribute("channleTypeList");
	%>
</head>
<body style="background-color:#f9f9f9">
<form name="form1" method="post" action="">
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >> 营销渠道 >> <em class="red">营销渠道类型列表</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">
      <button class="btn4"> 全部导出 </button>
       
      <button class="btn4"> 文件下载 </button>
    </span> </div>
    <div class="topsearch">
      <table width="100%">

        <tr>
          <td align="right" width="15%">渠道类型名称： </td>
          <td><input id="qry_channleType_name" name="qry_channleType_name" class="txtinput" value="<%=qryStruct.channleType_name%>" style="width:150px"/></td>
          <td align="right" width="15%">渠道类型状态： </td>
          <td><BIBM:TagSelectList focusID="<%=qryStruct.channleType_state%>" script="class='easyui-combobox'"	listName="qry_channleType_state" listID="0" allFlag="" selfSQL="<%=channelTypeStateSql%>" /></td>
          <td align="right" width="15%">&nbsp;</td>
          <td>
                 <button class="btn3"  type="button" onClick="mySubmit('search')"> 查 询 </button>
       	  </td>
        </tr>
      </table>
    </div>
    <div class="topsearch_btn"> <span>
      <button class="btn3"  type="button" onClick="myAdd('add')"> 新 建 </button>
       
      <button class="btn3"  type="button" onClick="myModify('modify')"> 修 改 </button>
       
      <button class="btn3"  type="button" onClick="myDelect('delect')"> 删 除 </button>
	     

    </span> </div>
    <div class="list_content">
      <table>
        <tr>
          <th width="10%" align="center"> 选择 </th>
          <th width="20%" align="center"> 渠道类型名称 </th>
          <th width="25%" align="center"> 渠道类型状态</th>
          <th width="25%" align="center">设置时间</th>
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
            <input type="checkbox" id="checkbox_<%=list.get(i).getChannleTypeId() %>" name="checkbox" value="<%=list.get(i).getChannleTypeId() %>">
          </label></td>
          <td align="center"> <%=list.get(i).getChannleTypeName() %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CHANNEL_TYPE_STATE",Integer.toString(list.get(i).getState()))  %></td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i).getSetTime()) %></td>
        </tr>
                    <%
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
