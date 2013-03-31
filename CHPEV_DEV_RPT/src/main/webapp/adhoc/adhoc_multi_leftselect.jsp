<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%
			response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
			response.setHeader("Pragma", "no-cache"); //HTTP 1.0
%>
<%			
			String[][] svces = null;

			String[][] svcesTmp = null;

			String split_char = AdhocConstant.ADHOC_SPLIT_CHAR;
			//关键字
			String search_word = CommTool.getParameter8895_1(request,AdhocConstant.ADHOC_MULTI_SELECT_QRY_SWEARCH_WORD);
			//条件 con_id
			String con_id = CommTool.getParameterGB(request,AdhocConstant.ADHOC_MULTI_SELECT_QRY_CONDITION_ID);			
			//业务类型
			String svcknd = CommTool.getParameterGB(request,AdhocConstant.ADHOC_MULTI_SELECT_QRY_SVC_KND);			
			//区域信息
		    String areaid = CommTool.getParameterGB(request,AdhocConstant.ADHOC_MULTI_SELECT_QRY_AREA_ID);		  
			//二级信息
			String sec_area_id = CommTool.getParameterGB(request,AdhocConstant.ADHOC_MULTI_SELECT_QRY_SEC_AREA_ID);
		
     		
			//提交类型
		    String subtype = CommTool.getParameterGB(request,"subtype");
		    if(subtype==null && !subtype.equals("")){
		         subtype="0";
		    }
		   
			//提取数组			
			if(subtype.equals("1")){
				svcesTmp = AdhocHelper.getConditionListArray(con_id,sec_area_id,areaid ,svcknd, session);
//				svces = AdhocHelper.getConditionListArray(con_id,sec_area_id,areaid ,svcknd, session);

			}
			
			if (null != svcesTmp && svcesTmp.length > 0) {
				if (svcesTmp[0].length<4){
					svces = new String[svcesTmp.length][4];	
					for (int i = 0; i < svcesTmp.length; i++) {
						for (int k=0;k<4;k++){

							switch(k){
								case 2:
									svces[i][k] = "0";
									break;
								case 3:
									svces[i][k] = "全部";
									break;
								default:
									svces[i][k] = svcesTmp[i][k];
									break;
							}
						
							
						}
					}
				}else{
					svces = svcesTmp;
				}
				
			}
			


			String selectedIDs=(String) session.getAttribute(AdhocConstant.ADHOC_MULTI_SELECT_CONDITION_VALUE_SESSION);
			if(null==selectedIDs){
				selectedIDs="";
			}
			
			//此数组包含所有的多选值和类型值， 默认按子id、子名称、类型ID，类型名称

			//过虑查询 
			if (null != search_word && !"".equalsIgnoreCase(search_word.trim())) {
				if (null != svces && svces.length > 0) {
					//先要统计一下符合条件的个数
					int count = 0;
					List tmpL = new ArrayList();
					for (int i = 0; i < svces.length; i++) {
						if (svces[i][1].indexOf(search_word) >= 0
								|| svces[i][3].indexOf(search_word) >= 0) {
							tmpL.add(count, svces[i]);
							count++;
						}
					}
					//声明一个新数组，个数为 Count
					String[][] tmpSvces = new String[count][];
					for (int i = 0; i < tmpSvces.length; i++) {
						tmpSvces[i] = (String[]) tmpL.get(i);
					}
					svces = tmpSvces;
				}
			}

			//临时字符串
			String parentIDs = "";
			String parentDescs = "";
			String childIDs = "";
			String childDescs = "";

			//临时保存的类型编号
			String tmpTypeID = "";
			int typeIndex = 0;
			if (null != svces && svces.length > 0) {
				for (int i = 0; i < svces.length; i++) {
					//数组一开循环，或者遇到新的类型，要考虑最后一次循环肯定不变
					String secStr = svces[i][1];
					String frthStr = svces[i][3];
					secStr = secStr.replaceAll(" ", "&nbsp;");
					frthStr = frthStr.replaceAll(" ", "&nbsp;");//处理空格
					if (!svces[i][2].equalsIgnoreCase(tmpTypeID)) {
						tmpTypeID = svces[i][2];
						typeIndex++;
						parentIDs = parentIDs + split_char + svces[i][2];
						//对单项的特殊处理  sql语句中"全部"字符有乱码有问题
						if(frthStr.indexOf("???")>-1){
						  frthStr="全部";
						}
						parentDescs = parentDescs + split_char + frthStr;
						//System.out.println("------frthStr==="+frthStr);
						childIDs = childIDs + split_char + "_" + split_char
								+ svces[i][0];
						childDescs = childDescs + split_char + "_" + split_char
								+ secStr;
					} else {
						childIDs = childIDs + split_char + svces[i][0];
						childDescs = childDescs + split_char + secStr;
					}
				}

			}

			%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>条件选择-多项选择</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css"
	type="text/css">
