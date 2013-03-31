function byteLength(str){
  var tep = str;
  var reg = new RegExp("[^\x00-\xff]","g");
  return tep.replace(reg,"aa").length; 
}

function truncateStr(str,sizeLimit){
  var tep = str;
  var reg = new RegExp("[^\x00-\xff]","g");
  var	charStr = "";
  var char = "";
  var counter = 0; 
  for (var i = 0;i < tep.length;i++) {
  	char = ""+tep.charAt(i);
  	if (char.match(reg)) { 
  		counter += 2;
  	} else {
  		counter++;
  	}
  	if (counter > sizeLimit) {
  	  return charStr;	
		} else {
			charStr += char;
  	}
  }
  return charStr;
  //alert(str3);
}

function showsubitem(titleid,itemid,id,name)
{
	 var tepName = name;
   var tepcode = "";
	 var  strLength=20; 
  if (name != null && byteLength(name) >strLength)  {
			tepName = truncateStr(name,strLength) +"..."; 
			tepcode  = "title='"+name+"'";
	} //"titleid + "," + itemid + "
		//var	str="<tr><td  width='10'><a href='#' onClick='showChildren("+titleid + "," + itemid + ")'><img id='button_"+titleid+"_"+itemid+"' src='../biimages/button_close.gif' width='8' height='8' border='0'></a></td><td width='100%'><a "+tepcode+" href='javascript:void(0)' >";
		var	str="<tr><td  width='10'><a href='#' onClick='showChildren("+titleid + "," + itemid + ")'><img id='button_"+titleid+"_"+itemid+"' src='../biimages/button_close.gif' width='8' height='8' border='0'></a></td><td width='100%'>";
		//str += titleid + "," + itemid + ")'>" + outlookbar.itemlist[titleid][itemid].title + "</a></td></tr>";
	str +=  tepName + "</td></tr>";
	str += "<tr><td  width='100%' height='1' colspan='2' background='../biimages/black-dot.gif' ></td></tr>";
	str +="<tr style='display:none'><td colspan='2' id=child_"+titleid+"_"+itemid+"><table>";
	for(var i=0;i<outlookbar.subitemlist[titleid][itemid].length;i++)
		str +=showitem(outlookbar.subitemlist[titleid][itemid][i].key,outlookbar.subitemlist[titleid][itemid][i].title);
	str +="</table></td></tr>";
	//alert("showsubitem:"+str);
	return(str);
}

function showsubitemNoChild(titleid,itemid,id,name)
{
   var tepName = name;
     var tepcode = "";
	 var  strLength=20;
  if (name != null && byteLength(name) >strLength)  {
			tepName = truncateStr(name,strLength) +"...";
			//alert('goes here,'+tepName);			
			tepcode  = "title='"+name+"'";
	} 
	
  //var str = "<tr ><td width='100%' colspan='2' nowarp ><a  "+tepcode+"target=bodyFrame  href='"+id+"'>"+tepName+"</a></td></tr>";
    	
	var	str="<tr><td  width='10'><img src='../biimages/button_open.gif' width='8' height='8'></td><td width='100%'><a "+tepcode+" target=bodyFrame  href='"+id+"'>";
	//str += outlookbar.itemlist[titleid][itemid].title + "</a></td></tr>";
	str += tepName + "</a></td></tr>";
	str += "<tr><td  width='100%' height='1' colspan='2' background='../biimages/black-dot.gif' ></td></tr>";

	//alert("showsubitem:"+str);
	return(str);
}

function showChildren(titleid,itemid)
{
	if(eval("child_"+titleid+"_"+itemid).parentNode.style.display == "") {
		eval("child_"+titleid+"_"+itemid).parentNode.style.display = "none";
		//document.getElementByID(); 
		//eval("document.button_"+titleid+"_"+itemid+'.src='../biimages/button_open.gif'");
		eval('document.getElementById('+"'button_"+titleid+"_"+itemid + "'" +').src = "../biimages/button_close.gif"');
	} else {
		eval("child_"+titleid+"_"+itemid).parentNode.style.display = "";
		//document.getElementById();
		eval('document.getElementById('+"'button_"+titleid+"_"+itemid + "'" +').src = "../biimages/button_open.gif"'); 
	}
}
	
