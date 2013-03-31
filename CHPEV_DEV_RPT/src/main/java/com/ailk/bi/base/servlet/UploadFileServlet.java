package com.ailk.bi.base.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 上传文件类
 *
 * @author Administrator
 *
 */
@SuppressWarnings({ "rawtypes" })
public class UploadFileServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		return;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		if (ServletFileUpload.isMultipartContent(req)) {
			req.setCharacterEncoding("utf-8");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			/**
			 * 临时文件存储路径要真实存在
			 */
			String foldPath = this.getInitParameter("filepath");
			factory.setRepository(new File(foldPath));
			// 内存最大占用
			factory.setSizeThreshold(1024000);
			ServletFileUpload sfu = new ServletFileUpload(factory);
			// 单个文件最大值byte
			sfu.setSizeMax(102400000);
			// 所有上传文件的总和最大值byte
			sfu.setSizeMax(204800000);
			List items = null;
			try {
				items = sfu.parseRequest(req);
			} catch (SizeLimitExceededException e) {
				System.out.println("size limit exception!");
			} catch (Exception e) {
				e.printStackTrace();
			}

			Iterator iter = items == null ? null : items.iterator();
			while (iter != null && iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
					//BIWeb/sysparam/param_error.jsp
					res.sendRedirect("sysparam/param_error.jsp");
					String fileName = item.getName().substring(
							item.getName().lastIndexOf("\\"));
					BufferedInputStream in = new BufferedInputStream(
							item.getInputStream());
					// 文件存储在工程的upload目录下
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(new File(foldPath + fileName)));
					byte[] data = new byte[1];
					while (in.read(data) != -1) {
						out.write(data);
					} // 将缓冲区中的资料全部写出
					out.flush(); // 关闭串流
					in.close();
					out.close();
				}
			}
		} else {
			res.sendRedirect("sysparam/param_error.jsp");
		}
	}

}
