package com.ailk.bi.base.taglib.flashmap;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.ailk.bi.base.struct.LeaderQryStruct;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.table.PubInfoConditionTable;
import com.ailk.bi.base.util.CommTool;
import com.ailk.bi.base.util.NullProcFactory;
import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.app.FormatUtil;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.common.flashmap.GetAnyWhere;
import com.ailk.bi.leader.struct.LeaderKpiInfoStruct;

public class TagFlashMap extends TagSupport {

	private static final long serialVersionUID = 1L;
	// 条件实体对象
	private LeaderQryStruct whereobj;
	// 地图实例ID
	private String mapid;
	// 数据查询主体SQL ID
	private String datasqlfirstid;

	private String biztype;

	public String getBiztype() {
		return biztype;
	}

	public void setBiztype(String biztype) {
		this.biztype = biztype;
	}

	private LeaderKpiInfoStruct kpistruct;

	public LeaderQryStruct getWhereobj() {
		return whereobj;
	}

	public void setWhereobj(LeaderQryStruct whereobj) {
		this.whereobj = whereobj;
	}

	public String getMapid() {
		return mapid;
	}

	public void setMapid(String mapareaid) {
		this.mapid = mapareaid;
	}

	public String getDatasqlfirstid() {
		return datasqlfirstid;
	}

	public void setDatasqlfirstid(String datasqlfirstid) {
		this.datasqlfirstid = datasqlfirstid;
	}

	public LeaderKpiInfoStruct getKpistruct() {
		return kpistruct;
	}

	public void setKpistruct(LeaderKpiInfoStruct kpistruct) {
		this.kpistruct = kpistruct;
	}

	public int doEndTag() throws JspException {
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			// 生成地图XML块
			String mapchart = generateXML(request, mapid, whereobj,
					datasqlfirstid, kpistruct, biztype);
			pageContext.getOut().println(mapchart);
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return super.doEndTag();
	}

	/**
	 * 1）为该地图配置地图实例信息，表名：dw.UI_MAP_tab 2）为该地图配置地域区域，该表为常用表，已配置好 。表名：dw.area 3)
	 * 为该地图所需要展示的指标配置阀值数据，表名：DW.UI_LEADER_KPI_INFO 4）为该地图所需要展示的指标配置查询条件
	 * 表名:UI_PUB_INFO_CONDITION 5)
	 * 为该指标配置所需要的查询数据主体SQL,可在程序中定义（sql_def.xml）,请参考实体“MAPQY01”
	 * 6）注意传入的实体LeaderQryStruct，需要必须参数 msu_id,msu_name，day_id
	 *
	 * @param MapAreaId
	 * @param obj
	 * @param DataSQLFirstID
	 * @return
	 * @throws Exception
	 */
	public String generateXML(HttpServletRequest request, String MapAreaId,
			LeaderQryStruct obj, String DataSQLFirstID,
			LeaderKpiInfoStruct kpiStruct, String bizType) throws Exception {
		// 得到查询地图实体信息的SQL
		if (MapAreaId == null || "".equals(MapAreaId))
			return "地图实例ID未传入，无法显示";

		String mapsql = SQLGenator.genSQL("MAP001", MapAreaId);
		// 得到地图实例结果集
		String[][] mapresult = WebDBUtil.execQryArray(mapsql, "");
		String contextPath = request.getContextPath();
		// 地图名称：如重庆
		String mapName = mapresult[0][1];
		// 地图宽度
		String width = mapresult[0][2];
		// 地图高度
		String height = mapresult[0][3];
		// 地图前景色
		String mapColor = mapresult[0][4];
		// 地图背景色
		String BgColor = mapresult[0][5];
		// 地图是否显示背景色
		String isShowBackground = mapresult[0][6];
		// 地图是否出现提示
		String isShowPop = mapresult[0][7];
		// 得到地域查询SQL
		String areasql = mapresult[0][8];
		// 地图文件名称
		String mapSrc = mapresult[0][9];
		// 条件资源ID 所在表：UI_PUB_INFO_CONDITION
		String res_id = mapresult[0][10];
		// 条件资源类型 地图类型已确定为10
		String res_type = mapresult[0][11];
		// 地图点击事件
		String jsfunction = mapresult[0][12] == null ? "" : mapresult[0][12];
		// 阀值颜色配置
		String[] LEVELCOLOR = mapresult[0][13].split(",");
		// 区域查询
		String[][] arearesult = WebDBUtil.execQryArray(areasql.trim(), "");
		// 数据源
		// String datasource = "";
		// 数据权限分割地图
		UserCtlRegionStruct regionStruct = (UserCtlRegionStruct) request
				.getSession().getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);

