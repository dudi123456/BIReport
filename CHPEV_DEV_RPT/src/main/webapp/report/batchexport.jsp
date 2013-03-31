<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import="com.ailk.bi.base.util.WebKeys"%>
<%@ page import="com.ailk.bi.base.common.CSysCode"%>
<%@ page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page import="com.ailk.bi.report.domain.*"%>


<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;


//查询条件
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_REPORT_QRYSTRUCT);
if(qryStruct==null){
	qryStruct = new ReportQryStruct();
}
//报表信息
RptResourceTable[] rptList  = (RptResourceTable[])session.getAttribute(WebKeys.ATTR_REPORT_RESOURCE_DEFINE);
if(rptList==null){
	rptList = new RptResourceTable[0];
}


%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=CSysCode.SYS_TITLE%></title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/subject.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<script language="javascript">
//改变周期
function ChangeDateBar(){
	document.tableQryForm.action = "ReportBatchExportAction.rptdo";
	document.tableQryForm.target = "_self";
	document.tableQryForm.oper_type.value = "query";	
	document.tableQryForm.report_date.value = "";	
	document.tableQryForm.submit();
}
//查询过滤
function CallQuery(){
	document.tableQryForm.action = "ReportBatchExportAction.rptdo";
	document.tableQryForm.target = "_self";
	document.tableQryForm.oper_type.value = "query";	
	document.tableQryForm.submit();
}
//全选
function checkAll(allObj){
	var obj = document.tableQryForm.elements.tags("input") 
	for (i=0; i < obj.length; i++){
		var e = obj[i];
		if (e.name != null && e.name.length >=12){
			if(e.name.substring(0,12) == "report_check" ){
				e.checked = allObj.checked;
			}
		}
			
	}
	
}
//批量导出
function BatchExport(){	
		document.tableQryForm.action = "ReportBatchExportAction.rptdo";
		document.tableQryForm.target = "_blank";
		document.tableQryForm.oper_type.value = "export";	
		document.tableQryForm.submit();
}

//日控件选择日
function _DayChange(obj){
	document.tableQryForm.action = "ReportBatchExportAction.rptdo";
	document.tableQryForm.target = "_self";
	document.tableQryForm.oper_type.value = "";
	document.tableQryForm.report_date.value = document.tableQryForm.report_date.value.substring(0,6)+obj.value;	
	document.tableQryForm.submit();
}

//日控件选择签换
function _DayClick(dayID){
	document.tableQryForm.action = "ReportBatchExportAction.rptdo";
	document.tableQryForm.target = "_self";
	document.tableQryForm.oper_type.value = "";
	document.tableQryForm.report_date.value = dayID;	
	document.tableQryForm.submit();
}
//月控件选择月
function _MonthClick(monthID){
	document.tableQryForm.action = "ReportBatchExportAction.rptdo";
	document.tableQryForm.target = "_self";
	document.tableQryForm.oper_type.value = "";
	document.tableQryForm.report_date.value = monthID;	
	document.tableQryForm.submit();
}

</script>
</head>

<body>
<form name="tableQryForm" >
<input type="hidden" name="report_date" value="<%=qryStruct.rpt_date%>">
<input type="hidden" name="oper_type" >

