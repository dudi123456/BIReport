<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.report.struct.ReportQryStruct"%>
<%@ page import="com.ailk.bi.common.app.StringB"%>
<%@ include file="/base/commonHtml.jsp"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta http-equiv="Content-Language" content="zh-cn">

		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/other/newmain.css">
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/jquery-ui-1.10.0.custom.min.css">	
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/titleStyle.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/Scroll.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/dd99.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/chart.js"></script>
		<SCRIPT language=javascript
			src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/wait.js"></SCRIPT>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/jquery-ui-1.10.0.custom.js"></SCRIPT>
		<title>门店明细</title>


		<%
			ReportQryStruct qryStruct = (ReportQryStruct) session
					.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			if (qryStruct == null) {
				qryStruct = new ReportQryStruct();
			}
			String ctl_lv1_str=qryStruct.ctl_lvl;
			String prov_code=qryStruct.province_code;
			String Dim4Sql =  "select u.ioid_id0,u.eva_name from st_eva_all_para u where u.supper_id='D05' order by ioid_id0";
			String ChnlTypeSql = "SELECT  distinct a.channel_type_lvl3 as CHNL_ID, substr(a.channel_type_lvl3_name,instr(a.channel_type_lvl3_name,'-') + 1) as CHNL_NAME FROM DIM_CHANNEL_TYPE a where a.channel_type_lvl3 LIKE '2010%'";
			String ProvSql = "SELECT t.province_code,t.province_name FROM ST_DIM_AREA_PROVINCE t order by t.sort_id asc";
			String CitySql = "SELECT city_code,city_name FROM dim_pub_city where province_code ='"+prov_code+"' and city_code != '000' order by sort_id";
			String dim6=StringB.NulltoBlank(qryStruct.dim6);
			if(dim6.equals("")){
				dim6=StringB.NulltoBlank(qryStruct.dim11);
			}
	  %>


		<BIBM:SelfRefreshTag pageNames="TableQryForm.qry" attrNames="<%=WebKeys.ATTR_SUBJECT_QUERY_STRUCT%>" warn="1" />

