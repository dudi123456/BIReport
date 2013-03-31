<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.upload.dao.impl.*"%>
<%@ page import="com.ailk.bi.upload.domain.*"%>
<%@ page import="com.ailk.bi.upload.facade.*"%>
<%@ page import="com.ailk.bi.upload.util.*"%>
<%@ page import="com.ailk.bi.upload.common.*"%>
<%@ page import="com.ailk.bi.common.app.DateUtil"%>
<%@ page import="com.ailk.bi.common.chart.CreateChartObj"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>

<%
			UploadFacade facade = new UploadFacade( new UploadDao());
			//报表资源
			String report_id = request.getParameter(UploadConstant.REPORT_ID);
			if (report_id == null && "".equals(report_id)){
					return;
			}

			//得到报表相关信息
			UiMsuUpReportConfigTable reportInfo = facade.getReportInfo(report_id);
			if(reportInfo == null){
				reportInfo = new UiMsuUpReportConfigTable();
			}

      		//控制业务类型是否显示全部
			String svcall = request.getParameter("svcall");
			if (svcall == null){
				svcall = "0";
			}

			//记录类型
			String record_code = request.getParameter(UploadConstant.REPORT_RECORD_CODE);
			if (record_code == null || record_code.equals("")){
				record_code = facade.getFirstRecord(report_id);
			}

			//结束日期
			String end_date = request.getParameter(UploadConstant.REPORT_QRY_END_DATE);

			if (end_date == null){
				if("D".equals(reportInfo.getReport_type())){//日报
					end_date = facade.getMaxDayByReportIdAndRecord(report_id,record_code);
				}else if("M".equals(reportInfo.getReport_type())){//月报
					end_date = facade.getMaxMonthByReportIdAndRecord(report_id,record_code);
				}else if("W".equals(reportInfo.getReport_type())){//周报
					end_date = facade.getMaxDayByReportIdAndRecord(report_id,record_code);
				}else{//默认日报
					end_date = facade.getMaxDayByReportIdAndRecord(report_id,record_code);
				}

				if(null==end_date || "".equals(end_date)){
					if("D".equals(reportInfo.getReport_type())){//日报
						end_date = DateUtil.getDiffDay(-1,new Date());
					}else if("M".equals(reportInfo.getReport_type())){//月报
						end_date = DateUtil.getDiffMonth(-1,new Date());
					}else if("W".equals(reportInfo.getReport_type())){//周报
						end_date = DateUtil.getDiffDay(-1,new Date());
					}else{//默认日报
						end_date = DateUtil.getDiffDay(-1,new Date());
					}

				}
			}

			//
			String tmpSpanTimes ="";
			if("D".equals(reportInfo.getReport_type())){//日报
				tmpSpanTimes = DateUtil.getDaySpansStr(-6, end_date, "yyyyMMdd", true);
			}else if("M".equals(reportInfo.getReport_type())){//月报
				tmpSpanTimes = DateUtil.getMonthSpansStr(-6, end_date, "yyyyMM", true);
			}else if("W".equals(reportInfo.getReport_type())){//周报
				tmpSpanTimes = DateUtil.getWeekSpansStr(-5, end_date, "yyyyMMdd", true);
			}else{//默认日报
				tmpSpanTimes = DateUtil.getDaySpansStr(-5, end_date, "yyyyMMdd", true);
			}
			//
			String[] strDates = tmpSpanTimes.split(",");

			//开始日期
			String begin_date = request.getParameter(UploadConstant.REPORT_QRY_BEGIN_DATE);
			if (begin_date == null){
				begin_date = strDates[0];
			}


			//业务类型标记
			String svcFlag = facade.getRecordSvcFlag(report_id,record_code);
			if(svcFlag == null || "".equals(svcFlag)){
				svcFlag = "1";
			}
			//提取元数据数组
			UiMsuUpReportMetaInfoTable[] metaInfo =  facade.getReportMetaInfo(report_id,record_code);
			if(metaInfo == null){
				metaInfo = new UiMsuUpReportMetaInfoTable[0];
			}
			//纬度和指标
			 ArrayList dimList = new ArrayList();
			 ArrayList msuList = new ArrayList();
			for(int i=0;metaInfo!=null&&i<metaInfo.length;i++){
				if("2".equals(metaInfo[i].getFiled_type())){
					msuList.add(metaInfo[i]);
				}else if("1".equals(metaInfo[i].getFiled_type())){
					dimList.add(metaInfo[i]);
				}
			}

			//纬度
			UiMsuUpReportMetaInfoTable[] dimMeta =  (UiMsuUpReportMetaInfoTable[])dimList.toArray( new UiMsuUpReportMetaInfoTable[dimList.size()] );
			//指标
			UiMsuUpReportMetaInfoTable[] msuMeta =  (UiMsuUpReportMetaInfoTable[])msuList.toArray( new UiMsuUpReportMetaInfoTable[msuList.size()] );


			//取得条件下拉串（ 值,描述;值,描述）
			String[] BIBMValue = UploadHelper.getBIBMListValue(dimMeta);

			//条件名称和值定义变量
			String[] latName = null;
			String[] param = null;
			if (dimMeta != null) {
				latName = new String[dimMeta.length];
				param = new String[dimMeta.length];
				for (int i = 0; i < dimMeta.length; i++) {
					latName[i] = "qry__"+dimMeta[i].getCol_code();
				}
			}

			if (latName != null) {
				for (int i = 0; i < latName.length; i++) {
					//取得对应的值lat1;lat2;lat3
					param[i] = request.getParameter(latName[i]);
					if (param[i] == null) {//空则空
						param[i] = "";
					}
				}
			}


			//根据报表类型、当前报表条件字典、当前展示指标、求出当前值
			String[][] list = UploadHelper.getSumDataList(reportInfo.getReport_type(),dimMeta,msuMeta, param, report_id, record_code, begin_date,end_date);
			String[][] listASC = list;

