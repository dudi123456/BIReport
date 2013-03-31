<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.table.PubInfoResourceTable"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapDateStruct"%>
<%@page import="com.ailk.bi.olap.util.RptOlapConsts"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapChartAttrStruct"%>
<%@page import="com.ailk.bi.base.table.RptOlapDimTable"%>
<%@page import="com.ailk.bi.base.table.RptOlapMsuTable"%>
<%@ include file="/base/commonHtml.jsp"%>
<style>
span.setbtn{float:right;padding-right:10px;*margin-top:10px}
span.setbtn a{padding:2px 0 0 20px;margin-left:20px;line-height:14px;}
</style>

<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
	response.setDateHeader("Expires", 1);
%>

<%
	//String warnPage = "htmlError.screen";
	//没有表报标识
	String reportId = (String) request.getAttribute("report_id");
%>
<%
String chartType = (String) request.getAttribute("chart_type");
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

	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DOMAINS_OBJ
			+ "_" + reportId);

	List chartStructs = (List) tmpObj;

	String checked_id = "msu_group_" ;
	if(chartStructs.size() > 0){
		Iterator iter2 = chartStructs.iterator();
		int count = 0;
		while(iter2.hasNext()){

			RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter2.next();
			if (!chartStruct.isDim()) {
			count++;
			if(count == 1){
					RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct.getRptStruct();
					checked_id = "msu_group_" + rptMsu.msu_id;
			}
			}
		}
	}

	String check_type = "0";
	if (!RptOlapConsts.OLAP_FUN_BAR.equals(chartType))
	{
		check_type = "0";   //radio
	}else{
		check_type = "1";   //checkbox
	}

	//第一次加载使用正常方式，后续使用AJAX方式,速度是否赶的上
%>
<!DOCTYPE html>
<html>
<head>
	<!-- js-add-start -->
    <script type="text/javascript">

        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}

function resetTime(){
  	//开始得日期重新设置
  	var start=document.getElementById("start_date");
  	if(start){
  		var hStart=document.getElementById("hStartDate");
  		if(hStart){
  			start.value=hStart.value;
  		}
  	}
  	var end=document.getElementById("end_date");
  	if(end){
  		var hEnd=document.getElementById("hEndDate");
  		if(hEnd){
  			end.value=hEnd.value;
  		}
  	}
  }



    </script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联通统一经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/net.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/dojo.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/wait.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/olap/js/olap_common.js"></SCRIPT>
<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css"
	type="text/css">
<script type="text/javascript">
  dojo.require("dojo.event.*");
  dojo.require("dojo.widget.Checkbox");
