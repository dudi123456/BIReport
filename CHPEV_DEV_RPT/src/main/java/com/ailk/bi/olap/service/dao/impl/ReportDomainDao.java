package com.ailk.bi.olap.service.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.ailk.bi.base.exception.ReportOlapException;
import com.ailk.bi.olap.service.dao.IReportDomainDao;
import com.ailk.bi.olap.util.RptOlapConsts;
import com.ailk.bi.olap.util.RptOlapTabDomainUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportDomainDao implements IReportDomainDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asiabi.olap.service.dao.IReportDomainDao#genTableColStruct(java.util
	 * .List, java.util.List)
	 */
	public List genTableColStruct(List rptDims, List rptMsus)
			throws ReportOlapException {
		List tableCols = null;
		if (null == rptDims || rptDims.size() <= 0 || null == rptMsus
				|| rptMsus.size() <= 0)
			throw new IllegalArgumentException("组装表格列对象时，输入的参数为空");
		tableCols = new ArrayList();
		List list = RptOlapTabDomainUtil.convertRptDims(0, rptDims);
		tableCols.addAll(list);
		list = RptOlapTabDomainUtil.convertRptMsus(list.size(), rptMsus,
				RptOlapConsts.ZERO_STR);
		tableCols.addAll(list);
		return tableCols;
	}

}
