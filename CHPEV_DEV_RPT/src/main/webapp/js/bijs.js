//(鼠标移上移出时)切换样式
function switchClass(theObj)
{
    if (theObj.className == undefined) {
        theObj = event.srcElement;
    }
    if (theObj.className.indexOf("-over") < 0)
    {
        theObj.className = theObj.className + "-over";
    }
    else
    {
        theObj.className = theObj.className.replace("-over", "");
    }
}


//(鼠标移上移出时)切换源，如切换图片img
function switchSrc(theObj)
{
    if (theObj.src.indexOf("_over") < 0)
    {
        srcMain = theObj.src.substr(0, theObj.src.lastIndexOf("."));
        srcPostfix = theObj.src.substr(theObj.src.lastIndexOf("."));
        theObj.src = srcMain + "_over" + srcPostfix;
    }
    else
    {
        theObj.src = theObj.src.replace("_over", "");
    }
}

//隐藏或显示leftFrame
function switchLeftFrame()
{
    if (leftFrameSwitcher.src.indexOf("left") > -1)
    {
        leftFrameSwitcher.src = "../biimages/home/toright.gif";
        parent.bodyFrameset.cols = "5,*";
    }
    else
    {
        leftFrameSwitcher.src = "../biimages/home/toleft.gif";
        parent.bodyFrameset.cols = "185,*";
    }
}


//title菜单
function buildTaberArea(theTaberSpanID, theActivedTaberId, theTaberArray)
{
    var theTaberSpanTemp = "";
    var theTaberSpan = document.getElementById(theTaberSpanID);
    for (var i = 1; i < theTaberArray.length; i++){
    		if(i==1){
    			theTaberSpanTemp += '<li class="active"><a href="javascript:" onclick="javascript:switchTab('+i+')">' + theTaberArray[i][1] + '</a></li>';
    		}else{
    			theTaberSpanTemp += '<li><a href="javascript:" onclick="javascript:switchTab('+i+')">' + theTaberArray[i][1] + '</a></li>';
    		}

    }

    theTaberSpan.innerHTML = theTaberSpanTemp;
}



function switchHead()
	{
		if(HeadSwitcher.src.indexOf('hide')>-1)
		{
			HeadSwitcher.src="../biimages/home/top_showhead.jpg";
			HeadTable.style.display="none";
			parent.frame1.rows="30,*,20";
		}
		else
		{
			HeadSwitcher.src="../biimages/home/top_hidehead.jpg";
			HeadTable.style.display="";
			parent.frame1.rows="95,*,20";
		}
	}



//生成标签
function buildTaber(theActivedId)
{
    var taberInnerHTMLTemp = '';

    taberInnerHTMLTemp += '<table width="100%" border="0" cellpadding="0" cellspacing="0"';
    taberInnerHTMLTemp += '          <tr> ';
    taberInnerHTMLTemp += '          <td valign="bottom"><img src="../biimages/taber/tab_line.gif"></td>';

    for (i = 1; i < taberArr.length; i++)
    {
        if (i > 1)
        {
            taberInnerHTMLTemp += '            <td><img src="../biimages/size.gif" width="1"></td>';
        }
        if (taberArr[i][0] == theActivedId)
        {
            if (i != 1)taberInnerHTMLTemp += '            <td></td>';
            taberInnerHTMLTemp += '            <td><img src="../biimages/taber/tab_left_on.gif"></td>';
            taberInnerHTMLTemp += '            <td nowrap background="../biimages/taber/tab_bg_on.gif" class="tab-button-on" onClick="buildTaber(\'' + taberArr[i][0] + '\')">' + taberArr[i][1] + '</td>';
            taberInnerHTMLTemp += '            <td><img src="../biimages/taber/tab_right_on.gif"></td>';
            eval(taberArr[i][3]).location.href = taberArr[i][2];
        }
        else
        {
            if (i != 1)taberInnerHTMLTemp += '            <td></td>';
            taberInnerHTMLTemp += '            <td><img src="../biimages/taber/tab_left_off.gif"></td>';
            taberInnerHTMLTemp += '            <td nowrap background="../biimages/taber/tab_bg_off.gif" class="tab-button-off" onMouseOver="this.className=\'tab-button-off-over\'" onMouseOut="this.className=\'tab-button-off\'" onClick="buildTaber(\'' + taberArr[i][0] + '\')">' + taberArr[i][1] + '</td>';
            taberInnerHTMLTemp += '            <td><img src="../biimages/taber/tab_right_off.gif"></td>';
        }
    }

    taberInnerHTMLTemp += '          <td valign="bottom"><img src="../biimages/taber/tab_right_line.gif"></td>';
    taberInnerHTMLTemp += '          </tr>';
    taberInnerHTMLTemp += '        </table>';
    taber.innerHTML = taberInnerHTMLTemp;
}