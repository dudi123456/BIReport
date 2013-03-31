<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM" %>
<%
if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,response))
		return;
%>
<!DOCTYPE html>
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
	  String end_date = request.getParameter("end_date");
	  if(end_date == null||"".equals(end_date)){   //缺省日期
	  	end_date = com.ailk.bi.common.app.DateUtil.getDiffDay(0, new java.util.Date());
	  }
	  String begin_date = request.getParameter("begin_date");
	  if(begin_date == null||"".equals(begin_date)){   //缺省日期
		  begin_date = end_date;
	  }
	  String oper_type = request.getParameter("oper_type");
	  if(oper_type == null ){
		 oper_type = "";
	  }
	  String date_type = request.getParameter("date_type");
	  if(date_type == null || "".equals(date_type)){
		 date_type = "";
	  }
	  String oper_status = request.getParameter("oper_status");
	  if(oper_status == null ){
		 oper_status = "";
	  }
	  String file_name = request.getParameter("file_name");
	  if(file_name == null ){
		 file_name = "";
	  }
	  String report_kind = request.getParameter("report_kind");
	  if(report_kind == null ){
		 report_kind = "";
	  }
	  file_name = file_name.trim();

  //查询结构
  String[][] list = (String[][])request.getAttribute("list");
  String total_num = "0";
  if(list != null && list.length > 0){
	  total_num = String.valueOf(list.length);
  }

%>
<script language="javascript">
  function _onQuery()
  {
	  var start = document.getElementById("begin_date");
	  var end = document.getElementById("end_date");
	  if(start.value > end.value)
		  {
		  		alert("起始日期应该小于截止日期！");
		  		end.focus();
		  		return;
		  }
      tableQryForm.target = "_self"
	  tableQryForm.action = "UploadMonitorNew.rptdo?optype=view_log";
	  tableQryForm.submit();
  }

  function resetValue()
  {
	  var start = document.getElementById("begin_date");
	  var end = document.getElementById("end_date");
	  if(start)
		  {
		  	var hStart = document.getElementById("hBegin_date");
		  	if(hStart)
		  		{
		  			start.value = hStart.value;
		  		}
		  }
	  if(end)
	  {
	  	var hEnd = document.getElementById("hEnd_date");
	  	if(hEnd)
	  		{
	  			end.value = hEnd.value;
	  		}
	  }
  }
