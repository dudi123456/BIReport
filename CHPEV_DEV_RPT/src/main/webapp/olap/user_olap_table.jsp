<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.base.table.PubInfoResourceTable"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.olap.util.RptOlapConsts"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapDateStruct"%>
<%@page import="waf.controller.web.action.HTMLActionException"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="com.ailk.bi.olap.action.ReportOlapTableHTMLAction"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
	response.setDateHeader("Expires", 1);
%>
<%
	String context = request.getContextPath();
	Log log = LogFactory.getLog(ReportOlapTableHTMLAction.class);
%>
<%
	//没有登陆
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
			response)) {
		response.sendRedirect(context + "/index.jsp");
		return;
	}
%>
<%
	String warnPage = "htmlError.screen";
	//没有表报标识
	String reportId = (String) request.getAttribute("report_id");
	if (null == reportId || "".equals(reportId)) {
		HTMLActionException he = new HTMLActionException(session,
		HTMLActionException.WARN_PAGE,
		"由于长时间没有操作，您已经超时，请重新登陆后再重试此操作。" + "如果此问题依然存在，请联系系统管理员，"
				+ "由此造成对您的工作不变，深表歉意！");
		log.error(he);
		response.sendRedirect(warnPage);
		return;
	}
%>
<%
	//表报对象
	Object tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ
			+ "_" + reportId);
	PubInfoResourceTable report = (PubInfoResourceTable) tmpObj;
	//表格对象
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_TABLE_HTML + "_"
			+ reportId);

	String[] HTML = (String[]) tmpObj;
	//日期对象
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_"
			+ reportId);

	RptOlapDateStruct ds = (RptOlapDateStruct) tmpObj;
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_FIR_URL + "_"
			+ reportId);

	String firUrl = (String) tmpObj;

	//第一次加载使用正常方式，后续使用AJAX方式,速度是否赶的上
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Expires" CONTENT="0">
<meta http-equiv="Cache-Control" CONTENT="no-cache">
<meta http-equiv="Pragma" CONTENT="no-cache">
<title>联通统一经营分析系统</title>
<script language=javascript src="<%=context%>/js/date/scw.js"></script>
<script language=javascript src="<%=context%>/js/date/scwM.js"></script>
<SCRIPT language=JavaScript src="<%=context%>/js/dojo.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/wait.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/net.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/popup.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/js.js"></SCRIPT>
<script language=javascript src="<%=context%>/js/align_tab_by_head.js"></script>
<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css"
	type="text/css">
