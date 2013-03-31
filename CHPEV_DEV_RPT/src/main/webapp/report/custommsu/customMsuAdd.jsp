<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@page import="com.ailk.bi.base.util.CommTool"%>
<%@page import="com.ailk.bi.base.util.TableConsts"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%
	Log log = LogFactory.getLog(CustomMsuUtil.class);
	String stat_period = CommTool
			.getParameterGB(request, "stat_period");
	if (null == stat_period || "".equals(stat_period)) {
		//显示警告信息，目前没有
		log.error("增加自定义指标，未给定统计周期");
		return;
	}
	session.setAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_STAT_PERIOD,
			stat_period);
	String token = "";
	Object tmpObj = session
			.getAttribute(WebKeys.ATTR_REPORT_CUSTOM_MSU_TOKEN);
	if (null != tmpObj) {
		token = (String) tmpObj;
	}
	String period = TableConsts.STAT_PERIOD_MONTH;
	if (null != stat_period && !"".equals(stat_period)) {
		period = stat_period;
	}
	String context = request.getContextPath();
	String path = context + "/report/custommsu";
	String firLvlNodes = CustomMsuUtil.assembleTreeNodes(CustomMsuUtil
			.getFirLvlMsuNodes(period), false);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加自定义指标</title>
<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css"
  type="text/css">
<SCRIPT language=javascript src="<%=context%>/js/date/scw.js"></SCRIPT>
<script language="javascript" src="<%=context%>/js/tab_js.js"></script>
<script language="javascript" src="<%=context%>/js/dojo.js"></script>
<SCRIPT language=javascript src="<%=context%>/js/wait.js"></SCRIPT>
<script language="javascript" src="<%=context%>/js/net.js"></script>
<script language="javascript" src="<%=context%>/js/popup.js"></script>
<script type="text/javascript">
  dojo.require("dojo.event.*");
  dojo.require("dojo.io.*");
  dojo.require("dojo.widget.Tree");
  dojo.require("dojo.widget.TreeNode");
  dojo.require("dojo.widget.TreeSelector");
  dojo.require("dojo.widget.TreeLoadingController");

  <!-- put code here -->
  function treeSelectFired() {
  
     <!-- get a reference to the treeSelector and get the selected node -->
      var treeSelector = dojo.widget.manager.getWidgetById('treeSelector');
      var treeNode = treeSelector.selectedNode;
  
      <!-- get a reference to the songDisplay div -->
      var selectedMsuObj = document.getElementById("selectedMsu");
      var srcTabIdObj= document.getElementById("srcTabId");
	  var selectedBaseMsu = document.getElementById("selectedBaseMsu");
      selectedBaseMsu.innerText=treeNode['title'];
      <!-- determine if this node is a folder (!isFolder == leaf node ) -->
      <!-- get the node's title and output it to the songDisplay div -->
      var msuId=treeNode['widgetId'];
      var srcTabId=treeNode['objectId'];
      selectedMsuObj.value=msuId;
      srcTabIdObj.value=srcTabId;
  }
  
  //加载基本指标的可定义维度
  function loadBaseMsuDims(){
    var selectedMsuObj=document.getElementById('selectedMsu');
    var loadMsuDimsBtn=document.getElementById('loadMsuDimsBtn');
    var srcTabObj=document.getElementById('srcTabId');
    var selectedMsu=selectedMsuObj.value;
    if(!selectedMsu || ''==selectedMsu){
      alert('请从左侧指标数选择一个要衍生的基本指标');
      return;
    }
    var srcTabId=srcTabObj.value;
    var url='<%=path%>/baseMsuDimsLoadAJAXAction.jsp';
    var params=['srcTabId='+srcTabId,'selectedMsu='+selectedMsu];
    params.push('action=ajaxLoadMsuDims');
    ShowWait();
    var ajaxHelper=new net.ContentLoader(url,params,loadBaseMsuDimsUpdate,ajaxError);
    ajaxHelper.sendRequest();
  }

