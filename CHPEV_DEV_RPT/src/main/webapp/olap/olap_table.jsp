<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%@page import="com.ailk.bi.base.table.PubInfoResourceTable"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.olap.util.RptOlapConsts"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapDateStruct"%>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.base.table.RptOlapDimTable"%>
<%@page import="com.ailk.bi.base.table.RptOlapMsuTable"%>
<%@page import="com.ailk.bi.base.table.DimLevelTable"%>
<%@page import="com.ailk.bi.olap.util.RptOlapDimUtil"%>
<%@page import="com.ailk.bi.base.table.RptUserOlapTable"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapFuncStruct"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@ include file="/base/commonHtml.jsp"%>
<%
	//没有表报标识
	String reportId = (String) request.getAttribute("report_id");
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

	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_RPT_DIMS + "_"
			+ reportId);

	List rptDims = (List) tmpObj;

	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_RPT_MSUS + "_"
			+ reportId);

	List rptMsus = (List) tmpObj;
	String userReportNames = "";
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_USER_REPORTS + "_"
			+ reportId);
	if (null != tmpObj) {
		List userReports = (List) tmpObj;
		Iterator iter = userReports.iterator();
		while (iter.hasNext()) {
			RptUserOlapTable userReport = (RptUserOlapTable) iter
			.next();
			userReportNames += userReport.custom_rptname + "::";
		}
	}
	String curFunc = null;
	String displayMode = null;
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_"
			+ reportId);
	if (null != tmpObj) {
		RptOlapFuncStruct olapFun = (RptOlapFuncStruct) tmpObj;
		curFunc = olapFun.getCurFunc();
		displayMode = olapFun.getDisplayMode();
	}
	if (null == curFunc)
		curFunc = RptOlapConsts.OLAP_FUN_DATA;
	//第一次加载使用正常方式，后续使用AJAX方式,速度是否赶的上
	//经测试速度不好
%>


<%@page import="java.util.Iterator"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
    </script>

<!-- js-add-end -->

<title>联通统一经营分析系统</title>
<SCRIPT language=javascript src="<%=context%>/js/net.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/align_tab_by_head.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/wait.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/olap/js/olap_common.js"></SCRIPT>

<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css"
	type="text/css">
</head>
<script type="text/javascript">
  //为定制指标和维度弹出dojo模式对话框
  var scrollTop;
  var firstAjax=false;
  var userReportNames="<%=userReportNames%>";
  var restoreImage="Image1";
  var restroeImageSrc="<%=context%>/images/date_chart.gif";
  var displayMode ="<%=displayMode%>";
