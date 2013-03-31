<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ include file="/base/commonHtml.jsp"%>
<%@ page import="com.ailk.bi.common.app.DateUtil"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Content-Language" content="zh-cn">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/Scroll.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/dd99.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js"></script>
	<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/wait.js"></SCRIPT>
	<title>评价指标分析</title>
	<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if (qryStruct == null) {
			qryStruct = new ReportQryStruct();
		}
		String sql = "select IOID_ID0,EVA_NAME from st_eva_all_para where supper_id='D06' order by IOID_ID0";
		String month = DateUtil.getDiffMonth(-1,DateUtil.getNowDate());
	%>
 <script>	
    $(document).ready(function() {
		initframe();		
	});
	
	function initframe() {			
		var tableIframe = document.getElementById("table_prov_T_01");
		tableIframe.src="SubjectCommTable.rptdo?table_id=<%=qryStruct.target_name%>&first=Y&table_height=400";
	}
	
	function doQuery() {	
		TableQryForm.submit();
	}
	
	function subgo(){
		var strUrl = "Weight.rptdo?ioid_id0=D06";
        var newsWin = window.open(strUrl);
        if (newsWin != null) {
           	newsWin.focus();
        }
	}
	
	function export_table_content(){
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		var province_code='<%=qryStruct.province_code%>';//用户所在省分
		var acct_month=document.getElementById("qry__gather_month").value;

		var city_code='<%=qryStruct.city_code%>';//用户所在地市
		var tableName="战略渠道评价指标下载";
		
		if(ctl_lvl=="1"){//省分
			tableName= "::province_code::"+tableName;
			tableName=encodeURI(encodeURI(tableName));
			window.open("ExportTools.rptdo?exportId=SOC_PRO_DETAIL&tableName="+tableName+"&province_code="+province_code+"&acct_month="+acct_month+"&sqlArgs="+province_code+","+acct_month,"数据导出","");
		}else if(ctl_lvl=="2"){//地市
			tableName= "::province_code::::city_code::"+tableName;
			tableName=encodeURI(encodeURI(tableName));
			window.open("ExportTools.rptdo?exportId=SOC_CITY_DETAIL&tableName="+tableName+"&province_code="+province_code+"&city_code="+city_code+"&acct_month="+acct_month+"&sqlArgs="+province_code+","+city_code+","+acct_month,"数据导出","");
		}else if(ctl_lvl=="0"){
			tableName= "全国"+tableName;
			tableName=encodeURI(encodeURI(tableName));
		    window.open("ExportTools.rptdo?exportId=SOC_COUN_DETAIL&tableName="+tableName+"&acct_month="+acct_month+"&sqlArgs="+acct_month,"数据导出","");
		}
	}
	
 function doreset(){
	 document.getElementById("qry__chain_level").value ="";
	 document.getElementById("qry__gather_month").value = <%=month%>;
	 document.getElementById("qry__dim1").value ="D0601";
  }
</script>
</head>
<body>
<form name="TableQryForm" action="SocChnl.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<div style="display: none; position: absolute;" id=altlayer></div>	
	<!-- 查询条件区,开始 -->
	<%if(qryStruct.optype.equals("county2")){%>	
	<div class="topsearch">
	<table width="70%" border=0 cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td align="right" class="td-bg">月份：</td>
			<td>
				<input class="Wdate" style="height: 22px;" type="text" id="qry__gather_month" name="qry__gather_month" value="<%=qryStruct.gather_month%>" size="15" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})" />			
			</td>
			
			<td align="right" class="td-bg">连锁级别: </td>
			<td>
			   <BIBM:SelectForCodeList htmlId="qry__chain_level" codeType="<%=WebConstKeys.SOC_CHANNEL_CHAIN_LEVEL %>" 
				value="<%=qryStruct.chain_level %>" htmlName="qry__chain_level" disabled="false" hasAll="true"></BIBM:SelectForCodeList>	
			</td>
			<td align="right" class="td-bg">评价指标: </td>
			<td>
			   <BIBM:TagSelectList focusID="<%=qryStruct.dim1%>" script="class='easyui-combobox'" listID="0" listName="qry__dim1" selfSQL="<%=sql%>" />
			</td>
			<td>
				<input id="button_submit" type="button" class="btn_search" value="查询"
					onclick="doQuery()" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" class="btn3" value="重置" onclick="doreset()" />
			</td>
		</tr>
	</table>
	</div>
	<!-- 查询条件区,结束 -->
	<div class="result_title">
		<%if(qryStruct.ctl_lvl.equals("0")){%>
		  <span >战略渠道评价指标分析</span>	
		<%}else if(qryStruct.ctl_lvl.equals("1")){%>	
		  <span >战略渠道评价指标分析</span>
		<%}else if(qryStruct.ctl_lvl.equals("2")){%>	
		  <span >战略渠道评价指标分析</span>
		<%}%>		
	    <div class="iw">
	       <input type="button" id="button_submit"  class="btn_excel"  value="下载指标明细" onclick="javascript:export_table_content()">
		   <input  id="button_submit"  class="btn_iw" type="button" value="评分指标及权重" onclick="subgo()"/>
		</div>
	</div>
	<% }else{ %>
	<div class="result_title"><span>战略渠道评价指标分析</span></div>		
	<% } %>
	<!-- 图表展示区 ,开始-->
	<table style="width: 100%;" id="content_1" cellspacing="0" cellpaddingx="0">
		<!-- 表格区 -->
		<tr>
			<td>
				<table cellspacing="0" cellpaddingx="0" style="width: 100%;">
					<tr valign="top">
						<td align="left" colspan="2"><iframe id="table_prov_T_01" width="100%" height="430" src=""
								frameborder="0" scrolling="no"></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<!-- 图表展示区 ,结束-->	
</form>
	<script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
        domHover(".btn_iw", "btn_iw_hover");
       domHover(".btn_excel", "btn_excel_hover");
    </script>
</body>
</html>
