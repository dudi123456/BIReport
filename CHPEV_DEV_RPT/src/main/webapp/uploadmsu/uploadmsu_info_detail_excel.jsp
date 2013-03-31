<%@ page contentType="application/vnd.ms-excel;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ page import="com.ailk.bi.upload.dao.impl.*"%>
<%@ page import="com.ailk.bi.upload.domain.*"%>
<%@ page import="com.ailk.bi.upload.facade.*"%>
<%@ page import="com.ailk.bi.upload.util.*"%>
<%@ page import="com.ailk.bi.upload.common.*"%>
<%@ page import="java.util.*"%>
<%@page import="com.ailk.bi.common.app.FormatUtil"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
</head>

<%
			if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  				return;

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
			String begin_date = request.getParameter(UploadConstant.REPORT_QRY_BEGIN_DATE);
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

%>

<BODY class="main-body">
<form name="msuForm" action="" method="post">
<input type="hidden" name="report_id" value="<%=report_id%>" >

<table width="100%" border="0" cellpadding="0" cellspacing="0">

<tr> 
<td  height="10"></td>
</tr>	   

<!-----------------------------------------结果区 start----------------------------------------->

  		<tr> 
    		<td align="center"><span  class="tab-boldtitle"><%=reportInfo.getReport_name()%></span></td>    		
  		</tr>
		
          <%for(int i=0;i<recordCode.length;i++){%>      
          <tr> 
    		<td valign="bottom">
    		<span class="tab-font">
    			<img src="../biimages/feedback-ioc1.gif" width="22" height="14" hspace="3">记录类型: <%=recordCode[i]%></span>
      		</td>
      		
  		 </tr>
  		 <tr> 
    		<td class="tab-side2"> 
    		<table width="100%" border="1" align="center" cellpadding="0" cellspacing="1" class="table-bg">
        	<tr align="center"> 
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
          			<td align="center" class="tab-title2"><%=listDataName[l]%></td>
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
  	        		out.println("<tr class=\"table-white-bg\">");
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
  	            			    		out.println("<td  align=\"center\" rowspan=\""+rowspan+"\" >"+viewRpt[i_index][2*j+1]+"</td>");
  	            			    		break;
  	            			    	}
  	            			    }
  	        			    }
  	        			    
  	        			
  	        			}else{
  	        				String results=FormatUtil.formatStr(viewRpt[i_index][dimCount+j],Integer.parseInt(listDigit[j]),true);
  	        				//指标
  	        				
  	        				out.println("<td height=\"22\">"+results+"</td>");
  	                	}
  	        			
  	        		}
  	        		out.println("</tr>");
  	        		
  	        		
  	        	}
				//合计项
  		    	if("1".equals(isAllFlag)){
  			 	out.println("<tr class=\"tab-row-bg\">");
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
  			}
   			%>
	     

  
</table>
<%}%>
</td>
</tr>
</table>

<!---------------------------------------------------------结果区 end---------------------------------->
</form>
</body>
</html>