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
		<title>渠道评价</title>
		<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(qryStruct == null){
			qryStruct = new ReportQryStruct();
		}
		//String ChnlTypeSql = "SELECT  distinct a.channel_type_lvl3 as CHNL_ID, substr(a.channel_type_lvl3_name,instr(a.channel_type_lvl3_name,'-') + 1) as CHNL_NAME FROM DIM_CHANNEL_TYPE a where a.channel_type_lvl3 LIKE '1010%'";
		String ChnlTypeSql = "SELECT code_id,code_name FROM ui_code_list where type_code='CHANNEL_TYPE' and status='1' order by code_seq";
		%>
		
		
		
<script>
	
	$(document).ready(function() {
		initframe();		
	});
	
	function initframe(){	
		loadFrameByLevel()
	}
	
	function loadFrameByLevel() {
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		var chartIframe = document.getElementById("chart_self_chnl_C_01");
		var tableIframe = document.getElementById("table_self_chnl_T_01");
		if(ctl_lvl=="0") { //总部
			chartIframe.src="SubjectCommChart.screen?chart_id=self_chnl_C_01";
			tableIframe.src="SubjectCommTable.rptdo?table_id=self_chnl_T_01&first=Y&table_height=250";
		} else if(ctl_lvl=="1"){ //省份
			chartIframe.src="SubjectCommChart.screen?chart_id=self_chnl_C_01_prov";
			tableIframe.src="SubjectCommTable.rptdo?table_id=self_chnl_T_01_prov&first=Y&table_height=250";
		} else {
			alert("对不起，您无权查看此页面信息");
			return false;
		}
	}
  	
	function doQuery() {
		SelfChnl.submit();
	}
/*
**得到默认账期
*/
	function getOriDate(){
		var oriDate = document.getElementById("qry__rpt_date");
		return oriDate.value
 	} 
/*
**  重置按钮，时间函数 .
*/
	function doReset() {
		var oChlType = document.getElementById("qry__tactic_type");
		var oSerMonth = document.getElementById("qry__gather_month");

		oChlType.value="";
		oSerMonth.value=getOriDate();
	}
/*
**  链接跳转函数.
*/	
	function callOtherURL()
    {
    		var strUrl = "Weight.rptdo?ioid_id0=B0201";
        	var newsWin = window.open(strUrl);
        		if (newsWin != null) {
            	newsWin.focus();
        	}
    }
</script>
	</head>
		
<body>
	
<FORM name="SelfChnl" action="SelfChnlEvl.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden id="oriDate" name="qry__rpt_date"  value="<%=qryStruct.rpt_date%>">

<!-- 查询条件区,开始 -->
<div class="topsearch">
	 <table width="80%"  border=0 cellpadding="0" cellspacing="0" align="center">
                  <tr>
                  <td width="13%" align="right" class="td-bg" nowrap="nowrap">月份：</td>
                    <td width="15%" >
						<input id="qry__gather_month" class="Wdate" style="height:22px;" type="text" name="qry__gather_month" value="<%=qryStruct.gather_month%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>
                   </td>
<!-- 
                   	<td width="13%" align="right" class="td-bg" nowrap="nowrap">考核区域：</td>
                    <td >
                    	<select id="AreaSelect" name="qry__attach_region" disabled >
  							<option value="1">北方</option>
  							<option value="0">南方</option>
						</select>
                    </td>
 -->  
                    <td width="13%" align="right" class="td-bg" nowrap="nowrap">渠道类型：</td>
                    <td width="20%" >
                    	<BIBM:TagSelectList  focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry__tactic_type" listID="0" allFlag="" selfSQL="<%=ChnlTypeSql%>" />
                    </td>
                    <td width="10%" ><input id="button_submit" class="btn_search" type="button" value="查询" onClick="doQuery();"/></td>
                    <td ><input id="button_submit" class="btn3" type="button" value="重置" onClick="doReset();"/></td>
                  </tr>
    </table>
	</div>
	<!-- 查询条件区,结束 -->
	
	<div class="result_title">
		<span >自有营业厅评价地域总览</span>		
		<div class="iw">
		   <input  id="button_submit"  class="btn_iw" type="button" value="评分指标及权重" onClick="callOtherURL()"/>
		</div>
	</div>
	
	<!-- 图表展示区 ,开始-->
	<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
	<!-- 图形区 -->
	<tr  valign="top" id="add_type_1" >
	    <td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;margin-bottom: 15px;">
			<tr  valign="top">
		 	</tr> 
			<tr valign="top">
			<td align="left" valign="top" ><iframe id="chart_self_chnl_C_01" 
						scrolling="no" width="100%" height="220" border="0" frameborder="0"
						marginwidth="0" marginheight="0" src=""></iframe></td>				
			</tr>
			</tr>
		</table>	
		</td>
	</tr>
	<!-- 表格区 -->
	<tr>	
		<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="table_self_chnl_T_01" width="100%" height="250"
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
