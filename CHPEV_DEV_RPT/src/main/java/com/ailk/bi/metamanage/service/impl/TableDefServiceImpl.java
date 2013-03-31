package com.ailk.bi.metamanage.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.metamanage.dao.ITableDefDao;
import com.ailk.bi.metamanage.dao.impl.TableDefDaoImpl;
import com.ailk.bi.metamanage.model.TableDef;
import com.ailk.bi.metamanage.service.ITableDefService;

@SuppressWarnings({ "rawtypes" })
public class TableDefServiceImpl implements ITableDefService {

	private ITableDefDao dao = new TableDefDaoImpl();

	public TableDef getTableDef(String tableId) {
		TableDef info = dao.getTableDef(tableId);
		return info;
	}

	public boolean isExistTableId(String tableId) {
		boolean flag = dao.isExistTableId(tableId);
		return flag;
	}

	public void add(HttpServletRequest request) {
		dao.add(request);
	}

	public void save(HttpServletRequest request) {
		dao.save(request);
	}

	public void del(String tableId) {
		dao.del(tableId);
	}

	public List getTableField(String tableId) {
		return dao.getTableField(tableId);
	}

	public void addTableField(HttpServletRequest request) {
		dao.addTableField(request);
	}

	public void saveTableField(HttpServletRequest request) {
		dao.saveTableField(request);
	}

	public void delTableField(String tableId, String field_id) {
		dao.delTableField(tableId, field_id);
	}
}
