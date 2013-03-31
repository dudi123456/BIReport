<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.ailk.bi.SysConsts"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.PageConditionUtil"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.workplatform.entity.ContactInfo"%>
<%@page import="com.ailk.bi.workplatform.entity.OrderInfo"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>北京联通统一经营分析系统</title>
<BIBM:SelfRefreshTag pageNames="frmEdit.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart.js" ></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/icon.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/icontent.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/pubilicButton.css" />
<%
			List<ContactInfo> contactList = (List<ContactInfo>)request.getAttribute("contactList");
			OrderInfo orderInfo = (OrderInfo)request.getAttribute("orderInfo");
			String no="";
			String number="";
			String custName="";
			String custID="";
			String orderType="";
			String contactMode="";
			int activityId=0;
			int contactModeId = 0;
			if(orderInfo!=null){
				if(orderInfo.getActivityInfo()!=null){
					activityId = orderInfo.getActivityInfo().getActivityId();
				}
				no = orderInfo.getOrder_no()+"";
				orderType = orderInfo.getOrder_type()+"";
				if(orderInfo.getServ_number()!=null){
					number = orderInfo.getServ_number();
				}
				if(orderInfo.getCust_name()!=null){
					custName = orderInfo.getCust_name();
					custID = orderInfo.getCust_id();
				}
				contactMode = CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTAC_MODE",String.valueOf(orderInfo.getContact_mode()));
				contactModeId = orderInfo.getContact_mode();
			}

			String imgUrl =request.getContextPath()+"/images/validatebox_warning.gif";
			SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String selectCss = "class='easyui-combobox' style='width:180px;'";
			//访问性质
			String natureSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_NATURE'";
			//访问类型
			String intervienTypeSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_INTERVIEW_TYPE'";
			//是与否
			String trueSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_TRUE' order by t.code_id ";
			//未访问到原因
			String noreplyReasonSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_NOREPLY_REASON'";
			//是否有流失倾向
			String awaySql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_AWAY_TREND' order By code_id desc";
			//流失原因
			String awayReasonSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_AWAY_REASON'";
			//是否满意
			String pleasedSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_PLEASED_STATE'";
			//是否参加活动
			String interestSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CONTACT_INTEREST'";
			//性别
			String sexSql = "select t.code_id,t.code_name from ui_code_list t  where t.type_code='MK_PL_CUST_SEX' ORDER BY code_id ASC";
	%>