</script>
<style>
.btn3{background:url("../images/unicom/btn_4.gif") no-repeat scroll 0 0 transparent;width:65px;height:24px;line-height:24px;border:0 none;}
.btn3_hover{background:url("../images/unicom/btn_8.gif") no-repeat scroll 0 0 transparent;width:65px;height:24px;line-height:24px;border:0 none;color:White}
</style>
</head>
<body class="main-body">
<form  name="tableQryForm" action=""  method="POST" >
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td> <table width="100%" border="0" cellpadding="0" cellspacing="0" class="squareB" >
                <tr>
                  <td><img src="<%=request.getContextPath()%>/images/common/tab/square_corner_1.gif" width="5" height="5"></td>
                  <td background="<%=request.getContextPath()%>/images/common/tab/square_line_1.gif"></td>
                  <td><img src="<%=request.getContextPath()%>/images/common/tab/square_corner_2.gif" width="5" height="5"></td>
                </tr>
                <tr>
                  <td background="<%=request.getContextPath()%>/images/common/tab/square_line_2.gif"></td>
                  <td width="100%" valign="top">
                   <table width="100%" border="0">
                      <tr>
                        <td nowrap>起始日期</td>
                        <td nowrap>
                        	<input type="hidden" id="hBegin_date" name="hBegin_date" value="<%=begin_date%>">
                        	<input class="Wdate" type="text" id="begin_date" name="begin_date" size="15" value="<%=begin_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/>
                        </td>
                        <td nowrap>截止日期</td>
                        <td nowrap>
                       		<input type="hidden" id="hEnd_date" name="hEnd_date" value="<%=begin_date%>">
                        	<input class="Wdate" type="text" id="end_date" name="end_date" size="15" value="<%=end_date%>" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/>
                        </td>
                        <td nowrap>日志类型</td>
                        <td nowrap>
                        	<BIBM:TagSelectList listID="#" listName="oper_type" allFlag=""
						      focusID="<%=oper_type%>" selfSQL="CREATE,生成接口;UP,上传接口;DOWN,取回执;" />
                        </td>
                        <td nowrap>报表分类</td>
                        <td nowrap>
                        	<BIBM:TagSelectList listID="#" listName="report_kind" allFlag=""
						      focusID="<%=report_kind%>" selfSQL="1,总部普通上传;2,号码级上传;3,代理商接口" />
                        </td>
                      </tr>
                      <tr>
                        <td nowrap>周期类型</td>
                        <td nowrap>
                        	<BIBM:TagSelectList listID="#" listName="date_type" allFlag=""
						      focusID="<%=date_type%>" selfSQL="D,日报;W,周报;M,月报;K,快报;" />
                        </td>
                        <td nowrap>日志状态</td>
                        <td nowrap>
                        	<BIBM:TagSelectList listID="#" listName="oper_status" allFlag=""
						      focusID="<%=oper_status%>" selfSQL="0,不成功;1,成功;-1,未知或其他;" />
                        </td>
                        <td nowrap>文件名</td>
                        <td nowrap>
                        	<input type="text" name="file_name" title="支持模糊查询" value="<%=file_name%>" class="input-text" size="20">
                        </td>
                        <td align="center" colspan="2" nowrap>
                        	<input name="Submit222" type="button" class="btn3" value="查询" onclick="_onQuery();"
								onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
							<input name="Submit2222" type="reset" class="btn3" value="重置" onclick="resetValue()"
								onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
						</td>
                      </tr>
                    </table></td>
                  <td background="<%=request.getContextPath()%>/images/common/tab/square_line_3.gif"></td>
                </tr>
                <tr>
                  <td height="6"><img src="<%=request.getContextPath()%>/images/common/tab/square_corner_3.gif" width="5" height="5"></td>
                  <td background="<%=request.getContextPath()%>/images/common/tab/square_line_4.gif"></td>
                  <td><img src="<%=request.getContextPath()%>/images/common/tab/square_corner_4.gif" width="5" height="5"></td>
                </tr>
              </table> </td>
          </tr>
          <tr>
            <td height="5"></td>
          </tr>
          <tr><td>
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td>
                  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="9"></td>
                        <td nowrap></td>
                        <td width="27"></td>
                        <td  width="100%" align="right" valign="bottom">
                        	<font color=blue><b>总记录数：<%=total_num%></b></font></td>
                      </tr>
                    </table></td>
                </tr>
                <tr>
                  <td height="360" valign="top">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
                            <tr class="table-th">
                              <td class="table-item" align="center" >报表ID</td>
                              <td class="table-item" align="center" >报表名</td>
                              <td class="table-item" align="center" >文件名</td>
                              <td class="table-item" align="center" >日志类型</td>
                              <td class="table-item" align="center" >日志状态</td>
                              <td align="center" class="table-item" >状态描述</td>
                              <td align="center" class="table-item" >记录时间</td>
                            </tr>
<!--循环取出值显示-->
<%
if(list == null || list.length<=0){
%>
	<tr class="table-trb">
       <td class="table-item" align="center" colspan="7"><b><font color="red">当前条件下无上传文件</font></b></td>
    </tr>
<%
}else {
   String type = null;
   String status = null;
   String time = null;
   for(int i=0; i<list.length ;i++){
      if("CREATE".equals(list[i][2])){
    	  type = "生成接口";
      } else if("UP".equals(list[i][2])){
    	  type = "上传接口";
      } else if("DOWN".equals(list[i][2])){
    	  type = "取回执";
      }
      if("0".equals(list[i][3])){
    	  status = "<font color=red><b>不成功</b></font>";
      } else if("1".equals(list[i][3])){
    	  status = "成功";
      } else if("-1".equals(list[i][3])){
    	  status = "未知或其他";
      }
      if(list[i][4] == null || list[i][4].length() < 14){
        	time = "&nbsp;";
      } else {
        	time = list[i][5].subSequence(0,4) + "-"
                 + list[i][5].subSequence(4,6) + "-"
                 + list[i][5].subSequence(6,8) + " "
  	             + list[i][5].subSequence(8,10) + ":"
                 + list[i][5].subSequence(10,12) + ":"
                 + list[i][5].subSequence(12,14) ;
      }

      if((i+1)%2==1){
%>
   <tr class="table-tr" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
   <%}else{ %>
   <tr class="table-trb" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)">
   <%} %>
                     <td align="left" class="table-td" nowrap ><%=list[i][0]%></td>
                     <td align="left" class="table-td" nowrap ><%=list[i][6]%></td>
                     <td align="left" class="table-td" nowrap ><%=list[i][1]%></td>
                     <td align="left" class="table-td" nowrap ><%=type%></td>
                     <td align="left" class="table-td" nowrap ><%=status%></td>
                     <td align="left" class="table-td" ><%=list[i][4]%></td>
                     <td align="left" class="table-td" nowrap ><%=time%></td>
   </tr>
<%}
}%>
                        </table></td>
                     </tr>
                  </table>
       </td></tr></table>
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
