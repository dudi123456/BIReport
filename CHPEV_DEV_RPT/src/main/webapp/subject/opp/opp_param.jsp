<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.asiabi.oppsubject.facade.*"%>
<%@ page import="com.asiabi.oppsubject.service.*"%>
<%@ page import="com.asiabi.oppsubject.service.impl.*"%>
<%@ page import="com.asiabi.oppsubject.dao.impl.*"%>
<%@ page import="com.asiabi.oppsubject.dao.*"%>
<%@ page import="com.asiabi.oppsubject.struct.*"%>

<%@ page import="java.util.*"%>

<%
OppFacade facade = new OppFacade();
OppService service = new OppService();
OppDao dao = new OppDao();
service.setDao(dao);
facade.setService(service);


%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/wait.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/bimain.css">




<script language="javascript">
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

//新增
function _newParam(){	
	//
	var param_id = document.dimForm.param_id.value;
	var param_name = document.dimForm.param_name.value;
	var param_type = document.dimForm.param_type.value;
	var param_rule = document.dimForm.param_rule.value;
	var param_weight = document.dimForm.param_weight.value;
	var param_status = document.dimForm.param_status.value;
	var param_desc = document.dimForm.param_desc.value; 
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}

	if(param_name == null || param_name == ""){
		alert("参数名称不能为空！");
		return;
	}

	if(param_type == null || param_type == ""){
		alert("参数类型不能为空！");
		return;
	}

	if(param_weight == null || param_weight == ""){
		alert("参数评估权重不能为空！");
		return;
	}

	
	var value =   param_id+":"+param_name+":"+param_type+":"+param_rule+":"+param_weight+":"+param_status+":"+param_desc+":end";	
	var dimTableStr = baseXmlSubmit.callAction("../subject/OppSubejctParamAjax.rptdo?oper_type=add&value="+value);
	dimTableStr=dimTableStr.replace(/^\s+|\n+$/g,'');
	if(dimTableStr == ""){
		document.getElementById("oppparamspan").innerHTML = "新增参数失败！";
		
	}else{
		document.getElementById("oppparamspan").innerHTML = "新增参数成功！";
		document.getElementById("oppparamdiv").innerHTML = dimTableStr;
		document.dimForm.h_param_id.value = param_id;
		
	}
	closeWaitWin();
    
}

//编辑
function _editParam(){	
	var param_id = document.dimForm.param_id.value;
	var param_name = document.dimForm.param_name.value;
	var param_type = document.dimForm.param_type.value;
	var param_rule = document.dimForm.param_rule.value;
	var param_weight = document.dimForm.param_weight.value;
	var param_status = document.dimForm.param_status.value;
	var param_desc = document.dimForm.param_desc.value;
	var h_param_id = document.dimForm.h_param_id.value; 
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}

	if(param_name == null || param_name == ""){
		alert("参数名称不能为空！");
		return;
	}

	if(param_type == null || param_type == ""){
		alert("参数类型不能为空！");
		return;
	}

	if(param_weight == null || param_weight == ""){
		alert("参数评估权重不能为空！");
		return;
	}

	
	var value =   param_id+":"+param_name+":"+param_type+":"+param_rule+":"+param_weight+":"+param_status+":"+param_desc+":"+h_param_id;	
	var dimTableStr = baseXmlSubmit.callAction("../subject/OppSubejctParamAjax.rptdo?oper_type=update&value="+value);
	dimTableStr=dimTableStr.replace(/^\s+|\n+$/g,'');
	if(dimTableStr == ""){
		document.getElementById("oppparamspan").innerHTML = "修改参数失败！";
		
	}else{
		document.getElementById("oppparamspan").innerHTML = "修改参数成功！";
		document.getElementById("oppparamdiv").innerHTML = dimTableStr;
		document.dimForm.h_param_id.value = param_id;
		
	}
	closeWaitWin();
}

