<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.*"%>
<%@ include file="/base/commonHtml.jsp"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="Content-Language" content="zh-cn">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
		<title>地市明细</title>
		<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(qryStruct == null){
			qryStruct = new ReportQryStruct();
		}
		String loadProvSql = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t where t.geo_flag=\'"+qryStruct.attach_region + "\'";
		System.out.println(loadProvSql);
		%>
		
<script>
	
	function window.onload() {
		initframe();
		
	}
	
	function initframe(){	
		
		var tableIframe = document.getElementById("table_self_chnl_T_01");
		tableIframe.src="SubjectCommTable.rptdo?table_id=self_chnl_T_city&first=Y&table_height=250";
	}
  	
	function doQuery() {
		SelfChnl.submit();
	}

</script>
	</head>
<body>
	
<FORM name="SelfChnl" action="SelfChnlEvl.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden name="qry__gather_month" value="<%=qryStruct.gather_month%>">
<INPUT type=hidden name="qry__attach_region" value="<%=qryStruct.attach_region%>">
<INPUT type=hidden name="qry__tactic_type" value="<%=qryStruct.tactic_type%>">

<!-- 查询条件区,开始 -->
<div class="topsearch">
	 <table width="60%"  border=0 cellpadding="0" cellspacing="0">
                  <tr>                  	
                    <td width="10%" align="right" class="td-bg">省分：</td>
                    <td width="20%" >
                    	<BIBM:TagSelectList  focusID="<%=qryStruct.metro_id%>" script="class='easyui-combobox'"	listName="qry__metro_id" listID="0"  selfSQL="<%=loadProvSql%>" />
                    </td>
                    <td  ><input id="button_submit" class="btn_search" type="button" value="查询" onClick="doQuery();"/></td>
                  </tr>
    </table>
</div>
	<div class="result_title">
		<span >地市详细信息</span>		
	</div>
	<!-- 查询条件区,结束 -->
	
	<!-- 图表展示区 ,开始-->
	<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
	<!-- 表格区 -->
	<tr>	
		<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top">
				<td align="left" colspan="2" height="400px;"><iframe id="table_self_chnl_T_01" width="100%" height="100%"
							src=""
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</FORM>
<!-- 图表展示区 ,结束-->
    <script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
	    domHover(".btn_iw", "btn_iw_hover");
    </script>
	</body>
</html>
