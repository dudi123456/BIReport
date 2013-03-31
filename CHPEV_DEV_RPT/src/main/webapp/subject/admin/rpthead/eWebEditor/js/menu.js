/*
*######################################
* eWebEditor v3.80 - Advanced online web based WYSIWYG HTML editor.
* Copyright (c) 2003-2006 eWebSoft.com
*
* For further information go to http://www.ewebsoft.com/
* This copyright notice MUST stay intact for use.
*######################################
*/


var sMenuHr="<tr><td align=center valign=middle height=2><TABLE border=0 cellpadding=0 cellspacing=0 width="+(lang["UIMenuWidth"]-22)+" height=2><tr><td height=1 class=HrShadow><\/td><\/tr><tr><td height=1 class=HrHighLight><\/td><\/tr><\/TABLE><\/td><\/tr>";
var sMenu1="<TABLE border=0 cellpadding=0 cellspacing=0 class=Menu width="+lang["UIMenuWidth"]+"><tr><td width=18 valign=bottom align=center style='background:url(sysimage/contextmenu.gif);background-position:bottom;'><\/td><td width="+(lang["UIMenuWidth"]-18)+" class=RightBg><TABLE border=0 cellpadding=0 cellspacing=0>";
var sMenu2="<\/TABLE><\/td><\/tr><\/TABLE>";

var oPopupMenu = null;
if (BrowserInfo.IsIE55OrMore){
	oPopupMenu = window.createPopup();
}


function getMenuRow(s_Disabled, s_Event, s_Image, s_Html) {
	var s_MenuRow = "";
	s_MenuRow = "<tr><td align=center valign=middle><TABLE border=0 cellpadding=0 cellspacing=0 width="+(lang["UIMenuWidth"]-18)+"><tr "+s_Disabled+"><td valign=middle height=20 class=MouseOut onMouseOver=this.className='MouseOver'; onMouseOut=this.className='MouseOut';";
	if (s_Disabled==""){
		s_MenuRow += " onclick=\"parent."+s_Event+";parent.oPopupMenu.hide();\"";
	}
	s_MenuRow += ">"
	if (s_Image !=""){
		s_MenuRow += "&nbsp;<img border=0 src='buttonimage/"+s_Image+"' width=20 height=20 align=absmiddle "+s_Disabled+">&nbsp;";
	}else{
		s_MenuRow += "&nbsp;";
	}
	s_MenuRow += s_Html+"<\/td><\/tr><\/TABLE><\/td><\/tr>";
	return s_MenuRow;

}


function getFormatMenuRow(menu, html, image){
	var s_Disabled = "";
	if (!eWebEditor.document.queryCommandEnabled(menu)){
		s_Disabled = "disabled";
	}
	var s_Event = "format('"+menu+"')";
	var s_Image = menu+".gif";
	if (image){
		s_Image = image;
	}
	return getMenuRow(s_Disabled, s_Event, s_Image, html)
}


