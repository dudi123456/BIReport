
var strHTML =[];
    strHTML.push("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
    strHTML.push("<tr>");
    strHTML.push("<td align='right'>");
    strHTML.push("<img src='../images/wait.gif' border='0'></td>");
    strHTML.push("<td nowrap align='left'>&nbsp;&nbsp;&nbsp;&nbsp;正在处理，请稍候...</td>");
    strHTML.push("</tr></table>");

    //由于此时没有任何

function createPopup(){
	 $.blockUI({
		 message:strHTML.join(""),
         css: {
             //top:  ($(window).height() - 400) /2 + 'px',
             //left: ($(window).width() - 400) /2 + 'px',
             //height: '50px'
        	 //width : '400px'
         }
     });
}

function ShowWait(){
	createPopup();
}

function ShowWaitByNone(){
	createPopup();
}

function ShowWaitTrunByAction(formName,toPage,toTarget){
	createPopup();
}

function ShowWaitTrunByLocation(locat,toPage){
	createPopup();
}

function closeWaitWin(){
	$.unblockUI();
}
