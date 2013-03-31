function HarshMap(){
	this.names=new Array();
	this.values=new Array();
	this.put=function(pname,pvalue){
		if(pname==null)
			return;
		for(var i=0;i<this.names.length;i++){
			if(this.names[i]==pname){
				this.values[i]=pvalue;
				return;
			}
		}
		this.names[this.names.length]=pname;
		this.values[this.values.length]=pvalue;
	};

	this.get=function(pname){
		for(var i=0;i<this.names.length;i++){
			if(this.names[i]==pname)
				return this.values[i];
		}
		return null;
	}
	this.containsKey=function(pname){
		for(var i=0;i<this.names.length;i++){
			if(this.names[i]==pname)
				return true;
		}
		return false;
	}
}

function getOuterForm(o){
	if("FORM"==o.tagName){
		return o;
	}
	else if(!o.parentNode){
		return null;
	}
	return getOuterForm(o.parentNode);
}

function __d9Cancel(){
	var d9;
	if(arguments&& arguments.length==1)
		d9=document.getElementById(arguments[0]);
	else
		d9=__getOuterD9(event.srcElement);
	d9.style.display='none';
	d9.__d9Display='n';
	setITitle(document.getElementById(d9.descId),"点击显示查询条件");
}

function __d9Show(d9Id){
	event.cancelBubble=true;
	var d9=document.getElementById(d9Id);
	var o=document.getElementById(d9.descId);

	__initD9(d9,o);
	if('y'!=d9.__d9Display){
		d9.style.display='';
		d9.__d9Display='y';
		setITitle(o,"点击隐藏查询条件");
	}
	else{
		d9.style.display='none';
		d9.__d9Display='n';
		setITitle(o,"点击显示查询条件");
	}
}


function __initD9(d9,vSrcElement){

	if('y'!=d9.initDone){
		var pForm=getOuterForm(d9);
		pForm.appendChild(d9);
		d9.style.zIndex="-9";
		d9.style.display="";
		d9.style.width="auto";
		d9.style.height="auto";
		d9.style.width=d9["preferedWidth"]=d9.offsetWidth;
		d9.style.height=d9.offsetHeight;
		d9.style.display='none';
		d9.style.zIndex="9";
		d9.initDone='y';
	}

	var a=_d9getP(vSrcElement);

	d9.style.left=a[0]+vSrcElement.offsetWidth-d9["preferedWidth"];
	d9.style.top=a[1]+vSrcElement.offsetHeight;
}

function __getTrueSrcElement(o){
	if("SPAN"==o.tagName && "d9_sel_desc"==o.className){
		return o;
	}
	else if(!o.parentNode){
		return null;
	}
	return __getTrueSrcElement(o.parentNode);
}

function __getOuterD9(o){
	if("DIV"==o.tagName && o.className && o.className=='d9_pop'){
		return o;
	}
	else if(!o.parentNode){
		return null;
	}
	return __getOuterD9(o.parentNode);
}

function _d9getP(o){
	if(o.offsetParent){
		var a2=_d9getP(o.offsetParent);
		return [o.offsetLeft+a2[0],o.offsetTop+a2[1]];
	}
	else{
		if(o.offsetLeft && o.offsetTop)
			return [o.offsetLeft,o.offsetTop];
		else
			return [0,0];
	}
}

/* for ctrl */

var __ctrlMapD9=new HarshMap();

/* for d9 ctrl click*/
function d9tdov(obj){
	if("-1"==obj.dvalue)
		return;

	if(obj.innerText.length>0){
		obj.className="d9_pop_td_sel";
		var ts=obj.getElementsByTagName("SPAN");
		if(ts && ts.length>0)
			ts=ts[0];
		else
			ts=null;
		(ts==null ? obj :ts).style.textDecoration="underline";
		if(ts!=null)
			obj.style.textDecoration="none";
	}
}

