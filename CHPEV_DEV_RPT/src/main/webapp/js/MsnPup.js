/*
    caption     标题栏
    title       消息标题
    message     消息主题
    target      链接框架
    action      链接地址
*/
function MessShow(id,width,height,caption,title,message,target,action)
{
    this.id         = id;
    this.title      = title;
    this.caption    = caption;
    this.message    = message;
    this.target     = target;
    this.action     = action;
    this.width      = width?width:250;
    this.height     = 126;//height?height:150;
    this.timeout    = 500;      //消息停留时间
    this.speed      = 10;       //消息速度，越小越快
    this.step       = 2;        //移动步长
    this.right      = screen.width -1;
    this.bottom     = screen.height;
    this.left       = this.right - this.width;
    this.top        = this.bottom - this.height;
    this.timer      = 0;
    this.pause      = false;
    this.close      = false;
    this.autoHide   = true;
}
// 隐藏消息方法
MessShow.prototype.hide = function()
{
    if(this.onunload())
    {
        //var offset = this.height>this.bottom-this.top?this.height:this.bottom-this.top;
        var me = this;
        if(this.timer>0)
        {
            window.clearInterval(me.timer);
        }
        var fun = function()
        {
            if(me.pause==false||me.close)
            {
                var x = me.left;
                var y = 0;
                var width = me.width;
                var height = 0;
                if(me.offset>0){
                    height = me.offset;
                }
                y = me.bottom - height;
                if(y>=me.bottom){
                    window.clearInterval(me.timer);
                    me.Pop.hide();
                } else {
                    me.offset = me.offset - me.step;
                }
                me.Pop.show(x,y,width,height);
            }
        }
        this.timer = window.setInterval(fun,this.speed);
    }
}
//消息卸载事件，可以重写

MessShow.prototype.onunload = function()
{
    return true;
}
// 消息命令事件，要实现自己的连接，请重写它
MessShow.prototype.oncommand = function()
{
    window.open(this.action,this.target);
    this.hide();
}
// 消息显示方法
MessShow.prototype.show = function(){
    var oPopup; //IE5.5+
    if ($.browser.msie){
    	oPopup = window.createPopup(); //IE5.5+
    	this.Pop = oPopup;
    }

    //var w = this.width;
    //var h = this.height;

    var buf=[];
    buf.push("<div id=\"dialogBox\" style=\"background-image: url(../images/unicom/bg_28.png); background-repeat: no-repeat;" +
    		"background-attachment:scroll; background-position: 0px 0px;" +
    		"width: 259px;height:126px; position: absolute;Z-INDEX: 99999\">");
    buf.push("<SPAN title=\"关闭\" style=\"position:absolute;right:12px;top:5px; cursor:hand; \" id='btSysClose' >");
    buf.push("    <img src=\"../images/common/com_10.gif\" style=\"cursor:hand;\"/></span>");
    buf.push( "<table width='100%' border='0' cellspacing='0' cellpadding='0' style=\"position:absolute; top:60px;\">");

        var id = this.id.split("$");
        var info = this.title.split("$");
        for(var i=0;i<id.length;i++) {
        	buf.push( "<tr><td style='height:23px;border-bottom: 1px dotted #C7C7C7;vertical-align: bottom;font-size: 12px' >");
        	buf.push( "&nbsp;&nbsp;<img src='../images/system/arr.gif' hspace='5' />  <a href='javascript:void(0)' style=\"position: relative; left: 5px; top: 0px;\"id='btCommand"+i+"' name="+id[i]+">"+info[i]+"</a>");
        	buf.push( "</tr></td>");
        }
        buf.push( "</table>");
        buf.push( "</DIV>") ;
        if ($.browser.msie){
        	oPopup.document.body.innerHTML = buf.join("");
        }else{
        	jQuery(top.document.body).append(buf.join(""));
        }

    this.offset = 0;
    var me = this;
    var fun = function()
    {
        var x = me.left;
        var y = 0;
        var width    = me.width;
        var height    = me.height;

            if(me.offset>me.height)
            {
                height = me.height;
            } else
            {
                height = me.offset;
            }
        y = me.bottom - me.offset;
        if(y<=me.top)
        {
            me.timeout--;
            if(me.timeout==0)
            {
                window.clearInterval(me.timer);
                if(me.autoHide)
                {
                    me.hide();
                }
            }
        }
        else
        {
            me.offset = me.offset + me.step;
        }
        me.Pop.show(x,y,width,height);
    }

    this.timer = window.setInterval(fun,this.speed);
    var btClose ;
    if ($.browser.msie){
    	btClose = oPopup.document.getElementById("btSysClose");
    }
    if(!btClose){
    	btClose = top.document.getElementById("btSysClose");
    }
    btClose.onclick = function() {
    	if ($.browser.msie){
    		me.close = true;
    		me.hide();
    	}else{
    		$("#dialogBox",top.document).css("display","none");
    	}
    }
    //这里只能是IE可以实现
    if ($.browser.msie){
	    for(var j=0;j<id.length;j++) {
		   eval("btCommand"+j+ "= oPopup.document.getElementById('btCommand"+j+"') ");
		   var msgId = eval("btCommand"+j+".name");
		   eval("btCommand"+j+".onclick = function() { var url = 'bulletinAdmin.rptdo?opt_type=openshow&id="+msgId+"'; window.open(url, '', 'height=500, width=600, top=100, left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=0, location=0, status=0');} ");
	    }
    }else{
    	//Firefox
    	   var e;
    	   var msgId;
    	   for(var j=0;j<id.length;j++) {
    		   msgId=null;
    		   e=null;
    		   e=$("#btCommand"+j,top.document);
    		   if(e){
    			   msgId=$("#btCommand"+j,top.document).attr("name");
    			   e=e[0];
    			   e.onclick = function(){
    				   var url = 'bulletinAdmin.rptdo?opt_type=openshow&id='+msgId;
    				   window.open(url, '', 'height=500, width=600, top=100, left=100,toolbar=no, menubar=no, scrollbars=yes, resizable=0, location=0, status=0');
    			   }
    		   }
    	    }
    }
}

// 设置速度方法
MessShow.prototype.speed = function(s)
{
    var t = 10;
    try
    {
        t = praseInt(s);
    }
    catch(e){}

    this.speed = t;
}
// 设置步长方法
MessShow.prototype.step = function(s)
{
    var t = 2;
    try
    {
        t = praseInt(s);
    }
    catch(e){}
    this.step = t;
}
MessShow.prototype.rect = function(left,right,top,bottom)
{
    try
    {
        this.left    = left?left:0;
        this.right    = right?right:screen.availWidth -1;
        this.top    = top?top:0;
        this.bottom = bottom?bottom:screen.availHeight;
    }
    catch(e)
    {}
}
