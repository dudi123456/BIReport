package com.ailk.bi.adhoc.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

@SuppressWarnings({ "rawtypes" })
public class ZipFileUtil {

	private String zipName = "FIN_REPORT.zip";
	private List listFile; // 存的文件列表名称
	private String rootPath = "";// 文件存放位置

	public String doZipFile() {
		String strRetn = rootPath + "/" + zipName;
		strRetn = strRetn.replace(File.separatorChar, '/');

		if (FileOperator.fileExist(strRetn)) {
			FileOperator.delFile(strRetn);
		}
		FileOutputStream fileOutputStream = null;
		CheckedOutputStream cs = null;
		ZipOutputStream out = null;

		try {

			fileOutputStream = new FileOutputStream(rootPath + "/" + zipName);
			// 使用输出流检查
			cs = new CheckedOutputStream(fileOutputStream, new CRC32());
			// 声明输出zip流
			out = new ZipOutputStream(new BufferedOutputStream(cs));
			byte b[] = new byte[512];

			for (int m = 0; m < listFile.size(); m++) {

				String fileName = (String) listFile.get(m);
				String fileTmpPath = rootPath + File.separatorChar + fileName;
				fileTmpPath = fileTmpPath.replace(File.separatorChar, '/');
				// System.out.println("fileTmpPath:" + fileTmpPath);
				if (FileOperator.fileExist(fileTmpPath)) {
					InputStream in = new FileInputStream(fileTmpPath);
					ZipEntry e = new ZipEntry(fileName);
					out.putNextEntry(e);
					int len = 0;
					while ((len = in.read(b)) != -1) {
						out.write(b, 0, len);
					}
					out.closeEntry();
					in.close();

				}
			}
			out.close();
			cs.close();
			fileOutputStream.close();

			// System.out.println("build zip file:" + strRetn);
			return strRetn;
		} catch (Exception ex) {
			return null;
		} finally {
			try {
				out.close();
				cs.close();
				fileOutputStream.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	public String getZipName() {
		return zipName;
	}

	public void setZipName(String zipName) {
		this.zipName = zipName;
	}

	public List getListFile() {
		return listFile;
	}

	public void setListFile(List listFile) {
		this.listFile = listFile;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
}