function getTableMenuRow(what){
	var s_Menu = "";
	var s_Disabled = "disabled";
	switch(what){
	case "TableInsert":
		if (!isTableSelected()) s_Disabled="";
		s_Menu += getMenuRow(s_Disabled, "TableInsert()", "tableinsert.gif", lang["TableInsert"])
		break;
	case "TableProp":
		if (isTableSelected()||isCursorInTableCell()) s_Disabled="";
		s_Menu += getMenuRow(s_Disabled, "TableProp()", "tableprop.gif", lang["TableProp"])
		break;
	case "TableCell":
		if (isCursorInTableCell()) s_Disabled="";
		s_Menu += getMenuRow(s_Disabled, "TableCellProp()", "tablecellprop.gif", lang["TableCellProp"])
		s_Menu += getMenuRow(s_Disabled, "TableCellSplit()", "tablecellsplit.gif", lang["TableCellSplit"])
		s_Menu += sMenuHr;
		s_Menu += getMenuRow(s_Disabled, "TableRowProp()", "tablerowprop.gif", lang["TableRowProp"])
		s_Menu += getMenuRow(s_Disabled, "TableRowInsertAbove()", "tablerowinsertabove.gif", lang["TableRowInsertAbove"]);
		s_Menu += getMenuRow(s_Disabled, "TableRowInsertBelow()", "tablerowinsertbelow.gif", lang["TableRowInsertBelow"]);
		s_Menu += getMenuRow(s_Disabled, "TableRowMerge()", "tablerowmerge.gif", lang["TableRowMerge"]);
		s_Menu += getMenuRow(s_Disabled, "TableRowSplit(2)", "tablerowsplit.gif", lang["TableRowSplit"]);
		s_Menu += getMenuRow(s_Disabled, "TableRowDelete()", "tablerowdelete.gif", lang["TableRowDelete"]);
		s_Menu += sMenuHr;
		s_Menu += getMenuRow(s_Disabled, "TableColInsertLeft()", "tablecolinsertleft.gif", lang["TableColInsertLeft"]);
		s_Menu += getMenuRow(s_Disabled, "TableColInsertRight()", "tablecolinsertright.gif", lang["TableColInsertRight"]);
		s_Menu += getMenuRow(s_Disabled, "TableColMerge()", "tablecolmerge.gif", lang["TableColMerge"]);
		s_Menu += getMenuRow(s_Disabled, "TableColSplit(2)", "tablecolsplit.gif", lang["TableColSplit"]);
		s_Menu += getMenuRow(s_Disabled, "TableColDelete()", "tablecoldelete.gif", lang["TableColDelete"]);
		break;
	}
	return s_Menu;
}


function showContextMenu(event){
	if (!bEditMode) return false;

	var width = lang["UIMenuWidth"];
	var height = 0;
	var lefter = event.clientX;
	var topper = event.clientY;

	var oPopDocument = oPopupMenu.document;
	var oPopBody = oPopupMenu.document.body;

	var sMenu="";

	if (isCursorInTableCell()){
		sMenu += getTableMenuRow("TableProp");
		sMenu += getTableMenuRow("TableCell");
		sMenu += sMenuHr;
		height += 286;
	}

	if (isControlSelected("TABLE")){
		sMenu += getTableMenuRow("TableProp");
		sMenu += sMenuHr;
		height += 22;
	}

	if (isControlSelected("IMG")){
		sMenu += getMenuRow("", "showDialog('img.htm', true)", "img.gif", lang["CMenuImg"]);
		sMenu += sMenuHr;
		sMenu += getMenuRow("", "zIndex('forward')", "forward.gif", lang["zIndexForward"]);
		sMenu += getMenuRow("", "zIndex('backward')", "backward.gif", lang["zIndexBackward"]);
		sMenu += sMenuHr;
		height += 64;
	}

	sMenu = sMenu1 + sMenu + sMenu2;

	oPopDocument.open();
	oPopDocument.write(config.StyleMenuHeader+sMenu);
	oPopDocument.close();

	height+=2;
	if(lefter+width > document.body.clientWidth) lefter=lefter-width;

	oPopupMenu.show(lefter, topper, width, height, eWebEditor.document.body);
	return false;

}