<style type="text/css">
<!--
.bg {
	background-color: #E8E8E8;
	border: 1px solid #CCCCCC;
}
-->
</style>
</head>

<SCRIPT language="javascript">
<!--	
	var split_char="<%=split_char%>"; 
	var parentValues=new Array(<%=typeIndex%>);//一维数组
	var parentDescs=new Array(<%=typeIndex%>);//一维数组
	var parentChecked=new Array(<%=typeIndex%>);//一维数组
	var childValues=new Array(<%=typeIndex%>);//二维数组
	var childDescs=new Array(<%=typeIndex%>);//二维数组
	var childChecked=new Array(<%=typeIndex%>);//二维数组
	
	var parentIDs="<%=parentIDs%>";
	var parentDescs="<%=parentDescs%>";
	var childIDs="<%=childIDs%>";
	var childDescs="<%=childDescs%>";
	var splitChar="<%=split_char%>";
	var splitCharExt="<%=split_char%>_<%=split_char%>";
	var selectedIDs="<%=selectedIDs%>";
	
	

	function splitToArray(splitStr){
		var ret=new Array();
		var index=-1;
		var tmpStr=splitStr;
		//去掉第一个分隔符
		tmpStr=tmpStr.substring(tmpStr.indexOf(splitChar)+splitChar.length);
		while(tmpStr.indexOf(splitChar)>=0){
			index++;
			ret[index]=tmpStr.substring(0,tmpStr.indexOf(splitChar));
			tmpStr=tmpStr.substring(tmpStr.indexOf(splitChar)+splitChar.length);
		}
		if(tmpStr.length>0){
			index++;
			ret[index]=tmpStr;
		}
		return ret;
	}
	
	parentValues=splitToArray(parentIDs);
	parentDescs=splitToArray(parentDescs);
	

	for(var i=0;i<parentChecked.length;i++){
		parentChecked[i]=false;
	}
	
	function splitToArrayExt(splitStr){
		var ret=new Array(parentValues.length);		
		var patentIndex=-1;
		var childIndex=-1;
		var tmpStr=splitStr;
		//去掉第一个分隔符
		tmpStr=tmpStr.substring(tmpStr.indexOf(splitCharExt)+splitCharExt.length);
		while(tmpStr.indexOf(splitCharExt)>=0){
			patentIndex++;
			var tmpAry=new Array();
			childIndex=-1;
			var childStr;
			if(tmpStr.indexOf(splitCharExt)>=0)
				childStr=tmpStr.substring(0,tmpStr.indexOf(splitCharExt));
			else
				childStr=tmpStr;
			while(childStr.indexOf(splitChar)>=0){
				childIndex++;
				tmpAry[childIndex]=childStr.substring(0,childStr.indexOf(splitChar));
				childStr=childStr.substring(childStr.indexOf(splitChar)+splitChar.length);
			}
			childIndex++;
			tmpAry[childIndex]=childStr;
			ret[patentIndex]=tmpAry;
			tmpStr=tmpStr.substring(tmpStr.indexOf(splitCharExt)+splitCharExt.length);
		}
		patentIndex++;
		var tmpAry=new Array();
		childIndex=-1;
		var childStr;
		if(tmpStr.indexOf(splitCharExt)>=0)
			childStr=tmpStr.substring(0,tmpStr.indexOf(splitCharExt));
		else
			childStr=tmpStr;
		while(childStr.indexOf(splitChar)>=0){
				childIndex++;
				tmpAry[childIndex]=childStr.substring(0,childStr.indexOf(splitChar));
				childStr=childStr.substring(childStr.indexOf(splitChar)+splitChar.length);
		}
		childIndex++;
		tmpAry[childIndex]=childStr;
		ret[patentIndex]=tmpAry;		
		return ret;
	}

	childValues=splitToArrayExt(childIDs);
	childDescs=splitToArrayExt(childDescs);
	
	function initChildCheck(){
	   	var ret=new Array(parentValues.length);
		for(var i=0;i<childValues.length;i++){
			var tmpAry=new Array();
			for(var j=0;j<childValues[i].length;j++){
				tmpAry[j]=false;
			}		
			ret[i]=tmpAry;
		}
		if(""!=selectedIDs)
			selectedIDs=","+selectedIDs+",";
		for(var i=0;i<ret.length;i++){
			for(var j=0;j<ret[i].length;j++){
				if(selectedIDs.indexOf(","+childValues[i][j]+",")>=0)
					ret[i][j]=true;
			}
		}
		return ret;
	}
	childChecked=initChildCheck();

	
	//创建父级节点
	function createParents(){
		var innerHTML="";
		
		
		for(var i=0;i<parentValues.length;i++){
			innerHTML=innerHTML+"<div id=\"parent_"+i+"\">\n";
			innerHTML=innerHTML+"<table border=0 cellspacing=0 cellpadding=0>\n";
			innerHTML=innerHTML+"<tr>\n";
			innerHTML=innerHTML+"<td>\n";
			innerHTML=innerHTML+"<a href=\"#\" onClick=\"expandParent("+i+")\">";
			if(i==0)
				innerHTML=innerHTML+"<img src=\"../images/system/firstnode.gif\" border=0>";
			else if(i==parentValues.length-1)
				innerHTML=innerHTML+"<img src=\"../images/system/ez.gif\" border=0>";
			else
				innerHTML=innerHTML+"<img src=\"../images/system/ez1.gif\" border=0>";	
			innerHTML=innerHTML+"</a>";
			innerHTML=innerHTML+"</td>\n";
			innerHTML=innerHTML+"<td nowrap valign=middle>\n";
			if(parentChecked[i])
				innerHTML=innerHTML+"<input type=\"checkbox\" name=\"parent_"
					+i+"_"+parentValues[i]+"\"  value=\""
					+parentDescs[i]
					+"\" checked onClick=\"checkParent(this)\">"+parentDescs[i];
			else
				innerHTML=innerHTML+"<input type=\"checkbox\" name=\"parent_"
					+i+"_"+parentValues[i]+"\"  value=\""
					+parentDescs[i]
					+"\" onClick=\"checkParent(this)\">"+parentDescs[i];
			innerHTML=innerHTML+"</td>\n";
			innerHTML=innerHTML+"</tr>\n";
			innerHTML=innerHTML+"</table>\n";
			innerHTML=innerHTML+"</div>\n";
		}
		content.innerHTML=innerHTML;
	}

	//展开父级节点
	function expandParent(parentIndex){

		var parent=eval("parent_"+parentIndex);
		var parentInnerHTML="";

		parentInnerHTML=parentInnerHTML+"<table border=0 cellspacing=0 cellpadding=0>\n";
		parentInnerHTML=parentInnerHTML+"<TBODY>\n";
		parentInnerHTML=parentInnerHTML+"<tr>\n";
		parentInnerHTML=parentInnerHTML+"<td>\n";
		parentInnerHTML=parentInnerHTML
			+"<a href=\"#\" onClick=\"collapseParent("+parentIndex+")\">";
		if(parentIndex==0)
			parentInnerHTML=parentInnerHTML
				+"<img src=\"../images/system/mnode2.gif\" border=0>";
		else if (parentIndex==parentValues.length-1)
			parentInnerHTML=parentInnerHTML
				+"<img src=\"../images/system/mnode1.gif\" border=0>";
		else
			parentInnerHTML=parentInnerHTML
				+"<img src=\"../images/system/mnode.gif\" border=0>";
		parentInnerHTML=parentInnerHTML+"</a>";
		parentInnerHTML=parentInnerHTML+"</td>\n";
		parentInnerHTML=parentInnerHTML
			+"<td nowrap valign=middle colspan=\"2\">\n";
		if(parentChecked[parentIndex])
			parentInnerHTML=parentInnerHTML
				+"<input type=\"checkbox\" name=\"parent_"
				+parentIndex+"_"+parentValues[parentIndex]
				+"\" checked  value=\""+parentDescs[parentIndex]
				+"\" onClick=\"checkParent(this)\">"
				+parentDescs[parentIndex];
		else
			parentInnerHTML=parentInnerHTML
				+"<input type=\"checkbox\" name=\"parent_"
				+parentIndex+"_"+parentValues[parentIndex]
				+"\"  value=\""+parentDescs[parentIndex]
				+"\" onClick=\"checkParent(this)\">"
				+parentDescs[parentIndex];			
		parentInnerHTML=parentInnerHTML+"</td>\n";
		parentInnerHTML=parentInnerHTML+"</tr>\n";
		for(var i=0;i<childValues[parentIndex].length;i++){
			parentInnerHTML=parentInnerHTML+"<tr>\n";
			if(parentIndex==(parentValues.length-1) &&  i==childValues[parentIndex].length-1)
				parentInnerHTML=parentInnerHTML
					+"<td><img src=\"../images/system/line1.gif\" border=0></td>\n"
			else
				parentInnerHTML=parentInnerHTML
					+"<td><img src=\"../images/system/line3.gif\" border=0></td>\n"
			if(i==childValues[parentIndex].length-1)
				parentInnerHTML=parentInnerHTML
					+"<td><img src=\"../images/system/line1.gif\" border=0></td>\n"
			else
				parentInnerHTML=parentInnerHTML
					+"<td><img src=\"../images/system/line2.gif\" border=0></td>\n"
			parentInnerHTML=parentInnerHTML+"<td nowrap>\n"
			if(childChecked[parentIndex][i])
				parentInnerHTML=parentInnerHTML
					+"<input type=\"checkbox\" name=\"parent_"
					+parentIndex+"_"+parentValues[parentIndex]
					+"_child_"+i+"_"+childValues[parentIndex][i]
					+"\" checked value=\""+childDescs[parentIndex][i]
					+"\" onClick=\"checkChild(this)\">"+childDescs[parentIndex][i];
			else
				parentInnerHTML=parentInnerHTML
					+"<input type=\"checkbox\" name=\"parent_"
					+parentIndex+"_"+parentValues[parentIndex]
					+"_child_"+i+"_"+childValues[parentIndex][i]
					+"\"  value=\""+childDescs[parentIndex][i]
					+"\" onClick=\"checkChild(this)\">"+childDescs[parentIndex][i];				
			parentInnerHTML=parentInnerHTML+"</td>\n";
			parentInnerHTML=parentInnerHTML+"</tr>\n";
		}
		parentInnerHTML=parentInnerHTML+"</TBODY>\n";
		parentInnerHTML=parentInnerHTML+"</table>\n";
		parent.innerHTML=parentInnerHTML;
	}

	function collapseParent(parentIndex){
		var parent=eval("parent_"+parentIndex);
		var parentInnerHTML="";

		parentInnerHTML=parentInnerHTML+"<table border=0 cellspacing=0 cellpadding=0>\n";
		parentInnerHTML=parentInnerHTML+"<TBODY>\n";
		parentInnerHTML=parentInnerHTML+"<tr>\n";
		parentInnerHTML=parentInnerHTML+"<td>\n";
		parentInnerHTML=parentInnerHTML
			+"<a href=\"#\" onClick=\"expandParent("+parentIndex+")\">";
		if(parentIndex==0)
			parentInnerHTML=parentInnerHTML
				+"<img src=\"../images/system/firstnode.gif\" border=0>";
		else if(parentIndex==parentValues.length-1)
			parentInnerHTML=parentInnerHTML
				+"<img src=\"../images/system/ez.gif\" border=0>";
		else
			parentInnerHTML=parentInnerHTML
				+"<img src=\"../images/system/ez1.gif\" border=0>";
		parentInnerHTML=parentInnerHTML+"</a>";			
		parentInnerHTML=parentInnerHTML+"</td>\n";
		parentInnerHTML=parentInnerHTML+"<td nowrap colspan=\"2\">\n";
		if(parentChecked[parentIndex])
			parentInnerHTML=parentInnerHTML
				+"<input type=\"checkbox\" name=\"parent_"
				+parentIndex+"_"+parentValues[parentIndex]
				+"\" checked value=\""+parentDescs[parentIndex]
				+"\" onClick=\"checkParent(this)\">"
				+parentDescs[parentIndex];
		else
			parentInnerHTML=parentInnerHTML
				+"<input type=\"checkbox\" name=\"parent_"
				+parentIndex+"_"+parentValues[parentIndex]
				+"\" value=\""+parentDescs[parentIndex]
				+"\" onClick=\"checkParent(this)\">"
				+parentDescs[parentIndex];			
		parentInnerHTML=parentInnerHTML+"</td>\n";
		parentInnerHTML=parentInnerHTML+"</tr>\n";
		parentInnerHTML=parentInnerHTML+"</TBODY>\n";
		parentInnerHTML=parentInnerHTML+"</table>\n";
		parent.innerHTML=parentInnerHTML;
	}

	function expandAll(){
		for(var i=0;i<parentValues.length;i++){
			expandParent(i);
		}		
	}

	function collapseAll(){
		createParents();
	}
	
	function addToRight(){

		//获取右边原来的值
		var rightParentValues=parent.right.document.getElementsByName("parentValues")[0].value;
		var rightParentDescs=parent.right.document.getElementsByName("parentDescs")[0].value;
		var rightChildValues=parent.right.document.getElementsByName("childValues")[0].value;
		var rightChildDescs=parent.right.document.getElementsByName("childDescs")[0].value;	
			
		if(null==rightParentValues)
			rightParentValues="";
		if(null==rightParentDescs)
			rightParentDescs="";
		if(null==rightChildValues)
			rightChildValues="";
		if(null==rightChildDescs)
			rightChildDescs="";	
	

		var parentObj;
		
		//处理左边
		var e=document.getElementsByTagName("input");		
		for(var i=0;i<e.length;i++){	
	
			if(e[i].type=="checkbox" ){
			    var tmpObj=e[i];
				//先统计父类数，好声明数组
				
				if(tmpObj.name.indexOf("parent_")>=0 && tmpObj.name.indexOf("_child_")<0){
				//由于是排序过，所以肯定先是父节点
					parentObj=tmpObj;
					var tmpStr=tmpObj.name.substring(tmpObj.name.indexOf("parent_")+7);
					var parentIndex=eval(tmpStr.substring(0,tmpStr.indexOf("_")));
					tmpStr=tmpStr.substring(tmpStr.indexOf("_")+1);	
					if(e[i].checked==true)
					{
						if(rightParentValues.indexOf(split_char+tmpStr+split_char)<0 && 
							!(rightParentValues.indexOf(tmpStr)>=0 
							&& (rightParentValues.lastIndexOf(tmpStr)+tmpStr.length)==rightParentValues.length)){
							rightParentValues=rightParentValues+split_char+tmpStr;
							rightParentDescs=rightParentDescs+split_char+e[i].value;	
							//分隔
							rightChildValues=rightChildValues+split_char+"_"+split_char;
							rightChildDescs=rightChildDescs+split_char+"_"+split_char;				
						}
					}
					else{
						var hasChildChecked=false;
						for(var j=0;j<childValues[parentIndex].length;j++){
							if(childChecked[parentIndex][j]){
								hasChildChecked=true;
								break;
							}
						}
						if(hasChildChecked){
						if(rightParentValues.indexOf(split_char+tmpStr+split_char)<0 && 
							!(rightParentValues.indexOf(tmpStr)>=0 
							&& (rightParentValues.lastIndexOf(tmpStr)+tmpStr.length)==rightParentValues.length)){
								rightParentValues=rightParentValues+split_char+tmpStr;
								rightParentDescs=rightParentDescs+split_char+e[i].value;	
								//分隔
								rightChildValues=rightChildValues+split_char+"_"+split_char;
								rightChildDescs=rightChildDescs+split_char+"_"+split_char;				
							}						
						}
					}
					//如果孩子没有展开, 需要处理
					for(var j=0;j<childValues[parentIndex].length;j++){
						if(childChecked[parentIndex][j]){
							if(rightChildValues.indexOf(split_char+childValues[parentIndex][j]+split_char)<0 && 
								!(rightChildValues.indexOf(childValues[parentIndex][j])>=0 && ( rightChildValues.lastIndexOf(childValues[parentIndex][j])+childValues[parentIndex][j].length)==rightChildValues.length)){
								rightChildValues=rightChildValues+split_char+childValues[parentIndex][j];
								rightChildDescs=rightChildDescs+split_char+childDescs[parentIndex][j];	
							}			
						}
					}			
				}	
			}				
		}
	
	
		//全部处理完毕，要调用右边的
				var right=parent.right;
				right.document.getElementsByName("parentValues")[0].value=rightParentValues;
				right.document.getElementsByName("parentDescs")[0].value=rightParentDescs;
				right.document.getElementsByName("childValues")[0].value=rightChildValues;
				right.document.getElementsByName("childDescs")[0].value=rightChildDescs;
				var tmpStr="";
				var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
					+"createParents();\n"
				//	+"expandAll();\n"
					+"</SCRIPT"+">\n";
				right.tmpDiv.innerHTML ="&nbsp;"+tmpStr;
		
	}
	
	
	function addAllToRight(){

		//获取右边原来的值
		var rightParentValues="";
		var rightParentDescs="";
		var rightChildValues="";
		var rightChildDescs="";	
	
		for(var i=0;i<parentValues.length;i++){
			rightParentValues=rightParentValues+split_char+parentValues[i];
			rightParentDescs=rightParentDescs+split_char+parentDescs[i];			
		}
		
		for(var i=0;i<childValues.length;i++){
			rightChildValues=rightChildValues+split_char+"_"+split_char;
			rightChildDescs=rightChildDescs+split_char+"_"+split_char;	
			for(var j=0;j<childValues[i].length;j++){
				rightChildValues=rightChildValues+split_char+childValues[i][j];
				rightChildDescs=rightChildDescs+split_char+childDescs[i][j];			
			}
		}
		//全部处理完毕，要调用右边的
		var right=parent.right;
		right.document.getElementsByName("parentValues")[0].value=rightParentValues;
		right.document.getElementsByName("parentDescs")[0].value=rightParentDescs;
		right.document.getElementsByName("childValues")[0].value=rightChildValues;
		right.document.getElementsByName("childDescs")[0].value=rightChildDescs;
		var tmpStr="";
		var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
				+"createParents();\n"
				+"</SCRIPT"+">\n";
		right.tmpDiv.innerHTML ="&nbsp;"+tmpStr;
		allChecked();
		
	}	
	
	function allChecked(){
		var e=document.getElementsByTagName("input");
		for (var i=0;i<e.length;i++){
			if(e[i].type=="checkbox")
			  e[i].checked=true;
		}		
		for(var i=0;i<parentChecked.length;i++){
			parentChecked[i]=true;
		}
		for(var i=0;i<childChecked.length;i++){
			for(var j=0;j<childChecked[i].length;j++){
				childChecked[i][j]=true;
			}
		}
	}
	
	function allUnChecked(){
		var e=document.getElementsByTagName("input");
		for (var i=0;i<e.length;i++){
			if(e[i].type=="checkbox") 
			  e[i].checked=false;
		}	
		for(var i=0;i<parentChecked.length;i++){
			parentChecked[i]=false;
		}
		for(var i=0;i<childChecked.length;i++){
			for(var j=0;j<childChecked[i].length;j++){
				childChecked[i][j]=false;
			}
		}			
	}
	
	function cancelSomeChecked(){
		var cancelParent=document.getElementsByName("parentValues")[0].value;
		cancelParent=cancelParent+split_char;
		var cancelChild=document.getElementsByName("childValues")[0].value;
		cancelChild=cancelChild+split_char;
		
		
		var e=document.getElementsByTagName("input");
		for (var i=0;i<e.length;i++){
			if(e[i].type=="checkbox"){
			 	if(e[i].name.indexOf("parent_")>=0 && e[i].name.indexOf("_child_")<0){
				 	var parentID=e[i].name;
				 	parentID=parentID.substring(parentID.indexOf("parent_")+7);
				 	parentID=parentID.substring(parentID.indexOf("_")+1);
				 	if(cancelParent.indexOf(split_char+parentID+split_char)>=0)
				  		e[i].checked=false;
			  	}
			  	else if(e[i].name.indexOf("parent_")>=0 && e[i].name.indexOf("_child_")>=0){
			  		var childID=e[i].name;
				 	childID=childID.substring(childID.indexOf("_child_")+7);
				 	childID=childID.substring(childID.indexOf("_")+1);
				 	if(cancelChild.indexOf(split_char+childID+split_char)>=0)
				  		e[i].checked=false;
			  	}
			}
		}	
		
		for(var i=0;i<parentChecked.length;i++)	{
			var parentID=parentValues[i];
			if(cancelParent.indexOf(split_char+parentID+split_char)>=0)
				parentChecked=false;
		}	
	
		for(var i=0;i<childChecked.length;i++){
			for(var j=0;j<childChecked[i].length;j++){
				var childID=childValues[i][j];
				if(cancelChild.indexOf(split_char+childID+split_char)>=0){
					childChecked[i][j]=false;
					
				}
			}			
		}
	}	

	function delayExec(){
			window.setTimeout('addToRight()', 2000);
	}	
		
