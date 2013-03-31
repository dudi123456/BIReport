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
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/unicomcontent.css">
		<title>渠道明细</title>
		<SCRIPT language=javascript src="<%=context%>/js/net.js"></SCRIPT>
		<SCRIPT language=javascript src="<%=context%>/js/js.js"></SCRIPT>
		<SCRIPT language=javascript src="<%=context%>/js/align_tab_by_head.js"></SCRIPT>
		<%
			ReportQryStruct qryStruct = (ReportQryStruct) session
					.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			if (qryStruct == null)
			{
				qryStruct = new ReportQryStruct();
			}
			String ChnlTypeSql = "SELECT  distinct a.channel_type_lvl3 as CHNL_ID, substr(a.channel_type_lvl3_name,instr(a.channel_type_lvl3_name,'-') + 1) as CHNL_NAME FROM DIM_CHANNEL_TYPE a where a.channel_type_lvl3 LIKE '2010%'";
			String ProvSql = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t order by t.sort_id asc";
			
			String geo =  qryStruct.attach_region;
			//qryStruct.qry
			//if(null!=geo && !"".equals(geo))
			//{
				ProvSql = "SELECT * FROM ST_DIM_AREA_PROVINCE t where t.geo_flag='"+geo+"' order by t.sort_id asc";
			//}
			String Dim4Sql =  "select u.ioid_id0,u.eva_name from st_eva_all_para u where u.supper_id='D05' order by u.ioid_id0";
			String CitySql = "SELECT city_code,city_name FROM dim_pub_city where province_code ='"+qryStruct.metro_id+"' and city_code != '000' order by sort_id";
			String AreaSql = "select t.code_id,t.code_name from ui_code_list t where t.type_code='AREA_DEF' order by t.code_seq";			
			String levelSql = "select t.code_id,t.code_name from ui_code_list t where t.type_code='SOC_CHNL_LEVEL' order by t.code_seq";
			String ChainLevelSql = "select t.code_id,t.code_name from ui_code_list t where t.type_code='CHAIN_LEVEL' order by t.code_seq";
		%>
		
<script>
	
	$(document).ready(function() {
		initframe();		
	});
	
	function initframe(){	
		var tableIframe = document.getElementById("table_sct_chnl_T_01_detail");
		var ctl_lvl = '<%=qryStruct.ctl_lvl%>';
		//var city_id = document.getElementById("qry__city_id").value;
		//评价指标
		var dim4='<%=qryStruct.dim4%>';
		if(dim4=="D0501")
		{
			//总部
			if(ctl_lvl=="0")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_3gs_zb&first=Y&table_height=400";
			//省份
			}else if(ctl_lvl=="1")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_3gs_sf&first=Y&table_height=400";
			//地市
			}else if(ctl_lvl=="2")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_3gs_ds&first=Y&table_height=400";
			}
		}else if(dim4=="D0502")
		{
			//总部
			if(ctl_lvl=="0")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_com_zb&first=Y&table_height=400";
			//省份
			}else if(ctl_lvl=="1")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_com_sf&first=Y&table_height=400";
			//地市
			}else if(ctl_lvl=="2")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_com_ds&first=Y&table_height=400";
			}
		}else if(dim4=="D0503")
		{
			//总部
			if(ctl_lvl=="0")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_hd_zb&first=Y&table_height=400";
			//省份
			}else if(ctl_lvl=="1")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_hd_sf&first=Y&table_height=400";
			//地市
			}else if(ctl_lvl=="2")
			{
				tableIframe.src="SubjectCommTable.rptdo?table_id=sct_chnl_T_01_hd_ds&first=Y&table_height=400";
			}
		}
	}
	
	function doQuery() {
		SelfChnl.submit();
	}
	
/*
*   Ajax  
*/
function loadNewContent(arsType,arsValue){
   	var url="<%=context%>/subject/QryTools.rptdo?qryType=" + arsType + "&" + "argsCode=" + arsValue;
    var rpcUrl=encodeURI(url);
    var params=[];
    var pos=rpcUrl.indexOf("?");
    if(pos>=0){
      var param=rpcUrl.substring(pos+1);
      rpcUrl=rpcUrl.substring(0,pos);
      params=param.split("&");
    }
     //记录下当前的滚动条位置
     if(arsType=="ALL"){
     	var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadTableUpdate,ajaxError);
     }
     if(arsType=="PROV"){
     	var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadSelectDist,ajaxError);
     }
     if(arsType=="AREA"){
      	var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadSelectProv,ajaxError);
      }
    ajaxHelper.sendRequest();  
  }
  //加载返回的维度值
  function loadTableUpdate(){
    	var jsonTxt=this.req.responseText;
    	if(jsonTxt){
    		var obj = eval('('+jsonTxt+')');
    		loadJsonList("qry__metro_id",obj.RESULT.ProvList);
    		
    		loadJsonList("qry__city_id",obj.RESULT.DistList);
    		
    	}
  }
  function loadSelectProv(){
  	var jsonTxt=this.req.responseText;
	if(jsonTxt){
		var obj = eval('('+jsonTxt+')');
		loadJsonList("qry__metro_id",obj.RESULT);
	}
}
  function loadSelectDist(){
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
		var provCodeList = document.getElementById(listName);
    	//清空SELECT options
    	provCodeList.length=0;
    	provCodeList.options.add(new Option("全部",""));
    	
    	for(var i=0;i < jSonObj.length;i++)
    	{
    		provCodeList.options.add(new Option(jSonObj[i].argsName,jSonObj[i].argsCode));
    	} 
	}

