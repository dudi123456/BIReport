//切换theObj的样式，在"样式"和"样式-over"之间
//如： onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
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

//(鼠标移上移出时)切换源，如切换图片img
function switchSrc(theObj)
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
	alert(theAlignToTable.firstChild.firstChild.childNodes.length);
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
//add by jcm
function alignTableWidthSingle(theAlignToTable,theAlignedTable,paddingAdjustment,isRowHead)
{
	if(!theAlignToTable) return;

	if(!paddingAdjustment) paddingAdjustment=8;//等于iThTd的左右padding之和
	
	theAlignedTable.width=theAlignToTable.offsetWidth;
	
}

function fixTable(mainTableHead,mainTableBody,Layer1)
{	
	
	for(var i=0;i<mainTableHead.rows[0].cells.length;i++)
	{
	 if(mainTableBody.rows[0].cells[i]!=null){
	  if(mainTableHead.rows[0].cells[i].offsetWidth>mainTableBody.rows[0].cells[i].offsetWidth)
		{
      mainTableBody.rows[0].cells[i].style.width = mainTableHead.rows[0].cells[i].offsetWidth-11;
		}
		else
		{
      mainTableHead.rows[0].cells[i].style.width = mainTableBody.rows[0].cells[i].offsetWidth-11;
		}
	 }
	}

	if(mainTableHead.offsetWidth>mainTableBody.offsetWidth)
	{
		mainTableBody.style.width = mainTableHead.offsetWidth
	}
	else
	{
		mainTableHead.width = mainTableBody.offsetWidth
	}
	Layer1.style.width = Layer1.scrollWidth+18
	Layer1.style.height = document.body.clientHeight - Layer1.offsetTop
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
/*锁定表格表头左列相关function*/
