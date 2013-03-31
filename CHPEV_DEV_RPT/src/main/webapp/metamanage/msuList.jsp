<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.report.util.DateUtil" %>
<%@ page import="com.ailk.bi.metamanage.model.Measure" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    Measure info = (Measure)request.getAttribute("list");
    String msg = (String)request.getAttribute("msg");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
    <title>指标管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/date/scw.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/picker/menuPickerModule.js"/>"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/XmlRPC.js" ></script>
	<style>
		.tab-button-on {
		    background-image: url(../images/common/taber/tab_bg_on.gif);
		    vertical-align: bottom;
		    font-size: 12px;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #9f4700;
		}

		.tab-button-off {
		    background-image: url(../images/common/taber/tab_bg_off.gif);
		    vertical-align: bottom;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #666666;
		}
	</style>
    <script type="text/javascript">
    <%if(msg != null && msg.length()>0) {%>
		alert('<%=msg%>');
	<%}%>

	function menuChanger(contentID,obj){
		if(contentID==2) {
			<c:if test="${list.msu_id == null || empty list.msu_id}">
			alert("请选择指标!");
			return;
			</c:if>
			obj.href="<%=request.getContextPath()%>/metamanage/msuTable.jsp?msu_id=<c:out value='${list.msu_id}'/>&msu_name=<c:out value='${list.msu_name}'/>";
		}
	}
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
        function BaseXmlSubmit(){}
        BaseXmlSubmit.prototype.callAction = function f_callAction(url)
        {
        	var dom = "";
        	try
        	{
        		var rpc = new XmlRPC(url);
        		rpc.send();
        		dom = rpc.getText();
        	}
        	catch(e)
        	{
        		alert(e.message);
        	}
        	return dom;
        }
        var baseXmlSubmit = new BaseXmlSubmit();

        function query(value)
        {
            if (value == '') {
                alert('请输入指标标识！');
                form.msu_id.focus();
                return;
            }
            var doc = baseXmlSubmit.callAction("../system/regionAction.jsp?oper_type=11&msu_id="+value);
            doc = doc.replace(/^\s+|\n+$/g,'');
            if(doc== 'true')
           	{
           		alert("已存在该指标标识，请尝试其他指标标识！");
           		document.form.msu_id.value = "";
           		document.form.msu_id.focus();
           		return ;
           	}
            else if(doc== 'false')
           	{
           		alert("不存在该指标标识，可以使用！");
           		return;
           	}

            //form.method.value = "";
            //this.form.submit();
        }
         function save(type) {
            if (form.msu_id.value == '') {
                alert('请输入指标标识！');
                form.msu_id.focus();
                return;
            }
            if (form.msu_name.value == '') {
                alert('请输入指标名称！');
                form.msu_name.focus();
                return;
            }
            if (form.msu_type_id.value == '') {
                alert('请输入指标类型！');
                return;
            }
            if (form.msu_field.value == '') {
                alert('请输入指标字段！');
                form.msu_field.focus();
                return;
            }
            if(type == 2) {
                if (form.msu_id.value != form.msu_old_id.value) {
                    alert('指标标识不能修改！');
                    return;
                }
           		form.method.value = "save";
            }
            this.form.submit();
            parent.msu_frame.location.href = "msuTree.jsp";
        }

        function del() {
            form.method.value = "delete";
            this.form.submit();
            parent.msu_frame.location.href = "msuTree.jsp";
        }

		function resetForm() {
			form.msu_id.value="";
			form.msu_name.value="";
			form.msu_desc.value="";
			form.msu_type_id.value="";
			form.msu_type_name.value="";
			form.msu_unit.value="";
			form.msu_code.value="";
			form.parent_id.value="";
			form.msu_field.value="";
			form.msu_rule.value="";
			form.msu_digit.value="";
			form.msu_rule_desc.value="";
			form.msu_app_rule.value="";
			form.msu_app_rule_desc.value="";
			form.is_calmsu.value="";
			form.is_timemsu.value="";
			form.is_deri.value="";

		}
    </script>
</head>
<body class="side-7">
<form name="form" method="post" action="msuDef.rptdo">
<input type="hidden" name="method" id="method"  value="add"/>
<input type="hidden" id="msu_old_id" name="msu_old_id" value='<c:out value="${list.msu_id}"/>'/>
<table width="99" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom"><img src="../images/common/taber/tab_line.gif" width="16" height="5"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
    <tr>
          <td width="5"><img src="../images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-on"><a href="javascript:void(0)" onclick="menuChanger(1,this)" class="tab-button-off-link">指标管理</a></td>
          <td width="5"><img src="../images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>

      </table></td>
    <td><img src="../biimages/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5"><img src="../images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(2,this)" class="tab-button-off-link">指标与数据表关系</a></td>
          <td width="5"><img src="../images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td valign="bottom"><img src="../images/common/taber/tab_right_line.gif" width="280" height="5"></td>
  </tr>