		// 构造时间
		SimpleDateFormat sdf = null;
		Date d = null;
		String dataSource = obj.data_source;
		if (bizType.equals("1")) {
			sdf = new SimpleDateFormat("yyyyMM");
			d = sdf.parse(obj.gather_mon);
			sdf.applyPattern("yyyy年MM月");
			dataSource = "UI_LEADER_KPI_INFO_DATA_M";
		}
		if (bizType.equals("2")) {
			sdf = new SimpleDateFormat("yyyyMMdd");
			d = sdf.parse(obj.gather_day);
			sdf.applyPattern("yyyy年MM月dd日");
			dataSource = "UI_LEADER_KPI_INFO_DATA_D";
		}
		// 构造数据查询SQL，并得到需要的数据
		PubInfoConditionTable[] ConditionWhere = GetAnyWhere.genCondition(
				res_id, res_type);
		String where_sql = GetAnyWhere.getPubWhere(ConditionWhere, obj);// 得到查询where
																		// SQL
		String sql = SQLGenator.genSQL(DataSQLFirstID, dataSource, where_sql);
		System.out.println("地图SQL===============" + sql);
		String[][] result = WebDBUtil.execQryArray(sql, "");
		if (result == null || result.length == 0) {
			return "该地图无数据无法生成";
		}
		// 根据指标，得到阀值查询SQL，并查出数据
		String type = bizType.equals("2") ? "day" : "month";
		String sql_limit = SQLGenator.genSQL("MAP002", type, type, type, type,
				kpiStruct.msu_id);
		String[][] result_limit = WebDBUtil.execQryArray(sql_limit, "");

		// 实例阀值数据
		String[] color_limit = null;
		int compareType = -1;// 0、环比 1、同比 2、累计值环比 3、累计值同比
		if (result_limit != null && result_limit.length > 0) {
			for (int i = 0; i < result_limit[0].length; i++) {
				if (result_limit[0][i] != null
						&& !"0".equals(result_limit[0][i])
						&& !"".equals(result_limit[0][i].trim())) {
					color_limit = result_limit[0][i].split(",");
					compareType = i;
					break;
				}
			}
			if (color_limit == null) {
				color_limit = new String[] { "0" };
			}
		} else {
			return "该指标没有阀值配置,请进行阀值配置!";
		}
		if ((color_limit.length + 1) > LEVELCOLOR.length) {
			return "阀值的个数多于阀值的颜色配置个数，请重新配置!";
		}

		double[] db_color_limit = new double[color_limit.length];
		for (int i = 0; i < color_limit.length; i++) {
			db_color_limit[i] = Double.parseDouble(color_limit[i]);
		}

		// 开始准备构造地图标题信息
		StringBuffer statTitle = new StringBuffer();
		/*
		 * statTitle.append("＜b＞地域影响分析  ") .append(mapName)
		 * .append(obj.msu_name) .append("＜/b＞");
		 */
		StringBuffer statNote = new StringBuffer();
		if (regionStruct.ctl_lvl.equals("1")) {
			mapName = "北京市";
		} else if (regionStruct.ctl_city_str.indexOf(",") > -1) {
			mapName = "北京市";
		} else {

			String cityy = "";
			if (regionStruct.ctl_city_str.equals("2000000000")) {
				cityy = "2005000000";
			} else {
				cityy = regionStruct.ctl_city_str;
			}

			mapName = CommTool
					.getResName(request.getSession(), "region", cityy);
		}
		statNote.append(sdf.format(d))
				.append(" ")
				// 全市==北京市
				// 单区 == 区名称
				// 多区 == 北京市
				.append(mapName).append(" ＜font color=“#FF6633”＞")
				.append(kpiStruct.msu_name).append("＜/font＞＜br＞");

		// 得到汇总数据，取结果集最后一行，并以此算出环比和变化量，将该信息构造到标题头
		String sumCurValue = result[result.length - 1][2];// 本期值
		String sumLastValue = result[result.length - 1][3];// 上期值
		String sumAggValue = FormatUtil.formatStr(result[result.length - 1][4],
				kpiStruct.digit, true);// 本期累计值
		// String sumLastAggValue_tmp =
		// FormatUtil.formatStr(result[result.length - 1][6], kpiStruct.digit,
		// true);//本期累计值

