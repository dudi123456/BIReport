package com.ailk.bi.syspar.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.syspar.domain.UiParamInfoConfigTable;
import com.ailk.bi.syspar.domain.UiParamMetaConfigTable;
import com.ailk.bi.syspar.util.ParamConstant;
import com.ailk.bi.syspar.util.ParamHelper;
import com.ailk.bi.system.facade.impl.CommonFacade;

public class ParamViewHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 检查登陆
		if (!com.ailk.bi.common.app.WebChecker
				.isLoginUser(request, response)) {
			return;
		}
		//
		HttpSession session = request.getSession(true);
		String sessionID = request.getSession().getId();
		String ip = CommTool.getClientIP(request);
		String oper_oper_no = CommonFacade.getLoginId(request.getSession());

		// 值辅助MAP
		// HashMap map = new HashMap();
		// 当前节点元数据
		UiParamMetaConfigTable[] configInfo = (UiParamMetaConfigTable[]) session
				.getAttribute(ParamConstant.PARAM_CONF_META_TABLE);
		if (configInfo == null || configInfo.length <= 0) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "当前参数节点配置参数缺失,请重新登陆！");
		}
		//

		// 当前节点配置信息
		UiParamInfoConfigTable nodeInfo = (UiParamInfoConfigTable) session
				.getAttribute(ParamConstant.PARAM_CONF_INFO_TABLE);
		if (nodeInfo == null) {
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "当前参数节点数据库表参数缺失,请重新登陆！");
		}
		//
		String node_id = CommTool.getParameterGB(request, "t_node_id");
		String table_name = CommTool.getParameterGB(request, "t_table_name");

		//
		String tableFiledStr = "";// 字段名称
		String tableFiledValue = "";// 字段值
		String updateFiledStr = "";// 变更实体组织,变更字段串
		String updateWhereStr = "";// 变更实体组织变更字段条件串
		String addWhereStr = "";// 新增基本实体的条件串

		//
		for (int i = 0; configInfo != null && i < configInfo.length; i++) {
			// 字段数据类型
			String dataType = configInfo[i].getColumn_data_type().toUpperCase();
			// 字段显示类型
			String showType = configInfo[i].getColumn_show_type().toUpperCase();
			// 当前字段值（很重要）
			String value = CommTool.getParameterGB(request,
					configInfo[i].getColumn_en_name());
			String rule_value = CommTool.getParameterGB(request,
					configInfo[i].getColumn_show_rule());
			String hValue = CommTool.getParameterGB(request, "H__"
					+ configInfo[i].getColumn_en_name());
			// 过滤替换
			if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_V)
					|| dataType.equals(ParamConstant.COLUMN_DATA_TYPE_L)) {
				if (value != null && !"".equals(value)) {
					value = StringB.replace(value, "'", "''");
				}

				if (hValue != null && !"".equals(hValue)) {
					hValue = StringB.replace(hValue, "'", "''");
				}
			}

			// 字段格式化
			String format = configInfo[i].getColumn_format();
			// 判断唯一索引不为空！
			if ("Y".equals(configInfo[i].getUnique_index())
					&& configInfo[i].getUnique_index() != null
					&& (value == null || "".equals(value))) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "当前参数节点{"
								+ nodeInfo.getParam_name()
								+ "}唯一索引值缺失,请检查录入界面！");
			}

			// 当前节点值(有值才处理！)
			if ((value != null && !"".equals(value))
					|| (hValue != null && !"".equals(hValue))) {
				// 自定义radio是否有描述值
				boolean radioDesc = true;

				// 拼装字段
				if (ParamConstant.COLUMN_SHOW_TYPE_8.equals(showType)) {
					// 自定义radio编码值
					if (tableFiledStr.length() > 0) {
						tableFiledStr += ",";
					}
					tableFiledStr += configInfo[i].getColumn_show_rule();
					// 自定义radio描述值，type等于N没有
					if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_N)) {
						radioDesc = false;
					}
				}
				if (radioDesc) {
					if (tableFiledStr.length() > 0) {
						tableFiledStr += ",";
					}
					tableFiledStr += configInfo[i].getColumn_en_name();
				}

				// 拼装数据值
				if (ParamConstant.COLUMN_SHOW_TYPE_8.equals(showType)) {
					// 自定义radio编码值
					if (tableFiledValue.length() > 0) {
						tableFiledValue += ",";
					}
					tableFiledValue += "'" + rule_value + "'";
				}
				if (radioDesc) {
					if (tableFiledValue.length() > 0) {
						tableFiledValue += ",";
					}

					if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_V)
							|| dataType
									.equals(ParamConstant.COLUMN_DATA_TYPE_R)) {
						if (format != null && !"".equals(format)) {
							tableFiledValue += "to_char('" + value + "','"
									+ format + "')";
						} else {
							tableFiledValue += "'" + value + "'";
						}
					} else if (dataType
							.equals(ParamConstant.COLUMN_DATA_TYPE_N)) {
						tableFiledValue += value;
					} else if (dataType
							.equals(ParamConstant.COLUMN_DATA_TYPE_D)) {
						if (format != null && !"".equals(format)) {
							tableFiledValue += "to_date('" + value + "','"
									+ format + "')";
						} else {
							tableFiledValue += "to_date('" + value
									+ "','yyyymmdd')";
						}
					} else if (dataType
							.equals(ParamConstant.COLUMN_DATA_TYPE_L)) {
						tableFiledValue += "'" + value + "'";
					}
				}

				// 变更字段
				if (updateFiledStr.length() > 0) {
					updateFiledStr += ",";
				}
				if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_V)
						|| dataType.equals(ParamConstant.COLUMN_DATA_TYPE_R)) {
					// 拼装字段
					if (ParamConstant.COLUMN_SHOW_TYPE_8.equals(showType)) {
						if (rule_value == null || "".equals(rule_value)) {
							updateFiledStr += configInfo[i]
									.getColumn_show_rule() + " = NULL";
						} else {
							updateFiledStr += configInfo[i]
									.getColumn_show_rule()
									+ " = '"
									+ rule_value + "'";
						}
						// 自定义radio编码值
						if (updateFiledStr.length() > 0) {
							updateFiledStr += ",";
						}
					}

					if (format != null && !"".equals(format)) {
						if (value == null || "".equals(value)) {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = NULL";
						} else {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = to_char('" + value + "','" + format
									+ "')";
						}

					} else {

						if (value == null || "".equals(value)) {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = NULL";
						} else {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = '" + value + "'";
						}
					}

				} else if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_N)) {
					if (ParamConstant.COLUMN_SHOW_TYPE_8.equals(showType)) {
						if (rule_value == null || "".equals(rule_value)) {
							updateFiledStr += configInfo[i]
									.getColumn_show_rule() + " = NULL";
						} else {
							updateFiledStr += configInfo[i]
									.getColumn_show_rule() + " = " + rule_value;
						}
					} else {
						if (value == null || "".equals(value)) {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = NULL";
						} else {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = " + value;
						}
					}
				} else if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_D)) {
					if (format != null && !"".equals(format)) {

						if (value == null || "".equals(value)) {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = NULL";
						} else {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = to_date('" + value + "','" + format
									+ "')";
						}

					} else {

						if (value == null || "".equals(value)) {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = NULL";
						} else {
							updateFiledStr += configInfo[i].getColumn_en_name()
									+ " = to_date('" + value + "','yyyymmdd')";
						}

					}
				} else if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_L)) {

					if (value == null || "".equals(value)) {
						updateFiledStr += configInfo[i].getColumn_en_name()
								+ " = NULL";
					} else {
						updateFiledStr += configInfo[i].getColumn_en_name()
								+ " = '" + value + "'";
					}

				}

				// 判断唯一索引
				if (ParamHelper.hasUniqeIndex(configInfo)) {
					// update or delete
					if (configInfo[i].getUnique_index().toUpperCase()
							.equals("Y")
							&& hValue != null && !"".equals(hValue)) {
						if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_V)
								|| dataType
										.equals(ParamConstant.COLUMN_DATA_TYPE_R)) {
							if (format != null && !"".equals(format)) {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_char('" + hValue + "','"
										+ format + "')";
							} else {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = '" + hValue + "'";
							}
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_N)) {
							updateWhereStr += " AND "
									+ configInfo[i].getColumn_en_name() + " = "
									+ hValue;
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_D)) {

							if (format != null && !"".equals(format)) {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + hValue + "','"
										+ format + "')";
							} else {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + hValue
										+ "','yyyymmdd')";
							}

						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_L)) {
							updateWhereStr += " AND "
									+ configInfo[i].getColumn_en_name()
									+ " = '" + hValue + "'";
						}
					}
					// add
					if (configInfo[i].getUnique_index().toUpperCase()
							.equals("Y")
							&& value != null && !"".equals(value)) {
						if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_V)
								|| dataType
										.equals(ParamConstant.COLUMN_DATA_TYPE_R)) {
							if (format != null && !"".equals(format)) {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_char('" + value + "','"
										+ format + "')";
							} else {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = '" + value + "'";
							}
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_N)) {
							addWhereStr += " AND "
									+ configInfo[i].getColumn_en_name() + " = "
									+ value;
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_D)) {
							if (format != null && !"".equals(format)) {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + value + "','"
										+ format + "')";
							} else {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + value
										+ "','yyyymmdd')";
							}
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_L)) {
							addWhereStr += " AND "
									+ configInfo[i].getColumn_en_name()
									+ " = '" + value + "'";
						}
					}

				} else {// 没有唯一索引
					// update or delte
					if (hValue != null && !"".equals(hValue)) {
						if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_V)
								|| dataType
										.equals(ParamConstant.COLUMN_DATA_TYPE_R)) {
							if (format != null && !"".equals(format)) {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_char('" + hValue + "','"
										+ format + "')";
							} else {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = '" + hValue + "'";
							}
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_N)) {
							updateWhereStr += " AND "
									+ configInfo[i].getColumn_en_name() + " = "
									+ hValue;
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_D)) {
							if (format != null && !"".equals(format)) {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + hValue + "','"
										+ format + "')";
							} else {
								updateWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + hValue
										+ "','yyyymmdd')";
							}
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_L)) {
							updateWhereStr += " AND "
									+ configInfo[i].getColumn_en_name()
									+ " = '" + hValue + "'";
						}
					}

					// add
					if (value != null && !"".equals(value)) {
						if (dataType.equals(ParamConstant.COLUMN_DATA_TYPE_V)
								|| dataType
										.equals(ParamConstant.COLUMN_DATA_TYPE_R)) {
							if (format != null && !"".equals(format)) {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_char('" + value + "','"
										+ format + "')";
							} else {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = '" + value + "'";
							}
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_N)) {
							addWhereStr += " AND "
									+ configInfo[i].getColumn_en_name() + " = "
									+ value;
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_D)) {
							if (format != null && !"".equals(format)) {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + value + "','"
										+ format + "')";
							} else {
								addWhereStr += " AND "
										+ configInfo[i].getColumn_en_name()
										+ " = to_date('" + value
										+ "','yyyymmdd')";
							}
						} else if (dataType
								.equals(ParamConstant.COLUMN_DATA_TYPE_L)) {
							addWhereStr += " AND "
									+ configInfo[i].getColumn_en_name()
									+ " = '" + value + "'";
						}
					}
				}

			}
		}
		// 默认字段处理
		if (nodeInfo.getIsdimtable().equals("Y")
				&& nodeInfo.getDefault_tag().equals("Y")) {
			tableFiledStr += ",UPDATE_DATE";
			tableFiledValue += ",SYSDATE";
			updateFiledStr += " , UPDATE_DATE = SYSDATE";
		}

		//
		String operType = CommTool.getParameterGB(request, "oper_type");
		if (operType == null && "".equals(operType)) {
			operType = "add";
		}
		int i = 0;
		if ("add".equals(operType)) {
			// 增加之前需要判断是否有重复记录
			String dupsql = "select * from " + nodeInfo.getTable_name()
					+ " where 1=1 " + addWhereStr;
			// System.out.println("sql============dupsql=============" +
			// dupsql);
			if (ParamHelper.isDupValue(dupsql)) {
				throw new HTMLActionException(session,
						HTMLActionException.WARN_PAGE, "当前数据表中{"
								+ nodeInfo.getParam_name()
								+ "}已经存在相同的值,请检查录入界面！");
			}
			String[] sqlArr = null;
			// 增加实体以及记录日志
			if (!"".equals(StringB.NulltoBlank(nodeInfo.getLog_table_name()
					.trim()))) {
				String logSql = "insert into "
						+ nodeInfo.getLog_table_name()
						+ "(SERVICE_SN,LOG_SEQ,OPER_OPER_NO,OPER_IP,OPER_TIME,OPER_TYPE,"
						+ tableFiledStr + ")" + " select log_seq_sn.nextval, '"
						+ sessionID + "' , '" + oper_oper_no + "' , '" + ip
						+ "' , to_char(sysdate,'yyyy-mm-dd HH24:MI:SS'),'1', "
						+ tableFiledStr + " from " + nodeInfo.getTable_name()
						+ "  where 1=1 " + addWhereStr;
				String sql = "insert into " + nodeInfo.getTable_name() + "("
						+ tableFiledStr + ") values(" + tableFiledValue + ")";
				System.out.println("sql============add=============" + sql);
				System.out.println("logsql=============add============"
						+ logSql);
				sqlArr = new String[] { sql, logSql };
			} else {
				String sql = "insert into " + nodeInfo.getTable_name() + "("
						+ tableFiledStr + ") values(" + tableFiledValue + ")";
				System.out.println("sql============add=============" + sql);
				sqlArr = new String[] { sql };
			}
			try {
				i = WebDBUtil.execTransUpdate(sqlArr);
			} catch (AppException e) {
				i = -1;

				e.printStackTrace();
			}

		} else if ("update".equals(operType)) {
			String[] sqlArr = null;
			if (!"".equals(StringB.NulltoBlank(nodeInfo.getLog_table_name()
					.trim()))) {
				if (updateWhereStr != null && !"".equals(updateWhereStr)) {
					String logSql = "insert into "
							+ nodeInfo.getLog_table_name()
							+ "(SERVICE_SN,LOG_SEQ,OPER_OPER_NO,OPER_IP,OPER_TIME,OPER_TYPE,"
							+ tableFiledStr
							+ ")"
							+ " select log_seq_sn.nextval, '"
							+ sessionID
							+ "' , '"
							+ oper_oper_no
							+ "' , '"
							+ ip
							+ "' , to_char(sysdate,'yyyy-mm-dd HH24:MI:SS'),'2', "
							+ tableFiledStr + "  from "
							+ nodeInfo.getTable_name() + "  where 1=1 "
							+ updateWhereStr;
					String sql = "update " + nodeInfo.getTable_name() + " set "
							+ updateFiledStr + " where 1=1 " + updateWhereStr;
					System.out.println("sql=============update============"
							+ sql);
					System.out.println("logsql=============update============"
							+ logSql);
					sqlArr = new String[] { sql, logSql };
				}
			} else {
				String sql = "update " + nodeInfo.getTable_name() + " set "
						+ updateFiledStr + " where 1=1 " + updateWhereStr;
				System.out.println("sql=============update============" + sql);
				sqlArr = new String[] { sql };
			}
			try {
				i = WebDBUtil.execTransUpdate(sqlArr);
			} catch (AppException e) {
				i = -1;

				e.printStackTrace();
			}

		} else if ("delete".equals(operType)) {
			String[] sqlArr = null;
			if (!"".equals(StringB.NulltoBlank(nodeInfo.getLog_table_name()
					.trim()))) {
				String logSql = "insert into "
						+ nodeInfo.getLog_table_name()
						+ "(SERVICE_SN,LOG_SEQ,OPER_OPER_NO,OPER_IP,OPER_TIME,OPER_TYPE,"
						+ tableFiledStr + ")" + " select log_seq_sn.nextval, '"
						+ sessionID + "' , '" + oper_oper_no + "' , '" + ip
						+ "' , to_char(sysdate,'yyyy-mm-dd HH24:MI:SS'),'3' , "
						+ tableFiledStr + "  from " + nodeInfo.getTable_name()
						+ "  where 1=1 " + updateWhereStr;
				String sql = "delete from  " + nodeInfo.getTable_name()
						+ " where 1=1 " + updateWhereStr;
				System.out.println("sql=============delete============" + sql);
				System.out.println("logsql=============delete============"
						+ logSql);
				sqlArr = new String[] { logSql, sql };
			} else if (!"".equals(updateWhereStr)) {
				String sql = "delete from  " + nodeInfo.getTable_name()
						+ " where 1=1 " + updateWhereStr;
				System.out.println("sql=============delete============" + sql);
				sqlArr = new String[] { sql };
			}
			try
			{
				i = WebDBUtil.execTransUpdate(sqlArr);
			} catch (AppException e) {
				i = -1;

				e.printStackTrace();

			}
		}
		if (i >= 0) {
			throw new HTMLActionException(session,
					HTMLActionException.SUCCESS_PAGE, "操作执行成功！",
					"ParamQuery.rptdo?node_id=" + node_id + "&table_name="
							+ table_name);
		} else { //i为负。
			throw new HTMLActionException(session,
					HTMLActionException.ERROR_PAGE, "当前操作失败，请通知系统管理员！");
		}

	}

}