<script>
	
	$(document).ready(function() {
		initframe();	
		var cache = {};
   		$( "#qry__dim6" ).autocomplete({
   			//source: availableTags,
   			delay: 300,
   			source: function(request, response ) {
   				var term = request.term;
   				if ( term in cache ) {
   					response(cache[term]);
   					return;
   				}
   				$.ajax({
   					url: "<%=request.getContextPath()%>/subject/AjaxAutoComplete.rptdo",
   					dataType: "json",
   					data:{
   						serchSqlId: 'LoadChannelName',
   						searchKey: encodeURI(request.term)
   					},
   					success: function( data ) {
   						cache[ term ] = data;
   						response(data);
   					},
   					error: function(XMLHttpRequest, textStatus, errorThrown){
   						//alert(XMLHttpRequest.status);
                        //alert(XMLHttpRequest.readyState);
                        //alert(textStatus);
   					}
   				});
   			},
   			minLength: 1,
   			open: function() { 
   		        $('#qry__dim6').autocomplete("widget").width(300) 
   		    }  
   		});
   		// Hover states on the static widgets
   		$( "#dialog-link, #icons li" ).hover(
   			function() {
   				$( this ).addClass( "ui-state-hover" );
   			},
   			function() {
   				$( this ).removeClass( "ui-state-hover" );
   			}
   		);
	});
	function initframe(){					
		var tableIframe = document.getElementById("table_prov_T_01");
		//var dim7='<%=qryStruct.dim7%>';//评价指标
		var dim7='';//评价指标
		var ctl_lvl='<%=ctl_lv1_str%>';//用户级别
		
		if(dim7=="" || dim7=="D0501"){//业绩指标
			if("0"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_ACH_ALL&first=Y&table_height=550";
			}else if("1"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_ACH_PROV&first=Y&table_height=550";
			} else if("2"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_ACH_CITY&first=Y&table_height=550";
			}					
		}else if(dim7=="D0502"){//硬件资源指标
			if("0"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_HARD_ALL&first=Y&table_height=550";
			}else if("1"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_HARD_PROV&first=Y&table_height=550";
			} else if("2"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_HARD_CITY&first=Y&table_height=550";
			}
		}else if(dim7=="D0503"){//合作情况
			if("0"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_COOP_ALL&first=Y&table_height=550";
			}else if("1"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_COOP_PROV&first=Y&table_height=550";
			} else if("2"==ctl_lvl){
				tableIframe.src="SubjectCommTable.rptdo?table_id=SOC_CHANNL_COOP_CITY&first=Y&table_height=550";
			}
		}		
	}

	
	
	function doQuery() {	
		TableQryForm.submit();
	}
	

	
	function export_table_content(){
		//导出未明确暂不实现
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		var province_code="<%=qryStruct.province_code%>";
		var city_code="<%=qryStruct.city_code%>";
		var acct_month="<%=qryStruct.gather_month%>";
		var tableName="门店明细下载";
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
	    ajaxHelper.sendRequest();  
	  }
	  //加载返回的维度值
	  function loadTableUpdate(){
	    	var jsonTxt=this.req.responseText;
	    	if(jsonTxt){
	    		var obj = eval('('+jsonTxt+')');
	    		loadJsonList("qry__province_code",obj.RESULT.ProvList);
	    		
	    		loadJsonList("qry__city_code",obj.RESULT.DistList);
	    		
	    	}
	  }
	  function loadSelectDist(){
		  	var jsonTxt=this.req.responseText;
		  	if(jsonTxt){
		        var obj = eval('('+jsonTxt+')');
		    	loadJsonList("qry__city_code",obj.RESULT);
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
				var provCodeList = document.getElementById("qry__province_code");
				provCodeList.length=0;
				provCodeList.options.add(new Option("全部",""));
				
				var cityCodeList = document.getElementById("qry__city_code");
				cityCodeList.length=0;
				cityCodeList.options.add(new Option("全部",""));
			} else {
				loadNewContent("ALL",obj.value)
			}
		}
		
	/*
	**  链接跳转函数.
	*/	
		function callOtherURL(url_type,optype)

	    {
			if(url_type=="static"){
	    		var strUrl = '<%=request.getContextPath()%>'+"/subject/societychannel/"+optype+".jsp";
	        	var newsWin = window.open(strUrl);
	        		if (newsWin != null) {
	            	newsWin.focus();
	        	}
	    	}
	    }
	
	/*
	**得到默认账期
	*/
	function getOriDate(){
		var oriDate = document.getElementById("qry__rpt_date");
		return oriDate.value;
 	}
	
	function resetCondition(){
		var ctl_lvl='<%=qryStruct.ctl_lvl%>';//用户级别
		var oSerMonth = document.getElementById("qry__gather_month");
		oSerMonth.value=getOriDate();
		$("#qry__dim1")[0].selectedIndex = 0;
		$("#qry__chain_level")[0].selectedIndex = 0;
		$("#qry__dim8")[0].selectedIndex = 0;
		//$("#qry__dim7")[0].selectedIndex = 0;
		if("0"==ctl_lvl){
			$("#qry__province_code")[0].selectedIndex = 0;
			$("#qry__city_code")[0].selectedIndex = 0;
		}else if("1"==ctl_lvl){
			document.getElementById("qry__province_code").value='<%=prov_code%>';
			$("#qry__city_code")[0].selectedIndex = 0;
		} else if("2"==ctl_lvl){
			document.getElementById("qry__province_code").value='<%=prov_code%>';
			document.getElementById("qry__city_code").value='<%=qryStruct.city_code%>';
		}
		
		$("#qry__chanal_type")[0].selectedIndex = 0;
		document.getElementById("qry__dim6").value="";
	}
	
	function subgo(){
		//南北方标识 1:北方；0:南方
		//var geo_flag = document.getElementById("qry__attach_region").value;
		window.open("Weight.rptdo?ioid_id0=D05","评分指标及权重","");
	}
