

	//???????????"'"
	//??????????
	function listNull(obj)
	{
		if (obj.selectedIndex!=-1)
		{
				return true;
		}
		return false;
	}

	//??????????
	function selNull(obj)
	{
		if(obj.checked)
		{
			return true;
		}
		for(var i=0;i<obj.length;i++)
		{
			if(obj[i].checked)
			{
				return true;
			}
		}
		return false;
	}

	//???????????

	function textNull(obj)
	{
		if(obj.value.length<1)
		{
			return false;
		}
		return true;
	}
	
	//??????????????
	function checkDate(inputDate)
	{
		date=new Date();
		var month;
		if((date.getMonth()+1)<10)
		{
			month="0"+(date.getMonth()+1);
		}
		else
		{
			month=(date.getMonth()+1);
		}
		var day;
		if(date.getDate()<10)
		{
			day="0"+date.getDate();
		}
		else
		{
			day=date.getDate();
		}
		var str=date.getYear()+""+month+day;
		if(str>inputDate)
		{
			return false;
		}
		return true;
	}

	//???????????????

	function isNumbers(obj)
	{
		if(isNaN(obj.value))
		{
			return false;
		}
		return true;
	}	

	//????????
	function isDates(obj)
	{
		if(obj.value.length!=19)
		{
			return false;
		}
		if(obj.value.substring(4,5)!="-")
		{
			return false;
		}
		if(obj.value.substring(7,8)!="-")
		{
			return false;
		}
		if(obj.value.substring(10,11)!=" ")
		{
			return false;
		}
		if(obj.value.substring(13,14)!=":")
		{
			return false;
		}
		if(obj.value.substring(16,17)!=":")
		{
			return false;
		}
		var year=obj.value.substring(0,4);
		var month=obj.value.substring(5,7);
		var day=obj.value.substring(8,10);
		var hour=obj.value.substring(11,13);
		var min=obj.value.substring(14,16);
		var sec=obj.value.substring(17,19);
		date=new Date();
		if(isNumbers(year) | isNumbers(month) | isNumbers(day) | isNumbers(hour) | isNumbers(min) | isNumbers(sec))
		{
			return false;
		}
		if(month>12 | day>31 | hour>24 | min>59 | sec>59)
		{
			return false;
		}
		var flag=false;
		if(year%400==0)
		{
			flag=true;
		}
		if(year%100!=0 || year%4==0)
		{
			flag=true;
		}
		switch(parseInt(month))
		{
			case 2:
				if(day>29)
				{
					return false;
				}
				if(flag)
				{	
					if(day>28)
					{
						return false;
					}
				}				
				break;
			case 4:
				if(day==31)
				{
					return false;
				}
				break;
			case 6:
				if(day==31)
				{
					return false;
				}
				break;
			case 9:
				if(day==31)
				{
					return false;
				}
				break;
			case 11:
				if(day==31)
				{
					return false;
				}
				break;
		}
		return true;
	}
	
	//Trim??
	function trim(strValue) {
		 var intLen = strValue.length;
		 var intCnt;
		 for (intCnt = 0; intCnt < intLen; intCnt++) {
			 if (strValue.indexOf(' ') == 0) {//????
			 	strValue=strValue.substring(1, intLen);
			 }else if (strValue.indexOf(' ') == 0) {//????
			 	strValue=strValue.substring(1, intLen);
			 }else if (strValue.indexOf(' ') == 0) { //TAB
			 	strValue = strValue.substring(1, intLen);
			 }else {
			 	break;
			 }
		 }
		 intLen = strValue.length;
		 for (intCnt = intLen; intCnt > 0; intCnt--) {
		 	if (strValue.lastIndexOf(' ') == intCnt - 1 ) {//????
		   		strValue = strValue.substring(0, intCnt - 1 );
		  	}else if (strValue.lastIndexOf(' ') == intCnt - 1) {//????
		   		strValue = strValue.substring(0, intCnt - 1 );
		  	}else if (strValue.lastIndexOf(' ') == intCnt - 1) { //TAB
		   		strValue = strValue.substring(0, intCnt - 1 );
		  	}else {
		   		break;
		  	}
		 	intLen = strValue.length;
		}
		return strValue;
	}
	
	//?????????n?????

	//???????yyyy-mm-dd
	function calToDate(strDate, n){
		
		var start = new Date(strDate.substring(0,4),strDate.substring(5,7)-1,strDate.substring(8,10));
		var end = new Date(start.getYear(),start.getMonth() + n,start.getDate());

		endYear = (end.getYear()<1000 ? end.getYear()+1900 : end.getYear());
		endMonth = (end.getMonth()+1)<10? "0"+(end.getMonth()+1):(end.getMonth()+1);
		endDate = (end.getDate())<10?"0"+end.getDate():end.getDate();		
		
		return ( endYear + "-" + endMonth + "-" +endDate );	
	}
	
	
	/* ?????????

	 * ??????yyyy-mm-dd?????

	 * 
	 * ??:?dateOne <= dateTwo ??true; ????false
	 */
	function compareDate(dateOne, dateTwo)	{
	
		var oneMonth = dateOne.substring(5,dateOne.lastIndexOf ("-"));
		var oneDay = dateOne.substring(dateOne.length,dateOne.lastIndexOf ("-")+1);
		var oneYear = dateOne.substring(0,dateOne.indexOf ("-"));
		
		var twoMonth = dateTwo.substring(5,dateTwo.lastIndexOf ("-"));
		var twoDay = dateTwo.substring(dateTwo.length,dateTwo.lastIndexOf ("-")+1);
		var twoYear = dateTwo.substring(0,dateTwo.indexOf ("-"));
		
		if (Date.parse(oneMonth+"/"+oneDay+"/"+oneYear) < Date.parse(twoMonth+"/"+twoDay+"/"+twoYear)){
			return true;
		}else{
			return false;
		}
	}		
	
	//?????

	// ???srcStr -- ???????

	//		 nAfterDot -- ?????????


	function formatNumber(srcStr,nAfterDot){
		var resultStr="";
		var nTen = 1 ;
		var nLength = 0;	//?????

		var dotPos = 0;		//???????

		
		var srcString = ""+ srcStr + "";		
		nLength = srcString.length;
		dotPos = srcString.indexOf(".",0);

		if (dotPos == -1){
			resultStr = srcString + ".";
			for (i=0;i<nAfterDot;i++){
				resultStr = resultStr+"0";
			}
			return resultStr;
		} else {
			if ((nLength - dotPos - 1) >= nAfterDot){
				nAfter = dotPos + nAfterDot + 1;
				//nTen =1;
				for(j=0;j<nAfterDot;j++){
					nTen = nTen*10;
				}
				resultStr = Math.round(parseFloat(srcString)*nTen)/nTen;
				return resultStr;
			} else {
				resultStr = srcString;
				for (i=0;i<(nAfterDot - nLength + dotPos + 1);i++){
					resultStr = resultStr+"0";
				}
				return resultStr;
			}
		}
	}
	/* 
	* ?????

	* ???srcStr -- ???????

	*		nAfterDot -- ?????????

	*		?????????? nAfterDot, ????????
	* 		
	*/

	function formatNumber2(srcStr,nAfterDot){
		var resultStr="";
		var nTen = 1 ;
		var nLength = 0;	//?????

		var dotPos = 0;		//???????

		
		var srcString = ""+ srcStr + "";		
		nLength = srcString.length;
		dotPos = srcString.indexOf(".",0);

		if (dotPos == -1){
			resultStr = srcString;
			return resultStr;
		} else {
			if ((nLength - dotPos - 1) >= nAfterDot){
				nAfter = dotPos + nAfterDot + 1;
				//nTen =1;
				for(j=0;j<nAfterDot;j++){
					nTen = nTen*10;
				}
				resultStr = Math.round(parseFloat(srcString)*nTen)/nTen;
				return resultStr;
			} else {
				resultStr = srcString;
				return resultStr;
			}
		}
	}
	
	
	/*
	*	???????????

	* 	????0
	*   ?????NaN
	*/
	function getTextValue( obj){
		
		if(obj == null || isNaN(obj.value)){
			return NaN;		
		} else if (obj.value==""){
			return "0";
		} else{
			return obj.value;
		}
	}
