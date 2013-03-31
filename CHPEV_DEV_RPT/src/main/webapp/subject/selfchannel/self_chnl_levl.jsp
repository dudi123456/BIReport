<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.*"%>
<%@ include file="/base/commonHtml.jsp"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="Content-Language" content="zh-cn">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
		<title>渠道评级</title>
		<SCRIPT language=javascript src="<%=context%>/js/net.js"></SCRIPT>
		<SCRIPT language=javascript src="<%=context%>/js/js.js"></SCRIPT>
		<SCRIPT language=javascript src="<%=context%>/js/align_tab_by_head.js"></SCRIPT>
		<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(qryStruct == null){
			qryStruct = new ReportQryStruct();
		}
		String ChnlTypeSql = "SELECT code_id,code_name FROM ui_code_list where type_code='CHANNEL_TYPE' and status='1' order by code_seq";
		String ProvSql = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t order by t.sort_id asc";
		String CitySql = "SELECT city_code,city_name FROM dim_pub_city where province_code ='"+qryStruct.metro_id+"' and city_code != '000' order by sort_id";
		%>
		
<script>
	
	function window.onload() {
		initframe();
	}
	
	function initframe(){
		document.getElementById("table_self_chnl_T_02").src="SubjectCommTable.rptdo?table_id=self_chnl_T_02&first=Y&table_height=400";
	}
	function loadFrameByLevel() {
		
	}
	
	function doQuery() {
		SelfChnl.submit();
	}
	
/*
*   Ajax  实现下拉框联动效果.
*/
	function loadNewContent(arsType,arsValue){

    var rpcUrl=encodeURI("<%=context%>/subject/QryTools.rptdo?qryType=" + arsType + "&" + "argsCode=" + arsValue);
    var params=[];
    var pos=rpcUrl.indexOf("?");
    if(pos>=0){
      var param=rpcUrl.substring(pos+1);
      rpcUrl=rpcUrl.substring(0,pos);
      params=param.split("&");
    }
     //记录下当前的滚动条位置
    var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadTableUpdate,ajaxError);
    
    ajaxHelper.sendRequest();
  }
  //加载返回的维度值
  function loadTableUpdate(){
  	
    var jsonTxt=this.req.responseText;

    if(jsonTxt){
    	var obj = eval('('+jsonTxt+')');
    	loadJsonList("qry__city_id",obj.RESULT);
    }    
  }
  function ajaxError(){
  	alert("错误")
  	closeWaitWin();
  }
/*
**  区域联动省份函数
*/	
	function ProvChange(obj) {
		if(obj.value==""){
			var cityCodeList = document.getElementById("qry__city_id");
			cityCodeList.length=0;
			cityCodeList.options.add(new Option("全部",""));
		} else {
			loadNewContent("PROV",obj.value)
		}
	}
/*
**  将json数据 导入SELECT
*/		
	function loadJsonList(listName,jSonObj) {
		var dataList = document.getElementById(listName);
    	//清空SELECT options
    	dataList.length=0;
    	dataList.options.add(new Option("全部",""));
    	for(var i=0;i < jSonObj.length;i++)
    	{
    		dataList.options.add(new Option(jSonObj[i].argsName,jSonObj[i].argsCode));
    	} 
	}
	
/*
**得到默认账期
*/
	function getOriDate(){
		var oriDate = document.getElementById("qry__rpt_date");
		return oriDate.value
 	} 
 	
/*
**  重置按钮
*/
	function doReset() {
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		document.getElementById("qry__tactic_type").value="";
		document.getElementById("qry__gather_month").value=getOriDate();
		
		var provCodeList = document.getElementById("qry__metro_id");
		var cityCodeList = document.getElementById("qry__city_id");

		if(provCodeList.disabled == false)
			provCodeList.value="";
		if(cityCodeList.disabled == false){
			if(ctl_lvl=="0"){
				cityCodeList.length=0;
				cityCodeList.options.add(new Option("全部",""));
			}
			cityCodeList.value="";
		}
	}
/*
**  链接跳转函数.
*/	
	function open_metaExplain()
    {
    	var strUrl = "Weight.rptdo?ioid_id0=B0202";
        var newsWin = window.open(strUrl);
        if (newsWin != null) {
           	newsWin.focus();
        }
    }
</script>
	</head>
		
<body>
<FORM name="SelfChnl" action="SelfChnlEvl.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
<INPUT type=hidden id="oriDate" name="qry__rpt_date"  value="<%=qryStruct.rpt_date%>">
	<!-- 页面头,开始 -->
	<!--
	<div>
		<span >自有实体渠道评级</span>		
	</div>
	 -->
	<!-- 页面头,结束 -->
<!-- 查询条件区,开始 -->
<div class="topsearch">
	 <table width="80%"  border=0 cellpadding="0" cellspacing="0" align="center">
		<tr>
        	<td align="right"  class="td-bg">月份：</td>
            <td >
				<input id="qry__gather_month" class="Wdate" style="height:22px;" type="text" name="qry__gather_month" value="<%=qryStruct.gather_month%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>
            </td>
          <!--   
            <td align="right" class="td-bg">考核区域：</td>
            <td >
            	<select id="AreaSelect" name="qry__attach_region" onchange="AreaChange(this)" >
                   	<option value="">全部</option>
  					<option value="1">北方</option>
  					<option value="0">南方</option>
				</select>
            </td>
          -->  
            <td align="right" class="td-bg">省分：</td>
            <td >
           	<% if(qryStruct.ctl_lvl.equals("1") || qryStruct.ctl_lvl.equals("2")) {%>
				<BIBM:TagSelectList focusID="<%=qryStruct.metro_id%>" script="disabled=true onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__metro_id" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
			<%} else {%>
				<BIBM:TagSelectList focusID="<%=qryStruct.metro_id%>" script="onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__metro_id" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
			<%}%>
            </td>
            
            <td align="right" class="td-bg">地市：</td>
            <td >
            <% if(qryStruct.ctl_lvl.equals("2")) {%>
            	<BIBM:TagSelectList focusID="<%=qryStruct.city_id%>" script="disabled=true class='easyui-combobox'" listName="qry__city_id" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
            <%} else {%>
            	<BIBM:TagSelectList focusID="<%=qryStruct.city_id%>" script="class='easyui-combobox'" listName="qry__city_id" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
            <%}%>
            </td>
                        
			<td align="right" class="td-bg">渠道类型：</td>
            <td >
            	<BIBM:TagSelectList  focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry__tactic_type" listID="0" allFlag="" selfSQL="<%=ChnlTypeSql%>" />
            </td>
            
            <td><input id="button_submit" class="btn_search" type="button" value="查询" onClick="doQuery();"/></td>
            <td><input id="button_submit" class="btn3" type="button" value="重置" onClick="doReset();"/></td>
    	</tr> 
	</table>
	</div>
	<!-- 查询条件区,结束 -->
	<!-- 大柱子!!!! -->
	<div class="result_title">
		<span >自有营业厅评级</span>		
	    <div class="iw">
		   <input  id="button_submit"  class="btn_iw" type="button" value="评分指标及权重" onClick="open_metaExplain()"/>
		</div>
	</div>
	
	<!-- 图表展示区 ,开始-->
	<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
	<!-- 表格区 -->
	<tr>	
		<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="table_self_chnl_T_02" width="100%" height="415"
							src=""
					frameborder="0" scrolling="no"></iframe></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</FORM>
<!-- 图表展示区 ,结束-->
    <script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
	    domHover(".btn_iw", "btn_iw_hover");
    </script>
	</body>
</html>
