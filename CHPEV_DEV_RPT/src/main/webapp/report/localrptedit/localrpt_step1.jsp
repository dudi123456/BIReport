<%@page contentType="text/html; charset=UTF-8"%>

<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<%@page import="com.ailk.bi.base.util.SQLGenator"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.common.dbtools.WebDBUtil"%>
<%@page import="com.ailk.bi.common.app.AppException"%>
<%@page import="com.ailk.bi.report.util.*"%>
<%@page import="java.sql.*"%>
<%@page import="com.ailk.bi.base.util.WebConstKeys" %>
<%@page import="com.ailk.bi.base.util.StringTool" %>
<%@page import="com.ailk.bi.base.struct.UserCtlRegionStruct" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表对象
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
if(rptTable==null){
	out.print("<center>");
	out.print("<br><br>操作信息丢失！<br>");
	out.print("</center>");
	return;
}
//可操作状态
String status_disabled = "";
if(ReportConsts.YES.equals(rptTable.status)){
	status_disabled = "disabled";
}
//是否翻页显示
int ipagecount = StringB.toInt(rptTable.pagecount,0);
String pagecount_type = ReportConsts.NO;
if(ipagecount>0){
	pagecount_type = ReportConsts.YES;
}
//报表类型
String kind_sql = SQLGenator.genSQL("Q3170");
String prikind_sql = SQLGenator.genSQL("Q3171");
//报表基本信息提交类型
String submit = "";
if("".equals(rptTable.rpt_id)){
	submit="insert";
}else{
	submit="update";
}

// 加入权限控制条件-用户控制区域
UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
		.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
if (ctlStruct == null) {
	ctlStruct = new UserCtlRegionStruct();
}

String city = null;

if (!StringTool.checkEmptyString(ctlStruct.ctl_city_str)) {// 地市
	city = ctlStruct.ctl_city_str;
	if (!StringTool.checkEmptyString(ctlStruct.ctl_county_str) && ctlStruct.ctl_county_str.length()==4) {// 区县
		city = ctlStruct.ctl_county_str;
	}
}else {
	city = "999";
}

System.out.println("city=" +city);
%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<script language="javascript">
  //页面初始化
  function pageOnLoad()
  {
    reportEditForm.parent_id.options[0] = new Option('--请选择--','');
    <%if(rptTable.parent_id==null||"".equals(rptTable.parent_id)){%>
      reportEditForm.parent_id.selectedIndex=0;
    <%}%>
    reportEditForm.parent_id2.options[0] = new Option('--请选择--','');
    <%if(rptTable.parent_id==null||"".equals(rptTable.parent_id)){%>
      reportEditForm.parent_id2.selectedIndex=0;
    <%}%>
    change_status('<%=pagecount_type%>');
  }
</script>
</head>

