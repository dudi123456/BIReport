<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.common.app.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.syspar.domain.*"%>
<%@ page import="com.ailk.bi.syspar.dao.impl.*"%>
<%@ page import="com.ailk.bi.syspar.manage.*"%>
<%@ page import="com.ailk.bi.syspar.util.*"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"
%>

<%

	/*******************************参数*************************************/
	//当前分析参数结点
	String node_id = request.getParameter(ParamConstant.PARAM_CONDITION_NODE_ID);
	if(node_id ==null||"".equals(node_id)){
		node_id=(String)session.getAttribute(ParamConstant.PARAM_CONDITION_NODE_ID);
		if(node_id == null ||"".equals(node_id)){
			node_id=ParamConstant.DEFAULT_APP_NODE_ID;//需要提取值,目前默认
		}
	}

	//条件定制结果
	String conValue = (String)session.getAttribute(ParamConstant.PARAM_CONDITION_RESULT);
	if(conValue == null){
		conValue = "";
	}

	/*******************************逻辑处理*************************************/
	//接受服务
	ParamFacade facade = new ParamFacade(new ParamDao());
	//节点信息
	UiParamInfoConfigTable nodeInfo = facade.getParamConfigTreeMetaByNodeID(node_id);
	session.setAttribute(ParamConstant.PARAM_CONF_INFO_TABLE,nodeInfo);
	//当前节点参数
	//配置参数表
	UiParamMetaConfigTable[] tableInfo = null;
	ArrayList conf_list = facade.getParamConfigMetaByNodeID(node_id);
	if(conf_list!=null&&!conf_list.isEmpty()){
		tableInfo = (UiParamMetaConfigTable[]) conf_list.toArray(new UiParamMetaConfigTable[conf_list.size()]);
	}
	session.setAttribute(ParamConstant.PARAM_CONF_META_TABLE,tableInfo);

	//下拉列表显示值
	String selectStr = ParamHelper.getSelectListParam(tableInfo);
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@ include file="/base/commonHtml.jsp"%>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/XmlRPC.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/subject.js"></SCRIPT>
<SCRIPT language=javascript src="../sysparam/bi_zz.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/subject.css" type="text/css">

<script language="JavaScript" type="text/JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
//-->
</script>

<script language="JavaScript">
//增加
 function addValues(){
    //转换按钮
 	document.getElementById("ad_btn").style.display = "none";
	document.getElementById("ad_btn_s").style.display = "block";
 	//初始化sequence
 	var indexI = 0;
 	<%
	for(int i=0;tableInfo!=null&&i<tableInfo.length-1;i++){
		//显示类型
		String showType_add = tableInfo[i].getColumn_show_type();
		//特殊规则
		String showRule_add = tableInfo[i].getColumn_show_rule();
		//数据类型
		String dataType_add = tableInfo[i].getColumn_data_type();
 		if( (dataType_add.equals(ParamConstant.COLUMN_DATA_TYPE_N)||dataType_add.equals(ParamConstant.COLUMN_DATA_TYPE_R)) && null!=showRule_add.trim() && !"".equals(showRule_add.trim()) && !showType_add.equals(ParamConstant.COLUMN_SHOW_TYPE_8) ){
    %>
    		var objname = "<%=tableInfo[i].getColumn_en_name()%>";
			var obj = eval("document.edit_form."+objname);
			obj.value = jQuery.trim("<%=ParamHelper.getSeqNextValue(showRule_add)%>");
	<%
    	}
    }
	%>


 	//重置信息
 	//document.add_form.addConResult.value="";  //清空文本域。
 	//document.edit_form.reset();
 }
//增加提交
 function addValuesS(){
    //
 	document.edit_form.action="ParamView.rptdo?oper_type=add";
 	document.edit_form.submit();
 }
//修改
 function updateValues(){
	var rule_id = selectValue(list_form.select_target);
	var oldValue = list_form.select_target.dim_type_id;
	if(rule_id==null||rule_id==""){
		alert("请选定需要修改的记录！");
		return;
	}

    //
 	document.edit_form.action="ParamView.rptdo?oper_type=update";
 	document.edit_form.submit();
 }