<table width="100%">
  <tr>
    <td valign="top"><img src="../biimages/educe_title.gif"></td>
  </tr>
  <tr>
    <td valign="top"><table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="squareB" >
      <tr>
        <td><img src="../biimages/square_corner_1.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_1.gif"></td>
        <td><img src="../biimages/square_corner_2.gif" width="5" height="5"></td>
      </tr>
      <tr>
        <td background="../biimages/square_line_2.gif"></td>
        <td width="100%" height="100%" valign="top"><table width="100%" align="center">
          <tr>
            <td class="ash"><img src="../biimages/arrow6.gif" width="14" height="7"> <span class="welcome-text">导出说明： </span></td>
          </tr>
          <tr>
            <td height="1" background="../biimages/black-dot.gif" ></td>
          </tr>
          <tr>
            <td class="ash">（1）由于批量报表进行报表数据提取数据量很大，因此需要等待相应时间。</td>            
          </tr>
          <tr>
            <td class="ash">（2）文件的下载由于文件比较大，采用压缩下载方式实现</td>
          </tr>
          <tr>
            <td height="1" background="../biimages/black-dot.gif" ></td>
          </tr>          
        </table>          
        <table width="100%" >
            <tr>
              <td class="educe-bg"><img src="../biimages/zt/educe_bg.gif">请选择周期</td>
              <td align="left">
              <BIBM:TagRadio radioName="qry__rpt_cycle" radioID="S3025" focusID="<%=qryStruct.rpt_cycle%>"  script="onclick='ChangeDateBar()'"/>
              </td>
              <td class="educe-bg"><img src="../biimages/zt/educe_bg.gif">报表名称</td>
              <td align="left">
                <input name="qry__rpt_name" type="text" class="normalField2"  value="<%=qryStruct.rpt_name%>"><img src="../biimages/zt/filtrate.gif"  title="过滤" onclick="CallQuery()">
              </td>
            </tr>
          </table>
          <table>
            <tr>
              <td class="educe-bg"><img src="../biimages/zt/educe_bg.gif">请选择日期</td>
              <td id="date">
              	<%
              	String year = "";
              	String month="";
              	String day ="";
              
              	if("6".equals(qryStruct.rpt_cycle)){
              		year = qryStruct.rpt_date.substring(0,4);
              		month = qryStruct.rpt_date.substring(4,6);
              		day = qryStruct.rpt_date.substring(6,8);
              	%>
              	
              	<style>.DayNavi{behavior:url(../htc/dayNavi.htc);}</style>
              	<DIV    class='DayNavi' DefaultYear='<%=year%>' DefaultMonth='<%=month%>' DefaultDay='<%=day%>' DayChange='_DayChange' DayClick='_DayClick'></DIV>
              	<%
              	}else{
              		year = qryStruct.rpt_date.substring(0,4);
              		month = qryStruct.rpt_date.substring(4,6);
              	%>
              	<style>.MonthNavi{behavior:url(../htc/MonthNavi.htc);}</style>                
                <DIV     class='MonthNavi' DefaultYear='<%=year%>' DefaultMonth='<%=month%>' MonthClick='_MonthClick'></DIV>
              
              	<%
              	}
              	%>
              
				</td>
            </tr>
          </table>
          <table >
            <tr>
              <td class="educe-bg"><img src="../biimages/zt/educe_bg.gif">选择报表分类</td>
            </tr>
          </table>
          <table width="100%" >
           <%
           ArrayList typeList = new  ArrayList();
           HashMap typeMap = CodeParamUtil.codeListParamFetcher(request,"RPT_GROUP");
           Iterator it = typeMap.keySet().iterator();
           while(it.hasNext()){
        	   typeList.add(it.next().toString());
           }
           //
           String[] typeValue = (String[])typeList.toArray(new String[typeList.size()]); 
           Arrays.sort(typeValue);
           for(int i=0;typeValue!=null&&i<typeValue.length;i++){
        	   if(i==0||i%5==0){
        		   out.println("<tr>");
        	   }
        	   String vlaue = typeValue[i];
        	   String checkStr ="";
        	   if(qryStruct.rpt_type.indexOf(vlaue)>-1){
        		   checkStr = "checked";
        	   }
        	   out.println("<td><input type=\"checkbox\" name=\"rpt_type\" onclick=\"CallQuery(this)\" value=\""+vlaue+"\" "+checkStr+">"+CodeParamUtil.codeListParamFetcher(request,"RPT_GROUP",vlaue)+"</td>");
        	   if(i==typeMap.size()-1){
        		   if(i%5 == 4){
        			   out.println("<td>&nbsp;</td>"); 
        		   }else{
        			   for(int j=0;j<(4-i%5);j++){
        				   out.println("<td>&nbsp;</td>"); 
        			   }
        		   }
        		   out.println("</tr>");
        	   }        	   
           }
           %>
          </table></td>
        <td background="../biimages/square_line_3.gif"></td>
      </tr>
      <tr>
        <td height="6"><img src="../biimages/square_corner_3.gif" width="5" height="5"></td>
        <td background="../biimages/square_line_4.gif"></td>
        <td><img src="../biimages/square_corner_4.gif" width="5" height="5"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td align="right">
         <input type="button" name="export_batch" value="批量导出" class="button-save" onclick="BatchExport()">
    </td>
  </tr>
  <tr>
    <td class="tab-side2">
    <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0"  style="border-collapse: collapse" bordercolor="#777777">
        <tr align="center">
          <td width="10%" class="tab-title2">选择
           <input type=checkbox name="check_all" onclick="checkAll(this)" />
          </td>
          <td width="20%" class="tab-title2">报表类型</td>
          <td width="35%" class="tab-title2">报表名称</td>
          <td width="35%" class="tab-title2">导出规则</td>
        </tr>
        <%
        	//
        	HashMap ruleMap = CodeParamUtil.codeListParamFetcher(request,"RPT_EXPORT_RULE");
            String[] ruleArr = (String[])ruleMap.keySet().toArray(new String[ruleMap.size()]);
        	Arrays.sort(ruleArr);
        	//
        	for(int j=0;rptList!=null&&j<rptList.length;j++){
				out.println("<tr class=\"table-white-bg\">");
				out.println("<td align=\"center\"><input type=checkbox name=\"report_check\" value=\""+rptList[j].rpt_id+"\" /></td>");
				out.println("<td>"+CodeParamUtil.codeListParamFetcher(request,"RPT_GROUP",rptList[j].rpt_type)+"</td>");
				out.println("<td>"+rptList[j].name+"</td>");				
				out.println("<td>");
				for(int index = 0;ruleArr!=null&&index<ruleArr.length;index++){
					if(rptList[j].rpt_export_rule.indexOf(ruleArr[index]) >-1){
						out.println("<input type=checkbox name=\"report_export_rule_"+ruleArr[index]+"\" value=\""+rptList[j].rpt_id+"\" />"+ruleMap.get(ruleArr[index])+"");
					}
				}
				
				out.println("&nbsp;</td>");
				out.println("</tr>");
        		
        	}
        		
        	
        %>
        
          
          
        
    </table></td>
  </tr>
</table>
</body>
</html>