</head>
<script type="text/javascript">
  var firstAjax=false;
  //根据link加载新的表格内容
  function loadNewContent(link){
    var rpcUrl=link;
    if(!rpcUrl){
      alert("您没有提供访问地址！");
      return;
    }
    var preUrlObj=document.getElementById("preUrl");
    if(preUrlObj){
      if(firstAjax){
        var hisObj=document.getElementById("hisUrl");
        if(hisObj){
          hisObj.value=preUrlObj.value;
        }
      }
      firstAjax=true;
      preUrlObj.value=rpcUrl;
    }
    rpcUrl=encodeURI(rpcUrl);
    var params=[];
    var pos=rpcUrl.indexOf("?");
    if(pos>=0){
      var param=rpcUrl.substring(pos+1);
      rpcUrl=rpcUrl.substring(0,pos);
      params=param.split("&");
    }
    var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadTableUpdate,ajaxError);
    ajaxHelper.sendRequest();
    ShowWait();
  }
  //加载返回的维度值
  function loadTableUpdate(){
    var jsonTxt=this.req.responseText;
    if(jsonTxt){
      //显示 
      var tableContent=document.getElementById("olapTableContent");
      if(tableContent){
        tableContent.innerHTML=jsonTxt;
        closeWaitWin();
        alignTable();
      }    
    }else{
      alert('您的操作成功');
      closeWaitWin();
    }
  }
  //向服务器发起请求失败
  function ajaxError(){
      var alertStr='向远程服务器请求数据失败!\n\n';
      alertStr +='对此我们深表遗憾，请关闭浏览器重新登陆，\n如果问题依旧，请联系系统管理员\n\n';
      if(this.req){
        alertStr +='当前请求是否完毕状态: '+this.req.readyState;
        alertStr +='\n当前请求状态: '+this.req.status;
        alertStr +='\n请求包内容:\n '+this.req.getAllResponseHeaders();
      }
      alert(alertStr);
      closeWaitWin();
  }
  function clearRemoteServerSession(){
    var rpcUrl="clearOlapCache.do";
    var params=[];
    var hReportIdObj=document.getElementById("report_id");
    if(hReportIdObj){
      params.push("report_id="+hReportIdObj.value);
      var ajaxHelper=new net.ContentLoader(rpcUrl,params);
      ajaxHelper.sendRequest();
    }
  }
  function dataAna(){
      var rpcUrl=clearOtherFunc(rpcUrl);
      loadNewContent(rpcUrl);  
  }
  function percentAna(){
      var rpcUrl=clearOtherFunc(rpcUrl);
      rpcUrl=rpcUrl
        +"&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_PERCENT%>";
      loadNewContent(rpcUrl);   
  }
  function sameRatioAna(){
      var rpcUrl=clearOtherFunc(rpcUrl);
      rpcUrl=rpcUrl
        +"&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_SAME%>";
      loadNewContent(rpcUrl);
  }
  function lastRatioAna(){
      var rpcUrl=clearOtherFunc(rpcUrl);
      rpcUrl=rpcUrl
        +"&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_LAST%>";
      loadNewContent(rpcUrl);
  }
  function back(){
    var link;
    var hisUrlObj=document.getElementById("hisUrl");
    if(hisUrlObj){
      link=hisUrlObj.value;
      loadNewContent(link);
    }
  }
  function clearOtherFunc(){
    var link;
    var preUrlObj=document.getElementById("preUrl");
    if(preUrlObj){
      link=preUrlObj.value;
      //好像不能清除收缩，仅清除占比、同比、环比属性吧
      //是否要清除掉排序呢
      var replace="<%=RptOlapConsts.REQ_OLAP_FUNC%>";
      link=clearLink(link,replace);
      replace="<%=RptOlapConsts.REQ_EXPAND_LEVEL%>";
      link=clearLink(link,replace);
      replace="<%=RptOlapConsts.REQ_COLLAPSE_EXPAND%>";
      link=clearLink(link,replace);
    }
    return link;
  }
  function clearLink(link,replace){
    var ret=link
     var pos=ret.indexOf("&"+replace+"=");
      if(pos>=0){
        var end=ret.indexOf("&",pos+1);
        if(end>=0){
          //后面还有东西
          ret=ret.substring(0,pos)+ret.substring(end);
        }else{
          //到最后了
          ret=ret.substring(0,pos);
        }
      }
    return ret;
  }
function checkUserSelect(){
    var flag=false;
    var startDateObj=document.getElementById("start_date");
    var endDateObj=document.getElementById("end_date");
    if(startDateObj && endDateObj){
      if(eval(startDateObj.value)>eval(endDateObj.value)){
       alert("您选择起始日期大于截至日期，请重新选择");
       startDateObj.focus();
       return false;        
      }
      var preLinkObj=document.getElementById("preUrl");
        if(preLinkObj){
          var preLink=preLinkObj.value;
          var pos=preLink.indexOf("&<%=RptOlapConsts.REQ_START_DATE%>=");
          var end;
          if(pos>=0){
            end=preLink.indexOf("&",pos+1);
            if(end>=0){
              preLink=preLink.substring(0,pos)+preLink.substring(end);
            }else{
              preLink=preLink.substring(0,pos);
            }
          }
          pos=preLink.indexOf("&<%=RptOlapConsts.REQ_END_DATE%>=");
          if(pos>=0){
            end=preLink.indexOf("&",pos+1);
            if(end>=0){
              preLink=preLink.substring(0,pos)+preLink.substring(end);
            }else{
              preLink=preLink.substring(0,pos);
            }
          }
          //补上新的
          preLink +="&<%=RptOlapConsts.REQ_START_DATE%>="+startDateObj.value;
          preLink +="&<%=RptOlapConsts.REQ_END_DATE%>="+endDateObj.value;
          loadNewContent(preLink);
        }       
      }
  }  
