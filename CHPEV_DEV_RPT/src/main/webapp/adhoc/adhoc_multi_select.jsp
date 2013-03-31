<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.base.util.CommTool"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>

<%
			request.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
			response.setHeader("Pragma", "no-cache"); //HTTP 1.0
			//切分符
			String split_char = AdhocConstant.ADHOC_SPLIT_CHAR;
			//随机数
			java.util.Random rnd = new java.util.Random(System.currentTimeMillis());
			//
			String sqlID = "" + rnd.nextLong();
			//选择的条件
			if (null == session.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_ID_SESSION)){
				return;
			}
			String con_id = (String) session.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_ID_SESSION);

			//条件名称
			String con_name=(String)session.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_NAME_SESSION);
			if(null==con_name){
				con_name="";
			}

		    //业务类型 GSM后付费业务
		    String svcknd = CommTool.getParameterGB(request, AdhocConstant.ADHOC_MULTI_SELECT_QRY_SVC_KND);
		    if (null == svcknd||"".equals(svcknd)){
		    	svcknd = "11";
		    }

			//区域 移动业务渠道发展管理部
			String qry_areaid	= CommTool.getParameterGB(request, AdhocConstant.ADHOC_MULTI_SELECT_QRY_AREA_ID);
		    if(null == qry_areaid||"".equals(qry_areaid)){
		    	qry_areaid = "1";
		    }

			//System.out.println(qry_areaid + ":????????????>>>>>>>>>>>>>>>>>>>>>>");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=con_name %>-多项选择</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css"
	type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/icontent.css" type="text/css">
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
<style type="text/css">
<!--
.bg {
	background-color: #E8E8E8;
	border: 1px solid #CCCCCC;
}
-->
</style>


</head>
<script language="JavaScript">
<!--
	var split_char="<%=split_char%>";

	function MM_reloadPage(init) {  //reloads the window if Nav4 resized
	  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
	    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
	  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
	}
	MM_reloadPage(true);

	//右边增加选择的文件夹和节点
	function addToRight(){
		var left=self.left;
		var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
				+"addToRight();\n"
				+"</SCRIPT"+">\n";
		left.tmpDiv.innerHTML ="&nbsp;"+tmpStr;
	}

	function addAllToRight(){
		var left=self.left;
		var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
				+"addAllToRight();\n"
				+"</SCRIPT"+">\n";
		left.tmpDiv.innerHTML ="&nbsp;"+tmpStr;
	}

	//右边减少选择的文件夹和节点
	function addToLeft(){
		var right=self.right;
		var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
				+"addToLeft();\n"
				+"</SCRIPT"+">\n";
		right.tmpDiv.innerHTML ="&nbsp;"+tmpStr;
	}

	//右边减少选择的文件夹和节点
	function addAllToLeft(){
		var right=self.right;
		var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
				+"addAllToLeft();\n"
				+"</SCRIPT"+">\n";
		right.tmpDiv.innerHTML ="&nbsp;"+tmpStr;
	}

	//过滤查询
	function search(){
		//关键字
		var search_word=self.document.getElementsByName("search_word")[0].value;
		//区域
		var areaid=self.document.all.qry__area_id.value;
		//联动二级信息
		var sec_area=self.document.all.Nclassid.value;
		//刷新
		self.left.location.href="adhoc_multi_leftselect.jsp?subtype=1&sec_area_id="+sec_area+"&area_id="+areaid+"&con_id=<%=con_id%>&search_word="+search_word+"&svc_knd="+<%=svcknd%>;
	}

	//确定提交
	function selectSubmit(){
		  var retStr="<%=sqlID%>";
		  var hiddenStr="<%=sqlID%>$$<%=con_id%>";
		  var allid="";
		  var alldesc="";
		  var childValues=self.right.document.getElementsByName("childValues")[0].value;
		  var childDescs=self.right.document.getElementsByName("childDescs")[0].value;
		var reStr=split_char+"_"+split_char;
		while(childDescs.indexOf(reStr)>=0){
			var fir=childDescs.substring(0,childDescs.indexOf(reStr));
			var sec=childDescs.substring(childDescs.indexOf(reStr)+reStr.length);
			childDescs=fir+sec;
		}
		while (childDescs.indexOf(split_char)>=0)
		{
			childDescs=childDescs.substring(childDescs.indexOf(split_char)+split_char.length);
			var tmpStr;
			if(childDescs.indexOf(split_char)>=0)
				tmpStr=childDescs.substring(0,childDescs.indexOf(split_char));
			else
				tmpStr=childDescs;
			retStr=retStr+"$$"+tmpStr;
			alldesc+=tmpStr+",";
		}

		while(childValues.indexOf(reStr)>=0){
			var fir=childValues.substring(0,childValues.indexOf(reStr));
			var sec=childValues.substring(childValues.indexOf(reStr)+reStr.length);
			childValues=fir+sec;
		}

		while (childValues.indexOf(split_char)>=0)
		{
			childValues=childValues.substring(childValues.indexOf(split_char)+split_char.length);
			var tmpStr;
			if(childValues.indexOf(split_char)>=0)
				tmpStr=childValues.substring(0,childValues.indexOf(split_char));
			else
				tmpStr=childValues;

			hiddenStr=hiddenStr+"$$"+tmpStr;

			allid+=tmpStr+",";
		}


   	window.returnValue=allid+"$$"+alldesc;
		this.close();
	}
	function cancelClose(){

		this.close();
	}

