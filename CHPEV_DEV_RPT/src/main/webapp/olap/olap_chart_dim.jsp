<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapChartAttrStruct"%>
<%@page import="com.ailk.bi.base.table.RptOlapDimTable"%>
<%@page import="com.ailk.bi.olap.domain.RptOlapDimStruct"%>
<%@page import="com.ailk.bi.olap.util.RptOlapConsts"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ailk.bi.base.table.DimLevelTable"%>
<%@page import="com.ailk.bi.olap.util.RptOlapStringUtil"%>
<%@page import="java.util.Map"%>
<%@ include file="/base/commonHtml.jsp"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
	response.setDateHeader("Expires", 1);
%>
<!--从session中取出值来-->
<%
	String reportId = null;
	Object tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_REPORT_ID);
	if (null == tmpObj) {
		return;
	}
	reportId = (String) tmpObj;

	String chartType = null;
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CUR_FUNC_OBJ + "_"
			+ reportId);
	if (null != tmpObj) {
		chartType = ((RptOlapFuncStruct) tmpObj).getCurFunc();
	}
	if (null == chartType)
		chartType = RptOlapConsts.OLAP_FUN_LINE;

	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DIM + "_"
			+ reportId);
	RptOlapChartAttrStruct chartStruct = null;
	if (null != tmpObj) {
		chartStruct = (RptOlapChartAttrStruct) tmpObj;
	} else
		return;

	String dimJSObj = null;
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DIM_JS_OBJECT
			+ "_" + reportId);
	if (null != tmpObj)
		dimJSObj = (String) tmpObj;
	if (null == dimJSObj)
		dimJSObj = "";
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DIM_VALUES
			+ "_" + reportId);
	Map map = null;
	if (null != tmpObj) {
		map = (Map) tmpObj;
	} else
		return;
	StringBuffer sbValue = new StringBuffer();
	List list = (List) map.get(chartStruct.getLevel());
	RptOlapDimStruct[] values = (RptOlapDimStruct[]) list
			.toArray(new RptOlapDimStruct[list.size()]);
	if(values.length <= 0){
		System.out.println("olap dim values is zero! level="+chartStruct.getLevel());
		System.out.println("olap dim values is zero! Object="+map.get(chartStruct.getLevel()));
		System.out.println(session.getAttribute(WebKeys.ATTR_OLAP_CHART_DIM_VALUES
				+ "_" + reportId));

	}
	for (int i = 0; i < values.length; i++) {
		sbValue.append(values[i].getDim_id()).append("::");
	}
	RptOlapDimTable rptDim = (RptOlapDimTable) chartStruct
			.getRptStruct();
	List levels = rptDim.dimInfo.dim_levels;
	String maxLevel = rptDim.max_level;
	tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DOMAINS_OBJ
			+ "_" + reportId);
	if (null == tmpObj) {
		return;
	}
	List chartStructs = (List) tmpObj;
%>
<%
	StringBuffer msuChkHTML = new StringBuffer();
	StringBuffer msuRdoHTML = new StringBuffer();
	msuChkHTML
			.append("<table width=\"100%\" border=\"0\" align=\"center\"");
	msuChkHTML.append(" cellpadding=\"0\" cellspacing=\"0\">");
	msuRdoHTML
			.append("<table width=\"100%\" border=\"0\" align=\"center\"");
	msuRdoHTML.append(" cellpadding=\"0\" cellspacing=\"0\">");
	Iterator iter = chartStructs.iterator();
	while (iter.hasNext()) {
		RptOlapChartAttrStruct tmpStruct = (RptOlapChartAttrStruct) iter
		.next();
		if (!tmpStruct.isDim()) {
			RptOlapMsuTable rptMsu = (RptOlapMsuTable) tmpStruct
			.getRptStruct();
			StringBuffer sb = new StringBuffer();
			sb.append("<tr>");
			sb.append("<td nowrap align=\"left\">");
			sb.append("<input type=\"");
			sb.append("checkbox");
			sb.append("\" name=\"msu_group\" id=\"msu_group_");
			sb.append(rptMsu.msu_id).append("\"");
			sb.append(" value=\"");
			sb.append(rptMsu.msu_id);
			sb.append("\" onclick=\"addMsu(this)\"/>");
			sb.append("<label for=\"msu_group_");
			sb.append(rptMsu.msu_id).append("\">");
			sb
			.append((null == rptMsu.col_name
			|| "".equals(rptMsu.col_name) ? rptMsu.msuInfo.msu_name
			: rptMsu.col_name));
			sb.append("</label>");
			sb.append("</td></tr>");
			msuChkHTML.append(sb);
			String tmpStr = sb.toString();
			tmpStr = tmpStr.replaceAll("checkbox", "radio");
			msuRdoHTML.append(tmpStr);
		}
	}
	msuChkHTML.append("</table>");
	msuRdoHTML.append("</table>");
%>
<%=WebPageTool
							.pageScript("dimFrm", "rptOlapChartDim.screen")%>
<%
	///获取翻页相关信息
	PagesInfoStruct pageInfo = null;
	pageInfo = WebPageTool.getPageInfo(request, values.length, 20);
%>

<%@page import="com.ailk.bi.olap.domain.RptOlapFuncStruct"%>
<%@page import="com.ailk.bi.base.table.RptOlapMsuTable"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联通统一经营分析系统</title>
<link rel="stylesheet" href="<%=context%>/css/other/css.css" type="text/css">
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css"
	type="text/css">
<script type="text/javascript">
  var dimObjId="<%=dimJSObj%>";
