<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ page import="com.ailk.bi.upload.dao.impl.*"%>
<%@ page import="com.ailk.bi.upload.domain.*"%>
<%@ page import="com.ailk.bi.upload.facade.*"%>
<%@ page import="com.ailk.bi.upload.util.*"%>
<%@ page import="com.ailk.bi.upload.common.*"%>
<%@ page import="java.util.*"%>
<%@page import="com.ailk.bi.common.app.FormatUtil"%>

<!DOCTYPE html>
<html>
<head>
<title>经营分析系统</title>
<%@ include file="/base/commonHtml.jsp"%>
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
</head>

<%

			//报表ID
			String report_id = request.getParameter(UploadConstant.REPORT_ID);
			if (report_id == null){
				report_id = "";
			}				
			
			//得到报表相关信息
			UploadFacade facade = new UploadFacade( new UploadDao());
			UiMsuUpReportConfigTable reportInfo = facade.getReportInfo(report_id);
			if(reportInfo == null){
				reportInfo = new UiMsuUpReportConfigTable();
			}
			
			//记录类型
			String record_code=request.getParameter(UploadConstant.REPORT_RECORD_CODE);
			if(record_code==null){
				record_code="";
			}			

			//获取日期参数				
			String begin_date = request.getParameter("qry__begin_day");
			if(begin_date==null){			
				if("D".equals(reportInfo.getReport_type())){//日报
					begin_date = facade.getMaxDayByReportId(report_id);
				}else if("M".equals(reportInfo.getReport_type())){//月报
					begin_date = facade.getMaxMonthByReportId(report_id);
				}else if("W".equals(reportInfo.getReport_type())){//周报
					begin_date = facade.getMaxDayByReportId(report_id);
				}else{//默认日报
					begin_date = facade.getMaxDayByReportId(report_id);
				}
			}

			
		    //是否显示合计
		    String isAllFlag = request.getParameter("is_all");
		    if(isAllFlag == null || "".equals(isAllFlag)){
		    	isAllFlag = "0";
		    }
			
		    
			//纬度和指标列信息
			UiMsuUpReportMetaInfoTable[] listInfo=facade.getReportMetaInfo(report_id);
			//记录数组
			String[] recordCode=null;
			if(record_code!=null && !record_code.equals("")){
  				recordCode = new String[1];
  				recordCode[0]=record_code; 
			}else{
  				recordCode=UploadHelper.getRecordCode(listInfo);
			}
			//从结构中提取标题头			
			HashMap titleMap=UploadHelper.getTitleMap(listInfo);
			//从结构中提取字段列
			HashMap fieldMap=UploadHelper.getFiledMap(listInfo,"0");
			//从结构中提取字段列
			HashMap fieldMapR=UploadHelper.getFiledMap(listInfo,"1");
			//从结构中提取字段精度
			HashMap digitMap=UploadHelper.getDigitMap(listInfo);

//记录类型
String recordSql=" select record_code,record_desc from UI_MSU_INFO_RPTRECORD where report_id='"+report_id+"' order by record_code ";
%>

