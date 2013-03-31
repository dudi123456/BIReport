<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page
	import="java.util.*,com.ailk.bi.base.util.CodeParamUtil,com.ailk.bi.base.struct.LeaderQryStruct,com.ailk.bi.leader.struct.LeaderKpiInfoStruct,com.ailk.bi.base.util.WebKeys,com.ailk.bi.base.util.CommTool,com.ailk.bi.base.struct.UserCtlRegionStruct,com.ailk.bi.base.util.WebConstKeys,com.ailk.bi.leader.util.*"%>
<%
	//当前分析指标
	LeaderKpiInfoStruct kpiStruct = null;
	String msu_id = CommTool.getParameterGB(request, "msu_id");
	if (msu_id == null || "".equals(msu_id)) {
		kpiStruct = (LeaderKpiInfoStruct) session
				.getAttribute(WebKeys.ATTR_LEADER_KPI_INFO_STRUCT_FIRST);
		msu_id = kpiStruct.msu_id;
	} else {
		kpiStruct = LeaderKpiUtil.getKpiInfoStruct(msu_id);
	}
	if (kpiStruct == null) {
		kpiStruct = new LeaderKpiInfoStruct();
	}
	LeaderQryStruct qryStruct = (LeaderQryStruct) session
			.getAttribute(WebKeys.ATTR_ailk_QRY_STRUCT);
	//
	String[][] views = LeaderKpiUtil.getViewByGuide(msu_id);
	int screenx = Integer.parseInt(session
			.getAttribute(WebKeys.Screenx) == null
			? "1024"
			: (String) session.getAttribute(WebKeys.Screenx));
	int screeny = Integer.parseInt(session
			.getAttribute(WebKeys.Screeny) == null
			? "768"
			: (String) session.getAttribute(WebKeys.Screeny));
	if (qryStruct == null) {
		qryStruct = new LeaderQryStruct();
	}
	String[][] rptInfos = LeaderKpiUtil
			.getRptInfoByRptBelong("LEADERVIEW");
	String rpt_id = "";
	String rpt_name = "";
	if (rptInfos != null) {
		rpt_id = rptInfos[0][0];
		rpt_name = rptInfos[0][1];
	}
	UserCtlRegionStruct regionStruct = (UserCtlRegionStruct) session
			.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);

	//查询结构
	ReportQryStruct qryStructRpt = (ReportQryStruct) session
			.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
	if (qryStructRpt == null) {
		qryStructRpt = new ReportQryStruct();
	}
	qryStructRpt.gather_month = qryStruct.gather_mon;
	qryStructRpt.gather_day = qryStruct.gather_day;
	qryStructRpt.dim1 = msu_id;
%>
<script language="javascript" type="text/javascript"
	src="js/WdatePicker.js"></script>
<script type="text/javascript">
  function myClick()
  {
	  parent.window.location.href="getmap.rptdo?mapid=quanguo";
   }
  function isExist(cityid,ids)
  {
	  var arr = ids.split(",");
	  for(var i=0;i<arr.length;i++)
	  {
		  if(cityid==arr[i])
		  {
		  	return true;
		  }
	  }
	  return false;
  }
	function init(city_id,ids,mapName)
	{

		if(typeof(ids)=="undefined")
		{
			 ids="";
		}
		var flag = isExist(city_id,ids);
		if(!flag){
			if('89'==city_id)
			{
				city_id = '';
				parent.window.location.href="home/getmap.rptdo?mapid=xinjiang";
				//parent.refresh("xinjiang");
			}
			if(mapName=="全国")
			{
				//parent.window.location.href="home/leaderViewAction.rptdo?mapid="+mapid;
				//window.location.href="home/getmap.rptdo?mapid=xinjiang";
			}
			mapClick(city_id);
		}


	}
	//输出测试，通过v传递flash变量
	function jsAlert(v) {
		alert(v);
	}
	//调用flash中的方法，"my_mv"为html页中swf的id
	function callExternal() {
		var num3  = document.getElementById("SearchTime").value;
		thisMovie("my_mv").flAlert(num3);
	}
	//搭建js与flash互通的环境
	function thisMovie(movieName) {
		if (navigator.appName.indexOf("Microsoft") != -1) {
			return window[movieName]
		}else{
			return document[movieName]
		}
	}
	  </script>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/other/newmain.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/other/css.css">
