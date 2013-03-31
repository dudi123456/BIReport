package com.ailk.bi.base.util;

import java.io.IOException;

import com.jspsmart.upload.*;

import javax.servlet.jsp.PageContext;
import javax.servlet.*;
import javax.servlet.http.*;

//import java.io.File;
/**
 * Title: class Description: 文档上传 Copyright: Copyright (c) 2000 Company: asiabi
 * tech
 * 
 * @author lds
 * @version 1.0
 */
public class UploadFile {
	SmartUpload mySmartUpload = new SmartUpload();
	int count = 0;
	String text = "", error = "";

	public UploadFile() {
	}

	/**
	 * 传递参数
	 * 
	 * @param PageContext
	 *            pageContext; ServletConfig config; HttpServletRequest request;
	 *            HttpServletResponse response
	 */

	public void setUpload(PageContext pageContext, ServletConfig config,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			mySmartUpload.initialize(pageContext);
			try {
				mySmartUpload.upload();
			} catch (SmartUploadException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		} catch (javax.servlet.ServletException se) {
		}
	}

	/**
	 * 得到要上传的文件数
	 * 
	 * @param
	 * @return int 要上传的文件数
	 */

	public int getFileCount() {
		int counts = mySmartUpload.getFiles().getCount();
		return counts;
	}

	/**
	 * 得到Request对象
	 * 
	 * @param
	 * @return Request对象
	 */
	public Request getRequest() {
		return mySmartUpload.getRequest();
	}

	/**
	 * 得到com.jspsmart.upload.File对象
	 * 
	 * @param int check 第几个文件
	 * @return com.jspsmart.upload.File
	 */
	public File getNextFile(int check) {
		com.jspsmart.upload.File file = mySmartUpload.getFiles().getFile(check);
		return file;
	}

	/**
	 * 得到文件大小
	 * 
	 * @param com
	 *            .jspsmart.upload.File
	 * @return int
	 */
	public int getFileSize(File file) {
		return file.getSize();
	}

	/**
	 * 得到文件后缀
	 * 
	 * @param com
	 *            .jspsmart.upload.File
	 * @return String
	 */
	public String getFileExt(File file) {
		return file.getFileExt();
	}

	/**
	 * 得到文件在客户端路径
	 * 
	 * @param com
	 *            .jspsmart.upload.File
	 * @return String
	 */
	public String getgetFilePathName(File file) {
		return file.getFilePathName();
	}

	/**
	 * 得到文件类型
	 * 
	 * @param com
	 *            .jspsmart.upload.File
	 * @return String
	 */
	public String getContentType(File file) {
		return file.getContentType();
	}

	/**
	 * 上传文档 参数： int check 第几个文件; String path 上传到服务器的路径； String fileName 保存时的文件名
	 * 返回值： boolean： true 表示上传成功； false表示上传不成功
	 */

	public boolean upload(int check, String path, String fileName) {
		boolean up = false;
		File file = mySmartUpload.getFiles().getFile(check);
		// Save it only if this file exists

		if (!file.isMissing() && file.getSize() > 0) {
			try {
				try {
					text = "save as";
					// Save the files with its original names in a virtual path
					// of the web server

					file.saveAs(path + fileName);

					// myFile.saveAs("/upload/" + myFile.getFileName(),
					// mySmartUpload.SAVE_VIRTUAL);
					// sample with a physical path
					// myFile.saveAs("c:\\temp\\" + myFile.getFileName(),
					// mySmartUpload.SAVE_PHYSICAL);

					// Display the properties of the current file
					/*
					 * System.out.println("FieldName = " + myFile.getFieldName()
					 * + "<BR>"); out.println("Size = " + myFile.getSize() +
					 * "<BR>"); out.println("FileName = " + myFile.getFileName()
					 * + "<BR>"); out.println("FileExt = " + myFile.getFileExt()
					 * + "<BR>"); out.println("FilePathName = " +
					 * myFile.getFilePathName() + "<BR>");
					 * out.println("ContentType = " + myFile.getContentType() +
					 * "<BR>"); out.println("ContentDisp = " +
					 * myFile.getContentDisp() + "<BR>");
					 * out.println("TypeMIME = " + myFile.getTypeMIME() +
					 * "<BR>"); out.println("SubTypeMIME = " +
					 * myFile.getSubTypeMIME() + "<BR>");
					 */
					up = true;

				} catch (com.jspsmart.upload.SmartUploadException se) {
					up = false;
					error = "smtuploadException";
				}
			} catch (java.io.IOException je) {
				up = false;
				error = "IOException";
			}
		} else {
			up = false;
			error = "no file";
		}
		return up;
	}
}