<BODY>
	<div id="maincontent">
	<form name="msuForm" action="" method="post">
	<input type="hidden" name="report_id" value="<%=report_id%>" >

        <div class="topsearch">
        <table border="0" width="100%">
            <tr class="tl">
                <td align="right">记录类型：</td>
                <td ><BIBM:TagSelectList listID="0" listName="record_code" allFlag="" focusID="<%=record_code%>" selfSQL="<%=recordSql%>" script = "onchange='selChange()'" />&nbsp;&nbsp;</td>
                <td align="right">起始日期：</td>
                <td >
				<%if("D".equals(reportInfo.getReport_type())){//日报%>
				<input class="Wdate" type="text" id="qry__begin_date" size="15" value="<%=begin_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/>&nbsp;&nbsp;
				<%}else if("M".equals(reportInfo.getReport_type())){//月报%>
				<input class="Wdate" type="text" id="qry__begin_date" size="15" value="<%=begin_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>&nbsp;&nbsp;
				<%}%>
                </td>
                <td align="right">是否显示合计：</td>
                <td ><BIBM:TagRadio radioID="#" radioName="is_all" focusID="<%=isAllFlag%>" selfSQL="1,显示;0,不显示;"  script="onclick='_qrySubmit();'"/>&nbsp;&nbsp;</td>
                <td width="23%" align="right">
                    <input name="qry_btn" type="button" class="btn3" value="查询" onclick="_qrySubmit();" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">&nbsp;
                    <input name="exp_btn" type="button" class="btn3" value="导出" onclick="openExcel('<%=begin_date%>','<%=report_id%>','<%=isAllFlag%>');" onMouseOut="switchClass(this)">&nbsp;
                    <input name="cls_btn" type="button" class="btn3" value="关闭" onclick="_closeSubmit();" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
                </td>
            </tr>
        </table>
        </div>
<!-----------------------------------------结果区 start----------------------------------------->
        <div class="listbox">
            <div class="listtitle">
            <span class="bigtitle" align="center"><%=reportInfo.getReport_name()%></span>
            </div>
            
            <%for(int i=0;i<recordCode.length;i++){%>
			<div class="listtitle"><span class="time">记录类型: <%=recordCode[i]%></span></div>
            <div class="list_content hasbg">
    		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1">
        	<tr align="center" class="tl"> 
  		 	<%
       				String tmp=(String)titleMap.get(recordCode[i]);//数据显示名称					
       				String[] listDataName=tmp.split(","); // 数据显示名称	     
       				String digit=(String)digitMap.get(recordCode[i]);//数据小数长度
       				String[] listDigit=digit.split(","); //数据小数长度      				
       				String sqlselect=(String)fieldMap.get(recordCode[i]);//该记录类型提取数据字段（UI_MSU_UP_INFO_DAY_DATA）    				

       				//
       				String rFileds=(String)fieldMapR.get(recordCode[i]);
       				String[] listFieldR=rFileds.split(","); //数据小数长度   
       				
        			//获取维度的个数进行合计项统计
       				int dimCount= UploadHelper.getDimCount(listInfo,recordCode[i]);
       				//UiMsuUpReportMetaInfoTable[] metaInfo =  facade.getReportMetaInfo(report_id,recordCode[i]);
       				//对维度为0时的特殊处理
	    			for(int l=0;l<listDataName.length;l++){
    				%>
          			<td align="center"><%=listDataName[l]%></td>
       				<%}
					%>
			</tr>
			<%    
				//纬度和指标具体值
     			String[][] list=UploadHelper.getReportDetailList(reportInfo.getReport_type(),report_id,begin_date,recordCode[i],sqlselect,dimCount,listFieldR);
			
				//处理报表索引
				int comArr[] = UploadUtil.getCombineArrIndex(dimCount,"1");		
				//计算合并行数组
				ArrayList helperList =  UploadUtil.getCombinRowArr(list, comArr,0 , 1);
				//合并处理
				String[][] viewRpt =  UploadUtil.getCombineHelperList(list,comArr,helperList);
 			%>
			
  			<%
  			if(viewRpt!=null && viewRpt.length>0){      			

  		    	for(int i_index=0;i_index<viewRpt.length;i_index++){
  	        		out.println("<tr class=\"bgwl\">");
  	        		for(int j=0;j<listFieldR.length;j++){
  	        			if(j<dimCount){//纬度
  	        				ArrayList tmpList = (ArrayList)helperList.get(j);						
  	        			    if(i_index == 0){//第一行
  	        			    	int rowspan = Integer.parseInt((String)tmpList.get(0));
  	        			    	out.println("<td  align=\"center\" rowspan=\""+rowspan+"\" >"+viewRpt[i_index][2*j+1]+"</td>");
  	        			    }else{							
  	            			    int countRowIndex = 0;              			    
  	            			    for(int t_index = 0;t_index<tmpList.size();t_index++){              			    	
  	            			    	countRowIndex +=Integer.parseInt((String)tmpList.get(t_index));
  	            			    	if(i_index==countRowIndex){
  	            			    		int rowspan = Integer.parseInt((String)tmpList.get(t_index+1));
  	            			    		out.println("<td align=\"left\" rowspan=\""+rowspan+"\" >"+viewRpt[i_index][2*j+1]+"</td>");
  	            			    		break;
  	            			    	}
  	            			    }
  	        			    }
  	        			    
  	        			
  	        			}else{
  	        				String results=FormatUtil.formatStr(viewRpt[i_index][dimCount+j],Integer.parseInt(listDigit[j]),true);
  	        				//指标
  	        				String inputStr ="<input name=\"text\" type=\"text\" class=\"text-right\" onMouseOver=\"switchClass(this)\" onMouseOut=\"switchClass(this)\" onChange=\"msuAjaxProcess('"+i_index+"','"+(j)+"','"+report_id+"','"+recordCode[i]+"','"+begin_date+"',this)\" value=\""+results+"\">";
  	        				out.println("<td height=\"22\" align=\"right\">"+results+"</td>");
  	                	}
  	        			
  	        		}
  	        		out.println("</tr>");
  	        		
  	        		
  	        	}
  		    	//合计项
  		    	if("1".equals(isAllFlag)){
  			 	out.println("<tr class=\"jg\">");
  			 	out.println("<td colspan=\""+dimCount+"\" align=\"center\"><strong>合计</strong></td>");
  			 	for(int t=0;t<listDataName.length-dimCount;t++){
  					int digitLength=0;
  					if(!"".equals(listDigit[t+dimCount]) && listDigit[t+dimCount].length()>0){
  						digitLength=Integer.parseInt(listDigit[t+dimCount]);
  					} 	 	
  			 	
  				   out.println("<td nowrap  align=\"right\">"+FormatUtil.formatStr(UploadUtil.getResultSum(2*dimCount+t,viewRpt),digitLength,true)+"</td>");
  			 	}
  			 	
  			 	out.println("</tr>");
  		    	}
  		    	
  			}else{
  				//没有数据
  			}
   			%>
			</table>
            </div>
            <%}%>
        </div>
