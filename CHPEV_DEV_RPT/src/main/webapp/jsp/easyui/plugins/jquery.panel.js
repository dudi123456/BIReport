﻿/**
 * jQuery EasyUI 1.1.2
 * 
 * Licensed under the GPL:
 *   http://www.gnu.org/licenses/gpl.txt
 *
 * Copyright 2010 stworthy [ stworthy@gmail.com ] 
 * 
 */
(function($){
function _1(_2){
_2.each(function(){
$(this).remove();
if($.browser.msie){
this.outerHTML="";
}
});
};
function _3(_4,_5){
var _6=$.data(_4,"panel").options;
var _7=$.data(_4,"panel").panel;
var _8=_7.find(">div.panel-header");
var _9=_7.find(">div.panel-body");
if(_5){
if(_5.width){
_6.width=_5.width;
}
if(_5.height){
_6.height=_5.height;
}
if(_5.left!=null){
_6.left=_5.left;
}
if(_5.top!=null){
_6.top=_5.top;
}
}
if(_6.fit==true){
var p=_7.parent();
_6.width=p.width();
_6.height=p.height();
}
_7.css({left:_6.left,top:_6.top});
_7.css(_6.style);
_7.addClass(_6.cls);
_8.addClass(_6.headerCls);
_9.addClass(_6.bodyCls);
if(!isNaN(_6.width)){
if($.boxModel==true){
_7.width(_6.width-(_7.outerWidth()-_7.width()));
_8.width(_7.width()-(_8.outerWidth()-_8.width()));
_9.width(_7.width()-(_9.outerWidth()-_9.width()));
}else{
_7.width(_6.width);
_8.width(_7.width());
_9.width(_7.width());
}
}else{
_7.width("auto");
_9.width("auto");
}
if(!isNaN(_6.height)){
if($.boxModel==true){
_7.height(_6.height-(_7.outerHeight()-_7.height()));
_9.height(_7.height()-_8.outerHeight()-(_9.outerHeight()-_9.height()));
}else{
_7.height(_6.height);
_9.height(_7.height()-_8.outerHeight());
}
}else{
_9.height("auto");
}
_7.css("height",null);
_6.onResize.apply(_4,[_6.width,_6.height]);
_7.find(">div.panel-body>div").triggerHandler("_resize");
};
function _a(_b,_c){
var _d=$.data(_b,"panel").options;
var _e=$.data(_b,"panel").panel;
if(_c){
if(_c.left!=null){
_d.left=_c.left;
}
if(_c.top!=null){
_d.top=_c.top;
}
}
_e.css({left:_d.left,top:_d.top});
_d.onMove.apply(_b,[_d.left,_d.top]);
};
function _f(_10){
var _11=$(_10).addClass("panel-body").wrap("<div class=\"panel\"></div>").parent();
_11.bind("_resize",function(){
var _12=$.data(_10,"panel").options;
if(_12.fit==true){
_3(_10);
}
return false;
});
return _11;
};
function _13(_14){
var _15=$.data(_14,"panel").options;
var _16=$.data(_14,"panel").panel;
_1(_16.find(">div.panel-header"));
if(_15.title&&!_15.noheader){
var _17=$("<div class=\"panel-header\"><div class=\"panel-title\">"+_15.title+"</div></div>").prependTo(_16);
if(_15.iconCls){
_17.find(".panel-title").addClass("panel-with-icon");
$("<div class=\"panel-icon\"></div>").addClass(_15.iconCls).appendTo(_17);
}
var _18=$("<div class=\"panel-tool\"></div>").appendTo(_17);
if(_15.closable){
$("<div class=\"panel-tool-close\"></div>").appendTo(_18).bind("click",_19);
}
if(_15.maximizable){
$("<div class=\"panel-tool-max\"></div>").appendTo(_18).bind("click",_1a);
}
if(_15.minimizable){
$("<div class=\"panel-tool-min\"></div>").appendTo(_18).bind("click",_1b);
}
if(_15.collapsible){
$("<div class=\"panel-tool-collapse\"></div>").appendTo(_18).bind("click",_1c);
}
if(_15.tools){
for(var i=_15.tools.length-1;i>=0;i--){
var t=$("<div></div>").addClass(_15.tools[i].iconCls).appendTo(_18);
if(_15.tools[i].handler){
t.bind("click",eval(_15.tools[i].handler));
}
}
}
_18.find("div").hover(function(){
$(this).addClass("panel-tool-over");
},function(){
$(this).removeClass("panel-tool-over");
});
_16.find(">div.panel-body").removeClass("panel-body-noheader");
}else{
_16.find(">div.panel-body").addClass("panel-body-noheader");
}
function _1c(){
if($(this).hasClass("panel-tool-expand")){
_38(_14,true);
}else{
_28(_14,true);
}
return false;
};
function _1b(){
_43(_14);
return false;
};
function _1a(){
if($(this).hasClass("panel-tool-restore")){
_47(_14);
}else{
_27(_14);
}
return false;
};
function _19(){
_1d(_14);
return false;
};
};
function _1e(_1f){
var _20=$.data(_1f,"panel");
if(_20.options.href&&(!_20.isLoaded||!_20.options.cache)){
_20.isLoaded=false;
var _21=_20.panel.find(">div.panel-body");
_21.html($("<div class=\"panel-loading\"></div>").html(_20.options.loadingMessage));
_21.load(_20.options.href,null,function(){
if($.parser){
$.parser.parse(_21);
}
_20.options.onLoad.apply(_1f,arguments);
_20.isLoaded=true;
});
}
};
function _22(_23,_24){
var _25=$.data(_23,"panel").options;
var _26=$.data(_23,"panel").panel;
if(_24!=true){
if(_25.onBeforeOpen.call(_23)==false){
return;
}
}
_26.show();
_25.closed=false;
_25.minimized=false;
_25.onOpen.call(_23);
if(_25.maximized==true){
_27(_23);
}
if(_25.collapsed==true){
_28(_23);
}
if(!_25.collapsed){
_1e(_23);
}
};
function _1d(_29,_2a){
var _2b=$.data(_29,"panel").options;
var _2c=$.data(_29,"panel").panel;
if(_2a!=true){
if(_2b.onBeforeClose.call(_29)==false){
return;
}
}
_2c.hide();
_2b.closed=true;
_2b.onClose.call(_29);
};
function _2d(_2e,_2f){
var _30=$.data(_2e,"panel").options;
var _31=$.data(_2e,"panel").panel;
if(_2f!=true){
if(_30.onBeforeDestroy.call(_2e)==false){
return;
}
}
_1(_31);
_30.onDestroy.call(_2e);
};
function _28(_32,_33){
var _34=$.data(_32,"panel").options;
var _35=$.data(_32,"panel").panel;
var _36=_35.find(">div.panel-body");
var _37=_35.find(">div.panel-header .panel-tool-collapse");
if(_37.hasClass("panel-tool-expand")){
return;
}
_36.stop(true,true);
if(_34.onBeforeCollapse.call(_32)==false){
return;
}
_37.addClass("panel-tool-expand");
if(_33==true){
_36.slideUp("normal",function(){
_34.collapsed=true;
_34.onCollapse.call(_32);
});
}else{
_36.hide();
_34.collapsed=true;
_34.onCollapse.call(_32);
}
};
function _38(_39,_3a){
var _3b=$.data(_39,"panel").options;
var _3c=$.data(_39,"panel").panel;
var _3d=_3c.find(">div.panel-body");
var _3e=_3c.find(">div.panel-header .panel-tool-collapse");
if(!_3e.hasClass("panel-tool-expand")){
return;
}
_3d.stop(true,true);
if(_3b.onBeforeExpand.call(_39)==false){
return;
}
_3e.removeClass("panel-tool-expand");
if(_3a==true){
_3d.slideDown("normal",function(){
_3b.collapsed=false;
_3b.onExpand.call(_39);
_1e(_39);
});
}else{
_3d.show();
_3b.collapsed=false;
_3b.onExpand.call(_39);
_1e(_39);
}
};
function _27(_3f){
var _40=$.data(_3f,"panel").options;
var _41=$.data(_3f,"panel").panel;
var _42=_41.find(">div.panel-header .panel-tool-max");
if(_42.hasClass("panel-tool-restore")){
return;
}
_42.addClass("panel-tool-restore");
$.data(_3f,"panel").original={width:_40.width,height:_40.height,left:_40.left,top:_40.top,fit:_40.fit};
_40.left=0;
_40.top=0;
_40.fit=true;
_3(_3f);
_40.minimized=false;
_40.maximized=true;
_40.onMaximize.call(_3f);
};
function _43(_44){
var _45=$.data(_44,"panel").options;
var _46=$.data(_44,"panel").panel;
_46.hide();
_45.minimized=true;
_45.maximized=false;
_45.onMinimize.call(_44);
};
function _47(_48){
var _49=$.data(_48,"panel").options;
var _4a=$.data(_48,"panel").panel;
var _4b=_4a.find(">div.panel-header .panel-tool-max");
if(!_4b.hasClass("panel-tool-restore")){
return;
}
_4a.show();
_4b.removeClass("panel-tool-restore");
var _4c=$.data(_48,"panel").original;
_49.width=_4c.width;
_49.height=_4c.height;
_49.left=_4c.left;
_49.top=_4c.top;
_49.fit=_4c.fit;
_3(_48);
_49.minimized=false;
_49.maximized=false;
_49.onRestore.call(_48);
};
function _4d(_4e){
var _4f=$.data(_4e,"panel").options;
var _50=$.data(_4e,"panel").panel;
if(_4f.border==true){
_50.find(">div.panel-header").removeClass("panel-header-noborder");
_50.find(">div.panel-body").removeClass("panel-body-noborder");
}else{
_50.find(">div.panel-header").addClass("panel-header-noborder");
_50.find(">div.panel-body").addClass("panel-body-noborder");
}
};
function _51(_52,_53){
$.data(_52,"panel").options.title=_53;
$(_52).panel("header").find("div.panel-title").html(_53);
};
$(window).unbind(".panel").bind("resize.panel",function(){
var _54=$("body.layout");
if(_54.length){
_54.layout("resize");
}else{
$("body>div.panel").triggerHandler("_resize");
}
});
$.fn.panel=function(_55,_56){
if(typeof _55=="string"){
switch(_55){
case "options":
return $.data(this[0],"panel").options;
case "panel":
return $.data(this[0],"panel").panel;
case "header":
return $.data(this[0],"panel").panel.find(">div.panel-header");
case "body":
return $.data(this[0],"panel").panel.find(">div.panel-body");
case "setTitle":
return this.each(function(){
_51(this,_56);
});
case "open":
return this.each(function(){
_22(this,_56);
});
case "close":
return this.each(function(){
_1d(this,_56);
});
case "destroy":
return this.each(function(){
_2d(this,_56);
});
case "refresh":
return this.each(function(){
$.data(this,"panel").isLoaded=false;
_1e(this);
});
case "resize":
return this.each(function(){
_3(this,_56);
});
case "move":
return this.each(function(){
_a(this,_56);
});
case "maximize":
return this.each(function(){
_27(this);
});
case "minimize":
return this.each(function(){
_43(this);
});
case "restore":
return this.each(function(){
_47(this);
});
case "collapse":
return this.each(function(){
_28(this,_56);
});
case "expand":
return this.each(function(){
_38(this,_56);
});
}
}
_55=_55||{};
return this.each(function(){
var _57=$.data(this,"panel");
var _58;
if(_57){
_58=$.extend(_57.options,_55);
}else{
var t=$(this);
_58=$.extend({},$.fn.panel.defaults,{width:(parseInt(t.css("width"))||undefined),height:(parseInt(t.css("height"))||undefined),left:(parseInt(t.css("left"))||undefined),top:(parseInt(t.css("top"))||undefined),title:t.attr("title"),iconCls:t.attr("icon"),cls:t.attr("cls"),headerCls:t.attr("headerCls"),bodyCls:t.attr("bodyCls"),href:t.attr("href"),cache:(t.attr("cache")?t.attr("cache")=="true":undefined),fit:(t.attr("fit")?t.attr("fit")=="true":undefined),border:(t.attr("border")?t.attr("border")=="true":undefined),noheader:(t.attr("noheader")?t.attr("noheader")=="true":undefined),collapsible:(t.attr("collapsible")?t.attr("collapsible")=="true":undefined),minimizable:(t.attr("minimizable")?t.attr("minimizable")=="true":undefined),maximizable:(t.attr("maximizable")?t.attr("maximizable")=="true":undefined),closable:(t.attr("closable")?t.attr("closable")=="true":undefined),collapsed:(t.attr("collapsed")?t.attr("collapsed")=="true":undefined),minimized:(t.attr("minimized")?t.attr("minimized")=="true":undefined),maximized:(t.attr("maximized")?t.attr("maximized")=="true":undefined),closed:(t.attr("closed")?t.attr("closed")=="true":undefined)},_55);
t.attr("title","");
_57=$.data(this,"panel",{options:_58,panel:_f(this),isLoaded:false});
}
if(_58.content){
$(this).html(_58.content);
if($.parser){
$.parser.parse(this);
}
}
_13(this);
_4d(this);
if(_58.doSize==true){
_57.panel.css("display","block");
_3(this);
}
if(_58.closed==true||_58.minimized==true){
_57.panel.hide();
}else{
_22(this);
}
});
};
$.fn.panel.defaults={title:null,iconCls:null,width:"auto",height:"auto",left:null,top:null,cls:null,headerCls:null,bodyCls:null,style:{},href:null,cache:true,fit:false,border:true,doSize:true,noheader:false,content:null,collapsible:false,minimizable:false,maximizable:false,closable:false,collapsed:false,minimized:false,maximized:false,closed:false,tools:[],href:null,loadingMessage:"Loading...",onLoad:function(){
},onBeforeOpen:function(){
},onOpen:function(){
},onBeforeClose:function(){
},onClose:function(){
},onBeforeDestroy:function(){
},onDestroy:function(){
},onResize:function(_59,_5a){
},onMove:function(_5b,top){
},onMaximize:function(){
},onRestore:function(){
},onMinimize:function(){
},onBeforeCollapse:function(){
},onBeforeExpand:function(){
},onCollapse:function(){
},onExpand:function(){
}};
})(jQuery);

