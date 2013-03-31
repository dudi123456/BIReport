/**
 * struts-layout core javascript
 *
 * All rights reserved.
 */

function checkValue(field, property, type, required) {
        if (field.value!="") {
                document.images[property + "required"].src= imgsrc + "clearpixel.gif";
                if (type=="NUMBER" && !isNumber(field.value)) document.images[property + "required"].src= imgsrc + "ast.gif";
                if (type=="DATE" && !isDate(field.value)) document.images[property + "required"].src = imgsrc + "ast.gif";
                if (type=="EMAIL" && !isEmail(field.value)) document.images[property + "required"].src= imgsrc + "ast.gif";
        } else {
                if (required) document.images[property + "required"].src= imgsrc + "ast.gif";
        }
}

// Return true if value is an e-mail address
function isEmail(value) {
        invalidChars = " /:,;";
        if (value=="") return false;

        for (i=0; i<invalidChars.length;i++) {
           badChar = invalidChars.charAt(i);
           if (value.indexOf(badChar,0) != -1) return false;
        }

        atPos = value.indexOf("@", 1);
        if (atPos == -1) return false;
        if (value.indexOf("@", atPos + 1) != -1) return false;

        periodPos = value.indexOf(".", atPos);
        if (periodPos == -1) return false;

        if (periodPos+3 > value.length) return false;

        return true;
}



// Return true if value is a number
function isNumber(value) {
        if (value=="") return false;

        var d = parseInt(value);
        if (!isNaN(d)) return true; else return false;

}

// return true if value is a date
// ie in the format XX/YY/ZZ where XX YY and ZZ are numbers
function isDate(value) {
        if (value=="") return false;

        var pos = value.indexOf("/");
        if (pos == -1) return false;
        var d = parseInt(value.substring(0,pos));
        value = value.substring(pos+1, 999);
        pos = value.indexOf("/");
        if (pos==-1) return false;
        var m = parseInt(value.substring(0,pos));
        value = value.substring(pos+1, 999);
        var y = parseInt(value);
        if (isNaN(d)) return false;
        if (isNaN(m)) return false;
        if (isNaN(y)) return false;

        var type=navigator.appName;
        if (type=="Netscape") var lang = navigator.language;
        else var lang = navigator.userLanguage;
        lang = lang.substr(0,2);

        if (lang == "fr") var date = new Date(y, m-1, d);
        else var date = new Date(d, m-1, y);
        if (isNaN(date)) return false;
        return true;
 }

// menu functions

function initMenu(menu) {
        if (getMenuCookie(menu)=="hide") {
                document.getElementById(menu).style.display="none";
        } else {
                document.getElementById(menu).style.display="";
        }
}

function changeMenu(menu) {
if (document.getElementById(menu).style.display=="none") {
        document.getElementById(menu).style.display="";
        element = document.getElementById(menu+"b");
        if (element != null) {
                document.getElementById(element).style.display="none";
        }
        setMenuCookie(menu,"show");
} else {
        document.getElementById(menu).style.display="none";
        element = document.getElementById(menu+"b");
        if (element != null) {
                var width = document.getElementById(menu).offsetWidth;
                if (navigator.vendor == ("Netscape6") || navigator.product == ("Gecko"))
                        document.getElementById(menu+"b").style.width = width;
                else
                        document.getElementById(menu+"b").width = width;
                document.getElementById(menu+"b").style.display="";
        }
        setMenuCookie(menu,"hide");
}
return false;
}

function setMenuCookie(name, state) {
        if (name.indexOf("treeView")!=-1) {
                if (state=="show") {
                        var cookie = getMenuCookie("treeView", "");
                        if (cookie=="???") cookie = "_";
                        cookie = cookie + name + "_";
                        document.cookie = "treeView=" + escape(cookie);

                } else {
                        var cookie = getMenuCookie("treeView", "");
                        var begin = cookie.indexOf("_" + name + "_");
                        if (cookie.length > begin + name.length + 2) {
                                cookie = cookie.substring(0, begin+1) + cookie.substring(begin + 2 + name.length);
                        } else {
                                cookie = cookie.substring(0, begin+1);
                        }
                        document.cookie = "treeView=" + escape(cookie);
                }
        } else {
                var cookie = name + "STRUTSMENU=" + escape(state);
                document.cookie = cookie;
        }
}

function getMenuCookie(name, suffix) {
        if (suffix==null) {
                suffix = "STRUTSMENU";
        }
        var prefix = name + suffix + "=";
        var cookieStartIndex = document.cookie.indexOf(prefix);
        if (cookieStartIndex == -1) return "???";
        var cookieEndIndex = document.cookie.indexOf(";", cookieStartIndex + prefix.length);
        if (cookieEndIndex == -1) cookieEndIndex = document.cookie.length;
        return unescape(document.cookie.substring(cookieStartIndex + prefix.length, cookieEndIndex));
}

// sort functions
function arrayCompare(e1,e2) {
        return e1[0] < e2[0] ? -1 : (e1[0] == e2[0] ? 0 : 1);

}

var tables = new Array();
function arraySort(tableName, column, lineNumber, columnNumber) {
        var aTable = tables[tableName];
        var arrayToSort;
        var array;
        var reverse = 0;
        if (aTable) {
                array = aTable[0];
                arrayToSort = new Array(lineNumber);
                for (i=0;i<lineNumber;i++) {
                        arrayToSort[i] = new Array(2);
                        arrayToSort[i][0] = array[i][column];
                        arrayToSort[i][1] = i;
                }
                reverse = 1 - aTable[1];
                aTable[1] = reverse;
        } else {
                array = new Array(lineNumber);
                arrayToSort = new Array(lineNumber);
                for (i=0;i<lineNumber;i++) {
                        array[i] = new Array(columnNumber);
                        for (j=0;j<columnNumber;j++) {
                                obj = document.getElementById("t" + tableName + "l" + (i+1) +"c" + j);
                                array[i][j] = obj.innerHTML;
                        }
                        arrayToSort[i] = new Array(2);
                        arrayToSort[i][0] = array[i][column];
                        arrayToSort[i][1] = i;

                        aTable = new Array(2);
                        aTable[0] = array;
                        aTable[1] = 0;
                        tables[tableName] = aTable;
                }
        }

        arrayToSort.sort(arrayCompare);
        if (reverse) {
                arrayToSort.reverse();
        }

        for (i=0;i<lineNumber;i++) {
                goodLine = arrayToSort[i][1];
                for (j=0;j<columnNumber;j++) {
                        document.getElementById("t" + tableName + "l" + (i+1) +"c" + j).innerHTML = array[goodLine][j];
                }
        }
}

// calendar functions

var calformname;
var calformelement;
var calpattern;

/**
 * Static code included one time in the page.
 *
 * a {text-decoration: none; color: #000000;}");
 * TD.CALENDRIER {background-color: #C2C2C2; font-weight: bold; text-align: center; font-size: 10px; }");
 *
 * bgColor => #000000, #C9252C,
 */
