package com.ailk.bi.metamanage.dao;

public interface ISourceAnalyseDao {
	public String[][] getMsuTable(String msu_id);

	public String[][] getTableInfo(String table_id);

	public String[][] getSourceInfo(String str);

	public String[][] getLayerColor();

	public boolean isExistTable(String table_id);
}
