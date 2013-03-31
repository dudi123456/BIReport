var outObj = null;
var hidObj = null;


document.writeln('<iframe id="regionPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function setRegion(nameObj, idObj, regionValue,rootUrl){ 	//主调函数

    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;


	var pkStyle  = document.all.regionPicker.style;
	var n_top  = nameObj.offsetTop;
	var n_left = nameObj.offsetLeft;
	var n_height = nameObj.clientHeight;

	while(nameObj = nameObj.offsetParent){
		n_top += nameObj.offsetTop;
		n_left += nameObj.offsetLeft;
	}
  	pkStyle.top  = n_top + n_height + 5;
	pkStyle.left = n_left;
	pkStyle.visibility = 'visible';
//	alert(rootUrl);
	document.all.regionPicker.src=rootUrl+"?region_parent="+regionValue;
}

function closeLayerArea(){               //这个层的关闭
	document.all.regionPicker.src="";
	document.all.regionPicker.style.visibility="hidden";
}
function getArea(rgName, rgId){
	outObj.value = rgName;
	hidObj.value = rgId;
	closeLayerArea();
}
document.onclick = function(){
	closeLayerArea();
}
function window.onresize()
{
	if(outObj)
	{
		var nameObj = outObj;
		var n_top  = nameObj.offsetTop;
		var n_left = nameObj.offsetLeft;
		var n_height = nameObj.clientHeight;

		while(nameObj = nameObj.offsetParent){
			n_top += nameObj.offsetTop;
			n_left += nameObj.offsetLeft;
		}

		var pkStyle  = document.all.regionPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}