<script language="javascript">

	function cancel(){
		var url = "orderInfoAction.rptdo?optype=orderInfo&doType=searchOne&orderId=<%=no%>&custId=<%=custID%>";
		window.open(url,'newwindow','height=600,width=900,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
	}
	function  isTrueValue()
	{
		return true;
	}
	function mySave(op) {
		if(isTrueValue()){
			form2.action="contactAction.rptdo?optype=orderInfo&doType=save";
			form2.submit();
			var url = "orderInfoAction.rptdo?optype=orderInfo&doType=searchOne&orderId=<%=no%>&custId=<%=custID%>";
			window.open(url,'newwindow','height=600,width=900,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
		}
	}
	function isRequest(myValue){
		var tt = document.getElementById("txt_noreplyReason");
		tt.disabled=!tt.disabled;
		if(1==myValue){
			tt.value = "";
		}else{
			tt.options[0].selected = true;
		}
	}
	function isaway(myValue){
		var tt = document.getElementById("txt_awayReason");
		if(1==myValue){
			tt.disabled=!tt.disabled;
			tt.options[0].selected = true;
		}else{
			tt.disabled="disabled";
			tt.value = "";
		}
	}

	function isContcat(myValue){
		var tt = document.getElementById("txt_date02");
		tt.disabled=!tt.disabled;
		if(0==myValue){
			tt.value = "";
		}else{
			var myDate = new Date();
			var yy = myDate.getFullYear();    //获取完整的年份(4位,1970-????)
			var mm =  myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
			var dd = myDate.getDate();
			if(mm<10){
				mm="0"+mm;
			}
			if(dd<10){
				dd="0"+dd;
			}
			myDate1=yy+"-"+mm+"-"+dd;
			tt.value = myDate1;
		}
	}
	</script>

</head>
<body style="background-color:#f9f9f9" onload="myLoad()">
<form action="" method="post" name="form2">
<input type="hidden" id="txt_tacticId" name="txt_tacticId" >

  <script type="text/javascript">
        domHover(".btn4", "btn4_hover");
        domHover(".btn3", "btn3_hover");
    </script>
<div class="validatebox-tabTD-title">营维方案信息录入</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="validateBottom">
        <tr>
          <td width="18%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />客户名称：</td>
          <td width="25%" class="validatebox-tabTD-right">
          <input type="hidden" name="txt_custid" id ="txt_custid" value="<%=custID%>">
           <input type="hidden" name="txt_activityid" id ="txt_activityid" value="<%=activityId%>">
          <input type="hidden" name="txt_orderType" id ="txt_orderType" value="<%=orderType%>">
          <input value="<%=custName %>" readonly="readonly" class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_custName" name="txt_custName"></td>
          <td width="18%" class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />性别：</td>
          <td class="validatebox-tabTD-right"> <BIBM:TagSelectList  script=""	listName="txt_cust_sex" listID="0"  selfSQL="<%=sexSql%>" /></td>
        </tr>
         <tr>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />服务号码：</td>
          <td  class="validatebox-tabTD-right">
       		 <input value="<%=number %>" readonly="readonly"   class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_number" name="txt_number">
          </td>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />其他联系方式：</td>
          <td class="validatebox-tabTD-right">
              <input style="width: 200px"    class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_number" name="txt_number">
          </td>
        </tr>
         <tr>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />工单编号：</td>
          <td  class="validatebox-tabTD-right">
           <input value="<%=no %>" readonly="readonly"   class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_orderId" name="txt_orderId">
          </td>
          <td  class="validatebox-tabTD-left">接触方式：</td>
          <td class="validatebox-tabTD-right">
          <input type="hidden" id="txt_contactModeId" name="txt_contactModeId" value="<%=contactModeId%>"/>
           <input value="<%=contactMode %>" style="width: 200px" readonly="readonly"   class="easyui-validatebox" required="true" validType="length[1,20]" id="txt_number" name="txt_number">
          </td>
        </tr>

          <tr>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />访问性质：</td>
          <td  class="validatebox-tabTD-right">
       		<BIBM:TagSelectList  script=""	listName="txt_nature" listID="0"  selfSQL="<%=natureSql%>" />
          </td>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />访问类型：</td>
          <td class="validatebox-tabTD-right">
             	<BIBM:TagSelectList  script=""	listName="txt_ntervienType" listID="0"  selfSQL="<%=intervienTypeSql%>" />
          </td>
        </tr>

          <tr>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />是否访问成功：</td>
          <td  class="validatebox-tabTD-right">
       		<BIBM:TagSelectList  script="onChange=isRequest(this.value)"	listName="txt_isreply" listID="0"  selfSQL="<%=trueSql%>" />
          </td>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />未访问成功原因：</td>
          <td class="validatebox-tabTD-right">
            <BIBM:TagSelectList  script=""	listName="txt_noreplyReason" listID="0"  selfSQL="<%=noreplyReasonSql%>" />
          </td>
        </tr>

         <tr>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />是否需要再次接触：</td>
          <td  class="validatebox-tabTD-right">
			<BIBM:TagSelectList  script="onChange=isContcat(this.value)" listName="txt_contact2" listID="0"  selfSQL="<%=trueSql%>" />
			</td>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />再次接触时间：</td>
          <td class="validatebox-tabTD-right"><input id="txt_date02" disabled="disabled"   name="txt_date02" type="text" class="Wdate" onblur="myTimeClick()"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})"></td>
        </tr>
         <tr>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />用户是否满意：</td>
          <td  class="validatebox-tabTD-right"><BIBM:TagSelectList  script="onChange=isRequest(this.value)"	listName="txt_pleased" listID="0"  selfSQL="<%=pleasedSql%>" /></td>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />是否愿意参加活动：</td>
          <td class="validatebox-tabTD-right"><BIBM:TagSelectList  listName="txt_interest" listID="0"  selfSQL="<%=interestSql%>" /></td>
        </tr>
   		<tr>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />是否有流失倾向：</td>
          <td  class="validatebox-tabTD-right"><BIBM:TagSelectList  script="onChange=isaway(this.value)"	listName="txt_isaway" listID="0"  selfSQL="<%=awaySql%>" /></td>
          <td  class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />流失倾向原因：</td>
          <td class="validatebox-tabTD-right"><BIBM:TagSelectList  script=""	listName="txt_awayReason" listID="0"  selfSQL="<%=awayReasonSql%>" /></td>
        </tr>
         <tr>
          <td class="validatebox-tabTD-left"><img src="<%=imgUrl %>" width="16" height="16" border="0" />回访内容：</td>
          <td colspan="3" class="validatebox-tabTD-right"><textarea  class="easyui-validatebox" required="true" style="height:100px;width: 400px" id="txt_content" name="txt_content"></textarea>
          </td>
        </tr>
        </tr>