function d9tdou(obj){
	if(!obj.sel || obj.sel!="true"){
		obj.className="d9_pop_td";
		var ts=obj.getElementsByTagName("SPAN");
		if(ts && ts.length>0)
			ts=ts[0];
		else
			ts=null;
		(ts==null ? obj :ts).style.textDecoration="";
	}
}

function d9clk(obj,qry_id,qry_name){
	var vname=obj.vname;
	var vdescname=obj.vdescname;
	var oldCtrl=__ctrlMapD9.get(vname);
	if(obj!=oldCtrl){
		obj.sel="true";
		d9tdov(obj);
		__ctrlMapD9.put(vname,obj);
		if(oldCtrl!=null){
			oldCtrl.sel="false";
			d9tdou(oldCtrl);
		}
	}
	var strFormName=null;
	if(obj.formName){
		strFormName=obj.formName;
	}
	else{
		strFormName=obj.formName=getOuterForm(obj).name;
	}
	//alert(strFormName+"."+vdescname);

	eval(strFormName+"."+vname).value=obj.vvalue;
	var span_vdescname = vdescname.substring(5);
	//alert(span_vdescname);
	if(document.getElementById(span_vdescname)){
		eval(document.getElementById(span_vdescname)).innerHTML=obj.vvaluedesc;
	}
	eval(strFormName+"."+vdescname).value=obj.vvaluedesc;
}

/* for calendar */

function d9calover(){
	var obj=event.srcElement;
	if(0==obj["flag"])
		obj.className="d9_pop_td_sel";
}

function d9calout(){
	var obj=event.srcElement;
	if(0==obj["flag"])
		obj.className="d9_pop_td";
}

function d9calclk(){
	event.cancelBubble=true;
	var obj=event.srcElement;
	if(2==obj["flag"])
		return;

	var tBody=null;
	var objId= (obj.tagName=='IMG' ? obj.id : (tBody=obj.parentNode.parentNode).id);
	var suffix=objId.substring(objId.lastIndexOf("_")+1);
	//alert('objId='+objId);
	var ctrl=document.getElementById(objId.substring(0,objId.lastIndexOf("_")));
	var monthOnly= ctrl.monthOnly;
	if(!ctrl["originalDate"])
		ctrl["originalDate"]=ctrl.value;

	var y=parseInt(ctrl.value.substring(0,4),10);
	var m=parseInt(ctrl.value.substring(4,6),10)-1;// m : java style
	var d=parseInt(ctrl.value.substring(6),10);
	var changeType=null;


	if(obj.tagName=='IMG'){
		if(obj.src.indexOf("lower")!=-1)
			return;
		var inc=(suffix.indexOf("inc")!=-1);
		if(suffix.indexOf("date")==0){
			// for date inc,desc
			var td=__ymd2Date(ctrl["originalDate"]+(monthOnly ? "01":""));
			if(monthOnly){
				td.setMonth(td.getMonth()+(inc?1:-1));
				ctrl.value=(""+__date2Ymd8(td)).substring(0,6);
			}
			else{
				td.setDate(td.getDate()+(inc?1:-1));
				ctrl.value=""+__date2Ymd8(td);
			}
			getOuterForm(ctrl).submit();
			return;
		}
		else{
			changeType='y';
			y+=(inc?1:-1);
		}
	}
	else{
		var s=obj.innerText;
		if(suffix.indexOf("month")==0){
			changeType='m';
			m=parseInt(s.substring(0,s.length-1),10)-1;
		}
		else{
			changeType='d';
			d=parseInt(s,10);
		}
	}

	if(m<0){
		y--;m=11;
	}
	else if(m>11){
		y++;m=0;
	}

	if(monthOnly){
		ctrl.value=""+(y*100+m+1);

		var span_vdescname = ctrl.name.substring(5);
		//alert(span_vdescname);
		if(document.getElementById(span_vdescname)){
			eval(document.getElementById(span_vdescname)).innerHTML=ctrl.value;
		}
	}
	else{
		var maxDays=getDaysOfMonth(y,m);
		if(d>maxDays)
			d=maxDays;

		ctrl.value=""+(y*10000+(m+1)*100+d);

		var span_vdescname = ctrl.name.substring(5);
		//alert(span_vdescname);
		if(document.getElementById(span_vdescname)){
			eval(document.getElementById(span_vdescname)).innerHTML=ctrl.value;
		}
	}

	if(ctrl.value>ctrl.max)
		ctrl.value=ctrl.max;
	else if(ctrl.value<ctrl.min)
		ctrl.value=ctrl.min;

	if('y'==changeType){
		__d9CalRedrawYear(ctrl.id);
		__d9CalRedrawMonth(ctrl.id);
		__d9CalRedrawDate(ctrl.id);
	}
	else if('m'==changeType){
		__d9calclkTd(tBody,obj);
		__d9CalRedrawDate(ctrl.id);
	}
	else if('d'==changeType){
		__d9calclkTd(tBody,obj);
	}
}


