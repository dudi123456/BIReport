package com.ailk.bi.sigma;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.DateUtil;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;

@SuppressWarnings({ "unused", "rawtypes" })
public class SigmaGridUtil {

	private int sigmaId;
	private SigmaGridEntityBean sigmaBean;

	private List<SigmaGridConditionBean> conditonBean;

	public List<SigmaGridConditionBean> getConditonBean() {
		return conditonBean;
	}

	public void setConditonBean(List<SigmaGridConditionBean> conditonBean) {
		this.conditonBean = conditonBean;
	}

	private String root_path;

	public String getRoot_path() {
		return root_path;
	}

	public void setRoot_path(String rootPath) {
		root_path = rootPath;
	}

	public SigmaGridEntityBean getSigmaReportInfo(int sigmaId) {
		SigmaGridEntityBean bean = new SigmaGridEntityBean();
		String sql = "select sigma_name,sigma_desc from ui_sigma_define_base where sigma_id="
				+ sigmaId;
		try {
			String[][] res = WebDBUtil.execQryArray(sql);
			if (res != null && res.length > 0) {
				bean.setReportName(StringB.NulltoBlank(res[0][0]));
				bean.setReportDesc(StringB.NulltoBlank(res[0][1]));

			}
		} catch (AppException e) {

			e.printStackTrace();
		}

		return bean;
	}

	public SigmaGridUtil(int sigmaId) {
		this.sigmaId = sigmaId;
	}

	public SigmaGridUtil(int sigmaId, String rootPath) {

		this.sigmaId = sigmaId;
		this.root_path = rootPath;
	}

	public void initBeanInfo() {
		getSigmaBeanInfo();

	}

	public void initConditonInfo() {

		this.conditonBean = getSigmaGridConditionInfo();

	}

