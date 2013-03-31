<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:directive.page import="com.ailk.bi.base.util.WebConstKeys"/>
<jsp:directive.page import="com.ailk.bi.base.struct.UserCtlRegionStruct"/>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.adhoc.domain.*"%>
<%@ page import="com.ailk.bi.adhoc.struct.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.ServletContext"%>
<%@ page import="com.ailk.bi.adhoc.struct.AdhocViewStruct"%>
<%@ page import="com.ailk.bi.common.app.FormatUtil"%>
<%@ page import="com.ailk.bi.pages.PagesInfoStruct,
 com.ailk.bi.pages.WebPageTool
"
%>

<%
//当前条件
		UiAdhocFavoriteDefTable  favinfo  =  (UiAdhocFavoriteDefTable)session.getAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
	if(favinfo == null){
		favinfo =  new UiAdhocFavoriteDefTable();
	}

	//即席查询ID
	String adhoc = request.getParameter(AdhocConstant.ADHOC_ROOT);
	if (adhoc == null || "".equals(adhoc)) {
		adhoc = (String) session.getAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
		if (adhoc == null || "".equals(adhoc)) {
			adhoc = AdhocConstant.ADHOC_ROOT_DEFAULT_VALUE;
		}
	}

UiAdhocConditionMetaTable[]  conMeta  = (UiAdhocConditionMetaTable[])session.getAttribute(AdhocConstant.ADHOC_VIEW_CONDITION_SESSION_KEY);
if(conMeta ==null){
	conMeta = new UiAdhocConditionMetaTable[0];
}
//当前纬度
UiAdhocDimMetaTable[] dimMeta = (UiAdhocDimMetaTable[])session.getAttribute(AdhocConstant.ADHOC_VIEW_DIM_SESSION_KEY);
if(dimMeta ==null){
	dimMeta = new UiAdhocDimMetaTable[0];
}
//可钻取维度信息
UiAdhocDimMetaTable[] dimExpandMeta = (UiAdhocDimMetaTable[])session.getAttribute(AdhocConstant.ADHOC_VIEW_DIM_EXPAND_SESSION_KEY);
//当前指标
UiAdhocMsuMetaTable[] msuMeta = (UiAdhocMsuMetaTable[])session.getAttribute(AdhocConstant.ADHOC_VIEW_MSU_SESSION_KEY);
if(msuMeta == null){
	msuMeta = new UiAdhocMsuMetaTable[0];
}
//
AdhocViewQryStruct qryStruct = (AdhocViewQryStruct)session.getAttribute(AdhocConstant.ADHOC_VIEW_QUERY_STRUCT);
if(qryStruct == null){
	qryStruct = new AdhocViewQryStruct();
}

/***************************************************************************************************/
//列表
String[][] viewList = (String[][])session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST);
if(viewList == null){
	viewList = new String[0][0];
}
//视图信息
AdhocViewStruct adhocView = (AdhocViewStruct)session.getAttribute(AdhocConstant.ADHOC_VIEW_STRUCT);
if(adhocView ==null){
	adhocView = new AdhocViewStruct();
}
//标题头
String[] headArr = adhocView.headStr.split(",");
String[] headDesc= adhocView.headDesc.split(",");
//字段长度
int  columnLength =  dimMeta.length+msuMeta.length;

//字段类型
String sort_type=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
if (sort_type==null){
	//sort_type = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
	if(sort_type==null){
		sort_type="";
	}
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_TYPE,sort_type);

//列索引
String sortidx=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
if (sortidx==null){
	//sortidx = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
	if(sortidx==null){
		sortidx="0";
	}
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_SORT_INDEX,sortidx);

//1,asc;2,desc
String orderflag=request.getParameter(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
if (orderflag==null){
	//orderflag = (String) session.getAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
	if(orderflag == null){
		orderflag = "";
	}
}
session.removeAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG);
session.setAttribute(AdhocConstant.ADHOC_VIEW_LIST_ORDER_FLAG,orderflag);

boolean firstRun = false;
if (request.getAttribute("FirstRun")!=null){
	firstRun = true;
}

//
if (null!=sortidx && null != orderflag && null != viewList) {
		// 按数值排序
		System.out.println(sortidx + ":" + orderflag + ":" + orderflag);

		if (orderflag.length()>0){
			if ("1".equalsIgnoreCase(sort_type)) {
						System.out.println("testest1:");
						Arrays.sort(viewList, new NumberComparator(Integer.parseInt(sortidx), orderflag));

			} else {//2
					// 按字符排序2

						if (firstRun==false){
							System.out.println("testest2:");
							Arrays.sort(viewList, new CharacterComparator(Integer.parseInt(sortidx), orderflag));
						}

			}
		}

}


