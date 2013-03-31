<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.marketing.entity.TacticInfo"%>
<%@page import="com.ailk.bi.base.util.WebConstKeys"%>
<%@taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@taglib uri="/WEB-INF/tld/TAG.tld" prefix="TAG"%>
<%@page import="java.util.*"%>
<%@ include file="/base/commonHtml.jsp"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="Content-Language" content="zh-cn">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/other/newmain.css">
		<title>自有渠道明细</title>
		<SCRIPT language=javascript src="<%=context%>/js/net.js"></SCRIPT>
		<SCRIPT language=javascript src="<%=context%>/js/js.js"></SCRIPT>
		<SCRIPT language=javascript src="<%=context%>/js/align_tab_by_head.js"></SCRIPT>
		<%
		ReportQryStruct qryStruct = (ReportQryStruct) session.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
		if(qryStruct == null){
			qryStruct = new ReportQryStruct();
		}
		boolean LabDisable = qryStruct.ctl_lvl.equals("0")?false:true;
		String ChnlTypeSql = "SELECT code_id,code_name FROM ui_code_list where type_code='CHANNEL_TYPE' and status='1' order by code_seq";
		String ProvSql = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t WHERE t.geo_flag='"+qryStruct.area_id+"' order by t.sort_id asc";
		String CitySql = "SELECT city_code,city_name FROM dim_pub_city where province_code ='"+qryStruct.metro_id+"' and city_code != '000' order by sort_id";
		String MeaSql = "SELECT IOID_ID0,EVA_NAME FROM st_eva_all_para  where supper_id='B02' ";
		%>
		
<script>
	
	$(document).ready(function() {
		initframe();		
	});
	
	function initframe(){
		var tableId = MeasureChange("<%=qryStruct.dim4%>");
		document.getElementById("table_self_chnl_T_01_detail").src="SubjectCommTable.rptdo?table_id="+tableId+"&first=Y&table_height=400";
	}
	function loadFrameByLevel() {
	}
	
	function doQuery() {
	
		SelfChnl.submit();
	}
