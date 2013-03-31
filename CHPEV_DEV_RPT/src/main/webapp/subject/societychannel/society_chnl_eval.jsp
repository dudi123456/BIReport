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
		<title>社会渠道门店评价地域总览</title>
		<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(qryStruct == null){
			qryStruct = new ReportQryStruct();
		}
		String ChnlTypeSql = "SELECT  distinct a.channel_type_lvl3 as CHNL_ID, substr(a.channel_type_lvl3_name,instr(a.channel_type_lvl3_name,'-') + 1) as CHNL_NAME FROM DIM_CHANNEL_TYPE a where a.channel_type_lvl3 LIKE '2010%'";
		%>
		
		
		
<script>
	
	$(document).ready(function() {
		initframe();		
	});
	
	function initframe(){	
		
		var chartIframe = document.getElementById("chart_society_chnl_C_01");
		var tableIframe = document.getElementById("table_society_chnl_T_01");
		//总部
		if('<%=qryStruct.ctl_lvl%>'=='0')
		{
			chartId = "";
			tableId = "";
			chartIframe.src="SubjectCommChart.screen?chart_id=society_chnl_C_01";
			tableIframe.src="SubjectCommTable.rptdo?table_id=society_chnl_T_01&first=Y&table_height=250";
		//省份
		}else if('<%=qryStruct.ctl_lvl%>'=='1')
		{
			chartIframe.src="SubjectCommChart.screen?chart_id=society_chnl_C_PROV";
			tableIframe.src="SubjectCommTable.rptdo?table_id=society_chnl_T_PROV&first=Y&table_height=250";
		}
	}
  	
	function doQuery() {
		SocietyChnl.submit();
	}
/*
**获取上一月日期函数YYYYMMDD
*/
	function getNowFormatDate(){
    	var day = new Date();
    	var Year = 0;
    	var Month = 0;
    	var Day = 0;
    	var CurrentDate = "";
    	Year= day.getFullYear();//支持IE和火狐浏览器.
    	Month= day.getMonth()+1;
    	CurrentDate += Year;
    	//上一月
    	if (Month > 10 ){
     		CurrentDate += (Month-1);
    	}
    	//若果为1月减一年+"12"月,如201212
    	else if(Month==1){
     		return Year-1+"12";
     	//
    	}else
    	{
    		CurrentDate += "0" + (Month-1);
    	}
    	return CurrentDate;
 	} 
/*
**  重置按钮，时间函数 .
*/
	function doReset() {
		var oChlType = document.getElementById("qry__tactic_type");
		var oSerMonth = document.getElementById("qry__gather_month");
		oChlType.value="";
		oSerMonth.value=getNowFormatDate();
	}
	
/*
**  链接跳转函数.
*/	
	function callOtherURL(url_type,optype)
    {
		if(url_type=="static"){
    		var strUrl = "./societychannel/"+optype+".jsp";
        	var newsWin = window.open(strUrl);
        		if (newsWin != null) {
            	newsWin.focus();
        	}
    	}
    }
</script>
	</head>
		
<body>
	
<FORM name="SocietyChnl" action="SocietyChnlEvl.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">

<!-- 查询条件区,开始 -->
<div class="topsearch">
	 <table width="80%"  border=0 cellpadding="0" cellspacing="0" align="center">
                  <tr>
                  <td width="13%" align="right" class="td-bg" nowrap="nowrap">月份：</td>
                    <td width="15%" >
						<input id="qry__gather_month" class="Wdate" style="height:22px;" type="text" name="qry__gather_month" value="<%=qryStruct.gather_month%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>
                   </td>
                    
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
		<span >社会渠道门店地域总览</span>		
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
			<td align="left" valign="top" ><iframe id="chart_society_chnl_C_01" 
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
				<td align="left" colspan="2"><iframe id="table_society_chnl_T_01" width="100%" height="250"
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
