<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.base.util.CObjKnd"%>
<%@ page import="com.ailk.bi.base.util.SQLGenator"%>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page import="com.ailk.bi.common.dbtools.WebDBUtil"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
String oper_no = CommonFacade.getLoginId(session);
String user_role = CommTool.getRoleStringByOper(oper_no);
ReportQryStruct lsbiQry = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
if(lsbiQry==null){
	lsbiQry = new ReportQryStruct();
	lsbiQry.rpt_status = "W";
}

String roleSql1 = "";
String roleSql1_1 = "";
String roleSql2 = "";
//单选列表值
String strTask = "W,待审核;Y,已审核";
if(user_role.indexOf(CObjKnd.PREOCESS_RETURN_ROLE) >= 0){
	strTask = "W,待审核;Y,已审核;R,审核回退";
}
if ("R".equals(lsbiQry.rpt_status)&&user_role.indexOf(CObjKnd.PREOCESS_RETURN_ROLE) >= 0){
	roleSql1 = " AND D.P_STEP_FLAG='E' AND D.RPT_DATE<>0";
	roleSql1_1 = " AND E.P_STEP_FLAG='E' AND E.RPT_DATE<>0";
	roleSql2 = " AND C.P_STEP_FLAG='E'";
}else{
	roleSql1 = " AND D.R_ROLE_ID IN ("+user_role+")";
	roleSql1_1 = " AND E.R_ROLE_ID IN ("+user_role+")";
	roleSql2 = " AND C.R_ROLE_ID IN ("+user_role+")";
}
String whereSql1 = "";
String whereSql1_1 = "";
String whereSql21 = "";
String whereSql22 = "";
if(lsbiQry.rpt_name!=null&&!"".equals(lsbiQry.rpt_name)){
	whereSql1 += " AND A.NAME LIKE '%"+lsbiQry.rpt_name+"%'";
	whereSql1_1 += " AND A.NAME LIKE '%"+lsbiQry.rpt_name+"%'";
	whereSql21 += " AND A.NAME LIKE '%"+lsbiQry.rpt_name+"%'";
}
if(lsbiQry.date_s!=null&&!"".equals(lsbiQry.date_s)){
	String m = lsbiQry.date_s;
	String d1 = m+"01";
	String d2 = m+"32";
	whereSql1 += " AND (B.RPT_DATE="+m+" OR (B.RPT_DATE>="+d1+" AND B.RPT_DATE<"+d2+"))";
	whereSql1_1 += " AND (C.DATE_ID="+m+" OR (C.DATE_ID>="+d1+" AND C.DATE_ID<"+d2+"))";
	whereSql22 += " AND (RPT_DATE="+m+" OR (RPT_DATE>="+d1+" AND RPT_DATE<"+d2+"))";
}

String strSql = SQLGenator.genSQL("Q3330");
//out.print(strSql);
String[][] result1 = WebDBUtil.execQryArray(strSql, "");

strSql = SQLGenator.genSQL("Q3332",roleSql1,whereSql1,roleSql1_1,whereSql1_1);
//out.print(strSql);
String[][] result2 = WebDBUtil.execQryArray(strSql, "");