/*
*   指标变更，关联对应表格样式
*/
	function MeasureChange(obj) {
		var tableId="";
		
		if(obj=="B0201")//销售绩效
			tableId="self_chnl_T_D_pfm";
		else if(obj=="B0202")//硬件资源（地理位置）
			tableId="self_chnl_T_D_local";
	/*		
		else if(obj=="B0203")//渠道效益
			tableId="self_chnl_T_D_bene";
	*/
		else if(obj=="B0203")//发展质量 
			tableId="self_chnl_T_D_qual";
		else
			tableId="self_chnl_T_D_pfm";
		//tableId="self_chnl_T_01_det";
		return tableId;
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
	if(arsType=="PROV"){
     	var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadSelectProv,ajaxError);
    }
    if(arsType=="AREA"){
      	var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadSelectArea,ajaxError);
	}
    
    ajaxHelper.sendRequest();
  }
  //加载选中区域省份
  function loadSelectArea(){
  	var jsonTxt=this.req.responseText;
	if(jsonTxt){
		var obj = eval('('+jsonTxt+')');
		loadJsonList("qry__metro_id",obj.RESULT);
	}
  }
  //加载选中省地市
  function loadSelectProv(){
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
**  重置按钮
*/
	function doReset() {
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		document.getElementById("qry__tactic_type").value=""; //渠道类型
		document.getElementById("qry__gather_month").value=getOriDate(); //账期
		document.getElementById("LevelSelect").value=""; //门店级别 
		document.getElementById("ScorceSelect").value="";//得分等级
		document.getElementById("qry__dim4").value="B0201";//指标
		document.getElementById("WaveSelect").value="";  //波动
		
		var areaList = document.getElementById("qry__area_id");
		var provCodeList = document.getElementById("qry__metro_id");
		var cityCodeList = document.getElementById("qry__city_id");
		
		if(areaList.disabled == false)
			areaList.value="";
		if(provCodeList.disabled == false){
			provCodeList.length=0;
			provCodeList.options.add(new Option("全部",""));
			provCodeList.value="";
		}
		if(cityCodeList.disabled == false){
			if(ctl_lvl=="0"){
				cityCodeList.length=0;
				cityCodeList.options.add(new Option("全部",""));
			}
			cityCodeList.value="";
		}
	}

/*
**得到默认账期
*/
	function getOriDate(){
		var oriDate = document.getElementById("qry__rpt_date");
		return oriDate.value;
 	} 
/*
**  区域联动
*/	
	function AreaChange(obj) {
		if(obj.value==""){
			var provCodeList = document.getElementById("qry__metro_id");
			provCodeList.length=0;
			provCodeList.options.add(new Option("全部",""));
			
			var cityCodeList = document.getElementById("qry__city_id");
			cityCodeList.length=0;
			cityCodeList.options.add(new Option("全部",""));
		} else {
			loadNewContent("AREA",obj.value)
		}
	}
/*
**  省份联动
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
**  调用导出页面.
*/		
	function export_func(){
		//导出未明确暂不实现
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		var province_code="<%=qryStruct.metro_id%>";
		var city_code="<%=qryStruct.city_id%>";
		var acct_month="<%=qryStruct.gather_month%>";
		var tableName="自有渠道指标明细";
		if("0"==ctl_lvl){
			tableName= "全国"+tableName;
		}else if("1"==ctl_lvl){
			tableName= "::province_code::"+tableName;
		} else if("2"==ctl_lvl){
			tableName= "::province_code::::city_code::"+tableName;
		}
		tableName=encodeURI(encodeURI(tableName));
		if(ctl_lvl=="0"){//总部
			window.open("ExportTools.rptdo?exportId=ZBChannelDetail&tableName="+tableName+"&acct_month="+acct_month+"&sqlArgs="+acct_month,"","");
		}else if(ctl_lvl=="1"){//省分
			window.open("ExportTools.rptdo?exportId=ProvChannelDetail&tableName="+tableName+"&acct_month="+acct_month+"&province_code="+province_code+"&sqlArgs="+acct_month+","+province_code,"","");
		}else if(ctl_lvl=="2"){//地市
			window.open("ExportTools.rptdo?exportId=CityChannelDetail&tableName="+tableName+"&acct_month="+acct_month+"&province_code="+province_code+"&city_code="+city_code+"&sqlArgs="+acct_month+","+province_code+","+city_code,"","");
		}
	}
/*
**  链接跳转函数.
*/	
	function callOtherURL()
    {
    		var strUrl = "Weight.rptdo?ioid_id0=B0201";
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

<!-- 查询条件区,开始 -->
<div class="topsearch">
	 <table width="95%"  border=0 cellpadding="0"  cellspacing="3" align="center">
		<tr>
			<td align="right" class="td-bg">月份：</td> 	        
            <td ><input id="qry__gather_month" class="Wdate" style="height:22px;width:100px;" type="text" name="qry__gather_month" value="<%=qryStruct.gather_month%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>
            </td>
            
			<td align="right" class="td-bg">考核区域：</td>
            <td >
            	<BIBM:SelectForCodeList htmlId="qry__area_id" codeType="<%=WebConstKeys.SUBJECT_CHANNEL_AREA_DEF%>" 
		   		value="<%=qryStruct.area_id%>" htmlName="qry__area_id" hasAll="true" disabled="<%=LabDisable%>"  
		   		htmlAttr="onChange='AreaChange(this)' style='width:103px;'" />
			</td>
            
            <td width="" align="right" class="td-bg" >省分：</td>
            <td  >
            <% if(qryStruct.ctl_lvl.equals("1") || qryStruct.ctl_lvl.equals("2")) {%>
				<BIBM:TagSelectList focusID="<%=qryStruct.metro_id%>" script="disabled=true onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__metro_id" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
			<%} else {%>
				<BIBM:TagSelectList focusID="<%=qryStruct.metro_id%>" script="onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__metro_id" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
		  	<%}%>			
		  	</td>
			
            <td width="" align="right" class="td-bg">地市：</td>
            <td  >
            <% if(qryStruct.ctl_lvl.equals("2")) {%>
            	<BIBM:TagSelectList focusID="<%=qryStruct.city_id%>" script="disabled=true class='easyui-combobox'" listName="qry__city_id" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
            <%} else {%>
            	<BIBM:TagSelectList focusID="<%=qryStruct.city_id%>" script="class='easyui-combobox'" listName="qry__city_id" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
          	<%}%>            
            </td>
            
            <td width="" align="right" class="td-bg" style="padding-left:10px;">渠道类型：</td>
            <td >
       	  		<BIBM:TagSelectList  focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry__tactic_type" listID="0"  selfSQL="<%=ChnlTypeSql%>" />           
       	   	</td>
            
          <td width="" >&nbsp;</td>
  		</tr> 
  		
		 <tr>   
		   <td width="" align="right" class="td-bg">门店级别：</td>
           <td  >
		   		<BIBM:SelectForCodeList htmlId="LevelSelect" codeType="<%=WebConstKeys.SELF_CHNL_LEVEL%>" 
		   		value="<%=qryStruct.dim1%>" htmlName="qry__dim1" hasAll="true" htmlAttr="style='width:103px;'" />	
       	   </td>
         
           <td align="right" class="td-bg">排名波动：</td>
           <td >
           		<BIBM:SelectForCodeList htmlId="WaveSelect" codeType="<%=WebConstKeys.SOC_RANK_WAVE%>" 
				value="<%=qryStruct.dim3%>" htmlName="qry__dim3" hasAll="true" htmlAttr="style='width:100px;'" />	         
           </td>
           
           <td align="right" class="td-bg" style="padding-left:10px;">得分等级：</td>
           <td >
               <BIBM:SelectForCodeList htmlId="ScorceSelect" codeType="<%=WebConstKeys.SELF_SCORE_RANK%>" 
				value="<%=qryStruct.dim2%>" htmlName="qry__dim2" hasAll="true" htmlAttr="style='width:100px;'" />
           </td>
           
           <td align="right" class="td-bg" style="padding-left:10px;">评价指标：</td>
           <td >
           		<BIBM:TagSelectList focusID="<%=qryStruct.dim4%>" listName="qry__dim4" listID="0" selfSQL="<%=MeaSql%>" />
           </td>
            <td width="" >&nbsp;</td>
           <td >
           		<input id="button_submit" class="btn_search" type="button" value="查询" onClick="doQuery();"/>
		   		<input id="button_submit" class="btn3" type="button" value="重置" onClick="doReset();"/>		   
		   </td>
        </tr> 
	</table>
</div>
	<!-- 查询条件区,结束 -->
	<div class="result_title">
		<span >自有营业厅门店明细</span>
		<div class="iw">
		<input  id="button_submit"  class="btn_exceldown button" type="button" value="下载指标明细" onClick="export_func()"/>
		<input  id="button_submit2"  class="btn_iw" type="button" value="评分指标及权重" onClick="callOtherURL()"/>
		</div>
	</div>
	<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
		<tr>	
		<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="table_self_chnl_T_01_detail" width="100%" height=432
							src=""
					frameborder="0" scrolling="no"></iframe></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</FORM>
    <script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
	    domHover(".btn_exceldown", "btn_exceldown_hover");
    </script>
	</body>
</html>