</script>
<script type="text/javascript" DEFER>
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
     //记录下当前的滚动条位置
    var domTableContent=document.getElementById("iTable_ContentTable1");
    if(domTableContent)
    	scrollTop=domTableContent.parentNode.scrollTop;
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
    	document.getElementById("content_date").innerHTML="统计期："+document.getElementById("start_date").value+"-"+document.getElementById("end_date").value;
        tableContent.innerHTML=jsonTxt;
        closeWaitWin();

        //设置滚动条位置为原来
        var domTableContent=document.getElementById("iTable_ContentTable1");
    	if(domTableContent){
    		alignTable();
    		domTableContent.parentNode.scrollTop=scrollTop;
    	}
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
    var rpcUrl="clearOlapCache.rptdo";
    var params=[];
    var hReportIdObj=document.getElementById("report_id");
    if(hReportIdObj){
      params.push("report_id="+hReportIdObj.value);
      var ajaxHelper=new net.ContentLoader(rpcUrl,params);
      ajaxHelper.sendRequest();
    }
  }
  function dataAna(){
	  var time=assembleTime();
	  if(time){
		  ShowWait();
	      var rpcUrl=clearOtherFunc();
	      rpcUrl=rpcUrl
	        +"&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_DATA%>"+time;
	      loadNewContent(rpcUrl);
  	 }else{
  		 return false;
  	 }
  }
  function percentAna(){
	  var time=assembleTime();
	  if(time){
		  ShowWait();
	      var rpcUrl=clearOtherFunc();
	      rpcUrl=rpcUrl
	        +"&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_PERCENT%>"+time;
	      //  alert(rpcUrl);
	      loadNewContent(rpcUrl);
	  }else{
	  		 return false;
	  }
  }
  function sameRatioAna(){
	  var time=assembleTime();
	  if(time){
		  ShowWait();
	      var rpcUrl=clearOtherFunc();
	      rpcUrl=rpcUrl
	        +"&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_SAME%>"+time;
	      //  alert(rpcUrl);
	      loadNewContent(rpcUrl);
	  }else{
	  		 return false;
	  }
  }
  function lastRatioAna(){
	  var time=assembleTime();
	  if(time){
		  ShowWait();
	      var rpcUrl=clearOtherFunc();
	      rpcUrl=rpcUrl
	        +"&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_LAST%>"+time;
	      //  alert(rpcUrl);
	      loadNewContent(rpcUrl);
	  }else{
   		 return false;
   	 }
  }
  function back()
  {
	resetTime();
	//document.getElementById("content_date").innerHTML = "";
    var link;
    var hisUrlObj=document.getElementById("hisUrl");
    if(hisUrlObj){
      link=hisUrlObj.value;
      //当这里有排序字段时，得发送到后台重新查询
      if(link.indexOf("&<%=RptOlapConsts.REQ_SORT%>=")>=0){
      	//加个特殊参数
      	link +="&<%=RptOlapConsts.REQ_HAS_NEW_CONTENT%>=true";
      }
      loadNewContent(link);
    }
  }
  function clearOtherFunc(){
    var link="";
    var preUrlObj=document.getElementById("preUrl");

    if(preUrlObj){
      link=preUrlObj.value;
      //好像不能清除收缩，仅清除占比、同比、环比属性吧
      var replace="<%=RptOlapConsts.REQ_OLAP_FUNC%>";
      link=clearLink(link,replace);
      replace="<%=RptOlapConsts.REQ_EXPAND_LEVEL%>";
      link=clearLink(link,replace);
      replace="<%=RptOlapConsts.REQ_COLLAPSE_EXPAND%>";
      link=clearLink(link,replace);
      //清除掉排序
      replace="<%=RptOlapConsts.REQ_SORT%>";
      link=clearLink(link,replace);
      replace="<%=RptOlapConsts.REQ_SORT_ORDER%>";
      link=clearLink(link,replace);
      replace="<%=RptOlapConsts.REQ_SORT_THIS%>";
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

<body onload="alignTable();"
	onresize="alignTable();" onunload="">

<div id="maincontent">
<!-- 导航区 -->
<div class="toptag">
	<Tag:Bar/>
</div>

<!-- 条件区 -->
<div class="topsearch">
	<table><tr>
	<%if (RptOlapConsts.RPT_STATIC_DAY_PERIOD.equals(report.cycle)) {%>
		<td align="right" width="15%">起始日期：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="start_date" size="15" value="<%=ds.getStart()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td align="right" width="15%">截至日期：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="end_date" size="15" value="<%=ds.getEnd()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/></span>
		</td>
	<%} else {%>
		<td align="right" width="15%">起始月份：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="start_date" size="15" value="<%=ds.getStart()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td align="right" width="15%">截至月份：</td>
		<td>
		<input class="Wdate" style="height:22px;" type="text" id="end_date" size="15" value="<%=ds.getEnd()%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/></span>
		</td>
	<%}%>
		<td align="right" nowrap width="15%">
		<input type="button" name="Submit" id="search_button"
						class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
						onClick="checkUserSelect();" style="cursor:hand;"
						value="查 询">
		<input type="reset" name="Reset"
						class="btn3" onMouseOver="switchClass2(this)" style="cursor:hand;"
						onMouseOut="switchClass2(this)" value="重 置" onclick="resetTime();">
		<input type="button" name="custom" class="btn3"
						onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"
						value="定 制" onclick="displayCustom();" style="cursor:hand;">
		</td>
	</tr></table>
				<!-- 下面是隐藏变量区 -->
					<input type="hidden" name="report_id" value="<%=reportId%>" />
					<input type="hidden" id="preUrl" name="preUrl" value="<%=firUrl%>" />
					<input type="hidden" id="hisUrl" name="hisUrl" value="<%=firUrl%>" />
					<input type="hidden" id="customUrl" name="customUrl" value="" />
					<input type="hidden" id="hStartDate" name="hStartDate" value="<%=ds.getStart()%>"/>
					<input type="hidden" id="hEndDate" name="hEndDate" value="<%=ds.getEnd()%>"/>
				<!-- 返回用 -->
</div>

	   <div class="listbox">
            <div class="listtitle">
                <span class="icon title"><%=report.name%></span><span class="date" id="content_date">统计期：<%=ds.getStart()%>-<%=ds.getEnd()%></span>
            </div>

            <div class="listwrap">
              <div class="widget">
              	<div class="widget_header">
              				<span class="hl">.</span> <span class="hr">.</span><span class="setbtn">
              				<a href="javascript:;" class="icon set1" onclick="{dataAna();changeImage('Image1','<%=context%>/images/date_chart_over.gif');}"  >数据</a>
              				<a href="javascript:;" class="icon set2" onclick="{percentAna();changeImage('Image22','<%=context%>/images/portrait_over.gif');}" >占比</a>
              				<a href="javascript:;" onclick="{sameRatioAna();changeImage('Image23','<%=context%>/images/sameness_over.gif');}" class="icon set3">同比</a>
              				<a href="javascript:;" onclick="{lastRatioAna();changeImage('Image24','<%=context%>/images/tach_over.gif');}" class="icon set4">环比</a>
              				<a href="javascript:;" onclick="loadWithTime('loadOlapChart.rptdo?report_id=<%=reportId%>&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_LINE%>');" class="icon set5">趋势</a>
              				<a href="javascript:;" onclick="loadWithTime('loadOlapChart.rptdo?report_id=<%=reportId%>&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_BAR%>');" class="icon set6">对比</a>
              				<a href="javascript:;" onclick="loadWithTime('loadOlapChart.rptdo?report_id=<%=reportId%>&<%=RptOlapConsts.REQ_OLAP_FUNC%>=<%=RptOlapConsts.OLAP_FUN_PIE%>');" class="icon set7">构成</a>
              				<a href="<%=context %>/olap/olap_table_export.jsp?report_id=<%=reportId %>" class="icon set8">导出</a>
              				<a href="javascript:back();" class="icon set9">返回</a>
              				<a href="" class="icon set10" onclick="location.reload();" >刷新</a>
              				</span>


              	</div>
              	<div class="widget_content1">
                 	<div class="widget_tb_content_big1">
                 	<tr>
                 	<td class="side-5">
                 	 <table width="100%"  border="0" cellpadding="0"
							cellspacing="0">
                 	 	<tr>
								<td id="olapTableContent" align="center">
									<%
										if (null != HTML) {
											for(int i=0;i<HTML.length;i++)
												out.println(HTML[i]);
										}
									%>
								</td>
						</tr>
					 </table>
					 </td>
					 </tr>
                 	</div>
                </div>
               <div class="widget_bottom">
                        <span class="bl">.</span> <span class="br">.</span>
               </div>
               <div class="tipbox">
                        <span class="blue title"><%=CommTool.getEditorHtml(reportId, "0")%></span></div>
                </div>


			  </div>
			</div>
			<div class="listbottom">
            </div>
	 </div>
	</div>
</div>





<div id="dlgCustom" style="display: none;">
<form onsubmit="javascript:blank();">
<table width="660"  border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td height="30" valign="top"><span class="tab-font">展现方式：</span> <input id="dig"
			name="digMode" type="radio" value="dig" checked
			onclick="hide('expandDiv','Layer3');display('digDiv','Layer1','Layer2');">
		<label for="dig">层层向下钻取</label> <input type="radio" id="expand"
			name="digMode" value="expand"
			onclick="hide('digDiv','Layer1','Layer2');display('expandDiv','Layer3');">
		<label for="expand">折叠展开钻取</label></td>
	</tr>
	<tr>
		<td id="digDiv" colspan="2" >
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>

				<td width="90%" height="100" valign="top">

				<div id="digDimDiv" style="width:100%; height:100%;  overflow: auto;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tab-side2">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" class="table-bg">
							<%
								StringBuffer fir = new StringBuffer();
								StringBuffer sec = new StringBuffer();
								StringBuffer thd = new StringBuffer();
								fir.append("<tr height=\"26\">\n");
								fir
										.append("<td align=\"center\" nowrap class=\"tab-title\">名称</td>\n");
								sec.append("<tr height=\"26\" class=\"table-white-bg\"> \n");
								sec.append("<td nowrap class=\"tab-row-bg\">是否显示</td>\n");
								thd.append("<tr height=\"26\" class=\"table-white-bg\"> \n");
								thd.append("<td nowrap class=\"tab-row-bg\">排列顺序</td>\n");
								int index = -1;
								Iterator iter = rptDims.iterator();
								while (iter.hasNext()) {
									index++;
									RptOlapDimTable rptDim = (RptOlapDimTable) iter.next();
									fir.append("<td id=\"f_dim_").append(
									rptDim.dim_id + "_" + index).append(
									"\" align=\"center\" nowrap class=\"tab-title\">")
									.append(rptDim.dimInfo.dim_name).append("</td>\n");
									sec
									.append("<td id=\"s_dim_")
									.append(rptDim.dim_id + "_" + index)
									.append(
											"\" nowrap align=\"center\"> "
											+ "<input type=\"checkbox\" name=\"dimDisplay_dig\" value=\"dim_")
									.append(rptDim.dim_id + "_" + index)
									.append("_" + index).append("\"");
									if (RptOlapConsts.YES.equalsIgnoreCase(rptDim.default_display))
										sec.append(" checked ");
									sec.append("></td>\n");
									thd.append("<td id=\"t_dim_").append(
									rptDim.dim_id + "_" + index).append(
									"\" nowrap align=\"center\">");
									thd.append("<a href=\"javascript:moveLeft('dim_"
									+ rptDim.dim_id + "_" + index + "')\">");
									thd
									.append("<img src=\"../images/move_left.gif\" border=\"0\">");
									thd.append("</a> ");
									thd.append("<a href=\"javascript:moveRight('dim_"
									+ rptDim.dim_id + "_" + index + "')\">");
									thd
									.append("<img src=\"../images/move_right_on.gif\" border=\"0\">");
									thd.append("</a> ");
									thd.append("</td>\n");
								}
								fir.append("</tr>\n");
								sec.append("</tr>\n");
								thd.append("</tr>\n");
								out.println(fir.toString());
								out.println(sec.toString());
								out.println(thd.toString());
							%>
						</table>
						</td>
					</tr>
				  </table>
				</div>
				</td>
				</tr>
				<tr>
				<td width="90%" height="100" valign="top">
				<div id="digMsuDiv"
					style=" width:660px; height:100px;  overflow: auto;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tab-side2">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" class="table-bg">
							<%
								fir.delete(0, fir.length());
								sec.delete(0, sec.length());
								thd.delete(0, thd.length());
								fir.append("<tr height=\"26\">\n");
								sec.append("<tr height=\"26\" class=\"table-white-bg\"> \n");
								thd.append("<tr height=\"26\" class=\"table-white-bg\"> \n");
								iter = rptMsus.iterator();
								while (iter.hasNext()) {

									index++;
									RptOlapMsuTable rptMsu = (RptOlapMsuTable) iter.next();
									fir.append("<td id=\"f_msu_").append(
									rptMsu.msu_id + "_" + index).append(
									"\" align=\"center\" nowrap class=\"tab-title\">")
									.append(rptMsu.msuInfo.msu_name).append("</td>\n");
									//这里的选择框名字如果统一，左右箭头时变化值，怎么办，有了，值为dim_index_index(最后一个进行变化)
									sec
									.append("<td id=\"s_msu_")
									.append(rptMsu.msu_id + "_" + index)
									.append(
											"\" nowrap align=\"center\"> "
											+ "<input type=\"checkbox\" name=\"msuDisplay_dig\" value=\"msu_")
									.append(rptMsu.msu_id + "_" + index)
									.append("_" + index).append("\"");
									if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.default_display))
										sec.append(" checked ");
									sec.append("></td>\n");
									thd.append("<td id=\"t_msu_").append(
									rptMsu.msu_id + "_" + index).append(
									"\" nowrap align=\"center\">");
									thd.append("<a href=\"javascript:moveLeft('msu_"
									+ rptMsu.msu_id + "_" + index + "')\">");
									thd
									.append("<img src=\"../images/move_left.gif\" border=\"0\">");
									thd.append("</a> ");
									thd.append("<a href=\"javascript:moveRight('msu_"
									+ rptMsu.msu_id + "_" + index + "')\">");
									thd
									.append("<img src=\"../images/move_right_on.gif\" border=\"0\">");
									thd.append("</a> ");
									thd.append("</td>\n");
								}
								fir.append("</tr>\n");
								sec.append("</tr>\n");
								thd.append("</tr>\n");
								out.println(fir.toString());
								out.println(sec.toString());
								out.println(thd.toString());
							%>
						</table>
						</td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td id="expandDiv" colspan="2" style="width: 700px; display:none">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top" width="40%" class="tab-side2">
				 <div id="expandDimDiv"
					style="width:310px; height:150px;  overflow: auto;">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="1" class="table-bg">
					<%
						index = -1;
						fir.delete(0, fir.length());
						sec.delete(0, sec.length());
						thd.delete(0, thd.length());
						fir.append("<tr height=\"26\">\n");
						fir
								.append("<td width=\"10%\" align=\"center\" nowrap class=\"tab-title2\">路径</td>\n");
						fir
								.append("<td width=\"47%\" align=\"center\" nowrap class=\"tab-title2\">名称</td>\n");
						fir
								.append("<td width=\"22%\" align=\"center\" nowrap class=\"tab-title2\">是否显示</td>\n");
						fir
								.append("<td width=\"21%\" align=\"center\" nowrap class=\"tab-title2\">排列顺序</td>\n");
						int flag = -1;
						String spaces = "";
						String dimId = null;
						boolean multiDimDisplay=false;
						String singleDisplayId=null;
						iter = rptDims.iterator();
						while (iter.hasNext()) {
							index++;
							RptOlapDimTable rptDim = (RptOlapDimTable) iter.next();
							String maxLevel = rptDim.max_level;
							List levels = rptDim.dimInfo.dim_levels;
							if ((RptOlapConsts.NO.equalsIgnoreCase(rptDim.is_timedim) && (null == levels
							|| 0 >= levels.size() || "".equals(maxLevel) || RptOlapConsts.ZERO_STR
							.equals(maxLevel)))
							|| (RptOlapConsts.YES
									.equalsIgnoreCase(rptDim.is_timedim) && (null == rptDim.timeLevel
									|| RptOlapConsts.ZERO_STR
									.equals(rptDim.max_level) || ""
									.equals(rptDim.max_level)))) {
								if(RptOlapConsts.YES
										.equalsIgnoreCase(rptDim.default_display)){
									multiDimDisplay=true;
								}
								sec.append("<tr  height=\"26\" id=\"tr_dim_").append(
								rptDim.dim_id + "_" + index).append(
								"\" class=\"table-white-bg\"> \n");
								sec.append("<td align=\"left\" nowrap>");
								if (-1 == flag)
								{
									//第一次需要加上单选按钮
									//默认选中
									dimId = "dim_" + rptDim.dim_id;
									singleDisplayId = dimId;
									sec.append("<input type=\"radio\" id=\"")
										.append(dimId)
										.append(
											"\" name=\"dimSelect\" value=\"multiDims\" checked>");
								}
								else
								{    //修改折叠钻去维度排列错误。
									//spaces += "&nbsp;&nbsp;";
									//sec.append("&nbsp;");
								}
								flag++;
								sec.append("</td>\n");
								sec.append("<td align=\"left\" nowrap>");
								sec.append("<label for=\"").append(dimId).append("\">");
								sec.append(spaces + rptDim.dimInfo.dim_name);
								sec.append("</label>");
								sec.append("</td>\n");
								//这里的选择框名字如果统一，左右箭头时变化值，怎么办，有了，值为dim_index_index(最后一个进行变化)
								sec
								.append(
								"<td nowrap align=\"center\"> "
										+ "<input type=\"checkbox\" name=\"dimDisplay_exp\" value=\"dim_")
								.append(rptDim.dim_id + "_" + index).append(
								"_" + index).append("\"");
								if (RptOlapConsts.YES
								.equalsIgnoreCase(rptDim.default_display))
							sec.append(" checked ");
								sec.append("></td>\n");
								sec.append("<td nowrap align=\"center\">");
								sec.append("<a href=\"javascript:moveUp('dim_"
								+ rptDim.dim_id + "_" + index + "')\">");
								sec
								.append("<img src=\"../images/move_up.gif\" border=\"0\">");
								sec.append("</a> ");
								sec.append("<a href=\"javascript:moveDown('dim_"
								+ rptDim.dim_id + "_" + index + "')\">");
								sec
								.append("<img src=\"../images/move_down_on.gif\" border=\"0\">");
								sec.append("</a> ");
								sec.append("</td>\n");
								sec.append("</tr>\n");
							} else if (RptOlapConsts.YES
							.equalsIgnoreCase(rptDim.is_timedim)) {
								if(RptOlapConsts.YES
										.equalsIgnoreCase(rptDim.default_display)){
									multiDimDisplay=false;
									singleDisplayId=rptDim.dim_id;
								}
								//时间维
								thd.append("<tr height=\"26\" class=\"table-white-bg\"> \n");
								thd.append("<td align=\"left\" nowrap>");
								//第一次需要加上单选按钮
								//默认选中
								thd.append("<input type=\"radio\" id=\"dim_").append(
								rptDim.dim_id).append(
								"\" name=\"dimSelect\" value=\"").append(
								rptDim.dim_id).append("\">");
								dimId = "dim_" + rptDim.dim_id;
								thd.append("</td>\n");
								thd.append("<td align=\"left\" nowrap>");
								//第一次需要加上单选按钮
								//默认选中
								thd.append("<label for=\"").append(dimId).append("\">");
								thd.append(RptOlapDimUtil.getTimeTopLevelName(rptDim));
								thd.append("</label>");
								thd.append("</td>\n");

								//这里的选择框名字如果统一，左右箭头时变化值，怎么办，有了，值为dim_index_index(最后一个进行变化)
								thd.append("<td nowrap align=\"center\"> &nbsp;</td>\n");
								thd.append("<td nowrap align=\"center\">&nbsp;");
								thd.append("</td>\n");
								thd.append("</tr>\n");
								//开始将所有得层次放进去
								String lvSpace = "";
								//还不能显示所有路径，应该到相应层次
								Iterator lvIter = RptOlapDimUtil.genTimeNonTopLevelNames(
								rptDim).iterator();
								while (lvIter.hasNext()) {
							String levelName = (String) lvIter.next();
							thd.append("<tr  height=\"26\" class=\"table-white-bg\"> \n");
							thd
									.append("<td nowrap align=\"center\"> &nbsp;</td>\n");
							lvSpace += "&nbsp;&nbsp;";
							thd.append("<td align=\"left\" nowrap>");
							thd.append("<label for=\"").append(dimId).append("\">");
							thd.append(lvSpace + levelName);
							thd.append("</label>");
							thd.append("</td>\n");

							//这里的选择框名字如果统一，左右箭头时变化值，怎么办，有了，值为dim_index_index(最后一个进行变化)
							thd
									.append("<td nowrap align=\"center\"> &nbsp;</td>\n");
							thd.append("<td nowrap align=\"center\">&nbsp;");
							thd.append("</td>\n");
							thd.append("</tr>\n");
								}
							} else {
								if(RptOlapConsts.YES
										.equalsIgnoreCase(rptDim.default_display)){
									multiDimDisplay=false;
									singleDisplayId=rptDim.dim_id;
								}
								//不能调整顺序，也不能选择是否显示，因为是纵向路径
								thd.append("<tr  height=\"26\" class=\"table-white-bg\"> \n");
								thd.append("<td align=\"left\" nowrap>");
								//第一次需要加上单选按钮
								//默认选中
								thd.append("<input type=\"radio\" id=\"dim_").append(
								rptDim.dim_id).append(
								"\" name=\"dimSelect\" value=\"").append(
								rptDim.dim_id).append("\">");
								dimId = "dim_" + rptDim.dim_id;
								thd.append("</td>\n");
								thd.append("<td align=\"left\" nowrap>");
								//第一次需要加上单选按钮
								//默认选中
								thd.append("<label for=\"").append(dimId).append("\">");
								thd.append(rptDim.dimInfo.dim_name);
								thd.append("</label>");
								thd.append("</td>\n");

								//这里的选择框名字如果统一，左右箭头时变化值，怎么办，有了，值为dim_index_index(最后一个进行变化)
								thd.append("<td nowrap align=\"center\"> &nbsp;</td>\n");
								thd.append("<td nowrap align=\"center\">&nbsp;");
								thd.append("</td>\n");
								thd.append("</tr>\n");
								//开始将所有得层次放进去
								String lvSpace = "";
								//还不能显示所有路径，应该到相应层次
								Iterator lvIter = levels.iterator();
								while (lvIter.hasNext()) {
							DimLevelTable lvStruct = (DimLevelTable) lvIter.next();
							thd.append("<tr  height=\"26\" class=\"table-white-bg\"> \n");
							thd
									.append("<td nowrap align=\"center\"> &nbsp;</td>\n");
							lvSpace += "&nbsp;&nbsp;";
							thd.append("<td align=\"left\" nowrap>");
							thd.append("<label for=\"").append(dimId).append("\">");
							thd.append(lvSpace + lvStruct.lvl_name);
							thd.append("</label>");
							thd.append("</td>\n");

							//这里的选择框名字如果统一，左右箭头时变化值，怎么办，有了，值为dim_index_index(最后一个进行变化)
							thd
									.append("<td nowrap align=\"center\"> &nbsp;</td>\n");
							thd.append("<td nowrap align=\"center\">&nbsp;");
							thd.append("</td>\n");
							thd.append("</tr>\n");
								}
							}
						}
						fir.append("</tr>\n");
						out.println(fir.toString());
						out.println(sec.toString());
						out.println(thd.toString());
					%>
				</table>
				</div>
				</td>

				<td width="60%"  valign="top">
				<div id="expandMsuDiv"
					style="width:310px; height:150px;  overflow: auto;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="tab-side2">
						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="1" class="table-bg">
							<%
								fir.delete(0, fir.length());
								sec.delete(0, sec.length());
								thd.delete(0, thd.length());
								fir.append("<tr height=\"26\">\n");
								sec.append("<tr height=\"26\" class=\"table-white-bg\"> \n");
								thd.append("<tr height=\"26\" class=\"table-white-bg\"> \n");
								int count = 0;
								iter = rptMsus.iterator();
								while (iter.hasNext()) {
									count++;
									index++;
									RptOlapMsuTable rptMsu = (RptOlapMsuTable) iter.next();
									fir.append("<td id=\"f_msu_").append(
									rptMsu.msu_id + "_" + index).append(
									"\" align=\"center\" nowrap class=\"tab-title\">")
									.append(rptMsu.msuInfo.msu_name).append("</td>\n");
									//这里的选择框名字如果统一，左右箭头时变化值，怎么办，有了，值为dim_index_index(最后一个进行变化)
									sec
									.append("<td id=\"s_msu_")
									.append(rptMsu.msu_id + "_" + index)
									.append(
											"\" nowrap align=\"center\"> "
											+ "<input type=\"checkbox\" name=\"msuDisplay_exp\" value=\"msu_")
									.append(rptMsu.msu_id + "_" + index)
									.append("_" + index).append("\"");
									if (RptOlapConsts.YES.equalsIgnoreCase(rptMsu.default_display))
										sec.append(" checked ");
									sec.append("></td>\n");
									thd.append("<td id=\"t_msu_").append(
									rptMsu.msu_id + "_" + index).append(
									"\" nowrap align=\"center\">");
									thd.append("<a href=\"javascript:moveLeft('msu_"
									+ rptMsu.msu_id + "_" + index + "')\">");
									thd
									.append("<img src=\"../images/move_left.gif\" border=\"0\">");
									thd.append("</a> ");
									thd.append("<a href=\"javascript:moveRight('msu_"
									+ rptMsu.msu_id + "_" + index + "')\">");
									thd
									.append("<img src=\"../images/move_right_on.gif\" border=\"0\">");
									thd.append("</a> ");
									thd.append("</td>\n");
								}
								fir.append("</tr>\n");
								sec.append("</tr>\n");
								thd.append("</tr>\n");
								out.println(fir.toString());
								out.println(sec.toString());
								out.println(thd.toString());
							%>
							<tr>
								<td height="10"></td>
							</tr>
							<tr>
								<td colspan="<%=count %>">注意：在维度选择中，将单层次维度组合成一条路径，并可以调整顺序</br>
								对于多层次的维度，只能对其进行观察，不能再将其他维度进行组合，层次顺序也不能进行改变</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="45" colspan="2" align="center">

		<input type="button" name="customOk" class="btn3" value="查 询" onclick="userCustom();">
		<input type="button" name="cancelCustom" class="btn3" value="关 闭" onclick="hideModalDialog();">
		<input type="button" name="saveCustom" class="btn3" value="保 存" onclick="saveUserCustom();">
		</td>
	</tr>
