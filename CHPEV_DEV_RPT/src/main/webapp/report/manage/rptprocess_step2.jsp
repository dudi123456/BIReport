<%@page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptProcessStepTable"%>
<%@page import="com.ailk.bi.report.domain.RptProcessTable"%>

<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程步骤信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<%
//审核流程信息
RptProcessTable pTable = (RptProcessTable) session.getAttribute(WebKeys.ATTR_REPORT_PROCESS);
//流程步骤
RptProcessStepTable[] pStepTable = (RptProcessStepTable[]) session.getAttribute(WebKeys.ATTR_REPORT_PROCESS_STEP);
int colNum = 0;
if (null != pStepTable) {
	colNum = pStepTable.length;
}
%>
</head>
<body>
<FORM METHOD="POST" name="form1" action="rptProcess.rptdo">
<table width="100%" height="100%" border="0" cellpadding="0"
  cellspacing="0">
  <tr>
    <td valign="top" class="squareB">
    <table width="100%" height="100%" border="0" cellpadding="0"
      cellspacing="0">
      <tr>
        <td width="5" height="7"
          background="../biimages/square_line_2.gif"></td>
        <td width="100%" colspan="2"></td>
        <td><img src="../biimages/tab/field_upline_right.gif"
          width="5" height="7"></td>
      </tr>
      <tr>
        <td height="7" background="../biimages/square_line_2.gif">&nbsp;</td>
        <td height="100%" colspan="2" valign="top">
        <table width="95%" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td class="tree" height="30">
            <%=pTable.p_flag_name%>步骤：
            </td>
          </tr>
          <tr>
            <td width="70%" valign="top" class="side-2">
            <!-- 审核流程步骤表格开始 -->
            <table id="tbReport" width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr id="tbHead" class="table-th">
                <td align="center" nowrap class="table-item" height="20">步骤</td>
                <td align="center" nowrap class="table-item">步骤名称</td>
                <td align="center" nowrap class="table-item">审核角色</td>
                <td align="center" nowrap class="table-item">数据可见</td>
                <td align="center" nowrap class="table-item2">是否选中</td>
              </tr>
              <%if(pStepTable==null||pStepTable.length==0){%>
              <tr id="nullmsg">
                <td colspan="5" class="table-td" height="18" align="center">
                  还没有定义任何审核步骤角色
                </td>
              </tr>
              <%}%>
              <%
              int m = 1;
              for (int i = 0; pStepTable != null && i < pStepTable.length; i++) {
            	  RptProcessStepTable rsInfo = (RptProcessStepTable) pStepTable[i];
       			String step_flag = "";
       			if(rsInfo.p_step_flag==null||"".equals(rsInfo.p_step_flag)){
				  step_flag = "----";
				}else{
				  step_flag = "第 "+m+" 步";
				  m++;
				}
              %>
              <tr id="tr_<%=i%>">
                <td class="table-td" align="center"><%=step_flag%></td>
                <td class="table-td"><%=rsInfo.p_step_flag_name%>
                <input type="hidden" name="flag_name_<%=i%>" value="<%=rsInfo.p_step_flag_name%>" />
                </td>
                <td class="table-td"><%=rsInfo.role_name%>
                <input type="hidden" name="role_id_<%=i%>" value="<%=rsInfo.role_code%>" size="40"/>
                <input type="hidden" name="role_name_<%=i%>" value="<%=rsInfo.role_name%>" size="40"/>
                </td>
                <td align="center" class="table-td">
                <input type="radio" name="visible_data" value="<%=i%>" <%if(ReportConsts.YES.equals(rsInfo.p_step_visible_data)){out.print("checked");}%>/>
                </td>
                <td align="center" class="table-td2">
                <input type="checkbox" name="col_id" value="<%=i%>" <%if(rsInfo.r_role_id!=null&&!"".equals(rsInfo.r_role_id)) out.println("checked=true ");%>>
                </td>
              </tr>
              <%}%>
            </table>
            <!-- 纬度定义表格结束 --></td>
          </tr>
          <tr>
            <td class="tree" height="30">
              说明：所有流程步骤的修改，对应报表从下个周期起开始使用新流程。
            </td>
          </tr>
        </table>
        </td>
        <td background="../biimages/square_line_3.gif">&nbsp;</td>
      </tr>
      <tr>
        <td height="5"><img src="../biimages/square_corner_3.gif"
          width="5" height="5"></td>
        <td colspan="2" background="../biimages/square_line_4.gif"></td>
        <td><img src="../biimages/square_corner_4.gif" width="5"
          height="5"></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td height="35" align="center" valign="bottom">
        <input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)" 
		  value="保存" onclick="setSubmitFlag('step2','save','current')">
		<input name="pre" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		  value="上一步" onclick="setSubmitFlag('step2','save','pre')">
    </td>
  </tr>
</table>
<input type="hidden" id="rowCount" name="rowCount" value="<%=colNum%>" />
<INPUT TYPE="hidden" id="opType" name="opType" value="" />
<INPUT TYPE="hidden" id="opSubmit" name="opSubmit" value="" />
<INPUT TYPE="hidden" id="opDirection" name="opDirection" value="" />
</FORM>
</body>
<script language="JavaScript">
function setSubmitFlag(type,submit,direction){
  document.form1.opType.value = type;
  document.form1.opSubmit.value = submit;
  document.form1.opDirection.value = direction;
  document.form1.action = "rptProcess.rptdo";
  document.form1.submit();
}
</script>
</html>
