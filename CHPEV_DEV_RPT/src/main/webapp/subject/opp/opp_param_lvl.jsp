<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.asiabi.oppsubject.facade.*"%>
<%@ page import="com.asiabi.oppsubject.service.*"%>
<%@ page import="com.asiabi.oppsubject.service.impl.*"%>
<%@ page import="com.asiabi.oppsubject.dao.impl.*"%>
<%@ page import="com.asiabi.oppsubject.dao.*"%>
<%@ page import="com.asiabi.oppsubject.struct.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%
ArrayList list = (ArrayList) request.getAttribute(WebKeys.ATTR_OPP_SUBJECT_PARAM_LVL_lIST);
if(list == null){
	list = new ArrayList();
}

OppParamStruct struct  = (OppParamStruct)list.get(0);

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
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
function _newParamLvl(){	
	//
	var param_id = document.dimForm.h_param_id.value;	
	var param_type = document.dimForm.h_param_type.value;
	var h_lvl_id = document.dimForm.h_lvl_id.value;
	var lvl_id = document.dimForm.lvl_id.value;
	var lvl_name = document.dimForm.lvl_name.value;
	var start_val = document.dimForm.start_val.value;
	var end_val = document.dimForm.end_val.value; 
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}

	

	if(param_type == null || param_type == ""){
		alert("参数类型不能为空！");
		return;
	}

	if(lvl_id == null || lvl_id == ""){
		alert("参数评估分档标识不能为空！");
		return;
	}

	if(lvl_name == null || lvl_name == ""){
		alert("参数评估分档标识不能为空！");
		return;
	}

	
	var value =   param_id+":"+param_type+":"+lvl_id+":"+lvl_name+":"+start_val+":"+end_val+":end";	
	var dimTableStr = baseXmlSubmit.callAction("../subject/OppSubejctParamLvlAjax.rptdo?oper_type=add&value="+value);
	dimTableStr=dimTableStr.replace(/^\s+|\n+$/g,'');
	if(dimTableStr == ""){
		document.getElementById("oppparamlvlspan").innerHTML = "新增参数值域失败！";
		
	}else{
		document.getElementById("oppparamlvlspan").innerHTML = "新增参数值域成功！";
		document.getElementById("oppparamlvldiv").innerHTML = dimTableStr;
		document.dimForm.h_lvl_id.value = lvl_id;
		document.dimForm.h_param_id.value = param_id;
		document.dimForm.h_param_type.value = param_type;
		
	}
	closeWaitWin();
    
}

//编辑
function _editParamLvl(){	
	//
	var param_id = document.dimForm.h_param_id.value;	
	var param_type = document.dimForm.h_param_type.value;
	var h_lvl_id = document.dimForm.h_lvl_id.value;
	var lvl_id = document.dimForm.lvl_id.value;
	var lvl_name = document.dimForm.lvl_name.value;
	var start_val = document.dimForm.start_val.value;
	var end_val = document.dimForm.end_val.value; 
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}

	

	if(param_type == null || param_type == ""){
		alert("参数类型不能为空！");
		return;
	}

	if(lvl_id == null || lvl_id == ""){
		alert("参数评估分档标识不能为空！");
		return;
	}

	if(lvl_name == null || lvl_name == ""){
		alert("参数评估分档标识不能为空！");
		return;
	}

	
	var value =   param_id+":"+param_type+":"+lvl_id+":"+lvl_name+":"+start_val+":"+end_val+":"+h_lvl_id;	
	var dimTableStr = baseXmlSubmit.callAction("../subject/OppSubejctParamLvlAjax.rptdo?oper_type=update&value="+value);
	dimTableStr=dimTableStr.replace(/^\s+|\n+$/g,'');
	if(dimTableStr == ""){
		document.getElementById("oppparamlvlspan").innerHTML = "修改参数值域失败！";
		
	}else{
		document.getElementById("oppparamlvlspan").innerHTML = "修改参数值域成功！";
		document.getElementById("oppparamlvldiv").innerHTML = dimTableStr;
		document.dimForm.h_lvl_id.value = lvl_id;
		document.dimForm.h_param_id.value = param_id;
		document.dimForm.h_param_type.value = param_type;
		
	}
	closeWaitWin();
}

//编辑
function _delParamLvl(){	
	var param_id = document.dimForm.h_param_id.value;	
	var param_type = document.dimForm.h_param_type.value;
	var h_lvl_id = document.dimForm.h_lvl_id.value;
	var lvl_id = document.dimForm.lvl_id.value;
	var lvl_name = document.dimForm.lvl_name.value;
	var start_val = document.dimForm.start_val.value;
	var end_val = document.dimForm.end_val.value; 
	if(param_id == null || param_id == ""){
		alert("参数标识不能为空！");
		return;
	}

	

	if(param_type == null || param_type == ""){
		alert("参数类型不能为空！");
		return;
	}

	if(lvl_id == null || lvl_id == ""){
		alert("参数评估分档标识不能为空！");
		return;
	}

	if(lvl_name == null || lvl_name == ""){
		alert("参数评估分档标识不能为空！");
		return;
	}

	
	var value =   param_id+":"+param_type+":"+lvl_id+":"+lvl_name+":"+start_val+":"+end_val+":"+h_lvl_id;	
	var dimTableStr = baseXmlSubmit.callAction("../subject/OppSubejctParamLvlAjax.rptdo?oper_type=delete&value="+value);
	dimTableStr=dimTableStr.replace(/^\s+|\n+$/g,'');
	if(dimTableStr == ""){
		document.getElementById("oppparamlvlspan").innerHTML = "删除参数值域失败！";
		
	}else{
		document.getElementById("oppparamlvlspan").innerHTML = "删除参数值域成功！";
		document.getElementById("oppparamlvldiv").innerHTML = dimTableStr;
		document.dimForm.h_param_id.value = "";
		document.dimForm.h_param_type.value = "";
		document.dimForm.h_lvl_id.value =""; 
		document.dimForm.lvl_id.value =""; 
		document.dimForm.lvl_name.value =""; 
		document.dimForm.start_val.value =""; 
		document.dimForm.end_val.value =""; 
	}
	    closeWaitWin();
}