</table>
</form>
</div>


<div dojoType="dialog" id="dlgSave" bgColor="white" bgOpacity="0.3"
	toggle="fade" toggleDuration="250" style="display: none">
<form onsubmit="javascript:blank();">
<table border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td nowrap>报表名称:</td>
		<td nowrap><input type="text" id="cusRptName" name="report_name"
			value="<%=report.name %>" size="20" /></td>
	</tr>
	<tr>
		<td height="45" colspan="2" align="center"><input type="button"
			name="saveOk" class="button" value="收藏" onclick="saveCusReport();"><input
			type="button" name="cancelSave" class="button" value="取消"
			onclick="hideModalDialog();"></td>
	</tr>
</table>
</form>
</div>


<SCRIPT LANGUAGE=JavaScript DEFER>
<!--
  var digAction="<%=RptOlapConsts.OLAP_DIG_ACTION%>?<%=RptOlapConsts.REQ_REPORT_ID%>=";
  var reportId="<%=reportId%>";
  var resetMode="<%=RptOlapConsts.REQ_RESET_MODE%>";
  var dig="<%=RptOlapConsts.RESET_MODE_DIG%>";
  var expand="<%=RptOlapConsts.RESET_MODE_EXPAND%>";
  var expandmode="<%=RptOlapConsts.REQ_EXPAND_MODE%>";
  var singleDim="<%=RptOlapConsts.EXPAND_SIGLE_DIM%>";
  var multiDim="<%=RptOlapConsts.EXPAND_MULTI_DIM%>";
  var level="<%=RptOlapConsts.REQ_DIM_LEVEL_PREFIX%>";
  var saveCusRpt="<%=RptOlapConsts.CUSTOM_REPORT_SAVE_ACTION%>?<%=RptOlapConsts.REQ_REPORT_ID%>=";
  //用户定制查询
  function userCustom(){
    var checked=validateCustom();
    if(!checked){
      alert("您至少要选择一个维度或指标");
      return false;
    }
    //重新生成新的URL，还要通知远程服务器
    var link=digAction;
    link +=reportId;
    link +="&"+resetMode+"=";
    var expandObj=document.getElementById("expand");
    if(expandObj){
      if(expandObj.checked){
        link +=expand;
        //折叠展开模式
        //首先区分是否是单维度还是多维度路径
        link +="&"+expandmode+"=";
        var radios=document.getElementsByName("dimSelect");
        if(radios){
          for(var i=0;i<radios.length;i++){
            if(radios[i].checked){
              var value=radios[i].value;
              if(value=="multiDims"){
                //多维度
                link +=multiDim;
                //加上维度
                var dims=document.getElementsByName("dimDisplay_exp");
                link +=convertUrl(dims);
                var msus=document.getElementsByName("msuDisplay_exp");
                link +=convertUrl(msus);
              }else{
                //单维度
                link +=singleDim;
                link +="&dim_"+value+"_order=0"
                link +="&dim_"+value+"_display=true"
                var msus=document.getElementsByName("msuDisplay_exp");
                link +=convertUrl(msus);
              }
            }
          }
          //如果是单维度，则只要传此维度的标识既可
          //如果是多维度，则传多个维度的值
        }
      }else{
        link +=dig;
        var dims=document.getElementsByName("dimDisplay_dig");
        link +=convertUrl(dims);
        var msus=document.getElementsByName("msuDisplay_dig");
        link +=convertUrl(msus);
      }
    }else{
      link +=dig;
      var dims=document.getElementsByName("dimDisplay_dig");
      link +=convertUrl(dims);
      var msus=document.getElementsByName("msuDisplay_dig");
      link +=convertUrl(msus);
    }
    //下面开始生成所有选中的维度和指标列表
    //仅生成dim_dimId_order=index形式
    hideModalDialog();
    //保存用户的新的URL
    var customUrl=document.getElementById("customUrl");
    if(customUrl){
    	customUrl.value=link;
    }
    loadNewContent(link);
  }
  //生成部分URL
  function convertUrl(objs){
    var link="";
    if(objs){
      for(var i=0;i<objs.length;i++){
        var e=objs[i];
        if(e.checked){
          //用户选择了
          var value=e.value;
          var values=value.split("_");
          if(values.length>=4){
            link +="&"+values[0]+"_"+values[1]+"_order="+values[3];
            link +="&"+values[0]+"_"+values[1]+"_display=true";
          }
        }else{
          var value=e.value;
          var values=value.split("_");
          if(values.length>=4){
            link +="&"+values[0]+"_"+values[1]+"_order="+values[3];
            link +="&"+values[0]+"_"+values[1]+"_display=false";
          }
        }
      }
    }
    return link;
  }



  //用户选择日期或月份时提交新的
	function checkUserSelect()
  {
	var flag=false;
    var startDateObj=document.getElementById("start_date");
    var endDateObj=document.getElementById("end_date");
    if(startDateObj && endDateObj)
    {
		  if(eval(startDateObj.value)>eval(endDateObj.value))
		  {
			 alert("您选择起始日期大于截至日期，请重新选择");
			 startDateObj.focus();
			 return false;
	}
    var preLinkObj=document.getElementById("preUrl");
    if(preLinkObj)
    {
        var preLink=preLinkObj.value;
        var pos=preLink.indexOf("&<%=RptOlapConsts.REQ_START_DATE%>=");
        var end;
        if(pos>=0)
        {
          end=preLink.indexOf("&",pos+1);
          if(end>=0)
          {
            preLink=preLink.substring(0,pos)+preLink.substring(end);
          }
          else
          {
            preLink=preLink.substring(0,pos);
          }
        }
        pos=preLink.indexOf("&<%=RptOlapConsts.REQ_END_DATE%>=");
        if(pos>=0)
        {
          end=preLink.indexOf("&",pos+1);
          if(end>=0)
          {
            preLink=preLink.substring(0,pos)+preLink.substring(end);
          }
          else
          {
            preLink=preLink.substring(0,pos);
          }
        }
          //补上新的
          preLink +="&<%=RptOlapConsts.REQ_START_DATE%>="+startDateObj.value;
          preLink +="&<%=RptOlapConsts.REQ_END_DATE%>="+endDateObj.value;
          if(preLink.indexOf("<%=RptOlapConsts.REQ_EXPAND_LEVEL%>")>=0){
          //如果是扩展链接时，此时重新从第一级开始
          	var customUrl=document.getElementById("customUrl");
          	if(customUrl){
          		preLink=customUrl.value;
          		preLink +="&<%=RptOlapConsts.REQ_START_DATE%>="+startDateObj.value;
          		preLink +="&<%=RptOlapConsts.REQ_END_DATE%>="+endDateObj.value;
          	}
          }
          loadNewContent(preLink);
        }
      }
	}

      function moveLeft(tdObjId){
        //首先找到
        if(tdObjId){
          //第一行的单元格移动
          var nameTd=document.getElementById("f_"+tdObjId);
          moveTableCellLeft(nameTd);
          //第二行的单元格移动
          var checkTd=document.getElementById("s_"+tdObjId);
          var child=checkTd.lastChild;
          var checked=false;
          if(child && child.checked)
            checked=true;
          var success=moveTableCellLeft(checkTd);
          //此持需要将选择框的值的最后的数字减一
          if(success)
          {
            //移动成功，减一
            inputValueUpdate(tdObjId,false);
            //还有看看是不是选中了,然后还得恢复
            child.checked=checked;
          }
          //第三行的单元格移动
          var moveTd=document.getElementById("t_"+tdObjId);
          moveTableCellLeft(moveTd);
        }
      }
      function moveRight(tdObjId){
        if(tdObjId){
          //第一行的单元格移动
          var nameTd=document.getElementById("f_"+tdObjId);
          moveTableCellRight(nameTd);
          //第二行的单元格移动
          var checkTd=document.getElementById("s_"+tdObjId);
          var child=checkTd.lastChild;
          var checked=false;
          if(child && child.checked)
            checked=true;
          var success=moveTableCellRight(checkTd);
          //此持需要将选择框的值的最后的数字减一
          if(success){
            inputValueUpdate(tdObjId,true);
            child.checked=checked;
          }
        //第三行的单元格移动
        var moveTd=document.getElementById("t_"+tdObjId);
        moveTableCellRight(moveTd);
      }
    }
     function moveUp(objId){
      if(objId){
          //找到上一行
          var curTr=document.getElementById("tr_"+objId);
          if(curTr){
            var preTr=curTr.previousSibling;
            var preTrId=preTr.id;
            var curTrId=curTr.id;
            if(preTr && preTrId){
              //先将行标识换过来
              preTr.id=curTrId;
              curTr.id=preTrId;
              var curHtml;
              var preHtml;
              //将所有的ID替换
              var curObjId=objId;
              var preObjId=preTrId;
              var pos=preObjId.indexOf("tr_");
              if(pos>=0)
                preObjId=preObjId.substring(pos+3);
              //由于不让直接写tr的innerHTML，循环其子节点
              var curChildren=curTr.childNodes;
              var preChildren=preTr.childNodes;
              for(var i=1;i<curChildren.length;i++){
                preHtml=preChildren[i].innerHTML;
                curHtml=curChildren[i].innerHTML;
                preHtml=preHtml.replace(/preObjId/g,curObjId);
                curHtml=curHtml.replace(/curObjId/g,preObjId);
                if(i==1){
                  //需要空格交替
                  //下面的减,这样保证只去掉两个空格
                  curHtml =curHtml.replace("&nbsp;&nbsp;","");
                  //上面的加
                  preHtml ="&nbsp;&nbsp;"+preHtml;
                }
                preChildren[i].innerHTML=curHtml;
                curChildren[i].innerHTML=preHtml;
                 if(i==2){
                  //将checkbox的值替换
                  //下面的减1
                  var curChild=curChildren[i].lastChild;
                  var value=curChild.value;
                  value=value.replace(preObjId+"_","");
                  value=eval(value);
                  value++;
                  curChild.value=preObjId+"_"+value
                  //上面的加1
                  var preChild=preChildren[i].lastChild;
                  value=preChild.value;
                  value=value.replace(curObjId+"_","");
                  value=eval(value);
                  value--;
                  preChild.value=curObjId+"_"+value
                }
              }
            }
          }
      }
     }
     function moveDown(objId){
      if(objId){
          //找到上一行
          var curTr=document.getElementById("tr_"+objId);
          if(curTr){
            var nxtTr=curTr.nextSibling;
            var nxtTrId=nxtTr.id;
            var curTrId=curTr.id;
            if(nxtTr && nxtTrId){
              //先将行标识换过来
              nxtTr.id=curTrId;
              curTr.id=nxtTrId;
              var curHtml;
              var nxtHtml;
              //将所有的ID替换
              var curObjId=objId;
              var nxtObjId=nxtTrId;
              var pos=nxtObjId.indexOf("tr_");
              if(pos>=0)
                nxtObjId=nxtObjId.substring(pos+3);
              //由于不让直接写tr的innerHTML，循环其子节点
              var curChildren=curTr.childNodes;
              var nxtChildren=nxtTr.childNodes;
              for(var i=1;i<curChildren.length;i++){
                nxtHtml=nxtChildren[i].innerHTML;
                curHtml=curChildren[i].innerHTML;
                nxtHtml=nxtHtml.replace(/nxtObjId/g,curObjId);
                curHtml=curHtml.replace(/curObjId/g,nxtObjId);
                if(i==1){
                  //需要空格交替
                  //下面的减,这样保证只去掉两个空格
                  nxtHtml =nxtHtml.replace("&nbsp;&nbsp;","");
                  //上面的加
                  curHtml ="&nbsp;&nbsp;"+curHtml;
                }
                nxtChildren[i].innerHTML=curHtml;
                curChildren[i].innerHTML=nxtHtml;
                 if(i==2){
                  //将checkbox的值替换
                  //下面的减1
                  var curChild=curChildren[i].lastChild;
                  var value=curChild.value;
                  value=value.replace(nxtObjId+"_","");
                  value=eval(value);
                  value--;
                  curChild.value=nxtObjId+"_"+value
                  //上面的加1
                  var nxtChild=nxtChildren[i].lastChild;
                  value=nxtChild.value;
                  value=value.replace(curObjId+"_","");
                  value=eval(value);
                  value++;
                  nxtChild.value=curObjId+"_"+value
                }
              }
            }
          }
      }
     }
    //将表格的单元格向左移动
    function moveTableCellLeft(tdObj){
      var success=false;
      if(tdObj){
          var previousNode=tdObj.previousSibling;
          if(previousNode){
            //有是看
            var preId=previousNode.id;
            if(preId){
              //如果有，说明它的左边还有,则可以左移
              var parent=previousNode.parentNode;
              if(parent){
                parent.removeChild(tdObj);
                parent.insertBefore(tdObj,previousNode);
                success=true;
              }
            }
          }
        }
        return success;
    }
     //将表格的单元格向右移动
    function moveTableCellRight(tdObj){
      var success=false;
      if(tdObj){
          var afterNode=tdObj.nextSibling;
          //看看还有没有右边的节点
          if(afterNode){
            //有
            var parent=afterNode.parentNode;
            if(parent){
               parent.removeChild(tdObj);
               insertAfter(parent,tdObj,afterNode);
               //前一个内容减一
               success=true;
            }
          }
       }
       return success;
    }

    //将DOM节点插入某个节点之后
    function insertAfter(parent, node, referenceNode) {
      if(referenceNode.nextSibling) {
          parent.insertBefore(node, referenceNode.nextSibling);
      } else {
          parent.appendChild(node);
      }
    }
    //将单元格的值的最后部分减一或加一
    function inputValueUpdate(tdObjId,add){
      if(tdObjId){
        //查找第二行的单元格对象
        var tdObj=document.getElementById("s_"+tdObjId);
        var preNode=tdObj.previousSibling;
        var nextNode=tdObj.nextSibling;
        if(tdObj){
          var child=tdObj.lastChild;
          if(child){
            var value=child.value;
            value=value.replace(tdObjId+"_","");
            value=eval(value);
            if(add){
              value++;
            }else{
              value--;
            }
            child.value=tdObjId+"_"+value;
          }
          //
          if(add){
            //增加了，找前面的
            if(preNode){
              child=preNode.lastChild;
              var nodeId=preNode.id;
              nodeId=nodeId.substring(nodeId.indexOf("_")+1);
              var value=child.value;
              value=value.replace(nodeId+"_","");
              value=eval(value);
              value--;
              child.value=nodeId+"_"+value;
            }
          }else{
            if(nextNode){
              child=nextNode.lastChild;
              var nodeId=nextNode.id;
              nodeId=nodeId.substring(nodeId.indexOf("_")+1);
              var value=child.value;
              value=value.replace(nodeId+"_","");
              value=eval(value);
              value++;
              child.value=nodeId+"_"+value;
            }
          }
        }
      }
    }
  //将选择框的选择状态重新选中
  function restorSelection(selection){
    if(selection){
      for(var i=0;i<selection.length;i++){
        var e=document.getElementById(selection[i]);
        if(e){
          e.checked=true;
        }
      }
    }
  }
  function display(objId,div1,div2){
    if(objId){
      var obj=document.getElementById(objId);
      obj.style.display="block";
      if(div1){
        obj=document.getElementById(div1);
        if(obj){
          obj.style.display="block";
        }
      }
      if(div2){
        obj=document.getElementById(div2);
        if(obj){
          obj.style.display="block";
        }
      }
    }
  }
  function hide(objId,div1,div2){
    if(objId){
      var obj=document.getElementById(objId);
      obj.style.display="none";

       if(div1){
        obj=document.getElementById(div1);
        if(obj){
          obj.style.display="none";
        }
      }
      if(div2){
        obj=document.getElementById(div2);
        if(obj){
          obj.style.display="none";
        }
      }
    }
  }
  function saveCusReport(){
    var eCusRptName=document.getElementById("cusRptName");
    if(eCusRptName){
      var report_name=eCusRptName.value;
      var tmpName=report_name+"::";
      //判断一下是否有重名，这里不用AJAX了
      if(userReportNames.indexOf(tmpName)>=0)
      {
        alert("您已经保存过相同名称的分析型报表\n为了便于区分，请重新进行命名");
        return false;
      }
      var link=saveCusRpt+reportId;
      link +="&report_name="+report_name;
      loadNewContent(link);
      hideModalDialog();
    }
  }
  function hideModalDialog(){
	  $.unblockUI();
  }
  function validateCustom(){
  var expandObj=document.getElementById("expand");
    if(expandObj){
      if(expandObj.checked){
        //扩展方式
        var msus=document.getElementsByName("msuDisplay_exp");
        var selected=hasSelect(msus);
        if(!selected)
          return selected;
        selected=false;
        var radios=document.getElementsByName("dimSelect");
        for(var i=0;i<radios.length;i++){
           if(radios[i].checked){
              var value=radios[i].value;
              if(value=="multiDims"){
                //多维度
                //加上维度
                var dims=document.getElementsByName("dimDisplay_exp");
                var selected=hasSelect(dims);
                break;
              }else{
                selected=true;
              }
           }
        }
        return selected;
      }
      else{
        //钻取方式
        var dims=document.getElementsByName("dimDisplay_dig");
        var selected=hasSelect(dims);
        if(!selected)
          return selected;
        var msus=document.getElementsByName("msuDisplay_dig");
        selected=hasSelect(msus);
        return selected;
      }
    }
    return true;
  }
  function hasSelect(objs){
    var has=false;
    if(objs){
      for(var i=0;i<objs.length;i++){
        if(objs[i].checked){
          has=true;
          break;
        }
      }
    }
    return has;
  }
  function blank(){
	  return false;
  }
  function changeImage(imgId,imgSrc){
    if(imgId && imgSrc){
      if(imgId!=restoreImage){
        var imgObj=document.getElementById(restoreImage);
        if(imgObj){
          imgObj.src=restroeImageSrc;
        }
      }
      var imgObj=document.getElementById(imgId);
      if(imgObj)
      {
        restoreImage=imgId;
        restroeImageSrc=imgObj.src;
        restroeImageSrc=restroeImageSrc.replace("_over","");
        //alert(restroeImageSrc);
        imgObj.src=imgSrc;
      }
    }
  }
