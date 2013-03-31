// XmlRPC.js
function f_createDom(){
   var dom = new ActiveXObject("Msxml2.DOMDocument.3.0");
   dom.setProperty("SelectionLanguage", "XPath");
   return dom;
}

var objXMLHTTP;


function XmlRPC(p_connection,p_method,asy) {
   this.baseUrl = "";
   this.connection = p_connection?p_connection:"err.jsp";
   this.method = p_method?p_method:"POST";
   this.asy = asy?asy:false;
   this.sendXml = f_sendXml;
   this.sendText = f_sendText;
   this.send = f_send;
   this.getXml = f_getXml;
   this.getText = f_getText;
   this.abort = f_abort;
}


function f_sendXml(dom){
   
   this.send(dom);
}

function f_sendText(text){
   this.send(text);
}

function f_send(arg){
    var content = arg?arg:'';
    objXMLHTTP = new ActiveXObject("Microsoft.XMLHTTP");
    objXMLHTTP.Open(this.method, this.connection,this.asy);
    objXMLHTTP.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
   // objXMLHTTP.setRequestheader("content-length",content.length); 
    objXMLHTTP.setRequestHeader("charset","utf-8");   
    //objXMLHTTP.setRequestHeader("charset","ISO8859-1");   
    objXMLHTTP.Send(content); 
}

function f_getXml(){
    if(objXMLHTTP.status == '200'){
       var dom = f_createDom();
       dom.load(objXMLHTTP.responseXML);

       //objXMLHTTP = null;
       dom.setProperty("SelectionLanguage", "XPath");
       
       if(dom.xml != ''){
          var error = dom.documentElement.selectSingleNode('//Error');
      
          if( error != null){
            alert(error.text);
            return null;
          }
       } 
//       alert(dom.xml);
       return dom;
    }
    else{
       alert("?????" + objXMLHTTP.status + "?????");
       
       return null;
       //throw new Error('error');
    }
}

function f_abort(){
    objXMLHTTP.abort();
}

function f_getText(){
   return objXMLHTTP.responseText;
}

function CallService(url){
	var dom = "";
	 try{
	   var rpc = new XmlRPC(url);
	   rpc.send(url);
	   
	   dom = rpc.getText();
	 }
	 catch(e){
	   alert("错误信息:"+e.message);
	 }
	 return dom;
}