//加载返回的维度值
function loadBaseMsuDimsUpdate(){
   var jsonTxt=this.req.responseText;
    if(jsonTxt){
      //转换为JSON对象
      try {
    	 var jsonObj=eval("("+jsonTxt+")");
	      if(jsonObj){
	        //取出DIMS对象
	        var dims=jsonObj.dims;
	        if(dims){
	          //显示维度列表
	          displayDims(dims);
	          closeWaitWin();
	        }else{
	          //发生错误时弹出错误信息 
	          closeWaitWin();
	          popmsg(jsonObj.title,jsonObj.content,'<%=context%>');
	        }
	      }else{
	        //显示错误
	        closeWaitWin();
	        ajaxError();
	      }
      } catch(e) {
      	closeWaitWin();
        alert("分析数据发生错误，维度值中可能含有特殊符号");
    }
    }else{
      closeWaitWin();
      ajaxError();
    }
}
//显示自定义指标的维度列表
  function displayDims(dims){
    if(dims){
      //默认情况下维度列表最多显示两列
      //因此这里还需要美化
      var half=dims.length/2;
      half=eval(half+0.5);
      if(half<2)
        half=2;
      var secTdInnerHTML="";
      var thdTd1InnerHTML="&nbsp;";
      var thdTd2InnerHTML="&nbsp;";
      var thdTd3InnerHTML="&nbsp;";
      for(var i=0;i<dims.length;i++){
        var dimObj=dims[i];
        var tableHTML=createDimDom(dimObj);
        if(i<2){
          secTdInnerHTML +=tableHTML;
        }else{
          if((i+2)%3==0)
           thdTd1InnerHTML +=tableHTML;
          if((i+2)%3==1)
           thdTd2InnerHTML +=tableHTML;
          if((i+2)%3==2)
           thdTd3InnerHTML +=tableHTML;
        }
      }
      //这里还缺基本指标名称
      var secTdObj=document.getElementById('secondCol');
      if(secTdObj){
        var tmpStr=secTdInnerHTML;
        var treeSelector = dojo.widget.manager.getWidgetById('treeSelector');
        var treeNode = treeSelector.selectedNode;
        var msuName=treeNode.title;
        secTdInnerHTML ='<br>自定义指标名称：<input name="customMsuName"  class ="input-text" value="';
        secTdInnerHTML +=msuName;
        secTdInnerHTML +='"> <br><br>';
        secTdInnerHTML +=tmpStr;
        secTdObj.innerHTML=secTdInnerHTML;
      }
      if(thdTd1InnerHTML){
        if(thdTd1InnerHTML.length>0){
          var thdTdObj=document.getElementById('thirdCol1');
          if(thdTdObj)
           thdTdObj.innerHTML=thdTd1InnerHTML;
        }
      }
      if(thdTd2InnerHTML){
        if(thdTd2InnerHTML.length>0){
          var thdTdObj=document.getElementById('thirdCol2');
          if(thdTdObj)
           thdTdObj.innerHTML=thdTd2InnerHTML;
        }
      }      
      if(thdTd3InnerHTML){
        if(thdTd3InnerHTML.length>0){
          var thdTdObj=document.getElementById('thirdCol3');
          if(thdTdObj)
           thdTdObj.innerHTML=thdTd3InnerHTML;
        }
      }            
    }
  }
  //创建维度选择框DOM对象
  function createDimDom(dimObj){
    if(dimObj){
      var title=dimObj.dimName;
      var dimId=dimObj.dimId;
      var dimValues=dimObj.dimValues;
      var tableHTML='<table width="100%" border="0">\n';
      tableHTML +='<tr>\n';
      tableHTML +='<td align="left" nowrap class="tree" width="20%">'+title+'</td>\n';
      tableHTML +='<td align="left" class="tree">';
      tableHTML +='<a href="javascript:unselectOption(\''+dimId+'\')">取消选择</a></td>\n';
      tableHTML +='</tr>\n';
      tableHTML +='<tr>\n';
      tableHTML +='<td colspan="2">\n';
      var selectHTML='<select name="'+dimId+'" size="7" ';
      selectHTML +='multiple="multiple" class="input-select" ';
      selectHTML +='>';
      for(var i=0;i<dimValues.length;i++){
        var values=dimValues[i];
        if(values.selected=='true'){
          selectHTML +='<option value="'+values.codeId+'" selected>';
          selectHTML +=values.codeValue+'</option>';
        }else{
          selectHTML +='<option value="'+values.codeId+'">';
          selectHTML +=values.codeValue+'</option>';
        }
      }
      selectHTML +='</select>';
      tableHTML +=selectHTML;
      tableHTML +='</td>\n'
      tableHTML +='</tr>\n';
      tableHTML +='</table>\n';
      return tableHTML;
    }
  }
  //取消选择列表的选项
  function unselectOption(selectId){
    var selectObj=document.getElementById(selectId);
    if(selectObj){
       var options=selectObj.options;
       for(var i=0;i<options.length;i++)
        options[i].selected=false;
    }
  }
  //向服务器发起请求失败
  function ajaxError(){
      var alertStr='向远程服务器请求数据失败!\n\n';
      alertStr +='对此我们深表遗憾，请关闭浏览器重新登陆，\n如果问题依旧，请联系系统管理员\n\n';
      if(this.req){
        alertStr +='当前请求是否完毕状态: '+this.req.readyState;
         alertStr +='\n当前请求状态: '+this.req.status;
         alertStr +='\n请求包内容:\n '+this.req.getAllResponseHeaders();
      }
      alert(alertStr);
  }
  //检查用户是否进行了正确选择
  function validate(){
   var customMsuNameObj=document.getElementById("customMsuName");
   if(!customMsuNameObj){
      alert('请从左侧的树中选择一个具体指标，然后单击“获取指标维度”\n显示相应信息并进行填写后再单击“完成”按钮');
      return false;
    }
    if(!customMsuNameObj || ''==customMsuNameObj.value){
      alert('请输入自定义的指标的名称');
      if(customMsuNameObj)
        customMsuNameObj.focus();
      return false;
    }
    selectObjs=document.getElementsByTagName("select");
    if(selectObjs){
       var selected=false;
       for(var i=0;i<selectObjs.length;i++){
         if(selectObjs[i].selectedIndex>=0){
           selected=true;
         }
       }
       if(!selected){
         alert('您没有选择任何一个维度值,您至少要选择一个维度值');
         return false;
       }
    }else{
       return false;
    }
    var minFrmObj=document.getElementById('mainFrm');
    minFrmObj.addMsuBtn.disabled=true;  
    return true;
  }  
  
  function cancelAll(){
  	window.location.reload();
  }

  function init() {
      <!-- get a reference to the treeSelector -->
      var treeSelector = dojo.widget.manager.getWidgetById('treeSelector');
      var loadMsuDimsBtn=document.getElementById('loadMsuDimsBtn');
      
      <!-- connect the select event to the function treeSelectFired() -->
      dojo.event.connect(treeSelector,'select','treeSelectFired');
      dojo.event.connect(loadMsuDimsBtn,'onclick','loadBaseMsuDims');
  }
  dojo.addOnLoad(init);
