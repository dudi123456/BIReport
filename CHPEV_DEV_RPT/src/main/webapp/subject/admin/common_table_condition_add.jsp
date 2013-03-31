<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="com.ailk.bi.subject.admin.entity.UiPubInfoCondition"%>
<%@page import="com.ailk.bi.subject.admin.SubjectCommonConst"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>

</head>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
  String table_id = (String)request.getAttribute("table_id");
%>
<body>
<FORM name="reportEditForm" action="SubjectCommonTblDef.rptdo" method="POST">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top" class="squareB"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="5" height="7" background="../images/square_line_2.gif"></td>
          <td width="100%" colspan="2"></td>
          <td><img src="../images/tab/field_upline_right.gif" width="5" height="7"></td>
        </tr>
        <tr> 
          <td height="7" background="../images/square_line_2.gif">&nbsp;</td>
          <td height="100%" colspan="2" valign="top"> <table width="95%" align="center" cellpadding="5">
              <tr> 
                <td width="20%">报表ID：</td>
                <td width="30%"><input type="text" name="table_id" maxlength="30" value="<%=table_id%>" readonly class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td width="20%"></td>
                <td width="30%"></td>
              </tr>
              <tr> 
                <td>资源类型：</td>             
                <td><SELECT ID="res_type" name=res_type >
                	<OPTION value='0' selected >报表</OPTION>
                	<OPTION value='1' >分析型表格</OPTION>
                	<OPTION value='2' >图形</OPTION>
                	<OPTION value='10' >地图</OPTION>                	
                </select>
               </td>
                <td>查询类型：</td>             
                <td><input type='radio' id='qry_type' name='qry_type' value='0' checked>查询结构
<input type='radio' id='qry_type' name='qry_type' value='1' >超链接</td>
              </tr>
               <tr> 
                <td>查询代码：</td>
                <td><input type="text" name="qry_code" maxlength="30" value="" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>条件代码：</td>
                <td><input type="text" name="con_code" maxlength="30" value="" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              
              <tr> 
                <td>字段类型：</td>                
                <td><SELECT ID="data_type" name="data_type" >
                	<OPTION value='1'   selected >数值型</OPTION>
									<OPTION value='2'   >字符型</OPTION>
                </select>
               </td>                
                <td>条件操作符号</td>
                <td><SELECT ID=con_tag name=con_tag ><OPTION value='='   selected  >=</OPTION>
<OPTION value='>='   >>=</OPTION>
<OPTION value='<='   ><=</OPTION>
<OPTION value='in'   >in</OPTION>
<OPTION value='like'   >like</OPTION>
<OPTION value='like_r'   >右like</OPTION>
<OPTION value='like_l'   >左like</OPTION>
</SELECT></td>             
              </tr>
              <tr> 
                <td>排序ID:</td>                
                <td><input type="text" name="sequence" value="1" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
               </td>                
                <td>是否有效：</td>
                <td><input type='radio' id='status' name='status' value='Y' checked >有效
<input type='radio' id='status' name='status' value='N' >无效</td>
</td>              
              </tr>
                        
            </table></td>
          <td background="../images/square_line_3.gif">&nbsp;</td>
        </tr>
        <tr> 
          <td height="5"><img src="../images/square_corner_3.gif" width="5" height="5"></td>
          <td colspan="2" background="../images/square_line_4.gif"></td>
          <td><img src="../images/square_corner_4.gif" width="5" height="5"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td height="35" align="center" valign="bottom">
      <input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)" 
		  value="确定" onclick="setSubmitFlag('doAddCommTblCondition')">

	  <input name="btnclose" type="button" class="button"	onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		  value="关闭" onclick="javascript:window.close();" >
	 
	</td>
  </tr>
</table>
<INPUT TYPE="hidden" id="opt_type" name="opt_type" value="" />
<INPUT TYPE="hidden" id="opSubmit" name="opSubmit" value="" />
<INPUT TYPE="hidden" id="opDirection" name="opDirection" value="" />
</FORM>
</body>
</html>
<script language="javascript">
//add by wenna 校验长度
function checkLength(obj,len,desc){
	var length=obj.value.length;
	if(length>len){
		alert(desc+"不能超过"+len+"个字!");
		return false;
	}
	return true;
}

function _delete(opt){
 if(confirm("您确定要删除吗？此操作不可恢复!")){
 	 document.reportEditForm.opt_type.value = opt;
 	
	document.reportEditForm.submit();
}
}

function trim(str) {  
    // 用正则表达式将前后空格  
    // 用空字符串替代。  
    return str.replace(/(^\s*)|(\s*$)/g, "");  
}

	function setSubmitFlag(type){
	  document.reportEditForm.opt_type.value = type;
		
		if (reportEditForm.qry_code.value==null || trim(reportEditForm.qry_code.value)==''){
		  alert('查询代码不能为空！！！');
		  reportEditForm.qry_code.focus();
		  return;
		}
		
		if (reportEditForm.con_code.value==null || trim(reportEditForm.con_code.value)==''){
		  alert('条件代码不能为空！！！');
		    reportEditForm.con_code.focus();
		  return;
		}
		if (reportEditForm.sequence.value==null || trim(reportEditForm.sequence.value)==''){
		  alert('排序ID不能为空！！！');
		    reportEditForm.sequence.focus();
		  return;
		}
		 document.reportEditForm.submit();
	
//	window.close();
	}
	
</script>