<script type="text/javascript"
	src='<%=request.getContextPath()%>/dwr/engine.js'></script>
<script type="text/javascript"
	src='<%=request.getContextPath()%>/dwr/interface/LeaderKpiUtil.js'></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/js/FusionMaps/FusionMaps.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/js/FusionMaps/FusionCharts.js"></script>
<script type='text/javascript'>
        var last_contents = "1";
        var IsShowView2 = 0;
        var IsShowView3 = 0;
        var IsShowView4 = 0;
        var IsShowView5 = 0;
        function menuChanger(contentID, viewID)
        {
            //document.getElementById("menu_" + last_contents).className = "tab2";
            //document.getElementById("menu_" + contentID).className = "tab_act2";
            document.getElementById("content_" + last_contents).style.display = "none";
            document.getElementById("content_" + contentID).style.display = "block";
            last_contents = contentID;
            var kpiStruct = {
                "msu_id":"<%=msu_id%>",
                "msu_name":"<%=kpiStruct.msu_name%>",
                "unit":"<%=kpiStruct.unit%>",
                "digit":"<%=kpiStruct.digit%>"
            }
            var qryStruct = {
                "data_cycle":"<%=qryStruct.data_cycle%>",
                "gather_mon":"<%=qryStruct.gather_mon%>",
                "gather_day":"<%=qryStruct.gather_day%>",
                "cust_group_id":"<%=qryStruct.cust_group_id%>",
                "city_id":"<%=qryStruct.city_id%>",
                "data_source":"<%=qryStruct.data_source%>"
            }
            if (viewID == 'VIEW002' && IsShowView2 == 0)
            {
                //ShowWait();
                IsShowView2 = 1;
                //closeWaitWin();
            }
            if (viewID == 'VIEW003' && IsShowView3 == 0)
            {
                //ShowWait();
                IsShowView3 = 1;
                //closeWaitWin();
            }
            if (viewID == 'VIEW004' && IsShowView4 == 0)
            {
                //ShowWait();
                IsShowView4 = 1;
                //closeWaitWin();
            }
            if (viewID == 'VIEW005' && IsShowView5 == 0)
            {
                //ShowWait();
                IsShowView5 = 1;
                document.frames["RPTLXJ001"].resizeTable();
                document.frames["RPTLXJ001"].loadNewContent('SubjectCommTable.rptdo?table_id=<%=rpt_id%>&table_func=5&dim_level_1=0&dim_level_2=0&msu_level_3=0&msu_level_4=0&sort_index=5&sort_data_type=1&sort_order=1');
                //closeWaitWin();
            }
        }

        function mapClick(id) {
            var qryStruct = {
                "data_cycle":"<%=qryStruct.data_cycle%>",
                "gather_mon":"<%=qryStruct.gather_mon%>",
                "gather_day":"<%=qryStruct.gather_day%>",
                "cust_group_id":"<%=qryStruct.cust_group_id%>",
                "group_type":"<%=qryStruct.group_type%>",
                "city_id":id,
                "data_source":"<%=qryStruct.data_source%>"
            };
            var regionStruct = {
                "ctl_lvl":"<%=regionStruct.ctl_lvl%>",
                "ctl_in_or_equals":"<%=regionStruct.ctl_in_or_equals%>",
                "ctl_city_str":"<%=regionStruct.ctl_city_str%>",
                "ctl_city_str_add":"<%=regionStruct.ctl_city_str_add%>"
            };
            var area;

            LeaderKpiUtil.findKpiInfoStructByCityId(qryStruct, regionStruct,"<%=request.getContextPath()%>", function(des) {
                    <%HashMap map = CodeParamUtil
					.codeListParamFetcher(request, "CITY_ID");
			if (map != null) {
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					//out.println("case '" + entry.getKey() + "': area = '" + entry.getValue() + "';break;");
					out.println("if(id=='" + entry.getKey() + "'){area = '"
							+ entry.getValue() + "';}");
				}
			}

			out.println("if(id==''){area = '新疆';}");%>
                parent.document.getElementById("area").innerHTML = area;
                parent.document.getElementById("area2").innerHTML = "区域："+area;
                if (des != null) {
                	//alert(des);
                    parent.document.getElementById("jysd").innerHTML = des;
                }
                parent.document.getElementById("leaderChart").src = "<%=request.getContextPath()%>/leader/leaderchart.jsp?msu_id=<%=msu_id%>&oper_type=trend&city_id=" + id + "&area=" + encodeURIComponent(encodeURIComponent(area));
            });
        }

		function openLeadSmsWin() {
           var h = "500";
var w = "650";
var top=(screen.availHeight-h)/2;
var left=(screen.availWidth-w)/2;
  var optstr = "height=" + h +",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
  var strUrl = "leaderSMSView.rptdo";
  window.open(strUrl,"",optstr);
  var mapXml = "";
        }
    </script>
