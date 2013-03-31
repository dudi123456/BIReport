<%@ page language="java" contentType="text/xml;charset=UTF-8" %>
<%@ page import="com.ailk.bi.base.util.XMLTranser,
                 com.ailk.bi.common.app.AppException,
                 com.ailk.bi.common.dbtools.WebDBUtil,
                 org.jdom.Document,
                 org.jdom.output.Format,
                 org.jdom.output.XMLOutputter,
                 java.io.PrintWriter,
                 java.util.Vector" %>
<%
	response.setCharacterEncoding("UTF-8");
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);

    String msu_type_id = request.getParameter("msu_type_id");
    StringBuffer sql = new StringBuffer();
    Vector result = null;
    try {
        sql.append("select msu_type_id,msu_type_name from UI_META_INFO_MEASURE_TYPE where parent_type_id='"+msu_type_id+"'");

        // 查询数据存入Vector或数组
        result = WebDBUtil.execQryVector(sql.toString(), "");
    } catch (AppException e) {
        e.printStackTrace();
    }
    if (result != null) {
        // 根据sql字符串，查询取得的数据生成源XML文档
        Document sourcedoc = XMLTranser.getDocument(sql.toString(), result);
        // 源xml文档结合指定xsl生成目标xml文档
        Document doc = XMLTranser.transWithXsl(sourcedoc, "createMsuTypeTreeXML.xsl");
        // 从服务器端返回目标文档
        XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
        PrintWriter pw = response.getWriter();
        outp.output(doc, pw);
    }
%>