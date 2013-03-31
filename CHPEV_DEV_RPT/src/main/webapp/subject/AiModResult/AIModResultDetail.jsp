<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import = "java.util.*" %>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ include file="/base/commonHtml.jsp"%>
<%@page import="com.ailk.bi.common.app.StringB"%>
<% 
  String head2 = (String)session.getAttribute("head2");
  String param = (String)session.getAttribute("param");
  String deal_month = (String)session.getAttribute("deal_month");
  String result_string = (String)session.getAttribute("result_string");
  int month2 = 1;
  String year = "";
  if(deal_month!=null && !deal_month.equals("")){
	  month2 = Integer.parseInt(deal_month.substring(4, 6));
	  year = deal_month.substring(0, 4);
  }
  String month = "select gather_mon,gather_mon from d_month where year_id="+year;  
%>
<html>
  <head>
     <title>结果明细</title>
  </head>
<style>
.content{
  font-family:Arial;
  font-size:12px;
  font-weight:bold;
  font-style:normal;
  text-decoration:underline;
  text-align:right;
  color:#0000EE;
}

.td1{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  text-decoration:none;
  color:#333333;
  text-align:center;
  background-color:#F5F5F5;
  border-top: 1px solid;
  border-top-color: #545454;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
}

.td2{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  text-decoration:none;
  text-align:center;
  color:#333333;
  background-color:#F5F5F5;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
}
.td3{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  color:#333333;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
  border-left: 1px solid;
  border-left-color: #545454;
}

.td4{
  font-family:Arial;
  font-size:13px;
  font-style:normal;
  font-weight:normal;
  text-decoration:none;
  color:#333333;
  border-bottom: 1px solid;
  border-bottom-color: #545454;
  border-right: 1px solid;
  border-right-color: #545454;
}
</style>
<script type="text/javascript">
 function doQuery(){
   AiModResultForm.action="AiModResultDetail.rptdo?optype=select&param=<%=param%>&deal_month="+document.getElementById("qry__month").value;
   AiModResultForm.submit();
 }
 
 function excelOut(){
  <%if(!result_string.isEmpty() && result_string!=null && !result_string.equals("")){%>
   AiModResultForm.action="AiModResultDetail.rptdo?optype=export&param=<%=param%>&deal_month="+document.getElementById("qry__month").value;
   AiModResultForm.submit();
  <%}else{%>
    alert("查询数据为空，不允许导出");
  <%}%>
 }
</script>
  <body style="margin:0px 10px 15px 10px;">
  <form name="AiModResultForm" action="AiModResultDetail.rptdo" method="post">
  <div class="topsearch">
    <table width="50%" border=0 cellpadding="0" cellspacing="0">
		<tr>
			<td align="right" style="font-size: 12px;">月份：</td>
			<td>
                <BIBM:TagSelectList focusID="<%=deal_month%>" script="style='width:150px'" listName="qry__month" listID="0" selfSQL="<%=month%>" />
			</td>
			<td>
				<input id="button_submit" type="button" class="btn_search" value="查询" onclick="doQuery()" />
			</td>
			<td>
				<input type="button" class="btn3" value="导出全部" onclick="excelOut()" />
			</td>
		</tr>
	</table>
	</div>
    <table width="100%" cellpadding="0" cellspacing="0">
        <tr>
           <td class='td1' style='border-left: 1px solid;border-left-color: #545454;'>&nbsp;</td>
           <td class='td1'>&nbsp;</td>
           <td class='td1' colspan='4'><%=month2%>月</td>           
        </tr>
        <tr>
           <td class='td2' style='font-weight:bold; border-left: 1px solid;border-left-color: #545454;'>业务活动</td>
           <td class='td2' style='font-weight:bold;'>每个区间出账收入（应收/实收）中间值</br>1.如ARPU、套餐月费或平均套餐月费</td>
           <td class='td2' style='font-weight:bold;'>其中社会渠道<%=head2%></td>
           <td class='td2' style='font-weight:bold;'>出账收入(元)</td>
           <td class='td2' style='font-weight:bold;'>佣金(元)</td>
           <td class='td2' style='font-weight:bold;'>佣金占收比(%)</td>
        </tr>
        <%=result_string %>
    </table>
  </form>
  </body>
</html>
