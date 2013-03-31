package com.ailk.bi.report.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.report.domain.RptColDictTable;
import com.ailk.bi.report.domain.RptResourceTable;
import com.ailk.bi.report.service.ILReportService;
import com.ailk.bi.report.service.impl.LReportServiceImpl;
import com.ailk.bi.report.util.ReportConsts;
import com.jspsmart.upload.SmartUpload;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReportBatchLoadHTMLAction extends HTMLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆信息
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response))
			return;

		HttpSession session = request.getSession();
		SmartUpload mySmartUpload = new SmartUpload();
		try {
			mySmartUpload.initialize(this.config, request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}

		try {
			mySmartUpload.setAllowedFilesList("txt,xls");
			// 上载文件
			mySmartUpload.upload();
		} catch (Exception e) {

		}

		com.jspsmart.upload.File myFile = null;

		try {
			myFile = mySmartUpload.getFiles().getFile(0);
		} catch (Exception e) {

		}
		byte[] buff = new byte[myFile.getSize()];
		for (int i = 0; i < myFile.getSize(); i++) {
			buff[i] = myFile.getBinaryData(i);
		}
		try {
			String[] result = processExcelFile(buff);
			session.setAttribute(WebKeys.ATTR_REPORT_BATCH_IMPORT, result);
		} catch (Exception e) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "倒入报表数据失败！");
		}
		this.setNextScreen(request, "batchAddLocalRptResult.screen");
	}

	/**
	 * 处理Excel
	 * 
	 * @param buff
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private String[] processExcelFile(byte[] buff) throws Exception {
		ByteArrayInputStream inputStrean = new ByteArrayInputStream(buff);
		String[] result = null;
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStrean);
			int sheetNumber = 0;
			if (workbook != null) {
				sheetNumber = workbook.getNumberOfSheets();
			}
			result = new String[sheetNumber];
			// 定义报表操作对象
			ILReportService rptService = new LReportServiceImpl();

			for (int i = 0; i < sheetNumber; i++) {
				// 初始化一个报表对象
				RptResourceTable rptTable = new RptResourceTable();

				String field_name = "";
				String data_type = "";
				HSSFSheet aSheet = workbook.getSheetAt(i);// 获得一个sheet
				int rowNum = aSheet.getLastRowNum();
				for (int rowNumOfSheet = 1; rowNumOfSheet < rowNum; rowNumOfSheet++) {
					HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
					short codeCellNum = 0;
					short valueCellNum = 2;
					HSSFCell codeCell = aRow.getCell(codeCellNum);
					HSSFCell valueCell = aRow.getCell(valueCellNum);
					String code = codeCell.getStringCellValue();
					String value = getValue(valueCell);

					if ("RPT_OPERATE".equals(code.toUpperCase().trim())) {
						rptTable.rpt_operate = value;
					}
					if ("RPT_ID".equals(code.toUpperCase().trim())) {
						rptTable.rpt_id = value;
					}
					if ("NAME".equals(code.toUpperCase().trim())) {
						rptTable.name = value;
					}
					if ("CYCLE".equals(code.toUpperCase().trim())) {
						rptTable.cycle = value;
					}
					if ("PARENT_ID".equals(code.toUpperCase().trim())) {
						rptTable.parent_id = value;
					}
					if ("START_DATE".equals(code.toUpperCase().trim())) {
						rptTable.start_date = value;
					}
					if ("TITLE_NOTE".equals(code.toUpperCase().trim())) {
						rptTable.title_note = value;
					}
					if ("RPT_TYPE".equals(code.toUpperCase().trim())) {
						rptTable.rpt_type = value;
					}
					if ("FIELD_TITLE".equals(code.toUpperCase().trim())) {
						field_name = value;
					}
					if ("DATA_TYPE".equals(code.toUpperCase().trim())) {
						data_type = value;
					}
				}
				// 记录报表内容
				try {
					rptTable.privateflag = ReportConsts.NO;
					rptTable.dispenseflag = ReportConsts.NO;
					rptTable.pagecount = ReportConsts.ZERO;
					rptTable.ishead = ReportConsts.NO;
					rptTable.isleft = ReportConsts.NO;
					rptTable.status = ReportConsts.NO;
					rptTable.data_table = ReportConsts.RPT_DEFAUTL_DATATABLE;
					rptTable.data_date = ReportConsts.RPT_DEFAULT_DATADATE;
					if (StringTool.checkEmptyString(rptTable.data_where)
							&& ReportConsts.RPT_DEFAUTL_DATATABLE
									.equals(rptTable.data_table)) {
						rptTable.data_where = "WHERE RPT_ID=''"
								+ rptTable.rpt_id + "''";
					}
					if (!StringTool.checkEmptyString(rptTable.rpt_id)) {
						if ("INSERT".equals(rptTable.rpt_operate)) {
							if (rptService.existReport(rptTable.rpt_id)) {
								result[i] = "报表ID=" + rptTable.rpt_id
										+ "的报表添加失败，该报表ID已经存在。";
								continue;
							}
							rptService.insertReport(rptTable);
						} else if ("UPDATE".equals(rptTable.rpt_operate)) {
							rptService.updateReport(rptTable);
						}
					}
				} catch (AppException e) {
					result[i] = "报表ID=" + rptTable.rpt_id + "的报表添加失败";
					continue;
				}

				// 记录报表列内容
				List listRptCol = new ArrayList();
				String[] arrField_name = field_name.split(";");
				String[] arrData_type = data_type.split(";");
				if (arrField_name == null || arrData_type == null) {
					rptService.delReport(rptTable.rpt_id);
					result[i] += "报表ID=" + rptTable.rpt_id + "的报表添加列定义为空";
					continue;
				}
				if (arrField_name.length != arrData_type.length) {
					rptService.delReport(rptTable.rpt_id);
					result[i] += "报表ID=" + rptTable.rpt_id + "的报表添加列定义和列字段类型不符";
					continue;
				}
				// 根据报表列数取得传进的值，要注意的是并不是每个值都有
				int iCharLen = 1;// 描述列的总数
				int maxCharSequence = 1;// 记录最大描述列的序号
				int iNumLen = 1;// 数值列的总数
				int maxNumSequence = 21;// 记录最大数值列的序号
				for (int col = 0; col < arrField_name.length; col++) {
					// 对象数组值
					String[] svces = null;
					if (ReportConsts.DATA_TYPE_NUMBER.equals(arrData_type[col])) {
						svces = getColValueInit(rptTable.rpt_id,
								arrField_name[col], arrData_type[col],
								Integer.toString(iNumLen),
								Integer.toString(maxNumSequence));
						iNumLen++;
						maxNumSequence++;
					} else {
						svces = getColValueInit(rptTable.rpt_id,
								arrField_name[col], arrData_type[col],
								Integer.toString(iCharLen),
								Integer.toString(maxCharSequence));
						iCharLen++;
						maxCharSequence++;
					}
					// 定义报表列对象
					RptColDictTable reportCol = RptColDictTable
							.genReportColFromArray(svces);
					// 增加到数组
					listRptCol.add(reportCol);
				}
				// 添加列定义信息
				try {
					rptService.insertReportCol(listRptCol);
				} catch (AppException e) {
					rptService.delReport(rptTable.rpt_id);
					result[i] += "报表ID=" + rptTable.rpt_id + "的报表添加列定义失败";
				}
			}
		} catch (Exception ex) {
			throw new Exception("读取Excel数据出错");
		}
		return result;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param aCell
	 * @return
	 * @throws Exception
	 */
	private static String getValue(HSSFCell aCell) {
		String ret = "";
		if (null != aCell) {
			int cellType = aCell.getCellType();
			switch (cellType) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				ret = Double.toString(aCell.getNumericCellValue());
				if (ret.indexOf(".") >= 0) {
					ret = ret.substring(0, ret.indexOf("."));
				}
				break;
			case HSSFCell.CELL_TYPE_STRING: // String
				ret = aCell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:// blank
				ret = aCell.getStringCellValue();
				break;
			}
		}
		return ret;
	}

	/**
	 * 初始化列定义值范围
	 * 
	 * @param rpt_id
	 * @param field_title
	 * @param data_type
	 * @param num
	 * @param sequence
	 * @return
	 */
	private static String[] getColValueInit(String rpt_id, String field_title,
			String data_type, String num, String sequence) {
		List svc = new ArrayList();

		// 报表ID
		svc.add(rpt_id);

		// 列顺序
		svc.add(sequence);

		// 是否显示
		String default_display = ReportConsts.YES;
		svc.add(default_display);

		// 是否显示编码
		String dim_code_display = ReportConsts.NO;
		svc.add(dim_code_display);

		// 是否展开列
		String is_expand_col = ReportConsts.NO;
		svc.add(is_expand_col);

		// 是否进行小计
		String is_subsum = ReportConsts.NO;
		svc.add(is_subsum);

		// 合计值是否有效
		String valuable_sum = ReportConsts.YES;
		svc.add(valuable_sum);

		// 编码字段代码
		String field_dim_code = "";
		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			field_dim_code = "DATA_INT" + num;
		}
		svc.add(field_dim_code);

		// 显示字段代码
		String field_code = "";
		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			field_code = "DATA_CHAR" + num;
		} else {
			field_code = "DATA_NUM" + num;
		}
		svc.add(field_code);

		// 字段描述
		svc.add(field_title);

		// 数据类型
		svc.add(data_type);

		// 指标精度
		String msu_length = "0";
		svc.add(msu_length);

		// 计量单位
		String msu_unit = "";
		svc.add(msu_unit);

		// 是否千分位分隔
		String comma_splitted = ReportConsts.YES;
		svc.add(comma_splitted);

		// 0 值处理
		String zero_proc = ReportConsts.ZERO_TO_ZERO;
		svc.add(zero_proc);

		// 数据转化为%
		String ratio_displayed = ReportConsts.NO;
		svc.add(ratio_displayed);

		// 是否占比
		String has_comratio = ReportConsts.NO;
		svc.add(has_comratio);

		// 是否同比
		String has_same = ReportConsts.NO;
		svc.add(has_same);

		// 是否环比
		String has_last = ReportConsts.NO;
		svc.add(has_last);

		// 是否链接
		String has_link = ReportConsts.NO;
		svc.add(has_link);

		// 链接URL
		String link_url = "";
		svc.add(link_url);

		// 链接TARGET
		String link_target = ReportConsts.TARGET_SELF;
		svc.add(link_target);

		// 是否排序
		String data_order = ReportConsts.NO;
		svc.add(data_order);

		// 短信标志
		String sms_flag = ReportConsts.NO;
		svc.add(sms_flag);

		// 文字是否换行
		String td_wrap = ReportConsts.NO;
		svc.add(td_wrap);

		// 表头样式
		String title_style = "";
		svc.add(title_style);

		// 列样式
		String col_style = "";
		svc.add(col_style);

		// 打印表头样式
		String print_title_style = "";
		svc.add(print_title_style);

		// 打印列样式
		String print_col_style = "";
		svc.add(print_col_style);

		// 是否预警
		String need_alert = ReportConsts.NO;
		svc.add(need_alert);

		// 比较对象
		String compare_to = ReportConsts.ALERT_COMPARE_TO_LAST_PERIOD;
		svc.add(compare_to);

		// 是否比率
		String ratio_compare = ReportConsts.YES;
		svc.add(ratio_compare);

		// 阈值上限
		String high_value = "0.1";
		svc.add(high_value);

		// 阈值下限
		String low_value = "-0.1";
		svc.add(low_value);

		// 预警方式
		String alert_mode = ReportConsts.ALERT_MODE_ARROW;
		svc.add(alert_mode);

		// 阈值大于上限颜色
		String rise_color = ReportConsts.ALERT_COLOR_GREEN;
		svc.add(rise_color);

		// 阈值小于下限颜色
		String down_color = ReportConsts.ALERT_COLOR_RED;
		svc.add(down_color);

		// 是否指标字段
		String is_msu = "";
		if (ReportConsts.DATA_TYPE_STRING.equals(data_type)) {
			is_msu = ReportConsts.NO;
		} else {
			is_msu = ReportConsts.YES;
		}
		svc.add(is_msu);

		// 是否衍生指标
		String is_user_msu = ReportConsts.YES;
		svc.add(is_user_msu);

		// 字段标识
		String msu_id = "";
		svc.add(msu_id);

		// 数据源标识
		String datatable = "";
		svc.add(datatable);

		// 状态
		String status = ReportConsts.YES;
		svc.add(status);

		String[] svces = null;
		if (svc != null && svc.size() >= 0) {
			svces = (String[]) svc.toArray(new String[svc.size()]);
		}
		return svces;
	}
}
