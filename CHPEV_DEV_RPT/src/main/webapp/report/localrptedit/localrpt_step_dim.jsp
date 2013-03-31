<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ailk.bi.base.util.StringTool"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.base.table.DimensionTable"%>
<%@page import="com.ailk.bi.report.domain.RptColDictTable"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.service.ILReportService"%>
<%@page import="com.ailk.bi.report.service.impl.LReportServiceImpl"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表维度信息</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<%
//报表对象
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
//报表ID
String rpt_id = rptTable.rpt_id;
//以下是描述定制报表的信息内容
RptColDictTable[] tmprs = (RptColDictTable[])session.getAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE);
List listDict = null;
if(tmprs!=null&&tmprs.length>0){
	listDict = new ArrayList();
}
for(int i=0;tmprs!=null&&i<tmprs.length;i++){
	if(ReportConsts.DATA_TYPE_STRING.equals(tmprs[i].data_type)){
		listDict.add(tmprs[i]);
	}
}
RptColDictTable[] rptDims = null;
if(listDict!=null&&listDict.size()>0){
	rptDims = (RptColDictTable[])listDict.toArray(new RptColDictTable[listDict.size()]);
}
int colNum = 0;
if (null != rptDims) {
	colNum = rptDims.length;
}
String strRptDim = StringTool.changArrToStrAll(rptDims, "msu_id");
if (strRptDim != null && !"".equals(strRptDim.trim())) {
	strRptDim = "," + strRptDim + ",";
}

