//===============================================
//	检查相应数据类型的合法性
//	oform----表单
//	flag-----为空标志，1表示不能为空
//	name-----元素变量名
//	disp-----元素名
//==============================================
function _checkCharData(oform,flag,name,disp){
	var obj=oform.elements[name];
	if(flag=="1"){
		if(obj.value==""){
			alert(disp+" 不能为空");
			obj.focus();
			return false;
		}
	}

	if(obj.disable!=true && obj.type!='text')
		return true;
	var data=jsTrim(obj.value);
	var r,re;
	
	if (data.indexOf("'") >= 0)  {
		alert(disp+" 中不能输入'");
		obj.focus();
		return false;
	}
		
        re=new RegExp("[^0-9a-zA-Z_()-]","g");
        r=data.match(re);
        if(r!=null )
        {
		alert(disp+" 输入了非法字符");
		obj.focus();
		return false;
	}

	return true;
}
	
function _checkIntData(oform,flag,name,disp){
	var obj=oform.elements[name];
	
	if(obj.value==""){
		if(flag=="1"){
			alert(disp+" 不能为空");
			obj.focus();
			return false;
		}else{
			return true;
		}
	}
	if(obj.type!='text')
		return true;
	var data=jsTrim(obj.value);
	var r,re;
    re=new RegExp("[^0-9]","g");
    r=data.match(re);
    if(r!=null){
		alert(disp+" 输入了非法字符，只能输入整数。");
		obj.focus();
		return false;
	}
	if(data.length>1 && data.substr(0,1)=='0'){
		alert(disp+" 输入的不是有效的整数");
		obj.focus();
		return false;
	}
	if(isNaN(parseInt(data))){
		alert(disp+" 输入的不是有效的整数");
		obj.focus();
		return false;
	}

	return true;
}

function _checkFloatData(oform,flag,name,disp){
	var obj=oform.elements[name];
	if(flag=="1"){
		if(obj.value==""){
		alert(disp+" 不能为空");
		obj.focus();
		return false;
		}
	}
	if(obj.type!='text')
		return true;
	var data=jsTrim(obj.value);
	var r,re;
        re=new RegExp("[^0-9.]","g");
        r=data.match(re);
        if(r!=null){
		alert(disp+" 输入了非法字符，只能输入浮点数(整数或小数)");
		obj.focus();
		return false;
	}
	if(isNaN(parseFloat(data))){
		alert(disp+" 输入的不是有效的浮点数");
		obj.focus();
		return false;
	}
	if(data.length>1){
		if(data.substring(0,1)=="0" && data.substring(1,2)!="."){
			alert(disp+ " 输入不合法");
			return false;
		}
		}
	if(data.indexOf(".")>0 &&(data.length-(data.indexOf(".")))>3){
		alert(disp+" 最多只能输入2位小数");
		return false;
	}
	return true;
}
   	
function _checkNotNull(oform,name,disp) {
	var obj=oform.elements[name];
	if (jsTrim(obj.value) == "") {
		alert(disp + " 不能为空！");
		obj.focus();
		return false;
	}
	return true;
}

function _checkMaxLength(oform,name,maxLen,disp) {
	var obj=oform.elements[name];
	alert(maxLen);
	alert(checkStrLen(obj.value));
	if (checkStrLen(obj.value) > maxLen) {
		alert(disp + " 输入的字符长度不能超过128个字符！（一个汉字2个字符长）");
		obj.focus();
		return false;
	}
	return true;	
}

//=============================================
//Purpose: Trim left spaces
//=============================================
function jsLTrim(str){
	var rtnStr;
	rtnStr=""
	for (var i=0;i<str.length;i++){
		if (str.charAt(i)!=" "){
			rtnStr=str.substr(i);
			break;
		}
	}
	return rtnStr;
}

//==========================================
//Purpose: Trim right spaces
//==========================================
function jsRTrim(str){
	var rtnStr;
	rtnStr=""
	for (var i=str.length-1;i>=0;i--){
		if (str.charAt(i)!=" "){
			rtnStr=str.substring(0,i+1);
			break;
		}
	}
	return rtnStr;
}

//==========================================
//Purpose: Trim both left and right spaces
//==========================================
function jsTrim(str){
	return(jsLTrim(jsRTrim(str)));
}


//检查字符串的真实长度（一个汉字相当于2个字符长）
function checkStrLen(value){
	var str,Num = 0;
	for (var i=0;i<value.length;i++){
		str = value.substring(i,i+1);
		if (str<="~") //判断是否双字节
			Num+=1;
		else
			Num+=2;
	}
	return Num;
}

//判断字符串中是否包含汉字
function checkCNInStr(value) {
	for (var i=0;i<value.length;i++){
		str = value.substring(i,i+1);
		if (str>"~") //判断是否双字节
			return true;
	}
	return false;
}

function strReplace(inStr,old,newr){
	var re,tmp;
	re=new RegExp("["+old+"]","g");
	tmp=inStr.replace(re,newr);
	return tmp;
}

/**************************************************************
函数名称：SpecialString
处理机能: 用于判断一个字符串是否含有或不含有某些字符
返回值：true或false
参数：
string ： 需要判断的字符串
compare ： 比较的字符串(基准字符串)
BelongOrNot： true或false，“true”表示string的每一个字符都包含在compare中，
“false”表示string的每一个字符都不包含在compare中
**************************************************************/
function SpecialString(string,compare,BelongOrNot)
{
	if ((string==null) || (compare==null) || ((BelongOrNot!=null) && (BelongOrNot!=true) && (BelongOrNot!=false)))
	{
		alert("function SpecialString(string,compare,BelongOrNot)参数错误");
		return false;
	}
	if (BelongOrNot==null || BelongOrNot==true)
	{
		for (var i=0;i<string.length;i++)
		{
			if (compare.indexOf(string.charAt(i))==-1)
				return false;
		}
		return true;
	}
	else
	{
		for (var i=0;i<string.length;i++)
		{
			if (compare.indexOf(string.charAt(i))!=-1)
				return false;
		}
		return true;
	}
}