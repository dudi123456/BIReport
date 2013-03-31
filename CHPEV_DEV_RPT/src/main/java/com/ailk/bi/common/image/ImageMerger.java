package com.ailk.bi.common.image;

import java.awt.*;
import java.awt.image.*;

/**
 * @author hailong.wang 将两个图像进行合并
 */
public class ImageMerger {
	protected double w1, w2;

	protected Image i1;

	protected Image i2;

	protected ColorModel cm;

	int rwid, rhgt;

	protected int results[];

	/**
	 * 对于两个图象创建一个图象合并器. 它必须调用Generate方法才能产生. 这个构造器的缺省高度是0.5，宽度是0.5
	 */
	public ImageMerger(Image img1, Image img2) {
		cm = null;
		i1 = img1;
		i2 = img2;
		w1 = 0.5;
		w2 = 0.5;
		rwid = 0;
		rhgt = 0;
		results = null;
	}

	/**
	 * 合并图象的具体方法
	 * 
	 * @param img1
	 *            需要合并图象1
	 * @param img2
	 *            需要合并图象2
	 * @param w1
	 *            图象1的w1=weight*height
	 * @param w2
	 *            图象2的w2=weight*height
	 * @param comp
	 *            图象能够在其上进行移动的容器或组件
	 * @return 返回合并后的图象
	 */
	public static Image getImageMerger(Image img1, Image img2, double w1,
			double w2, Component comp) {
		Image imgr = null;
		ImageMerger imerge = new ImageMerger(img1, img2);
		imerge.setWeights(w1, w2);
		imerge.generate(comp);
		imgr = imerge.getGeneratedImage();
		imerge.dispose();
		return imgr;
	}

	/**
	 * 设置两个图象的相对宽度. Usually, these should add up to 1.0, but they don't have to.
	 */
	public void setWeights(double img1weight, double img2weight) {
		w1 = img1weight;
		w2 = img2weight;
	}

	/**
	 * 创建图象合并 Generate the merged image and store it for later hand-off to an
	 * ImageConsumer. The caller must supply a Component c on which the image
	 * will eventually be drawn.
	 */
	public boolean generate(Component comp) {
		MediaTracker mt;
		mt = new MediaTracker(comp);
		mt.addImage(i1, 1);
		mt.addImage(i2, 2);
		try {
			mt.waitForAll();
		} catch (Exception ie) {
		}

		int wid1, wid2;
		int hgt1, hgt2;

		wid1 = i1.getWidth(comp);
		wid2 = i2.getWidth(comp);
		hgt1 = i1.getHeight(comp);
		hgt2 = i2.getHeight(comp);

		rwid = Math.max(wid1, wid2);
		rhgt = Math.max(hgt1, hgt2);

		results = new int[rwid * rhgt];

		int[] p1 = new int[rwid * rhgt];
		int[] p2 = new int[rwid * rhgt];

		PixelGrabber pg1 = new PixelGrabber(i1, 0, 0, wid1, hgt1, p1, 0, rwid);
		try {
			pg1.grabPixels();
		} catch (Exception ie1) {
		}

		PixelGrabber pg2 = new PixelGrabber(i2, 0, 0, wid2, hgt2, p2, 0, rwid);
		try {
			pg2.grabPixels();
		} catch (Exception ie2) {
		}

		cm = ColorModel.getRGBdefault();

		int y, x, rp, rpi;
		int red1, red2, redr;
		int green1, green2, greenr;
		int blue1, blue2, bluer;
		int alpha1, alpha2, alphar;
		double wgt1, wgt2;

		for (y = 0; y < rhgt; y++) {
			for (x = 0; x < rwid; x++) {
				rpi = y * rwid + x;
				rp = 0;
				blue1 = p1[rpi] & 0x00ff;
				blue2 = p2[rpi] & 0x00ff;
				green1 = (p1[rpi] >> 8) & 0x00ff;
				green2 = (p2[rpi] >> 8) & 0x00ff;
				red1 = (p1[rpi] >> 16) & 0x00ff;
				red2 = (p2[rpi] >> 16) & 0x00ff;
				alpha1 = (p1[rpi] >> 24) & 0x00ff;
				alpha2 = (p2[rpi] >> 24) & 0x00ff;

				// Computations for combining the pixels,
				// perform this any way you like!
				// Here we just use simple weighted addition.
				wgt1 = w1 * (alpha1 / 255.0);
				wgt2 = w2 * (alpha2 / 255.0);
				redr = (int) (red1 * wgt1 + red2 * wgt2);
				redr = (redr < 0) ? (0) : ((redr > 255) ? (255) : (redr));
				greenr = (int) (green1 * wgt1 + green2 * wgt2);
				greenr = (greenr < 0) ? (0) : ((greenr > 255) ? (255)
						: (greenr));
				bluer = (int) (blue1 * wgt1 + blue2 * wgt2);
				bluer = (bluer < 0) ? (0) : ((bluer > 255) ? (255) : (bluer));
				alphar = 255;

				rp = (((((alphar << 8) + (redr & 0x0ff)) << 8) + (greenr & 0x0ff)) << 8)
						+ (bluer & 0x0ff);

				// save the pixel
				results[rpi] = rp;
			}
		}
		return true;
	}

	/**
	 * 产生图象的一个简单的方法 MemoryImageSource.
	 */
	public Image getGeneratedImage() {
		Image ret;
		MemoryImageSource mis;
		if (results == null) {
			return null;
		}
		mis = new MemoryImageSource(rwid, rhgt, cm, results, 0, rwid);
		ret = Toolkit.getDefaultToolkit().createImage(mis);
		return ret;
	}

	/**
	 * Call this to free up pixel storage allocated by this ImageMerger object.
	 */
	public void dispose() {
		results = null;
		return;
	}

}
