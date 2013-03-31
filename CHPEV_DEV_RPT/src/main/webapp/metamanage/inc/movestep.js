self.onError=null;
var CURRENTX = CURRENTY = 0;
var COORDX = COORDY = 2000;
var XRATE = YRATE = 0;
var MOVEOBJ = null;
var CANVASOBJ = null;
var MOVEFLAG = false;

function dragIt(obj,canvasIt) {
	document.onmousemove = moveIt;
	document.onmouseup = dropIt;

    MOVEOBJ = obj; 
    CURRENTX = (event.clientX + document.body.scrollLeft);
    CURRENTY = (event.clientY + document.body.scrollTop);
	
    CANVASOBJ = document.getElementById(canvasIt);
	var canvasSize = CANVASOBJ.coordsize+'';
	COORDX = parseInt(canvasSize.slice(0,canvasSize.indexOf(',')));
	COORDY = parseInt(canvasSize.slice(canvasSize.indexOf(',')+1,canvasSize.length));
	XRATE = COORDX/parseInt(CANVASOBJ.style.width);
	YRATE = COORDY/parseInt(CANVASOBJ.style.height);
	
    return true;
}

function moveIt() {
    if (MOVEOBJ == null) { return false; }
	MOVEFLAG = true;
	
	newX = (event.clientX + document.body.scrollLeft);
    newY = (event.clientY + document.body.scrollTop);
    distanceX = parseInt(XRATE*(newX - CURRENTX));//X坐标换算
    distanceY = parseInt(YRATE*(newY - CURRENTY));//Y坐标换算	
	newLeft = parseInt(MOVEOBJ.style.left) + distanceX;
	newTop = parseInt(MOVEOBJ.style.top) + distanceY;

//	if(newLeft >= 0 && newLeft <= (COORDX-parseInt(MOVEOBJ.style.width)) && newTop >= 0 && newTop <= (COORDY-parseInt(MOVEOBJ.style.height))){		
		MOVEOBJ.style.left = newLeft;
		MOVEOBJ.style.top = newTop;
		MOVEOBJ.style.zIndez = CONST_LAY_TOPPEST;
		CURRENTX = newX;
		CURRENTY = newY;

		var FlowXML = document.all.FlowXML;
	    if(FlowXML.value!=''){
		   var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
		   xmlDoc.async = false;
		   xmlDoc.loadXML(FlowXML.value);
	   
		   var xmlRoot = xmlDoc.documentElement;           
		   var Actions = xmlRoot.getElementsByTagName("Actions").item(0);
		   for ( var i = 0;i < Actions.childNodes.length;i++ ) {
			   Action = Actions.childNodes.item(i);
			   aId = Action.getElementsByTagName("BaseProperties").item(0).getAttribute("id"); 
               fromStep = Action.getElementsByTagName("BaseProperties").item(0).getAttribute("from");
               toStep = Action.getElementsByTagName("BaseProperties").item(0).getAttribute("to");
               if(fromStep == MOVEOBJ.id || toStep == MOVEOBJ.id){                  
                  document.getElementById(aId).style.zIndex = CONST_LAY_TOPPEST;
				  Action.getElementsByTagName("VMLProperties").item(0).setAttribute("zIndex",CONST_LAY_TOPPEST);
                  aType = Action.getElementsByTagName("BaseProperties").item(0).getAttribute("actionType");					  
				  document.getElementById(aId).points.value = getActionPoints(aType,document.getElementById(fromStep),document.getElementById(toStep));				  

               }

           }

		   AUTODRAW = false;FlowXML.value = xmlRoot.xml;
		}
//	}	

    event.returnValue = false;    
    return false;
}

function dropIt() {
	if(MOVEFLAG) {
		MOVEFLAG = false;

		var FlowXML = document.all.FlowXML;
		if(FlowXML.value!='' && MOVEOBJ!=null){
			   var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
			   xmlDoc.async = false;
			   xmlDoc.loadXML(FlowXML.value);
		   
			   var xmlRoot = xmlDoc.documentElement;
			   var Steps = xmlRoot.getElementsByTagName("Steps").item(0);
			   for ( var i = 0;i < Steps.childNodes.length;i++ ) {
				   Step = Steps.childNodes.item(i);
				   if(MOVEOBJ.id == Step.getElementsByTagName("BaseProperties").item(0).getAttribute("id")){
					  Step.getElementsByTagName("VMLProperties").item(0).setAttribute("x",MOVEOBJ.style.left);
					  Step.getElementsByTagName("VMLProperties").item(0).setAttribute("y",MOVEOBJ.style.top);
					  Step.getElementsByTagName("VMLProperties").item(0).setAttribute("zIndex",CONST_LAY_TOPPEST);
				   }
			   }		   

			   AUTODRAW = false;FlowXML.value = xmlRoot.xml;AUTODRAW = true;
		}
	}
	
	MOVEOBJ = null;	
    return true;
}


