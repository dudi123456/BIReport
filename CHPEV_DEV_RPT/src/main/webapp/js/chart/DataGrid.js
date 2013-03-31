
function gridSetCurRowBg(rowId) {
	var obj = window.event.srcElement;
	var tdObj = obj;
	while ("TD" != tdObj.tagName) {
		tdObj = tdObj.parentNode;
	}
	var eventType = window.event.type;
	if ("mouseover" == eventType) {
		var brotherTdObjs = tdObj.parentNode.childNodes;
		for (var i = 0; i < brotherTdObjs.length; i++) {
			brotherTdObjs[i].style.background = "rgb(210, 232, 244)";
		}
		//tdObj.style.background="rgb(218, 236, 245)";
	} else {
		if ("mouseout" == eventType) {
			var brotherTdObjs = tdObj.parentNode.childNodes;
			var bgColor;
			if (rowId % 2 == 1) {
				bgColor = "#F1FAFC";
			} else {
				bgColor = "#FFFFFF";
			}
			for (var i = 0; i < brotherTdObjs.length; i++) {
				brotherTdObjs[i].style.background = bgColor;
			}
		}
	}
}

function gridScroll(tableId) {
	var headDivObj = document.getElementById(tableId + "_head_div");
	var dataDivObj = document.getElementById(tableId + "_data_div");
	headDivObj.scrollLeft = dataDivObj.scrollLeft;
}

