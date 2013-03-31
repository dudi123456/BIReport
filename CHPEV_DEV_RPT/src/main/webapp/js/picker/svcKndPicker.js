var outObj = null;
var hidObj = null;
var hlevelObj = null;


document.writeln('<iframe id="svcKndPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function pickerDataSvcKnd(nameObj,idObj,levelObj,svcValue,rootUrl){ 	//主调函数
    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;	
	hlevelObj = levelObj;


	var pkStyle  = document.all.svcKndPicker.style;
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

	document.all.svcKndPicker.src=rootUrl+"?svc_parent="+svcValue;
}

function closeLayerSvcKnd(){               //这个层的关闭
	document.all.svcKndPicker.src="";
	document.all.svcKndPicker.style.visibility="hidden";
}
function getSvcKndData(rgName, rgId, rgLevel){	
	
	outObj.value = rgName;	
	hidObj.value = rgId;
	hlevelObj.value = rgLevel;	
	closeLayerSvcKnd();
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

		var pkStyle  = document.all.svcKndPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

if (document.addEventListener)
{
	document.addEventListener('click',closeLayerSvcKnd, false);
}
else
{
	document.attachEvent('onclick',closeLayerSvcKnd);
}



