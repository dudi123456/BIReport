var outObj = null;
var hidObj = null;
var myChgObj = null;

 //AJAX
  var data=null;
  var req=null;
  var console=null;
  var READY_STATE_UNINITIALIZED=0;
  var READY_STATE_LOADING=1;
  var READY_STATE_LOADED=2;
  var READY_STATE_INTERACTIVE=3;
  var READY_STATE_COMPLETE=4;

  function sendRequest(url,params,HttpMethod){
  	if (!HttpMethod){
        	HttpMethod="GET";
        }
        req=initXMLHTTPRequest();
        if (req){
        	req.onreadystatechange=onReadyState;
                req.open(HttpMethod,url,true);
                req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                req.send(params);
        }
  }
  function initXMLHTTPRequest(){
  	var xRequest=null;
        if (window.XMLHttpRequest){
        	xRequest=new XMLHttpRequest();
        } else if (window.ActiveXObject){
        	xRequest=new ActiveXObject("Microsoft.XMLHTTP");
        }
        return xRequest;
  }

  function onReadyState(){
 	 var ready=req.readyState;
  	if (ready==READY_STATE_COMPLETE){
  		data=req.responseText;
  		data=data.replace(/^\s+|\n+$/g,'');
  		ChangeOption(data);

  	}
  }
  //
  function cleanSelect(theObj){
  var i=0;
  var j=theObj.length;
  for (i=0;i<j;i++){
    theObj.options.remove(0);
  }
}





document.writeln('<iframe id="rgPicker" frameborder=1 scrolling="auto" style="position: absolute; width:160; height:250; z-index:9998; visibility:hidden"></iframe>');
function setRegion(nameObj, idObj,chgObj,regionId, rootUrl){ 	//主调函数

    event.cancelBubble = true;
	outObj = nameObj;
	hidObj = idObj;
	myChgObj = chgObj;
	var pkStyle  = document.all.rgPicker.style;
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

	document.all.rgPicker.src=rootUrl+"?region_parent="+regionId;
}

function closeLayerRegion(value){               //这个层的关闭
	document.all.rgPicker.src="";
	document.all.rgPicker.style.visibility="hidden";
	filterDept(value);
}
function getRegion(rgName, rgId){
	outObj.value = rgName;
	hidObj.value = rgId;
	closeLayerRegion(rgId);
}
//document.onclick = function(){
//	closeLayerRegion();
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

		var pkStyle  = document.all.rgPicker.style;
	  	pkStyle.top  = n_top + n_height + 5;
		pkStyle.left = n_left;
	}
}

  //过滤
 function filterDept(region){//这里你可以变成日期选择框发生变化时调用
	var params  = encodeURI("region_id=" + region);
	sendRequest("../system/filter_ajax.jsp",params,"POST");//这里你可以申请获取jsp，servlet 或者do操作，
 }

//解析
function ChangeOption(data){
  var data_a = data.split("|");//id/desc
  try{
	var theObj=myChgObj;
	cleanSelect(theObj);
	theObj.options[theObj.length]=new Option('不属任何部门','');

	//
	for(var i=0;i<data_a.length;i++){
	    var data_b = data_a[i].replace(/^\s+|\n+$/g,'').split(":");
		var name=data_b[1].replace(/^\s+|\n+$/g,'');
		var value=data_b[0].replace(/^\s+|\n+$/g,'');
                theObj.options[theObj.length]=new Option(name,value);
	}
 }catch(e){;}
 }


// ****************************************
// Start of document level event definition
// ****************************************
if (document.addEventListener)
{document.addEventListener('click',closeLayerRegion, false);}
else{document.attachEvent('onclick',closeLayerRegion);}
// ****************************************
//  End of document level event definition
// ****************************************

