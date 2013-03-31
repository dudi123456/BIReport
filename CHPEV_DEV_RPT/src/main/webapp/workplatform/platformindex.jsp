<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@page import="com.ailk.bi.workplatform.entity.OrderInfo"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ProjectInfo"%>
<%@page import="com.ailk.bi.marketing.entity.ActivityInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.marketing.common.CommonFormate"%>
<%@page import="com.ailk.bi.base.util.CodeParamUtil"%>
<%@page import="com.ailk.bi.base.common.InitServlet"%>
<%@page import="com.ailk.bi.base.table.InfoOperTable"%>
<%@page import="com.ailk.bi.system.facade.impl.CommonFacade"%>

<%
InfoOperTable loginUser = CommonFacade.getLoginUser(session);
//今日工单
List<OrderInfo> todayOrderList = (List<OrderInfo>) request.getAttribute("platformOrderInfo");

//协同工单
String[][] outChannelOrderList = (String[][]) request.getAttribute("platformOutChannelOrderInfo");

//二次回访今日工单
List<OrderInfo> twiceTodayOrderList = (List<OrderInfo>) request.getAttribute("platformTwiceTodayOrderInfo");

//获取所有活动工单汇总
String[][] platformAllOrderList = (String[][])request.getAttribute("platformAllOrderInfo");

// 系统消息
String[][] newsArr = (String[][])session.getAttribute("WorkPlatFormNews");

String  tacticCount = (String ) request.getAttribute("tacticCount");
String  projectCount = (String ) request.getAttribute("projectCount");
String  activityCount = (String ) request.getAttribute("activityCount");
String  transCount = (String ) request.getAttribute("transCount");
String  creatorCount = (String ) request.getAttribute("creatorCount");

%>
<!DOCTYPE html>

<html>
<head>
<title>工作平台</title>


<%@ include file="/base/commonHtml.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=context%>/css/other/condition.css">
<script src="<%=context%>/js/jquery.min.js"></script>
<script src="<%=context%>/js/jquery.bi.js"></script>
<script type="text/javascript" src="<%=context%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
<script type="text/javascript" src="<%=context%>/js/dojo.js" djConfig="parseOnLoad:true, isDebug:false"></script>
<script type="text/javascript">