<body onLoad="change_parent('<%=rptTable.privateflag%>');pageOnLoad();">
<FORM name="reportEditForm" action="editLocalReport.rptdo" method="POST">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top" class="squareB"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="5" height="7" background="../biimages/square_line_2.gif"></td>
          <td width="100%" colspan="2"></td>
          <td><img src="../biimages/tab/field_upline_right.gif" width="5" height="7"></td>
        </tr>
        <tr> 
          <td height="7" background="../biimages/square_line_2.gif">&nbsp;</td>
          <td height="100%" colspan="2" valign="top"> <table width="95%" align="center" cellpadding="5">
              <tr> 
                <td width="14%">报表ID：</td>
                <td width="23%"><input type="text" name="rpt_id" maxlength="100" value="<%=rptTable.rpt_id%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)" <%if(rptTable.rpt_id!=null&&!"".equals(rptTable.rpt_id)){out.print("readonly");}%>></td>
                <td width="14%">本地应用编码：</td>
                <td><input type="text" name="local_res_code" maxlength="22" value="<%=rptTable.local_res_code%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr> 
                <td>报表名称：</td>
                <td colspan="3"><input type="text" name="name" maxlength="50" value="<%=rptTable.name%>" class="input-text" size="69" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr> 
                <td>统计周期：</td>
                <%if(!ReportConsts.YES.equals(rptTable.status)){ %>
                <td><BIBM:TagRadio radioName="cycle" radioID="S3001" focusID="<%=rptTable.cycle%>" script="<%=status_disabled%>"/></td>
                <%}else{ %>
                <td><%=rptTable.cycle_desc%><INPUT TYPE="hidden" id="cycle" name="cycle" value="<%=rptTable.cycle%>" /></td>
                <%} %>
                <td>资源归属：</td>
                <td id="pub"><BIBM:TagSelectList listID="0" listName="parent_id" allFlag="" focusID="<%=rptTable.parent_id%>" selfSQL="<%=kind_sql%>" /></td>
                <td id="pri"><BIBM:TagSelectList listID="0" listName="parent_id2" allFlag="" focusID="<%=rptTable.parent_id%>" selfSQL="<%=prikind_sql%>" /></td>
              </tr>
              <tr> 
                <td>区间日期：</td>
                <td><BIBM:TagRadio radioName="start_date" radioID="S3012" focusID="<%=rptTable.start_date%>"/></td>
                <td>私有报表：</td>
                <%if(!ReportConsts.YES.equals(rptTable.status)){ %>
                <td><BIBM:TagRadio radioName="privateflag" radioID="S3005" focusID="<%=rptTable.privateflag%>" script="onclick=change_parent(this.value)"/></td>
                <%}else{ %>
                <td><BIBM:TagRadio radioName="privateflag" radioID="S3005" focusID="<%=rptTable.privateflag%>" script="<%=status_disabled%>"/></td>
                <%} %>
              </tr>
              <tr> 
                <td>报表右上角说明：</td>
                <td colspan="3"><input type="text" name="title_note" maxlength="50" value="<%=rptTable.title_note%>" class="input-text" size="69" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr> 
                <td>是否显示合计行：</td>
                <td><BIBM:TagRadio radioName="rpt_type" radioID="S3011" focusID="<%=rptTable.rpt_type%>"/></td>
                <td>是否翻页显示：</td>
                <td><BIBM:TagRadio radioName="pagecount_type" radioID="S3005" focusID="<%=pagecount_type%>" script="onclick=change_status(this.value)"/>
                <input type="text" name="pagecount" value="<%=rptTable.pagecount%>" class="input-text" size="5" onFocus="switchClass(this)" onBlur="switchClass(this)" onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"></td>
              </tr>
              <tr> 
                <td>数据表名：</td>
                <td><input type="text" name="data_table" value="<%=rptTable.data_table%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <td>日期字段名：</td>
                <td><input type="text" name="data_date" value="<%=rptTable.data_date%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
               <tr> 
			   
                <td>排序字段名：</td>
                <td><input type="text" name="data_sequence" value="<%=rptTable.data_sequence%>" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
                <!--<td>显示设置：</td>
                <td><BIBM:TagSelectList listID="S3026" listName="divcity_flag" focusID="<%=rptTable.divcity_flag%>"/>--></td>
              </tr>
              <tr> 
                <td>提取数据的条件：</td>
                <td colspan="3"><input type="text" name="data_where" value="<%=rptTable.data_where%>" class="input-text" size="69" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr>
                <td nowrap>需求提出部门：</td>
                <td>
                  <input type="hidden" id="needdept_no" name="needdept_no" value="<%=rptTable.needdept%>" />
                  <input type="text" name="needdept" maxlength="32" class="input-text"
							value="<%=rptTable.needdept%>" onFocus="switchClass(this)"
							onBlur="switchClass(this)">
                </td>
                <td nowrap>需求提出人：</td>
                <td><input type="text" name="needperson" maxlength="32" class="input-text" value="<%=rptTable.needperson%>" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
              </tr>
              <tr>
                <td nowrap>引擎状态：</td>
                <td>
                  <select name="rp_status">
                  	<option value="Y" <%if (rptTable.rp_status.equals("Y")){%>selected<% }%>>有效</option>
                  	<option value="N" <%if (rptTable.rp_status.equals("N")){%>selected<% }%>>无效</option>
                  	
                  </select>                  
                  
                </td>
                <td nowrap>数据引擎：</td>
                
                <td>
                <select name="rp_engine">
                <%
				String sql = "select rp_engine,rp_engine_desc from UI_RPT_INFO_SQL_ENGINE order by rp_engine_desc ";
				System.out.println(sql);
				int n = 0;
				try {

				String[][] svces = WebDBUtil.execQryArray(sql, "");
				for(int i=0;svces != null && i<svces.length;i++){
				%>	
				<option value="<%=svces[i][0]%>" 
				<%if (svces[i][0]!=null)
					{
					if (rptTable.rp_engine.equals(svces[i][0])){%>
					selected
					<% }}%>><%=svces[i][1]%></option>
                <%
				}

				} catch (AppException e) {
					e.printStackTrace();
				}
				%>
                  </select>                  
				</td>
              </tr>
              <tr>
                <td valign="top" nowrap>需求背景：</td>
                <td colspan="3"><textarea name="needreason" cols="50" rows="3" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this);checkLength(this,500,'需求背景')"><%=rptTable.needreason%></textarea></td>
              </tr>
              <tr>
                <td width="14%" valign="top" >报表说明：</td>
                <td colspan="3" > <textarea name="inputnote" cols="50" rows="3" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this);checkLength(this,500,'报表说明')"><%=rptTable.inputnote%></textarea></td>
              </tr>
            </table></td>
            <input type="hidden" name="filldept" value="<%if (rptTable.filldept.equals("")){%><%=city%><%}else{%><%=rptTable.filldept%><% }%>">
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
      <input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)" 
		  value="保存" onclick="setSubmitFlag('step1','<%=submit%>','current')">
	  <%//if(ReportConsts.YES.equals(rptTable.privateflag)){ %>
	  <input name="sc" type="button" class="button"	onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		  value="删除" onclick="setSubmitFlag('step1','delete','current')" <%if("".equals(rptTable.rpt_id)){out.print("disabled=true");}%>>
	  <%//} %>
      <input name="next" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		  value="下一步" onclick="setSubmitFlag('step1','<%=submit%>','next')">
	</td>
  </tr>