/*
**获取上一月日期函数YYYYMMDD
*/
function getNowFormatDate(){
    	var day = new Date();
    	var Year = 0;
    	var Month = 0;
    	var Day = 0;
    	var CurrentDate = "";
    	Year= day.getFullYear();//支持IE和火狐浏览器.
    	Month= day.getMonth()+1;
    	CurrentDate += Year;
    	//上一月
    	if (Month > 10 ){
     		CurrentDate += (Month-1);
    	}
    	//若果为1月减一年+"12"月,如201212
    	else if(Month==1){
     		return Year-1+"12";
     	//
    	}else
    	{
    		CurrentDate += "0" + (Month-1);
    	}
    	return CurrentDate;
 	} 
/*
**  重置按钮
*/
	function doReset() {
		var oChlType = document.getElementById("qry__tactic_type");
		var oSerMonth = document.getElementById("qry__gather_month");
		var levelSelect = document.getElementById("qry__dim1");
		
		//考核区域
		var areaSelect = document.getElementById("qry__attach_region");
		//省份
		var provCodeList = document.getElementById("qry__metro_id");
		//地市
		var cityCodeList = document.getElementById("qry__city_id");
		
		//连锁级别
		var chain_level = document.getElementById("qry__chain_level");
		chain_level.value="";
		
		//排名波动
		var waveSelect = document.getElementById("WaveSelect");
		waveSelect.value="";
		
		//评价指标
		//var measureList = document.getElementById("qry__dim4");
		//measureList.value="D0501";
		
		oChlType.value="";
		oSerMonth.value=getNowFormatDate();
		levelSelect.value="";
		//总部可以重置考核区域、省份、地市
		if('<%=qryStruct.ctl_lvl%>'=='0')
		{
			areaSelect.value="";
			provCodeList.length=0;
			provCodeList.options.add(new Option("全部",""));
			
			cityCodeList.length=0;
			cityCodeList.options.add(new Option("全部",""));
			
			cityCodeList.length=0;
			cityCodeList.options.add(new Option("全部",""));
		//省份可以重置地市
		}else if('<%=qryStruct.ctl_lvl%>'=='1')
		{
			cityCodeList.value="";
		}
	}

/*
**  区域联动地市函数
*/		
	function ProvChange(obj)
	{
		loadNewContent("PROV",obj.value)
	}
	
/*
**  区域联动省份函数
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
	**  链接跳转函数.
	*/	
		function callOtherURL(url_type,optype)
	    {
			if(url_type=="static"){
	    		var strUrl = "./societychannel/"+optype+".jsp";
	        	var newsWin = window.open(strUrl);
	        		if (newsWin != null) {
	            	newsWin.focus();
	        	}
	    	}
	    }
		function subgo(){
			//南北方标识 1:北方；0:南方
			//var geo_flag = document.getElementById("qry__attach_region").value;
			window.open("Weight.rptdo?ioid_id0=D05","评分指标及权重","");
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
				var tableName="社会渠道指标明细";
				if("0"==ctl_lvl){
					tableName= "全国"+tableName;
				}else if("1"==ctl_lvl){
					tableName= "::province_code::"+tableName;
				} else if("2"==ctl_lvl){
					tableName= "::province_code::::city_code::"+tableName;
				}
				tableName=encodeURI(encodeURI(tableName));
				if(ctl_lvl=="0"){//总部
					window.open("ExportTools.rptdo?exportId=SocChannelDetailZB&tableName="+tableName+"&acct_month="+acct_month+"&sqlArgs="+acct_month,"","");
				}else if(ctl_lvl=="1"){//省分
					window.open("ExportTools.rptdo?exportId=SocChannelDetailSF&tableName="+tableName+"&acct_month="+acct_month+"&province_code="+province_code+"&sqlArgs="+acct_month+","+province_code,"","");
				}else if(ctl_lvl=="2"){//地市
					window.open("ExportTools.rptdo?exportId=SocChannelDetailDS&tableName="+tableName+"&acct_month="+acct_month+"&province_code="+province_code+"&city_code="+city_code+"&sqlArgs="+acct_month+","+province_code+","+city_code,"","");
				}
				
							
			}
</script>
	</head>
		
<body>
	
<FORM name="SelfChnl" action="SocietyChnlEvl.rptdo">
<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">

