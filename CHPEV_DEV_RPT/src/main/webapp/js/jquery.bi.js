//封装hover事件
// <param name="domid">元素id</param>
// <param name="classname">样式名称</param>
function domHover(obj, classname)
{
    $(obj).each(function()
    {
        $(this).hover(
                      function()
                      {
                          $(this).addClass(classname);
                      },
                      function()
                      {
                          $(this).removeClass(classname);
                      });
    });
}

function layout()
{//让中间层的高度随屏幕变化而变化
    var clientHeight = document.documentElement.clientHeight; //屏幕高度
    var headHeight = 107; //页头高度
    if ($("#head").css("display") == "none")
    {
        headHeight = 37;
    }
    var footHeight = 28; //页脚高度
    var contentHeight = clientHeight - headHeight - footHeight;
    $("#content").css("height", contentHeight + "px");
}

function widgetWidth()
{//解决ie6,ie7在页面出现滚动条时,50%依然相对与整个BODY出现的问题
    var clientWidth = document.body.clientWidth;
    if ($.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0") && !$.support.style)
    {//28为滚动条宽度
        $(".widget-place").css("width", (clientWidth - 28) / 2 + "px");
    }
}

window.onresize = function()
{
    layout();
    widgetWidth();
}
function addMyFavourate()
{
    var html = "<div class=\"alert_box_bg\" id=\"alert_box_bg\"><iframe></iframe></div><div class=\"alert_box_container\" id=\"alert_box_container\">"
       + "<div class=\"alert_box flowbox3\">"
       + "<div class=\"layerbox\">"
       + "<div class=\"fv_box\">"
       + "<span class=\"fvinput\"><input value=\"我的收藏夹1\" class=\"txtinput\"/></span>"
       + "<div class=\"fvtree\">"
       + "<div class=\"fvtreebox\"><p class=\"icon\">选择文件夹</p>"
       +$("#fvtreebox").html()
       + "</div></div></div>"
       + "<div class=\"co_btn\" id=\"co_btn\">"
       + "<button class=\"btn\">保 存</button>&nbsp;"
       + "<button class=\"btn\">新 建</button>&nbsp;"
       + "<button class=\"btn\" onclick=\"jQuery('.alert_box_bg,.alert_box_container').remove();\">取 消</button>"
       + "</div>"
    + "</div></div></div>"

    jQuery(document.body).append(html);bindFvTree();
    domHover("#fv_btn button", "btn_hover");
};

function showNotice()
{
    jQuery('.alert_box_bg,.alert_box_container').remove();
    var html = "<div class=\"alert_box_bg\" id=\"alert_box_bg\"><iframe></iframe></div><div class=\"alert_box_container\" id=\"alert_box_container\">"
       + "<div class=\"alert_box flowbox1\">"
       + "<div class=\"layerbox\">"
       + "<div class=\"img_box\">"
       + "<img src=\"images/common/com_7.png\">"
       + "</div>"
       + "<div class=\"co_box\">"
       + "<a class=\"close\" href=\"javascript:;\" onclick=\"jQuery('.alert_box_bg,.alert_box_container').remove();\"><img src=\"images/common/com_8.png\"></a>"
       + "<ul>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "</ul>"
       + "</div>"
    + "</div></div></div>"

    jQuery(document.body).append(html);
    domHover("#fv_btn button", "btn_hover");
};

function showHelp()
{
    jQuery('.alert_box_bg,.alert_box_container').remove();
    var html = "<div class=\"alert_box_bg\" id=\"alert_box_bg\"><iframe></iframe></div><div class=\"alert_box_container\" id=\"alert_box_container\">"
       + "<div class=\"alert_box flowbox1\">"
       + "<div class=\"layerbox dialog1\">"
       + "<div class=\"img_box\">"
       + "<img src=\"images/common/com_9.gif\">"
       + "</div>"
       + "<div class=\"co_box\">"
       + "<a class=\"close\" href=\"javascript:;\" onclick=\"jQuery('.alert_box_bg,.alert_box_container').remove();\"><img src=\"images/common/com_8.png\"></a>"
       + "<ul>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "<li>2010-09-11 <a href=\"\" class=\"blue\">测试一下拉</a></li>"
       + "</ul>"
       + "</div>"
    + "</div></div></div>"

    jQuery(document.body).append(html);
    domHover("#fv_btn button", "btn_hover");
};

function editPw()
{
    jQuery('.alert_box_bg,.alert_box_container').remove();
    var html = "<div class=\"alert_box_bg\" id=\"alert_box_bg\"><iframe></iframe></div><div class=\"alert_box_container\" id=\"alert_box_container\">"
       + "<div class=\"alert_box flowbox2\">"
       + "<div class=\"layerbox dialog1\">"
       + "<div class=\"co_box\">"
       + "<table width=\"100%\"><tr>"
       + "<tr><td align=\"right\" width=\"80px\">原密码：</td><td><input value=\"\" class=\"txtinput\"></td></tr>"
       + "<tr><td align=\"right\" width=\"80px\">新密码：</td><td><input value=\"\" class=\"txtinput\"></td></tr>"
       + "<tr><td align=\"right\" width=\"80px\">重复密码：</td><td><input value=\"\" class=\"txtinput\"></td></tr>"
       + "<tr><td align=\"right\" width=\"80px\">密码强度：</td><td><div class=\"jindu\"><div></div></div></td></tr>"
       + "</table>"
       + "</div>"
       + "<div class=\"co_btn\" id=\"fv_btn\">"
       + "<button class=\"btn\">保 存</button>&nbsp;"
       + "<button class=\"btn\">新 建</button>&nbsp;"
       + "<button class=\"btn\" onclick=\"jQuery('.alert_box_bg,.alert_box_container').remove();\">取 消</button>"
       + "</div>"
    + "</div></div></div>"

    jQuery(document.body).append(html);
    domHover("#fv_btn button", "btn_hover");
};
function showLoad()
{
    var img = "../images/unicom/bg_43.png";
    if ($.browser.msie&&($.browser.version == "6.0"))
    {
        var img = "../images/unicom/bg_43.gif";
    }
    var html = "<div class=\"alert_box_bg\" id=\"alert_box_bg\"><iframe></iframe></div><div class=\"alert_box_container\" id=\"alert_box_container\">"
       + "<div class=\"alert_box flowbox4\"><img src=\""+img+"\" alt=\"\" usemap=\"#planetmap\">"
       + "</div></div>"

    jQuery(document.body).append(html);
    $("#alert_box_bg").height($("body").height());
    $(".alert_box_container").css({position: "absolute",top:"50px"});
};
