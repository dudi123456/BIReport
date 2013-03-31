<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.struct.*"%>
<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/js.js"></script>
<script language="javascript">
//增加查询条件
  function addCondition(){
     if (document.CheckQueryForm.addConValue.value==""){
      	alert("字段对应的值不能为空!");
      	return;
     }
     
  	if(document.CheckQueryForm.addConResult.value==""){
             if (checkColumnData()==""){
             		document.CheckQueryForm.addConResult.value="";
             }else {
             		document.CheckQueryForm.addConResult.value="("+ checkColumnData() + ")";
            }

      }else{
            if (checkColumnData()==""){
              document.CheckQueryForm.addConResult.value=document.CheckQueryForm.addConResult.value;
            }else{
        	    document.CheckQueryForm.addConResult.value=document.CheckQueryForm.addConResult.value+" \n"+document.CheckQueryForm.addConRelation.options[document.CheckQueryForm.addConRelation.selectedIndex].value+" ("+ checkColumnData()+ ")";
            }
      }
  }

  //简单校验对应的条件值
  function  checkColumnData(){
  var returnStr="";
  var columnName="";
  var dataType="";
  var index=0;
   
   //条件名称
    var conStr = document.CheckQueryForm.addConName.options[CheckQueryForm.addConName.selectedIndex].value;
    var operType = document.CheckQueryForm.addConOper.options[CheckQueryForm.addConOper.selectedIndex].value;
    var conValue = document.CheckQueryForm.addConValue.value;
   //
   index = conStr.indexOf("|");
   //字段名称
   columnName=conStr.substring(0, index);
   //字段单位
   dataType=conStr.substring(index+1, index+2);

   if(dataType!='2'&&operType=='LIKE'){
   		    alert("模糊查询匹配不正确！");
     		returnStr="";
     		return returnStr;
   }
   if (dataType=='1'){
     returnStr = columnName +" " +operType +" " +conValue;
   }else if (dataType=='2'){
     if(operType == "LIKE")	{
     		returnStr = columnName +" "+operType + " '%" + conValue + "%'"  ;
     }else if(operType == "NOT LIKE"){
    	 returnStr = columnName +" "+operType + " '%" + conValue + "%'"  ;
     }else{
     		returnStr = columnName +" "+operType + " '" + conValue + "'"  ;
     }
     
   }
   return returnStr;
  }
  //清空当前条件值
  function clearData(){
  	document.CheckQueryForm.addConResult.value="";
  }


  function  queryAdd(){
	    document.CheckQueryForm.target="dispFrame";
     	document.CheckQueryForm.action="ParamRadio.rptdo?opt_type=radioCheckQry";
    	document.CheckQueryForm.submit();
  }

  	function tabj(){
		if(event.keyCode == 13){

			addCondition();
			CheckQueryForm.addConValue.value="";
			queryAdd();
		}
	}
</script>

</head>
<%  
  String pageRows=(String)session.getAttribute(WebKeys.ATTR_HiPageRows);
  if (pageRows==null || "".equals(pageRows))
           pageRows="10";
  
  //关闭标志
  session.setAttribute(WebKeys.ATTR_ANYFLAG,"1");
  
  String node_id = request.getParameter("node_id");
  String en_name = request.getParameter("en_name");
%>

<body class="main-body">

<form name="CheckQueryForm" method="post" action="ParamRadio.rptdo?opt_type=radioCheckQry" target="dispFrame" onsubmit="return   false;">
<input name="page_rows" type="hidden" value="<%=pageRows%>">
<input name="node_id" type="hidden" value="<%=node_id%>">
<input name="en_name" type="hidden" value="<%=en_name%>">
<input name="oper_type" type="hidden" value="2">
<br>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
  <td><img src="../images/common/system/search-ico.gif" width="20" height="20"></td>
  <td align="center">编码ID:<input type="text" name="qry_id" class="normalField2" tabindex="1"></td>        
  <td align="center">描述名称:<input type="text" name="qry_name" class="normalField2" tabindex="1"></td>        
  <td valign="top" align="left">
	<input type="button" name="slt_query"  class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onClick="queryAdd()">
  </td>      
</tr>
</table>
</form>
</body>
</html>