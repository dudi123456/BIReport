package com.ailk.bi.base.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件下载
 * 
 * @author Administrator
 * 
 */
public class DownloadFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) {

		String downLoadPath = "";
		String downLoadFileName = "";
		downLoadPath = this.getInitParameter("filepath");
		downLoadFileName = req.getParameter("file_name");

		// 首先判断文件是否存在
		// 存在直接下载
		// 不存在先生成在下载

		if (!isExsit(downLoadPath, downLoadFileName)) {
			System.out.println("不存在先生成在下载=====?");
		}
		// 下载
		try {
			downloadFile(req, res, downLoadPath, downLoadFileName);
		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		return;
	}

	protected void downloadFile(HttpServletRequest req,
			HttpServletResponse res, String path, String filename)
			throws IOException {

		// FileInputStream in = null; // 输入流
		BufferedInputStream in = null;
		ServletOutputStream out = null; // 输出流

		try {
			res.setContentType("application/vnd.ms-excel; charset=UTF-8");
			res.setHeader("Content-disposition", "attachment; filename="
					+ filename);
			// 读入文件
			in = new BufferedInputStream(new FileInputStream(path + filename));
			// 打开写入流
			out = res.getOutputStream();
			int aRead = 0;
			while ((aRead = in.read()) != -1 & in != null) {
				out.write(aRead);
			}
			// 全部写入
			out.flush();
			// res.sendRedirect("/param_error.jsp?closeFlag=1&mess=下载文件成功！");
		} catch (Throwable e) {
			System.out.println("下载文件失败！");
			res.sendRedirect("/param_error.jsp?closeFlag=1&mess=下载文件失败！");
		} finally {
			try {
				in.close();
				out.close();
			} catch (Throwable e) {
				System.out.println("下载文件关闭数据流失败！");
				res.sendRedirect("/param_error.jsp?closeFlag=1&mess=下载文件关闭数据流失败！");
			}
		}
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 * @param filename
	 * @return
	 */
	protected boolean isExsit(String path, String filename) {
		File file = new File(path + filename);
		return file.exists();
	}

}