//删除
 function deleteValues(){
	var rule_id = selectValue(list_form.select_target);
	if(rule_id==null||rule_id==""){
		alert("请选定需要删除的记录！");
		return;
	}
 	document.edit_form.action="ParamView.rptdo?oper_type=delete";
 	document.edit_form.submit();
 }
//增加
 function resetValues(){
    //转换按钮
 	document.getElementById("ad_btn").style.display = "block";
	document.getElementById("ad_btn_s").style.display = "none";

 	//重置信息
 	document.add_form.addConResult.value="";
 	document.edit_form.reset();
 }

//确定选中
function selectValue(radio_oj){
	var selectValue;
	if(radio_oj==null){
		return selectValue;
	}
	if(radio_oj.length==null){
		if(radio_oj.checked){
			selectValue = radio_oj.value;
			//alert(selectValue);
		}
	}else{
	for(var i=0;i<radio_oj.length;i++){
		if(radio_oj[i].checked){
			selectValue = radio_oj[i].value;
   			break; //停止循环
		}
	}
	}
	return selectValue;
}

//显示选中的对应值
function showValues(value){
	var arr = value.split("$$");
	var indexI = 0;
	<%
	    for(int i=0;tableInfo!=null&&i<tableInfo.length;i++)
	    {

		    //显示类型
			String showType = tableInfo[i].getColumn_show_type();
			//特殊规则
			String showRule = tableInfo[i].getColumn_show_rule();
	%>

			<%if (showType.equals(ParamConstant.COLUMN_SHOW_TYPE_8)&&!"".equals(showRule))
			{// 单选%>
				var objid = "<%=showRule%>";
				var idobj = eval("document.edit_form."+objid);
				idobj.value = jQuery.trim(arr[indexI]);
				indexI = Number(indexI) + 1;
			<%}%>

			var indexI = "<%=i%>";
			var objname = "<%=tableInfo[i].getColumn_en_name()%>";
			if(indexI == 0)
				{
					var objenname = "<%="H__"+tableInfo[i].getColumn_en_name()%>";//隐藏字段保留原先值
					// 默认
					var obj = eval("document.edit_form."+objname);
					obj.value = jQuery.trim(arr[indexI]);
					obj.readOnly = true;
				}
			else{
				var objenname = "<%="H__"+tableInfo[i].getColumn_en_name()%>";//隐藏字段保留原先值
				// 默认
				var obj = eval("document.edit_form."+objname);
				obj.value = jQuery.trim(arr[indexI]);
			}
			// 隐藏
			var hobj = eval("document.edit_form."+objenname);
			hobj.value = jQuery.trim(arr[indexI]);
			indexI = Number(indexI) + 1;
	<%
	    }
	%>
}

//增加查询条件
function addCondition(){
   if (document.add_form.addConValue.value==""){
    	alert("字段对应的值不能为空!");
    	return;
   }

	if(document.add_form.addConResult.value==""){
           if (checkColumnData()==""){
           		document.add_form.addConResult.value="";
           }else {
           		document.add_form.addConResult.value="("+ checkColumnData() + ")";
          }

    }else{
          if (checkColumnData()==""){
            document.add_form.addConResult.value=document.add_form.addConResult.value;
          }else{
      	    document.add_form.addConResult.value=document.add_form.addConResult.value+" \n"+document.add_form.addConRelation.options[document.add_form.addConRelation.selectedIndex].value+" ("+ checkColumnData()+ ")";
          }
    }
}

