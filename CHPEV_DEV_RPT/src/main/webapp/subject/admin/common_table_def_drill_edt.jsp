<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="com.ailk.bi.subject.admin.entity.UiSubjectCommDimhierarchy"%>
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
  
  UiSubjectCommDimhierarchy drillObj = (UiSubjectCommDimhierarchy)request.getAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF);


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
                <td width="30%"><input type="text" name="table_id" maxlength="30" value="<%=drillObj.getTableId()%>" readonly class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td width="20%"><input type="hidden" name="col_id" value="<%=drillObj.getColId()%>"><input type="hidden" name="lev_id" value="<%=drillObj.getLevId()%>"><input type="hidden" name="idfld_type" value="<%=drillObj.getIdfldType()%>"></td>
                <td width="30%"></td>
              </tr>
              <tr> 
                <td>钻取名称：</td>             
                <td><input type="text" name="lev_name" maxlength="30" value="<%=drillObj.getLevName()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>钻取描述：</td>             
                <td><input type="text" name="lev_memo" maxlength="30" value="<%=drillObj.getLevMemo()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
               <tr> 
                <td>源字段ID：</td>
                <td><input type="text" name="src_idfld" maxlength="30" value="<%=drillObj.getSrcIdfld()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>源字段描述名称：</td>
                <td><input type="text" name="src_namefld" maxlength="30" value="<%=drillObj.getSrcNamefld()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              
              <tr> 
                <td>是否描述当成提示：</td>                
                <td><SELECT ID="desc_astitle" name=desc_astitle >
                	<OPTION value='Y' <%if (drillObj.getDescAstitle().equals("Y")) out.print("selected");%>>是</OPTION>
                	<OPTION value='N' <%if (drillObj.getDescAstitle().equals("N")) out.print("selected");%>>否</OPTION>
                </select>
               </td>                
                <td>是否有超链接</td>
                <td><input type='radio' id='has_link' name='has_link' value='Y'   <%if (drillObj.getHasLink().equals("Y")) out.print("checked");%> >是
<input type='radio' id='has_link' name='has_link' value='N' <%if (drillObj.getHasLink().equals("N")) out.print("checked");%>  >否</td>              
              </tr>
              <tr> 
                <td>超链接地址：</td>                
                <td><input type="text" name="link_url" value="<%=drillObj.getLinkUrl()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
               </td>                
                <td>链接目标:</td>
                <td><input type="text" name="link_target" value="<%=drillObj.getLinkTarget()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>              
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
		  value="修改" onclick="setSubmitFlag('doEdtCommTblDefDrill')">
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
		
		 document.reportEditForm.submit();
	}
	
</script>