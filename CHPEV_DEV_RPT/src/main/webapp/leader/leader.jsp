<%@page import="com.ailk.bi.SysConsts"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%@ page import="com.ailk.bi.base.common.CSysCode,com.ailk.bi.base.struct.LeaderQryStruct,com.ailk.bi.leader.struct.LeaderKpiInfoStruct,com.ailk.bi.leader.util.LeaderKpiUtil,com.ailk.bi.base.util.WebKeys,com.ailk.bi.base.struct.*,com.ailk.bi.base.util.*" %>
<%
	//屏幕宽高
    int screenx = Integer.parseInt(session.getAttribute(WebKeys.Screenx) == null ? "1024" : (String) session.getAttribute(WebKeys.Screenx));
    int screeny = Integer.parseInt(session.getAttribute(WebKeys.Screeny) == null ? "768" : (String) session.getAttribute(WebKeys.Screeny));
    //指标结构数组
    LeaderKpiInfoStruct[] kpiStruct = (LeaderKpiInfoStruct[]) session.getAttribute(WebKeys.ATTR_LEADER_KPI_INFO_STRUCT);
    if (kpiStruct == null) {
        kpiStruct = new LeaderKpiInfoStruct[0];
    }
    //查询结构
    LeaderQryStruct qryStruct = (LeaderQryStruct) session.getAttribute(WebKeys.ATTR_ailk_QRY_STRUCT);
    if (qryStruct == null) {
        qryStruct = new LeaderQryStruct();
    }
    //地域名称
    String cityName = qryStruct.city_name;

    //当前分析指标
    LeaderKpiInfoStruct kpiStructChart = null;
    String msuId = request.getParameter("msu_id");
    if (StringTool.checkEmptyString(msuId)) {
        kpiStructChart = (LeaderKpiInfoStruct) session.getAttribute(WebKeys.ATTR_LEADER_KPI_INFO_STRUCT_FIRST);
    } else {
        kpiStructChart = LeaderKpiUtil.getKpiInfoStruct(msuId);
    }
    if (kpiStructChart == null) {
        kpiStructChart = new LeaderKpiInfoStruct();
    }
%>
<!DOCTYPE html>
<html>
<head>
<title><%=CSysCode.SYS_TITLE%></title>
<%@ include file="/base/commonHtml.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=context%>/css/other/condition.css">
<script src="<%=context%>/js/jquery.min.js"></script>
<script src="<%=context%>/js/jquery.bi.js"></script>
<script type="text/javascript" src="<%=context%>/js/dd99.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/chart/FusionCharts.js"></script>
<script type="text/javascript" src="<%=context%>/js/dojo.js" djConfig="parseOnLoad:true, isDebug:false"></script>
    <script type="text/javascript">
        var dlgChart;
        var djConfig = { parseWidgets: false, searchIds: ["dlgChartZoomOut"] };
        dojo.require("dojo.widget.Dialog");
        dojo.addOnLoad(function (e) {
            dlgChart = dojo.widget.byId("dlgChartZoomOut");
        });

        function ZoomOut(msu_id) {
            dlgChart.checkSize();
            dojo.io.bind({
                url: "<%=request.getContextPath() %>/leader/leaderchart.jsp",
                content: {ajax_flag:'true',msu_id: msu_id},
                handler: function(type, data, evt) {
                    if (type == 'error') {
                        alert('从服务器端读数据错!');
                    } else {
                        //alert(data);
                        //dojo.addOnLoad(function() {
                        dojo.byId("dlgChartZoomOut").innerHTML = data;
                        dlgChart.show();
                        //});
                    }
                },
                encoding: "UTF-8",
                method: "POST"//(默认方法GET)
            });
        }
        function ShowMsuInfo(msu_id,msu_name,msu_cycle) {
            dojo.byId("leaderViewFrame").src = "<%=request.getContextPath() %>/leader/leaderMapAction.rptdo?msu_id=" + msu_id +"&rand="+Math.random();
            dojo.byId("leaderChart").src = "<%=request.getContextPath() %>/leader/leaderchart.jsp?msu_id=" + msu_id + "&msu_cycle="+msu_cycle+"&oper_type=trend";
            document.getElementById("msu_name2").innerHTML = msu_name;
        }
        function CallQuery(cycle) {
            document.LeaderViewForm.qry__data_cycle.value = cycle;
            document.LeaderViewForm.qry__data_source.value = "";
            document.LeaderViewForm.action = "leaderViewAction.rptdo";
            document.LeaderViewForm.submit();
        }
        function SetCwinHeight(obj) {
            var cwin = obj;
            if (document.getElementById) {
                if (cwin && !window.opera) {
                    if (cwin.contentDocument && cwin.contentDocument.body.offsetHeight) {
                        cwin.height = cwin.contentDocument.body.offsetHeight;
                    } else if (cwin.Document && cwin.Document.body.scrollHeight) {
                        cwin.height = cwin.Document.body.scrollHeight;
                    }
                }
            }
        }
        function swtichPic(obj) {
            var flag = true;
            if (obj.src.indexOf('sign_show.gif') > 0) {
                obj.src = '<%=request.getContextPath() %>/biimages/sign_hidden.gif?' + Math.random();
                flag = false;
            }
            if (flag && obj.src.indexOf('sign_hidden.gif') > 0) {
                obj.src = '<%=request.getContextPath() %>/biimages/sign_show.gif?' + Math.random();
            }
        }
        function HighLightSelRow(e) {
            var ent = (e == null ? event.srcElement : e.target);
            if (ent.tagName.toUpperCase() != "A") {
                return;
            }
            var tr = ent.parentNode;
            if (tr.nodeName.toLowerCase() != 'tr') {
                tr = tr.parentNode;
            }
            if (this.selRow != null) {
                setTrReveal(this.selRow, "background:white");
            }
            setTrReveal(tr, "background:#fefece");
            this.selRow = tr;
        }
        function setTrReveal(tr, css) {
            for (var i = 0; i < tr.cells.length; i++) {
                tr.cells[i].style.cssText += ";" + css;
            }
        }
        function refresh(mapid){

        	ShowMsuInfo("U1001","测试信息",mapid);
        	//window.location.href="home/leaderViewAction.rptdo?mapid=1";

        }
    </script>
