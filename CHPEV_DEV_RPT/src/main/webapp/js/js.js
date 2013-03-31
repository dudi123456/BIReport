
function switchClass(theObj)
{
	if(theObj.className.indexOf("-over")<0)
	{
		theObj.className=theObj.className+"-over";
	}
	else
	{
		theObj.className=theObj.className.replace("-over","");
	}
}

function switchLeftFrame(theWidth)
{
	if(leftFrameSwitcher.src.indexOf("left")>-1)
	{
		leftFrameSwitcher.src="../biimages/toright.gif";
		parent.bodyFrameset.cols="6,*";
	}
	else
	{
		var fset = theWidth+",*";
		leftFrameSwitcher.src="../biimages/toleft.gif";
		parent.bodyFrameset.cols = fset;
	}
}


function switchObjects(theObjIdArray,theShowObjId)
{
	for(var i=0;i<theObjIdArray.length;i++)
	{
		if(theObjIdArray[i]==theShowObjId)
		{
			eval(theObjIdArray[i]).style.display="";
			eval(theObjIdArray[i]+"1").style.display="";
		}
		else
		{
			eval(theObjIdArray[i]).style.display="none";
			eval(theObjIdArray[i]+"1").style.display="none";
		}
	}
}


function switchSrc(theObj)
{
/*	if(theObj.src.indexOf("_over")<0)
	{
		srcMain=theObj.src.substr(0,theObj.src.lastIndexOf("."));
		srcPostfix=theObj.src.substr(theObj.src.lastIndexOf("."));
		theObj.src=srcMain+"_over"+srcPostfix;
	}
	else
	{
		theObj.src=theObj.src.replace("_over","");
	}
*/
}
//add by jcm
function switchImgSrc(theObj)
{
	if(theObj.src.indexOf("_over")<0)
	{
		srcMain=theObj.src.substr(0,theObj.src.lastIndexOf("."));
		srcPostfix=theObj.src.substr(theObj.src.lastIndexOf("."));
		theObj.src=srcMain+"_over"+srcPostfix;
	}
	else
	{
		theObj.src=theObj.src.replace("_over","");
	}

}


function buildFieldHeadTab(theTaberSpanID,theActivedTaberId,theTaberArrayName)
{
	var theTaberSpanTemp="";
	var theTaberArray=eval(theTaberArrayName);
	var theTaberSpan=eval(theTaberSpanID);


	theTaberSpanTemp+='			<table border="0" cellpadding="0" cellspacing="0" class="field-head">';
	theTaberSpanTemp+='            <tr>';

	for(var i=1;i<theTaberArray.length;i++)
	{
		if(theTaberArray[i][0]==theActivedTaberId)
		{
			if(i==1)
			{
				theTaberSpanTemp+='    <td><img src="../biimages/field_tab_diver_first_on.gif" width="16" height="20"></td>';
			}

            theTaberSpanTemp+='    <td nowrap class="field-tab-on">'+theTaberArray[i][1]+'</td>';

			if(i==theTaberArray.length-1)
			{
            	theTaberSpanTemp+='    <td><img src="../biimages/field_tab_diver_end_on.gif" width="28" height="20"></td>';
			}
			else
			{
            	theTaberSpanTemp+='    <td><img src="../biimages/field_tab_diver_right_on.gif" width="28" height="20"></td>';
			}

			eval(theTaberArray[i][2]);
		}
		else
		{
			if(i==1)
			{
				theTaberSpanTemp+='    <td><img src="../biimages/field_tab_diver_first.gif" width="16" height="20"></td>';
			}

            theTaberSpanTemp+='    <td nowrap class="field-tab"><a href="javascript:"  onClick="buildFieldHeadTab(\''+theTaberSpanID+'\',\''+theTaberArray[i][0]+'\',\''+theTaberArrayName+'\')" class="field-tab-link">'+theTaberArray[i][1]+'</a></td>';
			if(i==theTaberArray.length-1)
			{
            	theTaberSpanTemp+='    <td><img src="../biimages/field_tab_diver_end.gif" width="28" height="20"></td>';
			}
			else
			{
				if(theTaberArray[i+1][0]==theActivedTaberId)
				{
	            	theTaberSpanTemp+='    <td><img src="../biimages/field_tab_diver_left_on.gif" width="25" height="20"></td>';
				}else
				{
            	theTaberSpanTemp+='    <td><img src="../biimages/field_tab_diver_right.gif" width="16" height="20"></td>';
				}
			}
		}
	}

	theTaberSpanTemp+='        	</tr>';
	theTaberSpanTemp+='         </table>';
	theTaberSpan.innerHTML=theTaberSpanTemp;

}

