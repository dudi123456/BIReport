var  strLength=18;
var m_active_menu_key = "";
var MENU_SEL_CSS = "font-bold";//选中样式
var MENU_NOMAL_CSS = "";//一般样式


function byteLength(str) {
	var tep = str;
	var reg = new RegExp("[^\x00-\xff]", "g");
	return tep.replace(reg, "aa").length;
}

function truncateStr(str, sizeLimit) {
	var tep = str;
	var reg = new RegExp("[^\x00-\xff]", "g");
	var charStr = "";
	var char = "";
	var counter = 0;
	for ( var i = 0; i < tep.length; i++) {
		char = "" + tep.charAt(i);
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
}

//显示
function showsubitem(titleid, itemid, id, name) {
	var tepName = name;
	//var strLength = 20;
	if (name != null && byteLength(name) > strLength) {
		tepName = truncateStr(name, strLength) + "...";
		tepcode = "title='" + name + "'";
	}
	var str = "<div class=\"node1\">" +
			"<a href='javascript:;' class=\"icon1 open\">" +tepName+
			"</a></div>";
	str += "<div class=\"node1_box childBox\">";
	for ( var i = 0; i < outlookbar.subitemlist[titleid][itemid].length; i++){
		str += showitem(titleid, itemid,i,outlookbar.subitemlist[titleid][itemid][i].key,outlookbar.subitemlist[titleid][itemid][i].title);
	}
	str += "</div>";
	return (str);
}

//显示没有孩子节点
function showsubitemNoChild(titleid, itemid, id, name) {
	var tepName = name;
	var tepcode = "";
	if (name != null && byteLength(name) > strLength) {
		tepName = truncateStr(name, strLength) + "...";
		tepcode = "title='" + name + "'";
	}
	//
	var str = " <div class=\"node5\">" +
			"<a "+ tepcode + " target=bodyFrame  class=\"icon1\" href='" + id + "' >";
	   str += tepName + "</a></div>";
	return (str);
}

//显示下级菜单
function showChildren(titleid, itemid) {
	if (eval("child_" + titleid + "_" + itemid).parentNode.style.display == "") {
		eval("child_" + titleid + "_" + itemid).parentNode.style.display = "none";
		eval('document.getElementById(' + "'button_" + titleid + "_" + itemid+ "'" + ').src = "../biimages/home/button_close.gif"');
	} else {
		eval("child_" + titleid + "_" + itemid).parentNode.style.display = "";
		eval('document.getElementById(' + "'button_" + titleid + "_" + itemid+ "'" + ').src = "../biimages/home/button_open.gif"');
	}

}
//显示
function showitem(titleid, itemid,index,id, name) {
	var tepName = name;
	var tepcode = "";
	if (name != null && byteLength(name) > strLength) {
		tepName = truncateStr(name, strLength) + "...";
		tepcode = "title='" + name + "'";
	}

	var str = "<div class=\"node2\">" +
			"<a  "
			+ tepcode + "target=bodyFrame class=\"icon1\" href='"+ id+ "'>"+ tepName
			+ "</a></div>";
	return (str);
}


//切换工作条
function switchoutlookBar(number) {
	var i = outlookbar.opentitle;
	outlookbar.opentitle = number;
	var id1, id2, id1b, id2b;
	if (number != i && outlooksmoothstat == 0) {
		if (number != -1) {
			if (i == -1) {
				id2 = "blankdiv";
				id2b = "blankdiv";
			} else {
				id2 = "outlookdiv" + i;
				id2b = "outlookdivin" + i;
				document.all("outlooktitle" + i).style.border = "1px none ";
				document.all("outlooktitle" + i).style.textalign = "center";
			}
			id1 = "outlookdiv" + number;
			id1b = "outlookdivin" + number;
			document.all("outlooktitle" + number).style.border = "1px none white";
			document.all("outlooktitle" + number).style.textalign = "center";
			smoothout(id1, id2, id1b, id2b, 0);
		} else {
			document.all("blankdiv").style.display = "";
			document.all("blankdiv").sryle.height = "100%";
			document.all("outlookdiv" + i).style.display = "none";
			document.all("outlookdiv" + i).style.height = "0%";
			document.all("outlooktitle" + i).style.border = "1px none navy";
			document.all("outlooktitle" + i).style.textalign = "center";
		}
	}

}
//平滑
function smoothout(id1, id2, id1b, id2b, stat) {
	if (stat == 0) {
		tempinnertext1 = document.all(id1b).innerHTML;
		tempinnertext2 = document.all(id2b).innerHTML;
		document.all(id1b).innerHTML = "";
		document.all(id2b).innerHTML = "";
		outlooksmoothstat = 1;
		document.all(id1b).style.overflow = "hidden";
		document.all(id2b).style.overflow = "hidden";
		document.all(id1).style.height = "0%";
		document.all(id1).style.display = "";
		setTimeout("smoothout('" + id1 + "','" + id2 + "','" + id1b + "','"
				+ id2b + "'," + outlookbar.inc + ")", outlookbar.timedalay);
	} else {
		stat += outlookbar.inc;
		if (stat > 100)
			stat = 100;
		document.all(id1).style.height = stat + "%";
		document.all(id2).style.height = (100 - stat) + "%";
		if (stat < 100)
			setTimeout("smoothout('" + id1 + "','" + id2 + "','" + id1b + "','"
					+ id2b + "'," + stat + ")", outlookbar.timedalay);
		else {
			document.all(id1b).innerHTML = tempinnertext1;
			document.all(id2b).innerHTML = tempinnertext2;
			outlooksmoothstat = 0;
			document.all(id1b).style.overflow = "auto";
			document.all(id2).style.display = "none";
		}
	}
}

//
function getOutLine() {
	var outline = [];
	for (var i = 0; i < (outlookbar.titlelist.length); i++) {
		outline.push("<div class=\"treenode\">");
		outline.push(outlookbar.titlelist[i].title);
		outline.push("</div>");
		outline.push("<div name=\"childBox\" ");
		if (i != outlookbar.opentitle){
			outline.push("style=\"display: none;overflow-y:auto\" ");
		}else{
			outline.push("style=\"overflow-y:auto\" ");
		}
		outline.push(">");
		if(outlookbar.itemlist[i].length>0){
			outline.push("<div class=\"childnodebox childBox\">");
			for (var j = 0; j < (outlookbar.itemlist[i].length); j++) {
				if (outlookbar.subitemlist[i][j].length == 0) {
					outline.push(showsubitemNoChild(i, j,outlookbar.itemlist[i][j].key,outlookbar.itemlist[i][j].title));
				} else{
					outline.push(showsubitem(i, j, outlookbar.itemlist[i][j].key,outlookbar.itemlist[i][j].title));
				}
			}
			outline.push("</div>");
		}
		outline.push("</div>");
	 }
	return outline.join("");

}
//
function show() {
	var outline;
	outline = outlookbar.getOutLine();
	document.write(outline);
}
//
function theitem(intitle, instate, inkey) {
	this.state = instate;
	this.otherclass = " nowrap ";
	this.key = inkey;
	this.title = intitle;
}
//
function addtitle(intitle) {
	outlookbar.itemlist[outlookbar.titlelist.length] = new Array();
	outlookbar.subitemlist[outlookbar.titlelist.length] = new Array();
	outlookbar.titlelist[outlookbar.titlelist.length] = new theitem(intitle, 1,0);
	return (outlookbar.titlelist.length - 1);
}
//
function additem(intitle, parentid, inkey) {
	if (parentid >= 0 && parentid <= outlookbar.titlelist.length) {
		outlookbar.subitemlist[parentid][outlookbar.itemlist[parentid].length] = new Array();
		outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length] = new theitem(intitle, 2, inkey);
		outlookbar.itemlist[parentid][outlookbar.itemlist[parentid].length - 1].otherclass = " nowrap align=left style='height:5' ";
		return (outlookbar.itemlist[parentid].length - 1);
	} else {
		return -1;
	}
}

