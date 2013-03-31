<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.metamanage.model.Dimension" %>

<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    Dimension info = (Dimension)request.getAttribute("list");
    String msg = (String)request.getAttribute("msg");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
    <title>维度管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/picker/menuPickerModule.js"></script>
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
			<c:if test="${list.dim_id == null || empty list.dim_id}">
			alert("请选择维度!");
			return;
			</c:if>
			obj.href="<%=request.getContextPath()%>/metamanage/dimTable.jsp?dim_id=<c:out value='${list.dim_id}'/>&dim_name=<c:out value='${list.dim_name}'/>";
		}
	}
        function checkNumber(theobj) {
            var value = theobj.value;
            var patrn = /^[-0-9.]{1,20}$/;
            if (value != null && value != "" && !patrn.exec(value)) {
                alert("请输入数字！");
                theobj.value = "";
                theobj.select();
                return false;
            }
            return true;
        }

		function BaseXmlSubmit(){}
		//动态添加方法
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
		//对象
		var baseXmlSubmit = new BaseXmlSubmit();
		//查询标示是否已经存在
        function query(value)
		{
            if (value == '') {
                alert('请输入维度标识！');
                document.form.dim_id.focus();
                return;
            }
            var doc = baseXmlSubmit.callAction("../system/regionAction.jsp?oper_type=10&dim_id="+value);
            doc = doc.replace(/^\s+|\n+$/g,'');
            if(doc== 'true')
           	{
           		alert("已存在该维度标识，请尝试其他维度标识！");
           		document.form.dim_id.value = "";
           		document.form.dim_id.focus();
           		return ;
           	}
            else if(doc== 'false')
           	{
           		alert("不存在该维度标识，可以使用！");
           		return;
           	}

            //form.method.value = "";
            //this.form.submit();
        }
         function save(type) {
            if (form.dim_id.value == '') {
                alert('请输入维度标识！');
                form.dim_id.focus();
                return;
            }
            if (form.dim_name.value == '') {
                alert('请输入维度名称！');
                form.dim_name.focus();
                return;
            }
            if (form.dim_type_id.value == '') {
                alert('请输入维度类型！');
                return;
            }
            if (form.code_id_field.value == '') {
                alert('请输入维度编码字段！');
                form.code_id_field.focus();
                return;
            }
            if (form.id_field_type.value == '') {
                alert('请输入编码字段类型！');
                form.id_field_type.focus();
                return;
            }
            if (form.code_desc_field.value == '') {
                alert('请输入维度描述字段！');
                form.code_desc_field.focus();
                return;
            }
            if(type == 2) {
                if (form.dim_id.value != form.dim_old_id.value) {
                    alert('维度标识不能修改！');
                    return;
                }
           		form.method.value = "save";
            }
            this.form.submit();
            parent.dim_frame.location.href = "dimTree.jsp";
        }

        function del() {
            form.method.value = "delete";
            this.form.submit();
            parent.dim_frame.location.href = "dimTree.jsp";
        }

		function resetForm() {
			form.dim_id.value="";
			form.dim_name.value="";
			form.dim_desc.value="";
			form.dim_type_id.value="";
			form.dim_type_name.value="";
			form.dim_unit.value="";
			form.code_id_field.value="";
			form.id_field_type.value="1";
			form.code_desc_field.value="";
			form.dim_rule.value="";
			form.dim_map_code.value="";
			form.dim_lvl.value="0";
			form.is_same_dim.value="";
			form.sequence.value="";
			form.status.value="";
			form.dim_chain_id.value="";
			form.dim_table_id.value="";
		}
    </script>
</head>
<body class="side-7">
<form name="form" method="post" action="dimDef.rptdo">
<input type="hidden" name="method" id="method"  value="add"/>
<input type="hidden" id="dim_old_id" name="dim_old_id" value='<c:out value="${list.dim_id}"/>'/>

