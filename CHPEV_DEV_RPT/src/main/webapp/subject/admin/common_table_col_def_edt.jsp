<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="com.ailk.bi.subject.admin.entity.UiSubjectCommonColDef"%>
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
  UiSubjectCommonColDef tableDef = (UiSubjectCommonColDef)request.getAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF);

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
                <td width="20%">列ID(请用数字)：<input type="hidden" name="last_var_dis" value="<%=tableDef.getLastVarDisplay()%>"><input type="hidden" name="loop_var_dis" value="<%=tableDef.getLoopVarDisplay()%>"><input type="hidden" name="total_dis" value="<%=tableDef.getTotalDisplayed()%>"><input type="hidden" name="rowid" value="<%=tableDef.getRowId()%>"></td>
                <td width="30%"><input type="text" name="col_id" maxlength="30" value="<%=tableDef.getColId()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr> 
                <td>列名称：</td>             
                <td><input type="text" name="col_name" maxlength="30" value="<%=tableDef.getColName()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>列描述：</td>             
                <td><input type="text" name="col_desc" maxlength="30" value="<%=tableDef.getColDesc()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
               <tr> 
                <td>显示顺序(请用数字)</td>
                <td><input type="text" name="col_sequence" maxlength="30" value="<%=tableDef.getColSequence()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>是否是指标：</td>
                <td><input type='radio' id='is_measure' name='is_measure' value='Y' <%if (tableDef.getIsMeasure().equals("Y")) out.print("checked");%> >是
<input type='radio' id='is_measure' name='is_measure' value='N'  <%if (tableDef.getIsMeasure().equals("N")) out.print("checked");%> >否</td>
              </tr>
              
              <tr> 
                <td>是否维度当成条件：</td>                
                <td><input type='radio' id='dim_aswhere' name='dim_aswhere' value='Y' <%if (tableDef.getDimAswhere().equals("Y")) out.print("checked");%> >是
<input type='radio' id='dim_aswhere' name='dim_aswhere' value='N'  <%if (tableDef.getDimAswhere().equals("N")) out.print("checked");%> >否
               </td>                
                <td>是否默认显示</td>
                <td><input type='radio' id='default_display' name='default_display' value='Y' <%if (tableDef.getDefaultDisplay().equals("Y")) out.print("checked");%>>是
<input type='radio' id='default_display' name='default_display' value='N' <%if (tableDef.getDefaultDisplay().equals("N")) out.print("checked");%>>否</td>              
              </tr>
              <tr> 
                <td>是否展开列：</td>                
                <td><input type='radio' id='is_expand_col' name='is_expand_col' value='Y' <%if (tableDef.getIsExpandCol().equals("Y")) out.print("checked");%>>是
<input type='radio' id='is_expand_col' name='is_expand_col' value='N' <%if (tableDef.getIsExpandCol().equals("N")) out.print("checked");%> >否</td>                 
                <td>是否默认钻取:</td>
                <td><input type='radio' id='default_drill' name='default_drill' value='Y' <%if (tableDef.getDefaultDrilled().equals("Y")) out.print("checked");%>>是
<input type='radio' id='default_drill' name='default_drill' value='N' <%if (tableDef.getDefaultDrilled().equals("N")) out.print("checked");%> >否</td>         
              </tr>
               <tr> 
                <td>初始级别：</td>                
                <td><input type="text" name="init_level" maxlength="30" value="" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>             
                <td>是否维度当成列:</td>
                <td><input type='radio' id='dim_ascol' name='dim_ascol' value='Y' <%if (tableDef.getDimAscol().equals("Y")) out.print("checked");%>>是
<input type='radio' id='dim_ascol' name='dim_ascol' value='N' <%if (tableDef.getDimAscol().equals("N")) out.print("checked");%> >否</td>         
              </tr>        
			     <tr> 
                <td>代码字段：</td>                
                <td><input type="text" name="code_field" maxlength="30" value="<%=tableDef.getCodeField()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>             
                <td>代码描述字段:</td>
                <td><input type="text" name="desc_field" maxlength="30" value="<%=tableDef.getDescField()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>        
              </tr>       
			  <tr> 
                <td>是否比率显示:</td>                
                <td><input type='radio' id='is_ratio' name='is_ratio' value='Y' <%if (tableDef.getIsRatio().equals("Y")) out.print("checked");%>>是