</table>
<INPUT TYPE="hidden" id="opType" name="opType" value="" />
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
	  document.reportEditForm.opType.value = type;
	  document.reportEditForm.opSubmit.value = submit;
	  document.reportEditForm.opDirection.value = direction;

	  //add by wenna 校验长度
	  var lengthFlag=checkLength(document.all("needreason"),20,'需求背景');
	  if(lengthFlag){
	  	lengthFlag=checkLength(document.all("inputnote"),20,'报表说明');
	  }

	  if(!lengthFlag){
	  	return;
	  }
	  
	  if(submit == 'delete'){
	    if(confirm("您确定要删除该报表吗？此操作不可恢复!")){
          document.reportEditForm.submit();
        }
	  }else{
	    if (reportEditForm.rpt_id.value==null || trim(reportEditForm.rpt_id.value)==''){
		  alert('报表ID不能为空！！！');
		  return;
		}
	    if (reportEditForm.name.value==null || trim(reportEditForm.name.value)==''){
		  alert('报表名称不能为空！！！');
		  return;
		}
		var r=reportEditForm.elements['privateflag']
		var privateflag;
		for(i=0;i<r.length;i++)if(r[i].checked){privateflag=r[i].value;break}
		if (privateflag=='N'&&(reportEditForm.parent_id.value==null || reportEditForm.parent_id.value=='')){
		  alert('资源归属不能为空！！！');
		  return;
		}
		if (privateflag=='Y'&&(reportEditForm.parent_id2.value==null || reportEditForm.parent_id2.value=='')){
		  alert('资源归属不能为空！！！');
		  return;
		}
		if (reportEditForm.data_table.value==null || trim(reportEditForm.data_table.value)==''){
		  alert('数据表名不能为空！！！');
		  return;
		}

        if(submit == 'insert'||submit == 'update'){

 //       	document.all("privateflag")[0].disabled=false;
 //       	document.all("privateflag")[1].disabled=false;

		  document.reportEditForm.submit();
        }
	  }
	}
	function change_parent(objValue){
	  if(objValue=="N"){
	    pub.style.display="block";
	    pri.style.display="none";
	  }else{
	    pub.style.display="none";
	    pri.style.display="block";
	  }
	}
	function change_status(objValue){
	  if(objValue=="N"){
	  	document.all.pagecount.disabled = true;
	  }else{
	    document.all.pagecount.disabled = false;
	  }
	}
</script>