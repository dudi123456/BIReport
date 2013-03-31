<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
	//获取用户标识
	String userId = CommonFacade.getLoginId(session);;
	String context = request.getContextPath();
	String path = context + "/report/custommsu";
	String firLvlNodes = CustomMsuUtil.assembleTreeNodes(CustomMsuUtil
			.getDayAndMonthNodes(userId),false);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.ailk.bi.report.util.CustomMsuUtil"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自定义指标管理</title>
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

  //树节点选择时更新隐藏域
  function treeSelectFired() {
  
     <!-- get a reference to the treeSelector and get the selected node -->
      var treeSelector = dojo.widget.manager.getWidgetById('treeSelector');
      var treeNode = treeSelector.selectedNode;
  
      <!-- get a reference to the songDisplay div -->
      var selectedMsuObj = document.getElementById("selectedMsu");
      var srcTabObj= document.getElementById("srcTabId");
  
      <!-- determine if this node is a folder (!isFolder == leaf node ) -->
      <!-- get the node's title and output it to the songDisplay div -->
      var msuId=treeNode['widgetId'];
      var srcTabId=treeNode['objectId'];
      var msuName=treeNode['title'];
      var selectedMsuNameObj = document.getElementById("selectedMsuName");
      selectedMsuNameObj.value=msuName;
      selectedMsuObj.value=msuId;
      srcTabObj.value=srcTabId;
  }
  
  //想服务器端获取自定义指标的维度
  function loadMsuDims(){
    var selectedMsuObj=document.getElementById('selectedMsu');
    var loadMsuDimsObj=document.getElementById('loadMsuDims');
    var srcTabObj=document.getElementById('srcTabId');
    var selectedMsu=selectedMsuObj.value;
    if(!selectedMsu || ''==selectedMsu){
      alert('请从左侧指标树选择一个自定义指标');
      return;
    }
    var srcTabId=srcTabObj.value;
    var values=srcTabId.split("|");
    srcTabId=values[0];
    if(srcTabId=='MONTH_TYPE' || srcTabId=='DAY_TYPE'){
      alert('请不要选择自定义指标基本分类情况下点击获取指标维度\n请点击其前面的加号图标选择具体的自定义指标，再单击获取指标维度\n如果没有加号图标，请选择分类，单击增加按钮先进行添加');
      return;
    }
    var rpcUrl='<%=path%>/customMsuDimsLoadAJAXAction.jsp';
    var params=['srcTabId='+srcTabId,'selectedMsu='+selectedMsu];
    params.push('action=ajaxLoadMsuDims');
    ShowWait();
    var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadDimsUpdate,ajaxError);
    ajaxHelper.sendRequest();
  }
  //加载返回的维度值
  function loadDimsUpdate(){
    var jsonTxt=this.req.responseText;
    if(jsonTxt){
      //转换为JSON对象
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
        var msuNameObj=document.getElementById('selectedMsuName');
        var msuName=msuNameObj.value;
        var tmpStr=secTdInnerHTML;
        secTdInnerHTML ='<br>自定义指标名称：<input name="customMsuName" class ="input-text" value="';
        secTdInnerHTML +=msuName+'" onKeyDown="enableModifyBtn();"><br><br>';
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
      selectHTML +='onchange="enableModifyBtn()">';
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
    	enableModifyBtn();
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
  //使维度修改按钮启用
  function enableModifyBtn(){
    var minFrmObj=document.getElementById('mainFrm');
    minFrmObj.modifyMsuBtn.disabled=false;  
  }
  //检查用户是否进行了正确选择
  function validate(){
    var customMsuNameObj=document.getElementById("customMsuName");
    if(!customMsuNameObj){
      alert('请从左侧的树中选择一个具体指标，然后单击“获取指标维度”\n显示相应信息并修改后再单击“修改”按钮');
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
    minFrmObj.modifyMsuBtn.disabled=true;  
    return true;
  }
  //修改自定义指标，不允许更改基本指标
  function modifyCustomMsu(){
    if(validate()){
      var url='<%=path%>/rptCustomMsuModifyAJAXAction.jsp';
      //构造参数
      var params=[];
      params.push('action=ajaxCustomMsuModify');
      var tmpObj=document.getElementById('selectedMsu');
      var selectedMsu=tmpObj.value;
      params.push('selectedMsu='+selectedMsu);
      tmpObj=document.getElementById('customMsuName');
      var customMsuName=tmpObj.value;
      params.push('customMsuName='+customMsuName);
      var options=getSelectedDimValue();
      if ( options && options.length > 0 ) {
        for(var i=0;i<options.length;i++){
          params.push(options[i]);
        }
      }
      ShowWait();
      var ajaxHelper=new net.ContentLoader(url,params,modifyCustomMsuUpdate,ajaxError);
      ajaxHelper.sendRequest();
    }
  }
  //获取维度列表的选择值  
  function getSelectedDimValue(){
    var params=[];
    var e=document.getElementsByTagName('select');
    if(e){
      for(var i=0;i<e.length;i++){
        var select=e[i];
        var options=select.options;
        for(var k=0;k<options.length;k++){
          if(options[k].selected){
            params.push(select.name+'='+options[k].value);
          }
        }
      }
    }
    return params;
  }
  //显示修改的结果给用户
  function modifyCustomMsuUpdate(){
    var jsonTxt=this.req.responseText;
    if(jsonTxt){
      var jsonObj=eval("("+jsonTxt+")");
      if(jsonObj){
        //显示结果信息
        closeWaitWin();
        popmsg(jsonObj.title,jsonObj.content,'<%=context%>');
        //操作是否成功
        if(jsonObj.success){
          // 如果成功，更改树的显示名称
          var tmpObj=document.getElementById('customMsuName');
          var customMsuName=tmpObj.value;
          //获取树控制器
          var treeSelector = dojo.widget.manager.getWidgetById('treeSelector');
          //获取选择的树节点
          var treeNode = treeSelector.selectedNode;
          if(treeNode){
            //声明属性对象
            var props={};
            props.title=customMsuName;
            treeNode.edit(props);
          }
        }else{
          //没有成功，还原列表的原来选择
          loadMsuDims();
          closeWaitWin();
        }
      }
    }else{
      closeWaitWin();
      ajaxError();
    }
  }
  //删除自定义指标
  function deleteCustomMsu(){
    //提交后台数据库
    //mainFrm.action="deleteCustomMsu.do";
    //mainFrm.submit();
     var tmpObj=document.getElementById('selectedMsu');
     var selectedMsu=tmpObj.value;
     if(!selectedMsu || ''==selectedMsu){
        alert('请从左侧指标树选择一个自定义指标');
        return;
     }
     var srcTabObj=document.getElementById('srcTabId');
     var srcTabId=srcTabObj.value;
     var values=srcTabId.split("|");
     srcTabId=values[0];
     if(srcTabId=='MONTH_TYPE' || srcTabId=='DAY_TYPE'){
        alert('请不要选择自定义指标基本分类，选择具体的自定义指标');
        return;
     }
     ShowWait();
     var url='<%=path%>/rptCustomMsuDeleteAJAXAction.jsp';
     var params=[];
     params.push('action=ajaxDeleteCustomMsu');
     params.push('selectedMsu='+selectedMsu);
     var ajaxHelper=new net.ContentLoader(url,params,deleteMsuUpdate,ajaxError);
     ajaxHelper.sendRequest();
   
  }
  //删除后更新树或者显示返回信息
  function deleteMsuUpdate(){
       var jsonTxt=this.req.responseText;
    if(jsonTxt){
      var jsonObj=eval("("+jsonTxt+")");
      if(jsonObj){
        //显示结果信息
        closeWaitWin();
        //popmsg(jsonObj.title,jsonObj.content,'<%=context%>');
        //操作是否成功
        if(jsonObj.success){
          // 如果成功，删除树节点
          var treeController = dojo.widget.manager.getWidgetById('treeController');
          var treeSelector = dojo.widget.manager.getWidgetById('treeSelector');
          //获取选择的树节点
          var treeNode = treeSelector.selectedNode;
          if(treeController && treeNode){
            treeController.removeNode(treeNode);
          }
          var secTdObj=document.getElementById('secondCol');
          var thdTd1Obj=document.getElementById('thirdCol1');
          var thdTd2Obj=document.getElementById('thirdCol2');
          var thdTd3Obj=document.getElementById('thirdCol3');
          if(secTdObj)
            secTdObj.innerHTML='&nbsp;';
          if(thdTd1Obj)
            thdTd1Obj.innerHTML='&nbsp;';
          if(thdTd2Obj)
            thdTd2Obj.innerHTML='&nbsp;';
          if(thdTd3Obj)
            thdTd3Obj.innerHTML='&nbsp;';              
          var tmpObj=document.getElementById('selectedMsu');
          if(tmpObj)
            tmpObj.value="";
        }else{
          //没有成功，还原列表的原来选择
          //loadMsuDims();
           closeWaitWin();
        }
      }
    }else{
      closeWaitWin();
      ajaxError();
    }
  }
 function addCustomMsu(){
  var eSelectedMsu=document.getElementById("selectedMsu");
  var type=eSelectedMsu.value;
  if(type!="MONTH_TYPE" && type!="DAY_TYPE"){
    alert("请您先选择是添加月指标或是日指标，单击左侧树的“月指标”或“日指标”，然后单击增加按钮");
    return false;
  }
  if(type=="MONTH_TYPE"){
    closeWaitWin();
    window.location="loadCustomMsu.rptdo?stat_period=4";
  }
  if(type=="DAY_TYPE"){
    closeWaitWin();
    window.location="loadCustomMsu.rptdo?stat_period=6";
  }
 }
  //初始化树
  function init() {
      <!-- get a reference to the treeSelector -->
      var treeSelector = dojo.widget.manager.getWidgetById('treeSelector');
      var loadMsuDimsBtn=document.getElementById('loadMsuDimsBtn');
      
      <!-- connect the select event to the function treeSelectFired() -->
      dojo.event.connect(treeSelector,'select','treeSelectFired');
      dojo.event.connect(loadMsuDimsBtn,'onclick','loadMsuDims');
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
    <TABLE width=100% height=100% border=0 cellPadding=0 cellspacing="0"
      class="B">
      <TBODY>
        <TR>
          <TD width=5 background="<%=context%>/images/square_line_2.gif"></TD>
          <TD width="100%" background="<%=context%>/images/dot4.gif">
          <form name="mainFrm" action="modifyCustomMsu.rptdo" method="post"
            onsubmit="return validate();"><input type="hidden"
            name="selectedMsu" value=""> <input type="hidden"
            name="srcTabId" value=""> <input type="hidden"
            name="selectedMsuName" value="">
          <table width="98%" border="0" align="center">
            <tr>
              <td height="8" colspan="2"></td>
            </tr>
            <tr>
              <td><img src="<%=context%>/images/derive_arrow.gif"
                width="7" height="7"> 浏览并选择指标</td>
              <td><input id="loadMsuDimsBtn" type="button"
                class="button-image" name="loadMsuDimsBtn"></td>
              <td width="34%">&nbsp;</td>
            </tr>
            <tr>
              <td width="33%" valign="top">
              <table width="100%" border="0">
                <tr>
                  <td class="tree"><img
                    src="<%=context%>/images/icon_file4.gif" width="17"
                    height="14"> 自定义指标</td>
                </tr>
                <tr>
                  <td class="input-text">
                  <table width="100%" height="100%" border="0"
                    cellpadding="0" cellspacing="0">
                    <tr>
                      <td height="330" valign="top" bgcolor="#FFFFFF">
                      <div id="Layer1"
                        style="position:absolute; width:100%; height:330; z-index:1; overflow: auto;">
                      <div dojotype="TreeLoadingController"
                        rpcurl="<%=path%>/customMsuTreeListen.jsp"
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
              <td id="secondCol" valign="top" width="30%"></td>
              <td valign="top">
              <table width="100%" border="0">
                <tr>
                  <td width="5%">&nbsp;</td>
                  <td width="5%"><img
                    src="<%=context%>/images/icon_file4.gif" width="17"
                    height="14"></td>
                  <td colspan="2" class="tree">操作提示：</td>
                </tr>
                <tr>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td width="5%" align="right">1.</td>
                  <td width="90%">单击加、减号图标浏览自定义指标</td>
                </tr>
                <tr>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">2.</td>
                  <td>单击指标名称选择自定义指标</td>
                </tr>
                <tr>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">3.</td>
                  <td>选择指标后，单击上面的“获取指标维度”显示维度值下拉框</td>
                </tr>
                <tr>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">4.</td>
                  <td>
                  要更高某个维度值，单击其名称，要多选，按住鼠标左键（或者按住shift键或Ctrl键），然后移动鼠标或单击</td>
                </tr>
                <tr>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">5.</td>
                  <td>要不选择维度值，单击该维度名称旁边的“不选”连接</td>
                </tr>
                <tr>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">6.</td>
                  <td>确认“自定义指标名称”输中的指标名称</td>
                </tr>
                <tr>
                  <td align="right">&nbsp;</td>
                  <td align="right">&nbsp;</td>
                  <td align="right">7.</td>
                  <td>单击修改按钮提交</td>
                </tr>
                <tr>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">&nbsp;</td>
                  <td align="right" valign="top">8.</td>
                  <td>要删除某个自定义指标，在树中选择，然后单击“删除”按钮</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td><img src="<%=context%>/images/icon_file4.gif"
                    width="17" height="14"></td>
                  <td colspan="2" class="tree">单击修改或删除按钮提交</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td colspan="2"><input name="modifyMsuBtn"
                    type="button" class="button"
                    onMouseOver="switchClass(this)"
                    onMouseOut="switchClass(this)" value="修改"
                    onclick="modifyCustomMsu();"> <input
                    name="deleteMsuBtn" type="button" class="button"
                    onMouseOver="switchClass(this)"
                    onMouseOut="switchClass(this)" value="删除"
                    onClick="deleteCustomMsu();"></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td colspan="2"><input name="modifyMsuBtn2"
                    type="button" class="button"
                    onMouseOver="switchClass(this)"
                    onMouseOut="switchClass(this)" value="增加"
                    onclick="addCustomMsu();"> <input
                    name="Cancel" type="reset" class="button"
                    onMouseOver="switchClass(this)"
                    onMouseOut="switchClass(this)" value="取消"></td>
                </tr>
              </table>
              </td>
            </tr>
            <tr>
              <td id="thirdCol1" valign="top">&nbsp;</td>
              <td id="thirdCol2" valign="top"></td>
              <td id="thirdCol3" valign="top">&nbsp;</td>
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
