<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="java.io.File"%>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />

<%
request.setCharacterEncoding("GBK");

String saveDirectory = request.getSession().getServletContext().getRealPath("/") + File.separatorChar + "adhoc";
String filename = saveDirectory + File.separatorChar + "zhibiaodesc.doc";

mySmartUpload.initialize(pageContext);
// mySmartUpload.initialize(request.getSession().getServletContext(), request, response);
  mySmartUpload.setContentDisposition(null);
  mySmartUpload.downloadFile(filename,null,"zhibiaodesc.doc");//.downloadFile(filePath, null, fileName);
/*
	//request.setCharacterEncoding("GBK");
	request.setCharacterEncoding("UTF-8");   
	response.setContentType("application/vnd.ms-word; charset=UTF-8");
	//response.setContentType("text/html;charset=UTF-8");
    //response.setContentType("application/msword");
	//response.setContentType("application/x-msdownload;");
    response.setHeader("Content-disposition","attachment; filename=zhibiaodesc.doc");

    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    try {
        bis = new BufferedInputStream(new FileInputStream(filename));
        bos = new BufferedOutputStream(response.getOutputStream());

        byte[] buff = new byte[2048];
        int bytesRead;

        while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff,0,bytesRead);
        }

    } catch(final IOException e) {
        System.out.println ( "出现IOException." + e );
    } finally {
        if (bis != null)
            bis.close();
        if (bos != null)
            bos.close();
    }
	*/
%>

