<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@ page import="com.ailk.bi.base.util.SQLGenator"%>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%@ page import="com.ailk.bi.base.table.PubInfoChartDefTable"%>
<%@ page import="com.ailk.bi.base.table.PubInfoChartFunTable"%>
<%@ page import="com.ailk.bi.report.util.ReportConsts"%>
<%@ page import="com.ailk.bi.report.util.*"%>
<%@page import="java.sql.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//图形对象
PubInfoChartDefTable chartDef = (PubInfoChartDefTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
if(chartDef==null){
	out.print("<center>");
	out.print("<br><br>操作信息丢失！<br>");
	out.print("</center>");
	return;
}
PubInfoChartFunTable[] chartFun = (PubInfoChartFunTable[])session.getAttribute(WebKeys.ATTR_REPORT_CHART_FUN);
//图形类型
String jfreechart_sql = "select CHART_TYPE_ID, CHART_TYPE_DESC from UI_PUB_INFO_CHART_KIND_DEF where CHART_DISTYPE=1 order by dis_sequence";
String fusionchart_multisql = "select CHART_TYPE_ID, CHART_TYPE_DESC from UI_PUB_INFO_CHART_KIND_DEF where CHART_DISTYPE=2 order by dis_sequence";
String fusionchart_singlesql = "select CHART_TYPE_ID, CHART_TYPE_DESC from UI_PUB_INFO_CHART_KIND_DEF where CHART_DISTYPE=3 order by dis_sequence";
String fusionchart_scatter_sql = "select CHART_TYPE_ID, CHART_TYPE_DESC from UI_PUB_INFO_CHART_KIND_DEF where CHART_DISTYPE=4 order by dis_sequence";
String fusionchart_bubble_sql = "select CHART_TYPE_ID, CHART_TYPE_DESC from UI_PUB_INFO_CHART_KIND_DEF where CHART_DISTYPE=5 order by dis_sequence";
//图形提交类型
String submit = "";
if("".equals(chartDef.chart_id)){
	submit="insert";
}else{
	submit="update";
}

%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
</head>

<body onLoad="change_parent('<%=chartDef.chart_distype%>');">
<FORM name="reportEditForm" action="ChartList.rptdo" method="POST">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr bgcolor="#b3eaff">
    <td valign="top" bgcolor="#b3eaff" class="squareB">
    	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="5" height="7" background="../biimages/square_line_2.gif"></td>
          <td width="100%" colspan="2">*****图形配置定义*****
          <input name="bc" type="button" class="button2" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
		  value="保存内容" onclick="setSubmitFlag('chartedit','<%=submit%>','current')">
		  <input name="bc" type="button" class="button2" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
		  value="添加新的" onclick="setSubmitFlag('new','<%=submit%>','current')">
		  <input name="bc" type="button" class="button2" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
		  value="预览图形" onclick="_preview('<%=chartDef.chart_id%>','')"></td>
          <td><img src="../biimages/tab/field_upline_right.gif" width="5" height="7"></td>
        </tr>
        <tr>
          <td height="7" background="../biimages/square_line_2.gif">&nbsp;</td>
          <td height="100%" colspan="2" valign="top"> <table width="95%" align="center" cellpadding="5">
              <tr>
                <td width="14%">图形ID：</td>
                <td width="23%"><input type="text" name="chart_id" maxlength="20" value="<%=chartDef.chart_id%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)" <%if(!"".equals(chartDef.chart_id)){out.print("readonly"); }%>></td>
                <td width="14%">图形归属名称：</td>
                <td><input type="text" name="chart_belong" maxlength="22" value="<%=chartDef.chart_belong%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr>
                <td>图资源类型：</td>
                <td><BIBM:TagSelectList listName="chart_distype" listID="S4001" focusID="<%=chartDef.chart_distype%>" script="onclick=change_parent(this.value)"/></td>
                <td>图形类型：</td>
                <td id="jfreechart"><BIBM:TagSelectList listID="0" listName="jfreechart" focusID="<%=chartDef.chart_type%>" selfSQL="<%=jfreechart_sql%>" /></td>
                <td id="fusionchart_multi"><BIBM:TagSelectList listID="0" listName="fusionchart_multi" focusID="<%=chartDef.chart_type%>" selfSQL="<%=fusionchart_multisql%>" /></td>
                <td id="fusionchart_single"><BIBM:TagSelectList listID="0" listName="fusionchart_single" focusID="<%=chartDef.chart_type%>" selfSQL="<%=fusionchart_singlesql%>" /></td>
                <td id="fusionchart_scatter"><BIBM:TagSelectList listID="0" listName="fusionchart_scatter" focusID="<%=chartDef.chart_type%>" selfSQL="<%=fusionchart_scatter_sql%>" /></td>
                <td id="fusionchart_bubble"><BIBM:TagSelectList listID="0" listName="fusionchart_bubble" focusID="<%=chartDef.chart_type%>" selfSQL="<%=fusionchart_bubble_sql%>" /></td>
              </tr>
              <tr>
                <td><a href="javascript:_attribute('sub1')" class="bule">图形属性：（点击查看说明）</a></td>
                <td colspan="3">
                <textarea name="chart_attribute" cols="120" rows="4" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this);checkLength(this,500,'图形属性')"><%=chartDef.chart_attribute%></textarea>
				</td>
              </tr>
              <tr>
                <td><a href="javascript:_attribute('sub2')" class="bule">图形序列属性：（点击查看说明，分号区分各个序列）</a></td>
                <td colspan="3">
                <textarea name="series_attribute" cols="120" rows="4" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this);checkLength(this,500,'图形序列属性')"><%=chartDef.series_attribute%></textarea>
				</td>
              </tr>
              <tr>
                <td>图形SQL语句：</td>
                <td colspan="3">
                <textarea name="sql_main" cols="120" rows="4" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this);checkLength(this,2000,'图形SQL语句')"><%=chartDef.sql_main%></textarea>
				</td>
              </tr>
              <tr>
                <td>图形条件语句：</td>
                <td colspan="3"><input type="text" name="sql_where" maxlength="200" value="<%=chartDef.sql_where%>" class="input-text" size="120" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr>
                <td>图形排序分组语句：</td>
                <td colspan="3"><input type="text" name="sql_order" maxlength="500" value="<%=chartDef.sql_order%>" class="input-text" size="120" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr>
                <td>图形数据组织类型：</td>
                <td colspan="3"><BIBM:TagRadio radioName="isusecd" radioID="S4002" focusID="<%=chartDef.isusecd%>"/>&nbsp;&nbsp;&nbsp;&nbsp;<b>根据后续括号标注配置下列字段</b></td>
              </tr>
              <tr>
                <td><b>图形X轴描述索引列：</b></td>
                <td><input type="text" name="series_index" value="<%=chartDef.series_index%>" class="input-text" size="5" onFocus="switchClass(this)" onBlur="switchClass(this)" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">(0)(1)(2)</td>
                <td><b>图形分组索引列：</b></td>
                <td><input type="text" name="category_index" value="<%=chartDef.category_index%>" class="input-text" size="5" onFocus="switchClass(this)" onBlur="switchClass(this)" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">(0)(1)</td>
              </tr>
              <tr>
                <td><b>图形Y轴数据索引列：</b></td>
                <td colspan="3"><input type="text" name="value_index" maxlength="20" value="<%=chartDef.value_index%>" class="input-text" size="20" onFocus="switchClass(this)" onBlur="switchClass(this)">(0)(1)</td>
              </tr>
              <tr>
                <td><b>分组描述设置：</b></td>
                <td colspan="3"><input type="text" name="category_desc" maxlength="200" value="<%=chartDef.category_desc%>" class="input-text" size="120" onFocus="switchClass(this)" onBlur="switchClass(this)">(1)(2)</td>
              </tr>
              <tr>
                <td><b>分组描述索引设置：</b></td>
                <td colspan="3"><input type="text" name="category_desc_index" maxlength="200" value="<%=chartDef.category_desc_index%>" class="input-text" size="120" onFocus="switchClass(this)" onBlur="switchClass(this)">(2)</td>
              </tr>
              <tr>
                <td>图形特殊变化列索引：</td>
                <td><input type="text" name="chart_index" value="<%=chartDef.chart_index%>" class="input-text" size="5" onFocus="switchClass(this)" onBlur="switchClass(this)" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"></td>
                <td>图形X轴趋势长度设置：</td>
                <td><input type="text" name="series_length" value="<%=chartDef.series_length%>" class="input-text" size="5" onFocus="switchClass(this)" onBlur="switchClass(this)" onKeyPress="JHshNumberText()" ></td>
              </tr>
              <tr>
                <td>图形X轴描述截取设置：</td>
                <td><BIBM:TagRadio radioName="series_cut" radioID="S4003" focusID="<%=chartDef.series_cut%>"/></td>
                <td>图形X轴描述截取定位：</td>
                <td><input type="text" name="series_cut_len" value="<%=chartDef.series_cut_len%>" class="input-text" size="5" onFocus="switchClass(this)" onBlur="switchClass(this)" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"></td>
              </tr>
            </table></td>
          <td background="../biimages/square_line_3.gif">&nbsp;</td>
        </tr>
        <tr>
          <td height="5"><img src="../biimages/square_corner_3.gif" width="5" height="5"></td>
          <td colspan="2" background="../biimages/square_line_4.gif"></td>
          <td><img src="../biimages/square_corner_4.gif" width="5" height="5"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td height="35" align="center" valign="bottom">
      图形选择功能配置
	</td>
  </tr>
  <tr>
    <td>
    <TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
	  <tr align="center">
		<td class="tab-title">类型描述</td>
		<td class="tab-title">分组描述</td>
		<td class="tab-title">分组描述索引</td>
		<td class="tab-title">Y轴数据索引</td>
		<td class="tab-title">条件内容</td>
		<td class="tab-title">替换内容</td>
		<td class="tab-title">是否选中</td>
		<td class="tab-title">操作</td>
	  </tr>
	  <%if(chartFun==null||chartFun.length==0){ %>
	  <tr class="table-white-bg">
		<td colspan="7" nowrap align="center">该条件下没有符合要求的数据</td>
	  </tr>
	  <%}else{ %>
	  <%for(int i=0;i<chartFun.length;i++){ %>
	  <tr class="table-tr">
		<td><input type="text" name="fun_chart_desc<%=i%>" value="<%=chartFun[i].chart_desc%>" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_category_desc<%=i%>" value="<%=chartFun[i].category_desc%>" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_category_desc_index<%=i%>" value="<%=chartFun[i].category_desc_index%>" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_value_index<%=i%>" value="<%=chartFun[i].value_index%>" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_wheresql<%=i%>" value="<%=chartFun[i].wheresql%>" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_replace_content<%=i%>" value="<%=chartFun[i].replace_content%>" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><%String name = "fun_is_checked"+i;%><BIBM:TagRadio radioName="<%=name%>" radioID="S3005" focusID="<%=chartFun[i].is_checked%>"/></td>
		<td nowrap align="center">
		<input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
		  value="修改" onclick="setSubmitFlag('chartfunedit','update','<%=i%>')">
		<input name="del" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
		  value="删除" onclick="setSubmitFlag('chartfunedit','chartfundelete','<%=i%>')">
		<INPUT TYPE="hidden" id="fun_col_sequence<%=i%>" name="fun_col_sequence<%=i%>" value="<%=chartFun[i].col_sequence%>" />
		</td>
	  </tr>
	  <%} %>
	  <%} %>
	  <tr class="table-tr" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
		<td><input type="text" name="fun_chart_desc" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_category_desc" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_category_desc_index" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_value_index" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_wheresql" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><input type="text" name="fun_replace_content" maxlength="20" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
		<td><BIBM:TagRadio radioName="fun_is_checked" radioID="S3005" focusID="N"/></td>
		<td nowrap align="center"><input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
		  value="新增" onclick="setSubmitFlag('chartfunedit','insert','current')"></td>
	  </tr>
	</TABLE>
    </td>
  </tr>
  <tr>
    <td align="center">

	</td>
  </tr>
