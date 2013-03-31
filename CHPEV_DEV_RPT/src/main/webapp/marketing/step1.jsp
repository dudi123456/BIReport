<%@page import="org.apache.hadoop.hive.ql.parse.HiveParser.limitClause_return"%>
<%@page import="org.apache.hadoop.hive.metastore.api.ThriftHiveMetastore.list_privileges_args"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.QuestionInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="com.ailk.bi.marketing.entity.NameListInfo"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<script type="text/javascript">
<%
List<QuestionInfo> list1 = (List<QuestionInfo>)session.getAttribute("questionList1");
List<QuestionInfo> list2 = (List<QuestionInfo>)session.getAttribute("questionList2");
%>
function toAdd(){
	form1.action="stepAction.rptdo?doStep=step1&optype=step1&doType=toAdd";
	form1.submit();
}
function toRemove(){
	form2.action="stepAction.rptdo?doStep=step1&optype=step1&doType=toRemove";
	form2.submit();
}
function toNext(){
	var tt = parent.document.getElementById("txt_survey_name").value;
	if(null==tt||tt=="")
		{alert("问卷名称不能为空！请填写");}
	else{
		parent.form1.action="stepAction.rptdo?doStep=step2&optype=step2";
		parent.form1.submit();
	}

}
function myCancel() {
	parent.form1.action="surveyAction.rptdo?doType=search&optype=surveyList";
	parent.form1.submit();
}
</script>
</head>
<body  >

<!-- 弹出模式窗口 -->
 <table  width="100%"><tr>

          <td width="45%" valign="top">
<form action="" method="post" name="form1">
          <div class="list_content">
                 <table id="mytable1" >

               <% if(null!=list1)
        	{
        		for(int i = 0 ;i<list1.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr  ><%} %>
                  <td align="center"><label>
                  <input type="checkbox" value="<%=list1.get(i).getQuestionId()%>" name="kexuan" id = "<%=list1.get(i).getQuestionId()%>">
                  </label></td>
                  <td align="center"><div align="left"><%=list1.get(i).getContent() %></div></td>
                  </tr>
<%
        		}
        	}%>
              </table>
            </div>
</form>
          </td>
          <td  width="10%">&nbsp;
          <div align="center"><button class="btn4" onclick="toAdd()">>></button><br>
          <button class="btn4" onclick="toRemove()"><<</button><br>
          <button class="btn4" type="button" onClick="toNext()">下一步</button><br>
          <button class="btn4" onclick="myCancel()">取 消</button></div>
          </td>
          <td width="45%" valign="top">
<form action="" method="post" name="form2">
           <div class="list_content">
                 <table id="mytable2" >

<% if(null!=list2)
        	{
        		for(int i = 0 ;i<list2.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr  ><%} %>
                  <td align="center"><label>
                  <input type="checkbox" value="<%=list2.get(i).getQuestionId()%>" name="kexuan" id = "<%=list2.get(i).getQuestionId()%>">
                  </label></td>
                  <td align="center"><div align="left"><%=list2.get(i).getContent() %></div></td>
                  </tr>
<%
        		}
        	}%>
              </table>
            </div>
</form>
          </td>

          </tr></table>

<!-- 模式是窗口结束 -->

</body>
</html>