function printCalendar(day1, day2, day3, day4, day5, day6, day7, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, day, month, year) {

        document.write('<div id="caltitre" style="z-index:10;">');
        document.write('<table cellpadding="0" cellspacing="0" border="0" width="253">');
//	document.write('<form>');
        document.write('<tr><td colspan="15" class="CALENDARBORDER"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td></tr>');
        document.write('<tr>');
        document.write('	<td class="CALENDARBORDER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=20></td>');
        document.write('	<td class="CALENDARTITLE" colspan="3" align="right"><img src="' + imgsrc + 'previous.gif" onclick="cal_before(' + day + ',' + month + ',' + year + ');"></td>');
        document.write('	<td colspan=7 align="center" class="CALENDARTITLE">');
        document.write('<select id="calmois" name="calmois" onchange="cal_chg(' + day +',' + month + ',' + year + ', this.options[this.selectedIndex].value);"><option value=0>...</option>');

        var str='';
        for(i=1;i<37;i++) {
                str+='<option value='+i+'>';
                str+=(year + Math.floor((i-1)/12)) + ' ';	//
                monthIndex = (i-1)%12;
                switch (monthIndex) {
                        case 0: str += month1; break;
                        case 1: str += month2; break;
                        case 2: str += month3; break;
                        case 3: str += month4; break;
                        case 4: str += month5; break;
                        case 5: str += month6; break;
                        case 6: str += month7; break;
                        case 7: str += month8; break;
                        case 8: str += month9; break;
                        case 9: str += month10; break;
                        case 10: str += month11; break;
                        case 11: str += month12; break;
                }
//                str+=' ' + (year + Math.floor((i-1)/12));
        }
        document.write(str);

        document.write('</select>');
        document.write('	</td>');
        document.write('	<td class="CALENDARTITLE" align="left" colspan="3"><img src="' + imgsrc + 'next.gif" onclick="cal_after(' + day + ',' + month + ',' + year + ');">&nbsp;&nbsp;<img src="' + imgsrc + 'close.gif" onclick="hideCalendar()"></td>');
        document.write('	<td class="CALENDARBORDER" width=1><img src="' + imgsrc + 'shim.gif" width="1" height="1"></td>');
        document.write('</tr>');
        document.write('<tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td></tr>');
        document.write('<tr>');
        document.write('	<td class="CALENDARBORDER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('	<td class="CALENDRIER" width="35">' + day1 + '</td>');
        document.write('	<td class="CALENDRIER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('	<td class="CALENDRIER" width="35">' + day2 + '</td>');
        document.write('	<td class="CALENDRIER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('	<td class="CALENDRIER" width="35">' + day3 + '</td>');
        document.write('	<td class="CALENDRIER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('	<td class="CALENDRIER" width="35">' + day4 + '</td>');
        document.write('	<td class="CALENDRIER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('	<td class="CALENDRIER" width="35">' + day5 + '</td>');
        document.write('	<td class="CALENDRIER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('	<td class="CALENDRIER" width="35">' + day6 + '</td>');
        document.write('	<td class="CALENDRIER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('	<td class="CALENDRIER" width="35">' + day7 + '</td>');
        document.write('	<td class="CALENDARBORDER" width="1"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>');
        document.write('</tr>');
        document.write('<tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td></tr>');
//	document.write('</form>');
        document.write('</table>');
        document.write('</div>');
//	document.write('<div id="caljour" style="position:absolute; left:0px; top:45px; width:253; height:130; z-index:10;"></div>');
        document.write('<div id="caljour" style="z-index:10;"></div>');
}

function printCalendar(){
}
/**
 * Show the calendar
 */
function showCalendar(formName, formProperty, event) {

	var ofy=document.body.scrollTop;
    var ofx=document.body.scrollLeft;
    if(document.all.slcalcod) {

		genCalendar();
	 }
	document.all.slcalcod.style.left = event.clientX+ofx+10;
    document.all.slcalcod.style.top = event.clientY+ofy+10;
    document.all.slcalcod.style.visibility="visible";
    document.all.calmois.selectedIndex= month;

}
function showCalendar(year, month, day, pattern, formName, formProperty, event) {

        if(document.all) {
                // IE.
                var ofy=document.body.scrollTop;
                var ofx=document.body.scrollLeft;
                document.all.slcalcod.style.left = event.clientX+ofx+10;
                document.all.slcalcod.style.top = event.clientY+ofy+10;
                document.all.slcalcod.style.visibility="visible";
                document.all.calmois.selectedIndex= month;
                hideElement("SELECT");
        } else if(document.layers) {
                // Netspace 4
                document.slcalcod.left = e.pageX+10;
                document.slcalcod.top = e.pageY+10;
                document.slcalcod.visibility="visible";
                document.slcalcod.document.caltitre.document.forms[0].calmois.selectedIndex=month;
        } else {
                // Mozilla
                var calendrier = document.getElementById("slcalcod");
                var ofy=document.body.scrollTop;
                var ofx=document.body.scrollLeft;
                calendrier.style.left = event.clientX+ofx+10;
                calendrier.style.top = event.clientY+ofy+10;
                calendrier.style.visibility="visible";
                document.getElementById("calmois").selectedIndex=month;
        }
        if (document.forms[formName].elements[formProperty].stlayout) {
                var lc_day = document.forms[formName].elements[formProperty].stlayout.day;
                var lc_month = document.forms[formName].elements[formProperty].stlayout.month;
                var lc_year = parseInt(document.forms[formName].elements[formProperty].stlayout.year);
                cal_chg(lc_day, lc_month, lc_year, lc_month);
        } else {
                cal_chg(day, month, year, month);
        }
        calformname = formName;
        calformelement = formProperty;
        calpattern = pattern;
}

/**
 * Redraw the calendar for the current date and a selected month
 */
function cal_chg(day, month, year, newMonth){
        var str='',j;
        var lc_annee = year;
        lc_annee = year + Math.floor((newMonth-1)/12);
        if (newMonth>12) newMonth = newMonth - 12;
        if (newMonth>12) newMonth = newMonth - 12;

        if(newMonth>0) {

                j=1;

                str+='<table cellpadding=0 cellspacing=0 border=0 width=253>\n';
                for(u=0;u<6;u++){
                        str+='	<tr>\n';
                        for(i=0;i<7;i++){
                                ldt=new Date(lc_annee,newMonth-1,j);
                                str+='		<td class="CALENDARBORDER" width=1><img src="' + imgsrc + 'shim.gif" width=1 height=20></td>\n';
                                str+='		<td class="CALENDAR'; if(ldt.getDay()==i && ldt.getDate()==j && j==day && newMonth==month && lc_annee==year) str+='SELECTED'; else if(i==0 || i==6) str+='WEEKEND'; else str+='WEEK'; str+='" width="35" align="center">';
                                if (ldt.getDay()==i && ldt.getDate()==j) {str+='<a class="CALENDRIER" href="javascript://" class="CALENDRIER" onmousedown="dtemaj(\'' + j + '\',\'' + newMonth + '\',\'' + lc_annee +'\');">'+j+'</a>'; j++;} else str+='&nbsp;';
                                str+='</td>\n';
                        }
                        str+='		<td class="CALENDARBORDER" width=1><img src="' + imgsrc + 'shim.gif" width=1 height=1></td>\n';
                        str+='	</tr>\n';
                        str+='	<tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'shim.gif" width=1 height=1></td></tr>\n';
                }
                str+='</table>\n';

        }

        if(document.all) document.all.caljour.innerHTML=str;
        if(document.layers) {obj=document.calendrier.document.caljour; obj.top=48; obj.document.write(str); obj.document.close();}
        if (!document.all && document.getElementById) document.getElementById("caljour").innerHTML = str;
}

/**
 * Display the previous month
 */
function cal_before(day, month, year) {
        var champ;
        if (document.all) champ = document.all.calmois;//document.all.calendrier.document.caltitre.form.calmois;
        if (document.layers) champ = document.calendrier.document.caltitre.document.form.calmois;
        if (!document.all && document.getElementById) champ = document.getElementById("calmois");
        if (champ.selectedIndex>1) champ.selectedIndex--;
        cal_chg(day, month, year, champ.options[champ.selectedIndex].value);
}

/**
 * Display the next month
 */
function cal_after(day, month, year) {
        // r闰up閞ation de l'objet
        var champ;
        if (document.all) champ = document.all.calmois;//document.all.calendrier.form.calmois;
        if (document.layers) champ = document.calendrier.document.form.calmois;
        if (!document.all && document.getElementById) champ = document.getElementById("calmois");
        if (champ.selectedIndex<champ.options.length) champ.selectedIndex++;
        cal_chg(day, month, year, champ.options[champ.selectedIndex].value);
}

/**
 * Update the date in the input field and hide the calendar.
 * PENDING: find a way to make the format customable.
 */

function dtemaj(jour, mois, annee){
	if (langue=='fr') {
//		document.forms[calformname].elements[calformelement].value = jour + "/" + mois + "/" + annee;
		document.forms[calformname].elements[calformelement].value = annee + "-" + mois + "-" + jour;
	} else {
//		document.forms[calformname].elements[calformelement].value = mois + "/" + jour + "/" + annee;
		document.forms[calformname].elements[calformelement].value = annee + "-" + mois + "-" + jour;
	}
	document.forms[calformname].elements[calformelement].stlayout = new Object();
	document.forms[calformname].elements[calformelement].stlayout.day = jour;
	document.forms[calformname].elements[calformelement].stlayout.month = mois;
	document.forms[calformname].elements[calformelement].stlayout.year = annee;
	hideCalendar();
}

function hideCalendar() {
        if(document.all) {
                // IE.
                document.all.slcalcod.style.visibility="hidden";
                showElement("SELECT");
        } else if(document.layers) {
                // Netspace 4
                document.slcalcod.visibility="hidden";
        } else {
                // Mozilla
                var calendrier = document.getElementById("slcalcod");
                calendrier.style.visibility="hidden";
        }
}

/**
 * Fix IE bug
 */
function hideElement(elmID)
{
        if (!document.all) {
                return;
        }
        x = parseInt(document.all.slcalcod.style.left);
        y = parseInt(document.all.slcalcod.style.top);
        xxx = 253; // document.all.slcalcod.offsetWidth;
        yyy = 145; // document.all.slcalcod.offsetHeight;

        for (i = 0; i < document.all.tags(elmID).length; i++)
        {
                obj = document.all.tags(elmID)[i];
                if (! obj || ! obj.offsetParent || obj.id=="calmois")
                        continue;

                // Find the element's offsetTop and offsetLeft relative to the BODY tag.
                objLeft   = obj.offsetLeft;
                objTop    = obj.offsetTop;
                objParent = obj.offsetParent;
                while (objParent.tagName.toUpperCase() != "BODY")
                {
                        objLeft  += objParent.offsetLeft;
                        objTop   += objParent.offsetTop;
                        objParent = objParent.offsetParent;
                }

                // Adjust the element's offsetTop relative to the dropdown menu
                //objTop = objTop - y;

                if (x > (objLeft + obj.offsetWidth) || objLeft > (x + xxx))
                        ;
                else if (objTop > y + yyy)
                        ;
                else if (y > (objTop + obj.offsetHeight))
                        ;
                else
                        obj.style.visibility = "hidden";
        }
}

/**
 * Fix IE bug
 */
function showElement(elmID)
{
        if (!document.all) {
                return;
        }
        for (i = 0; i < document.all.tags(elmID).length; i++)
        {
                obj = document.all.tags(elmID)[i];
                if (! obj || ! obj.offsetParent)
                        continue;
                obj.style.visibility = "";
        }
}

/**
 * Tabs code.
 */
function selectTab(tabGroupId, tabGroupSize, selectedTabId, enabledStyle, disabledStyle, errorStyle) {
        // first unselect all tab in the tag groups.
        for (i=0;i<tabGroupSize;i++) {
                element = document.getElementById("tabs" + tabGroupId + "head" + i);
                if (element.classNameErrorStdLayout) {
                        element.className = errorStyle;
                        element.style.color = "";
                        document.getElementById("tabs" + tabGroupId + "tab" + i).style.display = "none";
                } else if (element.className == enabledStyle) {
                        element.className = disabledStyle;
                        element.style.color = "";
                        document.getElementById("tabs" + tabGroupId + "tab" + i).style.display = "none";
                } else if (element.className == errorStyle) {
                        document.getElementById("tabs" + tabGroupId + "tab" + i).style.display = "none";
                }
        }
        if (document.getElementById("tabs" + tabGroupId + "head" + selectedTabId).className==errorStyle) {
                document.getElementById("tabs" + tabGroupId + "head" + selectedTabId).classNameErrorStdLayout = new Object();
        }
        document.getElementById("tabs" + tabGroupId + "head" + selectedTabId).className = enabledStyle;
        document.getElementById("tabs" + tabGroupId + "head" + selectedTabId).style.cursor = "default";
        document.getElementById("tabs" + tabGroupId + "tab" + selectedTabId).style.display = "";
}
function onTabHeaderOver(tabGroupId, selectedTabId, enabledStyle) {
        element = document.getElementById("tabs" + tabGroupId + "head" + selectedTabId);
        if (element.className == enabledStyle) {
                element.style.cursor = "default";
        } else {
                element.style.cursor = "hand";
        }
}

/**
 * Treeview code
 */
function loadTree(url, tree) {
        element = document.getElementById("treeView" + url);
        element.innerHTML = tree;
        element.style.display = "";
        element = document.getElementById("treeViewNode" + url);
        element.href = "javascript://";
        setMenuCookie("treeView" + url, "show")
}

function changeTree(tree, image1, image2) {
        var image = document.getElementById("treeViewImage" + tree);
        if (image.src.indexOf(image1)!=-1) {
                image.src = image2;
        } else {
                image.src = image1;
        }

        if (document.getElementById("treeView" + tree).innerHTML == "") {
                return true;
        } else {
                changeMenu("treeView" + tree);
                return false;
        }
}

/**
 * Popup code
 */
function openpopup(form, popup, width, height, e) {
        var xx, yy;
        xx = e.screenX;
        yy = e.screenY;
        window.open('about:blank', 'popup', 'directories=0, location=0, menubar=0, status=0, toolbar=0, width=' + width + ', height=' + height + ', top=' + yy + ', left=' + xx);
        var action = form.action;
        var target = form.target;
        if (popup == null || popup == "") {
                popup = action;
        }
        form.target='popup';
        form.action = popup
        form.submit();
        form.target = target;
        form.action = action;

        return false;
}

function closepopup(form, openerField, popupField) {
        var inputField = form[popupField];
        var value;
        if (inputField.options) {
                value = inputField.options[form[popupField].selectedIndex].value;
        } else {
                for (i=0; i < form.elements.length; i++) {
                        var element = form.elements[i];
                        if (element.name == popupField && element.checked) {
                                value = element.value;
                                break;
                        }
                }
        }
        window.opener.document.forms[0][openerField].value = value;
        window.close();
}
/**
 * form changes detect code
 */
function checkFormChange(link, text) {
  var ok = true;
  for (var form=0; form < document.forms.length; form++) {
    what = document.forms[form];
    for (var i=0, j=what.elements.length; i<j; i++) {

        if (what.elements[i].type == "checkbox" || what.elements[i].type == "radio") {
            if (what.elements[i].checked != what.elements[i].defaultChecked) {
                ok = false; break;
            }
        } else if (what.elements[i].type == "text" || what.elements[i].type == "hidden" || what.elements[i].type == "password" || what.elements[i].type == "textarea") {
            if (what.elements[i].value != what.elements[i].defaultValue) {
                ok = false; break;
            }
        } else if (what.elements[i].type == "select-one" || what.elements[i].type == "select-multiple") {
                for (var k=0, l=what.elements[i].options.length; k<l; k++) {
                        if (what.elements[i].options[k].selected != what.elements[i].options[k].defaultSelected) {
                                ok = false; break;
                        }
                }
        } else if (what.elements[i].type == "submit") {
                break;
        } else {
                alert(what.elements[i].type);
        }
    }
  }
    if (ok) {
        window.location.href = link;
        return;
    }
    if (confirm(text == null ? "Data will be lost. Continue ?" : text)) {
        window.location.href = link;
        return;
    }
}
/**
 *日历选择 yangufeng 6-26
*/

   //设置"month"型的大小 yuexiuyi 2003-10-25
   document.write("<div id=meizzCalendarLayerMonth style='position: absolute; z-index:9999;width: 144; height: 61; display: none'>");
   document.write("<iframe name=meizzCalendarIframeMonth scrolling=no frameborder=0 width=100% height=61px></iframe></div>");
   //date和datetime的大小
   document.write("<div id=meizzCalendarLayer style='position: absolute; z-index:9999;width: 144; height: 193; display: none'>");
   document.write("<iframe name=meizzCalendarIframe scrolling=no frameborder=0 width=100% height=100%></iframe></div>");

function writeIframe()
{
   //头部
    var strIframe = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'><style>"+
    "*{font-size: 12px; font-family: 宋体}"+
    ".bg{  color: "+ WebCalendar.lightColor +"; cursor: default; background-color: "+ WebCalendar.darkColor +";}";
    if(WebCalendar.display_type!="month"){
        strIframe +="table#tableMain{ width: 142; height: 180;}";
    }
    strIframe +="table#tableTime{ width: 142; height: 180;}"+
    "table#tableWeek td{ color: "+ WebCalendar.lightColor +";}"+
    "table#tableDay  td{ font-weight: bold;}"+
    "td#meizzYearHead, td#meizzYearMonth，td#meizzHour,td#meizzMinute,td#meizzmeizzSecond{color: "+ WebCalendar.wordColor +"}"+
    ".out { text-align: center; border-top: 1px solid "+ WebCalendar.DarkBorder +"; border-left: 1px solid "+ WebCalendar.DarkBorder +";"+
    "border-right: 1px solid "+ WebCalendar.lightColor +"; border-bottom: 1px solid "+ WebCalendar.lightColor +";}"+
    ".over{ text-align: center; border-top: 1px solid #FFFFFF; border-left: 1px solid #FFFFFF;"+
    "border-bottom: 1px solid "+ WebCalendar.DarkBorder +"; border-right: 1px solid "+ WebCalendar.DarkBorder +"}"+
    "input{ border: 1px solid "+ WebCalendar.darkColor +"; padding-top: 1px; height: 18; cursor: hand;"+
    "       color:"+ WebCalendar.wordColor +"; background-color: "+ WebCalendar.btnBgColor +"}"+
    "</style></head><body onselectstart='return false' style='margin: 0px' oncontextmenu='return false'><form name=meizz>";

    //javascript函数区
    if (WebCalendar.drag){
    strIframe += "<scr"+"ipt language=javascript>"+
    "var drag=false, cx=0, cy=0, o = parent.WebCalendar.calendar; function document.onmousemove(){"+
    "if(parent.WebCalendar.drag && drag){if(o.style.left=='')o.style.left=0; if(o.style.top=='')o.style.top=0;"+
    "o.style.left = parseInt(o.style.left) + window.event.clientX-cx;"+
    "o.style.top  = parseInt(o.style.top)  + window.event.clientY-cy;}}"+
    "function document.onkeydown(){ switch(window.event.keyCode){  case 27 : parent.hiddenCalendar(); break;"+
    "case 37 : parent.prevM(); break; case 38 : parent.prevY(); break; case 39 : parent.nextM(); break; case 40 : parent.nextY(); break;"+
    "case 84 : document.forms[0].today.click(); break;} window.event.keyCode = 0; window.event.returnValue= false;}"+
    "function dragStart(){cx=window.event.clientX; cy=window.event.clientY; drag=true;}</scr"+"ipt>"
    }
    //年月选择区域
    strIframe +=
    " <select name=tmpYearSelect  onblur='parent.hiddenSelect(this)' style='z-index:1;position:absolute;top:3;left:18;display:none'"+
    " onchange='parent.WebCalendar.thisYear =this.value; parent.hiddenSelect(this); parent.writeCalendar();'></select>"+
    " <select name=tmpMonthSelect onblur='parent.hiddenSelect(this)' style='z-index:1; position:absolute;top:3;left:74;display:none'"+
    " onchange='parent.WebCalendar.thisMonth=this.value; parent.hiddenSelect(this); parent.writeCalendar();'></select>"+

    " <table id=tableMain class=bg border=0 cellspacing=2 cellpadding=0>"+
    " <tr><td width=140 height=19 bgcolor='"+ WebCalendar.lightColor +"'>"+
    " <table width=140 id=tableHead border=0 cellspacing=1 cellpadding=0><tr align=center>"+
    " <td width=15 height=19 class=bg title='向前翻 1 月' style='cursor: hand' onclick='parent.prevM()'><b>&lt;</b></td>"+
    " <td width=60 id=meizzYearHead  title='点击此处选择年份' onclick='parent.funYearSelect(parseInt(this.innerText, 10))'"+
    " onmouseover='this.bgColor=parent.WebCalendar.darkColor; this.style.color=parent.WebCalendar.lightColor'"+
    " onmouseout='this.bgColor=parent.WebCalendar.lightColor; this.style.color=parent.WebCalendar.wordColor'></td>"+
    " <td width=50 id=meizzYearMonth title='点击此处选择月份' onclick='parent.funMonthSelect(parseInt(this.innerText, 10))'"+
    " onmouseover='this.bgColor=parent.WebCalendar.darkColor; this.style.color=parent.WebCalendar.lightColor'"+
    " onmouseout='this.bgColor=parent.WebCalendar.lightColor; this.style.color=parent.WebCalendar.wordColor'></td>"+
    " <td width=15 class=bg title='向后翻 1 月' onclick='parent.nextM()' style='cursor: hand'><b>&gt;</b></td></tr></table>"+
    " </td></tr>"+
    "<tr><td height=20><table id=tableWeek border=1 width=140 cellpadding=0 cellspacing=0 ";
    if(WebCalendar.drag){
       strIframe += "onmousedown='dragStart()' onmouseup='drag=false' onmouseout='drag=false'";
    }
    if(WebCalendar.display_type!="month"){
       strIframe +=
       " borderColorLight='"+ WebCalendar.darkColor +"' borderColorDark='"+ WebCalendar.lightColor +"'>"+
       " <tr align=center><td height=20>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr></table>"+
       " </td></tr><tr><td valign=top width=140 bgcolor='"+ WebCalendar.lightColor +"'>"+
       " <table id=tableDay height=120 width=140 border=0 cellspacing=1 cellpadding=0>";
    }
    for(var x=0; x<5; x++){
       strIframe += "<tr>";
       for(var y=0; y<7; y++)
          strIframe += "<td class=out id='meizzDay"+ (x*7+y) +"'></td>"; strIframe += "</tr>";
    }
    strIframe += "<tr>";
    if(WebCalendar.display_type!="month"){
       for(var x=35; x<39; x++)
          strIframe += "<td class=out id='meizzDay"+ x +"'></td>";
    }
    //关闭按钮
    if(WebCalendar.display_type!="month"){
       strIframe +=
       " <td colspan=3 class=out title='"+ WebCalendar.regInfo +"'><input style=' background-color: "+
         WebCalendar.btnBgColor +";cursor: hand; padding-top: 4px; width: 100%; height: 100%; border: 0' onfocus='this.blur()'"+
       " type=button value='&nbsp; &nbsp; 关闭' onclick='parent.hiddenCalendar()'></td></tr></table>";
    }
    if(WebCalendar.display_type=="month"){
       strIframe +=
       " <td class=out ><input style=' background-color: "+
            WebCalendar.btnBgColor +";cursor: hand; padding-top: 0px; width: 68px; height: 18px; border: 0' '"+
       " type=button value='确 定' onclick='parent.returnDate()'></td>"+
       " <td class=out title='"+ WebCalendar.regInfo +"' ><input style=' background-color: "+
         WebCalendar.btnBgColor +";cursor: hand; padding-top: 0px; width: 68px; height: 18px; border: 0' onfocus='this.blur()'"+
       " type=button value='关 闭' onclick='parent.hiddenCalendar()'></td></tr></table>";
   }
   if(WebCalendar.display_type!="month"){
     strIframe += "<tr><td height=20 width=140 bgcolor='"+ WebCalendar.lightColor +"'>";
   }
   //最下面一行
   if(WebCalendar.display_type=="date"){
       strIframe += " <table border=0 cellpadding=1 cellspacing=0 width=140>"+
       " <tr><td><input name=prevYear title='向前翻 1 年' onclick='parent.prevY()' type=button value='&lt;&lt;'"+
       " onfocus='this.blur()' style='meizz:expression(this.disabled=parent.WebCalendar.thisYear==1000)'><input"+
       " onfocus='this.blur()' name=prevMonth title='向前翻 1 月' onclick='parent.prevM()' type=button value='&lt;&nbsp;'>"+
       " </td><td align=center><input name=today type=button value='Today' onfocus='this.blur()' style='width: 50' title='当前日期'"+
       " onclick=\"parent.returnDate(new Date().getDate() +'/'+ (new Date().getMonth() +1) +'/'+ new Date().getFullYear())\">"+
       " </td><td align=right><input title='向后翻 1 月' name=nextMonth onclick='parent.nextM()' type=button value='&nbsp;&gt;'"+
       " onfocus='this.blur()'><input name=nextYear title='向后翻 1 年' onclick='parent.nextY()' type=button value='&gt;&gt;'"+
       " onfocus='this.blur()' style='meizz:expression(this.disabled=parent.WebCalendar.thisYear==9999)'></td></tr></table>"+
       " </td></tr><table>";
  }
 /**
 * 加上时间输入框 yuexiuyi 2003-10-25
 */
   if(WebCalendar.display_type=="datetime"){
       strIframe +=
       " <select name=tmpHourSelect  onblur='parent.hiddenSelect(this)' style='z-index:1;position:absolute;top:173;left:0;display:none'"+
       " onchange='parent.WebCalendar.thisHour = this.value; parent.hiddenSelect(this); parent.writeCalendar();'></select>"+
      " <select name=tmpMinuteSelect onblur='parent.hiddenSelect(this)' style='z-index:3;position:absolute;top:173;left:45;display:none'"+
      " onchange='parent.WebCalendar.thisMinute = this.value; parent.hiddenSelect(this); parent.writeCalendar();'></select>"+
       " <select name=tmpSecondSelect onblur='parent.hiddenSelect(this)' style='z-index:2;position:absolute;top:173;left:90;display:none'"+
       " onchange='parent.WebCalendar.thisSecond = this.value; parent.hiddenSelect(this); parent.writeCalendar();'></select>"+

      " <table border=0 cellpadding=1 cellspacing=0 width=140><tr>"+
      " <td align=left width=45 id=meizzHour  title='点击此处选择小时' onclick='parent.funHourSelect(parseInt(this.innerText, 10))'"+
      " onmouseover='this.bgColor=parent.WebCalendar.darkColor; this.style.color=parent.WebCalendar.lightColor'"+
      " onmouseout='this.bgColor=parent.WebCalendar.lightColor; this.style.color=parent.WebCalendar.wordColor'></td>"+
      " <td align=left width=45 id=meizzMinute title='点击此处选择分钟' onclick='parent.funMinuteSelect(parseInt(this.innerText, 10))'"+
      " onmouseover='this.bgColor=parent.WebCalendar.darkColor; this.style.color=parent.WebCalendar.lightColor'"+
      " onmouseout='this.bgColor=parent.WebCalendar.lightColor; this.style.color=parent.WebCalendar.wordColor'></td>"+
      " <td align=left width=45 id=meizzSecond title='点击此处选择秒' onclick='parent.funSecondSelect(parseInt(this.innerText, 10))'"+
      " onmouseover='this.bgColor=parent.WebCalendar.darkColor; this.style.color=parent.WebCalendar.lightColor'"+
      " onmouseout='this.bgColor=parent.WebCalendar.lightColor; this.style.color=parent.WebCalendar.wordColor'></td>"+
      "</tr></table>";
   }
   strIframe +="</td></tr>";
   strIframe +="<table></form></body></html>";

   with(WebCalendar.iframe)
    {
        document.writeln(strIframe);
        document.close();
        if(WebCalendar.display_type!="month"){
          for(var i=0; i<39; i++)
           {
               WebCalendar.dayObj[i] = eval("meizzDay"+ i);
               WebCalendar.dayObj[i].onmouseover = dayMouseOver;
               WebCalendar.dayObj[i].onmouseout  = dayMouseOut;
               WebCalendar.dayObj[i].onclick     = returnDate;
           }
        }
    }
}

function WebCalendar() //初始化日历的设置
{
    this.regInfo    = "WEB Calendar Ver1.0 &#13;北京华美博弈软件公司";
    //this.regInfo    = "WEB Calendar Ver1.0 &#13;作者：meizz(梅花雪疏影横斜) &#13;网站：http://www.boyi.com.cn/";
    //this.regInfo   += "&#13;&#13;Ver 2.0：walkingpoison(水晶龙)&#13;Ver 1.0：meizz(梅花雪疏影横斜)";
    this.daysMonth  = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    this.day        = new Array(39);            //定义日历展示用的数]组
    this.dayObj     = new Array(39);            //定义日期展示控件数组
    this.dateStyle  = null;                     //保存格式化后日期数组
    this.objExport  = null;                     //日历回传的显示控件
    this.eventSrc   = null;                     //日历显示的触发控件
    this.inputDate  = null;                     //转化外的输入的日期(d/m/yyyy)
    this.thisYear   = new Date().getFullYear(); //定义年的变量的初始值
    this.thisMonth  = new Date().getMonth()+ 1; //定义月的变量的初始值
    this.thisDay    = new Date().getDate();     //定义日的变量的初始值
    this.thisHour   = null;                     //定义年的变量的初始值
    this.thisMinute = null;                     //定义年的变量的初始值
    this.thisSecond = null;                     //定义年的变量的初始值
    this.display_type = "date";  //date是日期显示格式，datetime是日期时间显示格式，month是月份显示格式
    this.today      = this.thisDay +"/"+ this.thisMonth   +"/"+ this.thisYear;   //今天(d/m/yyyy)
    this.iframe     = null;                //window.frames("meizzCalendarIframe"); //日历的 iframe 载体
    this.calendar   = null;                //getObjectById("meizzCalendarLayer"); //日历的层
    this.dateReg    = "";           //日历格式验证的正则式
    this.yearFall   = 50;           //定义年下拉框的年差值
    this.format     = "yyyy-mm-dd"; //回传日期的格式
    this.timeShow   = true;         //是否返回时间
    this.drag       = false;        //是否允许拖动
    this.darkColor  = "#0000D0";    //控件的暗色
    this.lightColor = "#FFFFFF";    //控件的亮色
    this.btnBgColor = "#E6E6FA";    //控件的按钮背景色
    this.wordColor  = "#000080";    //控件的文字颜色
    this.wordDark   = "#DCDCDC";    //控件的暗文字颜色
    this.dayBgColor = "#F5F5FA";    //日期数字背景色
    this.todayColor = "#FF0000";    //今天在日历上的标示背景色
    this.DarkBorder = "#D4D0C8";    //日期显示的立体表达色
}

var WebCalendar = new WebCalendar();

function calendar(displayMode) //主调函数－－－data、datetime、month
{
    //判断类型－－data、datetime、month
    WebCalendar.display_type=displayMode;
    //初始化时、分、秒（每次双击时刷新当前时间）
    WebCalendar.thisHour= new Date().getHours();
    WebCalendar.thisMinute= new Date().getMinutes();
    WebCalendar.thisSecond=new Date().getSeconds();
    //根据类型设置不同大小的层
    if(displayMode=="month"){
       WebCalendar.iframe     = window.frames("meizzCalendarIframeMonth"); //日历的 iframe 载体--month
       //WebCalendar.calendar   = window.document.getElementById("meizzCalendarLayerMonth");  //日历的层--month
       WebCalendar.calendar   = getObjectById("meizzCalendarLayerMonth");  //日历的层--month
    }
   if(displayMode=="date" || displayMode=="datetime"){
       WebCalendar.iframe     = window.frames("meizzCalendarIframe"); //日历的 iframe 载体
       //WebCalendar.calendar   = window.document.getElementById("meizzCalendarLayer");  //日历的层
       WebCalendar.calendar   = getObjectById("meizzCalendarLayer");  //日历的层
    }
    var e = window.event.srcElement;
    writeIframe();
    var o = WebCalendar.calendar.style;
    WebCalendar.eventSrc = e;

/*注释原因：不管参数
    if (arguments.length == 0) WebCalendar.objExport = e;
    else WebCalendar.objExport = eval(arguments[0]);
*/
    WebCalendar.objExport = e;

    WebCalendar.iframe.tableWeek.style.cursor = WebCalendar.drag ? "move" : "default";
    var t = e.offsetTop,  h = e.clientHeight, l = e.offsetLeft, p = e.type;
    while (e = e.offsetParent){
       t += e.offsetTop; l += e.offsetLeft;
    }
    o.display = "";
    WebCalendar.iframe.document.body.focus();//显示编辑框
    var cw = WebCalendar.calendar.clientWidth, ch = WebCalendar.calendar.clientHeight;
    var dw = document.body.clientWidth, dl = document.body.scrollLeft, dt = document.body.scrollTop;
    if (document.body.clientHeight + dt - t - h >= ch) o.top = (p=="image")? t + h : t + h + 6;
    else o.top  = (t - dt < ch) ? ((p=="image")? t + h : t + h + 6) : t - ch;
    if (dw + dl - l >= cw) o.left = l; else o.left = (dw >= cw) ? dw - cw + dl : dl;
    if  (!WebCalendar.timeShow) WebCalendar.dateReg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/;
    else WebCalendar.dateReg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
    try{
        if (WebCalendar.objExport.value.trim() != ""){
            WebCalendar.dateStyle = WebCalendar.objExport.value.trim().substr(0,19).match(WebCalendar.dateReg);
            WebCalendar.dateStyle = WebCalendar.objExport.value.trim().match(WebCalendar.dateReg);
            if (WebCalendar.dateStyle == null)
            {
                var dateString = WebCalendar.objExport.value.trim();
                var dateString_array = dateString.split("-");

                if(WebCalendar.display_type=="date"){
                   WebCalendar.thisYear   = parseInt(dateString_array[0], 10);
                   WebCalendar.thisMonth  = parseInt(dateString_array[1], 10);
                   WebCalendar.thisDay    = parseInt(dateString_array[2], 10);
                   WebCalendar.inputDate  = parseInt(WebCalendar.thisDay, 10) +"/"+ parseInt(WebCalendar.thisMonth, 10) +"/"+
                   parseInt(WebCalendar.thisYear, 10);
                   writeCalendar();
                }
                if(WebCalendar.display_type=="month"){
                   WebCalendar.thisYear   = parseInt(dateString_array[0], 10);
                   WebCalendar.thisMonth  = parseInt(dateString_array[1], 10);
                   writeCalendar();
                }
            }else{
                WebCalendar.thisYear   = parseInt(WebCalendar.dateStyle[1], 10);
                WebCalendar.thisMonth  = parseInt(WebCalendar.dateStyle[3], 10);
                WebCalendar.thisDay    = parseInt(WebCalendar.dateStyle[4], 10);
                WebCalendar.thisHour   = parseInt(WebCalendar.dateStyle[5], 10);
                WebCalendar.thisMinute = parseInt(WebCalendar.dateStyle[6], 10);
                WebCalendar.thisSecond = parseInt(WebCalendar.dateStyle[7], 10);
                WebCalendar.inputDate  = parseInt(WebCalendar.thisDay, 10) +"/"+ parseInt(WebCalendar.thisMonth, 10) +"/"+
                parseInt(WebCalendar.thisYear, 10);
                writeCalendar();
           }
        }else{
          WebCalendar.thisYear   = new Date().getFullYear();
          WebCalendar.thisMonth  = new Date().getMonth()+ 1;
          WebCalendar.thisDay    = new Date().getDate();
          //alert("原文本框里的日期有错误！\n可能与你定义的显示时分秒有冲突！");
          writeCalendar();
          //return false;

          //writeCalendar();
        }
    }catch(e){writeCalendar();}
}


function funMonthSelect() //月份的下拉框
{
    var m = isNaN(parseInt(WebCalendar.thisMonth, 10)) ? new Date().getMonth() + 1 : parseInt(WebCalendar.thisMonth);
    var e = WebCalendar.iframe.document.forms[0].tmpMonthSelect;
    for (var i=1; i<13; i++) e.options.add(new Option(i +"月", i));
    e.style.display = ""; e.value = m; e.focus();
}
function funYearSelect() //年份的下拉框
{
    var n = WebCalendar.yearFall;
    var e = WebCalendar.iframe.document.forms[0].tmpYearSelect;
    var y = isNaN(parseInt(WebCalendar.thisYear, 10)) ? new Date().getFullYear() : parseInt(WebCalendar.thisYear);
        y = (y <= 1000)? 1000 : ((y >= 9999)? 9999 : y);
    var min = (y - n >= 1000) ? y - n : 1000;
    var max = (y + n <= 9999) ? y + n : 9999;
        min = (max == 9999) ? max-n*2 : min;
        max = (min == 1000) ? min+n*2 : max;
    for (var i=min; i<=max; i++) e.options.add(new Option(i +"年", i));
    e.style.display = ""; e.value = y; e.focus();
}
function funHourSelect()//小时的下拉框
{
    var m = isNaN(parseInt(WebCalendar.thisHour, 10)) ? new Date().getHours() : parseInt(WebCalendar.thisHour);
    var e = WebCalendar.iframe.document.forms[0].tmpHourSelect;
    for (var i=0; i<24; i++) e.options.add(new Option(i +" 时", i));
    e.style.display = "";
    e.value = m;
    e.focus();

}
function funMinuteSelect()//分钟的下拉框
{
    var m = isNaN(parseInt(WebCalendar.thisMinute, 10)) ? new Date().getMinutes() : parseInt(WebCalendar.thisMinute);
    var e = WebCalendar.iframe.document.forms[0].tmpMinuteSelect;
    for (var i=0; i<60; i++) e.options.add(new Option(i +" 分", i));
    e.style.display = "";
    e.value = m;
    e.focus();

}
function funSecondSelect()//秒的下拉框
{
    var m = isNaN(parseInt(WebCalendar.thisSecond, 10)) ? new Date().getSeconds() : parseInt(WebCalendar.thisSecond);
    var e = WebCalendar.iframe.document.forms[0].tmpSecondSelect;
    for (var i=0; i<60; i++) e.options.add(new Option(i +" 秒", i));
    e.style.display = "";
    e.value = m;
    e.focus();

}
function prevM()  //往前翻月份
{
    WebCalendar.thisDay = 1;
    if (WebCalendar.thisMonth==1)
    {
        WebCalendar.thisYear--;
        WebCalendar.thisMonth=13;
    }
    WebCalendar.thisMonth--; writeCalendar();
}
function nextM()  //往后翻月份
{
    WebCalendar.thisDay = 1;
    if (WebCalendar.thisMonth==12)
    {
        WebCalendar.thisYear++;
        WebCalendar.thisMonth=0;
    }
    WebCalendar.thisMonth++; writeCalendar();
}
function prevY(){WebCalendar.thisDay = 1; WebCalendar.thisYear--; writeCalendar();}//往前翻 Year
function nextY(){WebCalendar.thisDay = 1; WebCalendar.thisYear++; writeCalendar();}//往后翻 Year
function hiddenSelect(e){for(var i=e.options.length; i>-1; i--)e.options.remove(i); e.style.display="none";}
function getObjectById(id){
    if(document.all){
     return(eval("document.all."+ id));
    }
     return(eval(id));
}
function hiddenCalendar(){
   getObjectById("meizzCalendarLayer").style.display = "none";
   getObjectById("meizzCalendarLayerMonth").style.display = "none";
}
function appendZero(n){return(("00"+ n).substr(("00"+ n).length-2));}//日期自动补零程序
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}
function dayMouseOver()
{
    this.className = "over";
    this.style.backgroundColor = WebCalendar.darkColor;
    if(WebCalendar.day[this.id.substr(8)].split("/")[1] == WebCalendar.thisMonth)
    this.style.color = WebCalendar.lightColor;
}
function dayMouseOut()
{
    this.className = "out"; var d = WebCalendar.day[this.id.substr(8)], a = d.split("/");
    this.style.removeAttribute('backgroundColor');
    if(a[1] == WebCalendar.thisMonth && d != WebCalendar.today)
    {
        if(WebCalendar.dateStyle && a[0] == parseInt(WebCalendar.dateStyle[4], 10))
        this.style.color = WebCalendar.lightColor;
        this.style.color = WebCalendar.wordColor;
    }
}
function writeCalendar() //对日历显示的数据的处理程序
{
    var y = WebCalendar.thisYear;
    var m = WebCalendar.thisMonth;
    var d = WebCalendar.thisDay;
    var h = WebCalendar.thisHour;
    var mi = WebCalendar.thisMinute;
    var s = WebCalendar.thisSecond;
    WebCalendar.daysMonth[1] = (0==y%4 && (y%100!=0 || y%400==0)) ? 29 : 28;
    if (!(y<=9999 && y >= 1000 && parseInt(m, 10)>0 && parseInt(m, 10)<13 && parseInt(d, 10)>0)){
        alert("对不起，你输入了错误的日期！");
        WebCalendar.thisYear   = new Date().getFullYear();
        WebCalendar.thisMonth  = new Date().getMonth()+ 1;
        WebCalendar.thisDay    = new Date().getDate(); }
    y = WebCalendar.thisYear;
    m = WebCalendar.thisMonth;
    d = WebCalendar.thisDay;  	
    
 	//alert(WebCalendar.iframe.document.getElementById("meizzYearHead"));
 	//alert(WebCalendar.iframe.document.getElementById("meizzYearMonth"));
 	//alert(WebCalendar.iframe.document.getElementById("meizzYearHead").innerText);
 	//alert(WebCalendar.iframe.document.getElementById("meizzYearMonth").innerText);
    //WebCalendar.iframe.document.getElementById("meizzYearHead").innerText  = y +" 年";
    //WebCalendar.iframe.document.getElementById("meizzYearMonth").innerText = parseInt(m, 10) +" 月";
    WebCalendar.iframe.meizzYearHead.innerText  = y +" 年";
    WebCalendar.iframe.meizzYearMonth.innerText = parseInt(m, 10) +" 月";

    if(WebCalendar.display_type=="datetime"){
        WebCalendar.iframe.meizzHour.innerText = parseInt(h, 10) +" 时";
        WebCalendar.iframe.meizzMinute.innerText = parseInt(mi, 10) +" 分";
        WebCalendar.iframe.meizzSecond.innerText = parseInt(s, 10) +" 秒";
    }
    WebCalendar.daysMonth[1] = (0==y%4 && (y%100!=0 || y%400==0)) ? 29 : 28; //闰年二月为29天
    var w = new Date(y, m-1, 1).getDay();
    var prevDays = m==1  ? WebCalendar.daysMonth[11] : WebCalendar.daysMonth[m-2];
    for(var i=(w-1); i>=0; i--) //这三个 for 循环为日历赋数据源（数组 WebCalendar.day）格式是 d/m/yyyy
    {
        WebCalendar.day[i] = prevDays +"/"+ (parseInt(m, 10)-1) +"/"+ y;
        if(m==1) WebCalendar.day[i] = prevDays +"/"+ 12 +"/"+ (parseInt(y, 10)-1);
        prevDays--;
    }
    for(var i=1; i<=WebCalendar.daysMonth[m-1]; i++) WebCalendar.day[i+w-1] = i +"/"+ m +"/"+ y;
    for(var i=1; i<39-w-WebCalendar.daysMonth[m-1]+1; i++)
    {
        WebCalendar.day[WebCalendar.daysMonth[m-1]+w-1+i] = i +"/"+ (parseInt(m, 10)+1) +"/"+ y;
        if(m==12) WebCalendar.day[WebCalendar.daysMonth[m-1]+w-1+i] = i +"/"+ 1 +"/"+ (parseInt(y, 10)+1);
    }
  if(WebCalendar.display_type!="month"){
    for(var i=0; i<39; i++)    //这个循环是根据源数组写到日历里显示
    {
        var a = WebCalendar.day[i].split("/");
        WebCalendar.dayObj[i].innerText    = a[0];
        WebCalendar.dayObj[i].title        = a[2] +"-"+ appendZero(a[1]) +"-"+ appendZero(a[0]);
        WebCalendar.dayObj[i].bgColor      = WebCalendar.dayBgColor;
        WebCalendar.dayObj[i].style.color  = WebCalendar.wordColor;
        if ((i<10 && parseInt(WebCalendar.day[i], 10)>20) || (i>27 && parseInt(WebCalendar.day[i], 10)<12))
            WebCalendar.dayObj[i].style.color = WebCalendar.wordDark;
        if (WebCalendar.inputDate==WebCalendar.day[i])    //设置输入框里的日期在日历上的颜色
        {WebCalendar.dayObj[i].bgColor = WebCalendar.darkColor; WebCalendar.dayObj[i].style.color = WebCalendar.lightColor;}
        if (WebCalendar.day[i] == WebCalendar.today)      //设置今天在日历上反应出来的颜色
        {WebCalendar.dayObj[i].bgColor = WebCalendar.todayColor; WebCalendar.dayObj[i].style.color = WebCalendar.lightColor;}
    }
  }

}

function returnDate() //根据日期格式等返回用户选定的日期
{
    var displayMode = WebCalendar.display_type;
    if(WebCalendar.objExport)
    {
      var returnValue;
      var a ;
      var d;
      var flag;
      if (displayMode !="month"){
          a = (arguments.length==0) ? WebCalendar.day[this.id.substr(8)].split("/") : arguments[0].split("/");
          d = WebCalendar.format.match(/^(\w{4})(-|\/)(\w{1,2})\2(\w{1,2})$/);
          if(d==null){alert("你设定的日期输出格式不对！\r\n\r\n请重新定义 WebCalendar.format ！"); return false;}
          flag = d[3].length==2 || d[4].length==2; //判断返回的日期格式是否要补零
          returnValue = flag ? a[2] +d[2]+ appendZero(a[1]) +d[2]+ appendZero(a[0]) : a[2] +d[2]+ a[1] +d[2]+ a[0];
      }
      var inputYearObject;
      var inputMonthObject;
      var inputYearValue;
      var inputMonthValue;
      inputYearObject = WebCalendar.iframe.document.getElementById("meizzYearHead");
      inputYearValue = inputYearObject.innerText;
      inputYearValue = inputYearValue.substr(0,inputYearValue.length-2);

      inputMonthObject = WebCalendar.iframe.document.getElementById("meizzYearMonth");
      inputMonthValue = inputMonthObject.innerText;
      inputMonthValue = inputMonthValue.substr(0,inputMonthValue.length-2);
      var inputHourObject;
      var inputHourValue;
      var inputMinuteObject;
      var inputMinuteValue;
      var inputSecondObject;
      var inputSecondValue;
      if (displayMode =="datetime"){
         inputHourObject = WebCalendar.iframe.document.getElementById("meizzHour");
         inputHourValue = inputHourObject.innerText;
         inputHourValue = inputHourValue.substr(0,inputHourValue.length-2);

         inputMinuteObject = WebCalendar.iframe.document.getElementById("meizzMinute");
         inputMinuteValue = inputMinuteObject.innerText;
         inputMinuteValue = inputMinuteValue.substr(0,inputMinuteValue.length-2);

         inputSecondObject = WebCalendar.iframe.document.getElementById("meizzSecond");
         inputSecondValue = inputSecondObject.innerText;
         inputSecondValue = inputSecondValue.substr(0,inputSecondValue.length-2);
      }
        if(WebCalendar.timeShow)
        {
            var h = new Date().getHours(), m = new Date().getMinutes(), s = new Date().getSeconds();
            returnValue += flag ? " "+ appendZero(h) +":"+ appendZero(m) +":"+ appendZero(s) : " "+  h  +":"+ m +":"+ s;
        }
        /**show*///取得显示时间
        if (displayMode =="date"){
           returnValue = returnValue.trim().substr(0,10);//
        }
        if (displayMode =="datetime"){
           returnValue = returnValue.trim().substr(0,10)+" "+appendZero(inputHourValue)+":"+appendZero(inputMinuteValue)+":"+appendZero(inputSecondValue);
           //returnValue = returnValue.trim().substr(0,19);//
        }
        if (displayMode =="month"){
           returnValue = inputYearValue+"-"+appendZero(inputMonthValue);//
        }
        /**send to web server *///取得隐藏上传时间
        var destElementName = WebCalendar.objExport.name;
        
        /* ??????????????????  2006-10-26 */
        var destElementUniqueID = WebCalendar.objExport.uniqueID;					
        var destElementMasks = window.document.getElementsByName(destElementName);
        
        var index = 0;
        for (var i=0; i<destElementMasks.length; i++){        	
        	if (destElementUniqueID == destElementMasks[i].uniqueID){
        		index = i;
        		break;
        	}        	
        } //update by xuxf
        
        var delim = destElementName.indexOf("_MASK");
        if (delim > 0) {
           // “_MASK”以前的加“_MASK”以后的
           destElementName= destElementName.substring(0,delim ) + destElementName.substring(delim+5,destElementName.length);
         }
                  
        //var destElementId = window.document.getElementById(destElementName);  //2006-10-26
        var destElements = window.document.getElementsByName(destElementName);        
        var destElementId;
        if (destElements[index]){
        	destElementId = destElements[index];
        } else {
        	destElementId = window.document.getElementById(destElementName);
        }//end; xuxf
        
		var originalValue = destElementId.value;
        /**send to web server */
        if (displayMode =="date"){
           destElementId.value = returnValue+" 00:00:00.0";
           WebCalendar.objExport.value = returnValue;
        }
        if (displayMode =="datetime"){
           destElementId.value = returnValue+".0";
           WebCalendar.objExport.value = returnValue;
        }
        if (displayMode =="month"){
           destElementId.value = returnValue+"-01 00:00:00.0";
           WebCalendar.objExport.value = returnValue;
        }
        hiddenCalendar();
	//如果时间值发生变化，相应onchange事件
	if(originalValue!=destElementId.value){
	  destElementId.onchange();
	}
    }
}
document.onclick = function()
{
    if(WebCalendar.eventSrc != window.event.srcElement) hiddenCalendar();
}
//-->

/**
 * 选择户号 xuyu 6-6
 */
function selectUserNo(cell) {
  var url = new String(document.URL);
  var atI = url.indexOf("/j2yd/");
  if (atI > 0) {
     url = url.substring(0,atI+6);
  } else url="";
 url = url+"jsp/selectUserNo.jsp?CONTAINER="+cell.name;
 var move = screen ? ',left=' + ((screen.width - 730) >> 1) + ',top=' + ((screen.height - 550) >> 1) : '';
 window.open(url,"","width=730,height=550,top=0,left=0,menubar=no,scrollbars=yes,toolbar=no,resizable=yes,status=no"+move);
}
/**
 * 选择户号 yangufeng 6-6
 */
function go(action) {
  document.forms[0].action.value=action;
}

function goWithForm(curForm,action) {
  curForm.action.value=action;
}

function goWithButton(action) {
  document.forms[0].action.value=action;
  document.forms[0].submit();
}
/**
 *设置输入域只读
 */
function setReadonly(field){
if(field=="ALL"){
  for(i=0;i<document.forms[0].elements.length;i++){
        var element = document.forms[0].elements[i]
    if (element.type!="button"){
         element.readOnly="true"
        }
  }
}
else{
 document.forms[0].elements[field].readOnly="true"
 }

}

/**
 *设置输入域disabled
 */
function setDisabled(field){
if(field=="ALL"){
  for(i=0;i<document.forms[0].elements.length;i++){
        var element = document.forms[0].elements[i]
    if (element.type!="button"){
         element.disabled="true"
        }
  }
}
else{
 document.forms[0].elements[field].disabled="true"
 }

}


/**
*  打开 open treeview pop window
* phd
*/
function open_treeview_popwindow(location) {
  var window_width = 200;
  var window_height = 300;
  var window_left = (screen.availWidth/2)-(window_width/2);
  var window_top = (screen.availHeight/2)-(window_height/2);
  var windows_chrome  = "height="+window_height+
                                ",width="+window_width+
                                ",left="+window_left+
                                ",top="+window_top+
                                ",scrollbars=1"+
                                ",resizable=yes";
 tree_popwindow = window.open(location,"TreeView选择器",windows_chrome);
 tree_popwindow.focus();
}

/**
*  打开 open treeview pop window 后,选择返回数值
* phd
*/
 function setParameters(theCodeobjectname,theNameobjectname,code,name) {
       var theCodeElement = window.opener.document.getElementById(theCodeobjectname);
       var theNameElement = window.opener.document.getElementById(theNameobjectname);
       theNameElement.value = name;
       theCodeElement.value = code;
       window.close();
 }


/**
* 用于批量修改\删除界面
* phd
*/
function selectedliston(check_object){
  var checked_status = check_object.checked;
  //alert("check_object.value: "+check_object.value)
  if (checked_status == true) {
      check_object.value = 1;
   } else {
      check_object.value = 0;
   }
}
/** 用于CHECKBOX选中,赋数值 */
function checkSelected(check_object){
  check_object.value = check_object.checked;
  //alert("check_object.value: "+check_object.value)
}



/**
* 分页模式修改当前页码
* yangufeng 2003/7/30
*/
function getPage(page){
  var curPageNum = document.forms[0].elements['curPageNum'].value;
  var allPageNum = document.forms[0].elements['allPageNum'].value;
  if (allPageNum<=1) return ;
  switch(page){
    case "first":
        if(curPageNum==1)
          return ;
	else
	  curPageNum=1;
	  break;
    case "pre":
	if (curPageNum==1)
 	   curPageNum = allPageNum ;
	else
	   curPageNum--;
	   break;
    case "next":
        if (curPageNum==allPageNum)
	    curPageNum = 1 ;
	else
	    curPageNum++;
	    break;
    case "end":
	if (curPageNum==allPageNum)
	    return ;
	else
	   curPageNum= allPageNum;
	   break;
    case "page":
	var index = document.forms[0].elements['page'].selectedIndex;
            curPageNum =  document.forms[0].elements['page'].options[index].value;
 	break;
    default:
	return ;
    	break;
}
    document.forms[0].elements['curPageNum'].value = curPageNum;
    go('initForm');
    document.forms[0].submit();
}
/**
* 设置背景颜色
* yangufeng 2003/7/30
*/
function setPointer(target, mode){
	var oldbg = target.style.backgroundColor;
	var newbg = '#ccffcc';
	var clickbg = '#ffff80';
/*mode
click:鼠标点击时改变颜色
out：鼠标移走时改变颜色
over：鼠标移过时改变颜色
*/
    if (mode=="click"){
	if(oldbg!==clickbg)
		    target.style.backgroundColor=clickbg;
  	 else if (oldbg=="")
 		    target.style.backgroundColor=clickbg;
		else
           target.style.backgroundColor="";
		}

   if(mode=="out"){
       if(oldbg==newbg)
  	    target.style.backgroundColor="";
	   }
   if(mode=="over"){
		if(!oldbg==clickbg||oldbg=="")
	       target.style.backgroundColor=newbg;
  }

 }

/*
  打开一个通用打印窗口。
  djh 2003-8-23
*/
  function openprintcommwindow(templetname) {
     var retrieveUrl = "/j2yd/printer/printComm.jsp?templetName="+templetname;
     window.open(retrieveUrl,"printwindow","");
  }

/**
*lirui, add for second form pageing,2003-09-20
*/
function getPageWithForm(curForm,page){
  var curPageNum = curForm.elements['curPageNum'].value;
  var allPageNum = curForm.elements['allPageNum'].value;
  if (allPageNum<=1) return ;
  switch(page){
    case "first":
        if(curPageNum==1)
          return ;
	else
	  curPageNum=1;
	  break;
    case "pre":
	if (curPageNum==1)
 	   curPageNum = allPageNum ;
	else
	   curPageNum--;
	   break;
    case "next":
        if (curPageNum==allPageNum)
	    curPageNum = 1 ;
	else
	    curPageNum++;
	    break;
    case "end":
	if (curPageNum==allPageNum)
	    return ;
	else
	   curPageNum= allPageNum;
	   break;
    case "page":
	var index = curForm.elements['page'].selectedIndex;
            curPageNum =  curForm.elements['page'].options[index].value;
 	break;
    default:
	return ;
    	break;
}
    curForm.elements['curPageNum'].value = curPageNum;
    go('initForm');
    curForm.submit();
}

//通用排序：打开通用排序窗口
function opensortwindow(dsName,sortString) {
  var url = new String(document.URL);
  var atI = url.indexOf("/j2yd/");
  if (atI > 0) {
     url = url.substring(0,atI+6);
  } else url="";
  var retrieveUrl = new String("jsp/generalsort.jsp");
  retrieveUrl = url + retrieveUrl;
  retrieveUrl = retrieveUrl + "?dsName="+dsName+"&sortString="+sortString;
  var move = screen ? ',left=' + ((screen.width - 500) >> 1) + ',top=' + ((screen.height - 320) >> 1) : '';
  window.open(retrieveUrl,"","height=320,width=500,toolbar=no,menubar=no,location=no,status=no"+move);
}
//通用排序：选择列
function gs_selectColumn(line,idx,objName){
 var checkName = objName+"[" + idx + "].checked";
 var checkObject = document.all.item(checkName);
 if(checkObject.value=="1"){
   checkObject.value="0";
   setPointer(line,'out');
 }else{
  checkObject.value="1";
  setPointer(line,'over');
 }
}
//通用排序：可选列页面打开时调用
function gs_allcols_load(){
 var actionValue = document.forms[0].action.value;
 var selectedCols = document.forms[0].selectedCols.value;
 if(actionValue=="allColsAdd"){
   window.parent.sortColumnFrame.document.forms[0].selectedCols.value=selectedCols;
   window.parent.sortColumnFrame.document.forms[0].sm_addSortCols.click();
   document.forms[0].action.value = "";
 }
}
//通用排序：选中列页面打开时调用
function gs_sortcols_load(){
 var actionValue = document.forms[0].action.value;
 var selectedCols = document.forms[0].selectedCols.value;
 if(actionValue=="sortColsRemove"){
   window.parent.allColumnFrame.document.forms[0].selectedCols.value=selectedCols;
   window.parent.allColumnFrame.document.forms[0].sm_rmCols.click();
   document.forms[0].action.value = "";
 }
 if(actionValue=="applySort"){
   if (window.parent.opener && !window.parent.opener.closed) {
       window.parent.opener.document.forms[0].sortString.value = document.forms[0].sortString.value;
       window.parent.opener.document.forms[0].cb_sort.click();
     }
     window.parent.close();
 }
 if(actionValue=="moveUpColumn"||actionValue=="moveDownColumn"){

 }
}
//通用排序根据选中值设置背景色
function gs_settr(trName,idx){
 var checkName = "sortColsListIndex[" + idx + "].checked";
 var checkObject = document.all.item(checkName);
 var trObject = document.all.item(trName);
 if(checkObject.value=="1"){
   setPointer(trObject,'over');
 }
}
//帮助方式一:针对某个具体的功能,打开帮助文件窗口,如HTML
function open_help_win(helpUrl){
  var url = new String(document.URL);
  var atI = url.indexOf("/j2yd/");
  if (atI > 0) {
     url = url.substring(0,atI+6);
  }
  else url="";
  if(helpUrl ==null || helpUrl==""){
    url = url+"nohelp.jsp";
  }else{
    url = url+"help"+helpUrl;
  }
  var move = screen ? ',left=' + ((screen.width - 310)) + ',top=' + ((screen.height - 500) >> 1) : '';
  var help_win = window.open(url,"","height=500,width=300,toolbar=no,resizable=yes,menubar=no,location=no,status=no"+move);
  help_win.focus();
}

/**隐藏菜单树*/
function hideMenuTree(){
  parent.middleframe.oa_tree.onclick();
}
