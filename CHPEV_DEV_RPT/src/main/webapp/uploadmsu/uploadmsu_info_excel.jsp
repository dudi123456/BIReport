<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>

<%@ page import="com.ailk.bi.upload.common.*"%>
<%@ page import="com.ailk.bi.upload.dao.impl.*"%>
<%@ page import="com.ailk.bi.upload.domain.*"%>
<%@ page import="com.ailk.bi.upload.facade.*"%>
<%@ page import="com.ailk.bi.upload.util.*"%>
<%@ page import="java.util.*"%>
<%@page import="com.ailk.bi.common.app.FormatUtil"%>
<%@page import="com.ailk.bi.common.app.DateUtil"%>


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
			
			
			//记录类型
			String record_code = request.getParameter(UploadConstant.REPORT_RECORD_CODE);
			if (record_code == null || record_code.equals("")){				
				record_code = facade.getFirstRecord(report_id);	
			}
			
			String cols_str = request.getParameter("cols_str");
			String cols[] = null;
			String[] param=null;
			String[] paramValues=null;
			if(cols_str!=null && !"".equals(cols_str)){
				cols = cols_str.substring(0,cols_str.length()-1).split(",");
				param = new String[cols.length];
				paramValues = new String[cols.length];
			}
			for(int i=0;cols!=null&&i<cols.length;i++){
				String[] tmp = cols[i].split("[:]");
				if(tmp.length ==2){
					param[i] = tmp[0];
					paramValues[i] = tmp[1]; 
				}else if(tmp.length ==1){
					param[i] = tmp[0];
					paramValues[i] = "";
				}else if(tmp.length ==0){
					param[i] = "";
					paramValues[i] = ""; 
				}
				
				
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
			String begin_date = request.getParameter("qry__begin_date");
			if (begin_date == null){
				begin_date = strDates[strDates.length - 1];
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
				
			//根据报表类型、当前报表条件字典、当前展示指标、求出当前值
			String[][] list = UploadHelper.getSumDataList(reportInfo.getReport_type(),dimMeta,msuMeta, paramValues, report_id, record_code, begin_date,end_date);
			//String[][] listASC = list;

			
%>


<html>
<head>
<title>经营分析系统</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
</head>

<body  class="main-body">
<form name="frmEdit" action="uploadmsu_info.jsp" method="post">	

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="the-square">
	
<!-----------------------------------------报表名称----------------------------------------------->
<tr> 
    <td align="center"><%=reportInfo.getReport_name()%></td>
</tr>
<TR>
	<td align="center">&nbsp;</td>
</TR>
<tr>
	<td >
	<table width="100%" border="0" cellpadding="0" cellspacing="0" >
	<tr>
		<td ></td>
		<td >记录类型：<%=record_code%></td>
		<td ></td>
		<td >起始日期：<%=begin_date%></td>
		<td ></td>
		<td >结束日期：<%=end_date%></td>
	</tr>
	<tr>
    <%for(int v=0;BIBMValue!=null && v<BIBMValue.length;v++){
             if (paramValues!=null && paramValues.length>0){
    %>
    		<td ></td>
			<td ><%=dimMeta[v].getNew_title()%>：{<%=paramValues[v]+":"+param[v]%>}	</td>
  	<%		}
      }
    %>
</tr>	 		  
</table>
</td>
</tr>

<TR>
	<td align="center">&nbsp;</td>
</TR>	
<!----------------------------------结果区 start----------------------------------------------->		
<tr> 
    <td > 
	<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0">
      
       <%
       if (msuMeta != null && msuMeta.length > 0) {
	   %>
    	   <tr align="center">
		   <td  class="tab-title2">日 期</td>
			<%
				for(int i=0;i< msuMeta.length; i++){
			%>
				<td  class="tab-title2"  >
				<%=msuMeta[i].getNew_title()+msuMeta[i].getUnit()%>
				</td>
				
		<%	} %>
			</tr>
		<%} %>

	  <%
	  if (list != null && list.length > 0) {
			for (int i = 0; i < list.length; i++) {
				
	  %>
		<tr  >
		<td  align="center"><%=list[i][0]%></td>
				
		<%	for (int j = 1; j < list[i].length; j++) {
					String tempSum = list[i][j];
					int digit=0;
					if(!"".equals(msuMeta[j-1].getDigit()) && msuMeta[j-1].getDigit().length()>0){
			  			digit=Integer.parseInt(msuMeta[j-1].getDigit());
					}	
		%>
				<td  align="right"><%=FormatUtil.formatStr(tempSum,digit,true)%></td>
		<%} %>
				</tr>
			<%}
		}else{
			%>
			<tr class="table-trb">
			<td colspan="<%if (msuMeta!=null && msuMeta.length>0){
				out.print(msuMeta.length+1);
		}else{
			out.print(1);
		} %> " align="center" >无数据</td>
			

			</tr>
		<%}%>
	  
		
    </table>
    </td>
</tr> 


	<!--结果区 end-->

</table>
</form>
</body>
</html>