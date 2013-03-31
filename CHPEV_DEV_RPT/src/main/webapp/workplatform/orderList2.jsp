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
<%@page import="java.util.Map"%>
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
		Map<Integer,OrderInfo> map =(Map<Integer,OrderInfo>) session.getAttribute("orderMap");
%>
<script type="text/javascript">
function myDeleteWindow(delOrderId){
	 form1.action = "transAction.rptdo?optype=orderList2&doType=delOrder&delOrderId="+delOrderId;
	 form1.submit();
}

</script>
</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp();myLoad()">

<jsp:include page="../marketing/processbar.jsp"></jsp:include>
<form ID="form1" name="form1" method="post" action="">

  <div id="maincontent">
    <div class="list_content">
      <table >
        <tr width="100%">
          <th width="5%" align="center"> 工单编号</th>
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
          <th width="5%" align="center"> 删除</th>
        </tr>
         <%
 int i=0;
         if(null!=map){
        	 for(Integer o: map.keySet()){
        		 i++;
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>

          <td  align="center">
          <a href="javascript:myopWindow('<%=map.get(o).getOrder_no()%>','<%=map.get(o).getCust_id()%>')" ><font color="blue">
          <%=map.get(o).getOrder_no() %></font></a></td>
          <td align="center"><%=map.get(o).getActivityInfo().getActivityName()  %> </td>
          <td align="center"><%=map.get(o).getCust_name()  %> </td>
          <td align="center"><%=map.get(o).getServ_number()  %> </td>
          <td align="center"><% if(map.get(o).getPerformer_id()!=null){
        	  %>
        	  <%=map.get(o).getPerformer_id().getUserName()%>
        	  <%
          } %> </td>
          <td align="center">
          <% if(map.get(o).getChannelInfo()!=null){
        	  %>
        	  <%=map.get(o).getChannelInfo().getChannleName()%>
        	  <%
          } %>
           </td>
            <td align="center">
          <% if(map.get(o).getOldChannelInfo()!=null){
        	  %>
        	  <%=map.get(o).getOldChannelInfo().getChannleName()%>
        	  <%
          } %>
           </td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_ORDER_LEVEL",map.get(o).getCust_level())  %> </td>
          <td align="center"><%=CommonFormate.dateFormate(map.get(o).getCreatedate()) %></td>
          <td align="center"><%=CommonFormate.dateFormate(map.get(o).getContact_start_date()) %></td>
          <td align="center" ><% if(map.get(o).getNext_contact_date()!=null){
        	  %>
        	  <%=CommonFormate.dateFormate(map.get(o).getNext_contact_date()) %>
        	  <%
          }%>
        	  </td>
          <td align="center" class="last"> <a href="javascript:myDeleteWindow('<%=map.get(o).getOrder_no()%>')" ><font color="blue">删除</font></a></td>
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
