package com.ailk.bi.olap.service.dao.impl;

import org.springframework.stereotype.Repository;

import com.ailk.bi.base.dao.BaseDAO;
import com.ailk.bi.domain.olap.UiRptInfoUserOlap;
import com.ailk.bi.olap.service.dao.IUiRptInfoUserOlapDAO;

@Repository
public class UiRptInfoUserOlapDAOImpl extends
		BaseDAO<UiRptInfoUserOlap, Integer> implements IUiRptInfoUserOlapDAO {

	@Override
	public boolean save(UiRptInfoUserOlap entity) {
		return super.save(entity);
	}

}
