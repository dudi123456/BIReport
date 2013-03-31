<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="java.util.List"%>
    <%@page import="com.ailk.bi.marketing.entity.GroupInfo"%>
    <%@page import="com.ailk.bi.common.app.ReflectUtil"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<script type="text/javascript">
var checkResult =false;
function myDelete(id){
	form1.action = "groupAction.rptdo?optype=groupListForActivity&doType=removeForActivity&removeId="+id;
	form1.submit();
}
function checkNum(obj,num2){

	if(!isNaN(obj.value)){
		var iNum1 =parseInt(obj.value);
		var iNum2 = parseInt(num2);
		if(iNum1>iNum2){
			alert("你数如的数字超过包含客户数");
			obj.value=num2;
			checkResult =false;
		}else{
			checkResult =true;
		}
	}else{
		alert("您输入的不是数字类型");
		obj.value=num2;
		checkResult =false;
	}
}
</script>
</head>
<body style="font-size: 12px" >
 <form action="" method="post"  name="form1">
  <%
  	List<GroupInfo> groupList = (List<GroupInfo>) session.getAttribute("groups");
  %>
	 <div class="list_content">
   <table >
     <tr width="100%">
       <th width="8%" align="center"> 序号 </th>
         <th width="20%" align="center"> 客户群名称 </th>
          <th width="10%" align="center"> 客户群类型 </th>
           <th width="10%" align="center"> 客户群来源类型 </th>
            <th width="10%" align="center"> 所属品牌 </th>
             <th width="10%" align="center"> 提取数据状态 </th>
              <th width="10%" align="center"> 包含客户数 </th>
               <th width="10%" align="center"> 提取客户数 </th>
                <th width="10%" align="center"> 删除 </th>
     </tr>
<%
 	if(null!=groupList){
       		for(int i = 0 ;i<groupList.size();i++){
       			if(i%2!=0){
       				%>
       				<tr class="jg">
       				<%
       			}else{
       			%>
       			 <tr><%} %>
          <td align="center">客户群<%=i+1 %></td>
          <td align="center"><%=groupList.get(i).getGroupName() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_TYPE",String.valueOf(groupList.get(i).getGroupTypeId()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_CREATE_TYPE",String.valueOf(groupList.get(i).getCreateType()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_BRAND_OF",String.valueOf(groupList.get(i).getBrandOf()))  %> </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_GROUP_STATUS",groupList.get(i).getStatus())  %> </td>
          <td align="center"><%=String.valueOf(groupList.get(i).getCustomerCount())  %> </td>
          <td align="center"><input  onblur="checkNum(this,'<%=groupList.get(i).getCustomerCount()%>')" type="text" id="numCount" name="numCount" style="width: 50px"  value="<%=String.valueOf(groupList.get(i).numCount)%>"/></td>
        <td align="center"><a style= "cursor:hand" href="javascript:myDelete('<%=groupList.get(i).getGroupId()%>')">删除</a></td>
       </tr>	<%
       		}
       	}%>
     </table>
   </div>
</form>
</body>
</html>