<input type='radio' id='is_ratio' name='is_ratio' value='N' <%if (tableDef.getIsRatio().equals("N")) out.print("checked");%> >否</td>              
                <td>数据类型:</td>
                <td><input type='radio' id='data_type' name='data_type' value='0' <%if (tableDef.getDataType().equals("0")) out.print("checked");%>>文本
<input type='radio' id='data_type' name='data_type' value='1' <%if (tableDef.getDataType().equals("1")) out.print("checked");%> >数字</td>      
              </tr>       
   <tr> 
                <td>小数位数：</td>                
                <td><input type="text" name="digit_length" maxlength="30" value="<%=tableDef.getDigitLength()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>             
                <td>是否显示同比:</td>
                <td><input type='radio' id='has_comratio' name='has_comratio' value='Y' <%if (tableDef.getHasComratio().equals("Y")) out.print("checked");%>>是
<input type='radio' id='has_comratio' name='has_comratio' value='N' <%if (tableDef.getHasComratio().equals("N")) out.print("checked");%> >否</td>        
              </tr> 
			     <tr> 
                <td>是否同期</td>                
                <td><input type='radio' id='has_last' name='has_last' value='Y' <%if (tableDef.getHasLast().equals("Y")) out.print("checked");%>>是
<input type='radio' id='has_last' name='has_last' value='N' <%if (tableDef.getHasLast().equals("N")) out.print("checked");%> >否     
               </td>                
                <td>是否有超链接</td>
                <td><input type='radio' id='has_link' name='has_link' value='Y' <%if (tableDef.getHasLink().equals("Y")) out.print("checked");%>>是
<input type='radio' id='has_link' name='has_link' value='N' <%if (tableDef.getHasLink().equals("N")) out.print("checked");%> >否</td>              
              </tr>
              <tr> 
                <td>超链接地址：</td>                
                <td><input type="text" name="link_url" value="<%=tableDef.getLinkUrl()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
               </td>                
                <td>链接目标:</td>
                <td><input type="text" name="link_target" value="<%=tableDef.getLinkTarget()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>   </tr>
				    <tr> 
                <td>同期值是否显示</td>                
                <td><input type='radio' id='last_display' name='last_display' value='Y' <%if (tableDef.getLastDisplay().equals("Y")) out.print("checked");%>>是
<input type='radio' id='last_display' name='last_display' value='N' <%if (tableDef.getLastDisplay().equals("N")) out.print("checked");%> >否     
               </td>                
                <td>上升颜色预警</td>
                <td><input type='radio' id='rise_color' name='rise_color' value='' <%if (tableDef.getRiseArrowColor().length()==0) out.print("checked");%>>不设置
<input type='radio' id='rise_color' name='rise_color' value='R' <%if (tableDef.getRiseArrowColor().equals("R")) out.print("checked");%>>红色<input type='radio' id='rise_color' name='rise_color' value='G' <%if (tableDef.getRiseArrowColor().equals("G")) out.print("checked");%>>绿色</td>          
              </tr>
     <tr> 
                <td>是否环比</td>                
                <td><input type='radio' id='has_loop' name='has_loop' value='Y' <%if (tableDef.getHasLoop().equals("Y")) out.print("checked");%>>是
<input type='radio' id='has_loop' name='has_loop' value='N' <%if (tableDef.getHasLoop().equals("N")) out.print("checked");%> >否     
               </td>                
                <td>是否有同比超链接</td>
                <td><input type='radio' id='has_last_link' name='has_last_link' value='Y' <%if (tableDef.getHasLastLink().equals("Y")) out.print("checked");%>>是
