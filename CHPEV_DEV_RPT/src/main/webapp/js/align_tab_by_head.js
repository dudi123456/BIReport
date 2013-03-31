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
function alignTableWidth(theAlignToTable,theAlignedTable,paddingAdjustment,isRowHead){
	var alignToTable=document.getElementById(theAlignToTable);
	var alignedTable=document.getElementById(theAlignedTable);
	if(!alignToTable) return;
	if(alignToTable.offsetWidth==0) return;
	if(!paddingAdjustment) paddingAdjustment=8;//等于iThTd的左右padding之和

	if ($.browser.webkit) {
		paddingAdjustment=0;
	}
	alignedTable.width=alignToTable.offsetWidth;

	var alignToTableBody = alignToTable.getElementsByTagName("tbody")[0];
	var alignedTableBody = alignedTable.getElementsByTagName("tbody")[0];
    var alignToTableRows      = alignToTableBody.getElementsByTagName("tr");
    var alignedTableRows      = alignedTableBody.getElementsByTagName("tr");
    //to be aligned getting the first line
    var alignToFirRow = alignToTableRows[0].getElementsByTagName("td");
    //the base getting the last row, if two rows' length not equal, then need to consider the 
    var alignedlastRow = alignedTableRows[alignedTableRows.length-1].getElementsByTagName("td");
    if(alignedlastRow.length != alignToFirRow.length && alignedTableRows.length >=2){
    	var targetRow=[];
    	//need to expand the alignedlastRow to right size,how
    	var hasRowSpan=false;
    	var hasColSpan=false;
    	//先将原来的复制进去，然后移动，只能支持两行，太多行太复杂了
    	var beforeLastRowCells=alignedTableRows[alignedTableRows.length-2].getElementsByTagName("td");
    	var index=0;
    	var lastRowIndex=0;
    	var targetIndex=0;
    	for(var i=0;i<beforeLastRowCells.length;i++){
    		
    		var colspans=beforeLastRowCells[index].colSpan;
    		var rowspans=beforeLastRowCells[index].rowSpan;
    		if( colspans >1){
    			for(var j=0; j< colspans; j++){
    				targetRow[targetIndex]=alignedlastRow[lastRowIndex];
    				lastRowIndex ++;
    				targetIndex ++;
    			}    			
    		}else if (rowspans >1){
    			targetRow[targetIndex]=beforeLastRowCells[index];
    			targetIndex ++;
    		}else{
    			targetRow[targetIndex]=alignedlastRow[lastRowIndex];
    			lastRowIndex ++;
    			targetIndex ++;
    		}
    		index ++;	
    	}
    	alignedlastRow=targetRow;
    }
	for(var i=0;i<alignToFirRow.length;i++){
		alignedlastRow[i].width=alignToFirRow[i].offsetWidth-paddingAdjustment;
		if((!isRowHead)&&(i==alignToFirRow.length-1)&&(alignToTable.offsetHeight>alignToTable.parentNode.offsetHeight)){
			alignedlastRow[i].width = alignToFirRow[i].offsetWidth + scrollBarWidth - paddingAdjustment;
			alignedTable.width = alignToTable.offsetWidth + scrollBarWidth;
		}
	}
}