function opWindow(url){
	window.open(url,'newwindow','height=600,width=1200,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
}
function myopWindow(id,custId){
	var url = "orderInfoAction.rptdo?optype=orderInfo&doType=searchOne&orderId="+id+"&custId="+custId;
	window.open(url,'newwindow','height=600,width=1200,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
}
function myAddWindow(){
	var url = "<%=request.getContextPath()%>/workplatform/contactAdd.jsp";
	window.open(url,'newwindow','height=600,width=900,top=60,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=no,location=no, status=no') ;
}
</script>
</head>

<body>
<table width="100%" border="0">
<tr>
<td width="79%" valign="top">
    <!-- 左侧内容 -->
    <div class="widget_place pl">
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name1">待办任务</h2>
                <span class="hr">.</span><span class="wdtime" id="area2"></span>
            </div>
            <div class="widget_content">
                 <div class="widget_bb" style="text-align:left;">
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/arr.gif" />&nbsp;&nbsp;您 有 <font color="red"><%=tacticCount %> 条 </font> 营 销 策 略 待 审 核。
                &nbsp;<a href="javascript:opWindow('<%=request.getContextPath()%>/marketing/tacticAction.rptdo?doType=search&optype=tacticList&qry__tactic_state=1&decider=<%=loginUser.user_id%>')"><font color="blue">我去处理...</font></a> <br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/arr.gif" />&nbsp;&nbsp;您 有 <font color="red"><%=projectCount %> 条 </font> 营 销 方 案 待 审 核。
                &nbsp;<a href="javascript:opWindow('<%=request.getContextPath()%>/marketing/projectAction.rptdo?doType=search&optype=projectList&qry__project_state=1&warnName=<%=loginUser.user_id%>')"><font color="blue">我去处理...</font></a> <br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/arr.gif" />&nbsp;&nbsp;您 有 <font color="red"><%=activityCount %> 条 </font> 营 销 活 动 待 审 核。
                &nbsp;<a href="javascript:opWindow('<%=request.getContextPath()%>/marketing/activityAction.rptdo?doType=search&optype=activityList&qry__activity_state=1&decider=<%=loginUser.user_id%>')"><font color="blue">我去处理...</font></a> <br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/arr.gif" />&nbsp;&nbsp;您 有 <font color="red"><%=creatorCount %> 条 </font> 渠 道 转 派 待  提 交。
                &nbsp;<a href="javascript:opWindow('<%=request.getContextPath()%>/marketing/transAction.rptdo?doType=search&optype=transList&showType=1&transState=-1')"><font color="blue">我去处理...</font></a><br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/arr.gif" />&nbsp;&nbsp;您 有 <font color="red"><%=transCount %> 条 </font> 渠 道 转 派 待 审 核。
                &nbsp;<a href="javascript:opWindow('<%=request.getContextPath()%>/marketing/transAction.rptdo?doType=search&optype=transList&showType=2&transState=0')"><font color="blue">我去处理...</font></a>
                </div>
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name1">协同工单</h2>
                <span class="hr">.</span><span class="wdtime" id="area2">
                <a href="orderInfoAction.rptdo?optype=orderList&doType=search&txt_contact2=0&outChannle=1&setType=1"><font color="blue">更多 </font></a>
                </span>
            </div>
            <div class="widget_content">
              <div id="jysd" style="width: 100%; margin-top: 2px;  height:160px;">
                <div class="widget_tb_head"><table><tr>
				<td width="20%" align="center">活动名称</td>
				<td width="20%" align="center">原渠名称</td>
				<td width="20%" align="center">工单总数</td>
				<td width="20%" align="center">未完成数量</td>
				<td width="20%" align="center">已完成数量</td>

				</tr></table></div>
				<div class="widget_tb_content" style="width: 96%; height:130px;">
				<table id="tablebox1">
				<%for(int i=0;outChannelOrderList!=null&&i<outChannelOrderList.length;i++){
				  String csstr = " ";
				  if(i%2==1){
					  csstr = " class=\"jg\"";
				  }
				%>
				<tr <%=csstr%>>
				<td width="20%" align="left"><%=outChannelOrderList[i][1] %></td>
				<td width="20%" align="center"><%=outChannelOrderList[i][5] %></td>
				<td width="20%" align="center"><%=outChannelOrderList[i][2] %></td>
				<td width="20%" align="center"><%=outChannelOrderList[i][3] %></td>
				<td width="20%" align="center"><%=outChannelOrderList[i][4] %></td>
				</tr>
				<%} %>
				</table>
			    </div>
			  </div>
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
    </div>
    <!-- 左侧内容结束 -->

    <!-- 中间内容 -->
    <div class="widget_place pl">
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name2">今日工单</h2>
                <span class="hr">.</span><span class="wdtime" id="area2">
         	<a href="orderInfoAction.rptdo?optype=orderList&doType=search&txt_contact2=0&outChannle=0&setType=1"><font color="blue">更多 </font></a>
                </span>
            </div>
            <div class="widget_content">
              <div id="jysd" style="width: 100%; margin-top: 2px; height:160px;">
                <div class="widget_tb_head"><table><tr>
				<td width="20%" align="center">工单号码</td>
				<td width="40%" align="center">活动名称</td>
				<td width="20%" align="center">客户名称</td>
				<td width="20%" align="center">服务号码</td>
				</tr></table></div>
				<div class="widget_tb_content" style="width: 96%; height:130px;">
				<table id="tablebox1">
				<%for(int i=0;todayOrderList!=null&&i<todayOrderList.size();i++){
				  String csstr = " ";
				  if(i%2==1){
					  csstr = " class=\"jg\"";
				  }
				%>
				<tr <%=csstr%>>
				<td width="20%" align="center">
				 <a href="javascript:myopWindow('<%=todayOrderList.get(i).getOrder_no()%>','<%=todayOrderList.get(i).getCust_id()%>')" ><font color="blue">
				<%=todayOrderList.get(i).getOrder_no() %></font></a></td>
				<td width="40%" align="center"><%=todayOrderList.get(i).getActivityInfo().getActivityName() %></td>
				<td width="20%" align="center"><%=todayOrderList.get(i).getCust_name() %></td>
				<td width="20%" align="center"><%=todayOrderList.get(i).getServ_number() %></td>
				</tr>
				<%} %>
				</table>
			    </div>
			  </div>
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name2">二次回访</h2>
                <span class="hr">.</span><span class="wdtime" id="area2">

                	<a href="orderInfoAction.rptdo?optype=orderList&doType=search&txt_contact2=1&outChannle=0&setType=1"><font color="blue">更多 </font></a>
                </span>
            </div>
            <div class="widget_content">
              <div id="jysd" style="width: 100%; margin-top: 2px; height:160px;">
                <div class="widget_tb_head"><table><tr>
				<td width="20%" align="center">工单号码</td>
				<td width="40%" align="center">活动名称</td>
				<td width="20%" align="center">客户名称</td>
				<td width="20%" align="center">服务号码</td>
				</tr></table></div>
				<div class="widget_tb_content" style="width: 96%; height:130px;">
				<table id="tablebox1">
				<%for(int i=0;twiceTodayOrderList!=null&&i<twiceTodayOrderList.size();i++){
				  String csstr = " ";
				  if(i%2==1){
					  csstr = " class=\"jg\"";
				  }
				%>
				<tr <%=csstr%>>
				<td width="20%" align="center">
				 <a href="javascript:myopWindow('<%=twiceTodayOrderList.get(i).getOrder_no()%>','<%=twiceTodayOrderList.get(i).getCust_id()%>')" ><font color="blue">
				<%=twiceTodayOrderList.get(i).getOrder_no() %></font></a></td>
				<td width="40%" align="center"><%=twiceTodayOrderList.get(i).getActivityInfo().getActivityName() %></td>
				<td width="20%" align="center"><%=twiceTodayOrderList.get(i).getCust_name() %></td>
				<td width="20%" align="center"><%=twiceTodayOrderList.get(i).getServ_number() %></td>
				</tr>
				<%} %>
				</table>
			    </div>
			  </div>
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
    </div>
    <!-- 中间内容结束 -->

    <!-- 左侧2内容 -->
    <div class="widget_platform_large pl">
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name1">任务列表</h2>
                <span class="hr">.</span><span class="wdtime" id="area2"></span>
            </div>
            <div class="widget_content">
              <div id="jysd" style="width: 100%; margin-top: 2px; height:160px;">
                <div class="widget_tb_head"><table><tr>
				<td width="30%" align="center">活动名称</td>
				<td width="10%" align="center">工单总数</td>
				<td width="10%" align="center">已完成数量</td>
				<td width="10%" align="center">当日工单完成</td>
				<td width="10%" align="center">未完成数量</td>
				<td width="10%" align="center">正常未完成数量</td>
				<td width="10%" align="center">超期未完成数量</td>
				<td width="10%" align="center">关闭数量</td>
				</tr></table></div>
				<div class="widget_tb_content" style="width: 96%; height:130px;">
				<table id="tablebox1">
				<%for(int i=0;platformAllOrderList!=null&&i<platformAllOrderList.length;i++){
				  String csstr = " ";
				  if(i%2==1){
					  csstr = " class=\"jg\"";
				  }
				%>
				<tr <%=csstr%>>
				<td width="30%" align="left"><%=platformAllOrderList[i][1] %></td>
				<td width="10%" align="center"><a href="orderInfoAction.rptdo?optype=orderList&doType=search&qry__activityID=<%=platformAllOrderList[i][0] %>&txt_activityName=<%=platformAllOrderList[i][1] %>&setType=1">
				<font color="blue">
				<%=platformAllOrderList[i][2] %></font></a></td>
				<td width="10%" align="center"><%=platformAllOrderList[i][3] %></td>
				<td width="10%" align="center"><%=platformAllOrderList[i][4] %></td>
				<td width="10%" align="center"><%=platformAllOrderList[i][5] %></td>
				<td width="10%" align="center"><%=platformAllOrderList[i][6] %></td>
				<td width="10%" align="center"><%=platformAllOrderList[i][7] %></td>
				<td width="10%" align="center"><%=platformAllOrderList[i][8] %></td>
				</tr>
				<%} %>
				</table>
			    </div>
			  </div>
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
    </div>
    <!-- 左侧2内容结束 -->
</td>
<td width="21%" valign="top">
    <!-- 右侧内容 -->
    <div class="widget_platform_large pl">
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name3">公告信息</h2>
                <span class="hr">.</span><span class="wdtime" id="area2"></span>
            </div>
            <div class="widget_content">
              <div id="marquees">
              <%
               for(int i=0; newsArr!=null && i < newsArr.length; i++)
                {
              %>
				&nbsp;<image src="<%=request.getContextPath() %>/images/system/arr.gif" />&nbsp;&nbsp;<a href="javascript:openBullInfo('<%=newsArr[i][4] %>')"><%=newsArr[i][0] %></a><br>
              <%}%>
              <br><br><br><br>
            </div></div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name4">客户视图</h2>
                <span class="hr">.</span>
            </div>
            <div class="widget_content">
                <br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/search-ico.gif" />
                <input type="text" name="serv_number" size="15" class="txtinput" value="请输入服务号码" onblur="phoneBlur();" onFocus="phoneFocus();"/>
				<input type="button" name="search_custview" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="查看" />
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1" id="msu_name4">快速链接</h2>
                <span class="hr">.</span>
            </div>
            <div class="widget_content">
                <br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/scj.png" />&nbsp;&nbsp;
                 <a href="javascript:myAddWindow('','')" ><font color="blue">
                	客户来访录入</font></a><br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/scj.png" />&nbsp;&nbsp;
                <a href="contactAction.rptdo?optype=contactList&doType=searchAll"><font color="blue">接触历史信息</font></a>
                	<br><br>
                	<%if(!"1".equals(loginUser.duty_id)) {
                		%>
                		  &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/scj.png" />&nbsp;&nbsp;
       <a href="orderInfoAction.rptdo?optype=orderList&doType=search&txt_contact2=0&outChannle=0&setType=2"><font color="blue">
                工单任务分配</font></a><br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/scj.png" />&nbsp;&nbsp;
                 <a href="orderInfoAction.rptdo?optype=orderList&doType=search&txt_contact2=0&outChannle=0&setType=3"><font color="blue">
                工单任务调整</font></a><br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/scj.png" />&nbsp;&nbsp;
      <a href="adjustAction.rptdo?optype=adjustList&doType=search"><font color="blue">
                工单调整历史信息</font></a><br><br>
                &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/scj.png" />&nbsp;&nbsp;
     <a href="orderInfoAction.rptdo?optype=orderList&doType=search&txt_contact2=0&outChannle=0&setType=4"><font color="blue">
                工单跨渠道转派</font></a><br><br>
                 &nbsp;&nbsp;<image src="<%=request.getContextPath() %>/images/system/scj.png" />&nbsp;&nbsp;
     <a href="<%=request.getContextPath()%>/marketing/transAction.rptdo?doType=search&optype=transList&showType=1"><font color="blue">
               工单跨渠道信息维护</font></a><br><br>

                		<%
                	}%>


            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
    </div>
    <!-- 右侧内容结束 -->
</td>
</tr>
</table>

</body>

<script type="text/javascript">
domHover(".btn3", "btn3_hover");

function switchClass2(theObj){
	if(theObj.className.indexOf("_hover")<0){
		theObj.className=theObj.className+"_hover";
	}
	else{
		theObj.className=theObj.className.replace("_hover","");
	}
}

	marqueesHeight=80;
	stopscroll=false;
	with(marquees){
	  style.width="100%";
	  style.height=marqueesHeight;
	  style.overflowX="visible";
	  style.overflowY="hidden";
	  noWrap=true;
	  onmouseover=new Function("stopscroll=true");
	  onmouseout=new Function("stopscroll=false");
	}
	document.write('<div id="templayer" style="position:absolute;z-index:1;visibility:hidden"></div>');
	preTop=0; currentTop=0;
	document.body.onload=init;

    function init(){
	  templayer.innerHTML="";
	  while(templayer.offsetHeight < marqueesHeight)
	  {
	    templayer.innerHTML += marquees.innerHTML;
	  }
	  marquees.innerHTML= templayer.innerHTML + templayer.innerHTML;
	  setInterval("scrollUp()",200);
	}

	function scrollUp(){
	  if(stopscroll==true) return;
	     preTop=marquees.scrollTop;
	  marquees.scrollTop+=1;
	  if(preTop==marquees.scrollTop)
	  {
	    marquees.scrollTop=templayer.offsetHeight-marqueesHeight;
	    marquees.scrollTop+=1;
	  }
	}
	function openBullInfo(id) {
		var url = "bulletinAdmin.rptdo?opt_type=openshow&id=" + id;
		window.open(url,'','height=500, width=600,top=100, left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=0,	location=0, status=0');
	}
    function phoneBlur() {
      if (serv_number.value == '') {
    	  serv_number.value = "请输入服务号码";
      }
    }
    function phoneFocus() {
    	serv_number.value = "";
    }
</script>
</html>