		// 月
		String linkRelative = Arith.divPerNot(
				Arith.sub(sumCurValue, sumLastValue), sumLastValue, 2);// 环比
		// 日
		String linkRelative_d = Arith.divPerNot(Arith.sub(
				result[result.length - 1][4], result[result.length - 1][6]),
				result[result.length - 1][6], 2);// 环比
		if ("0.00".equals(sumLastValue) || "0".equals(sumLastValue)) {
			linkRelative = "100";
		}
		// 变化量
		if (bizType.equals("1")) {// 月
			statNote.append("本期值：＜font color=“#008200”＞")
					.append(sumCurValue)
					.append("＜/font＞(" + kpiStruct.unit
							+ ")＜br＞上期值：＜font color=“#008200”＞")
					.append(sumLastValue)
					.append("＜/font＞＜br＞&nbsp;&nbsp;&nbsp;&nbsp;环比：＜font color=“#008200”＞")
					.append(linkRelative)
					.append("％＜/font＞(" + kpiStruct.unit + ")'/>");
		}
		if (bizType.equals("2")) {// 日
			statNote.append("本月累计：＜font color=“#008200”＞")
					.append(sumAggValue)
					.append("＜/font＞(" + kpiStruct.unit
							+ ")＜br＞累计环比：＜font color=“#008200”＞")
					.append(linkRelative_d)
					.append("％＜/font＞＜br＞&nbsp;&nbsp;&nbsp;&nbsp;变化量：＜font color=“#008200”＞")
					.append(FormatUtil.formatStr(Arith.sub(
							result[result.length - 1][4],
							result[result.length - 1][6]), kpiStruct.digit,
							true)).append("＜/font＞(" + kpiStruct.unit + ")'/>");
		}

		// 地图点击函数xml串构造
		StringBuffer dataStr = new StringBuffer();
		String funcitem = "";
		if (jsfunction != null && !"".equals(jsfunction)) {
			funcitem = "statFuncName='" + jsfunction + "'";
		}

