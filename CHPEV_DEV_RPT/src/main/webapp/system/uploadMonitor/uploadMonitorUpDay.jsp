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
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.bi.js"></script>
<script type="text/JavaScript" src="<%=request.getContextPath()%>/js/date/WdatePicker.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">

<%
	String up_switch = (String)request.getAttribute("up_switch");
	if(up_switch == null){
		up_switch = "";
	}
	String rpt_date = (String)request.getAttribute("rpt_date");
	if(rpt_date == null){
		rpt_date = "";
	}
	String cur_date = (String)request.getAttribute("cur_date");
	if(cur_date == null){
		cur_date = "";
	}
	String cur_date_stat = (String)request.getAttribute("cur_date_stat");
	if(cur_date_stat == null){
		cur_date_stat = "0";
	}
//    String[][] dateList = (String[][])request.getAttribute("dateList");
//    for(int i=0; i<dateList.length; i++){
//    	System.out.println(dateList[i][0]);
//    }
%>
<script language="javascript">
  function _changeSwitch(){
    tableQryForm.target = "_self"
	tableQryForm.action = "UploadMonitorNew.rptdo?optype=up_day&submit=1";
	tableQryForm.submit();
  }
  //确定按钮触发事件
  function _submit()
  {
    var rpt_code = document.getElementById("rpt_code");
    if(rpt_code.value == "")
    {
    	alert("您还没有选择报表！");
    	return;
    }
    var msg = "您确定要生成以下报表吗？";
    var tmp = rpt_code.value.split(",");
    for(var i = 0; i < tmp.length; i++)
    {
    	msg += "\n " + tmp[i];
    }
    if(confirm(msg)){
	    tableQryForm.target = "_self";
		tableQryForm.action = "UploadMonitorNew.rptdo?optype=view_log";// "UploadMonitorNew.rptdo?optype=up_day&submit=3";
		tableQryForm.submit();
	}
  }


  //选择查询条件
  function SelectValue(theObj){
    var rpt_id = document.getElementById("rpt_id");
    var rpt_code = document.getElementById("rpt_code");
    var str = window.showModalDialog("UploadMonitorNew.rptdo?optype=up_day&submit=2&checkIDs=" + rpt_id.value,
             theObj,
    		"dialogWidth:800px; dialogHeight:600px; dialogLeft:150px; dialogTop:100px; status:no; directories:yes;scrollbars:yes;help:no;");
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



  function loadDateStat(dateId){
    var url = "UploadMonitorNew.rptdo?optype=up_day&submit=5";
  	SmartXMLHttp.sendReq('POST', url, "dateId="+dateId, changeDateStat, null);
  }

  function changeDateStat(xmlHttpObj, paramArr){
    if("1"==xmlHttpObj.responseText){
   		document.getElementById('dateStatSel1').checked = true;
    }else if("0"==xmlHttpObj.responseText){
    	document.getElementById('dateStatSel2').checked = true;
    }else{
    	document.getElementById('dateStatSel2').checked = true;
    }
  }

  function _selChange(obj){
  	obj.parentNode.nextSibling.value=obj.value;
  	loadDateStat(document.getElementById('start_date').value);
  }

  function synchSelAndInput(obj){
  	var seledDate = obj.value;
  	document.getElementById('dateSel').value = seledDate;
  	loadDateStat(document.getElementById('start_date').value);
  }

  function _changeForceSwitch(){
    tableQryForm.target = "_self"
	tableQryForm.action = "UploadMonitorNew.rptdo?optype=up_day&submit=6";
	tableQryForm.submit();
  }

  function pageLoad(){
  	//document.getElementById('dateSel').value = document.getElementById('start_date').value;
  }
</script>
<style>
.btn3{background:url("../images/unicom/btn_4.gif") no-repeat scroll 0 0 transparent;width:65px;height:24px;line-height:24px;border:0 none;}
.btn3_hover{background:url("../images/unicom/btn_8.gif") no-repeat scroll 0 0 transparent;width:65px;height:24px;line-height:24px;border:0 none;color:White}
</style>
</head>
<body class="main-body" onload="pageLoad()">
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
								<input name="Submit2222" type="button" class="button" value="修改" onclick="_changeSwitch()"
									onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
							  </td>
							  <td class="table-item" align="right" width="200">强关标志：
                              	<INPUT type=radio id="dateStatSel1" name="dateStatSel" value="1"
                              		<%if("1".equalsIgnoreCase(cur_date_stat)){%>CHECKED<%}%>>开启
								<INPUT type=radio id="dateStatSel2" name="dateStatSel" value="0"
									<%if("0".equalsIgnoreCase(cur_date_stat)){%>CHECKED<%}%>>关闭
							  </td>
							  <td class="table-item" align="right"  width="118">
							    <div style="position:relative;">
							     <span style="margin-left:100px;width:18px;overflow:hidden;">
								 <select id="dateSel" style="width:118px;margin-left:-100px" onchange="_selChange(this)">
								 	<%//if(null!=dateList){ %>
								 	<%//for(int i=0; i<dateList.length; i++){%>
								 		<%//if(dateList[i][0].equals(cur_date)){ %>
								 		<option value="<%//=dateList[i][0] %>" selected><%//=dateList[i][0] %></option>
								 		<%//}else{ %>
								 		<option value="<%//=dateList[i][0] %>"><%//=dateList[i][0] %></option>
								 		<%//} %>
								 	<%//}}%>
								 </select>
								 </span>
							     <input type="text" readonly name="start_date" value="<%=cur_date%>" style="width:100px;position:absolute;left:0px;" size="15">
							    </div>
							  </td>
							  <td class="table-item" align="left" width="30">
							  <img src="../biimages/calander.gif" onClick="scwNextAction=synchSelAndInput.runsAfterSCW(this,document.getElementById('start_date'));scwShow(document.getElementById('start_date'),this)" height="18" border="0">
							  </td>
							  <td class="table-item" align="left" width="30">
							  <input name="Submit2222" type="button" class="button" value="保存" onclick="_changeForceSwitch()" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
							  </td>
                            </tr>
                  </table></td>
        </tr>   -->
        <tr>
            <td height="8"></td>
        </tr>
        <tr><td>
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="9"></td>
                        <td nowrap></td>
                        <td width="27"></td>
                        <td  width="100%" align="right" valign="bottom" >
                        </td>
                      </tr>
                    </table></td>
                </tr>
                <tr>
                  <td >
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
                          <input class="Wdate" type="text" id="rpt_date" size="15" value="<%=rpt_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/>
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
						 <LI>报表日期选择YYYYMMDD，报表数据日期则为YYYYMMDD-1。
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
						<input name="Submit2222" type="button" class="btn3" value="确定" onclick="_submit()"
							   onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
				   </td>
                 </tr>
              </table></td>
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
