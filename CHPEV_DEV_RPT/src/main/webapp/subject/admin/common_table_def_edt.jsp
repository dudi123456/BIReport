<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="com.ailk.bi.subject.admin.entity.UiSubjectCommonTableDef"%>
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
  
  UiSubjectCommonTableDef tableDef = (UiSubjectCommonTableDef)request.getAttribute(SubjectCommonConst.CONST_SUBJECT_TABLE_DEF);


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
                <td width="30%"><input type="text" name="table_id" maxlength="30" value="<%=tableDef.getTableId()%>" readonly class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td width="20%"><input type="hidden" name="throw_old" value="<%=tableDef.getThrowOld()%>"></td>
                <td width="30%"></td>
              </tr>
              <tr> 
                <td>报表名称：</td>
                <td colspan="3"><input type="text" name="table_name" maxlength="50" value="<%=tableDef.getTableName()%>" class="input-text" size="69" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
               <tr> 
                <td>报表描述：</td>
                <td colspan="3"><input type="text" name="table_desc" maxlength="50" value="<%=tableDef.getTableDesc()%>" class="input-text" size="69" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr> 
                <td>统计周期：</td>
                
                <td><SELECT ID=time_level name=time_level >
                	<OPTION value='6' <%if (tableDef.getTimeLevel().equals("6")) out.print("selected");%>>日报</OPTION>
                	<OPTION value='4' <%if (tableDef.getTimeLevel().equals("4")) out.print("selected");%>>月报</OPTION>
                </select>
               </td>                
                <td></td>
                <td id="pub"></td>
              
              </tr>
              <tr> 
                <td>日期字段名：</td>                
                <td><input type="text" name="time_field" value="<%=tableDef.getTimeField()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
               </td>                
                <td>日期字段类型:</td>
                <td id="pub"><SELECT ID=field_type name=field_type >
                	<OPTION value='1' <%if (tableDef.getFieldType().equals("1")) out.print("selected");%> >数字</OPTION>
                	<OPTION value='2' <%if (tableDef.getFieldType().equals("2")) out.print("selected");%>>文本</OPTION>
                </select></td>
              
              </tr>
             <tr> 
                <td>数据表名：</td>
                <td><input type="text" name="data_table" value="<%=tableDef.getDataTable()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>提取数据的条件</td>
                <td><input type="text" name="data_where" value="<%=tableDef.getDataWhere()%>" maxlength="200" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
             
              <tr> 
                <td>是否显示合计：</td>
                <td><input type='radio' id='sum_desplay' name='sum_desplay' value='Y'   <%if (tableDef.getSumDisplay().equals("Y")) out.print("checked");%> >是
<input type='radio' id='sum_desplay' name='sum_desplay' value='N' <%if (tableDef.getSumDisplay().equals("N")) out.print("checked");%>  >否
</td>
                <td>是否翻页显示：</td>
                <td><input type='radio' id='has_paging' name='has_paging' value='Y' <%if (tableDef.getHasPaging().equals("Y")) out.print("checked");%> >是
<input type='radio' id='has_paging' name='has_paging' value='N'   <%if (tableDef.getHasPaging().equals("N")) out.print("checked");%> >否
</td>
              </tr>
              <tr> 
                <td>是否展开:</td>
                <td><input type='radio' id='has_expand' name='has_expand' value='Y' <%if (tableDef.getHasExpand().equals("Y")) out.print("checked");%>>是
<input type='radio' id='has_expand' name='has_expand' value='N' <%if (tableDef.getHasExpand().equals("N")) out.print("checked");%>  >否</td>
                <td>是否全部展开</td>
                <td><input type='radio' id='has_expandall' name='has_expandall' value='Y' <%if (tableDef.getHasExpandall().equals("Y")) out.print("checked");%>   >是
<input type='radio' id='has_expandall' name='has_expandall' value='N' <%if (tableDef.getHasExpandall().equals("N")) out.print("checked");%>  >否</td>
              </tr>
              <tr> 
                <td>点击行图形是否变化</td><td><input type='radio' id='row_click_chart_chg' name='row_click_chart_chg' value='Y' <%if (tableDef.getRowClickedChartChange().equals("Y")) out.print("checked");%>  >是
<input type='radio' id='row_click_chart_chg' name='row_click_chart_chg' value='N' <%if (tableDef.getRowClickedChartChange().equals("N")) out.print("checked");%>  >否</td>
                <td>图形ID</td><td><input type="text" name="rlt_chart_id" value="<%=tableDef.getRltChartId()%>" maxlength="200" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                
              </tr>
               <tr> 
                <td>图形变化JS脚本</td><td><input type="text" name="chart_change_js" value="<%=tableDef.getChartChangeJs()%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>数据表格类型</td><td>
                	<SELECT ID=table_type name=table_type >
                	<OPTION value='100' <%if (tableDef.getTableType().equals("100")) out.print("selected");%> >普通</OPTION>
                	<OPTION value='101' <%if (tableDef.getTableType().equals("101")) out.print("selected");%>>日表</OPTION>
                	<OPTION value='102' <%if (tableDef.getTableType().equals("102")) out.print("selected");%>>月表</OPTION>
                </select>
                	</td>                
              </tr>
                <tr> 
                <td>维度是否作为展现列</td><td><input type='radio' id='dim_ascol' name='dim_ascol' value='Y' <%if (tableDef.getDimAscol().equals("Y")) out.print("checked");%>  >是
<input type='radio' id='dim_ascol' name='dim_ascol' value='N' <%if (tableDef.getDimAscol().equals("N")) out.print("checked");%>  >否</td>
                <td>是否是指标库中自定义指标</td><td><input type='radio' id='custom_msu' name='custom_msu' value='Y' <%if (tableDef.getCustomMsu().equals("Y")) out.print("checked");%>  >是
<input type='radio' id='custom_msu' name='custom_msu' value='N' <%if (tableDef.getCustomMsu().equals("N")) out.print("checked");%>  >否</td>
              </tr>
                  <tr> 
                <td>是否有复杂表头</td><td colspan=3><input type='radio' id='has_head' name='has_head' value='Y' <%if (tableDef.getHasHead().equals("Y")) out.print("checked");%>  >是
<input type='radio' id='has_head' name='has_head' value='N' <%if (tableDef.getHasHead().equals("N")) out.print("checked");%>  >否</td>
              
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
		  value="修改" onclick="setSubmitFlag('doEdtCommTblDef')">
	  <input name="btndelete" type="button"
      class="button" onmouseover="switchClass(this)"
      onmouseout="switchClass(this)" value="删除"
      onclick="_delete('doDelCommTblDef');" />
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
	 
	 
		if (reportEditForm.table_name.value==null || trim(reportEditForm.table_name.value)==''){
		  alert('报表名称不能为空！！！');
		  reportEditForm.table_name.focus();
		  return;
		}
		
		if (reportEditForm.data_table.value==null || trim(reportEditForm.data_table.value)==''){
		  alert('数据表名不能为空！！！');
		    reportEditForm.data_table.focus();
		  return;
		}
		
		 document.reportEditForm.submit();
	}
	
</script>