package com.ailk.bi.adhoc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.adhoc.domain.UiAdhocUserListTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.system.facade.impl.CommonFacade;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdhocBuildUserDetail {

	private UiAdhocUserListTable[] mdefineInfo;

	private HttpServletRequest request;

	/**
	 * 
	 * @param request
	 * @param sqlCount
	 *            :记录集数sql
	 * @param sqlQryDetail
	 *            :清单SQL
	 * @param listField
	 *            :导出字段
	 * @param expDetailName
	 *            :导出名称
	 * @param typeId
	 *            :类型ID
	 * @param resId
	 *            :资源ID
	 */
	public AdhocBuildUserDetail(HttpServletRequest request, String sqlCount,
			String sqlQryDetail, List listField, String expDetailName,
			String typeId, String resId) {
		this.request = request;
		this.sqlCount = sqlCount;
		this.sqlQryDetail = sqlQryDetail;
		this.listField = listField;
		this.expDetailName = expDetailName;
		this.adhoc_id = typeId;
		this.res_id = resId;
	}

	public AdhocBuildUserDetail(HttpServletRequest request, String sqlCount,
			String sqlQryDetail, List listField, String expDetailName,
			String typeId, int sigmaId, String sigmaParam) {
		this.request = request;
		this.sqlCount = sqlCount;
		this.sqlQryDetail = sqlQryDetail;
		this.listField = listField;
		this.expDetailName = expDetailName;
		this.adhoc_id = typeId;
		this.sigmaId = sigmaId;
		this.sigmaParam = sigmaParam;

	}

	private String adhoc_id = "-1";
	private String res_id = "";
	private String sqlCount;

	private int sigmaId;
	private String sigmaParam;

	public int getSigmaId() {
		return sigmaId;
	}

	public void setSigmaId(int sigmaId) {
		this.sigmaId = sigmaId;
	}

	public String getSigmaParam() {
		return sigmaParam;
	}

	public void setSigmaParam(String sigmaParam) {
		this.sigmaParam = sigmaParam;
	}

	/**
	 * 格式：fieldEngName|fieldGBKName|MAPCODE|UNIT|DIGITNUM|FIELDTYPE
	 * 如：FEE|本月实际应收(总账)||元|2|C
	 */
	private List listField;

	private String sessionId;

	private String Ip;

	private String expDetailName;

	private String sqlQryDetail;

	private String oper_no;

	private String selectFldStr = "";

	private boolean init() {

		oper_no = CommonFacade.getLoginId(request.getSession());
		sessionId = request.getSession().getId();
		Ip = CommTool.getClientIP(request);
		try {

			List listChild = new ArrayList();
			// System.out.println(listField.size());
			for (int j = 0; j < listField.size(); j++) {
				String fieldInputString = StringB
						.NulltoBlank((String) listField.get(j));
				/**
				 * 格式：fieldEngName|fieldGBKName|MAPCODE|UNIT|DIGITNUM|FIELDTYPE
				 * 如：FEE|本月实际应收(总账)||元|2|C
				 */
				// System.out.println(fieldInputString + ":");
				if (fieldInputString.length() > 0) {
					String[] strField = fieldInputString.split("\\|", 6);
					// System.out.println(fieldInputString + ":" +
					// strField.length + ":" + strField[0] + ":" );

					UiAdhocUserListTable tmp = new UiAdhocUserListTable();

					tmp.setMsu_field(StringB.NulltoBlank(strField[0]));

					tmp.setMsu_name(StringB.NulltoBlank(strField[1]));
					tmp.setMap_code(StringB.NulltoBlank(strField[2]));
					tmp.setMsu_unit(StringB.NulltoBlank(strField[3]));
					tmp.setMsu_digit(StringB.NulltoBlank(strField[4]));
					tmp.setMsu_type(StringB.NulltoBlank(strField[5]));

					listChild.add(tmp);

					if (selectFldStr.length() == 0) {
						selectFldStr = strField[0];
					} else {
						selectFldStr += "," + strField[0];
					}

				}

			}

			mdefineInfo = (UiAdhocUserListTable[]) listChild
					.toArray(new UiAdhocUserListTable[listChild.size()]);

			if (sqlCount == null || sqlCount.length() == 0) {
				sqlCount = "select count(*) from (" + this.sqlQryDetail + ")";
			}
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int doSaveSigmaExpDetailTask() {
		if (init() == false) {
			return -1;
		}

		String mainID = CommTool.dbGetMaxIDBySeqName("ADHOC_BUILDXLS_TASK_SEQ");

		String[] sqlM = new String[mdefineInfo.length + 1];

		String sqlTmp = "select " + selectFldStr
				+ " from (select JFTOT.*,ROWNUM RN from (" + sqlQryDetail
				+ ") JFTOT Where rownum<=? ) where RN>?";

		sqlM[0] = "insert into ui_adhoc_buildxls_task(id,oper_no,xls_name,xls_desc,row_cnt,cnt_sql,qry_sql,flag,add_date,adhoc_id,session_id,ip_addr,SIGMA_ID,SIGMA_PARAM) "
				+ " values("
				+ mainID
				+ ",'"
				+ oper_no
				+ "','"
				+ expDetailName
				+ "','"
				+ expDetailName
				+ "','-1','"
				+ sqlCount.replaceAll("'", "''")
				+ "','"
				+ sqlTmp.replaceAll("'", "''")
				+ "',0,sysdate,'"
				+ this.adhoc_id
				+ "','"
				+ sessionId
				+ "','"
				+ Ip
				+ "',"
				+ this.sigmaId + ",'" + this.sigmaParam + "')";

		for (int j = 0; j < mdefineInfo.length; j++) {
			sqlM[j + 1] = "insert into ui_adhoc_buildxls_task_extend values(ADHOC_BLDXLS_TASKEXT_SEQ.nextval,"
					+ mainID + "," + mdefineInfo[j].toString() + ")";
			// System.out.println(sqlM[j+1]);
			// logcommon.debug(sqlM[j+1]);
		}

		try {
			WebDBUtil.execTransUpdate(sqlM);
		} catch (AppException e) {

			e.printStackTrace();
			return -1;
		}

		AdhocBuildXlsBean bean = new AdhocBuildXlsBean();
		bean.setMainID(mainID);

		bean.setRcnt("-1");
		bean.setCntSql(sqlCount);

		AdhocSaveTaskInJob jobSave = new AdhocSaveTaskInJob(bean);
		AdhocExportXlsTask task = new AdhocExportXlsTask(jobSave);
		Timer timer = new Timer();
		timer.schedule(task, 100);

		return 1;
	}

	/**
	 * 
	 * @return:成功返回true。
	 */

	public int doSaveExpDetailTask() {
		if (init() == false) {
			return -1;
		}

		String mainID = CommTool.dbGetMaxIDBySeqName("ADHOC_BUILDXLS_TASK_SEQ");

		String[] sqlM = new String[mdefineInfo.length + 1];

		String sqlTmp = "select " + selectFldStr
				+ " from (select JFTOT.*,ROWNUM RN from (" + sqlQryDetail
				+ ") JFTOT Where rownum<=? ) where RN>?";

		sqlM[0] = "insert into ui_adhoc_buildxls_task(id,oper_no,xls_name,xls_desc,row_cnt,cnt_sql,qry_sql,flag,add_date,adhoc_id,session_id,ip_addr,BAK_FLD_02) "
				+ " values("
				+ mainID
				+ ",'"
				+ oper_no
				+ "','"
				+ expDetailName
				+ "','"
				+ expDetailName
				+ "','-1','"
				+ sqlCount.replaceAll("'", "''")
				+ "','"
				+ sqlTmp.replaceAll("'", "''")
				+ "',0,sysdate,'"
				+ this.adhoc_id
				+ "','"
				+ sessionId
				+ "','"
				+ Ip
				+ "','"
				+ this.res_id + "')";

		for (int j = 0; j < mdefineInfo.length; j++) {
			sqlM[j + 1] = "insert into ui_adhoc_buildxls_task_extend values(ADHOC_BLDXLS_TASKEXT_SEQ.nextval,"
					+ mainID + "," + mdefineInfo[j].toString() + ")";
			// System.out.println(sqlM[j+1]);
			// logcommon.debug(sqlM[j+1]);
		}

		try {
			WebDBUtil.execTransUpdate(sqlM);
		} catch (AppException e) {

			e.printStackTrace();
			return -1;
		}

		AdhocBuildXlsBean bean = new AdhocBuildXlsBean();
		bean.setMainID(mainID);

		bean.setRcnt("-1");
		bean.setCntSql(sqlCount);

		AdhocSaveTaskInJob jobSave = new AdhocSaveTaskInJob(bean);
		AdhocExportXlsTask task = new AdhocExportXlsTask(jobSave);
		Timer timer = new Timer();
		timer.schedule(task, 100);

		return 1;
	}

	public String getExpDetailName() {
		return expDetailName;
	}

	public void setExpDetailName(String expDetailName) {
		this.expDetailName = expDetailName;
	}

	public List getListField() {
		return listField;
	}

	public void setListField(List listField) {
		this.listField = listField;
	}

	public String getSqlCount() {
		return sqlCount;
	}

	public void setSqlCount(String sqlCount) {
		this.sqlCount = sqlCount;
	}

	public String getSqlQryDetail() {
		return sqlQryDetail;
	}

	public void setSqlQryDetail(String sqlQryDetail) {
		this.sqlQryDetail = sqlQryDetail;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
