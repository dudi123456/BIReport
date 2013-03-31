  var oPopup = window.createPopup();
  function popshow(){
     var iwidth=300 ;
     var iheight=150 ;
     var ileft=(screen.width-iwidth)/2;
     var itop=(screen.height-iheight)/2;   
     oPopup.show(ileft,itop, iwidth, iheight);  
     setTimeout("hiddenWin()",10000);  
  }
  function hiddenWin(){   
      oPopup.hide();  
  }

  function popmsg(title,content,contextPath){
    var strHTML ="";
    strHTML +='<html>\n';
    strHTML +='<head>\n';
    strHTML +='<meta http-equiv="Content-Type" content="text/html; charset=utf-8">\n';
    strHTML +='<title>重庆联通统一经营分析系统</title>\n';
    strHTML +='<link href="'+contextPath+'/css/css.css" rel="stylesheet" type="text/css">\n';
    strHTML +='</head>\n';
    strHTML +='<body>\n';
    strHTML += '<table height="100%" width="100%" border="0" cellpadding="0" cellspacing="0">\n';
    strHTML += '<tr>\n';
    strHTML += '<td width="7">';
    strHTML += '<img src="'+contextPath+'/biimages/ts/tab_corner_1.gif" ';
    strHTML += 'width="7" height="22"></td>';
    strHTML += '<td width="20" valign="bottom" ';
    strHTML += 'background="'+contextPath+'/biimages/ts/tab_corner_bg.gif">';
    strHTML += '<img src="'+contextPath+'/biimages/info.gif" width="14" height="16"></td>';
    strHTML += '<td width="100%" align="center" ';
    strHTML += 'valign="bottom" background="'+contextPath+'/biimages/ts/tab_corner_bg.gif" ';
    strHTML += 'style="padding:3px 0px 0px 5px;font-size:12px;color:#4D575E;">';
    strHTML += '<B>'+title+'</B></td>';
    strHTML += '<td width="96" background="'+contextPath+'/biimages/ts/tab_corner_bg.gif">';
    strHTML += '<a href=\"#\" onClick="parent.hiddenWin();">';
    strHTML += '<img src="'+contextPath+'/biimages/info_close.gif" width="13" ';
    strHTML += 'height="13" border="0"></a></td>';
    strHTML += '<td width="10" align="right" ';
    strHTML += 'background="'+contextPath+'/biimages/ts/tab_corner_bg.gif">';
    strHTML += '<img src="'+contextPath+'/biimages/ts/tab_corner_5.gif" ';
    strHTML += 'width="7" height="22"></td>';
    strHTML += '</tr>';
    strHTML += '<tr>';
    strHTML += '<td height="100%" align="center" ';
    strHTML += 'style="border-right-width: 1px;border-bottom-width: ';
    strHTML += '1px;border-left-width: 1px;border-right-style: ';
    strHTML += 'solid;border-bottom-style: solid;border-left-style: ';
    strHTML += 'solid;border-right-color: #D66B6C;border-bottom-color: ';
    strHTML += '#D66B6C;border-left-color: #D66B6C;background-color: ';
    strHTML += '#FFFFFF;" colspan="5">';
    strHTML += '<span style="color:#757575;font-size:12px;"><br>';
    strHTML += '<br><br>'+content+'<br><br>';
    strHTML += '</span></td>';    
    strHTML += '</tr></table>';
    strHTML += '</body></html>';
    oPopup.document.body.innerHTML = strHTML;
    popshow();
  }
