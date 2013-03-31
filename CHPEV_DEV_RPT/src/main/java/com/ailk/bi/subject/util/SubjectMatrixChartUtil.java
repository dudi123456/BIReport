package com.ailk.bi.subject.util;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.base.util.CommConditionUtil;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.subject.taglib.MatrixChartBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SubjectMatrixChartUtil {

	private Log logger = LogFactory.getLog(SubjectMatrixChartUtil.class);

	private final String qrySql = "select CHART_ID,DATA_FACT_TBL,GROUP_DIM_FLD,ARITH_MSU_X,ARITH_MSU_Y,DIGIT_LENGTH_X,DIGIT_LENGTH_Y,"
			+ "CON_RES_ID,SPLIT_CNT_X,SPLIT_CNT_Y,ANA_TABLE_ID,TABLE_WIDTH,TABLE_HEIGHT,XDESC,YDESC,YCOLDESC,FILTER_X_Y,COLOR_DEF,"
			+ "MSU_X,MSU_Y,FIELD_BAK_01,JSFUN_NAME,JSOPEN_WINNAME,CHART_NAME from UI_SUBJECT_MATRIX_CHART_DEF";

	private String chartId;
	private ReportQryStruct qryStruct;
	private HttpServletRequest request;

	public final static int CONST_SPLITX_ONE = 1;
	public final static int CONST_SPLITX_TWO = 2;
	public final static int CONST_SPLITX_THREE = 3;
	public final static int CONST_SPLITX_FOUR = 4;

	public final static int CONST_SPLITY_ONE = 1;
	public final static int CONST_SPLITY_TWO = 2;
	public final static int CONST_SPLITY_THREE = 3;
	public final static int CONST_SPLITY_FOUR = 4;

	private int xSize = 0;
	private int ySize = 0;
	private String[][] arrDesc;

	public SubjectMatrixChartUtil(String chartId, ReportQryStruct qryStruct,
			HttpServletRequest request) {
		this.chartId = chartId;
		this.qryStruct = qryStruct;
		this.request = request;
	}

	public SubjectMatrixChartUtil() {

	}

	public StringBuffer printScriptInfo() {
		StringBuffer sb = new StringBuffer();

		MatrixChartBean bean = getMatrixChartDef();

		initXYSize(bean);
		sb.append("<script for=\"d\" event=\"onclick\">\n");
		sb.append("var clickOBJ=null;\n");
		sb.append("this.style.backgroundColor=\"#ffffff\";\n");
		sb.append("if(clickOBJ!=null)\n");
		sb.append("clickOBJ.style.backgroundColor=\"\";\n");
		sb.append("clickOBJ=this;\n");
		sb.append("</script>\n");
		sb.append("<script language=\"javascript\">\n");

		if (bean.getAnaTableId().length() > 0) {// 控制分析型表格
			sb.append("function tablec(obj,tableID,index,value,isMax,isMin,desc){\n");
			sb.append("var url=\"../SubjectCommTable.rptdo?table_id=\"+tableID+\"&first=Y&table_height=450&filter_level=0&filter_indexs=\"+index+\"&filter_values=\"+value+\"&isMax=\"+isMax+\"&isMin=\"+isMin;\n");
			sb.append("var target=\"parent.table_\"+tableID;\n");
			sb.append("target=eval(target);\n");
			sb.append("target.location=url;\n");
			sb.append("document.all.desc.innerText=desc;\n");
			sb.append("setcolor();\n");
			sb.append("obj.bgColor=\"#ffffff\";\n");
			sb.append("}\n");
			sb.append("function tableori(obj,tableID,desc){\n");
			sb.append("var url=\"../SubjectCommTable.rptdo?table_id=\"+tableID+\"&first=Y&table_height=450\";\n");
			sb.append("var target=\"parent.table_\"+tableID;\n");
			sb.append("target=eval(target);\n");
			sb.append("target.location=url;\n");
			sb.append("document.all.desc.innerText=desc;\n");
			sb.append("setcolor();\n");
			sb.append("obj.bgColor=\"#ffffff\";\n");
			sb.append("}\n");
		}

		sb.append(" function setfrist(desc){\n");
		sb.append("document.all.desc.innerText=desc;\n");
		sb.append("setcolor();\n");
		int xtmp = xSize - 1;
		int ytmp = ySize - 1;
		sb.append("document.all.X" + xtmp + "Y" + ytmp
				+ ".bgColor=\"#ffffff\";\n");
		sb.append("}\n");
		sb.append("function setcolor(){\n");
		int iColor = 0;

		String colorDef[] = bean.getColorDef().split("\\;", -1);
		String arrColor[][] = getBackgroundColor();

		for (int i = 0; i < xSize; i++) {
			for (int y = 0; y < ySize; y++) {

				if (colorDef.length > 1) {// 已定义了颜色
					if (iColor >= colorDef.length) {
						if (arrColor == null) {
							sb.append("document.all.X" + i + "Y" + y
									+ ".bgColor=\"#FFFF99\"\n");
						} else {
							sb.append("document.all.X" + i + "Y" + y
									+ ".bgColor=\""
									+ StringB.NulltoBlank(arrColor[iColor][0])
									+ "\"\n");
						}
					} else {
						if (colorDef[iColor].length() == 0) {
							sb.append("document.all.X" + i + "Y" + y
									+ ".bgColor=\"#FFFF99\"\n");
						} else {
							sb.append("document.all.X" + i + "Y" + y
									+ ".bgColor=\""
									+ StringB.NulltoBlank(colorDef[iColor])
									+ "\"\n");
						}
					}

				} else {// 没有定义，从颜色表中取
					if (arrColor == null) {
						sb.append("document.all.X" + i + "Y" + y
								+ ".bgColor=\"#FFFF99\"\n");
					} else {
						sb.append("document.all.X" + i + "Y" + y
								+ ".bgColor=\""
								+ StringB.NulltoBlank(arrColor[iColor][0])
								+ "\"\n");
					}
				}

				iColor++;

			}
		}
		sb.append("}\n");
		sb.append("window.onload = function(){\n");
		sb.append("setfrist('" + arrDesc[0][ytmp] + "');\n");
		sb.append("}\n");

		sb.append("</script>");
		return sb;
	}

	/**
	 * 
	 * @return
	 * @Desc:get color define from ui_subject_chart_color_def table
	 * 
	 */
	private String[][] getBackgroundColor() {
		String sql = "select color_value from ui_subject_chart_color_def order by sort_num";
		String arr[][] = null;
		try {
			arr = WebDBUtil.execQryArray(sql, "");
		} catch (AppException e) {

			e.printStackTrace();
			return null;
		}
		return arr;

	}

	private void initXYSize(MatrixChartBean bean) {

		SplitChartOpt s_chart = getSplitChartOpt_Info(bean);
		if (s_chart != null) {
			logger.debug("s_chart:" + s_chart.getArith_msuX());
		} else {
			logger.debug("sql::--initXYSize");
		}
		String[] arrX = new String[bean.getSplitCntX()];
		switch (bean.getSplitCntX()) {
		case SubjectMatrixChartUtil.CONST_SPLITX_ONE:
			arrX[0] = qryStruct.splitx_one;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITX_TWO:
			arrX[0] = qryStruct.splitx_one;
			arrX[1] = qryStruct.splitx_two;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITX_THREE:
			arrX[0] = qryStruct.splitx_one;
			arrX[1] = qryStruct.splitx_two;
			arrX[2] = qryStruct.splitx_three;

			break;
		case SubjectMatrixChartUtil.CONST_SPLITX_FOUR:
			arrX[0] = qryStruct.splitx_one;
			arrX[1] = qryStruct.splitx_two;
			arrX[2] = qryStruct.splitx_three;
			arrX[3] = qryStruct.splitx_four;

			break;

		}

		String[] arrY = new String[bean.getSplitCntY()];
		switch (bean.getSplitCntX()) {
		case SubjectMatrixChartUtil.CONST_SPLITY_ONE:
			arrY[0] = qryStruct.splity_one;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITY_TWO:
			arrY[0] = qryStruct.splity_one;
			arrY[1] = qryStruct.splity_two;
			break;
		case SubjectMatrixChartUtil.CONST_SPLITY_THREE:
			arrY[0] = qryStruct.splity_one;
			arrY[1] = qryStruct.splity_two;
			arrY[2] = qryStruct.splity_three;

			break;
		case SubjectMatrixChartUtil.CONST_SPLITY_FOUR:
			arrY[0] = qryStruct.splity_one;
			arrY[1] = qryStruct.splity_two;
			arrY[2] = qryStruct.splity_three;
			arrY[3] = qryStruct.splity_four;

			break;
		}

		String[] RatesX = s_chart.getRate(arrX, "per1");
		String[] RatesY = s_chart.getRate(arrY, "per2");
		this.xSize = RatesX.length;
		this.ySize = RatesY.length;
		arrDesc = s_chart.getRateValueArr(RatesX, RatesY, bean.getChartName()
				+ "----" + bean.getXDesc() + "：", " " + bean.getYDesc() + "：");

	}

	public SplitChartOpt getSplitChartOpt_Info(MatrixChartBean bean) {

		// MatrixChartBean bean = getMatrixChartDef();

		SplitChartOpt s_chart = new SplitChartOpt(bean.getDataFactTbl());
		s_chart.setCountMsu(bean.getGroupDimField());

		s_chart.setArith_msuX(bean.getArithMsuX());
		s_chart.setArith_msuY(bean.getArithMsuY());

		// s_chart.setCalX(true);
		// s_chart.setCalY(true);
		s_chart.setDigit_xlength(bean.getDigitLenX());
		s_chart.setDigit_ylength(bean.getDigitLenY());
		s_chart.setWhereSql(" where 1=1 "
				+ CommConditionUtil.getPubWhere(bean.getConResId(), request,
						qryStruct));

		return s_chart;
	}

	/**
	 * 
	 * @return
	 * @Desc:根据CHART_ID从UI_SUBJECT_MATRIX_CHART_DEF表查询矩阵图形的定义
	 * 
	 */
	public MatrixChartBean getMatrixChartDef() {
		MatrixChartBean bean = null;
		try {
			bean = new MatrixChartBean();
			String sql = qrySql + " where chart_id='" + chartId + "'";

			String arr[][] = WebDBUtil.execQryArray(sql, "");
			if (arr != null && arr.length > 0) {
				bean.setChartId(chartId);
				bean.setDataFactTbl(StringB.NulltoBlank(arr[0][1]));
				bean.setGroupDimField(StringB.NulltoBlank(arr[0][2]));
				bean.setArithMsuX(StringB.NulltoBlank(arr[0][3]));
				bean.setArithMsuY(StringB.NulltoBlank(arr[0][4]));
				if (StringB.NulltoBlank(arr[0][5]).length() == 0) {
					bean.setDigitLenX(0);
				} else {
					bean.setDigitLenX(Integer.parseInt(arr[0][5]));
				}

				if (StringB.NulltoBlank(arr[0][6]).length() == 0) {
					bean.setDigitLenY(0);
				} else {
					bean.setDigitLenY(Integer.parseInt(arr[0][6]));
				}

				bean.setConResId(StringB.NulltoBlank(arr[0][7]));

				if (StringB.NulltoBlank(arr[0][8]).length() == 0) {
					bean.setSplitCntX(1);
				} else {
					bean.setSplitCntX(Integer.parseInt(arr[0][8]));
				}

				if (StringB.NulltoBlank(arr[0][9]).length() == 0) {
					bean.setSplitCntY(1);
				} else {
					bean.setSplitCntY(Integer.parseInt(arr[0][9]));
				}

				bean.setAnaTableId(StringB.NulltoBlank(arr[0][10]));

				if (StringB.NulltoBlank(arr[0][11]).length() == 0) {
					bean.setTableWidth(150);
				} else {
					bean.setTableWidth(Integer.parseInt(arr[0][11]));
				}
				if (StringB.NulltoBlank(arr[0][12]).length() == 0) {
					bean.setTableHeight(60);
				} else {
					bean.setTableHeight(Integer.parseInt(arr[0][12]));
				}

				bean.setXDesc(StringB.NulltoBlank(arr[0][13]));
				bean.setYDesc(StringB.NulltoBlank(arr[0][14]));
				bean.setYColDesc(StringB.NulltoBlank(arr[0][15]));
				bean.setFilterXY(StringB.NulltoBlank(arr[0][16]));
				bean.setColorDef(StringB.NulltoBlank(arr[0][17]));
				bean.setMsuX(StringB.NulltoBlank(arr[0][18]));
				bean.setMsuY(StringB.NulltoBlank(arr[0][19]));
				bean.setFieldBak01(StringB.NulltoBlank(arr[0][20]));
				bean.setJsFunName(StringB.NulltoBlank(arr[0][21]));
				bean.setJsOpenWinName(StringB.NulltoBlank(arr[0][22]));
				bean.setChartName(StringB.NulltoBlank(arr[0][23]));

			} else {
				return null;
			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return bean;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getChartId() {
		return chartId;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	public ReportQryStruct getQryStruct() {
		return qryStruct;
	}

	public void setQryStruct(ReportQryStruct qryStruct) {
		this.qryStruct = qryStruct;
	}
}