function __ymd2Date(ymd){
	return new Date(parseInt(ymd.substring(0,4),10),parseInt(ymd.substring(4,6),10)-1,parseInt(ymd.substring(6),10),0,0,0);
}
function __date2Ymd8(vDate){
	return vDate.getYear()*10000+(vDate.getMonth()+1)*100+vDate.getDate();
}
function __initCalendar(ctrlId){
	__d9CalRedrawYear(ctrlId);
	__d9CalRedrawMonth(ctrlId);
	__d9CalRedrawDate(ctrlId);
}

__d9CalendarStyles=["d9_pop_td","d9_pop_td_sel","d9_pop_td_disable"];
// td flag : 0,normal;1,selected;2,disabled
function __d9calclkTd(tBody,clkTd){
	var oldTd=tBody["selTd"];
	if(oldTd){
		oldTd.className="d9_pop_td";
		oldTd["flag"]=0;
	}
	clkTd.className="d9_pop_td_sel";
	clkTd["flag"]=1;
	tBody["selTd"]=clkTd;
}

function __d9CalRedrawYear(ctrlId){
	//update date
	//
	var ctrl=document.getElementById(ctrlId);
	var yDecArrow=document.getElementById(ctrlId+"_yeardecr");
	var yIncArrow=document.getElementById(ctrlId+"_yearincr");
	//2007
	var selY=ctrl.value.substring(0,4);
	//2003
	var minY=2006;//ctrl.min.substring(0,4);
	//2007
	var maxY=2020;//ctrl.max.substring(0,4);
	if(selY<=minY){
		yDecArrow.style.cursor="normal";
		yDecArrow.src="../images/common/x/left_arrow_lower.gif";
	}
	else{
		yDecArrow.style.cursor="hand";
		yDecArrow.src="../images/common/x/left_arrow.gif";
	}
	if(selY>=maxY){
		yIncArrow.style.cursor="normal";
		yIncArrow.src="../images/common/x/right_arrow_lower.gif";
	}
	else{
		yIncArrow.style.cursor="hand";
		yIncArrow.src="../images/common/x/right_arrow.gif";
	}
	if(document.all){
		document.getElementById(ctrlId+"_year").innerText=selY;
	}else{
		document.getElementById(ctrlId+"_year").textContent=selY;
	}

}

function __d9CalRedrawMonth(ctrlId){
	//update date
	var ctrl=document.getElementById(ctrlId);
	var selY=parseInt(ctrl.value.substring(0,4),10);
	var selM=parseInt(ctrl.value.substring(4,6),10);// non java style
	var min="20060101";
	var minY=min.substring(0,4);
	var max="20200101";
	var maxY=max.substring(0,4);
	var minM;
	if(selY==minY){
		minM = parseInt(min.substring(4,6),10);
	}else{
		minM=0;
	}
	var maxM;
	if(selY==maxY){
		maxM=parseInt(ctrl.max.substring(4,6),10);
	}else{
		maxM=99;
	}
	var monthNdx=1;

	var tBody=document.getElementById(ctrlId+"_month");
	rows=tBody.rows;
	for(var i=0;i<rows.length;i++){
		var cells=rows[i].cells;
		for(var j=0;j<cells.length;j++,monthNdx++){
			if(monthNdx<minM || monthNdx>maxM){
				if(2!=cells[j]["flag"]){
					cells[j]["flag"]=2;
					cells[j].className=__d9CalendarStyles[2];
				}
			}
			else{
				var newFlag= (selM==monthNdx? 1:0);
				if(newFlag!=cells[j]["flag"]){
					cells[j]["flag"]=newFlag;
					cells[j].className=__d9CalendarStyles[newFlag];
				}
				if(selM==monthNdx)
					tBody["selTd"]=cells[j];
			}
		}
	}
}