		// 地图的标题信息准备好后，开始构造
		dataStr.append("<myxml><statInfo>").append("<statItem ")
				.append(funcitem).append(" showPop='").append(isShowPop)
				.append("' mapColor='").append(mapColor)
				.append("' showBackground='").append(isShowBackground)
				.append("' BgColor='").append(BgColor).append("' statTitle='")
				.append(statTitle.toString()).append("' statNote='");
		// 开始准备构造各区域地图块
		StringBuffer areaItem = new StringBuffer();
		String areaColor = "";
		// 对数据结果进行循环，准备构造区域块
		for (int i = 0; i < result.length; i++) {
			String CityID = result[i][0];
			String CityName = result[i][1];
			if (CityName != null) {
				// 以下指标分别代表每个地图块所标示的本期值，上期值，环比
				sumCurValue = result[i][2];// 本期值
				sumLastValue = result[i][3];// 上期值

				String sumAggValue1 = result[i][4];// 本期累计值
				String sumSameValue = result[i][5];// 同期值
				String sumLastAggValue = result[i][6];// 上期累计值
				String sumSameAggValue = result[i][7];// 同期累计值
				double last_per = 0;
				if (!"0.00".equals(sumLastValue) || "0".equals(sumLastValue)) {
					switch (compareType) {// 0、环比 1、同比 2、累计值环比 3、累计值同比
					case 0:
						last_per = Double.parseDouble(NullProcFactory
								.transNullToFixedRate(Arith.divPerNot(
										Arith.sub(sumCurValue, sumLastValue),
										sumLastValue, 2), "100"));
						break;
					case 1:
						last_per = Double.parseDouble(NullProcFactory
								.transNullToFixedRate(Arith.divPerNot(
										Arith.sub(sumCurValue, sumSameValue),
										sumSameValue, 2), "100"));
						break;
					case 2:
						last_per = Double.parseDouble(NullProcFactory
								.transNullToFixedRate(Arith.divPerNot(Arith
										.sub(sumAggValue1, sumLastAggValue),
										sumLastAggValue, 2), "100"));
						break;
					case 3:
						last_per = Double.parseDouble(NullProcFactory
								.transNullToFixedRate(Arith.divPerNot(Arith
										.sub(sumAggValue1, sumSameAggValue),
										sumSameAggValue, 2), "100"));
						break;
					}
					sumCurValue = FormatUtil.formatStr(sumCurValue,
							kpiStruct.digit, true);
				} else {
					last_per = 100;
				}
				// 针对各地图的环比不同，做相应的地图区域颜色预警
				if (db_color_limit != null && db_color_limit.length > 0) {
					for (int t = 0; t < db_color_limit.length; t++) {
						if (t == 0) {// 判断第一个阀值点的情况
							if (last_per <= db_color_limit[t]) {
								areaColor = LEVELCOLOR[t];
								break;
							} else if (last_per > db_color_limit[t]
									&& db_color_limit.length == 1) {
								areaColor = LEVELCOLOR[t + 1];
								break;
							} else if ((last_per > db_color_limit[t])
									&& db_color_limit.length > 1
									&& (last_per <= db_color_limit[t + 1])) {
								areaColor = LEVELCOLOR[t + 1];
								break;
							}
						} else if (t == (db_color_limit.length - 1)) {// 判断最后一个阀值点的情况
							if (last_per > db_color_limit[t]
									&& db_color_limit.length > 1) {
								areaColor = LEVELCOLOR[t + 1];
								break;
							} else if (last_per <= db_color_limit[t]
									&& db_color_limit.length > 1) {
								areaColor = LEVELCOLOR[t];
								break;
							}
						} else {// 判断中间阀值点的情况
							if ((last_per > db_color_limit[t])
									&& (last_per <= db_color_limit[t + 1])) {
								areaColor = LEVELCOLOR[t + 1];
								break;
							} else if (last_per <= db_color_limit[t]) {
								areaColor = LEVELCOLOR[t];
								break;
							}
						}
					}
				}
				// 开始构造地图区域块
				for (int j = 0; j < arearesult.length; j++) {

					// 需要判断权限！
					if (regionStruct.ctl_lvl.equals("1")) {
						if (CityID.equals(arearesult[j][0])) {
							areaItem.append("<AreaItem AreaID='")
									.append(arearesult[j][0])
									.append("' AreaTxt='")
									// .append(arearesult[j][1])
									.append(kpiStruct.msu_name + "：")
									.append(sumCurValue)
									// 本期值
									.append("' AreaColor='").append(areaColor)
									.append("'/>");
							break;
						}

					} else {
						if (regionStruct.ctl_city_str.equals("2000000000")) {
							if ((regionStruct.ctl_county_str.indexOf(CityID) > -1)
									&& CityID.equals(arearesult[j][0])) {
								areaItem.append("<AreaItem AreaID='")
										.append(arearesult[j][0])
										.append("' AreaTxt='")
										// .append(arearesult[j][1])
										.append(kpiStruct.msu_name + "：")
										.append(sumCurValue)
										// 本期值
										.append("' AreaColor='")
										.append(areaColor).append("'/>");
								break;

							}
						} else {
							if ((regionStruct.ctl_city_str.indexOf(CityID) > -1)
									&& CityID.equals(arearesult[j][0])) {
								areaItem.append("<AreaItem AreaID='")
										.append(arearesult[j][0])
										.append("' AreaTxt='")
										// .append(arearesult[j][1])
										.append(kpiStruct.msu_name + "：")
										.append(sumCurValue)
										// 本期值
										.append("' AreaColor='")
										.append(areaColor).append("'/>");
								break;
							}
						}

					}

				}
			}
		}
		dataStr.append(statNote.toString()).append("'/>")
				.append("</statInfo><AreaData>").append(areaItem.toString())
				.append("</AreaData><info>");
		// 阀值提示构造
		dataStr.append("<infoItem infoColor='" + LEVELCOLOR[0] + "' infoTxt='＜"
				+ color_limit[0] + "％'/>");
		for (int y = 0; y < color_limit.length; y++) {
			dataStr.append("<infoItem infoColor='" + LEVELCOLOR[y + 1]
					+ "' infoTxt='＞=" + color_limit[y] + "％'/>");
		}
		dataStr.append("</info></myxml>");
		// 开始整体构造地图
		StringBuffer results = new StringBuffer();
		results.append(
				"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\" ")
				.append("width=\"")
				.append(width)
				.append("\" height=\"")
				.append(height)
				.append("\" id=\"map\" align=\"middle\">")
				.append("<embed src=\"" + contextPath + "/swf/map/")
				.append(mapSrc)
				.append("\" width=\"")
				.append(width)
				.append("\" height=\"")
				.append(height)
				.append("\" wmode=\"transparent\" quality=\"high\" bgcolor=\"#ffffff\" name=\"map\" align=\"middle\" allowscriptaccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\" http://www.macromedia.com/go/getflashplayer\" />")
				.append("<param name=\"allowScriptAccess\" value=\"sameDomain\" />")
				.append("<param name=\"movie\" value=\"" + contextPath
						+ "/swf/map/").append(mapSrc).append("\" />")
				.append("<param name=\"wmode\" value=\"Transparent\"/>")
				.append("<param name=\"quality\" value=\"high\" />")
				.append("<param name=\"WMode\" value=\"#Transparent\" />")
				.append("<param name=\"bgcolor\" value=\"#ffffff\" />")
				.append("<param name=\"FlashVars\" value=\"inxml=")
				.append(dataStr.toString()).append("\" /></object>");
		return results.toString();
	}

}