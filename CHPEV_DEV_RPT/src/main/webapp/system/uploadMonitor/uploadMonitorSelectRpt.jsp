<%@ page contentType="text/html;charset=UTF-8" %>
<%	
    if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;

	String optype = request.getParameter("optype");
	if (optype == null || "".equals(optype)) {
		optype = "up_day"; // 默认页面
	}
	String title = "指标2.0日报接口";
	if("up_month".equalsIgnoreCase(optype)){
		title = "指标2.0月报接口";
	} else if("up_week".equalsIgnoreCase(optype)){
		title = "1.0周报接口";
	}
	
	// 获取查询框的值
	String code = com.ailk.bi.base.util.CommTool.getParameterGB(request,"code");
	if(code==null)
		code = "";
	String name = com.ailk.bi.base.util.CommTool.getParameterGB(request,"name");
	if(name==null)
		name = "";
	
	String checkIDs = com.ailk.bi.base.util.CommTool.getParameterGB(request,"checkIDs"); 
	if(checkIDs == null){
		checkIDs = "";
	}
	
	
	
	String[][] list = (String[][])request.getAttribute("list");
	if(list == null || list.length == 0){
		out.println("<p>没有相应的报表！</p>");
		return;
	}
	
  %>
<html>
<head>
<title><%=title%></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/SmartMenuToc.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/kw.css" type="text/css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>

  <script language="JavaScript">    
    var UserNo = "";
    function pageOnload(){
    	
    }
    
    function returnNewUserNo()
    { 
       window.returnValue = UserNo;  
    }
    
    function checkValue(){    
       UserNo = "";
       var checks = document.getElementsByName("checks");
       for(var i = 0; i < checks.length; i++){
			if(checks[i].checked){
				UserNo += "," + checks[i].value;
			}
	   }
       //去首逗号
       if (UserNo.substring(0,1)==",")
            UserNo=UserNo.substring(1,UserNo.length);
       //去尾逗号
       if (UserNo.substring(UserNo.length-1,UserNo.length)==",")
            UserNo=UserNo.substring(0,UserNo.length-1);      
    }
    
    function returnUserNo()
    {
       checkValue();     
       window.close();
    }
    
    function _reset(){
    	UserNo = "";
    }
    
    function checkall(){
       var checks = document.getElementsByName("checks");
       for(var i = 0; i < checks.length; i++){
       		checks[i].checked = true;
	   }
    }
    
    function _clear(){ 
       var checks = document.getElementsByName("checks");
       for(var i = 0; i < checks.length; i++){
       		checks[i].checked = false;
	   }
    }
      
   function document.onkeydown()		//按Enter键
	{ 
    	if (window.event.keyCode == 13){  
			return false;
		}
	}    
   
   function _onSubmit(){
    this.name='win1';
  	document.myform.target = "win1";
  	var optype = document.getElementById("optype").value;
  	var checkIDs = document.getElementById("checkIDs").value;
  	document.myform.action = "UploadMonitorNew.rptdo?submit=2&todo=query&optype="+optype+"&checkIDs="+checkIDs;
  	document.myform.submit();
   }
  </script>
<style>
.btn3{background:url("../images/unicom/btn_4.gif") no-repeat scroll 0 0 transparent;width:65px;height:24px;line-height:24px;border:0 none;}
.btn3_hover{background:url("../images/unicom/btn_8.gif") no-repeat scroll 0 0 transparent;width:65px;height:24px;line-height:24px;border:0 none;color:White}
</style>
</head>
<body class="kw-body" onload="pageOnload();" onUnload="javascript:returnNewUserNo()">
<form name="myform" method="post" action="">
<table width="100%">
 
  <tr>
    <td align = "center" width="100%">
      <table width="50%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td nowrap>&nbsp;&nbsp;接口代码&nbsp;&nbsp;</td>
	      <td nowrap >
	    	<input name="code" type="text" size="12" class="input-text" value="<%=code%>" >	    	
	      </td>
	      <td nowrap>&nbsp;&nbsp;接口名称&nbsp;&nbsp;</td>
	      <td nowrap >
	    	<input name="name" type="text" size="25" class="input-text" value="<%=name%>" >	    	
	      </td>
	      <td nowrap> 
			&nbsp;&nbsp;<input type="button" name="selectchannel" value="模糊查询" class="btn3" onClick="javascript:_onSubmit()">
	      </td>
	      <input id = "optype" type = "hidden" 	value = "<%=optype %>" />	
	      <input id = "checkIDs" type = "hidden" value = "<%=checkIDs %>" />	
        </tr>
      </table>
    </td>
  </tr>
  
  <tr width="100%">
    <td align="center">
     <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table2">         
         <tr class="table-th" align="center" width="100%">                  
                  <td align="center" class="table-item" >接口代码及序列号</td>                           
                  <td align="center" class="table-item" >接口名称</td>   
                  <td align="center" class="table-item" >选择</td>
     	 </tr>
      <%        
        for( int i = 0; i < list.length; i++) {
        	if((i%2) != 0){
      %>
	 <tr width="100%" class="table-tr" align="center" >
      <%	}else {%>
	 <tr width="100%" class="table-trb" align="center">
	  <%	}%>        
        <td class="table-td" align="center"><%=list[i][1]%></td>   
        <td class="table-td" align="left"><%=list[i][2]%></td>  
        <td align="center" class="table-td">
        	<input type="checkbox" name="checks" value="<%=(list[i][0] + ":" + list[i][1])%>"
        	 <%if(checkIDs.indexOf(list[i][0]) != -1){%> checked <%}%> >
        </td>                
       </tr>
      <%}%>
  </table></td></tr>
  <tr>       
       <td class="list-bg" align="center" > 
         <input type="button" name="all" class="btn3" onclick="checkall()" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="全选">
       	 <input type="button" name="clear" class="btn3" onclick="_clear()" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="清除">      	 
         <input type="button" name="okbutton" class="btn3" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="确定" onclick="returnUserNo();">
         <input type="reset" name="okbutton" class="btn3" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)" value="重置" onclick="_reset();">
       </td>
     </tr>
  </table>
  </form>
</body>
<script language=javascript>
        domHover(".btn3", "btn3_hover");
        function switchClass2(theObj)
		{
			if(theObj.className.indexOf("_hover")<0)
		{
			theObj.className=theObj.className+"_hover";
		}
		else
		{
			theObj.className=theObj.className.replace("_hover","");
		}
		}
</script>
</html>