//编辑
function _delParam(){	
	var param_id = document.dimForm.param_id.value;
	var param_name = document.dimForm.param_name.value;
	var param_type = document.dimForm.param_type.value;
	var param_rule = document.dimForm.param_rule.value;
	var param_weight = document.dimForm.param_weight.value;
	var param_status = document.dimForm.param_status.value;
	var param_desc = document.dimForm.param_desc.value;
	var h_param_id = document.dimForm.h_param_id.value;
	var value =   param_id+":"+param_name+":"+param_type+":"+param_rule+":"+param_weight+":"+param_status+":"+param_desc+":"+h_param_id;	
	var dimTableStr = baseXmlSubmit.callAction("../subject/OppSubejctParamAjax.rptdo?oper_type=delete&value="+value);
	dimTableStr=dimTableStr.replace(/^\s+|\n+$/g,'');
	if(dimTableStr == ""){
		document.getElementById("oppparamspan").innerHTML = "删除参数失败！";
		
	}else{
		document.getElementById("oppparamspan").innerHTML = "删除参数成功！";
		document.getElementById("oppparamdiv").innerHTML = dimTableStr;
		document.dimForm.h_param_id.value = "";
		document.dimForm.param_id.value = "";
		document.dimForm.param_name.value = "";
		document.dimForm.param_type.value = "";
		document.dimForm.param_rule.value = "";
		document.dimForm.param_desc.value = "";
		document.dimForm.param_weight.value = "";
		document.dimForm.param_status.value = "";	}
	    closeWaitWin();
}


//编辑值
function _editParamValue(param_id){	
	window.open("../subject/OppSubjectParamLvl.rptdo?param_id="+param_id,"oppParam","");
}

function selectValue(obj){

	var arr = obj.value.split("$$");
	document.dimForm.h_param_id.value =arr[0]; 
	document.dimForm.param_id.value =arr[0]; 
	document.dimForm.param_name.value =arr[1]; 
	document.dimForm.param_type.value =arr[2]; 
	document.dimForm.param_rule.value =arr[3]; 
	document.dimForm.param_weight.value =arr[4]; 
	document.dimForm.param_status.value =arr[5]; 
	document.dimForm.param_desc.value =arr[6]; 
	document.getElementById("oppparamspan").innerHTML = "&nbsp;";

	
}

function ajaxCheck(obj){
	alert(this.value);
}

</script>

</head>

