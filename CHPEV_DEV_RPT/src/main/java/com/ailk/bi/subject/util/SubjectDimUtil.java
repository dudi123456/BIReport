package com.ailk.bi.subject.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ailk.bi.base.table.SubjectCommDimHierarchy;
import com.ailk.bi.base.table.SubjectCommTabCol;
import com.ailk.bi.base.table.SubjectCommTabDef;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
import com.ailk.bi.subject.domain.SubjectDimStruct;
import com.ailk.bi.subject.domain.TableCurFunc;
import com.ailk.bi.subject.domain.TableSelectQuery;

/**
 * @author xdou 专题通用模版维度工具类
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SubjectDimUtil {
	/**
	 * 生成维度状态的链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param svces
	 *            一维数组
	 * @return 链接字符串
	 */
	public static Map genDimStateURL(SubjectCommTabDef subTable, TableCurFunc curFunc, String[] svces) {
		Map mpState = new HashMap();
		// 存储维度的值链接
		StringBuffer codeState = new StringBuffer();
		// 存储维度的描述信息
		StringBuffer descState = new StringBuffer();
		// 存储图形名称信息
		StringBuffer chartState = new StringBuffer();
		chartState.append("&chart_name=");
		int aryIndex = 0;
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
					String urllevel = SubjectConst.ZERO;
					// 判断是否全部展开,如果是
					if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
						// 全部展开，应该所有列都有了
						// 先加上第0层
						codeState.append("&")
								.append(SubjectStringUtil.clearVirTabName(tabCol.code_field).toLowerCase()).append("=");
						codeState.append(svces[aryIndex]);
						aryIndex++;
						String descValue = svces[aryIndex];
						try {
							descValue = URLEncoder.encode(URLEncoder.encode(descValue, "UTF-8"), "UTF-8");
						} catch (UnsupportedEncodingException e) {
						}
						// 描述信息
						descState.append("&")
								.append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase()).append("=");
						descState.append(descValue);
						codeState.append("&")
								.append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase()).append("=");
						codeState.append(descValue);
						// 判断一下
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.desc_astitle))
							chartState.append(svces[aryIndex - 1]).append(" ");
						else
							chartState.append(descValue).append(" ");
						aryIndex++;
						List levels = tabCol.levels;
						if (null != levels) {
							Iterator levIter = levels.iterator();
							while (levIter.hasNext()) {
								SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();
								codeState.append("&")
										.append(SubjectStringUtil.clearVirTabName(levObj.src_idfld).toLowerCase())
										.append("=");
								codeState.append(svces[aryIndex]);
								aryIndex++;
								descState.append("&")
										.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld).toLowerCase())
										.append("=");
								descState.append(svces[aryIndex]);
								codeState.append("&")
										.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld).toLowerCase())
										.append("=");
								codeState.append(svces[aryIndex]);
								if (SubjectConst.YES.equalsIgnoreCase(levObj.desc_astitle))
									chartState.append(svces[aryIndex - 1]).append(" ");
								else
									chartState.append(svces[aryIndex]).append(" ");
								aryIndex++;
								urllevel = levObj.lev_id;
							}
						}
					} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel
							&& null != curFunc.filterValues) {

						int level = 0;
						int maxLevel = Integer.parseInt(curFunc.filterLevel);
						codeState.append("&")
								.append(SubjectStringUtil.clearVirTabName(tabCol.code_field).toLowerCase()).append("=");
						codeState.append(svces[aryIndex]);
						aryIndex++;
						String descValue = svces[aryIndex];
						try {
							descValue = URLEncoder.encode(URLEncoder.encode(descValue, "UTF-8"), "UTF-8");
						} catch (UnsupportedEncodingException e) {
						}
						descState.append("&")
								.append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase()).append("=");
						descState.append(descValue);
						codeState.append("&")
								.append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase()).append("=");
						codeState.append(descValue);
						if (SubjectConst.YES.equalsIgnoreCase(tabCol.desc_astitle))
							chartState.append(svces[aryIndex - 1]).append(" ");
						else
							chartState.append(descValue).append(" ");
						aryIndex++;
						List levels = tabCol.levels;
						if (null != levels) {
							Iterator levIter = levels.iterator();
							while (levIter.hasNext() && level <= maxLevel) {
								level++;
								SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();

								codeState.append("&")
										.append(SubjectStringUtil.clearVirTabName(levObj.src_idfld).toLowerCase())
										.append("=");
								codeState.append(svces[aryIndex]);
								aryIndex++;
								descState.append("&")
										.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld).toLowerCase())
										.append("=");
								descState.append(svces[aryIndex]);
								codeState.append("&")
										.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld).toLowerCase())
										.append("=");
								codeState.append(svces[aryIndex]);
								if (SubjectConst.YES.equalsIgnoreCase(levObj.desc_astitle))
									chartState.append(svces[aryIndex - 1]).append(" ");
								else
									chartState.append(svces[aryIndex]).append(" ");
								aryIndex++;
								urllevel = levObj.lev_id;
							}
						}
					} else {
						// 这里如果是到某个层次了，则需要将前面展开的加上
						// 这里只简单加上这个显然有问题
						Map values = tabCol.values;
						if (null != values) {
							// 需要找到以前的值
							Object obj = values.get(SubjectConst.ZERO);
							if (null != obj) {
								codeState.append("&")
										.append(SubjectStringUtil.clearVirTabName(tabCol.code_field).toLowerCase())
										.append("=").append(obj);

							}
							// 看看各层
							String level = tabCol.level;
							urllevel = level;
							List levels = tabCol.levels;
							if (null != levels && null != level) {
								boolean found = false;
								Iterator levIter = levels.iterator();
								while (levIter.hasNext() && !found) {
									SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();
									if (level.equals(levObj.lev_id)) {
										found = true;
										codeState
												.append("&")
												.append(SubjectStringUtil.clearVirTabName(levObj.src_idfld)
														.toLowerCase()).append("=").append(svces[aryIndex]);
										aryIndex++;
										descState
												.append("&")
												.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld)
														.toLowerCase()).append("=").append(svces[aryIndex]);
										codeState
												.append("&")
												.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld)
														.toLowerCase()).append("=").append(svces[aryIndex]);
										if (SubjectConst.YES.equalsIgnoreCase(levObj.desc_astitle))
											chartState.append(svces[aryIndex - 1]).append(" ");
										else
											chartState.append(svces[aryIndex]).append(" ");
										aryIndex++;
									}
									obj = values.get(levObj.lev_id);
									if (null != obj) {
										codeState
												.append("&")
												.append(SubjectStringUtil.clearVirTabName(levObj.src_idfld)
														.toLowerCase()).append("=").append(obj);
									}
								}
							}
						} else {
							String level = tabCol.level;
							urllevel = level;
							List levels = tabCol.levels;
							if (SubjectConst.ZERO.equals(level)) {
								codeState.append("&")
										.append(SubjectStringUtil.clearVirTabName(tabCol.code_field).toLowerCase())
										.append("=");
								codeState.append(svces[aryIndex]);
								aryIndex++;
								String descValue = svces[aryIndex];
								try {
									descValue = URLEncoder.encode(URLEncoder.encode(descValue, "UTF-8"), "UTF-8");
								} catch (UnsupportedEncodingException e) {
								}
								descState.append("&")
										.append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase())
										.append("=");
								descState.append(descValue);
								codeState.append("&")
										.append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase())
										.append("=");
								codeState.append(descValue);
								if (SubjectConst.YES.equalsIgnoreCase(tabCol.desc_astitle))
									chartState.append(svces[aryIndex - 1]).append(" ");
								else
									chartState.append(descValue).append(" ");
								aryIndex++;
							} else {
								Iterator levIter = levels.iterator();
								while (levIter.hasNext()) {
									SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levIter.next();
									if (level.equals(levObj.lev_id)) {
										codeState
												.append("&")
												.append(SubjectStringUtil.clearVirTabName(levObj.src_idfld)
														.toLowerCase()).append("=").append(svces[aryIndex]);
										aryIndex++;
										descState
												.append("&")
												.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld)
														.toLowerCase()).append("=").append(svces[aryIndex]);
										codeState
												.append("&")
												.append(SubjectStringUtil.clearVirTabName(levObj.src_namefld)
														.toLowerCase()).append("=").append(svces[aryIndex]);
										if (SubjectConst.YES.equalsIgnoreCase(levObj.desc_astitle))
											chartState.append(svces[aryIndex - 1]).append(" ");
										else
											chartState.append(svces[aryIndex]).append(" ");
										aryIndex++;
										break;
									}
								}
							}
						}

					}
					codeState.append("&level=").append(urllevel);
				} else {
					codeState.append("&").append(SubjectStringUtil.clearVirTabName(tabCol.code_field).toLowerCase())
							.append("=");
					codeState.append(svces[aryIndex]);
					aryIndex++;
					String descValue = svces[aryIndex];
					try {
						descValue = URLEncoder.encode(URLEncoder.encode(descValue, "UTF-8"), "UTF-8");
					} catch (UnsupportedEncodingException e) {
					}
					descState.append("&").append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase())
							.append("=");
					descState.append(descValue);
					codeState.append("&").append(SubjectStringUtil.clearVirTabName(tabCol.desc_field).toLowerCase())
							.append("=");
					codeState.append(descValue);
					if (SubjectConst.YES.equalsIgnoreCase(tabCol.desc_astitle))
						chartState.append(svces[aryIndex - 1]).append(" ");
					else
						chartState.append(descValue).append(" ");
					aryIndex++;
				}
			}
		}
		mpState.put("code_state", codeState.toString());
		mpState.put("desc_state", descState.toString());
		mpState.put("chart_state", chartState.toString());
		return mpState;
	}

	/**
	 * 生成全部展开链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @return 全部展开链接
	 */
	public static StringBuffer genExpandAllLink(SubjectCommTabDef subTable, TableCurFunc curFunc,
			SubjectCommTabCol tabCol) {
		StringBuffer link = new StringBuffer();
		if (SubjectConst.NO.equalsIgnoreCase(subTable.has_expandall))
			return link;
		link.append(SubjectConst.TABLE_ACTION_DOT_DO).append("?");
		link.append(SubjectConst.REQ_TABLE_ID).append("=").append(subTable.table_id);
		// 加上当前功能,是某行扩展
		link.append("&").append(SubjectConst.REQ_TABLE_FUNC).append("=").append(SubjectConst.TABLE_FUNC_EXPAND_ALL);
		@SuppressWarnings("unused")
		int aryIndex = 0;
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol colObj = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(colObj.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES.equalsIgnoreCase(colObj.is_expand_col)) {
					String nextLevel = getExpandNextLevel(colObj);
					link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
							.append(nextLevel);
				} else {
					link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
							.append(colObj.level);
				}
				aryIndex++;
				aryIndex++;
			} else if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
					&& SubjectConst.YES.equalsIgnoreCase(colObj.is_measure)) {
				link.append("&").append(SubjectConst.REQ_MSU_LEVEL_PREFIX).append(colObj.index).append("=")
						.append(colObj.level);
			}
		}
		// 加上本层次的值
		StringBuffer sb = new StringBuffer();
		link.append("&").append(SubjectConst.REQ_AJAX_REQUEST).append("=Y");
		String tmpStr = link.toString();
		tmpStr = SubjectConst.AJAX_REQUEST_JS_FUNCTION.replaceAll("::LINK::", tmpStr);
		sb.append("<a href=\"javascript:").append(tmpStr);
		sb.append("\">").append(SubjectStringUtil.genHtmlImg(curFunc.imagePath, curFunc.drillExpandGif, 9))
				.append("</a>&nbsp;");

		return sb;
	}

	/**
	 * 生成全部收缩链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @return 收缩链接字符串
	 */
	public static StringBuffer genCollapseAllLink(SubjectCommTabDef subTable, TableCurFunc curFunc,
			SubjectCommTabCol tabCol) {
		StringBuffer link = new StringBuffer();
		link.append(SubjectConst.TABLE_ACTION_DOT_DO).append("?");
		link.append(SubjectConst.REQ_TABLE_ID).append("=").append(subTable.table_id);
		// 加上当前功能,是某行扩展
		link.append("&").append(SubjectConst.REQ_TABLE_FUNC).append("=").append(SubjectConst.TABLE_FUNC_INIT);
		@SuppressWarnings("unused")
		int aryIndex = 0;
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol colObj = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(colObj.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES.equalsIgnoreCase(colObj.is_expand_col)) {
					String nextLevel = getExpandNextLevel(colObj);
					link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
							.append(nextLevel);
				} else {
					link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
							.append(colObj.level);

				}
				aryIndex++;
				aryIndex++;
			} else if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
					&& SubjectConst.YES.equalsIgnoreCase(colObj.is_measure)) {
				link.append("&").append(SubjectConst.REQ_MSU_LEVEL_PREFIX).append(colObj.index).append("=")
						.append(colObj.level);
			}
		}
		// 加上本层次的值
		StringBuffer sb = new StringBuffer();
		link.append("&").append(SubjectConst.REQ_AJAX_REQUEST).append("=Y");
		String tmpStr = link.toString();
		tmpStr = SubjectConst.AJAX_REQUEST_JS_FUNCTION.replaceAll("::LINK::", tmpStr);
		sb.append("<a href=\"javascript:").append(tmpStr);
		sb.append("\">").append(SubjectStringUtil.genHtmlImg(curFunc.imagePath, curFunc.drillCollapseGif, 9))
				.append("</a>&nbsp;");

		return sb;
	}

	/**
	 * 生成行收缩链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param svces
	 *            一维数组
	 * @return 收缩链接字符串
	 */
	public static StringBuffer genCollapseLink(SubjectCommTabDef subTable, TableCurFunc curFunc,
			SubjectCommTabCol tabCol, String[] svces) {
		// 要生成全状态链接即各维度值，各层次值
		// 最后类似dim_value_index_level=值,dim_level_index
		StringBuffer link = new StringBuffer();
		// 看看是否还需要生成折叠展开链接
		// 参数变量，先将要展开的参数加上
		link.append(SubjectConst.TABLE_ACTION_DOT_DO).append("?");
		link.append(SubjectConst.REQ_TABLE_ID).append("=").append(subTable.table_id);
		// 加上当前功能,是某行扩展
		link.append("&").append(SubjectConst.REQ_TABLE_FUNC).append("=").append(SubjectConst.TABLE_FUNC_ROW_COLLAPSE);
		// 加上当前的折叠行号
		link.append("&").append(SubjectConst.REQ_DIM_COLLAPSE_ROWID).append("=")
				.append("::" + SubjectConst.REQ_DIM_COLLAPSE_ROWID + "::");

		// 还需要加上当前的所有维度的值和层次水平
		int aryIndex = 0;
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol colObj = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(colObj.is_measure)) {
				if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
						&& SubjectConst.YES.equals(colObj.is_expand_col)) {
					link.append("&").append(SubjectConst.REQ_DIM_EXPAND_ROWID).append("=::")
							.append(SubjectConst.REQ_DIM_EXPAND_ROWID).append("::");
					String nextLevel = getExpandNextLevel(colObj);
					link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
							.append(nextLevel);
				} else {
					link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
							.append(colObj.level);

				}
				link.append("&").append(SubjectConst.REQ_DIM_VALUE_PREFIX).append(colObj.index).append("_")
						.append(colObj.level).append("=").append(svces[aryIndex]);
				aryIndex++;
				aryIndex++;
			} else if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
					&& SubjectConst.YES.equalsIgnoreCase(colObj.is_measure)) {
				link.append("&").append(SubjectConst.REQ_MSU_LEVEL_PREFIX).append(colObj.index).append("=")
						.append(colObj.level);
			}
		}
		// 加上以前的值
		link.append(genPreExpandDimLink(subTable));
		// 加上本层次的值
		StringBuffer sb = new StringBuffer();
		link.append("&").append(SubjectConst.REQ_AJAX_REQUEST).append("=Y");
		String tmpStr = link.toString();
		tmpStr = SubjectConst.AJAX_REQUEST_JS_FUNCTION.replaceAll("::LINK::", tmpStr);
		sb.append("<a href=\"javascript:").append(tmpStr);
		sb.append("\">").append(SubjectStringUtil.genHtmlImg(curFunc.imagePath, curFunc.drillCollapseGif, 9))
				.append("</a>&nbsp;");

		return sb;
	}

	/**
	 * 生成行扩展链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param curFunc
	 *            功能对象
	 * @param tabCol
	 *            表格列对象
	 * @param svces
	 *            一维数组
	 * @return 扩展链接字符串
	 */
	public static StringBuffer genExpandLink(SubjectCommTabDef subTable, TableCurFunc curFunc,
			SubjectCommTabCol tabCol, String[] svces) {
		// 要生成全状态链接即各维度值，各层次值
		// 最后类似dim_value_index_level=值,dim_level_index
		StringBuffer link = new StringBuffer();
		// 看看是否还需要生成折叠展开链接
		boolean hasExpandLink = false;
		hasExpandLink = hasExpandNextLevel(tabCol);
		if (hasExpandLink) {
			// 参数变量，先将要展开的参数加上
			link.append(SubjectConst.TABLE_ACTION_DOT_DO).append("?");
			link.append(SubjectConst.REQ_TABLE_ID).append("=").append(subTable.table_id);
			// 加上当前功能,是某行扩展
			link.append("&").append(SubjectConst.REQ_TABLE_FUNC).append("=").append(SubjectConst.TABLE_FUNC_ROW_EXPAND);

			// 还需要加上当前的所有维度的值和层次水平
			int aryIndex = 0;
			List tabCols = subTable.tableCols;
			Iterator iter = tabCols.iterator();
			while (iter.hasNext()) {
				SubjectCommTabCol colObj = (SubjectCommTabCol) iter.next();
				if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
						&& SubjectConst.NO.equalsIgnoreCase(colObj.is_measure)) {
					if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
							&& SubjectConst.YES.equals(colObj.is_expand_col)) {
						link.append("&").append(SubjectConst.REQ_DIM_EXPAND_ROWID).append("=::")
								.append(SubjectConst.REQ_DIM_EXPAND_ROWID).append("::");
						String nextLevel = getExpandNextLevel(colObj);
						link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
								.append(nextLevel);
					} else {
						link.append("&").append(SubjectConst.REQ_DIM_LEVEL_PREFIX + colObj.index).append("=")
								.append(colObj.level);

					}
					link.append("&").append(SubjectConst.REQ_DIM_VALUE_PREFIX).append(colObj.index).append("_")
							.append(colObj.level).append("=").append(svces[aryIndex]);
					aryIndex++;
					aryIndex++;
				} else if (SubjectConst.YES.equalsIgnoreCase(colObj.default_display)
						&& SubjectConst.YES.equalsIgnoreCase(colObj.is_measure)) {
					link.append("&").append(SubjectConst.REQ_MSU_LEVEL_PREFIX).append(colObj.index).append("=")
							.append(colObj.level);
				}
			}
			// 加上以前的值
			link.append(genPreExpandDimLink(subTable));
			// 加上本层次的值
			StringBuffer sb = new StringBuffer();
			link.append("&").append(SubjectConst.REQ_AJAX_REQUEST).append("=Y");
			String tmpStr = link.toString();
			tmpStr = SubjectConst.AJAX_REQUEST_JS_FUNCTION.replaceAll("::LINK::", tmpStr);
			sb.append("<a href=\"javascript:").append(tmpStr);
			sb.append("\">").append(SubjectStringUtil.genHtmlImg(curFunc.imagePath, curFunc.drillExpandGif, 9))
					.append("</a>&nbsp;");

			return sb;
		} else {
			link.append(SubjectConst.INDENT_SPACE);
			return link;
		}
	}

	/**
	 * 获取扩展列的下一层次
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @return 下一层次字符串
	 */
	public static String getExpandNextLevel(SubjectCommTabCol tabCol) {
		String nextLevel = null;
		if (null != tabCol) {
			String curLevel = tabCol.level;
			if (SubjectConst.ZERO.equals(curLevel)) {
				List levels = tabCol.levels;
				if (null != levels) {
					nextLevel = ((SubjectCommDimHierarchy) levels.get(0)).lev_id;
				}
			} else {
				List levels = tabCol.levels;
				if (null != levels) {
					boolean found = false;
					SubjectCommDimHierarchy lasLvl = null;
					Iterator iter = levels.iterator();
					while (iter.hasNext()) {
						SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) iter.next();
						if (found) {
							nextLevel = levObj.lev_id;
							break;
						}
						if (curLevel.equals(levObj.lev_id)) {
							found = true;
						}
						lasLvl = levObj;
					}
					// 如果找到最后了，还没有，怎么办，设置成最后一个
					if (null == nextLevel)
						nextLevel = lasLvl.lev_id;
				}
			}
		}
		return nextLevel;
	}

	private static boolean hasExpandNextLevel(SubjectCommTabCol tabCol) {
		boolean hasNextLevel = false;
		if (null != tabCol) {
			String curLevel = tabCol.level;
			if (SubjectConst.ZERO.equals(curLevel) && null == tabCol.levels) {
				return hasNextLevel;
			} else if (SubjectConst.ZERO.equals(curLevel)) {
				hasNextLevel = true;
				return hasNextLevel;
			} else {
				List levels = tabCol.levels;
				if (null != levels) {
					boolean found = false;
					Iterator iter = levels.iterator();
					while (iter.hasNext()) {
						SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) iter.next();
						if (found) {
							hasNextLevel = true;
							break;
						}
						if (curLevel.equals(levObj.lev_id)) {
							found = true;
						}
					}
				}
			}
		}
		return hasNextLevel;
	}

	/**
	 * 获取扩展列的前一层次
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @return 前一层次字符串
	 */
	public static String getExpandPreLevel(SubjectCommTabCol tabCol) {
		String preLevel = null;
		if (null != tabCol) {
			String curLevel = tabCol.level;
			if (!SubjectConst.ZERO.equals(curLevel)) {
				List levels = tabCol.levels;
				if (null != levels) {
					SubjectCommDimHierarchy levObj = (SubjectCommDimHierarchy) levels.get(0);
					if (curLevel.equals(levObj.lev_id)) {
						preLevel = SubjectConst.ZERO;
					} else {
						levObj = null;
						Iterator iter = levels.iterator();
						while (iter.hasNext()) {
							SubjectCommDimHierarchy lev = (SubjectCommDimHierarchy) iter.next();
							if (lev.lev_id.equals(curLevel)) {
								preLevel = levObj.lev_id;
								break;
							}
							levObj = lev;
						}
					}
				}
			}
		}
		return preLevel;
	}

	/**
	 * 生成扩展列扩展维度的链接，用于图形联动
	 * 
	 * @param subTable
	 *            表格对象
	 * @return 生成扩展前维度链接
	 */
	private static StringBuffer genPreExpandDimLink(SubjectCommTabDef subTable) {
		StringBuffer dimLink = new StringBuffer();
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				dimLink.append(genPreExpandDimLink(subTable, tabCol));
			}
		}
		return dimLink;
	}

	/**
	 * 生成扩展前维度链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @return 扩展前维度链接字符串
	 */
	private static StringBuffer genPreExpandDimLink(SubjectCommTabDef subTable, SubjectCommTabCol tabCol) {
		StringBuffer dimLink = new StringBuffer();
		Map values = tabCol.values;
		if (null != values) {
			if (null != values.get(SubjectConst.ZERO)) {
				// 第一级有值
				dimLink.append("&").append(SubjectConst.REQ_DIM_VALUE_PREFIX).append(tabCol.index).append("_")
						.append(SubjectConst.ZERO).append("=");
				dimLink.append((String) values.get(SubjectConst.ZERO));
			}
			if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
					&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
				List levels = tabCol.levels;
				if (null != levels) {
					Iterator iter = levels.iterator();
					while (iter.hasNext()) {
						SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) iter.next();
						if (null != values.get(colLev.lev_id)) {
							dimLink.append("&").append(SubjectConst.REQ_DIM_VALUE_PREFIX).append(tabCol.index)
									.append("_").append(colLev.lev_id).append("=");
							dimLink.append((String) values.get(colLev.lev_id));
						}
					}
				}
			}
		}
		return dimLink;
	}

	/**
	 * 生成扩展列维度链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @return 维度链接字符串
	 */
	public static StringBuffer genPreExpandDimUrl(SubjectCommTabDef subTable) {
		StringBuffer dimUrl = new StringBuffer();
		List tabCols = subTable.tableCols;
		Iterator iter = tabCols.iterator();
		while (iter.hasNext()) {
			SubjectCommTabCol tabCol = (SubjectCommTabCol) iter.next();
			if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_display)
					&& SubjectConst.NO.equalsIgnoreCase(tabCol.is_measure)) {
				dimUrl.append(genPreExpandDimUrl(subTable, tabCol));
			}
		}
		return dimUrl;
	}

	/**
	 * 生成扩展列维度链接
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @return 维度链接字符串
	 */
	public static StringBuffer genPreExpandDimUrl(SubjectCommTabDef subTable, SubjectCommTabCol tabCol) {
		StringBuffer dimUrl = new StringBuffer();
		Map values = tabCol.values;
		if (SubjectConst.YES.equalsIgnoreCase(subTable.has_expand)
				&& SubjectConst.YES.equalsIgnoreCase(tabCol.is_expand_col)) {
			if (null != values) {
				if (null != values.get(SubjectConst.ZERO)) {
					String code_fld = tabCol.code_field;
					code_fld = SubjectStringUtil.clearVirTabName(code_fld);
					dimUrl.append("&").append(code_fld).append("=");
					String value = (String) values.get(SubjectConst.ZERO);
					if (SubjectConst.DATA_TYPE_STRING.equals(tabCol.data_type)) {
						value = "'" + value + "'";
					}
					dimUrl.append(value);
				}
				List levels = tabCol.levels;
				if (null != levels) {
					Iterator iter = levels.iterator();
					while (iter.hasNext()) {
						SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) iter.next();
						if (null != values.get(colLev.lev_id)) {
							String code_fld = colLev.src_idfld;
							code_fld = SubjectStringUtil.clearVirTabName(code_fld);
							dimUrl.append("&").append(code_fld).append("=");
							String value = (String) values.get(colLev.lev_id);
							if (SubjectConst.DATA_TYPE_STRING.equals(colLev.idfld_type)) {
								value = "'" + value + "'";
							}
							dimUrl.append(value);
						}
					}
				}
			}
		} else {
			//
			if (null != values && null != values.get(SubjectConst.ZERO)) {
				String code_fld = tabCol.code_field;
				code_fld = SubjectStringUtil.clearVirTabName(code_fld);
				dimUrl.append("&").append(code_fld).append("=");
				String value = (String) values.get(SubjectConst.ZERO);
				if (SubjectConst.DATA_TYPE_STRING.equals(tabCol.data_type)) {
					value = "'" + value + "'";
				}
				dimUrl.append(value);
			}
		}
		return dimUrl;
	}

	/**
	 * 生成维度链接字符串
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param svces
	 *            一维数组
	 * @param aryIndex
	 *            数组下标
	 * @return 维度链接字符串
	 */
	public static StringBuffer genExpandDimUrl(SubjectCommTabCol tabCol, String[] svces, int aryIndex) {
		StringBuffer dimUrl = new StringBuffer();
		String level = tabCol.level;
		if (SubjectConst.ZERO.equals(level)) {
			String code_fld = tabCol.code_field;
			code_fld = SubjectStringUtil.clearVirTabName(code_fld);
			dimUrl.append("&").append(code_fld).append("=");
			String value = svces[aryIndex];
			if (SubjectConst.DATA_TYPE_STRING.equals(tabCol.data_type)) {
				value = "'" + value + "'";
			}
			dimUrl.append(value);
		} else {
			List levels = tabCol.levels;
			if (null != levels) {
				Iterator iter = levels.iterator();
				while (iter.hasNext()) {
					SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) iter.next();
					if (level.equals(colLev.lev_id)) {
						String code_fld = colLev.src_idfld;
						code_fld = SubjectStringUtil.clearVirTabName(code_fld);
						dimUrl.append("&").append(code_fld).append("=");
						String value = svces[aryIndex];
						if (SubjectConst.DATA_TYPE_STRING.equals(colLev.idfld_type)) {
							value = "'" + value + "'";
						}
						dimUrl.append(value);
					}
				}
			}
		}
		return dimUrl;
	}

	/**
	 * 获取维度的值列表
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param virTableName
	 *            维表名
	 * @return 维度结构列表
	 */
	public static List getDimValues(SubjectCommTabDef subTable, SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String virTableName) {
		List values = new ArrayList();
		String selectSQL = genGetDimValueSQL(subTable, tabCol, curFunc, virTableName);
		try {
			String[][] svces = WebDBUtil.execQryArray(selectSQL, "");
			if (null != svces) {
				// 组装成维度对象
				values = assembleDimStructs(tabCol, svces);
			}
		} catch (AppException ae) {
		}
		return values;
	}

	/**
	 * 组装维度结构列表
	 * 
	 * @param tabCol
	 *            表格对象
	 * @param svces
	 *            一维数组
	 * @return 维度结构列表
	 */
	private static List assembleDimStructs(SubjectCommTabCol tabCol, String[][] svces) {
		List dimStructs = new ArrayList();
		for (int i = 0; i < svces.length; i++) {
			SubjectDimStruct dimStruct = new SubjectDimStruct();
			dimStruct.code_fld = tabCol.code_field;
			dimStruct.data_type = tabCol.data_type;
			dimStruct.dim_code = svces[i][0];
			dimStruct.dim_desc = svces[i][1];
			dimStructs.add(dimStruct);
		}
		return dimStructs;
	}

	/**
	 * 生成获取维度值的查询语句
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param virTableName
	 *            维表名称
	 * @return 查询字符串
	 */
	private static String genGetDimValueSQL(SubjectCommTabDef subTable, SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String virTableName) {
		StringBuffer selectSQL = new StringBuffer();
		// 组装维度取值的条件
		String code_fld = tabCol.code_field;
		String desc_fld = tabCol.desc_field;
		code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
		desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);
		selectSQL.append(" SELECT ");
		selectSQL.append(code_fld).append(",");
		selectSQL.append(desc_fld);
		String data_where = SubjectDataTableUtil.genDataTableWHERE(subTable, virTableName);
		selectSQL.append(SubjectDataTableUtil.genDataTableFROM(subTable, virTableName, data_where));
		selectSQL.append(" WHERE ");
		// 数据表的定义的查询条件
		selectSQL.append(data_where);
		// 由其他程序传进来的条件
		selectSQL.append(SubjectStringUtil.processDataTableWHERE(curFunc, virTableName));
		selectSQL.append(" GROUP BY  ");
		selectSQL.append(code_fld).append(",");
		selectSQL.append(desc_fld);
		selectSQL.append(" ORDER BY  ");
		selectSQL.append(code_fld);
		return selectSQL.toString();
	}

	/**
	 * 生成非扩展列维度的查询结构
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param virTableName
	 *            伪表名
	 * @return 查询结构
	 */
	public static TableSelectQuery genDimNoExpandSelectParts(SubjectCommTabDef subTable, SubjectCommTabCol tabCol,
			TableCurFunc curFunc, String virTableName) {
		TableSelectQuery parts = new TableSelectQuery();

		// 由于是最基本的形式，
		// 且这里暂不关注分页，分页还是采取架构的一次取出
		String id_fld = tabCol.code_field;
		String desc_fld = tabCol.desc_field;
		// System.out.println("id_fld===========>"+id_fld+"=======desc_fld===========>"+desc_fld);
		// 替换伪表名
		id_fld = SubjectStringUtil.replaceVirTabName(id_fld, virTableName);
		desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);
		// System.out.println("id_fld===========>"+id_fld+"=======desc_fld===========>"+desc_fld);
		//
		parts.select.append(id_fld).append(" AS F").append(tabCol.col_sequence).append(",");
		parts.select.append(desc_fld).append(" AS D").append(tabCol.col_sequence).append(",");
		parts.groupby.append(id_fld).append(",");
		parts.groupby.append(desc_fld).append(",");
		parts.orderby.append(id_fld).append(",");
		// 加上查询条件，有没有，这里好像没有
		if (null != tabCol.values && SubjectConst.YES.equalsIgnoreCase(tabCol.dim_aswhere)) {
			// 有值，且要求值作为条件
			parts.where.append(genColWhere(tabCol, virTableName));
		}
		// if (SubjectConst.YES.equalsIgnoreCase(tabCol.default_sort)) {
		// parts.orderby.append("F").append(tabCol.col_sequence).append(" ").append(tabCol.sort_order).append(",");
		// parts.orderby.append("D").append(tabCol.col_sequence).append(" ").append(tabCol.sort_order).append(",");
		// }
		return parts;
	}

	/**
	 * 生成扩展列查询部分结构体
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param virTableName
	 *            伪表名
	 * @return 查询结构体
	 */
	public static TableSelectQuery genDimExpandSelectParts(SubjectCommTabDef subTable, SubjectCommTabCol tabCol,
			TableCurFunc curFunc, String virTableName) {
		TableSelectQuery parts = new TableSelectQuery();

		// 由于是折叠展开模式，因此不管分页，
		String code_fld = tabCol.code_field;
		String desc_fld = tabCol.desc_field;
		// 替换伪表名
		code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
		desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);

		// 下面判断
		// 首先看看是否为折叠/展开列
		if (SubjectConst.NO.equalsIgnoreCase(tabCol.is_expand_col)) {
			// 非展开列
			parts = genDimNoExpandSelectParts(subTable, tabCol, curFunc, virTableName);
			return parts;
		} else {
			// 这里可能是过虑或者是全部展开
			if (SubjectConst.TABLE_FUNC_EXPAND_ALL.equals(curFunc.curUserFunc)) {
				// 全部展开
				parts = genExpandAllSelectParts(tabCol, curFunc, virTableName);
				return parts;
			} else if (null != curFunc.filterIndexs && null != curFunc.filterLevel && null != curFunc.filterValues) {
				// 矩阵过虑
				int maxLevel = 0;
				try {
					maxLevel = Integer.parseInt(curFunc.filterLevel);
				} catch (NumberFormatException nfe) {
				}
				parts = genFilterSelectParts(tabCol, curFunc, virTableName, maxLevel);
				return parts;

			} else if (!curFunc.banExpanded) {
				// 普通展开列情况
				// 这里还要判断一下
				parts = genExpandSelectParts(subTable, tabCol, curFunc, virTableName);
				return parts;
			} else {
				// 普通列情况
				parts = genDimNoExpandSelectParts(subTable, tabCol, curFunc, virTableName);
				return parts;
			}

		}
	}

	/**
	 * 生成扩展查询结构
	 * 
	 * @param subTable
	 *            表格对象
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param virTableName
	 *            伪表名
	 * @return 查询结构
	 */
	private static TableSelectQuery genExpandSelectParts(SubjectCommTabDef subTable, SubjectCommTabCol tabCol,
			TableCurFunc curFunc, String virTableName) {
		TableSelectQuery parts = new TableSelectQuery();
		String level = tabCol.level;
		if (curFunc.throwOldExpandContent) {
			level = curFunc.expandLevel;
		}
		if (SubjectConst.NO.equalsIgnoreCase(subTable.has_expand) || (null != level && SubjectConst.ZERO.equals(level))) {
			parts = genDimNoExpandSelectParts(subTable, tabCol, curFunc, virTableName);
		} else if (!SubjectConst.ZERO.equals(level)) {
			List levels = tabCol.levels;
			if (null != levels && 0 < levels.size()) {
				Iterator iter = levels.iterator();
				while (iter.hasNext()) {
					SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) iter.next();
					if (colLev.lev_id.equals(level)) {
						// 开始
						String code_fld = colLev.src_idfld;
						String desc_fld = colLev.src_namefld;
						code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
						desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);
						parts.select.append(code_fld).append(",");
						parts.select.append(desc_fld).append(",");
						parts.groupby.append(code_fld).append(",");
						parts.groupby.append(desc_fld).append(",");
						parts.orderby.append(code_fld).append(",");
					}
				}
				parts.where.append(genColWhere(tabCol, virTableName));
			}

		}
		return parts;
	}

	/**
	 * 生成过滤查询结构
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param virTableName
	 *            伪表名
	 * @param maxLevel
	 *            最大过滤层次
	 * @return 查询结构
	 */
	private static TableSelectQuery genFilterSelectParts(SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String virTableName, int maxLevel) {
		TableSelectQuery parts = new TableSelectQuery();

		// 由于是折叠展开模式，因此不管分页，
		String code_fld = tabCol.code_field;
		String desc_fld = tabCol.desc_field;
		// 替换伪表名
		code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
		desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);

		// 先到第0级
		parts.select.append(code_fld).append(",");
		parts.select.append(desc_fld).append(",");
		parts.groupby.append(code_fld).append(",");
		parts.groupby.append(desc_fld).append(",");
		parts.orderby.append(code_fld).append(",");

		int count = 1;
		List levels = tabCol.levels;
		if (null != levels && 0 < levels.size()) {
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) iter.next();
				code_fld = colLev.src_idfld;
				desc_fld = colLev.src_namefld;
				code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
				desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);
				parts.select.append(code_fld).append(",");
				parts.select.append(desc_fld).append(",");
				parts.groupby.append(code_fld).append(",");
				parts.groupby.append(desc_fld).append(",");
				parts.orderby.append(code_fld).append(",");
				count++;
				if (count > maxLevel)
					break;
			}
		}
		return parts;
	}

	/**
	 * 生成全部扩展的查询结构
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param curFunc
	 *            功能对象
	 * @param virTableName
	 *            伪表名
	 * @return 全部扩展查询结构
	 */
	private static TableSelectQuery genExpandAllSelectParts(SubjectCommTabCol tabCol, TableCurFunc curFunc,
			String virTableName) {
		TableSelectQuery parts = new TableSelectQuery();
		// 由于是折叠展开模式，因此不管分页，
		String code_fld = tabCol.code_field;
		String desc_fld = tabCol.desc_field;
		// 替换伪表名
		code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
		desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);

		// 先到第0级
		parts.select.append(code_fld).append(",");
		parts.select.append(desc_fld).append(",");
		parts.groupby.append(code_fld).append(",");
		parts.groupby.append(desc_fld).append(",");
		parts.orderby.append(code_fld).append(",");
		// 下面加上所有的层次
		List levels = tabCol.levels;
		if (null != levels && 0 < levels.size()) {
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) iter.next();
				code_fld = colLev.src_idfld;
				desc_fld = colLev.src_namefld;
				code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
				desc_fld = SubjectStringUtil.replaceVirTabName(desc_fld, virTableName);
				parts.select.append(code_fld).append(",");
				parts.select.append(desc_fld).append(",");
				parts.groupby.append(code_fld).append(",");
				parts.groupby.append(desc_fld).append(",");
				parts.orderby.append(code_fld).append(",");
			}
		}
		return parts;
	}

	/**
	 * 生成表格列的查询WHERE部分
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param virTableName
	 *            伪表名
	 * @return WHERE查询字符串
	 */
	public static String genColWhere(SubjectCommTabCol tabCol, String virTableName) {
		StringBuffer where = new StringBuffer();
		// 先判断一下是否有层次
		List levels = tabCol.levels;
		Map values = tabCol.values;
		if (null == levels || 0 >= levels.size()) {
			// 没有层次,取出字段
			where.append(genColZeroLevelWhere(tabCol, virTableName));
		} else {
			// 有层次
			where.append(genColZeroLevelWhere(tabCol, virTableName));
			// 这里不考虑具体到列定义钻取到个层次了
			Iterator iter = levels.iterator();
			while (iter.hasNext()) {
				SubjectCommDimHierarchy colLev = (SubjectCommDimHierarchy) iter.next();
				String code_fld = colLev.src_idfld;
				code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
				if (null != values && null != values.get(colLev.lev_id)) {
					String value = (String) values.get(colLev.lev_id);
					where.append(" AND ").append(code_fld).append("=");
					if (SubjectConst.DATA_TYPE_STRING.equals(colLev.idfld_type))
						value = "'" + value + "'";
					where.append(value);
				}
			}
		}
		return where.toString();
	}

	/**
	 * 生成表格列最初水平查询WHERE
	 * 
	 * @param tabCol
	 *            表格列对象
	 * @param virTableName
	 *            伪表名
	 * @return WHERE查询
	 */
	private static String genColZeroLevelWhere(SubjectCommTabCol tabCol, String virTableName) {
		StringBuffer where = new StringBuffer();
		// 先判断一下是否有层次
		Map values = tabCol.values;
		// 没有层次,取出字段
		String code_fld = tabCol.code_field;
		code_fld = SubjectStringUtil.replaceVirTabName(code_fld, virTableName);
		// 看看是否有这层的值
		if (null != values && null != values.get(SubjectConst.ZERO)) {
			String value = (String) values.get(SubjectConst.ZERO);
			where.append(" AND ").append(code_fld).append("=");
			if (tabCol.data_type.equalsIgnoreCase(SubjectConst.DATA_TYPE_NUMBER))
				where.append(value);
			if (tabCol.data_type.equalsIgnoreCase(SubjectConst.DATA_TYPE_STRING))
				where.append("'").append(value).append("'");
		}
		return where.toString();
	}
}
