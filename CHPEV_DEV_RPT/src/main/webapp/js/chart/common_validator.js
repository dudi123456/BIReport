 /***********************************************
	程序说明：公共校验程序，提供20种表单校验
	1.是否为空；
	2.中文字符；
	3.双字节字符
	4.英文；
	5.数字；
	6.整数；
	7.实数；
	8.Email；
	9.使用HTTP协议的网址；
	10.电话号码；
	11.货币；
	12.手机号码；
	13.邮政编码；
	14.身份证号码；
	15.IP地址；
	16.日期；
	17.符合安全规则的密码；
	18.某项的重复值；
	19.两数的关系比较；
	20.判断输入值是否在(n, m)区间；
	21.输入字符长度限制(可按字节比较)；
	22.对于具有相同名称的单选按钮的选中判断；
	23.限制具有相同名称的多选按钮的选中数目；
	24.自定义的正则表达式验证；
*************************************************/

 Validator = {
	Require : /.+/,	
	Integer : "this.integerCheck(value,getAttribute('nullable'),getAttribute('digit'))",
	Number : "this.numberCheck(value,getAttribute('nullable'),getAttribute('digit'))",
	Double : "this.doubleCheck(value,getAttribute('nullable'),getAttribute('digit'))",
	Chinese : "this.chineseCheck(value,getAttribute('nullable'))",
	English : "this.englishCheck(value,getAttribute('nullable'))",
	Email : "this.mailCheck(value,getAttribute('nullable'))",
	Phone : "this.phoneCheck(value,getAttribute('nullable'))",
	URL : "this.urlCheck(value,getAttribute('nullable'))",
	IdCard : "this.idcardCheck(value,getAttribute('nullable'))",
	Currency : "this.currencyCheck(value,getAttribute('nullable'))",
	Zip : "this.zipCheck(value,getAttribute('nullable'))",
	Limit : "this.limitCheck(value.length,getAttribute('min'),getAttribute('max'),getAttribute('nullable'))",
	LimitB : "this.limitCheck(this.LenB(value), getAttribute('min'), getAttribute('max'),getAttribute('nullable'))",
	Date : "this.dateCheck(value,getAttribute('format'))",
	Repeat : "value == document.getElementsByName(getAttribute('to'))[0].value",
	Range : "this.rangeCheck(value,getAttribute('min'),getAttribute('max'))",
	Compare : "this.compareCheck(value,getAttribute('operator'),getAttribute('to'))",
	Custom : "this.Exec(value, getAttribute('regexp'))",
	Group : "this.groupCheck(getAttribute('name'), getAttribute('min'), getAttribute('max'))",
	Ip : "this.ipCheck(value,getAttribute('nullable'))",
	Mobile :"this.mobileCheck(value,getAttribute('nullable'),getAttribute('nullable'))",
	
	ErrorItem : [document.forms[0]],
	ErrorMessage : ["以下原因导致提交失败：\t\t\t\t"],
	
	Validate : function(theForm, mode){
		var obj = theForm || event.srcElement;
		var count =0;

		if(obj!=null && obj.elements!=undefined){
			count = obj.elements.length;
		}

		this.ErrorMessage.length = 1;
		this.ErrorItem.length = 1;
		this.ErrorItem[0] = obj;
		for(var i=0;i<count;i++){
			with(obj.elements[i]){
				var _dataType = getAttribute("dataType");
				if(typeof(_dataType) == "object" || typeof(this[_dataType]) == "undefined")  continue;
				this.ClearState(obj.elements[i]);
				if(getAttribute("require") == "false" && value == "") continue;
				
				//alert(_dataType);
				switch(_dataType){
					case "Integer":
					case "Double":
					case "Number":
					case "Currency":
					case "IdCard":
					case "URL":
					case "Phone":
					case "Email":
					case "Zip":
					case "Chinese":
					case "English" :
					case "Ip" :
					case "Mobile" :
					case "Date" :
					case "Repeat" :
					case "Range" :
					case "Compare" :
					case "Custom" :
					case "Group" : 
					case "Limit" :
					case "LimitB" :
					
					if(!eval(this[_dataType]))	{
						this.AddError(i, getAttribute("msg"));
					}
						break;
					default :
						if(!this[_dataType].test(value)){
							this.AddError(i, getAttribute("msg"));
						}
						break;
				}
			}
		}
		
		//错误信息输出
		if(this.ErrorMessage.length > 1){
			mode = mode || 2;
			var errCount = this.ErrorItem.length;
			switch(mode){
			case 1 :
				alert(this.ErrorMessage.join("\n"));
				this.ErrorItem[1].focus();
				break;
			case 2 :
				for(var i=1;i<errCount;i++){
					this.ErrorItem[i].style.color = "red";
				}
			case 3 :
				for(var i=1;i<errCount;i++){
				try{
					var span = document.createElement("SPAN");
					span.id = "__ErrorMessagePanel";
					span.style.color = "red";
					this.ErrorItem[i].parentNode.appendChild(span);
					span.innerHTML = this.ErrorMessage[i].replace(/\d+:/,"*");
					}
					catch(e){alert(e.description);}
				}
				try{//如果元素不可见的话 是不能focus add by panwei 2007-07-09
					this.ErrorItem[1].focus();
				}catch(e){}
				break;
			default :
				alert(this.ErrorMessage.join("\n"));
				break;
			}
			return false;
		}
		return true;
	},
	
	//增加提示信息
	AddError : function(index, str){
		this.ErrorItem[this.ErrorItem.length] = this.ErrorItem[0].elements[index];
		this.ErrorMessage[this.ErrorMessage.length] = this.ErrorMessage.length + ":" + str;
	},
	
	//字符区间限制
	limitCheck : function(len,min, max,nullable){
		
		if(nullable=="N" && len==0 ){
			return false;
		}
		
		min = min || 0;
		max = max || Number.MAX_VALUE;
		return min <= len && len <= max;
	},
	
	//中文字节计算,一个中文字是两个字节
	LenB : function(str){
		return str.replace(/[^\x00-\xff]/g,"**").length;
	},
	
	//清除已校验过的项目
	ClearState : function(elem){
		with(elem){
			if(style.color == "red")
				style.color = "";
			var lastNode = parentNode.childNodes[parentNode.childNodes.length-1];
			if(lastNode.id == "__ErrorMessagePanel")
				parentNode.removeChild(lastNode);
		}
	},
	//如果使用Ajax模式,需要在页面清除错误信息,而不是等待页面跳转 edited by panwei
	ClearAllState : function(theForm, mode){
		var obj = theForm || event.srcElement;
		var count =0;

		if(obj!=null && obj.elements!=undefined){
			count = obj.elements.length;
		}
		for(var i=0;i<count;i++){
			with(obj.elements[i]){
				var _dataType = getAttribute("dataType");
				if(typeof(_dataType) == "object" || typeof(this[_dataType]) == "undefined")  continue;
				this.ClearState(obj.elements[i]);
			}
		}
	},
	//自定义的正则表达式验证
	Exec : function(op, reg){
		return new RegExp(reg,"g").test(op);
	},
	
	//两数的关系比较
	compareCheck : function(op1,operator,op2){
		switch (operator) {
			case "NotEqual":
				return (op1 != op2);
			case "GreaterThan":
				return (op1 > op2);
			case "GreaterThanEqual":
				return (op1 >= op2);
			case "LessThan":
				return (op1 < op2);
			case "LessThanEqual":
				return (op1 <= op2);
			default:
				return (op1 == op2);            
		}
		return true;
	},
	
	//限制具有相同名称的多选按钮的选中数目
	groupCheck: function(name, min, max){
		var groups = document.getElementsByName(name);
		var hasChecked = 0;
		min = min || 1;
		max = max || groups.length;
		for(var i=groups.length-1;i>=0;i--)
			if(groups[i].checked) hasChecked++;
		return min <= hasChecked && hasChecked <= max;
		
		return true;
	},
	
	//日期格式校验
	dateCheck : function(op, formatString){
		formatString = formatString || "ymd";
		var m, year, month, day;
		switch(formatString){
			case "ymd" :
				m = op.match(new RegExp("^((\\d{4})|(\\d{2}))([-./])(\\d{1,2})\\4(\\d{1,2})$"));
				if(m == null ) return false;
				day = m[6];
				month = m[5]--;
				year =  (m[2].length == 4) ? m[2] : GetFullYear(parseInt(m[3], 10));
				break;
			case "dmy" :
				m = op.match(new RegExp("^(\\d{1,2})([-./])(\\d{1,2})\\2((\\d{4})|(\\d{2}))$"));
				if(m == null ) return false;
				day = m[1];
				month = m[3]--;
				year = (m[5].length == 4) ? m[5] : GetFullYear(parseInt(m[6], 10));
				break;
			default :
				break;
		}
		if(!parseInt(month)) return false;
		month = month==12 ?0:month;
		var date = new Date(year, month, day);
        return (typeof(date) == "object" && year == date.getFullYear() && month == date.getMonth() && day == date.getDate());
		function GetFullYear(y){return ((y<30 ? "20" : "19") + y)|0;}
		
		return true;
	},
	
	//校验IP地址格式 
	ipCheck : function(obj_value,nullable){ 
		var scount=0; 
		var iplength = obj_value.length; 
		var Letters = "1234567890."; 
		
		if(nullable=="N" && iplength==0 ){
			return false;
		}
		
		else if(iplength>0){
			for (i=0; i < iplength; i++) 
			{ 
			   var CheckChar = obj_value.charAt(i); 
			   if (Letters.indexOf(CheckChar) == -1) return false;
			} 
			
			for (var i = 0;i<iplength;i++)
			{ 
			  (ip.substr(i,1)==".")?scount++:scount; 
			}
			
			if(scount!=3) 
			{ 
			  return false; 
			} 
		
			first = obj_value.indexOf("."); 
			last = obj_value.lastIndexOf("."); 
			str1 = obj_value.substring(0,first); 
			subip = obj_value.substring(0,last); 
			sublength = subip.length; 
			second = subip.lastIndexOf("."); 
			str2 = subip.substring(first+1,second); 
			str3 = subip.substring(second+1,sublength); 
			str4 = obj_value.substring(last+1,iplength); 
			
			if (str1=="" || str2=="" ||str3== "" ||str4 == "")
			{
				return false; 
			} 
			if((str1< 0 || str1 >255) && (str2< 0 || str2 >255) && (str3< 0 || str3 >255) && (str4< 0 || str4 >255))
			{
			    return false; 
			} 
		}
		return true;
		
	},

	//判断手机号码的有效性
	mobileCheck : function(obj_value,nullable)   
	{
		if(nullable=="N" && obj_value.length!=11){ 
			return false;
		}else{
			if(obj_value.length>0)
			{
				if(obj_value.length!=11) return false;
			 	//数字有效性校验
			 	var Letters = "1234567890"; 
				for (i=0; i<obj_value.length; i++) 
				{ 
					var CheckChar = obj_value.charAt(i); 
					if (Letters.indexOf(CheckChar) == -1) 
					{ 
						return false; 
					} 
				}
				//手机号码只能以13开头 
			    if(obj_value.substring(0,1) !=1 || obj_value.substring(1,2) !=3)
			    {
					return false;
			    }
			}
		}
		return true;
	},
	
	//英文字母限制
	englishCheck : function(obj_value,nullable){
		re = /^[A-Za-z]+$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	//中文限制
	chineseCheck : function(obj_value,nullable){
		re = /^[\u0391-\uFFE5]+$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	//邮政编码检查
	zipCheck : function(obj_value,nullable){
		re = /^[1-9]\d{5}$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},

	//Mail检查
	mailCheck : function(obj_value,nullable){
		re = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	//电话号码检查
	phoneCheck : function(obj_value,nullable){
		re = /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	

	//URL检查
	urlCheck : function(obj_value,nullable){
		re = /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	
	//identity card检查
	idcardCheck : function(obj_value,nullable){
		re = /^\d{15}(\d{2}[A-Za-z0-9])?$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	
	//货币检查
	currencyCheck : function(obj_value,nullable){
		re = /^\d+(\.\d+)?$/;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	//区间判断
	rangeCheck : function(obj_value,min,max,nullable){
		if(obj_value.length==0 && nullable=="Y"){
       		return false;
		}
	else if(obj_value.length>0){
			if(parseInt(obj_value)>=parseInt(min) && parseInt(obj_value) <=parseInt(max)){
				return true;
			}else{
				return false;
			}
		}
		return true;
	},
	
	
	//数字检查
	numberCheck : function(obj_value,nullable,digit){
		re = /^\d+$/;
		if(digit!=null && obj_value.length>digit) return false;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	//数字检查
	doubleCheck : function(obj_value,nullable,digit){
		re = /^[-\+]?\d+(\.\d+)?$/;
		if(digit!=null && obj_value.length>digit) return false;
		if(nullable=="N"){
			return  re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	},
	
	//整数检查
	integerCheck : function(obj_value,nullable,digit){
		re = /^[-\+]?\d+$/;
		if(digit!=null && obj_value.length>digit) return false;
		if(nullable=="N"){
			return re.test(obj_value);
		}else{
			if(obj_value.length>0)
			{
			return re.test(obj_value);
			}
		}
		return true;
	}
 }
 

//数据键，键盘控制
function isKeyNumberdot(ifdot) 
{       
    var s_keycode=(navigator.appname=="Netscape")?event.which:event.keyCode;
	if(ifdot==0) {
		if(s_keycode>=48 && s_keycode<=57) {
			return true;
		} else {
			return false;
		}
    } else if (ifdot==1) {
		if((s_keycode>=48 && s_keycode<=57) || s_keycode==46) {
		      return true;
		} else if(s_keycode==45) {
		    alert('Please type positive number.');
		    return false;
		} else {
		    return false;
		}
    } else if (ifdot==2) {
        if((s_keycode>=48 && s_keycode<=57) || s_keycode==46 || s_keycode==45) {
		      return true;
		} else {
		    return false;
		}
    }
}

//限制textarea 的字符数
var TextUtil = new Object();
TextUtil.isNotMax = function(oTextArea){
	return oTextArea.value.length < oTextArea.getAttribute("maxlength");
}

