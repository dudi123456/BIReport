/*----------------------------------------------------------------------------\
|                               bi_zz 1.0                                     |
|-----------------------------------------------------------------------------|
|                         Created by jcm   				                      |
|-----------------------------------------------------------------------------|
|                     总帐报表界面元素控制脚本                                                                                         |
| Created 2010-01-10 | All changes are in the log above. | Updated 2010-01-10 |
\----------------------------------------------------------------------------*/
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
//生成工具条
function bi_zz(paramValue){
    alert(1);
    var node = baseXmlSubmit.callAction("../sysparam/param_ajax.jsp?type=plan&plan_id="+paramValue);
    node=node.replace(/^\s+|\n+$/g,'');
    //
    var setObj = eval(document.edit_form.APP_DESC);
    alert(setObj);
    setObj.value = node;
}

 
 
 
 