//记录类型
String recordSql=" select record_code,record_desc from UI_MSU_INFO_RPTRECORD where report_id='"+report_id+"' order by record_code ";
%>

<!DOCTYPE html>
<HTML>
<head>
<title>经营分析系统</title>
<%@ include file="/base/commonHtml.jsp"%>
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
<script language="javascript">
function _saveSubmit()
{
	//进行开始时间和截止时间的大小判断
	var beginDate = document.getElementById("qry__begin_date").value;
	var endDate = document.getElementById("qry__end_date").value;

	//string从0开始
	var date1 = new Date(beginDate.substr(0,4),beginDate.substr(4,2),beginDate.substr(6,2));
	var date2 = new Date(endDate.substr(0,4),endDate.substr(4,2),endDate.substr(6,2));
	if(date1.getTime() > date2.getTime())
		{
			alert("结束时间小于开始时间，请重新选择！");
			return ;
		}
	document.frmEdit.submit();
}

//变更图形指标序列
function chartSeries(obj_value){
    document.frmEdit.chartS.value = obj_value;
  	document.frmEdit.submit();
}

//导出excel
function openExcel(len){

    var dim="";
 	var dimSelected="";
    var allDim="";
    <%
    for (int i = 0; i < dimMeta.length; i++) {
    %>
    	dim="document.frmEdit.qry__<%=dimMeta[i].getCol_code()%>";
		dimSelected=dim+".selectedIndex";
        var dimText=dim+"["+dimSelected+"].text";
        var dimValue="document.frmEdit.qry__<%=dimMeta[i].getCol_code()%>.value";
        allDim+=eval(dimText)+":"+eval(dimValue)+",";

	<%
	}
	%>

	var url ="uploadmsu_info_excel.jsp?report_id="+document.frmEdit.report_id.value;
      	url +="&record_code="+document.frmEdit.record_code.value;
        url +="&qry__end_date="+document.frmEdit.qry__end_date.value;
		url +="&qry__begin_date="+document.frmEdit.qry__begin_date.value;
		url +="&cols_str="+allDim;



	metaWin = window.open(url,"metaWin");
    if(metaWin!=null)
	 metaWin.focus();
}