//增加发展人部门控制
UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct)session.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
String tmpKey = "";
if(ctlStruct!=null&&ctlStruct.ctl_county_str!=null&&!"".equals(ctlStruct.ctl_county_str)){
	tmpKey=ctlStruct.ctl_county_str;
}
String tmpMap_code = "USER_DEVELOP_DEPART_ID4";
String tmpValue = "";
ServletContext servletContext = request.getSession().getServletContext();
HashMap codeMap = (HashMap)servletContext.getAttribute(WebConstKeys.ATTR_C_CODE_LIST);
		if(codeMap!=null){
			HashMap map = (HashMap)codeMap.get(tmpMap_code.trim().toUpperCase());
			if(map!=null){
				if(map.get(tmpKey) == null){
					tmpValue = "";
				}else{
					tmpValue = (String)map.get(tmpKey);
				}

			}
		}
if(!"".equals(tmpValue)){
	tmpValue = "(受限发展渠道查询："+tmpValue+")";
}
%>
<!DOCTYPE html>
<html>
<head>
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<script language=javascript>
	    domHover(".btn3", "btn3_hover");

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
<%@ include file="/base/commonHtml.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/net.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/dojo.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/wait.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/kw.js"></SCRIPT>

<script language="JavaScript">

function sort(order,colidx,datatype){
	location.href="../adhoc/adhoc_view.jsp?order_flag="+order+"&sort_idx="+colidx+"&sort_type="+datatype;
}

function showJobReason(){
		if(document.form1.radiobutton[0].checked)
		{

			document.getElementById("list").style.display="block";
			document.getElementById("report").style.display="none";
		}
		if(document.form1.radiobutton[1].checked)
		{

			document.getElementById("list").style.display="none";
			document.getElementById("report").style.display="block";

		}
	}




  //页面登陆
  function pageOnLoad(){
	showJobReason()

  }
  //导出
  function ExportExcel(){
  	 var type = "";
  	 for(i=0;i<document.form1.radiobutton.length;i++){
				if(document.form1.radiobutton[i].checked == true){
					type=document.form1.radiobutton[i].value;
				}
	 }
	 //
	 if("list" == type){
	 	window.open("AdhocListToExcel.screen");
	 }else if("report" == type){
	 	window.open("AdhocReportToExcel.screen");
	 }
  }

//用户清单
function QueryList(){
	 //提取名称
     //var row = window.showModalDialog("adhoc_alert_info.jsp","select1","dialogWidth:350px;DialogHeight=270px;status:no;scroll:no");
   	 //if(row == "undefined"){
   	 //	row = "";
   	 //}
	 form1.target="_blank";
	 	 	   var constr="";
	   var dimstr="";
	   var msustr="";
	   //提取条件(不包含默认条件)
	   var conArr = document.getElementsByName("con_check");
	   for (i=0;i<conArr.length;i++ ){
	        if(conArr[i].checked && !conArr[i].disabled){
	             constr=constr+conArr[i].value+",";
	        }
	   }
	   constr=constr.substring(0,constr.length-1);
	   //条件纬度(不包含默认纬度)
	   var dimArr = document.getElementsByName("dim_check");
	   for (i=0;i<dimArr.length;i++ ){
	        if(dimArr[i].checked && !dimArr[i].disabled){
	             dimstr=dimstr+dimArr[i].value+",";
	        }
	   }
	   dimstr=dimstr.substring(0,dimstr.length-1);

	    var msuArr = document.getElementsByName("msu_check");
	   //提取指标(不包含默认指标)
	   for (i=0;i<msuArr.length;i++ ){
	        if(msuArr[i].checked && !msuArr[i].disabled){
	             msustr=msustr+msuArr[i].value+",";
	        }
	   }

	  msustr=msustr.substring(0,msustr.length-1);

	 form1.conStr001.value = constr;

	form1.dimStr001.value = dimstr;
	form1.msuStr001.value = msustr;

	 form1.action="AdhocUserList.rptdo";

  	 form1.submit();
}
//查询
function CallQuery(){
	form1.target="_self";
	form1.action="AdhocView.rptdo?oper_type=listView"
	ShowWait();
	form1.submit();
}

//扩展查询
function CallExpandQuery(dimid,order){
	form1.target="_self";
	form1.action="AdhocView.rptdo?oper_type=listView&dimid="+dimid+"&expand_order="+order;
	ShowWait();
	form1.submit();
}

