/*
*######################################
* eWebEditor v3.80 - Advanced online web based WYSIWYG HTML editor.
* Copyright (c) 2003-2006 eWebSoft.com
*
* For further information go to http://www.ewebsoft.com/
* This copyright notice MUST stay intact for use.
*######################################
*/


document.write ("<table border=0 cellpadding=0 cellspacing=0 width='100%' height='100%' class='EditorBody'>");
document.write ("<tr><td>");

showToolbar();

document.write ("</td></tr>");
document.write ("<tr><td height='100%'>");

document.write ("	<table border=0 cellpadding=0 cellspacing=0 width='100%' height='100%'>");
document.write ("	<tr><td height='100%'>");
document.write ("	<input type='hidden' ID='ContentEdit' value=''>");
document.write ("	<input type='hidden' ID='ModeEdit' value=''>");
document.write ("	<input type='hidden' ID='ContentLoad' value=''>");
document.write ("	<input type='hidden' ID='ContentFlag' value='0'>");
document.write ("	<iframe class='Composition' ID='eWebEditor' MARGINHEIGHT='1' MARGINWIDTH='1' width='100%' height='100%' scrolling='auto' FRAMEBORDER='0'>");
document.write ("	</iframe>");
document.write ("	</td></tr>");
document.write ("	</table>");

document.write ("</td></tr>");

if (config.StateFlag=="1"){
	document.write ("<tr><td height=28>");
	document.write ("	<TABLE border='0' cellPadding='0' cellSpacing='0' width='100%' class=StatusBar height=25>");
	document.write ("	<TR valign=middle>");
	document.write ("	<td>");
	document.write ("		<table border=0 cellpadding=0 cellspacing=0 height=20>");
	document.write ("		<tr>");
	document.write ("		<td width=10></td>");
	document.write ("		<td class=StatusBarBtnOff id=eWebEditor_CODE onclick=\"setMode('CODE')\" unselectable=on><img border=0 src='buttonimage/modecode.gif' width=20 height=15 align=absmiddle>" + lang["StatusModeCode"] + "</td>");
	document.write ("		<td width=5></td>");
	document.write ("		<td class=StatusBarBtnOff id=eWebEditor_EDIT onclick=\"setMode('EDIT')\" unselectable=on><img border=0 src='buttonimage/modeedit.gif' width=20 height=15 align=absmiddle>" + lang["StatusModeEdit"] + "</td>");
	document.write ("		<td width=5></td>");
	document.write ("		<td class=StatusBarBtnOff id=eWebEditor_TEXT onclick=\"setMode('TEXT')\" unselectable=on><img border=0 src='buttonimage/modetext.gif' width=20 height=15 align=absmiddle>" + lang["StatusModeText"] + "</td>");
	document.write ("		<td width=5></td>");
	document.write ("		<td class=StatusBarBtnOff id=eWebEditor_VIEW onclick=\"setMode('VIEW')\" unselectable=on><img border=0 src='buttonimage/modeview.gif' width=20 height=15 align=absmiddle>" + lang["StatusModeView"] + "</td>");
	document.write ("		</tr>");
	document.write ("		</table>");
	document.write ("	</td>");
	document.write ("	<td align=right>");
	document.write ("		<table border=0 cellpadding=0 cellspacing=0 height=20>");
	document.write ("		<tr>");
//	document.write ("		<td><a href='http://www.4ngel.net' target='_blank' style='padding-right:30px;color:#000000; text-decoration:underline;'>Www.4ngel.Net</a></td>");
	document.write ("		<td style='cursor:pointer;' onclick='sizeChange(300)'><img border=0 SRC='buttonimage/sizeplus.gif' width=20 height=20 alt='" + lang["SizePlus"] + "'></td>");
	document.write ("		<td width=5></td>");
	document.write ("		<td style='cursor:pointer;' onclick='sizeChange(-300)'><img border=0 SRC='buttonimage/sizeminus.gif' width=20 height=20 alt='" + lang["SizeMinus"] + "'></td>");
	document.write ("		<td width=10></td>");
	document.write ("		</tr>");
	document.write ("		</table>");
	document.write ("	</td>");
	document.write ("	</TR>");
	document.write ("	</Table>");
	document.write ("</td></tr>");
}
document.write ("</table>");

document.write ("<div id='eWebEditor_Temp_HTML' style='VISIBILITY: hidden; OVERFLOW: hidden; POSITION: absolute; WIDTH: 1px; HEIGHT: 1px'></div>");
