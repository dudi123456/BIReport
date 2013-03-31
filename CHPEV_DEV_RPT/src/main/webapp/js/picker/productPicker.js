var outObj = null;
var hidObj = null;
var hlevelObj = null;


document.writeln('<iframe id="ProductPicker" frameborder=1 scrolling="auto" style="position: absolute; width:200; height:300; z-index:9998; visibility:hidden"></iframe>');
function pickerProduct(nameObj,idObj,levelObj,Value,rootUrl){ 	//主调函数
    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;	
	hlevelObj = levelObj;


	var pkStyle  = document.all.ProductPicker.style;
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

	document.all.ProductPicker.src=rootUrl+"?parent_product="+Value;
}

function closeLayerProduct(){               //这个层的关闭
	document.all.ProductPicker.src="";
	document.all.ProductPicker.style.visibility="hidden";
}
//
function getProductData(rgName, rgId, rgLevel){	
	
	outObj.value = rgName;	
	hidObj.value = rgId;
	hlevelObj.value = rgLevel;	
	
	closeLayerProduct();
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

		var pkStyle  = document.all.ProductPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

if (document.addEventListener)
{
	document.addEventListener('click',closeLayerProduct, false);
}
else
{
	document.attachEvent('onclick',closeLayerProduct);
}



