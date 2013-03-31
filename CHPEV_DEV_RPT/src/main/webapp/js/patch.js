/**
 * ���¼���Easy UI  combobox�е�ֵ ,
 *  select id
 *  opts ��ݣ�����ʾ��[{value:'1',text:'AAA'},{value:'2',text:'BBB'}]��
 *  Ĭ��ѡ ��ֵ
 */
function reloadOptions(id, opts, v) {
	var sel = $('#' + id);
	var options = '', opt = '', sc = '';

	for ( var i = 0; i < opts.length; i++) {
		if (opts[i].value == v) {
			sc = ' selected="selected"';
		}
		opt = '<option value="' + opts[i].value + '"' + sc + '>' + opts[i].text
				+ '</option>';
		options = options + opt;
	}
	sel.html(options);
	sel.combobox('setValue', v);
	sel.combobox('loadData', opts);
}

/**
 * ���Comboboxѡ������
 */
$(document).ready(function() {
	//�൱��select��onchange�¼�
	$('.easyui-combobox').combobox({
		onChange : function(newValue, oldValue) {
			var ops = this.options;
			var sc = this.getAttribute('desc');
			if (sc) {
				eval(sc);
			}
			for ( var i = 0; i < ops.length; i++) {
				if (ops[i].value == newValue) {
					ops[i].selected = 'selected';
				} else {
					//delete ops[i].selected;
				}
			}
		}
	});
});

/**--------------------------------------------联动效果----------------------------------------------------*/
/**
 * 实现报表中的条件联动效果（可支持多级联动）
 * @author  方慧
 * @version  [版本号, 2012-04-01]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

//对象
function BaseXmlSubmit(){
}
//动作
BaseXmlSubmit.prototype.callAction = function f_callAction(url)
{
  var dom = null;
  try{
    var rpc = new XmlRPC(url);
    rpc.send();
    dom = rpc.getText();
  }
  catch(e){
    alert(e.message);
  }
  return dom;
}
//实例
var baseXmlSubmit =new BaseXmlSubmit();

var req;
var arr = new Array();
var result  = "";
var childID = "";
var optString ="";
var opts;
function fristOpen(fID, cID, fieldName) {
	doOpen(fID, cID, fieldName);
}
function doOpen(fID, cID, fieldName) {
	//alert("你进入了doOpen方法");
	var fieldvalue=$("#"+fID).combobox("getValue");
	//alert("你选的是:"+" : "+fieldvalue);
	arr = cID.split(",");
	if (arr.length > 0) {
		childID = arr[0];
		var url = "doValidate.jsp?fieldValue=" + fieldvalue + "&fieldName="
				+ fieldName + "&childId=" + childID;
		/*if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		req.open("GET", url, true);
		req.onreadystatechange = callback;
		req.send(null);
		*/
		var bar = baseXmlSubmit.callAction(url);
		bar=bar.replace(/^\s+|\n+$/g,'');
		callback(bar);
	}
}
function callback(result) {
	//if (req.readyState == 4) {
	//	if (req.status == 200) {
			//result = req.responseText;
			//alert("result:"+result);
			document.getElementById(childID).length = 0;
			var strs = new Array(); //定义一数组
			strs = result.split(";");
			opts = new Array();
			for ( var i = 0; i < strs.length; i++) {
				var valuesDate = new Array();
				valuesDate = strs[i].split(",");
				var cobValue = new Object();
				cobValue.value= valuesDate[0];
				cobValue.text = valuesDate[1];
				opts.push(cobValue);
			}
			//alert("childID:"+childID+"---------------opts:"+opts);
			reloadOptions(childID,opts,'');
			optString="";//用完清空
			//alert("----------------调用已经结束------------");
//			if (arr.length > 0) {
//				for ( var i = 1; i < arr.length; i++) {
//					//document.getElementById(arr[i]).length = 0;
//					//var newOption = document.createElement("option");
//					var cobValue = new Object();
//					cobValue.value= "";
//					cobValue.text = "全部";
//					opts.push(cobValue);
//					reloadOptions(arr[i],opts,'');
//					optString="";//用完清空
//				}
//			}
		//}
	//}
}
/**--------------------------------------------联动效果结束----------------------------------------------------*/