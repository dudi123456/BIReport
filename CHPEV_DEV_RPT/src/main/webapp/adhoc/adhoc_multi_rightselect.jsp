<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.adhoc.util.*"%>


<%
			response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
			response.setHeader("Pragma", "no-cache"); //HTTP 1.0
			String split_char = AdhocConstant.ADHOC_SPLIT_CHAR;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>条件选择-多项选择</title>
<SCRIPT language=javascript
	src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=JavaScript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
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
<SCRIPT language="javascript">		
<!--
		var split_char='<%=split_char%>';
		
	var split_char="<%=split_char%>";
	var parentValues;//一维数组
	var parentDescs;//一维数组
	var parentChecked;//一维数组
	var childValues;//二维数组
	var childDescs;//二维数组
	var childChecked;//二维数组
			
	var splitChar="<%=split_char%>";
	var splitCharExt="<%=split_char%>_<%=split_char%>";
				
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
		if(tmpStr.length>=0){
			index++;
			ret[index]=tmpStr;
		}
		return ret;
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
			childStr=childStr.substring(childStr.indexOf(splitChar)+splitChar.length);
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
		childStr=childStr.substring(childStr.indexOf(splitChar)+splitChar.length);			
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
	
	function rebuildArray(){
		var tmpValue;
		tmpValue=document.getElementsByName("parentValues")[0].value;
		parentValues=splitToArray(tmpValue);
		tmpValue=document.getElementsByName("parentDescs")[0].value;
		parentDescs=splitToArray(tmpValue);		
		parentChecked=new Array();
		for(var i=0;i<parentValues.length;i++){
			parentChecked[i]=false;
		}
		tmpValue=document.getElementsByName("childValues")[0].value;
		childValues=splitToArrayExt(tmpValue);
		tmpValue=document.getElementsByName("childDescs")[0].value;
		childDescs=splitToArrayExt(tmpValue);	
		
		childChecked=new Array(childValues.length);
		for(var i=0;i<childValues.length;i++){
			var tmpAry=new Array();
			for(var j=0;j<childValues[i].length;j++){
				tmpAry[j]=false;
			}
			childChecked[i]=tmpAry;
		}	
	}

	//创建父级节点
	function createParents(){
		var innerHTML="";
		var tmpValue=document.getElementsByName("parentValues")[0].value;
		if(""!=tmpValue){		
		
	 		rebuildArray();
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
	
	function addToLeft(){

		//获取右边原来的值
		var rightParentValues="";
		var rightParentDescs="";
		var rightChildValues="";
		var rightChildDescs="";	
		var parentObj;
		
		var leftParentValues="";
		var leftChildValues="";
				
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
					if(e[i].checked==false)
					{
						if(rightParentValues.indexOf(tmpStr)<0){
							rightParentValues=rightParentValues+split_char+tmpStr;
							rightParentDescs=rightParentDescs+split_char+e[i].value;	
							//分隔
							rightChildValues=rightChildValues+split_char+"_"+split_char;
							rightChildDescs=rightChildDescs+split_char+"_"+split_char;				
						}
						
						var hasChildChecked=false;
						for(var j=0;j<childValues[parentIndex].length;j++){
							if(childChecked[parentIndex][j]){
								hasChildChecked=true;
								break;
							}
						}	
						if(hasChildChecked)
							leftParentValues=leftParentValues+split_char+tmpStr;					
					}
					else{
						//左边加上
						
						leftParentValues=leftParentValues+split_char+tmpStr;
						
						var hasChildChecked=true;
						for(var j=0;j<childValues[parentIndex].length;j++){
							if(!childChecked[parentIndex][j]){
								hasChildChecked=false;
								break;
							}
						}
						if(!hasChildChecked){
							if(rightParentValues.indexOf(tmpStr)<0){
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
						if(!childChecked[parentIndex][j]){
							rightChildValues=rightChildValues+split_char+childValues[parentIndex][j];
							rightChildDescs=rightChildDescs+split_char+childDescs[parentIndex][j];								
						}
						else{
							leftChildValues=leftChildValues+split_char+childValues[parentIndex][j];						}
					}			
				}	
			}				
		}
		
		document.getElementsByName("parentValues")[0].value=rightParentValues;
		document.getElementsByName("parentDescs")[0].value=rightParentDescs;
		document.getElementsByName("childValues")[0].value=rightChildValues;
		document.getElementsByName("childDescs")[0].value=rightChildDescs;		
		createParents();
		
	
		
		var left=parent.left;
		left.document.getElementsByName("parentValues")[0].value=leftParentValues;
		left.document.getElementsByName("childValues")[0].value=leftChildValues;
		var tmpStr="";
		var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
				+"cancelSomeChecked();\n"
				+"</SCRIPT"+">\n";
		left.tmpDiv.innerHTML ="&nbsp;"+tmpStr;		

	}
	
	function addAllToLeft(){
		document.getElementsByName("parentValues")[0].value="";
		document.getElementsByName("parentDescs")[0].value="";
		document.getElementsByName("childValues")[0].value="";
		document.getElementsByName("childDescs")[0].value="";
		
		
		var leftParentValues="";
		var leftChildValues="";
		
		for(var i=0;i<parentValues.length;i++){
			leftParentValues=leftParentValues+split_char+parentValues[i];
		}	

		for(var i=0;i<childValues.length;i++){
			for(var j=0;j<childValues[i].length;j++){
				leftChildValues=leftChildValues+split_char+childValues[i][j];
			}
		}

		createParents();	
			
		var left=parent.left;
		left.document.getElementsByName("parentValues")[0].value=leftParentValues;
		left.document.getElementsByName("childValues")[0].value=leftChildValues;
		var tmpStr="";
		var tmpStr=tmpStr+"<SCRIPT DEFER>\n"
				+"cancelSomeChecked();\n"
				+"</SCRIPT"+">\n";
		left.tmpDiv.innerHTML ="&nbsp;"+tmpStr;			
			
	}
//-->
</SCRIPT>


</head>

<body>
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top">
		<div id="content"></div>
		</td>
	</tr>
</table>
<div id="tmpDiv" style="display:'none'"></div>
<input type="hidden" name="parentValues" value="">
<input type="hidden" name="parentDescs" value="">
<input type="hidden" name="childValues" value="">
<input type="hidden" name="childDescs" value="">
</body>

</html>

<script language="JavaScript">
<!--

	function checkParent(parent)
	{
		var checked=parent.checked;
		var parentName=parent.name;
		parentName=parentName.substring(parentName.indexOf("_")+1);
		var parentIndex=parentName.substring(0,parentName.indexOf("_"));
		parentIndex=eval(parentIndex);
		parentChecked[parentIndex]=checked;
		//要看看这个方法是否支持正则表达式
		//var e=document.getElementsByName(parent.name);
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
		childChecked[parentIndex][childIndex]=checked

		var allChecked=true;
		var parent;
		var parentName=child.name.substring(0,child.name.indexOf("_child"));
		//var e=document.getElementsByName(child.name);
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

