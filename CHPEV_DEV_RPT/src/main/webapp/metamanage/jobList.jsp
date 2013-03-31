<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.metamanage.model.EtlJob" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    EtlJob info = (EtlJob)request.getAttribute("list");
    String msg = (String)request.getAttribute("msg");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
    <title>ETL调度管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/date/scw.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/picker/menuPickerModule.js"/>"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'> </script>
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
            if (form.job_id.value == '') {
                alert('请输入作业标识！');
                form.job_id.focus();
                return;
            }
            form.method.value = "";
            this.form.submit();
        }
         function save(type) {
            if (form.job_id.value == '') {
                alert('请输入作业标识！');
                form.job_id.focus();
                return;
            }
            if (form.job_name.value == '') {
                alert('请输入作业名称！');
                form.job_name.focus();
                return;
            }
            if (form.flow_id.value == '') {
                alert('请选择作业分层！');
                form.flow_id.focus();
                return;
            }
            if(type == 2) {
                if (form.job_id.value != form.job_old_id.value) {
                    alert('作业标识不能修改！');
                    return;
                }
           		form.method.value = "save";
            }
            this.form.submit();
            parent.job_frame.location.href = "jobTree.jsp";
        }

        function del() {
        	if(confirm("确定要删除任务？"))
        		{
	        		form.method.value = "delete";
	                this.form.submit();
	                parent.job_frame.location.href = "jobTree.jsp";
        		}
        }

		function resetForm() {
			form.job_id.value="";
			form.job_name.value="";
			form.job_desc.value="";
			form.job_rule.value="";
			form.flow_id.value="";
			form.status.value="";
			form.in_table_id.value="";
			form.out_table_id.value="";
			form.job_cron.value="";
		}
    </script>
</head>
<body class="side-7">
<form name="form" method="post" action="etlJob.rptdo">
<input type="hidden" name="method" id="method"  value="add"/>
<input type="hidden" id="job_old_id" name="job_old_id" value='<c:out value="${list.job_id}"/>'/>
<table width="100%">
	<tr>
		<td height="22"><img src="<%=request.getContextPath()%>/images/common/system/arrow7.gif" width="7"
			height="7">&nbsp;<b>ETL调度管理</b></td>
	</tr>
	<tr>
		<td height="1" background="<%=request.getContextPath()%>/images/common/system/black-dot.gif"></td>
	</tr>

    <tr>
        <td>
            <TABLE bgColor="#999999" WIDTH="100%" CELLPADDING="4" border="0" cellspacing="1">
               <TR bgColor="#ffffff">
                   <td align="right">作业标识</td>
                   <td><input type="text" id="job_id" name="job_id" class="ormalField2" value='<c:out value="${list.job_id}"/>'/>
                   	   <input type="button" class="button" value="查询" id="queryBtn" onclick="query()"/>
                   </td>
                   <td align="right">作业名称</td>
                   <td><input type="text" id="job_name" name="job_name" class="ormalField2" value='<c:out value="${list.job_name}"/>'/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">作业描述</td>
                   <td><input type="text" id="job_desc" name="job_desc" class="ormalField2" value='<c:out value="${list.job_desc}"/>'/></td>
                   <td align="right">作业规则</td>
                   <td><input type="text" id="job_rule" name="job_rule" class="ormalField2" value='<c:out value="${list.job_rule}"/>' /></td>
               </TR>
               <tr bgColor="#ffffff">
                   <td align="right">作业分层</td>
                   <td><BIBM:TagSelectList listName="flow_id" allFlag="" selfSQL="select flow_id,flow_name from UI_META_INFO_DATA_FLOW order by sequence" listID="0" script=" class=query-input-text" focusID="<%=info.getFlow_id() %>" /></td>
                   <td align="right">作业状态</td>
                   <td><BIBM:TagSelectList listName="status" allFlag="" selfSQL="1,有效;0,无效;" listID="#" script=" class=query-input-text" focusID="<%=info.getStatus() %>" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">调度规则</td>
                   <td colspan="3"><input type="text" id="job_cron" name="job_cron" class="ormalField2" value='<c:out value="${list.job_cron}"/>'/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">作业入参标识</td>
                   <td colspan="3"><input type="text" id="in_table_id" size="100" name="in_table_id" class="ormalField2" value='<c:out value="${list.in_table_id}"/>'/></td>
               </TR>
               <TR bgColor="#ffffff">
                   <td align="right">作业出参标识</td>
                   <td colspan="3"><input type="text" id="out_table_id" size="100" name="out_table_id" class="ormalField2" value='<c:out value="${list.out_table_id}"/>'/></td>
              </TR>

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
