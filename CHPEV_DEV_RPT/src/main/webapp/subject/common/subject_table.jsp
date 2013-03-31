<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.ailk.bi.base.util.WebKeys"%>
<%@ include file="/base/commonHtml.jsp"%>
<%
	//没有登陆
	if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request,
			response)) {
		response.sendRedirect(context + "/index.jsp");
		return;
	}
%>
<%
	String tableId = (String) request
			.getAttribute(SubjectConst.REQ_TABLE_ID);
	String pageSize = (String) request
			.getAttribute(SubjectConst.REQ_TABLE_PAGESIZE);
	if (null == tableId) {
		pageSize = request.getParameter("page__iLinesPerPage");
	}
	boolean needAlignTable = true;
	boolean with_bar=false;
	String setParentHeight=(String)request.getAttribute("setParentHight");
	String withBar= (String)request.getAttribute("with_bar");
	if(SubjectConst.YES.equalsIgnoreCase(withBar))
		with_bar=true;
	//获取session值,
	if (null == tableId) {
		tableId = request.getParameter(SubjectConst.REQ_TABLE_ID);
		if (null == tableId) {
			response.sendRedirect(context + "/index.jsp");
			return;
		}
	}
	//取出表定义对象
	Object tmpObj = null;
	tmpObj = request.getAttribute("align_table");
	if (null != tmpObj
			&& SubjectConst.NO.equalsIgnoreCase((String) tmpObj)) {
		needAlignTable = false;
	}
	SubjectCommTabDef subTable = null;
	tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_TABLE_OBJ
			+ "_" + tableId);
	if (null != tmpObj)
		subTable = (SubjectCommTabDef) tmpObj;
	TableCurFunc curFunc = null;
	tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_FUNC_OBJ
			+ "_" + tableId);
	if (null != tmpObj) {
		curFunc = (TableCurFunc) tmpObj;
	}
%>
<%@page import="com.ailk.bi.subject.util.SubjectConst"%>
<%@page import="java.util.List"%>
<%@page import="com.ailk.bi.base.table.SubjectCommTabDef"%>
<%@page import="com.ailk.bi.subject.service.dao.ITableHeadHTMLDAO"%>
<%@page import="com.ailk.bi.subject.service.dao.impl.TableHeadHTMLDAO"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ailk.bi.pages.WebPageTool"%>
<%@page import="com.ailk.bi.subject.domain.TableRowStruct"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ailk.bi.subject.util.SubjectStringUtil"%>
<%@page import="com.ailk.bi.base.table.SubjectCommTabCol"%>
<%@page import="com.ailk.bi.pages.PagesInfoStruct"%>
<%@page import="com.ailk.bi.subject.domain.TableCurFunc"%>
<%@page import="com.ailk.bi.subject.service.dao.ITableRowStructDAO"%>
<%@page import="com.ailk.bi.subject.service.dao.impl.TableRowStructDAO"%>

<html>
<head>

<script src="js/jquery.min.js"></script>
<script src="js/jquery.bi.js"></script>
<script type="text/javascript">

    </script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Expires" CONTENT="0">
<meta http-equiv="Cache-Control" CONTENT="no-cache">
<meta http-equiv="Pragma" CONTENT="no-cache">
<title>统一经营分析系统</title>

<SCRIPT language=javascript src="<%=context%>/js/chart.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/net.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/js.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/align_tab_by_head.js"></SCRIPT>
<SCRIPT language=javascript src="<%=context%>/js/wait.js"
	charset="UTF-8"></SCRIPT>
<link rel="stylesheet" href="<%=context%>/css/other/css.css"
	type="text/css">
<link rel="stylesheet" href="<%=context%>/css/other/tab_css.css"
	type="text/css">
