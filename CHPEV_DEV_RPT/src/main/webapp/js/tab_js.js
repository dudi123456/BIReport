//页面信息展示区的页签
function buildTaberArea(theTaberSpanID,theActivedTaberId,theTaberArrayName,theRptID,theDictsLen)
{
	var theTaberSpanTemp="";
	var theTaberArray=eval(theTaberArrayName);
	var theTaberSpan=eval(theTaberSpanID);
	theDictsLen = eval(theDictsLen);

	theTaberSpanTemp+='<table border="0" cellspacing="0" cellpadding="0">';
	theTaberSpanTemp+='  <tr>';
	for(var i=1;i<theTaberArray.length;i++)
	{
		if(theTaberArray[i][0]==theActivedTaberId){		  
			theTaberSpanTemp+='<td class="body-tabarea-button-left-on"><img src="../images/common/tab/size.gif" width="5" height="1"></td>';
			theTaberSpanTemp+='<td nowrap class="body-tabarea-button-on"><a class="body-tabarea-button-on-link" href="javascript:" onClick="buildTaberArea(\''+theTaberSpanID+'\',\''+theTaberArray[i][0]+'\',\''+theTaberArrayName+'\',\''+theRptID+'\',\''+theDictsLen+'\')">'+theTaberArray[i][1]+'</a></td>';
			theTaberSpanTemp+='<td class="body-tabarea-button-right-on"><img src="../images/common/tab/size.gif" width="5" height="1"></td>';
			theTaberSpanTemp+='<td ><img src="../images/common/tab/size.gif" width="1" height="1"></td>';
			eval(theTaberArray[i][2]);
		}else if(theRptID==""||theRptID=="0"||(theRptID!="0"&&theDictsLen==0&&i>2)){   
			theTaberSpanTemp+='<td class="body-tabarea-button-left"><img src="../images/common/tab/size.gif" width="5" height="1"></td>';
			theTaberSpanTemp+='<td nowrap class="body-tabarea-button">'+theTaberArray[i][1]+'</td>';
			theTaberSpanTemp+='<td class="body-tabarea-button-right"><img src="../images/common/tab/size.gif" width="5" height="1"></td>';
			theTaberSpanTemp+='<td><img src="../images/common/tab/size.gif" width="1" height="1"></td>';
		}else{		   
			theTaberSpanTemp+='<td class="body-tabarea-button-left"><img src="../images/common/tab/size.gif" width="5" height="1"></td>';
			theTaberSpanTemp+='<td nowrap class="body-tabarea-button"><a class="body-tabarea-button-link" href="javascript:" onClick="buildTaberArea(\''+theTaberSpanID+'\',\''+theTaberArray[i][0]+'\',\''+theTaberArrayName+'\',\''+theRptID+'\',\''+theDictsLen+'\')">'+theTaberArray[i][1]+'</a></td>';
			theTaberSpanTemp+='<td class="body-tabarea-button-right"><img src="../images/common/tab/size.gif" width="5" height="1"></td>';
			theTaberSpanTemp+='<td><img src="../images/common/tab/size.gif" width="1" height="1"></td>';
		}
	}

	theTaberSpanTemp+='        	</tr>';
	theTaberSpanTemp+='         </table>';
	
	theTaberSpan.innerHTML=theTaberSpanTemp;
}

//(鼠标移上移出时)切换样式
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