//-->
</SCRIPT>
<body <%if(svces!=null && svces.length>0){%>
   onLoad="createParents();expandParent(0);"

   <%}%>
   >
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<%if(svces!=null && svces.length>0){%>
		<td valign="top">
		<div id="content"></div>
		</td>
		<script>		  
		  parent.mainfrm.Submit20.disabled=false;
		  parent.mainfrm.Submit21.disabled=false;
		  parent.mainfrm.Submit22.disabled=false;
		  parent.mainfrm.Submit23.disabled=false;
		</script>		
		<%}else{%>
		<td valign="center">
		<div id="content">无记录</div>
		</td>
		<script>		  
		 // parent.mainfrm.Submit11.disabled=true;
		  parent.mainfrm.Submit20.disabled=true;
		  parent.mainfrm.Submit21.disabled=true;
		  parent.mainfrm.Submit22.disabled=true;
		  parent.mainfrm.Submit23.disabled=true;
		</script>
		<%}%>
	</tr>
</table>
<div id="tmpDiv" style="display:'none'"></div>
<input type="hidden" name="parentValues" value="">
<input type="hidden" name="childValues" value="">
<!-- 
<input type="button" name="Add" value="全部展开" onClick="expandAll();">
<input type="button" name="Add" value="全部折叠" onClick="collapseAll();">
-->
</body>
</html>

