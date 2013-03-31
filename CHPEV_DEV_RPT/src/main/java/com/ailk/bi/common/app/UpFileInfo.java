package com.ailk.bi.common.app;

import java.util.*;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UpFileInfo {
	public String firstFileName = null;

	private byte[] content = null; // 每个单元的内容是byte[]

	private String upFileName = null;

	private HashMap upFileInfo = new HashMap(); // 上载文件的内容,如果一次提交上传多个文件的话会存放在这里

	private HashMap upParameters = new HashMap(); // 单元内存放的是parameter数组Vector

	/**
	 * 取得同上传文件一起上来的页面输入信息
	 * 
	 * @param para
	 * @return
	 */
	public String getParameter(String para) {
		if (upParameters.containsKey(para)) {
			Vector strA = (Vector) upParameters.get(para);
			return (String) strA.elementAt(0);
		} else
			return null;
	}

	/**
	 * 取得同上传文件一起上来的页面输入数组信息
	 * 
	 * @param para
	 * @return
	 */
	public String[] getParameters(String para) {
		if (upParameters.containsKey(para)) {
			Vector strA = (Vector) upParameters.get(para);
			String[] retStr = new String[strA.size()];
			for (int i = 0; i < strA.size(); i++) {
				retStr[i] = (String) strA.elementAt(i);
			}
			return retStr;
		} else
			return null;
	}

	/**
	 * 上传文件的尺寸
	 * 
	 * @return
	 */
	public int getFileSize() {
		return getFileSize(this.firstFileName);
	}

	/**
	 * 上传指定name文件的尺寸
	 * 
	 * @return
	 */
	public int getFileSize(String fileFieldName) {
		UpFileInfo fileInfo = getFileInfo(fileFieldName);
		if (fileInfo == null)
			return 0;
		byte[] cont = fileInfo.getContent();
		if (cont == null)
			return 0;
		else
			return cont.length;
	}

	/**
	 * 上传文件的名字
	 * 
	 * @return
	 */
	public String getFullFileName() {
		return getFullFileName(firstFileName);
	}

	/**
	 * 上传文件的名字
	 * 
	 * @return
	 */
	public String getFullFileName(String fileFieldName) {
		UpFileInfo fileInfo = getFileInfo(fileFieldName);
		if (fileInfo == null)
			return null;
		String fullFileName = fileInfo.upFileName;
		return fullFileName;
	}

	/**
	 * 获取文件名称
	 * 
	 * @return
	 */
	public String getFileName() {
		return getFileName(firstFileName);
	}

	/**
	 * 获取指定上传name的上传文件名
	 * 
	 * @param fileFieldName
	 * @return
	 */
	public String getFileName(String fileFieldName) {
		UpFileInfo fileInfo = getFileInfo(fileFieldName);
		if (fileInfo == null)
			return null;
		String fileName = fileInfo.upFileName;
		if (fileName == null)
			return null;
		int iP = fileName.lastIndexOf("\\");
		if (iP < 0)
			return fileName;
		else {
			return fileName.substring(iP + 1);
		}
	}

	/**
	 * 获取上传文件的内容
	 * 
	 * @return
	 */
	public byte[] getContent() {
		return getContent(this.firstFileName);
	}

	/**
	 * 获取指定名称的上传文件的内容
	 * 
	 * @param fileFieldName
	 *            该name即页面上的<input type="file" name="XXXX">中的XXXX取值
	 * @return
	 */
	public byte[] getContent(String fileFieldName) {
		UpFileInfo fileInfo = getFileInfo(fileFieldName);
		if (fileInfo == null)
			return null;
		return fileInfo.content;
	}

	/**
	 * 以串的方式获取上传文件内容
	 * 
	 * @return
	 */
	public String getStrContent() {
		return getStrContent(this.firstFileName);
	}

	/**
	 * 以串的方式获取上传文件内容
	 * 
	 * @param fileFieldName
	 * @return
	 */
	public String getStrContent(String fileFieldName) {
		if (getContent(fileFieldName) == null)
			return null;
		String s = new String(getContent(fileFieldName));
		return s;
	}

	/**
	 * 对外用户禁用
	 * 
	 * @param content
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * 对外用户禁用
	 * 
	 * @param content
	 */
	public void setUpFileName(String upFileName) {
		this.upFileName = upFileName;
	}

	/**
	 * 对外用户禁用
	 * 
	 * @param content
	 */
	public HashMap getUpParameters() {
		return upParameters;
	}

	/**
	 * 对外用户禁用
	 * 
	 * @param content
	 */
	public void setUpParameters(HashMap upParameters) {
		this.upParameters = upParameters;
	}

	public void putFileInfo(String fileField, UpFileInfo fileInfo) {
		upFileInfo.put(fileField, fileInfo);
	}

	private UpFileInfo getFileInfo(String fileField) {
		if (fileField == null)
			return null;
		UpFileInfo ret = (UpFileInfo) upFileInfo.get(fileField);
		return ret;
	}
}