//明细界面
function _detailSubmit(value){
  window.open("uploadmsu_info_detail.jsp?report_id="+value);
}
//
function selChange(){
	if(document.all.frmEdit){
		document.all.frmEdit.submit();
	}
}

function dateNotice()
{

}

function resetValue()
{
	var start = document.getElementById("qry__begin_date");
	var end = document.getElementById("qry__end_date");

	if(start)
		{
			var hStart = document.getElementById("hStartDate");
			if(hStart)
				{
					start.value = hStart.value;
				}
		}
	if(end)
	{
		var hEnd = document.getElementById("hEndDate");
		if(hEnd)
			{
				end.value = hEnd.value;
			}
	}

}

</script>

</head>
<BODY style="background-color:#f9f9f9">
<div id="maincontent">
<form name="frmEdit" action="uploadmsu_info.jsp" method="post">
<INPUT TYPE="hidden" id="report_id" name="report_id" value="<%=report_id%>" />

        <div class="toptag">
            <Tag:Bar />
        </div>

        <div class="topsearch">

        <table border="0" width="100%">
            <tr>
                <td width="10%" align="right">记录类型：</td>
                <td width="23%"><BIBM:TagSelectList listID="0" listName="record_code" focusID="<%=record_code%>" selfSQL="<%=recordSql%>" script = "onchange='selChange()'" />&nbsp;&nbsp;</td>
                <td width="10%" align="right">起始日期：</td>

                <td width="23%">
				<%if("D".equals(reportInfo.getReport_type())){//日报%>
				<input class="Wdate" type="text" id="qry__begin_date" name="qry__begin_date" size="15" value="<%=begin_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/>&nbsp;&nbsp;
				<%}else if("M".equals(reportInfo.getReport_type())){//月报%>
				<input class="Wdate" type="text" id="qry__begin_date" size="15" name="qry__begin_date" value="<%=begin_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>&nbsp;&nbsp;
				<%}%>
                </td>
                <td width="10%" align="right">终止日期：</td>

                <td width="23%">
				<%if("D".equals(reportInfo.getReport_type())){//日报%>
				<input class="Wdate" type="text" id="qry__end_date" size="15" name="qry__end_date" value="<%=end_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/>&nbsp;&nbsp;
				<%}else if("M".equals(reportInfo.getReport_type())){//月报%>
				<input class="Wdate" type="text" id="qry__end_date" size="15" name="qry__end_date" value="<%=end_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>&nbsp;&nbsp;
				<%}%>
                </td>
            </tr>

			<%
				int icol = 0;
				for (int v = 0; BIBMValue != null && v < BIBMValue.length; v++) {
					if (v % 3 == 0) {
						icol = 0;
			%>
			<tr>
			<%} icol++;%>
				<td width="10%" align="right" nowrap><%=dimMeta[v].getNew_title()%>：</td>
				<td width="23%">
				<%if(dimMeta[v].getRef_map_type().trim().equals(UploadConstant.SVC_MAP_TYPE)&&"1".equals(svcFlag)){%>
						<BIBM:TagSelectList
								allFlag=""
								listID="#"
								focusID="<%=param[v]%>"
								listName="<%=latName[v]%>"
								selfSQL="<%=BIBMValue[v]%>" />
				<%}else if(dimMeta[v].getRef_map_type().trim().equals(UploadConstant.SVC_MAP_TYPE)&&"0".equals(svcFlag)){%>
						<BIBM:TagSelectList
							listID="#"
							focusID="<%=param[v]%>"
							listName="<%=latName[v]%>"
							selfSQL="<%=BIBMValue[v]%>" />
				<%}else{%>
						<BIBM:TagSelectList
							allFlag=""
							listID="#"
							focusID="<%=param[v]%>"
							listName="<%=latName[v]%>"
							selfSQL="<%=BIBMValue[v]%>" />
				<%}%>
				</td>
			<%if (icol == 3 || v == BIBMValue.length-1) {%>
			</tr>
			<%}} %>
		</table>
        </div>

        <div class="topsearch_btn">
            <span>
				<input name="Submit222" type="button" class="btn3" value="查询" onclick="_saveSubmit();">&nbsp;
				<input name="Submit2" type="button" class="btn4" value="明细分析" onclick="_detailSubmit('<%=report_id%>');">&nbsp;
				<input name="Submit2222" type="reset" class="btn3" value="重置" onclick="resetValue()">
            </span>
        </div>
					<!-- 隐藏变量 -->
					<input type="hidden" id="hStartDate" name="hStartDate" value="<%=begin_date%>"/>
					<input type="hidden" id="hEndDate" name="hEndDate" value="<%=end_date%>"/>

        <div class="listbox">
            <div class="listtitle">
            <span class="bigtitle" align="center"><%=reportInfo.getReport_name()%></span>
            </div>
            <div class="list_content hasbg">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1">
			<%=UploadViewHander.getHeadString(msuMeta,reportInfo.getReport_type())%>
			<%=UploadViewHander.getTableString(list,reportInfo,msuMeta)%>
		    </table>
            </div>
        </div>

        <div class="listbox">
        	<div class="list_content">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				class="the-square">
				<tr>
					<td bgcolor="white" align="center"><br>
					<%
					String[] series = null;
					String[] seriesI = null;
					String[] category = null;

					//定义图形显示(默认情况下显示所有指标的趋势)
					String chartS = request.getParameter("chartS");
					if (chartS == null || "".equals(chartS)) {
						chartS = "all";
					}
					//定义系列长度和索引
					if (msuMeta != null && msuMeta.length > 0) {
						if ("all".equals(chartS)) {//全部指标
							series = new String[msuMeta.length];
							seriesI = new String[msuMeta.length];
						} else {//单一指标
							series = new String[1];
							seriesI = new String[1];
						}
					}
					for (int i = 0; msuMeta != null && i < msuMeta.length; i++) {
						if ("all".equals(chartS)) {
							series[i] = msuMeta[i].getNew_title();
							seriesI[i] = Integer.toString(i+1);
						} else {
							for (int tt = 0; tt < msuMeta.length; tt++) {
								if (chartS.equals(msuMeta[tt].getNew_title())) {
									series[0] = msuMeta[tt].getNew_title();
									seriesI[0] = Integer.toString(tt+1);
								}
							}
						}
					}

					//置换得到系列
					String tmp= UploadUtil.changArrsToStr(listASC,1);
					category = tmp.split(";");
					String[] tmps=new String[category.length];
	        		for(int i=0;i<category.length;i++){
	           			 tmps[i]=category[i];
	        		}
					category=tmps;

					String[][] valuearr = CreateChartObj.getFlashChartObjValue(listASC, series, seriesI, 0);
					%>
					<INPUT TYPE="hidden" id="chartS" name="chartS" value="<%=chartS%>" />
					<div id="chartdiv" style="padding-top:5px"></div>
                	<BIBM:FlashChart caption="" subcaption="" categories="<%=category%>"
                                 seriesname="<%=series%>" dataset="<%=valuearr%>" width="850"
                                 height="250" configId="MSLine" jsfunc_name="showChart1"
                                 render="chartdiv" chartType="MSLine" visible="true"
                                 path="<%=request.getContextPath()%>"/>

					</td>
				</tr>
			</table>
			</div>
        </div>
</form>
</div>
    <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
</BODY>
</HTML>