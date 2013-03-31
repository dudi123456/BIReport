var outObj = null;
var hidObj = null;
var valObj_A = null;
var valObj_B = null;

document.writeln('<iframe id="dpPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function setDept(nameObj, idObj,myObjA,myObjB,deptno,rootUrl){ 	//主调函数

    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;
	valObj_A = myObjA;
	valObj_B = myObjB;

	var pkStyle  = document.all.dpPicker.style;
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
//	alert(rootUrl+"?dept_parent="+deptno);
	document.all.dpPicker.src=rootUrl+"?dept_parent="+deptno;
}

function closeLayerDept(){               //这个层的关闭
	document.all.dpPicker.src="";
	document.all.dpPicker.style.visibility="hidden";
}
function getDept(dept_name,dept_no,rgName,rgId){
	outObj.value = dept_name;
	hidObj.value = dept_no;
	valObj_A.value = rgId;
	valObj_B.value = rgName;
	closeLayerDept();
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

		var pkStyle  = document.all.dpPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

// ****************************************
// Start of document level event definition
// ****************************************
if (document.addEventListener)
{document.addEventListener('click',closeLayerDept, false);}
else{document.attachEvent('onclick',closeLayerDept);}
// ****************************************
//  End of document level event definition
// ****************************************