</script>
</head>
<body>
<form name="dimFrm" target="_self">
<!--//设置翻页隐藏域-->
<%=WebPageTool.pageHidden(pageInfo)%>
<input type="hidden" name="selectAll" value="">
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td align="center" valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="7"><img
					src="<%=context%>/images/common/system/tab_corner_1.gif" width="7" height="22"></td>

				<td width="100%" valign="bottom"
					background="<%=context%>/images/common/system/tab_corner_bg.gif"><img
					src="<%=context%>/images/common/system/ztfx_pic21.gif" width="17" height="13"
					hspace="5"><B><%=rptDim.dimInfo.dim_name%></B></td>
				<td width="10" align="right" valign="baseline"
					background="<%=context%>/images/common/system/tab_corner_bg.gif"><img
					src="<%=context%>/images/common/system/tab_corner_5.gif" width="7" height="22"></td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" valign="top" class="bule-side-3">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td align="right"><img
							src="<%=context%>/images/common/system/check_all.gif" width="16" height="15">
						<a href="javascript:selectAll(<%= chartStruct.getLevel()%>);"
							class="bule">全选</a> <img
							src="<%=context%>/images/common/system/check_cancel.gif" width="16" height="15">
						<a href="javascript:cancelAll(<%= chartStruct.getLevel()%>);"
							class="bule">取消全选</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>
								<%
											if (!RptOlapConsts.ZERO_STR.equals(maxLevel) && null != levels
											&& 0 < levels.size()) {
										StringBuffer sb = new StringBuffer();
										sb.append("维度层次：");
										sb.append("<a href=\"");
										sb.append(RptOlapConsts.CHART_DIM_VALUES_LOAD + "?report_id="
										+ reportId);
										sb.append("&dim_id=" + rptDim.dim_id);
										sb.append("&dim_object=" + dimJSObj);
										sb.append("&dim_level=" + RptOlapConsts.ZERO_STR);
										sb.append("\" onclick=\"javascript:setParentBlocking();\" class=\"bule\" target=\"_self\">");
										if (RptOlapConsts.ZERO_STR.equals(chartStruct.getLevel())) {
											sb.append("<strong>");
											sb.append(rptDim.dimInfo.dim_name);
											sb.append("</strong>");
										} else
											sb.append(rptDim.dimInfo.dim_name);
										sb.append("</a>&gt;&gt;");
										//有其他层次
										iter = levels.iterator();
										while (iter.hasNext()) {
											DimLevelTable level = (DimLevelTable) iter.next();
											sb.append("<a href=\"javascript:;\"");
											sb.append("\" onclick=\"javascript:setParentBlocking('");
											sb.append(RptOlapConsts.CHART_DIM_VALUES_LOAD
													+ "?report_id=" + reportId);
											sb.append("&dim_id=" + rptDim.dim_id);
											sb.append("&dim_object=" + dimJSObj);
											sb.append("&dim_level=" + level.lvl_id);
											sb.append("')\" class=\"bule\" target=\"_self\">");
											if (level.lvl_id.equals(chartStruct.getLevel())) {
										sb.append("<strong>");
										sb.append(level.lvl_name);
										sb.append("</strong>");
											} else
										sb.append(level.lvl_name);
											sb.append("</a>&gt;&gt;");
											if (maxLevel.equals(level.lvl_id)) {
										break;
											}
										}
										String tmpStr = sb.toString();
										tmpStr = RptOlapStringUtil.removeLastSubStr(tmpStr, "&gt;&gt;");
										out.println(tmpStr);
									}
								%>
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td nowarp width="100%"><%=WebPageTool.pagePolit(pageInfo)%></td>
					</tr>
				</table>
				<table width="98%" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td>
			<!--循环取出值显示-->
			<%
 			for (int i = 0; i < pageInfo.iLinesPerPage
 			&& (i + pageInfo.absRowNoCurPage()) < pageInfo.iLines; i++) {
		 		out.println("<tr>\n");
		 		for (int j = 0; j < 2; j++) {
		 			int index =  i + pageInfo.absRowNoCurPage();
		 			if (index < values.length) {
				 		RptOlapDimStruct dimStruct = values[index];
				 		//需要从1－4，然后换行
				 		StringBuffer sb = new StringBuffer();
				 		sb.append("<td nowrap align=\"left\""+(j==0?"width=\"50%\"":"")+">");
				 		sb.append("<input type=\"checkbox\" id=\"dim_value_");
				 		sb.append(dimStruct.getDim_id());
				 		sb.append("\" name=\"dim_value\" value=\"");
				 		sb.append(dimStruct.getDim_id());
				 		sb.append("\"");
				 		if (dimStruct.isChecked())
				 			sb.append(" checked");
				 		//加上单击事件，需要维度标识、层次标识、维度值
				 		sb.append(" onClick=\"onDimValueClick(this,'").append(
				 				rptDim.dim_id).append("',").append(
				 				chartStruct.getLevel()).append(",'").append(
				 				dimStruct.getDim_id()).append("');\" ");
				 		sb.append(">");
				 		sb.append("<label for=\"dim_value_").append(
				 				dimStruct.getDim_id()).append("\">");
				 		sb.append(dimStruct.getDim_desc());
				 		sb.append("</label>");
				 		sb.append("</td>");
				 		out.println(sb.toString());
			 		} else {
			 			out.println("<td>&nbsp;</td>\n");
			 		}
			 		if(j<1)
			 			i++;
			 	}
			 	out.println("</tr>\n");
 	}
 %>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
