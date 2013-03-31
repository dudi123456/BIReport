package com.ailk.bi.common.image;

import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 * @author hailong.wang 这个CLASS允许用户按照自己的输入将图象分割为许多部分 但是缺点是这些部分的大小是相同的
 */
public class SplitImage implements ImageObserver {
	/**
	 * 将图象分割为不同的部分
	 * 
	 * @param image
	 *            需要进行分割的图象
	 * @param rows
	 *            每列图象的数量，缺省必须大于0.
	 * @param columns
	 *            每行的图象的数量，缺省必须大于0.
	 */
	public SplitImage(Image image, int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.image = image;

		// arguments checking
		if (rows < 1 || columns < 1)
			error = readen = true;
		else {
			height = image.getHeight(this);
			width = image.getWidth(this);
			checkIfStart();
		}
	}

	/**
	 * 取得每个需要拆分图象的高度的数值
	 * 
	 * @return 返回需要拆分图象的高度的数值，如果图象错误或行、列数值无效是返回-1.
	 * @exception InterruptedException
	 */
	public synchronized int getHeight() throws InterruptedException {
		while (!readen)
			wait();
		return finalHeight;
	}

	/**
	 * 取得拆分图象的宽度的数值.
	 * 
	 * @return 返回需要拆分图象的宽度的数值，如果图象错误或行、列数值无效是返回-1.
	 * @exception InterruptedException
	 */
	public synchronized int getWidth() throws InterruptedException {
		while (!readen)
			wait();
		return finalWidth;
	}

	/**
	 * 对于被选择的图象生成一个图象产生器.
	 * 
	 * @return ImageProducer 被创建,如果有错误则返回NULL. The error may have been generated
	 *         because (1) bad initialization, wrong number of rows or columns
	 *         (2) error reading the image (3) wrong parameter in the call to
	 *         the method, row and/or col are not valids
	 * @param row
	 *            端口行.必须是-1和0之间的数值
	 * @param col
	 *            端口列，必须是-1和0之间的数值
	 * @exception InterruptedException
	 */
	public synchronized ImageProducer getImageProducer(int row, int col)
			throws InterruptedException {
		while (!readen)
			wait();
		ImageProducer imageProducer = null;
		if (!error && row >= 0 && col >= 0 && row < rows && col < columns)
			imageProducer = new FilteredImageSource(image.getSource(),
					new CropImageFilter(col * finalWidth, row * finalHeight,
							finalWidth, finalHeight));
		return imageProducer;
	}

	/**
	 * ImageObserver Method
	 */
	public synchronized boolean imageUpdate(Image img, int flags, int x, int y,
			int w, int h) {
		boolean ret;
		if ((flags & ImageObserver.ERROR) != 0) {
			ret = false;
			error = readen = true;
			notifyAll();
		} else {
			if ((flags & ImageObserver.WIDTH) != 0)
				width = w;
			if ((flags & ImageObserver.HEIGHT) != 0)
				height = h;
			ret = !checkIfStart();
		}
		return ret;
	}

	/**
	 * Checks if the height and the width are already available, and -if so-, it
	 * creates the needed filters;
	 * 
	 * @return true if the checking has been positive
	 */
	synchronized boolean checkIfStart() {
		boolean ret = width != -1 && height != -1;
		if (ret) {
			finalWidth = width / columns;
			finalHeight = height / rows;
			readen = true;
			notifyAll();
		}
		return ret;
	}

	/**
	 * Identifies any error given by the Image Producer
	 */
	boolean error = false;

	/**
	 * Identifies when the image has been readed (in fact, when the height and
	 * width are known)
	 */
	boolean readen = false;

	/**
	 * The image being splitting
	 */
	Image image;

	/**
	 * Number of rows and columns used to split the image
	 */
	int rows, columns;

	/**
	 * Dimensions of the original image
	 */
	int width = -1, height = -1;

	/**
	 * Dimensions of the final image
	 */
	int finalWidth = -1, finalHeight = -1;
}
