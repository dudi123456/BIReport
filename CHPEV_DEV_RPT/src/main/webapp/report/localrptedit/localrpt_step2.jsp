<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptColDictTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表对象
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
if(rptTable==null){
	out.print("<center>");
	out.print("<br><br>报表信息丢失，请重新查询确定你需要查看的报表信息！<br>");
	out.print("</center>");
	return;
}
//报表列信息对象
RptColDictTable[] reportCols = (RptColDictTable[])session.getAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE);
int iCharLen = 0;// 描述列的总数
int iNumLen = 0;// 数值列的总数
for(int i=0;reportCols!=null&&i<reportCols.length;i++){
	if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){
		iCharLen++;
	}
	if(ReportConsts.DATA_TYPE_NUMBER.equals(reportCols[i].data_type)){
		iNumLen++;
	}
}
//模板标示
String moban = rptTable.ishead+","+rptTable.isleft;
String opSubmit=(String)session.getAttribute("opSubmit");
System.out.println("opSubmit:"+opSubmit);
if(opSubmit==null){
	opSubmit="";
}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/subject.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
</head>

<body onLoad="change_select('<%=rptTable.metaflag%>')">
<FORM name="reportEditForm" action="editLocalReport.rptdo" method="POST">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top" class="squareB"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr> 
          <td width="5" height="7" background="../images/common/tab/square_line_2.gif"></td>
          <td width="100%" colspan="2"></td>
          <td><img src="../images/common/tab/field_upline_right.gif" width="5" height="7"></td>
        </tr>
        <tr> 
          <td height="7" background="../images/common/tab/square_line_2.gif">&nbsp;</td>
          <td height="100%" colspan="2" valign="top">
            <table width="100%" align="center" cellpadding="5">
                <%if(!ReportConsts.YES.equals(rptTable.status)){ %>
              <tr> 
                <%}else{%>
              <tr style="display:none;"> 
                <%} %>
                <td width="12%">列定义来源：</td>
                <td width="38%"><BIBM:TagRadio radioName="rpt_metaflag" radioID="S3017" focusID="<%=rptTable.metaflag%>" script="onclick=change_select(this.value)"/></td>
                <td id=manual1 width="10%">描述列：</td>
                <td id=manual2 width="15%"><input type="text" name="charcount" value="<%=iCharLen%>" class="input-text" <%if(ReportConsts.YES.equals(rptTable.status)){out.print("readonly");}%> onFocus="switchClass(this)" onBlur="switchClass(this)" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"></td>
                <td id=manual3 width="10%">数值列：</td>
                <td id=manual4 width="15%"><input type="text" name="numcount" value="<%=iNumLen%>" class="input-text" <%if(ReportConsts.YES.equals(rptTable.status)){out.print("readonly");}%> onFocus="switchClass(this)" onBlur="switchClass(this)" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"></td>
                <td id=meta width="50%">****通过选取已经存在的指标定义来组成报表信息****</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="38"><img src="../images/common/tab/title_arrow_2.gif" width="38" height="21"></td>
                <td width="5%" nowrap class="title-bg">基本报表模版</td>
                <td width="44"><img src="../images/common/tab/title_bg.gif" width="44" height="21"></td>
                <td width="91%" class="title-bg-change">&nbsp;</td>
              </tr>
            </table>
            <table width="100%">
              <tr>
                <td align="center" valign="top"><table width="100"  border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="20" class="chart-font">
                    <input type='radio' id='moban' name='moban' value='N,N' <%if("N,N".equals(moban)){out.print("checked");}%>>
                       简单表头基本报表</td>
                  </tr>
                  <tr>
                    <td class="chart-image"></td>
                  </tr>
                  <tr>
                    <td><img src="../report/images/moban001.jpg"></td>
                  </tr>
                </table></td>
                <td align="center" valign="top"><table width="100"  border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="20" class="chart-font">
                    <input type='radio' id='moban' name='moban' value='Y,N' <%if("Y,N".equals(moban)){out.print("checked");}%>>
                       复杂表头基本报表</td>
                  </tr>
                  <tr>
                    <td class="chart-image"></td>
                  </tr>
                  <tr>
                    <td><img src="../report/images/moban002.jpg"></td>
                  </tr>
                </table></td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="38"><img src="../images/common/tab/title_arrow_2.gif" width="38" height="21"></td>
                <td width="5%" nowrap class="title-bg">左侧合并模版</td>
                <td width="44"><img src="../images/common/tab/title_bg.gif" width="44" height="21"></td>
                <td width="91%" class="title-bg-change">&nbsp;</td>
              </tr>
            </table>
            <table width="100%">
              <tr>
                <td align="center" valign="top"><table width="100"  border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="20" class="chart-font">
                      <input type='radio' id='moban' name='moban' value='N,Y' <%if("N,Y".equals(moban)){out.print("checked");}%>>
                         简单表头左侧合并报表</td>
                    </tr>
                    <tr>
                      <td class="chart-image"></td>
                    </tr>
                    <tr>
                      <td><img src="../report/images/moban003.jpg"></td>
                    </tr>
                </table></td>
                <td align="center" valign="top"><table width="100"  border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="20" class="chart-font">
                      <input type='radio' id='moban' name='moban' value='Y,Y' <%if("Y,Y".equals(moban)){out.print("checked");}%>>
                         复杂表头左侧合并报表</td>
                    </tr>
                    <tr>
                      <td class="chart-image"></td>
                    </tr>
                    <tr>
                      <td><img src="../report/images/moban004.jpg"></td>
                    </tr>
                </table></td>
              </tr>
            </table>
          </td>
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
        value="保存" onclick="setSubmitFlag('step2','save','current')">
      <input name="pre" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
        value="上一步" onclick="setSubmitFlag('step2','save','pre')">
      <input name="next" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
        value="下一步" onclick="setSubmitFlag('step2','save','next')">
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
function setSubmitFlag(type,submit,direction){
	document.reportEditForm.opType.value = type;
	document.reportEditForm.opSubmit.value = submit;
	document.reportEditForm.opDirection.value = direction;
	  
<%if(!ReportConsts.YES.equals(rptTable.status)){ %>
	var r=reportEditForm.elements['rpt_metaflag']
	var metaflag;
	for(i=0;i<r.length;i++)if(r[i].checked){metaflag=r[i].value;break}
	if(metaflag!='<%=rptTable.metaflag%>' && "<%=opSubmit%>"!="insert"){
	  if(!confirm("您确定要修改报表的列定义原来方式吗？此操作将删除原有的列定义！")){
		return;
	  }
	}
	if(metaflag=='N'){
	  if (reportEditForm.charcount.value==null || reportEditForm.charcount.value==''){
		alert('描述列不能为空！！！');
		return;
	  }
	  //if (reportEditForm.charcount.value>30 || reportEditForm.charcount.value<0){
	//	alert('描述列的取值在1-20以内！！！');
	//	return;
	//  }
	  if (reportEditForm.numcount.value==null || reportEditForm.numcount.value==''){
		alert('数值列不能为空！！！');
		return;
	  }
	  if (reportEditForm.numcount.value>80 || reportEditForm.numcount.value<=0){
		alert('数值列的取值在1-80以内！！！');
		return;
	  }
	}
<%}%>
    if(submit == 'save'){
		document.reportEditForm.submit();
    }
}
function change_select(objValue){
	if(objValue=="N"){
	  manual1.style.display="block";
	  manual2.style.display="block";
	  manual3.style.display="block";
	  manual4.style.display="block";
	  meta.style.display = "none";
	}else{
	  manual1.style.display="none";
	  manual2.style.display="none";
	  manual3.style.display="none";
	  manual4.style.display="none";
	  meta.style.display = "block";
	}
}
</script>