//-->
</SCRIPT>
<script type="text/javascript">
        domHover(".btn3", "btn3_hover");
</script>


</body>
</html>
<script type="text/javascript">

	//用户定制功能
	function displayCustom(){
		//先重新刷一次页面； loadOlapTable.rptdo?report_id=9904704&SysMenuCode=904919
		 $.blockUI({
			 message:$('#dlgCustom'),
	         css: {
	             top:  ($(window).height() - $('#dlgCustom').height()) /2 + 'px',
	             left: ($(window).width() - 660) /2 + 'px',
	             //height: '50px'
	             cursor : 'default',
	        	 width : '660px'

	         }
	     });
		 setUserCustom();
	}


	/*
	** 保存用户的定制
	*/
	function saveUserCustom(){
		//检查用户是否选择一个维度和指标
		if(!validateCustom()){
			alert("您须至少选择一个维度和指标!");
			return false;
		}
		//将用户信息发送过去保存
		var  olapMode=$("#dig").attr("checked")?"<%=RptOlapConsts.CUSTOM_DISPLAY_DIG%>":"<%=RptOlapConsts.CUSTOM_DISPLAY_EXPAND%>";
		//获取维度

		//获取指标
		var customMsus = [];
		var customDims = [];
		if(olapMode == "<%=RptOlapConsts.CUSTOM_DISPLAY_DIG%>"){
			$("input[name=dimDisplay_dig]").each(function() {
				if($(this).attr("checked")){
					customDims.push($(this).val());
				}
			});
			$("input[name=msuDisplay_dig]").each(function() {
				if($(this).attr("checked")){
					customMsus.push($(this).val());
				}
			});
		}else{
			//这里维度不太一样
			$("input[name=dimSelect]").each(function() {
				if($(this).attr("checked")){
					if($(this).val() == "multiDims"){
						$("input[name=dimDisplay_exp]").each(function() {
							if($(this).attr("checked")){
								customDims.push($(this).val());
							}
						});
					}else{
						customDims.push($(this).val());
					}
				}
			});
			$("input[name=msuDisplay_exp]").each(function() {
				if($(this).attr("checked")){
					customMsus.push($(this).val());
				}
			});
		}
		customMsus = customMsus.join(",");
		customDims = customDims.join(",");
		var url="loadOlapUserTable.rptdo?actionType=save&reportId=<%=reportId%>&displayMode="+olapMode;
		//alert(url);
		jQuery.post(url,{"userDim":customDims,"userMsu":customMsus},saveSuccess,"text");
	}

	function saveSuccess(data){
		if(data && data == "Success"){
			alert("保存定制信息成功，请单击菜单再次进入此页面，定制生效！");
		}else{
			alert("保存定制信息失败，请联系系统管理员！");
		}

	}
	//展示客户定制内容。
	function setUserCustom()
	{
		if(displayMode =="<%=RptOlapConsts.RESET_MODE_DIG%>"){
			$("#expand").removeAttr("checked");
			$("#dig").attr("checked",true);
			hide('expandDiv','Layer3');
			display('digDiv','Layer1','Layer2');
		}else{
			$("#dig").removeAttr("checked");
			$("#expand").attr("checked","checked");
			hide('digDiv','Layer1','Layer2');
			display('expandDiv','Layer3');
		}
		var multiDimDisplay ="<%=multiDimDisplay%>" =="true"?true:false;
		var singleDisplayId="<%=singleDisplayId%>";

		$("input[name=dimSelect]").each(function() {
			if(multiDimDisplay){
				if($(this).attr("id") == singleDisplayId){
					$(this).attr("checked",true);
				}else{
					$(this).removeAttr("checked");
				}
			}else{
				if($(this).attr("id") == "dim_"+singleDisplayId){
					$(this).attr("checked",true);
				}else{
					$(this).removeAttr("checked");
				}
			}
		});
	}

	function getScrollBarWidth (){
		  document.body.style.overflow = 'hidden';
		  var width = document.body.clientWidth;
		  document.body.style.overflow = 'scroll';
		  width -= document.body.clientWidth;
		  if(!width) width = document.body.offsetWidth - document.body.clientWidth;
		  document.body.style.overflow = '';
		  return width;
	}
    function getScrollerWidth() {
        var scr = null;
        var inn = null;
        var wNoScroll = 0;
        var wScroll = 0;

        // Outer scrolling div
        scr = document.createElement('div');
        scr.style.position = 'absolute';
        scr.style.top = '-1000px';
        scr.style.left = '-1000px';
        scr.style.width = '100px';
        scr.style.height = '50px';
        // Start with no scrollbar
        scr.style.overflow = 'hidden';

        // Inner content div
        inn = document.createElement('div');
        inn.style.width = '100%';
        inn.style.height = '200px';

        // Put the inner div in the scrolling div
        scr.appendChild(inn);
        // Append the scrolling div to the doc

        document.body.appendChild(scr);

        // Width of the inner div sans scrollbar
        wNoScroll = inn.offsetWidth;
        // Add the scrollbar
        scr.style.overflow = 'auto';
        // Width of the inner div width scrollbar
        wScroll = inn.offsetWidth;

        // Remove the scrolling div from the doc
        document.body.removeChild(
        document.body.lastChild);

        // Pixel width of the scroller
        return (wNoScroll - wScroll);
    }


	var scrollBarWidth;
	if(!scrollBarWidth){
		scrollBarWidth = getScrollBarWidth();
		if(scrollBarWidth ==0){
			scrollBarWidth = getScrollerWidth();
		}
		if(scrollBarWidth ==0){
			scrollBarWidth = 17;
		}
	}
</script>
