<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.report.util.DateUtil" %>
<%@ page import="com.ailk.bi.system.entity.SystemInfo" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    SystemInfo info = (SystemInfo)request.getAttribute("list");
    String msg = (String)request.getAttribute("msg");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
    <title>子系统管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/date/scw.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/picker/menuPickerModule.js"/>"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'> </script>
	<script src="<%=request.getContextPath()%>/js/XmlRPC.js" language="javascript"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/TableDef.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'> </script>
    <script type="text/javascript">
    <%if(msg != null && msg.length()>0) {%>
		alert('<%=msg%>');
	<%}%>
        function checkNumber(theobj) {
            var value = theobj.value;
            var patrn = /^[-0-9.]{1,20}$/;
            if (value != null && value != "" && !patrn.exec(value)) {
                alert("请输入数字！");
                theobj.select();
                return false;
            }
            return true;
        }

        function query() {

            form.method.value = "";
            this.form.submit();
        }
         function save(type) {
            if (form.system_id.value == '') {
                alert('请输入系统标识！');
                form.system_id.focus();
                return;
            }
            if (form.system_name.value == '') {
                alert('请输入系统名称！');
                form.system_name.focus();
                return;
            }
            if(type == 2) {
                if (form.system_id.value != form.system_old_id.value) {
                    alert('系统标识不能修改！');
                    return;
                }
           		form.method.value = "save";
            }
            this.form.submit();
            parent.system_frame.location.href = "subSysTree.jsp";
        }

        function del()
        {
        	if(document.form.system_id.value == "" || document.form.system_id.value == null)
       		{
       			alert("请选择要删除的子系统！");
       			return ;
       		}
        	else
       	    {
        		if((confirm("是否确认要删除该子系统?")))
       			{
        			form.method.value = "delete";
    	            this.form.submit();
    	            parent.system_frame.location.href = "subSysTree.jsp";
       			}
        		else
       			{
       				return;
       			}
       	    }
        }

		function resetForm() {
			form.system_id.value="";
			form.system_name.value="";
			form.system_desc.value="";
			form.system_url.value="";
			form.status.value="";
			form.sequence.value="";
		}

		function BaseXmlSubmit(){}

		BaseXmlSubmit.prototype.callAction = function f_callAction(url)
		{
		  var dom = "";
		  try{
		    var rpc = new XmlRPC(url);
		    rpc.send();
		    dom = rpc.getText();
		  }
		  catch(e){
		    alert(e.message);
		  }
		  return dom;
		}

		var baseXmlSubmit = new BaseXmlSubmit();

		function CheckExsit(value,oper_type)
		{
			if (value == '' || value ==null)
			{
                alert('请输入系统标识！');
                form.system_id.focus();
                return;
            }
			var ret=false;
			var doc=baseXmlSubmit.callAction("../system/regionAction.jsp?system	_id="+value+"&oper_type="+oper_type);
			doc=doc.replace(/^\s+|\n+$/g,'');
	        if(doc == "false"){//成功
				alert("不存在相同的系统标识，您可以继续操作！");
	        }else if(doc == "true"){//不成功
				alert("存在相同的系统标识，请您尝试别的系统标识！");
				document.form.system_id.value="";
				document.form.system_id.focus();
				return;
	        }
		}
    </script>
</head>
<body class="side-7">
<form name="form" method="post" action="system.rptdo">
<input type="hidden" name="method" id="method"  value="add"/>
<input type="hidden" id="system_old_id" name="system_old_id" value='<c:out value="${list.system_id}"/>'/>
<table width="100%">
	<tr>
		<td height="22"><img src="../images/common/system/arrow7.gif" width="7"
			height="7">&nbsp;<b>子系统管理</b></td>
	</tr>
	<tr>
		<td height="1" background="../images/common/system/black-dot.gif"></td>
	</tr>

    <tr>
        <td>
            <TABLE bgColor="#999999" WIDTH="100%" CELLPADDING="4" border="0" cellspacing="1">
               <TR bgColor="#ffffff">
                   <td align="right">系统标识</td>
                   <td><input type="text" id="system_id" name="system_id" class="ormalField2" value='<c:out value="${list.system_id}"/>'/>
                   	   <input type="button" class="button" value="查询" id="queryBtn" onclick="CheckExsit(document.form.system_id.value,9)"/>
                   </td>
                   <td align="right">系统名称</td>
                   <td><input type="text" id="system_name" name="system_name" class="ormalField2" value='<c:out value="${list.system_name}"/>'/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">系统描述</td>
                   <td><input type="text" id="system_desc" name="system_desc" class="ormalField2" value='<c:out value="${list.system_desc}"/>'/></td>
                   <td align="right">系统URL</td>
                   <td><input type="text" id="system_url" name="system_url" class="ormalField2" value='<c:out value="${list.system_url}"/>'/></td>
			   </tr>
			   </TR>
               <tr bgColor="#ffffff">
                   <td align="right">是否有效</td>
                   <td><BIBM:TagSelectList listName="status" allFlag="" selfSQL="1,有效;0,无效;" listID="#" script=" class=query-input-text" focusID="<%=info.getStatus() %>" /></td>
                   <td align="right">显示顺序</td>
                   <td><input type="text" id="sequence" name="sequence" class="ormalField2" value='<c:out value="${list.sequence}"/>'/></td>
                </tr>
            </table>
        </td>
    </tr>
	<tr align="center">
		<td>
              <input type="button" class="button" value="新增" id="addBtn" onclick="save(1)"/>
              <input type="button" class="button" value="修改" id="saveBtn" onclick="save(2)"/>
              <input type="button" class="button" value="清空" id="resetBtn" name="resetBtn" onclick="resetForm()">
              <input type="button" class="button" value="删除" id="delBtn" onclick="del()"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>