strSql = SQLGenator.genSQL("Q3334",roleSql2,whereSql21,whereSql22);
//out.print(strSql);
String[][] result3 = WebDBUtil.execQryArray(strSql, "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Tag:Log logType="2" defaultValue="9900437"/>
<html>
<head>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/scwM.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<title>待办报表任务</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_REPORT_QRYSTRUCT%>" warn="1"/>
</head>
<body class="main-body" onLoad="selfDisp();">
<form NAME="frmEdit" ID="frmEdit" ACTION="rptProcess.rptdo?opType=self_task" method="post">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
              <Tag:Bar defaultValue="9900437"/>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1" background="../biimages/black-dot.gif"></td>
	</tr>
	<tr>
		<td height="2"></td>
	</tr>
	<!--条件区展示 start-->
  <tr>
    <td>
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="squareB" >
      <tr> 
        <td><img src="../biimages/square_corner_1.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_1.gif"></td>
        <td><img src="../biimages/square_corner_2.gif" width="5" height="5"></td>
      </tr>
      <tr> 
        <td background="../biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top">
        <table width="100%" border="0">
          <tr>
        	<td width="5%" nowrap>任务状态：</td>
			<td width="20%" nowrap><BIBM:TagRadio radioName="qry__rpt_status" radioID="#" focusID="<%=lsbiQry.rpt_status%>" selfSQL="<%=strTask%>"/></td>
			
            <td width="5%" nowrap>日期限定：</td>
			<td nowrap><input type="text" size="10" name="qry__date_s" readonly onClick="scwShowM(this,this);" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
            
            <td width="5%" nowrap>报表名称：</td>
			<td nowrap><input type="text" size="20" name="qry__rpt_name" class="input-text" onFocus="switchClass(this)" onBlur="switchClass(this)"></td>
            
            <td width="5%">
             <input type="button" name="search" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="查询" onclick="javascript:fnSubmit();"> 
            </td>
          </tr>
				  
        </table>
        </td>
        <td background="../biimages/square_line_3.gif"></td>
      </tr>
      <tr> 
        <td height="6"><img src="../biimages/square_corner_3.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_4.gif"></td>
        <td><img src="../biimages/square_corner_4.gif" width="5" height="5"></td>
      </tr>
    </table>
    </td>
  </tr>
  <!--条件区展示 end-->
  <tr>
    <td height="5"></td>
  </tr>
  <!--报表显示 start-->
  <tr>
  <td class="tab-side2">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
  <tr align="center">
    <td class="tab-title">报表名称</td>
    <td class="tab-title">报表日期</td>
    <td class="tab-title">操作</td>
  </tr>
  <%boolean isDisplay = false;
  int icount = 0;//记录显示条数
  int inum = 0;
  for(int i=0;result2!=null&&i<result2.length;i++){
	  isDisplay = false;
	  inum = 0;
	  String res_id = result2[i][0];
	  String rpt_date = result2[i][1];
	  String name = result2[i][2];
	  String url = result2[i][3]+"&p_date="+rpt_date;
	  String p_id = result2[i][4];
	  String step = result2[i][5];
	  if("W".equals(lsbiQry.rpt_status)){
		  for(int j=0;result3!=null&&j<result3.length;j++){
			  if(res_id.equals(result3[j][0])&&rpt_date.equals(result3[j][1])){
				  if(inum>=1){
					  break;
				  }
			  	  if("Y".equals(result3[j][4])&&!"E".equals(result3[j][5])&&step.equals(result3[j][6])){
				  	  isDisplay = true;
			  	  }
			  	  inum++;
			  }
		  }
		  if(("1".equals(step)||"E".equals(step))&&!isDisplay&&inum==0){
			  for(int k=0;result1!=null&&k<result1.length;k++){
				  if(inum>=1){
					  break;
				  }
				  if(p_id.equals(result1[k][1])){
					  if(step.equals(result1[k][2])){
					  	  isDisplay = true;
					  }
					  inum++;
				  }
			  }
		  }
	  }
	  if("Y".equals(lsbiQry.rpt_status)){
		  for(int j=0;result3!=null&&j<result3.length;j++){
			  if(res_id.equals(result3[j][0])&&rpt_date.equals(result3[j][1])){
				  if(inum>=1){
					  break;
				  }
			  	  if("Y".equals(result3[j][4])&&step.equals(result3[j][5])&&oper_no.equals(result3[j][7])){
				  	  isDisplay = true;
			  	  }
			  	  inum++;
			  }
		  }
	  }
	  if("R".equals(lsbiQry.rpt_status)){
		  for(int j=0;result3!=null&&j<result3.length;j++){
			  if(res_id.equals(result3[j][0])&&rpt_date.equals(result3[j][1])){
				  if(inum>=1){
					  break;
				  }
			  	  if("R".equals(result3[j][4])){
				  	  isDisplay = true;
			  	  }
			  	  inum++;
			  }
		  }
	  }
  %>
  <%if(isDisplay){ %>
  <tr class="table-white-bg">
    <td nowrap><%=name %></td>
    <td nowrap><%=rpt_date %></td>
    <td nowrap align="center"><a href="<%=url %>" class="bule">查看</a></td>
  </tr>
  <%icount++;} %>
  <%} %>
  <%if(icount==0){ %>
  <tr class="table-white-bg">
    <td colspan="3" nowrap align="center">该条件下没有符合要求的数据</td>
  </tr>
  <%} %>
</TABLE>
    </td>
  </tr>
  <!--报表显示 end-->
</table>
<INPUT TYPE="hidden" id="qry__visible_data" name="qry__visible_data" value="<%=lsbiQry.visible_data%>" />
</form>
</body>
<script language=javascript>
function fnSubmit(){
  document.frmEdit.submit();
}
</script>
</html>