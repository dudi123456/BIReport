var m_wndWait4Me=window.createPopup();
var m_PopupDisplayFlag=true;
var m_message = "正在处理数据，请稍等";
function showWait4MeWindow(message)
{
	var processBarDisplayFlag = "true";
	if (document.all.processBarDisplayFlag != undefined)
		processBarDisplayFlag = document.all.processBarDisplayFlag.value;
	if(m_PopupDisplayFlag==false || processBarDisplayFlag == 'false')
	{
		m_wndWait4Me.hide();
		return;
	}
	var nWidth=300;
	var strHtml ="";
	
	//方案1
	strHtml+="<table border=1 cellpadding=0 style='border-collapse: collapse' cellspacing=0 width=300 bordercolorlight='#C0C0C0' bordercolordark='#666699'>";
	strHtml+="<tr><td width=100% >";
	strHtml+="<table border=1 cellpadding=0 style='border-collapse: collapse' cellspacing=0 width=100% bordercolorlight='#FFFFFF' bordercolordark='#666699'>";
	strHtml+="<tr><td width=100% >";
	strHtml+="<table border='0' width=100% style='border-collapse: collapse' cellpadding=0 cellspacing=0 height=31>";
	strHtml+="<tr><td width='100%' bgcolor='#000080' height='18'><b><font color='#FFFFFF' size='2'>" + m_message + "</font></b></td></tr>";
	strHtml+="<tr><td width='100%' bgcolor='menu' height='13'>　</td></tr> ";
	strHtml+="<tr> <td align='center' width='100%' bgcolor='menu' height='23'>";
	strHtml+="<marquee style='border:1px solid #6699CC' direction='right' width='280' scrollamount='5' scrolldelay='10' bgcolor='#ECF2FF'>";
  	strHtml+="<table cellspacing='1' cellpadding='0'><tr height=8><td bgcolor=#3399FF width=8></td><td></td><td bgcolor=#3399FF width=8></td><td></td><td bgcolor=#3399FF width=8></td><td></td><td bgcolor=#3399FF width=8></td><td></td></tr></table></marquee>";
	strHtml+="</tr>";
	strHtml+="<tr><td width='100%' bgcolor='menu' height='13'>　</td></tr> ";
	strHtml+="</table>";
	strHtml+="</td></tr></table></td></tr></table>";
	
	//方案2
	//strHtml+="<table border=1 cellpadding=0 style='border-collapse: collapse' cellspacing=0 width=300 bordercolorlight='#C0C0C0' bordercolordark='#666699'>";
	//strHtml+="<tr><td width=100% >";
	//strHtml+="<table border=1 cellpadding=0 style='border-collapse: collapse' cellspacing=0 width=100% bordercolorlight='#FFFFFF' bordercolordark='#666699'>";
	//strHtml+="<tr><td width=100% >";
	//strHtml+="<table border='0' width=100% style='border-collapse: collapse' cellpadding=0 cellspacing=0 height=31>";
	//strHtml+="<tr><td width='100%' bgcolor='#000080' height='18'><b><font color='#FFFFFF' size='2'>正在处理数据，请稍等</font></b></td></tr>";
	//strHtml+="<tr><td width='100%' bgcolor='menu' height='13'>　</td></tr> ";
	//strHtml+="<tr> <td width='100%' bgcolor='menu' height='13'> <font size=2><p align='left' id=TIMEA>&nbsp&nbsp请稍等  "+message+"</font></td> </tr>";
	//strHtml+="<tr><td width='100%' bgcolor='menu' height='13'>　</td></tr> ";
	//strHtml+="</table>";
	//strHtml+="</td></tr></table></td></tr></table>";
	
	//方案3
	//strHtml = "<table width='300' border=\"0\" cellspacing=\"0\" cellpadding=\"0\" id=\"loaderContainer\" onClick=\"return false;\"><tr><td id=\"loaderContainerWH\"><div id=\"loader\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style='font-size:9pt'><tr><td><p><img src=\"/images/loading.gif\" height=\"32\" width=\"32\" alt=\"\"/><strong>请稍候。<br>正在加载…</strong></p></td></tr></table></div></td></tr></table>";
	
	m_wndWait4Me.document.body.innerHTML = strHtml; 
	var popupBody = m_wndWait4Me.document.body;
  m_wndWait4Me.show(0, 0,nWidth , 0);
  var realHeight = popupBody.scrollHeight;
  m_wndWait4Me.hide();
  m_wndWait4Me.show(280,200,nWidth,realHeight,document.body);
}
//showWait4MeWindow("123");

var bar = 0 
var line = "|" 
var amount ="" 
//count() ;

function count(){ 
	bar= bar+2 
	amount =amount + line 
	//document.loading.chart.value=amount 
	if (bar<35) 
	{
	} 
	else 
	{
		bar=0;
		amount="";
	} 
	showWait4MeWindow(amount);
	setTimeout("count()",500);
} 


function showProcessBar(){
	m_PopupDisplayFlag=true;
	document.all.processBarDisplayFlag.value = "true";
	count();
}

function showProcessBar2(msg){
	m_message = msg;
	showProcessBar();
}

function showStaticProcessBar() {
	m_PopupDisplayFlag=true;
	showWait4MeWindow("");
}

function showStaticProcessBar2(msg) {
	m_message = msg;
	showStaticProcessBar();
}

function stopStaticProcessBar() {
	m_PopupDisplayFlag=false;
	showWait4MeWindow("");
}