function __d9CalRedrawDate(ctrlId){
	//update date
	var ctrl=document.getElementById(ctrlId);

	if(ctrl.monthOnly)
		return;
	var selYm=ctrl.value.substring(0,6);
	var selY=parseInt(selYm.substring(0,4),10);
	var selM=parseInt(selYm.substring(4,6),10)-1;// java style
	var selDate=parseInt(ctrl.value.substring(6),10);
	var min="20060101";
	var minYm=min.substring(0,6);
	var max="20200101";
	var maxYm=max.substring(0,6);
	var minDate;
	if(selYm==minYm){
		minDate = parseInt(min.substring(6),10);
	}else{
		minDate=0;
	}
	var maxDate;
	if(selYm==maxYm){
		maxDate=parseInt(max.substring(6),10);
	}else{
		maxDate=99;
	}
	var daysOfMonth=getDaysOfMonth(selY,selM);
	var dateNdx=new Date(selY,selM,1).getDay();// 0:Sunday;1:Monday
	//dateNdx= (0==dateNdx? -5 :2-dateNdx);
	if(0==dateNdx){
		dateNdx=-5;
	}else{
		dateNdx=2-dateNdx;
	}

	var tBody=document.getElementById(ctrlId+"_date");
	rows=tBody.rows;
	for(var i=0;i<rows.length;i++){
		var cells=rows[i].cells;
		for(var j=0;j<cells.length;j++,dateNdx++){
			if(dateNdx<1 || dateNdx>daysOfMonth){
				if(2!=cells[j]["flag"]){
					cells[j]["flag"]=2;
					cells[j].className=__d9CalendarStyles[2];
				}
				cells[j].innerHTML="&nbsp;";
			}
			else if(dateNdx<minDate || dateNdx>maxDate){
				if(2!=cells[j]["flag"]){
					cells[j]["flag"]=2;
					cells[j].className=__d9CalendarStyles[2];
				}
				if(document.all){
					cells[j].innerText=dateNdx;
				}else{
					cells[j].textContent=dateNdx;
				}
				//cells[j].innerText=dateNdx;
			}
			else{
				var newFlag= (selDate==dateNdx? 1:0);
				if(newFlag!=cells[j]["flag"]){
					cells[j]["flag"]=newFlag;
					cells[j].className=__d9CalendarStyles[newFlag];
				}
				if(selDate==dateNdx)
					tBody["selTd"]=cells[j];

				if(document.all){
					cells[j].innerText=dateNdx;
				}else{
					cells[j].textContent=dateNdx;
				}
				//cells[j].innerText=dateNdx;
			}
		}
	}
}



var DAY_OF_MONTH=new Array(31,28,31,30,31,30,31,31,30,31,30,31);
// param m : java style
function getDaysOfMonth(y,m){
	return DAY_OF_MONTH[m]+((m==1 && y%4==0 && !(y%100==0 && y%400 !=0)) ? 1:0);
}

function customizationView(id){
  var customizationDiv=document.getElementById("customizationDiv");
  var url = "system.ext.measure.cust.do?measure_id="+id;
  linkPageByContainerId(url,"customizationSpan");
  customizationDiv.style.display = '';
}

function customizationClose(){
	window.location.reload();
  //var customizationDiv=document.getElementById("customizationDiv");
  //customizationDiv.style.display = 'none';
}
