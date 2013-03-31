<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.QuestionInfo"%>
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
<script language="javascript">
	function mySubmit() {
		form1.action="surveyAction.rptdo?doType=search&optype=surveyList";
		form1.submit();
	}
	function mySave() {
		form1.action="surveyAction.rptdo?optype=surveyList&doType=save";
		form1.submit();
	}
	function goBack(){
		form1.action="surveyAddFrist.screen";
		form1.submit();
	}
	</script>
	<%
		List<QuestionInfo> list =(List<QuestionInfo>) session.getAttribute("questionList2");
	%>
</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp()">
<form name="form1" method="post" action="">

  <div id="maincontent">
     <div class="toptag" > 您所在位置：营销策划 >> 营销策略 >> <em class="red">问题管理</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span> </div>

    <div class="topsearch">

    </div>
<div class="validatebox-tabTD-title" ><center><h2>问卷调查信息维护第2/2步骤</h2></center></div>
    <div class="list_content">
      <table>
        <tr>
          <th width="5%" align="center"> 序号 </th>
          <th width="55%" align="center">问题</th>
          <th width="8%" align="center">问题类型</th>
          <th width="12%" align="center">问题描述</th>
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
          <td align="center">NO.<%=i + 1%></td>
       	  <td align="left"><%=list.get(i).getContent()  %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_QUESTION_TYPE",String.valueOf(list.get(i).getQuestionType()))  %></td>
          <td align="center"><%=list.get(i).getDesc() %></td>
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
<div class="buttonArea">
    <button class="btn4" type="button" onClick="mySave()"> 保 存 </button>
    <button class="btn4" type="button" onClick="goBack()"> 上一步 </button>
    <button class="btn4" type="button" onClick="mySubmit()"> 取 消 </button>
</div>
</form>
</body>
</html>
