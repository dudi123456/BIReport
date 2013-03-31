<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptColDictTable"%>

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
//列计数
int irowcount = 1;
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/jquery.min.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/align_tab_by_head.js"></SCRIPT>
</head>

<body onLoad="createPopup();alignTable();" onResize="alignTable();">
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
          <td height="100%" colspan="2" valign="top"> <table width="100%" align="center">
              <tr>
                <td valign="top">

                <span id="tableContent">
                <table width='100%' border='0' height="400" cellpadding='0' cellspacing='0' id="iTable_TableContainer">

                <tr>
				<td align=center valign='top'>
				<table id="iTable_LeftHeadTable1" width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
                  <tr class="table-th">
                    <td nowrap align="center" class="table-item">名称</td>
                  </tr>
                </table>
                </td>

                <td width="100%" align="left" valign="top">
                <div id="Layer1" style="position:absolute; width:100%; z-index:1; overflow: hidden;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="iTable" id="iTable_HeadTable1">
				  <tr class="table-th">
				    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" align="center" nowrap <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("class=table-item-rptedit-red");}else{out.print("class=table-item-rptedit-green");}%>>
                      <input type="text" name="field_title_<%=i%>" value="<%=reportCols[i].field_title%>" class="normalField2" />
                      <input type="hidden" name="col_id" value="<%=i%>" />
                      <input type="hidden" name="data_type_<%=i%>" value="<%=reportCols[i].data_type%>" />
                      <input type="hidden" name="msu_unit_<%=i%>" value="<%=reportCols[i].msu_unit%>" />
                      <input type="hidden" name="sms_flag_<%=i%>" value="<%=reportCols[i].sms_flag%>" />
                      <input type="hidden" name="title_style_<%=i%>" value="<%=reportCols[i].title_style%>" />
                      <input type="hidden" name="col_style_<%=i%>" value="<%=reportCols[i].col_style%>" />
                      <input type="hidden" name="print_title_style_<%=i%>" value="<%=reportCols[i].print_title_style%>" />
                      <input type="hidden" name="print_col_style_<%=i%>" value="<%=reportCols[i].print_col_style%>" />
                      <input type="hidden" name="is_msu_<%=i%>" value="<%=reportCols[i].is_msu%>" />
                      <input type="hidden" name="is_user_msu_<%=i%>" value="<%=reportCols[i].is_user_msu%>" />
                      <input type="hidden" name="msu_id_<%=i%>" value="<%=reportCols[i].msu_id%>" />
                      <input type="hidden" name="datatable_<%=i%>" value="<%=reportCols[i].datatable%>" />
                    </td>
                    <%} %>
                  </tr>
                </table>
                </div>
                </td>
                </tr>

                <tr valign="top">
				<td height="100%">
				<div id="LayerLeft1"style="position:absolute; width:100%; z-index:1; overflow: hidden; height: 100%;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="iTable" id="iTable_LeftTable1">
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">1.是否显示</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">2.排列顺序</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">3.纬度编码代码</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">4.描述字段代码</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">5.是否显示维度编码</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">6.是否展开列</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">7.是否进行小计</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">8.合计值是否有效</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">9.指标精度</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">10.是否千分位分隔</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">11.零值是否显示为空</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">12.数据转化为%</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">13.是否显示占比</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">14.是否显示同比</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">15.是否显示环比</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">16.是否链接</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">17.链接URL</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">18.链接是否弹出</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">19.是否排序</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">20.文字是否换行</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">21.是否预警</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">22.预警比较对象</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">23.预警是否用比率比较</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">24.预警阈值上限</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">25.预警上限颜色</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">26.预警阈值下限</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">27.预警下限颜色</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">28.预警方式</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">29.是否有效</td></tr>
				  <tr><td>&nbsp;</td></tr>
				</table>
				</div>
				</td>

				<td align="left">
				<div id="LayerRight1" style="position:absolute; width:100%; z-index:1; overflow: auto; height: 100%;" onscroll="syncScroll()">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="iTable" id="iTable_ContentTable1">
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="default_display_<%=i%>" value="<%=rptTable.rpt_id+"_"+i+"_"+i%>" <%if(ReportConsts.YES.equals(reportCols[i].default_display)){out.print("checked");}%>></td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <a href="javascript:moveLeft('<%=rptTable.rpt_id+"_"+i%>')"><img src="../images/common/tab/move_left.gif" width="11" height="11" hspace="1" border="0"></a>
                      <a href="javascript:moveRight('<%=rptTable.rpt_id+"_"+i%>')"><img src="../images/common/tab/move_right_on.gif" width="11" height="11" hspace="1" border="0"></a>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="field_dim_code_<%=i%>" maxlength="200" value="<%=reportCols[i].field_dim_code%>" class="normalField2" <%if(ReportConsts.DATA_TYPE_NUMBER.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>/>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="field_code_<%=i%>" maxlength="200" value="<%=reportCols[i].field_code%>" class="normalField2"/>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="dim_code_display_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].dim_code_display)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_NUMBER.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="is_expand_col_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].is_expand_col)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_NUMBER.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="is_subsum_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].is_subsum)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_NUMBER.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="valuable_sum_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].valuable_sum)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="msu_length_<%=i%>" value="<%=reportCols[i].msu_length%>" class="normalField2" <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%> onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="comma_splitted_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].comma_splitted)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="zero_proc_<%=i%>" <%if(ReportConsts.ZERO_TO_BLANK.equals(reportCols[i].zero_proc)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ String tmpName="ratio_displayed_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                     <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){%>
                      <BIBM:TagSelectList listID="S3016" listName="<%=tmpName%>" focusID="<%=reportCols[i].ratio_displayed%>" script="style='display:none'" />
                     <%}else{ %>
                      <BIBM:TagSelectList listID="S3016" listName="<%=tmpName%>" focusID="<%=reportCols[i].ratio_displayed%>" />
                     <%} %>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="has_comratio_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].has_comratio)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="has_same_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].has_same)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="has_last_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].has_last)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="has_link_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].has_link)){out.print("checked");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="link_url_<%=i%>" maxlength="120" value="<%=reportCols[i].link_url%>" class="normalField2" />
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="link_target_<%=i%>" <%if(ReportConsts.TARGET_BLANK.equals(reportCols[i].link_target.toLowerCase())){out.print("checked");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="data_order_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].data_order)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="td_wrap_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].td_wrap)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_NUMBER.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="need_alert_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].need_alert)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ String tmpName="compare_to_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                     <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){%>
                      <BIBM:TagSelectList listID="S3013" listName="<%=tmpName%>" focusID="<%=reportCols[i].compare_to%>" script="style='display:none'" />
                     <%}else{ %>
                      <BIBM:TagSelectList listID="S3013" listName="<%=tmpName%>" focusID="<%=reportCols[i].compare_to%>" />
                     <%} %>
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="ratio_compare_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].ratio_compare)){out.print("checked");}%> <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="high_value_<%=i%>" value="<%=reportCols[i].high_value%>" class="normalField2" <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%> />
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ String tmpName="rise_color_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                     <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){%>
                      <BIBM:TagSelectList listID="S3014" listName="<%=tmpName%>" focusID="<%=reportCols[i].rise_color%>" script="style='display:none'" />
                     <%}else{ %>
                      <BIBM:TagSelectList listID="S3014" listName="<%=tmpName%>" focusID="<%=reportCols[i].rise_color%>" />
                     <%} %>
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="low_value_<%=i%>" value="<%=reportCols[i].low_value%>" class="normalField2" <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){out.print("style='display:none'");}%> />
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ String tmpName="down_color_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                     <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){%>
                      <BIBM:TagSelectList listID="S3014" listName="<%=tmpName%>" focusID="<%=reportCols[i].down_color%>" script="style='display:none'" />
                     <%}else{ %>
                      <BIBM:TagSelectList listID="S3014" listName="<%=tmpName%>" focusID="<%=reportCols[i].down_color%>" />
                     <%} %>
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ String tmpName="alert_mode_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                     <%if(ReportConsts.DATA_TYPE_STRING.equals(reportCols[i].data_type)){%>
                      <BIBM:TagSelectList listID="S3015" listName="<%=tmpName%>" focusID="<%=reportCols[i].alert_mode%>" script="style='display:none'" />
                     <%}else{ %>
                      <BIBM:TagSelectList listID="S3015" listName="<%=tmpName%>" focusID="<%=reportCols[i].alert_mode%>" />
                     <%} %>
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;reportCols!=null&&i<reportCols.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="status_<%=i%>" <%if(ReportConsts.YES.equals(reportCols[i].status)){out.print("checked");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                </table>
                </div>
                </td>
                </tr>
                </table>
                </span>

              </td>
              </tr>
          </table>                      </td>
          <td background="../images/common/tab/square_line_3.gif">&nbsp;</td>
        </tr>
        <tr>
          <td height="5"><img src="../images/common/tab/square_corner_3.gif" width="5" height="5"></td>
          <td colspan="2" background="../images/common/tab/square_line_4.gif"></td>
          <td><img src="../images/common/tab/square_corner_4.gif" width="5" height="5"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td height="35" align="center" valign="bottom">
     			 <input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
					  value="保存" onclick="setSubmitFlag('step3','save','current')">
				 <input name="pre" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					  value="上一步" onclick="setSubmitFlag('step3','save','pre')">
    			 <input name="next" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					  value="下一步" onclick="setSubmitFlag('step3','save','next')">
    </td>
  </tr>
</table>
<INPUT TYPE="hidden" id="opType" name="opType" value="" />
<INPUT TYPE="hidden" id="opSubmit" name="opSubmit" value="" />
<INPUT TYPE="hidden" id="opDirection" name="opDirection" value="" />
</FORM>
</body>
</html>
<SCRIPT LANGUAGE=JavaScript DEFER>

	function setSubmitFlag(type,submit,direction){
	  document.reportEditForm.opType.value = type;
	  document.reportEditForm.opSubmit.value = submit;
	  document.reportEditForm.opDirection.value = direction;
      if(submit == 'save'){
	    document.reportEditForm.submit();
      }
	}

      function moveLeft(tdObjId){
        //首先找到
        if(tdObjId){
          //第一行的单元格移动
          var nameTd=document.getElementById("1_"+tdObjId);
          moveTableCellLeft(nameTd);
          //第二行的单元格移动
          var checkTd=document.getElementById("2_"+tdObjId);
          var child=checkTd.lastChild;
          var checked=false;
          if(child && child.checked)
            checked=true;
          var success=moveTableCellLeft(checkTd);
          //此持需要将选择框的值的最后的数字减一
          if(success){
            //移动成功，减一
            inputValueUpdate(tdObjId,false);
            //还有看看是不是选中了,然后还得恢复
            child.checked=checked;
          }

          for(i=3;i<=5;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=6;i<=9;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }
          for(i=10;i<=10;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=11;i<=12;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }
          for(i=13;i<=13;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=14;i<=17;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }
          for(i=18;i<=18;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=19;i<=22;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }
          for(i=23;i<=23;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=24;i<=24;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }
          for(i=25;i<=29;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=30;i<=30;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }

        }
      }
      function moveRight(tdObjId){
        if(tdObjId){
          //第一行的单元格移动
          var nameTd=document.getElementById("1_"+tdObjId);
          moveTableCellRight(nameTd);
          //第二行的单元格移动
          var checkTd=document.getElementById("2_"+tdObjId);
          var child=checkTd.lastChild;
          var checked=false;
          if(child && child.checked)
            checked=true;
          var success=moveTableCellRight(checkTd);
          //此持需要将选择框的值的最后的数字减一
          if(success){
            inputValueUpdate(tdObjId,true);
            child.checked=checked;
          }

          for(i=3;i<=5;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=6;i<=9;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }
          for(i=10;i<=10;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=11;i<=12;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }
          for(i=13;i<=13;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=14;i<=17;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }
          for(i=18;i<=18;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=19;i<=22;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }
          for(i=23;i<=23;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=24;i<=24;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }
      	  for(i=25;i<=29;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=30;i<=30;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }

      }
    }

    //将表格的单元格向左移动
    function moveTableCellLeft(tdObj){
      var success=false;
      if(tdObj){
          var previousNode=tdObj.previousSibling;
          if(previousNode){
            //有是看
            var preId=previousNode.id;
            if(preId){
              //如果有，说明它的左边还有,则可以左移
              var parent=previousNode.parentNode;
              if(parent){
                parent.removeChild(tdObj);
                parent.insertBefore(tdObj,previousNode);
                success=true;
              }
            }
          }
        }
        return success;
    }
     //将表格的单元格向右移动
    function moveTableCellRight(tdObj){
      var success=false;
      if(tdObj){
          var afterNode=tdObj.nextSibling;
          //看看还有没有右边的节点
          if(afterNode){
            //有
            var parent=afterNode.parentNode;
            if(parent){
               parent.removeChild(tdObj);
               insertAfter(parent,tdObj,afterNode);
               //前一个内容减一
               success=true;
            }
          }
       }
       return success;
    }

    //将DOM节点插入某个节点之后
    function insertAfter(parent, node, referenceNode) {
      if(referenceNode.nextSibling) {
          parent.insertBefore(node, referenceNode.nextSibling);
      } else {
          parent.appendChild(node);
      }
    }

    //将单元格的值的最后部分减一或加一
    function inputValueUpdate(tdObjId,add){
      if(tdObjId){
        //查找第二行的单元格对象
        var tdObj=document.getElementById("2_"+tdObjId);
        var preNode=tdObj.previousSibling;
        var nextNode=tdObj.nextSibling;
        if(tdObj){
          var child=tdObj.lastChild;
          if(child){
            var value=child.value;
            value=value.replace(tdObjId+"_","");
            value=eval(value);
            if(add){
              value++;
            }else{
              value--;
            }
            child.value=tdObjId+"_"+value;
          }
          //
          if(add){
            //增加了，找前面的
            if(preNode){
              child=preNode.lastChild;
              var nodeId=preNode.id;
              nodeId=nodeId.substring(nodeId.indexOf("_")+1);
              var value=child.value;
              value=value.replace(nodeId+"_","");
              value=eval(value);
              value--;
              child.value=nodeId+"_"+value;
            }
          }else{
            if(nextNode){
              child=nextNode.lastChild;
              var nodeId=nextNode.id;
              nodeId=nodeId.substring(nodeId.indexOf("_")+1);
              var value=child.value;
              value=value.replace(nodeId+"_","");
              value=eval(value);
              value++;
              child.value=nodeId+"_"+value;
            }
          }
        }
      }
    }

	function getScrollBarWidth (){
		  document.body.style.overflow = 'hidden';
		  var width = document.body.clientWidth;
		  document.body.style.overflow = 'scroll';
		  width -= document.body.clientWidth;
		  if(!width) width = document.body.offsetWidth - document.body.clientWidth;
		  document.body.style.overflow = '';
		  return width;
	}
  function getScrollerWidth() {
      var scr = null;
      var inn = null;
      var wNoScroll = 0;
      var wScroll = 0;

      // Outer scrolling div
      scr = document.createElement('div');
      scr.style.position = 'absolute';
      scr.style.top = '-1000px';
      scr.style.left = '-1000px';
      scr.style.width = '100px';
      scr.style.height = '50px';
      // Start with no scrollbar
      scr.style.overflow = 'hidden';

      // Inner content div
      inn = document.createElement('div');
      inn.style.width = '100%';
      inn.style.height = '200px';

      // Put the inner div in the scrolling div
      scr.appendChild(inn);
      // Append the scrolling div to the doc

      document.body.appendChild(scr);

      // Width of the inner div sans scrollbar
      wNoScroll = inn.offsetWidth;
      // Add the scrollbar
      scr.style.overflow = 'auto';
      // Width of the inner div width scrollbar
      wScroll = inn.offsetWidth;

      // Remove the scrolling div from the doc
      document.body.removeChild(
      document.body.lastChild);

      // Pixel width of the scroller
      return (wNoScroll - wScroll);
  }


	var scrollBarWidth;
	if(!scrollBarWidth){
		scrollBarWidth = getScrollBarWidth();
		if(scrollBarWidth ==0){
			scrollBarWidth = getScrollerWidth();
		}
		if(scrollBarWidth ==0){
			scrollBarWidth = 17;
		}
	}
</SCRIPT>