<input type='radio' id='has_last_link' name='has_last_link' value='N' <%if (tableDef.getHasLastLink().equals("N")) out.print("checked");%> >否</td>              
              </tr>
              <tr> 
                <td>同比超链接地址：</td>                
                <td><input type="text" name="last_url" value="<%=tableDef.getLastUrl()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
               </td>                
                <td>同比链接目标:</td>
                <td><input type="text" name="last_target" value="<%=tableDef.getLastTarget()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>   </tr>
 <tr> 
                <td>环比是否显示</td>                
                <td><input type='radio' id='loop_display' name='loop_display' value='Y' <%if (tableDef.getLoopDisplay().equals("Y")) out.print("checked");%>>是
<input type='radio' id='loop_display' name='loop_display' value='N' <%if (tableDef.getLoopDisplay().equals("N")) out.print("checked");%> >否     
               </td>                
                <td>是否有环比超链接</td>
                <td><input type='radio' id='has_loop_link' name='has_loop_link' value='Y' <%if (tableDef.getHasLoopLink().equals("Y")) out.print("checked");%>>是
<input type='radio' id='has_loop_link' name='has_loop_link' value='N' <%if (tableDef.getHasLoopLink().equals("N")) out.print("checked");%> >否</td>              
              </tr>
              <tr> 
                <td>环比超链接地址：</td>                
                <td><input type="text" name="loop_url" value="<%=tableDef.getLoopUrl()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
               </td>                
                <td>环比链接目标:</td>
                <td><input type="text" name="loop_target" value="<%=tableDef.getLoopTarget()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>   </tr>
 <tr> 
                <td>是否点击列图形变化</td>                
                <td><input type='radio' id='is_col_click_chart_chg' name='is_col_click_chart_chg' value='Y' <%if (tableDef.getIsColClickChartChange().equals("Y")) out.print("checked");%>>是
<input type='radio' id='is_col_click_chart_chg' name='is_col_click_chart_chg' value='N' <%if (tableDef.getIsColClickChartChange().equals("N")) out.print("checked");%> >否     
               </td>                
                <td>列变化图形的ID</td>
                <td><input type="text" name="col_rlt_chart_id" value="<%=tableDef.getColRltChartId()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
			   <tr> 
                <td>是否点击单元格图形变化</td>                
                <td><input type='radio' id='is_celll_click_chart_chg' name='is_celll_click_chart_chg' value='Y' <%if (tableDef.getIsCellClickChartChange().equals("Y")) out.print("checked");%>>是
<input type='radio' id='is_celll_click_chart_chg' name='is_celll_click_chart_chg' value='N' <%if (tableDef.getIsCellClickChartChange().equals("N")) out.print("checked");%> >否     
               </td>                
                <td>单元格变化图形的ID</td>
                <td><input type="text" name="cell_rlt_chart_id" value="<%=tableDef.getCellRltChartId()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>

<tr> 
                <td>状态：</td>                
                <td><input type='radio' id='status' name='status' value='Y' <%if (tableDef.getStatus().equals("Y")) out.print("checked");%>>有效
<input type='radio' id='status' name='status' value='N' <%if (tableDef.getStatus().equals("N")) out.print("checked");%>>否     
               </td>                
                <td>是否描述当成提示</td>
                <td><input type='radio' id='desc_astitle' name='desc_astitle' value='Y' <%if (tableDef.getDescAstitle().equals("Y")) out.print("checked");%>>有效
<input type='radio' id='desc_astitle' name='desc_astitle' value='N' <%if (tableDef.getDescAstitle().equals("N")) out.print("checked");%>>否</td>
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
		  value="确定修改" onclick="setSubmitFlag('doEdtCommTblColDef')">

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
		
		if (reportEditForm.col_id.value==null || trim(reportEditForm.col_id.value)==''){
		  alert('列ID不能为空！！！');
		  reportEditForm.col_id.focus();
		  return;
		}
		
		if (reportEditForm.col_name.value==null || trim(reportEditForm.col_name.value)==''){
		  alert('列名称不能为空！！！');
		    reportEditForm.col_name.focus();
		  return;
		}
	if (reportEditForm.code_field.value==null || trim(reportEditForm.code_field.value)==''){
		  alert('字段ID不能为空！！！');
		    reportEditForm.code_field.focus();
		  return;
		}

		 document.reportEditForm.submit();
		 
	}
	
</script>