<script type="text/javascript">
	var scrollTop;
	var IsPage = -1;
	
	$(document).ready(function(){
		<%if(!SubjectConst.NO.equalsIgnoreCase(setParentHeight)){%>
			setParentHeight();
		<%}%>
	});
	$(window).resize(function(){
		<%if(!SubjectConst.NO.equalsIgnoreCase(setParentHeight)){%>
			setParentHeight();
		<%}%>
	});
	function setParentHeight(){
		if(!document.getElementById("iTable_TableContainer")){
			//此时设置一下父亲的高度
			var parentFrm=window.frameElement;
			if(parentFrm){
				var realHeight=$(parent.window).height()-$(parentFrm).offset().top;
				$(document.body).height(realHeight);
				$("#tableContent").height(realHeight);
				$(parentFrm).height(realHeight);
			}
		}
	}
	function childFunc(){
		alignTable();
	}
 	function loadNewContent(link){
	    var rpcUrl=link;
	    if(!rpcUrl){
	      alert("您没有提供访问地址！");
	      return;
	    }
	    rpcUrl=encodeURI(rpcUrl);
	    var params=[];
	    var pos=rpcUrl.indexOf("?");
		IsPage = rpcUrl.indexOf("ajax_request");
	    if(pos>=0){
	      var param=rpcUrl.substring(pos+1);
	      rpcUrl=rpcUrl.substring(0,pos);
	      params=param.split("&");
	    }
	    //记录下当前的滚动条位置
	    var domTableContent=document.getElementById("iTable_ContentTable1");
	    if(domTableContent)
	    	scrollTop=domTableContent.parentNode.scrollTop;

	    //if(IsPage<0)
			//rpcUrl = "SubjectCommonPageOrder.rptdo";

	    var ajaxHelper=new net.ContentLoader(rpcUrl,params,loadTableUpdate,ajaxError);
	    ajaxHelper.sendRequest();
	    ShowWait();
 	}
 	 //加载返回的新的值
	function loadTableUpdate(){
		closeWaitWin();
	    var jsonTxt=this.req.responseText;
	    if(jsonTxt){
	      //显示 alert(jsonTxt)
	      var tableContent=document.getElementById("tableContent");
	      if(tableContent){
	        tableContent.innerHTML=jsonTxt;
	        alignTable();
	        //设置滚动条位置为原来
	        var domTableContent=document.getElementById("iTable_ContentTable1");
	    	if(domTableContent)
	        	domTableContent.parentNode.scrollTop=scrollTop;
	      }
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
      closeWaitWin();
  }
  	function linkProcess(url,target){
  		var tar=target;
  		if(!tar)
  			tar="_self";
  		if(url){
  			if(tar=="_blank")
  				window.open(url,"产品定义","scrollbars=yes, toolbar=no, menubar=no, resizable=yes,location=no, status=no");
  			else if (tar=="parent")
  				parent.location=url;
  			else
  				window.location.href=url;
  		}
  	}
  	var before_row_id;
	var before_row_class;
	function changeTRBgColor(row_id){
		if(null==before_row_id){
			//记住当前的
			before_row_id=row_id;
			before_row_class=document.getElementById("L_"+row_id).className;
			document.getElementById("L_"+row_id).className="table-tr-on";
			document.getElementById("R_"+row_id).className="table-tr-on";
		}else{
			//先恢复，再记住当前的
			var tmpObj=document.getElementById("L_"+before_row_id);
			if(tmpObj){
				tmpObj.className=before_row_class;
			}
			tmpObj=document.getElementById("R_"+before_row_id);
			if(tmpObj){
				tmpObj.className=before_row_class;
			}
			before_row_id=row_id;
			before_row_class=document.getElementById("L_"+row_id).className;
			document.getElementById("L_"+row_id).className="table-tr-on";
			document.getElementById("R_"+row_id).className="table-tr-on";
		}
		window.status="完毕";
	}

	function resizeTable(){
		alignTable();
	}
</script>
<%=WebPageTool.pageScript("frmTableContent","SubjectCommTablePaging.rptdo")%>
</head>
<body class="main-body"
	onLoad="<%=(needAlignTable ? "alignTable();" : "")%>"
	onResize="<%=(needAlignTable ? "alignTable();" : "")%>">
	<span id="tableContent"> <%
 	if (null != subTable
 			&& SubjectConst.NO.equalsIgnoreCase(subTable.has_expand)
 			&& SubjectConst.YES.equalsIgnoreCase(subTable.has_paging)) {
 		//有分页功能这里，这里重新组织
 		//生成
 		String[][] svces = null;
 		tmpObj = session
 				.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_BODY_SVCES
 						+ "_" + tableId);
 		if (null != tmpObj) {
 			svces = (String[][]) tmpObj;
 		}
 		if (subTable.totalRows >0) {
 			out.println("<form name=\"frmTableContent\" target=\"_self\">");
 			out.println("<input type=\"hidden\" name=\"table_id\" value=\""
 					+ tableId + "\"/>");
 			out.println("<input type=\"hidden\" name=\"setParentHight\" value=\""
 					+ setParentHeight + "\"/>");
 			out.println("<input type=\"hidden\" name=\"with_bar\" value=\""
 					+ withBar + "\"/>");
 			out.println("<input type=\"hidden\" name=\"align_table\" value=\""
 					+ (needAlignTable?SubjectConst.YES:SubjectConst.NO) + "\"/>");
 			//获取翻页相关信息
 			PagesInfoStruct pageInfo = null;
 			pageInfo = WebPageTool.getPageInfo(request,
 					SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby)?subTable.totalRows - 1:subTable.totalRows, Integer.parseInt(pageSize));
 			out.println(WebPageTool.pageHidden(pageInfo));
 			
 			//输出导航条
 			//out.println(WebPageTool.pagePolit(pageInfo));
 			//开始组装表头
 			out.println("<table width='100%' border='0' cellpadding='0' cellspacing='0' "
 					+ ">\n");
 			out.println("<tr><td class=\"side-left\">");
 			out.println("<table width='100%' border='0' height=\""
 					+ curFunc.tableHeight + "\" "
 					+ "cellpadding='0' cellspacing='0' "
 					+ "id=\"iTable_TableContainer\">\n");
 			ITableHeadHTMLDAO tableHeadDao = new TableHeadHTMLDAO();
 			List head = tableHeadDao.getTableHead(subTable, curFunc,
 					svces);
 			if (null != head) {
 				Iterator iter = head.iterator();
 				while (iter.hasNext()) {
 					out.println(iter.next());
 				}
 			}
 			List body = new ArrayList();
 			List left = new ArrayList();
 			List right = new ArrayList();
 			ITableRowStructDAO tableRowDAO = new TableRowStructDAO();
 			for (int i = 0; i < pageInfo.iLinesPerPage
 					&& (i + pageInfo.absRowNoCurPage()) < pageInfo.iLines; i++) {
 				int index = i;
 				TableRowStruct row = tableRowDAO.genTableRowStruct(
 						subTable, curFunc, svces[index],
 						SubjectConst.NO.equalsIgnoreCase(subTable.no_groupby)?svces[svces.length - 1]:null, false);
 				left.add(row.leftHTML.toString());
 				right.add(row.rightHTML.toString());
 			}

 			body.add(SubjectStringUtil.genFixedBodyLeftPart1());
 			body.addAll(left);
 			// 在这要加上一行多余的行,为了对齐高度
 			body.add("<tr >");
 			List tabCols = subTable.tableCols;
 			Iterator iter = tabCols.iterator();
 			while (iter.hasNext()) {
 				SubjectCommTabCol tabCol = (SubjectCommTabCol) iter
 						.next();
 				if (SubjectConst.YES
 						.equalsIgnoreCase(tabCol.default_display)
 						&& SubjectConst.NO
 								.equalsIgnoreCase(tabCol.is_measure))
 					body.add("<td class=\"table-td-withbg\" >&nbsp;</td>");
 			}
 			body.add("</tr>");
 			body.add(SubjectStringUtil.genFixedBodyLeftPart2());
 			body.add(SubjectStringUtil.genFixedBodyRightPart1());
 			body.addAll(right);
 			body.add(SubjectStringUtil.genFixedBodyRightPart2());
 			//开始输出
 			boolean isOdd = true;
 			String trStyle = curFunc.tabEvenTRClass;
 			String tdStyle = curFunc.tabTDEvenClass;
 			iter = body.iterator();
 			while (iter.hasNext()) {
 				String temp = (String) iter.next();
 				if (temp.indexOf(SubjectConst.TR_CLASS_REPLACE) >= 0) {
 					isOdd = !isOdd;
 				} else {
 					isOdd = true;
 				}
 				if (isOdd) {
 					trStyle = curFunc.tabOddTRClass;
 					tdStyle = curFunc.tabTDOddClass;
 				} else {
 					trStyle = curFunc.tabEvenTRClass;
 					tdStyle = curFunc.tabTDEvenClass;
 				}
 				temp = temp.replaceAll(SubjectConst.TR_CLASS_REPLACE,
 						trStyle);
 				temp = temp.replaceAll(SubjectConst.TD_CLASS_REPLACE,
 						tdStyle);
 				out.println(temp);
 			}
 			out.println("</table>\n</td></tr><tr><td class='page_bg'>");
 			out.println(WebPageTool.pagePolit(pageInfo));
 			out.println("</td></tr></table>");
 			out.println("</form>");
 		} else {
 			out.println("<table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0' "
 					+ ">\n");
 			out.println("<tr><td align=\"center\" height='100%' class=\"searchnobg\" valign=\"middle\"><div class=\"searchno\">");
 			out.println("没有查询到相关信息");
 			out.println("</div></td></tr>");
 			out.println("</table>\n");
 		}
 	} else {
 		//取出session 中的值
 		tmpObj = session.getAttribute(WebKeys.ATTR_SUBJECT_COMMON_HTML
 				+ "_" + tableId);
 		if (null != tmpObj) {
 			boolean isOdd = true;
 			String trStyle = curFunc.tabEvenTRClass;
 			String tdStyle = curFunc.tabTDEvenClass;
 			String[] html = (String[]) tmpObj;
 			for (int i = 0; i < html.length; i++) {
 				// 	 这里替换样式
 				if (html[i].indexOf(SubjectConst.TR_CLASS_REPLACE) >= 0) {
 					isOdd = !isOdd;
 				} else {
 					isOdd = true;
 				}
 				if (isOdd) {
 					trStyle = curFunc.tabOddTRClass;
 					tdStyle = curFunc.tabTDOddClass;
 				} else {
 					trStyle = curFunc.tabEvenTRClass;
 					tdStyle = curFunc.tabTDEvenClass;
 				}
 				String temp = html[i].replaceAll(
 						SubjectConst.TR_CLASS_REPLACE, trStyle);
 				temp = temp.replaceAll(SubjectConst.TD_CLASS_REPLACE,
 						tdStyle);
 				out.println(temp);
 			}
 		}
 	}
 %>

	</span>

