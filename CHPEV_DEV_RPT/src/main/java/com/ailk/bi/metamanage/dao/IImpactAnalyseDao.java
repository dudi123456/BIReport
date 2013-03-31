package com.ailk.bi.metamanage.dao;

public interface IImpactAnalyseDao {
	public String[][] getMsuTable(String msu_id);

	public String[][] getTableInfo(String table_id);

	public String[][] getImpactInfo(String table_id);

	public String[][] getLayerColor();
}
