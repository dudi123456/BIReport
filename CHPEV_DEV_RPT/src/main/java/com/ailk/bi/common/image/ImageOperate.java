package com.ailk.bi.common.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.ailk.bi.common.app.Arith;

public class ImageOperate {
	Image img1;
	String message = "";
	String filePath = ""; // 程序本身所在的根目录， 绝对路径。
	String waterImg1 = "", waterImg2 = ""; // 水印图片文件名
	String waterPath = ""; // 水印图片所在路径，绝对路径
	String str = new String("test");

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	public void waterImage(String oldFile, String newFile) {
		IIOImage ioImage = null;
		ImageWriter writer = null;
		ImageOutputStream ios = null;
		FileOutputStream out = null;
		try {
			File myFile = new File(filePath + oldFile); // 指定文件名含路径

			Image src = javax.imageio.ImageIO.read(myFile); // 构造Image对象
			int w = src.getWidth(null); // 得到源图宽
			int h = src.getHeight(null); // 得到源图长
			Graphics gim = src.getGraphics();
			BufferedImage tag = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, w, h, null);
			if (!waterImg1.equals("") && waterImg2.equals("")) {
				Image img1 = javax.imageio.ImageIO.read(new File(waterPath
						+ waterImg1));// 构造Image对象
				int w1 = img1.getWidth(null); // 得到源图宽
				int h1 = img1.getHeight(null); // 得到源图长
				Graphics2D tag2D = (Graphics2D) tag.getGraphics();
				tag2D.setComposite(makeComposite(0.5F));
				// tag2D.drawImage(img1,w-w/10-12,h-((h1*w)/(w1*10))-12,w/10,((h1*w)/(w1*10)),null);
				tag2D.drawImage(img1, w - w1 - 12, h - h1 - 12, w1, h1, null);
				// tag.getGraphics().drawImage(img1,w-w1-12,h-30,w1,h1,null);
			}
			if (!waterImg1.equals("") && !waterImg2.equals("")) {
				Image img1 = javax.imageio.ImageIO.read(new File(waterPath
						+ waterImg1));// 构造Image对象
				int w1 = img1.getWidth(null); // 得到源图宽
				int h1 = img1.getHeight(null); // 得到源图长
				Image img2 = javax.imageio.ImageIO.read(new File(waterPath
						+ waterImg2));// 构造Image对象
				int w2 = img2.getWidth(null); // 得到源图宽
				int h2 = img2.getHeight(null); // 得到源图长
				tag.getGraphics().drawImage(img2, w - w2 - 12, h - 30, w2, h2,
						null);
				tag.getGraphics().drawImage(img1, w - w1 - w2 - 15, h - 32, w1,
						h1, null);
			}
			out = new FileOutputStream(filePath + newFile); // 输出到文件流
			// 获取输出流助手
			writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg")
					.next();
			// 设置参数
			ImageWriteParam param = writer.getDefaultWriteParam();
			// 显式压缩
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.2f);
			// 生成图片输出流
			ios = ImageIO.createImageOutputStream(out);
			// 设置输出流
			writer.setOutput(ios);
			// 输出图片
			writer.write(ioImage);
			gim.dispose();
			out.flush();
		} catch (Exception e) {
			message = waterPath + e.toString() + filePath;
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
				try {
					ios.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ios = null;
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void writeImage(String oldFile, String newFile) {
		IIOImage ioImage = null;
		ImageWriter writer = null;
		ImageOutputStream ios = null;
		FileOutputStream out = null;
		try {
			File myFile = new File(filePath + oldFile); // 指定文件名含路径
			Image src = javax.imageio.ImageIO.read(myFile); // 构造Image对象
			int w = src.getWidth(null); // 得到源图宽
			int h = src.getHeight(null); // 得到源图长
			// 在你创立的image上写字
			Graphics gim = src.getGraphics();
			Font font = new Font("Tahoma", Font.BOLD, 14);
			gim.setFont(font);
			/*
			 * gim.setColor(Color.black); gim.drawString(str,
			 * w-(str.length()*10-8), h-19); gim.setColor(Color.lightGray);
			 * gim.drawString(str, w-(str.length()*10-7), h-20);
			 */
			gim.setColor(Color.black);
			gim.drawString(str, w - (str.length() * 10), h - 19);
			gim.setColor(Color.lightGray);
			gim.drawString(str, w - (str.length() * 10), h - 20);

			BufferedImage tag = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, w, h, null);
			out = new FileOutputStream(filePath + newFile); // 输出到文件流
			gim.dispose();
			// 获取输出流助手
			writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg")
					.next();
			// 设置参数
			ImageWriteParam param = writer.getDefaultWriteParam();
			// 显式压缩
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.2f);
			// 生成图片输出流
			ios = ImageIO.createImageOutputStream(out);
			// 设置输出流
			writer.setOutput(ios);
			// 输出图片
			writer.write(ioImage);
			out.flush();
		} catch (Exception e) {
			message = e.toString();
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
				try {
					ios.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ios = null;
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	public void shrinkImage(int w, int h, String oldFile, String newFile)
			throws Exception {
		// 参数w表示缩小后的宽度 h缩小后的高度，任何一个为0 则按另一方比例缩小。
		Arith arith = new Arith();
		double wd = 0;
		double hd = 0;
		String ws = "", hs = "";
		IIOImage ioImage = null;
		ImageWriter writer = null;
		ImageOutputStream ios = null;
		FileOutputStream out = null;
		try {
			File myFile = new File(filePath + oldFile); // 指定文件名含路径
			Image src = javax.imageio.ImageIO.read(myFile); // 构造Image对象
			int wideth = src.getWidth(null); // 得到源图宽
			int height = src.getHeight(null); // 得到源图长
			if ((w > 0 && h == 0) && w < wideth) {
				// 计算出宽度的缩放比例。
				wd = arith.div(Double.parseDouble(String.valueOf(w)),
						Double.parseDouble(String.valueOf(wideth)), 10);
				hd = arith.mul(wd, Double.parseDouble(String.valueOf(height)));
				hd = arith.round(hd, 0); // 四舍五入保留小数点0位
				hs = String.valueOf(hd);
				hs = hs.substring(0, hs.indexOf("."));
				h = Integer.parseInt(hs); // 按比例缩小高度
			} else if ((h > 0 && w == 0) && h < height) {
				// 计算出高度的缩放比例。
				hd = arith.div(Double.parseDouble(String.valueOf(h)),
						Double.parseDouble(String.valueOf(height)), 10);
				wd = arith.mul(hd, Double.parseDouble(String.valueOf(wideth)));
				wd = arith.round(wd, 0); // 四舍五入保留小数点0位
				ws = String.valueOf(wd);
				ws = ws.substring(0, ws.indexOf("."));
				w = Integer.parseInt(ws); // 按比例缩小高度
			} else {
				w = wideth;
				h = height;
			}
			java.awt.image.BufferedImage tag = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, w, h, null); // 绘制缩小后的图
			out = new FileOutputStream(filePath + newFile); // 输出到文件流
			// 获取输出流助手
			writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg")
					.next();
			// 设置参数
			ImageWriteParam param = writer.getDefaultWriteParam();
			// 显式压缩
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.2f);
			// 生成图片输出流
			ios = ImageIO.createImageOutputStream(out);
			// 设置输出流
			writer.setOutput(ios);
			// 输出图片
			writer.write(ioImage);
			out.flush();
		} catch (Exception e) {
			message = e.toString();
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
				try {
					ios.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ios = null;
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getMessage() {
		return this.message;
	}

	public void setFilePath(String filePath) {
		if (filePath != null) {
			this.filePath = filePath;
		} else {
			this.filePath = "";
		}
	}

	public void setStr(String str) {
		if (str != null) {
			this.str = str;
		} else {
			this.str = "test";
		}
	}

	public void setWaterImg1(String str) {
		if (str != null) {
			this.waterImg1 = str;
		} else {
			this.waterImg1 = "";
		}
	}

	public void setWaterImg2(String str) {
		if (str != null) {
			this.waterImg2 = str;
		} else {
			this.waterImg2 = "";
		}
	}

	public void setWaterPath(String str) {
		if (str != null) {
			this.waterPath = str;
		} else {
			this.waterPath = "";
		}
	}

}
