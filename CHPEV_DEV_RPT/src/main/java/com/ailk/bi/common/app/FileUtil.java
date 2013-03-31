package com.ailk.bi.common.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 文件上传bean
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author wiseking update it at 20040910
 * @version 1.0
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class FileUtil {
	/**
	 * 给定文件名和路径 删除文件
	 */
	public static boolean deleteFile(String dir, String FileName) {
		File del = new File(dir + FileName);
		return del.delete();
	}

	/**
	 * 读取文件并将内容以byte[]形势返回
	 * 
	 * @param fileName
	 * @return
	 * @throws AppException
	 */
	public static byte[] readFile(String fileName) throws AppException {
		File file = new File(fileName);
		byte[] content = null;
		if (file.exists() && file.isFile()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				byte b[] = new byte[1024];
				byte[] pOld = null;
				int iTotal = 0;
				for (;;) {
					int iLen = fis.read(b);
					if (iLen == -1)
						break;
					if (content == null) {
						content = new byte[iLen];
					} else {
						pOld = content;
						content = new byte[iTotal + iLen];
					}
					if (pOld != null) {
						for (int i = 0; i < pOld.length; i++) {
							content[i] = pOld[i];
						}
					}
					for (int i = 0; i < iLen; i++) {
						content[i + iTotal] = b[i];
					}
					iTotal += iLen;
				}
				fis.close();
			} catch (Exception e) {
				throw new AppException("读取文件错误:[" + e.toString() + "]");
			}
		}
		return content;
	}

	/**
	 * @param fileInfo
	 *            文件信息
	 * @param dir
	 *            目录名
	 * @param fileName
	 *            文件名
	 * @return true写成功
	 */
	public static boolean saveFile(byte[] content, String dir, String fileName) {
		try {
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				// Directory doesn't exist, create it
				fileDir.mkdirs();
			}

			File file = new File(dir, fileName);

			FileOutputStream fileoutputstream = new FileOutputStream(file);
			fileoutputstream.write(content);
			fileoutputstream.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 以行的方式读取文本类型的文件
	 * 
	 * @param fileName
	 *            要读取的文件的名称
	 * @return 将文本中的每一行存放在Vector单元中，如果读取过程中出现错误则返回null
	 */
	public static Vector readFileByLine(String fileName) {
		Vector retV = new Vector();
		try {
			FileInputStream inStream = null;
			inStream = new FileInputStream(fileName);
			BufferedReader d = new BufferedReader(new InputStreamReader(
					inStream));

			String aLine = null;
			for (;;) {
				aLine = d.readLine();
				if (aLine == null)
					break;
				aLine = aLine.trim();
				retV.add(aLine);
			}
			d.close();
			inStream.close();
			return retV;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 处理从浏览器上载的文件 必须是这样的格式 enctype="multipart/form-data"|"multipart/mixed"
	 * method="post"> ...<input type="..."> ...<input type="file"
	 * name="xx">[可以支持多个file同时上传，但是name不能重复] </form>
	 * 
	 * @param req
	 * @return
	 * @throws java.lang.Exception
	 */
	public static UpFileInfo uploadFile(HttpServletRequest req)
			throws AppException {

		List lists = null;

		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			lists = upload.parseRequest(req);
		} catch (FileUploadException e) {
			throw new AppException(
					"读取上传前台页面错误!form format must is [form action=\"XXX\" enctype=\"multipart/form-data\"|\"multipart/mixed\" method=\"post\"]");
		}

		Iterator it = lists.iterator();
		byte input[];
		int iFileCount = 0;
		UpFileInfo retFileInfo = new UpFileInfo();
		while (it.hasNext()) {
			FileItem fi = (FileItem) it.next();
			if (!(fi.isFormField())) {
				if (iFileCount == 0)
					retFileInfo.firstFileName = fi.getFieldName();
				iFileCount++;
				UpFileInfo sbFileInfo = new UpFileInfo();
				sbFileInfo.setUpFileName(fi.getName());
				input = fi.get();
				sbFileInfo.setContent(input);
				retFileInfo.putFileInfo(fi.getFieldName(), sbFileInfo);
			} else {
				HashMap hm = retFileInfo.getUpParameters();
				String fieldName = fi.getFieldName();
				if (hm.containsKey(fieldName)) {
					Vector v = (Vector) hm.get(fieldName);
					v.add(fi.getString());
				} else {
					Vector v = new Vector();
					v.add(fi.getString());
					hm.put(fieldName, v);
				}
			}
		}
		return retFileInfo;
	}

	/**
	 * 判断一个文件在web服务器上是否存在
	 * 
	 * @param application
	 * @param pathName
	 * @return
	 */
	private static boolean isVirtual(String pathFileName) {
		File virtualFile = new File(pathFileName);
		return virtualFile.exists();

	}

	/**
	 * 从带路径的串中取得文件名称
	 * 
	 * @param filePathName
	 * @return
	 */
	private static String getFileName(String filePathName) {
		int pos = 0;
		pos = filePathName.lastIndexOf("/");
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		pos = filePathName.lastIndexOf("\\");
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		else
			return filePathName;
	}

	/**
	 * 从Web上下载文件
	 * 
	 * @param resp
	 *            HttpServletResponse
	 * @param sourceFilePathName
	 *            下载文件带路径全名
	 * @param destFileName
	 *            下载另存文件名
	 * @throws AppException
	 */
	public static void downloadFile(HttpServletResponse resp,
			String sourceFilePathName, String destFileName) throws AppException {
		boolean denyPhysicalPath = false;
		boolean isV;
		String contentDisposition = null;
		String contentType = null;
		int blockSize = 65000;
		if (sourceFilePathName == null)
			throw new AppException("download filename is null!");
		if (sourceFilePathName.equals(""))
			throw new AppException("download filename is null!");
		isV = isVirtual(sourceFilePathName);
		if (!isV && denyPhysicalPath)
			throw new AppException("Physical path is denied (1035).");

		try {
			File file = new File(sourceFilePathName);
			FileInputStream fileIn = new FileInputStream(file);
			long fileLen = file.length();
			int readBytes = 0;
			int totalRead = 0;
			byte b[] = new byte[blockSize];
			if (contentType == null)
				resp.setContentType("application/x-download");
			else if (contentType.length() == 0)
				resp.setContentType("application/x-download");
			else
				resp.setContentType(contentType);
			resp.setContentLength((int) fileLen);
			contentDisposition = contentDisposition != null ? contentDisposition
					: "attachment;";
			if (destFileName == null)
				destFileName = getFileName(sourceFilePathName);
			destFileName = new String(destFileName.getBytes(), "iso-8859-1");

			if (destFileName.length() == 0)
				resp.setHeader("Content-Disposition", contentDisposition);
			else {
				resp.setHeader("Content-Disposition", contentDisposition
						+ " filename=" + destFileName);
			}

			while ((long) totalRead < fileLen) {
				readBytes = fileIn.read(b, 0, blockSize);
				totalRead += readBytes;
				resp.getOutputStream().write(b, 0, readBytes);
			}
			resp.getOutputStream().flush();
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("文件下载错误![" + e.getMessage() + "]");
		}
	}

	/**
	 * getParameter (HttpServletRequest,String)
	 * form.enctype="MULTIPART/FORM-DATA"; 传入需要读取的表单项的名字，返回值为该表单项的值。 public
	 * static String getParameter(HttpServletRequest req, String item) throws
	 * Exception { try { //req.getCharacterEncoding() byte b[] = new byte[4096];
	 * int ai[] = new int[1]; //返回值 String resultpara = ""; String line;
	 * ServletInputStream sis = req.getInputStream(); while ( (line =
	 * readLine(b, ai, sis, CHARACTER_ENCODING)) != null) { if
	 * (line.indexOf("name=\"" + item + "\"") > 0) { while ( (line = readLine(b,
	 * ai, sis, CHARACTER_ENCODING)) != null) { if
	 * (line.indexOf(getContentType(req.getContentType())) < 0) { line =
	 * removeCarrige(line); resultpara = resultpara + line; } else break; }
	 * break; } } return resultpara; } catch (Exception e) {
	 * e.printStackTrace(); throw e; } }
	 */

	/**
	 * 从浏览器前台获取上传文件
	 * 
	 * @param req
	 * @param fileBoxName
	 *            上传文件的信息,格式为: <input type="file" name="file_box_name">
	 *            form.enctype="MULTIPART/FORM-DATA";
	 * @return 文件内容
	 * @throws java.lang.Exception
	 *             public static UpFileInfo uploadFile(HttpServletRequest req,
	 *             String file_box_name) throws Exception { long iFileLen = 0;
	 *             String filename = null; byte b[] = new byte[4096]; int ai[] =
	 *             new int[1]; Vector retV = new Vector(); //返回值 String line;
	 *             try { //得到文件名 ServletInputStream sis = req.getInputStream();
	 *             String contType = getContentType(req.getContentType()); int
	 *             aaa = 0; while ( (line = readLine(b, ai, sis,
	 *             CHARACTER_ENCODING)) != null) {
	 *             System.out.println("line="+line); if
	 *             (line.indexOf("filename=\"") > 0) { filename = line; filename
	 *             = filename.substring(filename.indexOf("ame=\"" +
	 *             file_box_name + "\"; filename=\"") + file_box_name.length() +
	 *             18); filename = filename.substring(0,
	 *             filename.indexOf("\n")); filename =
	 *             filename.substring(filename.lastIndexOf("\\") + 1,
	 *             filename.indexOf("\"")); break; } }
	 *             //-----------------------------7d226137250336 if (filename !=
	 *             null && !filename.equals("")) { String sContentType =
	 *             readLine(b, ai, sis, CHARACTER_ENCODING); if
	 *             (sContentType.indexOf("Content-Type:") >= 0) readLine(b, ai,
	 *             sis, CHARACTER_ENCODING); while ( (sContentType = readLine(b,
	 *             ai, sis, CHARACTER_ENCODING)) != null) {
	 *             System.out.println("sContentType="+sContentType); if
	 *             (sContentType.indexOf(contType) == 0 && b[0] == 45 && (b[1]
	 *             == 45) && (b[2] == 45) && (b[3] == 45) && (b[4] == 45))
	 *             break; //存储读取的内容 retV.add(b); iFileLen += ai[0]; } } FileInfo
	 *             retFileInfo = new FileInfo(); retFileInfo.fileLen = iFileLen;
	 *             retFileInfo.upFileName = filename; retFileInfo.content =
	 *             retV; return retFileInfo; } catch (Exception e) {
	 *             e.printStackTrace(); throw e; } } private static String
	 *             readLine(byte lb[], int ai[], ServletInputStream sis, String
	 *             CharacterEncoding) { try { //Reads a line from the POST data.
	 *             ai[0] = sis.readLine(lb, 0, lb.length);
	 *             System.out.println("22lb="+(new String(lb,0,10)));
	 *             System.out.println("ai="+ai[0]); if (ai[0] == -1) return
	 *             null; } catch (IOException _ex) { _ex.printStackTrace();
	 *             return null; } try { if (CharacterEncoding == null) {
	 *             //用缺省的编码方式把给定的byte数组转换为字符串 return new String(lb, 0, ai[0]); }
	 *             else { //用给定的编码方式把给定的byte数组转换为字符串 return new String(lb, 0,
	 *             ai[0], CharacterEncoding); } } catch (Exception _ex) { return
	 *             null; } } private static String getContentType(String s) {
	 *             int j; if( s==null ) return "text/html; charset=gb2312"; if (
	 *             (j = s.indexOf("boundary=")) != -1) { s = s.substring(j + 9);
	 *             s = "--" + s; } return s; } private static String
	 *             removeCarrige(String source) { if (source == null) return
	 *             null; if (source.indexOf("\n") >= 0) { source =
	 *             source.substring(0, source.indexOf("\n")); } if
	 *             (source.indexOf("\r") >= 0) { source = source.substring(0,
	 *             source.indexOf("\r")); } return source; }
	 */
	public static void main(String[] args) {
		Vector v = FileUtil.readFileByLine("d:/test.txt");
		for (int i = 0; v != null && i < v.size(); i++) {
			System.out.println("" + v.elementAt(i));
		}

	}
}