//单选选择
function radioSelected(con_rule,selected_id,en_name,qry_id,qry_name){
    var returnValue;
    var acptsite;
	var alreadyValue = "";
    var url = "";
   	var time = new Date();
   	var url = con_rule+"&Rnd="+Math.random()+"&node_id=<%=node_id%>&en_name="+en_name;
   	//alert(url);
    returnValue=window.showModalDialog(url,selected_id,"dialogWidth:1000px; dialogHeight:850px; dialogLeft:60px; dialogTop:80px; status:no; directories:yes;scrollbars:yes;Resizable=no;help:no");
    //alert(returnValue);
    //returnValue=window.open(url,acct_item_type_id,"dialogWidth:1000px; dialogHeight:850px; dialogLeft:60px; dialogTop:80px; status:no; directories:yes;scrollbars:yes;Resizable=no;help:no");
    var returnValueDesc = baseXmlSubmit.callAction("<%=request.getContextPath()%>/sysparam/param_radio_item_name.jsp?node_id=<%=node_id%>&&en_name="+en_name+"&qry_radio_id="+returnValue);
	//alert(returnValueDesc);
	if(returnValue!=null&&returnValue!=""){
		document.getElementById(qry_id).value = returnValue;
		document.getElementById(qry_name).value = returnValueDesc;
	}
}

//简单校验对应的条件值
function  checkColumnData()
{
	var returnStr="";
	var columnName="";
	var dataType="";
	var index=0;

 //条件名称
  var conStr = document.add_form.addConName.options[add_form.addConName.selectedIndex].value;
  var operType = document.add_form.addConOper.options[add_form.addConOper.selectedIndex].value;
  var conValue = document.add_form.addConValue.value;
 //
 index = conStr.indexOf("|");

 //字段名称
 columnName=conStr.substring(0, index);
 //字段单位
 dataType=conStr.substring(index+1, index+2);

 if((dataType=='D'&&dataType=='M') &&operType=='LIKE'){
 		alert("模糊查询匹配不正确！");
   		returnStr="";
   		return returnStr;
 }
 //D 日期
 if (dataType=='D'){
  	if (conValue.length!=8){
   		alert("输入日期数据格式不对!正确格式〔yyyymmdd〕");
   		returnStr="";
   		return returnStr;
  	}
   returnStr = "to_char(" + columnName + ",'yyyymmdd')" +" " +operType+ " '" + conValue + "'" ;

 }else if(dataType=='M'){
 	if (conValue.length!=6){
   		alert("输入月份数据格式不对!正确格式〔yyyymm〕");
   		returnStr="";
   		return returnStr;
  	}
   returnStr = "to_char(" + columnName + ",'yyyymm')" +" " +operType+ " '" + conValue + "'" ;

 }else if (dataType=='N'){
   if(operType == "LIKE")	{
   		returnStr = columnName +" " +operType + " '%" + conValue + "%'"  ;
   }else{
   		returnStr = columnName +" " +operType +" " +conValue;
   }

 }else if ((dataType=='V'||dataType=='R')&&conValue!=""){
   if(operType == "LIKE")	{
   		returnStr = columnName +" "+operType + " '%" + conValue + "%'"  ;
   }else{
   		returnStr = columnName +" "+operType + " '" + conValue + "'"  ;
   }

 }else if (dataType=='L'){
   		returnStr = columnName +"  LIKE  '%" + conValue + "%'"  ;
 }
 return returnStr;
}
//清空当前条件值
function clearData(){
	document.add_form.addConResult.value="";
}


function  queryAdd(){
	var str = checkColumnData();
	//alert(str);
	if(document.add_form.addConResult.value==""){
		document.add_form.addConResult.value = str;
	}
   	document.add_form.action="ParamQuery.rptdo";
  	document.add_form.submit();
}

//校验是否为数字
function checkNumber(theobj){
	var value = theobj.value;
	//此表达式不正确！
	var patrn=/^[-0-9.]{1,20}$/;
	if (value!=null&&value!=""&&!patrn.exec(value)){
		alert("顺序输入框请输入阿拉伯数字！");
		theobj.value = "";
		theobj.focus();
	}
}
//查询日志信息
function queryLogInfo(node_id){
	window.open("../sysparam/param_log.jsp?t_node_id="+node_id,"paramwindow","");
}


function downloadMoBan(node_id){
	window.open("../DownloadServlet?file_name=jcmzz.xls","paramwindow","");
}