</table>
<INPUT TYPE="hidden" id="optype" name="optype" value="" />
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

function setSubmitFlag(type,submit,direction){
  document.reportEditForm.optype.value = type;
  document.reportEditForm.opSubmit.value = submit;
  document.reportEditForm.opDirection.value = direction;

  if(type == 'new'){
    if(confirm("您确定要创建一个新图形吗？")){
         document.reportEditForm.submit();
    }
  }else if(submit == 'delete'){
    if(confirm("您确定要删除该图形吗？此操作不可恢复!")){
         document.reportEditForm.submit();
    }
  }else{
    if (reportEditForm.chart_id.value==null || trim(reportEditForm.chart_id.value)==''){
	  alert('图形ID不能为空！！！');
	  return;
	}
    if(submit == 'insert'||submit == 'update'||submit == 'chartfundelete'){
	  document.reportEditForm.submit();
    }
  }
}

function change_parent(objValue){
  jfreechart.style.display="none";
  fusionchart_multi.style.display="none";
  fusionchart_single.style.display="none";
  fusionchart_scatter.style.display="none";
  fusionchart_bubble.style.display="none";
  if(objValue=="1"){
    jfreechart.style.display="block";
  }else if(objValue=="2"){
    fusionchart_multi.style.display="block";
  }else if(objValue=="3"){
    fusionchart_single.style.display="block";
  }else if(objValue=="4"){
    fusionchart_scatter.style.display="block";
  }else if(objValue=="5"){
    fusionchart_bubble.style.display="block";
  }
}

function _preview(chart_id,addinfo){
  var optstr = "height=500,width=1000,left=50,top=50,status=no,toolbar=no,menubar=no,location=no,scrollbars=auto,resizable=yes"
  window.open("../subject/common/chart_preview.jsp?chart_id="+chart_id+addinfo,"prechart",optstr);
}

function _attribute(tab_name){
  var optstr = "height=500,width=1000,left=50,top=50,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no"
  window.open("../subject/common/chart_attribute_tab.jsp?tab_name="+tab_name,"attribute",optstr);
}

function JHshNumberText()
{
if ( !(((window.event.keyCode >= 48) && (window.event.keyCode <= 57))
|| (window.event.keyCode == 13) || (window.event.keyCode == 46)
|| (window.event.keyCode == 45)))
{
   window.event.keyCode = 0 ;
}

return ;
}
</script>