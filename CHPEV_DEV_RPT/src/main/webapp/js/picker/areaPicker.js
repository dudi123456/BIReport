var outObj = null;
var hidObj = null;
var myRegion = null;


document.writeln('<iframe id="areaPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function setArea(nameObj, idObj, regionObj,rootUrl){ 	//主调函数

    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;
	myRegion  = regionObj;

	var pkStyle  = document.all.areaPicker.style;
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

	document.all.areaPicker.src=rootUrl+"?region_parent="+myRegion.value;
}

function closeLayerArea(){               //这个层的关闭
	document.all.areaPicker.src="";
	document.all.areaPicker.style.visibility="hidden";
}
function getArea(rgName, rgId){
	outObj.value = rgName;
	hidObj.value = rgId;
	closeLayerArea();
}
//document.onclick = function(){
//	closeLayerArea();
//}
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

		var pkStyle  = document.all.areaPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

// ****************************************
// Start of document level event definition
// ****************************************
if (document.addEventListener)
{document.addEventListener('click',closeLayerArea, false);}
else{document.attachEvent('onclick',closeLayerArea);}
// ****************************************
//  End of document level event definition
// ****************************************