</body>
</html>
<script type="text/javascript">
	function getScrollBarWidth (){
		  document.body.style.overflow = 'hidden';
		  var width = document.body.clientWidth;
		  document.body.style.overflow = 'scroll';
		  width -= document.body.clientWidth;
		  if(!width) width = document.body.offsetWidth - document.body.clientWidth;
		  document.body.style.overflow = '';
		  return width;
	}
    function getScrollerWidth() {
        var scr = null;
        var inn = null;
        var wNoScroll = 0;
        var wScroll = 0;

        // Outer scrolling div
        scr = document.createElement('div');
        scr.style.position = 'absolute';
        scr.style.top = '-1000px';
        scr.style.left = '-1000px';
        scr.style.width = '100px';
        scr.style.height = '50px';
        // Start with no scrollbar
        scr.style.overflow = 'hidden';

        // Inner content div
        inn = document.createElement('div');
        inn.style.width = '100%';
        inn.style.height = '200px';

        // Put the inner div in the scrolling div
        scr.appendChild(inn);
        // Append the scrolling div to the doc

        document.body.appendChild(scr);

        // Width of the inner div sans scrollbar
        wNoScroll = inn.offsetWidth;
        // Add the scrollbar
        scr.style.overflow = 'auto';
        // Width of the inner div width scrollbar
        wScroll = inn.offsetWidth;

        // Remove the scrolling div from the doc
        document.body.removeChild(
        document.body.lastChild);

        // Pixel width of the scroller
        return (wNoScroll - wScroll);
    }


	var scrollBarWidth;
	if(!scrollBarWidth){
		scrollBarWidth = getScrollBarWidth();
		if(scrollBarWidth ==0){
			scrollBarWidth = getScrollerWidth();
		}
		if(scrollBarWidth ==0){
			scrollBarWidth = 17;
		}
	}

</script>