<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.marketing.entity.QuestionInfo"%>
<%@page import="com.ailk.bi.marketing.entity.SurveyInfo"%>
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
	function mySave(op) {
		form1.action="targetAction.rptdo?doStep=step1&optype=targetList&doType="+op;
		form1.submit();
	}
	function mySubmit(op) {
		form1.action="targetAction.rptdo?optype=targetList&doType="+op;
		form1.submit();
	}

    function delRow(id){
     //获得表格对象
     var tb=document.getElementById('mytable1');
     //根据id获得将要删除行的对象
     var tr=document.getElementById(id);
     //取出行的索引，设置删除行的索引
     tb.deleteRow(tr.rowIndex);
     addTr();
    }
    function addTr(tr)
    {
     //获得表格对象
     var tb=document.getElementById('mytable2');
     var newTr = tb.insertRow(-1);//在最下的位置
     //给这个行设置id属性，以便于管理（删除）
     newTr.id='tr'+1;
     //设置对齐方式（只是告诉你，可以以这种方式来设置任何它有的属性）
     newTr.align='center';
     //添加两列
     var newTd0 = newTr.insertCell();
     var newTd1 = newTr.insertCell();
     //设置列内容和属性
     newTd0.innerHTML = "本行id为："; //让你看到删除的是指定的行
     newTd1.innerHTML= "<button onclick=\"moveTr('"+1+"');\" >移除</button>";;

     id++;

    }

	</script>
	<%
	ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		 if(null==qryStruct.step1_survey_name){
			qryStruct.step1_survey_name="";
		  }
	 List<QuestionInfo> list1 = (List<QuestionInfo>)request.getAttribute("questionList1");
	 List<QuestionInfo> list2 = (List<QuestionInfo>)request.getAttribute("questionList2");
		//获得类型下拉框数据
		String typeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_SURVEY_TYPE'";
	%>
</head>
<body style="background-color:#f9f9f9">
<form name="form1" method="post" action="">
  <div id="maincontent">
    <div class="toptag"> 您所在位置：营销策划 >>套餐管理>><em class="red">新增套餐信息</em> <span class="hbt"><a href="javascript:;"
                class="icon dhelp" onClick="addMyFavourate()">指标说明</a>&nbsp;运营商：中国联通&nbsp;时间：201108 </span><span class="hbt1">

    </span></div>

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title" ><center><h2>问卷调查信息维护第1/2步骤</h2></center></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="12%" class="validatebox-tabTD-left"><img src="images/validatebox_warning.gif" width="16" height="16" border="0" />目标名称：</td>
          <td class="validatebox-tabTD-right"><input id="txt_survey_name" name="txt_survey_name" value="<%=qryStruct.step1_survey_name %>"  class="easyui-validatebox" required="true" validType="length[1,20]" ></td>
        </tr>

        <tr>
          <td class="validatebox-tabTD-left">目标类型：</td>
          <td class="validatebox-tabTD-right"><label>
           <BIBM:TagSelectList  focusID="<%=qryStruct.step1_survey_type %>" script="class='easyui-combobox'"	listName="txt_survey_type" listID="0" selfSQL="<%=typeSql%>" />
           </label></td>
        </tr>
         <tr>
          <td colspan="2" class="validatebox-tabTD-left" valign="top" height="500px">
<br>
          <table  width="100%"><tr>

          <td width="45%" valign="top">

          <div class="list_content">
                 <table id="mytable1" >
              <tr>
                  <th colspan="2" align="center"> <h4>可选问题列表</h4> </th>
                  </tr>

              </table>
            </div>

          </td>
          <td width="10%">&nbsp;
          </td>
          <td width="45%" valign="top">

           <div class="list_content">
                 <table id="mytable2" >
              <tr>
                  <th colspan="2" align="center"> <h4>已选问题列表</h4> </th>
                  </tr>

              </table>
            </div>

          </td>

          </tr></table>
<iframe id="pp" scrolling="0"width="100%" height="100%"  border="0" frameborder="0"marginwidth="0" marginheight="0" src="marketing/step1.jsp"></iframe><br>

          </td>
        </tr>

</table>

</form>
</body>
</html>