function selectValueLvl(obj){

	var arr = obj.value.split("$$");
	document.dimForm.h_param_id.value =arr[0]; 
	document.dimForm.h_param_type.value =arr[1]; 
	document.dimForm.h_lvl_id.value =arr[2]; 
	document.dimForm.lvl_id.value =arr[2]; 
	document.dimForm.lvl_name.value =arr[3]; 
	document.dimForm.start_val.value =arr[4]; 
	document.dimForm.end_val.value =arr[5]; 
	document.getElementById("oppparamlvlspan").innerHTML = "&nbsp;";

	
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
            <td height="1">&nbsp;</td>
          </tr>
          <tr>
            <td><table width="100%" >
              <tr>
                <td width="22"><img src="../biimages/icon_ztfx3.gif" width="16" height="16"></td>
                <td valign="bottom" nowrap><span class="title-bold">系统配置参数{<%=struct.getParam_name()%>}</span></td>
                <td ><span class="title-bold"><img src="../biimages/kw/broken-line.gif" ></span></td>
                <td width="54" id="y_1" style="display:">    
                </td> </tr>
              <tr id="defaultDim" style="display:">
                <td colspan="4" >
               <div id="oppparamlvldiv" style="width: 100%; margin-top: 2px; overflow: auto; height:307px;">
               <table style="width: 100%" border="0" bgcolor="#CFCFCF" cellpadding="1" cellspacing="1"
                                       style="margin:0;">          
                  <tr class="celtitle FixedTitleRow">
                  	<td align="center"  bgcolor="E3E3E3" width="10%">选择</td>   
                    <td align="center"  bgcolor="E3E3E3" width="20%">标识</td>
                    <td align="center"  bgcolor="E3E3E3" width="20%">名称</td>
                    <td align="center"  bgcolor="E3E3E3" width="20%">值上域（闭）</td>                    
                    <td align="center"  bgcolor="E3E3E3" width="20%">值下域（开）</td>                 
                  </tr>   
                  <%
                  ArrayList paramList = (ArrayList)struct.getValueObjs();
                  if(paramList!=null&&paramList.size() >0){
                	  for(int i=0;i<paramList.size();i++){
                		  OppParamValueStruct lvlStruct =(OppParamValueStruct)paramList.get(i);
                	 	  String radioValue = lvlStruct.getParam_id()+"$$"+lvlStruct.getParam_type()+"$$"+lvlStruct.getLvl_id()+"$$"+lvlStruct.getLvl_name()+"$$"+lvlStruct.getStart_val()+"$$"+lvlStruct.getEnd_val();
                	 
                	  %>
                	  
                	  <tr class="celdata" onmouseover="this.className='mouseM'" onmouseout="this.className='celdata'">
                	  <td align="center"><input type="radio"  name="param" value="<%=radioValue%>" onclick="selectValueLvl(this)"></td>          
                      <td align="center" ><%=lvlStruct.getLvl_id()%></td>
                      <td align="left" ><%=lvlStruct.getLvl_name()%></td>
                      <td align="center"><%=lvlStruct.getStart_val()%></td> 
                      <td align="left"><%=lvlStruct.getEnd_val()%></td> 
                    
                               
                                  
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
                <td valign="bottom" nowrap><span class="title-bold">{<%=struct.getParam_name()%>}参数值域编辑区</span></td>
                <td><span class="bulebig"><img src="../biimages/kw/broken-line.gif"></span></td>
              </tr>
              <tr>
                <td colspan="4" nowrap align="center"><span id="oppparamlvlspan" class="title-big-bold">&nbsp;</span></td>
              </tr>
                <tr>
                <td colspan="4" nowrap>
                <input name="h_lvl_id" type="hidden">
                <input name="h_param_id" type="hidden" value="<%=struct.getParam_id()%>">
                <input name="h_param_type" type="hidden" value="<%=struct.getParam_type()%>">
                <table width="100%"  border=1 cellpadding="0" cellspacing="0" borderColorLight=#C0C0C0 borderColorDark=#ffffff>
                  <tr>
                    <td align="center" class="td-bg">标识</td>
                    <td ><input name="lvl_id" type="text" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">名称</td>
                    <td ><input type="text" name="lvl_name" class="query-input-text-1" ></td>
                    </tr>
                    <tr>              
                    <td align="center" class="td-bg">值上域（闭）</td>
                    <td ><input type="text" name="start_val" class="query-input-text-1" ></td>
                    <td align="center" class="td-bg">值下域（开）</td>
                    <td ><input type="text" name="end_val" class="query-input-text-1" ></td>
                  </tr>              
                </table>
                  </td>
              </tr>
               <tr>
                <td colspan="4" nowrap>&nbsp;</td>
              </tr>
              <tr align="center">
                <td colspan="4" nowrap>
                  <input name="btn_add" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="增加" onClick="ShowWait();_newParamLvl();">
                  <input name="btn_back" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="修改" onClick="ShowWait();_editParamLvl();" >
                  <input name="btn_del" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"  value="删除" onClick="ShowWait();_delParamLvl();">
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
