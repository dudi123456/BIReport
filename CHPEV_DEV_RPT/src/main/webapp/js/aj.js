//JScript文件

//ajax请求功能函数
//作者：吴宝佑
//get调用方式：(1)实例化 var aj=new ajax(); (2)调用get函数 aj.get(url,callback) (3)写回调函数 function callback(xhr){xhr.responsetext}
//post调用方式：(1)实例化 var aj=new ajax(); (2)调用post函数 aj.post(url,content,callback) (3)写回调函数 function callback(xhr){xhr.responsetext}

//构造ajax对象

function ajax() 
{
    function getXHR()//获取xmlhttprequest
    {
        var request=false;
        try 
        {
            request = new XMLHttpRequest();
        } 
        catch (trymicrosoft) 
        {
              try 
              {
                request = new ActiveXObject("Msxml2.XMLHTTP");
              } 
              catch (othermicrosoft) 
              {
                try 
                {
                      request = new ActiveXObject("Microsoft.XMLHTTP");
                } 
                catch (failed) 
                {
                      request = false;
                }
              }
        }
        return request;
    }
    
    this.get = function (openUrl,successFun)//ajax对象的get方法,通过get方式发送请求,openUrl为请求的页面,successFun为成功回调执行的函数
    {
        var request = getXHR();
        request.open("get",openUrl,true);
//        request.onreadystatechange = function ()
//        {
//            if (request.readystate==4)
//            {
//                if (request.status==200)
//                {
//                    successFun(request);
//                }
//            }
//        };
        request.onreadystatechange = update;
        function update()
        {
            if (request.readystate==4)
            {
                if (request.status==200)
                {
                    successFun(request);
                }
            }
        }
        request.send(null);
    }
    
    this.post = function (openUrl,sendContent,successFun)//ajax对象的post方法,通过post方式发送请求,openUrl为请求的页面,successFun为成功回调执行的函数
    {
        var request = getXHR();
        request.open("post",openUrl,true);
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");//告诉服务器发送的是文本
        request.onreadystatechange = update;
        function update()
        {
            if (request.readystate==4)
            {
                if (request.status==200)
                {
                    successFun(request);
                }
            }
        }
        request.send(sendContent);
    }
}