//定义报表操作对象
ILReportService rptService = new LReportServiceImpl();
//指标相关维度
DimensionTable[] customRptDims = rptService.getCustomRptDimTable(rpt_id);
%>
</head>
<body>
<FORM METHOD="POST" name="form1" action="editLocalReport.rptdo">
<table width="100%" height="100%" border="0" cellpadding="0"
  cellspacing="0">
  <tr>
    <td valign="top" class="squareB">
    <table width="100%" height="100%" border="0" cellpadding="0"
      cellspacing="0">
      <tr>
        <td width="5" height="7"
          background="../biimages/square_line_2.gif"></td>
        <td width="100%" colspan="2"></td>
        <td><img src="../biimages/tab/field_upline_right.gif"
          width="5" height="7"></td>
      </tr>
      <tr>
        <td height="7" background="../biimages/square_line_2.gif">&nbsp;</td>
        <td height="100%" colspan="2" valign="top">
        <table width="95%" align="center" cellpadding="0" cellspacing="0">
          <%if("Y".equals(rptTable.status)){%>
          <tr>
            <td height="30" colspan="3" align="center">
              <font color="red">该报表已经通过认证，不能对维度进行增删操作！</font>
            </td>
          </tr>
          <%}%>
          <tr>
            <td colspan="2" class="tree">可选择维度：</td>
            <td class="tree">选择的维度：</td>
          </tr>
          <tr>
            <td width="20%" valign="top">
            <select name="left_list" size="15" multiple style="width:100%" class="input-select">
              <%
              for (int i = 0; customRptDims != null && i < customRptDims.length; i++) {
              %>
              <%
              if (strRptDim.indexOf(","+customRptDims[i].dim_id+",") < 0) {
              %>
              <option value="<%=customRptDims[i].dim_id%>|<%=customRptDims[i].dim_table%>|<%=customRptDims[i].code_idfld%>|<%=customRptDims[i].code_descfld%>"><%=customRptDims[i].dim_name%></option>
              <%
              	}
              	}
              %>
            </select></td>
            <td width="10%" align="center">
            <%if(!"Y".equals(rptTable.status)){%>
            <input name="right" type="button" class="button1"
              onMouseOver="switchClass(this)"
              onMouseOut="switchClass(this)" title="右移" value="&gt;"
              onClick="addMusToReport()">
            <%} %>
            </td>
            <td width="70%" valign="top" class="side-2">
            <!-- 维度定义表格开始 -->
            <table id="tbReport" width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr id="tbHead" class="table-th">
                <td align="center" nowrap class="table-item" height="20">纬度名称</td>
                <td align="center" nowrap class="table-item2">操作</td>
              </tr>
              <%if(rptDims==null||rptDims.length==0){%>
              <tr id="nullmsg">
                <td colspan="4" class="table-td" height="18" align="center">
                  还没有选择任何纬度
                </td>
              </tr>
              <%}%>
              <%
              		for (int i = 0; rptDims != null && i < rptDims.length; i++) {
              			RptColDictTable rsInfo = (RptColDictTable) rptDims[i];
              %>
              <tr id="tr_<%=i%>">
                <td class="table-td">
                <input type="text" name="field_title_<%=i%>" value="<%=rsInfo.field_title%>" />
                <input type="hidden" name="col_id" value="<%=i%>" />
                <input type="hidden" name="default_display_<%=i%>" value="<%=rsInfo.default_display %>" />
                <input type="hidden" name="dim_code_display_<%=i%>" value="<%=rsInfo.dim_code_display %>" />
                <input type="hidden" name="is_expand_col_<%=i%>" value="<%=rsInfo.is_expand_col %>" />
                <input type="hidden" name="is_subsum_<%=i%>" value="<%=rsInfo.is_subsum %>" />
                <input type="hidden" name="valuable_sum_<%=i%>" value="<%=rsInfo.valuable_sum %>" />
                <input type="hidden" name="field_dim_code_<%=i%>" value="<%=rsInfo.field_dim_code %>" />
                <input type="hidden" name="field_code_<%=i%>" value="<%=rsInfo.field_code %>" />
                <input type="hidden" name="msu_unit_<%=i%>" value="<%=rsInfo.msu_unit %>" />
                <input type="hidden" name="comma_splitted_<%=i%>" value="<%=rsInfo.comma_splitted %>" />
                <input type="hidden" name="zero_proc_<%=i%>" value="<%=rsInfo.zero_proc %>" />
                <input type="hidden" name="ratio_displayed_<%=i%>" value="<%=rsInfo.ratio_displayed %>" />
                <input type="hidden" name="has_comratio_<%=i%>" value="<%=rsInfo.has_comratio %>" />
                <input type="hidden" name="has_same_<%=i%>" value="<%=rsInfo.has_same %>" />
                <input type="hidden" name="has_last_<%=i%>" value="<%=rsInfo.has_last %>" />
                <input type="hidden" name="has_link_<%=i%>" value="<%=rsInfo.has_link %>" />
                <input type="hidden" name="link_url_<%=i%>" value="<%=rsInfo.link_url %>" />
                <input type="hidden" name="link_target_<%=i%>" value="<%=rsInfo.link_target %>" />
                <input type="hidden" name="data_order_<%=i%>" value="<%=rsInfo.data_order %>" />
                <input type="hidden" name="sms_flag_<%=i%>" value="<%=rsInfo.sms_flag %>" />
                <input type="hidden" name="td_wrap_<%=i%>" value="<%=rsInfo.td_wrap %>" />
                <input type="hidden" name="title_style_<%=i%>" value="<%=rsInfo.title_style %>" />
                <input type="hidden" name="col_style_<%=i%>" value="<%=rsInfo.col_style %>" />
                <input type="hidden" name="print_title_style_<%=i%>" value="<%=rsInfo.print_title_style %>" />
                <input type="hidden" name="print_col_style_<%=i%>" value="<%=rsInfo.print_col_style %>" />
                <input type="hidden" name="need_alert_<%=i%>" value="<%=rsInfo.need_alert %>" />
                <input type="hidden" name="compare_to_<%=i%>" value="<%=rsInfo.compare_to %>" />
                <input type="hidden" name="ratio_compare_<%=i%>" value="<%=rsInfo.ratio_compare %>" />
                <input type="hidden" name="high_value_<%=i%>" value="<%=rsInfo.high_value %>" />
                <input type="hidden" name="low_value_<%=i%>" value="<%=rsInfo.low_value %>" />
                <input type="hidden" name="alert_mode_<%=i%>" value="<%=rsInfo.alert_mode %>" />
                <input type="hidden" name="rise_color_<%=i%>" value="<%=rsInfo.rise_color %>" />
                <input type="hidden" name="down_color_<%=i%>" value="<%=rsInfo.down_color %>" />
                <input type="hidden" name="status_<%=i%>" value="<%=rsInfo.status %>" />

                <input type="hidden" name="is_msu_<%=i%>" value="<%=rsInfo.is_msu%>" />
                <input type="hidden" name="is_user_msu_<%=i%>" value="<%=rsInfo.is_user_msu%>" />
                <input type="hidden" name="msu_id_<%=i%>" value="<%=rsInfo.msu_id%>" />
                <input type="hidden" name="datatable_<%=i%>" value="<%=rsInfo.datatable%>" />
                <input type="hidden" name="msu_length_<%=i%>" value="<%=rsInfo.msu_length%>" />
                </td>
                <%if("Y".equals(rptTable.status)){%>
                <td align="center" class="table-td2">
                <TABLE cellpadding="1" cellspacing="0">
                  <TR>
                    <TD align="center">
                    <IMG SRC="../images/common/report/up.gif" TITLE="上移" style="cursor:hand" onclick="moveUp('<%="tr_"+i%>')"></TD>
                    <TD align="center">
                    <IMG SRC="../images/common/report/down.gif" TITLE="下移" style="cursor:hand" onclick="moveDown('<%="tr_"+i%>')"></TD>
                  </TR>
                </TABLE>
                </td>
                <%}else{%>
                <td align="center" class="table-td2">
                <TABLE cellpadding="1" cellspacing="0">
                  <TR>
                    <TD align="center">
                    <IMG SRC="../images/common/report/delete.gif" TITLE="删除" style="cursor:hand" onclick="removeTableTr('<%="tr_"+i%>','<%=rsInfo.msu_id%>','<%=rsInfo.field_title%>')"></TD>
                    <TD align="center">
                    <IMG SRC="../images/common/report/up.gif" TITLE="上移" style="cursor:hand" onclick="moveUp('<%="tr_"+i%>')"></TD>
                    <TD align="center">
                    <IMG SRC="../images/common/report/down.gif" TITLE="下移" style="cursor:hand" onclick="moveDown('<%="tr_"+i%>')"></TD>
                  </TR>
                </TABLE>
                </td>
                <%} %>
              </tr>
              <%}%>
            </table>
            <!-- 纬度定义表格结束 --></td>
          </tr>
        </table>
        </td>
        <td background="../biimages/square_line_3.gif">&nbsp;</td>
      </tr>
      <tr>
        <td height="5"><img src="../biimages/square_corner_3.gif"
          width="5" height="5"></td>
        <td colspan="2" background="../biimages/square_line_4.gif"></td>
        <td><img src="../biimages/square_corner_4.gif" width="5"
          height="5"></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td height="35" align="center" valign="bottom">
	  <input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
		value="保存" onclick="setSubmitFlag('customrpt_dim','save','current')">
	  <input name="pre" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		value="上一步" onclick="setSubmitFlag('customrpt_dim','save','pre')">
	  <input name="next" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		value="下一步" onclick="setSubmitFlag('customrpt_dim','save','next')">
    </td>
  </tr>