</script>
<script type="text/javascript">
  var chartType="<%=chartType%>";
  var blockingLoaded = false;

 function loadDimValues(dimId){
  var action="<%=RptOlapConsts.CHART_DIM_VALUES_LOAD%>?";
  action +="report_id=<%=reportId%>";
  var e=document.getElementById(dimId);
  if(e && e.disable!=true){
    var checked=e.checked;
    var values=dimId.split("_");
    action +="&dim_id="+values[2];
    action +="&dim_object="+dimId;
    var isGroup=(values[1]=="group"?true:false);
    var parentNode=document.getElementById("hiddenSpan");
    var elements=parentNode.childNodes;
    var level="0";
    if(isGroup){
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>"){
          level=element.value;
          break;
        }
      }
    }else{
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+values[2])){
          level=element.value;
          break;
        }
      }
    }
    action +="&dim_level="+level;
    var ifDim=document.getElementById("chartDimValues");
    if(ifDim){
    	setTimeout(function(){
	      ifDim.src=action;
	      ShowWait();
	      blockingLoaded = true;
    	},0);
    }
    e.checked=!checked;
  }
 }

 function checkUserSelect(elements){
    var startDateObj=document.getElementById("start_date");
    var endDateObj=document.getElementById("end_date");
    if(startDateObj && endDateObj){
      if(eval(startDateObj.value)>eval(endDateObj.value)){
       alert("您选择起始日期大于截至日期，请重新选择");
       startDateObj.focus();
       return false;
      }
    }
    //如果一个指标也没有
    var hasMsu=false;
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP%>"){
        hasMsu=true;
        break;
      }
    }
    if(!hasMsu){
      alert("您必须至少选择一个指标");
      return false;
    }
    return true;
 }
 //用户选择了其他维度
 function userCustom(){
   var parentNode=document.getElementById("hiddenSpan");
   if(parentNode){
    var link="<%=RptOlapConsts.CHART_DIG_ACTION%>?";
    link +="<%=RptOlapConsts.REQ_REPORT_ID%>=<%=reportId%>";
    link +="&<%= RptOlapConsts.REQ_OLAP_FUNC%>=<%=chartType%>";
    var elements=parentNode.childNodes;
    //进行用户的验证
    var validationOk=checkUserSelect(elements);
    if(validationOk){
      //加上时间
      var startDateObj=document.getElementById("start_date");
      var endDateObj=document.getElementById("end_date");
      link +="&<%=RptOlapConsts.REQ_START_DATE%>="+startDateObj.value;
      link +="&<%=RptOlapConsts.REQ_END_DATE%>="+endDateObj.value;
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.type=="hidden" && element.name.indexOf("hidden")>=0){
          link +="&"+element.name+"="+element.value;
        }
      }
      hideModalDialog();
      //获取新的
      getChartContent(link);
    }
   }
 }

 function getChartContent(link){
    var rpcUrl=link;
    if(!rpcUrl){
      alert("您没有提供访问地址！");
      return;
    }
    var params=[];
    var pos=rpcUrl.indexOf("?");
    if(pos>=0){
      var param=rpcUrl.substring(pos+1);
      rpcUrl=rpcUrl.substring(0,pos);
      params=param.split("&");
    }
    var ajaxHelper=new net.ContentLoader(rpcUrl,params,updateChartContent,ajaxError);
    ajaxHelper.sendRequest();
    ShowWait();
 }