</script>
<body class="main-body"
	onLoad="MM_preloadImages('<%=context%>/biimages/date_chart_over.gif',
  '<%=context%>/biimages/date_chart.gif','<%=context%>/biimages/portrait_over.gif',
  '<%=context%>/biimages/sameness_over.gif','<%=context%>/biimages/tach_over.gif',
  '<%=context%>/biimages/line_chart_over.gif','<%=context%>/biimages/bar_over.gif',
  '<%=context%>/biimages/pie_over.gif');alignTable();closeWaitWin();"
	onResize="alignTable();" onUnload="clearRemoteServerSession();">
<div id="MsgBox"
	style="visibility:hidden;position:absolute;top:1px;left:1px;z-Index:3"></div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>

						<td width="85%" valign="bottom"><img
							src="<%=context%>/biimages/arrow7.gif" width="7" height="7">
						<span class="bulefont">所在位置：</span></td>
						<!-- 此处要显示导航条 -->
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td height="1" background="<%=context%>/biimages/black-dot.gif"></td>
			</tr>
			<tr>
				<td height="2"></td>
			</tr>
			<tr>
				<td class="dateselectarea">
				<table width="100%" border="0" align="center">
					<tr>
						<td></td>
						<%
						if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(report.cycle)) {
						%>
						<td width="164" height="25" align="right" nowrap>起始日期</td>
						<td width="93" nowrap class="dateselect-title"><input
							type="text" name="start_date" value="<%=ds.getStart() %>"
							onClick="scwShow(this,this);" class="input-text" size="15"></td>
						<td><img src="<%=context%>/biimages/calander.gif"
							onClick="scwShow(document.getElementById('start_date'),this);"
							height="18" border="0"></td>
						<td width="114" align="right" nowrap>截止日期</td>
						<td width="90"><input type="text" name="end_date"
							value="<%=ds.getEnd() %>" onclick="scwShow(this,this)"
							class="input-text" size="15"></td>
						<td><img src="<%=context%>/biimages/calander.gif"
							onClick="scwShow(document.getElementById('end_date'),this);"
							height="18" border="0"></td>
						<%
						} else {
						%>
						<td width="164" height="25" align="right" nowrap>起始月份</td>
						<td width="93" nowrap class="dateselect-title"><input
							type="text" name="start_date" value="<%=ds.getStart() %>"
							onClick="scwShowM(this,this);" class="input-text" size="15"></td>
						<td><img src="<%=context%>/biimages/calander.gif"
							onClick="scwShowM(document.getElementById('start_date'),this);"
							height="18" border="0"></td>
						<td width="114" align="right" nowrap>截止月份</td>
						<td width="90"><input type="text" name="end_date"
							value="<%=ds.getEnd() %>" onclick="scwShowM(this,this)"
							class="input-text" size="15"></td>
						<td><img src="<%=context%>/biimages/calander.gif"
							onClick="scwShowM(document.getElementById('end_date'),this);"
							height="18" border="0"></td>
						<%
						}
						%>
						<td align="center"><input type="button" name="Submit"
							class="button" onMouseOver="switchClass(this)"
							onMouseOut="switchClass(this)" onClick="checkUserSelect();"
							value="查询"> <input type="reset" name="Reset"
							class="button" onMouseOver="switchClass(this)"
							onMouseOut="switchClass(this)" value="重置"> <!-- 下面是隐藏变量区 -->
						<input type="hidden" name="report_id" value="<%=reportId%>" /> <input
							type="hidden" name="preUrl" value="<%=firUrl%>" /> <input
							type="hidden" name="hisUrl" value="<%=firUrl%>" /><!-- 返回用 --></td>
					</tr>
				</table>
				</td>
			</tr>

			<tr>
				<td height="20" align="right"></td>
			</tr>
			<tr>
				<td>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td><img src="<%=context%>/biimages/sh/corner_1.gif"
									height="21"></td>
								<td nowrap class="lable-bg"><img
									src="<%=context%>/biimages/dot.gif" width="7" height="7"> <span
									class="blackbold"><%=report.name%></span></td>
								<td><img src="<%=context%>/biimages/sh/corner_2.gif"
									height="21"></td>
								<td width="100%" align="right" class="side-3"><a
									href="javascript:dataAna();"
									onMouseOver="MM_swapImage('Image1','','<%=context%>/biimages/date_chart_over.gif',1)"
									onMouseOut="MM_swapImgRestore()" onclick="ShowWait()"><img
									src="<%=context%>/biimages/date_chart.gif" name="Image1"
									border="0" alt="数据分析"></a><a href="javascript:percentAna();"
									onMouseOut="MM_swapImgRestore()"
									onMouseOver="MM_swapImage('Image22','','<%=context%>/biimages/portrait_over.gif',1)"
									onclick="ShowWait()"><img
									src="<%=context%>/biimages/portrait.gif" name="Image22"
									border="0" alt="纵向占比分析"></a><a
									href="javascript:sameRatioAna();"
									onMouseOut="MM_swapImgRestore()"
									onMouseOver="MM_swapImage('Image23','','<%=context%>/biimages/sameness_over.gif',1)"
									onclick="ShowWait()"><img
									src="<%=context%>/biimages/sameness.gif" name="Image23"
									border="0" alt="同比分析"></a><a
									href="javascript:lastRatioAna();"
									onMouseOut="MM_swapImgRestore()"
									onMouseOver="MM_swapImage('Image24','','<%=context%>/biimages/tach_over.gif',1)"
									onclick="ShowWait()"><img
									src="<%=context%>/biimages/tach.gif" name="Image24" border="0"
									alt="环比分析"></a><a
									href="genDefaultReportChart.do?report_id=<%=reportId%>"
									onMouseOut="MM_swapImgRestore()"
									onMouseOver="MM_swapImage('Image25','','<%=context%>/biimages/line_chart_over.gif',1)"
									onclick="ShowWait()"><img
									src="<%=context%>/biimages/line_chart.gif" name="Image25"
									border="0" alt="趋势分析"></a><a
									href="genDefaultReportChart.do?report_id=<%=reportId%>"
									onMouseOut="MM_swapImgRestore()"
									onMouseOver="MM_swapImage('Image26','','<%=context%>/biimages/bar_over.gif',1)"
									onclick="ShowWait()"><img src="<%=context%>/biimages/bar.gif"
									name="Image26" border="0" alt="对比分析"></a><a
									href="genDefaultReportChart.do?report_id=<%=reportId%>"
									onMouseOut="MM_swapImgRestore()"
									onMouseOver="MM_swapImage('Image27','','<%=context%>/biimages/pie_over.gif',1)"
									onclick="ShowWait()"><img src="<%=context%>/biimages/pie.gif"
									name="Image27" border="0" alt="构成分析"></a><a
									href="rptOlapExport.screen?report_id=<%=reportId %>"
									target="_blank"><img src="<%=context%>/biimages/dc.gif"
									border="0" alt="导出到EXCEL"></a><a href="javascript:;"
									onClick="javascript:back();"><img
									src="<%=context%>/biimages/back.gif" border="0" alt="返回"></a><a
									href="javascript:;" onclick="location.reload();" onclick="ShowWait()"><img
									src="<%=context%>/biimages/refurbish.gif" border="0" alt="刷新本页"></a></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td class="side-5">
						<table width="100%" height="20" border="0" cellpadding="0"
							cellspacing="0">
							<tr>
								<td height="20" align="right" class="report-desc">统计期：<%=ds.getStart()%>-<%=ds.getEnd()%></td>
							</tr>
							<tr>
								<td id="olapTableContent" align="center">
								<%
									if (null != HTML) {
										for (int i=0;i<HTML.length;i++) 
											out.println(HTML[i]);
									}
								%>
								</td>
							</tr>
							<tr>
								<td>
								<table width="98%" align="center">
									<tr>
										<td><span class="blackbold"><span
											class="blackbold"><img
											src="<%=context%>/biimages/arrow6.gif" width="14" height="7"></span>
										</span> <a href="javascript:;" onClick="multi_edit()" class="bule">说明</a></td>
									</tr>
									<tr>
										<td height="1" background="<%=context%>/biimages/black-dot.gif"></td>
									</tr>
									<tr>
										<td id="report_msu_memo" width="19%"></td>
									</tr>
									<tr>
										<td height="1" background="<%=context%>/biimages/black-dot.gif"></td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>

