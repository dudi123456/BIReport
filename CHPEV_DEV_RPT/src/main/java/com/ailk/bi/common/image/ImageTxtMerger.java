package com.ailk.bi.common.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ailk.bi.common.image.ParamUtil;

/**
 * @author hailong.wang 此方法是在图象的X，Y位置放入具体的数据或文字 其在HTML中的调用方法如下 <img border="0"
 *         src=
 *         "/ImageTxtMerger?txtParam=热点聚焦&imageFileParam=/bg.jpg&xParam=20&yParam=20&fontColorParam=FFFFFF&fontStyleParam=bold&fontNameParam=宋体&fontSizeParam=16"
 *         >
 * 
 */
public class ImageTxtMerger extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2290085039657811365L;

	public ImageTxtMerger() {
	}

	public void init() throws ServletException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("image/jpeg;charset=GB2312");
		String text = "";
		String imageFile = "";
		int x = 0;
		int y = 0;
		String fontColor = "FFFFFF";
		int fontSize = 0;
		String fontStyle = "bold";
		String fontName = "宋体";
		try {
			text = ParamUtil.getParameter(request, "txtParam");
			imageFile = ParamUtil.getParameter(request, "imageFileParam");
			x = ParamUtil.getIntParameter(request, "xParam", 0);
			y = ParamUtil.getIntParameter(request, "yParam", 0);
			fontColor = ParamUtil.getParameter(request, "fontColorParam");
			fontSize = ParamUtil.getIntParameter(request, "fontSizeParam", 16);
			fontStyle = ParamUtil.getParameter(request, "fontStyleParam");
			fontName = ParamUtil.getParameter(request, "fontNameParam");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServletOutputStream output = response.getOutputStream();
		if (imageFile.toLowerCase().endsWith(".jpeg")
				|| imageFile.toLowerCase().endsWith(".jpg")) {
			response.setContentType("image/jpeg;charset=GB2312");
			imageFile = getServletContext().getRealPath(imageFile);
			IIOImage ioImage = null;
			ImageWriter writer = null;
			ImageOutputStream ios = null;
			InputStream imageIn = null;
			try {
				imageIn = new FileInputStream(new File(imageFile));
				BufferedImage image = ImageIO.read(imageIn);
				Graphics g = image.getGraphics();
				g.setColor(new Color(Integer.parseInt(fontColor, 16)));
				Font mFont = new Font(fontName, 0, fontSize);
				if (fontStyle.equalsIgnoreCase("italic"))
					mFont = new Font(fontName, 2, fontSize);
				if (fontStyle.equalsIgnoreCase("bold"))
					mFont = new Font(fontName, 1, fontSize);
				if (fontStyle.equalsIgnoreCase("plain"))
					mFont = new Font(fontName, 0, fontSize);
				g.setFont(mFont);
				g.drawString(text, x, y);
				// JPEGImageEncoder encoder =
				// JPEGCodec.createJPEGEncoder(output);
				// encoder.encode(image);
				// imageIn.close();
				ioImage = new IIOImage(image, null, null);
				// 获取输出流助手
				writer = (ImageWriter) ImageIO.getImageWritersByFormatName(
						"jpeg").next();
				// 设置参数
				ImageWriteParam param = writer.getDefaultWriteParam();
				// 显式压缩
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(0.2f);
				// 生成图片输出流
				ios = ImageIO.createImageOutputStream(imageIn);
				// 设置输出流
				writer.setOutput(ios);
				// 输出图片
				writer.write(ioImage);
				// 抛弃老的生成图片方式
				// encoder = JPEGCodec.createJPEGEncoder(sos);
				// encoder.encode(buffImg);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != writer) {
					writer.dispose();
					writer = null;
				}
				if (null != ioImage) {
					ioImage = null;
				}
				// 关闭图片输出流
				if (null != ios) {
					ios.close();
					ios = null;
				}
				if (null != imageIn) {
					imageIn.close();
				}
			}
		}
		output.close();
	}

}
