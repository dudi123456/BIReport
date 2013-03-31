<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptColDictTable"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>

<HTML>
<HEAD>
<TITLE>报表指标结构</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/css.css">
<LINK REL="stylesheet" TYPE="text/css" HREF="<%=request.getContextPath()%>/css/other/tab_css.css">
<style>
         .treeCheckBox {
                 height: 14px;
                 margin: 0px;
                 padding: 0px;
                 border: 1px;
                 vertical-align: middle;
         }
</style>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dojo.js"></script>
<script type="text/javascript">

    dojo.require("dojo.event.*");
    dojo.require("dojo.io.*");
    dojo.require("dojo.lang.*");
    dojo.require("dojo.widget.*");
    dojo.require("dojo.widget.Tree");
    dojo.require("dojo.widget.TreeNode");
    dojo.require("dojo.widget.TreeSelector");
    dojo.require("dojo.widget.TreeLoadingController");
    dojo.require("dojo.widget.TreeControllerExtension");


    dojo.hostenv.writeIncludes();

    // Process every DOMNode at creation-time for tree, add checkbox to it
    function addBaseCheckBox(message) {
        // add checkBox before titleNode
        var node = message.source;
        var checkBox = document.createElement('input');
        checkBox.setAttribute('name', 'chk_'+node.widgetId);
        checkBox.setAttribute('type', 'checkbox');
        checkBox.setAttribute('id', 'chk_'+node.widgetId); //id voor label
        dojo.html.setClass(checkBox, 'treeCheckBox');
        dojo.dom.insertBefore(checkBox, node.titleNode);
    }

    function addCutomCheckBox(message) {
        // add checkBox before titleNode
        var node = message.source;

        if(!node.isFolder){
          var checkBox = document.createElement('input');
          checkBox.setAttribute('name', 'chk_'+node.widgetId);
          checkBox.setAttribute('type', 'checkbox');
          checkBox.setAttribute('id', 'chk_'+node.widgetId); //id voor label
          dojo.html.setClass(checkBox, 'treeCheckBox');
          dojo.dom.insertBefore(checkBox, node.titleNode);
        }
    }
    // need to subscribe BEFORE dojo.addOnLoad
    // BEFORE widgets are made.
    dojo.event.topic.subscribe("baseMsuTree/createDOMNode", addBaseCheckBox);
    dojo.event.topic.subscribe("customMsuTree/createDOMNode", addCutomCheckBox);
    // open a new window
    function MM_openBrWindow(theURL,winName,features) { //v2.0
      var tmp=features.split(",");
      var newFeatures="";
      var width="";
      var height="";
      for(var i=0;i<tmp.length;i++){
        var e= tmp[i];
        if(e.indexOf("width=")>=0){
          width=e.substring(e.indexOf("width=")+6);
        }
        if(e.indexOf("height=")>=0){
          height=e.substring(e.indexOf("height=")+7);
        }
      }
      var left=eval((screen.width-width)/2);
      var top=eval((screen.height-height)/2);
      newFeatures +="left="+left+",";
      newFeatures +="top="+top+",";
      for(var i=0;i<tmp.length;i++){
        var e= tmp[i];
        if(e.indexOf("left=")<=0 && e.indexOf("top=")<=0){
          newFeatures +=e+",";
        }
      }
      window.open(theURL,winName,newFeatures);
    }
    //将指标添加到报表
    function addMusToReport(){
      var msgObj=document.getElementById('nullmsg');
      if(msgObj){
        nullmsg.style.display="none";
      }
      //获取所有的选择框，里面可能包含下面的选择框
      var chkNodes=dojo.html.getElementsByClass("treeCheckBox");
      if(chkNodes && chkNodes.length>0){
        for(var i=0;i<chkNodes.length;i++){
          var chkNode=chkNodes[i];
          if(chkNode && chkNode.checked==true){
              var widgetId=chkNode.name;
              widgetId=widgetId.replace(/^chk_(\w*)/gi,"$1");
              var treeNode=dojo.widget.manager.getWidgetById(widgetId);
              if(treeNode){
                appendToTable(treeNode);
              }
          }
        }
      }
    }

    //以自增行号为行标识，那样删除某一行出现断号
    function appendToTable(treeNode){
      var rowCount=0;
      var rowCountObj=document.getElementById('rowCount');
      if(rowCountObj){
        rowCount=rowCountObj.value;
      }
      var isBaseMsu="N";
      var tree=treeNode.tree;
      if(tree){
        if(tree['widgetId']=='baseMsuTree')
          isBaseMsu="Y";
        else
          isBaseMsu="N";
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
              if(value==treeNode['widgetId']){
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
          //alert(treeNode['objectId']);
          var treeSplit = treeNode['objectId'].split("|");
          tdObj.className="table-td";
          var tmpId=eval(rowCount);
          var innerHTML='';
          innerHTML +='<input type="text" name="field_title_'+rowCount+'" value="'+treeNode['title']+'" readonly/>';
          innerHTML +='<input type="hidden" name="col_id" value="'+tmpId+'"/>';
          innerHTML +='<input type="hidden" name="is_msu_'+rowCount+'" value="Y"/>';
          innerHTML +='<input type="hidden" name="is_user_msu_'+rowCount+'" value="'+isBaseMsu+'"/>';
          innerHTML +='<input type="hidden" name="msu_id_'+rowCount+'" value="'+treeNode['widgetId']+'"/>';
          innerHTML +='<input type="hidden" name="datatable_'+rowCount+'" value="'+treeSplit[0]+'"/>';
          innerHTML +='<input type="hidden" name="msu_length_'+rowCount+'" value="'+treeSplit[1]+'" />';
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
          innerHTML+='                  onclick="removeTableTr(\''+trObj.id+'\')">';
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
    function removeTableTr(trId){
      var trObj=document.getElementById(trId);
      if(trObj){
         var tab=document.getElementById("tbReport");
         var tabBody=tab.tBodies[0];
         tabBody.removeChild(trObj);
      }
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
      var name='has_comratio_'+Id;
      var e=document.getElementById(name);
      if(e && e.checked){
        checked.push(name);
      }
      name='has_same_'+Id;
      e=document.getElementById(name);
      if(e && e.checked){
        checked.push(name);
      }
      name='has_last_'+Id;
      e=document.getElementById(name);
      if(e && e.checked){
        checked.push(name);
      }
      name='comma_splitted_'+Id;
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
    function init() {
    // get a reference to the treeSelector
      var baseTreeSelector = dojo.widget.manager.getWidgetById('baseMsuTreeSelector');
      var customTreeSelector = dojo.widget.manager.getWidgetById('customMsuTreeSelector');


     //connect the select event to the function treeSelectFired()/
      dojo.event.connect(baseTreeSelector,'baseMsuSelect','treeSelectFired');
      dojo.event.connect(customTreeSelector,'customMsuSelect','treeSelectFired');
     }

     dojo.addOnLoad(init);
</script>
<%
	/**变量定义区**/
	RptResourceTable rptInfo = (RptResourceTable) session.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
	String period = rptInfo.cycle;//获取报表的统计周期
	String userId = CommonFacade.getLoginId(session);
	//取孩子节点时要使用
	session.setAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_STAT_PERIOD,
			period);
	String path = "../report/custommsu";
	String baseMsuNodes = CustomMsuUtil.assembleTreeNodes(CustomMsuUtil
			.getFirLvlMsuNodes(period),false);
	String customMsuNodes = CustomMsuUtil
			.assembleTreeNodes(CustomMsuUtil.getCustomChildrenNodes(
			userId, period),false);
	//报表ID
	String rpt_id = rptInfo.rpt_id;
	//报表指标Code
	String measure_code = request.getParameter("measure_code");

	//以下是描述定制报表的信息内容
	RptColDictTable[] tmprs = (RptColDictTable[])session.getAttribute(WebKeys.ATTR_REPORT_COLS_DEFINE);
	List listDict = null;
	if(tmprs!=null&&tmprs.length>0){
		listDict = new ArrayList();
	}
	for(int i=0;tmprs!=null&&i<tmprs.length;i++){
		if(ReportConsts.DATA_TYPE_NUMBER.equals(tmprs[i].data_type)){
			listDict.add(tmprs[i]);
		}
	}

	RptColDictTable[] rs = null;
	if(listDict!=null&&listDict.size()>0){
		rs = (RptColDictTable[])listDict.toArray(new RptColDictTable[listDict.size()]);
	}
	int colNum = 0;
	if (null != rs) {
		colNum = rs.length;
	}
%>
</HEAD>
<BODY>
<FORM METHOD="POST" name="fieldList" action="editLocalReport.rptdo">
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
        <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
          <%if("Y".equals(rptInfo.status)){%>
          <tr>
            <td height="30" colspan="3" align="center">
              <font color="red">该报表已经通过认证，不能对指标进行增删操作！</font>
            </td>
          </tr>
          <%}else{%>
          <tr>
            <td class="tree"><img src="../images/common/report/icon_file4.gif" width="17" height="14">基本指标</td>
            <td rowspan="4">
              <a href='javascript:addMusToReport()'><img src="../images/common/report/add_report.gif" width="101" height="22" border="0"></a>
              <br><br>
           <!--   <img src="../biimages/new_zb.gif" width="101" height="22" border="0" onclick="javascript:_editRptMeasure();" style="cursor:hand"> -->
			</td>
            <td class="tree"><img src="../images/common/report/icon_file4.gif" width="17" height="14">已选指标</td>
          </tr>
          <tr>
            <td width="48%" height="400" valign="top" class="tab-side">
            <!-- 系统定义指标开始 -->
            <table width="100%" height="100%" border="0">
              <tr>
                <td height="200" valign="top">
                <div id="Layer1"
                  style="position:absolute; width:100%; height:100%; z-index:1; overflow: auto;">
                <div dojotype="TreeLoadingController"
                  rpcurl="<%=path%>/customMsuAddTreeListen.jsp"
                  widgetid="baseMsuTreeController"></div>
                <div dojotype="TreeSelector"
                  widgetid="baseMsuTreeSelector"></div>
                <div dojotype="Tree" dndmode="between"
                  selector="baseMsuTreeSelector" widgetid="baseMsuTree"
                  controller="baseMsuTreeController"><%=baseMsuNodes%></div>
                </div>
                </td>
              </tr>
            </table>
            <!-- 系统定义指标结束 -->
            </td>
            <td width="48%" rowspan="3" valign="top" class="side-2">
            <!-- 指标定义表格开始 -->
            <table id="tbReport" width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr id="tbHead" class="table-th">
                <td align="center" nowrap class="table-item" height="20">指标名称</td>
                <td align="center" nowrap class="table-item2">操作</td>
              </tr>
              <%if(rs==null||rs.length==0){%>
              <tr id="nullmsg">
                <td colspan="2" class="table-td" height="18" align="center">
                  还没有添加任何指标
                </td>
              </tr>
              <%}%>
              <%
              		for (int i = 0; rs != null && i < rs.length; i++) {
              			RptColDictTable rsInfo = (RptColDictTable) rs[i];
              %>
              <tr id="tr_<%=i%>">
                <td class="table-td">
                <input type="text" name="field_title_<%=i%>" value="<%=rsInfo.field_title%>" readonly/>
                <input type="hidden" name="col_id" value="<%=i%>" />
                <input type="hidden" name="default_display_<%=i%>" value="<%=rsInfo.default_display %>" />
                <input type="hidden" name="valuable_sum_<%=i%>" value="<%=rsInfo.valuable_sum %>" />
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
                <%if("Y".equals(rptInfo.status)){%>
                <td align="center" class="table-td2">
                <TABLE cellpadding="1" cellspacing="0">
                  <TR>
                    <TD align="center">
                    <IMG SRC="../biimages/up.gif" TITLE="上移" style="cursor:hand" onclick="moveUp('<%="tr_"+i%>')"></TD>
                    <TD align="center">
                    <IMG SRC="../biimages/down.gif" TITLE="下移" style="cursor:hand" onclick="moveDown('<%="tr_"+i%>')"></TD>
                  </TR>
                </TABLE>
                </td>
                <%}else{%>
                <td align="center" class="table-td2">
                <TABLE cellpadding="1" cellspacing="0">
                  <TR>
                    <TD align="center">
                    <IMG SRC="../images/common/report/delete.gif" TITLE="删除" style="cursor:hand" onclick="removeTableTr('<%="tr_"+i%>')"></TD>
                    <TD align="center">
                    <IMG SRC="../images/common/report/up.gif" TITLE="上移" style="cursor:hand" onclick="moveUp('<%="tr_"+i%>')"></TD>
                    <TD align="center">
                    <IMG SRC="../images/common/report/down.gif" TITLE="下移" style="cursor:hand" onclick="moveDown('<%="tr_"+i%>')"></TD>
                  </TR>
                </TABLE>
                </td>
                <%}%>
              </tr>
              <%}%>
            </table>
            <!-- 指标定义表格结束 -->
            </td>
          </tr>
          <tr>
            <td class="tree">
             <!--  <img src="../images/common/report/icon_file4.gif" width="17" height="14">自定义指标 -->
            </td>
          </tr>
          <tr>
            <td  valign="top" class="tab-side">
            <!-- 自定义指标开始 -->
             <!--
            <table width="100%" height="100%" border="0">
              <tr>
                <td height="160" valign="top">
                <div id="customMsuList"
                  style="position:absolute; width:100%; height:100%; z-index:1; overflow: auto;">
                <div id="Layer1"
                  style="position:absolute; width:100%; height:100%; z-index:1; overflow: no;">
                <div dojotype="TreeLoadingController"
                  rpcurl="<%=path%>/rptCustomMsuTreeListen.jsp"
                  widgetid="customMsuTreeController"></div>
                <div dojotype="TreeSelector"
                  widgetid="customMsuTreeSelector"></div>
                <div dojotype="Tree" dndmode="between"
                  selector="customMsuTreeSelector"
                  widgetid="customMsuTree"
                  controller="customMsuTreeController"><%=customMsuNodes%></div>
                </div>
                </div>
                </td>
              </tr>
            </table>
            -->
            <!-- 自定义指标结束 -->
            </td>
          </tr>
          <%}%>
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
		value="保存" onclick="setSubmitFlag('customrpt_measure','save','current')">
	  <input name="pre" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		value="上一步" onclick="setSubmitFlag('customrpt_measure','save','pre')">
	  <input name="next" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
		value="下一步" onclick="setSubmitFlag('customrpt_measure','save','next')">
    </td>
  </tr>
</table>
<input type="hidden" name="KDG_SUBMIT_FORM" value="fieldList" />
<INPUT type="hidden" id="opType" name="opType" value="" />
<INPUT type="hidden" id="opSubmit" name="opSubmit" value="" />
<INPUT type="hidden" id="opDirection" name="opDirection" value="" />
<input type="hidden" name="rowCount" value="<%=colNum %>" />
<input type="hidden" name="rpt_id" value="<%=rpt_id%>" />
<input type="hidden" name="measure_code" value="<%=measure_code%>" />
</FORM>
</BODY>
<script language="JavaScript">
	function setSubmitFlag(type,submit,direction){
	  document.fieldList.opType.value = type;
	  document.fieldList.opSubmit.value = submit;
	  document.fieldList.opDirection.value = direction;
	  if(submit == 'save'){
	  	document.fieldList.submit();
      }
	}

function _editRptMeasure(){
  var optstr = "height=600,width=880,left=0,top=0,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no"
  newsWin = window.open("loadCustomMsu.rptdo?stat_period=<%=period%>","editRptMeasure",optstr);
  if(newsWin!=null)
    newsWin.focus();
}
</script>
</HTML>
