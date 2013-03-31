package com.ailk.bi.common.event;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.XMLUtils;
import com.ailk.bi.common.event.utils.Util;
import com.ailk.bi.common.sysconfig.GetSystemConfig;

/**
 * <p>
 * Title: asiabi J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description: 组合多个表的JavaBean
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: BeiJing asiabi Info System Co,Ltd.
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 */

public class JBCombTablesBase implements JBTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3619723597317811442L;

	private String ejbActionClassName = null;

	protected int extern_oper_flag = TableKeys.REC_NEW;

	/**
	 * 实现实体的复制
	 * 
	 * @return
	 */
	public Object clone() {
		JBCombTablesBase o = null;
		try {
			o = (JBCombTablesBase) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	public JBCombTablesBase() {
	}

	public void setEJBActionClassName(String ejbActionClassName) {
		this.ejbActionClassName = ejbActionClassName;
	}

	public String getEJBActionClassName() {
		return ejbActionClassName;
	}

	public String getTableName() {
		return getClass().getName();
	}

	/**
	 * 参见 JBTable#toXml()
	 * 
	 * @param simp
	 * @return
	 * @throws AppException
	 */
	public String toXml(boolean simp) throws AppException {
		String ret = null;

		Document doc = new DocumentImpl();
		Element root = Util.genJavaBeans(doc, this, this.getTableName(), simp);
		doc.appendChild(root);
		ret = XMLUtils.doc2String(doc, Util.DEFAULT_ENCODING);
		return ret;
	}

	/**
	 * 获得类对应地tagID
	 * 
	 * @return
	 */
	public int getModuleID() {
		int iRet = GetSystemConfig.getTagFromClass(getClass().getName());
		if (iRet == -1)
			System.err.println("NO FATAL ERROR: 类" + getClass().getName()
					+ "的tag映射值没有定义!");

		return iRet;
	}

	/**
	 * 参见 JBTable#toXml()
	 * 
	 * @param simp
	 * @return
	 * @throws AppException
	 */
	public String toXml() throws AppException {
		return toXml(true);
	}

	/**
	 * 获得该JB在session存放的session属性名称
	 * 
	 * @return
	 */
	public String getSessoinName() {
		return "";
	}

	/**
	 * 获取记录的修改标记
	 * 
	 * @return
	 */
	public int getRecFlag() {
		return extern_oper_flag;
	}

	/**
	 * 设置记录的修改标记
	 * 
	 * @param extern_oper_flag
	 *            取值应从TableKeys类中取值
	 */
	public void setRecFlag(int extern_oper_flag) {
		this.extern_oper_flag = extern_oper_flag;
	}

}