<script type="text/javascript">
  var msuChkHTML='<%=msuChkHTML.toString()%>';
  var msuRdoHTML='<%=msuRdoHTML.toString()%>';;
  var dimValuesSize=<%=values.length%>;
  //这里可能会很大，但没办法
  var dimAllValuesString="<%=sbValue.toString()%>";
  //转换成数组
  var dimAllValues=dimAllValuesString.split("::");




  function init(){
    var values=dimObjId.split("_");
    var dimId=values[2];
    var isGroup=(values[1]=="group"?true:false);
    var parentNode=parent.document.getElementById("hiddenSpan");
    var elements=parentNode.childNodes;
    if(isGroup){
      //维度分组
      var isThisDim=false;
      var useAllValues=false;
      var dimValueInclude=true;
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        //如果是多层次呢
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP%>"
            && element.value==dimId){
          isThisDim=true;
          break;
        }
      }
      if(isThisDim){
        isThisDim=false;
        for(var i=0;i<elements.length;i++){
          var element=elements[i];
          //如果是多层次呢
          if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>"
              && element.value=="<%=chartStruct.getLevel()%>"){
            isThisDim=true;
            break;
          }
        }
      }
      if(isThisDim){
        var values="";
        //就是这个维度
        for(var i=0;i<elements.length;i++){
          var element=elements[i];
          if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>"){
            useAllValues=(element.value=="true"?true:false);
          }
          if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>"){
            dimValueInclude=(element.value=="true"?true:false);
          }
          if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>"){
            values +=element.value+"::";
          }
        }
        if(useAllValues){
          //使用所有的值
          var e=document.getElementsByName("dim_value");
          for(var i=0;i<e.length;i++){
            e[i].checked=true;
          }
        }else{
          if(dimValueInclude){
            var e=document.getElementsByName("dim_value");
            for(var i=0;i<e.length;i++){
              if(values.indexOf((e[i].value+"::"))>=0){
                e[i].checked=true;
              }else{
                e[i].checked=false;
              }
            }
          }else{
            var e=document.getElementsByName("dim_value");
            for(var i=0;i<e.length;i++){
              if(values.indexOf(e[i].value+"::")<0)
               e[i].checked=true;
              else
               e[i].checked=false;
            }
          }
        }
      }else{
        //维度分组隐藏没有，它又是进行维度分组选择
         var e=document.getElementsByName("dim_value");
         for(var i=0;i<e.length;i++){
           e[i].checked=false;
         }
      }
    }else{
      //维度条件
      var hasThisDim=false;
      var useAllValues=false;
      var include=true;
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>"
            && element.value==dimId){
          hasThisDim=true;
          break;
        }
      }
      if(hasThisDim){
        hasThisDim=false;
        for(var i=0;i<elements.length;i++){
          var element=elements[i];
          if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId)
              && element.value=="<%=chartStruct.getLevel()%>"){
            hasThisDim=true;
            break;
          }
        }
      }
      if(hasThisDim){
        var values="";
        //就是这个维度
        for(var i=0;i<elements.length;i++){
          var element=elements[i];
          if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId)){
            useAllValues=(element.value=="true"?true:false);
          }
          if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId)){
            include=(element.value=="true"?true:false);
          }
          if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId)){
            values +=element.value+"::";
          }
        }
        if(useAllValues){
          //使用所有的值
          var e=document.getElementsByName("dim_value");
          for(var i=0;i<e.length;i++){
            e[i].checked=true;
          }
        }else{
          if(include){
            var e=document.getElementsByName("dim_value");
            for(var i=0;i<e.length;i++){
              if(values.indexOf((e[i].value+"::"))>=0)
               e[i].checked=true;
              else
               e[i].checked=false;
            }
          }else{
            var e=document.getElementsByName("dim_value");
            for(var i=0;i<e.length;i++){
              if(values.indexOf((e[i].value+"::"))<0)
               e[i].checked=true;
              else
               e[i].checked=false
            }
          }
        }
      }else{
        //维度分组隐藏没有，它又是进行维度分组选择
         var e=document.getElementsByName("dim_value");
         for(var i=0;i<e.length;i++){
           e[i].checked=false;
         }
      }
    }
  }

  $(document).ready(function(){
	  setTimeout(function(){
		  var lines=<%=(pageInfo.iLinesPerPage > pageInfo.iLines/2? (pageInfo.iLines/2==0? 1:pageInfo.iLines/2):pageInfo.iLinesPerPage/2)*20+90%>;
		  var frm=parent.document.getElementById("chartDimValues");
		  frm.height=lines;
		  //这里为了避免重复调用需要设置个参数才可以
		  parent.displayDim(lines);
	  },0);
	  init();
  });

  function selectAll(level){
    var e=document.getElementsByName("dim_value");
    for(var i=0;i<e.length;i++){
      e[i].checked=true;
    }
    var eSelectAll=document.getElementById("selectAll");
    if(eSelectAll){
      eSelectAll.value="true";
    }
    var parentNode=parent.document.getElementById("hiddenSpan");
    var elements=parentNode.childNodes;
    var element;
    var dimObj=parent.document.getElementById(dimObjId);
    if(dimObj){
      dimObj.checked=true;
      //需要设置隐藏变量,首先判断哪里是分组、条件维度

      //获得维度标识
      var values=dimObjId.split("_");
      var dimId=values[2];
      var isGroup=(values[1]=="group"?true:false);
      //先看看是不是分组维度那过来的
      //获得所有隐藏的变量
      if(isGroup){
        //判断有没有其他层次的分组
        var hasOtherLevel=false;
        for(var i=0;i<elements.length;i++){
          if(elements[i].name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>"
              && level!=elements[i].value){
            //有这个隐藏变量，且不是同一层次
            hasOtherLevel=true;
            break;
          }
        }
        if(!hasOtherLevel || (hasOtherLevel
            && confirm("您已经在该维度的其他层次选择了具体分组值,\n在本层次选择将覆盖其他层次的选择，您是否覆盖其他层次的选择"))){
          //覆盖其他层次，先删除所有分组维度,然后添加
          //删除分组维度值
          //这里还得看看，全选、单击取消某个，又单击该值，要设置全部,且具体维度值没有了
            deleteGroupHidden(parentNode,elements);
            //新增新的维度
            addGroupHidden(parentNode,dimId,level);
            //删除可能的条件
            e=foundValueChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>",dimId);
            if(e){
              parentNode.removeChild(e);
            }
            e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId);
            if(e){
              parentNode.removeChild(e);
            }
            e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId);
            if(e){
              parentNode.removeChild(e);
            }
            e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId);
            if(e){
              parentNode.removeChild(e);
            }
            var removeChildren=[];
            for(var i=0;i<elements.length;i++){
               if(elements[i].name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId)
                 removeChildren.push(elements[i]);
            }
            for(var i=0;i<removeChildren.length;i++){
               parentNode.removeChild(removeChildren[i]);
            }
            //删除其余的具体值
            removeChildren=[];
            for(var i=0;i<elements.length;i++){
                if(elements[i].name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>")
                  removeChildren.push(elements[i]);
            }
            for(var i=0;i<removeChildren.length;i++){
               parentNode.removeChild(removeChildren[i]);
            }
            //需要设置其他为不可用状态
            var chkBoxs=parent.document.getElementsByName("dim_group");
            for(var i=0;i<chkBoxs.length;i++){
              if(chkBoxs[i].id!="dim_group_"+dimId)
               chkBoxs[i].disabled="true";
            }
            chkBoxs=parent.document.getElementsByName("dim_where");
            for(var i=0;i<chkBoxs.length;i++){
              if(chkBoxs[i].id=="dim_where_"+dimId){
               chkBoxs[i].checked=false;
               chkBoxs[i].disabled="true";
              }
            }
        }
      }else{
        //是条件维度，可能有多个
        var hasOtherLevel=false;
        var compare="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId;
        for(var i=0;i<elements.length;i++){
          if(elements[i].name==compare && level!=elements[i].value){
            //有这个隐藏变量，且不是同一层次
            hasOtherLevel=true;
            break;
          }
        }
        //看看有没有
        if(!hasOtherLevel || (hasOtherLevel
            && confirm("您已经在该维度的其他层次选择了具体条件值,\n在本层次选择将覆盖其他层次的选择，您是否覆盖其他层次的选择"))){
          //覆盖其他层次，先删除所有分组维度,然后添加
          //删除分组维度值
          deleteWhereHidden(parentNode,elements,dimId);
          //新增新的维度
          addWhereHidden(parentNode,dimId,level);

          //删除维度分组,好像不用，因为既然点击到这里，维度值肯定不是这个维度了
          chkBoxs=parent.document.getElementsByName("dim_group");
          for(var i=0;i<chkBoxs.length;i++){
            if(chkBoxs[i].id=="dim_group_"+dimId)
             chkBoxs[i].disabled="disabled";
          }
        }
      }
    }
    convertMsuHidden(parentNode,elements);
  }
  function cancelAll(level){
     var e=document.getElementsByName("dim_value");
    for(var i=0;i<e.length;i++){
      e[i].checked=false;
    }
    var parentNode=parent.document.getElementById("hiddenSpan");
    var elements=parentNode.childNodes;
    var element;
    var eSelectAll=document.getElementById("selectAll");
    if(eSelectAll)
      eSelectAll.value="false";
     var dimObj=parent.document.getElementById(dimObjId);
     if(dimObj){
      dimObj.checked=false;
      //需要设置隐藏变量,首先判断哪里是分组、条件维度
      //获得维度标识
          //获得维度标识
      var values=dimObjId.split("_");
      var dimId=values[2];
      var isGroup=(values[1]=="group"?true:false);
      //先看看是不是分组维度那过来的
      //获得所有隐藏的变量
      if(isGroup){
        //设置
        //去除
        var e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP%>");
        if(e){
          parentNode.removeChild(e);
        }
        e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>");
        if(e)
         parentNode.removeChild(e);
        e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>");
        if(e)
         parentNode.removeChild(e);
        e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>");
        if(e)
         parentNode.removeChild(e);
        //这里需要设置其他的选择框为可选择状态

        var chkBoxs=parent.document.getElementsByName("dim_group");
        for(var i=0;i<chkBoxs.length;i++){
          //这里还要看看是否有维度条件选中
          var tmpDimId=chkBoxs[i].value;
          var hasValue=hasWhereValue(elements,tmpDimId);
          if(!hasValue)
             chkBoxs[i].disabled="";
        }
        chkBoxs=parent.document.getElementsByName("dim_where");
        for(var i=0;i<chkBoxs.length;i++){
          if(chkBoxs[i].id=="dim_where_"+dimId)
           chkBoxs[i].disabled="";
        }
        //删除其余的具体值
        //这里每删除一个节点，就总数少了，试试先找到每个引用，用数组保存，然后循环数组，再移除
        //这样做是可以的
        var removeChildren=[];
        for(var i=0;i<elements.length;i++){
            if(elements[i].name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>")
              removeChildren.push(elements[i]);
        }
        for(var i=0;i<removeChildren.length;i++){
           parentNode.removeChild(removeChildren[i]);
        }
      }else{
        //是条件维度，可能有多个
        var e=foundValueChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>",dimId);
        if(e){
           parentNode.removeChild(e);
        }
        e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId);
        if(e)
           parentNode.removeChild(e);
        e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId);
        if(e)
           parentNode.removeChild(e);
        e=foundChild(elements,"<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId);
        if(e)
           parentNode.removeChild(e);
        var removeChildren=[];
        for(var i=0;i<elements.length;i++){
            if(elements[i].name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId))
              removeChildren.push(elements[i]);
        }
        for(var i=0;i<removeChildren.length;i++){
           parentNode.removeChild(removeChildren[i]);
        }
        //得设置维度那里为可选
        //这里还得判断是否已经有选中的了
        chkBoxs=parent.document.getElementsByName("dim_group");
        var hasChkBoxSelected=false;
        for(var i=0;i<chkBoxs.length;i++){
        	if(chkBoxs[i].checked){
        		hasChkBoxSelected=true;
        		break;
        	}
        }
        if(!hasChkBoxSelected){
	        for(var i=0;i<chkBoxs.length;i++){
	          if(chkBoxs[i].id=="dim_group_"+dimId)
	           chkBoxs[i].disabled="";
	        }
        }
      }
     }
     convertMsuHidden(parentNode,elements);
  }
  function foundChild(elements,objId){
    var child;
    if(elements && objId){
      for(var i=0;i<elements.length;i++){
        if(elements[i].name==objId){
          child=elements[i];
          break;
        }
      }
    }
    return child;
  }
  function foundValueChild(elements,objId,value){
    var child;
    if(elements && objId && value){
      for(var i=0;i<elements.length;i++){
        if(elements[i].name==objId && elements[i].value==value){
          child=elements[i];
          break;
        }
      }
    }
    return child;
  }
  //当单击维度值选择框时，要判断是否选中，还是取消选择
  function onDimValueClick(dimValueObj,dimId,level,value){
    //还要判断是分组维度，抑或是条件维度，
    //层次水平呢，是加在每个变量上，还是单独一个变量
    //规则，首先判断不同层次是否有选择，有提示用户其他层次有选择，
    //让用户选择是否覆盖其他层次，是的话取消其他层次，然后再加上
    //如果用户选择否，什么也不进行操作，此原则同时适用于全选，取消全选
    if(dimValueObj){
      //取得是否选中
      var checked=dimValueObj.checked;
      var vlaue=dimValueObj.value;
      //获取隐藏的所有变量
      var parentNode=parent.document.getElementById("hiddenSpan");
      var elements=parentNode.childNodes;
      var element;
      //判断是否是分组
      var values=dimObjId.split("_");
      var dimId=values[2];
      var isGroup=(values[1]=="group"?true:false);
      if(checked){
        //选中状态
        //判断父窗口该维度的层次水平
        if(isGroup){
          //分组维度
          //循环所有隐藏变量，看是否是相同层次
          var hasOtherLevel=false;
          for(var i=0;i<elements.length;i++){
            if(elements[i].name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>"
                && level!=elements[i].value){
              //有这个隐藏变量，且不是同一层次
              hasOtherLevel=true;
              break;
            }
          }
          if(!hasOtherLevel || (hasOtherLevel
              && confirm("您已经在该维度的其他层次选择了具体分组值,\n在本层次选择将覆盖其他层次的选择，您是否覆盖其他层次的选择"))){
            //覆盖其他层次，先删除所有分组维度,然后添加
            //删除分组维度值
            //这里还得看看，全选、单击取消某个，又单击该值，要设置全部,且具体维度值没有了
            if(hasOtherLevel){
              deleteGroupHidden(parentNode,elements);
              //新增新的维度
              addGroupHidden(parentNode,dimId,level);
            }
            //判断以前就没有
            var onlyOneExclude=isOnlyOneGroupExclude(elements,value);
            if(onlyOneExclude){
              //只需设置
              deleteGroupHidden(parentNode,elements);
              addGroupHidden(parentNode,dimId,level);
              //
              deleteGroupHiddenValue(parentNode,elements,dimId,value);
              //设置使用所有的值
              setGroupAllValues(elements,true);
            }else{
              //这里判断一下
	            var groupAdded=hasGroupHiddenAdded(elements,dimId);
              if(!groupAdded){
                addGroupHidden(parentNode,dimId,level);
              }
              //这里还有判断当前是排除，还是包括
              var include=getGroupIncludeValue(elements);
              if(include){
                setGroupAllValues(elements,"false");
                addGroupHiddenValue(parentNode,dimId,level,value);
              }else{
                deleteGroupHiddenValue(parentNode,elements,dimId,value);
              }
            }
            //父亲选中
            var dimObj=parent.document.getElementById(dimObjId);
            if(dimObj){
              dimObj.checked=true;
            }
            //还得设置条件维度那里不能选择了
            var e=parent.document.getElementsByName("dim_where");
            for(var i=0;i<e.length;i++){
              if(e[i].value==dimId){
                e[i].disabled="true";
              }
            }
            e=parent.document.getElementsByName("dim_group");
            for(var i=0;i<e.length;i++){
              if(e[i].value!=dimId){
                e[i].disabled="true";
              }
            }
          }else{
            //什么也不作,恢复原状
            dimValueObj.checked=!checked;
          }
          convertGroupHidden(parentNode,elements);
        }else{
          //条件维度
          var hasOtherLevel=false;
          var compare="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId;
          for(var i=0;i<elements.length;i++){
            if(elements[i].name==compare && level!=elements[i].value){
              //有这个隐藏变量，且不是同一层次
              hasOtherLevel=true;
              break;
            }
          }
          //看看有没有
           if(!hasOtherLevel || (hasOtherLevel
              && confirm("您已经在该维度的其他层次选择了具体条件值,\n在本层次选择将覆盖其他层次的选择，您是否覆盖其他层次的选择"))){
            //覆盖其他层次，先删除所有分组维度,然后添加
            //删除分组维度值
            if(hasOtherLevel){
              deleteWhereHidden(parentNode,elements,dimId);
              //新增新的维度
              addWhereHidden(parentNode,dimId,level);
            }
            var onlyOneExclude=isOnlyOneWhereExclude(elements,dimId,value);
            if(onlyOneExclude){
              deleteWhereHidden(parentNode,elements,dimId);
              addWhereHidden(parentNode,dimId,level);
              deleteWhereHiddenValue(parentNode,elements,dimId,value);
              setWhereAllValues(elements,dimId,"true");
            }else{
              var whereAdded=hasWhereHiddenAdded(elements,dimId);
              if(!whereAdded){
                addWhereHidden(parentNode,dimId,level);
              }
              var isInclude=getWhereIncludeValue(elements,dimId);
              if(isInclude){
                setWhereAllValues(elements,dimId,"false");
                addWhereHiddenValue(parentNode,dimId,level,value);
              }else{
                deleteWhereHiddenValue(parentNode,elements,dimId,value);
              }
            }
            //父亲选中
            var dimObj=parent.document.getElementById(dimObjId);
            if(dimObj){
              dimObj.checked=true;
            }
            //还得设置条件维度那里不能选择了
            var e=parent.document.getElementsByName("dim_group");
            for(var i=0;i<e.length;i++){
              if(e[i].value==dimId){
                e[i].disabled="true";
              }
            }
          }else{
            //什么也不作,恢复原状
            dimValueObj.checked=!checked;
          }
          convertWhereHidden(parentNode,elements,dimId);
        }
      }else{
        //取消选中,不必进行判断了，删除即可
        if(isGroup){
          //删除分组值,如果是全部选中后，单击取消，做的工作更多
           //判断是否全部选中，如果是的话，排除本值
           var selectedAll=isSelectAllForGroup(elements);
           if(selectedAll){
            //需要排除
            //设置排除
            var include=false;
            setGroupInclude(elements,include);
            //由于前面应该是全部，因此没有此维度值，
            addGroupHiddenValue(parentNode,dimId,level,value);
            //不用删除维度值了，因为是排除
           }else{
            //这里如果用户点击取消一个，这是变成排除一个，
            //此时再点击又取消一个，需要加一个排除
            //还要考虑如果仅剩最后一个，或者几个，找个算法，如果选中的小于
            //一半时，则添加少的那边
            var isInclude=getGroupIncludeValue(elements);
            if(!isInclude){
              addGroupHiddenValue(parentNode,dimId,level,value);
            }else{
            //只删除维度值,这里还得判断，是否就自己一个值了，如果是，要清除全部其他变量
            //此时已经不是全部选择了，那看看还有其他值没有
              deleteGroupHiddenValue(parentNode,elements,dimId,value);
            }
            var hasOtherValue=hasOtherGroupValue(elements,value);
            if(!hasOtherValue){
              //没有其他值了
              deleteGroupHidden(parentNode,elements);
              //设置父亲为非选中
              var dimObj=parent.document.getElementById(dimObjId)
              dimObj.checked=false;
              var chkBoxs=parent.document.getElementsByName("dim_group");
              for(var i=0;i<chkBoxs.length;i++){
                //这里还要看看是否有维度条件选中
                var tmpDimId=chkBoxs[i].value;
                var hasValue=hasWhereValue(elements,tmpDimId);
                if(!hasValue)
                   chkBoxs[i].disabled="";
              }
              chkBoxs=parent.document.getElementsByName("dim_where");
              for(var i=0;i<chkBoxs.length;i++){
                if(chkBoxs[i].id=="dim_where_"+dimId)
                 chkBoxs[i].disabled="";
              }
            }
           }
           //这里用来判断
           convertGroupHidden(parentNode,elements);
        }else{
          //删除条件值，如果是全部选中后，单击取消，做的工作更多
          //觉得用排除法，这样省事
           var selectedAll=isSelectAllForWhere(elements,dimId);
           if(selectedAll){
            var include=false;
            setWhereInclude(elements,dimId,include);
            addWhereHiddenValue(parentNode,dimId,level,value);
           }else{
            var isInclude=getWhereIncludeValue(elements,dimId);
            if(!isInclude){
              addWhereHiddenValue(parentNode,dimId,level,value);
            }else{
              deleteWhereHiddenValue(parentNode,elements,dimId,value);
            }
            var hasOtherValue=hasOtherWhereValue(elements,dimId,value);
            if(!hasOtherValue){
              deleteWhereHidden(parentNode,elements,dimId);
              var dimObj=parent.document.getElementById(dimObjId)
              dimObj.checked=false;
              var chkBoxs=parent.document.getElementsByName("dim_group");
              var hasChkBoxSelected=false;
              for(var i=0;i<chkBoxs.length;i++){
              	if(chkBoxs[i].checked){
              		hasChkBoxSelected=true;
              		break;
              	}
              }
              if(!hasChkBoxSelected){
	              for(var i=0;i<chkBoxs.length;i++){
	                //这里还要看看是否有维度条件选中
	                var tmpDimId=chkBoxs[i].value;
	                var hasValue=hasWhereValue(elements,tmpDimId);
	                if(!hasValue)
	                   chkBoxs[i].disabled="";
	              }
              }
              chkBoxs=parent.document.getElementsByName("dim_where");
              for(var i=0;i<chkBoxs.length;i++){
                if(chkBoxs[i].id=="dim_where_"+dimId)
                 chkBoxs[i].disabled="";
              }
            }
          }
          convertWhereHidden(parentNode,elements,dimId);
        }
      }
      convertMsuHidden(parentNode,elements);
    }
  }
  function deleteGroupHidden(parentNode,elements){
    if(parentNode && elements){
      //判断是否存在
      var removeChildren=[];
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP%>"
            || element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>"
            || element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>"
            || element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>"
            || element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>"){
          removeChildren.push(element);
        }
      }
      for(var i=0;i<removeChildren.length;i++){
        parentNode.removeChild(removeChildren[i]);
      }
    }
  }
  function addGroupHidden(parentNode,dimId,level){
    if(parentNode){
       var element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP%>";
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP%>";
       element.value=dimId;
       parentNode.appendChild(element);

       element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>";
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPLEVEL%>";
       element.value=level;
       parentNode.appendChild(element);

       element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>";
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>";
       element.value="true";
       parentNode.appendChild(element);

       element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>";
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>";
       element.value="true";
       parentNode.appendChild(element);
    }
  }
  function addGroupHiddenValue(parentNode,dimId,level,value){
    if(parentNode){
       var element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>";
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>";
       element.value=value;
       parentNode.appendChild(element);
    }
  }
  function deleteWhereHidden(parentNode,elements,dimId){
    if(parentNode && elements){
      var removeChildren=[];
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>"
            && element.value==dimId){
          removeChildren.push(element);
        }
        if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId)){
          removeChildren.push(element);
        }
        if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId)){
           removeChildren.push(element);
        }
        if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId)){
           removeChildren.push(element);
        }
        if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId)){
           removeChildren.push(element);
        }
      }
      for(var i=0;i<removeChildren.length;i++){
        parentNode.removeChild(removeChildren[i]);
      }
    }
  }
  function addWhereHidden(parentNode,dimId,level){
    if(parentNode){
       var element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>";
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>";
       element.value=dimId;
       parentNode.appendChild(element);

       element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId;
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERELEVEL%>"+dimId;
       element.value=level;
       parentNode.appendChild(element);

       element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId;
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId;
       element.value="true";
       parentNode.appendChild(element);

       element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId;
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId;
       element.value="true";
       parentNode.appendChild(element);
    }
  }
  function addWhereHiddenValue(parentNode,dimId,level,value){
    if(parentNode){
       var element=parent.document.createElement("input");
       element.type="hidden";
       element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId;
       element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId;
       element.value=value;
       parentNode.appendChild(element);
    }
  }

 function isSelectAllForGroup(elements){
  var flag=false;
  if(elements){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>"
        && element.value=="true"){
        flag=true;
        break;
      }
    }
  }
  return flag;
 }
 function  setGroupInclude(elements,include){
  if(elements){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>"){
        element.value=include;
      }
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>"){
       element.value=false;
      }
    }
  }
 }
 function deleteGroupHiddenValue(parentNode,elements,dimId,value){
  if(parentNode && elements){
    var removeChildren=[];
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>"
          && element.value==value){
        removeChildren.push(element);
      }
    }
    for(var i=0;i<removeChildren.length;i++){
       parentNode.removeChild(removeChildren[i]);
    }
  }
 }
 function isSelectAllForWhere(elements,dimId){
  var flag=false;
  if(elements){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId)
        && element.value=="true"){
        flag=true;
        break;
      }
    }
  }
  return flag;
 }
 function setWhereInclude(elements,dimId,include){
  if(elements && dimId){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId)){
        element.value=include;
      }
      if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId)){
       element.value=false;
      }
    }
  }
 }
 function deleteWhereHiddenValue(parentNode,elements,dimId,value){
  if(parentNode && elements){
    var removeChildren=[];
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId)
          && element.value==value){
        removeChildren.push(element);
      }
    }
    for(var i=0;i<removeChildren.length;i++){
       parentNode.removeChild(removeChildren[i]);
    }
  }
 }
 function hasOtherGroupValue(elements,value){
  var has=false;
  if(elements && value){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>"
          && element.value!=value){
        has=true;
        break;
      }
    }
  }
  return has;
 }
 function hasOtherWhereValue(elements,dimId,value){
  var has=false;
  if(elements && dimId && value){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId)
        && element.value!=value){
        has=true;
        break;
      }
    }
  }
  return has;
 }
 function isOnlyOneGroupExclude(elements,value){
  var isOnlyOne=false;
  if(elements){
    var hasExclude=false;
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>"
          && element.value=="false"){
        hasExclude=true;
        break;
      }
    }
    if(hasExclude){
      isOnlyOne=true;
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>"
            && element.value!=value){
          isOnlyOne=false;
          break;
        }
      }
    }
  }
  return isOnlyOne;
 }
 function setGroupAllValues(elements,flag){
  if(elements && flag){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPALL%>"){
        element.value=flag;
        break;
      }
    }
  }
 }
 function isOnlyOneWhereExclude(elements,dimId,value){
  var isOnlyOne=false;
  if(elements && dimId && value){
    var hasExclude=false;
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId)
          && element.value=="false"){
        hasExclude=true;
        break;
      }
    }
    if(hasExclude){
      isOnlyOne=true;
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId)
            && element.value!=value){
          isOnlyOne=false;
          break;
        }
      }
    }
  }
  return isOnlyOne;
 }
 function setWhereAllValues(elements,dimId,flag){
  if(elements && dimId && flag){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name==("<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREALL%>"+dimId)){
        element.value=flag;
        break;
      }
    }
  }
 }
 function deleteGroupAllValues(parentNode,elements){
  if(parentNode && elements){
    var removeChildren=[];
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>")
        removeChildren.push(element);
    }
    for(var i=0;i<removeChildren.length;i++){
      parentNode.removeChild(removeChildren[i]);
    }
  }
 }
 function hasWhereValue(elements,dimId){
  var has=false;
  if(elements && dimId){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>"
          && element.value==dimId){
        has=true;
        break;
      }
    }
  }
  return has;
 }
 function hasGroupHiddenAdded(elements,dimId){
  var added=false;
  if(elements && dimId){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP%>"
          && element.value==dimId){
        added=true;
        break;
      }
    }
  }
  return added;
 }
 function hasWhereHiddenAdded(elements,dimId){
  var added=false;
  if(elements && dimId){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHERE%>"
          && element.value==dimId){
        added=true;
        break;
      }
    }
  }
  return added;
 }
 function getGroupIncludeValue(elements){
  var include=true;
  if(elements){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>"){
        include=(element.value=="true"?true:false);
        break;
      }
    }
  }
  return include;
 }
 function getWhereIncludeValue(elements,dimId){
  var include=true;
  if(elements && dimId){
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId){
        include=(element.value=="true"?true:false);
        break;
      }
    }
  }
  return include;
 }
 //如果当前隐藏的数目已经大于总数的一半，则设置另一半
 function convertGroupHidden(parentNode,elements){
  if(parentNode && elements){
    var totalSize=dimValuesSize;
    var realCount=0;
    var includeEle;
    var children=[];
    var alreadyValues="";
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPINCLUDE%>")
        includeEle=element;
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>"){
        realCount++;
        children.push(element);
        alreadyValues +=element.value+"::";
      }
    }
    //超过一半了
    if(realCount>(totalSize/2)){
      //设置include反向
      includeEle.value=(includeEle.value=="true"?"false":"true");
      //删除原先节
      for(var i=0;i<children.length;i++){
        //这里怎么不删除了
        parentNode.removeChild(children[i]);
      }
      //将剩下的节点加上,怎么找到剩下的呢
      for(var i=0;i<dimAllValues.length;i++){
        var dimValue=dimAllValues[i];
        if(alreadyValues.indexOf((dimValue+"::"))<0){
          //没有，加上
           var element=parent.document.createElement("input");
           element.type="hidden";
           element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>";
           element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUPVALUE%>";
           element.value=dimValue;
           parentNode.appendChild(element);
        }
      }
    }
    //一个也没有了
    if(realCount==0 && (!includeEle || includeEle.value=="false")){
      deleteGroupHidden(parentNode,elements);
    }
  }
 }
 //如果当前隐藏的数目已经大于总数的一半，则设置另一半
 function convertWhereHidden(parentNode,elements,dimId){
  if(parentNode && elements && dimId){
    var totalSize=dimValuesSize;
    var realCount=0;
    var includeEle;
    var children=[];
    var alreadyValues="";
    for(var i=0;i<elements.length;i++){
      var element=elements[i];
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREINCLUDE%>"+dimId)
        includeEle=element;
      if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId){
        realCount++;
        children.push(element);
        alreadyValues +=element.value+"::";
      }
    }
    //超过一半了
    if(realCount>(totalSize/2)){
      //设置include反向
      includeEle.value=(includeEle.value=="true"?"false":"true");
      //删除原先节
      for(var i=0;i<children.length;i++){
        //这里怎么不删除了
        parentNode.removeChild(children[i]);
      }
      //将剩下的节点加上,怎么找到剩下的呢
      for(var i=0;i<dimAllValues.length;i++){
        var dimValue=dimAllValues[i];
        if(alreadyValues.indexOf((dimValue+"::"))<0){
          //没有，加上
           var element=parent.document.createElement("input");
           element.type="hidden";
           element.name="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId;
           element.id="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMWHEREVALUE%>"+dimId;
           element.value=dimValue;
           parentNode.appendChild(element);
        }
      }
    }
    //一个也没有了
    if(realCount==0 && (!includeEle || includeEle.value=="false")){
      deleteWhereHidden(parentNode,elements,dimId);
    }
  }
 }
 //当图形类型是饼图，且没有分组时允许用户选择其他指标进行
 function convertMsuHidden(parentNode,elements){
  var chartType="<%=chartType%>";
  if(parentNode && elements){
    if(chartType=="<%=RptOlapConsts.OLAP_FUN_PIE%>"){
      //是饼图
      //判断是否没有分组维度了
      var hasGroupDim=false;
      var selectedMsus=[];
      for(var i=0;i<elements.length;i++){
        var element=elements[i];
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_DIMGROUP%>"){
          hasGroupDim=true;
        }
        if(element.name=="<%=RptOlapConsts.REQ_CHART_HIDDEN_MSUGROUP%>"){
          selectedMsus.push(element);
        }
      }
      if(!hasGroupDim){
        //没有分组，先删除以前的，然后加上新的
        //先找到所有的值，
        var divMsu=parent.document.getElementById("divMsuGroup");
        if(divMsu){
          divMsu.innerHTML=msuChkHTML;
        }
      }else{
        //恢复单选按钮
        var divMsu=parent.document.getElementById("divMsuGroup");
        if(divMsu){
          divMsu.innerHTML=msuRdoHTML;
        }
        //还有删除
      }
      //此处要根据选择状态设置选中


      if(hasGroupDim){
        //有分组
        var first=true;
        for(var i=0;i<selectedMsus.length;i++){
          if(first){
            var msuId=selectedMsus[i].value;
            var eMsu=parent.document.getElementById("msu_group_"+msuId);
            if(eMsu)
              eMsu.checked="true";
            first=false;
          }else{
            parentNode.removeChild(selectedMsus[i]);
          }
        }
      }else{
        for(var i=0;i<selectedMsus.length;i++){
          var msuId=selectedMsus[i].value;
          var eMsu=parent.document.getElementById("msu_group_"+msuId);
          if(eMsu)
            eMsu.checked="true";
        }
      }
    }
  }
 }
 function setParentBlocking(url){
	 parent.blockingLoaded=false;
	 var ifDim=parent.document.getElementById("chartDimValues");
	    if(ifDim && url){
	      ifDim.src=url;
	 }
 }
</script>
</body>
</html>
