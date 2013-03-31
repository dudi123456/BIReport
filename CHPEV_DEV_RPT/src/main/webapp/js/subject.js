function bodyOnload()
{
	alignTable();	
}

/*锁定表格表头左列相关function*/

/*
功能：	对齐表格列宽，theAlignedTable每列的宽度对齐到theAlignToTable每列的宽度
参数：	theAlignToTable：Object，对齐到该表格
				theAlignedTable：Object，被重新对齐的表格
				paddingAdjustment：数值，被重新对齐表格的每格的左右padding之和，如果不输入缺省为8
				isRowHead；布尔值，theAlignedTable是否是行头所在的表格
示例：
				alignTableWidth(iTable_LeftTable1,iTable_LeftHeadTable1,'',true);
				alignTableWidth(iTable_ContentTable1,iTable_HeadTable1);
返回值：无
*/
function alignTableWidth(theAlignToTable,theAlignedTable,paddingAdjustment,isRowHead)
{
	if(!theAlignToTable) return;

	if(!paddingAdjustment) paddingAdjustment=8;//等于iThTd的左右padding之和
	
	theAlignedTable.width=theAlignToTable.offsetWidth;
	for(var i=0;i<theAlignToTable.firstChild.firstChild.childNodes.length;i++)
	{
		theAlignedTable.firstChild.lastChild.childNodes[i].width=theAlignToTable.firstChild.firstChild.childNodes[i].offsetWidth-paddingAdjustment;

		if((!isRowHead)&&(i==theAlignToTable.firstChild.firstChild.childNodes.length-1)&&(theAlignToTable.offsetHeight>theAlignToTable.parentNode.offsetHeight))
		{
			theAlignedTable.firstChild.lastChild.childNodes[i].width=theAlignToTable.firstChild.firstChild.childNodes[i].offsetWidth+17-paddingAdjustment;
			theAlignedTable.width=theAlignToTable.offsetWidth+17;
		}
	}
}

/*
功能：	对齐表格行高，theAlignedTable每列的高度对齐到theAlignToTable每列的高度
参数：	theAlignToTable：Object，对齐到该表格
				theAlignedTable：Object，被重新对齐的表格
				paddingAdjustmentH：数值，被重新对齐表格的每格的上下padding之和，如果不输入缺省为8
				isHead；布尔值，theAlignedTable是否是表头所在的表格
示例：
				alignTableHeight(iTable_ContentTable1,iTable_LeftTable1);
				alignTableHeight(iTable_HeadTable1,iTable_LeftHeadTable1,'',true);
返回值：无
*/
function alignTableHeight(theAlignToTable,theAlignedTable,paddingAdjustmentH,isHead)
{
	if(!theAlignToTable) return;
	//if(!theAlignedTable)theAlignedTable=theAlignToTable.parentNode.previousSibling;
	if(!paddingAdjustmentH) paddingAdjustmentH=8;//等于iThTd的上下padding之和

	for(var i=0;i<theAlignToTable.firstChild.childNodes.length;i++)
	{
		theAlignedTable.firstChild.childNodes[i].lastChild.height=theAlignToTable.firstChild.childNodes[i].firstChild.offsetHeight;
		theAlignToTable.firstChild.childNodes[i].firstChild.height=theAlignedTable.firstChild.childNodes[i].lastChild.offsetHeight;
		if((!isHead)&&(i==theAlignToTable.firstChild.childNodes.length-1))
		{
			if(theAlignToTable.offsetWidth>theAlignToTable.parentNode.offsetWidth)
			{
				/*theAlignedTable.firstChild.childNodes[i].lastChild.height=theAlignToTable.firstChild.childNodes[i].firstChild.offsetHeight+17;
				theAlignToTable.firstChild.childNodes[i].firstChild.height=theAlignedTable.firstChild.childNodes[i].lastChild.offsetHeight-17;
				*/
				theAlignedTable.firstChild.childNodes[i+1].style.display="";
				theAlignedTable.firstChild.childNodes[i+1].lastChild.height=17;
			}
			else
			{
				theAlignedTable.firstChild.childNodes[i+1].style.display="none";
			}
					
		}
	}
}

//如果theObj1和theObj2的高度之和小于theTotalObj的高度，则重设theTotalObj的高度为theObj1与theObj2高度之和加theAdjustment调整值（整数）
//用于调整容器（table,iframe）高度
function setTotalHeight(theTotalObj,theObj1,theObj2,theAdjustment)
{
	if((theObj1.offsetHeight+theObj2.offsetHeight+theAdjustment)<theTotalObj.offsetHeight)
	{
		theTotalObj.style.height=theObj1.offsetHeight+theObj2.offsetHeight+theAdjustment;
	}
}

//根据系列号theTableSeriesNo执行该系列表格对齐操作
//如<body onLoad="alignTable()">
function alignTable(theTableSeriesNo)
{
	if(!theTableSeriesNo) theTableSeriesNo=1;
	switch(theTableSeriesNo){
		case 1:
		
			if(document.getElementById('iTable_LeftHeadTable1'))
			{//如果有锁定的左列
				alignTableWidth(iTable_LeftTable1,iTable_LeftHeadTable1,'5',true);
				alignTableWidth(iTable_ContentTable1,iTable_HeadTable1,5);
				alignTableHeight(iTable_ContentTable1,iTable_LeftTable1);
				alignTableHeight(iTable_HeadTable1,iTable_LeftHeadTable1,'',true);
				setTotalHeight(iTable_TableContainer,iTable_HeadTable1,iTable_LeftTable1,2);
			}
			else
			{//如果没有锁定的左列
				alignTableWidth(iTable_ContentTable1,iTable_HeadTable1);
				iTable_HeadTable1.parentNode.parentNode.height=iTable_HeadTable1.offsetHeight;
				setTotalHeight(iTable_TableContainer,iTable_HeadTable1,iTable_ContentTable1,18);
			}
			break;
		case 2:
			break;
		};
	

}

