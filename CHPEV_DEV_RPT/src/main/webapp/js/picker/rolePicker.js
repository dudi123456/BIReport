
document.writeln('<iframe id="role_list_frame" src="roleLeveTrees.jsp" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function showRole(nameObj){ 	//主调函数
    event.cancelBubble = true;
	outObj = nameObj;
	var pkStyle  = document.all("role_list_frame").style;
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
}

function closeLayerRole(){               //这个层的关闭
//	document.all.src="";
	document.all("role_list_frame").style.visibility="hidden";
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

		var pkStyle  = document.all("role_list_frame").style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

// ****************************************
// Start of document level event definition
// ****************************************
if (document.addEventListener)
{document.addEventListener('click',closeLayerRole, false);}
else{document.attachEvent('onclick',closeLayerRole);}
// ****************************************
//  End of document level event definition
// ****************************************