<title>领导看板</title>
</head>
<body scroll="no" onload="menuChanger(1, 'VIEW001');">
	<table style="width: 100%; height: 100%" border="0" cellspacing="0"
		cellpadding="0">

		<%
			String viewid = "";
			String viewname = "";
			if (views != null && views.length > 0) {
				for (int i = 0; i < views.length; i++) {
					viewid = views[i][0];
					viewname = views[i][1];
					//out.println("<td id='menu_" + (i + 1) + "' height=1>1</td><td width='2' height=1>2</td>");
				}
			}
		%>


		<tr>
			<td valign="top" align="left" width="<%=screenx / 2 - 20%>"
				height="510">
				<%
					if (views != null && views.length > 0) {
						for (int i = 0; i < views.length; i++) {
							viewid = views[i][0];
							//Flash地图
							if ("VIEW001".equalsIgnoreCase(viewid)) {
								qryStruct.msu_id = kpiStruct.msu_id;
								qryStruct.msu_name = kpiStruct.msu_name;
				%>
				<div id="content_<%=(i + 1)%>" valign="top" align="left"
					style="padding: 0px; width: 600px; height: 100%; overflow-x: hidden; overflow-y: hidden; cursor: default; display: ;">
					<table border="1"
						style="width: 100%; height: 100%; margin-top: 0px; color: #106287"
						cellspacing="0" cellpadding="0">
						<tr>
							<td valign='top' height="100%"><br>
								 <%=String.valueOf(session.getAttribute("mv"))%></td>
						</tr>
					</table>
				</div> <%
 	continue;
 			}
 			// 分析
 			if ("VIEW002".equalsIgnoreCase(viewid)) {
 				out.println("<div valign='top' align='left' id='content_"
 						+ (i + 1)
 						+ "' style=' width: 100%; height:100%;  overflow: auto; cursor: default; display: none;'>");
 				out.println("</div>");
 				continue;
 			}
 			// 分析
 			if ("VIEW003".equalsIgnoreCase(viewid)) {
 				out.println("<div valign='top' align='left'  id='content_"
 						+ (i + 1)
 						+ "' style='width: 100%;  height:100%;  overflow: auto; cursor: default; display: none;'>");
 				out.println("</div>");
 				continue;
 			}
 			// 分析
 			if ("VIEW004".equalsIgnoreCase(viewid)) {
 				out.println("<div valign='top' align='left'  id='content_"
 						+ (i + 1)
 						+ "' style=' overflow: auto; width: 100%; height:100%; cursor: default; display: none;'>");
 				out.println("</div>");
 				continue;
 			}
 			// 领导报表看板
 			if ("VIEW005".equalsIgnoreCase(viewid)) {
 				out.println("<div  valign='top' align='left'  id='content_"
 						+ (i + 1)
 						+ "' style=' width: 100%;  overflow: auto; height:100%; cursor: default; display: none;'>");
 				out.println("<table style=\"width: 100%\">");
 				out.println("<tr><td height='30' valign='middle' align='center'>");
 				out.println("报表选择：<select name=\"rpt_select\" style=\"width: 180px\" onchange=\"selChangeReport(this.value)\">");
 				for (int j = 0; rptInfos != null && j < rptInfos.length; j++) {
 					out.println("<option value='" + rptInfos[j][0]
 							+ "|" + rptInfos[j][1] + "'>"
 							+ rptInfos[j][1] + "</option>");
 				}
 				out.println("</select>");
 				out.println("</td></tr></table>\n");

 				out.println("<table width=\"100%\">");
 				out.println("<tr>");
 				out.println("<td class=\"newContent newlist\" width=\"100%\">");
 %> <iframe name="RPTLXJ001" id="RPTLXJ001" width="100%" height="240"
					src="SubjectCommTableDefaultOrder.rptdo?table_id=<%=rpt_id%>&first=Y&table_height=240&align_table=Y&table_func=5&dim_level_1=0&dim_level_2=0&msu_level_3=0&msu_level_4=0&sort_index=5&sort_data_type=1&sort_order=1"
					frameborder="0" scrolling="no"></iframe> <%
 	out.println("</td></tr>");
 				out.println("</td><td height='20' align='right'> <a class='a1' href='javascript:openWin();' >更多>></a>&nbsp;&nbsp;</td></tr>");
 				out.println("<tr><td class=\"newContent newlist\" width=\"100%\">");
 %> <iframe id="chart_RPTLXJ001" scrolling="no" width="100%"
					height="250" border="0" frameborder="0" marginwidth="0"
					marginheight="0"
					src="SubjectCommChart.screen?chart_id=<%=rpt_id%>_CHART&chart_name_r=<%=rpt_name%>&first=Y"></iframe>
				<%
					out.println("</td>");
								out.println("</tr>");
								out.println("</table>\n");
								out.println("</div>");
								continue;
							}
						}
					}
				%>
			</td>
		</tr>

	</table>
