package com.ailk.bi.metamanage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.model.TableDef;

@SuppressWarnings({ "rawtypes" })
public interface ITableDefService {

	public TableDef getTableDef(String tableId);

	public void add(HttpServletRequest request);

	public void save(HttpServletRequest request);

	public void del(String tableId);

	public boolean isExistTableId(String tableId);

	public List getTableField(String tableId);

	public void addTableField(HttpServletRequest request);

	public void saveTableField(HttpServletRequest request);

	public void delTableField(String tableId, String field_id);
}