<body class="main-body">
<form name="dimForm" method="post" action="">

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td> 
        <table width="100%" border="0" cellpadding="0" cellspacing="1">
          <tr>
            <td>
             <Tag:Bar defaultValue="902200"/>
            </td>
          </tr>
          <tr>
            <td height="1">&nbsp;</td>
          </tr>
          <tr>
            <td><table width="100%" >
              <tr>
                <td width="22"><img src="../biimages/icon_ztfx3.gif" width="16" height="16"></td>
                <td width="100" valign="bottom" nowrap><span class="title-bold">系统配置参数</span></td>
                <td ><span class="title-bold"><img src="../biimages/kw/broken-line.gif" ></span></td>
                <td width="54" id="y_1" style="display:">    
                </td> </tr>
              <tr id="defaultDim" style="display:">
                <td colspan="4" >
               <div id="oppparamdiv" style="width: 100%; margin-top: 2px; overflow: auto; height:266px;">
               <table style="width: 100%" border="0" bgcolor="#CFCFCF" cellpadding="1" cellspacing="1"
                                       style="margin:0;">          
                  <tr class="celtitle FixedTitleRow">
                  	<td align="center"  bgcolor="E3E3E3" width="5%">选择</td>   
                    <td align="center"  bgcolor="E3E3E3" width="5%">标识</td>
                    <td align="center"  bgcolor="E3E3E3" width="20%">名称</td>
                    <td align="center"  bgcolor="E3E3E3" width="5%">类型</td>                    
                    <td align="center"  bgcolor="E3E3E3" width="20%">规则</td>
                    <td align="center"  bgcolor="E3E3E3" width="5%">权重</td>
                    <td align="center"  bgcolor="E3E3E3" width="5%">状态</td>
                    <td align="center"  bgcolor="E3E3E3" width="25%">描述</td>
                    <td align="center"  bgcolor="E3E3E3" width="5%">编辑</td>
                                               
                  </tr>   
                  <%
                  ArrayList paramList = (ArrayList)facade.getOppParamList();
                  if(paramList!=null&&paramList.size() >0){
                	  for(int i=0;i<paramList.size();i++){
                		  OppParamStruct struct =(OppParamStruct)paramList.get(i);
                	 	  String radioValue = struct.getParam_id()+"$$"+struct.getParam_name()+"$$"+struct.getParam_type()+"$$"+struct.getParam_rule()+"$$"+struct.getParam_weight()+"$$"+struct.getParam_status()+"$$"+struct.getParam_desc();
                	 
                	  %>
                	  
                	  <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                	  <td align="center"><input type="radio"  name="param" value="<%=radioValue%>" onclick="selectValue(this)"></td>          
                      <td align="center" ><%=struct.getParam_id()%></td>
                      <td align="left" ><%=struct.getParam_name()%></td>
                      <td align="center"><%=struct.getParam_type()%></td> 
                      <td align="left"><%=struct.getParam_rule()%></td> 
                      <td align="center"><%=struct.getParam_weight()%></td> 
                      <td align="center"><%=struct.getParam_status()%></td> 
                      <td align="left"><%=struct.getParam_desc()%></td>
                      <td align="center"><a href="javascript:_editParamValue('<%=struct.getParam_id()%>');">编辑</a></td>  
                               
                                  
                    </tr>   
                 <%
                  }}                 
                  %>
                
                </table>
                </div>
                </td>
              </tr>
              <tr>
                <td colspan="4" nowrap>&nbsp;</td>
              </tr>
              
              <tr>
                <td><img src="../biimages/feedback-ioc1.gif" width="22" height="14"></td>
                <td valign="bottom" nowrap><span class="title-bold">参数编辑区</span></td>
                <td><span class="bulebig"><img src="../biimages/kw/broken-line.gif"></span></td>
              </tr>
              <tr>
                <td colspan="4" nowrap align="center"><span id="oppparamspan" class="title-big-bold">&nbsp;</span></td>
              </tr>
                <tr>
                <td colspan="4" nowrap>
                <input name="h_param_id" type="hidden">
                <table width="100%"  border=1 cellpadding="0" cellspacing="0" borderColorLight=#C0C0C0 borderColorDark=#ffffff>
                  <tr>
                    <td align="center" class="td-bg">参数标识</td>
                    <td ><input name="param_id" type="text" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">参数名称</td>
                    <td ><input type="text" name="param_name" class="query-input-text-1" ></td>
                  </tr>
                  <tr>
                    <td align="center" class="td-bg">参数类型</td>
                    <td ><input type="text" name="param_type" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">参数规则</td>
                    <td ><input type="text" name="param_rule" class="query-input-text-1" ></td>
                  </tr>
                  <tr>
                    <td align="center" class="td-bg">参数权重</td>
                    <td ><input type="text" name="param_weight" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">参数状态</td>
                    <td ><input type="text" name="param_status" class="query-input-text-1" ></td>
                  </tr>
                   <tr>
                    <td align="center" class="td-bg"  >参数描述</td>
                    <td colspan="3"><textarea name="param_desc" class="query-input-text-1" cols="120" rows="3"></textarea></td>
                  </tr>
                </table>
                  </td>
              </tr>
              
              <tr align="center">
                <td colspan="4" nowrap>
                  <input name="btn_add" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="增加" onClick="ShowWait();_newParam();">
                  <input name="btn_back" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="修改" onClick="ShowWait();_editParam();" >
                  <input name="btn_del" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"  value="删除" onClick="ShowWait();_delParam();">
                  <input type="reset"" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"  value="重置">
                 
                 </td>
              </tr>
            </table>
           </td>
          </tr>
        </table>
      </td>
  </tr>

</table>
  </form>
</body>
</html>
