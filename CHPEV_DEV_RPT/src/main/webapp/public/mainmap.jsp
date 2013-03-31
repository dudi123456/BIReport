<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<HTML>
<HEAD>
<TITLE>FusionMaps JS DrillDown Demo</TITLE>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<LINK href="FusionMaps JS DrillDown Demo.files/Style.css" type=text/css
	rel=stylesheet>

<LINK href="FusionMaps JS DrillDown Demo.files/dimming.css"
	type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>

//PowerMaps JavaScript DrillDown Class
if(typeof infosoftglobal == "undefined") var infosoftglobal = new Object();
if(typeof infosoftglobal.PowerMapUtil == "undefined") infosoftglobal.PowerMapUtil = new Object();
infosoftglobal.powerMapsUSDrill = function(){
	this.mapPath ="../../MapSWF/";
}

infosoftglobal.powerMapsUSDrill.prototype = {
	instantiateMap : function(mapDiv){	
		this.containerHTML = "<table cellpadding='0' cellspacing='0' border='0'><tr><td>" +
						 	"<div id='pmIdUSDrillRoot' align='center' style='overflow:hidden;'></div></td></tr></table>" ;
								
		this.containerWindowHTML ="<div style='width:960px;height:440px;overflow:hidden;background:#FFFFFF;'><table cellpadding='0' cellspacing='0' border='0'>" +
								"<tr>" +
								   "<td align='left' valign='top'><div id='fcId1' style='width:200;height:200;overflow:hidden;'>col3d</div></td>" +
								   "<td rowspan='3' align='center' valign='top' >" +
									  "<div id='pmIdUSDrillDown' style='" +
									  ((isFireFox || isNetscape )? "width:0px;height:0px;" :"width:758;height:430px;") +
									  "overflow:auto;' >Please click on a county to drilldown</div>" +
								   "</td>" +
								    
								 "</tr>" +
								 "<tr>" +
									 "<td align='right'  valign='top'><div id='fcId2' style='width:200;height:220;overflow:hidden;'Pie3d/div></td>" +
								 "</tr>" +
								
							 "</table></div>";
		
		this.mapContainer = document.getElementById(mapDiv);
		this.mapContainer.innerHTML = this.containerHTML;
		this.mapWindowContainer = document.getElementById("drilledDownWindow");
		this.mapWindowContainer.innerHTML += this.containerWindowHTML;
		

		//Define map list
		this.defineMapList();
	
		this.map = this.loadMap("US");

		//
		
	
	},
	onMapLoad : function(){
		this.getReferenceToMap();

		var XML = this.buildUSMapXML(this.mapIndex);

		this.updateMap(this.map,XML);
	},

	getUSDataXML :function(id){
	
		var v,value, eId, title, u, b, x, toolTip, jsLink;
		var XML = "";

		for (i=1; i<this.entities.length; i++){
			v = (Math.round(Math.random()*40)+60);
			value ="value='"+ v +"' ";

			title =	this.entities[i].lName;
			eId = "id='"+this.entities[i].id+"' ";
			u = Math.round(Math.random()*10);
			b = Math.round(Math.random()*(90-u));
			s = 100-(u+b);
			toolTip =" toolText='" + title.replace(/\'/g,"&apos;") + " (" + v +"% ) \nBusiness :" + b + "% \nService :" + s+"% \nUnemployed :" + u +"% ' ";
			jsLink ="link='javascript:drillDown(&quot;"+this.entities[i].id+"&quot;,"+b+","+s+","+u+")' ";

			XML += "   <entity "+eId+value+toolTip+jsLink+"/>\n";
			
			
		}
	
		XML = "<data>\n" + XML + "</data>\n";
		return  XML;
		
		
	},
	
	buildUSMapXML : function(id){
		
		var data = this.getUSDataXML(id);

		   var XMLColorRange ="\n\n<colorRange>\n" +
		  	"<color minValue='90' maxValue='101' displayValue='90% - 100%' color='8F8BD7' />\n" + 
	  	  	"<color minValue='80' maxValue='90' displayValue='80% - 90%' color='9EBDEC' />\n" + 
			"<color minValue='70' maxValue='80' displayValue='70% - 80%' color='D4BAE7' />\n" + 
			"<color minValue='60' maxValue='70' displayValue='60% - 70%' color='EDCFB1' />\n" +
		  "</colorRange>\n\n"; 
		  
		var XML = "<map animation='0' ";
		XML += "showBevel='0' useHoverColor='1' fillAlpha='70' " ; 

		XML += "canvasBorderColor='FFFFFF' borderColor='FFFFFF' hoverColor='FFFFFF' ";
		
		XML += "showLegend='1' legendPosition='RIGHT' legendBorderAlpha='0' legendAllowDrag='1' legendShadow='0' ";
		XML += "connectorColor='000000' fillAlpha='70' toolTipBgColor='DEEBED' ";
		XML += "hoverColor='DEEBED' ";
		XML += ">\n" + XMLColorRange + data +"</map>";
		
		
		return XML;
	},
	
	loadMap : function(id){
		if(id!=""){
			var bar = this.getMapBar(id);	
			//This method loads the map
			//Store the index of new map
			var index = this.mapIndex = this.getMapIndex(id);
			var map = new PowerMap(this.mapPath + this.mapList[index].swf, "PM"+this.mapIndex, this.mapList[index].width, this.mapList[index].height, "0", "1");      
			map.setDataXML("<map animation='0' showLabels='0' borderAlpha='0' fillColor='FFFFFF' fillAlpha='0' usehoverColor='0' showToolTip='0' bgAlpha='0' CanvasBorderAlpha='0'  showShadow='0' showBevel='0' ></map>");
			map.render(bar.mapBar);
			//Update mapNameDiv with the new map's name
			var dv = document.getElementById(bar.titleBar);
			dv.innerHTML = " Map of " + this.mapList[index].title +(id.toUpperCase()=="US" ? " [Please click on a county to drill-down to that county]" :" [Please click on an entity to show detailed data in charts]" );
			
			return id ;
		}
	},

	updateMap : function(id,XML){
			
			this.mapObj.setDataXML(XML);
	},
	
	
	
	drillDown : function(arg){
		id = (typeof(arg)=="undefined" || arg[0]==null) ? "US" : arg[0] ;
		var data= new Array();
		for (var i=1;i<arg.length;i++)
			data.push(arg[i]);
		if (id.toUpperCase()=="DC")	return ;
		if(isNaN(parseInt(id))){  // counties will always give alphabical id while districts give numerical id
			
			if(interval1==null)
			{
				displayFloatingDiv('drilledDownWindow',126,10,975,430);
				this.map = this.loadMap(id);
				this.loadCharts();
				setWindow(95,17,960,455);
				zoomWindow();
				
			}
			
			
		}
		else{
			this.showCharts(data);
			var eName = this.getEntityName(id);
			var title = document.getElementById("pmDrillDownTitle").innerHTML;
			var len = title.indexOf("[")>0 ? title.indexOf("[") : title.length-1;
			title= title.substring(0,len)+" [ Showing data of " + eName + "]";
			document.getElementById("pmDrillDownTitle").innerHTML = title ;
		}
			

	},
	getEntityName : function(id){
		var str="";
		for (i=1; i<this.entities.length; i++){
			if(this.entities[i].id==id)
				str = this.entities[i].lName;
		}
		return str;
	},
	
	getReferenceToMap : function(){
		//This method is invoked when the map has loaded and rendered. So, we can safely
		//get a reference to the map object.
		
		this.mapObj = infosoftglobal.PowerMapUtil.getMapObject("PM"+this.mapIndex);		

		//If we cannot get a reference to map object, it means	
		//Also, we can now get the map's entities and store it.	
		this.entities = this.mapObj.getEntityList();
	},

	getMapBar : function(id){
		var mapBar = "pmIdUSDrillDown";
		var titleBar = "pmDrillDownTitle";
		if(id.toLowerCase()=="us" || id==null) {
			mapBar ="pmIdUSDrillRoot"; 
			titleBar ="pmRootTitle" ;
		}
	
		return { mapBar : mapBar , titleBar : titleBar };
	
	},
	
	getMapIndex : function(id){
		var mapIndex = 0;
		for(var i in this.mapList){
			if(id.toLowerCase() == this.mapList[i].id.toLowerCase()){
				mapIndex = i;
				break;
			}
				
		}
		return mapIndex;
	},
	

	loadCharts : function(){
	
		var colChart1 = new FusionCharts("Charts/Column3D.swf?ChartNoDataText=Click on a district" , "ChartId1" , "200" , "200" , "0", "1" );
		colChart1.setDataXML("<chart></chart>");
		colChart1.render("fcId1");
	
		var colChart2 = new FusionCharts("Charts/Pie3D.swf?ChartNoDataText=Click on a district" , "ChartId2" , "200" , "200" , "0", "1" );
		colChart2.setDataXML("<chart></chart>");
		colChart2.render("fcId2");
	
	},


	showCharts : function(data){
		var XML ="";
		
//		XML = this.buildChartXML("unemployed","pie");
//		this.setChartXML("ChartId3",XML);

		XML = this.buildChartXML("business","");
		this.setChartXML("ChartId1",XML);
		
		XML = this.buildChartXML("service","pie");
			this.setChartXML("ChartId2",XML);
		

	},
	
	sortIt : function(a,b){		return(b-a)	},

	getData : function(cat){
		var labels= { business :"", service : "", unemployed :""};
		var config = { business :" caption='Major Businesses' yAxisName='&nbsp;&nbsp;Employees in %' ", service : " caption='Major Service Areas' yAxisName='  Employees in %' ", unemployed :" caption='Unemployed : Age Groups' "}
		
		labels["business"]=new Array( "Media","Telecom", "Finance", "Software", "Power");
		labels["service"]=new Array("Security","Admin","Education","Postal","Legal");
		labels["unemployed"]=new Array("20-30","30-40","40-50","50-60"," Above 60");
		
		var data = new Array(labels[cat].length);
		var b=37, c=0, i=0;
		for(i=0;i<data.length-1;i++){
			data[i]=(Math.round(Math.random()*(b-5))+5);
			b -=10;
			c += data[i];
		}

		
		data[i]=Math.round(Math.random()*5)+87-c;
		c += data[i];
		
		if(cat.toLowerCase()=="unemployed"){
			for(i in data)
				data[i] +=(100-c)/data.length;
		}

		
		return { value:  data.sort(this.sortIt), labels : labels[cat] , config : config[cat] };
	},
	


	buildChartXML : function(cat, param){
		var data = this.getData(cat);
	
		var XML=""; 
		XML +="<chart "+data.config +" numberSuffix='%' " +
			" animation='1' " +
			"showValues='0' showLabels='1' rotateLabels='1' showYAxisValues='0'  " +
			"" +
			(param.toLowerCase()=="pie" ? "  startingAngle='355'  " : " canvasBaseDepth='9' canvasBaseColor='cccccc' canvasBgAlpha='60' canvasBgDepth='0' canvasBgColor='FFFFFF'  " ) +
			" >";
		for(var i=0;i<data.labels.length;i++){
			var label = data.labels[i];
			XML +="<set value='"+data.value[i]+
				"' label='"+label+
				"' toolText = '"+ data.labels[i] +"," +data.value[i]+"%' " + 
				(param.toLowerCase()=="pie" ? (i==data.value.length-1 ? "isSliced='1' ":"") : "" )+
				"/>";
		}
	
		XML+="</chart>";
		return XML;
		
	},
	
	setChartXML : function(chartId,XML){
		var updateChart = getChartFromId(chartId);
		updateChart.setDataXML(XML);
	},
	
	defineMapList : function(){
		this.mapList = new Array();
		this.mapList.push({id:"US" ,title:"USA (Counties)",swf:"FCMap_USA.swf",width:950,height:400});
		this.mapList.push({id:"AL" ,title:"Alabama",swf:"FCMap_Alabama.swf",width:650,height:430});
		this.mapList.push({id:"AK" ,title:"Alaska",swf:"FCMap_Alaska.swf",width:650,height:430});
		this.mapList.push({id:"AZ" ,title:"Arizona",swf:"FCMap_Arizona.swf",width:650,height:430});
		this.mapList.push({id:"AR" ,title:"Arkansas",swf:"FCMap_Arkansas.swf",width:650,height:430});
		this.mapList.push({id:"CA" ,title:"California",swf:"FCMap_California.swf",width:650,height:430});
		this.mapList.push({id:"CO" ,title:"Colorado",swf:"FCMap_Colorado.swf",width:650,height:430});
		this.mapList.push({id:"CT" ,title:"Connecticut",swf:"FCMap_Connecticut.swf",width:650,height:430});
		this.mapList.push({id:"DE" ,title:"Delaware",swf:"FCMap_Delaware.swf",width:650,height:430});
		this.mapList.push({id:"FL" ,title:"Florida",swf:"FCMap_Florida.swf",width:650,height:430});
		this.mapList.push({id:"GA" ,title:"Georgia",swf:"FCMap_Georgia.swf",width:650,height:430});
		this.mapList.push({id:"HI" ,title:"Hawaii",swf:"FCMap_Hawaii.swf",width:430,height:430});
		this.mapList.push({id:"ID" ,title:"Idaho",swf:"FCMap_Idaho.swf",width:650,height:430});
		this.mapList.push({id:"IL" ,title:"Illinois",swf:"FCMap_Illinois.swf",width:650,height:430});
		this.mapList.push({id:"IN" ,title:"Indiana",swf:"FCMap_Indiana.swf",width:650,height:430});
		this.mapList.push({id:"IA" ,title:"Iowa",swf:"FCMap_Iowa.swf",width:650,height:430});
		this.mapList.push({id:"KS" ,title:"Kansas",swf:"FCMap_Kansas.swf",width:650,height:430});
		this.mapList.push({id:"KY" ,title:"Kentucky",swf:"FCMap_Kentucky.swf",width:650,height:430});
		this.mapList.push({id:"LA" ,title:"Lousiana",swf:"FCMap_Louisiana.swf",width:650,height:430});
		this.mapList.push({id:"ME" ,title:"Maine",swf:"FCMap_Maine.swf",width:650,height:430});
		this.mapList.push({id:"MD" ,title:"Maryland",swf:"FCMap_Maryland.swf",width:650,height:500});
		this.mapList.push({id:"MA" ,title:"Massachusetts",swf:"FCMap_Massachusetts.swf",width:650,height:450});
		this.mapList.push({id:"MI" ,title:"Michigan",swf:"FCMap_Michigan.swf",width:650,height:430});
		this.mapList.push({id:"MN" ,title:"Minnesota",swf:"FCMap_Minnesota.swf",width:650,height:430});
		this.mapList.push({id:"MS" ,title:"Mississippi",swf:"FCMap_Mississippi.swf",width:650,height:430});
		this.mapList.push({id:"MO" ,title:"Missouri",swf:"FCMap_Missouri.swf",width:650,height:430});
		this.mapList.push({id:"MT" ,title:"Montana",swf:"FCMap_Montana.swf",width:650,height:430});
		this.mapList.push({id:"NE" ,title:"Nebraska",swf:"FCMap_Nebraska.swf",width:650,height:430});
		this.mapList.push({id:"NV" ,title:"Nevada",swf:"FCMap_Nevada.swf",width:650,height:430});
		this.mapList.push({id:"NH" ,title:"New Hampshire",swf:"FCMap_NewHampshire.swf",width:650,height:430});
		this.mapList.push({id:"NJ" ,title:"New Jersey",swf:"FCMap_NewJersey.swf",width:650,height:430});
		this.mapList.push({id:"NM" ,title:"New Mexico",swf:"FCMap_NewMexico.swf",width:650,height:430});
		this.mapList.push({id:"NY" ,title:"New York",swf:"FCMap_NewYork.swf",width:650,height:430});
		this.mapList.push({id:"NC" ,title:"North Carolina",swf:"FCMap_NorthCarolina.swf",width:650,height:430});
		this.mapList.push({id:"ND" ,title:"North Dakota",swf:"FCMap_NorthDakota.swf",width:650,height:430});
		this.mapList.push({id:"OH" ,title:"Ohio",swf:"FCMap_Ohio.swf",width:650,height:430});
		this.mapList.push({id:"OK" ,title:"Oklahoma",swf:"FCMap_Oklahoma.swf",width:650,height:400});
		this.mapList.push({id:"OR" ,title:"Oregon",swf:"FCMap_Oregon.swf",width:650,height:430});
		this.mapList.push({id:"PA" ,title:"Pennsylvania",swf:"FCMap_Pennsylvania.swf",width:650,height:430});
		this.mapList.push({id:"RI" ,title:"Rhode Island",swf:"FCMap_RhodeIsland.swf",width:650,height:430});
		this.mapList.push({id:"SC" ,title:"South Carolina",swf:"FCMap_SouthCarolina.swf",width:650,height:430});
		this.mapList.push({id:"SD" ,title:"South Dakota",swf:"FCMap_SouthDakota.swf",width:650,height:430});
		this.mapList.push({id:"TN" ,title:"Tennessee",swf:"FCMap_Tennessee.swf",width:650,height:430});
		this.mapList.push({id:"TX" ,title:"Texas",swf:"FCMap_Texas.swf",width:700,height:560});
		this.mapList.push({id:"UT" ,title:"Utah",swf:"FCMap_Utah.swf",width:650,height:430});
		this.mapList.push({id:"VT" ,title:"Vermont",swf:"FCMap_Vermont.swf",width:650,height:430});
		this.mapList.push({id:"VA" ,title:"Virginia",swf:"FCMap_Virginia.swf",width:740, height:380});
		this.mapList.push({id:"WA" ,title:"Washington",swf:"FCMap_Washington.swf",width:650,height:430});
		this.mapList.push({id:"WV" ,title:"West Virginia",swf:"FCMap_WestVirginia.swf",width:650,height:430});
		this.mapList.push({id:"WI" ,title:"Wisconsin",swf:"FCMap_Wisconsin.swf",width:650,height:430});
		this.mapList.push({id:"WY" ,title:"Wyoming",swf:"FCMap_Wyoming.swf",width:650,height:430});
		
	
	}


}


/* Aliases for easy usage */
var powerMapsUSDrill = infosoftglobal.powerMapsUSDrill ;


	

	//
	// global variables
	//
	var isMozilla;
	var objDiv = null;
	var originalDivHTML = "";
	var DivID = "drilledDownWindow";
	var over = false;
	var mouseX = 0;
	var mouseY = 0;
	var interval1 = null;

	var mouseDrillX, mouseDrillY;
	var winIX, winIY, winL, winT, winW, winH, winIW, winIH, count;
	//
	// dinamically add a div to 
	// dim all the page
	//
	var setWT = 97, setWL = 17, setWW = 960, setWH = 455;

	function setWindow(t, l, w, h) {
		setWT = t;//97;
		setWL = l;//17;
		setWW = w;//960;
		setWH = h;//455;
	}

	function zoomWindow() {
		mouseDrillX = mouseX;
		mouseDrillY = mouseY;
		winL = parseInt(mouseX);
		winT = parseInt(mouseY);
		winIX = (winL - 15) / 10;
		winIY = (winT - 100) / 10;
		count = 0;
		//winIW =960/10;
		//winIH =460/10;
		winIW = setWW / 10;
		winIH = (setWH) / 10;
		winW = 0;
		winH = 0;
		interval1 = window.setInterval(zoomInterval, 70);

	}

	function zoomInterval() {
		if (count < 9) {
			winL -= winIX;
			winT -= winIY;
			winW += winIW;
			winH += winIH;

			document.getElementById(DivID).style.position = "absolute";
			document.getElementById(DivID).style.left = winL + "px";
			document.getElementById(DivID).style.top = winT + "px";
			document.getElementById(DivID).style.width = winW + "px";
			document.getElementById(DivID).style.height = winH + "px";
			document.getElementById(DivID).style.visibility = "visible";
			document.getElementById(DivID).style.zIndex = "10002";
			document.getElementById("dimmer").style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity="
					+ (count * 6) + ")";
			document.getElementById("dimmer").style.MozOpacity = count / 10;
			document.getElementById("dimmer").style.opacity = count / 10;

		} else {
			window.clearInterval(interval1);
			interval1 = null;
			document.getElementById(DivID).style.position = "absolute";
			document.getElementById(DivID).style.left = setWL + "px";//"15px";
			document.getElementById(DivID).style.top = setWT + "px";//"100px";
			document.getElementById(DivID).style.width = setWW + "px";//"960px";
			document.getElementById(DivID).style.height = setWH + "px";//"455px";

			if (isFireFox || isNetscape) {
				document.getElementById("pmIdUSDrillDown").style.MozOpacity = "1";
				document.getElementById("pmIdUSDrillDown").style.opacity = "1";
				document.getElementById("pmIdUSDrillDown").style.width = 760 + "px";
				document.getElementById("pmIdUSDrillDown").style.height = 433 + "px";

			}

		}
		count++;

	}

	//
	//
	function displayFloatingDiv(divId, dimmerTop, dimmerLeft, dimmerWidth,
			dimmerHeight) {
		DivID = divId;

		document.getElementById('dimmer').style.position = "absolute";
		document.getElementById('dimmer').style.left = dimmerLeft + "px";// "9px";
		document.getElementById('dimmer').style.top = dimmerTop + "px";//"98px";
		document.getElementById('dimmer').style.width = dimmerWidth + "px";//"975px";
		document.getElementById('dimmer').style.height = dimmerHeight + "px";//"430px";
		document.getElementById('dimmer').style.visibility = "visible";
		document.getElementById('dimmer').style.zIndex = "10000";

		var addHeader;

		if (originalDivHTML == "")
			originalDivHTML = document.getElementById(divId).innerHTML;

		addHeader = '<table style="width:100%;" class="floatingHeader textBoldLight" >'
				+ '<tr>'
				+ '<td style="height:22px;cursor:move;" onDblClick="void(0);" onMouseOver="over=true;" onMouseOut="over=false;" valign="middle"><div id="pmDrillDownTitle" style="height:16px;overflow:hidden;cursor:move;" ></div></td>'
				+ '<td style="width:14px;" align="left" valign="middle">'
				+ '<a href="javascript:closeWindow(\''
				+ divId
				+ '\');void(0);">'
				+ '<img alt="Close" title="Close" src="FW/close.gif" border="0">'
				+ '</a>' + '</td></tr></table>';

		// add to your div an header

		document.getElementById(divId).innerHTML = addHeader + originalDivHTML;

	}

	//

	function zoomOutInterval() {
		if (count > 1) {

			winL += winIX;
			winT += winIY;
			winW -= winIW;
			winH -= winIH;

			document.getElementById(DivID).style.position = "absolute";
			document.getElementById(DivID).style.width = winW + "px";
			document.getElementById(DivID).style.height = winH + "px";
			document.getElementById(DivID).style.left = winL + "px";
			document.getElementById(DivID).style.top = winT + "px";
			document.getElementById("dimmer").style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity="
					+ (count * 6) + ")";

			document.getElementById("dimmer").style.MozOpacity = (count) / 10;
			document.getElementById("dimmer").style.opacity = (count) / 10;
		} else {
			window.clearInterval(interval1);
			interval1 = null;
			document.getElementById(DivID).innerHTML = originalDivHTML;
			document.getElementById(DivID).style.position = "absolute";
			document.getElementById(DivID).style.width = "1px";
			document.getElementById(DivID).style.height = "1px";
			document.getElementById(DivID).style.left = "1000px";
			document.getElementById(DivID).style.top = "1px";
			document.getElementById(DivID).style.visibility = "hidden";

			document.getElementById('dimmer').style.position = "absolute";
			document.getElementById('dimmer').style.width = "1px";
			document.getElementById('dimmer').style.height = "1px";
			document.getElementById('dimmer').style.left = "1000px";
			document.getElementById('dimmer').style.top = "1px";
			document.getElementById('dimmer').style.visibility = "hidden";

			DivID = "";
		}
		count--;

	}

	//
	//
	function closeWindow(divId) {
		if (isFireFox || isNetscape) {
			document.getElementById("pmIdUSDrillDown").style.MozOpacity = "0";
			document.getElementById("dimmer").style.opacity = "0";
			document.getElementById("pmIdUSDrillDown").style.width = 1 + "px";
			document.getElementById("pmIdUSDrillDown").style.height = 1 + "px";
			document.getElementById("pmIdUSDrillDown").style.visibility = "hidden";
		}

		if (interval1 == null) {
			interval1 = window.setInterval(zoomOutInterval, 70);

		}
	}

	//
	//
	//
	function MouseDown(e) {
		if (over) {
			if (isMozilla) {
				objDiv = document.getElementById(DivID);
				X = e.layerX;
				Y = e.layerY;
				return false;
			} else {
				objDiv = document.getElementById(DivID);
				objDiv = objDiv.style;
				X = event.offsetX;
				Y = event.offsetY;
			}
		}
	}

	//
	//
	//
	function MouseMove(e) {

		if (isMozilla) {
			mouseX = e.pageX;
			mouseY = e.pageY;
		} else {
			mouseX = event.clientX;
			mouseY = event.clientY;
		}

		if (objDiv) {
			if (isMozilla) {
				objDiv.style.top = (e.pageY - Y) + 'px';
				objDiv.style.left = (e.pageX - X) + 'px';
				return false;
			} else {
				objDiv.pixelLeft = event.clientX - X + document.body.scrollLeft;
				objDiv.pixelTop = event.clientY - Y + document.body.scrollTop;
				return false;
			}
		}
	}

	//
	//
	//
	function MouseUp() {
		objDiv = null;
	}

	//
	//
	//
	function init() {
		// check browser
		isMozilla = (document.all) ? 0 : 1;

		if (isMozilla) {
			document.captureEvents(Event.MOUSEDOWN | Event.MOUSEMOVE
					| Event.MOUSEUP);
		}

		document.onmousedown = MouseDown;
		document.onmousemove = MouseMove;
		document.onmouseup = MouseUp;

		// add the div used to dim the main map

	}

	// call init
	init();



	var isNetscape = (navigator.userAgent.search(new RegExp("netscape", "ig")) >= 0);
	var isFireFox = (navigator.userAgent.search(new RegExp("firefox", "ig")) >= 0);

	function FC_Rendered(DOMId) {
		if (DOMId.substr(0, 2).toUpperCase() == "PM") {
			USDrill.onMapLoad();
		}
	}

	function drillDown() {
		USDrill.drillDown(arguments);
	}

	
</SCRIPT>

<META content="MSHTML 6.00.2900.3603" name=GENERATOR>
</HEAD>
<BODY>
<DIV class=fWindow id=drilledDownWindow></DIV>
<DIV class=dimmer id=dimmer></DIV>
<TABLE cellSpacing=0 cellPadding=0 width=970 border=0>
	<TBODY>
		<TR>
			<TD>
			<TABLE cellSpacing=0 cellPadding=3 width=970 border=0>
				<TBODY>


					<TR>
						<TD>
						<TABLE class=borderDarkBlue cellSpacing=0 cellPadding=0 width=975
							border=0>
							<TBODY>
								<TR>
									<TD vAlign=center align=middle>
									<TABLE class=BorderWhite style="TEXT-ALIGN: center"
										cellSpacing=0 cellPadding=0 width=973 border=0>
										<TBODY>
											<TR>
												<TD class=blueTr height=28><SPAN class=textBoldLight
													id=pmRootTitle>Map of USA [Please click on a county
												to drill down to that county]</SPAN></TD>
											</TR>
											<TR>
												<TD vAlign=top align=middle>
												<DIV id=mapdiv style="OVERFLOW: hidden">FusionMaps US
												DrillDown Demo</DIV>

												<TABLE border="1" width="100%">
													<TR>
														<TD>43143</TD>
														<TD>4321</TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
													</TR>
													<TR>
														<TD>4321</TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
													</TR>
													<TR>
														<TD>432</TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD>432</TD>
														<TD></TD>
														<TD></TD>
													</TR>
													<TR>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD>4321</TD>
														<TD></TD>
													</TR>
													<TR>
														<TD>4321</TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
														<TD></TD>
													</TR>
												</TABLE>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
									</TD>
								</TR>
							</TBODY>
						</TABLE>
						</TD>
					</TR>


				</TBODY>
			</TABLE>
</BODY>
</HTML>
