var outObj = null;
var hidObj = null;
document.writeln('<iframe id="menuPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function setMenu(nameObj,idObj,rootUrl){

    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;

	var pkStyle  = document.all.menuPicker.style;
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

	document.all.menuPicker.src=rootUrl;
}

function closeLayerMenu(){
	document.all.menuPicker.src="";
	document.all.menuPicker.style.visibility="hidden";

}
function getMenu(menuName, menuId){
	outObj.value = menuName;
	hidObj.value = menuId;
	closeLayerMenu();
}
//document.onclick = function(){
//	closeLayerMenu();
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

		var pkStyle  = document.all.menuPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

// ****************************************
// Start of document level event definition
// ****************************************
if (document.addEventListener)
{document.addEventListener('click',closeLayerMenu, false);}
else{document.attachEvent('onclick',closeLayerMenu);}
// ****************************************
//  End of document level event definition
// ****************************************