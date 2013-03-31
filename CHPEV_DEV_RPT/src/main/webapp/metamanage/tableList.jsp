<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@ page import="com.ailk.bi.metamanage.model.TableDef" %>
<%
    response.setHeader("Cache-Control", "no-stored");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    TableDef info = (TableDef)request.getAttribute("list");
    String domainSql = (String)request.getAttribute("domainSql");
    String jobSql = (String)request.getAttribute("jobSql");
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
    <script type="text/javascript" src="<c:url value="/js/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/date/scw.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/picker/menuPickerModule.js"/>"></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'> </script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/TableDef.js'></script>
	<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'> </script>
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
	</style>
    <script type="text/javascript">
    <%if(msg != null && msg.length()>0) {%>
		alert('<%=msg%>');
	<%}%>
	function menuChanger(contentID,obj){

		if(contentID==2) {
			<c:if test="${list.table_id == null || empty list.table_id}">
			alert("请选择数据源表!");
			return;
			</c:if>

			obj.href="<%=request.getContextPath()%>/metamanage/tableField.rptdo?table_id=<c:out value='${list.table_id}'/>&table_name=<c:out value='${list.table_name}'/>";
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

        function query() {
            if (form.table_id.value == '') {
                alert('请输入数据表标识！');
                form.table_id.focus();
                return;
            }
            form.method.value = "";
            this.form.submit();
        }
         function save(type) {
            if (form.table_id.value == '') {
                alert('请输入数据表标识！');
                form.table_id.focus();
                return;
            }
            if (form.table_name.value == '') {
                alert('请输入数据表名称！');
                form.table_name.focus();
                return;
            }
            if (form.layer_id.value == '') {
                alert('请选择数据分层！');
  //              form.layer_id.focus();
                return;
            }
            if(type == 2) {
                if (form.table_id.value != form.table_old_id.value) {
                    alert('数据表标识不能修改！');
                    return;
                }
           		form.method.value = "save";
            }
            this.form.submit();
            parent.table_frame.location.href = "tableTree.jsp";
        }

        function del() {
            form.method.value = "delete";
            this.form.submit();
            parent.table_frame.location.href = "tableTree.jsp";
        }
        function getMenu(menuName, menuId){
        	outObj.value = menuName;
        	hidObj.value = menuId;
        	getDomain();
        	closeLayerMenu(menuId);
        }
		function getDomain(){
			TableDef.getDomain(DWRUtil.getValue("layer_id"),updateDomain);

		}
		function updateDomain(data){
			DWRUtil.removeAllOptions("domain_id");
		    DWRUtil.addOptions("domain_id", {'':'全部'});
		    DWRUtil.addOptions("domain_id", data,0,1);
		}

		function getEtlJob(){
			TableDef.getEtlJob(DWRUtil.getValue("flow_id"),updateEtlJob);

		}
		function updateEtlJob(data){
			DWRUtil.removeAllOptions("job_id");
		    DWRUtil.addOptions("job_id", {'':'全部'});
		    DWRUtil.addOptions("job_id", data,0,1);
		}
		function resetForm() {
			form.table_id.value="";
			form.table_type_id.value="";
			form.flow_id.value="";
			form.table_name.value="";
			form.table_desc.value="";
			form.data_cycle.value="";
			form.layer_id.value="";
			form.layer_name.value="";
			form.domain_id.value="";
			form.sys_id.value="";
			form.job_id.value="";
			form.last_data_date.value="";
			form.tablespace_id.value="";
			form.is_source_flag.value="";
			form.app_type.value="";
			form.app_script.value="";
			form.app_table_ids.value="";
		}
    </script>
</head>
<body class="side-7">
<form name="form" method="post" action="tableDef.rptdo">
<input type="hidden" name="method" id="method"  value="add"/>
<input type="hidden" id="table_old_id" name="table_old_id" value='<c:out value="${list.table_id}"/>'/>
<table width="99" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_line.gif" width="16" height="5"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
    <tr>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-on"><a href="javascript:void(0)" onclick="menuChanger(1,this)" class="tab-button-off-link">数据源管理</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>

      </table></td>
    <td><img src="<%=request.getContextPath()%>/images/common/system/size.gif" width="1"></td>
    <td><table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_left_off.gif" width="5" height="22"></td>
          <td nowrap class="tab-button-off"><a href="javascript:void(0)" onclick="menuChanger(2,this)" class="tab-button-off-link">数据源字段管理</a></td>
          <td width="5"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_off.gif" width="5" height="22"></td>
        </tr>
      </table></td>
    <td valign="bottom"><img src="<%=request.getContextPath()%>/images/common/taber/tab_right_line.gif" width="280" height="5"></td>
  </tr>
</table>
<table width="100%">


    <tr>
        <td>
            <TABLE bgColor="#999999" WIDTH="100%" CELLPADDING="4" border="0" cellspacing="1">
               <TR bgColor="#ffffff">
                   <td align="right">数据表标识</td>
                   <td><input type="text" id="table_id" name="table_id" class="ormalField2" value='<c:out value="${list.table_id}"/>'/>
                   	   <input type="button" class="button" value="查询" id="queryBtn" onclick="query()"/>
                   </td>
                   <td align="right">数据表类型</td>
                   <td><BIBM:TagSelectList listName="table_type_id" allFlag="" selfSQL="select table_type_id,table_type_name from UI_META_INFO_TABLE_TYPE order by sequence" listID="0" script=" class=query-input-text" focusID="<%=info.getTable_type_id() %>" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据流向</td>
                   <td><BIBM:TagSelectList listName="flow_id" allFlag="" selfSQL="select flow_id,flow_name from UI_META_INFO_DATA_FLOW order by sequence" listID="0" script=" class=query-input-text onchange=getEtlJob()" focusID="<%=info.getFlow_id() %>" /></td>
                   <td align="right">数据表名称</td>
                   <td><input type="text" id="table_name" name="table_name" class="ormalField2" value='<c:out value="${list.table_name}"/>' /></td>
               </TR>
               <tr bgColor="#ffffff">
                   <td align="right">数据表描述</td>
                   <td><input type="text" id="table_desc" name="table_desc" class="ormalField2" value='<c:out value="${list.table_desc}"/>'/></td>
                   <td align="right">数据统计周期</td>
                   <td><BIBM:TagSelectList listName="data_cycle" allFlag="" selfSQL="4,月;6,日;" listID="#" script=" class=query-input-text" focusID="<%=info.getData_cycle() %>" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据分层</td>
                   <td>
                      <input type="text" id="layer_name" name="layer_name" class="ormalField2" readonly
                             style="cursor:text" size="20" value='<c:out value="${list.layer_name}"/>'
                              onclick="setMenu(this, document.all.layer_id, 'layerTree.jsp');"/>
                       <input type="hidden" id="layer_id" name="layer_id" value='<c:out value="${list.layer_id}"/>' />
					</td>
                   <td align="right">数据域</td>
                   <td><BIBM:TagSelectList listName="domain_id" allFlag="" selfSQL="<%=domainSql %>" listID="0" script=" class=query-input-text" focusID="<%=info.getDomain_id() %>" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据接口系统</td>
                   <td><BIBM:TagSelectList listName="sys_id" allFlag="" selfSQL="select sys_id,sys_name from UI_META_INFO_BUSI_SYS order by sequence" listID="0" script=" class=query-input-text" focusID="<%=info.getSys_id() %>" /></td>
                   <td align="right">ETL作业标识</td>
                   <td><BIBM:TagSelectList listName="job_id" allFlag="" selfSQL="<%=jobSql %>" listID="0" script=" class=query-input-text" focusID="<%=info.getJob_id() %>" /></td>
              </TR>
              <tr bgColor="#ffffff">
                   <td align="right">最近采集日期</td>
                   <td><input type="text" id="last_data_date" name="last_data_date" class="ormalField2"
                              onblur="checkNumber(this);" onclick="scwShow(this,this);" value='<c:out value="${list.last_data_date}"/>'/></td>
                   <td align="right">所在表空间</td>
                   <td><BIBM:TagSelectList listName="tablespace_id" allFlag="" selfSQL="select tablespace_id,tablespace_name from UI_META_INFO_TABLESPACE" listID="0" script=" class=query-input-text" focusID="<%=info.getTablespace_id() %>" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">是否数据源</td>
                   <td><BIBM:TagSelectList listName="is_source_flag" allFlag="" selfSQL="1,是;0,否;" listID="#" script=" class=query-input-text"  focusID="<%=info.getIs_source_flag() %>" /></td>
                   <td align="right">数据聚合方式</td>
                   <td><BIBM:TagSelectList listName="app_type" allFlag="" selfSQL="JOB,作业调度生成;SQL,SQL聚合;" listID="#" script=" class=query-input-text"  focusID="<%=info.getApp_type() %>" /></td>
               </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据聚合脚本</td>
                   <td colspan="3"><textarea  id="app_script" name="app_script" class="ormalField2" rows="3" cols="70" ><c:out value="${list.app_script}"/></textarea></td>
                </tr>
               <TR bgColor="#ffffff">
                   <td align="right">数据聚合表标识</td>
                   <td colspan="3"><input type="text" id="app_table_ids" name="app_table_ids" size="70" class="ormalField2" value='<c:out value="${list.app_table_ids}"/>'/></td>
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