//加载返回的维度值
  function updateChartContent(){
    var jsonTxt=this.req.responseText;
    if(jsonTxt){
      //显示
      var eChartTable=document.getElementById("chartTable");
      if(eChartTable){
        eChartTable.innerHTML=jsonTxt;
        //图形刷新
        document.getElementById('chartBody').contentWindow.location.reload(true);
        closeWaitWin();
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
</script>
</head>
<style type="text/css">
  body { font-family : sans-serif; }
  .dojoDialog {
    background : #eee;
    border : 1px solid #999;
    -moz-border-radius : 5px;
    padding : 4px;
  }

  form {
    margin-bottom : 0;
  }

  /* group multiple buttons in a row */
  .box {
    display: block;
    text-align: center;
  }
  .box .dojoButton {
    float: left;
    margin-right: 10px;
  }
  .dojoButton .dojoButtonContents {
    font-size: medium;
  }
</style>

<script type="text/javascript">
	function addMsuOnLoad(sid){
		document.getElementById(sid).checked=true;
		var obj = document.getElementById(sid);

		addMsu(obj);
		//checked_id
		userCustom();
	}
</script>

<body onload="">
<div id="maincontent">
<!-- 导航区 -->
<div class="toptag">
	<Tag:Bar/>
</div>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<form action="" method="post" enctype="multipart/form-data" name="form1">
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="100%" border="0" cellpadding="0"
					cellspacing="0" class="squareB">
					<tr>
					</tr>
					<tr>
						<td width="100%" height="100%" valign="top">
						<form id="frmSearch" name="frmSearch">

						<table width="100%" border="0" align="center">
					<tr>
					<td height="20px" valign="top">
<div class="topsearch">
	<table><tr>
	<%if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(report.cycle)) {%>
		<td align="right" width="10%">起始日期：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="start_date" size="15" value="<%=ds.getStart()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td align="right" width="10%">截至日期：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="end_date" size="15" value="<%=ds.getEnd()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/></span>
		</td>
	<%} else {%>
		<td align="right" width="10%">起始月份：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="start_date" size="15" value="<%=ds.getStart()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td align="right" width="10%">截至月份：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="end_date" size="15" value="<%=ds.getEnd()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/></span>
		</td>
	<%}%>
		<td align="right" nowrap width="10%">
		<input type="button" name="serach" id="serach_but"
										class="btn3"  onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
										onClick="userCustom();"
										value="查 询">
		<input type="reset" name="Reset"
						class="btn3" onMouseOver="switchClass2(this)"
						onMouseOut="switchClass2(this)" value="重 置" onclick="resetTime();">
		</td>
	</tr></table>
</div>

					</td>
					</tr>
					<tr>
					<td>

							 <!-- 下面是隐藏变量区 -->
								<span id="hiddenSpan">
	 <%
 	//放置分组维度及其值，如果是使用所有的值
 	StringBuffer hidden = new StringBuffer();
 	Iterator iter = chartStructs.iterator();
 	while (iter.hasNext()) {
 		RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
 		.next();
 		if (chartStruct.isDim()) {
 			RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
 			.getRptStruct();
 			if (chartStruct.isDisplay() && chartStruct.isUsedForGroup()
 			&& !chartStruct.isTime()) {
 		//分组维度
 		hidden.append("<input type=\"hidden\" id=\"");
 		hidden.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP);
 		hidden.append("\" name=\"").append(
 				RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP);
 		hidden.append("\" value=\"").append(rptDim.dim_id)
 				.append("\"/>");
 		hidden.append("<input type=\"hidden\" id=\"");
 		hidden
 				.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL);
 		hidden.append("\" name=\"").append(
 				RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL);
 		hidden.append("\" value=\"").append(
 				chartStruct.getLevel()).append("\"/>");
 		hidden.append("<input type=\"hidden\" id=\"");
 		hidden
 				.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE);
 		hidden.append("\" name=\"");
 		hidden
 				.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE);
 		hidden.append("\" value=\"true\"/>");
 		if (chartStruct.isUseAllValues()) {
 			hidden.append("<input type=\"hidden\" id=\"");
 			hidden
 			.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL);
 			hidden.append("\" name=\"");
 			hidden
 			.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL);
 			hidden.append("\" value=\"true\"/>");
 		} else {
 			//部分值
 			hidden.append("<input type=\"hidden\" id=\"");
 			hidden
 			.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL);
 			hidden.append("\" name=\"");
 			hidden
 			.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL);
 			hidden.append("\" value=\"false\"/>");
 			List values = chartStruct.getCurValues();
 			Iterator valIter = values.iterator();
 			while (valIter.hasNext()) {
 				String value = (String) valIter.next();
 				hidden.append("<input type=\"hidden\" name=\"");
 				hidden
 				.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE);
 				hidden.append("\" value=\"").append(value)
 				.append("\"/>");
 			}
 		}
 			} else if (!chartStruct.isTime()) {
 		//如果是条件维度
 		if (chartStruct.isDisplay()) {
 			hidden.append("<input type=\"hidden\" name=\"");
 			hidden
 			.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE);
 			hidden.append("\" value=\"").append(rptDim.dim_id)
 			.append("\"/>");
 			hidden.append("<input type=\"hidden\" name=\"");
 			hidden
 			.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL);
 			hidden.append(rptDim.dim_id).append("\" value=\"");
 			hidden.append(chartStruct.getLevel())
 			.append("\"/>");
 			hidden.append("<input type=\"hidden\" name=\"");
 			hidden
 			.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE);
 			hidden.append(rptDim.dim_id).append(
 			"\" value=\"true\"/>");
 			if (chartStruct.isUseAllValues()) {
 				hidden.append("<input type=\"hidden\" name=\"");
 				hidden
 				.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL);
 				hidden.append(rptDim.dim_id);
 				hidden.append("\" value=\"true\"/>");
 			} else {
 				//部分值
 				hidden.append("<input type=\"hidden\" name=\"");
 				hidden
 				.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL);
 				hidden.append(rptDim.dim_id).append(
 				"\" value=\"false\"/>");
 				List values = chartStruct.getCurValues();
 				Iterator valIter = values.iterator();
 				while (valIter.hasNext()) {
 			String value = (String) valIter.next();
 			hidden
 					.append("<input type=\"hidden\" name=\"");
 			hidden
 					.append(RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE);
 			hidden.append(rptDim.dim_id);
 			hidden.append("\" value=\"").append(value);
 			hidden.append("\"/>");
 				}
 			}
 		}
 			}
 		} else {
 			RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct
 			.getRptStruct();
 			//指标
 			if (chartStruct.isDisplay()) {
 		hidden.append("<input type=\"hidden\" name=\"");
 		hidden.append(RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP);
 		hidden.append("\" value=\"");
 		hidden.append(rptMsu.msu_id).append("\"/>");
 			}
 		}
 	}
 	out.println(hidden.toString());
 %> </span></td>
							</tr>
						</table>
						</form>
						<table width="100%" border="0">
							<tr>
								<td><span class="bulefont"><img
									src="../images/common/home/point.gif" width="10" height="12"> 观察分组维度</span>
								</td>
								<td><span class="bulefont"><img
									src="../images/common/home/point.gif" width="10" height="12"> 条件过滤维度</span>
								</td>
								<td><span class="bulefont"><img
									src="../images/common/home/point.gif" width="10" height="12"> 观察待选指标</span>
								</td>
							</tr>
							<tr>
								<!-- 观察分组维度 -->
								<td align="left" valign="top">
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0">
									<tr>
										<td width="10%"></td>
										<td>
										<DIV
											style="BORDER-RIGHT: #ffffff 2px inset; BORDER-TOP: #ffffff 2px inset;
                      OVERFLOW: auto; BORDER-LEFT: #ffffff 2px inset;
                      WIDTH: 180px; BORDER-BOTTOM: #ffffff 2px inset; HEIGHT: 100px">
										<table width="100%" border="0" align="center" cellpadding="0"
											cellspacing="0">
											<%
												hidden.delete(0, hidden.length());
												iter = chartStructs.iterator();
												while (iter.hasNext()) {
													RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
													.next();
													if (chartStruct.isDim() && !chartStruct.isTime()) {
														RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
														.getRptStruct();
														hidden.append("<tr> \n");
														hidden.append("<td nowrap align=\"left\">");
														hidden.append("<input type=\"checkbox\" ");
														hidden.append("name=\"dim_group\" id=\"dim_group_");
														hidden.append(rptDim.dim_id);
														hidden.append("\" onClick=\"loadDimValues(this.id)\"");
														if (!chartStruct.isDisplay()
														|| !chartStruct.isUsedForGroup())
													hidden.append(" disabled=true ");
														if (chartStruct.isUsedForGroup())
													hidden.append(" checked=true ");
														hidden.append(" value=\"").append(rptDim.dim_id).append(
														"\"/>");
														hidden.append("<label for=\"dim_group_").append(
														rptDim.dim_id);
														hidden.append("\">").append(rptDim.dimInfo.dim_name);
														hidden.append("</label>");
														hidden.append("\n");
														hidden.append("</td></tr>");
													}
												}
												out.println(hidden.toString());
											%>
										</table>
										</DIV>
										</td>
									</tr>
								</table>
								</td>
								<!-- 条件过滤维度 -->
								<td align="left" valign="top">
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0">
									<tr>
										<td width="10%"></td>
										<td>
										<DIV
											style="BORDER-RIGHT: #ffffff 2px inset; BORDER-TOP: #ffffff 2px inset;
                        						OVERFLOW: auto; BORDER-LEFT: #ffffff 2px inset;
                        						WIDTH: 180px; BORDER-BOTTOM: #ffffff 2px inset; HEIGHT: 100px">
										<table width="100%" border="0" align="center" cellpadding="0"
											cellspacing="0">
											<%
												hidden.delete(0, hidden.length());
												iter = chartStructs.iterator();
												while (iter.hasNext()) {
													RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
													.next();
													if (chartStruct.isDim() && !chartStruct.isTime()) {
														RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
														.getRptStruct();
														hidden.append("<tr> \n");
														hidden.append("<td nowrap align=\"left\">");
														hidden.append("<input type=\"checkbox\" ");
														hidden.append("name=\"dim_where\" id=\"dim_where_");
														hidden.append(rptDim.dim_id);
														hidden.append("\" onClick=\"loadDimValues(this.id)\"");
														if (chartStruct.isUsedForGroup())
													hidden.append(" disabled=true ");
														if (chartStruct.isDisplay()
														&& !chartStruct.isUsedForGroup())
													hidden.append(" checked ");
														hidden.append(" value=\"").append(rptDim.dim_id).append(
														"\"/>");
														hidden.append("<label for=\"dim_where_").append(
														rptDim.dim_id);
														hidden.append("\">").append(rptDim.dimInfo.dim_name);
														hidden.append("</label>");
														hidden.append("\n");
														;
														hidden.append("</td></tr>");
													}
												}
												out.println(hidden.toString());
											%>
										</table>
										</DIV>
										</td>
									</tr>
								</table>
								</td>
								<!-- 所有指标 -->
								<td align="left" valign="top">
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0">
									<tr>
										<td width="10%"></td>
										<td>
										<DIV id="divMsuGroup"
											style="BORDER-RIGHT: #ffffff 2px inset; BORDER-TOP: #ffffff 2px inset;
                          OVERFLOW: auto; BORDER-LEFT: #ffffff 2px inset;
                          WIDTH: 180px; BORDER-BOTTOM: #ffffff 2px inset; HEIGHT: 100px">
										<table width="100%" border="0" align="center" cellpadding="0"
											cellspacing="0">
											<%
												hidden.delete(0, hidden.length());
												iter = chartStructs.iterator();



												while (iter.hasNext()) {
													RptOlapChartAttrStruct chartStruct = (RptOlapChartAttrStruct) iter
													.next();
													if (!chartStruct.isDim()) {
														RptOlapMsuTable rptMsu = (RptOlapMsuTable) chartStruct
														.getRptStruct();
														hidden.append("<tr> \n");
														hidden.append("<td nowrap align=\"left\">");
														hidden.append("<input type=\"");
														if (!RptOlapConsts.OLAP_FUN_BAR.equals(chartType))
														{hidden.append("radio");}
														else
													{hidden.append("checkbox");}
														hidden.append("\" name=\"msu_group\" id=\"msu_group_");
														hidden.append(rptMsu.msu_id).append("\"");
														if (chartStruct.isDisplay())
														hidden.append(" checked=\"true\"");
														hidden.append(" value=\"");
														hidden.append(rptMsu.msu_id);
														hidden.append("\" onclick=\"addMsu(this)\"/>");
														hidden.append("<label for=\"msu_group_");
														hidden.append(rptMsu.msu_id).append("\">");
														hidden
														.append((null == rptMsu.col_name
														|| "".equals(rptMsu.col_name) ? rptMsu.msuInfo.msu_name
														: rptMsu.col_name));
														hidden.append("</label>");
														hidden.append("\n");
														hidden.append("</td></tr>");
													}
												}
												out.println(hidden.toString());
											%>
										</table>
										</DIV>
										</td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
						<td background="../biimages/square_line_3.gif"></td>
					</tr>
					<tr>


						<td><img src="../biimages/square_corner_4.gif" width="5"
							height="5"></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<table width="100%" border="0">
					<tr align="right">
						<td rowspan="2" valign="top"><iframe id="chartBody"
							src="rptOlapWebChart.screen?report_id=<%=reportId%>&chart_type=<%=chartType %>"
							width="100%" height="380" frameborder="0" allowTransparency="true"></iframe></td>
						<td valign="middle" width="50%" >
						<div class="widget" >
						<div class="widget_header" >
              				<span class="setbtn">
              				<a href="javascript:;" class="icon set1" onclick="loadWithTime('loadOlapTable.rptdo?report_id=<%=reportId%>');"  >数据</a>
              				<a href="javascript:;" onclick="loadWithTime('loadOlapChart.rptdo?report_id=<%=reportId%>&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_LINE%>');" class="icon set5"><font color="<%=(RptOlapConsts.OLAP_FUN_LINE.equals(chartType)?"red":"black")%>">趋势</font></a>
              				<a href="javascript:;" onclick="loadWithTime('loadOlapChart.rptdo?report_id=<%=reportId%>&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_BAR%>');" class="icon set6"><font color="<%=(RptOlapConsts.OLAP_FUN_BAR.equals(chartType)?"red":"black")%>">对比</font></a>
              				<a href="javascript:;" onclick="loadWithTime('loadOlapChart.rptdo?report_id=<%=reportId%>&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_PIE%>');" class="icon set7"><font color="<%=(RptOlapConsts.OLAP_FUN_PIE.equals(chartType)?"red":"black")%>">构成</font></a>
              				<a href="" class="icon set10" onclick="location.reload();" >刷新</a>
              				</span>
         				</div>
						</div>
						</td>
					</tr>

					<tr>
						<td width="50%" valign="top">
						<div id="chartTable" style="height:380;width:100%;overflow:auto">
						<%
								for (int i = 0; i < HTML.length; i++)
								out.println(HTML[i]);
						%>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	</form>