<!-- 查询条件区,开始 -->
<div class="topsearch">
				 <table width="90%"  border=0 align="center" cellpadding="0"  cellspacing="3">
					<tr>
					  	<td align="right" ><span class="td-bg">月份：</span></td>
			            <td >
			            	<input id="qry__gather_month" class="Wdate" style="height:22px;width: 97px" type="text" name="qry__gather_month" value="<%=qryStruct.gather_month%>" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})"/>			            </td>
			            
						<td align="right" class="td-bg">考核区域：</td>
			            <td >
			            	<% if(qryStruct.ctl_lvl.equals("0")) {%>
				            	<BIBM:TagSelectList focusID="<%=qryStruct.attach_region%>" script="onChange='AreaChange(this)' class='easyui-combobox'" listName="qry__attach_region" listID="0" allFlag="" selfSQL="<%=AreaSql%>" />
				            <%} else {%>
				                <BIBM:TagSelectList focusID="<%=qryStruct.attach_region%>" script="disabled=true onChange='AreaChange(this)' class='easyui-combobox'" listName="qry__attach_region" listID="0" allFlag="" selfSQL="<%=AreaSql%>" />
			  				<%}%>						</td>
			            <td align="right" class="td-bg" >省分：</td>
			            <td >
			            <% if(qryStruct.ctl_lvl.equals("1") || qryStruct.ctl_lvl.equals("2")) {%>
							<BIBM:TagSelectList focusID="<%=qryStruct.metro_id%>" script="disabled=true onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__metro_id" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
						<%} else {%>
							<BIBM:TagSelectList focusID="<%=qryStruct.metro_id%>" script="onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__metro_id" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
						<%}%>						</td>
						
			            <td align="right" class="td-bg">地市：</td>
			            <td >
			            <% if(qryStruct.ctl_lvl.equals("2")) {%>
			            	<BIBM:TagSelectList focusID="<%=qryStruct.city_id%>" script="disabled=true class='easyui-combobox'" listName="qry__city_id" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
			            <%} else {%>
			            	<BIBM:TagSelectList focusID="<%=qryStruct.city_id%>" script="class='easyui-combobox'" listName="qry__city_id" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
			            <%}%>			            </td>
			            
			          
			           <td >&nbsp;</td>
			  		</tr>   
			         <tr> 
			         	<td align="right" class="td-bg">渠道类型：</td>
			            <td >
			            	<BIBM:TagSelectList  focusID="<%=qryStruct.tactic_type%>" script="class='easyui-combobox'"	listName="qry__tactic_type" listID="0" allFlag="" selfSQL="<%=ChnlTypeSql%>" />			            </td>
			         	
			         	<td align="right" class="td-bg">连锁级别：</td>
			            <td>
			            	<BIBM:TagSelectList  focusID="<%=qryStruct.chain_level%>" script="class='easyui-combobox'"	listName="qry__chain_level" listID="0" allFlag="" selfSQL="<%=ChainLevelSql%>" />			            </td>
			           <td align="right" class="td-bg">排名波动：</td>
			           <td >
			              	<select id="WaveSelect" name="qry__dim5">
			              		<option value="">全部</option>
			              		<option value="1" <%=(qryStruct.dim5.equals("1")?"selected":"")%>>排名上升</option>
			              		<option value="-1" <%=(qryStruct.dim5.equals("-1")?"selected":"")%>>排名下降</option>
			              		<option value="0" <%=(qryStruct.dim5.equals("0")?"selected":"")%>>排名不变</option>
			            	</select>			           </td>
			           
<%--			           <td align="right" class="td-bg">评价指标：</td>--%>
<%--			           <td >--%>
<%--			            	<BIBM:TagSelectList  focusID="<%=qryStruct.dim4%>" script="class='easyui-combobox'"	listName="qry__dim4" listID="0" selfSQL="<%=Dim4Sql%>" />            --%>
<%--			           </td>--%>
			           <td align="right" class="td-bg">门店级别：</td>
			           <td><BIBM:TagSelectList focusID="<%=qryStruct.dim1%>" script="class='easyui-combobox'" listName="qry__dim1" listID="0" allFlag="" selfSQL="<%=levelSql%>" /></td>
					   <td>
					   <input id="button_submit" class="btn_search" type="button" value="查询" onClick="doQuery();"/>
					   		<input id="button_submit" class="btn3" type="button" value="重置" onClick="doReset();"/>					   </td>
				    </tr> 
				</table>
  </div>
	<!-- 查询条件区,结束 -->
	<div class="result_title">
		<span >社会渠道门店评价排名</span>		
	    <div class="iw">
	       <input type="button" id="button_submit"  class="btn_excel"  value="下载指标明细" onclick="export_func()">
		   <input  id="button_submit"  class="btn_iw" type="button" value="评分指标及权重" onclick="subgo()"/>
		</div>
	</div>
	<table style="width: 100%;"  id="content_1" cellspacing="0" cellpaddingx="0">
		<tr>	
		<td>
		<table  cellspacing="0" cellpaddingx="0" style="width: 100%;">
			<tr valign="top" >
				<td align="left" colspan="2"><iframe id="table_sct_chnl_T_01_detail" width="100%" height=428
							src=""
					frameborder="0" scrolling="no"></iframe>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</FORM>
    <script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
        domHover(".btn_excel", "btn_excel_hover");
        domHover(".btn_iw", "btn_iw_hover");
    </script>
	</body>
</html>