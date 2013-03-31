package com.ailk.bi.adhoc.service.impl;

import com.ailk.bi.adhoc.dao.IAdhocCheckDAO;
import com.ailk.bi.adhoc.service.IAdhocCheckService;
import com.ailk.bi.adhoc.struct.AdhocCheckStruct;
import com.ailk.bi.adhoc.util.*;
import com.ailk.bi.base.facade.IFacade;

public class AdhocCheckFacade implements IFacade {

	private IAdhocCheckService checkService = null;

	public void setHocService(IAdhocCheckService checkService) {
		this.checkService = checkService;
	}

	public AdhocCheckFacade(IAdhocCheckDAO dao) {
		AdhocCheckService service = new AdhocCheckService();
		service.setCheckDAO(dao);
		this.checkService = service;
	}

	/**
	 * 提取配置项信息
	 * 
	 * @param obj_type
	 * @return
	 */
	public AdhocCheckStruct[] getCheckSQLInfo(String obj_type) {

		AdhocCheckStruct[] struct = null;
		String[][] arr1 = checkService.getCheckSql(obj_type);

		if (arr1 == null || arr1.length == 0) {
			return struct;
		}

		String[][] arr = AdhocUtil.queryArrayFacade(arr1[0][1]);
		if (arr != null && arr.length > 0) {
			struct = new AdhocCheckStruct[arr.length];
			for (int i = 0; i < arr.length; i++) {
				struct[i] = new AdhocCheckStruct();
				struct[i].setKey(arr[i][0].trim());
				struct[i].setDesc(arr[i][1].trim());
				struct[i].setParent_key(arr[i][2].trim());
				struct[i].setParent_desc(arr[i][3].trim());
			}

		}

		return struct;
	}

	/**
	 * 提取配置项信息
	 * 
	 * @param obj_type
	 * @return
	 */
	public String[][] getCheckSQL(String obj_type) {

		String[][] arr = checkService.getCheckSql(obj_type);

		return arr;

	}

	/**
	 * 
	 * @param obj_type
	 * @return
	 */
	public String[][] getCheckRule(String obj_type) {
		return checkService.getCheckRule(obj_type);
	}

}
