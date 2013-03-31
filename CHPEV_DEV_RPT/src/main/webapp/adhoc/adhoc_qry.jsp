<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.adhoc.domain.*"%>
<%@ page import="com.ailk.bi.adhoc.service.impl.*"%>
<%@ page import="com.ailk.bi.adhoc.dao.impl.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.common.app.StringB" %>
<%@ page import="com.ailk.bi.system.facade.impl.CommonFacade" %>
<%@ taglib uri="/WEB-INF/tld/TAG.tld" prefix="Tag" %>

<%
    String roleId = CommonFacade.getUserAdhocRole(session);

	String fav_flag = StringB.NulltoBlank((String)session.getAttribute(AdhocConstant.ADHOC_FAV_FLAG));

	//即席查询ID
	String adhoc = request.getParameter(AdhocConstant.ADHOC_ROOT);
	if (adhoc == null || "".equals(adhoc)) {
		adhoc = (String) session.getAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS);
		if (adhoc == null || "".equals(adhoc)) {
			adhoc = AdhocConstant.ADHOC_ROOT_DEFAULT_VALUE;
		}
	}else{
		session.setAttribute(AdhocConstant.ADHOC_ROOT_WEBKEYS,adhoc);
	}

	//分组属性列表"a","b","c","d"
	//此列表初始化javascript数组,作为分组应用

	String groupTag = AdhocHelper.getAdhocTag(adhoc,session);

	//默认分组属性(树型选中的分组标记，便于切换)
	String group_tag = request.getParameter(AdhocConstant.ADHOC_TABLE_WEBKEYS);
	if (group_tag == null || "".equals(group_tag)) {
		group_tag = (String) session
		.getAttribute(AdhocConstant.ADHOC_TABLE_WEBKEYS);
		if (group_tag == null || "".equals(group_tag)) {
			group_tag = AdhocHelper.getAdhocDefaultTag(adhoc);
		}
	}

	//指标定制页默认选中的属性类页
	String group_msu_tag = request.getParameter(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
	if (group_msu_tag == null || "".equals(group_msu_tag)) {
		group_msu_tag = (String) session
		.getAttribute(AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS);
		if (group_msu_tag == null || "".equals(group_msu_tag)) {
			group_msu_tag = AdhocHelper.getAdhocMsuDefaultTag(adhoc);
		}

	}

	//刷新左侧属性分组树页面标记
	String reloadTree =  request.getParameter(AdhocConstant.ADHOC_RELOAD_TREE_KEY);
	if (reloadTree == null ){
		reloadTree = "";
	}

	//选中的收藏夹信息
	UiAdhocFavoriteDefTable  favinfo  =  (UiAdhocFavoriteDefTable)session.getAttribute(AdhocConstant.ADHOC_FAVORITE_DEF_TABLE_WEBKEYS);
	if(favinfo == null){
		favinfo =  new UiAdhocFavoriteDefTable();
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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统一经营分析系统</title>
<%@ include file="/base/commonHtml.jsp" %>

<script language=javascript src="<%=request.getContextPath()%>/js/js.js"></script>
<script language=javascript src="<%=request.getContextPath()%>/js/dojo.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/wait.js"></script>

<script language=javascript src="<%=request.getContextPath()%>/js/kw.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script language=javascript>
  //初始化全局表格标记数组
  var tableArr=new Array(<%=groupTag%>);

  //
  function pageOnLoad(){
  	//切换定制表格
  	switchTable(tableArr,'<%=group_tag%>','<%=adhoc%>');
  	//切换指标表格
  	//openMsuTree('<%=group_msu_tag%>');
  	//刷新左侧树
  	LoadTree('<%=reloadTree%>','<%=adhoc%>');
  }


  //切换显示当前的表格
function switchTable(theObjIdArray,theShowObjId,theHocId){
	for(var i=0;i<theObjIdArray.length;i++)
	{
		if(theObjIdArray[i]==theShowObjId)
		{
			document.getElementById(theObjIdArray[i]).style.display="";
		}
		else
		{
			document.getElementById(theObjIdArray[i]).style.display="none";
		}
	}
	//设置选择中的表格标记
	setTable(theShowObjId);
	//导航条
	genBar(theHocId,theShowObjId);
	//


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
//生成工具条
function genBar(hoc_id , group_tag){
    if(hoc_id == null || hoc_id == ""){
    	alert("没有指定特定的即席查询业务!");
    	return;
    }
    if(group_tag == null || group_tag == ""){
    	alert("没有分组属性!");
    	return;
    }
    var bar = baseXmlSubmit.callAction("../adhoc/adhoc_ajax.jsp?adhoc_id="+hoc_id+"&group_tag="+group_tag);
    bar=bar.replace(/^\s+|\n+$/g,'');
    //
    document.getElementById("bar").innerHTML = bar;
}
//设置选择中的表格标记
function setTable(theShowObjId){
	tableQryForm.group_tag.value=theShowObjId;
}
//
function openMsuTree(n)
{
	var t1 = "tree" + n;
	var t2 = "icon" + n;


	obj = document.getElementById(t1);
	sIcon = document.getElementById(t2);
	if (obj.style.display == "none")
	{
		sIcon.src="../images/sh/_.gif";
		obj.style.display = "block";//??????????
	}
	else
	{
		sIcon.src="../images/sh/+.gif";
		obj.style.display = "none";
	}
}
//提取界面参数
function FetchParam(value){
	tableQryForm.target="_self";
	tableQryForm.action="AdhocFetchParam.rptdo?adhoc_root="+value;
	tableQryForm.submit();
}
//交换数据行
function swapNode(node1,node2){
	var _parent = node1.parentNode;
	var _t1 = node1.nextSibling;
	var _t2 = node2.nextSibling;
	if(_t1){
		_parent.insertBefore(node2,_t1);
	}else{
		_parent.appendChild(node2);
	}
	if(_t2){
		_parent.insertBefore(node1,_t2);
	}else{
		_parent.appendChild(node1);
	}

}
//上移一行
function moveUp(_a,type){
	var _row = _a.parentNode.parentNode;
	var _first = _row.previousSibling;
	if(type == "1"){
		if(_row.previousSibling&&_first.previousSibling){
			swapNode(_row,_row.previousSibling);
		}
	}else if(type == "2"){
		if(_row.previousSibling&&_first.previousSibling&&_first.previousSibling.previousSibling){
			swapNode(_row,_row.previousSibling);
		}
	}else{
	if(_row.previousSibling&&_first.previousSibling){
		swapNode(_row,_row.previousSibling);
	}
	}


}
//下移一行
function moveDown(_a){
	var _row = _a.parentNode.parentNode;
	if(_row.nextSibling){
		swapNode(_row,_row.nextSibling);
	}

}

//上移一行
function moveRight(_col){
	if(_col.nextSibling){
		swapNode(_col,_col.nextSibling);
	}
}

//下移一行
function moveLeft(_col){
	if(_col.nextSibling){
		swapNode(_col,_col.previousSibling);
	}
}
//删除收藏夹
function DelFav(value){

	 if(confirm("您确定要删除吗？此操作不可恢复!")){

		tableQryForm.target="_self";
		tableQryForm.action="AdhocFav.rptdo?oper_type=del&fav_id="+value;
		tableQryForm.submit();
	 }
}

//增加收藏夹信息
function AddFav11111(){
	//alert("addfav");
	//window.open('adhoc_fav.jsp',"addFavor","width=711,height=310,top=250, left=250,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=auto,resizable=0");
	tableQryForm.target="_blank";//_self
	tableQryForm.action="adhoc_fav.jsp";
	tableQryForm.submit();
}

function AddFav()
{
	 $.blockUI(
	 {
		 message:$('#addFav'),
         css: {
	             top:  ($(window).height() - 310)/2 + 'px',
	             left: ($(window).width() - 711) /2 + 'px',
	             height: '300px',
	        	 width : '700px',
	        	 border : '0',
	        	 padding:	0,
	     		 margin:	0,
	        	 cursor : 'default'
        		}
    	});
}
function closeBlock()
{
	$.unblockUI();
}

function doAddFavor() {
//alert("doAddFavor");
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
	var favName = tableQryForm.fav_name.value;
	//alert(favName);
    if(favName.toString() == 'undefined'|| favName==''){
	  	alert("收藏夹名称提取失败，请通知系统管理员！");
	  	return;
	}

	tableQryForm.target="_self";//&fav_name="+favName+"
	tableQryForm.action="../AdhocFav.rptdo?oper_type=add&con_str="+constr+"&dim_str="+dimstr+"&msu_str="+msustr;
	tableQryForm.submit();
}
//重新定制（清空会话）
function ReCust(){
	tableQryForm.target="_self";
	tableQryForm.action="AdhocCleaner.rptdo"
	tableQryForm.submit();

}
//即席查询视图
function CallQuery(){
	//f_Onblur(theobj,texttype);
	//alert("1已通过验证！！");

	tableQryForm.target="_blank";//_self
	tableQryForm.action="AdhocView.rptdo";
	tableQryForm.submit();
}
//用户清单
function QueryList(){
	 tableQryForm.target="_blank";

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


	 tableQryForm.conStr001.value = constr;
	 tableQryForm.dimStr001.value = dimstr;
	 tableQryForm.msuStr001.value = msustr;

	 tableQryForm.action="AdhocUserList.rptdo";
  	 tableQryForm.submit();
}

//收藏夹收藏
function ShowFavorite(id,name){
  tableQryForm.target="_self";
  tableQryForm.action="AdhocFav.rptdo?fav_id="+id+"&fav_name="+name;
  tableQryForm.submit();
}

//刷新左侧树
function LoadTree(value,root){
	if(value == 'true'){
		parent.adhoc_leftFrame.location.href='adhoc_left.jsp?adhoc_root='+root;
	}
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
	var time =new Date();
    // window.open("AdhocUserUpLoadCon.rptdo?Rnd="+Math.random()+"&adhoc_id="+hoc_id+"&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+con_name);
	returnValue=window.showModalDialog("AdhocUserUpLoadCon.rptdo?Rnd="+Math.random()+"&adhoc_id="+hoc_id+"&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+encodeURI(encodeURI(con_name)) + "time=" + time,qry_name.value,"dialogWidth:600px; dialogHeight:450px; dialogLeft:320px; dialogTop:250px; status:no; directories:yes;scrollbars:no;Resizable=no;help:no");

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


  function open_metaExplain(adhoc_id)
    {
        var h = "500";
        var w = "750";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;
        var optstr = "height=" + h + ",width=" + w + ",left=" + left + ",top=" + top + ",status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no";
        var strUrl = "../adhoc/adhocMetaExplain.rptdo?adhoc_id=" + adhoc_id;
        var newsWin = window.open(strUrl, "editRptHead", optstr);
        if (newsWin != null) {
            newsWin.focus();
        }
    }

//objId 条件ID
//objValue  条件英文名
//objDesc   条件英文名描述
//objName   条件名称
//javascript:MultipleSelected(this,'202',qry__innet_channel,qry__innet_channel_desc,'入网渠道');
function MultipleSelected(theObj,hoc_id,con_id,qry_name,qry_desc,con_name){
    //
	var time =new Date();
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
	returnValue=window.showModalDialog("AdhocMultiSelect.rptdo?adhoc_id="+hoc_id+"&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+encodeURI(encodeURI((con_name+conStr)))+"&time=" + time ,window,"dialogWidth:805px; dialogHeight:475px; dialogLeft:320px; dialogTop:250px; status:no; directories:yes;scrollbars:no;Resizable=no;help:no");

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
	var alreadyValue = "";
    var url = "";
   	var time = new Date();
    returnValue=window.showModalDialog(con_rule+"?&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+con_name + "&time=" +time,qry_name.value,"dialogWidth:1000px; dialogHeight:850px; dialogLeft:60px; dialogTop:80px; status:no; directories:yes;scrollbars:yes;Resizable=no;help:no");
	//alert(returnValue);

  //window.open(con_rule+"?&con_id="+con_id+"&qry_name="+qry_name.value+"&con_name="+con_name + "&time=" +time)
    if(returnValue == null || returnValue == "" || returnValue == "undefined"){

		qry_name.value="";
	    qry_desc.value="";
	    qry_desc.title="";

        return;
    }else{
		if (qry_name.value=="")
		{
			alreadyValue = returnValue;
		}else{
			alreadyValue = qry_name.value +  "," + returnValue;
		}

		qry_name.value=alreadyValue;

		var bar_desc = baseXmlSubmit.callAction("../adhoc/adhoc_multiCheck_ajax.jsp?con_id="+con_id+"&idStr="+alreadyValue);
		bar_desc=bar_desc.replace(/^\s+|\n+$/g,'');
		qry_desc.value = bar_desc;
		qry_desc.title=bar_desc;


		//qry_name+"_desc".value = "sdfdsftest";
	/*
		alert(qry_name.value);
	    if(qry_name.value.toString() == ''){
	   		qry_name.value="";
	        qry_desc.value="";
	        qry_desc.title="";

	    }else{
			alert(returnValue);
				qry_name.value=returnValue;

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

//搜索指标、条件、维度
function searchMsu_Con_Dim(adhocId){
    //
    var returnValue;
	var acptsite;
   	var time = new Date();
	var conArray = new Array();
	var dimArray = new Array();
	var msuArray = new Array();
	var iCon = 0;
	var iDim = 0;
	var iMsu = 0;

	  var h = "650";
        var w = "600";
        var top = (screen.availHeight - h) / 2;
        var left = (screen.availWidth - w) / 2;

   returnValue=window.showModalDialog("adhocSearchInfo.rptdo?adhocId="+adhocId+"&time=" +time,"搜索","dialogWidth:" + w + "px; dialogHeight:" + h + "px; dialogLeft:" + left + "px; dialogTop:" + top + "px; status:no; directories:yes;scrollbars:yes;Resizable=no;help:no");
	//alert(returnValue);

  // window.open("adhocSearchInfo.rptdo?adhocId="+adhocId + "&time=" +time)
    if(returnValue == null || returnValue == "" || returnValue == "undefined"){
        return;
    }else{

		acptsite=returnValue.split(",");
		var idStr = "";
		var descStr = "";
		for(var i=0;i<acptsite.length;i++){
			var tmp = acptsite[i].split("|");
			var strFlag = tmp[1];
			if (strFlag=="CON_NAME")
			{
				conArray[iCon] = tmp[0];
				iCon = iCon + 1;
			}else if(strFlag=="WD_NAME"){
				dimArray[iDim] = tmp[0];
				iDim = iDim + 1;
			}else if(strFlag=="DL_NAME")
			{
				msuArray[iMsu] = tmp[0];
				iMsu = iMsu + 1;
			}
		}

	//	alert(conArray.length);

		for(var i=0;i<conArray.length;i++){
			var oCk = document.all.CON_NAME;
			for(var j=0;j<oCk.length;j++){
				if (oCk[j].value==conArray[i])
				{
					//alert(oCk[j].value);
					oCk[j].checked = true;
					break;
				}
			}
		}

		for(var i=0;i<dimArray.length;i++){
			var oCk = document.all.WD_NAME;
			for(var j=0;j<oCk.length;j++){
				if (oCk[j].value==dimArray[i])
				{
					//alert(oCk[j].value);
					oCk[j].checked = true;
					break;
				}
			}
		}

		for(var i=0;i<msuArray.length;i++){
			var oCk = document.all.DL_NAME;
			for(var j=0;j<oCk.length;j++){
				if (oCk[j].value==msuArray[i])
				{
					//alert(oCk[j].value);
					oCk[j].checked = true;
					break;
				}
			}
		}

		FetchParam(adhocId);
	}

}

//删除对应的条件，纬度以及指标
function deleteFixed(type,id){
	tableQryForm.target="_self";
	tableQryForm.action="../adhoc/AdhocFetchParam.rptdo?oper_type=1&type="+type+"&id="+id;
	tableQryForm.submit();
}
//自定义纬度
function doFixedDim(hoc_id , column_field){
	if(column_field == null || column_field == ""){
		alert('定制纬度字段为空！');
		return;
	}
	//
	window.open("../adhoc/AdhocSelfDim.rptdo?hoc_id="+hoc_id+"&col_field="+column_field,"jin","height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes, resizable=yes");
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
			if(!re.test(value))
			{
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
		if(value2 > value1)
		{
			alert('起始数据应小于等于结束数据！');
			theobj.select();
		}
	 }

	  if(theobj.name.indexOf("_A_11") >0){
		value1 = trim(theobj.value);
		obj1 =  theobj.name.substring(0,theobj.name.length-5);
		value2 = eval("document.all."+obj1+"_A_22.value");
		if((value2!=null && value2!="")&& value2 < value1)
		{
			alert('起始数据应小于等于结束数据！');
			theobj.select();
		}
	 }
}


</script>

</head>
<body onload="pageOnLoad();selfDisp();" style="background-color:White">
<div id="maincontent">

<form name="tableQryForm" method="post" action="">
<input	type="hidden" name="<%=AdhocConstant.ADHOC_ROOT%>" value="<%=adhoc%>" />
<input	type="hidden" name="<%=AdhocConstant.ADHOC_TABLE_WEBKEYS%>" value="<%=group_tag%>" />
<input	type="hidden" name="<%=AdhocConstant.ADHOC_MSU_TABLE_WEBKEYS%>" value="<%=group_msu_tag%>" />
<input	type="hidden" name="<%=AdhocConstant.ADHOC_FAVORITE_ID%>" value="<%=favinfo.getFavorite_id()%>" />
<input	type="hidden" name="fav_name" value="" />
<input	type="hidden" name="myFavor_id">
<input	type="hidden" name="conStr001">
<input	type="hidden" name="dimStr001">
<input	type="hidden" name="msuStr001">
<div class="toptag">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="position" id="bar" >
			<img src="../biimages/arrow7.gif" width="7" height="7"> <span class="bulefont">你所在位置： </span>即席查询	111 &gt;&gt; 条件区定制 &gt;&gt; 基本条件
			</td>
		</tr>
	</table>
</div>
<div class="formbox">
	<div class="formdiv1">
		<div class="fd_title">
			<span class="icon title">定制区选择</span>
		</div>
		<table width="100%" border="0">
		<%
		out.println(AdhocHelper.getCustomizeZoneTableHtml(adhoc, session));
		%>
		</table>
		<div class="fd_btn"><input onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" type="button" name="Submit" value="增 加" class="btn3" onclick="FetchParam('<%=adhoc%>')">&nbsp;&nbsp;<input type="button" name="btnsearch" value="搜 索" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" onclick="searchMsu_Con_Dim('<%=adhoc%>')"></div>
	</div>
	<div class="formdiv2">
		<div class="fd_title">
			<span class="icon title">已选条件</span>
		</div>
		<div class="fd_content">
			<div class="widget_zj">
			<table width="100%">
				<tr>
					<th align="center"  width="20%">条件说明</th>
					<th align="center"  width="25%">条件数值1</th>
					<th align="center"  width="25%">条件数值2</th>
					<th align="center"  width="8%">是否应用</th>
					<th align="center"  width="8%">删除</th>
					<th align="center"  width="8%" class="last">显示顺序</th>
				</tr>
				<%
				String[]  conArr  = (String[])session.getAttribute(AdhocConstant.ADHOC_CONDITION_WEBKEYS);
				if(conArr == null){
					conArr = new String[0];
				}
				String conStr = "";
				for(int i=0;conArr!=null&&i<conArr.length;i++){
					if(conStr.length()>0){
						conStr+= ",";
					}
					conStr += "'"+conArr[i]+"'";
				}
				AdhocFacade facade = new AdhocFacade(new AdhocDao());
				ArrayList selectedList = null;
				//已经选中
				if(!"".equals(conStr)){
					selectedList = (ArrayList)facade.getConListByString(conStr,adhoc,roleId);
				}

				//默认选择纬度
				//ArrayList conList = (ArrayList)facade.getDefaultConListByHocId(adhoc,roleId);
				ArrayList conList = (ArrayList)	session.getAttribute(AdhocConstant.ADHOC_DEFAULT_CONDITION_LIST);

				//融和
				ArrayList list = new ArrayList();
				if(conList!=null&&!conList.isEmpty()){
					for(int i=0;i<conList.size();i++){
						list.add(conList.get(i));
					}
				}

				//
				if(selectedList!=null&&!selectedList.isEmpty()){
					for(int i=0;i<selectedList.size();i++){
						list.add(selectedList.get(i));
					}
				}

				//
				if(list!=null&&!list.isEmpty()){
					//
					ArrayList arr =  (ArrayList)list;
					UiAdhocConditionMetaTable[]  con_meta= (UiAdhocConditionMetaTable[]) arr.toArray(new UiAdhocConditionMetaTable[arr.size()]);
					session.setAttribute(AdhocConstant.ADHOC_CONDITION_SELECTED,con_meta);

					for(int i=0;con_meta!=null&&i<con_meta.length;i++){


			   %>
				<tr>
					<td  align="center" title="<%= con_meta[i].getCon_desc().trim() %>"><%=con_meta[i].getCon_name().trim()%>
					<%
					out.println("<input type=\"hidden\" name=\"H_"+AdhocConstant.ADHOC_CONDITION_CHECK_NAME+"\" value=\""+con_meta[i].getCon_id()+"\"/>");
					%>
					</td>

					<td align="left">
					    <%
						if(AdhocConstant.ADHOC_CONDITION_TYPE_1.equals(con_meta[i].getCon_type())){//简单文本框
							out.print("&nbsp;&nbsp;<input type=\"text\" name=\""+con_meta[i].getQry_name().trim()+"\" size=\"15\" class=\"txtinput\" value=\"\" "+con_meta[i].getValidator()+">");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_20.equals(con_meta[i].getCon_type())){//不等于情况
							out.print("&nbsp;&nbsp;<input type=\"text\" name=\""+con_meta[i].getQry_name().trim()+"\" size=\"15\" class=\"txtinput\" value=\"\" "+con_meta[i].getValidator()+" >");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_16.equals(con_meta[i].getCon_type())){//textarea
							out.print("<TEXTAREA ROWS=\"5\" COLS=\"20\" name=\""+con_meta[i].getQry_name().trim()+"\">" +con_meta[i].getValidator()+"</TEXTAREA>");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_18.equals(con_meta[i].getCon_type())){//is null
							out.print("&nbsp;&nbsp;<input type=\"text\" readonly name=\""+con_meta[i].getQry_name().trim()+"\" size=\"15\" class=\"txtinput\" value=\"是\">");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_19.equals(con_meta[i].getCon_type())){//is not null
							out.print("&nbsp;&nbsp;<input type=\"text\" readonly name=\""+con_meta[i].getQry_name().trim()+"\" size=\"15\" class=\"txtinput\" value='是'>");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_17.equals(con_meta[i].getCon_type())){//上传文件类型
						%>
				  			&nbsp;&nbsp;<input name="<%=con_meta[i].getQry_name()+"_desc"%>" type="text" size="15" class="txtinput" readonly id="mulitSelect" onMouseOver="showTitle(this);"><input type="hidden" name="<%=con_meta[i].getQry_name()%>"><img src="<%=request.getContextPath()%>/images/system/icon-hit.gif" width="16" height="16" style="cursor:hand" onclick="javascript:userUpFileCondition(this,'<%=adhoc%>','<%=con_meta[i].getCon_id()%>',<%=con_meta[i].getQry_name()%>,<%=con_meta[i].getQry_name()+"_desc"%>,'<%=con_meta[i].getCon_name()%>');">
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_2.equals(con_meta[i].getCon_type())){//起始文本框，结束文本框
							out.print(">=<input type=\"text\" name=\""+con_meta[i].getQry_name().trim()+"_A_11"+"\" size=\"15\" class=\"txtinput\" value=\"\" "+con_meta[i].getValidator()+">");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_3.equals(con_meta[i].getCon_type())){//下拉列表框（指定SQLID）
						%>
							&nbsp;&nbsp;<BIBM:TagSelectList listName="<%=con_meta[i].getQry_name().trim()%>" allFlag="" listID="<%=con_meta[i].getCon_rule()%>" script=" class=txtinput " />
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_4.equals(con_meta[i].getCon_type())){//简单单选纽（指定SQLID）
						%>
							&nbsp;&nbsp;<BIBM:TagRadio radioName="<%=con_meta[i].getQry_name().trim()%>" allFlag="" radioID="<%=con_meta[i].getCon_rule()%>" script=" class=txtinput " />
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_5.equals(con_meta[i].getCon_type())){//多选button
						%>
							&nbsp;&nbsp;<input name="<%=con_meta[i].getQry_name()+"_desc"%>" type="text" size=\"15\" class="txtinput" readonly id="mulitSelect" onMouseOver="showTitle(this);"><input type="hidden" name="<%=con_meta[i].getQry_name()%>"><img src="<%=request.getContextPath()%>/images/system/icon-hit.gif" width="16" height="16" style="cursor:hand" onclick="javascript:MultipleSelected(this,'<%=adhoc%>','<%=con_meta[i].getCon_id()%>',<%=con_meta[i].getQry_name()%>,<%=con_meta[i].getQry_name()+"_desc"%>,'<%=con_meta[i].getCon_name()%>');">
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_6.equals(con_meta[i].getCon_type())){//单选button
						%>
							&nbsp;&nbsp;<input name="<%=con_meta[i].getQry_name()+"_desc"%>" type="text" size=\"15\" class="txtinput" id="acptSite" readonly><input type="hidden" name="<%=con_meta[i].getQry_name()%>"><input type="button" name="btnAcptSite" value="..." onclick="javascript:selectSite(this,'<%=con_meta[i].getCon_id()%>',<%=con_meta[i].getQry_name()%>,<%=con_meta[i].getQry_name()+"_desc"%>);">
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_7.equals(con_meta[i].getCon_type())){//日
							out.print("&nbsp;&nbsp;<input class=\"Wdate\" type=\"text\" name=\""+con_meta[i].getQry_name().trim()+"\" size=\"15\" value=\"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true})\" />");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_8.equals(con_meta[i].getCon_type())){//月
							out.print("&nbsp;&nbsp;<input class=\"Wdate\" type=\"text\" name=\""+con_meta[i].getQry_name().trim()+"\" size=\"15\" value=\""+con_meta[i].getValidator()+"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})\" />");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_9.equals(con_meta[i].getCon_type())){//日期时间断
							out.print(">=<input class=\"Wdate\" type=\"text\" name=\""+con_meta[i].getQry_name().trim()+"_A_11"+"\" size=\"15\" value=\"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true})\" />");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_10.equals(con_meta[i].getCon_type())){//月份时间段
							out.print(">=<input class=\"Wdate\" type=\"text\" name=\""+con_meta[i].getQry_name().trim()+"_A_11"+"\" size=\"15\" value=\""+con_meta[i].getValidator()+"\" onFocus=\"WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyyMM'})\" />");
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_11.equals(con_meta[i].getCon_type())){//下拉列表框（自定义SQL）
						%>
							&nbsp;&nbsp;<BIBM:TagSelectList listName="<%=con_meta[i].getQry_name().trim()%>"  allFlag=""  listID="0" selfSQL="<%=con_meta[i].getCon_rule()%>" script=" class=txtinput "/>
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_12.equals(con_meta[i].getCon_type())){//下拉列表框（内存参数）
						%>
							&nbsp;&nbsp;<BIBM:TagSelectList listName="<%=con_meta[i].getQry_name().trim()%>"   allFlag="" listID="#" selfSQL="<%=con_meta[i].getCon_rule()%>" script=" class=txtinput "/>
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_13.equals(con_meta[i].getCon_type())){//简单单选纽（自定义SQL）
						%>
							&nbsp;&nbsp;<BIBM:TagRadio radioName="<%=con_meta[i].getQry_name().trim()%>"    allFlag="" radioID="0" selfSQL="<%=con_meta[i].getCon_rule()%>"/>
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_14.equals(con_meta[i].getCon_type())){//简单单选纽（内存参数）
						%>
							&nbsp;&nbsp;<BIBM:TagRadio radioName="<%=con_meta[i].getQry_name().trim()%>"    allFlag="" radioID="#" selfSQL="<%=con_meta[i].getCon_rule()%>" script=" class=txtinput "/>
						<%
						}else if(AdhocConstant.ADHOC_CONDITION_TYPE_15.equals(con_meta[i].getCon_type())){//多选checkBox
						%>
							&nbsp;&nbsp;<input name="<%=con_meta[i].getQry_name()+"_desc"%>" type="text" size=\"15\" class="txtinput" id="mulitSelect" onMouseOver="showTitle(this);"><input type="hidden" name="<%=con_meta[i].getQry_name()%>"><img src="<%=request.getContextPath()%>/images/system/icon-hit.gif" width="16" height="16" style="cursor:hand" onclick="javascript:MultipleCheckSelected(this,'<%=con_meta[i].getCon_rule()%>','<%=con_meta[i].getCon_id()%>',<%=con_meta[i].getQry_name()%>,<%=con_meta[i].getQry_name()+"_desc"%>,'<%=con_meta[i].getCon_name()%>');">
						<%
						}
					    %>
					    </td>
					    <%
					    //条件2
						out.print("<td align=\"left\" >" +AdhocHelper.duraConditionParser(con_meta[i])+"</td>\n");
						//选中框
						if(con_meta[i].getGroup_id().equals("")){
							out.print("<td align=\"center\">&nbsp;<input type=\"checkbox\" name=\""+AdhocConstant.ADHOC_CONDITION_CHECK_NAME+"\" value=\""+con_meta[i].getCon_id()+"\" checked /></td>\n");
						}else{
							out.print("<td align=\"center\">&nbsp;<input type=\"checkbox\" name=\""+AdhocConstant.ADHOC_CONDITION_CHECK_NAME+"\" value=\""+con_meta[i].getCon_id()+"\" checked disabled /></td>\n");
						}
						//删除
						if(con_meta[i].isDefault()){
							out.print("<td align=\"center\" >" +
									"<a href=\"javascript:;\" class=\"icon del\"></a>" +
									"</td>\n");
						}else{
							out.print("<td align=\"center\" >" +
									"<a href=\"javascript:;\" class=\"icon del\" onclick=\"deleteFixed('con','"+con_meta[i].getCon_id()+"')\"></a>" +
									"</td>\n");
						}

						//上下移动
						out.print("<td class=\"last\" align=\"center\"><a href=\"javascript:;\" class=\"icon1 up\" onclick=\"moveUp(this)\"></a>&nbsp;<a href=\"javascript:;\" class=\"icon1 down\" onclick=\"moveDown(this)\"></a></td>\n");
					   %>
				 </tr>
				<%} }%>
			</table>
			</div>
		</div>
	</div>
	<div class="formdiv3">
		<div class="fd_title">
			<span class="icon title">已定制结果</span>
		</div>
		<div class="fd_content">
			<div class="widget_zj">
			<%=AdhocHelper.getSelectedDimResult(adhoc, session)%>
			<br>
			<%=AdhocHelper.getSelectedMsuResult(adhoc, session)%>
			</div>
		</div>
		<div class="fd_btn">
			<%if(!"".equals(favinfo.getFavorite_id())&& !"1".equals(fav_flag)){ %>
			<input name="del_fav_btn"	type="button" class="btn4" onMouseOver="switchClass2(this)"  onMouseOut="switchClass2(this)" value="删除收藏夹" onClick="DelFav('<%=favinfo.getFavorite_id()%>')">
			<%} %>
			<%if(!"1".equals(fav_flag)){ %>
			<input name="fav_btn"	type="button" class="btn4" onMouseOver="switchClass2(this)"  onMouseOut="switchClass2(this)" value="添加收藏夹" onClick="AddFav();"> <!--addMyFavourate();  -->
			<%} %>
			<input type="hidden" name="addFavor" onclick="doAddFavor()">
			<input name="redo" type="button" class="btn4" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" value="重新定制" onClick="ReCust()">
			<input name="qry_btn" type="button" class="btn3" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)" 	onClick="CallQuery()" value="查 询">
			<input name="list_btn" type="button" class="btn4" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"	value="提取清单" onClick="QueryList()">
		</div>
		<div id="addFav" style="display: none;">
			<iframe id="helps" src="adhoc_fav.jsp" width="711px" height="310px"
				frameborder="0" scrolling="no"> </iframe>
	    </div>
	</div>
</div>
</form>
</div>
</body>
</html>
<%
AdhocHelper.printJsFun_SelfDisp(out,request,"tableQryForm",favinfo,adhoc);
%>