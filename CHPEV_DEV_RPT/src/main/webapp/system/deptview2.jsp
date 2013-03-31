<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="java.util.List"%>
<%@ page import="com.ailk.bi.workplatform.entity.UserInfo"%>
<%@ page import="waf.controller.web.action.HTMLActionException"%>
<%@ page import="waf.controller.web.action.HTMLActionSupport"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
   InfoOperTable loginUser = CommonFacade.getLoginUser(session);
   List <UserInfo> userList = (List <UserInfo>) request.getAttribute("userList");
%>


<HTML>
<HEAD>
<TITLE>操作员信息</TITLE>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/syscss.css" >
<SCRIPT src="<%=request.getContextPath()%>/js/picker/deptPicker.js"></SCRIPT>
<SCRIPT src="<%=request.getContextPath()%>/js/picker/areaPicker.js"></SCRIPT>
<script src="<%=request.getContextPath()%>/js/XmlRPC.js" language="javascript"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<SCRIPT language="javascript">
</SCRIPT>
<style>
	.coolText{behavior:url(../htc/coolText.htc);}
</style>
</HEAD>
<BODY  class="side-7">
<div class="list_content">
 <table >
            <tr width="100%">
              <th width="10%" align="center"> 选  择</th>
              <th width="20%" align="center"> 用户名称</th>
              <th width="30%" align="center"> 所在部门</th>
              <th width="20%" align="center"> 所在渠道</th>
              <th width="20%" align="center"> 公司职位</th>
              </tr>
 <%

 if(userList!=null){
	    for(int i=0;i<userList.size();i++){
	    	if(null!=loginUser.channleId){


	    	  if(loginUser.channleId.equals(String.valueOf(userList.get(i).getChannleInfo().getChannleId()))){
        			%>
        			 <tr><%
        				 %>
        		            <td align="center"><label>
        		            <input type="checkbox" id="checkbox_<%=userList.get(i).getUserId() %>" name="checkbox" value="<%=userList.get(i).getUserId() %>@@@<%=userList.get(i).getUserName() %>">
        		          </label></td>
        		           <td align="center"><%=userList.get(i).getUserName() %></td>
        		           <td align="center"><%=userList.get(i).getDeptInfo().getDeptName() %></td>
        		           <td align="center"><%=userList.get(i).getChannleInfo().getChannleName() %></td>
        		           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_DUTY_ID",String.valueOf(userList.get(i).getDutyId())) %></td>
        		        </tr>
        		        			<%
    				 }
	               }
        		}
        	}%>
          </table>
 </div>
</BODY>
</HTML>








