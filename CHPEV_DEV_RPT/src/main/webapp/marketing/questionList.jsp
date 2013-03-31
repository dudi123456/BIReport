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
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="java.util.*"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
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
	function mySubmit(op) {
		form1.action="questionAction.rptdo?optype=questionList&doType="+op;
		form1.submit();
	}
	function myAdd(op) {
		form1.action="questionAction.rptdo?optype=questionAdd&doType="+op;
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
			form1.action="questionAction.rptdo?optype=questionAdd&doType="+op+"&questiontId="+id;
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
				form1.action="questionAction.rptdo?optype=questionList&doType="+op;
				form1.submit();
			}
		}else{
			alert("请至少选择一条要删除的信息！");
	    }
	}
	</script>
	<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
   		 if(null==qryStruct.question_content){
   			qryStruct.question_content="";
   		  }

		//获得类型下拉框数据
		String typeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_QUESTION_TYPE'";
		List<QuestionInfo> list =(List<QuestionInfo>) request.getAttribute("questionList");
	%>
</head>
<body style="background-color:#f9f9f9" onLoad="selfDisp()">
<div>
<form name="form1" method="post" action="">
<%=WebPageTool.pageScript("form1","questionAction.rptdo?optype=questionList&doType=search")%>
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
     <div class="toptag" > 您所在位置：营销策划 >> 营销策略 >> <em class="red">问题管理</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span> </div>

    <div class="topsearch">
      <table width="100%">

        <tr>
          <td align="right" width="15%">问题内容： </td>
          <td><input id=qry__question_content name=qry__question_content  value="<%=qryStruct.question_content %>" class="txtinput"  style="width:250px"/></td>
          <td align="right" width="15%">问题类型： </td>
          <td>
         	 <BIBM:TagSelectList focusID="<%=qryStruct.question_type%>" script="class='easyui-combobox'"	listName="qry__question_type" listID="0" allFlag="" selfSQL="<%=typeSql%>" />
          </td>
          <td align="right" width="15%">审批状态： </td>
          <td>
         	 <BIBM:TagSelectList focusID="<%=qryStruct.question_state%>" script="class='easyui-combobox'"	listName="qry__question_state" listID="#" allFlag="" selfSQL="1,已通过审批;0,等待审批中;-1,未通过审批" />
          </td>
          <td align="right" width="15%">&nbsp;</td>
          <td>
                 <button class="btn3" type="button" onClick="mySubmit('search')"> 查 询 </button>       	  </td>
        </tr>
      </table>
    </div>
    <div class="topsearch_btn"> <span>
      <button class="btn3" type="button" onClick="myAdd('add')"> 新 建 </button>
       
      <button class="btn3" type="button" onClick="myModify('modify')"> 修 改 </button>
       
      <button class="btn3" type="button" onClick="myDelect('delect')">删 除 </button>
	     
    </span> </div>
    <div class="list_content">
      <table>
        <tr>
          <th width="5%" align="center"> 选  择 </th>
          <th width="55%" align="center">问题</th>
          <th width="8%" align="center">问题类型</th>
          <th width="12%" align="center">问题描述</th>
          <th width="10%" align="center">审批状态</th>
          <th width="10%" align="center">创建时间</th>
        </tr>
        <%
        	  if(perPageCount>0){
            	    for(int i=0;i<pageInfo.iLinesPerPage && (1+i+pageInfo.absRowNoCurPage())<=pageInfo.iLines;i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
          <td align="center"><label> <input type="checkbox" id="checkbox_<%=list.get(i+pageInfo.absRowNoCurPage()).getQuestionId() %>" name="checkbox" value="<%=list.get(i+pageInfo.absRowNoCurPage()).getQuestionId() %>"></label></td>
       	  <td align="left"><%=list.get(i+pageInfo.absRowNoCurPage()).getContent()  %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_QUESTION_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getQuestionType()))  %></td>
          <td align="center"><%=list.get(i+pageInfo.absRowNoCurPage()).getDesc() %></td>
          <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_APPROVAL_TYPE",String.valueOf(list.get(i+pageInfo.absRowNoCurPage()).getState()))  %></td>
          <td align="center"><%=CommonFormate.dateFormate(list.get(i+pageInfo.absRowNoCurPage()).getCreateDate()) %></td>
        </tr>
        			<%
        		}
        	}%>
        	 <div id="div2"  style="border:0px solid green; width:100%; height:40px; position:absolute;  bottom:0px;"><%=WebPageTool.pagePolit(pageInfo)%></div>
      </table>
    </div>
  </div>
  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>

</form>
</div>
</body>
</html>
