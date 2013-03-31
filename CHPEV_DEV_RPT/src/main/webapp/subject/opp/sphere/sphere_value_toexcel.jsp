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

String[][] value = (String[][])session.getAttribute(WebKeys.ATTR_OPP_SUBJECT_VALUE_lIST);
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


               <table border="1">          
                  <tr>
                  	<td align="center"  >手机号码</td>   
                    <td align="center"  >运营商</td>
                    <td align="center"  >价值评估值 </td>
                    <td align="center"  >近3月通话次数估值</td>                    
                    <td align="center"  >近3月通话时长估值</td>
                    <td align="center"  >近3月次均时长估值</td>
                    <td align="center"  >近3月主被叫估值</td>
                    <td align="center"  >近3月交往圈估值</td>                    
                    <td align="center"  >稳定性估值</td>
                    <td align="center"  >高端客户数估值</td>
                    <td align="center"  >高价值客户数估值</td>
                  </tr>   
                  
                	  <%
                	  for(int i=0;value!=null&&i<value.length;i++){
                	  %>
                	   <tr>
                	  <td align="center"><%=value[i][0]%></td>          
                      <td align="center" ><%=qryStruct.carrier_name%></td>
                          <td align="center"><%=FormatUtil.formatStr(value[i][10],2,false)%></td> 
                      <td align="center" ><%=FormatUtil.formatStr(value[i][2],2,false)%></td>
                      <td align="center"><%=FormatUtil.formatStr(value[i][3],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][4],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][5],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][6],2,false)%></td> 
                      <td align="center"><%=FormatUtil.formatStr(value[i][7],2,false)%></td>
                      <td align="center"><%=FormatUtil.formatStr(value[i][8],2,false)%></td>   
                      <td align="center"><%=FormatUtil.formatStr(value[i][9],2,false)%></td>
                      
                      </tr>   
                	  <%                		  
                	  }
                	  %>
                	 
                </table>
</FORM>
</body>

</html>






