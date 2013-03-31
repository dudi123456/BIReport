<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.GroupInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>

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
<script language="javascript">
	function mySubmit() {
		form1.action="groupAction.rptdo?optype=groupList&doType=search";
		form1.submit();
	}
	function myAdd(op) {
		form1.action="activityAction.rptdo?optype=activityAdd&doType="+op;
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
			form1.action="activityAction.rptdo?optype=activityAdd&doType="+op+"&activityId="+id;
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
		if(checlCount>0){
			if(confirm("是否确定删除选中的"+checlCount+"条信息？")){
				form1.action="activityAction.rptdo?optype=activityList&doType="+op;
				form1.submit();
			}
		}else{
			alert("请至少选择一条要删除的信息！");
	    }
	}
	</script>
	<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(null==qryStruct.groupName)
		{
			qryStruct.groupName="";
		}
		//获得活动类型
		String groupTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_GROUP_TYPE'";
		//获得审批意见类型
		String groupCreateTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_GROUP_CREATE_TYPE'";
		//所属品牌
		String groupBrandSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_GROUP_BRAND_OF'";
		//提取数据状态
		String groupStatusTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_GROUP_STATUS'";

		List<GroupInfo> list =(List<GroupInfo>) request.getAttribute("groupList");

	%>
</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp();">
<form ID="form1" name="form1" method="post" action="">
<%=WebPageTool.pageScript("form1","activityAction.rptdo?optype=activityList&doType=search")%>
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
          <td align="right" width="15%">客户群名称： </td>
          <td width="18%"><input id=qry__groupName name=qry__groupName value="<%=qryStruct.groupName %>" class="txtinput"  />
          </td>
          <td align="right" width="15%"> 客户群类型： </td>
          <td width="18%">
           <BIBM:TagSelectList focusID="<%=qryStruct.groupType%>" script="class='easyui-combobox'"	listName="qry__groupType" listID="0" allFlag="" selfSQL="<%=groupTypeSql%>" />
          </td>
          <td align="right" width="15%">客户群来源类型： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.createType%>" script="class='easyui-combobox'"	listName="qry__createType" listID="0" allFlag="" selfSQL="<%=groupCreateTypeSql %>" />
          </td>
        </tr>
        <tr>
             <td align="right" width="15%"> 所属品牌： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.brandType%>" script="class='easyui-combobox'"	listName="qry__brandType" listID="0" allFlag="" selfSQL="<%=groupBrandSql %>" />
          </td>
            <td align="right" width="15%"> 提取数据状态： </td>
          <td width="18%">
          <BIBM:TagSelectList focusID="<%=qryStruct.status%>" script="class='easyui-combobox'"	listName="qry__status" listID="0" allFlag="" selfSQL="<%=groupStatusTypeSql %>" />
          </td>
          <td align="right" width="15%"></td>
          <td>
                <button class="btn3" type="button" onClick="mySubmit()"> 查 询 </button>
       	  </td>
        </tr>
      </table>
    </div>
     <div class="topsearch_btn"> <span>

<!-- 原来button -->

    </span> </div>
    <div class="list_content">
      <table >
        <tr width="100%">
          <th width="20%" align="center"> 客户群名称 </th>
          <th width="10%" align="center"> 客户群类型 </th>
          <th width="10%" align="center"> 客户群来源类型 </th>
           <th width="10%" align="center"> 所属品牌 </th>
            <th width="10%" align="center"> 提取数据状态 </th>
             <th width="10%" align="center"> 包含客户数 </th>
          <th width="30%" align="center"> 客户群特征描述</th>

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
          <td align="center">
            <a href="sellUserAction.rptdo?optype=sellUserInfo&doType=search&groupId=<%=list.get(i+pageInfo.absRowNoCurPage()).getGroupId() %>" ><font color="blue">
          <%=list.get(i+pageInfo.absRowNoCurPage()).getGroupName() %></font></a></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getGroupTypeId()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_CREATE_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getCreateType()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_BRAND_OF",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getBrandOf()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_STATUS",list.get(i+pageInfo.absRowNoCurPage()).getStatus())  %> </td>
          <td align="center"><%=String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getCustomerCount())  %> </td>
         <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getUserCharacter() %></td>
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
     <div id="div2"  style="border:0px solid green; width:100%; height:40px; position:absolute;  bottom:0px;"><%=WebPageTool.pagePolit(pageInfo)%></div>
</form>
</body>
</html>
