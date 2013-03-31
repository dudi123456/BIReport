package com.ailk.bi.base.taglib.flashchart;

import java.io.Serializable;

@SuppressWarnings({ "serial" })
public class FlashChartInfo implements Serializable {

	public FlashChartInfo() {

	}

	private String caption;
	private String subcaption;
	private String[] categories;
	private String[] seriesname;
	private String[][] dataset;
	private String width;
	private String height;
	private String configId;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getSubcaption() {
		return subcaption;
	}

	public void setSubcaption(String subcaption) {
		this.subcaption = subcaption;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public String[] getSeriesname() {
		return seriesname;
	}

	public void setSeriesname(String[] seriesname) {
		this.seriesname = seriesname;
	}

	public String[][] getDataset() {
		return dataset;
	}

	public void setDataset(String[][] dataset) {
		this.dataset = dataset;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}
}
