package com.ailk.bi.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.tools.zip.*;

/**
 * java 操作Zip处理类
 * 
 * @author chunming
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ZipUtil {

	/**
	 * zip压缩功能. 将d:\\temp\\zipout目录下的所有文件连同子目录压缩到d:\\temp\\out.zip.
	 * 
	 * @param baseDir
	 *            所要压缩的目录名（包含绝对路径）
	 * @param objFileName
	 *            压缩后的文件名
	 * @throws Exception
	 */
	public static void zip(String baseDir, String objFileName) throws Exception {
		// 源文件夹对象
		File folderObject = new File(baseDir);
		//
		if (folderObject.exists()) {
			// 文件夹下列表
			List fileList = getSubFiles(new File(baseDir));
			// 压缩文件流
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
					objFileName));
			//
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			int readLen = 0;
			for (int i = 0; i < fileList.size(); i++) {
				File f = (File) fileList.get(i);
				// 创建一个ZipEntry，并设置Name和其它的一些属性
				ze = new ZipEntry(getAbsFileName(baseDir, f));
				ze.setSize(f.length());
				ze.setTime(f.lastModified());

				// 将ZipEntry加到zos中，再写入实际的文件内容
				zos.putNextEntry(ze);
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					zos.write(buf, 0, readLen);
				}
				is.close();
				System.out.println("done...");
			}
			zos.close();
		} else {
			throw new Exception("this folder isnot exist,please check it!");
		}
	}

	/**
	 * zip压缩功能测试. 将指定文件压缩后存到一压缩文件中
	 * 
	 * @param baseDir
	 *            所要压缩的文件名
	 * @param objFileName
	 *            压缩后的文件名
	 * @return 压缩后文件的大小
	 * @throws Exception
	 */
	public static long createFileToZip(String sourceFileName, String zipFilename)
			throws Exception {
		// 源文件
		File sourceFile = new File(sourceFileName);
		byte[] buf = new byte[1024];
		// ZIP文件
		File objFile = new File(zipFilename);

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(objFile));
		ZipEntry ze = null;
		// 创建一个ZipEntry，并设置Name和其它的一些属性
		ze = new ZipEntry(sourceFile.getName());
		ze.setSize(sourceFile.length());
		ze.setTime(sourceFile.lastModified());

		// 将ZipEntry加到zos中，再写入实际的文件内容
		zos.putNextEntry(ze);

		InputStream is = new BufferedInputStream(
				new FileInputStream(sourceFile));

		int readLen = -1;
		while ((readLen = is.read(buf, 0, 1024)) != -1) {
			zos.write(buf, 0, readLen);
		}
		is.close();
		zos.close();

		return objFile.length();
	}

	/**
	 * zip压缩功能测试. 将指定文件压缩后存到一压缩文件中
	 * 
	 * @param baseDir
	 *            所要压缩的文件名
	 * @param objFileName
	 *            压缩后的文件名
	 * @return 压缩后文件的大小
	 * @throws Exception
	 */
	public static long createFileToZip(File sourceFile, File zipFile)
			throws IOException {

		byte[] buf = new byte[1024];

		// 压缩文件名
		File objFile = zipFile;

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(objFile));

		ZipEntry ze = null;
		// 创建一个ZipEntry，并设置Name和其它的一些属性
		ze = new ZipEntry(sourceFile.getName());
		ze.setSize(sourceFile.length());
		ze.setTime(sourceFile.lastModified());

		// 将ZipEntry加到zos中，再写入实际的文件内容
		zos.putNextEntry(ze);

		InputStream is = new BufferedInputStream(
				new FileInputStream(sourceFile));

		int readLen = -1;
		while ((readLen = is.read(buf, 0, 1024)) != -1) {
			zos.write(buf, 0, readLen);
		}
		is.close();
		zos.close();

		return objFile.length();
	}

	/**
	 * 测试解压缩功能. 将d:\\download\\test.zip连同子目录解压到d:\\temp\\zipout目录下.
	 * 
	 * @throws Exception
	 */
	public static void releaseZipToFile(String sourceZip, String outFileName)
			throws IOException {
		ZipFile zfile = new ZipFile(sourceZip);
		// System.out.println(zfile);
		Enumeration zList = zfile.getEntries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			// 从ZipFile中得到一个ZipEntry
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				continue;
			}
			// 以ZipEntry为参数得到一个InputStream，并写到OutputStream中
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(outFileName, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
			System.out.println("Extracted: " + ze.getName());
		}
		zfile.close();

	}

	/**
	 * 取得指定目录下的所有文件列表，包括子目录.
	 * 
	 * @param baseDir
	 *            File 指定的目录
	 * @return 包含java.io.File的List
	 */
	private static List getSubFiles(File baseDir) {
		List ret = new ArrayList();
		// File base=new File(baseDir);
		File[] tmp = baseDir.listFiles();
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].isFile()) {
				ret.add(tmp[i]);
			}
			if (tmp[i].isDirectory()) {
				ret.addAll(getSubFiles(tmp[i]));
			}
		}
		return ret;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	private static File getRealFileName(String baseDir, String absFileName) {

		String[] dirs = absFileName.split("/");
		// 基本路径
		File ret = new File(baseDir);
		// 文件长度>1
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				ret = new File(ret, dirs[i]);
			}
		}
		if (!ret.exists()) {
			ret.mkdirs();
		}
		ret = new File(ret, dirs[dirs.length - 1]);
		return ret;
	}

	/**
	 * 给定根目录，返回另一个文件名的相对路径，用于zip文件中的路径.
	 * 
	 * @param baseDir
	 *            java.lang.String 根目录
	 * @param realFileName
	 *            java.io.File 实际的文件名
	 * @return 相对文件名
	 */
	private static String getAbsFileName(String baseDir, File realFileName) {
		File real = realFileName;
		File base = new File(baseDir);
		String ret = real.getName();
		while (true) {
			real = real.getParentFile();
			if (real == null)
				break;
			if (real.equals(base))
				break;
			else {
				ret = real.getName() + "/" + ret;
			}
		}
		return ret;
	}

	/**
	 * 解压ZIP文件
	 * 
	 * @param fileDir
	 *            释放文件的文件夹目录
	 * @param sourceZip
	 *            目标ZIP文件
	 * @throws Exception
	 *             操作异常
	 */
	public static void UnZip(String sourceZip, String fileDir) throws Exception {
		// 释放文件的文件夹目录
		String baseDir = fileDir;
		// 目标ZIP文件
		ZipFile zfile = new ZipFile(sourceZip);
		// 实体列表
		Enumeration zList = zfile.getEntries();
		// ZIP实体
		ZipEntry ze = null;
		// 缓存
		byte[] buf = new byte[1024];
		// 遍历
		while (zList.hasMoreElements()) {
			// 从ZipFile中得到一个ZipEntry
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				continue;
			}
			// 以ZipEntry为参数得到一个InputStream，并写到OutputStream中
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(baseDir, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
			System.out.println("UnZip:" + ze.getName());
		}
		zfile.close();
	}

	public static void main(String args[]) {
		// ZipUtil manager = new ZipUtil();
		try {
			zip("C:\\batchexport\\1", "C:\\batchexport\\1\\BatcheExport.zip");
			// manager.createFileToZip("c:\\jcm.txt","c:\\jcm.zip");
			// manager.releaseZipToFile("c:\\jcm.zip","c:\\jcm01");
			// manager.UnZip("C:\\batchexport\\1\\BatcheExport.zip","C:\\batchexport\\1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("执行完成");
	}

}