function uploadData(node_id){
	document.getElementById("uploadtable").style.display = "";
}
//文件上传
function uploadFile(node_id)
{
    var myFile = document.getElementById("myfile").value;
	if(myFile == null || myFile=="")
	{
		alert("请选择要上传的文件！");
		return ;
	}
	document.upload_form.action="../UploadServlet";
  	document.upload_form.submit();
}

//
//对象
function BaseXmlSubmit(){
}
//动作
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = null;
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}

//实例
var baseXmlSubmit =new BaseXmlSubmit();
//生成工具条
/*
function bi_zz(type , paramValue){
    var node = baseXmlSubmit.callAction("../sysparam/param_ajax.jsp?type="+type+"&plan_id="+paramValue);
    node=node.replace(/^\s+|\n+$/g,'');

    if(type == 1){
    	document.edit_form.PP_DESC.value = node;
    }

}
*/
function bi_zz(type,id,name){
	if(type==1) {
		 var objId = document.getElementById(id);
		 var objName = document.getElementById(name);
		 objName.value = getOptionText(objId);
	}
}

function getOptionText(oSelect) {
	for(var i=0;i<oSelect.length;i++) {
	    if(oSelect.options[i].selected) {
	          return oSelect.options[i].innerText;
	    }
	}
}


</script>
</head>

<body class="zt-body" >
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <form name="bar_form" method="post" action="">
    <tr>
      <td>
        <div class="toptag">
            <Tag:Bar objID="901563"/>
        </div>
      </td>
    </tr>
  </form>
</table>
<table width="100%">
<form name="add_form" method="post" action="">
<input type="hidden" name="t_node_id"  value="<%=node_id%>">
<input type="hidden" name="t_table_name"  value="<%=nodeInfo.getTable_name()%>">
  <tr>
    <td class="search-bg">
    <table width="100%">
      <tr>
        <td>条件关系：<BIBM:TagSelectList listName="addConRelation" listID="P001" /></td>
        <td>配置字段：<BIBM:TagSelectList listName="addConName" listID="#" selfSQL="<%=selectStr%>" /></td>
        <td>关系符号：<BIBM:TagSelectList listName="addConOper" listID="P002" focusID="LIKE"/></td>
        <td>条件值:<input type="text" name="addConValue" class="normalField2"></td>
        <td nowrap rowspan="4">
        <input  type="button" name="add_btn" value="增加" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" onClick="addCondition()">
        <input type="button" name="qry_btn"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onClick="queryAdd()">
        <input type="button" name="cancel_btn"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="清空" onClick="clearData()">

        </td>
        </tr>
      <tr>
        <td colspan="4" nowrap><textarea name="addConResult" cols="100" rows="3" class="input-text" ><%=conValue%></textarea></td>
      </tr>
    </table>
    </td>
  </tr>
  </form>
</table>



<table width="100%" border="0" cellspacing="0" cellpadding="0">
<form name="list_form" method="post" action="">
<input type="hidden" name="t_node_id"  value="<%=node_id%>">
<input type="hidden" name="t_table_name"  value="<%=nodeInfo.getTable_name()%>">
<%
	String[][] paramList = (String[][])session.getAttribute(ParamConstant.PARAM_SQL_VALUE);
	if(paramList ==null){
		paramList = new String[0][0];
	}
%>
<!--显示script部分-->
<%=WebPageTool.pageScript("list_form","ParamQuery.screen")%>
<%
///获取翻页相关信息
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, paramList.length, 10 );
%>
<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>


<tr>
	<td>
	   <table width="100%" height="8" border="0" cellpadding="0" cellspacing="0">
	     <tr>
	       <td width="100%" class="report-desc" align="right"><%=WebPageTool.pagePolit(pageInfo)%></td>
	     </tr>
	   </table>
	</td>
</tr>

