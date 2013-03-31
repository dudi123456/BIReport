<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/BIBM.tld" prefix="BIBM"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.report.util.ReportConsts"%>
<%@page import="com.ailk.bi.report.domain.RptResourceTable"%>
<%@page import="com.ailk.bi.report.domain.RptFilterTable"%>

<%
if( !com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
  return;
%>
<%
//报表对象
RptResourceTable rptTable = (RptResourceTable)session.getAttribute(WebKeys.ATTR_REPORT_TABLE_DEFINE);
if(rptTable==null){
	out.print("<center>");
	out.print("<br><br>报表信息丢失，请重新查询确定你需要查看的报表信息！<br>");
	out.print("</center>");
	return;
}
//报表列信息对象
RptFilterTable[] rptFilters = (RptFilterTable[])session.getAttribute(WebKeys.ATTR_REPORT_FILTERS_DEFINE);
//列计数
int irowcount = 1;
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/other/tab_css.css" type="text/css">
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/tab_js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/jquery.min.js"></SCRIPT>
<SCRIPT language=javascript src="<%=request.getContextPath()%>/js/align_tab_by_head.js"></SCRIPT>
</head>

<%if(rptFilters==null||rptFilters.length==0){ %>
<body>
<%}else{ %>
<body onLoad="createPopup();alignTable();" onResize="alignTable();">
<%} %>
<FORM name="reportEditForm" action="editLocalReport.rptdo" method="POST">

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top" class="squareB"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="5" height="7" background="../images/common/tab/square_line_2.gif"></td>
          <td width="100%" colspan="2"></td>
          <td><img src="../images/common/tab/field_upline_right.gif" width="5" height="7"></td>
        </tr>
        <%if(rptFilters!=null&&rptFilters.length>0){ %>
        <tr>
          <td height="7" background="../images/common/tab/square_line_2.gif">&nbsp;</td>
          <td height="100%" colspan="2" valign="top"> <table width="100%" align="center">
              <tr>
                <td valign="top">

                <span id="tableContent">
                <table width='100%' border='0' height="400" cellpadding='0' cellspacing='0' id="iTable_TableContainer">

                <tr>
				<td align=center valign='top'>
				<table id="iTable_LeftHeadTable1" width="100%" border="0" cellpadding="0" cellspacing="0" class="table">
                  <tr class="table-th">
                    <td nowrap align="center" class="table-item">名称</td>
                  </tr>
                </table>
                </td>

                <td width="100%" align="left" valign="top">
                <div id="Layer1" style="position:absolute; width:100%; z-index:1; overflow: hidden;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="iTable" id="iTable_HeadTable1">
				  <tr class="table-th">
				    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" align="center" nowrap class="table-item-rptedit-red">
                      <input type="text" name="field_title_<%=i%>" value="<%=rptFilters[i].field_title%>" class="normalField2" />
                      <input type="hidden" name="col_id" value="<%=i%>" />
                      <input type="hidden" name="col_sequence_<%=i%>" value="<%=rptFilters[i].col_sequence%>" />
                    </td>
                    <%} %>
                  </tr>
                </table>
                </div>
                </td>
                </tr>

                <tr valign="top">
				<td height="100%">
				<div id="LayerLeft1"style="position:absolute; width:100%; z-index:1; overflow: hidden; height: 100%;">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="iTable" id="iTable_LeftTable1">
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">1.是否为条件</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">2.排列顺序</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">3.条件过滤对应字段</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">4.对应描述字段</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">5.条件类型</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">6.脚本代码</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">7.数据来源</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">8.提取数据语句</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">9.显示全部</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">10.默认值</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">11.字段类型</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">12.条件操作符号</td></tr>
				  <tr class="table-tr"><td nowrap class="table-td-withgreybg-rptedit">13.是否有效</td></tr>
				  <tr><td>&nbsp;</td></tr>
				</table>
				</div>
				</td>

				<td align="left">
				<div id="LayerRight1" style="position:absolute; width:100%; z-index:1; overflow: auto; height: 100%;" onscroll="syncScroll()">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="iTable" id="iTable_ContentTable1">
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="dim_conditon_<%=i%>" value="<%=rptTable.rpt_id+"_"+i+"_"+i%>" <%if(ReportConsts.YES.equals(rptFilters[i].dim_conditon)){out.print("checked");}%>></td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <a href="javascript:moveLeft('<%=rptTable.rpt_id+"_"+i%>')"><img src="../images/common/tab/move_left.gif" width="11" height="11" hspace="1" border="0"></a>
                      <a href="javascript:moveRight('<%=rptTable.rpt_id+"_"+i%>')"><img src="../images/common/tab/move_right_on.gif" width="11" height="11" hspace="1" border="0"></a>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="field_dim_code_<%=i%>" maxlength="32" value="<%=rptFilters[i].field_dim_code%>" class="normalField2" />
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="field_code_<%=i%>" value="<%=rptFilters[i].field_code%>" class="normalField2" readonly/>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ String tmpName="filter_type_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <BIBM:TagSelectList listID="S3020" listName="<%=tmpName%>" focusID="<%=rptFilters[i].filter_type%>" script="onChange='changeDataSource(this)'"/>
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="filter_script_<%=i%>" maxlength="300" value="<%=rptFilters[i].filter_script%>" class="normalField2"/>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ String tmpName="filter_datasource_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <BIBM:TagSelectList listID="S3021" listName="<%=tmpName%>" focusID="<%=rptFilters[i].filter_datasource%>" />
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="filter_sql_<%=i%>" maxlength="300" value="<%=rptFilters[i].filter_sql%>" class="normalField2"/>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="filter_all_<%=i%>" <%if(ReportConsts.YES.equals(rptFilters[i].filter_all)){out.print("checked");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <input type="text" name="filter_default_<%=i%>" maxlength="32" value="<%=rptFilters[i].filter_default%>" class="normalField2"/>
                    </td>
                    <%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ String tmpName="data_type_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <BIBM:TagSelectList listID="S3022" listName="<%=tmpName%>" focusID="<%=rptFilters[i].data_type%>" />
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;String tmpValue = "=,=;>=,>=;<=,<=;in,in;like,like;like_r,右like;like_l,左like";%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ String tmpName="con_tag_"+i; %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit">
                      <BIBM:TagSelectList listID="#" listName="<%=tmpName%>" focusID="<%=rptFilters[i].con_tag%>" selfSQL="<%=tmpValue%>"/>
					</td>
					<%} %>
                  </tr>
                  <tr class="table-tr">
                    <%irowcount++;%>
                    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
                    <td id="<%=irowcount+"_"+rptTable.rpt_id+"_"+i%>" class="table-td-withbg-rptedit"><input type="checkbox" name="status_<%=i%>" <%if(ReportConsts.YES.equals(rptFilters[i].status)){out.print("checked");}%>>&nbsp;</td>
                    <%} %>
                  </tr>
                </table>
                </div>
                </td>
                </tr>
                </table>
                </span>

              </td>
              </tr>
          </table>                      </td>
          <td background="../images/common/tab/square_line_3.gif">&nbsp;</td>
        </tr>
        <%} %>
        <tr>
          <td height="5"><img src="../images/common/tab/square_corner_3.gif" width="5" height="5"></td>
          <td colspan="2" background="../images/common/tab/square_line_4.gif"></td>
          <td><img src="../images/common/tab/square_corner_4.gif" width="5" height="5"></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td height="35" align="left" valign="top">
    <font size="2">