/**
*???????????????

*??
*/
function moveCur(){
	key=window.event.keyCode;
	var obj = event.srcElement;
	//?????

	var colElements = document.forms[0].all(obj.name);
	//?????

	var rowElements = document.forms[0].all(obj.id);	
	var col_Ele_len = colElements.length;
	var row_Ele_len = rowElements.length;	
	var m = 0;
	var n = 0;
	for (n=0;n<col_Ele_len;n++){
		if(colElements[n].type == "hidden"||colElements[n].readOnly||colElements[n].tagName!="INPUT"){
			continue;		
		}
		if(obj==colElements[n]){
			break;
		}
	}
	for (m=0;m<row_Ele_len;m++){
		if(rowElements[m].type == "hidden"||rowElements[m].readOnly||rowElements[m].tagName!="INPUT"){
			continue;		
		}
		if(obj==rowElements[m]){
			break;
		}
	}
	//???

	if (key==37){
		for(var i=m;i>=0; i--){
			if(i==0){
				return;
			}
			if (rowElements[i-1].type == "hidden"||rowElements[i-1].readOnly||rowElements[i-1].tagName!="INPUT"){
				 continue;
			}else{	
				rowElements[i-1].select();
				break;
			}
		}
	}
	//???

	if (key==39){
		for(var i=m;i<=row_Ele_len; i++){
			if(i==row_Ele_len){
				return;
			}
			if (rowElements[i+1].type == "hidden"||rowElements[i+1].readOnly||rowElements[i+1].tagName!="INPUT"){
				 continue;
			}else{
				rowElements[i+1].select();
				break;
			}
		}
	}
	//???

	if (key==38){
		for(var i=n;i>=0; i--){
			if(i==0){
				return;
			}
			if (colElements[i-1].type == "hidden"||colElements[i-1].readOnly||colElements[i-1].tagName!="INPUT"){
				 continue;
			}else{			

				colElements[i-1].select();
				break;
			}
		}
	}
	//???

	if (key==40){
		for(var i=n;i<=col_Ele_len; i++){
			if(i==col_Ele_len-1){
				return;
			}
			if (colElements[i+1].type == "hidden"||colElements[i+1].readOnly||colElements[i+1].tagName!="INPUT"){
					 continue;
			}else{
				colElements[i+1].select();
				break;
			}
		}
	}
}		
	/**
     * Description : ???????
     * @param URL
     * @return  
     */
	function goPage(URL) {
		document.location.href=URL;
	}  			

	/**
     * Description : ?????:yyyy-mm-dd?
     * @param year,month,day
     * @return  
     */	
	function getCurrDate(year,month,day) {
		var mth = parseInt(month);
		var dt = parseInt(day);
		if(mth < 10) {
			mth = "0"+month;
		}
		if(day == ""){
			return year+"-"+mth;
		}
		if(dt < 10) {
			dt = "0"+day;
		}	
		return year+"-"+mth+"-"+dt;
	}

	/**
     * Description : 计算日期为当年的第几周
     * @param year,month,day
     * @return  
     */	
	  function weekOfYear(year, month, day){

		  // 每周从周日开始
		  var date1 = new Date(year, 0, 1);
		  var date2 = new Date(year, month-1, day, 1);
		  var dayMS = 24*60*60*1000;
		  var firstDay = (7-date1.getDay())*dayMS;
		  var weekMS = 7*dayMS;
		  date1 = date1.getTime();
		  date2 = date2.getTime();
		  return Math.ceil((date2-date1-firstDay)/weekMS)+1;
	  }

	/**
     * Description : ???
     * @param month
     * @return  
     */		
	function getCurrQuarter(month) {
	  var quar = parseInt(month);

	  if(1 >= quar || quar <= 3) {
	  	quar = 1;
	  }else if(4 >= quar || quar <= 6) {
	  	quar = 2;
	  }else if(7 >= quar || quar <= 9) {
	  	quar = 3;
	  }else if(10 >= quar || quar <= 12) {
	  	quar = 4;
	  }
	  return quar;
	}	