<BIBM:SelfRefreshTag pageNames="LeaderViewForm.qry" attrNames="<%=WebKeys.ATTR_ailk_QRY_STRUCT%>" warn="1"/>
</head>
<input type="hidden" id="mapid">
<body onLoad="selfDisp();">
<form name="LeaderViewForm" action="leaderViewAction.rptdo">
<input type="hidden" name="qry__data_cycle" value="<%=qryStruct.data_cycle%>"/>
<input type="hidden" name="qry__data_source" value="<%=qryStruct.data_source%>"/>
<input type="hidden" id="qry__group_type" name="qry__group_type" value="<%=qryStruct.group_type%>"/>
    <!-- 左侧内容 -->
    <div class="widget_place pl">
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd1">增值业务营销视点</h2>
                <span class="hr">.</span>
                <span class="wdtime"><a href="javascript:;" onclick="showTimeBox(this)">日期：<span class="menuline_2" id=gather_day><%=qryStruct.gather_day%></span></a></span>
                <span class="wdarea">区域：<span class="menuline_1" id=area><%=cityName%></span></span>
                <INPUT id=__gind971_5 type=hidden value="<%=qryStruct.gather_day%>"
                                       name="qry__gather_day" max="20200101"
                                       min="20060101" size="10">
            </div>
            <div class="widget_content">
              <div id="jysd" style="width: 100%; margin-top: 2px; overflow: auto; height:<%=screeny-500%>px;">
                <%=LeaderKpiUtil.findKpiInfoStructForJsp(kpiStruct, qryStruct, request.getContextPath())%>
              </div>
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span>
                <h2 class="icon wd2" id="msu_name2"><%=kpiStructChart.msu_name%>(<%=kpiStructChart.unit%>)</h2>
                <span class="hr">.</span><span class="wdtime" id="area2">区域：<%=cityName%></span>
            </div>
            <div class="widget_content">
                <div class="widget_bb">
                <iframe style="height: 213px; width: 100%" marginwidth="0" marginheight="0" id="leaderChart"
                        name="leaderChart" frameborder="0" src="<%=request.getContextPath() %>/leader/leaderchart.jsp?chart_id=leader_chart_day"></iframe>
                </div>
            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
    </div>
    <!-- 左侧内容结束 -->

    <!-- 右侧内容 -->
    <div class="widget_place pr">
        <div class="widget">
            <div class="widget_header">
                <span class="hl">.</span> <span id="VIEW001" class="wdtab1 sed"><a href='javascript:void(0)' onclick="leaderViewFrame.window.menuChanger(1,'VIEW001');changeStyle('VIEW001')" class="icon">地域分析</a></span>
                <span id="VIEW005" class="wdtab2"><a href='javascript:void(0)' onclick="leaderViewFrame.window.menuChanger(2,'VIEW005');changeStyle('VIEW005')" class="icon">领导报表</a></span> <span class="hr">.</span>
            </div>
            <div class="widget_content">
                <iframe style=" width: 100%; height: 540px" marginwidth="0" marginheight="0" id="leaderViewFrame"
                        onload="SetCwinHeight(this)"
                        name="leaderViewFrame" frameborder="0" src="<%=request.getContextPath() %>/leader/leaderMapAction.rptdo">
                </iframe>

            </div>
            <div class="widget_bottom">
                <span class="bl">.</span> <span class="br">.</span>
            </div>
        </div>
    </div>
    <!-- 右侧内容结束 -->

    <!-- 条件操作框 -->
    <div id="menucontent" style="background-color:#FFF7BD;position: absolute;top:0;left:0;display:none">
        <table class="popwidow" cellpadding="0" cellspacing="5">
            <tr>
                <td class="poptitle" style="width:180px">时间</td>
                <td class="poptitle" style="width:40px">操作</td>
            </tr>
            <tr>
                <%if (SysConsts.STAT_PERIOD_DAY.equals(qryStruct.data_cycle)) {%>
                <td valign="top"><%=PageConditionUtil.getDayDesc()%></td>
                <%} else if (SysConsts.STAT_PERIOD_MONTH.equals(qryStruct.data_cycle)) {%>
                <td valign="top"><%=PageConditionUtil.getMonthDesc()%></td>
                <%}%>
                <td valign="top" align="center">
                    <table style="width: 100%">
                        <tr>
                            <td align="center" height="25"><input id="button_submit" class="btn3" type="submit"  value="确认"/></td>
                        </tr>
                        <tr>
                            <td align="center" height="25"><input id="button_reset" class="btn3" type="button" value="取消" onclick="$('#menucontent').hide()"/></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <!-- 条件操作框结束 -->

    <!-- 图形放大 -->
    <div dojoType="dialog" id="dlgChartZoomOut" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250"
         style="display:block;width:1000px;height:100px; border:1px solid red;" title="data dialog">
    </div>
    <!-- 图形放大结束 -->

    <script type="text/javascript">
    	domHover(".btn3", "btn3_hover");
        $("#tablebox1 a[name='haschild']").click(function()
        {
            $(this).parent().parent().parent().next("tbody").toggle();
            $(this).toggleClass("tdclose");
        });
        $("#tablebox1 tr").hover(function()
        {
            $(this).addClass("trhover");
        }, function()
        {
            $(this).removeClass("trhover");
        });
        function showTimeBox(obj)
        {

            if ($.browser.msie)
            {
                $("#menucontent").show().css({ top: $(obj).position().top + 37, left: $(obj).position().left - 90 });
            }
            else
            {
                $("#menucontent").show().css({ top: $(obj).position().top + 25, left: $(obj).position().left - 90 });
            }
        }
        function changeStyle(obj)
        {
        	document.getElementById("VIEW001").className="wdtab2";
        	document.getElementById("VIEW005").className="wdtab2";
        	if(obj=="VIEW005"){
        		document.getElementById("VIEW005").className="wdtab1 sed";
        		var link="SubjectCommTableDefaultOrder.rptdo?table_id=RPTLXJ001&first=Y&table_height=240&align_table=Y&table_func=5&dim_level_1=0&dim_level_2=0&msu_level_3=0&msu_level_4=0&sort_index=5&sort_data_type=1&sort_order=1";
        		var chartframe = "leaderViewFrame.RPTLXJ001";
        		target=eval(chartframe);
        	  	target.location=link;
        	}
        	if(obj=="VIEW001"){
        		document.getElementById("VIEW001").className="wdtab1 sed";
        	}
        }
    </script>
</body>
</html>
