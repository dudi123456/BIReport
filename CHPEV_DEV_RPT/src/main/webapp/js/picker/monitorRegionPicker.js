///////////////////////////////////////////////
var outreObj = null;
var hidreObj = null;
var hrelevelObj = null;

document.writeln('<iframe id="monitorRegionPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function pickerDataRegion(nameObj,idObj,levelObj,regionValue,rootUrl){ 	//主调函数
    event.cancelBubble = true;
	outreObj = nameObj;
	hidreObj = idObj;
	hrelevelObj = levelObj;

	var pkStyle  = document.all.monitorRegionPicker.style;
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

	document.all.monitorRegionPicker.src=rootUrl+"?region_parent="+regionValue;
}
//
function closeLayerMonitorRegion(){               //这个层的关闭
	document.all.monitorRegionPicker.src="";
	document.all.monitorRegionPicker.style.visibility="hidden";
}
//
function getRegionData(rgName, rgId, rgLevel){	
	outreObj.value = rgName;
	hidreObj.value = rgId;
	hrelevelObj.value = rgLevel;
	closeLayerMonitorRegion();
}

function window.onresize()
{
	if(outreObj)
	{
		var nameObj = outreObj;
		var n_top  = nameObj.offsetTop;
		var n_left = nameObj.offsetLeft;
		var n_height = nameObj.clientHeight;

		while(nameObj = nameObj.offsetParent){
			n_top += nameObj.offsetTop;
			n_left += nameObj.offsetLeft;
		}

		var pkStyle  = document.all.monitorRegionPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

if (document.addEventListener)
{
	document.addEventListener('click',closeLayerMonitorRegion, false);
}
else
{
	document.attachEvent('onclick',closeLayerMonitorRegion);
}