</script>


<%
if(con_id.equals("202")){
	out.print(AdhocHelper.adhocRelateInfoGenerator("channel",qry_areaid,session));
}
%>

<body>

<div id="leftSelect" style="position:absolute; left:10px; top:85px; width:260px; height:280px; z-index:1">
<iframe id="left" name="left" border="1" width="130%" height="100%"	scrolling="yes"	src="adhoc_multi_leftselect.jsp?&svc_knd=<%=svcknd%>&subtype=1&con_id=<%=con_id%>">
</iframe>
</div>

<div id="rightSelect" style="position:absolute; left:430px; top:85px; width:260px; height:282px; z-index:2">
<iframe id="right" name="right" border="1" width="130%" height="100%"	scrolling="yes"	src="adhoc_multi_rightselect.jsp">
</iframe>
</div>

<form name="mainfrm" method="post"	>
<table width="778" border="0" cellpadding="0" cellspacing="0" class="bg">
	<tr>
		<td height="416">
		<table width="778" border="0">
			<tr>
				<td height="25" colspan="3">&nbsp;</td>
			</tr>
			<tr align="left">
				<td height="25" colspan="3">&nbsp;&nbsp;
				<%if(con_id.equals(AdhocConditionTag.CHANNEL_CONDITION_ID)){
					String sql="select code,name from ui_sdt_dept ";
					if(!qry_areaid.equals("0")){
					  sql+=" where code='"+qry_areaid+"'";
					}else{
					  sql+=" where parent_id="+qry_areaid;
					}
				%>
				<BIBM:TagSelectList  listID="0" script="onChange='changelocation(document.mainfrm.qry__area_id.options[document.mainfrm.qry__area_id.selectedIndex].value)'"
										focusID="<%=qry_areaid%>" listName="qry__area_id"
										selfSQL="<%=sql%>" />
				<select name="Nclassid">
					<option selected value="000000">全部</option>
				</select>

				<%}else{%>
				    <input type="hidden" name="qry__area_id" value=""/>
				    <input type="hidden" name="Nclassid" value=""/>
				<%}%>
				<input type="text" name="search_word"	value="" maxLength="50">
				<input type="button" name="Submit11" class="btn3"	value="过滤" onClick="search();" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"/>
			</td>
			</tr>
			<tr>
				<td width="308" height="305">&nbsp;</td>
				<td width="98" align="center">
				<p>&nbsp;</p>
				<p><input type="button" name="Submit20" class="button-add" value="   &gt;  " style="color:red;"	onclick="addToRight()"></p>
				<p><input type="button" name="Submit21" class="button-add" value="  &gt;&gt;  "  style="color:red;" onclick="addAllToRight()"></p>
				<p><input type="button" name="Submit22" class="button-add" value="  &lt;   "	style="color:red;" onclick="addToLeft()"></p>
				<p><input type="button" name="Submit23" class="button-add" value="  &lt;&lt;  " style="color:red;" onclick="addAllToLeft()"></p>
				<p>&nbsp;</p>
				</td>
				<td width="300">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="3">
				<hr>
				</td>
			</tr>
			<tr align="center">
				<td colspan="3">
				<input type="hidden" name="selectedIDs" value="">
				<input type="button" class="btn3" name="Submit3" value="确 定" onClick="selectSubmit();" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"/>
				<input type="button" class="btn3" name="Submit4" value="取 消" onClick="cancelClose();" onMouseOver="switchClass2(this)" onMouseOut="switchClass2(this)"/>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
<%if(con_id.equals("202")){%>
<script>
changelocation('<%=qry_areaid%>');
search();
</script>
<%}%>