<script language="JavaScript">
<!--
  delayExec();
	function checkParent(parent)
	{
		var checked=parent.checked;
		var parentName=parent.name;
		parentName=parentName.substring(parentName.indexOf("_")+1);
		var parentIndex=parentName.substring(0,parentName.indexOf("_"));
		parentIndex=eval(parentIndex);
		parentChecked[parentIndex]=checked;
		//要看看这个方法是否支持正则表达式
		
		var e=document.getElementsByTagName("input");

		for (var i=0;i<e.length;i++)
		{
			//是选择框，是父级的节点的孩子
			if(e[i].type=="checkbox" && e[i].name.indexOf(parent.name)>=0){
				var childName=e[i].name;
				e[i].checked=checked;	
			}
		}
		for(var i=0;i<childChecked[parentIndex].length;i++){
			childChecked[parentIndex][i]=checked;
		}
	}

	function checkChild(child)
	{
		var checked=child.checked;
		var childName=child.name;
		var parentName=childName.substring(childName.indexOf("parent_")+7);
		var parentIndex=eval(parentName.substring(0,parentName.indexOf("_")));
		childName=childName.substring(childName.indexOf("_child_")+7);
		var childIndex=eval(childName.substring(0,childName.indexOf("_")));
		childChecked[parentIndex][childIndex]=checked;

		var allChecked=true;
		var parent;
		var parentName=child.name.substring(0,child.name.indexOf("_child"));
		
		var e=document.getElementsByTagName("input");

		for (var i=0;i<e.length;i++)
		{
			if(e[i].type=="checkbox" && e[i].name==parentName){
				parent=e[i];	
			}
			//是选择框，是父级的节点的孩子
			if(e[i].type=="checkbox" && e[i].name.indexOf(parentName)>=0 && e[i].checked==false && parent!=e[i]){
				allChecked=false;	
			}
		}
		parent.checked=allChecked;
		parentChecked[parentIndex]=allChecked
	}

//-->
</script>