<!---------------------------------------------------------结果区 end---------------------------------->
</form>
</body>

<script language="javascript">
domHover(".btn4", "btn4_hover");
domHover(".btn3", "btn3_hover");
        
//查询
function _qrySubmit(){
	document.msuForm.submit();
}
//关闭
function _closeSubmit(){
	window.close();
}
//导出excel
function openExcel(begin_date,report_id,allflag){

	var url ="uploadmsu_info_detail_excel.jsp?qry__begin_date="+begin_date+"&report_id="+report_id+"&is_all="+allflag;
        
	metaWin = window.open(url,"metaWin");
    if(metaWin!=null){
    	metaWin.focus();
    }
		
}
//
function BaseXmlSubmit2(){
}
//
BaseXmlSubmit2.prototype.callAction = function f_callAction(url)
{
  var dom = "";
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}
var baseXmlSubmit2 =new BaseXmlSubmit2();
//
function msuAjaxProcess(row , col , report_id , record_code ,date, obj){
	
	var doc=baseXmlSubmit2.callAction("../uploadmsu/UploadAjaxAction.rptdo?&report_id="+report_id+"&record_code="+record_code+"&value="+obj.value+"&row="+row+"&col="+col+"&date="+date);    
    doc=doc.replace(/^\s+|\n+$/g,'');
    var value = doc.split(":");
    if(value[0] == "-1"){
    	alert("修改失败！");
    }else{
    	obj.value = value[1];
    	obj.className = "text-right";
    }
    
}
</script>
</HTML>