</table>
</div>
<input type="hidden" id="hidden_checkid" value="<%=checked_id%>">
<input type="hidden" id="hidden_checktype" value="<%=check_type%>">

<div id="dlgDimValues" style="display: none;">
<table width="660px" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><iframe id="chartDimValues" src="" width="100%" height="400px"
			frameborder="0" scrolling="no"> </iframe></td>
	</tr>
	<tr>
		<td align="center"><input type="button" name="customOk"
			value="确定" onclick="userCustom();" class="btn3"> <input
			type="button" name="cancelCustom" value="关闭"
			onclick="hideModalDialog();" class="btn3"></td>
	</tr>
</table>
</div>
<script type="text/javascript">

 function hideModalDialog(){
	 $.unblockUI();
 }
 function addMsu(msuObj){
   if(msuObj){
     var checked=msuObj.checked;
     //alert(checked);
     if(checked){
       var inputType=msuObj.type;
       var parentNode=document.getElementById("hiddenSpan");
       if(parentNode){
        var elements=parentNode.childNodes;
        var removeChildren=[];
        for(var i=0;i<elements.length;i++){
          var element=elements[i];
          if(inputType=="radio"
              && element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP%>"
              && element.value!=msuObj.value){
            removeChildren.push(element);
          }
        }
         for(var i=0;i<removeChildren.length;i++){
            parentNode.removeChild(removeChildren[i]);
         }
         var element=document.createElement("input");
         element.type="hidden";
         element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP%>";
         element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP%>";
         element.value=msuObj.value;
         parentNode.appendChild(element);
       }
     }else{
      //取消了，找到该指标去除
      var parentNode=document.getElementById("hiddenSpan");
      if(parentNode){
        var elements=parentNode.childNodes;
        var removeChildren=[];
        for(var i=0;i<elements.length;i++){
          var element=elements[i];
          if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP%>"
              && element.value==msuObj.value){
            removeChildren.push(element);
          }
        }
        for(var i=0;i<removeChildren.length;i++){
          parentNode.removeChild(removeChildren[i]);
        }
      }
     }
   }
 }
</script>


<script type="text/javascript">
        domHover(".btn3", "btn3_hover");
</script>
</body>
</html>
<script type="text/javascript">
	function displayDim(height){
		if(blockingLoaded){
			setTimeout(function(){
			 $.blockUI({
				 message:$('#dlgDimValues'),
				 fadeIn: 2000,
				 fadeOut: 2000,
		         css: {
		             top:  ($(window).height() - height) /2 + 'px',
		             left: ($(window).width() - 660) /2 + 'px',
		             //height: (height+30)+'px',
		        	 width : '660px',
		        	 cursor : 'default'
		         }
		     });
			 blockingLoaded = false;
			},0);
		}
	}
</script>