<table width="99" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom"><img src="../images/common/taber/tab_line.gif" width="16" height="5"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
    <tr>
          <td width="5"><img src="../images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-on"><a href="javascript:void(0)" onclick="menuChanger(1,this)" class="tab-button-off-link">维度管理</a></td>
          <td width="5"><img src="../images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>

      </table></td>
    <td><img src="../biimages/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5"><img src="../images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(2,this)" class="tab-button-off-link">维度与数据表关系</a></td>
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
                   <td align="right">维度标识</td>
                   <td><input type="text" id="dim_id" name="dim_id" class="ormalField2" value='<c:out value="${list.dim_id}"/>'/>
                   	   <input type="button" class="button" value="查询" id="queryBtn" onclick="query(document.form.dim_id.value)"/>
                   </td>
                   <td align="right">维度名称</td>
                   <td><input type="text" id="dim_name" name="dim_name" class="ormalField2" value='<c:out value="${list.dim_name}"/>'/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">维度类型</td>
                   <td>
                      <input type="text" id="dim_type_name" name="dim_type_name" class="ormalField2" readonly
                             style="cursor:text" size="20" value='<c:out value="${list.dim_type_name}"/>'
                              onclick="setMenu(this, document.all.dim_type_id,'../metamanage/dimTypeTree.jsp');"/>
                       <input type="hidden" id="dim_type_id" name="dim_type_id" value='<c:out value="${list.dim_type_id}"/>' />
                   </td>
                   <td align="right">维度映射码</td>
                   <td><input type="text" id="dim_map_code" name="dim_map_code" class="ormalField2" value='<c:out value="${list.dim_map_code}"/>'/><font color="red">(前端应用编码)</font></td>
               </TR>
               <tr bgColor="#ffffff">
                   <td align="right">维度单位</td>
                   <td><input type="text" id="dim_unit" name="dim_unit" class="ormalField2" value='<c:out value="${list.dim_unit}"/>' /></td>
                   <td align="right">维度编码字段</td>
                   <td><input type="text" id="code_id_field" name="code_id_field" class="ormalField2" value='<c:out value="${list.code_id_field}"/>' /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">编码字段类型</td>
                   <td><BIBM:TagSelectList listName="id_field_type" selfSQL="1,数值型;2,字符型;" listID="#" script=" class=query-input-text" focusID="<%=info.getId_field_type() %>" /></td>
                   <td align="right">维度描述字段</td>
                   <td><input type="text" id="code_desc_field" name="code_desc_field" class="ormalField2" value='<c:out value="${list.code_desc_field}"/>'/></td>
              </TR>
               <TR bgColor="#ffffff">
                   <td align="right">维度层次</td>
                   <td><input type="text" id="dim_lvl" name="dim_lvl" class="ormalField2" value='<c:out value="${list.dim_lvl}"/>' onblur="checkNumber(this);"/></td>
                   <td align="right">是否同一维表</td>
                   <td><BIBM:TagSelectList listName="is_same_dim" allFlag="" selfSQL="Y,是;N,否;" listID="#" script=" class=query-input-text" focusID="<%=info.getIs_same_dim() %>" /></td>
              </TR>
               <TR bgColor="#ffffff">
                   <td align="right">顺序</td>
                   <td><input type="text" id="sequence" name="sequence" class="ormalField2" value='<c:out value="${list.sequence}"/>' onblur="checkNumber(this);"/></td>
                   <td align="right">是否有效</td>
                   <td><BIBM:TagSelectList listName="status" allFlag="" selfSQL="1,有效;0,无效;" listID="#" script=" class=query-input-text" focusID="<%=info.getStatus() %>" /></td>
               </tr>
               <tr bgColor="#ffffff">
                   <td align="right">维度归属链</td>
                   <td><BIBM:TagSelectList listName="dim_chain_id" allFlag="" selfSQL="select dim_chain_id,dim_chain_name from UI_META_INFO_DIM_CHAIN order by sequence" listID="0" script=" class=query-input-text" focusID="<%=info.getDim_chain_id() %>" /></td>
                   <td align="right">维度表标识</td>
                   <td><BIBM:TagSelectList listName="dim_table_id" allFlag="" selfSQL="select table_id,table_name from UI_META_INFO_TABLE_DEF where table_type_id='A01' order by table_name" listID="0" script=" class=query-input-text" focusID="<%=info.getDim_table_id() %>" /></td>
               </tr>
               <tr bgColor="#ffffff">
                   <td align="right">维度描述</td>
                   <td colspan="3"><textarea  id="dim_desc" name="dim_desc" class="ormalField2" rows="3" cols="70" ><c:out value="${list.dim_desc}"/></textarea></td>
               </tr>
               <tr bgColor="#ffffff">
                   <td align="right">维度规则</td>
                   <td colspan="3"><textarea id="dim_rule" name="dim_rule" class="ormalField2" rows="4" cols="70"><c:out value="${list.dim_rule}"/></textarea></td>
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