function MM_preloadImages() { //v3.0
	  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
	}

function MM_preloadbiimages() { //v3.0
  var d=document; if(d.biimages){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadbiimages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


function closewin() {
if (opener!=null && !opener.closed) {
opener.window.newwin=null;
opener.openbutton.disabled=false;
opener.closebutton.disabled=true;
}
}
var count=0;//????
var limit=new Array();//??????????????
var countlimit=1;//?????????????
function expandIt(el) {
obj = eval("sub" + el);
	if (obj.style.display == "none") {
		obj.style.display = "block";//?????
		if (count<countlimit) {//??2?
		limit[count]=el;//????
		count++;
		}
		else {
		eval("sub" + limit[0]).style.display = "none";
		for (i=0;i<limit.length-1;i++) {limit[i]=limit[i+1];}//????????????????
		limit[limit.length-1]=el;
		}
	}
	else {
	obj.style.display = "none";
	var j;
	for (i=0;i<limit.length;i++) {if (limit[i]==el) j=i;}//??????????limit??????
	for (i=j;i<limit.length-1;i++) {limit[i]=limit[i+1];}//j????????????
	limit[limit.length-1]=null;//????????
	count--;
	}
}

function MM_preloadbiimages() { //v3.0
  var d=document; if(d.biimages){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadbiimages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function switchContents(theObj,obj2)
{
    var obj2=eval(obj2);
	if(theObj.src.indexOf("_2")>-1)
	{
		theObj.src="../biimages/mail_bar_1.gif";
		obj2.style.display="none";

	}
	else if(theObj.src.indexOf("_1")>-1)
	{
		theObj.src="../biimages/mail_bar_2.gif";
		obj2.style.display="";

	}
}

function switchContents2(theObj,obj2)
{
    var obj2=eval(obj2);
	if(theObj.src.indexOf("_2")>-1)
	{
		theObj.src="../../biimages/mail_bar_1.gif";
		obj2.style.display="none";

	}
	else if(theObj.src.indexOf("_1")>-1)
	{
		theObj.src="../../biimages/mail_bar_2.gif";
		obj2.style.display="";

	}
}


function switchTr(theObj,obj2)
{	
    var obj2=eval(obj2);
	if(theObj.src.indexOf("-show")>-1)
	{
		theObj.src="../biimages/zt/icon-hide.gif";
		obj2.style.display="none";

	}
	else if(theObj.src.indexOf("-hide")>-1)
	{
		theObj.src="../biimages/zt/icon-show.gif";
		obj2.style.display="";

	}
}

function checkNumber(theObj)
{
	for (var i = 0; i < theObj.value.length; i++)
	{
		isNumber = 0;//isNumber
		for (var j=0; j<10; j++)
		{
			if ("" + j == theObj.value.charAt(i))
			{
				isNumber = 1;
			}
		}
		if (isNumber == 0)
		{
			return false;
		}
	}
	return true;
}

function checkEmail(theObj)
{//isEmail
	if((theObj.value.indexOf("@") == -1)||(theObj.value.indexOf(".") == -1))
	{
		return false;
	}
	return true;
}

function switchLeftFrames()
{
	if(parent.leftFrameSwitcher.src.indexOf("left")>-1)
	{
		parent.leftFrameSwitcher.src="../biimages/toright.gif";
		parent.parent.bodyFrameset.cols="6,*";
	}
	else
	{
		parent.leftFrameSwitcher.src="../biimages/toleft.gif";
		parent.parent.bodyFrameset.cols="160,*";
	}
}

function switchLeftFramesMenu()
{
	if(parent.leftFrame.leftFrameSwitcher.src.indexOf("left")>-1)
	{
		parent.leftFrame.leftFrameSwitcher.src="../biimages/toright.gif";
		parent.leftFrame.parent.bodyFrameset.cols="6,*";
	}
	else
	{
		parent.leftFrame.leftFrameSwitcher.src="../biimages/toleft.gif";
		parent.leftFrame.parent.bodyFrameset.cols="160,*";
	}
}

function switchContent(theObj)
{
	if(theObj.src.indexOf("opened")>-1)
	{
		theObj.src="../biimages/switchs_icon_closed.gif";
		theObj.parentNode.parentNode.nextSibling.style.display="none";
	}
	else if(theObj.src.indexOf("closed")>-1)
	{
		theObj.src="../biimages/switchs_icon_opened.gif";
		theObj.parentNode.parentNode.nextSibling.style.display="";
	}
}

var currentChartIndex = 0;
function setCurrentChartTypeIndex(typeIndex) {
  currentChartIndex=typeIndex;
}
function getCurrentChartType() {
    if (currentChartIndex == null || currentChartIndex == undefined){
    	currentChartIndex = 0;
    }
    return currentChartIndex;
}

//清除下拉列表框信息
function cleanSelect(theObj){
  var i=0;
  var j=theObj.length;
  for (i=0;i<j;i++){
    theObj.options.remove(0);
  }
}

//建立下拉框对象
function QueryOptionObject(objName,script){
  tmp=objName.split('__');
  hiObj=tmp[0]+'__hi_'+tmp[1];
  document.write("<input  TYPE='hidden' NAME='" + hiObj+ "' value=''>");
  document.write("<select    NAME='" + objName + "' onchange=\""+hiObj+".value=this.selectedIndex;"+script+";\"></select>");
}
//
//add by renhui change chart
function chartchange(chartID,where,framename){

		if(chartID){
			var pos=chartID.indexOf(",");
			if(pos>=0){
				var charts=chartID.split(",");
				var frames;
				if(framename!=null&&framename!=""){
				  frames = framename.split(",");
				}
				for(var i=0;i<charts.length;i++){
				  if(charts[i].indexOf("table")>=0){
				    var tid = charts[i].replace("table","");
				    var url="WebTable.screen?table_id="+tid+"&chart_id="+tid+where;
					  var target="web_chart_"+tid;
					  if(frames!=null&&frames[i]!=null&&frames[i]!=""){
					    url="WebChart.screen?flag=Y&chart_id="+charts[i]+where;
					    target="web_chart_"+frames[i];
					  }
					  target=eval(target);
					  target.location=url;
				  }else{
					  var url="WebChart.screen?chart_id="+charts[i]+where;
					  var target="web_chart_"+charts[i];
					  if(frames!=null&&frames[i]!=null&&frames[i]!=""){
					    url="WebChart.screen?flag=Y&chart_id="+charts[i]+where;
					    target="web_chart_"+frames[i];
					  }
					  target=eval(target);
					  target.location=url;
			    }
				}
			}else{
			      if(chartID.indexOf("table")>=0){
				    var tid = charts[i].replace("table","");
				    var url="WebTable.screen?table_id="+tid+"&chart_id="+tid+where;
					var target="web_chart_"+tid;
					if(framename!=null&&framename!=""){
					   url="WebChart.screen?flag=Y&chart_id="+chartID+where;
					   target="web_chart_"+framename;
				    }
					target=eval(target);
					target.location=url;
				  }else{
					var url="WebChart.screen?chart_id="+chartID+where;
					var target="web_chart_"+chartID;
					if(framename!=null&&framename!=""){
					   url="WebChart.screen?flag=Y&chart_id="+chartID+where;
					   target="web_chart_"+framename;
				    }
					target=eval(target);
					target.location=url;
			      }
			}
		}
		
		
}

function openTreeIMG(n) 
{
	obj = eval("tree" + n);
	sIcon=eval("icon" + n);
	if (obj.style.display == "none") 
	{
		sIcon.src="../biimages/button_open.gif";
		obj.style.display = "block";//显示子菜单		
	}
	else 
	{	
		sIcon.src="../biimages/button_close.gif";
		obj.style.display = "none";	
	}
	
}


function collapseAll(){
for(i=1;i<4;i++)
{
   eval("tree" +i+ ".style.display = 'none'")
   eval("icon" +i+ ".src = '../biimages/button_open.gif'")
}
}
function showAll(){
for(i=1;i<4;i++)
{
   eval("tree" +i+ ".style.display = 'block'")
   eval("icon" +i+ ".src = '../biimages/button_close.gif'")
 }
}