<tr>
  <td class="side-left">

  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">

    <tr class="table-th">
      <td class="table-item" align="center" >选定</td>
      <%
      	//循环显示需要添加的字段。
       	for(int i=0;tableInfo!=null&&i<tableInfo.length;i++){
          //显示类型
		  String showType = tableInfo[i].getColumn_show_type();
		  //特殊规则
		  String showRule = tableInfo[i].getColumn_show_rule();
          if (showType.equals(ParamConstant.COLUMN_SHOW_TYPE_8)&&!"".equals(showRule)) {// 单选
            out.println("<td class=\"table-item\" align=\"center\" >"+tableInfo[i].getColumn_cn_name()+"编码</td>");
          }
         	out.println("<td class=\"table-item\" align=\"center\" >"+tableInfo[i].getColumn_cn_name()+"</td>");
        }
       %>
    </tr>
    <!--循环取出值显示-->
    <%
if(paramList == null || paramList.length<=0){
%>
    <tr class="table-trb">
      <td class="table-item" align="center" colspan="<%=(tableInfo.length+1)%>"><b><font color="red">当前条件没有记录</font></b></td>
    </tr>
    <%
}
for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
      if((i+1)%2==1){
%>
    <tr class="table-tr" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
      <%}else{ %>
    <tr class="table-trb" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
      <%} %>
      <td align="left" class="table-td" nowrap >
        <%
             String paramStr ="";
             for(int j=0;paramList!=null&&j<paramList[i+pageInfo.absRowNoCurPage()].length;j++){
           	  if(paramStr.length()>0){
           		  paramStr += "$$";
           	  }

           	  paramStr +=  StringB.replace(paramList[i+pageInfo.absRowNoCurPage()][j],"'","\\'");
             }
         %>
        <input type="radio" class="input-radio" name="select_target"  onclick="showValues('<%=paramStr%>')">
      </td>
      <%
    		for(int j=0;paramList!=null&&j<paramList[i+pageInfo.absRowNoCurPage()].length;j++){
    			out.println("<td align=\"left\" class=\"table-td\" nowrap >"+paramList[i+pageInfo.absRowNoCurPage()][j]+"&nbsp;</td>");
    		}
       %>
    </tr>
    <%}%>
    </table>
    </td>
    </tr>

    </form>