</body>
</html>
<script language="javascript">
function changeReport(rpt_id,rpt_name){
	var tableframe = "RPTLXJ001";
	var link="SubjectCommTable.rptdo?table_id="+rpt_id+"&first=Y&table_height=285";
	target=eval(tableframe);
  	target.location=link;

	var chartframe = "chart_RPTLXJ001";
	link="SubjectCommChart.screen?chart_id="+rpt_id+"_CHART&chart_name_r="+rpt_name+"&first=Y";
	target=eval(chartframe);
  	target.location=link;

    //link="SubjectCommTable.rptdo?table_id="+rpt_id+"&table_func=5&dim_level_1=0&dim_level_2=0&msu_level_3=0&msu_level_4=0&sort_index=5&sort_data_type=1&sort_order=1";
	//target=eval(tableframe);
  	//target.location=link;
}

function selChangeReport(rpt){
	var rpt_id = rpt.split("|")[0];
	var rpt_name = rpt.split("|")[1];
	var tableframe = "RPTLXJ001";
	var link="SubjectCommTableDefaultOrder.rptdo?table_id="+rpt_id+"&first=Y&table_height=240&align_table=Y&table_func=5&dim_level_1=0&dim_level_2=0&msu_level_3=0&msu_level_4=0&sort_index=5&sort_data_type=1&sort_order=1";
	target=eval(tableframe);
  	target.location=link;

	var chartframe = "chart_RPTLXJ001";
	link="SubjectCommChart.screen?chart_id="+rpt_id+"_CHART&chart_name_r="+rpt_name+"&first=Y";
	target=eval(chartframe);
  	target.location=link;
}

function openWin(){
	  var rpt = document.getElementById("rpt_select").value;
	  var rpt_id = rpt.split("|")[0];
	  var rpt_name = rpt.split("|")[1];
	  rpt_id = rpt_id.substring(0,rpt_id.indexOf('XJ'))+'ALL'+ rpt_id.substring(rpt_id.indexOf('XJ'));
	  var optstr = "height=400,width=600,left=200,top=100,status=no,toolbar=no,menubar=no,location=no,scrollbars=auto,resizable=yes"
	  window.open("SubjectCommTableDefaultOrder.rptdo?table_id="+rpt_id+"&first=Y&table_height=400&align_table=Y&table_func=5&dim_level_1=0&dim_level_2=0&msu_level_3=0&msu_level_4=0&sort_index=5&sort_data_type=1&sort_order=1","prechart",optstr);
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
