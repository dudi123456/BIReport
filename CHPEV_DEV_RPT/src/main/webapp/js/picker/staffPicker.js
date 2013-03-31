var outObj = null;
var hidObj = null;
document.writeln('<iframe id="staffPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function setManager(nameObj,idObj,regionId,rootUrl){

    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;

	var pkStyle  = document.all.staffPicker.style;
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

	document.all.staffPicker.src=rootUrl+"?region_parent="+regionId;
}

function closeLayerStaff(){
	document.all.staffPicker.src="";
	document.all.staffPicker.style.visibility="hidden";

}
function getStaff(Name, Id){
	outObj.value = Name;
	hidObj.value = Id;
	closeLayerStaff();
}
document.onclick = function(){
	closeLayerStaff();
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

		var pkStyle  = document.all.staffPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

if (document.addEventListener)
{
	document.addEventListener('click',closeLayerStaff, false);
}
else
{
	document.attachEvent('onclick',closeLayerStaff);
}

