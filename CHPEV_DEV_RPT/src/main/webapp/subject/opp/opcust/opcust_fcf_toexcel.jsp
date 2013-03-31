<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>
<%@ page import = "java.io.PrintWriter" %>
<%@ page import = "com.ailk.bi.base.util.*" %>
<%@ page import = "com.ailk.bi.common.app.*" %>
<%@ page import = "com.ailk.bi.common.dbtools.*" %>
<%@ page import = "com.ailk.bi.report.struct.ReportQryStruct" %>
<%@ page import="com.ailk.bi.common.app.AppException"%>

<%
//
ReportQryStruct qryStruct = (ReportQryStruct)session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new ReportQryStruct();
}

String[][] value = (String[][])session.getAttribute(WebKeys.ATTR_OPP_SUBJECT_FCF_lIST);
if(value == null){
	value = new String[0][0];
}
%>


<html>
<head>
<meta http-equiv="Content-Language" content="zh-cn">
<script>
document.body.onmousemove=quickalt;
document.body.onmouseover=getalt;
document.body.onmouseout=restorealt;
</script>

</head>


<body>
<FORM name="TableQryForm" action="OppSubject.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<div style="display:none;position:absolute;" id=altlayer></div>

               <table style="width: 100%" border="1" >          
                  <tr class="celtitle FixedTitleRow">
                  	<td align="center"  >手机号码</td>
                  	<td align="center"  >策反风险系数</td>
                    <td align="center"  >高价值客户评估值</td>
                    <td align="center"  >交往圈客户评估值</td>                    
                    <td align="center"  >高价值对手风险评估值</td>
                    <td align="center"  >高稳定对手风险评估值</td>
                    <td align="center"  >业务预警评估值</td>
                    <td align="center"  >客服预警评估值</td>
                  
                    
                  </tr>   
                  
                	  <%
                	  for(int i=0;value!=null&&i<value.length;i++){
                	  %>
                	   <tr>
                	  <td align="center"><%=value[i][0]%></td>          
                      <td align="center"><%=FormatUtil.formatStr(value[i][7],2,false)%></td> 
                      <td align="center" ><%=FormatUtil.formatStr(value[i][1],2,false)%></td>
                      <td align="center"><%=FormatUtil.formatStr(value[i][2],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][3],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][4],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][5],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][6],2,false)%></td>
                      </tr>   
                	  <%                		  
                	  }
                	  %>
                	 
                 
                	
                 
                
                </table>
</FORM>
              
</body>

</html>