//根据系列号theDivSeriesNo执行该系列DIV的同步滚动
function syncScroll(theDivSeriesNo)
{
	if(!theDivSeriesNo) theDivSeriesNo=1;
	switch(theDivSeriesNo){
		case 1:
			if(document.getElementById('iTable_LeftHeadTable1'))
			{//如果有锁定的左列
				iTable_LeftTable1.parentNode.scrollTop=iTable_ContentTable1.parentNode.scrollTop;
				iTable_HeadTable1.parentNode.scrollLeft=iTable_ContentTable1.parentNode.scrollLeft;
			}
			else
			{//如果没有锁定的左列
				iTable_HeadTable1.parentNode.scrollLeft=iTable_ContentTable1.parentNode.scrollLeft;
			}
			break;
		case 2:
			break;
		};
}

//生成标签
function buildTaber(theActivedId)
	  {
	  	var taberInnerHTMLTemp='';

		taberInnerHTMLTemp+='<table width="100%" border="0" cellpadding="0" cellspacing="0"';
		taberInnerHTMLTemp+='          <tr> ';

		for(i=1;i<taberArr.length;i++)
		{
				if(i>1)
		{
			taberInnerHTMLTemp+='            <td><img src="../biimages/size.gif" width="1"></td>';
		}
			if(taberArr[i][0]==theActivedId)
			{
				if(i!=1)taberInnerHTMLTemp+='            <td></td>';
				taberInnerHTMLTemp+='            <td><img src="../biimages/zt/tab_left_on.gif"></td>';
				taberInnerHTMLTemp+='            <td nowrap background="../biimages/zt/tab_bg_on.gif" class="tab-button-on" onClick="buildTaber(\''+taberArr[i][0]+'\')">'+taberArr[i][1]+'</td>';
				taberInnerHTMLTemp+='            <td><img src="../biimages/zt/tab_right_on.gif"></td>';			
				eval(taberArr[i][3]).location.href=taberArr[i][2];
			}
			else
			{
				if(i!=1)taberInnerHTMLTemp+='            <td></td>';
				taberInnerHTMLTemp+='            <td><img src="../biimages/zt/tab_left_off.gif"></td>';
				taberInnerHTMLTemp+='            <td nowrap background="../biimages/zt/tab_bg_off.gif" class="tab-button-off" onMouseOver="this.className=\'tab-button-off-over\'" onMouseOut="this.className=\'tab-button-off\'" onClick="buildTaber(\''+taberArr[i][0]+'\')">'+taberArr[i][1]+'</td>';
				taberInnerHTMLTemp+='            <td><img src="../biimages/zt/tab_right_off.gif"></td>';
			}	
		}
		
                taberInnerHTMLTemp+='          <td nowrap width="15"></td>';
		taberInnerHTMLTemp+='          </tr>'; 		
		taberInnerHTMLTemp+='        </table>'; 
	  	taber.innerHTML=taberInnerHTMLTemp;	
	  }	  

//页面信息展示区的页签
function buildTaberArea(theTaberSpanID,theActivedTaberId,theTaberArrayName)
{
	var theTaberSpanTemp="";
	var theTaberArray=eval(theTaberArrayName);
	var theTaberSpan=eval(theTaberSpanID);
	
	theTaberSpanTemp+='				<table border="0" cellspacing="0" cellpadding="0">';
	theTaberSpanTemp+='        	<tr>';

	for(var i=1;i<theTaberArray.length;i++)
	{
			theTaberSpanTemp+='                              <td><img src="../biimages/size.gif" width="1"</td>';
	   
		if(theTaberArray[i][0]==theActivedTaberId)
		{
			theTaberSpanTemp+='        	  <td><img src="../biimages/zt/tab_left_on.gif"></td>';
			theTaberSpanTemp+='        	  <td nowrap class="tab-button-on"><a class="tab-button-on-link" href="javascript:" onClick="buildTaberArea(\''+theTaberSpanID+'\',\''+theTaberArray[i][0]+'\',\''+theTaberArrayName+'\')">'+theTaberArray[i][1]+'</a></td>';
			theTaberSpanTemp+='        	  <td><img src="../biimages/zt/tab_right_on.gif"></td>';
			eval(theTaberArray[i][2]);
			//alert(theTaberSpanTemp);
		}
		else
		{
			theTaberSpanTemp+='        	  <td><img src="../biimages/zt/tab_left_off.gif"></td>';
			theTaberSpanTemp+='        	  <td nowrap class="tab-button-off"><a class="tab-button-off-link" href="javascript:" onClick="buildTaberArea(\''+theTaberSpanID+'\',\''+theTaberArray[i][0]+'\',\''+theTaberArrayName+'\')">'+theTaberArray[i][1]+'</a></td>';
			theTaberSpanTemp+='        	  <td><img src="../biimages/zt/tab_right_off.gif"></td>';
		}
	}

	theTaberSpanTemp+='        	</tr>';
	theTaberSpanTemp+='         </table>';
	
	theTaberSpan.innerHTML=theTaberSpanTemp;
}

//iframe不出现滚动条
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
		theObj.src="../biimages/zt/title_arrow_1.gif";
		obj2.style.display="none";
		
	}
	else if(theObj.src.indexOf("_1")>-1)
	{
		theObj.src="../biimages/zt/title_arrow_2.gif";
		obj2.style.display="";
		
	}
	
	window.parent.dyniframesize('dev_commision');

}