function setScrollWidth(theAlignToTable,theAlignedTable,paddingAdjustment,isRowHead){
	var alignToTable=document.getElementById(theAlignToTable);
	var alignedTable=document.getElementById(theAlignedTable);
	if(!alignToTable) return;
	if(alignToTable.offsetWidth==0) return;
	if(!paddingAdjustment) paddingAdjustment=8;//等于iThTd的左右padding之和

	if ($.browser.webkit) {
		paddingAdjustment=0;
	}

	var alignToTableBody = alignToTable.getElementsByTagName("tbody")[0];
	var alignedTableBody = alignedTable.getElementsByTagName("tbody")[0];
    var alignToTableRows      = alignToTableBody.getElementsByTagName("tr");
    var alignedTableRows      = alignedTableBody.getElementsByTagName("tr");
    var alignToFirRow = alignToTableRows[0].getElementsByTagName("td");
    var alignedlastRow = alignedTableRows[alignedTableRows.length-1].getElementsByTagName("td");
    if(alignedlastRow.length != alignToFirRow.length && alignedTableRows.length >=2){
    	var targetRow=[];
    	//need to expand the alignedlastRow to right size,how
    	var hasRowSpan=false;
    	var hasColSpan=false;
    	//先将原来的复制进去，然后移动，只能支持两行，太多行太复杂了
    	var beforeLastRowCells=alignedTableRows[alignedTableRows.length-2].getElementsByTagName("td");
    	var index=0;
    	var lastRowIndex=0;
    	var targetIndex=0;
    	for(var i=0;i<beforeLastRowCells.length;i++){
    		
    		var colspans=beforeLastRowCells[index].colSpan;
    		var rowspans=beforeLastRowCells[index].rowSpan;
    		if( colspans >1){
    			for(var j=0; j< colspans; j++){
    				targetRow[targetIndex]=alignedlastRow[lastRowIndex];
    				lastRowIndex ++;
    				targetIndex ++;
    			}    			
    		}else if (rowspans >1){
    			targetRow[targetIndex]=beforeLastRowCells[index];
    			targetIndex ++;
    		}else{
    			targetRow[targetIndex]=alignedlastRow[lastRowIndex];
    			lastRowIndex ++;
    			targetIndex ++;
    		}
    		index ++;	
    	}
    	alignedlastRow=targetRow;
    }

	for(var i=0;i<alignToFirRow.length;i++){
		if((!isRowHead)&&(i==alignToFirRow.length-1)&&(alignToTable.offsetHeight>alignToTable.parentNode.offsetHeight)){
			alignedlastRow[i].width = alignToFirRow[i].offsetWidth + scrollBarWidth - paddingAdjustment;
			alignedTable.width = alignToTable.offsetWidth  + scrollBarWidth;
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
function alignTableHeight(theAlignToTable,theAlignedTable,paddingAdjustmentH,isHead){
	if(!theAlignToTable) return;

	var alignToTable = document.getElementById(theAlignToTable);
	var alignedTable = document.getElementById(theAlignedTable);

	var alignedTableBody = alignedTable.getElementsByTagName("tbody")[0];
    var alignedTableRows      = alignedTableBody.getElementsByTagName("tr");
    var alignedlastRow = alignedTableRows[alignedTableRows.length-1];

    var alignToTableBody = alignToTable.getElementsByTagName("tbody")[0];
    var alignToTableRows      = alignToTableBody.getElementsByTagName("tr");

    if(!isHead){
    	 alignToTableRows[alignToTableRows.length-1].style.height = alignToTableRows[0].offsetHeight;
    	 alignedTableRows[alignedTableRows.length-2].style.height = alignToTableRows[alignToTableRows.length-1].offsetHeight;
    }

	if(alignToTable.offsetHeight == 0) return;
	if(!paddingAdjustmentH) paddingAdjustmentH=0;//等于iThTd的上下padding之和

	if(alignedTable.offsetHeight > alignToTable.offsetHeight && isHead){
		alignToTable.style.height = alignedTable.offsetHeight;
	}else if(alignedTable.offsetHeight < alignToTable.offsetHeight){
		if(alignedTableRows.length > alignToTableRows.length){
			alignedTable.style.height = alignToTable.offsetHeight + alignedlastRow.offsetHeight + paddingAdjustmentH;
		}else{
			alignedTable.style.height = alignToTable.offsetHeight;
		}
	}

	if(!isHead){
		if(alignToTable.offsetHeight > alignToTable.parentNode.offsetHeight){
			if((alignToTable.offsetWidth + scrollBarWidth) > alignToTable.parentNode.offsetWidth){
				alignedlastRow.style.display = "";
				if ($.browser.webkit) {
					alignedlastRow.style.height = scrollBarWidth +1 ;
				}else{
					alignedlastRow.style.height = scrollBarWidth;
				}
			}else{

				alignedlastRow.style.display = "none";
			}
		}else{
			if(alignToTable.offsetWidth > alignToTable.parentNode.offsetWidth){
				alignedlastRow.style.display = "";
				if ($.browser.webkit) {
					alignedlastRow.style.height = scrollBarWidth +1;
				}else{
					alignedlastRow.style.height = scrollBarWidth;
				}

			}else{
				alignedlastRow.style.display = "none";
			}
		}
		//alert(alignedlastRow.offsetHeight);
	}


}


//如果theObj1和theObj2的高度之和小于theTotalObj的高度，则重设theTotalObj的高度为theObj1与theObj2高度之和加theAdjustment调整值（整数）
//用于调整容器（table,iframe）高度
function setTotalHeight(theTotalObj,theObj1,theObj2,theAdjustment){

	var totalObj = document.getElementById(theTotalObj);
	var obj1 = document.getElementById(theObj1);
	var obj2 = document.getElementById(theObj2);

	if((obj1.offsetHeight + obj2.offsetHeight + theAdjustment) < totalObj.offsetHeight){
		totalObj.style.height=obj1.offsetHeight + obj2.offsetHeight + theAdjustment;
	}
}

//根据系列号theTableSeriesNo执行该系列表格对齐操作
//如<body onLoad="alignTable()">
function alignTable(theTableSeriesNo){
	//alert(getScrollerWidth());
	if(!document.getElementById("iTable_TableContainer")){
		return;
	}
	if(!document.getElementById("iTable_HeadTable1")){
		return;
	}
	var padding = 9;
	var cellspace = 2;
	if ($.browser.msie){
		padding=9;
	}
	if ($.browser.mozilla){
		padding=7;
	}


	//需要设置DIV宽度和高度才可以
	//先获取父亲宽度
	var parentWidth=0;
	var pEle = document.getElementById("olapTableContent");
	var parentHeight = 0;
	if(pEle){
		parentHeight = pEle.offsetHeight;
		parentWidth = pEle.offsetWidth;
	}else{
		parentWidth = document.getElementById("iTable_TableContainer").offsetWidth;
		parentHeight = document.getElementById("iTable_TableContainer").offsetHeight;
	}
	//获取左侧的宽度，以标题宽度为准
	var leftWidth = document.getElementById("iTable_LeftTable1").offsetWidth;
	//计算右侧内容DIV宽度
    var rightWidth = parentWidth - leftWidth - cellspace;
    //设置右侧内容宽度
    document.getElementById("LayerRight1").style.width = rightWidth;
    //设置右侧头div宽度
    if(document.getElementById("fixHeadRightPart")){
    	document.getElementById("fixHeadRightPart").style.width = rightWidth;
    }
    if(document.getElementById("Layer1")){
    	document.getElementById("Layer1").style.width = rightWidth;
    }



	if(!theTableSeriesNo) theTableSeriesNo=1;
	switch(theTableSeriesNo){
		case 1:

			if(document.getElementById('iTable_LeftHeadTable1'))
			{//如果有锁定的左列
				if(document.getElementById('iTable_LeftTable1') && document.getElementById('iTable_ContentTable1') && document.getElementById('iTable_HeadTable1') && document.getElementById('iTable_LeftHeadTable1')){
					alignTableWidth("iTable_LeftTable1","iTable_LeftHeadTable1",padding,true);
					alignTableHeight("iTable_HeadTable1","iTable_LeftHeadTable1",'',true);
					alignTableWidth("iTable_ContentTable1","iTable_HeadTable1",padding);
					alignTableHeight("iTable_HeadTable1","iTable_LeftHeadTable1",'',true);
					alignTableHeight("iTable_ContentTable1","iTable_LeftTable1",3);
					setTotalHeight("iTable_TableContainer","iTable_HeadTable1","iTable_LeftTable1",3);
				}
			}
			else
			{//如果没有锁定的左列
				if(document.getElementById('iTable_ContentTable1') && document.getElementById('iTable_HeadTable1')){
					alignTableWidth("iTable_ContentTable1","iTable_HeadTable1");
					iTable_HeadTable1.parentNode.parentNode.height=iTable_HeadTable1.offsetHeight;
					setTotalHeight("iTable_TableContainer","iTable_HeadTable1","iTable_ContentTable1",18);
				}
			}
			break;
		case 2:
			break;
		};
		 //设置右侧DIV高度
	    var leftHeight = document.getElementById("iTable_ContentTable1").offsetHeight;
	    var parentWidth = document.getElementById("iTable_ContentTable1").parentNode.offsetWidth;

	    var scrollHeight = 0;
	    if(document.getElementById("iTable_ContentTable1").offsetWidth > parentWidth){
	    	leftHeight = leftHeight + scrollBarWidth;
	    	scrollHeight = scrollBarWidth;
	    }
	    if((leftHeight + scrollHeight)> parentHeight){
	    	//需要去掉 padding，和滚动条iTable_HeadTable1
	    	leftHeight = parentHeight - document.getElementById("iTable_HeadTable1").offsetHeight - cellspace;
	    	document.getElementById("LayerRight1").style.height = leftHeight + 1;
	     	document.getElementById("LayerLeft1").style.height = leftHeight + 1;
	    }
	   
	    var obj=document.getElementById("LayerRight1");
	    if((obj.scrollHeight > obj.clientHeight || obj.offsetHeight > obj.clientHeight) &&
	    		!(obj.scrollWidth > obj.clientWidth + scrollBarWidth || obj.offsetWidth > obj.clientWidth + scrollBarWidth)){
	    	alignTableWidth("iTable_ContentTable1","iTable_HeadTable1",padding);
	    	alignTableHeight("iTable_ContentTable1","iTable_LeftTable1",3);
	    }
	    setScrollWidth("iTable_ContentTable1","iTable_HeadTable1",padding);

	 window.status="完毕";
}

//根据系列号theDivSeriesNo执行该系列DIV的同步滚动
function syncScroll(theDivSeriesNo){
	if(!theDivSeriesNo) theDivSeriesNo=1;

	var leftTable = document.getElementById("iTable_LeftTable1");
	var headTable = document.getElementById("iTable_HeadTable1");
	var contentTable = document.getElementById("iTable_ContentTable1");
	switch(theDivSeriesNo){
		case 1:
			if(document.getElementById('iTable_LeftHeadTable1')){//如果有锁定的左列
				leftTable.parentNode.scrollTop = contentTable.parentNode.scrollTop;
				headTable.parentNode.scrollLeft = contentTable.parentNode.scrollLeft;
			}
			else{//如果没有锁定的左列
				headTable.parentNode.scrollLeft = contentTable.parentNode.scrollLeft;
			}
			break;
		case 2:
			break;
		};
}