</table>
<table width="100%">

    <tr>
        <td>
            <TABLE bgColor="#999999" WIDTH="100%" CELLPADDING="4" border="0" cellspacing="1">
               <TR bgColor="#ffffff">
                   <td align="right">指标标识</td>
                   <td><input type="text" id="msu_id" name="msu_id" class="ormalField2" value='<c:out value="${list.msu_id}"/>'/>
                   	   <input type="button" class="button" value="查询" id="queryBtn" onclick="query(document.form.msu_id.value)"/>
                   </td>
                   <td align="right">指标名称</td>
                   <td><input type="text" id="msu_name" name="msu_name" class="ormalField2" value='<c:out value="${list.msu_name}"/>'/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">指标类型</td>
                   <td>
                      <input type="text" id="msu_type_name" name="msu_type_name" class="ormalField2" readonly
                             style="cursor:text" size="20" value='<c:out value="${list.msu_type_name}"/>'
                              onclick="setMenu(this, document.all.msu_type_id, 'msuTypeTree.jsp');"/>
                       <input type="hidden" id="msu_type_id" name="msu_type_id" value='<c:out value="${list.msu_type_id}"/>' />
                   </td>
                   <td align="right">指标单位</td>
                   <td><input type="text" id="msu_unit" name="msu_unit" class="ormalField2" value='<c:out value="${list.msu_unit}"/>' /></td>
               </TR>
               <tr bgColor="#ffffff">
                   <td align="right">指标编码</td>
                   <td><input type="text" id="msu_code" name="msu_code" class="ormalField2" value='<c:out value="${list.msu_code}"/>' /></td>
                   <td align="right">上级指标标识</td>
                   <td><input type="text" id="parent_id" name="parent_id" class="ormalField2" value='<c:out value="${list.parent_id}"/>' /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">指标字段</td>
                   <td><input type="text" id="msu_field" name="msu_field" class="ormalField2" value='<c:out value="${list.msu_field}"/>'/></td>
                   <td align="right">指标精度</td>
                   <td><input type="text" id="msu_digit" name="msu_digit" class="ormalField2" value='<c:out value="${list.msu_digit}"/>'/></td>
              </TR>
              <tr bgColor="#ffffff">
                   <td align="right">是否计算指标</td>
                   <td><BIBM:TagSelectList listName="is_calmsu" selfSQL="N,否;Y,是;" listID="#" script=" class=query-input-text" focusID="<%=info.getIs_calmsu() %>" /></td>
                   <td align="right">是否时点指标</td>
                   <td><BIBM:TagSelectList listName="is_timemsu" selfSQL="N,时期;Y,时点;" listID="#" script=" class=query-input-text" focusID="<%=info.getIs_timemsu() %>" /></td>
              </tr>
              <tr bgColor="#ffffff">
                   <td align="right">是否衍生指标</td>
                   <td colspan="3"><BIBM:TagSelectList listName="is_deri" selfSQL="N,否;Y,是;" listID="#" script=" class=query-input-text" focusID="<%=info.getIs_deri() %>" /></td>
              </tr>
               <tr bgColor="#ffffff">
                   <td align="right">指标描述</td>
                   <td colspan="3"><textarea  id="msu_desc" name="msu_desc" class="ormalField2" rows="3" cols="70" ><c:out value="${list.msu_desc}"/></textarea></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据口径规则</td>
                   <td colspan="3"><textarea  id="msu_rule" name="msu_rule" class="ormalField2" rows="2" cols="70" ><c:out value="${list.msu_rule}"/></textarea></td>
              </tr>
              <tr bgColor="#ffffff">
                   <td align="right">数据口径规则描述</td>
                   <td colspan="3"><textarea  id="msu_rule_desc" name="msu_rule_desc" class="ormalField2" rows="2" cols="70" ><c:out value="${list.msu_rule_desc}"/></textarea></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据应用规则</td>
                   <td colspan="3"><textarea  id="msu_app_rule" name="msu_app_rule" class="ormalField2" rows="2" cols="70" ><c:out value="${list.msu_app_rule}"/></textarea></td>
              </TR>
               <TR bgColor="#ffffff">
                   <td align="right">数据应用规则描述</td>
                   <td colspan="3"><textarea  id="msu_app_rule_desc" name="msu_app_rule_desc" class="ormalField2" rows="2" cols="70" ><c:out value="${list.msu_app_rule_desc}"/></textarea></td>
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
