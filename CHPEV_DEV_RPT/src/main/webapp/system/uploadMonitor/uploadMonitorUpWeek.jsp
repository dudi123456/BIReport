<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经营分析系统</title>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">

<%
	String up_switch = (String)request.getAttribute("up_switch");
	if(up_switch == null || "".equals(up_switch)){
		up_switch = request.getParameter("up_switch");
	}
	String rpt_date = (String)request.getAttribute("rpt_date");
	if(rpt_date == null || "".equals(rpt_date)){
		rpt_date = request.getParameter("rpt_date");
	}

  
%>
<script language="javascript">         
  function _changeSwitch(){
    tableQryForm.target = "_self"
	tableQryForm.action = "UploadMonitorNew.rptdo?optype=up_week&submit=1";
	tableQryForm.submit();
  }
  
  function _DayClick(dayID){
	document.tableQryForm.rpt_date.value = dayID; 
	tableQryForm.target = "_self"
	tableQryForm.action = "UploadMonitorUpWeek.screen?optype=up_week";
	tableQryForm.submit();
  }
  
  function _WeekDayChange(obj){
	document.tableQryForm.rpt_date.value = obj.value;
	tableQryForm.target = "_self"
	tableQryForm.action = "UploadMonitorUpWeek.screen?optype=up_week";
	tableQryForm.submit();
  }
  
  function _submit(){
    var rpt_code = document.getElementById("rpt_code");  
    if(rpt_code.value == ""){
    	alert("您还没有选择报表！");
    	return;
    }
    var msg = "您确定要生成以下报表吗？";
    var tmp = rpt_code.value.split(","); 
    for(var i = 0; i < tmp.length; i++){
    	msg += "\n " + tmp[i];
    }
    if(confirm(msg)){
	    tableQryForm.target = "_self"
		tableQryForm.action = "UploadMonitorNew.rptdo?optype=up_week&submit=3";
		tableQryForm.submit();
	}
  }
  
  
  //选择查询条件
  function SelectValue(theObj){  
    var rpt_id = document.getElementById("rpt_id");  
    var rpt_code = document.getElementById("rpt_code");  
    var str = window.showModalDialog("UploadMonitorNew.rptdo?optype=up_week&submit=2&checkIDs=" + rpt_id.value,
             theObj,
    		"dialogWidth:700px; dialogHeight:500px; dialogLeft:150px; dialogTop:200px; status:no; directories:yes;scrollbars:yes;help:no;");
//alert(str.toString());
    if( str.toString() == undefined || str == -1 || str == ''){        
        rpt_id.value = "";
       	rpt_code.value = "";
    	return;
    }   
    var ary = str.split(",");     
    rpt_id.value = "";
    rpt_code.value = "";
    for(var i=0; i < ary.length; i++){
    	var temp = ary[i].split(":");    
    	rpt_id.value = rpt_id.value + "," + temp[0]; 
    	rpt_code.value = rpt_code.value + "," + temp[1]; 
    }  
     //去首逗号
    if (rpt_id.value.substring(0,1)==",")
        rpt_id.value = rpt_id.value.substring(1,rpt_id.value.length);
    //去尾逗号
    if (rpt_id.value.substring(rpt_id.value.length-1,rpt_id.value.length)==",")
        rpt_id.value = rpt_id.value.substring(0,rpt_id.value.length-1);  
    //去首逗号
    if (rpt_code.value.substring(0,1)==",")
        rpt_code.value = rpt_code.value.substring(1,rpt_code.value.length); 
    //去尾逗号
    if (rpt_code.value.substring(rpt_code.value.length-1,rpt_code.value.length)==",")
        rpt_code.value = rpt_code.value.substring(0,rpt_code.value.length-1);  
          
    rpt_code.title = rpt_code.value;
  }   
</script>