</script>
	</head>
	<body>
	<form name="TableQryForm" action="SocChannlPD.rptdo" method="post">
	<INPUT type=hidden name="optype" value="<%=qryStruct.optype%>">
	<INPUT type=hidden id="oriDate" name="qry__rpt_date"  value="<%=qryStruct.rpt_date%>">
		<div style="display: none; position: absolute;" id=altlayer></div>
		
		<!-- 查询条件区,开始 -->
		<div class="topsearch">
			<table width="95%" border=0 cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td height="27" align="right" class="td-bg">月份：</td>
					<td>
						<input class="Wdate" style="height: 22px; width: 100px;"
							type="text" id="qry__gather_month" name="qry__gather_month"
							value="<%=qryStruct.gather_month%>"
							onFocus="WdatePicker({crossFrame:false,isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})" />					</td>
					<td align="right" class="td-bg" >省分：</td>
		            <td >
		            <% if(ctl_lv1_str.equals("1") || ctl_lv1_str.equals("2")) {%>
						<BIBM:TagSelectList focusID="<%=qryStruct.province_code%>" script="disabled=true onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__province_code" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
					<%} else {%>
						<BIBM:TagSelectList focusID="<%=qryStruct.province_code%>" script="onChange='ProvChange(this)' class='easyui-combobox'" listName="qry__province_code" listID="0" allFlag="" selfSQL="<%=ProvSql%>" />
					<%}%>
					</td>		        
		            <td align="right" class="td-bg">地市：</td>
		            <td >
		            <% if(qryStruct.ctl_lvl.equals("2")) {%>
		            	<BIBM:TagSelectList focusID="<%=qryStruct.city_code%>" script="disabled=true class='easyui-combobox'" listName="qry__city_code" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
		            <%} else {%>
		            	<BIBM:TagSelectList focusID="<%=qryStruct.city_code%>" script="class='easyui-combobox'" listName="qry__city_code" listID="0" allFlag="" selfSQL="<%=CitySql%>" />
		            <%}%>
		           </td>
		           <td align="right" class="td-bg">门店级别：</td>
					<td>
					<BIBM:SelectForCodeList htmlId="qry__dim1" codeType="<%=WebConstKeys.SOC_CHNL_LEVEL %>" 
						value="<%=qryStruct.dim1 %>" htmlName="qry__dim1"
						disabled="false" hasAll="true" allOptionValue="" allOptionDesc="全部"
						htmlAttr="class='style='width: 100px;'"></BIBM:SelectForCodeList>
					</td>					
                   
				</tr>
				<tr>
					<td height="27" align="right" class="td-bg" style="padding-left:20px;">渠道类型：</td>
					<td>
					<BIBM:TagSelectList  focusID="<%=qryStruct.chanal_type%>" script="class='easyui-combobox'"	listName="qry__chanal_type" listID="0" allFlag="" selfSQL="<%=ChnlTypeSql%>" />					
					</td>			
                    <td align="right" class="td-bg">连锁级别：</td>
					<td>
					<BIBM:SelectForCodeList htmlId="qry__chain_level" codeType="<%=WebConstKeys.SOC_CHANNEL_CHAIN_LEVEL %>" 
						value="<%=qryStruct.chain_level %>" htmlName="qry__chain_level"
						disabled="false" hasAll="true" allOptionValue="" allOptionDesc="全部"
						htmlAttr="class='style='width: 100px;'"></BIBM:SelectForCodeList>					
					</td>
					<td align="right" class="td-bg">排名波动：</td>
					<td>
					<BIBM:SelectForCodeList htmlId="qry__dim8" codeType="<%=WebConstKeys.SOC_RANK_WAVE%>" 
						value="<%=qryStruct.dim8 %>" htmlName="qry__dim8"
						disabled="false" hasAll="true" allOptionValue="" allOptionDesc="全部"
						htmlAttr="class='style='width: 100px;'"></BIBM:SelectForCodeList>			
						</td>
					<td align="right" class="td-bg">归属社会渠道：</td>
					<td>
						<input type="text" id="qry__dim6" name="qry__dim6" value="<%=dim6%>" style="width:100px;"/>					
					</td>
<%--					<td align="right" class="td-bg">评价指标：</td>--%>
<%--					<td>--%>
<%--					<BIBM:TagSelectList  focusID="<%=qryStruct.dim7%>" script="class='easyui-combobox'"	listName="qry__dim7" listID="0" selfSQL="<%=Dim4Sql%>" />											--%>
<%--					</td>--%>
				  <td></td>			
				  <td><input id="button_submit" type="button" class="btn_search"
							value="查询" onClick="doQuery()" />
			      <input type="button" class="btn3" value="重置" onclick="resetCondition();"/>
				</tr>
			</table>
			<!-- 查询条件区,结束 -->
		</div>
	<div class="result_title">
		<span >战略渠道门店明细</span>
		<div class="iw">
	       <input type="button" id="button_submit"  class="btn_excel"  value="下载指标明细" onclick="javascript:export_table_content()">
		   <input  id="button_submit"  class="btn_iw" type="button" value="评分指标及权重" onclick="subgo()"/>
		</div>		
	</div>
		<!-- 图表展示区 ,开始-->
		<table style="width: 100%;" id="content_1" cellspacing="0"
			cellpaddingx="0">
			<!-- 表格区 -->
			<tr>
				<td>
					<table cellspacing="0" cellpaddingx="0" style="width: 100%;">
						<tr valign="top">
							<td align="left" colspan="2">
								<iframe id="table_prov_T_01" width="100%" height="430" src=""
									frameborder="0" scrolling="no"></iframe>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<!-- 图表展示区 ,结束-->

	<script type="text/javascript">
        domHover(".btn_search", "btn_search_hover");
        domHover(".btn3", "btn3_hover");
        domHover(".btn_iw", "btn_iw_hover");
       domHover(".btn_excel", "btn_excel_hover");
    </script>
	</form>
	</body>
</html>
