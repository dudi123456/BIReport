<%@page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
  %>
  
</head>

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
                <td width="30%"><input type="text" name="table_id" maxlength="30" value="" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td width="20%"><input type="hidden" name="throw_old" value="N"></td>
                <td width="30%"></td>
              </tr>
              <tr> 
                <td>报表名称：</td>
                <td colspan="3"><input type="text" name="table_name" maxlength="50" value="" class="input-text" size="69" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
               <tr> 
                <td>报表描述：</td>
                <td colspan="3"><input type="text" name="table_desc" maxlength="50" value="" class="input-text" size="69" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr> 
                <td>统计周期：</td>
                
                <td><SELECT ID=time_level name=time_level >
                	<OPTION value='6' selected >日报</OPTION>
                	<OPTION value='4'>月报</OPTION>
                </select>
               </td>                
                <td></td>
                <td id="pub"></td>
              
              </tr>
              <tr> 
                <td>日期字段名：</td>                
                <td><input type="text" name="time_field" value="RPT_DATE" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)">
               </td>                
                <td>日期字段类型:</td>
                <td id="pub"><SELECT ID=field_type name=field_type >
                	<OPTION value='1' selected >数字</OPTION>
                	<OPTION value='2'>文本</OPTION>
                </select></td>
              
              </tr>
             <tr> 
                <td>数据表名：</td>
                <td><input type="text" name="data_table" value="RPTW0001_temp_test" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>提取数据的条件</td>
                <td><input type="text" name="data_where" value="where table_id = 'RPTW0001'" maxlength="200" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
             
              <tr> 
                <td>是否显示合计：</td>
                <td><input type='radio' id='sum_desplay' name='sum_desplay' value='Y'   checked  >是
<input type='radio' id='sum_desplay' name='sum_desplay' value='N'   >否
</td>
                <td>是否翻页显示：</td>
                <td><input type='radio' id='has_paging' name='has_paging' value='Y'   >是
<input type='radio' id='has_paging' name='has_paging' value='N'   checked  >否
</td>
              </tr>
              <tr> 
                <td>是否展开:</td>
                <td><input type='radio' id='has_expand' name='has_expand' value='Y' checked  >是
<input type='radio' id='has_expand' name='has_expand' value='N'  >否</td>
                <td>是否全部展开</td>
                <td><input type='radio' id='has_expandall' name='has_expandall' value='Y' checked   >是
<input type='radio' id='has_expandall' name='has_expandall' value='N'  >否</td>
              </tr>
              <tr> 
                <td>点击行图形是否变化</td><td><input type='radio' id='row_click_chart_chg' name='row_click_chart_chg' value='Y'  >是
<input type='radio' id='row_click_chart_chg' name='row_click_chart_chg' value='N' checked  >否</td>
                <td>图形ID</td><td><input type="text" name="rlt_chart_id" value="" maxlength="200" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                
              </tr>
               <tr> 
                <td>图形变化JS脚本</td><td><input type="text" name="chart_change_js" value="" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>数据表格类型</td><td>
                	<SELECT ID=table_type name=table_type >
                	<OPTION value='100' selected >普通</OPTION>
                	<OPTION value='101'>日表</OPTION>
                	<OPTION value='102'>月表</OPTION>
                </select>
                	</td>                
              </tr>
                <tr> 
                <td>维度是否作为展现列</td><td><input type='radio' id='dim_ascol' name='dim_ascol' value='Y'  >是
<input type='radio' id='dim_ascol' name='dim_ascol' value='N' checked  >否</td>
                <td>是否是指标库中自定义指标</td><td><input type='radio' id='custom_msu' name='custom_msu' value='Y'  >是
<input type='radio' id='custom_msu' name='custom_msu' value='N' checked  >否</td>
              </tr>
                  <tr> 
                <td>是否有复杂表头</td><td colspan=3><input type='radio' id='has_head' name='has_head' value='Y'  >是
<input type='radio' id='has_head' name='has_head' value='N' checked  >否</td>
              
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
		  value="保存" onclick="setSubmitFlag('saveCommTblDef')">
	  
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
function trim(str) {  
    // 用正则表达式将前后空格  
    // 用空字符串替代。  
    return str.replace(/(^\s*)|(\s*$)/g, "");  
}

	function setSubmitFlag(type){
	  document.reportEditForm.opt_type.value = type;
	  if (reportEditForm.table_id.value==null || trim(reportEditForm.table_id.value)==''){
		  alert('报表ID不能为空！！！');
		  reportEditForm.table_id.focus();
		  return;
		}
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