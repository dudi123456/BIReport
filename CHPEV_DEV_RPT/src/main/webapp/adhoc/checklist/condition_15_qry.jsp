<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.base.struct.*"%>
<%
	
	//父类
	String parent_type = CommTool.getParameterGB(request,"parent_type");
	if(parent_type == null || "".equals(parent_type)){
		parent_type = "depart_type";
	}
	//子类
	String son_type = CommTool.getParameterGB(request,"son_type");
	if(son_type == null || "".equals(son_type)){
		son_type = "staff_type";
	}
	
	String depart_id="";
	// 增加发展人部门控制
	UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
	if (ctlStruct != null && ctlStruct.ctl_county_str != null && !"".equals(ctlStruct.ctl_county_str)) {
		depart_id= ctlStruct.ctl_county_str_add;
	}
	
	String obj_type =(String)session.getAttribute(WebKeys.ATTR_AdhocCheckStruct_ObjType);
	if(obj_type== null){
		obj_type = "";
	}
			
%>

<html>
<head>
 <script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
	<script language=javascript>
	  
        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}
	</script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/js.js"></script>



<%
//原来是<%=AdhocCheckHelper.ParentValueVsSubValue(session,parent_type,son_type,"CheckQueryForm.depart_id","CheckQueryForm.staff_id",depart_id)
 %>


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
     	document.CheckQueryForm.action="AdhocMultiCheckSelect.rptdo";
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
%>

<body class="main-body">

<form name="CheckQueryForm" method="post" action="AdhocMultiCheckSelect.rptdo" target="dispFrame" onsubmit="return   false;">
<input name="page_rows" type="hidden" value="<%=pageRows%>">
<input name="oper_type" type="hidden" value="2">
<input name="parent_type" type="hidden" value="<%=parent_type%>">
<input name="son_type" type="hidden" value="<%=son_type%>">
<input name="con_id" type="hidden" value="<%=obj_type%>">

<table width="96%" border="0" cellpadding="0" cellspacing="0" align="center">
<tr>
<td><img src="../images/system/search-ico.gif" width="20" height="20"></td>
</tr>
          <tr>
            <td class="search-bg">
              <table width="100%" border="0" height="8" cellspacing="0">
                <tr>
			<td>
                  <%
                  ////OBJ_TYPE , APP_TYPE , QRY_CODE , CON_CODE , CON_NAME , DATA_TYPE ,CON_TAG
                  String[][] rule = (String[][])session.getAttribute(WebKeys.ATTR_AdhocCheckStruct_Rule);               
                  String selectStr = "";
                  for(int i =0;i<rule.length;i++){
                	  selectStr += rule[i][3] + "|"
						+ rule[i][5] + ","
						+ rule[i][4] + ";";
                	  
                  }
                  %>
                  
                    <table width="100%">
      <tr>
        <td>条件关系：<BIBM:TagSelectList listName="addConRelation" listID="P001" /></td>
        <td>配置字段：<BIBM:TagSelectList listName="addConName" listID="#" selfSQL="<%=selectStr%>" /></td>
        <td>关系符号：<BIBM:TagSelectList listName="addConOper" focusID="LIKE" listID="P002" /></td>        
        <td>条件值:<input type="text" name="addConValue" class="normalField2"  tabindex="1" onkeyup="tabj()"></td>        
        <td valign="top" align="left">
        <input  type="button" name="add_btn" value="增加条件" class="btn4" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" onClick="addCondition()">
       
        <input type="button" name="slt_reset"  class="btn4" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="删除条件" onClick="clearData()">

		 <input type="button" name="slt_query"  class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="查 询" onClick="queryAdd()">
        </td>       
        </tr>
      <tr>     
        <td colspan="4"><textarea name="addConResult" cols="100" rows="3" class="input-text" ></textarea></td>           
		<td></td>    
      </tr>
    </table>
                 
                </tr>
              </table>
            </td>
          </tr>
</table>
</form>
</body>
</html>