</table>
<input type="hidden" name="rpt_id" value="<%=rpt_id%>" />
<input type="hidden" name="rowCount" value="<%=colNum %>" />
<INPUT type="hidden" id="opType" name="opType" value="" />
<INPUT type="hidden" id="opSubmit" name="opSubmit" value="" />
<INPUT type="hidden" id="opDirection" name="opDirection" value="" />
</FORM>
</body>
<script language="JavaScript">
	function setSubmitFlag(type,submit,direction){
	  document.form1.opType.value = type;
	  document.form1.opSubmit.value = submit;
	  document.form1.opDirection.value = direction;
	  if(submit == 'save'){
	  	document.form1.submit();
      }
	}

    //将指标添加到报表
    function addMusToReport(){
      var msgObj=document.getElementById('nullmsg');
      if(msgObj){
        nullmsg.style.display="none";
      }
      var chkNodes=form1.left_list;
      if(chkNodes){
        for(var i=0;i<chkNodes.options.length;i++){
          var chkNode=chkNodes.options[i];
          if(chkNode && chkNodes.options[i].selected==true){
            appendToTable(chkNode);
            chkNodes.options.remove(i);
            i--;
          }
        }
      }
    }

    //以自增行号为行标识，那样删除某一行出现断号
    function appendToTable(chkNode){
      var rowCount=0;
      var rowCountObj=document.getElementById('rowCount');
      if(rowCountObj){
        rowCount=rowCountObj.value;
      }

      var tab=document.getElementById("tbReport");
      var tabBody=tab.tBodies[0];
      if(tab && tabBody){
        var trObj=document.createElement("tr");

        var rowId='tr_'+rowCount;
        //得判断这个树节点是否已经加到了表格中
        //通过隐藏字段来判断
        var added=false;
        var e=document.getElementsByTagName("input");
        if(e){
          for(var i=0;i<e.length;i++){
            if(e[i].type=="hidden"){
              var value=e[i].value;
              if(value==chkNode.value){
                added=true;
                break;
              }
            }
          }
        }
        var tmpNode=document.getElementById(rowId);
        if(!added){
          trObj.id=rowId;
          //第一个
          var tdObj=document.createElement("td");
          var chkNodeSplit = chkNode.value.split("|");
          tdObj.className="table-td";
          var tmpId=eval(rowCount);
          var innerHTML='';
          innerHTML +='<input type="text" name="field_title_'+rowCount+'" value="'+chkNode.text+'" readonly/>';
          innerHTML +='<input type="hidden" name="col_id" value="'+tmpId+'"/>';
          innerHTML +='<input type="hidden" name="is_msu_'+rowCount+'" value="N"/>';
          innerHTML +='<input type="hidden" name="is_user_msu_'+rowCount+'" value="Y"/>';
          innerHTML +='<input type="hidden" name="msu_id_'+rowCount+'" value="'+chkNodeSplit[0]+'"/>';
          innerHTML +='<input type="hidden" name="datatable_'+rowCount+'" value="'+chkNodeSplit[1]+'"/>';
          tdObj.innerHTML=innerHTML;
          trObj.appendChild(tdObj);

          //操作
          tdObj=document.createElement("td");
          tdObj.className="table-td";
          tdObj.align="center";
          innerHTML='<TABLE cellpadding="1" cellspacing="0">'
          innerHTML+='              <TR>';
          innerHTML+='                <TD align="center"><IMG';
          innerHTML+='                  SRC="../images/common/report/delete.gif" TITLE="删除"';
          innerHTML+='                  style="cursor:hand"';
          innerHTML+='                  onclick="removeTableTr(\''+trObj.id+'\',\''+chkNode.value+'\',\''+chkNode.text+'\')">';
          innerHTML+='                </TD>';
          innerHTML+='                <TD align="center"><IMG';
          innerHTML+='                  SRC="../images/common/report/up.gif" TITLE="上移"';
          innerHTML+='                  style="cursor:hand"';
          innerHTML+='                  onclick="moveUp(\''+trObj.id+'\')"></TD>';
          innerHTML+='                <TD align="center"><IMG';
          innerHTML+='                  SRC="../images/common/report/down.gif" TITLE="下移"';
          innerHTML+='                  style="cursor:hand"';
          innerHTML+='                  onclick="moveDown(\''+trObj.id+'\')"></TD>';
          innerHTML+='              </TR>';
          innerHTML+='           </TABLE>';
          tdObj.innerHTML=innerHTML;
          trObj.appendChild(tdObj);
          tabBody.appendChild(trObj);
          rowCount++;
        }
      }
      rowCountObj.value=rowCount;
    }
    //删除该行
    function removeTableTr(trId,dim_id,dim_name){
      var trObj=document.getElementById(trId);
      if(trObj){
         var tab=document.getElementById("tbReport");
         var tabBody=tab.tBodies[0];
         tabBody.removeChild(trObj);
      }
      var chkNodes=form1.left_list;
      chkNodes.options[chkNodes.options.length]=new Option(dim_name,dim_id);
    }
    //上移该行
    function moveUp(trId){
      var trObj=document.getElementById(trId);
      var selection=getCheckedBox(trId);
      if(trObj){
         var tab=document.getElementById("tbReport");
         var tabBody=tab.tBodies[0];
         var previousNode=trObj.previousSibling;
         //没有在最上,上移
         if(previousNode && previousNode.id!="tbHead"){
          //先删除
          tabBody.removeChild(trObj);
          //然后插在前面
          tabBody.insertBefore(trObj,previousNode);
         }
         //恢复选择框得选择状态
         restorSelection(selection);
      }
    }
    //下移该行
    function moveDown(trId){
      var trObj=document.getElementById(trId);
      var selection=getCheckedBox(trId);
      if(trObj){
         var tab=document.getElementById("tbReport");
         var tabBody=tab.tBodies[0];
         var afterNode=trObj.nextSibling;
         var lastNode=tabBody.lastChild;
         //不是最后一行
         if(afterNode && trObj!=lastNode){
          //先删除
          tabBody.removeChild(trObj);
          //然后插在后面
          insertAfter(tabBody,trObj,afterNode);
         }
         //恢复选择框得选择状态
         restorSelection(selection);
      }
    }

    function insertAfter(parent, node, referenceNode) {
      if(referenceNode.nextSibling) {
          parent.insertBefore(node, referenceNode.nextSibling);
      } else {
          parent.appendChild(node);
      }
    }

    function getCheckedBox(rowId){
      var checked=[];
      var Id=rowId.replace(/^tr_(\w*)/gi,"$1");
      var name='dim_table_'+Id;
      var e=document.getElementById(name);
      if(e && e.checked){
        checked.push(name);
      }
      name='dim_condition_'+Id;
      e=document.getElementById(name);
      if(e && e.checked){
        checked.push(name);
      }
      return checked;
    }

    function restorSelection(selection){
      if(selection){
        for(var i=0;i<selection.length;i++){
          var e=document.getElementById(selection[i]);
          if(e){
            e.checked=true;
          }
        }
      }
    }
</script>
</html>
