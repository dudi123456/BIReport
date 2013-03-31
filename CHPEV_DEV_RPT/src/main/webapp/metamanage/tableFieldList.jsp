<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    String msg = (String)request.getAttribute("msg");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache, must-revalidate"/>
    <meta http-equiv="expires" content="0"/>
    <title>数据源管理</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/syscss.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/date/scw.js"/>"></script>
	<style>
		.tab-button-on {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_on.gif);
		    vertical-align: bottom;
		    font-size: 12px;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #9f4700;
		}

		.tab-button-off {
		    background-image: url(<%=request.getContextPath()%>/images/common/taber/tab_bg_off.gif);
		    vertical-align: bottom;
		    line-height: 22px;
		    padding-left: 10px;
		    padding-right: 10px;
		    color: #666666;
		}
		.celtitle {
		    color: #404040;
		    height: 24px;
		    text-align: right;
		    background-color: #CFCFCF;
		}

		.celtitle td {
		    padding: 2px 8px 0 8px;
		}
		.FixedTitleRow {
		    position: relative;
		    top: expression(this.offsetParent.scrollTop);
		    z-index: 10;
		}
		.title-bold  { COLOR: #AA8000; FONT-SIZE: 12px; font-weight: bold; TEXT-DECORATION: none; }
	</style>
    <script type="text/javascript">
    <%if(msg != null && msg.length()>0) {%>
		alert('<%=msg%>');
	<%}%>
		function menuChanger(contentID,obj){

			if(contentID==1) {
				obj.href="<%=request.getContextPath()%>/metamanage/tableDef.rptdo?table_id=<%=request.getParameter("table_id")%>";
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

         function save(type) {

            if (form.field_id.value == '') {
                alert('请输入数据表名称！');
                form.field_id.focus();
                return;
            }
            if(type == 2) {
           		form.method.value = "save";
            }
            this.form.submit();
        }

        function del() {
            form.method.value = "delete";
            this.form.submit();
        }
      //显示选中的对应值
        function showValues(value){
        	var arr = value.split("$$");
        	var field = new Array('field_id','field_name','filed_desc','filed_type','filed_en_code','data_type','data_length','data_precision','field_rule','field_rule_desc','filed_column_id','field_unit');
			for(var i=0;i<field.length;i++) {
	        	var obj = eval("document.form."+field[i]);
	        	obj.value = arr[i];
			}
        }
    </script>
</head>
<body class="side-7">
<form name="form" method="post" action="tableField.rptdo">
<input type="hidden" name="method" id="method"  value="add"/>
<input type="hidden" name="table_id" id="table_id"  value="<%=request.getParameter("table_id") %>"/>
<input type="hidden" name="table_name" id="table_name"  value="<%=request.getParameter("table_name") %>"/>
<table width="99" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_line.gif" width="16" height="5"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
    <tr>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(1,this)" class="tab-button-off-link">数据源管理</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>

      </table></td>
    <td><img src="<%=request.getContextPath()%>/images/common/system/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-on"><a href="javascript:void(0)" onclick="menuChanger(2,this)" class="tab-button-off-link">数据源字段管理</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_line.gif" width="280" height="5"></td>
  </tr>
</table>
<table width="100%">
	<tr>
		<td height="22"><img src="<%=request.getContextPath()%>/images/common/system/arrow7.gif" width="7"
			height="7"> <b>数据表：<%=request.getParameter("table_name")%></b>&nbsp;</td>
		<TD height="22" align="right"></td>
		<td height="22" align="left"></TD>
	</tr>
	<tr>
		<td height="1" background="<%=request.getContextPath()%>/images/common/system/black-dot.gif" colspan="3"></td>
	</tr>
</table>
<table width="100%" >
  <tr>
    <td width="22"><img src="<%=request.getContextPath()%>/images/common/system/icon_ztfx3.gif" width="16" height="16"></td>
    <td width="100" valign="bottom" nowrap><span class="title-bold">字段信息</span></td>
    <td ><span class="title-bold"><img src="<%=request.getContextPath()%>/images/common/system/broken-line.gif"></span></td>
    <td width="54" id="y_1" style="display:">
    </td>
   </tr>
</table>
<div id="oppparamdiv" style="width: 100%; margin-top: 2px; overflow: auto; height:266px;">
<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="table-bg">
     <tr align="center"  class="celtitle FixedTitleRow">
            <td class="tab-title" align="center">选择</td>
            <td class="tab-title" align="center">字段标识</td>
            <td  class="tab-title" align="center">字段名称</td>
            <td class="tab-title"  align="center">字段描述</td>
            <td class="tab-title"  align="center">字段属性</td>
            <td class="tab-title"  align="center">字段英文码</td>
            <td class="tab-title"  align="center">数据类型</td>
            <td class="tab-title"  align="center">数据长度</td>
            <td class="tab-title" align="center">数据精度</td>
            <td class="tab-title"  align="center">字段规则</td>
            <td class="tab-title" align="center">字段规则描述</td>
            <td class="tab-title"  align="center">字段顺序</td>
            <td class="tab-title"  align="center">字段单位</td>
        </tr>
 <c:if test="${list != null && !empty list}">
     <c:forEach var="item" items="${list}" >
         <TR class="table-white-bg" align="center">
             <td  align="center">&nbsp;
                 <input type="radio" name="chk" onclick="showValues('<c:out value="${item.PARAM}" />')"/>
             </td>
             <td align="center"><c:out value="${item.FIELD_ID}"/></td>
             <td align="center"><c:out value="${item.FIELD_NAME}"/></td>
             <td align="center"><c:out value="${item.FILED_DESC}"/></td>
             <td align="center">
				  <c:choose>
				  	  <c:when test="${item.FILED_TYPE == '1'}">
						维度
					  </c:when>
				  	  <c:when test="${item.FILED_TYPE == '2'}">
						指标
					  </c:when>
					  <c:when test="${item.FILED_TYPE == '3'}">
						状态
				  	  </c:when>
				  </c:choose>
             </td>
             <td align="center"><c:out value="${item.FILED_EN_CODE}"/></td>
             <td align="center">
				  <c:choose>
				  	  <c:when test="${item.DATA_TYPE == '1'}">
						 数值型
					  </c:when>
					  <c:when test="${item.DATA_TYPE == '2'}">
						 字符型
				  	  </c:when>
				  </c:choose>
             </td>
             <td align="center"><c:out value="${item.DATA_LENGTH}"/></td>
             <td align="center"><c:out value="${item.DATA_PRECISION}"/></td>
             <td align="center"><c:out value="${item.FIELD_RULE}"/></td>
             <td align="center"><c:out value="${item.FIELD_RULE_DESC}"/></td>
             <td align="center"><c:out value="${item.FILED_COLUMN_ID}"/></td>
             <td align="center"><c:out value="${item.FIELD_UNIT}"/></td>
         </tr>
     </c:forEach>
 </c:if>
 <c:if test="${list == null || empty list}">
     <TR class="table-white-bg" align="center">
         <td align="center" colspan="14">
             <span style="font-weight:bold;color:red;">没有记录！！</span>
         </td>
     </tr>
 </c:if>
</table>
</div>
<table width="100%" >
<tr>
    <td width="22"><img src="<%=request.getContextPath()%>/images/common/system/feedback-ioc1.gif" width="22" height="14"></td>
    <td width="100" valign="bottom" nowrap><span class="title-bold">&nbsp;编辑区</span></td>
    <td ><span class="title-bold"><img src="<%=request.getContextPath()%>/images/common/system/broken-line.gif" ></span></td>
    <td width="54" id="y_1" style="display:">
</tr>

</table>
<table width="100%">
    <tr>
        <td>
            <TABLE bgColor="#999999" WIDTH="100%" CELLPADDING="4" border="0" cellspacing="1">
               <TR bgColor="#ffffff">
                   <td align="right">字段标识</td>
                   <td><input type="text" id="field_id" name="field_id" class="ormalField2" /></td>
                   <td align="right">字段名称</td>
                   <td><input type="text" id="field_name" name="field_name" class="ormalField2" /></td>
                   <td align="right">字段描述</td>
                   <td><input type="text" id="filed_desc" name="filed_desc" class="ormalField2" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">字段属性分类</td>
                   <td><BIBM:TagSelectList listName="filed_type" allFlag="" selfSQL="1,维度;2,指标;3,状态;" listID="#" script=" class=query-input-text"/></td>
                   <td align="right">字段英文码</td>
                   <td><input type="text" id="filed_en_code" name="filed_en_code" class="ormalField2" /></td>
                   <td align="right">数据类型</td>
                   <td><BIBM:TagSelectList listName="data_type" allFlag="" selfSQL="1,数值型;2,字符型;" listID="#" script=" class=query-input-text" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据长度</td>
                   <td><input type="text" id="data_length" name="data_length" class="ormalField2" onblur="checkNumber(this);" /></td>
                   <td align="right">数据精度</td>
                   <td><input type="text" id="data_precision" name="data_precision" class="ormalField2" onblur="checkNumber(this);"/></td>
                   <td align="right">字段顺序</td>
                   <td><input type="text" id="filed_column_id" name="filed_column_id" class="ormalField2" onblur="checkNumber(this);"/></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">字段单位</td>
                   <td><input type="text" id="field_unit" name="field_unit" class="ormalField2" /></td>
                   <td align="right">字段规则</td>
                   <td><input type="text" id="field_rule" name="field_rule" class="ormalField2" /></td>
                   <td align="right">字段规则描述</td>
                   <td><input type="text" id="field_rule_desc" name="field_rule_desc" class="ormalField2" /></td>
              </TR>
            </table>
        </td>
    </tr>
	<tr align="center">
		<td>
              <input type="button" class="button" value="新增" id="addBtn" onclick="save(1)"/>
              <input type="button" class="button" value="修改" id="saveBtn" onclick="save(2)"/>
              <input type="reset" class="button" value="清空" id="resetBtn" name="resetBtn" >
              <input type="button" class="button" value="删除" id="delBtn" onclick="del()"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>