	private SigmaGridEntityBean getSigmaBeanInfo() {
		sigmaBean = new SigmaGridEntityBean();
		String sqlFld = "t.SIGMA_ID,t.SIGMA_NAME,t.SIGMA_DESC,t.SIGMA_WIDTH,t.SIGMA_HEIGHT,t.LOAD_URL,t.SAVE_URL,t.TBAR_POS,t.TBAR_CONTENT,t.STRIPE_ROW,t.SHOW_GDMENU,t.ALLOW_CT_SKIN,t.ALLOW_FREEZE,t.ALLOW_GRP,t.ALLOW_HIDE,t.PAGE_SIZE,"
				+ "t.PAGE_SIZELIST,t.SQL_DEFINE,t.SQL_CONDITION,t.SQL_ORDERBY,t.REMOTE_PAGE,t.CON_PER_ROW,t.HAS_COMPX_HEADER,t.COMPLX_HEADSTR,";

		String sql = "select "
				+ sqlFld
				+ "b.ONCELLCLK,b.ONCELLDBLCLK,b.ONROWCLK,b.ONROWDBLCLK,b.CSTM_ROW_ATTR,b.SHOW_INDX_COLNUM,b.ON_MOUSE_OVER,ON_MOUSE_OUT from ui_sigma_define_base t left outer join UI_SIGMA_DEFINE_BASE_EXT b on t.sigma_id=b.sigma_id where t.sigma_id="
				+ this.sigmaId;
		sigmaBean.setGrid_demo_id(SigmaGridConstant.GRID_NAME + this.sigmaId);
		sigmaBean.setVar_grid_demo_id("var " + SigmaGridConstant.GRID_NAME
				+ this.sigmaId + "=\"" + sigmaBean.getGrid_demo_id() + "\"");
		sigmaBean.setGridContainerBox(SigmaGridConstant.GRID_RENDER_BOX
				+ sigmaId);

		try {

			String sqlCol = "select * from ui_sigma_column_define t where  is_show=1 and sigma_id="
					+ this.sigmaId + " order by sort_num";
			String[][] resCol = WebDBUtil.execQryArray(sqlCol, "");

			StringBuffer sbFields = new StringBuffer();
			StringBuffer sbCols = new StringBuffer();
			StringBuffer sbColsJs = new StringBuffer();

			if (resCol != null && resCol.length > 0) {
				for (int i = 0; i < resCol.length - 1; i++) {
					sbFields.append("{name : '"
							+ StringB.NulltoBlank(resCol[i][2].toLowerCase())
							+ "'");
					sbCols.append("{id : '"
							+ StringB.NulltoBlank(resCol[i][2].toLowerCase())
							+ "',header:\"" + StringB.NulltoBlank(resCol[i][3])
							+ "\"");
					String width = StringB.NulltoBlank(StringB
							.NulltoBlank(resCol[i][7]));
					if (width.length() > 0) {
						sbCols.append(",width:'" + width + "'");

					}

					String type = StringB.NulltoBlank(resCol[i][4]);
					if (type.length() > 0) {
						sbFields.append(",type: '" + type + "'");

					}

					type = StringB.NulltoBlank(resCol[i][9]);
					if (type.length() > 0) {
						sbCols.append(",toolTip:" + type);

					}

					type = StringB.NulltoBlank(resCol[i][10]);
					if (type.length() > 0) {
						sbCols.append(",toolTipWidth:'" + type + "'");

					}

					type = StringB.NulltoBlank(resCol[i][14]);
					if (type.length() > 0) {
						sbCols.append(",frozen:" + type);

					}

					type = StringB.NulltoBlank(resCol[i][15]);
					if (type.length() > 0) {
						sbCols.append(",grouped:" + type);

					}

					type = StringB.NulltoBlank(resCol[i][16]);
					if (type.length() > 0) {
						sbCols.append(",renderer:" + type);

					}

					type = StringB.NulltoBlank(resCol[i][17]);
					if (type.length() > 0) {
						sbCols.append(",headAlign:'" + type + "'");

					}

					type = StringB.NulltoBlank(resCol[i][18]);
					if (type.length() > 0) {
						sbCols.append(",align:'" + type + "'");

					}

					type = StringB.NulltoBlank(resCol[i][19]);
					if (type.length() > 0) {
						sbCols.append(",emptyText:'" + type + "'");

					}

					type = StringB.NulltoBlank(resCol[i][20]);
					if (type.length() > 0) {
						sbCols.append(",sortable:" + type);

					}

					type = StringB.NulltoBlank(resCol[i][13]);
					if (type.length() > 0) {
						sbColsJs.append(type);

					}

					sbFields.append("},\n");
					sbCols.append("},\n");
					sbColsJs.append("\n");

				}

				int lastRow = resCol.length - 1;

				sbFields.append("{name : '"
						+ StringB.NulltoBlank(resCol[lastRow][2].toLowerCase())
						+ "'");
				sbCols.append("{id : '"
						+ StringB.NulltoBlank(resCol[lastRow][2].toLowerCase())
						+ "',header:\""
						+ StringB.NulltoBlank(resCol[lastRow][3]) + "\"");
				String width = StringB.NulltoBlank(resCol[lastRow][7]);
				if (width.length() > 0) {
					sbCols.append(",width:'" + width + "'");

				}

				String type = StringB.NulltoBlank(resCol[lastRow][4]);
				if (type.length() > 0) {
					sbFields.append(",type: '" + type + "'");

				}

				type = StringB.NulltoBlank(resCol[lastRow][9]);
				if (type.length() > 0) {
					sbCols.append(",toolTip:" + type);

				}

				type = StringB.NulltoBlank(resCol[lastRow][10]);
				if (type.length() > 0) {
					sbCols.append(",toolTipWidth:'" + type + "'");

				}

				type = StringB.NulltoBlank(resCol[lastRow][14]);
				if (type.length() > 0) {
					sbCols.append(",frozen:" + type);

				}

				type = StringB.NulltoBlank(resCol[lastRow][15]);
				if (type.length() > 0) {
					sbCols.append(",grouped:" + type);

				}

				type = StringB.NulltoBlank(resCol[lastRow][16]);
				if (type.length() > 0) {
					sbCols.append(",renderer:" + type);

				}

				type = StringB.NulltoBlank(resCol[lastRow][17]);
				if (type.length() > 0) {
					sbCols.append(",headAlign:'" + type + "'");

				}

				type = StringB.NulltoBlank(resCol[lastRow][18]);
				if (type.length() > 0) {
					sbCols.append(",align:'" + type + "'");

				}

				type = StringB.NulltoBlank(resCol[lastRow][19]);
				if (type.length() > 0) {
					sbCols.append(",emptyText:'" + type + "'");

				}

				type = StringB.NulltoBlank(resCol[lastRow][20]);
				if (type.length() > 0) {
					sbCols.append(",sortable:" + type);

				}

				type = StringB.NulltoBlank(resCol[lastRow][13]);
				if (type.length() > 0) {
					sbColsJs.append(type);

				}

				sbFields.append("}\n");
				sbCols.append("}\n");

				sigmaBean.setColJs(sbColsJs);

				//
				StringBuffer bf = new StringBuffer();
				bf.append("var " + SigmaGridConstant.GRID_COLS_OPTION + sigmaId
						+ "= [\n" + sbCols.toString() + "];\n");
				sigmaBean.setColsOption(bf);

				bf = new StringBuffer();

				bf.append("var " + SigmaGridConstant.GRID_DS_OPTION + sigmaId
						+ "= {\n fields :[\n" + sbFields.toString() + "]\n};\n");
				sigmaBean.setDsOption(bf);

				StringBuffer gridOption = new StringBuffer();
				gridOption.append("var " + SigmaGridConstant.GRID_GRID_OPTION
						+ sigmaId + "={\n");
				gridOption.append(" id:" + sigmaBean.getGrid_demo_id() + ",\n");

				String[][] res = WebDBUtil.execQryArray(sql, "");
				if (res != null && res.length > 0) {
					String strWidth = StringB.NulltoBlank(res[0][3]);
					if (strWidth.length() > 0) {
						gridOption.append(" width:\"" + strWidth + "\",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][4]);
					if (strWidth.length() > 0) {
						gridOption.append(" height:\"" + strWidth + "\",\n");
					}

					strWidth = SigmaGridConstant.GRID_RENDER_BOX + sigmaId;
					gridOption.append(" container:\"" + strWidth + "\",\n");

					// strWidth = StringB.NulltoBlank(res[0][5]);
					// strWidth = this.root_path + "/sigmaShow?sigmaId=" +
					// this.sigmaId;

					strWidth = this.root_path
							+ "/jsp/ctrl_showdata.jsp?sigmaId=" + this.sigmaId;

					if (strWidth.length() > 0) {
						gridOption.append(" loadURL:\"" + strWidth + "\",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][6]);
					if (strWidth.length() > 0) {
						gridOption.append(" saveURL:\"" + strWidth + "\",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][7]);
					if (strWidth.length() > 0) {
						gridOption.append(" toolbarPosition:\"" + strWidth
								+ "\",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][8]);
					if (strWidth.length() > 0) {
						gridOption.append(" toolbarContent:\"" + strWidth
								+ "\",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][9]);
					if (strWidth.length() > 0) {
						gridOption.append(" stripeRows:" + strWidth + ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][10]);
					if (strWidth.length() > 0) {
						gridOption.append(" showGridMenu:" + strWidth + ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][11]);
					if (strWidth.length() > 0) {
						gridOption.append(" allowCustomSkin:" + strWidth
								+ ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][12]);
					if (strWidth.length() > 0) {
						gridOption.append(" allowFreeze:" + strWidth + ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][13]);
					if (strWidth.length() > 0) {
						gridOption.append(" allowGroup:" + strWidth + ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][14]);
					if (strWidth.length() > 0) {
						gridOption.append(" allowHide:" + strWidth + ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][15]);
					if (strWidth.length() > 0) {
						gridOption.append(" pageSize:" + strWidth + ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][16]);
					if (strWidth.length() > 0) {
						gridOption
								.append(" pageSizeList:[" + strWidth + "],\n");
					}

					strWidth = StringB.NulltoBlank(res[0][20]);
					if (strWidth.length() > 0) {
						gridOption.append(" remotePaging:" + strWidth + ",\n");
					}

					// 复杂表头
					strWidth = StringB.NulltoBlank(res[0][22]);
					if (strWidth.equals("1")) {
						String strHeader = StringB.NulltoBlank(res[0][23]);
						gridOption.append(" customHead:'"
								+ SigmaGridConstant.GRID_COMPLX_HEADER
								+ this.sigmaId + "',\n");

					}

					// 单元格单击事件
					strWidth = StringB.NulltoBlank(res[0][24]);
					if (strWidth.length() > 0) {
						gridOption.append(" onCellClick:" + strWidth + ",\n");
					}

					// 单元格双击事件
					strWidth = StringB.NulltoBlank(res[0][25]);
					if (strWidth.length() > 0) {
						gridOption
								.append(" onCellDblClick:" + strWidth + ",\n");
					}

					// 行单击事件
					strWidth = StringB.NulltoBlank(res[0][26]);
					if (strWidth.length() > 0) {
						gridOption.append(" onRowClick:" + strWidth + ",\n");
					}

					// 行双击事件
					strWidth = StringB.NulltoBlank(res[0][27]);
					if (strWidth.length() > 0) {
						gridOption.append(" onRowDblClick:" + strWidth + ",\n");
					}

					//
					strWidth = StringB.NulltoBlank(res[0][28]);
					if (strWidth.length() > 0) {
						gridOption.append(" customRowAttribute:" + strWidth
								+ ",\n");
					}

					//
					strWidth = StringB.NulltoBlank(res[0][29]);
					if (strWidth.length() > 0) {
						gridOption.append(" showIndexColumn:" + strWidth
								+ ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][30]);
					if (strWidth.length() > 0) {
						gridOption.append(" onMouseOver:" + strWidth + ",\n");
					}

					strWidth = StringB.NulltoBlank(res[0][31]);
					if (strWidth.length() > 0) {
						gridOption.append(" onMouseOut:" + strWidth + ",\n");
					}

					strWidth = SigmaGridConstant.GRID_DS_OPTION + sigmaId;
					gridOption.append(" dataset:" + strWidth + ",\n");

					strWidth = SigmaGridConstant.GRID_COLS_OPTION + sigmaId;
					gridOption.append(" columns:" + strWidth + "\n");

					gridOption.append("};\n");

					sigmaBean.setGridOption(gridOption);
				}

			}

		} catch (AppException e) {

			e.printStackTrace();
		}
		return sigmaBean;
	}

	public StringBuffer showSigmaScriptLanguage() {
		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\" >\n");
		sb.append(sigmaBean.getVar_grid_demo_id() + ";\n");

		sb.append(" \n");

		sb.append(sigmaBean.getDsOption().toString());

		sb.append(" \n");

		sb.append(sigmaBean.getColsOption().toString());

		sb.append(" \n");

		sb.append(sigmaBean.getGridOption().toString());

		sb.append(" \n");

		sb.append("var " + SigmaGridConstant.GRID_VAR_NAME + sigmaId
				+ "=new Sigma.Grid( " + SigmaGridConstant.GRID_GRID_OPTION
				+ sigmaId + " );\n");

		/*
		 * sb.append("Sigma.Utils.onLoad( function(){\n"); sb.append("	mygrid_"
		 * + sigmaId + ".render();\n"); //sb.append("	mygrid_" + sigmaId +
		 * ".reload();\n"); sb.append("});\n");
		 */

		// sb.append("Sigma.Util.onLoad( Sigma.Grid.render(mygrid_" + sigmaId +
		// ") );\n");

		sb.append(" \n");

		sb.append(sigmaBean.getColJs().toString());

		sb.append(" \n");

		sb.append("</script> \n");
		return sb;

	}

	public StringBuffer sigmaDsOption() {

		return null;
	}

	public StringBuffer sigmaColsOption() {
		return null;
	}

	public StringBuffer sigmaGridConfig() {

		return null;
	}

	public int getSigmaId() {
		return sigmaId;
	}

	public void setSigmaId(int sigmaId) {
		this.sigmaId = sigmaId;
	}

	public SigmaGridEntityBean getSigmaBean() {
		return sigmaBean;
	}

	public void setSigmaBean(SigmaGridEntityBean sigmaBean) {
		this.sigmaBean = sigmaBean;
	}

	/**
	 * 
	 * @return
	 * @desc:获取隐藏的condition；
	 * 
	 */
	public List<SigmaGridConditionShowBean> getSigmaGridHiddenCondition() {
		List<SigmaGridConditionShowBean> conditonAll = this
				.getSigmaGridConditon();

		List<SigmaGridConditionShowBean> listRtn = new ArrayList<SigmaGridConditionShowBean>();

		for (int i = 0; i < conditonAll.size(); i++) {
			int iHidden = conditonAll.get(i).getShowHidden();
			if (iHidden == 0) {
				listRtn.add(conditonAll.get(i));
			}
		}
		return listRtn;
	}

	/**
	 * 
	 * @return
	 * @desc:获取显示的condition；
	 * 
	 */

	public List<SigmaGridConditionShowBean> getSigmaGridDisplayCondition() {
		List<SigmaGridConditionShowBean> conditonAll = this
				.getSigmaGridConditon();

		List<SigmaGridConditionShowBean> listRtn = new ArrayList<SigmaGridConditionShowBean>();

		for (int i = 0; i < conditonAll.size(); i++) {
			int iHidden = conditonAll.get(i).getShowHidden();
			if (iHidden == 1) {
				listRtn.add(conditonAll.get(i));
			}
		}
		return listRtn;
	}

	private List<SigmaGridConditionShowBean> getSigmaGridConditon() {
		List<SigmaGridConditionBean> list = null;

		if (conditonBean == null) {
			list = getSigmaGridConditionInfo();
		} else {
			list = this.conditonBean;
		}

		// List<SigmaGridConditionBean> list = this.conditonBean;

		List<SigmaGridConditionShowBean> listRtn = new ArrayList<SigmaGridConditionShowBean>();

		for (int i = 0; i < list.size(); i++) {
			SigmaGridConditionShowBean showBean = new SigmaGridConditionShowBean();
			SigmaGridConditionBean bean = list.get(i);
			int conType = bean.getConType();

			String conditionId = "_" + bean.getSigmaConId() + "_"
					+ bean.getSigmaId() + "_" + bean.getConId();
			String type = "";
			int showHid = bean.getShowHidden();
			showBean.setShowHidden(showHid);

			showBean.setConTag(bean.getConTag());
			showBean.setDataType(bean.getDataType());
			showBean.setConRange(bean.getConRange());
			showBean.setFieldName(bean.getFieldName());
			showBean.setIsNull(bean.getIsNull());
			showBean.setPassParam(bean.getPassParam());
			showBean.setPassType(bean.getPassType());

			switch (showHid) {
			case 0:
				type = "hidden";
				break;
			case 1:
				type = "text";
				break;
			}

			String defaultVal = "";
			String strTxt = "";
			String rConName = "";

			switch (showBean.getConRange()) {
			case 1:
				// 单条件
				rConName = SigmaGridConstant.GRID_CONDTION_TEXT + conditionId;
				showBean.setShowConName(bean.getConName());
				showBean.setReqConName(rConName);

				switch (conType) {
				case 1:
					defaultVal = bean.getDefaultValue();

					strTxt = "<input style='"
							+ SigmaGridConstant.GRID_TXT_CLASS + "' value='"
							+ defaultVal + "' type='" + type + "' name='"
							+ rConName + "'>";

					break;
				case 2:
					break;
				case 7:
					String dataFormat = bean.getDataFormat();
					defaultVal = bean.getDefaultValue();
					String value = "";
					if (bean.getIsNull() == 1) {
						if (defaultVal.length() == 0) {

							value = DateUtil.getDiffDay(0,
									DateUtil.getNowDate(), dataFormat);
						} else {
							value = DateUtil.getDiffDay(
									Integer.parseInt(defaultVal),
									DateUtil.getNowDate(), dataFormat);
						}
					}

					strTxt = "<input style='"
							+ SigmaGridConstant.GRID_TXT_CLASS1 + "' value='"
							+ value + "' type='" + type + "' name='" + rConName
							+ "' readonly onClick=\"scwShow(this,this);\">";

					break;

				case 8:
					dataFormat = bean.getDataFormat();
					defaultVal = bean.getDefaultValue();
					value = "";
					if (bean.getIsNull() == 1) {
						if (defaultVal.length() == 0) {
							value = DateUtil.getDiffMonth(0,
									DateUtil.getNowDate(), dataFormat);
						} else {
							value = DateUtil.getDiffMonth(
									Integer.parseInt(defaultVal),
									DateUtil.getNowDate(), dataFormat);
						}

					}

					strTxt = "<input style='"
							+ SigmaGridConstant.GRID_TXT_CLASS + "' value='"
							+ value + "' type='" + type + "' name='" + rConName
							+ "' readonly onClick=\"scwShowM(this,this);\">";

					break;

				case 11:
					// 下拉框形式
					String showAll = bean.getShowAll();
					String choseType = bean.getChoseType();
					String pConId = bean.getParentConId();
					String hasChild = bean.getHasChild();

					if (choseType.equals("0")) {
						// 单值选择
						if (pConId.equals("0")) {
							// 父节点情况
							String onChange = " ";
							if (hasChild.equals("1")) {
								onChange = "javascript:update2();";

							}

							strTxt = "<SELECT NAME='" + rConName + "' id='"
									+ rConName + "' style='"
									+ SigmaGridConstant.GRID_SELECT_CLASS
									+ "' " + onChange + ">";

							if (showAll.equals("1")) {
								strTxt += "<OPTION VALUE=''>全部</OPTION>";
							}

							strTxt += this.qryConditonSelect(bean);
							strTxt += "</SELECT>";

						} else {
							// 子节点情况

						}

					} else {

					}

					break;
				}

				showBean.setShowHtmlString(strTxt);

				listRtn.add(showBean);

				break;

			case 2:
				// 条件区间

				rConName = SigmaGridConstant.GRID_CONDTION_TEXT + conditionId;
				String conTag[] = bean.getConTag().split("\\|");

				for (int k = 0; k < 2; k++) {

					SigmaGridConditionShowBean showBean_New = new SigmaGridConditionShowBean();
					showBean_New.setShowHidden(showHid);
					showBean_New.setConRange(bean.getConRange());
					showBean_New.setConTag(conTag[k]);
					showBean_New.setDataType(bean.getDataType());
					showBean_New.setFieldName(bean.getFieldName());
					showBean_New.setIsNull(bean.getIsNull());

					if (k == 0) {
						showBean_New.setShowConName(bean.getConName());
						showBean_New.setReqConName(rConName + "_A");
					} else if (k == 1) {
						showBean_New.setShowConName("至");
						showBean_New.setReqConName(rConName + "_B");
					}

					switch (conType) {
					case 1:
						defaultVal = bean.getDefaultValue();

						strTxt = "<input style='"
								+ SigmaGridConstant.GRID_TXT_CLASS
								+ "' value='" + defaultVal + "' type='" + type
								+ "' name='" + showBean_New.getReqConName()
								+ "'>";

						break;
					case 2:
						break;
					case 7:

						String dataFormat = bean.getDataFormat();
						defaultVal = bean.getDefaultValue();
						String value = "";
						if (bean.getIsNull() == 1) {
							if (defaultVal.length() == 0) {
								value = DateUtil.getDiffDay(0,
										DateUtil.getNowDate(), dataFormat);
							} else {
								value = DateUtil.getDiffDay(
										Integer.parseInt(defaultVal),
										DateUtil.getNowDate(), dataFormat);
							}
						}

						strTxt = "<input style='"
								+ SigmaGridConstant.GRID_TXT_CLASS1
								+ "' value='" + value + "' type='" + type
								+ "' name='" + showBean_New.getReqConName()
								+ "' readonly onClick=\"scwShow(this,this);\">";

						break;

					case 8:

						dataFormat = bean.getDataFormat();
						defaultVal = bean.getDefaultValue();
						value = "";
						if (bean.getIsNull() == 1) {
							if (defaultVal.length() == 0) {
								value = DateUtil.getDiffMonth(0,
										DateUtil.getNowDate(), dataFormat);
							} else {
								value = DateUtil.getDiffMonth(
										Integer.parseInt(defaultVal),
										DateUtil.getNowDate(), dataFormat);
							}
						}

						strTxt = "<input style='"
								+ SigmaGridConstant.GRID_TXT_CLASS1
								+ "' value='"
								+ value
								+ "' type='"
								+ type
								+ "' name='"
								+ showBean_New.getReqConName()
								+ "' readonly onClick=\"scwShowM(this,this);\">";

						break;

					}

					showBean_New.setShowHtmlString(strTxt);

					listRtn.add(showBean_New);

				}

				break;
			}

		}

		return listRtn;
	}

	private List<SigmaGridConditionBean> getSigmaGridConditionInfo() {

		List<SigmaGridConditionBean> listRtn = new ArrayList<SigmaGridConditionBean>();

		String sql = "select sigma_con_id,sigma_id,con_id,con_name,con_desc,sort_num,field_name,con_tag,data_type,show_hide,con_type"
				+ ",src_con_name,src_con_desc,con_rule,where_sql,order_sql,group_sql,parent_conid"
				+ ",parent_fld,src_data_type,data_format,default_value,con_range,is_null,SHOW_ALL,CHOSE_TYPE,HAS_CHILD,PASS_PARAM,PASS_TYPE from V_SIGMA_REPORT_CONDITION where sigma_id="
				+ this.sigmaId + " order by SORT_NUM";

		System.out.println("a:" + sql);
		try {
			String[][] res = WebDBUtil.execQryArray(sql, "");
			if (res != null && res.length > 0) {
				for (int i = 0; i < res.length; i++) {
					SigmaGridConditionBean bean = new SigmaGridConditionBean();
					bean.setSigmaConId(res[i][0]);
					bean.setSigmaId(res[i][1]);
					bean.setConId(StringB.NulltoBlank(res[i][2]));
					bean.setConName(StringB.NulltoBlank(res[i][3]));
					bean.setConDesc(StringB.NulltoBlank(res[i][4]));
					bean.setSortNum(StringB.NulltoBlank(res[i][5]));
					bean.setFieldName(StringB.NulltoBlank(res[i][6]));
					bean.setConTag(StringB.NulltoBlank(res[i][7]));
					bean.setDataType(Integer.parseInt(StringB
							.NulltoBlank(res[i][8])));
					bean.setShowHidden(Integer.parseInt(StringB
							.NulltoBlank(res[i][9])));
					bean.setConType(Integer.parseInt(StringB
							.NulltoBlank(res[i][10])));
					bean.setSrcConName(StringB.NulltoBlank(res[i][11]));
					bean.setSrcConDesc(StringB.NulltoBlank(res[i][12]));
					bean.setSelectSql(StringB.NulltoBlank(res[i][13]));
					bean.setWhereSql(StringB.NulltoBlank(res[i][14]));
					bean.setOrderSql(StringB.NulltoBlank(res[i][15]));
					bean.setGroupSql(StringB.NulltoBlank(res[i][16]));
					bean.setParentConId(StringB.NulltoBlank(res[i][17]));
					bean.setParentFld(StringB.NulltoBlank(res[i][18]));
					bean.setSrcDataType(StringB.NulltoBlank(res[i][19]));
					bean.setDataFormat(StringB.NulltoBlank(res[i][20]));
					bean.setDefaultValue(StringB.NulltoBlank(res[i][21]));
					bean.setConRange(Integer.parseInt(StringB
							.NulltoBlank(res[i][22])));
					bean.setIsNull(Integer.parseInt(StringB
							.NulltoBlank(res[i][23])));

					bean.setShowAll(StringB.NulltoBlank(res[i][24]));
					bean.setChoseType(StringB.NulltoBlank(res[i][25]));
					bean.setHasChild(StringB.NulltoBlank(res[i][26]));

					bean.setPassParam(StringB.NulltoBlank(res[i][27]));
					bean.setPassType(Integer.parseInt(StringB
							.NulltoBlank(res[i][28])));

					listRtn.add(bean);
				}

			}

		} catch (AppException e) {

			e.printStackTrace();
		}

		return listRtn;
	}

	public StringBuffer showParamsRander(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script type=\"text/javascript\" >\n");

		List<SigmaGridConditionShowBean> listBean = getSigmaGridConditon();

		if (listBean != null && listBean.size() > 0) {
			// System.out.println("testtest1");
			String params = " var " + SigmaGridConstant.GRID_PARAM_NAME
					+ sigmaId + " ={\n";

			sb.append(" " + SigmaGridConstant.GRID_VAR_NAME + sigmaId
					+ ".cleanParameters();\n");
			for (SigmaGridConditionShowBean bean : listBean) {
				params += bean.getReqConName()
						+ ":\""
						+ StringB.NulltoBlank(request.getParameter(bean
								.getReqConName())) + "\",";
			}
			params = params.substring(0, params.length() - 1);
			params += "};\n";

			sb.append(params);

			sb.append("	" + SigmaGridConstant.GRID_VAR_NAME + sigmaId
					+ ".setParameters(" + SigmaGridConstant.GRID_PARAM_NAME
					+ sigmaId + ");\n");

		}

		sb.append("Sigma.Utils.onLoad( function(){\n");
		sb.append("	" + SigmaGridConstant.GRID_VAR_NAME + sigmaId
				+ ".render();\n");
		// sb.append("	mygrid_" + sigmaId + ".reload();\n");
		sb.append("});\n");

		sb.append("</script> \n");
		return sb;
	}

	public String getConditonSql(Map map, HttpServletRequest request) {
		List<SigmaGridConditionShowBean> list = getSigmaGridConditon();

		String sqlCon = "";
		String strCon = "";
		SigmaGridReportCondBean condBean = new SigmaGridReportCondBean();

		for (SigmaGridConditionShowBean bean : list) {
			String conditionId = bean.getReqConName();

			String conVal = StringB.NulltoBlank((String) map.get(conditionId));
			if (conVal.length() != 0) {
				sqlCon += " and " + bean.getFieldName() + bean.getConTag();
				if (strCon.length() == 0) {
					strCon = conVal;
				} else {
					strCon += "_" + conVal;
				}

				if (bean.getDataType() == 2) {
					// 文本
					sqlCon += "'" + conVal + "'";
				} else if (bean.getDataType() == 1) {
					// 数字
					sqlCon += conVal;
				}
			}
		}
		sqlCon += " ";

		condBean.setSigmaId(sigmaId);
		condBean.setSigmaConSql(sqlCon);
		condBean.setSigmaConValue(strCon);

		request.getSession().setAttribute(
				SigmaGridConstant.GRID_CONDITION_SESSION + sigmaId, condBean);

		return sqlCon;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 * @desc:根据条件定义中的select类型，得出option值
	 * 
	 */

	private String qryConditonSelect(SigmaGridConditionBean bean) {

		String sql = bean.getSelectSql() + " " + bean.getWhereSql() + " "
				+ bean.getGroupSql() + " " + bean.getOrderSql();
		String retn = "";
		try {
			String[][] res = WebDBUtil.execQryArray(sql);
			String defaultVal = bean.getDefaultValue();

			if (res != null && res.length > 0) {
				for (int i = 0; i < res.length; i++) {
					String id = StringB.NulltoBlank(res[i][0]);
					String strSelect = "";
					if (defaultVal.length() > 0) {
						if (defaultVal.equals(id)) {
							strSelect = " SELECTED";
						}
					}
					retn += "<OPTION VALUE='" + id + "'" + strSelect + ">"
							+ res[i][1] + "</OPTION>\n";

				}
			}

		} catch (AppException e) {

			e.printStackTrace();
		}

		return retn;

	}

	/**
	 * 获取报表信息
	 * 
	 * @return
	 */
	public static String reportBottom(String rpt_id) {
		String info = "";
		String infosql = "select top_comments from ui_rpt_info_comment t WHERE rpt_id='"
				+ rpt_id + "'";
		try {
			info = WebDBUtil.getSingleValue(infosql);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return info;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xx = "sdfds,sdfds,";
		System.out.println(xx.substring(0, xx.length() - 1));
		SigmaGridUtil util = new SigmaGridUtil(1);
		List<SigmaGridConditionShowBean> list = util
				.getSigmaGridHiddenCondition();
		// System.out.println(list.size());
		List<SigmaPassParamBean> listParam = new ArrayList<SigmaPassParamBean>();

		for (SigmaGridConditionShowBean bean : list) {
			// StringBuffer sb = list.get(i).getShowHtmlString();
			if (bean.getPassType() == 0) {
				String value = "";
				String rConName = bean.getReqConName();
				for (SigmaPassParamBean beanParam : listParam) {
					if (bean.getPassParam()
							.equals(beanParam.getPassParamName())) {
						value = beanParam.getPassParamValue();
						break;
					}
				}
				String strTxt = "<input style='"
						+ SigmaGridConstant.GRID_TXT_CLASS + "' value='"
						+ value + "' name='" + rConName + "'>";
			} else {

			}
			// out.println(bean.getShowHtmlString());

		}

	}

}
