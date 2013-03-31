<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.adhoc.domain.*"%>

<%
//即席业务
String adhoc_id = (String)session.getAttribute(AdhocConstant.ADHOC_DIM_FIXED_ADHOC_ID);
//关联字段
String dim_relation_field = (String)session.getAttribute(AdhocConstant.ADHOC_DIM_FIXED_RELATION_FIELD);
//操作员工号
String oper_no = (String)session.getAttribute(AdhocConstant.ADHOC_DIM_FIXED_OPER_NO);
//定制分档
UiAdhocRuleUserDimTable[] list  = (UiAdhocRuleUserDimTable[])session.getAttribute(AdhocConstant.ADHOC_DIM_FIXED_VALUE);
if(list == null){
	list = new UiAdhocRuleUserDimTable[0];
}

%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">


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
//增加
function genSelfDimTable(){
	//
	var adhoc_id = document.dimForm.adhoc_id.value;
	var dim_relation_field = document.dimForm.dim_relation_field.value;
	var oper_no = document.dimForm.oper_no.value;
	var dim_id = document.dimForm.dim_id.value;
	var dim_name = document.dimForm.dim_name.value;
	var low_value = document.dimForm.low_value.value;
	var hign_value = document.dimForm.hign_value.value; 
	if((low_value == null || low_value == "")&&(hign_value == null || hign_value == "")){
		alert("值得上下限不允许同时为空！");
		return;
	}
	var value =   adhoc_id+":"+dim_relation_field+":"+oper_no+":"+dim_id+":"+dim_name+":"+low_value+":"+hign_value+":end";
	
	var dimStr = baseXmlSubmit.callAction("../adhoc/AdhocSelfAjax.rptdo?oper_type=add&value="+value);
    dimStr=dimStr.replace(/^\s+|\n+$/g,'');
    document.getElementById("selfDim").innerHTML = dimStr;
}
//删除
function delectDimValue(id,name,low,hign){
	var adhoc_id = document.dimForm.adhoc_id.value;
	var dim_relation_field = document.dimForm.dim_relation_field.value;
	var oper_no = document.dimForm.oper_no.value;
	var value =   adhoc_id+":"+dim_relation_field+":"+oper_no+":"+id+":"+name+":"+low+":"+hign;
	var dimStr = baseXmlSubmit.callAction("../adhoc/AdhocSelfAjax.rptdo?oper_type=delete&value="+value);  
    dimStr=dimStr.replace(/^\s+|\n+$/g,'');
    document.getElementById("selfDim").innerHTML = dimStr;
	
}

function showDim(type){
	if(type == "1"){
	alert(type);
		document.getElementById("y_1").style.display = "block";
		document.getElementById("defaultDim").style.display = "block";
		document.getElementById("y_2").style.display = "none";
	}else{
		document.getElementById("y_1").style.display = "none"
		document.getElementById("defaultDim").style.display = "none";
		document.getElementById("y_2").style.display = "block";
	}
	
}
</script>

</head>

<body class="main-body">
<form name="dimForm" method="post" action="">

<input type="hidden" name="adhoc_id" value="<%=adhoc_id%>">
<input type="hidden" name="dim_relation_field" value="<%=dim_relation_field%>">
<input type="hidden" name="oper_no" value="<%=oper_no%>">

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td> 
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td></td>
          </tr>
          <tr>
            <td><table width="100%" >
              <tr>
                <td width="22"><img src="../biimages/icon_ztfx3.gif" width="16" height="16"></td>
                <td width="100" valign="bottom" nowrap><span class="title-bold">系统默认维度</span></td>
                <td ><span class="title-bold"><img src="../biimages/kw/broken-line.gif" ></span></td>
                <td width="44" id="y_1" style="display:">
                <a href="javascript:;"  title="隐藏"><img src="../biimages/arrowup.gif" border="0" onclick="showDim(2)"></a>                
                </td>
                <td width="44" id="y_2" style="display:none">
                 <a href="javascript:;"  title="显示"><img src="../biimages/arrowdown.gif" border="0" onclick="showDim(1)"></a>              
                </td>
               
                </tr>
              <tr id="defaultDim" style="display:">
                <td colspan="4" class="search-bg"><table width="100%" border="0" cellpadding="0"  cellspacing="1" class="choice-table-bg">
                  <tr align="center">
                    <td colspan="2" class="title-bulebg"> 当前系统维度定义</td>
                  </tr>
                  <tr class="table-gay-bg2">
                    <td align="center" class="subject-title" width="40%">标识</td>
                    <td align="center" class="subject-title" width="60%">名称</td>                    
                  </tr>                 
                  <%=AdhocHelper.getSelfDimDefaultValue(session)%>
                </table></td>
              </tr>
              
              <tr>
                <td><img src="../biimages/feedback-ioc1.gif" width="22" height="14"></td>
                <td valign="bottom" nowrap><span class="title-bold">维度定制</span></td>
                <td><span class="bulebig"><img src="../biimages/kw/broken-line.gif"></span></td>
                </tr>
              <tr>
                <td colspan="4" id="selfDim"  nowrap>
                
                    <%=AdhocHelper.getSelfDimValue(list)%>
                
                
                </td>
              </tr>
              <tr>
                <td colspan="4" nowrap>
                <table width="100%"  border=1 cellpadding="0" cellspacing="0" borderColorLight=#C0C0C0 borderColorDark=#ffffff>
                  <tr>
                    <td align="center" class="td-bg">标识</td>
                    <td > <input name="dim_id" type="text" class="query-input-text"  ></td>
                    <td align="center" class="td-bg">名称</td>
                    <td ><input type="text" name="dim_name" class="query-input-text" ></td>
                    <td align="center" class="td-bg">值上域（闭）</td>
                    <td ><input type="text" name="low_value" class="query-input-text" ></td>
                    <td align="center" class="td-bg">值下域（开）</td>
                    <td ><input type="text" name="hign_value" class="query-input-text" ></td>
                    
                    </tr>
                 
                 
                </table>
                  </td>
              </tr>
              <tr align="center">
                <td colspan="4" nowrap>
                  <input name="btn_add" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="增加" onClick="genSelfDimTable()">
                  <input name="btn_back" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" onClick="window.close()" value="返回">
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