</table>
<br>
<table width="100%">
  <tr>
    <td class="tree-area">
    <form name="edit_form" method="post" action="">
    <input type="hidden" name="t_node_id"  value="<%=node_id%>">
    <input type="hidden" name="t_table_name"  value="<%=nodeInfo.getTable_name()%>">
      <%
      for(int i=0;tableInfo!=null&&i<tableInfo.length;i++){
    	  String name = "H__"+tableInfo[i].getColumn_en_name();
      %>
      <input type="hidden" name="<%=name%>" value="">
      <%
      }
      %>

      <table width="100%">
        <%
    	for(int i=0;tableInfo!=null&&i<tableInfo.length;i++){
    		//如果等于3则换行
    		if(i%3 == 0){
					out.println("<tr>\n");
			}
    		out.println("<td>"+tableInfo[i].getColumn_cn_name()+"</td>");

    		String showType = tableInfo[i].getColumn_show_type();
    		String showrule = StringB.replace(tableInfo[i].getColumn_show_rule(),"\\s","");

    		String commCheck = tableInfo[i].getColumn_data_type();

    		String script = tableInfo[i].getColumn_event()+" style=\"width:170px\"";
			//
			if(ParamConstant.COLUMN_SHOW_TYPE_1.equals(showType)){//文本框
				if(commCheck.equals(ParamConstant.COLUMN_DATA_TYPE_R)){ //只读文本框
					out.println("<td><input type=\"text\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\" readonly></td>");
    			}else if(commCheck.equals(ParamConstant.COLUMN_DATA_TYPE_N)){  //数值类型文本
    				//if((null !=showrule.trim()) &&(!"".equals(showrule.trim()))){
    				//	out.println("<td><input type=\"text\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\"onblur=\"checkNumber(this)\" value=\""+ParamHelper.getSeqNextValue(showrule)+"\" ></td>");
    				//}else{
    					out.println("<td><input type=\"text\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\" onblur=\"checkNumber(this)\"><span style='color:red'>(请输入数字)</span></td>");
    				//}

    			}else{
    				if((null !=showrule.trim()) &&(!"".equals(showrule.trim()))){
    					if(showrule.trim().toUpperCase().equals(ParamConstant.ZZ_APP_LOGIN_USER)){
    						out.println("<td><input type=\"text\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\" value=\""+CommonFacade.getLoginId(session)+"\"></td>");
    					}else if(showrule.trim().toUpperCase().equals(ParamConstant.ZZ_APP_SEQ_ID)){
    						out.println("<td><input type=\"text\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\" value=\""+ParamHelper.getSeqNextValue(showrule)+"\"></td>");
    					}else{
    						out.println("<td><input type=\"text\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\" value=\""+showrule+"\"></td>");
    					}

    				}else{//走这
    					if(tableInfo[i].getColumn_en_name().equals("DIM_TYPE_ID") || tableInfo[i].getColumn_en_name().equals("MSU_TYPE_ID"))
    					{
    						out.println("<td><input type=\"text\" maxlength=\""+tableInfo[i].getColumn_length()+"\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\"></td>");
    					}
    					else
    					{
    						out.println("<td><input type=\"text\" maxlength=\""+tableInfo[i].getColumn_length()+"\" name=\""+tableInfo[i].getColumn_en_name()+"\" class=\normalField2\"></td>");
    					}
    				}
    			}

    		}else if(ParamConstant.COLUMN_SHOW_TYPE_2.equals(showType)){//日期
    			if((null !=showrule.trim()) &&(!"".equals(showrule.trim()))){
    				if(showrule.trim().toUpperCase().equals(ParamConstant.ZZ_APP_BEGIN_DATE)){
    					String begin_date = DateUtil.getDiffDay(-1 , new Date());
    					out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\""+begin_date+"\" onClick=\"scwShow(this,this);\"></td>");
    				}else if(showrule.trim().toUpperCase().equals(ParamConstant.ZZ_APP_END_DATE)){
    					String end_date = DateUtil.getDiffYear(20 ,DateUtil.getDiffDay(-1 , new Date())) + DateUtil.getDiffDay(-1 , new Date()).substring(4);
    					out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\""+end_date+"\" onClick=\"scwShow(this,this);\"></td>");
    				}else{
    					out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\""+showrule+"\" onClick=\"scwShow(this,this);\"></td>");
    				}

    			}else{
    				out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\"\" onClick=\"scwShow(this,this);\"></td>");
    			}

    		}else if(ParamConstant.COLUMN_SHOW_TYPE_3.equals(showType)){//月份
    			if((null !=showrule.trim()) &&(!"".equals(showrule.trim()))){
    			if(showrule.trim().toUpperCase().equals(ParamConstant.ZZ_APP_BEGIN_DATE)){
					String begin_date = DateUtil.getDiffMonth(-1 , new Date());
					out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\""+begin_date+"\" onClick=\"scwShowM(this,this);\"></td>");
				}else if(showrule.trim().toUpperCase().equals(ParamConstant.ZZ_APP_END_DATE)){
					String end_date =   DateUtil.getDiffYear(20 , DateUtil.getDiffMonth(-1 , new Date()))+DateUtil.getDiffMonth(-1 , new Date()).substring(5);
					out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\""+end_date+"\" onClick=\"scwShowM(this,this);\"></td>");
				}else{
					out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\""+showrule+"\" onClick=\"scwShowM(this,this);\"></td>");
				}
    			}else{
    				out.print("<td><input type=\"text\" class=\"query-input-value\"  name=\""+tableInfo[i].getColumn_en_name().trim()+"\" value=\"\" onClick=\"scwShowM(this,this);\"></td>");
    			}

    		}else if(ParamConstant.COLUMN_SHOW_TYPE_4.equals(showType)){//自定义SQL
    		%>
            <td><BIBM:TagSelectList listName="<%=tableInfo[i].getColumn_en_name().trim()%>" listID="0" selfSQL="<%=showrule%>" script="<%=script%>" /></td>
            <%
    		}else if(ParamConstant.COLUMN_SHOW_TYPE_5.equals(showType)){//自定义标识
    		%>
            <td><BIBM:TagSelectList listName="<%=tableInfo[i].getColumn_en_name().trim()%>"    listID="<%=showrule%>" script="<%=script%>" /></td>

             <%
    		}else if(ParamConstant.COLUMN_SHOW_TYPE_7.equals(showType)){//自定义标识
    		%>
            <td><BIBM:TagSelectList listName="<%=tableInfo[i].getColumn_en_name().trim()%>"    listID="#" selfSQL="<%=showrule%>" script="<%=script%>" /></td>

            <%
    		}else if(ParamConstant.COLUMN_SHOW_TYPE_6.equals(showType)){
    			out.println("<td><textarea name=\""+tableInfo[i].getColumn_en_name()+"\" cols=\"40\" rows=\"3\" class=\"input-text\" ></textarea></td>");

    		}else if(ParamConstant.COLUMN_SHOW_TYPE_8.equals(showType)){//单选
    		%>
    		<td><input type="hidden" name="<%=tableInfo[i].getColumn_show_rule()%>" value=''><input name="<%=tableInfo[i].getColumn_en_name()%>" type="text" size="20" value='' readonly><img src="../images/common/system/icon-hit.gif" width="16" height="16" style="cursor:hand" onclick="javascript:radioSelected('ParamRadio.rptdo?opt_type=radioCheck','','<%=tableInfo[i].getColumn_en_name()%>','<%=tableInfo[i].getColumn_show_rule()%>','<%=tableInfo[i].getColumn_en_name()%>');"></td>
    		<%}
    		if((i%3 == 2)||(i==tableInfo.length-1)){
					if((i==tableInfo.length-1)&&(i%3 != 2)){
						int count = 3-((tableInfo.length)%3);
						out.println(ParamUtil.getNbspTdInnerHtml(count));
					}
					out.println("</tr>\n");
			}


    	}
    %>
        <tr align="center">
          <td colspan="2" align="right" width="35%" nowrap>
            <input type="button" id="ad_btn" name="ad_btn"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="增加"  onclick="addValues()">
            <input type="button" id="ad_btn_s" name="ad_btn_s"  class="button2" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="增加保存"  onclick="addValuesS()" style="display:none">
          </td>
          <td colspan="4" align="left" nowrap>
            <input type="button" name="up_btn"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="修改" onclick="updateValues()">
            <input type="button" name="de_btn"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="删除" onclick="deleteValues()">
            <input type="button" name="ca_btn"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="清除" onclick="resetValues()">
            <input type="button" name="log_btn"  class="button2" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="日志监控" onclick="queryLogInfo('<%=node_id%>')">
            <%
            	if(!nodeInfo.getIsdimtable().equals("Y")){
            %>
            <input type="button" name="mb_btn"  class="button3" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="最新模板下载" onclick="downloadMoBan('<%=node_id%>')">
            <input type="button" name="excel_btn"  class="button2" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="批量导入" onclick="uploadData('<%=node_id%>')">
            <%
            	}
            %>
          </td>
        </tr>
    </table>
    </form>
     </td>
  </tr>
</table>

 <table width="100%" id="uploadtable" style="display:none">
  <tr>
    <td class="tree-area">
      <form name="upload_form" enctype="multipart/form-data" method="post" action="">
    	<input type="hidden" name="t_node_id"  value="<%=node_id%>">
    	<input type="hidden" name="t_table_name"  value="<%=nodeInfo.getTable_name()%>">
    	请选择你上传的文件：<input type="file" name="myfile" id="myfile">
       <input type="button" name="file_btn" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="上传" onclick="uploadFile('<%=node_id%>')">

     </form>
     </td>
     </tr>
   </table>

</body>
</html>
