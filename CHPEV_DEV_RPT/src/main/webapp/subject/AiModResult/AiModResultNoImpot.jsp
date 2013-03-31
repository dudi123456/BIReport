<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "com.ailk.bi.common.sysconfig.GetSystemConfig" %>
<% 
 List result_string = (List)session.getAttribute("result_string");
 List result_string2 = (List)session.getAttribute("result_string2");
 String yuyan = GetSystemConfig.getBIBMConfig().getYuyanUrl();
 List headlist = (List)session.getAttribute("head");
 List headlist2 = (List)session.getAttribute("head2list");
 String isadd = (String)session.getAttribute("isadd");
 int rowcount = 1;
 if(headlist.size()>0){
    rowcount = 2;
 }
 int rowcount2 = 1;
 if(headlist2.size()>0){
    rowcount2 = 2;
 }
%>
<html>
  <head>
    <title>预演结果分析</title>
  </head>
<style>
.content{
  font-family:Arial;
  font-size:12px;
  font-weight:bold;
  font-style:normal;
  text-decoration:none;
  color:#FF0000;
  line-height: 20px;
  padding: 2px;
}

.td1{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  text-decoration:none;
  color:#333333;
  text-align:center;
  border-top: 1px solid;
  border-top-color: #545454;  
  background-color:#F5F5F5;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
  border-left: 1px solid;
  border-left-color: #545454;
}

.td2{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  text-decoration:none;
  text-align:center;
  color:#333333;
  background-color:#F5F5F5;
  border-top: 1px solid;
  border-top-color: #545454;  
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
}
.td3{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  text-align:center;
  color:#333333;
  height:25px;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
  border-left: 1px solid;
  border-left-color: #545454;
}

.td4{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  text-align:center;
  font-weight:normal;
  text-decoration:none;
  color:#333333;
  height:25px;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
}

.result_title{
	height:31px;
	text-indent:10px;
	line-height:29px;
	margin-bottom:1px;
}
</style>
</style>
<script type="text/javascript">
function openModDetail1(mod_id){
    var url = "<%=yuyan%>/business/com.ailk.uchannel.precase.web.AiPreCaseAction?action=initPolicyQuery&detailflag=detailflag2&modSrcType=3&sessionSerialUID=-100&modId="+mod_id;
    window.open(url,"政策详细信息","height=600, width=1100, top=60, left=100, resizable=yes,scrollbars=yes");
}
function openModDetail2(mod_id){
    var url = "<%=yuyan%>/business/com.ailk.uchannel.precase.web.AiPreCaseAction?action=initPolicyQuery&detailflag=detailflag3&modSrcType=3&sessionSerialUID=-100&modId="+mod_id;
    window.open(url,"政策详细信息","height=600, width=1100, top=60, left=100, resizable=yes,scrollbars=yes");
}
</script>
  <body style="margin:20px 5px 0 5px">
  <form name="TableQryForm" action="AiModResult.rptdo" >
  <%if(isadd.equals("0")){ %>
    <div class="result_title">
      <span style="font-family:微软雅黑;font-size:15px;font-weight:bold;font-style:normal;text-decoration:none;color:#CC0000;">&nbsp;&nbsp;预演前结果</span>
    </div>
    <div style="border-top: 2px solid; border-top-color: #FF6600;"><span>&nbsp;</span></div>
     <% for(int j=0;j<result_string2.size();j++){ %>
     <span style="margin:0 0 0 10px;font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#CC0000;"><%= result_string2.get(j).toString().split("#&")[1]%></span>     
     <table width="95%" cellpadding="0" cellspacing="0" style="margin:0 0 0 10px">
     <tr>
          <td class='td1' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>适用省分</td>
          <td class='td2' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>现网政策名称</td>
		  <%if(rowcount2>1){%>
		  <td class='td2' colspan='<%=headlist2.size()%>' style='font-weight:bold;height:30px'>条件指标</td>
		  <%}else{%>
		  <td class='td2' style='font-weight:bold;height:30px'>条件指标</td>
		  <%}%>
		  <td class='td2' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>计算指标</td>
		  <td class='td2' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>计算公式</td>	
		  <td class='td2' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>现网政策出账用户(个)</td>
		  <td class='td2' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>现网政策出账收入(元)</td>
		  <td class='td2' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>现网政策佣金金额(元)</td>
		  <td class='td2' rowspan='<%=rowcount2%>' style='font-weight:bold;height:30px'>现网政策佣金占收比(%)</td>
      </tr>
      <%if(rowcount2>1){%>
      <tr>
         <%
           for(int i=0;i<headlist2.size();i++){
			Map result = (Map) headlist2.get(i); 
		 %>
		 <td class='td4' style='background-color:#F5F5F5;'><%=(String)result.get("BUSI_NAME")%></td>
		 <% } %>
      </tr>
      <%}%>
        <%= result_string2.get(j).toString().split("#&")[0] %>
     </table>
     <div>&nbsp;</div>
     <% } %>
     <% } %>
    <div class="result_title">
      <span style="font-family:微软雅黑;font-size:15px;font-weight:bold;font-style:normal;text-decoration:none;color:#CC0000;">&nbsp;&nbsp;本次预演结果</span>
    </div>
    <div style="border-top: 2px solid; border-top-color: #FF6600;"><span>&nbsp;</span></div>
     <% for(int j=0;j<result_string.size();j++){ %>
     <span style="margin:0 0 0 10px;font-family:Arial;font-size:13px;font-style:normal;text-decoration:none;color:#CC0000;"><%= result_string.get(j).toString().split("#&")[1] %></span>
     <table width="95%" cellpadding="0" cellspacing="0" style="margin:0 0 0 10px">
        <tr>
          <td class='td1' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>适用省分</td>
          <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>预演政策名称</td>
		  <%if(rowcount>1){%>
		  <td class='td2' colspan='<%=headlist.size()%>' style='font-weight:bold;height:30px'>条件指标</td>
		  <%}else{%>
		  <td class='td2' style='font-weight:bold;height:30px'>条件指标</td>
		  <%}%>
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>计算指标</td>
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>计算公式</td>	
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>预演政策出账用户(个)</td>
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>预演政策出账收入(元)</td>
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>预演政策佣金金额(元)</td>
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;height:30px'>预演政策佣金占收比(%)</td>
      </tr>
      <%if(rowcount>1){%>
      <tr>
         <%
           for(int i=0;i<headlist.size();i++){
			Map result = (Map) headlist.get(i); 
		 %>
		 <td class='td4' style='background-color:#F5F5F5;'><%=(String)result.get("BUSI_NAME")%></td>
		 <% } %>
      </tr>
      <%}%>
      <%= result_string.get(j).toString().split("#&")[0] %>
     </table>
     <div>&nbsp;</div>
     <% } %>
  </form>
  </body>
</html>