</table>

<div class="buttonArea">
<%
if(null!=orderInfo){
	boolean  isBig1 = true;
	if(null!=orderInfo.getContact_start_date()){
		if(date.before(orderInfo.getContact_start_date())){
			isBig1 = true;
		}else{
			isBig1 = false;
		}
	}


	boolean  isBig2=false;
	if(null!=orderInfo.getNext_contact_date()){
		if(date.before(orderInfo.getNext_contact_date())){
			isBig2 = true;
		}else{
			isBig2 = false;
		}
	}

	if(orderInfo.getPerform_state()==1&&!isBig1){
		%>
		<button class="btn4" type="button" onClick="mySave()"> 保 存  </button>
		<%
	}else if(orderInfo.getPerform_state()==2&&!isBig2){
		%>
		<button class="btn4" type="button" onClick="mySave()"> 保 存  </button>
		<%
	}
}
%>

    <button class="btn4" type="button" onClick="cancel()"> 取 消 </button>
</div>
<!-- 历史接触记录-->
<div class="validatebox-tabTD-title">历史接触记录</div>
<div class="list_content">
 <table >
            <tr width="100%">
              <th width="10%" align="center"> 工单编号</th>
              <th width="20%" align="center"> 接触方式</th>
              <th width="20%" align="center"> 接触时间 </th>
              <th width="20%" align="center"> 接触效果 </th>
              <th width="30%" align="center"> 接触内容 </th>
              </tr>
 <% if(null!=contactList){
        		for(int i = 0 ;i<contactList.size();i++){
        			if(i%2!=0){
        				%>
        				<tr class="jg">
        				<%
        			}else{
        			%>
        			 <tr><%} %>
           <td align="center"><%=contactList.get(i).getOrder_no() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTAC_MODE",String.valueOf(contactList.get(i).getContactMode())) %></td>
           <td align="center"><%=contactList.get(i).getContact_date() %></td>
           <td align="center"><%=CodeParamUtil.codeListParamFetcher(request,"MK_PL_CONTACT_PLEASED_STATE",String.valueOf(contactList.get(i).getPleased_state())) %></td>
           <td align="center"><%=contactList.get(i).getContact_content() %></td>
        </tr>
        			<%
        		}
        	}%>
            <tr class="jg">
              <td align="center"><label></label></td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              <td align="center">&nbsp;</td>
              </tr>
          </table>
 </div>
<!-- 历史接触记录结束 -->

</form>
</body>
</html>