function addsubitem(intitle, titleid, itemid, inkey) {
	if (titleid >= 0 && titleid <= outlookbar.titlelist.length) {
		if (itemid >= 0 && itemid <= outlookbar.itemlist[titleid].length) {
			outlookbar.subitemlist[titleid][itemid][outlookbar.subitemlist[titleid][itemid].length] = new theitem(intitle, 3, inkey);
			outlookbar.subitemlist[titleid][itemid][outlookbar.subitemlist[titleid][itemid].length - 1].otherclass = " nowrap align=left style='height:5' ";
			return (outlookbar.subitemlist[titleid][itemid].length - 1);
		}
	} else{
		return  -1;
	}
}
//
function outlook() {
	this.titlelist = new Array();
	this.itemlist = new Array();
	this.subitemlist = new Array();
	this.divstyle = "style='height:100%;width:100%;overflow:auto' align=center";
	this.otherclass = "style='height:100%;width:100%'valign=middle align=center ";
	this.addtitle = addtitle;
	this.additem = additem;
	this.addsubitem = addsubitem;
	this.starttitle = -1;
	this.show = show;
	this.getOutLine = getOutLine;
	this.opentitle = this.starttitle;
	this.reflesh = outreflesh;
	this.timedelay = 50;
	this.inc = 10;

}
//
function outreflesh() {
	document.all("outLookBarDiv").innerHTML = outlookbar.getOutLine();
}

function locatefold(foldname) {
	for ( var i = 0; i < outlookbar.titlelist.length; i++)
		if (foldname == outlookbar.titlelist[i].title) {
			outlookbar.starttitle = i;
			outlookbar.opentitle = i;
		}

}
//
function setLeftMenuFocus(titleid ,itemid,index){
	var tdname = "child_"+ titleid+ "_"+ itemid+"_"+index;
	var obj_td=document.getElementById(tdname);
	if(null!=obj_td){
		obj_td.className="font-bold";
		if(m_active_menu_key!=""){
			obj_td=document.getElementById(m_active_menu_key);
			if(null!=obj_td){
				obj_td.className="";
			}
		}
		m_active_menu_key=tdname;
	}
}
//
var outlookbar = new outlook();
var tempinnertext1, tempinnertext2, outlooksmoothstat;
outlooksmoothstat = 0;