</script>
</head>
<body>
<table width="95%" border="0" align="center" cellpadding="0"
  cellspacing="0">
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="81"><img
          src="<%=context%>/images/derive_define.gif"></td>
        <td width="100%"
          background="<%=context%>/images/feedback_bg.gif">&nbsp;</td>
        <td width="11" align="right"><img
          src="<%=context%>/images/feedback_ico.gif" width="11"
          height="38"></td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td bgcolor="#FFFFFF">
    <TABLE width=100% height=100% border=0 cellPadding=0 cellspacing="0">
      <TBODY>
        <TR>
          <TD width=5 background="<%=context%>/images/square_line_2.gif"></TD>
          <TD width="100%" background="<%=context%>/images/dot4.gif">
          <form name="mainFrm" action="saveCustomMsu.rptdo" method="post"
            onsubmit="return validate();"><input type="hidden"
            name="selectedMsu" value=""> <input type="hidden"
            name="srcTabId" value=""> <input type="hidden"
            name="token" value="<%=token%>">
          <table width="100%" height="100%" border="0" cellpadding="0"
            cellspacing="0">
            <tr>
              <td valign="top">
              <table width="98%" border="0" align="center">
                <tr>
                  <td height="8" colspan="3"></td>
                </tr>
                <tr>
                  <td><img
                    src="<%=context%>/images/derive_arrow.gif" width="7"
                    height="7"> 浏览并选择指标</td>
                  <td><input id="loadMsuDimsBtn" type="button"
                    class="button-image" name="loadMsuDimsBtn" /></td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td width="33%" valign="top">
                  <table width="100%" border="0">
                    <tr>
                      <td><img
                        src="<%=context%>/images/icon_file4.gif"
                        width="17" height="14"> <span class="tree">基本指标：</span><span
                        id="selectedBaseMsu" class="red"></span></td>
                    </tr>
                    <tr>
                      <td width="30%" class="input-text">
                      <table width="100%" height="100%" border="0"
                        cellpadding="0" cellspacing="0">
                        <tr>
                          <td height="330" valign="top"
                            bgcolor="#FFFFFF">
                          <div id="Layer1"
                            style="position:absolute; width:100%; height:330; z-index:1; overflow: auto;">
                          <div dojotype="TreeLoadingController"
                            rpcurl="<%=path%>/customMsuAddTreeListen.jsp"
                            widgetid="treeController"></div>
                          <div dojotype="TreeSelector"
                            widgetid="treeSelector"></div>
                          <div dojotype="Tree" dndmode="between"
                            selector="treeSelector" widgetid="msuTree"
                            controller="treeController"><%=firLvlNodes%></div>
                          </div>
                          </td>
                        </tr>
                      </table>
                      </td>
                    </tr>
                  </table>
                  </td>
                  <td id="secondCol" valign="top" width="35%"></td>
                  <td valign="top">
                  <table width="100%" border="0">
                    <tr>
                      <td width="5%">&nbsp;</td>
                      <td width="5%"><img
                        src="<%=context%>/images/icon_file4.gif"
                        width="17" height="14"></td>
                      <td colspan="2" class="tree">操作提示：</td>
                    </tr>
                    <tr>
                      <td align="right">&nbsp;</td>
                      <td align="right">&nbsp;</td>
                      <td width="5%" align="right">1.</td>
                      <td width="90%">单击加、减号图标浏览基本指标</td>
                    </tr>
                    <tr>
                      <td align="right">&nbsp;</td>
                      <td align="right">&nbsp;</td>
                      <td align="right">2.</td>
                      <td>单击基本指标名称选择基本指标</td>
                    </tr>
                    <tr>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">3.</td>
                      <td>选择基本指标后，单击“获取指标维度”显示相应维度</td>
                    </tr>
                    <tr>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">4.</td>
                      <td>要单选某个维度值，单击其名称，要多选，按住鼠标左键（或者按住shift键或Ctrl键），然后移动鼠标或单击</td>
                    </tr>
                    <tr>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">5.</td>
                      <td>要不选择维度值，单击该维度值旁边的“不选”连接</td>
                    </tr>
                    <tr>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">&nbsp;</td>
                      <td align="right" valign="top">6.</td>
                      <td>在“自定义指标名称”输入框中输入指标名称</td>
                    </tr>
                    <tr>
                      <td align="right">&nbsp;</td>
                      <td align="right">&nbsp;</td>
                      <td align="right">7.</td>
                      <td>单击完成按钮提交</td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td><img
                        src="<%=context%>/images/icon_file4.gif"
                        width="17" height="14"></td>
                      <td colspan="2" class="tree">提交并保存</td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td colspan="2"><input name="addMsuBtn"
                        type="submit" class="button"
                        onMouseOver="switchClass(this)"
                        onMouseOut="switchClass(this)" value="完成">
                      <input name="cancelOptBtn" type="button" onclick="cancelAll();"
                        class="button" onMouseOver="switchClass(this)"
                        onMouseOut="switchClass(this)" value="取消"></td>
                    </tr>
                  </table>
                  </td>
                </tr>
                <tr>
                  <td id="thirdCol1" valign="top">&nbsp;</td>
                  <td id="thirdCol2" valign="top">&nbsp;</td>
                  <td id="thirdCol3" valign="top">&nbsp;</td>
                </tr>
              </table>
              </td>
            </tr>
          </table>
          </form>
          </TD>
          <TD width=5 background="<%=context%>/images/square_line_3.gif"></TD>
        </TR>
        <TR>
          <TD width=5><IMG height=5
            src="<%=context%>/images/square_corner_3.gif" width=5
            border=0></TD>
          <TD height="5"
            background="<%=context%>/images/square_line_4.gif"></TD>
          <TD width=5><IMG height=5
            src="<%=context%>/images/square_corner_4.gif" width=5
            border=0></TD>
        </TR>
      </TBODY>
    </TABLE>
    </td>
  </tr>
</table>
</body>
</html>

