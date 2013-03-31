var net=new Object();
net.READY_STATE_UNINITIALIZED=0;
net.READY_STATE_LOADING=1;
net.READY_STATE_LAODED=2;
net.READY_STATE_INTERACTIVE=3;
net.READY_STATE_COMPLETE=4;

net.ContentLoader= function(url,params,onready,onerror,updateDom){
	this.url = url;
	this.requestParams =  params;
	this.method = "POST";
	this.onready = onready;
	this.onerror = (onerror)?onerror:this.defaultError;
	this.updateDom=updateDom;
}

net.ContentLoader.prototype={
	//get the httpxmlrequeset
	getTransport: function() {
      var transport;
      if ( window.XMLHttpRequest )
         transport = new XMLHttpRequest();
      else if ( window.ActiveXObject ) {
         try {
            transport = new ActiveXObject('Msxml2.XMLHTTP');
         }
         catch(err) {
            transport = new ActiveXObject('Microsoft.XMLHTTP');
         }
      }
      return transport;
   },

	//send request
	  sendRequest: function() {

      //if ( window.netscape && window.netscape.security.PrivilegeManager.enablePrivilege)
      //   netscape.security.PrivilegeManager.enablePrivilege('UniversalBrowserRead');

      var requestParams = []
      for ( var i = 0 ; i < arguments.length ;  i++ )
         requestParams.push(arguments[i]);

      var oThis = this;
      var request = this.getTransport();
      this.req=request;
      request.open( this.method, this.url, true );
      request.setRequestHeader( 'Content-Type', 'application/x-www-form-urlencoded');
      request.onreadystatechange = function() { oThis.handleAjaxResponse(request) };
      request.send( this.queryString(requestParams) );
  },

  queryString: function(args) {
     var requestParams = [];
     for ( var i = 0 ; i < this.requestParams.length ; i++ ){
        requestParams.push(this.requestParams[i]);
     }
     for ( var j = 0 ; j < args.length ; j++ )
        requestParams.push(args[j]);

     var queryString = "";
     if ( requestParams && requestParams.length > 0 ) {
        for ( var i = 0 ; i < requestParams.length ; i++ )
           queryString += requestParams[i] + '&';
        queryString = queryString.substring(0, queryString.length-1);
     }
     queryString=encodeURI(queryString);
     return queryString;
  },

  handleAjaxResponse: function(request) {
     if ( request.readyState == net.READY_STATE_COMPLETE ) {
        if ( this.isSuccess(request) ){
			if(this.onready)
				this.onready.call(this);
        }else{
           this.onerror.call(this);
        }
     }
  },

  isSuccess: function(request) {
    return  request.status == 0
        || (request.status >= 200 && request.status < 300);
  }

};

net.ContentLoader.prototype.defaultError=function(){
  alert("error fetching data!"
    +"\n\nreadyState:"+this.req.readyState
    +"\nstatus: "+this.req.status
    +"\nheaders: "+this.req.getAllResponseHeaders());
}