//显示标题
function showTitle(obj){
	var tmp=obj.value.split(",");
	var str="";
	var tar="";
  	if (obj.value!=""){
  		obj.title=obj.value;
		  for(var i=0;i<tmp.length;i++){
		     if((i>0) && (i%3==0)){
		        str+="<BR>";
		        str+=tmp[i]+";";
		     }else{
		        str+=tmp[i]+";";
		     }
		  }

    }
}

//对象
function BaseXmlSubmit(){
}
//动作
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = null;
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}

//实例
var baseXmlSubmit =new BaseXmlSubmit();

//前置条件区
function MultiAjax(hoc_id ,con_id){
	 if(hoc_id == null || hoc_id == ""){
    	alert("没有指定特定的即席查询业务!");
    	return;
    }
    if(con_id == null || con_id == ""){
    	alert("没有指定特定的即席查询条件!");
    	return;
    }
    var str = baseXmlSubmit.callAction("../adhoc/adhoc_multi_ajax.jsp?adhoc_id="+hoc_id+"&con_id="+con_id);
    str=str.replace(/^\s+|\n+$/g,'');
    return str;
}

function userUpFileCondition(theObj,hoc_id,con_id,qry_name,qry_desc,con_name){
	var returnValue;
    var acptsite;
//	alert("AdhocUserUpLoadCon.rptdo?adhoc_id="+hoc_id+"&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+con_name);

	returnValue=window.showModalDialog("AdhocUserUpLoadCon.rptdo?adhoc_id="+hoc_id+"&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+con_name,qry_name.value,"dialogWidth:600px; dialogHeight:450px; dialogLeft:320px; dialogTop:250px; status:no; directories:yes;scrollbars:no;Resizable=no;help:no");

	if(returnValue == null || returnValue == "" || returnValue == "undefined"){
        return;
    }else{

		if(qry_name.value.toString() == '|'){
	   		qry_name.value="";
	        qry_desc.value="";
	        qry_desc.title="";

	    }else{
			    acptsite=returnValue.split("|");
			    qry_name.value=acptsite[0];
			    qry_desc.value=acptsite[1];
			    qry_desc.title=acptsite[1];

	    }

	}
}


//objId 条件ID
//objValue  条件英文名
//objDesc   条件英文名描述
//objName   条件名称
//javascript:MultipleSelected(this,'202',qry__innet_channel,qry__innet_channel_desc,'入网渠道');
function MultipleSelected(theObj,hoc_id,con_id,qry_name,qry_desc,con_name){
    //
    var returnValue;
    var acptsite;
    while (theObj.tagName!="FORM"){
    		theObj=theObj.parentElement;
    }

    //提取前置条件con_id,con_str:con_id,con_str
    var conStr="";
    var objStr = MultiAjax(hoc_id,con_id);
    //解析对象数组提取值
    if(conStr == null|| conStr == "" || conStr == "undefined"  ){
    	conStr = "";
    }else{

	    var objArr = objStr.toString().split(":");
	    for(var i=0;i<objArr.length;i++){
	    	var tmpObj = objArr[i].split(",");
	    	var obj = eval("theObj."+tmpObj[0]+".value")
	    	conStr+="&"+tmpObj[1]+"="+obj;
	    }
    }
    //
    if(conStr == null || conStr == "undefined"){
    	conStr = "";
    }


	returnValue=window.showModalDialog("AdhocMultiSelect.rptdo?adhoc_id="+hoc_id+"&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+con_name+conStr,qry_name.value,"dialogWidth:805px; dialogHeight:475px; dialogLeft:320px; dialogTop:250px; status:no; directories:yes;scrollbars:no;Resizable=no;help:no");
    if(returnValue == null || returnValue == "" || returnValue == "undefined"){
        return;
    }else{
	    if(qry_name.value.toString() == '$$'){
	   		qry_name.value="";
	        qry_desc.value="";
	        qry_desc.title="";

	    }else{
			    acptsite=returnValue.split("$$");
			    qry_name.value=acptsite[0].substr(0,acptsite[0].length-1);
			    qry_desc.value=acptsite[1].substr(0,acptsite[1].length-1);
			    qry_desc.title=acptsite[1].substr(0,acptsite[1].length-1);
	    }

	}
    //document.getElementById("mulitSelect").readOnly=true;
}

//
function MultipleCheckSelected(theObj,con_rule,con_id,qry_name,qry_desc,con_name){
    //
    var returnValue;
    var acptsite;
    while (theObj.tagName!="FORM"){
    		theObj=theObj.parentElement;
    }
    var url = "";

    returnValue=window.showModalDialog(con_rule+"?&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+con_name,qry_name.value,"dialogWidth:1000px; dialogHeight:850px; dialogLeft:60px; dialogTop:80px; status:no; directories:yes;scrollbars:no;Resizable=no;help:no");
    if(returnValue == null || returnValue == "" || returnValue == "undefined"){
		qry_name.value="";
	    qry_desc.value="";
	    qry_desc.title="";
        return;
    }else{
				qry_name.value=returnValue;
		var bar_desc = baseXmlSubmit.callAction("../adhoc/adhoc_multiCheck_ajax.jsp?con_id="+con_id+"&idStr="+returnValue);
		bar_desc=bar_desc.replace(/^\s+|\n+$/g,'');
		qry_desc.value = bar_desc;
		qry_desc.title=bar_desc;

/*
	    if(qry_name.value.toString() == ','){
	   		qry_name.value="";
	        qry_desc.value="";
	        qry_desc.title="";

	    }else{
			    acptsite=returnValue.split(",");
			    var idStr = "";
			    var descStr = "";
			    for(var i=0;i<acptsite.length;i++){
			    		var tmp = acptsite[i].split(":");
						idStr+=tmp[0]+",";
						descStr+=tmp[1]+",";
			    }
			    qry_name.value=idStr.substr(0,idStr.length-1);
			    qry_desc.value=descStr.substr(0,descStr.length-1);
			    qry_desc.title=descStr.substr(0,descStr.length-1);
	    }
    */
	}

}

//校验函数
function f_Onblur(theobj,texttype){
	 var re;
	 value = trim(theobj.value);

	 switch(texttype.toUpperCase()){
	 	case "PINT":
	 		checkDura(theobj);
	 		re = /^(0{1})$|^[^0\D]+(\d)*$/;
	 		if(!re.test(value)){
	 			alert("请输入正整数!");
	 			theobj.select();
	 		}
			break;
		case "PINT2":
			checkDura(theobj);
	 		re = /^(0{1})$|^[^0\D]+(\d)*$/;
	 		if(value.length>0){
		 		if(!re.test(value)){
		 			alert("请输入正整数或者为空!");
		 			theobj.select();

		 		}
			}
			break;
		case "INT":
			checkDura(theobj);
	 		re = /^(0{1})$|^-?[^0\D]+(\d)*$/;
	 		if(!re.test(value)){
	 			alert("请输入整数!");
	 			theobj.select();
	 		}
			break;
		case "DEC2":
			checkDura(theobj);
			re = /^(0{1})$|^-?[1-9]+\d*(\.\d{1,2})?$|^0{1}(\.\d{1,2})?$|^-?[0]{1}(\.\d{1,2})?$/;
			if(!re.test(value)){
	 			alert("请输入两位小数!");
	 			theobj.select();
	 		}
			break;
		case "EMAIL":
		    re = /^[_a-zA-Z][_a-zA-Z0-9]*@[_a-z0-9]+\.[a-zA-Z]{2,5}(\.[a-zA-Z]{2,3})?$/;
		    if(!re.test(value)){
	 			alert("请输入正确的EMAIL地址!");
	 			theobj.select();
	 		}
			break;
		case "MP":
		    re =/^1\d{10}$/;
		    if(!re.test(value)){
	 			alert("请输入正确手机号码!");
	 			theobj.select();
	 		}
			break;
		case "AGE":
			checkDura(theobj);
			re = /^(0{1})$|^[^0\D]+(\d)*$/;
	 		if(value.length>0){
		 		if(!re.test(value)){
		 			alert("请输入正整数年龄或者为空!");
		 			theobj.select();

		 		}
			}
		    if(value >120 || value <=0){
	 			alert("请输入正确年龄!");
	 			theobj.select();
	 		}
			break;
		default:
			if(element.value.length == 0){
	 			break;
			}
	}
}
//
function trim(str)
{
	for(var i = 0 ; i<str.length && str.charAt(i)==" " ; i++ ) ;
	for(var j =str.length; j>0 && str.charAt(j-1)==" " ; j--) ;
	if(i>j) return "";
	return str.substring(i,j);
}
//
function checkDura(theobj){
	 if(theobj.name.indexOf("_A_22") >0){
		value1 = trim(theobj.value);
		obj1 =  theobj.name.substring(0,theobj.name.length-5);
		value2 = eval("document.all."+obj1+"_A_11.value");
		if(value2>value1){
			alert('起始数据应小于等于结束数据！');
			theobj.select();
		}
	 }

	  if(theobj.name.indexOf("_A_11") >0){
		value1 = trim(theobj.value);
		obj1 =  theobj.name.substring(0,theobj.name.length-5);
		value2 = eval("document.all."+obj1+"_A_22.value");
		if((value2!=null && value2!="")&& value2<value1){
			alert('起始数据应小于等于结束数据！');
			theobj.select();
		}
	 }
}


function onLoadWindow(sortFlag){
	pageOnLoad();
	selfDisp();
	//closeWaitWin();
}
</script>

</head>

<body style="background-color:White" onload="onLoadWindow('<%=orderflag%>')">
<div id="maincontent">
<div class="toptag">
你所在位置：即席查询
              &gt;&gt; <a href="javascript:;" class="red">结果查询 <%=tmpValue%></a>
</div>

<div class="formbox">
<form name="form1"  method="post" action="" >
<table width="100%">

<input type="hidden" name="<%=AdhocConstant.ADHOC_ROOT%>" value="<%=qryStruct.adhoc_id%>">

<input	type="hidden" name="conStr001">
<input	type="hidden" name="dimStr001">
<input	type="hidden" name="msuStr001">
<input	type="hidden" name="<%=AdhocConstant.ADHOC_FAVORITE_ID%>" value="<%=favinfo.getFavorite_id()%>" />


  <tr>
    <td class="search-bg">
     <div class="fd_title">
 		查询条件
 	</div>
 	</td>
  </tr>
  <tr>
    <td>
    <!-- 条件显示 -->
    <table width="100%" border="1" cellpadding="0" >
			<%
			for(int i=0;conMeta!=null&&i<conMeta.length;i++){
				if(i%3 == 0){
					out.print("<tr>\n");
				}
				if(conMeta[i].isDefault()){
					out.print("<td width=\"3%\" align=\"right\"><input type=\"checkbox\" name=\""+AdhocConstant.ADHOC_CONDITION_CHECK_NAME+"\" value=\""+conMeta[i].getCon_id()+"\" checked disabled></td>\n");
				}else{
					out.print("<td width=\"3%\" align=\"right\"><input type=\"checkbox\" name=\""+AdhocConstant.ADHOC_CONDITION_CHECK_NAME+"\" value=\""+conMeta[i].getCon_id()+"\" checked ></td>\n");
				}
				out.println("<td align=\"right\" width=\"10%\" title=\""+conMeta[i].getCon_desc().trim()+"\">"+conMeta[i].getCon_name().trim());
				out.println("<input type=\"hidden\" name=\"H_"+AdhocConstant.ADHOC_CONDITION_CHECK_NAME+"\" value=\""+conMeta[i].getCon_id()+"\"/>");
				out.println("</td>");
				out.println("<td width=\"15%\" align=\"left\">");

				if(AdhocConstant.ADHOC_CONDITION_TYPE_1.equals(conMeta[i].getCon_type())){//简单文本框
					out.print("<input type=\"text\" name=\""+conMeta[i].getQry_name().trim()+"\" class=\"txtinput\" value=\"\" "+conMeta[i].getValidator()+">");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_20.equals(conMeta[i].getCon_type())){//不等于情况
					out.print("<input type=\"text\" name=\""+conMeta[i].getQry_name().trim()+"\" class=\"txtinput\" value=\"\" "+conMeta[i].getValidator()+">");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_18.equals(conMeta[i].getCon_type())){//is null
					out.print("&nbsp;&nbsp;<input type=\"text\" readonly name=\""+conMeta[i].getQry_name().trim()+"\" class=\"txtinput\" value=\"是\">");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_19.equals(conMeta[i].getCon_type())){//is not null
					out.print("&nbsp;&nbsp;<input type=\"text\" readonly name=\""+conMeta[i].getQry_name().trim()+"\" class=\"txtinput\" value='是'>");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_16.equals(conMeta[i].getCon_type())){//文本区域
					out.print("<TEXTAREA  ROWS=\"5\" COLS=\"20\" name=\""+conMeta[i].getQry_name().trim()+"\">" +conMeta[i].getValidator()+"</TEXTAREA>");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_17.equals(conMeta[i].getCon_type())){//上传文件格式
				%>
				 <input name="<%=conMeta[i].getQry_name()+"_desc"%>"  type="text" size="12" id="mulitSelect" onMouseOver="showTitle(this);">
             					 <input type="hidden" name="<%=conMeta[i].getQry_name()%>">
             					 <img src="../images/system/icon-hit.gif" width="16" height="16"  onclick="javascript:userUpFileCondition(this,'<%=adhoc%>','<%=conMeta[i].getCon_id()%>',<%=conMeta[i].getQry_name()%>,<%=conMeta[i].getQry_name()+"_desc"%>,'<%=conMeta[i].getCon_name()%>');">
				<%

				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_2.equals(conMeta[i].getCon_type())){//起始文本框，结束文本框
					out.print("<input type=\"text\" name=\""+conMeta[i].getQry_name().trim()+"_A_11"+"\" class=\"txtinput\" value=\"\" "+conMeta[i].getValidator()+">");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_3.equals(conMeta[i].getCon_type())){//下拉列表框（指定SQLID）
				%>
					<BIBM:TagSelectList listName="<%=conMeta[i].getQry_name().trim()%>" listID="<%=conMeta[i].getCon_rule()%>" script=" class=query-input-text "/>
				<%
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_4.equals(conMeta[i].getCon_type())){//简单单选纽（指定SQLID）
				%>
					<BIBM:TagRadio radioName="<%=conMeta[i].getQry_name().trim()%>"  radioID="<%=conMeta[i].getCon_rule()%>" script=" class=query-input-text "/>
				<%
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_5.equals(conMeta[i].getCon_type())){//多选button
				%>
				 <input name="<%=conMeta[i].getQry_name()+"_desc"%>"  type="text" size="12" id="mulitSelect" onMouseOver="showTitle(this);">
             					 <input type="hidden" name="<%=conMeta[i].getQry_name()%>">
             					 <img src="../images/system/icon-hit.gif" width="16" height="16"  onclick="javascript:MultipleSelected(this,'<%=adhoc%>','<%=conMeta[i].getCon_id()%>',<%=conMeta[i].getQry_name()%>,<%=conMeta[i].getQry_name()+"_desc"%>,'<%=conMeta[i].getCon_name()%>');">
				<%

				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_6.equals(conMeta[i].getCon_type())){//单选button
				%>
				<input name="<%=conMeta[i].getQry_name()+"_desc"%>"  type="text" size="12" id="acptSite" readonly>
             					<input type="hidden" name="<%=conMeta[i].getQry_name()%>">
             					<input type="button" name="btnAcptSite" value="..." onclick="javascript:selectSite(this,'<%=conMeta[i].getCon_id()%>',<%=conMeta[i].getQry_name()%>,<%=conMeta[i].getQry_name()+"_desc"%>);">
				<%
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_7.equals(conMeta[i].getCon_type())){//日
					out.print("<input class=\"Wdate\" type=\"text\" name=\""+conMeta[i].getQry_name().trim()+"\" size=\"15\" value=\"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true})\" />");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_8.equals(conMeta[i].getCon_type())){//月
					out.print("<input class=\"Wdate\" type=\"text\" name=\""+conMeta[i].getQry_name().trim()+"\" size=\"15\" value=\""+conMeta[i].getValidator()+"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})\" />");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_9.equals(conMeta[i].getCon_type())){//日期时间断
					out.print("<input class=\"Wdate\" type=\"text\" name=\""+conMeta[i].getQry_name().trim()+"_A_11"+"\" size=\"15\" value=\"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true})\" />");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_10.equals(conMeta[i].getCon_type())){//月份时间段
					out.print("<input class=\"Wdate\" type=\"text\" name=\""+conMeta[i].getQry_name().trim()+"_A_11"+"\" size=\"15\" value=\""+conMeta[i].getValidator()+"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})\" />");
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_11.equals(conMeta[i].getCon_type())){//下拉列表框（自定义SQL）
				%>
					<BIBM:TagSelectList listName="<%=conMeta[i].getQry_name().trim()%>"   allFlag=""   listID="0" selfSQL="<%=conMeta[i].getCon_rule()%>" script=" class=query-input-text "/>
				<%
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_12.equals(conMeta[i].getCon_type())){//下拉列表框（内存参数）
				%>
					<BIBM:TagSelectList listName="<%=conMeta[i].getQry_name().trim()%>"    listID="#" selfSQL="<%=conMeta[i].getCon_rule()%>" script=" class=query-input-text "/>
				<%
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_13.equals(conMeta[i].getCon_type())){//简单单选纽（自定义SQL）
				%>
					<BIBM:TagRadio radioName="<%=conMeta[i].getQry_name().trim()%>"    radioID="0" selfSQL="<%=conMeta[i].getCon_rule()%>" script=" class=query-input-text "/>
				<%
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_14.equals(conMeta[i].getCon_type())){//简单单选纽（内存参数）
				%>
					<BIBM:TagRadio radioName="<%=conMeta[i].getQry_name().trim()%>"    radioID="#" selfSQL="<%=conMeta[i].getCon_rule()%>"  script=" class=query-input-text "/>
				<%
				}else if(AdhocConstant.ADHOC_CONDITION_TYPE_15.equals(conMeta[i].getCon_type())){//多选checkBox
				%>
			    <input name="<%=conMeta[i].getQry_name()+"_desc"%>"  type="text" class="query-input-text " size="12" id="mulitSelect" onMouseOver="showTitle(this);">
             					 <input type="hidden" name="<%=conMeta[i].getQry_name()%>">
             					<img src="../images/system/icon-hit.gif" width="16" height="16" style="cursor:hand" onclick="javascript:MultipleCheckSelected(this,'<%=conMeta[i].getCon_rule()%>','<%=conMeta[i].getCon_id()%>',<%=conMeta[i].getQry_name()%>,<%=conMeta[i].getQry_name()+"_desc"%>,'<%=conMeta[i].getCon_name()%>');">
				<%
				}
				out.println("&nbsp;"+AdhocHelper.duraConditionParser(conMeta[i]));
				out.print("</td>\n");
			    //
				//out.print("<td  align=\"left\">"++"</td>\n");

				if((i%3 == 2)||(i==conMeta.length-1)){
					if((i==conMeta.length-1)&&(i%3 != 2)){
						int count = 3-((conMeta.length)%3);
						out.print(AdhocUtil.getNbspTdInnerHtml(count));
					}
					out.print("</tr>\n");
				}
			}
			%>
    </table>
    </td>
  </tr>

  <tr>
        <td>
        <div class="fd_btn">
        <input name="btn_qry" type="button" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="查  询" onclick="CallQuery()">
        <input name="btn_list" type="button" class="btn4" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="提取清单" onclick="QueryList()">
        </div>
        </td>
  </tr>
  <tr>
    <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="100%" class="left-menu">
		<div class="fd_title">查询结果</div>
		</td>
	</tr>
    </table>
    </td>
  </tr>
  <tr>
    <td background="../images/system/bg-css.gif">
    <%=AdhocHelper.getSumTableHTML(session) %>
    </td>
  </tr>
  <tr>
    <td background="../images/system/bg-css.gif">
    <table width="100%"  border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="chart-font">
        <input name="radiobutton" type="radio" value="list" onclick="showJobReason()" checked>列表样章
        <input name="radiobutton" type="radio" value="report" onclick="showJobReason()">报表样章
        </td>
        <td align="right" >
        <div class="fd_btn">
          <input name="btn_export" type="button" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="导 出" onclick="ExportExcel();">
          <input name="btn_close" type="button" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="关 闭" onclick="window.close();">
          </div>
        </td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td>
   <!--显示script部分-->
<%=WebPageTool.pageScript("form1","../adhoc/adhoc_view.jsp")%>
<%
///获取翻页相关信息
PagesInfoStruct pageInfo = WebPageTool.getPageInfo(request, viewList.length, 20 );
%>
<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>


    <!-- <div id="Layer1" style="position:absolute; width:100%; overflow-y:hidden; z-index:1; overflow: auto;"> -->
    <div id="Layer1" >
    <table width="100%"  border="0" cellspacing="0" cellpadding="0">
 		<tr id="list">
      		<td>
      		<table width="100%"  border="0" cellspacing="0" cellpadding="0">
      			<tr>
				<td><%=WebPageTool.pagePolit(pageInfo)%></td>
				</tr>

				<tr>
        		<td>

        		<div class="list_content hasbg">
 				<table width="100%" border="0" cellpadding="0" cellspacing="0">
          		<tr class="tl">
          	<%
           for(int i=0;headArr!=null&&i<headArr.length;i++){
					if(i<dimMeta.length){
						String expandUrlUP = "";
						String expandUrlDOWN = "";
						if(i==dimMeta.length-1&&dimExpandMeta!=null&&dimExpandMeta.length>0){
							String headArrTitle = headArr[i];
							String dimid = "";
							for(int m=0;m<dimExpandMeta.length&&dimExpandMeta.length>1;m++){
								if(headArrTitle.equals(dimExpandMeta[m].getDim_name())){
								  dimid = dimExpandMeta[m].getDim_id();
								  if(m==0){
									expandUrlUP = "";
									expandUrlDOWN = "<a href=\"javascript:CallExpandQuery("+dimid+",'down');\"><img src=\"../images/system/icon-back_over.gif\" title=\"向下扩展\" border=\"0\" width=\"15\" height=\"15\"></a>";
								  }else if(m==dimExpandMeta.length-1){
								    expandUrlUP = "<a href=\"javascript:CallExpandQuery("+dimid+",'up');\"><img src=\"../images/system/icon-forward_over.gif\" title=\"向上收起\" border=\"0\" width=\"15\" height=\"15\"></a>";
									expandUrlDOWN = "";
								  }else{
								    expandUrlUP = "<a href=\"javascript:CallExpandQuery("+dimid+",'up');\"><img src=\"../images/system/icon-forward_over.gif\" title=\"向上收起\" border=\"0\" width=\"15\" height=\"15\"></a>";
									expandUrlDOWN = "<a href=\"javascript:CallExpandQuery("+dimid+",'down');\"><img src=\"../images/system/icon-back_over.gif\" title=\"向下扩展\" border=\"0\" width=\"15\" height=\"15\"></a>";
								  }
								}
							}
						}

						if("".equals(orderflag)){
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+expandUrlUP+"  "+headArr[i]+"<a href=\"#\" onclick=\"sort(1,"+i+",2)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>  "+expandUrlDOWN+"</td>");
						}else if("1".equals(orderflag)&&(Integer.parseInt(sortidx)==i)){
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+expandUrlUP+"  "+headArr[i]+"<a href=\"#\" onclick=\"sort(2,"+i+",2)\"><img src=\"../images/menu_up.gif\" border=\"0\"></a>  "+expandUrlDOWN+"</td>");
						}else if("2".equals(orderflag)&&(Integer.parseInt(sortidx)==i)){
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+expandUrlUP+"  "+headArr[i]+"<a href=\"#\" onclick=\"sort(1,"+i+",2)\"><img src=\"../images/menu_down.gif\" border=\"0\"></a>  "+expandUrlDOWN+"</td>");
						}else{
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+expandUrlUP+"  "+headArr[i]+"<a href=\"#\" onclick=\"sort(1,"+i+",2)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>  "+expandUrlDOWN+"</td>");
						}

					}else{
						if("".equals(orderflag)){
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(1,"+i+",1)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>"+"</td>");
						}else if("1".equals(orderflag)&&(Integer.parseInt(sortidx)==i)){
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(2,"+i+",1)\"><img src=\"../images/menu_up.gif\" border=\"0\"></a>"+"</td>");
						}else if("2".equals(orderflag)&&(Integer.parseInt(sortidx)==i)){
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(1,"+i+",1)\"><img src=\"../images/menu_down.gif\" border=\"0\"></a>"+"</td>");
						}else{
							out.println("<td align=\"center\" class=\"table-item\" title=\""+headDesc[i]+"\" nowrap>"+headArr[i]+"("+msuMeta[i-dimMeta.length].getUnit()+")<a href=\"#\" onclick=\"sort(1,"+i+",1)\"><img src=\"../images/menu_init.gif\" border=\"0\"></a>"+"</td>");
						}
					}

		 	}

          %>
          		</tr>
          <%
			for(int i=0;i<pageInfo.iLinesPerPage && (i+pageInfo.absRowNoCurPage())<pageInfo.iLines;i++){
				String[] value = viewList[i+pageInfo.absRowNoCurPage()];
				String strClassCaption = "table-td";
				String trClass = "bgwl";
				if((i+1)%2==1){
					strClassCaption = "";
					trClass = "bgwl";
				}else{
					strClassCaption = "";
					trClass = "jg";
				}
		  %>
          <tr class="<%=trClass%>" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
					 <%
					 		for(int j=0;j<columnLength;j++){
								if(j<dimMeta.length){
									out.println("<td align=\"left\" class=\"" + strClassCaption + "\" nowrap>"+value[j]+"&nbsp;</td>");
								}else{
									out.println("<td align=\"right\" class=\"" + strClassCaption + "\" nowrap>"+FormatUtil.formatStr(value[j],Integer.parseInt(msuMeta[j-dimMeta.length].getDigit()),true)+"&nbsp;</td>");
								}

					 		}
					 %>
		  </tr>
		  <%
          	}
          %>
       		 	</table>
       		 	</div>

        		</td>
        		</tr>

        	</table>
	  		</td>
	  	</tr>



       <tr id="report" style="display:none">
        <td>
		        <%=AdhocHelper.getReportStyleTable(session)%>

        </td>
       </tr>




    </table>
    </div>
    </td>
    </tr>
</table>
</form>
</div>
</div>
</body>
</html>
<%
//System.out.println("qryStruct.adhoc_id:::sdddddddddddd:" + qryStruct.adhoc_id);

if (orderflag.length()==0){
	AdhocHelper.printJsFun_SelfDisp(out,request,"form1",favinfo,qryStruct.adhoc_id);

}else{
	out.print(((java.lang.StringBuffer)session.getAttribute(AdhocConstant.ADHOC_CONDITION_SCRIPT_FUN)).toString());
}
%>
