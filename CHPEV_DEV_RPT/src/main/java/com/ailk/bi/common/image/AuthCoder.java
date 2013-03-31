package com.ailk.bi.common.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class AuthCoder {
	private AuthCoder() {

	}

	/**
	 * 生成验证码图片
	 *
	 * @param sos
	 * @return
	 * @throws IOException
	 */
	public static String genAuthCode(OutputStream sos) throws IOException {
		int width = 70, height = 34;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 24));

		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 10, 25);
		}

		g.dispose();

		// ImageIO.write(image, "JPEG", response.getOutputStream());
		IIOImage ioImage = null;
		ImageWriter writer = null;
		ImageOutputStream ios = null;
		try {
			// 使用新的生成图片方式
			// 声明一个虚拟图片源
			ioImage = new IIOImage(image, null, null);
			// 获取输出流助手
			writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg")
					.next();
			// 设置参数
			ImageWriteParam param = writer.getDefaultWriteParam();
			// 显式压缩
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.2f);
			// 生成图片输出流
			ios = ImageIO.createImageOutputStream(sos);
			// 设置输出流
			writer.setOutput(ios);
			// 输出图片
			writer.write(ioImage);
			// 抛弃老的生成图片方式
			// encoder = JPEGCodec.createJPEGEncoder(sos);
			// encoder.encode(buffImg);
			sos.flush();
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
			if (null != sos) {
				sos.close();
			}
		}
		return sRand;
	}

	private static Color getRandColor(int fc, int bc) {// ??????????
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