说明：条件javascript脚本,如果要实现条件联动效果格式如下：desc=doOpen('qry_dim1','qry_dim2','parent_id')<br>
参数说明：1.主联动条件ID，2.被联动条件ID，3.子表中表示父子关系的字段名称<br>
在条件定制步骤中条件ID按照qry_dim1，qry_dim2，qry_dim3 …顺序排列</font>
    </td>
  </tr>
  <tr>
    <td height="35" align="center" valign="bottom">
     			 <input name="bc" type="button" class="button" onMouseOver="switchClass(this)"	onMouseOut="switchClass(this)"
					  value="保存" onclick="setSubmitFlag('step4','save','current')">
				 <input name="pre" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					  value="上一步" onclick="setSubmitFlag('step4','save','pre')">
    			 <input name="next" type="button" class="button" onMouseOver="switchClass(this)" onMouseOut="switchClass(this)"
					  value="下一步" onclick="setSubmitFlag('step4','save','next')">
    </td>
  </tr>
</table>

<INPUT TYPE="hidden" id="opType" name="opType" value="" />
<INPUT TYPE="hidden" id="opSubmit" name="opSubmit" value="" />
<INPUT TYPE="hidden" id="opDirection" name="opDirection" value="" />
</FORM>
</body>
</html>
<SCRIPT LANGUAGE=JavaScript DEFER>
	//add by wenna
    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
		if(document.all("filter_type_<%=i%>").value=="T"){
			document.all("filter_datasource_<%=i%>").disabled=true;
			document.all("filter_sql_<%=i%>").disabled=true;
		}
    <%} %>
	function changeDataSource(obj){
		var name=obj.name;
		var index=name.substring(12,name.length);
		var name1="filter_datasource_"+index;
		var name2="filter_sql_"+index;
		if(obj.value=="T"){
			document.all(name1).value="N";
			document.all(name1).disabled=true;
			document.all(name2).value="";
			document.all(name2).disabled=true;
		}else{
			document.all(name1).disabled=false;
			document.all(name2).disabled=false;
		}
	}

	function setSubmitFlag(type,submit,direction){
	  document.reportEditForm.opType.value = type;
	  document.reportEditForm.opSubmit.value = submit;
	  document.reportEditForm.opDirection.value = direction;
      if(submit == 'save'){
	    <%for(int i=0;rptFilters!=null&&i<rptFilters.length;i++){ %>
			if(document.all("filter_type_<%=i%>").value=="T"){
				document.all("filter_datasource_<%=i%>").disabled=false;
				document.all("filter_sql_<%=i%>").disabled=false;
			}
	    <%} %>
	    document.reportEditForm.submit();
      }
	}

      function moveLeft(tdObjId){
        //首先找到
        if(tdObjId){
          //第一行的单元格移动
          var nameTd=document.getElementById("1_"+tdObjId);
          moveTableCellLeft(nameTd);
          //第二行的单元格移动
          var checkTd=document.getElementById("2_"+tdObjId);
          var child=checkTd.lastChild;
          var checked=false;
          if(child && child.checked)
            checked=true;
          var success=moveTableCellLeft(checkTd);
          //此持需要将选择框的值的最后的数字减一
          if(success){
            //移动成功，减一
            inputValueUpdate(tdObjId,false);
            //还有看看是不是选中了,然后还得恢复
            child.checked=checked;
          }

          for(i=3;i<=9;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=10;i<=10;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }
          for(i=11;i<=13;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellLeft(moveTd);
          }
          for(i=14;i<=14;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellLeft(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              //移动成功，减一
              inputValueUpdate(tdObjId,false);
              //还有看看是不是选中了,然后还得恢复
              child.checked=checked;
            }
          }

        }
      }
      function moveRight(tdObjId){
        if(tdObjId){
          //第一行的单元格移动
          var nameTd=document.getElementById("1_"+tdObjId);
          moveTableCellRight(nameTd);
          //第二行的单元格移动
          var checkTd=document.getElementById("2_"+tdObjId);
          var child=checkTd.lastChild;
          var checked=false;
          if(child && child.checked)
            checked=true;
          var success=moveTableCellRight(checkTd);
          //此持需要将选择框的值的最后的数字减一
          if(success){
            inputValueUpdate(tdObjId,true);
            child.checked=checked;
          }

          for(i=3;i<=9;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=10;i<=10;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }
          for(i=11;i<=13;i++){
            var moveTd=document.getElementById(i+"_"+tdObjId);
            moveTableCellRight(moveTd);
          }
          for(i=14;i<=14;i++){
            var checkTd=document.getElementById(i+"_"+tdObjId);
            var child=checkTd.lastChild;
            var checked=false;
            if(child && child.checked)
              checked=true;
            var success=moveTableCellRight(checkTd);
            //此持需要将选择框的值的最后的数字减一
            if(success){
              inputValueUpdate(tdObjId,true);
              child.checked=checked;
            }
          }

      }
    }

    //将表格的单元格向左移动
    function moveTableCellLeft(tdObj){
      var success=false;
      if(tdObj){
          var previousNode=tdObj.previousSibling;
          if(previousNode){
            //有是看
            var preId=previousNode.id;
            if(preId){
              //如果有，说明它的左边还有,则可以左移
              var parent=previousNode.parentNode;
              if(parent){
                parent.removeChild(tdObj);
                parent.insertBefore(tdObj,previousNode);
                success=true;
              }
            }
          }
        }
        return success;
    }
     //将表格的单元格向右移动
    function moveTableCellRight(tdObj){
      var success=false;
      if(tdObj){
          var afterNode=tdObj.nextSibling;
          //看看还有没有右边的节点
          if(afterNode){
            //有
            var parent=afterNode.parentNode;
            if(parent){
               parent.removeChild(tdObj);
               insertAfter(parent,tdObj,afterNode);
               //前一个内容减一
               success=true;
            }
          }
       }
       return success;
    }

    //将DOM节点插入某个节点之后
    function insertAfter(parent, node, referenceNode) {
      if(referenceNode.nextSibling) {
          parent.insertBefore(node, referenceNode.nextSibling);
      } else {
          parent.appendChild(node);
      }
    }

    //将单元格的值的最后部分减一或加一
    function inputValueUpdate(tdObjId,add){
      if(tdObjId){
        //查找第二行的单元格对象
        var tdObj=document.getElementById("2_"+tdObjId);
        var preNode=tdObj.previousSibling;
        var nextNode=tdObj.nextSibling;
        if(tdObj){
          var child=tdObj.lastChild;
          if(child){
            var value=child.value;
            value=value.replace(tdObjId+"_","");
            value=eval(value);
            if(add){
              value++;
            }else{
              value--;
            }
            child.value=tdObjId+"_"+value;
          }
          //
          if(add){
            //增加了，找前面的
            if(preNode){
              child=preNode.lastChild;
              var nodeId=preNode.id;
              nodeId=nodeId.substring(nodeId.indexOf("_")+1);
              var value=child.value;
              value=value.replace(nodeId+"_","");
              value=eval(value);
              value--;
              child.value=nodeId+"_"+value;
            }
          }else{
            if(nextNode){
              child=nextNode.lastChild;
              var nodeId=nextNode.id;
              nodeId=nodeId.substring(nodeId.indexOf("_")+1);
              var value=child.value;
              value=value.replace(nodeId+"_","");
              value=eval(value);
              value++;
              child.value=nodeId+"_"+value;
            }
          }
        }
      }
    }
</SCRIPT>