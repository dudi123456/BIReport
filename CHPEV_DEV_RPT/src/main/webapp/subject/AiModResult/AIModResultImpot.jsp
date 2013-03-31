<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "com.ailk.bi.common.sysconfig.GetSystemConfig" %>
<% 
  String head1 = (String)session.getAttribute("head1");
  String head2 = (String)session.getAttribute("head2");
  String result2 = (String)session.getAttribute("result2");
  String param = (String)session.getAttribute("param");
  List result_string = (List)session.getAttribute("result_string");  
  String yuyan = GetSystemConfig.getBIBMConfig().getYuyanUrl();
  List headlist = (List)session.getAttribute("head");
  int rowcount = 1;
  if(headlist.size()>0){
    rowcount = 2;
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
  text-decoration:underline;
  text-align:right;
  color:#0000EE;
}

.td1{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  text-decoration:none;
  color:#333333;
  text-align:center;
  background-color:#F5F5F5;
  border-top: 1px solid;
  border-top-color: #545454;
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
  color:#333333;
  height:25px;
  text-align:center;
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
  font-weight:normal;
  text-decoration:none;
  color:#333333;
  height:25px;
  text-align:center;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
}

.iw{
	width:80%;
	position:absolute;
	right:6%;
	text-align:right;
}

.result_title{
	height:31px;
	text-indent:10px;
	line-height:29px;
	margin-bottom:1px;
}
</style>
<script>
   function goDetail(){
     var url="AiModResultDetail.rptdo?optype=select&param=<%=param%>";
     window.open(url,"佣金预演明细");
   }
   
   function openModDetail(mod_id){
    var url = "<%=yuyan%>/business/com.ailk.uchannel.precase.web.AiPreCaseAction?action=initPolicyQuery&detailflag=detailflag3&modSrcType=3&sessionSerialUID=-100&modId="+mod_id;
    window.open(url,"政策详细信息","height=600, width=1100, top=60, left=100, resizable=yes,scrollbars=yes");
   }
</script>
  <body style="margin:20px 5px 0 5px">
  <form name="AiModResultForm" action="AiModResult.rptdo">
  <div id="class1">
   <div class="result_title">
      <span style="font-family:微软雅黑;font-size:15px;font-weight:bold;font-style:normal;text-decoration:none;color:#CC0000;">&nbsp;&nbsp;政策信息</span>
   </div>
   <div style="border-top: 2px solid; border-top-color: #FF6600;"><span>&nbsp;</span></div>
     <table width="95%" cellpadding="0" cellspacing="0" style="margin:0 0 0 10px">
        <tr>
          <td class='td1' rowspan='<%=rowcount%>' style='font-weight:bold;' height="30px">省分</td>
          <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;' height="30px">现网政策名称</td>
		  <%if(rowcount>1){%>
		  <td class='td2' colspan='<%=headlist.size()%>' style='font-weight:bold;' height="30px">条件指标</td>
		  <%}else{%>
		  <td class='td2' style='font-weight:bold;' height="30px">条件指标</td>
		  <%}%>
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;' height="30px">计算指标</td>
		  <td class='td2' rowspan='<%=rowcount%>' style='font-weight:bold;' height="30px">计算公式</td>	
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
       <%for(int j=0;j<result_string.size();j++){ %>
           <%= result_string.get(j).toString().split("#&")[0] %>
       <%} %>
     </table> 
     </div>   
     <div>&nbsp;</div>
     <div id="class2">
     <div class="result_title">
        <span style="font-family:微软雅黑;font-size:15px;font-weight:bold;font-style:normal;text-decoration:none;color:#CC0000;">&nbsp;&nbsp;预演结果</span>
        <div class="iw"><input type="button" value="查看明细" onclick="goDetail()"/></div> 
     </div>
     <div style="border-top: 2px solid; border-top-color: #FF6600;"><span>&nbsp;</span></div>     
     <table width="95%" cellpadding="0" cellspacing="0" style="margin:0 0 0 10px">
	   <tr>
	     <td class='td1' style='font-weight:bold;' height="30px"><%=head1 %></td>
	     <td class='td2' style='font-weight:bold;' height="30px">业务活动</td>
	     <td class='td2' style='font-weight:bold;' height="30px">其中社会渠道<%=head2%></td>
	     <td class='td2' style='font-weight:bold;' height="30px">出账收入(元)</td>
         <td class='td2' style='font-weight:bold;' height="30px">佣金金额(元)</td>
         <td class='td2' style='font-weight:bold;' height="30px">佣金占收比</td>
       </tr>
       <%= result2 %>
     </table>
     </div>
     </div>
  </form>
  </body>
</html>