</head>
<body class="main-body">    
<form  name="tableQryForm" action=""  method="POST" >    
     <table width="100%" border="0" cellpadding="0" cellspacing="0">          
       <!-- <tr><td>        
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr> 
                        <td width="9"><img src="../biimages/sh/corner_1.gif" width="9" height="21"></td>
                        <td class="lable-bg" nowrap><B>上传标志</B></td>
                        <td width="27"><img src="../biimages/sh/corner_2.gif" width="27" height="21"></td>
                        <td  width="100%" align="right" valign="bottom" class="side-3">
                        </td>
                      </tr>
                    </table></td>
                </tr>
                <tr> 
                  <td class="side-5">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
                            <tr class="table-trb" > 
                              <td class="table-item" align="center" >上传标志：
                              	<INPUT type=radio name="up_switch" value="Y"
                              		<%if("Y".equalsIgnoreCase(up_switch)){%>CHECKED<%}%>>开启
								<INPUT type=radio name="up_switch" value="N"
									<%if("N".equalsIgnoreCase(up_switch)){%>CHECKED<%}%>>关闭                              	
                              </td>
                              <td align="right" nowrap class="table-item">                        	
								<input name="Submit2222" type="button" class="button" value="修改" onclick="_changeSwitch()"
									onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"> 
							  </td>   
                            </tr>                         
                  </table></td>
        </tr>      -->
        <tr> 
            <td height="8"></td>
        </tr>
        <tr><td>        
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr> 
                        <td width="9"><img src="../biimages/sh/corner_1.gif" width="9" height="21"></td>
                        <td class="lable-bg" nowrap><B>手动上传</B></td>
                        <td width="27"><img src="../biimages/sh/corner_2.gif" width="27" height="21"></td>
                        <td  width="100%" align="right" valign="bottom" class="side-3">
                        </td>
                      </tr>
                    </table></td>
                </tr>
                <tr> 
                  <td class="side-5">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
                     <tr class="table-trb"> 
                     	<td class="table-item" nowrap align="center">
                     	  <input type="hidden" name="rpt_id" >
						  <input type="button" name="select_rpt" value="报表选择" onclick="javascript:SelectValue(this)" class="button3">
			         	</td>	
			         	<td class="table-item" nowrap align="center">
				  		  <input type="text" name="rpt_code" class="input-text" " size="30" title="已选择报表" readonly>
				  		</td>				  
                     </tr> 
                     <tr class="table-trb"> 
                     	<td nowrap class="table-item">报表日期</td>
                        <td nowrap class="table-item">
                          <input type="hidden" name="rpt_date" value="<%=rpt_date%>">
                          <style>.DateNavi{behavior:url(../htc/weekNavi2.htc);}</style>
                             <DIV id="dateDiv" class='DateNavi' DefaultYear='<%=rpt_date.substring(0,4)%>' DefaultMonth='<%=rpt_date.substring(4,6)%>' DefaultDay='<%=rpt_date.substring(6,8)%>'
                                DefaultStrDay='0' DayChange='_WeekDayChange(this)' DayClick='_DayClick'></DIV>
                        </td> 			  
                     </tr> 
                     <tr class="table-trb"> 
                       <td class="table-item" align="center" >文件类型</td>
                       <td class="table-item" align="center" >
                           <INPUT type=radio name="file_type" value="A" CHECKED>A文件
							<INPUT type=radio name="file_type" value="B">B文件                            	
                        </td>
                     </tr>
                     <tr class="table-trb"> 
                     	<td nowrap class="table-item" align="center">文件生成时间</td>
                        <td nowrap class="table-item" align="center">
                          <input type="text" name="create_time" value="0" class="input-text" size="20">
                        </td> 			  
                     </tr> 
                     <tr class="table-trb"> 
                     	<td nowrap class="table-item" align="center">文件生成多少分钟后上传</td>
                        <td nowrap class="table-item" align="center">
                          <input type="text" name="up_time" value="0" class="input-text" size="20">
                        </td> 			  
                     </tr> 
                    </table></td>
                 </tr>
                 <tr> 
		            <td height="8"></td>
		         </tr>
                 <tr>
                 	<table width="98%" align="center">
  					<tr>
    					<td><img src="../biimages/arrow6.gif" width="14" height="7">说明</td>
  					</tr>
					<tr>
					   <td height="1" background="../biimages/black-dot.gif"></td>
					 </tr>
					 <tr>
					   <td id="report_msu_memo" width="19%" height="60">
					   	<UL>
						 <LI>文件生成时间: YYYYMMDDHH24MI格式; 0表示立即生成
						 <LI>文件生成完多少分钟后上传: 0表示生成完立即上传；9999表示不上传
						</UL>
					   </td>
					 </tr>
					 <tr>
					   <td height="1" background="../biimages/black-dot.gif"></td>
					 </tr>
					</table>
                 </tr>
                 <tr> 
		            <td height="8"></td>
		         </tr>
                 <tr >
                 	<td align="center" nowrap>                        	
						<input name="Submit2222" type="button" class="button" value="确定" onclick="_submit()"
									onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"> 
				   </td>
                 </tr>
              </table></td>
        </tr>           
      </table>   
  </form>   
  </body>
</html>
