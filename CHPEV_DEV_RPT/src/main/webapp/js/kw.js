//????????
function buildTaber(theActivedId)
	  {
	  	var taberInnerHTMLTemp='';

		taberInnerHTMLTemp+='<table width="100%" border="0" cellpadding="0" cellspacing="0"';
		taberInnerHTMLTemp+='          <tr> '; 
		for(i=1;i<taberArr.length;i++)
		{
				if(i>1)
		{
			taberInnerHTMLTemp+='            <td><img src="../images/common/system/size.gif" width="1"></td>';
		}
			if(taberArr[i][0]==theActivedId)
			{
				if(i!=1)taberInnerHTMLTemp+='            <td></td>';
				taberInnerHTMLTemp+='            <td><img src="../images/common/system/tab_left_on.gif"></td>';
				taberInnerHTMLTemp+='            <td nowrap background="../images/common/system/tab_bg_on.gif" class="tab-button-on" onClick="buildTaber(\''+taberArr[i][0]+'\')">'+taberArr[i][1]+'</td>';
				taberInnerHTMLTemp+='            <td><img src="../images/common/system/tab_right_on.gif"></td>';			
				eval(taberArr[i][3]).location.href=taberArr[i][2];
			}
			else
			{
				if(i!=1)taberInnerHTMLTemp+='            <td></td>';
				taberInnerHTMLTemp+='            <td><img src="../images/common/system/tab_left_off.gif"></td>';
				taberInnerHTMLTemp+='            <td nowrap background="../images/common/system/tab_bg_off.gif" class="tab-button-off" onMouseOver="this.className=\'tab-button-off-over\'" onMouseOut="this.className=\'tab-button-off\'" onClick="buildTaber(\''+taberArr[i][0]+'\')">'+taberArr[i][1]+'</td>';
				taberInnerHTMLTemp+='            <td><img src="../images/common/system/tab_right_off.gif"></td>';
			}	
		}
		
                taberInnerHTMLTemp+='          <td nowrap width="15"></td>';
		taberInnerHTMLTemp+='          </tr>'; 		
		taberInnerHTMLTemp+='        </table>'; 
	  	taber.innerHTML=taberInnerHTMLTemp;	
	  }	  
	  
//????????????????????
function buildTaberArea(theTaberSpanID,theActivedTaberId,theTaberArrayName)
{
	var theTaberSpanTemp="";
	var theTaberArray=eval(theTaberArrayName);
	var theTaberSpan=eval(theTaberSpanID);
	
	theTaberSpanTemp+='				<table border="0" cellspacing="0" cellpadding="0">';
	theTaberSpanTemp+='        	<tr >';
	theTaberSpanTemp+='        	  <td><img src="../images/common/system/taber_button_left.gif"></td>';

	for(var i=1;i<theTaberArray.length;i++)
	{
		if(i>1)
		{
			theTaberSpanTemp+='            <td background="../images/common/system/taber_bg.gif"><img src="../biimages/kw/taber_button_off_left.gif"></td>';
		}
	   
		if(theTaberArray[i][0]==theActivedTaberId)
		{
			theTaberSpanTemp+='        	  <td nowrap class="taber-button-on"><a class="bule" href="javascript:" onClick="buildTaberArea(\''+theTaberSpanID+'\',\''+theTaberArray[i][0]+'\',\''+theTaberArrayName+'\')">'+theTaberArray[i][1]+'</a></td>';			
			eval(theTaberArray[i][2]);
			//alert(theTaberSpanTemp);
		}
		else
		{
			theTaberSpanTemp+='';
			theTaberSpanTemp+='        	  <td nowrap class="taber-button-off"><a class="taber-button-off-link" href="javascript:" onClick="buildTaberArea(\''+theTaberSpanID+'\',\''+theTaberArray[i][0]+'\',\''+theTaberArrayName+'\')">'+theTaberArray[i][1]+'</a></td>';

		}
	}
    theTaberSpanTemp+='        	<td><img src="../images/common/system/taber_button_off_right.gif" border="0"></td>';
	theTaberSpanTemp+='        	</tr>';
	theTaberSpanTemp+='         </table>';
	
	theTaberSpan.innerHTML=theTaberSpanTemp;
}