function showToolMenu(menu){
	if (!bEditMode) return false;
	var sMenu = ""
	var width = lang["UIMenuWidth"];
	var height = 0;

	var lefter = event.clientX;
	var leftoff = event.offsetX
	var topper = event.clientY;
	var topoff = event.offsetY;

	var oPopDocument = oPopupMenu.document;
	var oPopBody = oPopupMenu.document.body;

	switch(menu){
	case "font":
		sMenu += getFormatMenuRow("bold", lang["Bold"], "bold.gif");
		sMenu += getFormatMenuRow("italic", lang["Italic"], "italic.gif");
		sMenu += getFormatMenuRow("underline", lang["UnderLine"], "underline.gif");
		sMenu += sMenuHr;
		sMenu += getMenuRow("", "showDialog('selcolor.htm?action=forecolor', true)", "forecolor.gif", lang["ForeColor"]);
		sMenu += getMenuRow("", "showDialog('selcolor.htm?action=backcolor', true)", "backcolor.gif", lang["BackColor"]);
		height = 206;
		break;
	case "edit":
		var s_Disabled = "";
		if (history.data.length <= 1 || history.position <= 0) s_Disabled = "disabled";
		sMenu += getMenuRow(s_Disabled, "goHistory(-1)", "undo.gif", lang["UnDo"])
		if (history.position >= history.data.length-1 || history.data.length == 0) s_Disabled = "disabled";
		sMenu += getMenuRow(s_Disabled, "goHistory(1)", "redo.gif", lang["ReDo"])
		sMenu += sMenuHr;
		sMenu += getFormatMenuRow("Cut", lang["Cut"], "cut.gif");
		sMenu += getFormatMenuRow("Copy", lang["Copy"], "copy.gif");
		sMenu += getFormatMenuRow("Paste", lang["Paste"], "paste.gif");
		sMenu += getMenuRow("", "PasteText()", "pastetext.gif", lang["PasteText"]);
		sMenu += getMenuRow("", "PasteWord()", "pasteword.gif", lang["PasteWord"]);
		height = 248;
		break;
	case "object":
		sMenu += getMenuRow("", "showDialog('selcolor.htm?action=bgcolor', true)", "bgcolor.gif", lang["BgColor"]);
		sMenu += sMenuHr;
		sMenu += getMenuRow("", "insert('quote')", "quote.gif", lang["Quote"]);
		sMenu += getMenuRow("", "insert('code')", "code.gif", lang["Code"]);
		height = 166;
		break;
	case "component":
		sMenu += getMenuRow("", "showDialog('img.htm', true)", "img.gif", lang["Image"]);
		sMenu += getMenuRow("", "showDialog('flash.htm', true)", "flash.gif", lang["Flash"]);
		sMenu += getMenuRow("", "showDialog('media.htm', true)", "media.gif", lang["Media"]);
		sMenu += sMenuHr;
		sMenu += getMenuRow("", "showDialog('fieldset.htm', true)", "fieldset.gif", lang["Fieldset"]);
		sMenu += getFormatMenuRow("InsertHorizontalRule", lang["HorizontalRule"], "inserthorizontalrule.gif");
		sMenu += sMenuHr;
		sMenu += getMenuRow("", "createLink()", "createlink.gif", lang["CreateLink"]);
		sMenu += getFormatMenuRow("UnLink", lang["Unlink"], "unlink.gif");
		height = 266;
		break;
	case "tool":
		sMenu += getMenuRow("", "showDialog('symbol.htm', true)", "symbol.gif", lang["Symbol"]);
		height = 144;
		break;
	case "file":
		sMenu += getMenuRow("", "setMode('CODE')", "modecodebtn.gif", lang["ModeCode"]);
		sMenu += getMenuRow("", "setMode('EDIT')", "modeeditbtn.gif", lang["ModeEdit"]);
		sMenu += getMenuRow("", "setMode('TEXT')", "modetextbtn.gif", lang["ModeText"]);
		sMenu += getMenuRow("", "setMode('VIEW')", "modeviewbtn.gif", lang["ModeView"]);
		sMenu += sMenuHr;
		sMenu += getMenuRow("", "sizeChange(300)", "sizeplus.gif", lang["SizePlus"]);
		sMenu += getMenuRow("", "sizeChange(-300)", "sizeminus.gif", lang["SizeMinus"]);
		height = 208;
		break;
	case "table":
		sMenu += getTableMenuRow("TableInsert");
		sMenu += getTableMenuRow("TableProp");
		sMenu += sMenuHr;
		sMenu += getTableMenuRow("TableCell");
		height = 306;
		break;
	}
	
	sMenu = sMenu1 + sMenu + sMenu2;
	
	oPopDocument.open();
	oPopDocument.write(config.StyleMenuHeader+sMenu);
	oPopDocument.close();

	height+=2;
	if(lefter+width > document.body.clientWidth) lefter=lefter-width+22;

	oPopupMenu.show(lefter - leftoff - 2, topper - topoff + 22, width, height, document.body);

	return false;
}