function showitem(id,name)
{   var tepName = name;
     var tepcode = "";
	 var  strLength=19;
     if (name != null && byteLength(name) >strLength)  {
			tepName = truncateStr(name,strLength) +"...";
			tepcode  = "title='"+name+"'";
	} 
	
    var str = "<tr ><td width='100%' colspan='2' nowarp ><img src='../biimages/button_opened.gif' width='6' height='6' hspace='3'><a  "+tepcode+"target=bodyFrame  href='"+id+"'>"+tepName+"</a></td></tr>";
	//alert(str);
	return (str);
}

function switchoutlookBar(number)
{
	var i = outlookbar.opentitle;
	outlookbar.opentitle=number;
	var id1,id2,id1b,id2b
	if (number!=i && outlooksmoothstat==0){
	if (number!=-1)
		{
			if (i==-1)
				{
				id2="blankdiv";
				id2b="blankdiv";}
			else{
				id2="outlookdiv"+i;
				id2b="outlookdivin"+i;
				document.all("outlooktitle"+i).style.border="1px none ";
				document.all("outlooktitle"+i).style.textalign="center";
				}
			id1="outlookdiv"+number
			id1b="outlookdivin"+number
			document.all("outlooktitle"+number).style.border="1px none white";
			document.all("outlooktitle"+number).style.textalign="center";
			smoothout(id1,id2,id1b,id2b,0);
		}
	else
		{
			document.all("blankdiv").style.display="";
			document.all("blankdiv").sryle.height="100%";
			document.all("outlookdiv"+i).style.display="none";
			document.all("outlookdiv"+i).style.height="0%";
			document.all("outlooktitle"+i).style.border="1px none navy";
			document.all("outlooktitle"+i).style.textalign="center";
		}
	}
			
}

function smoothout(id1,id2,id1b,id2b,stat)
{
	if(stat==0){
		tempinnertext1=document.all(id1b).innerHTML;
		tempinnertext2=document.all(id2b).innerHTML;
		document.all(id1b).innerHTML="";
		document.all(id2b).innerHTML="";
		outlooksmoothstat=1;
		document.all(id1b).style.overflow="hidden";
		document.all(id2b).style.overflow="hidden";
		document.all(id1).style.height="0%";
		document.all(id1).style.display="";
		setTimeout("smoothout('"+id1+"','"+id2+"','"+id1b+"','"+id2b+"',"+outlookbar.inc+")",outlookbar.timedalay);
	}
	else
	{
		stat+=outlookbar.inc;
		if (stat>100)
			stat=100;
		document.all(id1).style.height=stat+"%";
		document.all(id2).style.height=(100-stat)+"%";
		if (stat<100) 
			setTimeout("smoothout('"+id1+"','"+id2+"','"+id1b+"','"+id2b+"',"+stat+")",outlookbar.timedalay);
		else
			{
			document.all(id1b).innerHTML=tempinnertext1;
			document.all(id2b).innerHTML=tempinnertext2;
			outlooksmoothstat=0;
			document.all(id1b).style.overflow="auto";
			document.all(id2).style.display="none";
			}
	}
}

