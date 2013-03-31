package com.ailk.bi.olap.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ailk.bi.base.table.PubInfoResourceTable;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.olap.domain.RptOlapChartAttrStruct;
import com.ailk.bi.olap.domain.RptOlapDateStruct;
import com.ailk.bi.olap.util.RptOlapChartUtil;
import com.ailk.bi.olap.util.RptOlapConsts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ReportOlapChartDimHTMLAction extends HTMLActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -1069073337870612373L;
	private transient static Log log = LogFactory
			.getLog(ReportOlapChartDimHTMLAction.class);

	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		Log log = LogFactory.getLog(this.getClass());
		if (null == request || null == response) {
			log.error("分析型报表表格分析－请求或回应对象为空");
			throw new IllegalArgumentException("获取分析性报表内容时参数为空");
		}
		HttpSession session = request.getSession();
		String reportId = request.getParameter("report_id");
		if (null == reportId || "".equals(reportId)) {
			log.error("分析型报表表格分析－无法获取报表标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表标识！");
		}
		// 获取所有的HTML[]
		String dimId = request.getParameter("dim_id");
		if (null == dimId || "".equals(dimId)) {
			log.error("分析型报表表格分析－无法获取维度标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取维度标识！");
		}
		String dimLevel = request.getParameter("dim_level");
		if (null == dimLevel || "".equals(dimLevel))
			dimLevel = RptOlapConsts.ZERO_STR;

		String dimObj = request.getParameter("dim_object");
		if (null == dimObj || "".equals(dimObj)) {
			log.error("分析型报表图形分析－无法获取维度标识");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取维度标识！");
		}
		//
		Object tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_DATE_OBJ + "_"
				+ reportId);
		if (null == tmpObj) {
			log.error("分析型报表表格分析－无法获取报表时间对象");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表时间对象！");
		}
		RptOlapDateStruct ds = (RptOlapDateStruct) tmpObj;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DOMAINS_OBJ + "_"
				+ reportId);
		if (null == tmpObj) {
			log.error("分析型报表表格分析－组合报表域对象列表时发生错误");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "组合报表域对象列表时发生错误！");
		}
		List chartStructs = (List) tmpObj;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_REPORT_OBJ + "_"
				+ reportId);
		if (null == tmpObj) {
			log.error("分析型报表表格分析－无法获取报表对象");
			throw new HTMLActionException(session,
					HTMLActionException.WARN_PAGE, "无法获取报表对象！");
		}
		PubInfoResourceTable report = (PubInfoResourceTable) tmpObj;
		RptOlapChartAttrStruct chartStruct = RptOlapChartUtil
				.getSelectedChartStruct(dimId, chartStructs);
		RptOlapChartAttrStruct chartTime = RptOlapChartUtil
				.getTimeStruct(chartStructs);
		chartStruct.setLevel(dimLevel);
		// 所有的维度值都进行缓存，考虑这里
		Map dimsValues = null;
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DIMS_VALUES);
		if (null == tmpObj)
			dimsValues = new HashMap();
		else
			dimsValues = (Map) tmpObj;
		Map dimAllLevelValues = null;
		List dimValues = null;
		// 这个要存MAP
		tmpObj = session.getAttribute(WebKeys.ATTR_OLAP_CHART_DIM_VALUES + "_"
				+ reportId);
		if (null == dimValues) {
			tmpObj = dimsValues.get(dimId);
			if (null == tmpObj) {
				dimAllLevelValues = new HashMap();
				dimValues = RptOlapChartUtil.getDimValues(chartStruct,
						chartTime, report, ds);
				log.info("olap chart get dim  values 1:get level ="
						+ chartStruct.getLevel() + ", dim values length is "
						+ dimValues.size());
				dimAllLevelValues.put(chartStruct.getLevel(), dimValues);
			} else {
				dimAllLevelValues = (Map) dimsValues.get(dimId);
				tmpObj = dimAllLevelValues.get(chartStruct.getLevel());
				if (null == tmpObj) {
					dimValues = RptOlapChartUtil.getDimValues(chartStruct,
							chartTime, report, ds);
					log.info("olap chart get dim  values 2:get level ="
							+ chartStruct.getLevel()
							+ ", dim values length is " + dimValues.size());
					dimAllLevelValues.put(chartStruct.getLevel(), dimValues);
				} else {
					dimValues = (List) tmpObj;
				}
			}
		} else {
			dimAllLevelValues = (Map) tmpObj;
			tmpObj = dimAllLevelValues.get(chartStruct.getLevel());
			if (null == tmpObj) {
				dimValues = RptOlapChartUtil.getDimValues(chartStruct,
						chartTime, report, ds);
				log.info("olap chart get dim  values 3:get level ="
						+ chartStruct.getLevel() + ", dim values length is "
						+ dimValues.size());
				dimAllLevelValues.put(chartStruct.getLevel(), dimValues);
			} else {
				dimValues = (List) tmpObj;
			}
		}
		dimsValues.put(dimId, dimAllLevelValues);
		dimValues = RptOlapChartUtil
				.setDimStructChecked(chartStruct, dimValues);

		if (log.isInfoEnabled()) {
			Iterator iter = dimAllLevelValues.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				log.info("olap chart dims keys: cur level="
						+ chartStruct.getLevel() + " : key=" + entry.getKey());
			}
		}
		if (null != chartStruct)
			session.setAttribute(WebKeys.ATTR_OLAP_CHART_DIM + "_" + reportId,
					chartStruct);
		if (null != dimValues)
			session.setAttribute(WebKeys.ATTR_OLAP_CHART_DIM_VALUES + "_"
					+ reportId, dimAllLevelValues);
		if (null != dimsValues)
			session.setAttribute(WebKeys.ATTR_OLAP_CHART_DIMS_VALUES,
					dimsValues);
		if (null != dimObj)
			session.setAttribute(WebKeys.ATTR_OLAP_CHART_DIM_JS_OBJECT + "_"
					+ reportId, dimObj);
		session.setAttribute(WebKeys.ATTR_OLAP_REPORT_ID, reportId);
		setNextScreen(request, "rptOlapChartDim.screen");
	}
}