function buildUnicomTab(theActivedId){
	var taberInnerHTMLTemp='';
	taberInnerHTMLTemp+='<ul>'; 
	for(i=1;i<taberArr.length;i++){
		if(taberArr[i][0]==theActivedId){
				taberInnerHTMLTemp+='<li id="current"><a href="javascript:;">'+taberArr[i][1]+'</a></li>';		
				eval(taberArr[i][3]).location.href=taberArr[i][2];
		}else{
				taberInnerHTMLTemp+='<li><a href="javascript:buildUnicomTab(\''+taberArr[i][0]+'\')">'+taberArr[i][1]+'</a></li>';
		}	
	}
	taberInnerHTMLTemp+='</ul>'; 
	tab.innerHTML=taberInnerHTMLTemp;	
}

//iframe????????????
var getFFVersion=navigator.userAgent.substring(navigator.userAgent.indexOf("Firefox")).split("/")[1]
//extra height in px to add to iframe in FireFox 1.0+ browsers
var FFextraHeight=getFFVersion>=0.1? 16 : 0 

function dyniframesize(iframename) {
  var pTar = null;
  if (document.getElementById){
    pTar = document.getElementById(iframename);
  }
  else{
    eval('pTar = ' + iframename + ';');
  }
  //alert('pTar===='+pTar);

  if (pTar && !window.opera){
    //begin resizing iframe
    pTar.style.display="block"
    
    if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
      //ns6 syntax
      pTar.height = pTar.contentDocument.body.offsetHeight+FFextraHeight; 
      //alert( pTar.height );
    }
    else if (pTar.Document && pTar.Document.body.scrollHeight){
      //ie5+ syntax
      pTar.height = pTar.Document.body.scrollHeight;
      //alert('else===='+ pTar.height );
    }
  }
}

			function test(name) {
						document.getElementById(name).className="red";
						var tempName = '';
						for (var i =1;i <7;i++) {
						    tempName = 'radio'+i ;
						    if (tempName!= name) {
						          document.getElementById(tempName).className="bule";									
							}
						}
			}
			
function openContent(theObj,obj2)
{
    var obj2=eval(obj2);
	if(theObj.src.indexOf("_2")>-1)
	{
		theObj.src="../images/common/system/title_arrow_1.gif";
		obj2.style.display="none";
		
	}
	else if(theObj.src.indexOf("_1")>-1)
	{
		theObj.src="../images/common/system/title_arrow_2.gif";
		obj2.style.display="";
		
	}
}

function openTree(n) 
{
	obj = eval("tree" + n);
	sIcon=eval("icon" + n);
	if (obj.style.display == "none") 
	{
		sIcon.src="../biimages/info_more2.gif";
		obj.style.display = "block";//??????????		
	}
	else 
	{	
		sIcon.src="../biimages/info_more1.gif";
		obj.style.display = "none";	
	}
}


function collapseAll(){
for(i=1;i<4;i++)
{
   eval("tree" +i+ ".style.display = 'none'")
   eval("icon" +i+ ".src = '../biimages/info_more2.gif'")
}
}
function showAll(){
for(i=1;i<4;i++)
{
   eval("tree" +i+ ".style.display = 'block'")
   eval("icon" +i+ ".src = '../biimages/info_more1.gif'")
}
}

function openTree1(n) 
{
	var t1 = "tree" + n;
	var t2 = "icon" + n;
	
	
	obj = document.getElementById(t1);
	sIcon = document.getElementById(t2);
	if (obj.style.display == "none") 
	{
		sIcon.src="../biimages/sh/_.gif";
		obj.style.display = "block";//??????????		
	}
	else 
	{	
		sIcon.src="../biimages/sh/+.gif";
		obj.style.display = "none";	
	}
}
function collapseAll(){
for(i=1;i<4;i++)
{
   eval("tree" +i+ ".style.display = 'none'")
   eval("icon" +i+ ".src = '../biimages/sh/_.gif'")
}
}
function showAll(){
for(i=1;i<4;i++)
{
   eval("tree" +i+ ".style.display = 'block'")
   eval("icon" +i+ ".src = '../biimages/sh/+.gif'")
}
}