function getOutLine()
{
	outline="<table width=100% border=0 cellpadding=0 cellspacing=0 "+outlookbar.otherclass+">";
	for (i=0;i<(outlookbar.titlelist.length);i++)
	{
		outline+="<tr style=cursor:hand  onclick='switchoutlookBar("+i+")'><td><img src='../biimages/left-ico1.gif' width='32' height='23'></td><td name=outlooktitle"+i+" id=outlooktitle"+i+" ";		
		if (i!=outlookbar.opentitle) 
			outline+=" nowrap style='cursor:hand;background-image: url(../biimages/left-ico.gif);height:23;width:100%;padding-left: 2px;padding-top: 4px;border:0' ";
		else
			outline+=" nowrap style='cursor:hand;background-image: url(../biimages/left-ico.gif);height:23;width:100%;padding-left: 2px;padding-top: 4px;border:0' ";
		outline+=outlookbar.titlelist[i].otherclass;
		outline+=" ><span class=left-rollmenu>";
		outline+=outlookbar.titlelist[i].title+"</span></td><td ><img src='../biimages/left-ico2.gif' width='5' height='23'></td></tr>";
		
		outline+="<tr><td  colspan=3 name=outlookdiv"+i+" valign=top id=outlookdiv"+i+" style='width:100%"
		if (i!=outlookbar.opentitle) 
			outline+=";display:none;height:0%;";
		else
			outline+=";display:;height:100%;";
		outline+="'><div name=outlookdivin"+i+" id=outlookdivin"+i+" style='overflow-y: auto;width:100%;height:100%'><table width=100% border=0>";
		//alert("subitemlist length is:"+outlookbar.subitemlist[i][j].length);
		for (j=0;j<(outlookbar.itemlist[i].length);j++){
			if(outlookbar.subitemlist[i][j].length == 0) {
				//outline+=showitem(outlookbar.itemlist[i][j].key,outlookbar.itemlist[i][j].title);
				//alert("11 2"+outlookbar.itemlist[i][j].key);
				outline+=showsubitemNoChild(i,j,outlookbar.itemlist[i][j].key,outlookbar.itemlist[i][j].title);								
			}else
				outline+=showsubitem(i,j,outlookbar.itemlist[i][j].key,outlookbar.itemlist[i][j].title); 			
		}
		outline+="</table></div></td></tr>";
	}
	outline+="</table>"
	//alert(outline);
	return outline

}

function show()
{
	var outline;
	outline="<div id=outLookBarDiv name=outLookBarDiv style='width=100%;height:100%'>";
	outline+=outlookbar.getOutLine();
	outline+="</div>";
	document.write(outline);
}

function theitem(intitle,instate,inkey)
{
	this.state=instate;
	this.otherclass=" nowrap ";
	this.key=inkey;
	this.title=intitle;
}

function addtitle(intitle)
{
	outlookbar.itemlist[outlookbar.titlelist.length]=new Array();
	outlookbar.subitemlist[outlookbar.titlelist.length]=new Array();
	outlookbar.titlelist[outlookbar.titlelist.length]=new theitem(intitle,1,0);
	return(outlookbar.titlelist.length-1);
}

function additem(intitle,parentid,inkey)
{
	if (parentid>=0 && parentid<=outlookbar.titlelist.length)
	{
		outlookbar.subitemlist[parentid][outlookbar.itemlist[parentid].length]=new Array();
		outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length]=new theitem(intitle,2,inkey);
		outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length-1].otherclass=" nowrap align=left style='height:5' ";
		return(outlookbar.itemlist[parentid].length-1);
	}
	else
		additem=-1;
}

function addsubitem(intitle,titleid,itemid,inkey)
{
	if (titleid>=0 && titleid<=outlookbar.titlelist.length)
	{
		if(itemid>=0 && itemid<=outlookbar.itemlist[titleid].length){
			outlookbar.subitemlist[titleid][itemid][outlookbar.subitemlist[titleid][itemid].length]=new theitem(intitle,3,inkey);
			outlookbar.subitemlist[titleid][itemid][outlookbar.subitemlist[titleid][itemid].length - 1].otherclass=" nowrap align=left style='height:5' ";
			return(outlookbar.subitemlist[titleid][itemid].length-1);
		}
	}
	else
		addsubitem=-1;
}

function outlook()
{
	this.titlelist=new Array();
	this.itemlist=new Array();
	this.subitemlist=new Array();
	this.divstyle="style='height:100%;width:100%;overflow:auto' align=center";
	this.otherclass="style='height:100%;width:100%'valign=middle align=center ";
	this.addtitle=addtitle;
	this.additem=additem;
	this.addsubitem=addsubitem;
	this.starttitle=-1;
	this.show=show;
	this.getOutLine=getOutLine;
	this.opentitle=this.starttitle;
	this.reflesh=outreflesh;
	this.timedelay=50;
	this.inc=10;
	
}

function outreflesh()
{
	document.all("outLookBarDiv").innerHTML=outlookbar.getOutLine();
}

function locatefold(foldname)
{
	for (var i=0;i<outlookbar.titlelist.length;i++)
		if(foldname==outlookbar.titlelist[i].title)
			{
				 outlookbar.starttitle=i;
				 outlookbar.opentitle=i;
			}
	
}

var outlookbar=new outlook();
var tempinnertext1,tempinnertext2,outlooksmoothstat
outlooksmoothstat = 0;
